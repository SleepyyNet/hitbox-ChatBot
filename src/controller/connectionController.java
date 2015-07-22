package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import main.Client;
import main.Main;

public class ConnectionController {

    @FXML
    public TextField connection_field_username;
    @FXML
    public TextField connection_field_password;
    @FXML
    public TextField connection_field_channel;

    @FXML
    public CheckBox checkbox_savePassword;

    @FXML
    public Button connection_button_connect;

    public void init(){
        if (Main.iniConfig.getProperty("general", "username") != null) connection_field_username.setText(Main.iniConfig.getProperty("general", "username"));
        if (Main.iniConfig.getProperty("general", "channel") != null) connection_field_channel.setText(Main.iniConfig.getProperty("general", "channel"));
        if (Main.iniConfig.getProperty("general", "password") != null && !Main.iniConfig.getProperty("general", "password").equals("none")){
            connection_field_password.setText(Main.iniConfig.getProperty("general", "password"));
            checkbox_savePassword.setSelected(true);
        }
    }

    Client client;
    public void connection_event_connect(ActionEvent event){
        if (connection_field_channel.getText().length() <= 25) {
            Main.mainController.connect(connection_field_username.getText(), connection_field_password.getText(), connection_field_channel.getText(), checkbox_savePassword.isSelected());
        }
    }

}
