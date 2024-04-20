package ar.edu.itba.pod.client.queryClient;

import ar.edu.itba.pod.client.Action;
import ar.edu.itba.pod.client.Client;
import ar.edu.itba.pod.client.eventsClient.EventsClient;

public class QueryClient extends Client {

        public QueryClient(String host, Action action) {
            super(host, action);
        }

        public static void main(String[] args) {
            String host = System.getProperty("serverAddress");
            String actionString = System.getProperty("action");
            Action action = QueryActions.getAction(actionString).getActionClass();

            try (Client client = new EventsClient(host, action)) {
                client.run();
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
}
