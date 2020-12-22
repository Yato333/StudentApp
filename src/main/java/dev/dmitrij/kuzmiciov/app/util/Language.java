package dev.dmitrij.kuzmiciov.app.util;

import org.jetbrains.annotations.NotNull;

/**
 * This enum contains all the languages that this app supports
 */

public enum Language {
    EN(java.util.Locale.UK),
    LT(new java.util.Locale("lt"));
    
    public final java.util.Locale locale;
    Language(@NotNull java.util.Locale locale) {
        this.locale = locale;
    }
}
