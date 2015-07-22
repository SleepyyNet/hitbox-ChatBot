package controller.tabs;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import main.Main;
import main.Parser;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import scripts.DownloadScript;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ScriptsTab {

    @FXML
    public TableView table;

    @FXML
    public TableColumn nameCol, urlCol, downloadedCol;

    @FXML
    public ProgressBar progressBar;

    @FXML
    public ContextMenu cm;

    private final ObservableList<Command> data = FXCollections.observableArrayList();
    JSONObject obj = new JSONObject(Parser.readUrl("http://gamingtom.com/h/chatbot/scripts/database.json"));

    public void init(){
        try {

            nameCol.setCellValueFactory(new PropertyValueFactory<Command, String>("scriptName"));
            urlCol.setCellValueFactory(new PropertyValueFactory<Command, String>("scriptUrl"));
            downloadedCol.setCellValueFactory(new PropertyValueFactory<Command, String>("scriptDownloaded"));

            table.setItems(data);

            data.add(new Command("test1", "test2", "test3"));

            fillTable();
        }catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

    public void fillTable(){
        if (table.getItems() != null) {
            data.removeAll(table.getItems());
        }

        File file = new File(Main.rootPath + "/resources/scripts/");

        ArrayList files = new ArrayList();
        if (file.listFiles().length > 0) {
            for (File i : file.listFiles()) {
                files.add(i.getName());
            }
        }

        Iterator<?> keys = obj.keys();

        while( keys.hasNext() ) {
            String key = (String)keys.next();
            Boolean downloaded = files.contains(key);

            data.add(new Command(key, obj.getJSONObject(key).getString("url"), downloaded.toString()));
        }
    }

    public void test (Event event){
        int id = table.getSelectionModel().getSelectedIndex();
        cm.getItems().get(0).setText(obj.getJSONObject(data.get(id).getScriptName()).getString("desc"));
    }

    public void downloadScript(ActionEvent event){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int id = table.getSelectionModel().getSelectedIndex();
                DownloadScript.download(data.get(id).getScriptUrl(), data.get(id).getScriptName() + ".zip");
            }
        }).start();
    }

    public void removeScript(ActionEvent event){
        try {
            int id = table.getSelectionModel().getSelectedIndex();
            FileUtils.deleteDirectory(new File(Main.rootPath + "/resources/scripts/" + data.get(id).getScriptName()));
            fillTable();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static class Command {
        private final SimpleStringProperty scriptName;
        private final SimpleStringProperty scriptUrl;
        private final SimpleStringProperty scriptDownloaded;

        private Command(String scriptName, String scriptUrl, String scriptDownloaded){
            this.scriptName = new SimpleStringProperty(scriptName);
            this.scriptUrl = new SimpleStringProperty(scriptUrl);
            this.scriptDownloaded = new SimpleStringProperty(scriptDownloaded);
        }

        public String getScriptName(){
            return scriptName.get();
        }

        public String getScriptUrl(){
            return scriptUrl.get();
        }

        public String getScriptDownloaded(){
            return scriptDownloaded.get();
        }
    }

}
