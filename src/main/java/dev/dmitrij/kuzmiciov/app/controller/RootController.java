package dev.dmitrij.kuzmiciov.app.controller;

import dev.dmitrij.kuzmiciov.app.App;
import dev.dmitrij.kuzmiciov.app.RootTable;
import dev.dmitrij.kuzmiciov.app.data.Group;
import dev.dmitrij.kuzmiciov.app.util.file.Loader;
import dev.dmitrij.kuzmiciov.app.util.file.Saver;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.converter.FormatStringConverter;

public class RootController extends Controller {
    @FXML
    private MenuItem
        openButton,
        saveButton,
        closeButton;
    @FXML
    private ChoiceBox<Group>
        groupChoiceBox;
    @FXML
    private HBox
        root;

    private RootTable table;

    @Override @FXML
    protected void initialize() throws InitError {
        openButton.setOnAction(e -> Loader.load());
        saveButton.setOnAction(e -> Saver.save());
        closeButton.setOnAction(e -> App.close());

        table = new RootTable();
        root.getChildren().add(table);
        HBox.setHgrow(table, Priority.ALWAYS);
    }

    @FXML
    private void onAddGroupButton() {

    }

    public RootTable getTable() {
        return table;
    }
}
