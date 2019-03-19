package com.yy.vokiller.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/18 13:40
 */
public class CollectionUtils {

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.size() == 0;
    }


    public static <K, V> boolean isContainsKey(Map<K, V> map, K k) {
        return map != null && map.containsKey(k);
    }
}
