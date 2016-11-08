package com.example.android.groupschedulecoordinator;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.Exclude;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by Emily on 10/27/2016.
 */
public class User {

    //groups user belongs to
    private String userName;
    private HashMap<String,String> pendingGroups;
    private HashMap<String,String> acceptedGroups;
    private HashMap<String, ArrayList<Integer>> freeTimes;

    public User(){
    }

    public User(String name){
        userName = name;
        pendingGroups = new HashMap<String,String>();
        acceptedGroups = new HashMap<String,String>();
        freeTimes = new HashMap<String,ArrayList<Integer>>();
    }

    public String getUserName(){
        return userName;
    }

    public HashMap<String,String> getPendingGroups(){
        return pendingGroups;
    }

    public HashMap<String,String> getAcceptedGroups(){
        return acceptedGroups;
    }

    public HashMap<String,ArrayList<Integer>> getFreeTimes(){
        return freeTimes;
    }

    public void setUserName(String str){
        userName = str;
    }

    public void setPendingGroups(HashMap<String,String> grps){
        pendingGroups = grps;
    }

    public void setAcceptedGroups(HashMap<String,String> grps) {
        acceptedGroups = grps;
    }

    public void setFreeTimes(HashMap<String, ArrayList<Integer>> inTimes){
        try {
            freeTimes = inTimes;
        }
        catch(Exception e){
            Log.e("Error: ",e.toString());
        }
    }

    @Exclude
    public void addToPendingGroup(String groupID, String groupName){
        if(pendingGroups.get(groupID)==null)
            pendingGroups.put(groupID,groupName);
    }

    @Exclude
    public void rejectGroup(String groupID){
        if(pendingGroups.get(groupID)!=null)
            pendingGroups.remove(groupID);
    }

    @Exclude
    public void acceptGroup(String groupID,String groupName){
        if(pendingGroups.get(groupID)!=null)
            pendingGroups.remove(groupID);
        if(acceptedGroups.get(groupID)==null)
            acceptedGroups.put(groupID,groupName);
    }

    @Exclude
    public void removeSelfFromGroup(String groupID){
        if(acceptedGroups.get(groupID)!=null)
            acceptedGroups.remove(groupID);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userName", userName);
        result.put("pendingGroups", pendingGroups);
        result.put("acceptedGroups", acceptedGroups);
        result.put("freeTimes", freeTimes);

        return result;
    }
}
