package ar.edu.itba.pod.client.queryClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.client.adminClient.AddSectorAction;
import ar.edu.itba.pod.client.adminClient.AdminActions;

import java.util.List;

public enum QueryActions {
    COUNTERS("counters", new CountersAction(List.of(CountersAction.OUTPATH, CountersAction.SECTOR))),
    CHECKINS("checkins", new CheckinsAction(List.of(CheckinsAction.OUTPATH, CheckinsAction.SECTOR, CheckinsAction.AIRLINE)));

    private final String actionName;
    private final Action action;

    QueryActions(String actionName, Action action) {
        this.actionName = actionName;
        this.action = action;
    }

    public static QueryActions getAction(String actionName) {
        for (QueryActions action : values()) {
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
