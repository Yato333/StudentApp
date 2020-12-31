package dev.dmitrij.kuzmiciov.app.util.debug;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;

public class DebugChangeListener<E> implements ChangeListener<E> {
    private final PrintStream printStream;

    public DebugChangeListener(@NotNull PrintStream printStream) {
        this.printStream = printStream;
    }

    public DebugChangeListener() {
        this(System.out);
    }

    @Override
    public void changed(ObservableValue<? extends E> observable, E oldValue, E newValue) {
        printStream.println(oldValue + " changed to " + newValue);
    }
}
