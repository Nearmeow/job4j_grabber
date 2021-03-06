package ru.job4j.grabber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {

    private static final String TODAY = "сегодня";
    private static final String YESTERDAY = "вчера";
    private static final Map<String, String> MONTHS = Map.ofEntries(
            Map.entry("янв", "January"),
            Map.entry("фев", "February"),
            Map.entry("мар", "March"),
            Map.entry("апр", "April"),
            Map.entry("май", "May"),
            Map.entry("июн", "June"),
            Map.entry("июл", "July"),
            Map.entry("авг", "August"),
            Map.entry("сен", "September"),
            Map.entry("окт", "October"),
            Map.entry("ноя", "November"),
            Map.entry("дек", "December")
    );
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dMMMMyy", Locale.ENGLISH);

    @Override
    public LocalDateTime parse(String parse) {
        String[] strings = parse.split(",");
        LocalTime time = LocalTime.parse(strings[1].trim());
        if (parse.contains(TODAY)) {
            return LocalDate.now().atTime(time);
        } else if (parse.contains(YESTERDAY)) {
            return LocalDate.now().minusDays(1).atTime(time);
        }
        String[] dateStrings = strings[0].split(" ");
        String engMonth = MONTHS.get(dateStrings[1]);
        String newDateString = dateStrings[0] + engMonth + dateStrings[2];
        LocalDate newDate = LocalDate.parse(newDateString, FORMATTER);
        return newDate.atTime(time);
    }
}
