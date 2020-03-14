package com.fly.util;


import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class EpointDateUtil {
    public static final long DAY_IN_MILLISECOND = 86400000L;
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_NOSPLIT_FORMAT = "yyyyMMdd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_MILLI_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public static Date intToDate(int dateint) {
        SimpleDateFormat dd = new SimpleDateFormat("yyyyMMdd");
        Date date = null;

        try {
            date = dd.parse(dateint + "");
        } catch (ParseException arg3) {
            arg3.printStackTrace();
        }

        return date;
    }

    public static Date convertString2Date(String datestr) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;

        try {
            date = dateformat.parse(datestr);
        } catch (ParseException arg3) {
            arg3.printStackTrace();
        }

        return date;
    }

    public static Date convertString2DateAuto(String datestr) {
        Date date = null;
        if (StringUtil.isNotBlank(datestr)) {
            if (datestr.indexOf(" CST ") != -1) {
                SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss \'CST\' yyyy", Locale.US);

                try {
                    date = format.parse(datestr);
                } catch (ParseException arg6) {
                    arg6.printStackTrace();
                }
            } else {
                datestr = datestr.trim();
                datestr = datestr.toString().replace("T", " ");
                String format1 = "yyyyMMdd";
                boolean haveTime = false;
                if (datestr.indexOf(":") != -1) {
                    haveTime = true;
                    String[] dateformat = datestr.split(":");
                    if (dateformat.length == 2) {
                        datestr = datestr + ":00";
                    }
                }

                if (datestr.indexOf("-") != -1) {
                    format1 = "yyyy-MM-dd";
                } else if (datestr.indexOf("/") != -1) {
                    format1 = "yyyy/MM/dd";
                } else if (datestr.indexOf("年") != -1 || datestr.indexOf("月") != -1 || datestr.indexOf("日") != -1) {
                    datestr = datestr.replaceAll("年", "-");
                    datestr = datestr.replaceAll("月", "-");
                    datestr = datestr.replaceAll("日", "");
                    format1 = "yyyy-MM-dd";
                }

                if (haveTime) {
                    format1 = format1 + "HH:mm:ss";
                }

                SimpleDateFormat dateformat1 = new SimpleDateFormat(format1);

                try {
                    date = dateformat1.parse(datestr);
                } catch (ParseException arg5) {
                    arg5.printStackTrace();
                }
            }
        }

        return date;
    }

    public static Date convertString2Date(String str, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date = null;

        try {
            date = format.parse(str);
        } catch (ParseException arg4) {
            arg4.printStackTrace();
        }

        return date;
    }

    public static String convertDate2String(Date date) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat formatter = null;

            try {
                formatter = new SimpleDateFormat("yyyy-MM-dd");
                return formatter.format(date);
            } catch (Exception arg2) {
                formatter = new SimpleDateFormat("yyyy-MM-dd");
                return formatter.format(date);
            }
        }
    }

    public static String convertDate2String(Date date, String format) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat formatter = null;

            try {
                formatter = new SimpleDateFormat(format);
                return formatter.format(date);
            } catch (Exception arg3) {
                formatter = new SimpleDateFormat("yyyy-MM-dd");
                return formatter.format(date);
            }
        }
    }

    public static Date getBeginOfDateStr(String datestr) {
        Date result = null;
        if (StringUtil.isNotBlank(datestr)) {
            datestr = datestr.trim();
            if (datestr.indexOf(":") != -1) {
                int endIndex = datestr.lastIndexOf(" ");
                datestr = datestr.substring(0, endIndex);
            }

            result = getCombinationDateByStr(datestr, " 00:00:00");
        }

        return result;
    }

    public static Date getBeginOfDate(Date date) {
        return getCombinationDate(date, " 00:00:00");
    }

    public static Date getEndOfDate(Date date) {
        return getCombinationDate(date, " 23:59:59");
    }

    public static Date getEndOfDateStr(String datestr) {
        Date result = null;
        if (StringUtil.isNotBlank(datestr)) {
            datestr = datestr.trim();
            if (datestr.indexOf(":") != -1) {
                int endIndex = datestr.lastIndexOf(" ");
                datestr = datestr.substring(0, endIndex);
            }

            result = getCombinationDateByStr(datestr, " 23:59:59");
        }

        return result;
    }

    public static Date getCombinationDate(Date date, String condition) {
        if (date == null) {
            date = new Date();
        }

        return getCombinationDateByStr(convertDate2String(date), condition);
    }

    private static Date getCombinationDateByStr(String date, String condition) {
        Date outDate = null;

        try {
            String e = date + condition;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            outDate = dateFormat.parse(e);
        } catch (Exception arg4) {
            arg4.printStackTrace();
        }

        return outDate;
    }

    public static int getYearOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(1);
    }

    public static int getMonthOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(2);
    }

    public static int getDayOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(5);
    }

    public static int getHourOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(10);
    }

    public static int getHourOfDate24(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(11);
    }

    public static int getMinuteOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(12);
    }

    public static int getSecondOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(13);
    }

    public static Date getBeforeTimeByMillis(long millis) {
        Date rntDate = new Date();
        rntDate.setTime(Calendar.getInstance().getTimeInMillis() - millis);
        return rntDate;
    }

    public static Date convertLongToDate(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return c.getTime();
    }

    public static boolean isLeapYear(String ddate) {
        Date d = convertString2DateAuto(ddate);
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(d);
        int year = gc.get(1);
        return year % 400 == 0 ? true : (year % 4 == 0 ? year % 100 != 0 : false);
    }

    public static String getEndDateOfMonth(String dat) {
        Date date = convertString2DateAuto(dat);
        int mon = getMonthOfDate(date) + 1;
        boolean day = false;
        byte day1;
        if (mon != 1 && mon != 3 && mon != 5 && mon != 7 && mon != 8 && mon != 10 && mon != 12) {
            if (mon != 4 && mon != 6 && mon != 9 && mon != 11) {
                if (isLeapYear(dat)) {
                    day1 = 29;
                } else {
                    day1 = 28;
                }
            } else {
                day1 = 0;
            }
        } else {
            day1 = 31;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(5, day1);
        return convertDate2String(calendar.getTime(), "yyyy-MM-dd HH:mm:ss");
    }

    public static Date addDay(Date date, int day) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(6, day);
        return calendar.getTime();
    }

    public static Date addSeconds(Date date, int seconds) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(13, seconds);
        return calendar.getTime();
    }

    public static Date addMinute(Date date, int minute) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(12, minute);
        return calendar.getTime();
    }

    public static Date addMonth(Date date, int months) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(2, months);
        return calendar.getTime();
    }

    public static Date addYear(Date date, int years) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(1, years);
        return calendar.getTime();
    }

    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(5, now.get(5) - day);
        return now.getTime();
    }

    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(5, now.get(5) + day);
        return now.getTime();
    }

    public static int compareDateOnDay(Date d1, Date d2) {
        return d1.compareTo(d2);
    }

    public static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        int dayOfWeek = cd.get(7) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }

        return dayOfWeek == 1 ? 0 : 1 - dayOfWeek;
    }

    public static Date getMondayOfWeek() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(5, mondayPlus);
        Date monday = currentDate.getTime();
        return monday;
    }

    public static Date getSundayOfWeek() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(5, mondayPlus + 6);
        Date sunday = currentDate.getTime();
        return sunday;
    }

    public static String getCurrentDate() {
        return convertDate2String(new Date());
    }

    public static String getCurrentDate(String format) {
        Date today = new Date();
        SimpleDateFormat formatter = null;

        try {
            formatter = new SimpleDateFormat(format);
            return formatter.format(today);
        } catch (Exception arg3) {
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(today);
        }
    }

    public static String changeDateRangeToString(Long timelong) {
        if (timelong == null) {
            return "";
        } else {
            long d = timelong.longValue() / 86400000L;
            long h = timelong.longValue() % 86400000L / 3600000L;
            long m = timelong.longValue() % 3600000L / 60000L;
            double s = timelong.doubleValue() % 60000.0D / 1000.0D;
            return (d != 0L ? d + "天" : "") + (h != 0L ? h + "小时" : "") + (m != 0L ? m + "分" : "")
                    + (s != 0.0D ? s + "秒" : "");
        }
    }

    public static String getCHSWeekNameByDayOfWeek(int weekNum) {
        switch (weekNum) {
            case 0 :
                return "星期天";
            case 1 :
                return "星期一";
            case 2 :
                return "星期二";
            case 3 :
                return "星期三";
            case 4 :
                return "星期四";
            case 5 :
                return "星期五";
            case 6 :
                return "星期六";
            default :
                return " ";
        }
    }

    public static boolean isSameDate(Date d1, Date d2) {
        boolean result = false;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        if (c1.get(1) == c2.get(1) && c1.get(2) == c2.get(2) && c1.get(5) == c2.get(5)) {
            result = true;
        }

        return result;
    }

    public static int getIntervalDays(Date startday, Date endday) {
        if (startday.after(endday)) {
            Date sl = startday;
            startday = endday;
            endday = sl;
        }

        long sl1 = startday.getTime();
        long el = endday.getTime();
        long ei = el - sl1;
        return (int) (ei / 86400000L);
    }

    public static java.sql.Date getSqlDate(String dateStr) {
        try {
            return new java.sql.Date(convertString2DateAuto(dateStr).getTime());
        } catch (Exception arg1) {
            return null;
        }
    }

    public static Time getSqlTime(String dateStr) {
        try {
            return new Time(convertString2DateAuto(dateStr).getTime());
        } catch (Exception arg1) {
            return null;
        }
    }

    public static Timestamp getSqlTimestamp(String dateStr) {
        try {
            return new Timestamp(convertString2DateAuto(dateStr).getTime());
        } catch (Exception arg1) {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(convertDate2String(getMondayOfWeek()));
        System.out.println(convertDate2String(getSundayOfWeek()));
    }
}
