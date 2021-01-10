package com.app.window;

import com.app.data.Mark;
import com.app.data.Student;
import com.app.util.TripleTuple;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import org.jetbrains.annotations.NotNull;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class SetMarksForStudentWindow extends Dialog<Void> {
    public SetMarksForStudentWindow(@NotNull Student student, @NotNull YearMonth month) {
        var headerText = new Label("Set marks for " + student.toString());
        headerText.setStyle("-fx-font-size: 18");
        var header = new VBox(headerText);
        header.setStyle("-fx-alignment: center; -fx-padding: 10, 10, 10, 10");
        getDialogPane().setHeader(header);

        var content = new Content(student, month);
        getDialogPane().setContent(content);

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        setResultConverter(buttonType -> {
            if(buttonType == ButtonType.OK) {
                for(var data : content.data) {
                    Mark mark;
                    if(data.second.isSelected())
                        mark = new Mark(false);
                    else if (data.third.getValue() != null)
                        mark = new Mark(data.third.getValue());
                    else
                        mark = new Mark();
                    student.MARKS.put(month.atDay(data.first), mark);
                }
            }
            return null;
        });
        initModality(Modality.APPLICATION_MODAL);
    }

    private static class Content extends GridPane {
        ArrayList<TripleTuple<Integer, CheckBox, ComboBox<Integer>>> data = new ArrayList<>();
        public Content(Student student, YearMonth month) {
            getColumnConstraints().add(new ColumnConstraints(100, 100, 100));
            for(int i = 1; i <= 31; ++i)
                getColumnConstraints().add(new ColumnConstraints(50, 50, 100));

            addColumn(0, new Label(month.toString()), new Label("Absent/Mark"));
            int columnIndex = 1;
            for(int i = columnIndex; i <= month.lengthOfMonth(); ++i, ++columnIndex) {
                add(new Label(String.valueOf(i)), columnIndex, 0);
                var mark = student.MARKS.get(month.atDay(i));

                var absentBox = new CheckBox();
                if(mark != null)
                    absentBox.setSelected(!mark.isPresent());

                var markBox = new ComboBox<Integer>();
                markBox.setStyle("-fx-max-width: 32");
                IntStream.range(1, 11).forEach(n -> markBox.getItems().add(n));
                markBox.disableProperty().bind(absentBox.selectedProperty());
                if(mark != null && mark.getMark() != null)
                    markBox.getSelectionModel().select((int)(mark.getMark() - 1));

                data.add(new TripleTuple<>(i, absentBox, markBox));
                var vbox = new VBox(5, absentBox, markBox);
                vbox.setStyle("-fx-alignment: center");
                add(vbox, columnIndex, 1);
            }

            getColumnConstraints().forEach(column -> column.setHalignment(HPos.CENTER));

            setStyle("-fx-vgap: 5; -fx-hgap: 5;");
        }
    }
}
