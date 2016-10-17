package com.example.android.groupschedulecoordinator;

import java.util.ArrayList;

/**
 * Created by Alex Pu on 10/17/2016.
 */

public class UserFreeTime {
    ArrayList<Integer> filledTimes = new ArrayList<Integer>();
    ArrayList<Integer> freeTimes = new ArrayList<Integer>();

    public UserFreeTime(){
        for (int i=0;i<48;i++){
            freeTimes.set(i,1);
        }
    }

    public void adjustFreeTimes(){
        for (int i=0;i<48;i++){
            if(filledTimes.get(i) != 0) {
                freeTimes.set(i, 0);
            }
        }
    }


}
