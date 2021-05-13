package com.fancy.common.util.convertor;

import net.sf.cglib.core.Converter;
import net.sf.cglib.core.ReflectUtils;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Collection <-> Collection 的转化器 , Collection范围包括Array(几种原型数组),List,Set各种实现类
 */
public class CollectionAndCollectionConvertor {

    protected static Map createMap(Class destClass, int length) {
        if (destClass == Map.class || destClass == HashMap.class) {
            return new HashMap(length);
        }

        if (destClass == TreeMap.class) {
            return new TreeMap();
        }

        if (destClass == LinkedHashMap.class) {
            return new LinkedHashMap(length);
        }
        try {
            return (Map) ReflectUtils.newInstance(destClass);
        } catch (Exception e) {
            throw new RuntimeException("Unsupported Map: [" + destClass.getName() + "]");
        }

    }

    protected static Collection createCollection(Class destClass, int length) {
        if (destClass == List.class || destClass == ArrayList.class) {
            return new ArrayList(length); // list默认为ArrayList
        }

        if (destClass == LinkedList.class) {
            return new LinkedList();
        }

        if (destClass == Vector.class) {
            return new Vector(length);
        }

        if (destClass == Set.class || destClass == HashSet.class) {
            return new HashSet(length); // set默认为HashSet
        }

        if (destClass == LinkedHashSet.class) {
            return new LinkedHashSet(length);
        }

        if (destClass == TreeSet.class) {
            return new TreeSet();
        }

        try {
            return (Collection) ReflectUtils.newInstance(destClass);
        } catch (Exception e) {
            throw new RuntimeException("Unsupported Collection: [" + destClass.getName() + "]");
        }
    }

    protected static void arraySet(Object src, int i, Object value) {
        Array.set(src, i, value);
    }

    protected static Object arrayGet(Object src, int i) {
        return Array.get(src, i);
    }

    /**
     * Collection -> Collection 转化
     */
    public static class CollectionToCollection implements Convertor {

        public Object convert(Object src, Class destClass) {
            if (Collection.class.isAssignableFrom(src.getClass()) && Collection.class.isAssignableFrom(destClass)) { // 必须都是Collection
                Collection collection = (Collection) src;
                Collection target = createCollection(destClass, collection.size());

                for (Iterator iter = collection.iterator(); iter.hasNext(); ) {
                    target.add(iter.next());
                }
                return target;
            }

            throw new RuntimeException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }

    }

    /**
     * array -> array 转化
     */
    public static class ArrayToArray implements Convertor {

        public Object convert(Object src, Class destClass) {
            if (src.getClass().isArray() && destClass.isArray()) { // 特殊处理下数组
                int size = Array.getLength(src);
                Object[] objs = (Object[]) Array.newInstance(destClass.getComponentType(), size);

                for (int i = 0; i < size; i++) {
                    Object obj = arrayGet(src, i);
                    arraySet(objs, i, obj);
                }
                return objs;
            }

            throw new RuntimeException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }

    }

    /**
     * array -> Collection 转化
     */
    public static class ArrayToCollection implements Convertor {

        public Object convert(Object src, Class destClass) {
            if (src.getClass().isArray() && Collection.class.isAssignableFrom(destClass)) { // 特殊处理下数组
                int size = Array.getLength(src);
                Collection target = createCollection(destClass, size);

                for (int i = 0; i < size; i++) {
                    Object obj = arrayGet(src, i);
                    target.add(obj);
                }
                return target;
            }

            throw new RuntimeException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }

    }

    /**
     * Collection -> array 转化
     */
    public static class CollectionToArray implements Convertor {

        public Object convert(Object src, Class destClass) {
            if (Collection.class.isAssignableFrom(src.getClass()) && destClass.isArray()) {
                Collection collection = (Collection) src;
                Object objs = Array.newInstance(destClass.getComponentType(), collection.size());

                int i = 0;
                for (Iterator iter = collection.iterator(); iter.hasNext(); ) {
                    Object item = iter.next();
                    arraySet(objs, i, item);
                    i++;
                }
                return objs;

            }

            throw new RuntimeException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }

    }

}

