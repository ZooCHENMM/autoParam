package com.autoparam;

import com.autoparam.config.ServerProperties;
import com.autoparam.service.ResponseFilterService;
import com.autoparam.service.ResponseFilterStrategy;
import com.autoparam.service.ResponseParserService;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.autoparam.constants.Constants.RESPONSE_FILTER;

@Activate(group = {CommonConstants.PROVIDER}, order = 10000)
public class ParamFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(ParamFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // before filter ...
        Result result = invoker.invoke(invocation);
        log.debug("========== before filter==========");
        log.debug(result.getValue().toString());
        // 扩展，用于处理响应
        ResponseParserService responseParserService = ApplicationContextProvider.getApplicationContext().getBean(ResponseParserService.class);
        Object response = responseParserService.parse(result);
        for (Object argument : invocation.getArguments()) {
            if (argument instanceof Map) {
                Map map = (Map) argument;
                // 请求自定义过滤参数
                Object o = map.get(RESPONSE_FILTER);
                // todo 多种过滤策略
                if (!(o instanceof Set)) {
                    log.error("responseFilter type is not set, filter is invalid!");
                    continue;
                }
                Set filter = (Set) o;
                ResponseFilterService responseFilterService = ApplicationContextProvider.getApplicationContext().getBean(ResponseFilterService.class);
                String filterStrategy = ApplicationContextProvider.getApplicationContext().getBean(ServerProperties.class).getFilterStrategy();
                ResponseFilterStrategy responseFilterStrategy = ApplicationContextProvider.getApplicationContext().getBean(filterStrategy == null ? "default" : filterStrategy, ResponseFilterStrategy.class);
                responseFilterService.filter(filter, response, responseFilterStrategy);
                result.setValue(response);
            }
        }
        log.debug("========== after filter==========");
        // after filter ...
        log.debug(result.getValue().toString());
        return result;
    }
}
