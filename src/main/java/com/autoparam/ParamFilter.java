package com.autoparam;

import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.rpc.*;
import com.autoparam.service.request.filter.RequestFilterService;
import com.autoparam.service.response.filter.ResponseFilterService;
import com.autoparam.service.response.filter.ResponseFilterStrategy;
import com.autoparam.service.response.parser.ResponseParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Activate(group = "provider", order = 10000)
public class ParamFilter implements Filter {

    private final ResponseFilterService responseFilterService;

    public ParamFilter() {
        this.responseFilterService = new ResponseFilterService();
    }

    private static final Logger log = LoggerFactory.getLogger(ParamFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // before filter ...
        Result result = invoker.invoke(invocation);
        log.debug("========== before filter==========");
        log.debug(result.getValue().toString());
        // 扩展，用于处理响应
        ResponseParserService responseParserService = ExtensionLoader.getExtensionLoader(ResponseParserService.class).getAdaptiveExtension();
        Object response = responseParserService.parse(result);
        RequestFilterService requestFilterService = ExtensionLoader.getExtensionLoader(RequestFilterService.class).getAdaptiveExtension();
        Set<String> filter = requestFilterService.getFilterParams(invocation);
        ResponseFilterStrategy responseFilterStrategy = ExtensionLoader.getExtensionLoader(ResponseFilterStrategy.class).getAdaptiveExtension();
        responseFilterService.filter(filter, response, responseFilterStrategy);
        result.setValue(response);

        log.debug("========== after filter==========");
        // after filter ...
        log.debug(result.getValue().toString());
        return result;
    }
}
