package com.app.data;

import com.app.RootTable;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

import java.time.LocalDate;

public class Student {
    public static final String
            FIRST_NAME_PROPERTY_NAME = "firstName",
            LAST_NAME_PROPERTY_NAME = "lastName",
            AVERAGE_PROPERTY_NAME = "average";

    public final ObservableMap<LocalDate, Mark> MARKS = FXCollections.observableHashMap();

    private final StringProperty firstName = new SimpleStringProperty(this, FIRST_NAME_PROPERTY_NAME);
    public String getFirstName() {
        return firstName.get();
    }
    public StringProperty firstNameProperty() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    private final StringProperty lastName = new SimpleStringProperty(this, LAST_NAME_PROPERTY_NAME);
    public String getLastName() {
        return lastName.get();
    }
    public StringProperty lastNameProperty() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    private final FloatProperty average = new SimpleFloatProperty(this, AVERAGE_PROPERTY_NAME);
    public float getAverage() {
        return average.get();
    }
    public FloatProperty averageProperty() {
        return average;
    }
    public void setAverage(float average) {
        this.average.set(average);
    }


    public Student() {
        this("", "");
    }

    public Student(String firstName, String lastName) {
        setFirstName(firstName);
        setLastName(lastName);
        setAverage(0);

        MARKS.addListener((MapChangeListener<? super LocalDate, ? super Mark>) x -> RootTable.getInstance().refresh());
    }

    @Override
    public String toString() {
        return firstName.get() + " " + lastName.get();
    }
}
