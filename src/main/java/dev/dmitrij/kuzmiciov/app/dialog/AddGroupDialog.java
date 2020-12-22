package dev.dmitrij.kuzmiciov.app.dialog;

import dev.dmitrij.kuzmiciov.app.controller.RootController;
import dev.dmitrij.kuzmiciov.app.controller.dialog.AddGroupDialogController;
import dev.dmitrij.kuzmiciov.app.data.Groups;
import dev.dmitrij.kuzmiciov.app.util.Regexes;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class AddGroupDialog extends Dialog<Boolean> {
    public AddGroupDialog() throws IOException {
        getDialogPane().setContent(FXMLLoader.load(getClass().getResource("/fxml/addGroupDialog.fxml")));
        getDialogPane().getButtonTypes().add(ButtonType.OK);
        setTitle("Add a new group");

        AtomicReference<Boolean> invalidName = new AtomicReference<>(false);

        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                var contr = AddGroupDialogController.getInstance();
                if (contr.getName().matches(Regexes.GROUP_EN.regex)) {
                    var group = Groups.addGroup(contr.getName(), contr.getStudents());
                    var root = RootController.getInstance();
                    root.getGroupChoiceBox().getItems().add(group);
                    if(root.getGroupChoiceBox().getValue() == null)
                        root.getGroupChoiceBox().setValue(group);
                    root.setGroup(group);
                } else
                    invalidName.set(true);
            }
            return invalidName.get();
        });
    }
}
