package dev.dmitrij.kuzmiciov.app.util;

import org.intellij.lang.annotations.Language;

public enum Regexes {
    GROUP_EN("^[A-Za-z0-9 ]+$"),
    NAME_EN("^[A-Z][a-z]+ [A-Z][a-z]+$"),
    GROUP_LT("^[A-ZĄČĖĮŠŪŽa-ząčęėįšųūž0-9 ]+$"),
    NAME_LT("^[A-ZĄČĖĮŠŲŪŽ][a-ząčęėįšųūž]+ [A-ZĄČĖĮŠŲŪŽ][a-ząčęėįšųūž]+$");

    @Language("regexp")
    public final String regex;
    Regexes(@Language("regexp") String regex) {
        this.regex = regex;
    }
}
