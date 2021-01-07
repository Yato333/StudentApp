package com.app.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.Unmodifiable;

import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.util.ArrayList;

public class TimePeriod {
    public final LocalDate startDate, endDate;
    public final Period period;
    public final ArrayList<YearMonth> totalMonths = new ArrayList<>();
    public @Unmodifiable ObservableList<YearMonth> getTotalMonthsUnmodifiable() {
        return FXCollections.unmodifiableObservableList(FXCollections.observableList(totalMonths));
    }

    public TimePeriod(LocalDate startDateInclusive, LocalDate endDateExclusive) {
        this.startDate = startDateInclusive;
        this.endDate = endDateExclusive;
        period = Period.between(startDate, endDate);

        var m = YearMonth.of(startDate.getYear(), startDate.getMonth());
        for(; !m.isAfter(YearMonth.of(endDate.getYear(), endDate.getMonth())); m = m.plusMonths(1))
            totalMonths.add(m);
    }
}
