package com.ay_za.ataylar_technic.util;

import org.apache.commons.lang3.StringUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.time.temporal.TemporalAdjusters.*;

public class DateUtil {

  public static final DateTimeFormatter shortDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public static final DateTimeFormatter shortDateFormatDayFirst =
      DateTimeFormatter.ofPattern("dd-MM-yyyy");

  public static final DateTimeFormatter dateTimeFormat =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  public static final DateTimeFormatter dateTimeTrFormat2 =
      DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

  public static final DateTimeFormatter dateTimeTrFormat3 =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public static final DateTimeFormatter dateTimeFormatTr =
      DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

  public static final TemporalField fieldISO = WeekFields.of(new Locale("tr", "TR")).dayOfWeek();

  public static final DateTimeFormatter trDateFormat =
      DateTimeFormatter.ofPattern("dd.MM.yyyy", new Locale("tr", "TR"));

  public static final DateTimeFormatter termFormatter =
      DateTimeFormatter.ofPattern("yyyy-MMMM", new Locale("tr", "TR"));

  public static final DateTimeFormatter trDateTimeFormat =
      DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

  public static final DateTimeFormatter dateTimeFormatTr2 =
      DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

  public static final DateTimeFormatter dateTimeFormatForInvoiceTransactionNo =
      DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");


  public static DateTimeFormatter dateTimeFormatterEnWithT = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd'T'hh:mm:ss");


  public static final DateTimeFormatter periodFormat = DateTimeFormatter.ofPattern("yyyy-MM");

  /***
   *
   * converts LocalDate to string in format(yyyy-MM-dd)
   *
   *
   * @param localDate
   * @return String
   */
  public static String localDateToShortString(LocalDate localDate) {
    String result = shortDateFormat.format(localDate);
    return result;
  }

  public static String shortStringDayFirst(String date) {
    LocalDate localDate = stringToLocalDate(date);
    String result = shortDateFormatDayFirst.format(localDate);

    return result;
  }

  public static LocalDate stringToLocalDate(String date) {
    LocalDate currentDate = LocalDate.parse(date);
    return currentDate;
  }

  public static List<LocalDate> workingDaysOfMonth(String startDate) {

    List<LocalDate> workingDayList = new ArrayList<>();

    LocalDate now = LocalDate.now();

    LocalDate start = now.with(firstDayOfYear());
    start = start.minusWeeks(1L);

    LocalDate tempDate = start;

    for (int i = 0; i < 375; i++) {
      if (!(tempDate.getDayOfWeek().name().equals("SATURDAY")
          || tempDate.getDayOfWeek().name().equals("SUNDAY"))) {
        workingDayList.add(tempDate);
      }

      tempDate = tempDate.plusDays(1L);
    }

    return workingDayList;
  }

  public static LocalDate firstDayOfWeek(String date) {
    LocalDate currentDate = LocalDate.parse(date);
    LocalDate firstDayOfWeek = currentDate.with(fieldISO, 1);
    return firstDayOfWeek;
  }

  public static LocalDate firstDayOfWeek() {
    LocalDate currentDate = LocalDate.now();
    LocalDate firstDayOfWeek = currentDate.with(fieldISO, 1);
    return firstDayOfWeek;
  }

  public static LocalDate firstDayOfWeek(LocalDate date) {
    LocalDate firstDayOfWeek = date.with(fieldISO, 1);
    return firstDayOfWeek;
  }

  public static String getFirstDayOfWeek(String date) {
    LocalDate currentDate = LocalDate.parse(date);
    LocalDate firstDayOfWeek = currentDate.with(fieldISO, 1);
    return DateUtil.localDateToShortString(firstDayOfWeek);
  }

  public static String getFirstDayOfWeek(LocalDate date) {
    LocalDate firstDayOfWeek = date.with(fieldISO, 1);
    return DateUtil.localDateToShortString(firstDayOfWeek);
  }

  public static LocalDate lastDayOfWeek(String date) {
    LocalDate currentDate = LocalDate.parse(date);
    LocalDate lastDayOfWeek = currentDate.with(fieldISO, 7);
    return lastDayOfWeek;
  }

  public static LocalDate lastDayOfWeek(LocalDate date) {
    LocalDate lastDayOfWeek = date.with(fieldISO, 7);
    return lastDayOfWeek;
  }

  public static LocalDate lastDayOfWeek() {
    LocalDate currentDate = LocalDate.now();
    LocalDate lastDayOfWeek = currentDate.with(fieldISO, 7);
    return lastDayOfWeek;
  }

  public static String getLastDayOfWeek(String date) {
    LocalDate currentDate = LocalDate.parse(date);
    LocalDate lastDayOfWeek = currentDate.with(fieldISO, 7);
    return DateUtil.localDateToShortString(lastDayOfWeek);
  }

  public static String getLastDayOfWeek(LocalDate date) {
    LocalDate lastDayOfWeek = date.with(fieldISO, 7);
    return DateUtil.localDateToShortString(lastDayOfWeek);
  }

  public static LocalDate minusWeek(String date, long amount) {
    LocalDate currentDate = LocalDate.parse(date);
    currentDate = currentDate.minusWeeks(amount);
    return currentDate;
  }

  public static LocalDate getFirstDayOfMonth(String date) {
    LocalDate currentDate = LocalDate.parse(date);
    LocalDate start = currentDate.with(firstDayOfMonth());
    return start;
  }

  public static LocalDate getFirstDayOfMonth(LocalDate localDate) {
    LocalDate start = localDate.with(firstDayOfMonth());
    return start;
  }

