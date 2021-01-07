package com.app.util;

import org.intellij.lang.annotations.Language;

public enum Regexes {
    GROUP_EN("^.+$"),
    NAME_EN("^[A-Z][A-Za-z]+$");

    @Language("regexp")
    public final String regex;
    Regexes(@Language("regexp") String regex) {
        this.regex = regex;
    }
}
