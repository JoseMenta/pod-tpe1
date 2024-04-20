package ar.edu.itba.pod.client.adminClient;

import ar.edu.itba.pod.client.Action;

import java.util.List;

public enum AdminActions {
    ADDSECTOR("addSector", new AddSectorAction()),
    ADDCOUNTER("addCounters", new AddCounterAction()),
    MANIFEST("manifest", new ManifestAction());

    private final String actionName;

    private final Action action;

    AdminActions(String actionName, Action action) {
        this.actionName = actionName;
        this.action = action;
    }

    public static AdminActions getAction(String actionName) {
        for (AdminActions action : values()) {
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
