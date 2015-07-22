package controller.settings;

import javafx.scene.control.TextField;
import main.Main;

public class CommandsController {

    public TextField cooldown;

    public void init(){
        if (Main.iniConfig.getProperty("commands", "cooldown") != null) cooldown.setText(Main.iniConfig.getProperty("commands", "cooldown"));
        else {
            Main.iniConfig.setProperty("commands", "cooldown", "0");
            cooldown.setText(Main.iniConfig.getProperty("commands", "cooldown"));
        }
    }

    public void saveSettings(){
        if (!cooldown.getText().isEmpty()) Main.iniConfig.setProperty("commands", "cooldown", cooldown.getText());
    }

}
