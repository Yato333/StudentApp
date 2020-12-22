package dev.dmitrij.kuzmiciov.app.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.HashMap;

public class Student {
    String firstName, lastName;
    Group group = null;
    protected HashMap<LocalDate, Mark> marks = new HashMap<>();

    public Student(@NotNull String firstName, @NotNull String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setGroup(@NotNull Group newGroup) {
        if(group == null)
            group = newGroup;
        else {
            group.remove(this);
            newGroup.add(this);
        }
    }
    
    public void setMark(@NotNull LocalDate date, @NotNull Mark mark) {
        marks.put(date, mark);
    }
    
    public void removeMark(@NotNull LocalDate date) {
        marks.remove(date);
    }
    
    /**
     *
     * @return a mark if it was set at that date; null, if it wasn't
     */
    public @Nullable Mark getMark(@NotNull LocalDate date) {
        return marks.get(date);
    }
    
    public boolean wasAbsent(@NotNull LocalDate date) {
        var mark = marks.get(date);
        if(mark == null)
            return false;
        return mark.isAbsent();
    }
}
