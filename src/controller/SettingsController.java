package controller;

import controller.settings.ChatController;
import controller.settings.CommandsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import main.Main;

public class SettingsController {

    @FXML
    public TabPane tabPane;

    @FXML
    public Tab tabChat, tabCommands;

    public ChatController chatController;
    public CommandsController commandsController;

    public void init(){
        try {

            FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("/fxml/settings/chat.fxml"));
            tabChat.setContent((AnchorPane)chatLoader.load());
            chatController = chatLoader.getController();
            chatController.init();

            FXMLLoader commandsLoader = new FXMLLoader(getClass().getResource("/fxml/settings/commands.fxml"));
            tabCommands.setContent((AnchorPane)commandsLoader.load());
            commandsController = commandsLoader.getController();
            commandsController.init();

        }catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

    public void saveSettings(ActionEvent event){
        chatController.saveSettings();
        commandsController.saveSettings();
        if (Main.config.connected)Main.mainController.detailsController.refreshStyle();
    }

}
