package ar.edu.itba.pod.client.eventsClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.client.adminClient.AdminActions;

import java.util.List;
import java.util.function.Supplier;

public enum EventsActions {
    RESGISTER("register", RegisterAction::new),
    UNREGISTER("unregister", UnregisterAction::new);

    private final String actionName;

    private final Supplier<Action> action;

    EventsActions(String actionName, Supplier<Action> action) {
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
        return action.get();
    }
}
