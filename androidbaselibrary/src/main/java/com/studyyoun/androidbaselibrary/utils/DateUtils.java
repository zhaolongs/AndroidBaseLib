package com.studyyoun.androidbaselibrary.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by androidlongs on 17/3/15.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

public class DateUtils {
    private DateUtils() {

    }


    private static class SingleDateUtil {
        private static DateUtils sDateUtils = new DateUtils();
    }

    public static DateUtils getInstance() {
        return SingleDateUtil.sDateUtils;
    }

    /**
     * 获取Calendar
     */
    private Calendar getCalendar() {
        return Calendar.getInstance();
    }

    /**
     * 获取Date
     */
    private Date getDate() {
        return new Date();
    }


    //—————————— —————————— —————————— —————————— —————————— —————————— —————————— —————————— ——————————

    public static String DEFAULT_DATE_FORMAT = "yyyy年MM月dd日";
    public static String DEFAULT_TIME_FORMAT = "yyyy年MM月dd日 HH : mm : ss";

    public static String DEFAULT_DATE_FORMAT2 = "yyyy-MM-dd";
    public static String DEFAULT_TIME_FORMAT2 = "yyyy-MM-dd HH : mm : ss";

    //—————————— —————————— —————————— —————————— —————————— —————————— —————————— —————————— ——————————


    /**
     * 格式化时间
     *
     * @param time long类型时间
     */
    public String formatTime(long time) {
        return formatTime(time, null);
    }

