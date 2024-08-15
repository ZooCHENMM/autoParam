package com.autoparam.service.impl;

import com.autoparam.constants.Constants;
import com.autoparam.service.request.RequestBodyService;
import com.autoparam.service.request.list.RequestSingle2ListService;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.autoparam.constants.Constants.SINGLE_TO_LIST;

@Adaptive
public class DefaultRequestSingle2ListServiceImpl implements RequestSingle2ListService {

    private final RequestBodyService requestBodyService;

    public DefaultRequestSingle2ListServiceImpl() {
        this.requestBodyService = ExtensionLoader.getExtensionLoader(RequestBodyService.class).getAdaptiveExtension();
    }

    @Override
    public List<Object[]> handle(Object[] arguments) {
        List<Object[]> request = new ArrayList<>();
        Map o = requestBodyService.getRequestBody(arguments);
        if (o.containsKey(SINGLE_TO_LIST)) {
            Map<String, List> sigle2ListMap = (Map)o.get(SINGLE_TO_LIST);
            return doHandle(arguments, sigle2ListMap);
        }
        return request;
    }

    private List<Object[]> doHandle(Object[] arguments, Map<String, List> sigle2ListMap) {
        List<Object[]> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(sigle2ListMap)) {
            return result;
        }
        Set<String> keys = sigle2ListMap.keySet();
        List<String> listKey = keys.stream().collect(Collectors.toList());
        for (int i = 0; i < sigle2ListMap.get(listKey.get(0)).size(); i++) {
            Object[] temp = Arrays.copyOf(arguments, arguments.length);
            int finalI = i;
            keys.forEach(key -> {
                String[] par = key.split(Constants.MAP_FIRST_SPLIT);
                List values = sigle2ListMap.get(key);
                for (Object o : temp) {
                    // 业务参数被替换后，需重新组装报文
                    requestBodyService.rebulid(temp, paramReplace(o, par, 0, values, finalI));
                }
            });
            result.add(temp);
        }
        return result;
    }

    private Object paramReplace(Object object, String[] par, int count, List values, int index) {
        if (par.length == count + 1) {
            if (object instanceof Map) {
                ((Map) object).put(par[count], values.get(index));
            }
        }
        if (object instanceof Map) {
            Object next = ((Map<?, ?>) object).get(par[count]);
            paramReplace(next, par, count + 1, values, index);
        } else if (object instanceof List) {
            for (Map m : (List<Map>) object) {
                paramReplace(m.get(par[count]), par, count + 1, values, index);
            }
        }
        return object;
    }
}
