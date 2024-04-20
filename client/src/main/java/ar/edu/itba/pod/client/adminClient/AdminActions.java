package ar.edu.itba.pod.client.adminClient;

import ar.edu.itba.pod.client.Action;

import java.util.List;
import java.util.function.Supplier;

public enum AdminActions {
//    -DserverAddress=localhost:50051 -Daction=addSector -Dsector=C
    ADDSECTOR("addSector", AddSectorAction::new),
    ADDCOUNTER("addCounters", AddCounterAction::new),
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
