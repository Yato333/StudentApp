package com.app.data;

import com.app.util.TimePeriod;
import org.jetbrains.annotations.NotNull;

import java.time.*;

public class StudyYear extends TimePeriod {
    private static MonthDay studyYearStart = MonthDay.of(Month.SEPTEMBER, 1);
    public static MonthDay getStudyYearStart() {
        return studyYearStart;
    }
    public static void setStudyYearStart(@NotNull MonthDay studyYearStart) {
        StudyYear.studyYearStart = studyYearStart;
        studyYearEnd = MonthDay.of(studyYearStart.getMonthValue() + studyYearLengthInMonths - 12, 1);
    }

    private static int studyYearLengthInMonths = 9;
    public static int getStudyYearLengthInMonths() {
        return studyYearLengthInMonths;
    }
    public static void setStudyYearLengthInMonths(int studyYearLengthInMonths) {
        StudyYear.studyYearLengthInMonths = studyYearLengthInMonths;
        studyYearEnd = MonthDay.of(studyYearStart.getMonthValue() + studyYearLengthInMonths - 12, 1);
    }

    private static MonthDay studyYearEnd = MonthDay.of(studyYearStart.getMonthValue() + studyYearLengthInMonths - 12, 1);
    private static MonthDay getStudyYearEnd() {
        return studyYearEnd;
    }

    public StudyYear(@NotNull Year startYear) {
        this(startYear.getValue());
    }

    public StudyYear(int startYear) {
        super(LocalDate.of(startYear, studyYearStart.getMonth(), studyYearStart.getDayOfMonth()),
                LocalDate.of(startYear + 12 / studyYearLengthInMonths, studyYearStart.getMonth().plus(studyYearLengthInMonths), studyYearStart.getDayOfMonth())
        );
    }

    @Override
    public String toString() {
        return startDate.getYear() + "-" + endDate.getYear();
    }
}
