package com.example.android.groupschedulecoordinator;

import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.Exclude;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.HashMap;

/**
 * Created by Emily on 10/27/2016.
 */
public class User implements Parcelable {

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

    /* Parcelable Methods **/
    /*
     * Name: describeContents()
     * Return: bitmask indicating the set of special objects
     */
    public int describeContents(){
        return 0; //TODO
    }

    /* flatten User into parcel */
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(userName);
        out.writeMap(pendingGroups);
        out.writeMap(acceptedGroups);
        out.writeMap(freeTimes);
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        userName = in.readString();
        in.readMap(pendingGroups, null); //?????
        in.readMap(acceptedGroups, null); //???
        in.readMap(freeTimes, null); //????
    }

    /** End Parcelable Methods **/


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
        if(pendingGroups.get(groupID)!=null) {
            pendingGroups.remove(groupID);
        }
        if (acceptedGroups.get(groupID) == null) {
                acceptedGroups.put(groupID, groupName);
        }
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
