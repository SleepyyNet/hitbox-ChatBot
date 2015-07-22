package listen;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public interface EventListener {

    public void newEvent(String event, JSONObject message);

    public void newEvent(String event, JSONArray message);

    /*public void onInfoMsg(JSONObject message);

    public void onStreamUpdate(JSONObject message);

    public void onHostUpdate(JSONObject message);

    public void onMotdUpdate(JSONObject message);

    public void onText(JSONObject message);

    public void onWhisper(JSONObject message);

    public void onSpecialChat(JSONObject message);

    public void onMediaLog(JSONObject message);

    public void onJoin(JSONArray names);

    public void onPart(JSONArray names);

    public void onUserList(JSONObject message);

    public void onUserInfo(JSONObject message);

    public void onNewSubscriber(JSONObject message);

    public void onGiveawayStared(JSONObject message);

    public void onGiveawayEnded(JSONObject message);

    public void onPollStared(JSONObject message);

    public void onPollEnded(JSONObject message);

    public void onPointsChanged();*/
}
