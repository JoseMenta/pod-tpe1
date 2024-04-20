package ar.edu.itba.pod.client.passengerClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.client.Client;


public class PassengerClient extends Client {
    public PassengerClient(String host, Action action) {
        super(host, action);
    }
    public static void main(String[] args){
        String host = System.getProperty("serverAddress");
        String actionString = System.getProperty("action");
        Action action = PassengersActions.getAction(actionString).getActionClass();

        try (Client client = new PassengerClient(host, action)){
            client.run();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
