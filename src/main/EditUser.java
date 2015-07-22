package main;

import controller.EditUserController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EditUser {

    public EditUser(String user){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/edituser.fxml"));
            Parent root = loader.load();

            EditUserController controller = loader.getController();
            controller.init(user);

            Scene scene = new Scene(root, 505, 95);
            Stage stage = new Stage();
            stage.setTitle(user);
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

}
