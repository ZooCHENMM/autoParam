package com.autoparam.service;

import com.alibaba.dubbo.rpc.Result;

import java.util.Map;

/**
 * 解析dubbo响应，返回业务数据map报文
 */
public interface ResponseParserService {

    /**
     *
     * @param result dubbo响应
     * @return 业务数据map
     */
    Object parse(Result result);
}
