package ru.gknsv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.gknsv.controllers.AuthorizationPage;

import java.io.IOException;


public class JavaFXApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Model model = new Model();
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFXApplication.class.getResource("AuthorizationPage.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> new AuthorizationPage(model));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}