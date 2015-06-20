package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import main.Client;
import main.Main;

public class connectionController {

    @FXML
    public TextField connection_field_username;
    @FXML
    public TextField connection_field_password;
    @FXML
    public TextField connection_field_channel;

    @FXML
    public Button connection_button_connect;

    Client client;
    public void connection_event_connect(ActionEvent event){
        Main.mainController.connect(connection_field_username.getText(), connection_field_password.getText(), connection_field_channel.getText());
    }

}
