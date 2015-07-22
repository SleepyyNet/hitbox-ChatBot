package controller;

import controller.tabs.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import listen.EventListener;
import listen.MessageListener;
import main.EditUser;
import main.Main;
import main.Parser;
import org.apache.commons.lang3.StringEscapeUtils;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;
import org.json.JSONObject;

import java.awt.*;
import java.net.URI;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailsController implements MessageListener {

    @FXML
    public WebView webView;

    @FXML
    public TextField details_field_message;

    @FXML
    public TabPane details_tabpane;

    public CommandsTab commandsTabController;
    public TimedMessagesTab timedMessagesTabController;
    public PointsTab pointsTabController;
    public DashboardTab dashboardTabController;
    public PointsGamesTab pointsGamesTabController;
    public ScriptsTab scriptsTabController;

    String subBadge;

    Boolean timestampEnabled;


    LinkedHashMap<CheckMenuItem, Tab> tabs = new LinkedHashMap<>();
    public void init() {

        JSONObject obj = new JSONObject(Parser.readUrl("http://api.hitbox.tv/mediabadges/" + Main.config.channel));
        subBadge = "http://edge.sf.hitbox.tv" + obj.getJSONArray("badges").getJSONObject(0).get("badge_image").toString();

        Main.initiator.addMessageListener(this);

        String backgroundColor = Main.iniConfig.getProperty("chat", "background-color");
        String fontColor = Main.iniConfig.getProperty("chat", "font-color");
        String fontSize = Main.iniConfig.getProperty("chat", "font-size");
        String fontFamily = Main.iniConfig.getProperty("chat", "font-family");

        webView.getEngine().loadContent("<!DOCTYPE html><html><head> <title></title> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> <script src=\"http://code.jquery.com/jquery-1.11.3.min.js\"></script> <script src=\"https://dl.dropboxusercontent.com/u/23313911/Development/assets/hitboxBot/emotify.js\"></script> <style>body{font-family: " + fontFamily + "; background-color: " + backgroundColor + ";color: " + fontColor + ";font-size: " + fontSize + ";}p{padding: 2px; margin: 2px;}img{max-height: 200px; width: auto;}.smiley{max-height: 32px; width: auto; vertical-align: middle;}a{text-decoration: none;}.badge{vertical-align: middle;}</style></head><body><script>$.getJSON(\"http://api.hitbox.tv/chat/icons/masta.json\", function(json){emotify.emoticons(\"http://edge.sf.hitbox.tv\", json.icons);});</script><div id=\"chat\"></div></body></html>");

        try {
            details_tabpane.getTabs().clear();

            FXMLLoader commandsTabContent = new FXMLLoader(getClass().getResource("/fxml/tabs/commands.fxml"));
            Tab commandsTab = new Tab("Commands");
            commandsTab.setContent((AnchorPane) commandsTabContent.load());
            commandsTabController = (CommandsTab) commandsTabContent.getController();
            commandsTabController.init();

            FXMLLoader timedMessagesTabContent = new FXMLLoader(getClass().getResource("/fxml/tabs/timedMessages.fxml"));
            Tab timedMessagesTab = new Tab("Timed Messages");
            timedMessagesTab.setContent((AnchorPane) timedMessagesTabContent.load());
            timedMessagesTabController = (TimedMessagesTab) timedMessagesTabContent.getController();
            timedMessagesTabController.init();

            FXMLLoader pointsTabContent = new FXMLLoader(getClass().getResource("/fxml/tabs/points.fxml"));
            Tab pointsTab = new Tab("Points");
            pointsTab.setContent((AnchorPane) pointsTabContent.load());
            pointsTabController = (PointsTab) pointsTabContent.getController();
            pointsTabController.init();

            FXMLLoader dashboardTabContent = new FXMLLoader(getClass().getResource("/fxml/tabs/dashboard.fxml"));
            Tab dashboardTab = new Tab("Dashboard");
            dashboardTab.setContent((AnchorPane) dashboardTabContent.load());
            dashboardTabController = (DashboardTab) dashboardTabContent.getController();
            dashboardTabController.init();

            FXMLLoader pointsGamesTabContent = new FXMLLoader(getClass().getResource("/fxml/tabs/pointsGames.fxml"));
            Tab pointsGamesTab = new Tab("Points Games");
            pointsGamesTab.setContent((AnchorPane) pointsGamesTabContent.load());
            pointsGamesTabController = (PointsGamesTab) pointsGamesTabContent.getController();
            pointsGamesTabController.init();

            FXMLLoader scriptsTabContent = new FXMLLoader(getClass().getResource("/fxml/tabs/scripts.fxml"));
            Tab scriptsTab = new Tab("Scripts");
            scriptsTab.setContent((AnchorPane) scriptsTabContent.load());
            scriptsTabController = (ScriptsTab) scriptsTabContent.getController();
            scriptsTabController.init();

            tabs.put(new CheckMenuItem("Dashboard"), dashboardTab);
            tabs.put(new CheckMenuItem("Commands"), commandsTab);
            tabs.put(new CheckMenuItem("Timed Messages"), timedMessagesTab);
            tabs.put(new CheckMenuItem("Points"), pointsTab);
            tabs.put(new CheckMenuItem("Points Games"), pointsGamesTab);
            tabs.put(new CheckMenuItem("Scripts"), scriptsTab);

            //List<Tab> order = Arrays.asList(dashboardTab, commandsTab, timedMessagesTab, pointsTab, pointsGamesTab, scriptsTab);
            List<String> tabList = Arrays.asList(dashboardTab.getText(), commandsTab.getText(), timedMessagesTab.getText(), pointsTab.getText(), pointsGamesTab.getText(), scriptsTab.getText());
            List<Tab> order = new ArrayList();
            for (CheckMenuItem key : tabs.keySet())
            {
                if (Main.iniConfig.getProperty("tabs", tabs.get(key).getText()) == null) Main.iniConfig.setProperty("tabs", tabs.get(key).getText(), "true");
                if (Boolean.parseBoolean(Main.iniConfig.getProperty("tabs", tabs.get(key).getText()))) order.add(tabs.get(key));
            }


            for (int i = 0; i < order.size(); i++) {
                details_tabpane.getTabs().add(i, order.get(i));
            }
            details_tabpane.getSelectionModel().select(0);

            linkListener();
        } catch (Exception e) {
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

    public void refreshStyle(){
        final String backgroundColor = Main.iniConfig.getProperty("chat", "background-color");
        final String fontColor = Main.iniConfig.getProperty("chat", "font-color");
        final String fontSize = Main.iniConfig.getProperty("chat", "font-size");
        final String fontFamily = Main.iniConfig.getProperty("chat", "font-family");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                webView.getEngine().executeScript("" +
                        "$(\"body\").css(\"backgroundColor\", \"" + backgroundColor + "\");" +
                        "$(\"body\").css(\"color\", \"" + fontColor + "\");" +
                        "$(\"body\").css(\"fontSize\", \"" + fontSize + "\");" +
                        "$(\"body\").css(\"fontFamily\", \"" + fontFamily + "\");");
            }
        });
    }

    public void details_event_send(ActionEvent event) {
        if (details_field_message.getText().length() > 0) {
            Main.mainController.client.sendMessage(details_field_message.getText().replace("\\", "\\\\").replace("\"", "\\\""));
            details_field_message.setText("");
        }
    }

    private void newMessage(final JSONObject message) {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (!Parser.isBuffer(message) && Parser.isChatMessage(message)) {
                        if (Main.iniConfig.getProperty("chat", "timestamp-enabled") == null) Main.iniConfig.setProperty("chat", "timestamp-enabled", "true");
                        timestampEnabled = Boolean.parseBoolean(Main.iniConfig.getProperty("chat", "timestamp-enabled"));
                        //webView.getEngine().executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startClosed');}");
                        webView.getEngine().executeScript("" +
                                "var text = '" + StringEscapeUtils.escapeEcmaScript(Parser.getText(message)) + "';" +
                                "var newText = emotify(text);" +
                                "if ((" + Parser.isModerator(message) + " || " + Parser.isAdmin(message) + ") && " + Parser.isSubscriber(message) + ") $('#chat').append('<p><span class=\"timestamp\">[" + Parser.getRTime() + "]</span> <span class=\"badge\" style=\"background-image:url(\\'https://dl.dropboxusercontent.com/u/23313911/Development/assets/hitboxBot/modIcon.png\\');background-size: 16px 16px;width: 16px; height: 16px;display:inline-block;\"></span> <span class=\"badge\" style=\"background-image:url(\\'" + subBadge + "\\');background-size: 16px 16px;width: 16px; height: 16px;display:inline-block;\"></span>" + " <a href=\\'http://hname-" + Parser.getName(message) + "\\'><span style=\\'color:#" + Parser.getNameColour(message) + "\\'>" + Parser.getName(message) + "</span></a>: ' + newText + '</p>');" +
                                "else if (" + Parser.isModerator(message) + " || " + Parser.isAdmin(message) + ") $('#chat').append('<p><span class=\"timestamp\">[" + Parser.getRTime() + "]</span> <span class=\"badge\" style=\"background-image:url(\\'https://dl.dropboxusercontent.com/u/23313911/Development/assets/hitboxBot/modIcon.png\\');background-size: 16px 16px;width: 16px; height: 16px;display:inline-block;\"></span>" + " <a href=\\'http://hname-" + Parser.getName(message) + "\\'><span style=\\'color:#" + Parser.getNameColour(message) + "\\'>" + Parser.getName(message) + "</span></a>: ' + newText + '</p>');" +
                                "else if (" + Parser.isSubscriber(message) + ") $('#chat').append('<p><span class=\"timestamp\">[" + Parser.getRTime() + "]</span> <span class=\"badge\" style=\"background-image:url(\\'" + subBadge + "\\');background-size: 16px 16px;width: 16px; height: 16px;display:inline-block;\"></span>" + " <a href=\\'http://hname-" + Parser.getName(message) + "\\'><span style=\\'color:#" + Parser.getNameColour(message) + "\\'>" + Parser.getName(message) + "</span></a>: ' + newText + '</p>');" +
                                "else $('#chat').append('<p><span class=\"timestamp\">[" + Parser.getRTime() + "]</span> " + "<a href=\\'http://hname-" + Parser.getName(message) + "\\'><span style=\\'color:#" + Parser.getNameColour(message) + "\\'>" + Parser.getName(message) + "</span></a>: ' + newText + '</p>');" +
                                "if (!" + timestampEnabled + ") $('.timestamp').remove();" +
                                //"$('#chat').append('<p>" + timestampEnabled.toString() + "</p>');" +
                                "window.scrollTo(0, document.body.scrollHeight);" +
                                "if ($('p').length > 100) $('p:first').remove();");
                    }
                }
            });
        } catch (Exception e) {
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

    public void printToChat(final String message) {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    webView.getEngine().executeScript("" +
                            "$('#chat').append('<p>" + message + "</p>');");
                }
            });
        } catch (Exception e) {
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

    private void linkListener() {
        webView.getEngine().locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, final String oldLoc, final String loc) {
                try {
                    final String oldContent = (String) webView.getEngine().executeScript("document.getElementsByTagName('html')[0].innerHTML");
                    if (loc.length() > 0) {
                        if (loc.startsWith("http://hname-")){
                            new EditUser(loc.substring(loc.indexOf("-") + 1).replace("/", ""));
                        }
                        else Desktop.getDesktop().browse(new URI(loc));
                    }
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            webView.getEngine().loadContent(oldContent);
                        }
                    });
                } catch (Exception e) {
                    Main.consoleController.eout(e);
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void messageReceived(JSONObject message) {
        newMessage(message);
    }

}
