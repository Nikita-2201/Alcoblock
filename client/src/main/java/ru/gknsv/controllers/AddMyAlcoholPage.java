package ru.gknsv.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.http.MediaType;
import ru.gknsv.Model;
import ru.gknsv.dto.AlcoDto;


import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class AddMyAlcoholPage {

    private Model model;
    private CalculationSide calculationSide;
    @FXML
    public TextField title;
    @FXML
    public TextField strength;

    public void setModel(Model model) {
        this.model = model;
    }

    public void setCalculationSide(CalculationSide calculationSide) {
        this.calculationSide = calculationSide;
    }

    public void addMyAlco(ActionEvent actionEvent) throws IOException, InterruptedException {
        AlcoDto dto = new AlcoDto();
        dto.setTitle(title.getText());
        dto.setStrength(strength.getText());
        dto.setUserId(model.getSessionUser().getId());

        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8085/alco/add"))
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
            calculationSide.displayAlcoholList();
        }

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }


}
