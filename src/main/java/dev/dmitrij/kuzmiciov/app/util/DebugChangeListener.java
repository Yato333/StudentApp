package dev.dmitrij.kuzmiciov.app.util;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.io.PrintStream;

public class DebugChangeListener<E> implements ChangeListener<E> {
    private final PrintStream printStream;

    public DebugChangeListener(@NotNull PrintStream printStream) {
        this.printStream = printStream;
    }

    public DebugChangeListener() {
        this(System.out);
    }

    /**
     * Called when the value of an {@link ObservableValue} changes.
     * <p>
     * In general, it is considered bad practice to modify the observed value in
     * this method.
     *
     * @param observable The {@code ObservableValue} which value changed
     * @param oldValue   The old value
     */
    @Override
    public void changed(ObservableValue<? extends E> observable, E oldValue, E newValue) {
        printStream.println(oldValue + " changed to " + newValue);
    }
}
