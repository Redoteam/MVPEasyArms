package com.yl.acoreui.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    public static final String FORMAT_HM = "HH:mm";
    public static final String FORMAT_HMS = "HH:mm:ss";
    public static final String FORMAT_YMD = "yyyy-MM-dd";
    public static final String FORMAT_MDHM = "MM-dd HH:mm";
    public static final String FORMAT_YMDHM = "yyyy-MM-dd HH:mm";
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YMD_CN = "yyyy年MM月dd日";
    private static final int MILLIS_SECOND = 1000;
    private static final int MILLIS_MINUTE = 60000;
    private static final int MILLIS_HOUR = 3600000;
    private static final int MILLIS_DAY = 86400000;

    /**
     * 默认格式字符串-->日期
     */
    public static Date str2Date(String str) {
        return str2Date(str, null);
    }

    /**
     * 指定格式字符串-->日期
     */
    public static Date str2Date(String str, String format) {
        if (str == null || str.length() == 0) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(str);
        } catch (Exception e) {
        }
        return date;
    }

    /**
     * 日期-->默认格式字符串
     */
    public static String date2Str(Date d) {// yyyy-MM-dd HH:mm:ss
        return date2Str(d, null);
    }

    /**
     * 日期-->指定格式字符串
     */
    public static String date2Str(Date d, String format) {
        if (d == null) {
            return "";
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        return new SimpleDateFormat(format).format(d);
    }

    /**
     * 默认格式字符串-->指定格式日期字符串
     */
    public static String str2Str(String str, String format) {
        return date2Str(str2Date(str), format);
    }

    /**
     * 获取当前时间,默认格式
     */
    public static String getTimeStr() {
        return getTimeStr(null);
    }

    /**
     * 获取当前时间,指定格式
     */
    public static String getTimeStr(String format) {
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * 时间戳-->默认格式字符串
     */
    public static String getTimeStr(long time) {
        return getTimeStr(time, null);
    }

    /**
     * 时间戳-->指定格式字符串
     */
    public static String getTimeStr(long time, String format) {
        if (time < 1) {
            return "";
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        return new SimpleDateFormat(format).format(new Date(time));
    }

    /**
     * 默认格式字符串-->时间戳
     */
    public static long str2TimeStamp(String time) {
        return str2TimeStamp(time, FORMAT);
    }

    /**
     * 指定格式字符串-->时间戳
     */
    public static long str2TimeStamp(String time, String format) {
        if (time == null || time.length() == 0) {
            return 0;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(time).getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取时间
     *
     * @param format (e.g. yyyyMMdd) 不能带特殊字符
     * @return (e.g. 20121212)
     */
    public static int getTime(String format) {
        String time = getTimeStr(format);
        return Integer.parseInt(time);
    }

    /**
     * 时间格式化
     */
    public static String millis2time(long millis) {
        if (millis <= 0) {
            return "00:00";
        }
        long second = (long) Math.ceil(millis / MILLIS_SECOND);
        StringBuffer sb = new StringBuffer("");
        if (second < 60) {
            sb.append("00:");
            appendStr(sb, second);
        } else {
            appendStr(sb, second / 60);
            sb.append(":");
            appendStr(sb, second % 60);
        }
        return sb.toString();
    }

    public static void appendStr(StringBuffer sb, long l) {
        if (l < 10) {
            sb.append("0").append(l);
        } else {
            sb.append(l);
        }
    }

    /**
     * 某一时间多少分钟后的时间
     */
    public static String appendTime(String format, String startTime, int minute) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(startTime);
            date.setTime(date.getTime() + minute * MILLIS_MINUTE);
            return sdf.format(date);
        } catch (ParseException e) {
            return "";
        }
    }

    public static boolean timeCompare(String format, String time1, String time2) {
        long long1 = str2TimeStamp(time1, format);
        long long2 = str2TimeStamp(time2, format);
        return long1 < long2;
    }

    /**
     * 计算时间差
     */
    public static String getDuration(String format, String start, String end) {
        StringBuffer sb = new StringBuffer("");
        try {
            Date date1 = new SimpleDateFormat(format).parse(start);
            Date date2 = new SimpleDateFormat(format).parse(end);
            long l = date1.getTime() - date2.getTime() < 0 ? date2.getTime() - date1.getTime() : date1.getTime() - date2.getTime();
            if (l < MILLIS_SECOND) {
                sb.append("1秒");
            } else if (l < MILLIS_MINUTE) {
                appendStr(sb, l, MILLIS_SECOND, "秒");
            } else if (l < MILLIS_HOUR) {
                appendStr(sb, l, MILLIS_MINUTE, "分钟");
                appendStr(sb, l % MILLIS_MINUTE, MILLIS_SECOND, "秒");
            } else {
                appendStr(sb, l, MILLIS_HOUR, "小时");
                appendStr(sb, l % MILLIS_HOUR, MILLIS_MINUTE, "分钟");
                appendStr(sb, l % MILLIS_MINUTE, MILLIS_SECOND, "秒");
            }
        } catch (ParseException e) {
            return sb.toString();
        }
        return sb.toString();
    }

    private static void appendStr(StringBuffer sb, long time, long divisor, String unit) {
        long s = time / divisor;
        if (s != 0) {
            sb.append(s).append(unit);
        }
    }


    /**
     * 获取友好时间
     */
    public static String getFriendlyTime(String timeStr) {
        if (TextUtils.isEmpty(timeStr)) {
            return "";
        }
        Date date = str2Date(timeStr);
        if (date == null) {
            return "";
        }
        String formatTime = "";
        Calendar currentDay = Calendar.getInstance();

        Calendar timeDay = Calendar.getInstance();
        timeDay.setTime(date);

        if (currentDay.get(Calendar.YEAR) != timeDay.get(Calendar.YEAR)) {
            return date2Str(date, FORMAT_YMD);
        }

        if (currentDay.get(Calendar.DAY_OF_MONTH) != timeDay.get(Calendar.DAY_OF_MONTH)) {
            return date2Str(date, FORMAT_MDHM);
        }

        return timeDay.get(Calendar.AM_PM) == 0 ? "上午" : "下午" + date2Str(date, "hh:mm");
    }

    public static String getWeek() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return "星期" + mWay;
    }
}
