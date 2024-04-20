package ar.edu.itba.pod.client.counterClient;

import ar.edu.itba.pod.client.Action;

import java.util.Collections;
import java.util.List;

public enum CounterActions {
    LISTSECTOR("listSectors",new ListSectorsAction()),
    LISTCOUNTERS("listCounters",new ListCountersAction()),
    ASSIGNCOUNTERS("assignCounters",new AssignCounterAction()),
    FREECOUNTERS("freeCounters", new FreeCounterAction()),
    CHECKINCOUNTERS("checkinCounters",new CheckInAction()),
    LISTPENDINGASSIGNMENTS("listPendingAssignments",new ListPendingAssignmentAction());
    private final String actionName;

    private final Action action;

    CounterActions(String actionName, Action action) {
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
        return action;
    }
}
