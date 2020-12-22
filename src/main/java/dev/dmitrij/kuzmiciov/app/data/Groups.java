package dev.dmitrij.kuzmiciov.app.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 */

public final class Groups {
    private static final List<Group> groups = new ArrayList<>();
    
    private Groups() {}
    
    public static Group getGroup(int index) {
        try {
            return groups.get(index);
        } catch(IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static Group addGroup(String name, Student... students) {
        var group = new Group(name, students);
        groups.add(group);
        return group;
    }

    public static Group addGroup(String name, Collection<Student> students) {
        var group = new Group(name, students);
        groups.add(new Group(name, students));
        return group;
    }

    public static Group addGroup(Student... students) {
        return addGroup(String.valueOf(groups.size() + 1), students);
    }

    public static Group addGroup(Collection<Student> students) {
        return addGroup(String.valueOf(groups.size() + 1), students);
    }

    public static Group addGroup(@NotNull Group group) {
        if(!groups.contains(group))
            groups.add(group);
        return group;
    }
    
    public static boolean contains(Group group) {
        return groups.contains(group);
    }
    
    public static Iterator<Group> iterator() {
        return groups.iterator();
    }
}
