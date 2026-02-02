package org.example;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface GreetActivities {

    @ActivityMethod
    String greet(String greeting, String name);

}