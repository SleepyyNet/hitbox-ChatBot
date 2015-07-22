package controller.tabs;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import main.EditUser;
import main.Main;
import main.Parser;
import org.json.JSONObject;

import java.util.ArrayList;

public class DashboardTab {

    @FXML
    public TextField dashboard_field_title;

    @FXML
    public TextField dashboard_field_game;

    @FXML
    public ChoiceBox dashboard_choiceBox_games;

    @FXML
    public TableView tableView;

    @FXML
    public TableColumn userLevelCol;

    @FXML
    public TableColumn viewerNameCol;

    @FXML
    public TableColumn viewerPointsCol;


    private final ObservableList<Viewer> data = FXCollections.observableArrayList();

    public ArrayList userList;

    String subBadge;

    public void init(){
        try {

            userLevelCol.setCellValueFactory(new PropertyValueFactory<Viewer, String>("userLevel"));
            viewerNameCol.setCellValueFactory(new PropertyValueFactory<Viewer, String>("viewerName"));
            viewerPointsCol.setCellValueFactory(new PropertyValueFactory<Viewer, String>("viewerPoints"));

            tableView.setItems(data);

        }catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

    public void fillTable(ArrayList userList){
        try {
            this.userList = userList;
            if (this.userList != null && !this.userList.isEmpty() && !tableView.getContextMenu().isShowing()) {
                if (tableView.getItems() != null) {
                    tableView.getSelectionModel().clearSelection();
                    data.removeAll(tableView.getItems());
                }
                for (int i = 0; i < this.userList.size(); i += 2) {
                    data.add(new Viewer(
                            this.userList.get(i + 1).toString(),
                            this.userList.get(i).toString(),
                            Main.mainController.detailsController.pointsTabController.getPoints(this.userList.get(i).toString())));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static class Viewer {
        private final SimpleStringProperty userLevel;
        private final SimpleStringProperty viewerName;
        private final SimpleStringProperty viewerPoints;

        private Viewer(String userLevel, String viewerName, String viewerPoints){
            this.userLevel = new SimpleStringProperty(userLevel);
            this.viewerName = new SimpleStringProperty(viewerName);
            this.viewerPoints = new SimpleStringProperty(viewerPoints);
        }

        public String getUserLevel(){
            return userLevel.get();
        }

        public String getViewerName(){
            return viewerName.get();
        }

        public String getViewerPoints(){
            return viewerPoints.get();
        }
    }

    public void gameTextChanged(KeyEvent event){
        try {
            JSONObject obj = new JSONObject(Parser.readUrl("http://www.hitbox.tv/api/games?liveonly=false&q=" + dashboard_field_game.getText().replace(" ", "%20")));
            dashboard_choiceBox_games.getItems().clear();
            for (int i = 0; i < obj.getJSONArray("categories").length(); i++) {
                //gameList.addItem(obj.getJSONArray("categories").getJSONObject(i).get("category_name"));
                dashboard_choiceBox_games.getItems().add(obj.getJSONArray("categories").getJSONObject(i).get("category_name"));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void setGame(ActionEvent event){
        if (!dashboard_choiceBox_games.getSelectionModel().getSelectedItem().toString().isEmpty()) {
            JSONObject obj = new JSONObject(Parser.readUrl("http://www.hitbox.tv/api/games?liveonly=false&q=" + dashboard_choiceBox_games.getSelectionModel().getSelectedItem().toString().toLowerCase().replace(" ", "%20")));
            String obj2 = "" +
                    "{\"livestream\":[" +
                    "{" +
                    "\"media_status\":\"" + new JSONObject(Parser.readUrl("http://www.hitbox.tv/api/media/live/" + Main.config.username + "?showHidden=true")).getJSONArray("livestream").getJSONObject(0).get("media_status").toString() + "\"," +
                    "\"media_category_id\":\"" + obj.getJSONArray("categories").getJSONObject(0).get("category_id") + "\"," +
                    "\"media_id\":\"" + new JSONObject(Parser.readUrl("http://www.hitbox.tv/api/media/live/" + Main.config.username + "?showHidden=true")).getJSONArray("livestream").getJSONObject(0).get("media_id").toString() + "\"," +
                    "\"media_user_name\":\"" + new JSONObject(Parser.readUrl("http://www.hitbox.tv/api/media/live/" + Main.config.username + "?showHidden=true")).getJSONArray("livestream").getJSONObject(0).get("media_user_name").toString() + "\"" +
                    "}" +
                    "]}";
            Parser.putUrl("http://www.hitbox.tv/api/media/live/" + Main.config.channel + "?authToken=" + Main.mainController.client.getToken(Main.config.username, Main.config.password), obj2);
        }
    }

    public void setTitle(ActionEvent event){
        String obj = "" +
                "{\"livestream\":[" +
                "{" +
                "\"media_status\":\"" + dashboard_field_title.getText() + "\"," +
                "\"media_category_id\":\"" + new JSONObject(Parser.readUrl("http://www.hitbox.tv/api/media/live/" + Main.config.username + "?showHidden=true")).getJSONArray("livestream").getJSONObject(0).get("category_id").toString() + "\"," +
                "\"media_id\":\"" + new JSONObject(Parser.readUrl("http://www.hitbox.tv/api/media/live/" + Main.config.username + "?showHidden=true")).getJSONArray("livestream").getJSONObject(0).get("media_id").toString() + "\"," +
                "\"media_user_name\":\"" + new JSONObject(Parser.readUrl("http://www.hitbox.tv/api/media/live/" + Main.config.username + "?showHidden=true")).getJSONArray("livestream").getJSONObject(0).get("media_user_name").toString() + "\"" +
                "}" +
                "]}";
        Parser.putUrl("http://www.hitbox.tv/api/media/live/" + Main.config.channel + "?authToken=" + Main.mainController.client.getToken(Main.config.username, Main.config.password), obj);
    }

    public void editUser(){
        int id = tableView.getSelectionModel().getSelectedIndex();
        new EditUser(data.get(id).getViewerName());
    }

    public int getLevelFromUserList(String name){
        int level = Integer.parseInt(Main.messageHandler.userList.get(Main.messageHandler.userList.indexOf(name) + 1).toString());
        return level;
    }

}
