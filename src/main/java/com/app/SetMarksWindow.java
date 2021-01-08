package com.app;

import com.app.controller.RootController;
import com.app.data.Mark;
import com.app.data.Student;
import com.app.util.EventHandlers;
import com.app.util.LTDateConverter;
import com.app.util.TripleTuple;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.util.StringConverter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SetMarksWindow extends Dialog<Void> {
    private static final StringConverter<LocalDate> dateConverter = new LTDateConverter();

    private static final StudentMarkingTable studentTable = new StudentMarkingTable();

    public SetMarksWindow(@NotNull LocalDate date) {
        setHeaderText("Set marks for " + dateConverter.toString(date));

        var students = Objects.requireNonNull(RootController.getInstance().getCurrentGroup()).getStudents();
        studentTable.setItems(students, date);

        getDialogPane().setContent(studentTable);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        setResultConverter(buttonType -> {
            if(buttonType == ButtonType.OK) {
                for(var data : studentTable.data) {
                    var student = data.first;
                    var mark = data.second.getMark();
                    var comment = data.third.getText();
                    mark.setComment(comment == null ? "" : comment);
                    student.MARKS.put(date, mark);
                }
            }
            return null;
        });

        initModality(Modality.APPLICATION_MODAL);
    }

    private static class StudentMarkingTable extends GridPane {
        private final ArrayList<TripleTuple<Student, MarkBox, TextField>> data = new ArrayList<>();

        StudentMarkingTable() {
            for (int i = 0; i < 3; i++)
                getColumnConstraints().add(new ColumnConstraints(100, 100, 200));
            for(var column : getColumnConstraints())
                column.setHalignment(HPos.CENTER);
            setStyle("-fx-vgap: 10; -fx-hgap: 10");

            addRow(0, new Label("Name"), new Label("Absent/Mark"), new Label("Comment"));
        }

        void setItems(List<Student> students, LocalDate date) {
            data.clear();
            getChildren().removeIf(node -> GridPane.getRowIndex(node) > 0);

            if(getRowConstraints().size() > 1)
                getRowConstraints().remove(1, getChildren().size());

            int i = 1;
            for(var student : students) {
                var existingMark = student.MARKS.get(date);
                var markBox = new MarkBox(existingMark);
                var commentBox = new TextField(existingMark == null ? "" : existingMark.getComment());
                commentBox.setOnKeyPressed(EventHandlers.commitOnEnterHandler(commentBox));
                commentBox.textProperty().addListener((observable, oldValue, newValue) -> {
                    if(newValue.length() > 30)
                        commentBox.deletePreviousChar();
                });

                data.add(new TripleTuple<>(student, markBox, commentBox));
                addRow(i++, new Label(student.toString()), markBox, commentBox);
            }
        }

        private static class MarkBox extends HBox {
            final CheckBox absentBox = new CheckBox();
            final ComboBox<Integer> markBox = new ComboBox<>();
            MarkBox(Mark existingMark) {
                super(5);
                for(int i = 1; i <= 10; ++i)
                    markBox.getItems().add(i);
                markBox.disableProperty().bind(absentBox.selectedProperty());
                getChildren().addAll(absentBox, markBox);
                setStyle("-fx-alignment: center");

                if(existingMark != null) {
                    if (existingMark.isPresent()) {
                        if(existingMark.getMark() != null)
                            markBox.getSelectionModel().select(existingMark.getMark() - 1);
                    } else
                        absentBox.setSelected(true);
                }
            }

            Mark getMark() {
                if(absentBox.isSelected())
                    return new Mark(false);
                else if(markBox.getValue() == null)
                    return new Mark();
                else
                    return new Mark(markBox.getValue());
            }
        }
    }
}
