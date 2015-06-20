package listen;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Initiator {
    List<MessageListener> listeners = new ArrayList<MessageListener>();

    public void addListener(MessageListener add){
        listeners.add(add);
    }

    public void sendMessage(JSONObject message){
        for (MessageListener msg: listeners){
            msg.messageReceived(message);
        }
    }
}