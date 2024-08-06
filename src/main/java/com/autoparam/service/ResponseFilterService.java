package com.autoparam.service;

import java.util.Map;
import java.util.Set;

public class ResponseFilterService {

    public void filter(Set filter, Map response, ResponseFilterStrategy responseFilterStrategy) {
        responseFilterStrategy.filter(filter, response);
    }
}
