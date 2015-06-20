package controller.tabs;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import listen.MessageListener;
import main.Main;
import main.Parser;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class PointsGamesTab implements MessageListener{

    @FXML
    public TextField raffle_field_ticketsCost;

    @FXML
    public TextField raffle_field_maxTickets;

    @FXML
    public TextField raffle_field_userLevel;

    @FXML
    public ListView raffle_list_entered;

    @FXML
    public Button raffle_button_start;

    @FXML
    public Button raffle_button_stop;

    @FXML
    public Button raffle_button_draw;

    @FXML
    public Button raffle_button_reset;

    @FXML
    public Label raffle_label_winner;

    @FXML
    public TextField lottery_field_ticketsCost;

    @FXML
    public TextField lottery_field_maxTickets;

    @FXML
    public TextField lottery_field_userLevel;

    @FXML
    public ListView lottery_list_entered;

    @FXML
    public Button lottery_button_start;

    @FXML
    public Button lottery_button_stop;

    @FXML
    public Button lottery_button_draw;

    @FXML
    public Button lottery_button_reset;

    @FXML
    public Label lottery_label_winner;

    public void init(){
        Main.initiator.addListener(this);
        Main.commandHandler.hardCommands.add("!joinRaffle");
        Main.commandHandler.hardCommands.add("!joinLottery");

        raffle_list_entered.setItems(raffleData);
        lottery_list_entered.setItems(lotteryData);
    }

    @Override
    public void messageReceived(JSONObject message){

        if (!Parser.isBuffer(message) && Parser.isChatMessage(message)){

            String text = Parser.getText(message);
/*            if (text.startsWith("!joinRaffle") && raffleRunning && Parser.getUserLevel(message) >= Integer.parseInt(raffle_field_userLevel.getText())){

                String[] text2 = text.split(" ");
                if (text2.length < 2 || !StringUtils.isNumeric(text2[1])) joinRaffle(Parser.getName(message).toLowerCase(), 1);
                else joinRaffle(Parser.getName(message).toLowerCase(), Integer.parseInt(text2[1]));

            }*/

            String[] text2 = text.split(" ");
            if (text2[0].equals("!joinRaffle") && raffleRunning && Parser.getUserLevel(message) >= Integer.parseInt(raffle_field_userLevel.getText())){
                if (text2.length < 2 || !text2[1].matches("[0-9]+")) joinRaffle(Parser.getName(message).toLowerCase(), 1);
                else joinRaffle(Parser.getName(message).toLowerCase(), Integer.parseInt(text2[1]));
            }else if (text2[0].equals("!joinLottery") && lotteryRunning && Parser.getUserLevel(message) >= Integer.parseInt(lottery_field_userLevel.getText())){
                if (text2.length < 2 || !text2[1].matches("[0-9]+")) joinLottery(Parser.getName(message).toLowerCase(), 1);
                else joinLottery(Parser.getName(message).toLowerCase(), Integer.parseInt(text2[1]));
            }


        }

    }

    //### RAFFLE
    ObservableList<String> raffleData = FXCollections.observableArrayList();

    private boolean raffleRunning = false;
    private ArrayList raffleEntriesLong;
    private ArrayList raffleEntriesShort;

    public void startRaffle(ActionEvent event){
        if (!raffle_field_ticketsCost.getText().isEmpty() && raffle_field_ticketsCost.getText().matches("[0-9]+") && !raffle_field_maxTickets.getText().isEmpty() && raffle_field_maxTickets.getText().matches("[0-9]+") && !raffle_field_userLevel.getText().isEmpty() && raffle_field_userLevel.getText().matches("[0-9]+")) {
            raffleRunning = true;
            raffleEntriesLong = new ArrayList();
            raffleEntriesShort = new ArrayList();

            raffle_button_start.setDisable(true);
            raffle_button_stop.setDisable(false);
        }
    }

    public void stopRaffle(ActionEvent event){
        raffleRunning = false;

        raffle_button_stop.setDisable(true);
        raffle_button_draw.setDisable(false);
    }

    public void drawRaffle(ActionEvent event){
        if (raffleEntriesLong.size() > 0) {
            String winner = raffleEntriesLong.get(new Random().nextInt(raffleEntriesLong.size()) + 0).toString();
            raffle_label_winner.setText(winner);
        }
    }

    public void resetRaffle(ActionEvent event){
        raffle_button_start.setDisable(false);
        raffle_button_stop.setDisable(true);
        raffle_button_draw.setDisable(true);

        raffleData.removeAll(raffle_list_entered.getItems());
        raffleEntriesShort.clear();
        raffleEntriesLong.clear();
    }

    public void joinRaffle(String name, int amount){
        if (amount <= Integer.parseInt(raffle_field_maxTickets.getText()) && Collections.frequency(raffleEntriesLong, name) < Integer.parseInt(raffle_field_maxTickets.getText())){
            if (amount * Integer.parseInt(raffle_field_ticketsCost.getText()) <= Integer.parseInt(Main.mainController.root2Controller.pointsTabController.getPoints(name))){

                for (int i = 0; i < amount; i++) {
                    raffleEntriesLong.add(name);
                }

                if (!raffleEntriesShort.contains(name)){
                    System.out.println("TRUE 1");
                    raffleEntriesShort.add(name);
                    raffleEntriesShort.add(amount);
                    raffleData.removeAll(raffle_list_entered.getItems());
                    for (int i = 0; i < raffleEntriesShort.size(); i+=2) {
                        raffleData.add(raffleEntriesShort.get(i) + " (" + raffleEntriesShort.get(i+1) + ")");
                    }
                } else {
                    System.out.println("TRUE 2");
                    raffleEntriesShort.set(raffleEntriesShort.indexOf(name) + 1, Integer.parseInt(raffleEntriesShort.get(raffleEntriesShort.indexOf(name) + 1).toString()) + amount);
                    raffleData.removeAll(raffle_list_entered.getItems());
                    for (int i = 0; i < raffleEntriesShort.size(); i+=2) {
                        raffleData.add(raffleEntriesShort.get(i) + " (" + raffleEntriesShort.get(i+1) + ")");
                    }
                }

                Main.mainController.root2Controller.pointsTabController.removePoints(name, amount * Integer.parseInt(raffle_field_ticketsCost.getText()));
                System.out.println(raffleEntriesLong);
            }else {
                System.out.println(name + " does not have enough points to enter this raffle.");
            }
        } else System.out.println(name + " has entered with too many tickets.");
    }

    //### LOTTERY
    ObservableList<String> lotteryData = FXCollections.observableArrayList();

    private boolean lotteryRunning = false;
    private ArrayList lotteryEntriesLong;
    private ArrayList lotteryEntriesShort;

    public void startLottery(ActionEvent event){
        if (!lottery_field_ticketsCost.getText().isEmpty() && lottery_field_ticketsCost.getText().matches("[0-9]+") && !lottery_field_maxTickets.getText().isEmpty() && lottery_field_maxTickets.getText().matches("[0-9]+") && !lottery_field_userLevel.getText().isEmpty() && lottery_field_userLevel.getText().matches("[0-9]+")) {
            lotteryRunning = true;
            lotteryEntriesLong = new ArrayList();
            lotteryEntriesShort = new ArrayList();

            lottery_button_start.setDisable(true);
            lottery_button_stop.setDisable(false);
        }
    }

    public void stopLottery(ActionEvent event){
        lotteryRunning = false;

        lottery_button_stop.setDisable(true);
        lottery_button_draw.setDisable(false);
    }

    public void drawLottery(ActionEvent event){
        if (lotteryEntriesLong.size() > 0) {
            String winner = lotteryEntriesLong.get(new Random().nextInt(lotteryEntriesLong.size()) + 0).toString();
            lottery_label_winner.setText(winner);
        }
    }

    public void resetLottery(ActionEvent event){
        if (!lottery_label_winner.getText().isEmpty()) Main.mainController.root2Controller.pointsTabController.addPoints(lottery_label_winner.getText(), lotteryEntriesLong.size() * Integer.parseInt(lottery_field_ticketsCost.getText()) + "");

        lottery_button_start.setDisable(false);
        lottery_button_stop.setDisable(true);
        lottery_button_draw.setDisable(true);

        lotteryData.removeAll(lottery_list_entered.getItems());
        lotteryEntriesShort.clear();
        lotteryEntriesLong.clear();
    }

    public void joinLottery(String name, int amount){
        if (amount <= Integer.parseInt(lottery_field_maxTickets.getText()) && Collections.frequency(lotteryEntriesLong, name) < Integer.parseInt(lottery_field_maxTickets.getText())){
            if (amount * Integer.parseInt(lottery_field_ticketsCost.getText()) <= Integer.parseInt(Main.mainController.root2Controller.pointsTabController.getPoints(name))){

                for (int i = 0; i < amount; i++) {
                    lotteryEntriesLong.add(name);
                }

                if (!lotteryEntriesShort.contains(name)){
                    System.out.println("TRUE 1");
                    lotteryEntriesShort.add(name);
                    lotteryEntriesShort.add(amount);
                    lotteryData.removeAll(lottery_list_entered.getItems());
                    for (int i = 0; i < lotteryEntriesShort.size(); i+=2) {
                        lotteryData.add(lotteryEntriesShort.get(i) + " (" + lotteryEntriesShort.get(i+1) + ")");
                    }
                } else {
                    System.out.println("TRUE 2");
                    lotteryEntriesShort.set(lotteryEntriesShort.indexOf(name) + 1, Integer.parseInt(lotteryEntriesShort.get(lotteryEntriesShort.indexOf(name) + 1).toString()) + amount);
                    lotteryData.removeAll(lottery_list_entered.getItems());
                    for (int i = 0; i < lotteryEntriesShort.size(); i+=2) {
                        lotteryData.add(lotteryEntriesShort.get(i) + " (" + lotteryEntriesShort.get(i+1) + ")");
                    }
                }

                Main.mainController.root2Controller.pointsTabController.removePoints(name, amount * Integer.parseInt(lottery_field_ticketsCost.getText()));
                System.out.println(lotteryEntriesLong);
            }else {
                System.out.println(name + " does not have enough points to enter this lottery.");
            }
        } else System.out.println(name + " has entered with too many tickets.");
    }


}
