package com.autoparam;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.bytecode.Wrapper;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.proxy.AbstractProxyInvoker;
import com.alibaba.dubbo.rpc.proxy.javassist.JavassistProxyFactory;
import com.alibaba.dubbo.rpc.proxy.jdk.JdkProxyFactory;
import com.autoparam.service.RequestSingle2ListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Adaptive
public class ProviderProxyExt extends JavassistProxyFactory {

    private static final Logger logger = LoggerFactory.getLogger(ParamFilter.class);

    private final JdkProxyFactory jdkProxyFactory = new JdkProxyFactory();

    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) throws RpcException {
        try {
            // TODO Wrapper cannot handle this scenario correctly: the classname contains '$'
            final Wrapper wrapper =
                    Wrapper.getWrapper(proxy.getClass().getName().indexOf('$') < 0 ? proxy.getClass() : type);
            return new AbstractProxyInvoker<T>(proxy, type, url) {
                @Override
                protected Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments)
                        throws Throwable {
                    Object o = wrapper.invokeMethod(proxy, methodName, parameterTypes, arguments);
                    RequestSingle2ListService requestSingle2ListService = ApplicationContextProvider.getApplicationContext().getBean(RequestSingle2ListService.class);
                    List<Object[]> request  = requestSingle2ListService.handle(arguments);
                    if(CollectionUtils.isEmpty(request)){
                        return o;
                    }else {
                        logger.info("request single to list, methodName:{}" + methodName);
                        List<Object> result = new ArrayList<>();
                        result.add(o);
                        for (Object[] objects : request) {
                            result.add(wrapper.invokeMethod(proxy, methodName, parameterTypes, objects));
                        }
                        logger.debug("request single to list, size:{}" + result.size());
                        return result;
                    }
                }
            };
        } catch (Throwable fromJavassist) {
            // try fall back to JDK proxy factory
            try {
                Invoker<T> invoker = jdkProxyFactory.getInvoker(proxy, type, url);
                logger.error("",
                        "",
                        "Failed to generate invoker by Javassist failed. Fallback to use JDK proxy success. "
                                + "Interfaces: " + type,
                        fromJavassist);
                // log out error
                return invoker;
            } catch (Throwable fromJdk) {
                logger.error(
                        "",
                        "",
                        "Failed to generate invoker by Javassist failed. Fallback to use JDK proxy is also failed. "
                                + "Interfaces: " + type + " Javassist Error.",
                        fromJavassist);
                logger.error(
                        "",
                        "",
                        "Failed to generate invoker by Javassist failed. Fallback to use JDK proxy is also failed. "
                                + "Interfaces: " + type + " JDK Error.",
                        fromJdk);
                throw fromJavassist;
            }
        }
    }
}
