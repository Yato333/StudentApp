package dev.dmitrij.kuzmiciov.app.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class Group {
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
        return getName();
    }


}
