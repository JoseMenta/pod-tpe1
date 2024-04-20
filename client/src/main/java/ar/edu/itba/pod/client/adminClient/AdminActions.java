package ar.edu.itba.pod.client.adminClient;

import ar.edu.itba.pod.client.Action;

import java.util.function.Supplier;

public enum AdminActions {
    ADD_SECTOR("addSector", AddSectorAction::new),
    ADD_COUNTER("addCounters", AddCounterAction::new),
    MANIFEST("manifest", ManifestAction::new);

    private final String actionName;

    private final Supplier<Action> action;

    AdminActions(String actionName, Supplier<Action> action) {
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
        return action.get();
    }
}
