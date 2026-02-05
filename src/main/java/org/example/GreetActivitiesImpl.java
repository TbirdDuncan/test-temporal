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

        attempts.incrementAndGet();
        ActivityExecutionContext context = Activity.getExecutionContext();
        Integer lastStep = context.getHeartbeatDetails(Integer.class).orElse(0);
        System.out.println("heartbeat " + lastStep);

        byte[] taskToken = context.getTaskToken();
        Thread.startVirtualThread(()-> {
            try {
                composeGreetingAsync(taskToken, greeting, name, context, lastStep);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        context.doNotCompleteOnReturn();

        return "ignored";
    }

    private void composeGreetingAsync(byte[] taskToken, String greeting, String name, ActivityExecutionContext context, Integer step) throws InterruptedException {
        String result = greeting + " " + name + "!";
        System.out.println("attempt: " + attempts.get());

        for(int i = step; i < 5; i++){
            context.heartbeat(i+1);
            if(i==2 ) {
                throw new RuntimeException("Crash after step 2");
            }
        }

        completionClient.complete(taskToken, result);
    }
}