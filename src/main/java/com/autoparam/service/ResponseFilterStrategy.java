package com.autoparam.service;

import java.util.Map;
import java.util.Set;

public interface ResponseFilterStrategy {
    void filter(Set filter, Object response);
}
