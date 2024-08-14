package com.autoparam.service;

import org.apache.dubbo.rpc.Invocation;

import java.util.Set;

public interface RequestFilterService {
    Set<String> getFilterParams(Invocation invocation);
}