  public static String findFirstDayOfMonth(String date) {
    LocalDate currentDate = LocalDate.parse(date);
    LocalDate start = currentDate.with(firstDayOfMonth());
    return DateUtil.localDateToShortString(start);
  }

  public static String findFirstDayOfMonth(LocalDate localDate) {
    LocalDate start = localDate.with(firstDayOfMonth());
    return DateUtil.localDateToShortString(start);
  }

  public static LocalDate getLastDayOfMonth(LocalDate currentDate) {
    LocalDate end = currentDate.with(lastDayOfMonth());
    return end;
  }

  public static LocalDate getLastDayOfMonth(String date) {
    LocalDate currentDate = LocalDate.parse(date);
    LocalDate end = currentDate.with(lastDayOfMonth());
    return end;
  }

  public static String findLastDayOfMonth(LocalDate currentDate) {
    LocalDate end = currentDate.with(lastDayOfMonth());
    return DateUtil.localDateToShortString(end);
  }

  public static String findLastDayOfMonth(String date) {
    LocalDate currentDate = LocalDate.parse(date);
    LocalDate end = currentDate.with(lastDayOfMonth());
    return DateUtil.localDateToShortString(end);
  }

  public static String toString(DateTimeFormatter dateTimeFormatter, LocalDate date) {
    String result = dateTimeFormatter.format(date);
    return result;
  }

  public static String toString(DateTimeFormatter dateTimeFormatter, LocalDateTime date) {
    String result = dateTimeFormatter.format(date);
    return result;
  }

  public static boolean isBetween(String currentDate, String startDate, String endDate) {

    LocalDate current = LocalDate.parse(currentDate);
    LocalDate start = LocalDate.parse(startDate);
    LocalDate end = LocalDate.parse(endDate);

    return !current.isBefore(start) && !current.isAfter(end);
  }

  public static boolean isBetween(LocalDate currentDate, LocalDate startDate, LocalDate endDate) {
    return !currentDate.isBefore(startDate) && !currentDate.isAfter(endDate);
  }

  public static int compareRange(String currentDate, String startDate, String endDate) {

    LocalDate current = LocalDate.parse(currentDate);
    LocalDate start = LocalDate.parse(startDate);
    LocalDate end = LocalDate.parse(endDate);

    int compareResult = 0;

    boolean inRange = isBetween(currentDate, startDate, endDate);
    boolean isBeforeRange = current.isBefore(start);
    boolean isAfterRange = current.isAfter(end);

    if (inRange) {
      compareResult = 0;
    } else if (isBeforeRange) {
      compareResult = -1;
    } else if (isAfterRange) {
      compareResult = 1;
    }

    return compareResult;
  }

  public static int findCountSundays(LocalDate start, LocalDate end) {

    int startDayNumber = start.getDayOfMonth();
    int endDayNumber = end.getDayOfMonth();
    int countSunday = 0;

    int diff = endDayNumber - startDayNumber + 1;

    for (int i = 0; i < diff; i++) {
      LocalDate newDate = start.plusDays(i);
      if (newDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
        countSunday++;
      }
    }

    return countSunday;
  }

  public static LocalDate getFirstDayOfYear() {
    final LocalDate now = LocalDate.now();
    return now.with(firstDayOfYear());
  }

  public static String formatLocalDateTime(DateTimeFormatter dateTimeFormat, String localDateTime) {
    if (localDateTime.length() > 23) {
      localDateTime = localDateTime.substring(0, 23).replace('T', ' ');
    }
    return LocalDateTime.parse(
            localDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS][.SS][.S]"))
        .format(dateTimeFormat);
  }

  public static LocalDateTime toLocalDateTime(String localDateTime) {
    return LocalDateTime.parse(
        localDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS][.SS][.S]"));
  }

  public static String getCurrentPeriod() {
    return periodFormat.format(LocalDate.now());
  }

  public static String getPreviousPeriod() {
    return periodFormat.format(LocalDate.now().minusMonths(1));
  }

  public static String formatDate(String date, String initDateFormat, String endDateFormat) {
    Date initDate;
    try {
      initDate = new SimpleDateFormat(initDateFormat).parse(date);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
    String parsedDate = formatter.format(initDate);
    return parsedDate;
  }

  public static String dateToString(Date date, String format) {
    return new SimpleDateFormat(format).format(date);
  }

  public static String formatConsentDate(String formattedConsentDate) {
    if (formattedConsentDate.length() > 25) {
      return formattedConsentDate.substring(0, 25);
    } else if (formattedConsentDate.length() < 25) {
      return StringUtils.rightPad(formattedConsentDate, 25, "0");
    } else {
      return formattedConsentDate;
    }
  }

  public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
    return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }


  public static LocalDateTime XMLGregorianCalendarToLocalDateTime(
      XMLGregorianCalendar xmlGregorianCalendar) {
    ZonedDateTime utcZoned = xmlGregorianCalendar.toGregorianCalendar().toZonedDateTime()
        .withZoneSameInstant(ZoneId.of("UTC"));
    return utcZoned.toLocalDateTime();
  }

  public static XMLGregorianCalendar localDateTimeToXMLGregorianCalendar(LocalDateTime time)
      throws DatatypeConfigurationException {
    final String dateTimeStr = time.format(dateTimeFormatterEnWithT);

    return DatatypeFactory.newInstance()
        .newXMLGregorianCalendar(dateTimeStr);
  }

  public static Date localDateTimeToDate(LocalDateTime localDateTime) {
    ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
    // ZonedDateTime'dan Instant'e dönüşüm
    Instant instant = zonedDateTime.toInstant();
    // Instant'ten Date'e dönüşüm
    return Date.from(instant);
  }


}
