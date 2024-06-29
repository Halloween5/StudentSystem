package com.example.studentsystem.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class dateUtils {
    public static long dayStartMillis(LocalDate date){
        return LocalDateTime.of(date, LocalTime.MIN).toEpochSecond(ZoneOffset.of("+8"))*1000;
    }
    public static long dayEndMillis(LocalDate date){
        return LocalDateTime.of(date, LocalTime.MAX).toEpochSecond(ZoneOffset.of("+8"))*1000;
    }
    public static LocalDateTime Millis2DateTime(long millis){
        return LocalDateTime.ofEpochSecond(millis/1000, 0, ZoneOffset.ofHours(8));
    }
    public static long DateTime2Millis(LocalDateTime dateTime){
        return dateTime.toEpochSecond(ZoneOffset.of("+8"))*1000;
    }

    public static LocalDate Millis2Date(long millis) {
        return Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}