package dev.dmitrij.kuzmiciov.app;

import dev.dmitrij.kuzmiciov.app.data.Mark;
import dev.dmitrij.kuzmiciov.app.data.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;

public class RootTable extends TableView<Student> {
    private static RootTable instance;

    private final ArrayList<TableColumn<Student, Mark>> markColumns = new ArrayList<>();
    public @Unmodifiable ObservableList<TableColumn<Student, Mark>> getMarkColumns() {
        return FXCollections.unmodifiableObservableList(FXCollections.observableList(markColumns));
    }

    public RootTable(@NotNull DatePicker datePicker) {
        instance = this;

        var firstNameColumn = new TableColumn<Student, String>("First Name");
        var lastNameColumn = new TableColumn<Student, String>("Last Name");
        var averageColumn = new TableColumn<Student, Float>("Average");

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>(Student.FIRST_NAME_PROPERTY_NAME));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>(Student.LAST_NAME_PROPERTY_NAME));
        averageColumn.setCellValueFactory(new PropertyValueFactory<>(Student.AVERAGE_PROPERTY_NAME));

        for(int i = 0; i < 31; ++i) {
            var column = new TableColumn<Student, Mark>(String.format("%02d", i + 1));
            column.setMaxWidth(16);
            markColumns.add(new TableColumn<>(String.format("%02d", i + 1)));
        }

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.getMonth().equals(oldValue.getMonth())) {
                for (int i = 29; i <= 31; ++i) {
                    if (newValue.getMonth().length(newValue.isLeapYear()) < i) {
                        for (int j = i; j <= 31; ++j)
                            markColumns.get(j - 1).setVisible(false);
                        break;
                    }
                    markColumns.get(i - 1).setVisible(true);
                }
            }
        });

        getColumns().add(firstNameColumn);
        getColumns().add(lastNameColumn);
        getColumns().add(averageColumn);
        getColumns().addAll(markColumns);

        firstNameColumn.setMinWidth(100);
        lastNameColumn.setMinWidth(100);
        averageColumn.setMinWidth(50);
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        setStyle("-fx-pref-width: 300; -fx-pref-height: 100");
    }

    public static RootTable getInstance() {
        return instance;
    }
}
