package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import main.Client;
import main.Main;

import java.util.Map;
import java.util.Timer;

public class MainController {

    public String username, password, channel;
    public Boolean savePassword;

    AnchorPane root1;
    AnchorPane root2;

    @FXML
    private BorderPane borderPane;

    public ConnectionController connectionController;
    public DetailsController detailsController;
    public Menu menu_tabs;


    public void init(){
        try {
            FXMLLoader root1Loader = new FXMLLoader(getClass().getResource("/fxml/connection.fxml"));
            root1 = root1Loader.load();
            connectionController = (ConnectionController)root1Loader.getController();
            connectionController.init();

            borderPane.centerProperty().set(root1);

        }catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

    public Client client;
    public void connect(String username, String password, String channel, Boolean savePassword){
        this.username = username;
        this.password = password;
        this.channel = channel;
        this.savePassword = savePassword;
        try {
            client = new Client(username, password, channel, Client.getIP());
        }catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

    public void connected(){
        try {
            Main.config.username = username.toLowerCase();
            Main.iniConfig.setProperty("general", "username", username);
            Main.config.password = password;
            if (savePassword) Main.iniConfig.setProperty("general", "password", password);
            else if (!savePassword) Main.iniConfig.setProperty("general", "password", "none");
            Main.config.channel = channel.toLowerCase();
            Main.iniConfig.setProperty("general", "channel", channel);
            Main.config.createFile(channel.toLowerCase() + ".txt");

            FXMLLoader root2Loader = new FXMLLoader(getClass().getResource("/fxml/details.fxml"));
            root2 = root2Loader.load();
            detailsController = (DetailsController) root2Loader.getController();
            detailsController.init();

            for (CheckMenuItem key : detailsController.tabs.keySet())
            {
                menu_tabs.getItems().add(key);
                if (Main.iniConfig.getProperty("tabs", detailsController.tabs.get(key).getText()) != null && Boolean.parseBoolean(Main.iniConfig.getProperty("tabs", detailsController.tabs.get(key).getText()))) key.setSelected(true);
                else key.setSelected(false);
            }
            menu_tabs.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    CheckMenuItem item = (CheckMenuItem)event.getTarget();
                    if (!item.isSelected()) {
                        detailsController.details_tabpane.getTabs().remove(detailsController.tabs.get(event.getTarget()));
                        Main.iniConfig.setProperty("tabs", detailsController.tabs.get(item).getText(), "false");
                    }
                    else {
                        detailsController.details_tabpane.getTabs().add(menu_tabs.getItems().indexOf(item), detailsController.tabs.get(event.getTarget()));
                        Main.iniConfig.setProperty("tabs", detailsController.tabs.get(item).getText(), "true");
                    }
                }
            });

            borderPane.centerProperty().set(root2);
            Main.mainStage.setTitle("tBot v" + Main.config.version + " - " + channel);
            Main.config.connected = true;

            Main.scriptManager.loadAllScripts();
        }catch (Exception e){
            Main.consoleController.eout(e);
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

    public void openSettings(ActionEvent event){
        if (!Main.config.settingsShown){
            Main.settingsStage.show();
            Main.config.settingsShown = true;
        }
        else if (Main.config.settingsShown){
            Main.settingsStage.setIconified(false);
            Main.settingsStage.toFront();
        }
    }

}
