package ar.edu.itba.pod.client.passengerClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.grpc.commons.Error;

import java.util.List;
import java.util.function.Supplier;

public enum  PassengersActions {

    FETCHCOUNTER("fetchCounter", FetchCounterAction::new),

    PASSENGERCHECKIN("passengerCheckIn", PassengerCheckInAction::new),

    PASSENGERSTATUS("passengerStatus",PassengerStatusAction::new);


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
