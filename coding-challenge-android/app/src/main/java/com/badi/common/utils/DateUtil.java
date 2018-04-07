/*
 * File: DateUtil.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.utils;

import android.content.Context;

import com.badi.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Date Util for converting the datetime to ISO8601 & other utilities related
 */
public class DateUtil {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    private static final SimpleDateFormat dateFormatHours = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    public static String getISO8601Date(long timestamp) {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        //Time zone formats available to SimpleDateFormat (Java 6 and earlier) are not ISO 8601 compliant.
        return dateFormat.format(new Date(timestamp)) + "Z";
    }

    public static boolean roomIsAvailable(Date roomAvailableDate) {
        return getDateDiff(new Date(), roomAvailableDate, TimeUnit.SECONDS) < 0;
    }

    public static boolean isYesterday(Date date) {
        Calendar now = Calendar.getInstance();
        Calendar cDate = Calendar.getInstance();
        cDate.setTime(date);

        now.add(Calendar.DATE, -1);

        return now.get(Calendar.YEAR) == cDate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cDate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cDate.get(Calendar.DATE);
    }

    /**
     * Checks if two dates are of the same day.
     * @param dateFirst   The time of the first date.
     * @param dateSecond  The time of the second date.
     * @return  Whether {@param dateFirst} and {@param dateSecond} are off the same day.
     */
    public static boolean hasSameDate(Date dateFirst, Date dateSecond) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return dateFormat.format(dateFirst).equals(dateFormat.format(dateSecond));
    }

    /**
     * Get a diff between two dates
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static DateFormat getDateFormatWithLocale() {
        return DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
    }

    public static DateFormat getDefaultDateFormatWithLocale() {
        return DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());
    }

    public static DateFormat getShortDateFormatWithLocale() {
        return DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
    }

    /**
     * Get a diff years between two dates
     * @param first the oldest date
     * @param last the newest date
     * @return the diff value as int
     */
    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    /**
     * Get a diff hour between two dates
     * @param first the oldest date
     * @param last the newest date
     * @return the diff value as int
     */
    public static int getDiffHours(Date first, Date last) {
        long diff = last.getTime() - first.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        return (int) hours;
    }

    private static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    public static String getRoomDateRepresentation(Context context, Date date) {
        String dateRepresentation;
        int daysBetween = daysBetween(date, new Date());
        if (daysBetween < 7)
            dateRepresentation = context.getString(R.string.date_room_this_week);
        else
            dateRepresentation = context.getString(R.string.date_room_over_a_week_ago);
        return dateRepresentation;
    }

    public static String getRoomDaysRepresentation(Context context, Integer days) {
        if (days != null) {
            String dateRepresentation;
            Date startDate = new Date();
            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(startDate);
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(startDate);
            endCalendar.add(Calendar.DATE, days);

            int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

            if (diffYear == 0) {
                if (diffMonth == 0) {
                    // Days
                    if (days == 1) {
                        dateRepresentation = context.getString(R.string.date_room_day);
                    } else {
                        if (days < 7) {
                            dateRepresentation = String.format(context.getString(R.string.date_room_days), days);
                        } else if (days < 14) {
                            dateRepresentation = context.getString(R.string.date_room_week);
                        } else {
                            dateRepresentation = String.format(context.getString(R.string.date_room_weeks), days / 7);
                        }
                    }
                } else {
                    // One month or more
                    if (diffMonth == 1) {
                        dateRepresentation = context.getString(R.string.date_room_month);
                    } else {
                        dateRepresentation = String.format(context.getString(R.string.date_room_months), diffMonth);
                    }
                }
            } else {
                if (diffMonth == 0) {
                    // Days
                    if (days == 1) {
                        dateRepresentation = context.getString(R.string.date_room_day);
                    } else {
                        if (days < 7) {
                            dateRepresentation = String.format(context.getString(R.string.date_room_days), days);
                        } else if (days < 14) {
                            dateRepresentation = context.getString(R.string.date_room_week);
                        } else {
                            dateRepresentation = String.format(context.getString(R.string.date_room_weeks), days / 7);
                        }
                    }
                } else {
                    // One month or more
                    if (diffMonth == 1) {
                        dateRepresentation = context.getString(R.string.date_room_month);
                    } else if (diffMonth < 12) {
                        dateRepresentation = String.format(context.getString(R.string.date_room_months), diffMonth);
                    } else {
                        //1 year or more
                        dateRepresentation = context.getString(R.string.date_room_year);
                    }
                }
            }
            return dateRepresentation;
        } else {
            return "";
        }
    }

    private static int daysBetween(Date d1, Date d2) {
        return (int) ( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
}
