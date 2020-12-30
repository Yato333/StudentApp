package dev.dmitrij.kuzmiciov.app.util.factory;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class RedWeekendDaysDayCellFactory implements Callback<DatePicker, DateCell> {
    @Override
    public DateCell call(DatePicker param) {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if(item.getDayOfWeek() == DayOfWeek.SATURDAY || item.getDayOfWeek() == DayOfWeek.SUNDAY)
                    setStyle("-fx-background-color: #ff7373");
            }
        };
    }
}
