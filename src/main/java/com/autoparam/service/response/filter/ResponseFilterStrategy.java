package com.autoparam.service.response.filter;

import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

import java.util.Set;

@SPI("default")
public interface ResponseFilterStrategy {
    @Adaptive
    void filter(Set filter, Object response);
}
