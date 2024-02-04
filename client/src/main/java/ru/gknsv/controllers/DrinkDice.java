package ru.gknsv.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.springframework.http.MediaType;
import ru.gknsv.dto.AlcoDto;
import ru.gknsv.dto.AlcoHistoryDto;
import ru.gknsv.dto.UserDto;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class DrinkDice {

    private AlcoDto alcoDto;

    private CalculationSide calculationSide;

    private HBox drinkDice;
    @FXML
    public Label alcoTitle;
    @FXML
    public Label alcoStrength;
    @FXML
    public TextField alcoVolume;
    @FXML
    public Button addButton;
    @FXML
    public Button deleteButton;

    public void setData(AlcoDto alcoDto, CalculationSide calculationSide, HBox drinkDice) {
        this.alcoDto = alcoDto;
        this.calculationSide = calculationSide;
        this.drinkDice = drinkDice;
        alcoTitle.setText(alcoDto.getTitle());
        alcoStrength.setText(alcoDto.getStrength() + "%");
    }

    public void addToHistory(ActionEvent actionEvent) throws IOException, InterruptedException {
        long interval;
        AlcoHistoryDto alcoHistoryDto = new AlcoHistoryDto();
        alcoHistoryDto.setVolume(alcoVolume.getText());
        alcoHistoryDto.setUserId(calculationSide.getModel().getSessionUser().getId());
        alcoHistoryDto.setAlcoId(alcoDto.getId());
        alcoHistoryDto.setTitle(alcoDto.getTitle());
        alcoHistoryDto.setPictureId(alcoDto.getPictureId());
        alcoHistoryDto.setStrength(alcoDto.getStrength());
        alcoHistoryDto.setId(alcoDto.getId());

        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8085/history/add"))
                .timeout(Duration.ofSeconds(15))
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(alcoHistoryDto)))
                .build();

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_NONE))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        if (response.statusCode() == 200) {
            interval = parseTime(response.body());
            calculationSide.getModel().setHungry(calculationSide.toggleSwitch.isSelected());
            calculationSide.getModel().addAclo(alcoHistoryDto, interval);
            calculationSide.displayInformation();
            calculationSide.displayHungry();

            addButton.setVisible(false);
            deleteButton.setVisible(false);
            alcoVolume.setEditable(false);
        }
    }

    private Long parseTime(String group) {
        try {
            return new ObjectMapper().readValue(group, long.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public void deleteIntake(ActionEvent actionEvent) {
        calculationSide.drinkContainer.getChildren().remove(drinkDice);
    }
}
