package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.paint.*;
import javafx.scene.web.WebView;
import main.Main;
import main.Parser;
import org.json.JSONObject;

import java.awt.*;
import java.awt.Color;

public class ConsoleController {

    @FXML
    public WebView webView;

    public void init(){
        try {
            webView.getEngine().loadContent("<!DOCTYPE html><html><head lang=\"en\"> <meta charset=\"UTF-8\"> <title></title> <script src=\"http://code.jquery.com/jquery-1.11.3.min.js\"></script> <style>body{background-color: black; margin: 0px;}p{font-family: 'Lucida Console'; font-size: 16px; font-weight: bold; padding: 0px; margin: 3px;}</style></head><body></body></html>");

        }catch (Exception e){
            Main.consoleController.eout(e.toString());
            e.printStackTrace();
        }
    }

    public void out(final String message) {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    webView.getEngine().executeScript("" +
                            "$('body').append('<p><span style=\"color:#" + "D3D3D3" + "\">" + message.replace("\\", "\\\\").replace("'", "\\'") + "</span></p>');" +
                            "window.scrollTo(0, document.body.scrollHeight);" +
                            "if ($('p').length > 1000) $('p:first').remove();");
                }
            });
        } catch (Exception e) {
            Main.consoleController.eout(e.toString());
            e.printStackTrace();
        }
    }

    public void eout(final String message) {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    webView.getEngine().executeScript("" +
                            "$('body').append('<p><span style=\"color:#" + "FF0000" + "\">" + message.replace("\\", "\\\\").replace("'", "\\'") + "</span></p>');" +
                            "window.scrollTo(0, document.body.scrollHeight);" +
                            "if ($('p').length > 1000) $('p:first').remove();");
                }
            });
        } catch (Exception e) {
            Main.consoleController.eout(e.toString());
            e.printStackTrace();
        }
    }

    public void out(final String message, final String color) {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    webView.getEngine().executeScript("" +
                            "$('body').append('<p><span style=\"color:#" + color + "\">" + message.replace("\\", "\\\\").replace("'", "\\'") + "</span></p>');" +
                            "window.scrollTo(0, document.body.scrollHeight);" +
                            "if ($('p').length > 1000) $('p:first').remove();");
                }
            });
        } catch (Exception e) {
            Main.consoleController.eout(e.toString());
            e.printStackTrace();
        }
    }

}
