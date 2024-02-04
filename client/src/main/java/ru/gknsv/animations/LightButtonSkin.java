package ru.gknsv.animations;

import javafx.animation.*;
import javafx.scene.control.Button;
import javafx.scene.control.skin.ButtonSkin;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;
import javafx.util.Duration;


public class LightButtonSkin extends ButtonSkin {

    public LightButtonSkin(Button control) {
        super(control);

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.0);
        colorAdjust.setSaturation(0.0);

        control.setEffect(colorAdjust);
        control.setOnMouseEntered(e -> {
            Timeline fadeInTimeline = new Timeline(
                    new KeyFrame(Duration.millis(0),
                            new KeyValue(colorAdjust.saturationProperty(), 0, Interpolator.EASE_OUT)),
                    new KeyFrame(Duration.millis(1),
                            new KeyValue(colorAdjust.saturationProperty(), -1, Interpolator.EASE_OUT)),
                    new KeyFrame(Duration.millis(0),
                            new KeyValue(control.textFillProperty(), Color.rgb(172, 172, 172), Interpolator.EASE_OUT),
                            new KeyValue(colorAdjust.brightnessProperty(), 0, Interpolator.EASE_OUT)),
                    new KeyFrame(Duration.millis(150),
                            new KeyValue(control.textFillProperty(), Color.rgb(255, 255, 255), Interpolator.EASE_OUT),
                            new KeyValue(colorAdjust.brightnessProperty(), 0.102, Interpolator.EASE_OUT))
            );
            fadeInTimeline.setCycleCount(1);
            fadeInTimeline.setAutoReverse(false);
            fadeInTimeline.play();
        });

        control.setOnMouseExited(e -> {
            Timeline fadeOutTimeline = new Timeline(
                    new KeyFrame(Duration.millis(0),
                            new KeyValue(colorAdjust.saturationProperty(), -1, Interpolator.EASE_OUT)),
                    new KeyFrame(Duration.millis(1),
                            new KeyValue(colorAdjust.saturationProperty(), 0, Interpolator.EASE_OUT)),
                    new KeyFrame(Duration.millis(0),
                            new KeyValue(control.textFillProperty(), Color.rgb(255, 255, 255), Interpolator.EASE_OUT),
                            new KeyValue(colorAdjust.brightnessProperty(), 0.102, Interpolator.EASE_OUT)),
                    new KeyFrame(Duration.millis(150),
                            new KeyValue(control.textFillProperty(), Color.rgb(172, 172, 172), Interpolator.EASE_OUT),
                            new KeyValue(colorAdjust.brightnessProperty(), 0, Interpolator.EASE_OUT))
            );
            fadeOutTimeline.setCycleCount(1);
            fadeOutTimeline.setAutoReverse(false);
            fadeOutTimeline.play();
        });
    }
}
