package com.autoparam.service.request.filter;

import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;
import org.apache.dubbo.rpc.Invocation;

import java.util.Set;

@SPI("default")
public interface RequestFilterService {
    @Adaptive
    Set<String> getFilterParams(Invocation invocation);
}
