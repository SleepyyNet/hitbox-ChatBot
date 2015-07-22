package main;

import config.Config;
import config.IniConfig;
import controller.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import listen.Initiator;
import scripts.ScriptManager;

import java.io.File;

public class Main extends Application{

    public static MainController mainController;
    public static ConsoleController consoleController;
    public static VersionController versionController;
    public static SettingsController settingsController;

    public static IniConfig iniConfig = new IniConfig();
    public static Config config = new Config();
    public static Initiator initiator = new Initiator();
    public static MessageHandler messageHandler = new MessageHandler();
    public static ScriptManager scriptManager = new ScriptManager();

    public static File rootPath;

    public static void main(String[] args) {
        initialize();
        launch(args);
    }

    public static Stage mainStage = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent mainRoot = mainLoader.load();

        mainController = (MainController)mainLoader.getController();
        mainController.init();

        Double dWidth;
        String sWidth = iniConfig.getProperty("general", "width");
        if (sWidth == null) dWidth = 1280.00;
        else dWidth = Double.parseDouble(sWidth);

        Double dHeight;
        String sHeight = iniConfig.getProperty("general", "height");
        if (sHeight == null) dHeight = 720.00;
        else dHeight = Double.parseDouble(sHeight);

        String sXPosition = iniConfig.getProperty("general", "xPosition");
        if (sXPosition != null) mainStage.setX(Double.parseDouble(sXPosition));

        String sYPosition = iniConfig.getProperty("general", "yPosition");
        if (sYPosition != null) mainStage.setY(Double.parseDouble(sYPosition));

        final Scene mainScene = new Scene(mainRoot, dWidth, dHeight);

        mainStage.setMinWidth(600);
        mainStage.setMinHeight(400);
        mainStage.setScene(mainScene);
        mainStage.setTitle("tBot v" + config.version);
        mainStage.show();

        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                iniConfig.setProperty("general", "width", String.valueOf(mainScene.getWidth()));
                iniConfig.setProperty("general", "height", String.valueOf(mainScene.getHeight()));
                iniConfig.setProperty("general", "xPosition", String.valueOf(mainStage.getX()));
                iniConfig.setProperty("general", "yPosition", String.valueOf(mainStage.getY()));
                if (config.connected) {
                    mainController.client.partChannel();
                    mainController.client.close();
                }
                System.exit(0);
            }
        });

        startConsole();
        if (checkVersion()) showVersionInfo();
        showStats();

        //new EditUser("GamingTom");
    }

    public static Stage consoleStage;
    private void startConsole() throws Exception{
        consoleStage = new Stage();
        FXMLLoader consoleLoader = new FXMLLoader(getClass().getResource("/fxml/console.fxml"));
        Parent consoleRoot = consoleLoader.load();
        consoleController = consoleLoader.getController();
        consoleController.init();

        Scene consoleScene = new Scene(consoleRoot, 600, 400);

        consoleStage.setScene(consoleScene);
        consoleStage.setTitle("Console");
        consoleStage.show();
        consoleStage.hide();

        consoleStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                config.consoleShown = false;
                consoleStage.hide();
            }
        });
    }

    private Stage versionStage;
    private void showVersionInfo() throws Exception{
        versionStage = new Stage();
        FXMLLoader versionLoader = new FXMLLoader(getClass().getResource("/fxml/version.fxml"));
        Parent versionRoot = versionLoader.load();
        versionController = versionLoader.getController();
        versionController.init();

        Scene versionScene = new Scene(versionRoot, 350, 128);

        versionStage.setTitle("Version Info");
        versionStage.setResizable(false);
        versionStage.setMinHeight(128);
        versionStage.setMinWidth(350);

        versionStage.setScene(versionScene);
        versionStage.show();
    }

    public static Stage settingsStage;
    private void showStats() throws Exception{
        settingsStage = new Stage();
        FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("/fxml/settings.fxml"));
        Parent settingsRoot = settingsLoader.load();
        settingsController = settingsLoader.getController();
        settingsController.init();

        Scene settingsScene = new Scene(settingsRoot, 600, 400);

        settingsStage.setTitle("Settings");
        settingsStage.setAlwaysOnTop(true);
        settingsStage.setResizable(false);

        settingsStage.setScene(settingsScene);
        settingsStage.show();
        settingsStage.hide();

        settingsStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                config.settingsShown = false;
                settingsStage.hide();
            }
        });
    }

    public static void initialize(){
        try {
            rootPath = new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent());
            if (!new File(rootPath + "/resources/").exists()) new File(rootPath + "/resources/").mkdirs();
            if (!new File(rootPath + "/resources/scripts/").exists()) new File(rootPath + "/resources/scripts/").mkdirs();

            iniConfig.init();
        }catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

    public static String latestVersion = Parser.readUrl("https://dl.dropboxusercontent.com/u/23313911/Development/Java/hitboxBot.files/latestversion.txt");
    public static String currentVersion = config.version;
    private static boolean checkVersion(){
        String latestVerArr[] = latestVersion.split("\\.");
        String currentVerArr[] = currentVersion.split("\\.");
        if (Integer.parseInt(latestVerArr[2]) > Integer.parseInt(currentVerArr[2])) return true;
        if (Integer.parseInt(latestVerArr[1]) > Integer.parseInt(currentVerArr[1])) return true;
        if (Integer.parseInt(latestVerArr[0]) > Integer.parseInt(currentVerArr[0])) return true;
        return false;
    }

}
