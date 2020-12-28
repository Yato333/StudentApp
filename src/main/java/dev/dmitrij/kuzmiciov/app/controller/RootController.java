package dev.dmitrij.kuzmiciov.app.controller;

import dev.dmitrij.kuzmiciov.app.App;
import dev.dmitrij.kuzmiciov.app.RootTable;
import dev.dmitrij.kuzmiciov.app.data.Group;
import dev.dmitrij.kuzmiciov.app.data.Student;
import dev.dmitrij.kuzmiciov.app.util.Regexes;
import dev.dmitrij.kuzmiciov.app.util.file.Loader;
import dev.dmitrij.kuzmiciov.app.util.file.Saver;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Modality;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a Controller for the root of the {@link javafx.scene.Scene} of this {@link javafx.application.Application}
 * @see Controller
 * @see App
 */

public final class RootController extends Controller {
    private static RootController instance;
    public static RootController getInstance() {
        return instance;
    }

    // Nodes loaded by FXML loader
    @FXML
    private BorderPane root;
    @FXML
    private MenuItem
        openButton,
        saveButton,
        closeButton;
    @FXML
    private ComboBox<Group>
        groupChoiceBox;
    @FXML
    private Button
        addStudentButton,
        removeGroupButton;

    // Custom nodes added to the root after the FXML loading
    private TextField groupName;
    private RootTable table;

    // List bounded with groupChoiceBox items property
    //private final ArrayList<Group> groupList = new ArrayList<>();

    /**
     * Default constructor, required by implSpec
     */
    public RootController() {
        instance = this;
    }

    /**
     * {@inheritDoc}
     */
    @Override @FXML
    protected void initialize() {
        openButton.setOnAction(e -> Loader.load());
        saveButton.setOnAction(e -> Saver.save());
        closeButton.setOnAction(e -> App.close());

        groupName = new TextField();
        var groupNameDefaultStyle = groupName.getStyle();
        var groupNameStyle = "-fx-background-color: transparent; -fx-background-insets: 0px";
        groupName.setStyle(groupNameStyle);
        groupName.setEditable(false);

        // Making it commit change on ENTER
        groupName.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER)
                groupName.getParent().requestFocus();
        });

        // Handling group name editing
        groupName.focusedProperty().addListener((obsVal, wasFocused, nowFocused) -> {
            if(!nowFocused) {
                if(groupName.getText().strip().matches(Regexes.GROUP_EN.regex)) {
                    var name = groupName.getText().strip();
                    groupChoiceBox.getValue().setName(name);

                    var selModel = groupChoiceBox.getSelectionModel();
                    int prevIndex = selModel.getSelectedIndex();
                    selModel.clearSelection();
                    selModel.clearAndSelect(prevIndex);

                    groupName.setStyle(groupNameStyle);
                    groupName.setEditable(false);
                } else {
                    groupName.setText("");
                    groupName.requestFocus();
                }
            }
        });

        // Creating the 'edit group name' button
        var editImage = new Image("/img/edit_icon.png", 16, 16, true, true);
        var editButton = new Button();
        editButton.setGraphic(new ImageView(editImage));
        editButton.setVisible(false);
        editButton.setTooltip(new Tooltip("Edit group name"));
        editButton.setOnAction(e -> {
            groupName.setStyle(groupNameDefaultStyle);
            groupName.setEditable(true);
            groupName.requestFocus();
        });

        // Wrapping group name and the button in a HBox
        var groupNameWrapper = new HBox(10, editButton, groupName);
        groupNameWrapper.setAlignment(Pos.CENTER);

        // Creating the table
        table = new RootTable();
        table.setEditable(true);

        // Adding the table are to the parent node
        var tableWrapper = new VBox(10, groupNameWrapper, table);
        root.setCenter(tableWrapper);
        BorderPane.setMargin(tableWrapper, new Insets(20));

        // Making sure that only the table expands vertically
        VBox.setVgrow(groupName, Priority.NEVER);
        VBox.setVgrow(table, Priority.ALWAYS);

        // Binding groupList to observable list
        //groupChoiceBox.setItems(FXCollections.observableArrayList(groupList));
        groupChoiceBox.setPromptText("Select Group");

        // Handling group selection change
        groupChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != oldValue) {
                if(newValue == null) {
                    groupName.clear();
                    table.setItems(FXCollections.emptyObservableList());
                    editButton.setVisible(false);
                    addStudentButton.setDisable(true);
                    removeGroupButton.setDisable(true);
                } else {
                    groupName.setText(newValue.getName());
                    table.setItems(newValue.getStudents());
                    editButton.setVisible(true);
                    addStudentButton.setDisable(false);
                    removeGroupButton.setDisable(false);
                }
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
        TextField firstNameField = new TextField(), lastNameField = new TextField();
        firstNameField.setTooltip(new Tooltip("Has to start with a capital letter. (John)"));
        lastNameField.setTooltip(new Tooltip("Has to start with a capital letter. (Smith)"));

        var dialog = new Dialog<Student>();
        dialog.setTitle("Add a Student");
        dialog.initModality(Modality.APPLICATION_MODAL);

        var content = new GridPane();
        content.addRow(0, new Label("First Name"), firstNameField);
        content.addRow(1, new Label("Last Name"), lastNameField);
        content.setStyle("-fx-vgap: 10; -fx-hgap: 10");
        dialog.getDialogPane().setContent(content);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            String  firstName = firstNameField.getText().strip(),
                    lastName = lastNameField.getText().strip();
            boolean valid = true;

            if(!firstName.matches(Regexes.NAME_EN.regex)) {
                valid = false;
                Platform.runLater(firstNameField::clear);
            }
            if(!lastName.matches(Regexes.NAME_EN.regex)) {
                valid = false;
                Platform.runLater(lastNameField::clear);
            }

            if(!valid)
                event.consume();
        });
        // Disable the button if some of the fields are empty
        okButton.disableProperty().bind(firstNameField.textProperty().isEmpty().or(lastNameField.textProperty().isEmpty()));

        dialog.setResultConverter(buttonType -> {
           if(buttonType == ButtonType.OK)
               return new Student(firstNameField.getText(), lastNameField.getText());
           return null;
        });

        var student = dialog.showAndWait();
        student.ifPresent(student1 -> {
            groupChoiceBox.getValue().getStudents().add(student1);
            table.refresh();
        });
    }

    @FXML
    private void onRemoveGroupButton() {
        Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
        warning.initModality(Modality.APPLICATION_MODAL);
        warning.setTitle("Warning");
        warning.setHeaderText("Are you sure you want to remove this group?");

        var result = warning.showAndWait();
        result.ifPresent(buttonType -> {
            if(buttonType == ButtonType.OK) {
                groupChoiceBox.getItems().remove(groupChoiceBox.getValue());
                groupChoiceBox.getSelectionModel().clearSelection();
            }
        });
    }
}
