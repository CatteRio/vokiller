package com.yy.vokiller;

import java.util.Collection;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/18 13:40
 */
public class CollectionUtils {

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.size() == 0;
    }
}
