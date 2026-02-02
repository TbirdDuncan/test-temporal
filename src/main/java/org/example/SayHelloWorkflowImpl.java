package org.example;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class SayHelloWorkflowImpl implements SayHelloWorkflow {

    private final GreetActivities activities = Workflow.newActivityStub(
            GreetActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(10))
                    .setHeartbeatTimeout(Duration.ofSeconds(2))
                    .setRetryOptions(
                            RetryOptions.newBuilder()
                                    .setMaximumAttempts(3)
                                    .build()
                    )
                    .build()
    );

    @Override
    public String sayHello(String greeting, String name) {
        System.out.println("new println");
        return activities.greet(greeting, name);
    }

}