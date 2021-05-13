package com.fancy.common.util.convertor;

/**
 * object <-> String 之间的转化器，目前只实现object -> String的转化
 * 
 */
public class StringAndObjectConvertor {

    /**
     * object -> string 转化
     */
    public static class ObjectToString implements Convertor {

        @Override
        public Object convert(Object src, Class destClass) {
            return src != null ? src.toString() : null;
        }
    }

}
