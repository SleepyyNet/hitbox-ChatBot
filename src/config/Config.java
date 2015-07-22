package config;

import main.Main;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Config {

    public String version = "0.0.7";

    public boolean consoleShown = false;
    public boolean settingsShown = false;

    public String username;
    public String password;
    public String channel;
    public boolean connected = false;

    JSONObject obj;

    public void createFile(String fileName){
        try {
            File file = new File(Main.rootPath + "/resources/" + fileName);
            if (!file.exists()){
                file.createNewFile();
            }

            obj = new JSONObject(readConfig());
        }catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

    public void setProperty(String header, String key, String value){
        if (obj.has(header)) {
            obj.getJSONObject(header).put(key, value);
            saveConfig();
        }
        else {
            obj.put(header, new JSONObject().put(key, value));
            saveConfig();
        }
    }

    public void setProperty(String header, String key, JSONObject value){
        if (obj.has(header)) {
            obj.getJSONObject(header).put(key, value);
            saveConfig();
        }
        else {
            obj.put(header, new JSONObject().put(key, value));
            saveConfig();
        }
    }

    public void removeProperty(String header, String key){
        if (obj.has(header)) {
            obj.getJSONObject(header).remove(key);
            saveConfig();
        }
        else {
            obj.put(header, new JSONObject().remove(key));
            saveConfig();
        }
    }

    public String getProperty(String header, String key){
        if (obj.has(header) && obj.getJSONObject(header).has(key)){
            return obj.getJSONObject(header).get(key).toString();
        }
        return null;
    }

    public void saveConfig(){
        try
        {
            PrintWriter writer = new PrintWriter(Main.rootPath + "/resources/" +  Main.config.channel + ".txt");
            writer.write(obj.toString());
            writer.close();
        }catch(Exception e)
        {
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

    public String readConfig(){
        try {
            String text = new String(Files.readAllBytes(Paths.get(Main.rootPath + "/resources/" + Main.config.channel + ".txt")), StandardCharsets.UTF_8);
            if (text.length() > 0) return text;
            return "{}";
        }catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
        return "{}";
    }




}
