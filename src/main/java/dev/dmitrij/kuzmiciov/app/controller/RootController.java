package dev.dmitrij.kuzmiciov.app.controller;

import dev.dmitrij.kuzmiciov.app.App;
import dev.dmitrij.kuzmiciov.app.RootTable;
import dev.dmitrij.kuzmiciov.app.data.Group;
import dev.dmitrij.kuzmiciov.app.util.file.Loader;
import dev.dmitrij.kuzmiciov.app.util.file.Saver;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public final class RootController extends Controller {
    @FXML
    private BorderPane
            root;
    @FXML
    private MenuItem
        openButton,
        saveButton,
        closeButton;
    @FXML
    private ComboBox<Group>
        groupChoiceBox;

    private TextField groupName;
    private RootTable table;

    private final ArrayList<Group> groupList = new ArrayList<>();

    @Override @FXML
    protected void initialize() {
        openButton.setOnAction(e -> Loader.load());
        saveButton.setOnAction(e -> Saver.save());
        closeButton.setOnAction(e -> App.close());

        groupName = new TextField();
        groupName.setStyle("-fx-alignment: center; -fx-background-color: transparent; -fx-background-insets: 0px");
        groupName.setEditable(false);

        table = new RootTable();
        table.setEditable(true);

        var vbox = new VBox(10, groupName, table);
        root.setCenter(vbox);

        // Making sure that only the table expands vertically
        VBox.setVgrow(groupName, Priority.NEVER);
        VBox.setVgrow(table, Priority.ALWAYS);


        // Binding groupList to observable list
        groupChoiceBox.setItems(FXCollections.observableArrayList(groupList));
        groupChoiceBox.setPromptText("Select Group");

        // Handling group selection change
        groupChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != oldValue) {
                if(oldValue == null)
                    groupName.textProperty().unbind();
                else
                    groupName.textProperty().unbindBidirectional(oldValue.nameProperty());

                if(newValue == null) {
                    groupName.clear();
                    groupName.setEditable(false);
                } else {
                    groupName.textProperty().bindBidirectional(newValue.nameProperty());
                    table.setItems(FXCollections.observableList(newValue.getStudents()));
                    if (!groupName.isEditable())
                        groupName.setEditable(true);
                }
            }
        });
        groupName.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER)
                Platform.runLater(() -> groupName.getParent().requestFocus());
        });
        groupName.focusedProperty().addListener((obsVal, wasFocused, nowFocused) -> {
            if(!nowFocused) {
                var selModel = groupChoiceBox.getSelectionModel();
                int prevIndex = selModel.getSelectedIndex();
                selModel.clearSelection();
                selModel.clearAndSelect(prevIndex);
            }
        });
    }

    public RootTable getTable() {
        return table;
    }

    @FXML
    private void onAddGroupButton() {
        groupChoiceBox.getItems().add(new Group(String.valueOf(groupChoiceBox.getItems().size() + 1)));
        groupChoiceBox.getSelectionModel().selectLast();
    }

    @FXML
    private void onAddStudentButton() {

    }
}
