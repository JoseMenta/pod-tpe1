package ar.edu.itba.pod.client.counterClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.client.Client;
import ar.edu.itba.pod.client.adminClient.AdminActions;

public class CounterClient extends Client {

    public CounterClient(String host, Action action) {
        super(host,action);
    }

    public static void main(String[] args) {
        String host = System.getProperty("serverAddress");
        String actionString = System.getProperty("action");
        Action action = CounterActions.getAction(actionString).getActionClass();

        try (Client client = new CounterClient(host,action)){
            client.run();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
