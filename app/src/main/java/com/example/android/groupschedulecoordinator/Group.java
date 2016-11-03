package com.example.android.groupschedulecoordinator;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Emily on 10/27/2016.
 */
public class Group {

    private String groupID;
    private ArrayList<String> members;
    private ArrayList<String> events;

    public void addMember(String newperson){
        if(members.indexOf(newperson)==-1)
            members.add(newperson);
    }

    public void deleteMember(String newperson){
        for(int i = 0; i < members.size(); i++){
            if (members.get(i).equals(newperson))
            {
                members.remove(i);
                break;
            }
        }
    }

    public void addEvent(String event){
        if(events.indexOf(event)==-1)
            events.add(event);
    }

    public void removeUser(String userID){
        if(members.indexOf(userID)!=-1)
            members.remove(members.indexOf(userID));
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("groupID", groupID);
        result.put("members", members);
        result.put("events", events);

        return result;
    }



}
