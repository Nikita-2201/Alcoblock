package ru.gknsv.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.gknsv.Model;
import ru.gknsv.dto.UserDto;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class LoginPage {

    private Model model;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private UserDto currentUser;
    private String login;
    private String password;

    public LoginPage(Model model) {
        this.model = model;
    }

    public void toAuthorizationPage(ActionEvent actionEvent) throws Exception {
        switchPage(actionEvent, "ru/gknsv/AuthorizationPage.fxml", AuthorizationPage.class);
    }

    public void login(ActionEvent actionEvent) throws Exception {
        login = loginField.getText();
        password = passwordField.getText();
        if (isUserExist()) {
            model.setSessionUser(currentUser);
            switchPage(actionEvent, "ru/gknsv/MainPage.fxml", MainPage.class);
        } else {
            showAlertWindow();
        }
    }

    //SERVER
    private boolean isUserExist() throws IOException, InterruptedException {
        UserDto dto = new UserDto();
        dto.setLogin(login);
        dto.setPassword(password);

        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8085/auth/signin"))
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
        alertPageController.displayAlert("Неверный логин или пароль!");

        stage = new Stage();
        stage.setTitle("Hello!");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
