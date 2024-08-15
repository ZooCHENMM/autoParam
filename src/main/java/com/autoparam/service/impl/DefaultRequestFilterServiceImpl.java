package com.autoparam.service.impl;

import com.autoparam.service.request.RequestBodyService;
import com.autoparam.service.request.filter.RequestFilterService;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.rpc.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static com.autoparam.constants.Constants.RESPONSE_FILTER;

@Adaptive
public class DefaultRequestFilterServiceImpl implements RequestFilterService {

    private static final Logger log = LoggerFactory.getLogger(DefaultRequestFilterServiceImpl.class);
    private final RequestBodyService requestBodyService;

    public DefaultRequestFilterServiceImpl() {
        this.requestBodyService = ExtensionLoader.getExtensionLoader(RequestBodyService.class).getAdaptiveExtension();
    }

    @Override
    public Set<String> getFilterParams(Invocation invocation) {
        Map map = requestBodyService.getRequestBody(invocation.getArguments());
        // 请求自定义过滤参数
        Object o = map.get(RESPONSE_FILTER);
        if (!(o instanceof Set)) {
            log.error("responseFilter type is not set, filter is invalid!");
        }
        return (Set<String>) o;
    }
}
