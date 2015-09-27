package net.bingyan.hustpass.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;

public class HustUtils {
//    AppLog mLog = new AppLog(getClass());
    // final static Calendar SECOND_SEMESTER =new
    // GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), 2-1,
    // 24);//(2013, 2-1, 28);//(2012, 2-1, 10);
    public final static int CLASSTIME[][] = new int[][]{
            // 上課,下課,上課,下課
            {60 * 8, 45, 10, 45}, {60 * 10 + 10, 45, 10, 45},

            {60 * 14, 45, 5, 45}, {60 * 15 + 55, 45, 5, 45},

            {60 * 18 + 30, 45, 5, 45}, {60 * 20 + 15, 45, 5, 45}

    };

    public static boolean isWinter() {
        Calendar now = Calendar.getInstance();
        int doy = now.get(Calendar.DAY_OF_YEAR);
        if (doy <= 121 || doy >= 275)
            return true;
        else
            return false;

    }

    /**
     * 負值即該節下課後
     *
     * @warnning 負值小數 @ *
     */
    public static float getCrouseNum() {
        Calendar now = Calendar.getInstance();
        float minuteofday = now.get(Calendar.HOUR_OF_DAY) * 60
                + now.get(Calendar.MINUTE);
        int summeroffset = (HustUtils.isWinter() ? 0 : 30);

        int t;
        for (int i = 0; i < 6; i++) {
            t = i > 1 ? summeroffset : 0;
            for (int j = 0; j < 3; j++) {
                t += CLASSTIME[i][j];
                if (minuteofday >= t && minuteofday < t + CLASSTIME[i][j + 1]) {
                    return ((j == 1) ? -1 : 1)
                            * (1 + i * 2 + (j == 2 ? 1 : 0) + ((float) (minuteofday - t) / ((j == 1) ? 10
                            : 45)));
                }
            }
            if (i == 5)
                return -12;
            else if (minuteofday >= t + 45
                    && minuteofday < CLASSTIME[i + 1][0]
                    + (i + 1 > 1 ? summeroffset : 0))
                return -(i + 1) * 2;
        }
        return 0;
    }

    public static String getCrouseName() {
        int crouseNum = (int) HustUtils.getCrouseNum();
        if (crouseNum == -12)
            return "";
        else if (crouseNum > 0)
            return "第" + String.valueOf(crouseNum) + "节课";
        else
            return "第" + String.valueOf(-(crouseNum)) + "节课";
    }

    public static int getCrouseFlag() {
        int crouseNum = (int) HustUtils.getCrouseNum();
        if (crouseNum == -12)
            return 1;
        else if (crouseNum > 0)
            return 1 << (crouseNum - 1);
        else
            return 1 << (-(crouseNum) + 1);
    }

    public static boolean isAfterClass() {
        return getCrouseNum() < 0;
    }

    /**
     * 取得下次上課或下課時間
     *
     * @return timestamp
     */
    public static long getNextCrouseChangeTime() {
        long dayOfYearMillis = System.currentTimeMillis() / 86400000 * 86400000
                - 8 * 3600000;// with GMT+8 fix
        long summeroffset = (HustUtils.isWinter() ? 0 : 30) * 1000 * 60;
        long currentTimeMillis = System.currentTimeMillis();

        long t;
        for (int i = 0; i < 6; i++) {
            t = dayOfYearMillis + (i > 1 ? summeroffset : 0);
            for (int j = 0; j < 4; j++) {
                t += CLASSTIME[i][j] * 60000;
                if (currentTimeMillis < t) {
                    return t;
                }
            }

        }

        return dayOfYearMillis + 86400000 + 8 * 3600000; // next day 8:00

    }

    public static boolean isFirstSemester() {
        Calendar now = Calendar.getInstance();
        int doy = now.get(Calendar.DAY_OF_YEAR);
        if (doy <= getSecondSemesterStartDay().get(Calendar.DAY_OF_YEAR)
                || doy >= 240)
            return true;
        else
            return false;
    }

    /**
     * 春节过后再放假15天
     */
    private static Calendar getSecondSemesterStartDay() {
        Calendar rightNow = Calendar.getInstance();
        int y = rightNow.get(Calendar.YEAR);
        Date date = DateBean.getGregorianCalendar(y + "-01-01");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 15);

