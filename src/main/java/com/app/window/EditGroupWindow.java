package com.app.window;

import com.app.controller.RootController;
import com.app.data.Group;
import com.app.data.Student;
import com.app.util.ReadOnlyTextField;
import com.app.util.Regexes;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class EditGroupWindow extends Dialog<Void> {
    private static EditGroupWindow instance;
    public static EditGroupWindow getInstance() {
        return instance;
    }

    private static final Header header = new Header();
    private final static Table table = new Table();

    public EditGroupWindow(@NotNull Group group) {
        instance = this;

        header.setGroup(group);
        table.setGroup(group);
        getDialogPane().setHeader(header);
        getDialogPane().setContent(table);
        getDialogPane().getButtonTypes().add(ButtonType.OK);
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Edit Group");
        update();
    }

    public void update() {
        setHeight(header.getHeight() + table.getHeight() + 150);
    }

    private static class Table extends GridPane {
        private final Button addStudentButton = new Button("Add Student");
        private Group group;

        public Table() {
            for (int i = 0; i < 3; i++)
                getColumnConstraints().add(new ColumnConstraints(100, 100, 200));
            add(new Label("First Name"), 1, 0);
            add(new Label("Last Name"), 2, 0);
            setStyle("-fx-vgap: 5; -fx-hgap: 5");
        }

        public void setGroup(Group group) {
            this.group = group;
            getChildren().removeIf(node -> getRowIndex(node) > 0);

            int i = 1;
            for(var student : group.getStudents()) {
                var firstNameField = new ReadOnlyTextField(student.getFirstName(), false, 30);
                var lastNameField = new ReadOnlyTextField(student.getLastName(), false, 30);
                var editButtons = new StudentEditButtons(student, firstNameField, lastNameField, this);
                addRow(i++, editButtons, firstNameField, lastNameField);
            }

            addStudentButton.setOnAction(e -> {
                var student = new Student();
                group.getStudents().add(student);
                var firstNameField = new ReadOnlyTextField("", false, 30);
                var lastNameField = new ReadOnlyTextField("", false, 30);
                var editButtons = new StudentEditButtons(student, firstNameField, lastNameField, this);
                int index = getRowIndex(addStudentButton);
                setRowIndex(addStudentButton, index + 1);
                add(editButtons, 0, index);
                add(firstNameField,1, index);
                add(lastNameField, 2, index);
                EditGroupWindow.getInstance().update();
                editButtons.editConfirmButton.fire();
            });
            add(addStudentButton,1 , i);
        }

        public void update() {
            setGroup(group);
        }

        private static class StudentEditButtons extends HBox {
            private static final Image
                EDIT_IMAGE = new Image("/img/edit_icon.png", 16, 16, true, true),
                DELETE_IMAGE = new Image("/img/delete_icon.png", 16, 16, true, true),
                CONFIRM_IMAGE = new Image("/img/confirm_icon.png", 16, 16, true, true);
            Button editConfirmButton;
            public StudentEditButtons(Student student, ReadOnlyTextField firstNameField, ReadOnlyTextField lastNameField, Table table1) {
                ImageView
                    editGraphic = new ImageView(EDIT_IMAGE),
                    deleteGraphic = new ImageView(DELETE_IMAGE),
                    confirmGraphic = new ImageView(CONFIRM_IMAGE);
                editConfirmButton = new Button("", editGraphic);
                Button deleteCancelButton = new Button("", deleteGraphic);

                AtomicBoolean editButtonActivated = new AtomicBoolean(false);
                editConfirmButton.setOnAction(e -> {
                    if(editButtonActivated.get()) {
                        String firstName = firstNameField.getText().strip(), lastName = lastNameField.getText().strip();
                        boolean valid = true;
                        if(!firstName.matches(Regexes.NAME_EN.regex)) {
                            firstNameField.clear();
                            valid = false;
                        }
                        if(!lastName.matches(Regexes.NAME_EN.regex)) {
                            lastNameField.clear();
                            valid = false;
                        }
                        if(valid) {
                            editConfirmButton.setGraphic(editGraphic);
                            editButtonActivated.set(false);
                            firstNameField.setReadOnly(true);
                            lastNameField.setReadOnly(true);
                            student.setFirstName(firstName);
                            student.setLastName(lastName);
                        }
                    } else {
                        editConfirmButton.setGraphic(confirmGraphic);
                        editButtonActivated.set(true);
                        firstNameField.setReadOnly(false);
                        lastNameField.setReadOnly(false);
                    }
                });

                deleteCancelButton.setOnAction(e -> {
                    if(editButtonActivated.get()) {
                        firstNameField.setText(student.getFirstName());
                        lastNameField.setText(student.getLastName());
                        firstNameField.setReadOnly(false);
                        lastNameField.setReadOnly(false);
                        editButtonActivated.set(false);
                        editConfirmButton.setGraphic(editGraphic);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING,
                                "Are you sure, you want to remove this student from this group?",
                                ButtonType.YES,
                                ButtonType.NO
                        );

                        alert.showAndWait().ifPresent(buttonType -> {
                            if(buttonType == ButtonType.YES) {
                                Objects.requireNonNull(RootController.getInstance().getCurrentGroup()).getStudents().remove(student);
                                table1.update();
                            }
                        });
                    }
                });

                getChildren().addAll(editConfirmButton, deleteCancelButton);
            }
        }
    }

    private static class Header extends HBox {
        private final static ReadOnlyTextField name = new ReadOnlyTextField("", true, 30);

        private ChangeListener<Boolean> groupNameChangeListener;

        public Header() {
            super(10);

            Button editButton = new Button("", new ImageView(new Image("/img/edit_icon.png", 16, 16, true, true)));
            editButton.setOnAction(e -> {
                name.setReadOnly(false);
                name.requestFocus();
            });

            getChildren().addAll(editButton, name);
            setAlignment(Pos.CENTER);
        }

        void setGroup(Group group) {
            name.setText(group.getName());
            if(groupNameChangeListener != null)
                name.focusedProperty().removeListener(groupNameChangeListener);
            groupNameChangeListener = (observable, oldValue, newValue) -> {
                if(!newValue) {
                    var text = name.getText();
                    if (text == null || text.isEmpty()) {
                        name.setReadOnly(false);
                        name.requestFocus();
                    } else {
                        group.setName(text.strip());
                        RootController.getInstance().updateGroupNames();
                    }
                }
            };
            name.focusedProperty().addListener(groupNameChangeListener);
        }
    }
}
