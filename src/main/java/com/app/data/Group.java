package com.app.data;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class Group {
    public final static int MAX_NAME_LENGTH = 30;

    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    private final ListProperty<Student> students = new SimpleListProperty<>(this, "studentList", FXCollections.observableArrayList());
    public ObservableList<Student> getStudents() {
        return students.get();
    }
    public ListProperty<Student> studentsProperty() {
        return students;
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
