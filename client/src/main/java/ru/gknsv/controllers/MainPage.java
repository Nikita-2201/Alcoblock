package ru.gknsv.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.gknsv.Model;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPage implements Initializable {
    @FXML
    public Label userLogin;
    @FXML
    public ToggleButton drinkButton;
    @FXML
    public ToggleButton historyButton;
    @FXML
    public ToggleButton settingsButton;
    @FXML
    public ImageView firstLogo;
    @FXML
    public ImageView secondLogo;
    @FXML
    public ImageView avatar;

    private ToggleGroup toggleGroup;

    private Model model;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private StackPane contentArea;

    public MainPage(Model model) {
        this.model = model;
    }


    public void toCalculationSide(ActionEvent actionEvent) throws Exception {
        switchSide("ru/gknsv/CalculationSide.fxml", CalculationSide.class);
    }

    public void toHistorySide(ActionEvent actionEvent) throws Exception {
        switchSide("ru/gknsv/HistorySide.fxml", HistorySide.class);
    }

    public void toSettingsSide(ActionEvent actionEvent) throws Exception {
        switchSide("ru/gknsv/SettingsSide.fxml", SettingsSide.class);
    }

    private void switchSide(String sideName, Class controller) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(sideName));

        Constructor constructor = controller.getConstructor(Model.class);
        loader.setControllerFactory(controllerClass -> {
            try {
                return constructor.newInstance(model);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Parent fxml = loader.load();

        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);
    }

    public void toAuthorizationPage(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ru/gknsv/AuthorizationPage.fxml"));
        loader.setControllerFactory(controllerClass -> new AuthorizationPage(model));
        root = loader.load();
        scene = new Scene(root);
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model.clearAlcoList();
        model.clearAmountAlcoholInBlood();
        Image first = new Image("ru/gknsv/firstLogo.png", 100, 100, true, true);
        Image second = new Image("ru/gknsv/secondLogo.png", 100, 100, true, true);
        Image third = new Image("ru/gknsv/userLogo.png", 100, 100, true, true);
        firstLogo.setImage(first);
        secondLogo.setImage(second);
        avatar.setImage(third);
        toggleGroup = new ToggleGroup();
        drinkButton.setToggleGroup(toggleGroup);
        historyButton.setToggleGroup(toggleGroup);
        settingsButton.setToggleGroup(toggleGroup);
        drinkButton.setSelected(true);
        try {
            switchSide("ru/gknsv/CalculationSide.fxml", CalculationSide.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        userLogin.setText(model.getSessionUser().getLogin());
    }
}