        int weekday = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (weekday < 3)
            c.add(Calendar.DAY_OF_MONTH, -weekday);
        else
            c.add(Calendar.DAY_OF_MONTH, (7 - weekday));
        return c;
    }

    /**
     * @return this year's SemesterStartDay in timestamp 9月1号开学
     */
    public static long getSemesterStartDay() {
        // TODO 可能有問題
        Calendar rightNow = Calendar.getInstance();
        int y = rightNow.get(Calendar.YEAR);
        Calendar date;
        if (isFirstSemester()) {
            date = new GregorianCalendar(
                    rightNow.get(Calendar.MONTH) < 3 ? y - 1 : y, 9 - 1, 1);
            int weekday = date.get(Calendar.DAY_OF_WEEK) - 1;
//            AppLog.appv("date:"+date.getTime());
//            AppLog.appv("M:"+date.get(Calendar.MONTH));
//            AppLog.appv("D:"+date.get(Calendar.DAY_OF_WEEK));
//            date.get(Calendar.WEEK_OF_YEAR);
//            AppLog.appv("weekday"+weekday);
            if (weekday < 3)
                return date.getTime().getTime() - weekday * 86400000;
            else
                return date.getTime().getTime() + (7 - weekday) * 86400000;
        } else {
            date = getSecondSemesterStartDay();
        }

        return date.getTime().getTime();

        // Calendar date = new GregorianCalendar(2012, 2-1, 12);
        // return
        // date.getTime().getTime()-(date.get(Calendar.DAY_OF_WEEK)-1)*86400000;
    }

    public static int getWeekNum() {
        AppLog.appv("weeknum+"+getWeekNum(System.currentTimeMillis()));

        if (isFirstSemester()) {
            Calendar rightNow = Calendar.getInstance();
            int y = rightNow.get(Calendar.YEAR);
            Calendar date;
            date = new GregorianCalendar(
                    rightNow.get(Calendar.MONTH) < 3 ? y - 1 : y, 9 - 1, 1);

            int week = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) - date.get(Calendar.WEEK_OF_YEAR)+1;
            if(week<=0){
                return 24;
            }
            return week;

        }

        return getWeekNum(System.currentTimeMillis());
    }

    public static int getWeekNum(long timestampMills) {
        AppLog.appv("startday"+getSemesterStartDay());
        return (int) ((timestampMills - getSemesterStartDay()) / (86400000 * 7)) + 1 ;
    }

    public static int getDayNum() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static int getWeekFlag(int week) {
        return 1 << (week - 1);
    }

    public static int getWeekFlag() {
        return getWeekFlag(HustUtils.getWeekNum());
    }

    public static String getWeekName() {
        if (HustUtils.getWeekNum() < 22)
            return "第" + HustUtils.getWeekNum() + "周";
        else if (isWinter())
            return "寒假";
        else
            return "暑假";
    }

    public static int getTerm() {
        Calendar now = Calendar.getInstance();
        int y = now.get(Calendar.YEAR);
        if (now.get(Calendar.MONTH) < Calendar.AUGUST)
            y--;
        return y * 10 + (isFirstSemester() ? 1 : 2);
    }

    public static String numFlag2String(int flags) {
        return numFlag2String(null, 0, flags);
    }

    public static String numFlag2String(Context ctx, int resId, int flags) {
        int n = (int) (Math.log(flags) / Math.log(2)) + 1;
        String result = "";
        for (int i = 0; i < n; i++) {
            if (result.length() > 0)
                result += ',';
            while (i < n && (flags & (1 << i)) == 0) {
                i++;
            }
            result += String.valueOf(i + 1);
            if ((flags & (1 << i + 1)) == 0)
                continue;
            result += '~';
            while (i < n && (flags & (1 << ++i + 1)) != 0)
                ;
            result += String.valueOf(i + 1);
        }
        if (ctx == null)
            return result;
        else
            return ctx.getString(resId, result);

    }


    /**
     * *****2.1**********
     */
    public static String getHomeClasstime() {
        String text="";
        String sFormat = new SimpleDateFormat("EEEE")
                .format(new java.util.Date(System.currentTimeMillis()));

        if (isAfterClass()) {
            if (getCrouseNum() == -12)
                text = getWeekName() + " " + sFormat + " " + "没课";
            else if (HustUtils.getCrouseNum() == -4) {
                text = getWeekName() + " " + sFormat + " " + "午休";
            } else {
                text = getWeekName() + " " + sFormat + " " + "下课";
            }
        } else {
            text = getWeekName() + " " + sFormat + " " + getCrouseName();
        }

        return text;
    }
}
