package controller.tabs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
        Main.initiator.addMessageListener(this);
        Main.messageHandler.hardCommands.add("!joinRaffle");
        Main.messageHandler.hardCommands.add("!joinLottery");

        raffle_list_entered.setItems(raffleData);
        lottery_list_entered.setItems(lotteryData);
    }

    @Override
    public void messageReceived(JSONObject message){

        if (!Parser.isBuffer(message) && Parser.isChatMessage(message)){

            String text = Parser.getText(message);

            String[] text2 = text.split(" ");
            if (text2[0].toLowerCase().equals("!joinraffle") && raffleRunning && Parser.getUserLevel(message) >= Integer.parseInt(raffle_field_userLevel.getText())){
                if (text2.length < 2 || !text2[1].matches("[0-9]+")) joinRaffle(Parser.getName(message).toLowerCase(), 1);
                else joinRaffle(Parser.getName(message).toLowerCase(), Integer.parseInt(text2[1]));
            }else if (text2[0].toLowerCase().equals("!joinlottery") && lotteryRunning && Parser.getUserLevel(message) >= Integer.parseInt(lottery_field_userLevel.getText())){
                if (text2.length < 2 || !text2[1].matches("[0-9]+")) joinLottery(Parser.getName(message).toLowerCase(), 1);
                else joinLottery(Parser.getName(message).toLowerCase(), Integer.parseInt(text2[1]));
            }else if (text.toLowerCase().equals("!printraffle") && Parser.getUserLevel(message) >= 2){
                if (raffleWinner != null) Main.mainController.client.sendMessage("The winner of the raffle is @" + raffleWinner);
            }else if (text.toLowerCase().equals("!printlottery") && Parser.getUserLevel(message) >= 2){
                if (lotteryWinner != null) Main.mainController.client.sendMessage("The winner of the lottery is @" + lotteryWinner);
            }

        }

    }

    //### RAFFLE
    ObservableList<String> raffleData = FXCollections.observableArrayList();

    private boolean raffleRunning = false;
    private ArrayList raffleEntriesLong;
    private ArrayList raffleEntriesShort;
    private String raffleWinner;

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
        raffleRunning = false;

        if (raffleEntriesLong.size() > 0) {
            raffleWinner = raffleEntriesLong.get(new Random().nextInt(raffleEntriesLong.size()) + 0).toString();
            raffle_label_winner.setText(raffleWinner);
        }
    }

    public void resetRaffle(ActionEvent event){
        raffleRunning = false;

        raffle_button_start.setDisable(false);
        raffle_button_stop.setDisable(true);
        raffle_button_draw.setDisable(true);

        raffleData.removeAll(raffle_list_entered.getItems());
        raffleEntriesShort.clear();
        raffleEntriesLong.clear();
    }

    public void joinRaffle(String name, int amount){
        if (amount <= Integer.parseInt(raffle_field_maxTickets.getText()) && Collections.frequency(raffleEntriesLong, name) < Integer.parseInt(raffle_field_maxTickets.getText())){
            if (amount * Integer.parseInt(raffle_field_ticketsCost.getText()) <= Integer.parseInt(Main.mainController.detailsController.pointsTabController.getPoints(name))){

                for (int i = 0; i < amount; i++) {
                    raffleEntriesLong.add(name);
                }

                if (!raffleEntriesShort.contains(name)){
                    raffleEntriesShort.add(name);
                    raffleEntriesShort.add(amount);
                    raffleData.removeAll(raffle_list_entered.getItems());
                    for (int i = 0; i < raffleEntriesShort.size(); i+=2) {
                        raffleData.add(raffleEntriesShort.get(i) + " (" + raffleEntriesShort.get(i+1) + ")");
                    }
                } else {
                    raffleEntriesShort.set(raffleEntriesShort.indexOf(name) + 1, Integer.parseInt(raffleEntriesShort.get(raffleEntriesShort.indexOf(name) + 1).toString()) + amount);
                    raffleData.removeAll(raffle_list_entered.getItems());
                    for (int i = 0; i < raffleEntriesShort.size(); i+=2) {
                        raffleData.add(raffleEntriesShort.get(i) + " (" + raffleEntriesShort.get(i+1) + ")");
                    }
                }

                Main.mainController.detailsController.pointsTabController.removePoints(name, amount * Integer.parseInt(raffle_field_ticketsCost.getText()));
            }else {
                Main.mainController.client.sendMessage("@" + name + " you do not have enough points to enter this raffle.");
                System.out.println(name + " does not have enough points to enter this raffle.");
            }
        } else {
            Main.mainController.client.sendMessage("@" + name + " you have entered this raffle with too many tickets.");
            System.out.println(name + " has entered with too many tickets.");
        }
    }

    //### LOTTERY
    ObservableList<String> lotteryData = FXCollections.observableArrayList();

    private boolean lotteryRunning = false;
    private ArrayList lotteryEntriesLong;
    private ArrayList lotteryEntriesShort;
    private String lotteryWinner;

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
            lotteryWinner = lotteryEntriesLong.get(new Random().nextInt(lotteryEntriesLong.size()) + 0).toString();
            lottery_label_winner.setText(lotteryWinner);
        }
    }

    public void resetLottery(ActionEvent event){
        if (!lottery_label_winner.getText().isEmpty()) Main.mainController.detailsController.pointsTabController.addPoints(lottery_label_winner.getText(), lotteryEntriesLong.size() * Integer.parseInt(lottery_field_ticketsCost.getText()));

        lottery_button_start.setDisable(false);
        lottery_button_stop.setDisable(true);
        lottery_button_draw.setDisable(true);

        lotteryData.removeAll(lottery_list_entered.getItems());
        lotteryEntriesShort.clear();
        lotteryEntriesLong.clear();
    }

    public void joinLottery(String name, int amount){
        if (amount <= Integer.parseInt(lottery_field_maxTickets.getText()) && Collections.frequency(lotteryEntriesLong, name) < Integer.parseInt(lottery_field_maxTickets.getText())){
            if (amount * Integer.parseInt(lottery_field_ticketsCost.getText()) <= Integer.parseInt(Main.mainController.detailsController.pointsTabController.getPoints(name))){

                for (int i = 0; i < amount; i++) {
                    lotteryEntriesLong.add(name);
                }

                if (!lotteryEntriesShort.contains(name)){
                    lotteryEntriesShort.add(name);
                    lotteryEntriesShort.add(amount);
                    lotteryData.removeAll(lottery_list_entered.getItems());
                    for (int i = 0; i < lotteryEntriesShort.size(); i+=2) {
                        lotteryData.add(lotteryEntriesShort.get(i) + " (" + lotteryEntriesShort.get(i+1) + ")");
                    }
                } else {
                    lotteryEntriesShort.set(lotteryEntriesShort.indexOf(name) + 1, Integer.parseInt(lotteryEntriesShort.get(lotteryEntriesShort.indexOf(name) + 1).toString()) + amount);
                    lotteryData.removeAll(lottery_list_entered.getItems());
                    for (int i = 0; i < lotteryEntriesShort.size(); i+=2) {
                        lotteryData.add(lotteryEntriesShort.get(i) + " (" + lotteryEntriesShort.get(i+1) + ")");
                    }
                }

                Main.mainController.detailsController.pointsTabController.removePoints(name, amount * Integer.parseInt(lottery_field_ticketsCost.getText()));
            }else {
                Main.mainController.client.sendMessage("@" + name + " you do not have enough points to enter this lottery.");
                System.out.println(name + " does not have enough points to enter this lottery.");
            }
        } else {
            Main.mainController.client.sendMessage("@" + name + " you have entered this lottery with too many tickets.");
            System.out.println(name + " has entered with too many tickets.");
        }
    }


}