    /**
     * 格式化时间
     *
     * @param time   long类型时间
     * @param format 格式
     */
    public String formatTime(long time, String format) {
        if (TextUtils.isEmpty(format)) {
            format = DEFAULT_DATE_FORMAT;
        }
        //获取Date
        Date date = null;
        try {
            date = getDate();

            //设置时间
            date.setTime(time);
            //设置格式
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            //格式化时间
            String timeString = simpleDateFormat.format(date);
            return timeString;
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    //—————————— —————————— —————————— —————————— —————————— —————————— —————————— —————————— ——————————

    /**
     * 解析 String类型时间
     *
     * @param time String类型时间
     */
    public long pareTime(String time) {
        return pareTime(time, null);
    }

    /**
     * 解析 String类型时间
     *
     * @param time   String类型时间
     * @param format 解析格式
     */
    public long pareTime(String time, String format) {
        if (TextUtils.isEmpty(format)) {
            format = DEFAULT_DATE_FORMAT;
        }
        try {
            //设置格式
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            //格式化时间
            Date timeLongDate = simpleDateFormat.parse(time);
            return timeLongDate.getTime();
        } catch (Exception e) {
            return 0;
        }

    }

//—————————— —————————— —————————— —————————— —————————— —————————— —————————— —————————— ——————————

    //获取当前的时间
    public String getCurrentTime() {
        return getCurrentTime(null);
    }

    /**
     * 获取当前的时间
     *
     * @param formatString 时间格式
     */
    public String getCurrentTime(String formatString) {
        if (TextUtils.isEmpty(formatString)) {
            formatString = DEFAULT_DATE_FORMAT;
        }
        //创建Date
        Date date = getDate();
        //格式化
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        return format.format(date);
    }

    /**
     * 获取当前的时间
     *
     * @return long 毫秒
     */
    public long getCurrentTimes() {
        return getDate().getTime();
    }
//—————————— —————————— —————————— —————————— —————————— —————————— —————————— —————————— ——————————

    public int getCurrentYear() {
        return getCurrentYear(0);
    }

    public int getCurrentYear(int y) {
        //获取Calendar
        Calendar calendar = getCalendar();
        //设置
        calendar.add(Calendar.YEAR, y);

        return calendar.get(Calendar.YEAR);
    }

    public int getCurrentMon() {
        return getCurrentMon(0);
    }

    public int getCurrentMon(int m) {
        //获取Calendar
        Calendar calendar = getCalendar();
        //设置
        calendar.add(Calendar.MONTH, m);

        return calendar.get(Calendar.MONTH);
    }

    /**
     * 获取 日
     *
     * @return int 日
     */
    public int getCurrentDay() {
        return getCurrentDay(0);
    }

    /**
     * 获取 日
     *
     * @param d 在当前日期上添加的天数
     * @return int 日
     */
    public int getCurrentDay(int d) {
        //获取Calendar
        Calendar calendar = getCalendar();
        //设置
        calendar.add(Calendar.DAY_OF_MONTH, d);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    //—————————— —————————— —————————— —————————— —————————— —————————— —————————— —————————— ——————————


    public String formatTimeFunction(long time) {
        String result = "00:00";
        if (time > 0) {
            long mil = time / 1000 % 60;
            long send = time / 1000 / 60 % 60;
            long hours = time / 1000 / 60 / 60 % 60;

            StringBuilder stringBuilder = new StringBuilder();

            if (hours < 10 && hours > 0) {
                stringBuilder.append("0");
                stringBuilder.append(hours);
                stringBuilder.append(":");
            } else if (hours > 10) {
                stringBuilder.append(hours);
                stringBuilder.append(":");
            }

            if (send < 10 && send >= 0) {
                stringBuilder.append("0");
                stringBuilder.append(send);
                stringBuilder.append(":");
            } else if (send > 10) {
                stringBuilder.append(send);
                stringBuilder.append(":");
            }

            if (mil < 10 && mil >= 0) {
                stringBuilder.append("0");
                stringBuilder.append(mil);
            } else if (mil > 10) {
                stringBuilder.append(mil);
            }

            return  stringBuilder.toString();

        }
        return result;
    }

    public String commonFormationTime(String format) {
        if (format != null && !format.equals("")) {
            String formatTime = formatTime(Long.valueOf(format), "HH");
            long valueOf = Long.valueOf(formatTime);
            if (valueOf >= 0 && valueOf <= 6) {
                formatTime = "凌晨";
            } else if (valueOf > 6 && valueOf <= 8) {
                formatTime = "早晨";
            } else if (valueOf > 8 && valueOf <= 12) {
                formatTime = "上午";
            } else if (valueOf > 12 && valueOf <= 14) {
                formatTime = "中午";
            } else if (valueOf > 14 && valueOf <= 19) {
                formatTime = "下午";
            } else if (valueOf > 19 && valueOf <= 23) {
                formatTime = "晚上";
            } else {
                formatTime = "凌晨";
            }

            String formatTime2 = formatTime(Long.valueOf(format), "yyyy年MM月dd日");
            String formatTime3 = formatTime(Long.valueOf(format), "HH:mm:ss");
            format = formatTime2 + " " + formatTime + " " + formatTime3;
        } else {
            format = "时间数据异常";
        }

        return format;
    }



    /**
     *
     * @doc 获取日期间的日期
     * @param start
     *            开始日期
     * @param end
     *            结束日期
     * @return List集合
     * @author lzy
     * @history 2017年10月17日 上午9:55:04 Create by 【lzy】
     */
    private  List<Date> getBetweenDates(Date start, Date end) {
        List<Date> result = new ArrayList<Date>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        //添加或减去指定的时间给定日历领域，基于日历的规则。例如，从日历当前的时间减去5天，您就可以通过
        tempStart.add(Calendar.DAY_OF_YEAR, 0);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        tempEnd.add(Calendar.DAY_OF_YEAR, 1);
        while (tempStart.before(tempEnd)) {
            result.add(tempStart.getTime());
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;
    }


    /**
     *
     * @doc 日期转换星期几
     * @param datetime
     *            日期 例:2017-10-17
     * @return String 例:星期二
     * @author lzy
     * @history 2017年10月17日 上午9:55:30 Create by 【lzy】
     */
    public  String dateToWeek(String datetime) {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        return dateToWeek(datetime,weekDays);
    }
    public  String dateToWeekEn(String datetime) {
        String[] weekDays = { "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat" };
        return dateToWeek(datetime,weekDays);
    }
    public  String dateToWeek2(String datetime) {
        String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
     return dateToWeek(datetime,weekDays);
    }

    private   String dateToWeek(String datetime, String[] weekDays) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public  List<Date> getTimeLengthFunction(int startYear,int startMonth,int endYear,int endMonth){
        return getTimeLengthFunction(startYear+"-"+startMonth+"-01",endYear+"-"+endMonth+"-31");
    }
    public  List<Date> getTimeLengthFunction(int startYear,int endYear){
        return getTimeLengthFunction(startYear+"-01-01",endYear+"-12-31");
    }

    public List<Date> getTimeLengthFunction(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return getBetweenDates(sdf.parse(startTime), sdf.parse(endTime));
        } catch (ParseException lE) {
            lE.printStackTrace();
            return null;
        }
    }
    public static String ms2Date(long ms){
        Date date = new Date();
        date.setTime(ms);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return format.format(date);
    }
}
