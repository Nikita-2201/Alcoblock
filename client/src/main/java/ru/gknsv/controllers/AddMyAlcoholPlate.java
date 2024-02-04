package ru.gknsv.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.gknsv.Model;

import java.io.IOException;

public class AddMyAlcoholPlate {

    private Model model;
    private CalculationSide calculationSide;

    public AddMyAlcoholPlate(Model model, CalculationSide calculationSide) {
        this.model = model;
        this.calculationSide = calculationSide;
    }

    public void addMyAlco(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ru/gknsv/AddMyAlcoholPage.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        AddMyAlcoholPage addMyAlcoholPageController = loader.getController();
        addMyAlcoholPageController.setModel(model);
        addMyAlcoholPageController.setCalculationSide(calculationSide);

        Stage stage = new Stage();
        stage.setTitle("Hello!");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
