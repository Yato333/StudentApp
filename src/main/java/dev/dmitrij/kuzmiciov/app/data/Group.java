package dev.dmitrij.kuzmiciov.app.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Group {
    private final StringProperty name = new SimpleStringProperty(this, "groupName");
    public String getName() {
        return name.get();
    }
    public StringProperty nameProperty() {
        return name;
    }
    public void setName(String name) {
        this.name.set(name);
    }

    private final ArrayList<Student> students = new ArrayList<>();
    public ArrayList<Student> getStudents() {
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
        return name.get();
    }


}
