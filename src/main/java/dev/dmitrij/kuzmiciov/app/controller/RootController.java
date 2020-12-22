package dev.dmitrij.kuzmiciov.app.controller;

import dev.dmitrij.kuzmiciov.app.App;
import dev.dmitrij.kuzmiciov.app.data.Group;
import dev.dmitrij.kuzmiciov.app.data.Groups;
import dev.dmitrij.kuzmiciov.app.dialog.AddGroupDialog;
import dev.dmitrij.kuzmiciov.app.util.file.Loader;
import dev.dmitrij.kuzmiciov.app.util.file.Saver;
import dev.dmitrij.kuzmiciov.app.util.shape.BorderRect;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.text.DecimalFormat;

public final class RootController extends Controller {
    private static RootController instance;
    private static final DecimalFormat DAY_FORMAT = new DecimalFormat("00");

    @FXML
    private MenuItem
        openButton,
        saveButton,
        closeButton;
    
    @FXML
    private ChoiceBox<Group>
        groupChoiceBox;
    private Group previousGroup = null;

    @FXML
    private GridPane table;

    Dialog<Boolean>
        addGroupDialog;

    @Override @FXML
    protected void initialize() throws InitError {
        instance = this;
        try {
            openButton.setOnAction(e -> Loader.load());
            saveButton.setOnAction(e -> Saver.save());
            closeButton.setOnAction(e -> App.close());

            createTable();
            groupChoiceBox.setOnHiding(e -> {
                if(groupChoiceBox.getValue() != previousGroup) {
                    setGroup(groupChoiceBox.getValue());
                    previousGroup = groupChoiceBox.getValue();
                }
            });

            addGroupDialog = new AddGroupDialog();

            // Making sure the table elements do not clip
            Platform.runLater(() -> {
                App.getPrimaryStage().setMinWidth(table.getWidth() + 300);
                App.getPrimaryStage().setMinHeight(table.getHeight());
            });
        } catch(Exception e) {
            throw new InitError(e);
        }
    }

    @FXML
    private void onAddGroupButton() {
        try {
            boolean invalidName;
            do {
                var optionalResult = addGroupDialog.showAndWait();
                invalidName = optionalResult.orElse(false);

                if (invalidName)
                    new Alert(Alert.AlertType.ERROR, "Group name is invalid", ButtonType.OK).showAndWait();
            } while(invalidName);
        } catch (Exception exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    private void createTable() {
        int x = 0, y = 0;

        for(; x < 31 + 2; ++x) {
            var pane = new StackPane();
            Rectangle rect;
            Label label = new Label();

            if(x < 2) {
                rect = BorderRect.NAME.clone();
                label.setText(x == 0 ? "Student Name" : "Total Missed");
            }
            else {
                rect = BorderRect.NUMBER.clone();
                label.setText(DAY_FORMAT.format(x - 1));
            }

            pane.getChildren().addAll(rect, label);
            table.add(pane, x, y);
        }
    }
    
    private void clearTable() {
        table.getRowConstraints().remove(1, table.getRowCount());
    }
    
    public void setGroup(Group group) {
        if (!Groups.contains(group))
            throw new IllegalArgumentException("This group does not belong to the list inside Groups class.");
        
        clearTable();
        
        int rowIndex = 1;
        for(var student : group) {
            table.addRow(rowIndex++,
                    new StackPane(BorderRect.NAME, new Label(String.join(" ", student.getFirstName(), student.getLastName()))),
                    new StackPane(BorderRect.NUMBER, new Label(String.valueOf(0)))
            );
        }
        for(var row : table.getRowConstraints())
            row.setMaxHeight(30);
            
        previousGroup = group;
    }

    public static RootController getInstance() {
        return instance;
    }
    public GridPane getTable() {
        return table;
    }

    public ChoiceBox<Group> getGroupChoiceBox() {
        return groupChoiceBox;
    }
}
