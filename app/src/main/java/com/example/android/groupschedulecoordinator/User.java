package com.example.android.groupschedulecoordinator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by Emily on 10/27/2016.
 */
public class User {

    //groups user belongs to
    private ArrayList<Group> groups;
    private ArrayList<Event> eventshosted;
    private HashMap<String, ArrayList<Integer>> freetimes;


    String username;

    public boolean isFree() {
        return false;
    }

    public void joinEvent(){

    }

    public void createEvent(){

    }

}
