package dev.dmitrij.kuzmiciov.app.util;

import org.jetbrains.annotations.NotNull;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class StudyYear extends TimePeriod {
    private static MonthDay studyYearStart = MonthDay.of(Month.SEPTEMBER, 1);
    public static MonthDay getStudyYearStart() {
        return studyYearStart;
    }
    public static void setStudyYearStart(@NotNull MonthDay studyYearStart) {
        if(studyYearStart.isAfter(studyYearEnd))
            throw new IllegalArgumentException("The start of the study year cannot be after the end of the year.");
        StudyYear.studyYearStart = studyYearStart;
    }

    private static MonthDay studyYearEnd = MonthDay.of(Month.JULY, 1);
    public static MonthDay getStudyYearEnd() {
        return studyYearEnd;
    }
    public static void setStudyYearEnd(@NotNull MonthDay studyYearEnd) {
        if(studyYearEnd.isBefore(studyYearStart))
            throw new IllegalArgumentException("The end of the study year cannot be before the start of the year.");
        StudyYear.studyYearEnd = studyYearEnd;
    }

    public StudyYear(@NotNull Year startYear) {
        this(startYear.getValue());
    }

    public StudyYear(int startYear) {
        super(LocalDate.of(startYear, studyYearStart.getMonth(), studyYearStart.getDayOfMonth()),
                LocalDate.of(startYear + 1, studyYearEnd.getMonth(), studyYearEnd.getDayOfMonth())
        );
    }
}
