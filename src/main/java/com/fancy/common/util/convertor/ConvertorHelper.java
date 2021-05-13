package com.fancy.common.util.convertor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * convert转化helper类，注册一些默认的convertor
 * 
 */
public class ConvertorHelper {

    private static final String    SEPERATOR   = ":";

    private static Map<String, Convertor> convertors  = new ConcurrentHashMap<>(10);

    // common对象范围：8种Primitive和对应的Java类型，BigDecimal, BigInteger
    private static Set<Class>               commonTypes                   = new HashSet<>();
    private static final Convertor          stringToCommon                = new StringAndCommonConvertor.StringToCommon();
    private static final Convertor          commonToCommon                = new CommonAndCommonConvertor.CommonToCommon();
    // 数组处理
    private static final Convertor          arrayToArray                  = new CollectionAndCollectionConvertor.ArrayToArray();
    private static final Convertor          arrayToCollection             = new CollectionAndCollectionConvertor.ArrayToCollection();
    private static final Convertor          collectionToArray             = new CollectionAndCollectionConvertor.CollectionToArray();
    private static final Convertor          collectionToCollection        = new CollectionAndCollectionConvertor.CollectionToCollection();
    // toString处理
    private static final Convertor          objectToString                = new StringAndObjectConvertor.ObjectToString();
    // 枚举处理
    private static final Convertor          stringToEnum                  = new StringAndEnumConvertor.StringToEnum();
    private static final Convertor          enumToString                  = new StringAndEnumConvertor.EnumToString();

    {
        commonTypes.add(int.class);
        commonTypes.add(Integer.class);
        commonTypes.add(short.class);
        commonTypes.add(Short.class);
        commonTypes.add(long.class);
        commonTypes.add(Long.class);
        commonTypes.add(boolean.class);
        commonTypes.add(Boolean.class);
        commonTypes.add(byte.class);
        commonTypes.add(Byte.class);
        commonTypes.add(char.class);
        commonTypes.add(Character.class);
        commonTypes.add(float.class);
        commonTypes.add(Float.class);
        commonTypes.add(double.class);
        commonTypes.add(Double.class);
        commonTypes.add(BigDecimal.class);
        commonTypes.add(BigInteger.class);
    }


    /**
     * 根据class获取对应的convertor
     * 
     * @return
     */
    public static Convertor getConvertor(Class src, Class dest) {
        if (src == dest) {
            // 对相同类型的直接忽略，不做转换
            return null;
        }

        // 按照src->dest来取映射
        Convertor convertor = convertors.get(mapperConvertorName(src, dest));
        if (convertor != null) {
            return convertor;
        }

        // 处理下Array|Collection的映射
        // 如果src|dest是array类型，取一下Array.class的映射，因为默认数组处理的注册直接注册了Array.class
        boolean isSrcArray = src.isArray();
        boolean isDestArray = dest.isArray();
        if (isSrcArray && isDestArray) {
            return arrayToArray;
        }
        boolean isSrcCollection = Collection.class.isAssignableFrom(src);
        boolean isDestCollection = Collection.class.isAssignableFrom(dest);
        if (isSrcArray && isDestCollection) {
            return arrayToCollection;
        }
        if (isDestArray && isSrcCollection) {
            return collectionToArray;
        }
        if (isSrcCollection && isDestCollection) {
            return collectionToCollection;
        }

        // 如果dest是string，获取一下object->string. (系统默认注册了一个Object.class -> String.class的转化)
        if (dest == String.class) {
            if (src.isEnum()) {// 如果是枚举
                return enumToString;
            }
            // 默认进行toString输出
            return objectToString;
        }

        // 如果是其中一个是String类
        if (src == String.class) {
            if (commonTypes.contains(dest)) {
                // 另一个是Common类型
                return stringToCommon;
            } else if (dest.isEnum()) {
                // 另一个是枚举对象
                return stringToEnum;
            }
        }

        // 如果src/dest都是Common类型，进行特殊处理
        if (commonTypes.contains(src) && commonTypes.contains(dest)) {
            return commonToCommon;
        }

        return null;
    }

    public static Convertor getConvertor(String alias) {
        return convertors.get(alias);
    }

    public static void register(Class src, Class dest, Convertor convertor) {
        String key = mapperConvertorName(src, dest);
        // 对于已经注册的convert，进行覆盖处理
        if (convertor != null) {
            convertors.put(key, convertor);
        }
    }

    public static void register(String alias, Convertor convertor) {
        // 对于已经注册的convert，进行覆盖处理
        if (convertor != null) {
            convertors.put(alias, convertor);
        }
    }

    private static String mapperConvertorName(Class src, Class dest) {
        String name1 = getName(src);
        String name2 = getName(dest);

        return name1 + SEPERATOR + name2;
    }


    private static String getName(Class type) {
        if (type.isPrimitive()) {
            if (type == int.class) {
                return Integer.class.getName();
            } else if (type == short.class) {
                return Short.class.getName();
            } else if (type == long.class) {
                return Long.class.getName();
            } else if (type == char.class) {
                return Character.class.getName();
            } else if (type == void.class) {
                return Void.class.getName();
            } else if (type == double.class) {
                return Double.class.getName();
            } else if (type == float.class) {
                return Float.class.getName();
            } else if (type == byte.class) {
                return Byte.class.getName();
            } else if (type == boolean.class) {
                return Boolean.class.getName();
            }
        }

        return type.getName();
    }

}
