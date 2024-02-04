package ru.gknsv.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.springframework.http.MediaType;
import ru.gknsv.Model;
import ru.gknsv.dto.AlcoDto;
import ru.gknsv.dto.RealImageDto;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class HistorySide implements Initializable {
    @FXML
    public GridPane historyContainer;
    private Model model;

    private ArrayList<Date> historyList;

    public HistorySide(Model model) {
        this.model = model;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            displayHistoryDices();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void displayHistoryDices() throws IOException, InterruptedException {
        historyContainer.getChildren().clear();
        try {
            historyList = new ArrayList<>(getHistoryList());
        } catch (Exception e) {
            historyList = new ArrayList<>();
        }

        int column = 0;
        int row = 1;

        for (Date date : historyList) {
            VBox historyBox = setHsitoryDice(date);
            if (column == 6) {
                column = 0;
                ++row;
            }
            historyContainer.add(historyBox, column, row);
            ++column;
            GridPane.setMargin(historyBox, new Insets(10));
        }


    }

    private VBox setHsitoryDice(Date date) throws IOException, InterruptedException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ru/gknsv/HistoryDice.fxml"));
        VBox historyBox = fxmlLoader.load();
        HistoryDice historyDice = fxmlLoader.getController();
        historyDice.setData(date);
        return historyBox;
    }

    private ArrayList<Date> getHistoryList() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8085/history/" + model.getSessionUser().getId()))
                .timeout(Duration.ofSeconds(15))
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .GET()
                .build();

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_NONE))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        if (response.statusCode() == 200) {
            ArrayList<Date> dates = parseDateList(response.body());
            return dates;
        } else {
            return null;
        }
    }

    private ArrayList<Date> parseDateList(String dateList) {
        try {
            return new ObjectMapper().readValue(dateList, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
