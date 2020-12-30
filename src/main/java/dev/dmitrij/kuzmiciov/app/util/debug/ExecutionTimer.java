package dev.dmitrij.kuzmiciov.app.util.debug;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintStream;

public class ExecutionTimer {
    private final Runnable task;
    private final String name;

    public ExecutionTimer(@NotNull Runnable task) {
        this.task = task;
        name = "";
    }

    public ExecutionTimer(@NotNull Runnable task, @NotNull String name) {
        this.task = task;
        this.name = name;
    }

    public long execute() {
        long timeBefore = System.currentTimeMillis();
        task.run();
        return System.currentTimeMillis() - timeBefore;
    }

    public void logExecution(PrintStream printStream) {
        long duration = execute();
        printStream.println("Task " + (name.length() == 0 ? "" : name) + " took " + duration + " ms to execute");
    }

    public void logExecution() {
        logExecution(System.out);
    }
}
