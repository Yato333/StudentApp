package dev.dmitrij.kuzmiciov.app;

import dev.dmitrij.kuzmiciov.app.controller.RootController;
import dev.dmitrij.kuzmiciov.app.data.Mark;
import dev.dmitrij.kuzmiciov.app.data.Student;
import dev.dmitrij.kuzmiciov.app.util.AppMath;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class RootTable extends TableView<Student> {
    private static RootTable instance;

    private final ArrayList<TableColumn<Student, Mark>> markColumns = new ArrayList<>();
    public @Unmodifiable ObservableList<TableColumn<Student, Mark>> getMarkColumns() {
        return FXCollections.unmodifiableObservableList(FXCollections.observableList(markColumns));
    }
    private final LinkedList<TableColumn<Student, Mark>> shownMarkColumns = new LinkedList<>();

    public RootTable(@NotNull ComboBox<YearMonth> monthPicker) {
        instance = this;

        var firstNameColumn = new TableColumn<Student, String>("First Name");
        var lastNameColumn = new TableColumn<Student, String>("Last Name");
        var averageColumn = new TableColumn<Student, String>("Average");

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>(Student.FIRST_NAME_PROPERTY_NAME));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>(Student.LAST_NAME_PROPERTY_NAME));

        for(int i = 0; i < 31; ++i) {
            var column = new TableColumn<Student, Mark>(String.format("%02d", i + 1));
            column.setMaxWidth(50);
            markColumns.add(column);
        }

        monthPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.getMonth().equals(oldValue.getMonth())) {
                for (int i = 29; i <= 31; ++i) {
                    if (newValue.getMonth().length(newValue.isLeapYear()) < i) {
                        for (int j = i; j <= 31; ++j)
                            markColumns.get(j - 1).setVisible(false);
                        break;
                    }
                    markColumns.get(i - 1).setVisible(true);
                }
                var group = RootController.getInstance().getCurrentGroup();
                assert (group != null);

                for(var student : group.getStudents()) {
                    assert(student != null);
                    //TODO: add student marks
                }
            }
        });

        averageColumn.setCellValueFactory(cellDataFeatures -> new ObjectBinding<>() {
            @Override
            protected String computeValue() {
                AtomicInteger
                    i = new AtomicInteger(),
                    sum = new AtomicInteger();
                markColumns.forEach(markColumn -> {
                    var cellValueForStudent = markColumn.getCellObservableValue(cellDataFeatures.getValue());
                    if(cellValueForStudent != null && cellValueForStudent.getValue() != null) {
                        var mark = cellValueForStudent.getValue().getMark();
                        if(mark != null) {
                            sum.addAndGet(mark);
                            i.incrementAndGet();
                        }
                    }
                });
                float average = (float)(sum.get()) / (float)(i.get());
                return i.get() == 0 ? "-" : String.valueOf(AppMath.round(average, 2));
            }
        });

        getColumns().add(firstNameColumn);
        getColumns().add(lastNameColumn);
        getColumns().add(averageColumn);
        getColumns().addAll(markColumns);

        firstNameColumn.setMinWidth(100);
        firstNameColumn.setMaxWidth(200);
        lastNameColumn.setMinWidth(100);
        lastNameColumn.setMaxWidth(200);
        averageColumn.setMinWidth(50);
        averageColumn.setMaxWidth(100);

        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);

        getColumns().forEach(column -> {
            column.setReorderable(false);
            column.setStyle("-fx-alignment: center");
        });

        setStyle("-fx-pref-width: 300; -fx-pref-height: 100; -fx-column-halignment: center");
    }

    public static RootTable getInstance() {
        return instance;
    }
}
