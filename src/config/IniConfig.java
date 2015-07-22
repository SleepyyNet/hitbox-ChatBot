package config;

import main.Main;
import org.ini4j.Ini;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class IniConfig {

    public void init(){
        if (getProperty("chat", "background-color") == null) setProperty("chat", "background-color", "white");
        if (getProperty("chat", "font-color") == null) setProperty("chat", "font-color", "black");
        if (getProperty("chat", "font-size") == null) setProperty("chat", "font-size", "100%");
        if (getProperty("chat", "font-family") == null) setProperty("chat", "font-family", "Arial,'Helvetica Neue',Helvetica,sans-serif");
        if (getProperty("chat", "timestamp-enabled") == null) setProperty("chat", "timestamp-enabled", "true");
    }

    public static void setProperty(String header, String title, String value){
        try {
            File file = new File(Main.rootPath + "/resources/config.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            Ini ini = new Ini(file);
            ini.put(header, title, value);
            ini.store();
        }catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

    public static String getProperty(String header, String title){
        try {
            File file = new File(Main.rootPath + "/resources/config.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            Ini ini = new Ini(file);
            return ini.get(header, title);
        }catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
        return null;
    }
}
