package main;

import commands.CommandHandler;
import config.Config;
import controller.ConsoleController;
import controller.mainController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import listen.Initiator;

import java.io.File;

public class Main extends Application{

    public static mainController mainController;
    public static ConsoleController consoleController;

    public static Config config = new Config();
    public static Initiator initiator = new Initiator();
    public static CommandHandler commandHandler = new CommandHandler();

    public static File rootPath;

    public static void main(String[] args) {
        //initiator = new Initiator();
        initialize();







        //test();
        launch(args);
    }

    public static Stage mainStage = new Stage();

    //Alert variableInformation = new Alert(Alert.AlertType.INFORMATION);

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent mainRoot = mainLoader.load();

        mainController = (mainController)mainLoader.getController();
        mainController.init();

        Scene mainScene = new Scene(mainRoot, 1280, 720);

        mainStage.setMinWidth(600);
        mainStage.setMinHeight(400);
        mainStage.setScene(mainScene);
        mainStage.show();

        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });

        startConsole();

/*        variableInformation.setTitle("Variable Information");
        variableInformation.setHeaderText("Information about available variables.");
        variableInformation.setContentText("" +
                "Here is a list of the available variables:" +
                "\n     %p: Gives current points of the person who executed the command." +
                "\n     %pN: Gives the name of the points." +
                "\n     %pA: Gives the amount of points given after each interval." +
                "\n     %pT: Gives the time interval of the points.");
        variableInformation.showAndWait();*/

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
        consoleStage.show();
        consoleStage.hide();

        consoleStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                consoleStage.hide();
            }
        });
    }

    public static void initialize(){
        try {
            rootPath = new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent());
            if (!new File(rootPath + "/resources/").exists()) new File(rootPath + "/resources/").mkdirs();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
