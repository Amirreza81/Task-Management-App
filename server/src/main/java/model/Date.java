package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.DAYS;

public class Date {
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd|HH:mm");
    private LocalDateTime localDate;
    private String date;

    public Date(String date) {
        this.localDate = LocalDateTime.parse(date, dtf);
        this.date = date;
    }

    public String toString() {
        return this.date;
    }

    public java.util.Date getDate() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd|HH:mm");
        return formatter.parse(this.date);
    }

    public static Integer getDaysBetween(Date inputDate1, Date inputDate2) {
        long daysBetween = DAYS.between(inputDate1.localDate, inputDate2.localDate);
        int result = (int) (daysBetween);
        return result;
    }

    public static Integer getTimeBetween(Date inputDate1, Date inputDate2) {
        long secondsBetween = ChronoUnit.SECONDS.between(inputDate1.localDate, inputDate2.localDate);
        int result = (int) secondsBetween;
        return result;
    }


    public static Date getNDaysAfter(Date inputDate1, int n) {
        String newDate = dtf.format(inputDate1.localDate.plusDays(n));
        return new Date(newDate);
    }

    public static Date getNow() {
        return new Date(dtf.format(LocalDateTime.now()));
    }


}
