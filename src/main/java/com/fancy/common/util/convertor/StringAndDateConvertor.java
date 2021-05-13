package com.fancy.common.util.convertor;


import com.fancy.common.util.comm.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * string <-> Date/LocalDate/LocalDateTime 之间的转化
 * 
 */
public class StringAndDateConvertor {

    /**
     * string(格式为："2010-10-01" 或者 "2010-10-01 00:00:00") -> Date
     */
    public static class StringToDateTime implements Convertor {
        // 注册到仓库中去
        {
            Convertor convertor = new StringToDateTime();
            ConvertorHelper.register(String.class, Date.class, convertor);
        }
        @Override
        public Object convert(Object src, Class destClass) {
            if (String.class.isInstance(src)) { // 必须是字符串
                String date = (String) src;
                if (date.length() > 10) {
                    return DateUtils.parse(date, DateUtils.DATE_PATTERN.yyyy_MM_dd_HH_mm_ss);
                }
                return DateUtils.parse((String) src, DateUtils.DATE_PATTERN.yyyy_MM_dd);
            }

            throw new RuntimeException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }

    }

    /**
     * Date -> string(格式为："2010-10-01 00:00:00")
     */
    public static class DateTimeToString implements Convertor {
        // 注册到仓库中去
        {
            Convertor convertor = new DateTimeToString();
            ConvertorHelper.register(Date.class, String.class, convertor);
        }
        @Override
        public Object convert(Object src, Class destClass) {
            if (Date.class.isInstance(src)) { // 必须是Date对象
                DateUtils.format((Date)src, DateUtils.DATE_PATTERN.yyyy_MM_dd_HH_mm_ss);
            }

            throw new RuntimeException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }

    }

    /**
     * string(格式为："2010-10-01") -> LocalDate
     */
    public static class StringToLocalDate implements Convertor {
        // 注册到仓库中去
        {
            Convertor convertor = new StringToLocalDate();
            ConvertorHelper.register(String.class, LocalDate.class, convertor);
        }
        @Override
        public Object convert(Object src, Class destClass) {
            if (String.class.isInstance(src)) { // 必须是字符串
                return DateUtils.parseDate((String)src, DateUtils.DATE_PATTERN.yyyy_MM_dd);
            }

            throw new RuntimeException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }
    }

    /**
     * string(格式为："2010-10-01 00:00:00") -> LocalDateTime
     */
    public static class StringToLocalDateTime implements Convertor {
        // 注册到仓库中去
        {
            Convertor convertor = new StringToLocalDateTime();
            ConvertorHelper.register(String.class, LocalDateTime.class, convertor);
        }
        @Override
        public Object convert(Object src, Class destClass) {
            if (String.class.isInstance(src)) { // 必须是字符串
                return DateUtils.parseTime((String)src, DateUtils.DATE_PATTERN.yyyy_MM_dd_HH_mm_ss);
            }

            throw new RuntimeException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }
    }

    /**
     * LocalDate -> string(格式为："2010-10-01")
     */
    public static class LocalDateToString implements Convertor {
        // 注册到仓库中去
        {
            Convertor convertor = new LocalDateToString();
            ConvertorHelper.register(LocalDate.class, String.class, convertor);
        }
        @Override
        public Object convert(Object src, Class destClass) {
            if (LocalDate.class.isInstance(src)) {
                return DateUtils.format((LocalDate)src);
            }

            throw new RuntimeException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }
    }

    /**
     * LocalDateTime -> string(格式为："2010-10-01 00:00:00")
     */
    public static class LocalDateTimeToString implements Convertor {
        // 注册到仓库中去
        {
            Convertor convertor = new LocalDateTimeToString();
            ConvertorHelper.register(LocalDateTime.class, String.class, convertor);
        }
        @Override
        public Object convert(Object src, Class destClass) {
            if (LocalDateTime.class.isInstance(src)) {
                return DateUtils.format((LocalDateTime)src);
            }

            throw new RuntimeException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }
    }
    /**
     * LocalDate 或者 LocalDateTime -> Date
     */
    public static class LocalDateTimeToDate implements Convertor {
        // 注册到仓库中去
        {
            Convertor convertor = new LocalDateTimeToDate();
            ConvertorHelper.register(LocalDateTime.class, Date.class, convertor);
            ConvertorHelper.register(LocalDate.class, Date.class, convertor);
        }
        @Override
        public Object convert(Object src, Class destClass) {
            LocalDateTime srcTime = null;
            if (LocalDateTime.class.isInstance(src)) { // 必须是Calendar
                srcTime = (LocalDateTime) src;
            } else if (LocalDate.class.isInstance(src)) { // 必须是Calendar
                srcTime = ((LocalDate) src).atStartOfDay();
            }

            if (srcTime != null) {
                ZonedDateTime zdt = srcTime.atZone(ZoneId.systemDefault());
                return Date.from(zdt.toInstant());
            }

            throw new RuntimeException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }
    }
    /**
     * LocalDate 或者 LocalDateTime -> Date
     */
    public static class DateToLocalDateTime implements Convertor {
        // 注册到仓库中去
        {
            Convertor convertor = new DateToLocalDateTime();
            ConvertorHelper.register(Date.class, LocalDateTime.class, convertor);
            ConvertorHelper.register(Date.class, LocalDate.class, convertor);
        }
        @Override
        public Object convert(Object src, Class destClass) {
            if (src.getClass().isInstance(Date.class)) {
                Date date = (Date) src;
                ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
                if (destClass.equals(LocalDateTime.class)) {
                    return zonedDateTime.toLocalDateTime();
                } else if (destClass.equals(LocalDate.class)) {
                    return zonedDateTime.toLocalDate();
                }
            }

            throw new RuntimeException("Unsupported convert: [" + src + "," + destClass.getName() + "]");
        }
    }
}
