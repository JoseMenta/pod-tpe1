package ar.edu.itba.pod.client.counterClient;

import ar.edu.itba.pod.client.Action;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public enum CounterActions {
    LISTSECTOR("listSectors",ListSectorsAction::new),
    LISTCOUNTERS("listCounters",ListCountersAction::new),
    ASSIGNCOUNTERS("assignCounters",AssignCounterAction::new),
    FREECOUNTERS("freeCounters", FreeCounterAction::new),
    CHECKINCOUNTERS("checkinCounters",CheckInAction::new),
    LISTPENDINGASSIGNMENTS("listPendingAssignments",ListPendingAssignmentAction::new);
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
