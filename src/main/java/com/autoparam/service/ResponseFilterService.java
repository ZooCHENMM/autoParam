package com.autoparam.service;

import java.util.Map;
import java.util.Set;

public class ResponseFilterService {

    public void filter(Set filter, Object response, ResponseFilterStrategy responseFilterStrategy) {
        responseFilterStrategy.filter(filter, response);
    }
}
