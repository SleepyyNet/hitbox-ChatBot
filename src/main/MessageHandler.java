package main;

import listen.MessageListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class MessageHandler implements MessageListener{

    public ArrayList userList;
    public Map<String, Long> commandTimeout = new HashMap<>();

    public MessageHandler(){
        ArrayList<String> hc = new ArrayList(Arrays.asList("!ban", "!unban", "!timeout"));
        for (String comm : hc){
            hardCommands.add(comm);
        }

        Main.initiator.addMessageListener(this);
        startTimer();
    }

    public void startTimer(){
        try {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            if (Main.config.connected) Main.mainController.client.getUserList();
                            //if (userList != null && userList.size() > 0) Main.statsController.addData(userList.size());
                            Thread.sleep(30000);
                        }
                    } catch (Exception e) {
                        Main.consoleController.eout(e);
                        e.printStackTrace();
                    }
                }
            };

            new Thread(runnable).start();
        } catch (Exception e) {
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

    public ArrayList<String> hardCommands = new ArrayList<>();

    public void messageReceived(JSONObject message){
        try {

            if (!Parser.isBuffer(message) && hardCommands.indexOf(Parser.getText(message).split(" ")[0]) != -1){
                System.out.println(Parser.getUserLevel(message));
                if (Parser.getText(message).startsWith("!ban") && Parser.getUserLevel(message) >= 2){
                    String text = Parser.getText(message);
                    String arr[] = text.split(" ");
                    if (arr.length == 2){
                        Main.mainController.client.banUser(arr[1]);
                    }
                }

                if (Parser.getText(message).startsWith("!unban") && Parser.getUserLevel(message) >= 2){
                    String text = Parser.getText(message);
                    String arr[] = text.split(" ");
                    if (arr.length == 2){
                        Main.mainController.client.unbanUser(arr[1]);
                    }
                }

                if (Parser.getText(message).startsWith("!timeout") && Parser.getUserLevel(message) >= 2){
                    String text = Parser.getText(message);
                    String arr[] = text.split(" ");
                    if (arr.length == 3 && arr[2].matches("[0-9]+")){
                        Main.mainController.client.timeoutUser(arr[1], Integer.parseInt(arr[2]));
                    }
                }
            }
            else if (!Parser.isBuffer(message) && commandExists(Parser.getText(message)) && Parser.getUserLevel(message) >= getCommandLevel(Parser.getText(message)) && !commandInTimeout(Parser.getText(message))) {
                String fMessage = getCommandMessage(Parser.getText(message));
                    fMessage = fMessage
                            .replace("%pN", Main.mainController.detailsController.pointsTabController.pointsName)
                            .replace("%pA", Main.mainController.detailsController.pointsTabController.pointsAmount)
                            .replace("%pT", Main.mainController.detailsController.pointsTabController.pointsTime)
                            .replace("%p", Main.mainController.detailsController.pointsTabController.getPoints(Parser.getName(message).toLowerCase()))
                            .replace("%uN", Parser.getName(message))
                            .replace("%uM", Parser.getText(message));
                Main.mainController.client.sendMessage(fMessage);
                commandTimeout.put(Parser.getText(message), System.currentTimeMillis());
            }

            if (!Parser.isBuffer(message) && Parser.getMethod(message).equals("directMsg") && commandExists("/w " + Parser.getText(message))){
                String fMessage = getCommandMessage("/w " + Parser.getText(message));
                fMessage = fMessage
                        .replace("%pN", Main.mainController.detailsController.pointsTabController.pointsName)
                        .replace("%pA", Main.mainController.detailsController.pointsTabController.pointsAmount)
                        .replace("%pT", Main.mainController.detailsController.pointsTabController.pointsTime)
                        .replace("%p", Main.mainController.detailsController.pointsTabController.getPoints(Parser.getDmFrom(message).toLowerCase())
                        .replace("%uN", Parser.getDmFrom(message))
                        .replace("%uM", Parser.getText(message)));
                Main.mainController.client.sendPrivateMessage(Parser.getDmFrom(message), fMessage + " ignore(" + (new Random().nextInt(90000) + 9999) + ")");
            }

            if (!Parser.isBuffer(message) && Parser.getMethod(message).equals("userList")) {
                userList = Parser.userList(message);
                Main.mainController.detailsController.dashboardTabController.fillTable(userList);
            }


            //### EVENTS

            if (!Parser.isBuffer(message)){

                if (Parser.getMethod(message).equals("infoMsg")) Main.initiator.event_onInfoMsg(message);

                if (Parser.getMethod(message).equals("serverMsg") && message.getJSONObject("params").get("type").equals("resourceUpdate")) Main.initiator.event_onStreamUpdate(message);

                if (Parser.getMethod(message).equals("serverMsg") && message.getJSONObject("params").get("type").equals("hostModeUpdate")) Main.initiator.event_onHostUpdate(message);

                if (Parser.getMethod(message).equals("motdMsg")) Main.initiator.event_onMotdUpdate(message);

                if (Parser.getMethod(message).equals("chatMsg")) Main.initiator.event_onText(message);

                if (Parser.getMethod(message).equals("directMsg")) Main.initiator.event_onWhisper(message);

                if (Parser.getMethod(message).equals("chatLog")) Main.initiator.event_onSpecialChat(message);

                if (Parser.getMethod(message).equals("mediaLog")) Main.initiator.event_onMediaLog(message);

                if (Parser.getMethod(message).equals("notifyMsg")){
                    if (message.getJSONObject("params").getJSONArray("part").length() != 0) {
                        JSONArray arr = message.getJSONObject("params").getJSONArray("part");
                        Main.initiator.event_onPart(arr);
                    }
                    if (message.getJSONObject("params").getJSONArray("join").length() != 0) {
                        JSONArray arr = message.getJSONObject("params").getJSONArray("join");
                        Main.initiator.event_onJoin(arr);
                    }
                }

                if (Parser.getMethod(message).equals("userList")) Main.initiator.event_onUserList(message);

                if (Parser.getMethod(message).equals("userInfo")) Main.initiator.event_onUserInfo(message);

                if (Parser.getMethod(message).equals("infoMsg") && message.getJSONObject("params").get("type").equals("subChannel")) Main.initiator.event_onNewSubscriber(message);

                if (Parser.getMethod(message).equals("raffleMsg") && message.getJSONObject("params").get("status").equals("started")) Main.initiator.event_onGiveawayStared(message);

                if (Parser.getMethod(message).equals("winnerRaffle")) Main.initiator.event_onGiveawayEnded(message);

                if (Parser.getMethod(message).equals("pollMsg") && message.getJSONObject("params").get("status").equals("started")) Main.initiator.event_onPollStared(message);

                if (Parser.getMethod(message).equals("pollMsg") && message.getJSONObject("params").get("status").equals("ended")) Main.initiator.event_onPollEnded(message);

            }

        }catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

    public boolean commandExists(String commandName){
        if (Main.config.getProperty("commands", commandName) != null){
            return true;
        }
        return false;
    }

    public boolean commandInTimeout(String command){
        if (commandTimeout.containsKey(command)){
            long oldTime = commandTimeout.get(command);
            long newTime = System.currentTimeMillis();
            long seconds = (newTime - oldTime)/1000;
            if (seconds <= Integer.parseInt(Main.iniConfig.getProperty("commands", "cooldown"))){
                return true;
            }
        }
        return false;
    }

    public boolean timedMessageExists(String id){
        if (Main.config.getProperty("timed-messages", id) != null){
            return true;
        }
        return false;
    }

    public void addTimedMessage(String id, String message, int interval){
        Main.config.setProperty("timed-messages", id, new JSONObject("{\"message\":\"" + message + "\",\"interval\":\"" + interval + "\",\"enabled\":\"true\"}"));
    }

    public void removeTimedMessage(String id){
        Main.config.removeProperty("timed-messages", id);
    }

    public String getTimedMessage(String id){
        if (Main.config.getProperty("timed-messages", id) != null){
            JSONObject obj = new JSONObject(Main.config.getProperty("timed-messages", id));
            return obj.getString("message");
        }
        return null;
    }

    public ArrayList getTimedMessages() {
        try {
            JSONObject obj = new JSONObject(Main.config.readConfig());
            if (obj.has("timed-messages")) {
                ArrayList arr = new ArrayList();
                for (String key : obj.getJSONObject("timed-messages").keySet()) {
                    arr.add(key);
                }
                return arr;
            }
            return null;
        } catch (Exception e) {
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
        return null;
    }

    public int getTimedMessageInterval(String id){
        if (Main.config.getProperty("timed-messages", id) != null){
            JSONObject obj = new JSONObject(Main.config.getProperty("timed-messages", id));
            return Integer.parseInt(obj.getString("interval"));
        }
        return -1;
    }

    public boolean getTimedMessageState(String id){
        if (Main.config.getProperty("timed-messages", id) != null){
            JSONObject obj = new JSONObject(Main.config.getProperty("timed-messages", id));
            return Boolean.parseBoolean(obj.getString("enabled"));
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
            Main.consoleController.eout(e);
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
