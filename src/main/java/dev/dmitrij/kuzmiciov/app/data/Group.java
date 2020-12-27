package dev.dmitrij.kuzmiciov.app.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Group {
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    private final ArrayList<Student> students = new ArrayList<>();
    public ObservableList<Student> getStudents() {
        return FXCollections.observableList(students);
    }

    public Group() {
        this("");
    }

    public Group(@NotNull String name) {
        setName(name);
    }

    public Group(@NotNull Collection<? extends Student> c) {
        this("", c);
    }

    public Group(@NotNull String name, @NotNull Collection<? extends Student> c) {
        this(name);
        students.addAll(c);
    }

    @Override
    public String toString() {
        return getName();
    }
}
