package listen;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Initiator {
    public List<MessageListener> messageListeners = new ArrayList<MessageListener>();

    public void addMessageListener(MessageListener listener){
        messageListeners.add(listener);
    }

    public void removeMessageListener(MessageListener listener){
        messageListeners.remove(listener);
    }

    public void sendMessage(JSONObject message){
        for (MessageListener listener: messageListeners){
            listener.messageReceived(message);
        }
    }


    public List<EventListener> eventListeners = new ArrayList<>();

    public void addEventListener(EventListener listener){
        eventListeners.add(listener);
    }

    public void removeEventListener(EventListener listener){
        eventListeners.remove(listener);
    }

    public void event_onInfoMsg(JSONObject message){
        for (EventListener listener : eventListeners){
            listener.newEvent("onInfoMsg", message);
        }
    }

    public void event_onStreamUpdate(JSONObject message){
        for (EventListener listener : eventListeners){
            listener.newEvent("onStreamUpdate", message);
        }
    }

    public void event_onHostUpdate(JSONObject message){
        for (EventListener listener : eventListeners){
            listener.newEvent("onHostUpdate", message);
        }
    }

    public void event_onMotdUpdate(JSONObject message){
        for (EventListener listener : eventListeners){
            listener.newEvent("onMotdUpdate", message);
        }
    }

    public void event_onText(JSONObject message){
        for (EventListener listener : eventListeners){
            listener.newEvent("onText", message);
        }
    }

    public void event_onWhisper(JSONObject message){
        for (EventListener listener : eventListeners){
            listener.newEvent("onWhisper", message);
        }
    }

    public void event_onSpecialChat(JSONObject message){
        for (EventListener listener : eventListeners){
            listener.newEvent("onSpecialChat", message);
        }
    }

    public void event_onMediaLog(JSONObject message){
        for (EventListener listener : eventListeners){
            listener.newEvent("onMediaLog", message);
        }
    }

    public void event_onJoin(JSONArray names){
        for (EventListener listener : eventListeners){
            listener.newEvent("onJoin", names);
        }
    }

    public void event_onPart(JSONArray names){
        for (EventListener listener : eventListeners){
            listener.newEvent("onPart", names);
        }
    }

    public void event_onUserList(JSONObject message){
        for (EventListener listener : eventListeners){
            listener.newEvent("onUserList", message);
        }
    }

    public void event_onUserInfo(JSONObject message){
        for (EventListener listener : eventListeners){
            listener.newEvent("onUserInfo", message);
        }
    }

    public void event_onNewSubscriber(JSONObject message){
        for (EventListener listener : eventListeners){
            listener.newEvent("onNewSubscriber", message);
        }
    }

    public void event_onGiveawayStared(JSONObject message){
        for (EventListener listener : eventListeners){
            listener.newEvent("onGiveawayStared", message);
        }
    }

    public void event_onGiveawayEnded(JSONObject message){
        for (EventListener listener : eventListeners){
            listener.newEvent("onGiveawayEnded", message);
        }
    }

    public void event_onPollStared(JSONObject message){
        for (EventListener listener : eventListeners){
            listener.newEvent("onPollStared", message);
        }
    }

    public void event_onPollEnded(JSONObject message){
        for (EventListener listener : eventListeners){
            listener.newEvent("onPollEnded", message);
        }
    }
}