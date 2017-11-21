package com.ducetech.framework.util;

import com.ducetech.framework.support.service.DynamicConfigService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

    private final static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public final static String DEFAULT_TIMEZONE = "GMT+8";

    public final static String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public final static String ISO_SHORT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public final static String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public final static String SHORT_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    public final static String FULL_SEQ_FORMAT = "yyyyMMddHHmmssSSS";

    public final static String[] MULTI_FORMAT = {DEFAULT_DATE_FORMAT, DEFAULT_TIME_FORMAT, ISO_DATE_TIME_FORMAT, ISO_SHORT_DATE_TIME_FORMAT,
            SHORT_TIME_FORMAT, "yyyy-MM"};

    public final static String FORMAT_YYYY = "yyyy";

    public final static String FORMAT_YYYYMM = "yyyyMM";

    public final static String FORMAT_YYYYMMDD = "yyyyMMdd";

    public final static String FORMAT_YYYYMMDDHH = "yyyyMMddHH";

    public final static String XIZHIMENDATE = "M.d";

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(date);
    }

    public static String formatDate(Date date, String format) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(format).format(date);
    }

    public static Integer formatDateToInt(Date date, String format) {
        if (date == null) {
            return null;
        }
        return Integer.valueOf(new SimpleDateFormat(format).format(date));
    }

    public static Long formatDateToLong(Date date, String format) {
        if (date == null) {
            return null;
        }
        return Long.valueOf(new SimpleDateFormat(format).format(date));
    }

    public static String formatTime(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(DEFAULT_TIME_FORMAT).format(date);
    }

    public static String formatShortTime(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(SHORT_TIME_FORMAT).format(date);
    }

    public static String formatDateNow() {
        return formatDate(DateUtil.currentDate());
    }

    public static String formatTimeNow() {
        return formatTime(DateUtil.currentDate());
    }

    public static Date parseDate(String date, String format) {
        if (date == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date parseTime(String date, String format) {
        if (date == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date parseDate(String date) {
        return parseDate(date, DEFAULT_DATE_FORMAT);
    }

    public static Date parseTime(String date) {
        return parseTime(date, DEFAULT_TIME_FORMAT);
    }

    public static String plusOneDay(String date) {
        DateTime dateTime = new DateTime(parseDate(date).getTime());
        return formatDate(dateTime.plusDays(1).toDate());
    }

    public static String plusOneDay(Date date) {
        DateTime dateTime = new DateTime(date.getTime());
        return formatDate(dateTime.plusDays(1).toDate());
    }

    public static String getHumanDisplayForTimediff(Long diffMillis) {
        if (diffMillis == null) {
            return "";
        }
        long day = diffMillis / (24 * 60 * 60 * 1000);
        long hour = (diffMillis / (60 * 60 * 1000) - day * 24);
        long min = ((diffMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long se = (diffMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        StringBuilder sb = new StringBuilder();
        if (day > 0) {
            sb.append(day + "D");
        }
        DecimalFormat df = new DecimalFormat("00");
        sb.append(df.format(hour) + ":");
        sb.append(df.format(min) + ":");
        sb.append(df.format(se));
        return sb.toString();
    }

    /**
     * 把类似2014-01-01 ~ 2014-01-30格式的单一字符串转换为两个元素数组
     */
    public static Date[] parseBetweenDates(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        date = date.replace("～", "~");
        Date[] dates = new Date[2];
        String[] values = date.split("~");
        dates[0] = parseMultiFormatDate(values[0].trim());
        dates[1] = parseMultiFormatDate(values[1].trim());
        return dates;
    }

    public static Date parseMultiFormatDate(String date) {
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(date, MULTI_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Title:getDiffDay
     * @Description:获取日期相差天数
     * @param:@param beginDate  字符串类型开始日期
     * @param:@param endDate    字符串类型结束日期
     * @param:@return
     * @return:Long 日期相差天数
     * @author:谢
     * @thorws:
     */
    public static Long getDiffDay(String beginDate, String endDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Long checkday = 0l;
        //开始结束相差天数
        try {
            checkday = (formatter.parse(endDate).getTime() - formatter.parse(beginDate).getTime()) / (1000 * 24 * 60 * 60);
        } catch (ParseException e) {

            e.printStackTrace();
            checkday = null;
        }
        return checkday;
    }

    /**
     * @Title:getDiffDay
     * @Description:获取日期相差天数
     * @param:@param beginDate Date类型开始日期
     * @param:@param endDate   Date类型结束日期
     * @param:@return
     * @return:Long 相差天数
     * @author: 谢
     * @thorws:
     */
    public static Long getDiffDay(Date beginDate, Date endDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strBeginDate = format.format(beginDate);

        String strEndDate = format.format(endDate);
        return getDiffDay(strBeginDate, strEndDate);
    }

    /**
     * N天之后
     *
     * @param n
     * @param date
     * @return
     */
    public static Date nDaysAfter(Integer n, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + n);
        return cal.getTime();
    }

    /**
     * N天之前
     *
     * @param n
     * @param date
     * @return
     */
    public static Date nDaysAgo(Integer n, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - n);
        return cal.getTime();
    }

    private static Date currentDate;

    public static Integer getWeekOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String yearString = formatDate(date, FORMAT_YYYY);
        int weekNum = c.get(Calendar.WEEK_OF_YEAR);
        if (weekNum < 10) {
            yearString = StringUtils.rightPad(yearString, 5, "0");
        }
        return Integer.valueOf(yearString + weekNum);
    }

    /**
     * 将HH:mm格式的字符串转换成分钟
     *
     * @param dateStr
     * @return
     */
    public static Integer timeToMinu(String dateStr) {
        Integer minute = null;
        String[] dates = dateStr.split(":");
        if (dates != null && dates.length == 2) {
            String hourStr = dates[0];
            String minuStr = dates[1];
            Integer hour = Integer.parseInt(hourStr);
            Integer minu = Integer.parseInt(minuStr);
            minute = hour * 60 + minu;
        }
        return minute;
    }

    /**
     * 将分钟转换为HH:mm字符串
     *
     * @param minu
     * @return
     */
    public static String minuToTime(Integer minu) {
        String time = null;
        Integer hour = minu / 60;
        Integer minute = minu % 60;
        String hourStr = "";
        String minuteStr = "";

        if (hour < 10) {
            hourStr = '0' + String.valueOf(hour);
        } else {
            hourStr = String.valueOf(hour);
        }

        if (minute < 10) {
            minuteStr = '0' + String.valueOf(minute);
        } else {
            minuteStr = String.valueOf(minute);
        }

        time = hourStr + ":" + minuteStr;

        return time;
    }


    /**
     * 将分钟转换为HH :mm字符串
     *
     * @param minu
     * @return
     */
    public static String minuToTime2(Integer minu) {
        String time = null;
        Integer hour = minu / 60;
        Integer minute = minu % 60;
        String hourStr = "";
        String minuteStr = "";

        if (hour < 10) {
            hourStr = '0' + String.valueOf(hour);
        } else {
            hourStr = String.valueOf(hour);
        }

        if (minute < 10) {
            minuteStr = '0' + String.valueOf(minute);
        } else {
            minuteStr = String.valueOf(minute);
        }

        time = hourStr + " :" + minuteStr;

        return time;
    }


    /**
     * 将格式23小时55分钟转换为分钟
     *
     * @param dateStr
     * @return
     */
    public static Integer strToMinu(String dateStr) {
        if (dateStr != null) {
            if (dateStr.contains("分钟")) {
                //23小时55分钟
                String hour = dateStr.substring(0, dateStr.indexOf("小时"));
                String minu = dateStr.substring(dateStr.indexOf("小时") + 2, dateStr.indexOf("分钟"));
                Integer minute = Integer.parseInt(hour) * 60 + Integer.parseInt(minu);
                return minute;
            } else {
                //0小时
                String hour = dateStr.substring(0, dateStr.indexOf("小时"));
                Integer minute = Integer.parseInt(hour) * 60;
                return minute;
            }
        }
        return null;
    }

    /**
     * 将分钟转换为格式23小时23分钟
     *
     * @param minu
     * @return
     */
    public static String minuToStr(Integer minu) {
        String dateStr = "";
        if (minu % 60 == 0) {
            //整小时
            dateStr = String.valueOf(minu / 60) + "小时";
        } else {
            //有小时有分钟
            dateStr = String.valueOf(minu / 60) + "小时" + String.valueOf(minu % 60) + "分钟";
        }
        return dateStr;
    }


    /**
     * 将字符串小时转换为时间
     *
     * @param hourStr
     * @return
     */
    public static Integer hourToMinu(String hourStr) {
        double hour = Double.parseDouble(hourStr);
        return (int) (hour * 60);
    }


    /**
     * 将分钟转换为字符串
     *
     * @param minu
     * @return
     */
    public static String minuToHour(Integer minu) {
        return String.valueOf(minu * 1.0 / 60);
    }

    public static Date setCurrentDate(Date date) {
        if (date == null) {
            currentDate = null;
        } else {
            currentDate = date;
        }
        return currentDate;
    }

    /**
     * 将分成30分钟一份的数字开头转换为HH:mm
     *
     * @param start
     * @return
     */
    public static String startMinuToTime(Integer start) {
        return minuToTime(start * 30);
    }

    /**
     * 将分成30分钟一份的数字结尾转换为HH:mm
     *
     * @param end
     * @return
     */
    public static String endMinuToTime(Integer end) {
        return minuToTime((end + 1) * 30);
    }

    /**
     * 为了便于在模拟数据程序中控制业务数据获取到的当前时间
     * 提供一个帮助类处理当前时间，为了避免误操作，只有在devMode开发模式才允许“篡改”当前时间
     *
     * @return
     */
    public static Date currentDate() {
        if (currentDate == null) {
            return new Date();
        }
        if (DynamicConfigService.isDevMode()) {
            return currentDate;
        } else {
            return new Date();
        }
    }

    public static void main(String[] args) {
        String str = "23小时55分钟";
        System.out.println(strToMinu(str));

        Integer i = 139;

        System.out.println(minuToTime(i));

        //String str = "02:19";
        //System.out.println(timeToMinu(str));

        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        System.out.println(DateUtil.formatDate(c.getTime(), DateUtil.FORMAT_YYYYMMDD));

        DateUtil.formatDate(new Date(), DEFAULT_DATE_FORMAT);

        Pattern p = Pattern.compile("(\\d{4})-(\\d{1,2})-(\\d{1,2})");
        Matcher m = p.matcher(DateUtil.formatDate(new Date(), DEFAULT_DATE_FORMAT));

        if (m.find()) {
            System.out.println("日期:" + m.group());
            System.out.println("年:" + m.group(1));
            System.out.println("月:" + m.group(2));
            System.out.println("日:" + m.group(3));
        }

        String time = "2015/11/02 ";

        System.out.println(parseDate(time, "yyyy/MM/dd"));
    }


    /**
     * 获取某月的最后一天 月份从0开始
     *
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
        return new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
    }

    /**
     * 获取某月的第一天 月份从0开始
     *
     * @param year
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
        return new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
    }
}
