package ar.edu.itba.pod.client.adminClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.client.Client;

public class AdminClient extends Client {

    public AdminClient(String host,Action action) {
        super(host,action);
    }

    public static void main(String[] args) {
        String host = System.getProperty("serverAddress");
        String actionString = System.getProperty("action");
        Action action = AdminActions.getAction(actionString).getActionClass();

        try (Client client = new AdminClient(host,action)){
            client.run();
            client.close();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
