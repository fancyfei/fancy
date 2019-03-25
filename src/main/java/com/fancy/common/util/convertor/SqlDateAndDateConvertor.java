package com.fancy.common.util.convertor;

import java.util.Date;

/**
 * Date <-> SqlDate 之间的转化
 * 
 */
public class SqlDateAndDateConvertor {


    public static class SqlDateToDateConvertor implements Convertor {

        // 注册到仓库中去
        {
            Convertor convertor = new SqlDateToDateConvertor();
            ConvertorHelper.register(java.sql.Date.class, Date.class, convertor);
            ConvertorHelper.register(java.sql.Timestamp.class, Date.class, convertor);
            ConvertorHelper.register(java.sql.Time.class, Date.class, convertor);
        }

        @Override
        public Object convert(Object src, Class destClass) {
            if (Date.class != destClass) {
                throw new RuntimeException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
            }
            if (src instanceof java.sql.Date) {
                return new Date(((java.sql.Date) src).getTime());
            }
            if (src instanceof java.sql.Timestamp) {
                return new Date(((java.sql.Timestamp) src).getTime());
            }
            if (src instanceof java.sql.Time) {
                return new Date(((java.sql.Time) src).getTime());
            }
            throw new RuntimeException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }
    }

    /**
     * Date转sql包中的子类Date、Timestamp、Time
     */
    public static class DateToSqlDateConvertor implements Convertor {

        // 注册到仓库中去
        {
            Convertor convertor = new DateToSqlDateConvertor();
            ConvertorHelper.register(Date.class, java.sql.Date.class, convertor);
            ConvertorHelper.register(Date.class, java.sql.Timestamp.class, convertor);
            ConvertorHelper.register(Date.class, java.sql.Time.class, convertor);
        }
        @Override
        public Object convert(Object src, Class destClass) {
            if (Date.class.isInstance(src)) { // 必须是字符串
                Date date = (Date) src;
                long value = date.getTime();
                // java.sql.Date
                if (destClass.equals(java.sql.Date.class)) {
                    return new java.sql.Date(value);
                }

                // java.sql.Time
                if (destClass.equals(java.sql.Time.class)) {
                    return new java.sql.Time(value);
                }

                // java.sql.Timestamp
                if (destClass.equals(java.sql.Timestamp.class)) {
                    return new java.sql.Timestamp(value);
                }
            }

            throw new RuntimeException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }
    }
}
