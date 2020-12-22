package dev.dmitrij.kuzmiciov.app;

import dev.dmitrij.kuzmiciov.app.data.Student;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class RootTable extends TableView<Student> {
    private static RootTable instance;

    public RootTable() {
        instance = this;

        var firstNameColumn = new TableColumn<Student, String>("First Name");
        var lastNameColumn = new TableColumn<Student, String>("Last Name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        getColumns().add(firstNameColumn);
        getColumns().add(lastNameColumn);

        setStyle("-fx-pref-width: 300; -fx-pref-height: 100;");
    }

    public static RootTable getInstance() {
        return instance;
    }
}
