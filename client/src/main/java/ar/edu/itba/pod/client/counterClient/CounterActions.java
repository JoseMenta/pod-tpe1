package ar.edu.itba.pod.client.counterClient;

import ar.edu.itba.pod.client.Action;

import java.util.function.Supplier;

public enum CounterActions {
    LIST_SECTOR("listSectors",ListSectorsAction::new),
    LIST_COUNTERS("listCounters",ListCountersAction::new),
    ASSIGN_COUNTERS("assignCounters",AssignCounterAction::new),
    FREE_COUNTERS("freeCounters", FreeCounterAction::new),
    CHECKIN_COUNTERS("checkinCounters",CheckInAction::new),
    LIST_PENDING_ASSIGNMENTS("listPendingAssignments",ListPendingAssignmentAction::new);
    private final String actionName;

    private final Supplier<Action> action;

    CounterActions(String actionName, Supplier<Action> action) {
        this.actionName = actionName;
        this.action = action;
    }
    public static CounterActions getAction(String actionName) {
        for (CounterActions action : values()) {
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
