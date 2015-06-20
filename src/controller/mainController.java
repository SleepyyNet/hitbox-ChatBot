package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import main.Client;
import main.Main;

public class mainController {

    public String username, password, channel;

    AnchorPane root1;
    AnchorPane root2;

    @FXML
    private BorderPane borderPane;

    connectionController root1Controller;
    public detailsController root2Controller;


    public void init(){
        try {
            FXMLLoader root1Loader = new FXMLLoader(getClass().getResource("/fxml/connection.fxml"));
            root1 = root1Loader.load();
            root1Controller = (connectionController)root1Loader.getController();

            borderPane.centerProperty().set(root1);

        }catch (Exception e){
            Main.consoleController.eout(e.toString());
            e.printStackTrace();
        }
    }

    public Client client;
    public void connect(String username, String password, String channel){
        this.username = username;
        this.password = password;
        this.channel = channel;
        try {
            client = new Client(username, password, channel, Client.getIP());
        }catch (Exception e){
            Main.consoleController.eout(e.toString());
            e.printStackTrace();
        }
    }

    public void connected(){
        try {
            Main.config.username = username.toLowerCase();
            Main.config.password = password;
            Main.config.channel = channel.toLowerCase();
            Main.config.createFile(channel.toLowerCase() + ".txt");

            FXMLLoader root2Loader = new FXMLLoader(getClass().getResource("/fxml/details.fxml"));
            root2 = root2Loader.load();
            root2Controller = (detailsController) root2Loader.getController();
            root2Controller.init();

            borderPane.centerProperty().set(root2);
            Main.mainStage.setTitle(channel);
            Main.config.connected = true;
        }catch (Exception e){
            Main.consoleController.eout(e.toString());
            e.printStackTrace();
        }
    }

    public void toggleConsole(ActionEvent event){
        if (!Main.config.consoleShown) {
            Main.consoleStage.show();
            Main.config.consoleShown = true;
        }
        else if (Main.config.consoleShown) {
            Main.consoleStage.hide();
            Main.config.consoleShown = false;
        }
    }

}
