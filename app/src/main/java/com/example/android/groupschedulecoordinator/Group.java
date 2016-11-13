package com.example.android.groupschedulecoordinator;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Emily on 10/27/2016.
 */
public class Group {

    private String groupName;
    private HashMap<String,String> members;
    private HashMap<String,String> events;

    public Group(){
    }

    public Group(String str){
        groupName = str;
        members = new HashMap<String,String>();
        events = new HashMap<String,String>();
    }

    public void addMember(String newPersonID, String newperson){
        if(members.get(newperson)==null)
            members.put(newPersonID, newperson);
    }

    public String getGroupName(){
        return groupName;
    }

    public HashMap<String,String> getMembers(){
        return members;
    }

    public HashMap<String,String> getEvents(){
        return events;
    }

    public void setGroupName(String group){
        groupName=group;
    }

    public void setMembers(HashMap<String,String> inList){
        members = inList;
    }

    public void setEvents(HashMap<String,String> inList){
        events = inList;
    }

    @Exclude
    public void deleteMember(String personID){
        if(members.get(personID)!=null)
            members.remove(personID);
    }

    @Exclude
    public void addEvent(String eventID, String eventName){
        if(events.get(eventID) == null)
            events.put(eventID,eventName);
    }

    @Exclude
    public void removeUser(String userID){
        if(members.get(userID)!=null)
            members.remove(userID);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("groupName", groupName);
        result.put("members", members);
        result.put("events", events);

        return result;
    }
}
