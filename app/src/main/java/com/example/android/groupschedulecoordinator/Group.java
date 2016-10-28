package com.example.android.groupschedulecoordinator;

import java.util.ArrayList;

/**
 * Created by Emily on 10/27/2016.
 */
public class Group {
    private ArrayList<User> members;

    public void addMember(User newperson){
        members.add(newperson);
    }

    public void deleteMember(User newperson){
        for(int i = 0; i < members.size(); i++){
            if (members.get(i).equals(newperson))
            {
                members.remove(i);
                break;
            }
        }
    }





}
