package ar.edu.itba.pod.client.passengerClient;

import ar.edu.itba.pod.client.Action;

import java.util.function.Supplier;

public enum  PassengersActions {

    FETCH_COUNTER("fetchCounter", FetchCounterAction::new),

    PASSENGER_CHECKIN("passengerCheckIn", PassengerCheckInAction::new),

    PASSENGER_STATUS("passengerStatus",PassengerStatusAction::new);


    private final String actionName;

    private final Supplier<Action> action;

    PassengersActions(String actionName, Supplier<Action> action) {
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
        return action.get();
    }
}
