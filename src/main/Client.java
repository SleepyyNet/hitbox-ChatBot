package main;

import config.Config;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

public class Client extends WebSocketClient {

    String name;
    String channel;

    public Client(String name, String pass, String channel, String IP) throws Exception{
        super(new URI("ws://" + IP + "/socket.io/1/websocket/" + getID(IP)), new Draft_10());
        this.name = name;
        this.channel = channel;
        connectBlocking();
        joinChannel(name, pass, channel.toLowerCase());
    }

    public void onOpen(ServerHandshake handshakedata) {

    }

    public void onMessage(String message){
        if (message.equals("2::")) {
            this.send("2::");
        } else if (message.startsWith("5:::")){
            Main.initiator.sendMessage(new JSONObject(new JSONObject(message.substring(message.indexOf("{"))).getJSONArray("args").get(0).toString()));
        }
    }

    public void onClose(int code, String reason, boolean remote){
    }

    public void onError(Exception e){
        Main.consoleController.eout(e.toString());
        e.printStackTrace();
    }

    public void joinChannel(String name, String pass, String channel){
        if (getToken(name, pass) != null) {
            this.send("5:::{\"name\":\"message\",\"args\":[{\"method\":\"joinChannel\",\"params\":{\"channel\":\"" + channel + "\",\"name\":\"" + name + "\",\"token\":\"" + getToken(name, pass) + "\",\"isAdmin\":false}}]}");
            Main.mainController.connected();
        }
    }

    public void sendMessage(String message){
        this.send("5:::{\"name\":\"message\",\"args\":[{\"method\":\"chatMsg\",\"params\":{\"channel\":\"" + this.channel + "\",\"name\":\"" + this.name + "\",\"nameColor\":\"FA5858\",\"text\":\"" + message + "\"}}]}");
    }

    public void sendPrivateMessage(String name, String message){
        this.send("5:::{\"name\":\"message\",\"args\":[{\"method\":\"directMsg\",\"params\":{\"channel\":\"" + this.channel + "\",\"from\":\"" + this.name + "\",\"to\":\"" + name + "\",\"nameColor\":\"FA5858\",\"text\":\"" + message + "\"}}]}");
    }

    public static String readUrl(String urlString) {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }
            reader.close();
            return buffer.toString();
        }catch (Exception e){
            Main.consoleController.eout(e.toString());
            e.printStackTrace();
        }
        return null;
    }

    //Receives the auth token for the entered username and password.
    private static String token;
    public static String getToken(String name, String pass){
        try {
            URL url = new URL("http://www.hitbox.tv/api/auth/token");
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            try (DataOutputStream os = new DataOutputStream(connection.getOutputStream ())) {
                String content = "login=" + name + "&pass=" + pass;
                os.writeBytes(content);
                os.flush();
            }
            try (DataInputStream is = new DataInputStream(connection.getInputStream ())) {
                token = new JSONObject(is.readLine()).get("authToken").toString();
                return token;
            }
        }catch (Exception e){
            Main.consoleController.eout(e.toString());
            //e.printStackTrace();
        }
        return null;
    }

    //Receives an IP address to connect to.
    public static String getIP(){
        JSONArray arr = new JSONArray(readUrl("http://hitbox.tv/api/chat/servers.json?redis=true"));
        return arr.getJSONObject(0).getString("server_ip");
    }

    private static String getID(String IP){
        String connectionID = readUrl("http://" + IP + "/socket.io/1/");
        String ID = connectionID.substring(0, connectionID.indexOf(":"));
        return ID;
    }


    public void getUserList(){
        this.send("5:::{\"name\":\"message\",\"args\":[{\"method\":\"getChannelUserList\",\"params\":{\"channel\":\"" + Main.config.channel + "\"}}]}");
    }

    public void getUserInfo(String name){
        this.send("5:::{\"name\":\"message\",\"args\":[{\"method\":\"getChannelUser\",\"params\":{\"channel\":\"" + Main.config.channel + "\",\"name\":\"" + name + "\"}}]}");
    }

}
