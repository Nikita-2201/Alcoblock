package ru.gknsv.animations;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ColorTransition {
    public static void AnimateBackgroundColor(VBox control, Color fromColor, Color toColor, int duration) {

        Rectangle rect = new Rectangle();
        rect.setFill(fromColor);

        FillTransition tr = new FillTransition();
        tr.setShape(rect);
        tr.setDuration(Duration.millis(duration));
        tr.setFromValue(fromColor);
        tr.setToValue(toColor);
        tr.setInterpolator(new Interpolator() {
            @Override
            protected double curve(double t) {
                control.setBackground(new Background(
                        new BackgroundFill(
                                rect.getFill(),
                                control.getBackground().getFills().get(0).getRadii(),
                                control.getBackground().getFills().get(0).getInsets())
                ));
                return t;
            }
        });

        tr.play();
    }

    public static void AnimateTextColor(Label control, Color fromColor, Color toColor, int duration) {
        control.setTextFill(fromColor);
        Timeline fadeInTimeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(control.textFillProperty(), fromColor, Interpolator.EASE_OUT)
                ),
                new KeyFrame(Duration.millis(duration),
                        new KeyValue(control.textFillProperty(), toColor, Interpolator.EASE_OUT)
                )
        );
        fadeInTimeline.setCycleCount(1);
        fadeInTimeline.setAutoReverse(false);
        fadeInTimeline.play();
    }
}
