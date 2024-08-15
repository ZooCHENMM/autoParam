package com.autoparam.service.request;

import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;
import org.apache.dubbo.rpc.Invocation;

import java.util.Map;

@SPI("default")
public interface RequestBodyService {
    @Adaptive
    Map getRequestBody(Object[] arguments);

    @Adaptive
    String getBodyName(Object[] arguments);
}
