package org.example;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class ClientProvider {

    private static final WorkflowServiceStubs service =
            WorkflowServiceStubs.newLocalServiceStubs();

    private static final WorkflowClient client =
            WorkflowClient.newInstance(service);

    private ClientProvider() {
        // prevent instantiation
    }

    public static WorkflowClient getClient() {
        return client;
    }
}
