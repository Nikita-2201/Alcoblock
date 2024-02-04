package ru.gknsv.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Calendar;
import java.util.Date;

public class HistoryDice {
    @FXML
    public Label dateLabel;
    @FXML
    public Label yearLabel;


    public void setData(Date date) {
        dateLabel.setText(getConvertedDayAndMonth(date));
        yearLabel.setText(getConvertedYear(date));
    }

    private String getConvertedDayAndMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
        String finalDate = day + "." + month;
        return finalDate;
    }

    private String getConvertedYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String finalDate = year;
        return finalDate;
    }
}
