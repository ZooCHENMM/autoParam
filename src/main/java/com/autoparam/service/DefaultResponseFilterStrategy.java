package com.autoparam.service;

import com.autoparam.constants.Constants;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultResponseFilterStrategy implements ResponseFilterStrategy {

    @Override
    public void filter(Set filter, Map response) {
        filterByRemove(filter, response);
    }

    private void filterByRemove(Set<String> filter, Object response) {
        List<Set<String>> list = new ArrayList<>();
        filter.forEach(item -> {
            String[] keys = item.split(Constants.MAP_FIRST_SPLIT);
            for (int i = 0; i < keys.length; i++) {
                if (list.size() < i + 1) {
                    list.add(i, new HashSet<>());
                }
                list.get(i).addAll(Arrays.stream(keys[i].split(Constants.MAP_SECOND_SPLIT)).collect(Collectors.toSet()));
            }
        });
        for (String string : filter) {
            List<String> keys = Arrays.asList(string.split(Constants.MAP_FIRST_SPLIT));
            doFilter(keys, list, response);
        }
    }


    private void doFilter(List<String> key, List<Set<String>> allKey, Object response) {
        if (key.size() == 1) {
            Set<String> key2 = Arrays.stream(key.get(0).split(Constants.MAP_SECOND_SPLIT)).collect(Collectors.toSet());
            if (response instanceof List) {
                for (Object l : ((List) response)) {
                    Set<String> rKeys = ((Map) l).keySet();
                    Set<String> filterSet = new HashSet<>();
                    filterSet.addAll(key2);
                    filterSet.addAll(allKey.get(0));
                    Set<String> noKey = rKeys.stream().filter(item -> !filterSet.contains(item)).collect(Collectors.toSet());
                    for (String item : noKey) {
                        ((Map) l).remove(item);
                    }
                }

            } else if (response instanceof Map) {
                Set<String> rKeys = ((Map) response).keySet();
                Set<String> filterSet = new HashSet<>();
                filterSet.addAll(key2);
                filterSet.addAll(allKey.get(0));
                Set<String> noKey = rKeys.stream().filter(item -> !filterSet.contains(item)).collect(Collectors.toSet());
                for (String item : noKey) {
                    ((Map) response).remove(item);
                }
            }
            return;
        }
        if (response instanceof List) {
            List<Map> list = (List) response;
            for (Map map : list) {
                doFilter(Arrays.asList(key.get(0)), Arrays.asList(allKey.get(0)), map);
                doFilter(key.subList(1, key.size()), allKey.subList(1, allKey.size()), map.get(key.get(0)));
            }
        } else if (response instanceof Map) {
            doFilter(Arrays.asList(key.get(0)), Arrays.asList(allKey.get(0)), response);
            doFilter(key.subList(1, key.size()), allKey.subList(1, allKey.size()), ((Map) response).get(key.get(0)));
        }
    }


    /**
     * 选择式处理类型为“S1:S11,S12|S2|S3”,太复杂，目前实现不完整
     *
     * @param filter
     * @param response
     * @return
     */
    private Map<Object, Object> filterByAdd(Set<String> filter, Map<String, Object> response) {
        Map<Object, Object> result = new HashMap<>();
        filter.forEach(item -> {
            String[] key1 = item.split(Constants.MAP_FIRST_SPLIT);
            Map m1 = new HashMap(response);

            for (int i = 0; i < key1.length; i++) {
                if (i == key1.length - 1) {
                    String[] key2 = key1[i].split(Constants.MAP_SECOND_SPLIT);
                    result.put(key1[0], bulidValue(i, key1, key2, m1));
                    break;
                }
                m1 = (Map) m1.get(key1[i]);
            }
        });
        return result;
    }

    private Object bulidValue(int i, String[] key1, String[] key2, Map data) {
        if (i == 0) {
            return data.get(key2[0]);
        }
        Map[] maps = new Map[i + 1];
        for (int k = 0; k <= i; k++) {
            maps[k] = new HashMap();
        }
        for (int j = 0; j < key2.length; j++) {
            maps[i].put(key2[j], data.get(key2[j]));
        }
        for (int l = i - 1; l > 0; l--) {
            maps[l].put(key1[l], maps[l + 1]);
        }
        return maps[1];
    }
}
