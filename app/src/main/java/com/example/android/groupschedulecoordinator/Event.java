package com.example.android.groupschedulecoordinator;
import java.util.ArrayList;
/**
 * Created by Emily on 10/27/2016.
 */
public class Event {
    String name;
    int start;
    int end;
    boolean pending;
    boolean completed;

    public Event(String name, int start, int end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }
}
