package ru.gknsv.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ru.gknsv.Model;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthorizationPage implements Initializable {

    public ImageView pic;
    private Model model;
    private Stage stage;
    private Scene scene;
    private Parent root;

    public AuthorizationPage(Model model) {
        this.model = model;
    }

    public void toLoginPage(ActionEvent actionEvent) throws Exception {
        switchPage(actionEvent, "ru/gknsv/LoginPage.fxml", LoginPage.class);
    }

    public void toRegistrationPage(ActionEvent actionEvent) throws Exception {
        switchPage(actionEvent, "ru/gknsv/RegistrationPage.fxml", RegistrationPage.class);
    }

    private void switchPage(ActionEvent actionEvent, String string, Class controller) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(string));

        Constructor constructor = controller.getConstructor(Model.class);
        loader.setControllerFactory(controllerClass -> {
            try {
                return constructor.newInstance(model);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        root = loader.load();
        scene = new Scene(root);
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image("ru/gknsv/logo-text.png", 140, 140, true, true);
        pic.setImage(img);
    }
}
