package com.autoparam.service;

import com.alibaba.dubbo.rpc.Result;

import java.util.Map;

/**
 * dubbo响应报文默认处理实现
 */
public class DefaultResponseParserServiceImpl implements ResponseParserService {

    @Override
    public Object parse(Result result) {
        return result.getValue();
    }
}
