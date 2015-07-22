package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.Main;

public class EditUserController {

    @FXML
    public TextField field_seconds;
    @FXML
    public TextField field_points;

    String username;

    public void init(String username){
        this.username = username;
    }

    public void unban(ActionEvent event){
        Main.mainController.client.unbanUser(username);
    }

    public void ban(ActionEvent event){
        Main.mainController.client.banUser(username);
    }

    public void purge(ActionEvent event){
        Main.mainController.client.timeoutUser(username, 1);
    }

    public void timeout30s(ActionEvent event){
        Main.mainController.client.timeoutUser(username, 30);
    }

    public void timeout2m(ActionEvent event){
        Main.mainController.client.timeoutUser(username, 120);
    }

    public void timeout5m(ActionEvent event){
        Main.mainController.client.timeoutUser(username, 300);
    }

    public void timeout10m(ActionEvent event){
        Main.mainController.client.timeoutUser(username, 600);
    }

    public void timeoutCustom(ActionEvent event){
        if (!field_seconds.getText().isEmpty() && field_seconds.getText().matches("[0-9]+") && !field_seconds.getText().equals("0")){
            Main.mainController.client.timeoutUser(username, Integer.parseInt(field_seconds.getText()));
        }
    }

    public void addPoints(ActionEvent event){
        if (!field_points.getText().isEmpty() && field_points.getText().matches("[0-9]+") && !field_points.getText().equals("0")) {
            Main.mainController.detailsController.pointsTabController.addPoints(username, Integer.parseInt(field_points.getText()));
        }
    }

    public void removePoints(ActionEvent event){
        if (!field_points.getText().isEmpty() && field_points.getText().matches("[0-9]+") && !field_points.getText().equals("0")) {
            Main.mainController.detailsController.pointsTabController.removePoints(username, Integer.parseInt(field_points.getText()));
        }
    }

    public void setPoints(ActionEvent event){
        if (!field_points.getText().isEmpty() && field_points.getText().matches("[0-9]+") && !field_points.getText().equals("0")) {
            Main.mainController.detailsController.pointsTabController.setPoints(username, Integer.parseInt(field_points.getText()));
        }
    }
}
