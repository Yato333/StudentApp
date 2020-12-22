package dev.dmitrij.kuzmiciov.app.controller.dialog;

import dev.dmitrij.kuzmiciov.app.controller.Controller;
import dev.dmitrij.kuzmiciov.app.data.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import dev.dmitrij.kuzmiciov.app.util.Regexes;

import java.util.ArrayList;

public class AddGroupDialogController extends Controller {
    private static AddGroupDialogController instance;
    
    private ArrayList<Student> students;
    @FXML
    private TextField nameField;
    @FXML
    private VBox studentView;
    
    @Override @FXML
    protected void initialize() {
        instance = this;
        students = new ArrayList<>();
    }

    public String getName() {
        return nameField.getText();
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    @FXML
    private void onAddStudentButton() {
        if(studentView.getChildren().stream().filter(node -> node instanceof TextField).toArray().length > 0)
            return;

        var studentInput = new TextField();
        studentInput.setPromptText("John Smith");
        studentInput.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                if(studentInput.getText().matches(Regexes.NAME_EN.regex)) {
                    String studentName = studentInput.getText();
                    studentView.getChildren().remove(studentInput);
                    var firstAndLastName = studentName.split(" ");
                    students.add(new Student(firstAndLastName[0], firstAndLastName[1]));
                    studentView.getChildren().add(new Label(studentName));
                } else
                    studentInput.setText("");
            }
        });
        studentView.getChildren().add(studentInput);
    }

    public static AddGroupDialogController getInstance() {
        return instance;
    }
}