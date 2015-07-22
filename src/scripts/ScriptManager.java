package scripts;

import main.Main;
import org.mozilla.javascript.NativeObject;

import java.io.File;
import java.util.HashMap;

public class ScriptManager {

    private HashMap<String, Script> scripts = new HashMap<>();
    public NativeObject g = new NativeObject();

    public void loadAllScripts(){
        File dir = new File(Main.rootPath + "/resources/scripts/");
        dir.mkdirs();
        File dirs[] = dir.listFiles();
        if (dirs.length > 0){
            for (File file1 : dirs){
                if (file1.isDirectory()) {
                    File files[] = file1.listFiles();
                    if (files.length > 0) {
                        for (File file2 : files) {
                            if (file2.getName().endsWith(".js")) {
                                loadScript(file2);
                                Main.consoleController.out("Loaded script: " + file2.getName());
                            }
                        }
                    }
                }
            }
        }
    }

    public void loadScript(final File file) {
        Script script = new Script(file);
        scripts.put(file.getPath().toString(), script);
    }

    public Script getScript(File file){
        if (!scripts.containsKey(file.getPath().toString())) return null;
        return scripts.get(file.getPath().toString());
    }

}
