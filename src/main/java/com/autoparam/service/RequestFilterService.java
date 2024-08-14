package com.autoparam.service;

import com.alibaba.dubbo.rpc.Invocation;

import java.util.Set;

public interface RequestFilterService {
    Set<String> getFilterParams(Invocation invocation);
}
