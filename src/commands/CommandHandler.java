package commands;

import listen.MessageListener;
import main.Main;
import main.Parser;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class CommandHandler implements MessageListener{

    public ArrayList userList;

    public CommandHandler(){
        Main.initiator.addListener(this);
    }

    public ArrayList<String> hardCommands = new ArrayList<>();

    public void messageReceived(JSONObject message){
        try {

            if (!Parser.isBuffer(message) && hardCommands.indexOf(Parser.getText(message).split(" ")[0]) != -1){

            }
            else if (!Parser.isBuffer(message) && commandExists(Parser.getText(message)) && Parser.getUserLevel(message) >= getCommandLevel(Parser.getText(message))) {
                String fMessage = getCommandMessage(Parser.getText(message));
                if (getCommandMessage(Parser.getText(message)).contains("%pN") || getCommandMessage(Parser.getText(message)).contains("%pA") || getCommandMessage(Parser.getText(message)).contains("%pT") || getCommandMessage(Parser.getText(message)).contains("%p"))
                {
                    fMessage = fMessage
                            .replace("%pN", Main.mainController.root2Controller.pointsTabController.pointsName)
                            .replace("%pA", Main.mainController.root2Controller.pointsTabController.pointsAmount)
                            .replace("%pT", Main.mainController.root2Controller.pointsTabController.pointsTime)
                            .replace("%p", Main.mainController.root2Controller.pointsTabController.getPoints(Parser.getName(message).toLowerCase()));
                }
                Main.mainController.client.sendMessage(fMessage);
            }

            if (!Parser.isBuffer(message) && Parser.getMethod(message).equals("directMsg") && commandExists("/w " + Parser.getText(message))){
                String fMessage = getCommandMessage("/w " + Parser.getText(message));
                fMessage = fMessage
                        .replace("%pN", Main.mainController.root2Controller.pointsTabController.pointsName)
                        .replace("%pA", Main.mainController.root2Controller.pointsTabController.pointsAmount)
                        .replace("%pT", Main.mainController.root2Controller.pointsTabController.pointsTime)
                        .replace("%p", Main.mainController.root2Controller.pointsTabController.getPoints(Parser.getDmFrom(message).toLowerCase()));
                Main.mainController.client.sendPrivateMessage(Parser.getDmFrom(message), fMessage + " ignore(" + (new Random().nextInt(90000) + 9999) + ")");
            }

            if (!Parser.isBuffer(message) && Parser.getMethod(message).equals("userList")) {
                userList = Parser.userList(message);
                Main.mainController.root2Controller.dashboardTabController.fillTable(userList);
            }
        }catch (Exception e){
            Main.consoleController.eout(e.toString());
            e.printStackTrace();
        }
    }

    public boolean commandExists(String commandName){
        if (Main.config.getProperty("commands", commandName) != null){
            return true;
        }
        return false;
    }

    public void addCommand(String commandName, String commandMessage, int userLevel){
        Main.config.setProperty("commands", commandName, commandMessage + "/," + userLevel);
    }

    public void removeCommand(String commandName){
        Main.config.removeProperty("commands", commandName);
    }

    public String[] getCommand(String commandName){
        if (Main.config.getProperty("commands", commandName) != null){
            return Main.config.getProperty("commands", commandName).split("/,");
        }
        return null;
    }

    public ArrayList getCommands() {
        try {
            JSONObject obj = new JSONObject(Main.config.readConfig());
            if (obj.has("commands")) {
                ArrayList arr = new ArrayList();
                for (String key : obj.getJSONObject("commands").keySet()) {
                    arr.add(key);
                }
                return arr;
            }
            return null;
        } catch (Exception e) {
            Main.consoleController.eout(e.toString());
            e.printStackTrace();
        }
        return null;
    }

    public String getCommandMessage(String commandName){
        if (Main.config.getProperty("commands", commandName) != null){
            return Main.config.getProperty("commands", commandName).split("/,")[0];
        }
        return null;
    }

    public int getCommandLevel(String commandName){
        if (Main.config.getProperty("commands", commandName) != null){
            return Integer.parseInt(Main.config.getProperty("commands", commandName).split("/,")[1]);
        }
        return -1;
    }

}
