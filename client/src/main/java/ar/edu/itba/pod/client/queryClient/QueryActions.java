package ar.edu.itba.pod.client.queryClient;

import ar.edu.itba.pod.client.Action;

import java.util.function.Supplier;

public enum QueryActions {
    COUNTERS("queryCounters", CountersAction::new),
    CHECKINS("checkins", CheckinsAction::new);

    private final String actionName;
    private final Supplier<Action> action;

    QueryActions(String actionName, Supplier<Action> action) {
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
        return action.get();
    }
}
