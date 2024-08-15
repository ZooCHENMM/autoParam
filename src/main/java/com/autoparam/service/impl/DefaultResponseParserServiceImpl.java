package com.autoparam.service.impl;

import com.autoparam.service.response.parser.ResponseParserService;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.rpc.Result;

/**
 * dubbo响应报文默认处理实现
 */
@Adaptive
public class DefaultResponseParserServiceImpl implements ResponseParserService {

    @Override
    public Object parse(Result result) {
        return result.getValue();
    }
}
