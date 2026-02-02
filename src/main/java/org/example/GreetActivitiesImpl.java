package org.example;

import io.temporal.activity.Activity;
import io.temporal.activity.ActivityExecutionContext;
import io.temporal.client.ActivityCompletionClient;

import java.util.concurrent.atomic.AtomicInteger;

public class GreetActivitiesImpl implements GreetActivities {
    private final ActivityCompletionClient completionClient = ClientProvider.getClient().newActivityCompletionClient();
    private static final AtomicInteger attempts = new AtomicInteger(0);

    public GreetActivitiesImpl() {
        System.out.println("GREET ACTIVITY CONSTRUCTOR: NEW VERSION");
    }

    @Override
    public String greet(String greeting, String name) {


        ActivityExecutionContext context = Activity.getExecutionContext();
        System.out.println("context: " + context);
        Integer lastStep = context.getHeartbeatDetails(Integer.class).orElse(0);

        // Set a correlation token that can be used to complete the activity asynchronously
        byte[] taskToken = context.getTaskToken();

        Thread.startVirtualThread(()-> {
            try {
                composeGreetingAsync(taskToken, greeting, name, context);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        context.doNotCompleteOnReturn();

        // Since we have set doNotCompleteOnReturn(), the workflow action method return value is
        // ignored.
        return "ignored";
    }

    // Method that will complete action execution using the defined ActivityCompletionClient
    private void composeGreetingAsync(byte[] taskToken, String greeting, String name, ActivityExecutionContext context, Integer step) throws InterruptedException {
        String result = greeting + " " + name + "!";
        Thread.sleep(2000);
        System.out.println("context: " + context);
        for(int i = step; i < 5; i++){
            context.heartbeat(step+1);
            if(i==2 ) {
                throw new RuntimeException("Crash after step 2");
            }
        }

        // Complete our workflow activity using ActivityCompletionClient
        completionClient.complete(taskToken, result);
    }
}