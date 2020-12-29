package dev.dmitrij.kuzmiciov.app.util;

import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalQueries;

public class LTDateConverter extends StringConverter<LocalDate> {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public String toString(LocalDate object) {
        return object == null ? "" : formatter.format(object);
    }

    @Override
    public LocalDate fromString(String string) {
        return string == null ? null : formatter.parse(string, TemporalQueries.localDate());
    }
}
