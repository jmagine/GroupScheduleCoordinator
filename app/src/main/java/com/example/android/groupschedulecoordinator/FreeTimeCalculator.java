package com.example.android.groupschedulecoordinator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Alex Pu on 10/17/2016.
 */

public class FreeTimeCalculator {
    ArrayList<Integer> possibleTimes;
    ArrayList<ArrayList<Integer>> listOfPeopleTimes;
    int startTime;
    int endTime;
    int duration;

    //Constructor

    /**
     * @param st is startTime INDEX (0-47)
     * @param et is endTime INDEX (0-47). Will be exlusive
     * @param d is duration in blocks.
     * e.g. st==6, et==12, d==2 will result in all
     * suggested times existing between 3:00am-6:00am, and
     * they HAVE to be 1 hour long.
     */
    public FreeTimeCalculator(int st,int et,int d){
        possibleTimes = new ArrayList<Integer>(48);
        listOfPeopleTimes = new ArrayList<>();
        startTime = st;
        endTime = et;
        duration = d;
    }

    //Adds people to considered in calculating event times
    public void addPersonTime(ArrayList<Integer> personTime){
        listOfPeopleTimes.add(personTime);
    }

    //Fills possibleTimes, in the order of most free time blocks(exits when more than 10)
    //Flattens all the times in listOfPeopleTimes, then calls helper functions
    public void fillPossibleTimes(){
        ArrayList<Integer> summedTimes = new ArrayList<Integer>(48);
        ArrayList<Integer> summedBlocks;
        for(int j=0;j<48;j++){
            summedTimes.add(0);
        }
        for(ArrayList<Integer> currList : listOfPeopleTimes){
            for(int i=0;i<currList.size();i++) {
                summedTimes.set(i,summedTimes.get(i)+currList.get(i));
            }
        }
        summedBlocks = sumUpBlocks(summedTimes);

        int currMinBlockTime = -1;
        while(possibleTimes.size()<10){
            currMinBlockTime = addBlockTimes(summedBlocks,currMinBlockTime);
            if(currMinBlockTime == 0x7FFFFFFF)
                break;
        }
        while(possibleTimes.size()>10){
            possibleTimes.remove(possibleTimes.size()-1);
        }
    }

    //Adds up blocks so that the value held in each index is the sum, taking into account duration
    //Returns ArrayList size 48 that holds the weight of each index, assuming event starts at index
    private ArrayList<Integer> sumUpBlocks(ArrayList<Integer> sumTimes){
        ArrayList<Integer> summedBlock = new ArrayList<Integer>(48);
        for(int i=0;i<48;i++){
            summedBlock.add(-1);
        }
        for(int i=startTime;i<=endTime-duration;i++){
            int eventScore = 0;
            for(int j = 0;j<duration;j++){
                eventScore+=sumTimes.get(i+j);
            }
            summedBlock.set(i,eventScore);
        }
        return summedBlock;
    }

    //Searches for next smallest weighted block times. Adds all the blocks with the
    //same weight to possibleTimes.
    //Returns the new minimum block weight
    private int addBlockTimes(ArrayList<Integer> blockTimes, int currMin){
        int min = 0x7FFFFFFF;
        for(Integer searchInt:blockTimes){
            if(searchInt > currMin && searchInt < min)
                min = searchInt;
        }

        for(int i=0;i<blockTimes.size();i++){
            if(blockTimes.get(i)==min)
                possibleTimes.add(i);
        }
        return min;
    }
}
