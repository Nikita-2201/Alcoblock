package ru.gknsv.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.http.MediaType;
import ru.gknsv.Model;
import ru.gknsv.animations.ToggleSwitch;
import ru.gknsv.dto.UserDto;

import java.io.IOException;
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

public class SettingsSide implements Initializable {

    private ToggleSwitch toggleSwitch;
    @FXML
    public PasswordField newPassword;
    @FXML
    public TextField heightField;
    @FXML
    public TextField weightField;
    @FXML
    public TextField experienceField;
    @FXML
    public AnchorPane spawnButton;
    @FXML
    public Label sexName;
    private Model model;

    public SettingsSide(Model model) {
        this.model = model;
    }

    public void applyChanges(ActionEvent actionEvent) throws IOException, InterruptedException {

        UserDto dto = getChanges();

        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8085/user/update"))
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
            model.setSessionUser(dto);
        }

    }

    private UserDto getChanges() {
        UserDto userDto = model.getSessionUser();
        if (!newPassword.getText().equals("")) {
            userDto.setPassword(newPassword.getText());
        }
        if (!heightField.getText().equals("")) {
            userDto.setHeight(heightField.getText());
        }
        if (!weightField.getText().equals("")) {
            userDto.setWeight(weightField.getText());
        }
        if (!experienceField.getText().equals("")) {
            userDto.setExperience(experienceField.getText());
        }
        userDto.setSex(toggleSwitch.isSelected() ? "female" : "male");

        return userDto;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        heightField.setText(model.getSessionUser().getHeight());
        weightField.setText(model.getSessionUser().getWeight());
        experienceField.setText(model.getSessionUser().getExperience());

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
        if (model.getSessionUser().getSex().equals("male")) {
            sexName.setText("Мужчина");
        } else {
            toggleSwitch.switchButton();
            sexName.setText("Женщина");
        }

        //sexField.setSelected(model.getSessionUser().getSex().equals("male"));
    }
}
