package ar.edu.itba.pod.client.adminClient;

import ar.edu.itba.pod.client.Action;

import java.util.List;

public enum AdminActions {
    ADDSECTOR("addSector", new AddSectorAction(List.of(AddSectorAction.SECTOR))),
    ADDCOUNTER("addCounters", new AddCounterAction(List.of(AddCounterAction.SECTOR, AddCounterAction.COUNTERS))),
    MANIFEST("manifest", new ManifestAction(List.of(ManifestAction.IN_PATH)));

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
