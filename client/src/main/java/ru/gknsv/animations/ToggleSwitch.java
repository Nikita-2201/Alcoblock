package ru.gknsv.animations;

import javafx.animation.*;
import javafx.scene.Parent;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;


public class ToggleSwitch extends Parent {

    private boolean isSelected;

    private Rectangle background;
    private TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.15));
    private FillTransition fillTransition = new FillTransition(Duration.seconds(0.15));
    private FillTransition triggerTransition = new FillTransition(Duration.seconds(0.15));

    private ParallelTransition parallelTransition = new ParallelTransition(translateTransition, fillTransition, triggerTransition);

    public ToggleSwitch() {
        isSelected = false;
        background = new Rectangle(100, 50);
        background.setArcWidth(50);
        background.setArcHeight(50);
        background.setFill(Color.TRANSPARENT);
        background.setStroke(Color.web("#F4F4F4"));
        background.setStrokeType(StrokeType.INSIDE);
        background.setStrokeWidth(4);

        Circle trigger = new Circle(16);
        trigger.setCenterX(25);
        trigger.setCenterY(25);
        trigger.setFill(Color.web("#F4F4F4"));

        translateTransition.setNode(trigger);
        fillTransition.setShape(background);
        triggerTransition.setShape(trigger);

        getChildren().addAll(background, trigger);

    }

    public void switchButton(){
        if (!isSelected()) {
            translateTransition.setToX(50);

            background.setStroke(Color.TRANSPARENT);
            fillTransition.setShape(background);

            fillTransition.setFromValue(Color.TRANSPARENT);
            fillTransition.setToValue(Color.web("#E29587"));

            //triggerTransition.setFromValue(Color.BLACK);
            triggerTransition.setFromValue(Color.web("#F4F4F4"));
            triggerTransition.setToValue(Color.web("#F4F4F4"));


            parallelTransition.play();

            isSelected = true;
        } else {
            translateTransition.setToX(0);

            fillTransition.setFromValue(Color.web("#E29587"));
            fillTransition.setToValue(Color.TRANSPARENT);

            //background.setStroke(Color.BLACK);
            background.setStroke(Color.web("#F4F4F4"));
            fillTransition.setShape(background);

            triggerTransition.setFromValue(Color.web("#F4F4F4"));
            //triggerTransition.setToValue(Color.BLACK);
            triggerTransition.setToValue(Color.web("#F4F4F4"));

            parallelTransition.play();
            isSelected = false;
        }
    }

    public boolean isSelected() {
        return isSelected;
    }

}
