package controller.tabs;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import main.Main;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimedMessagesTab {

    @FXML
    public TextField field_id, field_message, field_interval;

    @FXML
    public TableView table;

    @FXML
    public TableColumn idCol, messageCol, intervalCol, enabledCol;

    private final ObservableList<TableTimedMessage> data = FXCollections.observableArrayList();

    public void init(){

        idCol.setCellValueFactory(new PropertyValueFactory<TableTimedMessage, String>("id"));
        messageCol.setCellValueFactory(new PropertyValueFactory<TableTimedMessage, String>("message"));
        intervalCol.setCellValueFactory(new PropertyValueFactory<TableTimedMessage, String>("interval"));
        enabledCol.setCellValueFactory(new PropertyValueFactory<TableTimedMessage, String>("enabled"));

        messageCol.setCellFactory(TextFieldTableCell.forTableColumn());
        intervalCol.setCellFactory(TextFieldTableCell.forTableColumn());

        table.setItems(data);

        fillTable();
    }

    private ArrayList<String> runningMessages = new ArrayList<>();
    public void fillTable(){
        ArrayList<String> timedMessages = Main.messageHandler.getTimedMessages();
        if (!table.getItems().isEmpty())data.removeAll(table.getItems());
        if (timedMessages != null && timedMessages.size() > 0) {
            for (String timedMessage : timedMessages) {
                String message = Main.messageHandler.getTimedMessage(timedMessage);
                String interval = Integer.toString(Main.messageHandler.getTimedMessageInterval(timedMessage));
                String enabled = Boolean.toString(Main.messageHandler.getTimedMessageState(timedMessage));

                data.add(new TableTimedMessage(timedMessage, message, interval, enabled));

                if (!runningMessages.contains(timedMessage)) {
                    new TimedMessage(timedMessage);
                    runningMessages.add(timedMessage);
                }
            }
        }
    }

    public void addTimedMessage(Event event){
        if (!field_id.getText().isEmpty() && !Main.messageHandler.timedMessageExists(field_id.getText()) && !field_message.getText().isEmpty() && !field_interval.getText().isEmpty() && StringUtils.isNumeric(field_interval.getText())){
            Main.messageHandler.addTimedMessage(field_id.getText(), field_message.getText(), Integer.parseInt(field_interval.getText()));
            field_id.setText("");
            field_message.setText("");
            field_interval.setText("");
            fillTable();
        }
    }

    public void removeTimedMessage(Event event){
        int id = table.getSelectionModel().getSelectedIndex();
        String messageId = data.get(id).getId();
        Main.messageHandler.removeTimedMessage(messageId);
        runningMessages.remove(messageId);
        fillTable();
    }

    public void editTimedMessage(TableColumn.CellEditEvent event){
        int index = table.getColumns().indexOf(event.getSource());
        int id = table.getSelectionModel().getSelectedIndex();
        String messageId = data.get(id).getId();
        String message = Main.messageHandler.getTimedMessage(messageId);
        String interval = Integer.toString(Main.messageHandler.getTimedMessageInterval(messageId));
        String state = Boolean.toString(Main.messageHandler.getTimedMessageState(messageId));
        if (index == 1 && !event.getNewValue().toString().isEmpty()) Main.config.setProperty("timed-messages", messageId, new JSONObject("{\"message\":\"" + event.getNewValue() + "\",\"interval\":\"" + interval + "\",\"enabled\":\"" + state + "\"}"));
        else if (index == 2 && !event.getNewValue().toString().isEmpty() && StringUtils.isNumeric(event.getNewValue().toString())) Main.config.setProperty("timed-messages", messageId, new JSONObject("{\"message\":\"" + message + "\",\"interval\":\"" + event.getNewValue() + "\",\"enabled\":\"" + state + "\"}"));
        fillTable();
    }

    public void toggleState(Event event){
        int id = table.getSelectionModel().getSelectedIndex();
        String messageId = data.get(id).getId();
        String message = Main.messageHandler.getTimedMessage(messageId);
        String interval = Integer.toString(Main.messageHandler.getTimedMessageInterval(messageId));
        String state = Boolean.toString(Main.messageHandler.getTimedMessageState(messageId));

        if (state.equals("true")){
            Main.config.setProperty("timed-messages", messageId, new JSONObject("{\"message\":\"" + message + "\",\"interval\":\"" + interval + "\",\"enabled\":\"false\"}"));
        }else if (state.equals("false")) {
            Main.config.setProperty("timed-messages", messageId, new JSONObject("{\"message\":\"" + message + "\",\"interval\":\"" + interval + "\",\"enabled\":\"true\"}"));
        }

        fillTable();
    }

    private class TimedMessage {
        private TimedMessage(final String id){
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int i = 0;
                            while (Main.messageHandler.timedMessageExists(id)) {
                                Thread.sleep(Main.messageHandler.getTimedMessageInterval(id) * 1000);
                                if (Main.messageHandler.getTimedMessageState(id)) {
                                    if (Main.config.connected) {
                                        Main.mainController.client.sendMessage(Main.messageHandler.getTimedMessage(id));
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static class TableTimedMessage {
        private final SimpleStringProperty id;
        private final SimpleStringProperty message;
        private final SimpleStringProperty interval;
        private final SimpleStringProperty enabled;

        private TableTimedMessage(String id, String message, String interval, String enabled){
            this.id = new SimpleStringProperty(id);
            this.message = new SimpleStringProperty(message);
            this.interval = new SimpleStringProperty(interval);
            this.enabled = new SimpleStringProperty(enabled);
        }

        public String getId() {
            return id.get();
        }

        public String getMessage(){
            return message.get();
        }

        public String getInterval(){
            return interval.get();
        }

        public String getEnabled(){
            return enabled.get();
        }
    }

}
