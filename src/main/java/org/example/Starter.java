package org.example;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

import java.util.UUID;

public class Starter {
    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);

        SayHelloWorkflow workflow = client.newWorkflowStub(
                SayHelloWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue("virtual-thread")
                        .setWorkflowId("hey-"+ UUID.randomUUID())
                        .build()
        );

        String result = workflow.sayHello("GET OUT OF MY HEAD: ", "Temporal");
        System.out.println("Workflow result: " + result);
    }
}