package com.autoparam.service.impl;

import com.autoparam.service.request.RequestBodyService;
import org.apache.dubbo.common.extension.Adaptive;

import java.util.Collections;
import java.util.Map;

@Adaptive
public class DefaultRequestBodyServiceImpl implements RequestBodyService {
    @Override
    public Map getRequestBody(Object[] arguments) {
        for (Object argument : arguments) {
            if (argument instanceof Map) {
                Map map = (Map) argument;
                return map;
            }
        }
        return Collections.emptyMap();
    }

    @Override
    public Object[] rebulid(Object[] arguments, Object body) {
        for (Object argument : arguments) {
            if (argument instanceof Map) {
                Map map = (Map) argument;
                map.put("body", body);
            }
        }
        return arguments;
    }

}
