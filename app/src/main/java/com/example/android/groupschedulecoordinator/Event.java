package com.example.android.groupschedulecoordinator;
import java.util.ArrayList;
/**
 * Created by Emily on 10/27/2016.
 */
public class Event {

    private String name;
    private int start;
    private int end;
    private boolean isReady;
    private ArrayList<String> pendingUsers;
    private ArrayList<String> completedUsers;

    public Event(){
    }

    public Event(String name, ArrayList<String> pendingU) {
        pendingUsers = new ArrayList<String>(pendingU);
        completedUsers = new ArrayList<String>();
        this.name = name;
        this.start = 0;
        this.end = 0;
        isReady = false;
    }

    public void setTImes(int startT, int endT){
        start = startT;
        end = endT;
    }

    public boolean moveUser(String user){
        for(int i=0;i<pendingUsers.size();i++){
            if(pendingUsers.get(i).equals(user)){
                completedUsers.add(user);
                pendingUsers.remove(i);
                if(pendingUsers.size()==0){
                    isReady = true;
                }
                break;
            }
        }
        return isReady;
    }

}
