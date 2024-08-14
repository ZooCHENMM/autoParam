package com.autoparam;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.autoparam.config.ServerProperties;
import com.autoparam.service.RequestFilterService;
import com.autoparam.service.ResponseFilterService;
import com.autoparam.service.ResponseFilterStrategy;
import com.autoparam.service.ResponseParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.autoparam.constants.Constants.RESPONSE_FILTER;

@Activate(order = 10000)
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
        RequestFilterService requestFilterService = ApplicationContextProvider.getApplicationContext().getBean(RequestFilterService.class);
        Set<String> filter = requestFilterService.getFilterParams(invocation);
        ResponseFilterService responseFilterService = ApplicationContextProvider.getApplicationContext().getBean(ResponseFilterService.class);
        String filterStrategy = ApplicationContextProvider.getApplicationContext().getBean(ServerProperties.class).getFilterStrategy();
        ResponseFilterStrategy responseFilterStrategy = ApplicationContextProvider.getApplicationContext().getBean(filterStrategy == null ? "default" : filterStrategy, ResponseFilterStrategy.class);
        responseFilterService.filter(filter, response, responseFilterStrategy);
        ((RpcResult) result).setValue(response);

        log.debug("========== after filter==========");
        // after filter ...
        log.debug(result.getValue().toString());
        return result;
    }
}
