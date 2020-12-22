package dev.dmitrij.kuzmiciov.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import dev.dmitrij.kuzmiciov.app.util.Language;

public class App extends Application {
    public static final Language language = Language.EN;
    
    private static App instance;
    private static Stage primaryStage;
    
    @Override
    public void start(Stage primaryStage) {
        instance = this;
        App.primaryStage = primaryStage;

        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/fxml/root.fxml"));
            primaryStage.setScene(new Scene(parent));
            primaryStage.setTitle("Student App");
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    public static App getInstance() {
        return instance;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void close() {
        if(primaryStage != null)
            primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
