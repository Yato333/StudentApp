package dev.dmitrij.kuzmiciov.app.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Mark {
    public static final int MIN_MARK = 1, MAX_MARK = 10;

    private boolean present = true;
    private Integer mark = null;
    private String comment = "";

    public Mark(@Nullable Integer value) {
        setMark(value);
    }

    public Mark(boolean present) {
        this.present = present;
    }

    public Mark(int value, @NotNull String comment) {
        this(value);
        this.comment = comment;
    }

    public Mark(boolean present, @NotNull String comment) {
        this(present);
        this.comment = comment;
    }

    public boolean isPresent() {
        return present;
    }
    /**
     * @return a mark value if it was set, null otherwise
     */
    public @Nullable Integer getMark() {
        return mark;
    }
    public String getComment() {
        return comment;
    }

    public void setPresent(boolean present) {
        if(!present)
            mark = null;
        this.present = present;
    }
    public void setMark(@Nullable Integer mark) {
        if(mark != null && (mark < MIN_MARK || mark > MAX_MARK))
            throw new IllegalArgumentException("Mark has to be in range [" + MIN_MARK + "; " + MAX_MARK + "]");
        this.mark = mark;
    }
    public void setComment(@NotNull String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return present ? (mark == null ? "" : String.valueOf(mark)) : "n";
    }
}
