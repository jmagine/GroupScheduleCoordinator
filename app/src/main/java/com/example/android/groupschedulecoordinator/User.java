package com.example.android.groupschedulecoordinator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by Emily on 10/27/2016.
 */
public class User {

    //groups user belongs to
    private String userName;
    private ArrayList<String> groups;
    private HashMap<String, ArrayList<Integer>> freeTimes;

    public void User(){
    }

    public void User(String name){
        userName = name;
    }

    public HashMap<String,ArrayList<Integer>> getTimes(){
        return freeTimes;
    }

    public void setFreeTimes(HashMap<String,ArrayList<Integer>> inTimes){
        freeTimes = inTimes;
    }

    public void addGroup(String groupID){
        if(groups.indexOf(groupID) ==-1)
            groups.add(groupID);
    }

    public void removeSelfFromGoup(String group){
        if(groups.indexOf(group)!=-1)
            groups.remove(groups.indexOf(group));
    }
}
