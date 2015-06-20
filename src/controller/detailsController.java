package controller;

import controller.tabs.CommandsTab;
import controller.tabs.DashboardTab;
import controller.tabs.PointsGamesTab;
import controller.tabs.PointsTab;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import listen.MessageListener;
import main.Main;
import main.Parser;
import org.json.JSONObject;

import java.util.Collection;

public class detailsController implements MessageListener{

    @FXML
    public WebView webView;

    @FXML
    public TextField details_field_message;

    @FXML
    public TabPane details_tabpane;


    public CommandsTab commandsTabController;
    public PointsTab pointsTabController;
    public DashboardTab dashboardTabController;
    public PointsGamesTab pointsGamesTabController;


    public void init(){
        Main.initiator.addListener(this);
        webView.getEngine().loadContent("<!DOCTYPE html><html><head> <title></title> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> <script src=\"http://code.jquery.com/jquery-1.11.3.min.js\"></script> <script src=\"https://dl.dropboxusercontent.com/u/23313911/Development/assets/hitboxBot/emotify.js\"></script> <style>body{font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;}p{padding: 5px; margin: 5px;}img{max-height: 200px; width: auto;}.smiley{max-height: 32px; width: auto;}a{pointer-events: none;}</style></head><body><script>$.getJSON(\"http://api.hitbox.tv/chat/icons/masta.json\", function(json){emotify.emoticons(\"http://edge.sf.hitbox.tv\", json.icons);});</script></body></html>");
        //webView.getEngine().load("http://www.youtube.com/embed/gt0yhHthKz4?autoplay=1");

        try {
            details_tabpane.getTabs().clear();

            FXMLLoader commandsTabContent = new FXMLLoader(getClass().getResource("/fxml/tabs/commands.fxml"));
            details_tabpane.getTabs().add(0, new Tab("Commands"));
            details_tabpane.getTabs().get(0).setContent((AnchorPane)commandsTabContent.load());
            commandsTabController = (CommandsTab) commandsTabContent.getController();
            commandsTabController.init();

            FXMLLoader pointsTabContent = new FXMLLoader(getClass().getResource("/fxml/tabs/points.fxml"));
            details_tabpane.getTabs().add(1, new Tab("Points"));
            details_tabpane.getTabs().get(1).setContent((AnchorPane)pointsTabContent.load());
            pointsTabController = (PointsTab) pointsTabContent.getController();
            pointsTabController.init();

            FXMLLoader dashboardTabContent = new FXMLLoader(getClass().getResource("/fxml/tabs/dashboard.fxml"));
            details_tabpane.getTabs().add(2, new Tab("Dashboard"));
            details_tabpane.getTabs().get(2).setContent((AnchorPane)dashboardTabContent.load());
            dashboardTabController = (DashboardTab) dashboardTabContent.getController();
            dashboardTabController.init();

            FXMLLoader pointsGamesTabContent= new FXMLLoader(getClass().getResource("/fxml/tabs/pointsGames.fxml"));
            details_tabpane.getTabs().add(3, new Tab("Points Games"));
            details_tabpane.getTabs().get(3).setContent((AnchorPane)pointsGamesTabContent.load());
            pointsGamesTabController = (PointsGamesTab) pointsGamesTabContent.getController();
            pointsGamesTabController.init();

            details_tabpane.getTabs().setAll(details_tabpane.getTabs().get(2), details_tabpane.getTabs().get(0),details_tabpane.getTabs().get(1), details_tabpane.getTabs().get(3));
            details_tabpane.getSelectionModel().select(0);

        }catch (Exception e){
            Main.consoleController.eout(e.toString());
            e.printStackTrace();
        }
    }

    public void details_event_send(ActionEvent event){
        if (details_field_message.getText().length() > 0) {
            Main.mainController.client.sendMessage(details_field_message.getText());
            details_field_message.setText("");
        }
    }

    private void newMessage(final JSONObject message){
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (!Parser.isBuffer(message) && Parser.isChatMessage(message)) {
                        //webView.getEngine().executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startClosed');}");
                        webView.getEngine().executeScript("" +
                                "var text = '" + Parser.getText(message).replace("\\", "\\\\").replace("'", "\\'") + "';" +
                                "var newText = emotify(text);" +
                                "$('body').append('<p>" + Parser.getRTime() + " - " + "<span style=\\'color:#" + Parser.getNameColour(message) + "\\'>" + Parser.getName(message) + "</span>: ' + newText + '</p>');" +
                                "window.scrollTo(0, document.body.scrollHeight);" +
                                "if ($('p').length > 100) $('p:first').remove();");
                    }
                }
            });
        }catch (Exception e){
            Main.consoleController.eout(e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void messageReceived(JSONObject message){
        newMessage(message);
    }



}
