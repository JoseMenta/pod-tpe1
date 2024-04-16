package ar.edu.itba.pod.client.counterClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.client.adminClient.AdminActions;

import java.util.Collections;
import java.util.List;

public enum CounterActions {
    LISTSECTOR("listSectors",new ListSectorsAction(Collections.emptyList())),
    LISTCOUNTERS("listCounters",new ListCountersAction(List.of(ListCountersAction.SECTOR,ListCountersAction.COUNTERFROM,ListCountersAction.COUNTERTO))),
    ASSIGNCOUNTERS("assignCounters",new AssignCounterAction(List.of(AssignCounterAction.SECTOR,AssignCounterAction.COUNTERCOUNT,AssignCounterAction.AIRLINE,AssignCounterAction.FLIGHTS))),
    FREECOUNTERS("freeCounters", new FreeCounterAction(List.of(FreeCounterAction.SECTOR,FreeCounterAction.COUNTERFROM,FreeCounterAction.AIRLINE))),
    CHECKINCOUNTERS("checkinCounters",new CheckInAction(List.of(CheckInAction.SECTOR,CheckInAction.COUNTERFROM,CheckInAction.AIRLINE))),
    LISTPENDINGASSIGNMENTS("listPendingAssignments",new ListPendingAssignmentAction(List.of(ListPendingAssignmentAction.SECTOR)));
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
