package scripts;

import javafx.application.Platform;
import listen.EventListener;
import listen.MessageListener;
import main.Main;
import main.Parser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

import java.io.File;
import java.util.*;

public class Script implements MessageListener, EventListener{

    public File file;

    Context context;
    ScriptableObject scope;
    static Thread thread = Thread.currentThread();

    public Script(File file){
        this.file = file;
        load();
        Main.initiator.addMessageListener(this);
        Main.initiator.addEventListener(this);
    }

    public void messageReceived(final JSONObject object){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (!Parser.isBuffer(object)) {
                    Object args[] = {object};
                    runFunction("newMessage", args);
                }
            }
        });
    }

    //### Events



    private void runFunction(String functionName, Object args[]){
        Object fObj = scope.get(functionName, scope);
        if (!(fObj instanceof Function)) {
            //System.out.println("f is undefined or not a function.");
        }else {
            ((Function) fObj).call(context, scope, scope, args);
        }
    }

    NativeObject global = new NativeObject();

    public void load(){
        try{
            context = Context.enter();
            scope = context.initStandardObjects(global, false);
            scope.defineProperty("$", Main.scriptManager.g, 0);
            scope.defineProperty("$script", this, 0);
            scope.defineProperty("$api", ScriptApi.instance(), 0);

            context.evaluateString(scope, Parser.readFile(file), file.getName(), 1, null);
        }catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

    public void stopScript(){
        try {
            Main.initiator.removeMessageListener(this);
            Main.initiator.removeEventListener(this);
            for (Map.Entry<Integer, Timer> entry : timeouts.entrySet())
            {
                clearTimeout(entry.getKey());
            }
            for (Map.Entry<Integer, Timer> entry : intervals.entrySet())
            {
                clearInterval(entry.getKey());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void reloadScript(){
        stopScript();
        load();
    }

    private HashMap<Integer, Timer> timeouts = new HashMap<>();
    private HashMap<Integer, Timer> intervals = new HashMap<>();

    public int setTimeout(final Function string, int delay){
        System.out.println(string);
        Timer timer = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                string.call(context, scope, scope, null);
            }
        };
        timer.schedule(tt, delay);
        int id = timeouts.size() + 1;
        timeouts.put(id, timer);
        return id;
    }

    public void clearTimeout(int id){
        Timer timer = timeouts.get(id);
        timer.cancel();
        timer.purge();
    }

    public int setInterval(final Function string, int delay){
        System.out.println(string);
        Timer timer = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                string.call(context, scope, scope, null);
            }
        };
        timer.scheduleAtFixedRate(tt, delay, delay);
        int id = intervals.size() + 1;
        intervals.put(id, timer);
        return id;
    }

    public void clearInterval(int id){
        Timer timer = intervals.get(id);
        timer.cancel();
        timer.purge();
    }

    public void newEvent(String event, JSONObject message) {
        Object args[] = {message};
        if (event.equals("onInfoMsg")) runFunction("onInfoMsg", args);
        else if (event.equals("onStreamUpdate")) runFunction("onStreamUpdate", args);
        else if (event.equals("onHostUpdate")) runFunction("onHostUpdate", args);
        else if (event.equals("onMotdUpdate")) runFunction("onMotdUpdate", args);
        else if (event.equals("onText")) runFunction("onText", args);
        else if (event.equals("onWhisper")) runFunction("onWhisper", args);
        else if (event.equals("onSpecialChat")) runFunction("onSpecialChat", args);
        else if (event.equals("onMediaLog")) runFunction("onMediaLog", args);
        else if (event.equals("onUserList")) runFunction("onUserList", args);
        else if (event.equals("onUserInfo")) runFunction("onUserInfo", args);
        else if (event.equals("onNewSubscriber")) runFunction("onNewSubscriber", args);
        else if (event.equals("onGiveawayStared")) runFunction("onGiveawayStared", args);
        else if (event.equals("onGiveawayEnded")) runFunction("onGiveawayEnded", args);
        else if (event.equals("onPollStared")) runFunction("onPollStared", args);
        else if (event.equals("onPollEnded")) runFunction("onPollEnded", args);
    }

    public void newEvent(String event, JSONArray message) {
        Object args[] = {message};
        if (event.equals("onJoin")) runFunction("onJoin", args);
        else if (event.equals("onPart")) runFunction("onPart", args);
    }
}
