package ar.edu.itba.pod.client.passengerClient;

import ar.edu.itba.pod.client.Action;

import java.util.List;

public enum  PassengersActions {

    FETCHCOUNTER("fetchCounter", new FetchCounterAction(List.of(FetchCounterAction.BOOKING))),

    PASSENGERCHECKIN("passengerCheckIn", new PassengerCheckInAction(List.of(PassengerCheckInAction.BOOKING, PassengerCheckInAction.SECTION, PassengerCheckInAction.COUNTER))),

    PASSENGERSTATUS("passengerStatus", new PassengerStatusAction(List.of(PassengerStatusAction.BOOKING)));


    private final String actionName;

    private final Action action;

    PassengersActions(String actionName, Action action) {
        this.actionName = actionName;
        this.action = action;
    }

    public static PassengersActions getAction(String actionName) {
        for (PassengersActions action : values()) {
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
