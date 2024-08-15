package com.autoparam.service.request.list;

import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

import java.util.List;

@SPI("default")
public interface RequestSingle2ListService {
    @Adaptive
    List<Object[]> handle(Object[] arguments);
}
