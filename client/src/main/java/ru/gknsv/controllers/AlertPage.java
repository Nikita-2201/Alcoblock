package ru.gknsv.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.gknsv.JavaFXApplication;

import java.io.IOException;

public class AlertPage {

    @FXML
    private Label alertLabel;

    private Stage stage;


    public void toPreviousPage(ActionEvent actionEvent) {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void displayAlert(String string){
        alertLabel.setText(string);
    }

}
