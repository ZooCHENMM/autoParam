package com.autoparam.service;

import org.apache.dubbo.rpc.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static com.autoparam.constants.Constants.RESPONSE_FILTER;

public class DefaultRequestFilterServiceImpl implements RequestFilterService {

    private static final Logger log = LoggerFactory.getLogger(DefaultRequestFilterServiceImpl.class);

    @Override
    public Set<String> getFilterParams(Invocation invocation) {
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
                return (Set<String>) o;
            }
        }
        return Collections.emptySet();
    }
}
