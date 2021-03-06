package controller.tabs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import main.Main;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PointsTab {

    @FXML
    public CheckBox points_checkbox_enabled;

    @FXML
    public TextField points_field_name;

    @FXML
    public TextField points_field_amount;

    @FXML
    public TextField points_field_time;

    Runnable runnable;
    Thread t;

    public String pointsName;
    public String pointsAmount;
    public String pointsTime;

    Boolean timerRunning = false;

    public void init(){

        if (Main.config.getProperty("points-settings", "points enabled") == null) {
            Main.config.setProperty("points-settings", "points enabled", "true");
            points_checkbox_enabled.setSelected(true);
        } else if (Main.config.getProperty("points-settings", "points enabled").equals("true")){
            points_checkbox_enabled.setSelected(true);
            points_field_name.setDisable(false);
            points_field_amount.setDisable(false);
            points_field_time.setDisable(false);
        } else {
            points_checkbox_enabled.setSelected(false);
            points_field_name.setDisable(true);
            points_field_amount.setDisable(true);
            points_field_time.setDisable(true);
        }

        if (Main.config.getProperty("points-settings", "points name") == null) {
            Main.config.setProperty("points-settings", "points name", "Points");
            pointsName = Main.config.getProperty("points-settings", "points name");
            points_field_name.setText("Points");
        } else {
            points_field_name.setText(Main.config.getProperty("points-settings", "points name"));
            pointsName = Main.config.getProperty("points-settings", "points name");
        }

        if (Main.config.getProperty("points-settings", "points amount") == null) {
            Main.config.setProperty("points-settings", "points amount", "10");
            pointsAmount = Main.config.getProperty("points-settings", "points amount");
            points_field_amount.setText("10");
        } else {
            points_field_amount.setText(Main.config.getProperty("points-settings", "points amount"));
            pointsAmount = Main.config.getProperty("points-settings", "points amount");
        }

        if (Main.config.getProperty("points-settings", "points time") == null) {
            Main.config.setProperty("points-settings", "points time", "60");
            pointsTime = Main.config.getProperty("points-settings", "points time");
            points_field_time.setText("60");
        } else {
            points_field_time.setText(Main.config.getProperty("points-settings", "points time"));
            pointsTime = Main.config.getProperty("points-settings", "points time");
        }

        startTimer();

    }

    public void addPoints(String userName, int amount){
        userName = userName.toLowerCase();
        if (Main.config.getProperty("points", userName) != null || !Main.config.getProperty("points", userName).isEmpty()) {
            Main.config.setProperty("points", userName, Integer.parseInt(Main.config.getProperty("points", userName)) + amount + "");
            //Main.mainController.detailsController.dashboardTabController.fillTable(Main.commandHandler.userList);
        } else {
            Main.config.setProperty("points", userName, amount + "");
            //Main.mainController.detailsController.dashboardTabController.fillTable(Main.commandHandler.userList);
        }
    }

    public void removePoints(String userName, int amount){
        userName = userName.toLowerCase();
        if (Main.config.getProperty("points", userName) != null || !Main.config.getProperty("points", userName).isEmpty()){
            Main.config.setProperty("points", userName, (Integer.parseInt(getPoints(userName)) - amount) + "");
            //Main.mainController.detailsController.dashboardTabController.fillTable(Main.commandHandler.userList);
        } else {
            Main.config.setProperty("points", userName, "0");
            //Main.mainController.detailsController.dashboardTabController.fillTable(Main.commandHandler.userList);
        }
    }

    public void setPoints(String userName, int amount){
        userName = userName.toLowerCase();
        if (Main.config.getProperty("points", userName) != null || !Main.config.getProperty("points", userName).isEmpty()){
            Main.config.setProperty("points", userName, amount + "");
            //Main.mainController.detailsController.dashboardTabController.fillTable(Main.commandHandler.userList);
        } else {
            Main.config.setProperty("points", userName, amount + "");
            //Main.mainController.detailsController.dashboardTabController.fillTable(Main.commandHandler.userList);
        }
    }

    public String getPoints(String userName){
        if (Main.config.getProperty("points", userName) == null) Main.config.setProperty("points", userName, "0");
        return Main.config.getProperty("points", userName);
    }

    public void save(ActionEvent event){
        Main.config.setProperty("points-settings", "points enabled", Boolean.toString(points_checkbox_enabled.isSelected()));
        Main.config.setProperty("points-settings", "points name", points_field_name.getText());
        Main.config.setProperty("points-settings", "points amount", points_field_amount.getText());
        Main.config.setProperty("points-settings", "points time", points_field_time.getText());

        pointsName = points_field_name.getText();
        pointsAmount = points_field_amount.getText();
        pointsTime = points_field_time.getText();

        if (!points_checkbox_enabled.isSelected()) {
            points_field_name.setDisable(true);
            points_field_amount.setDisable(true);
            points_field_time.setDisable(true);

            try {
                //t.wait();
            }catch (Exception e){
                Main.consoleController.eout(e);
                e.printStackTrace();
            }
        }
        else {
            points_field_name.setDisable(false);
            points_field_amount.setDisable(false);
            points_field_time.setDisable(false);

            try {
                //t.notify();
            }catch (Exception e){
                Main.consoleController.eout(e);
                e.printStackTrace();
            }
        }
    }

    private void startTimer() {
        try {
            runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!Main.config.connected) {
                            Thread.sleep(2000);
                            Main.mainController.client.getUserList();
                        }
                        while (true) {
                            if (Main.config.getProperty("points-settings", "points enabled").equals("true")) {
                                if (Main.messageHandler.userList != null) {
                                    ArrayList userList = Main.messageHandler.userList;
                                    for (int i = 0; i < userList.size(); i += 2) {
                                        if (userList.get(i) != null && userList.get(i + 1) != null) {
                                            if (Main.config.getProperty("points", userList.get(i).toString()) != null && pointsAmount != null && pointsAmount.matches("[0-9]+"))
                                                addPoints(userList.get(i).toString(), Integer.parseInt(pointsAmount));
                                            else Main.config.setProperty("points", userList.get(i).toString(), "0");
                                        }
                                    }
                                    Main.config.saveConfig();
                                    Main.mainController.detailsController.dashboardTabController.fillTable(Main.messageHandler.userList);
                                }
                            }
                            Thread.sleep(Integer.parseInt(Main.config.getProperty("points-settings", "points time")) * 1000);
                        }
                    } catch (Exception e) {
                        Main.consoleController.eout(e);
                        e.printStackTrace();
                    }
                }
            };

            t = new Thread(runnable);
            t.start();
        } catch (Exception e) {
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

    public String readPoints(){
        try {
            String text = new String(Files.readAllBytes(Paths.get(Main.rootPath + "/resources/" +  Main.config.channel + "-points.ini")), StandardCharsets.UTF_8);
            return text;
        }catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
        return "{}";
    }



}
