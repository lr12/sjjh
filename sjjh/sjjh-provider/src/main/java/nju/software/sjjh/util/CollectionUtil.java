package nju.software.sjjh.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Nekonekod on 2017/4/24.
 */
public class CollectionUtil {


    /**
     * 是否为null或空
     * @param c
     * @return
     */
    public static boolean isEmpty(Collection c){
        return !isNotEmpty(c);
    }

    /**
     * 不为null且不为空
     * @param c
     * @return
     */
    public static boolean isNotEmpty(Collection c){
        return c != null && !c.isEmpty();
    }

    /**
     * 从一个类型的集合转为另一个类型的集合
     * @param src 原来的集合
     * @param callback 转换的逻辑
     * @param <T> 元类型
     * @param <K> 输出结果类型
     * @return
     */
    public static <T,K> List<T> mapping(List<K> src, MappingCallback<K,T> callback){
        ArrayList<T> result = new ArrayList<>(src.size());
        for(K k:src){
            result.add(callback.map(k));
        }
        return result;
    }

    public interface MappingCallback<K,T>{
        T map(K k);
    }

}
