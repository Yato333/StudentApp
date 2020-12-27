package dev.dmitrij.kuzmiciov.app.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Student {
    public static final String
            FIRST_NAME_PROPERTY_NAME = "firstName",
            LAST_NAME_PROPERTY_NAME = "lastName";

    StringProperty firstName;
    public String getFirstName() {
        return firstNameProperty().get();
    }
    public StringProperty firstNameProperty() {
        if(firstName == null)
            firstName = new SimpleStringProperty(this, FIRST_NAME_PROPERTY_NAME);
        return firstName;
    }
    public void setFirstName(String firstName) {
        firstNameProperty().set(firstName);
    }


    StringProperty lastName;
    public String getLastName() {
        return lastNameProperty().get();
    }
    public StringProperty lastNameProperty() {
        if(lastName == null)
            lastName = new SimpleStringProperty(this, LAST_NAME_PROPERTY_NAME);
        return lastName;
    }
    public void setLastname(String lastName) {
        lastNameProperty().set(lastName);
    }


    public Student() {
        this("", "");
    }

    public Student(String firstName, String lastName) {
        setFirstName(firstName);
        setLastname(lastName);
    }
}
