package com.pt_plus.Utils;


import com.pt_plus.cons.AppCons;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AppDateUtils {
    private static AppDateUtils _self;

    private AppDateUtils() {
    }

    public static AppDateUtils getInstance() {
        if (_self == null) {
            _self = new AppDateUtils();
        }
        return _self;
    }

   /* public String convertISTtoGMT(String ist_date) {
        try {
            Date date = convertStringToDate(ist_date, AppCons.YYYY_MM_DD_HH_MM_FORMAT);

            SimpleDateFormat gmtFormat = new SimpleDateFormat(AppCons.YYYY_MM_DD_HH_MM_FORMAT);

            TimeZone gmtTime = TimeZone.getTimeZone("GMT");
            gmtFormat.setTimeZone(gmtTime);

            String gmt_time = gmtFormat.format(date);
            ;

            AppLogger.log(">>>>> Convert IST to GMT <<<<<");
            AppLogger.log("IST Time : " + ist_date);
            AppLogger.log("GMT Time : " + gmt_time);

            return gmt_time;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }*/

    public Date convertStringToDateIst(String dateStr, final String dateFormat) {
        Date timestamp = null;
        try {
            DateFormat dateFormatObj = new SimpleDateFormat(dateFormat);
            timestamp = dateFormatObj.parse(dateStr);


            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("GMT"));
            cal.setTime(timestamp);
            cal.add(Calendar.HOUR, 5);
            cal.add(Calendar.MINUTE, 30);

            timestamp = cal.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    public boolean isValidDateSelected(String input) {
        try {
            return !(input.toLowerCase().contains("date") || input.toLowerCase().contains("time"));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getCurrentDateAndTime(String format) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat(format);
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public Date convertStringToDate(String dateStr, final String dateFormat) {
        Date date = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            date = formatter.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }

    public String convertDateToStr(Date date, String format) {
        String temp = null;
        try {
            Locale usLocale = Locale.US;
            SimpleDateFormat formatter = new SimpleDateFormat(format,usLocale);
            temp = formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public Date getDateFromCurrent(int diffDay, int diffMonth, int diffYear) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        if (diffDay != 0) {
            cal.add(Calendar.DATE, diffDay);
        }
        if (diffMonth != 0) {
            cal.add(Calendar.MONTH, diffMonth);
        }
        if (diffYear != 0) {
            cal.add(Calendar.YEAR, diffYear);
        }

        Date d = cal.getTime();
        //AppLog.log("date from current "+d.toString());
        return d;
    }


    public String convert24hrTimeTo12hr(String _24HourTime) {
        String _12HourTime = null;
        try {
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            _12HourTime = _12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _12HourTime;
    }


    public static String convertDateToOtherFormat(String dateStr, String inputPattern, String outputPattern) {
        if (dateStr != null && dateStr.length() > 0) {
            SimpleDateFormat inFormat = new SimpleDateFormat(inputPattern);
            Date date = null;
            try {
                date = inFormat.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat outFormat = new SimpleDateFormat(outputPattern);

            return outFormat.format(date);
        } else {
            return "---";
        }
    }

    //=========================== date convertion to display in list like Jan 12th, 8.00 AM, 2019 Jan etc..


    private String dayNameBy(int day) {
        String v = "";
        switch (day) {
            case 1:
            case 21:
            case 31: {
                v = day + "st";
                break;
            }
            case 2:
            case 22: {
                v = day + "nd";
                break;
            }
            case 3:
            case 23: {
                v = day + "rd";
                break;
            }
            default: {
                v = day + "th";
            }
        }
        return v;
    }


    public JSONObject getDateDifferenceInDDMMYYYY(Date from, Date to) {
        Calendar fromDate = Calendar.getInstance();
        Calendar toDate = Calendar.getInstance();
        fromDate.setTime(from);
        toDate.setTime(to);
        int increment = 0;
        int year, month, day;
        //System.out.println(fromDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        if (fromDate.get(Calendar.DAY_OF_MONTH) > toDate.get(Calendar.DAY_OF_MONTH)) {
            increment = fromDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        //System.out.println("increment"+increment);
// DAY CALCULATION
        if (increment != 0) {
            day = (toDate.get(Calendar.DAY_OF_MONTH) + increment) - fromDate.get(Calendar.DAY_OF_MONTH);
            increment = 1;
        } else {
            day = toDate.get(Calendar.DAY_OF_MONTH) - fromDate.get(Calendar.DAY_OF_MONTH);
        }

// MONTH CALCULATION
        if ((fromDate.get(Calendar.MONTH) + increment) > toDate.get(Calendar.MONTH)) {
            month = (toDate.get(Calendar.MONTH) + 12) - (fromDate.get(Calendar.MONTH) + increment);
            increment = 1;
        } else {
            month = (toDate.get(Calendar.MONTH)) - (fromDate.get(Calendar.MONTH) + increment);
            increment = 0;
        }

// YEAR CALCULATION
        year = toDate.get(Calendar.YEAR) - (fromDate.get(Calendar.YEAR) + increment);

        JSONObject res = new JSONObject();
        try {
            res.put("diffYear", year);
            res.put("diffMonth", month);
            res.put("diffDays", day);
            res.put("to_YearName", toDate.get(Calendar.YEAR));
            res.put("to_MonthName", getMonthName3By(toDate.get(Calendar.MONTH) + 1));
            res.put("to_Day", toDate.get(Calendar.DAY_OF_MONTH));
            res.put("to_Hr24", hrMinFormatedBy2Dig(toDate.get(Calendar.HOUR_OF_DAY)));
            res.put("to_Hr12", hrMinFormatedBy2Dig(toDate.get(Calendar.HOUR)));
            res.put("to_Min", hrMinFormatedBy2Dig(toDate.get(Calendar.MINUTE)));
            res.put("to_Sec", hrMinFormatedBy2Dig(toDate.get(Calendar.SECOND)));
            res.put("to_AM-PM", (toDate.get(Calendar.AM_PM) == 1 ? "PM" : "AM"));

            res.put("from_YearName", fromDate.get(Calendar.YEAR));
            res.put("from_MonthName", getMonthName3By(fromDate.get(Calendar.MONTH) + 1));
            res.put("from_Day", fromDate.get(Calendar.DAY_OF_MONTH));
            res.put("from_Hr24", hrMinFormatedBy2Dig(fromDate.get(Calendar.HOUR_OF_DAY)));
            res.put("from_Hr12", hrMinFormatedBy2Dig(fromDate.get(Calendar.HOUR)));
            res.put("from_Min", hrMinFormatedBy2Dig(fromDate.get(Calendar.MINUTE)));
            res.put("from_Sec", hrMinFormatedBy2Dig(fromDate.get(Calendar.SECOND)));
            res.put("from_AM-PM", (fromDate.get(Calendar.AM_PM) == 1 ? "PM" : "AM"));

            boolean isSameYears = (fromDate.get(Calendar.YEAR) == toDate.get(Calendar.YEAR));
            boolean isSameMonths = (fromDate.get(Calendar.MONTH) == toDate.get(Calendar.MONTH));
            boolean isSameDays = (fromDate.get(Calendar.DAY_OF_MONTH) == toDate.get(Calendar.DAY_OF_MONTH));
            res.put("isSameYears", isSameYears);
            res.put("isSameMonths", isSameMonths);
            res.put("isSameDays", isSameDays);

            boolean isSameMonth_year = (isSameYears && isSameMonths);
            boolean isSameDays_Month_year = (isSameMonth_year && isSameDays);
            res.put("isSameMonth_year", isSameMonth_year);

            res.put("isSameDays_Month_year", isSameDays_Month_year);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return res;
    }

    public String getMonthName3By(int monthNo) {
        String monthName = "" + monthNo;
        switch (monthNo) {
            case 1: {
                monthName = "Jan";
                break;
            }
            case 2: {
                monthName = "Feb";
                break;
            }
            case 3: {
                monthName = "Mar";
                break;
            }
            case 4: {
                monthName = "Apr";
                break;
            }
            case 5: {
                monthName = "May";
                break;
            }
            case 6: {
                monthName = "Jun";
                break;
            }
            case 7: {
                monthName = "July";
                break;
            }
            case 8: {
                monthName = "Aug";
                break;
            }
            case 9: {
                monthName = "Sep";
                break;
            }
            case 10: {
                monthName = "OCt";
                break;
            }
            case 11: {
                monthName = "Nov";
                break;
            }
            case 12: {
                monthName = "Dec";
                break;
            }
        }
        return monthName;
    }

    public String hrMinFormatedBy2Dig(int hr_or_min) {
        if (hr_or_min <= 9) {
            return "0" + hr_or_min;
        } else {
            return String.valueOf(hr_or_min);
        }
    }

    /*public String UtcToLocalTime(String datestr) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(AppCons.YYYY_MM_DD_HH_MM_S_FORMAT, Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date2 = df.parse(datestr);
        df.setTimeZone(TimeZone.getDefault());
        String formattedDate = df.format(date2);
        return formattedDate;
    }*/
    public static Date localToGMT(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gmt = new Date(sdf.format(date));

        return gmt;
    }

    public String formateDate(String date) {
        String dateString = date;

        // Parse the string date as an Instant
        Instant instant = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            instant = Instant.parse(dateString);
        }

        // Convert the Instant to LocalDateTime
        LocalDateTime dateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateTime = LocalDateTime.ofInstant(instant, java.time.ZoneOffset.UTC);
        }

        // Extract the date components
        int year = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            year = dateTime.getYear();
        }
        int monthValue = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            monthValue = dateTime.getMonthValue();
        }
        int dayOfMonth = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dayOfMonth = dateTime.getDayOfMonth();
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int dayOfWeekValue = dateTime.getDayOfWeek().getValue();
        }

        // Format the date components into the desired format
        LocalDate localDate = LocalDate.of(year, monthValue, dayOfMonth);
        String formattedDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDate = localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US) + ", " +
                    localDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.US) + " " +
                    localDate.getDayOfMonth() + ", " +
                    localDate.getYear();
        }

        // Print the formatted date
        return formattedDate;

    }
    public static String getDisplayableTime(long delta) {
        long difference = 0;
        Long mDate = System.currentTimeMillis();

        if (mDate > delta) {
            difference = mDate - delta;
            final long seconds = difference / 1000;
            final long minutes = seconds / 60;
            final long hours = minutes / 60;
            final long days = hours / 24;
            final long months = days / 31;
            final long years = days / 365;

            if (seconds < 0) {
                return "not yet";
            } else if (seconds < 60) {
                return seconds == 1 ? "one second ago" : seconds + " seconds ago";
            } else if (seconds < 120) {
                return "a minute ago";
            } else if (seconds < 2700) // 45 * 60
            {
                return minutes + " minutes ago";
            } else if (seconds < 5400) // 90 * 60
            {
                return "an hour ago";
            } else if (seconds < 86400) // 24 * 60 * 60
            {
                return hours + " hours ago";
            } else if (seconds < 172800) // 48 * 60 * 60
            {
                return "yesterday";
            } else if (seconds < 2592000) // 30 * 24 * 60 * 60
            {
                return days + " days ago";
            } else if (seconds < 31104000) // 12 * 30 * 24 * 60 * 60
            {

                return months <= 1 ? "one month ago" : days + " months ago";
            } else {

                return years <= 1 ? "one year ago" : years + " years ago";
            }
        }
        return null;
    }



}
