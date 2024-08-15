package com.autoparam.service.response.parser;

import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;
import org.apache.dubbo.rpc.Result;

/**
 * 解析dubbo响应，返回业务数据map报文
 */
@SPI("default")
public interface ResponseParserService {

    /**
     *
     * @param result dubbo响应
     * @return 业务数据map
     */
    @Adaptive
    Object parse(Result result);
}
