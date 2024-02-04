package ru.gknsv.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.http.MediaType;
import ru.gknsv.Model;
import ru.gknsv.animations.ToggleSwitch;
import ru.gknsv.dto.UserDto;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ResourceBundle;

public class RegistrationPage implements Initializable {
    @FXML
    public AnchorPane spawnButton;
    @FXML
    public Label sexName;
    private Model model;
    @FXML
    public TextField experienceField;
    @FXML
    public TextField weightField;
    @FXML
    public TextField heightField;

    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField loginField;

    public ToggleSwitch toggleSwitch;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private UserDto currentUser;
    private String login;
    private String password;
    private String experience;
    private String sex;
    private String height;
    private String weight;

    public RegistrationPage(Model model) {
        this.model = model;
    }

    public void toAuthorizationPage(ActionEvent actionEvent) throws Exception {
        switchPage(actionEvent, "ru/gknsv/AuthorizationPage.fxml", AuthorizationPage.class);
    }

    public void registration(ActionEvent actionEvent) throws Exception {
        login = loginField.getText();
        password = passwordField.getText();
        experience = experienceField.getText();
        height = heightField.getText();
        weight = weightField.getText();
        sex = toggleSwitch.isSelected() ? "female" : "male";

        if (isUserExist()) {
            switchPage(actionEvent, "ru/gknsv/AuthorizationPage.fxml", AuthorizationPage.class);
        } else {
            showAlertWindow();
        }
    }

    //SERVER
    private boolean isUserExist() throws IOException, InterruptedException {
        UserDto dto = new UserDto();
        dto.setLogin(login);
        dto.setPassword(password);
        dto.setSex(sex);
        dto.setWeight(weight);
        dto.setHeight(height);
        dto.setExperience(experience);


        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8085/auth/signup"))
                .timeout(Duration.ofSeconds(15))
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(dto)))
                .build();

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_NONE))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        if (response.statusCode() == 200) {
            currentUser = parseUser(response.body());
            return currentUser != null ? true : false;
        } else {
            return false;
        }
    }

    private UserDto parseUser(String group) {
        try {
            return new ObjectMapper().readValue(group, UserDto.class);
        } catch (JsonProcessingException e) {
            return null;
        }
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

    private void showAlertWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ru/gknsv/AlertPage.fxml"));
        root = loader.load();
        scene = new Scene(root);

        AlertPage alertPageController = loader.getController();
        alertPageController.displayAlert("Пользователь с таким именем уже существует!");

        stage = new Stage();
        stage.setTitle("Hello!");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toggleSwitch = new ToggleSwitch();
        toggleSwitch.setScaleX(0.5);
        toggleSwitch.setScaleY(0.5);
        toggleSwitch.setOnMouseClicked(event -> {
            toggleSwitch.switchButton();
            if (toggleSwitch.isSelected()) {
                sexName.setText("Женщина");
            } else {
                sexName.setText("Мужчина");
            }
        });
        spawnButton.getChildren().add(toggleSwitch);
        sexName.setText("Мужчина");
    }
}
