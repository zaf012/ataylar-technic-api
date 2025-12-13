package com.ay_za.ataylar_technic.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class TimeHelper {

  private static final DateTimeFormatter midnightNoonFormat = DateTimeFormatter.ofPattern("a");

  private static final DateTimeFormatter hourMinuteSecondFormat =
      DateTimeFormatter.ofPattern("HH:mm:ss");

  private static final DateTimeFormatter hourMinuteFormat = DateTimeFormatter.ofPattern("HH:mm");

  private static final TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();

  /***
   *
   * controls time is am or pm
   *
   *
   * @param time
   * @return true/false
   */
  public static Boolean isBeforeNoon(String time) {
    LocalTime localTime = LocalTime.parse(time);
    String result = midnightNoonFormat.format(localTime);
    return "AM".equals(result) ? true : false;
  }

  /***
   *
   * returns time in format(HH:mm)
   *
   *
   * @param time
   * @return true/false
   */
  public static String toHourMinuteFormat(String time) {
    LocalTime localTime = LocalTime.parse(time);
    String result = hourMinuteFormat.format(localTime);
    return result;
  }

  /***
   *
   * returns time in format(HH:mm:ss)
   *
   *
   * @param time
   * @return true/false
   */
  public static String toHourMinuteSecondFormat(String time) {
    LocalTime localTime = LocalTime.parse(time);
    String result = hourMinuteSecondFormat.format(localTime);
    return result;
  }

  /***
   *
   * returns time in format(HH:mm:ss)
   *
   *
   * @param minute
   * @return true/false
   */
  public static BigDecimal minuteToHour(long minute) {
    BigDecimal min = new BigDecimal(minute);
    BigDecimal hourFinderFactor = new BigDecimal(60);

    BigDecimal result = min.divide(hourFinderFactor, 2, RoundingMode.CEILING);

    return result;
  }
}
