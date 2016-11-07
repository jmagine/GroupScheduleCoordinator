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
/**
 * Created by Emily on 10/27/2016.
 */
public class User implements Parcelable {

    private String userName;
    private ArrayList<String> pendingGroups;
    private ArrayList<String> acceptedGroups;
    private HashMap<String, ArrayList<Integer>> freeTimes;

    public User(){
    }

    public User(String name){
        userName = name;
        pendingGroups = new ArrayList<String>(10);
        acceptedGroups = new ArrayList<String>(10);
        freeTimes = new HashMap<String,ArrayList<Integer>>();
    }

    public String getUserName(){
        return userName;
    }

    public ArrayList<String> getPendingGroups(){
        return pendingGroups;
    }

    public ArrayList<String> getAcceptedGroups(){
        return acceptedGroups;
    }

    public HashMap<String,ArrayList<Integer>> getFreeTimes(){
        return freeTimes;
    }

    public void setUserName(String str){
        userName = str;
    }

    public void setPendingGroups(ArrayList<String> grps){
        pendingGroups = grps;
    }

    public void setAcceptedGroups(ArrayList<String> grps) {
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
        out.writeList(pendingGroups);
        out.writeList(acceptedGroups);
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
        in.readList(pendingGroups, null); //?????
        in.readList(acceptedGroups, null); //???
        in.readMap(freeTimes, null); //????
    }

    /** End Parcelable Methods **/


    @Exclude
    public void addToPendingGroup(String groupID){
        if(pendingGroups.indexOf(groupID)==-1)
            pendingGroups.add(groupID);
    }

    @Exclude
    public void rejectGroup(String groupID){
        if(pendingGroups.indexOf(groupID)!=-1)
            pendingGroups.remove(pendingGroups.indexOf(groupID));
    }

    @Exclude
    public void acceptGroup(String groupID){
        if(pendingGroups.indexOf(groupID) !=-1)
            pendingGroups.remove(pendingGroups.indexOf(groupID));
        if(acceptedGroups.indexOf(groupID)==-1)
            acceptedGroups.add(groupID);
    }

    @Exclude
    public void removeSelfFromGroup(String groupID){
        if(acceptedGroups.indexOf(groupID)!=-1)
            acceptedGroups.remove(acceptedGroups.indexOf(groupID));
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
