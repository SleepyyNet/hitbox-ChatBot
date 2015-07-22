package main;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import javax.crypto.Cipher;

public class Parser {
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
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
        return null;
    }

    public static String readFile(File file){
        try {
            if (file.length() > 0) {
                String content = new Scanner(file, "UTF-8").useDelimiter("\\Z").next();
                return content;
            }

            return "";
        } catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
            return "";
        }
    }

    public static void writeFile(File file, String content) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);

            fw.write(content + "\n");

            fw.close();
        }catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

    public static void putUrl(String urlString, String message){
        try {

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("PUT");
            connection.connect();

            OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
            osw.write(message);
            osw.flush();
            osw.close();
            System.err.println(connection.getResponseCode());

        } catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }



    public static void writeResource(String fileName, String message) {
        try {
            if (!new File(Main.rootPath + "/resources/" + fileName).exists()) {
                new File(Main.rootPath + "/resources/" + fileName).createNewFile();
            }
            FileWriter fw = new FileWriter(new File(Main.rootPath + "/resources/" + fileName));

            fw.write(message + "\n");

            fw.close();
        }catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

    public static String readResource(String fileName){
        try {
            String content = new Scanner(new File(Main.rootPath + "/resources/" + fileName), "UTF-8").useDelimiter("\\Z").next();
            return content;
        } catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
            return null;
        }
    }

    public static String getMethod(JSONObject obj){
        try {
            String method = obj.getString("method");
            return method;
        } catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isChatMessage(JSONObject obj){
        if (obj.getString("method").equals("chatMsg")) {
            return true;
        }
        return false;
    }

    public static boolean isBuffer(JSONObject obj){
        if (obj.getJSONObject("params").optBoolean("buffer")) return true;
        return false;
    }

    public static boolean isCommand(JSONObject obj){
        if (getText(obj).startsWith("!")){
            return true;
        } else {
            return false;
        }
    }

    //Returns the name of the message sender.
    public static String getName(JSONObject obj){
        String name = "";
        //if (obj.getString("method").equals("chatMsg")){
        name = obj.getJSONObject("params").optString("name");
        //}
        return name;
    }

    public static String getDmFrom(JSONObject obj){
        String name = "";
        name = obj.getJSONObject("params").optString("from");
        return name;
    }

    //Returns the name colour of the message sender.
    public static String getNameColour(JSONObject obj){
        String nameColour = "";
        if (obj.getString("method").equals("chatMsg")){
            nameColour = obj.getJSONObject("params").getString("nameColor");
        }
        return nameColour;
    }

    //Returns the text of the message received.
    public static String getText(JSONObject obj){
        String text = "";
        if (obj.getString("method").equals("chatMsg") || obj.getString("method").equals("directMsg")){
            text = obj.getJSONObject("params").getString("text");
        }
        return text;
    }

    //Returns the time that the message was received.
    public static int getTime(JSONObject obj){
        int time = 0;
        if (obj.getString("method").equals("chatMsg")){
            time = obj.getJSONObject("params").getInt("time");
        }
        return time;
    }

    public static String getRTime(){
        Date date = new Date(System.currentTimeMillis());
        //Date date = new Date(getTime(message) * 1000);
        String RTime = date.toString().substring(date.toString().indexOf(":") - 2, date.toString().indexOf(":") + 3);
        return RTime;
    }

    public static String getRole(JSONObject obj){
        String role = obj.getJSONObject("params").optString("role");
        return role;
    }

    public static boolean isFollower(JSONObject obj){
        return obj.getJSONObject("params").getBoolean("isFollower");
    }

    public static boolean isSubscriber(JSONObject obj){
        return obj.getJSONObject("params").getBoolean("isSubscriber");
    }

    public static int getUserLevel(JSONObject obj){
        if (obj.getJSONObject("params").get("role").toString().equals("anon") && !isSubscriber(obj)) return 0;
        else if (isSubscriber(obj) && !isOwner(obj) && !isStaff(obj) && !obj.getJSONObject("params").getBoolean("isCommunity")) return 1;
        else if (obj.getJSONObject("params").get("role").toString().equals("user")) return 2;
        else if (getRole(obj).equals("admin") && !isOwner(obj) && !isStaff(obj) && !obj.getJSONObject("params").getBoolean("isCommunity")) return 3;
        else if (getRole(obj).equals("admin") && isOwner(obj)) return 4;
        else if (getRole(obj).equals("admin") && !isOwner(obj) && !isStaff(obj) && obj.getJSONObject("params").getBoolean("isCommunity")) return 5;
        else if (getRole(obj).equals("admin") && !isOwner(obj) && isStaff(obj)) return 6;
        else return -1;



/*        if (getRole(obj).equals("anon")) return 0;
        else if (getRole(obj).equals("user")) return 1;
        else if (getRole(obj).equals("admin") && !isOwner(obj) && !isStaff(obj) && !obj.getJSONObject("params").getBoolean("isCommunity")) return 2;
        else if (getRole(obj).equals("admin") && isOwner(obj)) return 3;
        else if (getRole(obj).equals("admin") && !isOwner(obj) && !isStaff(obj) && obj.getJSONObject("params").getBoolean("isCommunity")) return 4;
        else if (getRole(obj).equals("admin") && !isOwner(obj) && isStaff(obj)) return 5;
        else return -1;*/
    }

/*    public static int getUserLevel(String name){

    }*/

    public static boolean isModerator(JSONObject obj){
        if (getRole(obj).equals("user") && !isOwner(obj)) return true;
        else return false;
    }

    public static boolean isAdmin(JSONObject obj){
        if (getRole(obj).equals("admin") && !isOwner(obj)) return true;
        else return false;
    }

    public static boolean isOwner(JSONObject obj){
        return obj.getJSONObject("params").getBoolean("isOwner");
    }

    public static boolean isStaff(JSONObject obj){
        return obj.getJSONObject("params").getBoolean("isStaff");
    }

    //Returns a list of all users currently in the channel.
    public static ArrayList userList(JSONObject obj){
        ArrayList<String> userList = new ArrayList<String>();

        obj = obj.getJSONObject("params").getJSONObject("data");

        int staffLength = obj.getJSONArray("isStaff").length();
        int communityLength = obj.getJSONArray("isCommunity").length();
        int adminLength = obj.getJSONArray("admin").length();
        int userLength = obj.getJSONArray("user").length();
        int subscriberLength = obj.getJSONArray("isSubscriber").length();
        int anonLength = obj.getJSONArray("anon").length();

        for (int i = 0; i < staffLength; i++) {
            if (!userList.contains(obj.getJSONArray("isStaff").get(i).toString().toLowerCase())) {
                userList.add(obj.getJSONArray("isStaff").get(i).toString().toLowerCase());
                userList.add("" + 6);
            }
        }

        for (int i = 0; i < communityLength; i++) {
            if (!userList.contains(obj.getJSONArray("isCommunity").get(i).toString().toLowerCase())) {
                userList.add(obj.getJSONArray("isCommunity").get(i).toString().toLowerCase());
                userList.add("" + 5);
            }
        }

        for (int i = 0; i < adminLength; i++) {
            if (!userList.contains(obj.getJSONArray("admin").get(i).toString().toLowerCase())) {
                if (obj.getJSONArray("admin").get(i).toString().toLowerCase().equals(Main.config.channel)){
                    userList.add(obj.getJSONArray("admin").get(i).toString().toLowerCase());
                    userList.add("" + 4);
                } else {
                    userList.add(obj.getJSONArray("admin").get(i).toString().toLowerCase());
                    userList.add("" + 3);
                }
            }
        }

        for (int i = 0; i < userLength; i++) {
            if (!userList.contains(obj.getJSONArray("user").get(i).toString().toLowerCase())) {
                userList.add(obj.getJSONArray("user").get(i).toString().toLowerCase());
                userList.add("" + 2);
            }
        }

        for (int i = 0; i < subscriberLength; i++) {
            if (!userList.contains(obj.getJSONArray("isSubscriber").get(i).toString().toLowerCase())) {
                userList.add(obj.getJSONArray("isSubscriber").get(i).toString().toLowerCase());
                userList.add("" + 1);
            }
        }

        for (int i = 0; i < anonLength; i++) {
            if (!userList.contains(obj.getJSONArray("anon").get(i).toString().toLowerCase())) {
                userList.add(obj.getJSONArray("anon").get(i).toString().toLowerCase());
                userList.add("" + 0);
            }
        }

        return userList;
    }

}
