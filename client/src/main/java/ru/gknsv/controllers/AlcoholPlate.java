package ru.gknsv.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.springframework.http.MediaType;
import ru.gknsv.animations.ColorTransition;
import ru.gknsv.dto.AlcoDto;
import ru.gknsv.dto.ImageDto;
import ru.gknsv.dto.RealImageDto;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class AlcoholPlate {

    @FXML
    public ImageView image;
    @FXML
    public Label alcoholStrength;
    protected CalculationSide calculationSide;
    protected AlcoDto alcoDto;
    @FXML
    public Label alcoholName;

    public void setData(AlcoDto alcoDto, CalculationSide calculationSide) throws IOException, InterruptedException {
        this.alcoDto = alcoDto;
        this.calculationSide = calculationSide;
        alcoholName.setText(alcoDto.getTitle());
        //System.out.println(alcoDto.getStrength());
        alcoholStrength.setText(alcoDto.getStrength() + " %");
        setImage();
    }

    public void addAlco(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader;
        fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ru/gknsv/DrinkDice.fxml"));
        HBox drinkDice = fxmlLoader.load();
        DrinkDice drinkDiceController = fxmlLoader.getController();
        drinkDiceController.setData(alcoDto, calculationSide, drinkDice);
        calculationSide.drinkContainer.getChildren().add(drinkDice);
        calculationSide.drinkContainer.setMargin(drinkDice, new Insets(10,0,0,0));
    }

    public void setImage() throws IOException, InterruptedException {
        if (calculationSide.getModel().containPicture(alcoDto.getPictureId())) {
            image.setImage(calculationSide.getModel().getRealImageDto(alcoDto.getPictureId()).getImage());
        } else {
            HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8085/images/" + alcoDto.getPictureId()))
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
                Image img = new Image(parsePath(response.body()).getPath());
                RealImageDto realImageDto = new RealImageDto();
                realImageDto.setImage(img);
                realImageDto.setId(alcoDto.getPictureId());
                realImageDto.setPath(parsePath(response.body()).getPath());
                calculationSide.getModel().getRealImageList().add(realImageDto);
                image.setImage(img);
            }
        }
    }

    private ImageDto parsePath(String group) {
        try {
            return new ObjectMapper().readValue(group, ImageDto.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
