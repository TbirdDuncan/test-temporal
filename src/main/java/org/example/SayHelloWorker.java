package org.example;

import io.temporal.client.WorkflowClient;
import io.temporal.common.VersioningBehavior;
import io.temporal.common.WorkerDeploymentVersion;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerDeploymentOptions;
import io.temporal.worker.WorkerFactory;
import io.temporal.worker.WorkerOptions;

public class SayHelloWorker {

    public static void main(String[] args) {

        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = ClientProvider.getClient();
        WorkerFactory factory = WorkerFactory.newInstance(client);

        Worker worker = factory.newWorker("virtual-thread",  WorkerOptions.newBuilder()
                .setDeploymentOptions(
                        WorkerDeploymentOptions.newBuilder()
                                .setVersion(new WorkerDeploymentVersion("llm_srv", "1.0"))
                                .setUseVersioning(true)
                                .setDefaultVersioningBehavior(VersioningBehavior.AUTO_UPGRADE)
                                .build())
                .build());
        worker.registerWorkflowImplementationTypes(SayHelloWorkflowImpl.class);
        worker.registerActivitiesImplementations(new GreetActivitiesImpl());

        System.out.println("Starting Virtual thread worker for task queue 'my-task-queue'...");

        factory.start();

    }
}