package com.example.android.groupschedulecoordinator;

import java.util.ArrayList;

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





}
