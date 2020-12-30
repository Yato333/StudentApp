package dev.dmitrij.kuzmiciov.app.data;

import dev.dmitrij.kuzmiciov.app.util.TimePeriod;
import org.jetbrains.annotations.NotNull;

import java.time.*;

public class StudyYear extends TimePeriod {
    private static MonthDay studyYearStart = MonthDay.of(Month.SEPTEMBER, 1);
    public static MonthDay getStudyYearStart() {
        return studyYearStart;
    }
    public static void setStudyYearStart(@NotNull MonthDay studyYearStart) {
        StudyYear.studyYearStart = studyYearStart;
    }

    private static int studyYearLengthInMonths = 9;
    public static int getStudyYearLengthInMonths() {
        return studyYearLengthInMonths;
    }
    public static void setStudyYearLengthInMonths(int studyYearLengthInMonths) {
        StudyYear.studyYearLengthInMonths = studyYearLengthInMonths;
    }

    public StudyYear(@NotNull Year startYear) {
        this(startYear.getValue());
    }

    public StudyYear(int startYear) {
        super(LocalDate.of(startYear, studyYearStart.getMonth(), studyYearStart.getDayOfMonth()),
                LocalDate.of(startYear + 12 / studyYearLengthInMonths, studyYearStart.getMonth().plus(studyYearLengthInMonths), studyYearStart.getDayOfMonth())
        );
    }
}
