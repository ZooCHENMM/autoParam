package com.autoparam.service.response.filter;

import java.util.Set;

public class ResponseFilterService {

    public void filter(Set filter, Object response, ResponseFilterStrategy responseFilterStrategy) {
        responseFilterStrategy.filter(filter, response);
    }
}
