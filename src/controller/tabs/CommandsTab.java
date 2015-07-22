package controller.tabs;

import controller.ScriptEditor;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.Main;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

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
    public TableView table;

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

            userLevelCol.setCellFactory(TextFieldTableCell.forTableColumn());
            //commandNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
            commandMessageCol.setCellFactory(TextFieldTableCell.forTableColumn());

            table.setItems(data);

            fillTable();
        }catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

    public void fillTable(){
        ArrayList arr = Main.messageHandler.getCommands();
        if (!table.getItems().isEmpty())data.removeAll(table.getItems());
        if (arr != null) {
            for (int i = 0; i < arr.size(); i++) {
                data.add(new Command(
                        Integer.toString(Main.messageHandler.getCommandLevel(arr.get(i).toString())),
                        arr.get(i).toString(),
                        Main.messageHandler.getCommandMessage(arr.get(i).toString())));
            }
        }
    }

    public void commands_event_add(ActionEvent event){
        if (!commands_field_commandName1.getText().isEmpty() &&
                !commands_field_commandMessage.getText().isEmpty() &&
                !commands_field_userLevel.getText().isEmpty() &&
                commands_field_userLevel.getText().matches("[0-9]") &&
                !commands_field_commandMessage.getText().contains("/,") &&
                !Main.messageHandler.commandExists(commands_field_commandName1.getText()) &&
                !commands_field_commandName1.getText().equals(commands_field_commandMessage.getText()) &&
                !Main.messageHandler.hardCommands.contains(commands_field_commandName1.getText()
                )){
            data.add(new Command(commands_field_userLevel.getText(), commands_field_commandName1.getText(), commands_field_commandMessage.getText()));
            Main.messageHandler.addCommand(commands_field_commandName1.getText(), commands_field_commandMessage.getText(), Integer.parseInt(commands_field_userLevel.getText()));

            commands_field_commandName1.setText("");
            commands_field_commandMessage.setText("");
            commands_field_userLevel.setText("");
        }
    }

    public void commands_event_remove(ActionEvent event){
        //System.out.println(commands_field_commandName2.getText());
        if (!commands_field_commandName2.getText().isEmpty() && Main.messageHandler.commandExists(commands_field_commandName2.getText())){
            Main.messageHandler.removeCommand(commands_field_commandName2.getText());
            data.removeAll(table.getItems());
            fillTable();

            commands_field_commandName2.setText("");
        }
    }

    public void removeCommand(Event event){
        int id = table.getSelectionModel().getSelectedIndex();
        String commandName = data.get(id).getCommandName();
        Main.messageHandler.removeCommand(commandName);
        fillTable();
    }

    public void editCommand(TableColumn.CellEditEvent event){
        int index = table.getColumns().indexOf(event.getSource());
        int id = table.getSelectionModel().getSelectedIndex();
        String userLevel = data.get(id).getUserLevel();
        String commandName = data.get(id).getCommandName();
        String commandMessage = data.get(id).getCommandMessage();
        if (index == 0 && !event.getNewValue().toString().isEmpty() && StringUtils.isNumeric(event.getNewValue().toString())) Main.messageHandler.addCommand(commandName, commandMessage, Integer.parseInt(event.getNewValue().toString()));
        else if (index == 1 && !event.getNewValue().toString().isEmpty()) Main.messageHandler.addCommand(event.getNewValue().toString(), commandMessage, Integer.parseInt(userLevel));
        else if (index == 2 && !event.getNewValue().toString().isEmpty()) Main.messageHandler.addCommand(commandName, event.getNewValue().toString(), Integer.parseInt(userLevel));
        fillTable();
    }

    public Stage editorStage = null;
    public ScriptEditor scriptEditor;
    private boolean editorShown = false;
    public void loadEditor(Event event){
        try {
            if (editorStage == null) {
                editorStage = new Stage();
                FXMLLoader editorLoader = new FXMLLoader(getClass().getResource("/fxml/scriptEditor.fxml"));
                Parent editorRoot = editorLoader.load();
                scriptEditor = editorLoader.getController();
                scriptEditor.init();
                Scene editorScene = new Scene(editorRoot, 600, 400);
                editorStage.setScene(editorScene);
                editorStage.setTitle("Script Editor");
                editorStage.show();
                editorShown = true;

                editorStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        editorShown = false;
                        editorStage.hide();
                    }
                });
            }else {
                if (editorShown){
                    editorStage.setIconified(false);
                    editorStage.toFront();
                } else {
                    editorStage.show();
                    editorShown = true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
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
