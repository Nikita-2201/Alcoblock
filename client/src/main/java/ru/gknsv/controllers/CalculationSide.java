package ru.gknsv.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.springframework.http.MediaType;
import ru.gknsv.Model;
import ru.gknsv.animations.ColorTransition;
import ru.gknsv.animations.ToggleSwitch;
import ru.gknsv.dto.AlcoDto;
import ru.gknsv.dto.AlcoHistoryDto;

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
import java.util.List;
import java.util.ResourceBundle;

public class CalculationSide implements Initializable {
    @FXML
    public AnchorPane spawnButton;
    public ToggleSwitch toggleSwitch;
    @FXML
    public VBox drinkContainer;
    @FXML
    public Text alcoholWeatheringTime;
    @FXML
    public Text bloodAlcoholConcentration;
    @FXML
    public Text score;
    @FXML
    public Label isHungry;
    @FXML
    public ImageView conditionImage;
    private Model model;
    @FXML
    public GridPane alcoholContainer;

    public CalculationSide(Model model) {
        this.model = model;
    }

    private ArrayList<AlcoDto> alcoholList;

    public Model getModel() {
        return model;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toggleSwitch = new ToggleSwitch();
        toggleSwitch.setScaleX(0.5);
        toggleSwitch.setScaleY(0.5);
        toggleSwitch.setOnMouseClicked(event -> {
            toggleSwitch.switchButton();
            if (toggleSwitch.isSelected()) {
                isHungry.setText("Поел");
            } else {
                isHungry.setText("Голодный");
            }
        });
        spawnButton.getChildren().add(toggleSwitch);
        isHungry.setText("Голодный");
        try {
            displayAlcoholList();
            displayDrinkDices();
            displayInformation();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void displayDrinkDices() throws IOException {
        if (model.getAlcoList() != null) {
            for (AlcoHistoryDto alcoHistoryDto : model.getAlcoList()) {
                FXMLLoader fxmlLoader;
                fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ru/gknsv/DrinkDice.fxml"));
                HBox drinkDice = fxmlLoader.load();
                DrinkDice drinkDiceController = fxmlLoader.getController();
                drinkDiceController.setData(alcoHistoryDto, this, drinkDice);
                drinkDiceController.alcoVolume.setText(alcoHistoryDto.getVolume());
                drinkDiceController.addButton.setVisible(false);
                drinkDiceController.deleteButton.setVisible(false);
                drinkDiceController.alcoVolume.setEditable(false);
                drinkContainer.getChildren().add(drinkDice);
                drinkContainer.setMargin(drinkDice, new Insets(10, 0, 0, 0));
            }
        }
    }

    public void displayInformation() {
        alcoholWeatheringTime.setText(model.getAlcoholWeatheringTime());
        bloodAlcoholConcentration.setText(model.getBloodAlcoholConcentration());
        score.setText(model.getScore());
        conditionImage.setImage(model.getConditionImage());
    }

    public void displayHungry() {
        if (model.getHungry()) {
            isHungry.setText("Поел");
            if (!toggleSwitch.isSelected()) {
                toggleSwitch.switchButton();
            }
        } else {
            isHungry.setText("Голодный");
            if (toggleSwitch.isSelected()) {
                toggleSwitch.switchButton();
            }
        }
    }

    public void displayAlcoholList() throws IOException, InterruptedException {
        alcoholContainer.getChildren().clear();
        try {
            alcoholList = new ArrayList<>(getAlcoholList());
        } catch (Exception e) {
            alcoholList = new ArrayList<>();
        }

        int column = 0;
        int row = 1;

        for (AlcoDto alcohol : alcoholList) {
            VBox alcoholBox = setAlcoholPlate(alcohol);
            alcoholBox.setOnMouseEntered(event -> {
                if (alcohol.getUserId() != null) {
                    ColorTransition.AnimateBackgroundColor(alcoholBox, Color.web("#2d2d2d"), Color.web("#C3E9DE"), 200);
                } else {
                    ColorTransition.AnimateBackgroundColor(alcoholBox, Color.web("#2d2d2d"), Color.web("#E29587"), 200);
                }
                ColorTransition.AnimateTextColor((Label) alcoholBox.getChildren().get(0), Color.web("#ffffff"), Color.web("#000000"), 200);
                ColorTransition.AnimateTextColor((Label) alcoholBox.getChildren().get(1), Color.web("#acacac"), Color.web("#111111"), 200);
            });
            alcoholBox.setOnMouseExited(event -> {
                if (alcohol.getUserId() != null) {
                    ColorTransition.AnimateBackgroundColor(alcoholBox, Color.web("#C3E9DE"), Color.web("#2d2d2d"), 200);
                } else {
                    ColorTransition.AnimateBackgroundColor(alcoholBox, Color.web("#E29587"), Color.web("#2d2d2d"), 200);
                }
                ColorTransition.AnimateTextColor((Label) alcoholBox.getChildren().get(0), Color.web("#000000"), Color.web("#ffffff"), 200);
                ColorTransition.AnimateTextColor((Label) alcoholBox.getChildren().get(1), Color.web("#111111"), Color.web("#acacac"), 200);
            });
            if (column == 3) {
                column = 0;
                ++row;
            }
            alcoholContainer.add(alcoholBox, column, row);
            ++column;
            GridPane.setMargin(alcoholBox, new Insets(10));
        }

        if (column == 3) {
            column = 0;
            ++row;
        }

        VBox addMyAlcoholBox = setMyAlcoholPlate();
        addMyAlcoholBox.setOnMouseEntered(event -> {
            ColorTransition.AnimateBackgroundColor(addMyAlcoholBox, Color.web("#2d2d2d"), Color.web("#000000"), 200);
        });
        addMyAlcoholBox.setOnMouseExited(event -> {
            ColorTransition.AnimateBackgroundColor(addMyAlcoholBox, Color.web("#000000"), Color.web("#2d2d2d"), 200);
        });
        alcoholContainer.add(addMyAlcoholBox, column, row);
        ++column;
        GridPane.setMargin(addMyAlcoholBox, new Insets(10));
    }

    private VBox setMyAlcoholPlate() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ru/gknsv/AddMyAlcoholPlate.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> new AddMyAlcoholPlate(model, this));
        VBox alcoholBox = fxmlLoader.load();
        return alcoholBox;
    }

    private VBox setAlcoholPlate(AlcoDto alcohol) throws IOException, InterruptedException {
        FXMLLoader fxmlLoader;
        if (alcohol.getUserId() != null) {
            fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ru/gknsv/MyAlcoholPlate.fxml"));
        } else {
            fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ru/gknsv/AlcoholPlate.fxml"));
        }
        VBox alcoholBox = fxmlLoader.load();
        AlcoholPlate alcoholPlate = fxmlLoader.getController();
        alcoholPlate.setData(alcohol, this);
        return alcoholBox;
    }

    //SERVER
    private List<AlcoDto> getAlcoholList() throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8085/alco/" + model.getSessionUser().getId()))
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
            List<AlcoDto> alcoList = parseListAlco(response.body());
            return alcoList;
        } else {
            return null;
        }

    }

    private List<AlcoDto> parseListAlco(String listAlco) {
        try {
            return new ObjectMapper().readValue(listAlco, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public void clearHistory(ActionEvent actionEvent) {
        drinkContainer.getChildren().clear();
        model.clearAlcoList();
        model.clearAmountAlcoholInBlood();
        displayInformation();
    }
}
