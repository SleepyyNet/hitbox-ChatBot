package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.Main;

public class VersionController {

    @FXML
    public Label label;

    public void init(){
        label.setText("You are currently using " + Main.currentVersion + ", the latest version is " + Main.latestVersion + ".\nYou can get the latest version from\nhttp://gamingtom.com/h/chatbot.\nTo update, just replace the current Jar file with the new one.");
    }

}
