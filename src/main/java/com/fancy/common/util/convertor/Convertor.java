package com.fancy.common.util.convertor;

/**
 * 类型转换器
 *
 * @author : ranlongfei@tseveryday.com
 * @date 2019/3/22
 */
public interface Convertor {

    /**
     * 将src对象转换为指定类型的对象
     * @param src
     * @param destClass
     * @return
     */
    Object convert(Object src, Class destClass);
}
