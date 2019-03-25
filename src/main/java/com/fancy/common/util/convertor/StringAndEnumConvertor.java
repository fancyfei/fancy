package com.fancy.common.util.convertor;

/**
 * string <-> Enum 之间的转化
 * 
 */
public class StringAndEnumConvertor {

    /**
     * string -> Enum 对象的转化
     */
    public static class StringToEnum implements Convertor {

        @Override
        public Object convert(Object src, Class destClass) {
            if (src instanceof String && destClass.isEnum()) {
                return Enum.valueOf(destClass, (String) src);
            }

            throw new RuntimeException("Unsupported convert: [" + src.getClass() + "," + destClass.getName() + "]");

        }
    }

    /**
     * Enum -> String 对象的转化
     */
    public static class EnumToString implements Convertor {

        @Override
        public Object convert(Object src, Class destClass) {
            if (src.getClass().isEnum() && destClass == String.class) {
                return ((Enum) src).name(); // 返回定义的enum name
            }

            throw new RuntimeException("Unsupported convert: [" + src.getClass() + "," + destClass.getName() + "]");
        }
    }
}
