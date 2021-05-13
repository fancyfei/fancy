package com.fancy.common.util.comm;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 日期时间工具类
 */
public class DateUtils {

    private static Logger logger = LoggerFactory.getLogger(DateUtils.class);

    /**
     * 日期格式
     */
    public enum DATE_PATTERN {
        /**
         * yyyy-MM
         */
        yyyy_MM("yyyy-MM", "^\\d{2,4}-\\d{1,2}$"),

        /**
         * yyyyMMdd
         */
        yyyyMMdd("yyyyMMdd", "^\\d{2,4}\\d{1,2}\\d{1,2}$"),

        /**
         * yyyy-MM-dd
         */
        yyyy_MM_dd("yyyy-MM-dd", "^\\d{2,4}-\\d{1,2}-\\d{1,2}$"),

        /**
         * yyyy-MM-dd HH:mm
         */
        yyyy_MM_dd_HH_mm("yyyy-MM-dd HH:mm", "^\\d{2,4}-\\d{1,2}-\\d{1,2}\\s.{1,2}\\d{1,2}:\\d{1,2}$"),

        /**
         * yyyy-MM-dd HH:mm:ss
         */
        yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss", "^\\d{2,4}-\\d{1,2}-\\d{1,2}\\s.{1,2}\\d{1,2}:\\d{1,2}:\\d{1,2}$"),

        /**
         * yyyy-MM-dd HH:mm:ss.SSS
         */
        yyyy_MM_dd_HH_mm_ss_SSS("yyyy-MM-dd HH:mm:ss.SSS", "^\\d{2,4}-\\d{1,2}-\\d{1,2}\\s.{1,2}\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{3,}$");

        private String value;

        private String pattern;

        private DateTimeFormatter formatter;

        private DATE_PATTERN(String value, String pattern) {
            this.value = value;
            this.pattern = pattern;
            this.formatter = DateTimeFormatter.ofPattern(this.value);
        }

        private DateTimeFormatter getFormatter() {
            return formatter;
        }
        /**
         * 根据样本获取模式
         *
         * @param date
         * @return
         */
        public static DATE_PATTERN getPattern(String date) {

            if (StringUtils.isBlank(date)) {
                return null;
            }

            for (DATE_PATTERN value : DATE_PATTERN.values()) {
                if (date.trim().matches(value.pattern)) {
                    return value;
                }
            }

            return null;
        }

        public String getValue() {
            return this.value;
        }
        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * 解析DATE_PATTERN支持格式的时间
     *
     * @param date
     * @return
     */
    public static Date parse(String date) {

        if (StringUtils.isBlank(date)) {
            return null;
        }

        date = date.trim();
        if (date.indexOf("中国标准时间") != -1 || date.indexOf("CST") != -1) {
            return new Date(date);
        }

        DATE_PATTERN pattern = DATE_PATTERN.getPattern(date);
        if (pattern == null) {
            return null;
        }
        return parse(date, pattern);
    }

    /**
     * yyyy-MM-dd格式
     * @param date
     * @return
     */
    public static LocalDate parseDate(String date) {
        return LocalDate.parse(date, DATE_PATTERN.yyyy_MM_dd.formatter);
    }
    /**
     * yyyy-MM-dd格式
     * @param date
     * @return
     */
    public static LocalDateTime parseTime(String date) {
        return LocalDateTime.parse(date, DATE_PATTERN.yyyy_MM_dd_HH_mm_ss.formatter);
    }

    /**
     * 根据指定模式解析时间字符串为 date 对象并返回
     *
     * @param date    date字符串
     * @param pattern 模式
     * @return
     */
    public static Date parse(String date, DATE_PATTERN pattern) {
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(date, pattern.value);
        } catch (ParseException e) {
            logger.error("格式化出错", e);
        }
        return null;
    }


    /**
     * 根据指定模式解析时间字符串为 date 对象并返回
     *
     * @param date    date字符串
     * @param pattern 模式
     * @return
     */
    public static LocalDate parseDate(String date, DATE_PATTERN pattern) {
        return LocalDate.parse(date, pattern.formatter);
    }

    /**
     * 根据指定模式解析时间字符串为 date 对象并返回
     *
     * @param date    date字符串
     * @param pattern 模式
     * @return
     */
    public static LocalDateTime parseTime(String date, DATE_PATTERN pattern) {
        return LocalDateTime.parse(date, pattern.formatter);
    }

    /**
     * 格式化时间
     *
     * @param date
     * @param datePattern 模式
     * @return
     */
    public static String format(Date date, DATE_PATTERN datePattern) {
        return DateFormatUtils.format(date, datePattern.toString());
    }

    /**
     * 格式化时间【yyyy-MM-dd HH:mm:ss】
     *
     * @param date
     * @return
     */
    public static String formatTime(Date date) {
        return format(date, DATE_PATTERN.yyyy_MM_dd_HH_mm_ss);
    }
    /**
     * 格式化时间【yyyy-MM-dd】
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        return format(date, DATE_PATTERN.yyyy_MM_dd);
    }
    /**
     * 格式化时间【yyyy-MM-dd】
     *
     * @param date
     * @return
     */
    public static String format(LocalDate date) {
        return date.format(DATE_PATTERN.yyyy_MM_dd.formatter);
    }
    /**
     * 格式化时间【yyyy-MM-dd HH:mm:ss】
     *
     * @param date
     * @return
     */
    public static String format(LocalDateTime date) {
        return date.format(DATE_PATTERN.yyyy_MM_dd_HH_mm_ss.formatter);
    }

    public static void main(String[] args) {
        Date date = parse("2019-01-01 12:54:15", DATE_PATTERN.yyyy_MM_dd_HH_mm_ss);
        System.out.println(date);
//        System.out.println(parse(date.toString()));
        System.out.println(format(new Date()));
        System.out.println(formatTime(new Date()));

        long days = ChronoUnit.DAYS.between(date.toInstant(), new Date().toInstant());
        System.out.println(days);

    }
}
