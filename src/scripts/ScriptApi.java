package scripts;

import main.Main;
import main.Parser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScriptApi {
    private static final ScriptApi instance = new ScriptApi();

    public static ScriptApi instance(){
        return instance;
    }

    public void log(String message){
        System.out.println(message);
        Main.consoleController.out(message);
    }

    public void log(String message, String colour){
        Main.consoleController.out(message, colour);
    }

    public String readFile(Script script, String fileName){
        File file = new File(script.file.getParent() + "/" + fileName);
        if (file.exists()) return Parser.readFile(file);
        return "";
    }

    public void writeFile(Script script, String fileName, String content){
        File file = new File(script.file.getParent() + "/" + fileName);
        Parser.writeFile(file, content);
    }

    public void addToFile(Script script, String fileName, String content){
        File file = new File(script.file.getParent() + "/" + fileName);
        Parser.writeFile(file, Parser.readFile(file) + "\r\n" + content);
    }

    public void printToChat(String message){
        Main.mainController.detailsController.printToChat(message);
    }

    public boolean isConnected(){
        return Main.config.connected;
    }

    public String getBotName(){
        return Main.config.username;
    }

    public void sendMessage(String message){
        if (Main.mainController.client != null) {
            Main.mainController.client.sendMessage(message);
        }
    }

    public String getChannel(){
        return Main.config.channel;
    }

    public String getDate(){
        DateFormat df = new SimpleDateFormat("MM-dd-yy_HH-mm");
        Date today = Calendar.getInstance().getTime();
        String date = df.format(today);
        return date;
    }

    public String getTime(){
        return Parser.getRTime();
    }

    public int getUserLevel(String message){
        return Parser.getUserLevel(new JSONObject(message));
    }

    public String getUserList(){
        JSONArray arr = new JSONArray(Main.messageHandler.userList);
        return arr.toString();
    }

    public String getCommands(){
        JSONArray arr = new JSONArray(Main.messageHandler.getCommands());
        return arr.toString();
    }

    public String getCommand(String commandName){
        return Main.messageHandler.getCommand(commandName).toString();
    }

    public String getCommandMessage(String commandName){
        return Main.messageHandler.getCommandMessage(commandName);
    }

    public int getCommandLevel(String commandName){
        return Main.messageHandler.getCommandLevel(commandName);
    }

    public String getPoints(String name){
        return Main.mainController.detailsController.pointsTabController.getPoints(name.toLowerCase());
    }

    public void addPoints(String name, int amount){
        Main.mainController.detailsController.pointsTabController.addPoints(name.toLowerCase(), amount);
    }

    public void removePoints(String name, int amount){
        Main.mainController.detailsController.pointsTabController.removePoints(name.toLowerCase(), amount);
    }

    public void setPoints(String name, int amount){
        Main.mainController.detailsController.pointsTabController.setPoints(name.toLowerCase(), amount);
    }

    public void banUser(String name){
        Main.mainController.client.banUser(name);
    }

    public void unbanUser(String name){
        Main.mainController.client.unbanUser(name);
    }

    public void timeoutUser(String name, int time){
        Main.mainController.client.timeoutUser(name, time);
    }

}
