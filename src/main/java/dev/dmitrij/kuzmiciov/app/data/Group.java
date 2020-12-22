package dev.dmitrij.kuzmiciov.app.data;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * This class contains references to students, stored as a set.
 * This means that each instance of this class cannot have 2 references to a same student.
 * @implSpec all constructors must call {@link #Group()}
 */

public class Group extends TreeSet<Student> {
    String name;
    
    protected Group() {
        super(Comparator.comparing((Student s) -> s.lastName).thenComparing(s -> s.firstName));
    }
    
    private Group(Comparator<? super Student> comparator) {
    }
    
    private Group(@NotNull Collection<? extends Student> c) {
    
    }
    
    private Group(SortedSet<Student> s) {
    }
    
    Group(@NotNull String name, @NotNull Student... students) {
        this();
        this.name = name;
        Collections.addAll(this, students);
    }

    Group(@NotNull String name, @NotNull Collection<Student> students) {
        this();
        this.name = name;
        this.addAll(students);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
