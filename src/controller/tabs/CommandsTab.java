package controller.tabs;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class CommandsTab {

    @FXML
    public TextField commands_field_commandName1;

    @FXML
    public TextField commands_field_commandMessage;

    @FXML
    public TextField commands_field_userLevel;

    @FXML
    public TextField commands_field_commandName2;

    @FXML
    public TableView commands_table;

    @FXML
    public TableColumn userLevelCol;

    @FXML
    public TableColumn commandNameCol;

    @FXML
    public TableColumn commandMessageCol;

    private final ObservableList<Command> data = FXCollections.observableArrayList();

    public void init(){
        try {

            userLevelCol.setCellValueFactory(new PropertyValueFactory<Command, String>("userLevel"));
            commandNameCol.setCellValueFactory(new PropertyValueFactory<Command, String>("commandName"));
            commandMessageCol.setCellValueFactory(new PropertyValueFactory<Command, String>("commandMessage"));


            commands_table.setItems(data);

            fillTable();
        }catch (Exception e){
            Main.consoleController.eout(e.toString());
            e.printStackTrace();
        }
    }

    public void fillTable(){
        ArrayList arr = Main.commandHandler.getCommands();
        if (arr != null) {
            for (int i = 0; i < arr.size(); i++) {
                data.add(new Command(
                        Integer.toString(Main.commandHandler.getCommandLevel(arr.get(i).toString())),
                        arr.get(i).toString(),
                        Main.commandHandler.getCommandMessage(arr.get(i).toString())));
            }
        }
    }

    public void commands_event_add(ActionEvent event){
        if (!commands_field_commandName1.getText().isEmpty() &&
                !commands_field_commandMessage.getText().isEmpty() &&
                !commands_field_userLevel.getText().isEmpty() &&
                commands_field_userLevel.getText().matches("[0-9]") &&
                !commands_field_commandMessage.getText().contains("/,") &&
                !Main.commandHandler.commandExists(commands_field_commandName1.getText()) &&
                !commands_field_commandName1.getText().equals(commands_field_commandMessage.getText()
                )){
            data.add(new Command(commands_field_userLevel.getText(), commands_field_commandName1.getText(), commands_field_commandMessage.getText()));
            Main.commandHandler.addCommand(commands_field_commandName1.getText(), commands_field_commandMessage.getText(), Integer.parseInt(commands_field_userLevel.getText()));

            commands_field_commandName1.setText("");
            commands_field_commandMessage.setText("");
            commands_field_userLevel.setText("");
        }
    }

    public void commands_event_remove(ActionEvent event){
        //System.out.println(commands_field_commandName2.getText());
        if (!commands_field_commandName2.getText().isEmpty() && Main.commandHandler.commandExists(commands_field_commandName2.getText())){
            Main.commandHandler.removeCommand(commands_field_commandName2.getText());
            data.removeAll(commands_table.getItems());
            fillTable();

            commands_field_commandName2.setText("");
        }
    }

    public static class Command {
        private final SimpleStringProperty userLevel;
        private final SimpleStringProperty commandName;
        private final SimpleStringProperty commandMessage;

        private Command(String userLevel, String commandName, String commandMessage){
            this.userLevel = new SimpleStringProperty(userLevel);
            this.commandName = new SimpleStringProperty(commandName);
            this.commandMessage = new SimpleStringProperty(commandMessage);
        }

        public String getUserLevel(){
            return userLevel.get();
        }

        public String getCommandName(){
            return commandName.get();
        }

        public String getCommandMessage(){
            return commandMessage.get();
        }
    }

}
