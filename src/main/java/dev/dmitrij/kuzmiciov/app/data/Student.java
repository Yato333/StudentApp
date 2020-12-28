package dev.dmitrij.kuzmiciov.app.data;

import javafx.beans.property.*;

public class Student {
    public static final String
            FIRST_NAME_PROPERTY_NAME = "firstName",
            LAST_NAME_PROPERTY_NAME = "lastName",
            AVERAGE_PROPERTY_NAME = "average";


    StringProperty firstName = new SimpleStringProperty(this, FIRST_NAME_PROPERTY_NAME);
    public String getFirstName() {
        return firstName.get();
    }
    public StringProperty firstNameProperty() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }


    StringProperty lastName = new SimpleStringProperty(this, LAST_NAME_PROPERTY_NAME);
    public String getLastName() {
        return lastName.get();
    }
    public StringProperty lastNameProperty() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }


    FloatProperty average = new SimpleFloatProperty(this, AVERAGE_PROPERTY_NAME);
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
    }
}
