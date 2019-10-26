package com.hunter.magiccubesolver.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PopupController {
    @FXML
    private Label label;

    public void changeLabelText(String str){
        if(label != null) label.setText(str);
    }
}
