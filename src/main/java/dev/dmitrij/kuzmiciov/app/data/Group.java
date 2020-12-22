package dev.dmitrij.kuzmiciov.app.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Group extends TreeSet<Student> {
    private StringProperty name;
    public String getName() {
        return nameProperty().get();
    }
    public StringProperty nameProperty() {
        if(name == null)
            name = new SimpleStringProperty(this, "name");
        return name;
    }
    public void setName(String name) {
        nameProperty().set(name);
    }

    public Group(@NotNull String name) {
        super(Comparator.comparing(Student::getLastName).thenComparing(Student::getFirstName));
        setName(name);
    }

    public Group(@NotNull String name, @NotNull Collection<? extends Student> c) {
        this(name);
        addAll(c);
    }

    @Override
    public String toString() {
        return getName();
    }

    // Hidden Constructors
    @SuppressWarnings("unused")
    private Group() {}
    @SuppressWarnings("unused")
    private Group(@NotNull Collection<? extends Student> c) {}
    @SuppressWarnings("unused")
    private Group(SortedSet<Student> s) {}
    @SuppressWarnings("unused")
    private Group(Comparator<? super Student> comparator){}
}
