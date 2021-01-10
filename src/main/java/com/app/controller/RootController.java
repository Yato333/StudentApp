package com.app.controller;

import com.app.App;
import com.app.RootTable;
import com.app.data.Group;
import com.app.data.Student;
import com.app.data.StudyYear;
import com.app.util.EventHandlers;
import com.app.util.LTDateConverter;
import com.app.util.Regexes;
import com.app.util.factory.RedWeekendDaysDayCellFactory;
import com.app.util.file.Loader;
import com.app.util.file.Saver;
import com.app.window.EditGroupWindow;
import com.app.window.SetMarksForDateWindow;
import com.app.window.SetMarksForStudentWindow;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import org.jetbrains.annotations.Nullable;

import java.time.YearMonth;
import java.util.Comparator;
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
    private ComboBox<Student>
        studentSelector;
    @FXML
    private Button
        setMarksForDateButton,
        setMarksForStudentButton,
        addStudentButton,
        editGroupButton,
        removeGroupButton;
    @FXML
    private DatePicker datePicker;

    // Table area nodes
    private Label groupName;
    private ComboBox<YearMonth> monthPicker;
    private RootTable table;

    public ObjectProperty<Group> currentGroupProperty() {
        return groupChoiceBox.valueProperty();
    }
    public @Nullable Group getCurrentGroup() {
        return currentGroupProperty().get();
    }
    public ComboBox<Group> getGroupChoiceBox() {
        return groupChoiceBox;
    }
    public DatePicker getDatePicker() {
        return datePicker;
    }

    /**
     * {@inheritDoc}
     */
    @Override @FXML
    protected void initialize() {
        instance = this;

        // Assigning toolbar buttons their actions
        openButton.setOnAction(e -> Loader.load());
        saveButton.setOnAction(e -> Saver.save());
        closeButton.setOnAction(e -> App.close());

        // Creating the area at the top of the table(Group name, edit name button, study year selector)
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
                groupName.setText("");
                table.setItems(FXCollections.emptyObservableList());
                studentSelector.getItems().clear();
            } else {
                groupName.setText(newValue.getName());
                table.setItems(newValue.getStudents());
                studentSelector.setItems(newValue.getStudents());
                studentSelector.getSelectionModel().selectFirst();
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

        // If no group is selected in the group selector then these buttons should be disabled
        setMarksForDateButton.disableProperty().bind(
                groupChoiceBox.valueProperty().isNull().or (
                        datePicker.valueProperty().isNull()).or (
                        monthPicker.valueProperty().isNull())
        );
        studentSelector.disableProperty().bind(groupChoiceBox.valueProperty().isNull());
        setMarksForStudentButton.disableProperty().bind(studentSelector.valueProperty().isNull().or(monthPicker.valueProperty().isNull()));
        addStudentButton.disableProperty().bind(groupChoiceBox.valueProperty().isNull());
        editGroupButton.disableProperty().bind(groupChoiceBox.valueProperty().isNull());
        removeGroupButton.disableProperty().bind(groupChoiceBox.valueProperty().isNull());

        addDataForTesting();
    }

    private void addDataForTesting() {
        groupChoiceBox.getItems().add(new Group("JAVA", List.of(new Student("Dmitrij", "Kuzmiciov"), new Student("Petras", "Jonaitis"))));
        groupChoiceBox.getSelectionModel().selectFirst();
    }

    /**
     * Creates and returns a node, representing the area at the top of the table
     */
    private BorderPane createTableHeader() {
        var tableHeader = new BorderPane();

        // Configuring the group name Control
        groupName = new Label();
        groupName.setAlignment(Pos.CENTER);
        groupName.setMaxWidth(500);

        tableHeader.setCenter(groupName);

        // Configuring the month picker
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

        // Configuring the 'set study year' button
        var setStudyYearButton = new Button("Set Study Year");
        setStudyYearButton.setOnAction(e -> onSetStudyYearButton());

        // Putting all the nodes in a Pane
        var monthPickerWrapper = new VBox(10, setStudyYearButton, monthPicker);
        monthPickerWrapper.visibleProperty().bind(groupChoiceBox.valueProperty().isNotNull());

        tableHeader.setRight(monthPickerWrapper);

        return tableHeader;
    }

    public void updateGroupNames() {
        var selModel = groupChoiceBox.getSelectionModel();
        var selectedItem = selModel.getSelectedItem();
        selModel.clearSelection();
        groupChoiceBox.getItems().sort(Comparator.comparing(Group::getName));
        selModel.select(selectedItem);
    }

    @FXML
    private void onAddGroupButton() {
        groupChoiceBox.getItems().add(new Group(String.valueOf(groupChoiceBox.getItems().size() + 1)));
        groupChoiceBox.getSelectionModel().selectLast();
    }

    private void updateTable() {
        final int currentMonth = monthPicker.getSelectionModel().getSelectedIndex();
        final var currentDate = datePicker.getValue();
        if(currentMonth == 0)
            monthPicker.getSelectionModel().select(1);
        else
            monthPicker.getSelectionModel().selectFirst();
        Platform.runLater(() -> {
            monthPicker.getSelectionModel().select(currentMonth);
            datePicker.setValue(currentDate);
        });
    }

    @FXML
    private void onSetMarksForDateButton() {
        new SetMarksForDateWindow(datePicker.getValue()).showAndWait();
        updateTable();
    }

    @FXML
    private void onSetMarksForStudentButton() {
        new SetMarksForStudentWindow(studentSelector.getValue(), monthPicker.getValue()).showAndWait();
        updateTable();
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
        new EditGroupWindow(groupChoiceBox.getValue()).showAndWait();
        groupChoiceBox.getValue().getStudents().sort(Comparator.comparing(Student::getLastName).thenComparing(Student::getFirstName));
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

    private void onSetStudyYearButton() {
        ObservableList<StudyYear> yearList = FXCollections.observableArrayList();
        for(int y = App.getCurrentYear().startDate.getYear() - 5; y < App.getCurrentYear().endDate.getYear() + 5; ++y)
            yearList.add(new StudyYear(y));

        var yearSelector = new ComboBox<>(yearList);
        yearSelector.getSelectionModel().select(App.getCurrentYear());

        yearSelector.setVisibleRowCount(10);

        Dialog<StudyYear> dialog = new Dialog<>();
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
        resultYear.ifPresent(App::setCurrentYear);
    }

    public YearMonth getCurrentMonth() {
        return monthPicker.getValue();
    }
}
