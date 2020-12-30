package dev.dmitrij.kuzmiciov.app.controller;

import dev.dmitrij.kuzmiciov.app.App;
import dev.dmitrij.kuzmiciov.app.RootTable;
import dev.dmitrij.kuzmiciov.app.data.Group;
import dev.dmitrij.kuzmiciov.app.data.Student;
import dev.dmitrij.kuzmiciov.app.data.StudyYear;
import dev.dmitrij.kuzmiciov.app.util.LTDateConverter;
import dev.dmitrij.kuzmiciov.app.util.factory.RedWeekendDaysDayCellFactory;
import dev.dmitrij.kuzmiciov.app.util.Regexes;
import dev.dmitrij.kuzmiciov.app.util.EventHandlers;
import dev.dmitrij.kuzmiciov.app.util.file.Loader;
import dev.dmitrij.kuzmiciov.app.util.file.Saver;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import org.jetbrains.annotations.Nullable;

import java.time.Year;
import java.time.YearMonth;
import java.util.Comparator;

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
        editGroupButton,
        removeGroupButton;

    // Controls for entering marks for a certain day
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button setMarksButton;

    // Table Area nodes
    private TextField groupName;
    private ComboBox<YearMonth> monthPicker;
    private RootTable table;

    /**
     * Default constructor, required by implSpec
     */
    public RootController() {
        instance = this;
    }

    private BorderPane createTableHeader() {
        var tableHeader = new BorderPane();

        // Handling group name label
        groupName = new TextField();
        groupName.setAlignment(Pos.CENTER);
        groupName.setMaxWidth(500);
        var groupNameDefaultStyle = groupName.getStyle();
        var groupNameStyle = "-fx-background-color: transparent; -fx-background-insets: 0px";
        groupName.setStyle(groupNameStyle);
        groupName.setEditable(false);

        // Making it commit change on ENTER
        groupName.setOnKeyPressed(EventHandlers.commitOnEnterHandler(groupName));

        // Handling group name editing
        groupName.focusedProperty().addListener((obsVal, wasFocused, nowFocused) -> {
            if(!nowFocused) {
                if(groupName.getText().strip().matches(Regexes.GROUP_EN.regex)) {
                    var name = groupName.getText().strip();
                    groupChoiceBox.getValue().setName(name);

                    groupName.setStyle(groupNameStyle);
                    groupName.setEditable(false);

                    var selModel = groupChoiceBox.getSelectionModel();
                    var selectedItem = selModel.getSelectedItem();
                    selModel.clearSelection();
                    groupChoiceBox.getItems().sort(Comparator.comparing(Group::getName));
                    selModel.select(selectedItem);
                } else {
                    groupName.clear();
                    groupName.requestFocus();
                }
            }
        });

        // Constricting input size
        groupName.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() > Group.MAX_NAME_LENGTH) {
                if(newValue.length() == Group.MAX_NAME_LENGTH + 1)
                    groupName.deletePreviousChar();
                else
                    groupName.setText(groupName.getText().substring(0, 30));
            }
        });

        tableHeader.setCenter(groupName);

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
        editButton.visibleProperty().bind(groupChoiceBox.valueProperty().isNotNull());
        tableHeader.setLeft(editButton);

        monthPicker = new ComboBox<>();
        monthPicker.setItems(App.getCurrentYear().getTotalMonthsUnmodifiable());
        monthPicker.getSelectionModel().selectFirst();
        monthPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && !YearMonth.of(datePicker.getValue().getYear(), datePicker.getValue().getMonth()).equals(newValue))
                datePicker.setValue(newValue.atDay(1));
        });
        App.currentYearProperty().addListener((observable, oldValue, newValue) -> {
            monthPicker.setItems(App.getCurrentYear().getTotalMonthsUnmodifiable());
            monthPicker.getSelectionModel().selectFirst();
        });

        var setStudyYearButton = new Button("Set Study Year");
        setStudyYearButton.setOnAction(e -> {
            ObservableList<Year> yearList = FXCollections.observableArrayList();
            for(Year year = Year.now().minusYears(5); !year.isAfter(Year.now().plusYears(5)); year = year.plusYears(1))
                yearList.add(year);

            var yearSelector = new ComboBox<>(yearList);
            yearSelector.getSelectionModel().select(Year.now());
            yearSelector.setVisibleRowCount(10);

            Dialog<Year> dialog = new Dialog<>();
            dialog.setHeaderText("Select Study Year");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.getDialogPane().setContent(new StackPane(yearSelector));
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dialog.setResultConverter(buttonType -> {
                if(buttonType == ButtonType.OK)
                    return yearSelector.getValue();
                return null;
            });

            var resultYear = dialog.showAndWait();
            resultYear.ifPresent(year -> App.setCurrentYear(new StudyYear(year.getValue())));
        });

        var monthPickerWrapper = new VBox(10, setStudyYearButton, monthPicker);
        monthPickerWrapper.visibleProperty().bind(groupChoiceBox.valueProperty().isNotNull());

        tableHeader.setRight(monthPickerWrapper);

        return tableHeader;
    }

    /**
     * {@inheritDoc}
     */
    @Override @FXML
    protected void initialize() {
        // Assigning toolbar buttons their actions
        openButton.setOnAction(e -> Loader.load());
        saveButton.setOnAction(e -> Saver.save());
        closeButton.setOnAction(e -> App.close());

        var tableHeader = createTableHeader();

        // Creating the table
        table = new RootTable(monthPicker);
        table.setEditable(true);

        // Adding the table area to the parent node
        var tableWrapper = new VBox(10, tableHeader, table);
        root.setCenter(tableWrapper);
        BorderPane.setMargin(tableWrapper, new Insets(20));

        // Making sure that only the table expands vertically
        VBox.setVgrow(groupName, Priority.NEVER);
        VBox.setVgrow(table, Priority.ALWAYS);

        // Configuring groupChoiceBox
        groupChoiceBox.setPromptText("Select Group");
        groupChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null) {
                groupName.clear();
                table.setItems(FXCollections.emptyObservableList());
            } else {
                groupName.setText(newValue.getName());
                table.setItems(newValue.getStudents());
            }
        });

        // Configuring the date picker
        datePicker.disableProperty().bind(groupChoiceBox.valueProperty().isNull());
        datePicker.setConverter(new LTDateConverter());
        datePicker.setValue(monthPicker.getValue().atDay(1));
        datePicker.setOnKeyPressed(EventHandlers.commitOnEnterHandler(datePicker));
        datePicker.setDayCellFactory(new RedWeekendDaysDayCellFactory());
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && !monthPicker.getValue().equals(YearMonth.of(newValue.getYear(), newValue.getMonth()))) {
                var matchingMonths = monthPicker.getItems().filtered(yearMonth -> yearMonth.getMonth().equals(newValue.getMonth()));
                assert (matchingMonths.size() <= 1); // if the condition is false then something went wrong

                if(matchingMonths.size() == 0)
                    datePicker.setValue(monthPicker.getValue().atDay(1));
                else
                    monthPicker.setValue(YearMonth.of(newValue.getYear(), newValue.getMonth()));
            }
        });

        // Handle button availability for the user
        setMarksButton.disableProperty().bind(
                groupChoiceBox.valueProperty().isNull().or (
                datePicker.valueProperty().isNull()).or (
                monthPicker.valueProperty().isNull())
        );

        addStudentButton.disableProperty().bind(groupChoiceBox.valueProperty().isNull());
        editGroupButton.disableProperty().bind(groupChoiceBox.valueProperty().isNull());
        removeGroupButton.disableProperty().bind(groupChoiceBox.valueProperty().isNull());
    }

    public @Nullable Group getCurrentGroup() {
        return groupChoiceBox.getValue();
    }

    @FXML
    private void onAddGroupButton() {
        groupChoiceBox.getItems().add(new Group(String.valueOf(groupChoiceBox.getItems().size() + 1)));
        groupChoiceBox.getSelectionModel().selectLast();
    }

    @FXML
    private void onSetMarksButton() {

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

        dialog.setOnShowing(e -> Platform.runLater(firstNameField::requestFocus));
        var student = dialog.showAndWait();
        student.ifPresent(student1 -> {
            groupChoiceBox.getValue().getStudents().add(student1);
            table.refresh();
        });
    }

    @FXML
    private void onEditGroupButton() {
        // TODO: make edit group window
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
                if(groupChoiceBox.getItems().isEmpty())
                    groupChoiceBox.getSelectionModel().clearSelection();
                else if(groupChoiceBox.getSelectionModel().getSelectedIndex() > 0)
                    groupChoiceBox.getSelectionModel().selectPrevious();
                else
                    groupChoiceBox.getSelectionModel().selectNext();
            }
        });
    }
}
