package com.app;

import com.app.data.StudyYear;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

/**
 * Main class for this application.
 * @see javafx.application.Application
 * @author Dmitrij Kuzmiciov
 * @version 1.0
 */

public class App extends Application {
    private static App instance;
    private static Stage primaryStage;

    private static Pane root;

    private static void load() throws IOException {
        root = FXMLLoader.load(App.class.getResource("/fxml/root.fxml"));
    }

    private static final ObjectProperty<StudyYear> currentYear = new SimpleObjectProperty<>(new StudyYear(2019));
    public static ObjectProperty<StudyYear> currentYearProperty() {
        return currentYear;
    }
    public static StudyYear getCurrentYear() {
        return currentYear.get();
    }
    public static void setCurrentYear(StudyYear currentYear) {
        App.currentYear.set(currentYear);
    }

    @Override
    public void start(Stage primaryStage) {
        instance = this;
        App.primaryStage = primaryStage;

        try {
            load();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Student App");
            Platform.runLater(() -> {
                primaryStage.setMinHeight(720);
                primaryStage.setMinWidth(1280);
            });
        } catch (Exception e) {
            e.printStackTrace();
            Platform.exit();
        }

        primaryStage.show();
    }

    /**
     * @return a running instance of this class
     */
    public static App getInstance() {
        return instance;
    }

    /**
     * @return a primary {@link Stage} of the running instance
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Sends a request to close this application
     */
    public static void close() {
        primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
