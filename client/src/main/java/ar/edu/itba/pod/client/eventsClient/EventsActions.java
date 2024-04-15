package ar.edu.itba.pod.client.eventsClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.client.adminClient.AdminActions;

import java.util.List;

public enum EventsActions {
    RESGISTER("register", new RegisterAction(List.of(RegisterAction.AIRLINE))),
    UNREGISTER("unregister", new UnregisterAction(List.of(UnregisterAction.AIRLINE)));

    private final String actionName;

    private final Action action;

    EventsActions(String actionName, Action action) {
        this.actionName = actionName;
        this.action = action;
    }

    public static EventsActions getAction(String actionName) {
        for (EventsActions action : values()) {
            if (actionName.equals(action.actionName)) {
                return action;
            }
        }
        throw new IllegalArgumentException();
    }

    public Action getActionClass() {
        return action;
    }
}
