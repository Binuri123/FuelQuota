package lk.binuri.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class WeekUtil {
    public static LocalDateTime getStartOfWeek() {
        return LocalDate.now()
                .with(DayOfWeek.MONDAY)
                .atStartOfDay();
    }
    public static LocalDateTime getEndOfWeek() {
        return LocalDate.now()
                .with(DayOfWeek.SUNDAY)
                .atTime(LocalTime.of(23, 59, 59));
    }
}
