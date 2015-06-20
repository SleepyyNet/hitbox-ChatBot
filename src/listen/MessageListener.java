package listen;

import org.json.JSONObject;

public interface MessageListener {
    public void messageReceived(JSONObject message);
}
