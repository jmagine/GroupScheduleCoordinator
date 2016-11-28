package com.example.android.groupschedulecoordinator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityCreateMeeting extends AppCompatActivity {

    String grpID;
    ArrayList<String> memberIDList;
    ArrayList<HashMap<String,ArrayList<Integer>>> listOfPeopleFreeTimes;
    FreeTimeCalculator freeTimeCalculator;
    DatabaseReference mDatabase;
    DatabaseReference mGroupReference;
    DatabaseReference mUsersReference;
    Group currentGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle1 = getIntent().getExtras();
        grpID = bundle1.getString("groupID");
        memberIDList = bundle1.getStringArrayList("memberIDList");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mGroupReference = mDatabase.child("groups").child(grpID);
        mUsersReference = mDatabase.child("users");
        currentGroup = new Group();
        listOfPeopleFreeTimes = new ArrayList<>();

        ValueEventListener dataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    // Get CustomUser object and use the values to update the UI
                    System.out.println("Data: "+dataSnapshot.toString());
                    User tempUser = dataSnapshot.getValue(User.class);
                    if(tempUser.getFreeTimes()!=null)
                        listOfPeopleFreeTimes.add(tempUser.getFreeTimes());
                }
                else{
                    System.out.println(dataSnapshot.toString()+"Does not exist");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        };

        ValueEventListener groupListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("onDataChange");
                if (dataSnapshot.exists()){
                    // Get CustomUser object and use the values to update the UI
                    //System.out.println("Data change for " + userName);
                    System.out.println("Data: "+dataSnapshot.toString());
                    Group tempGroup = dataSnapshot.getValue(Group.class);

                    if(tempGroup.getEvents() == null)
                        tempGroup.setEvents(new HashMap<String, Event>());
                    if(tempGroup.getMembers() == null)
                        tempGroup.setMembers(new HashMap<String, String>());
                    if(tempGroup.getGroupName() == null)
                        tempGroup.setGroupName("");
                    currentGroup.setEvents(tempGroup.getEvents());
                    currentGroup.setMembers(tempGroup.getMembers());
                    currentGroup.setGroupName(tempGroup.getGroupName());
                }
                else{
                    System.out.println(dataSnapshot.toString()+"Does not exist");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("jasonlogs", "loadUser:onCancelled", databaseError.toException());
                // ...
            }
        };

        mGroupReference.addListenerForSingleValueEvent(groupListener);
        for(String memID : memberIDList){
            mUsersReference.child(memID).addListenerForSingleValueEvent(dataListener);
        }

        setContentView(R.layout.activity_create_meeting);


        Integer[] lengthHours = new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Spinner s = (Spinner) findViewById(R.id.spinnerLengthHour);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, lengthHours);
        s.setAdapter(adapter);


        Integer[] lengthMin = new Integer[] {0, 30};
        Spinner s2 = (Spinner) findViewById(R.id.spinnerLengthMinute);
        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, lengthMin);
        s2.setAdapter(adapter2);

        Integer[] beginHour = new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        Spinner s3 = (Spinner) findViewById(R.id.spinnerBeginHour);
        ArrayAdapter<Integer> adapter3 = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, beginHour);
        s3.setAdapter(adapter3);

        Integer[] beginMin = new Integer[] {0, 30};
        Spinner s4 = (Spinner) findViewById(R.id.spinnerBeginMinute);
        ArrayAdapter<Integer> adapter4 = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, beginMin);
        s4.setAdapter(adapter4);

        String[] beginTod = new String[] {"AM", "PM"};
        Spinner s5 = (Spinner) findViewById(R.id.spinnerBeginToD);
        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, beginTod);
        s5.setAdapter(adapter5);



        Integer[] endHour = new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        Spinner s6 = (Spinner) findViewById(R.id.spinnerEndHour);
        ArrayAdapter<Integer> adapter6 = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, endHour);
        s6.setAdapter(adapter6);

        Integer[] endMin = new Integer[] {0, 30};
        Spinner s7 = (Spinner) findViewById(R.id.spinnerEndMinute);
        ArrayAdapter<Integer> adapter7 = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, endMin);
        s7.setAdapter(adapter7);

        String[] endTod = new String[] {"AM", "PM"};
        Spinner s8 = (Spinner) findViewById(R.id.spinnerEndToD);
        ArrayAdapter<String> adapter8 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, endTod);
        s8.setAdapter(adapter8);



    }

    public void addMeeting(View v) {

        System.out.println(listOfPeopleFreeTimes);
        String meetingStr = "";
        EditText eventName = (EditText) findViewById(R.id.eventName);
        Spinner hourLength =  (Spinner) findViewById(R.id.spinnerLengthHour);
        Spinner minutesLength = (Spinner) findViewById(R.id.spinnerLengthMinute);

        Spinner beginHour =  (Spinner) findViewById(R.id.spinnerBeginHour);
        Spinner beginMin = (Spinner) findViewById(R.id.spinnerBeginMinute);
        Spinner beginTime = (Spinner) findViewById(R.id.spinnerBeginToD);

        Spinner endHour = (Spinner) findViewById(R.id.spinnerBeginHour);
        Spinner endMin = (Spinner) findViewById(R.id.spinnerEndMinute);
        Spinner endTime = (Spinner) findViewById(R.id.spinnerEndToD);


        String eventNameStr = eventName.getText().toString();
        String hourLenStr = hourLength.getSelectedItem().toString();
        String minLenStr = minutesLength.getSelectedItem().toString();
        int duration = Integer.parseInt(hourLenStr)*2 + Integer.parseInt(minLenStr)/30;

        String beginHourStr = beginHour.getSelectedItem().toString();
        String beginMinStr = beginMin.getSelectedItem().toString();
        String beginTimeStr = beginTime.getSelectedItem().toString();
        int start = Integer.parseInt(beginHourStr)*2 + Integer.parseInt(beginMinStr)/30;
        if(beginTime.equals("PM"))
            start+=24;
        if(beginHourStr.equals("12"))
            start-=24;

        String endHourStr = endHour.getSelectedItem().toString();
        String endMinStr = endMin.getSelectedItem().toString();
        String endTimeStr = endTime.getSelectedItem().toString();
        int end = Integer.parseInt(endHourStr)*2 + Integer.parseInt(endMinStr)/30;
        if(endTimeStr.equals("PM"))
            end+=24;
        if(endTimeStr.equals("12"))
            end-=24;

        String groupID;

        if(Integer.parseInt(beginHourStr) < 10)
            beginHourStr = "0" + beginHourStr;
        if(Integer.parseInt(beginMinStr) < 10)
            beginMinStr += "0";

        if(Integer.parseInt(endHourStr) < 10)
            endHourStr = "0" + endHourStr;
        if(Integer.parseInt(endMinStr) < 10)
            endMinStr = endMinStr+"0";

        System.out.println("Time: "+start +"-"+end+"-:"+duration);

        boolean printErrors = false;
        if(end<start){
            displayWarning("You're trying to go back in time!");
            printErrors = true;
        }
        if(end==start){
            displayWarning("Desired meeting 0 search range!");
            printErrors = true;
        }
        if(duration ==0){
            displayWarning("Desired meeting with 0 duration!");
            printErrors = true;
        }
        if(start+duration>end){
            displayWarning("Desired duration longer than search range!");
            printErrors = true;
        }

        meetingStr += eventNameStr + " - " + beginHourStr + ":" + beginMinStr + " " + beginTimeStr;
        if(eventNameStr.isEmpty()) {
            displayWarning("Please enter a valid event name");
            printErrors = true;
        }
        else if(hourLenStr.isEmpty() || minLenStr.isEmpty())
        {
            displayWarning("Please enter a valid length.");
            printErrors = true;
        }
        else if(beginHourStr.isEmpty() || beginMinStr.isEmpty())
        {
            displayWarning("Please enter a valid begin time.");
            printErrors = true;
        }
        if(printErrors)
            return;
        else
        {
            DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker2);
            int dayOfMonth = datePicker.getDayOfMonth();
            int dateYear = datePicker.getYear();
            int dateMonth = datePicker.getMonth()+1;
            String dateAsString = ""+dateMonth+"-"+dayOfMonth+"-"+dateYear;
            System.out.println("Trying: " + dateAsString);
            freeTimeCalculator = new FreeTimeCalculator(start,end,duration);
            for(int i=0;i<listOfPeopleFreeTimes.size();i++){
                HashMap<String,ArrayList<Integer>> tempMap = listOfPeopleFreeTimes.get(i);
                ArrayList<Integer> daysFreeTimes = tempMap.get(dateAsString);
                if(daysFreeTimes != null){
                    freeTimeCalculator.addPersonTime(daysFreeTimes);
                }
            }
            freeTimeCalculator.fillPossibleTimes();
            System.out.println("free calc: "+freeTimeCalculator.possibleTimes);

            if(freeTimeCalculator.possibleTimes.size() < 1){
                displayWarning("Selected date and times have no possible meeting");
                return;
            }

            System.out.println("currGroup events: "+currentGroup.getEvents());

            Event event = new Event(eventNameStr,memberIDList);

            //Take first option
            event.setStart(freeTimeCalculator.possibleTimes.get(0));
            event.setEnd(freeTimeCalculator.possibleTimes.get(0)+duration);
            event.setDuration(duration);

            currentGroup.addEvent(event);

            System.out.println("currGroup events after: "+currentGroup.getEvents());

            mGroupReference.child("events").setValue(currentGroup.getEvents());

            Intent intent = new Intent(ActivityCreateMeeting.this, GroupActivity.class);

            Bundle extras = getIntent().getExtras();
            ArrayList<String> event_list = new ArrayList<>();
            if( extras != null ) {
                event_list = extras.getStringArrayList("eventList");

                groupID = extras.getString("groupID");
            }
            else
                groupID = "";

            System.out.println("Create Meeting groupID:" + extras.getString("groupID"));

            if(event_list == null)
            {
                event_list = new ArrayList<>();
            }
            event_list.add(meetingStr);

            //intent.putExtra("memberName", memberName);
            intent.putStringArrayListExtra("eventList", event_list);
            intent.putExtra("eventName", eventNameStr);
            intent.putExtra("eventStart", beginHourStr);
            intent.putExtra("eventEnd", endHourStr);
            intent.putExtra("eventDuration", hourLenStr);
            intent.putExtra("groupID", groupID);
            intent.putExtra("calling", "createMeeting");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    public void displayWarning(String str) {
        android.content.Context context = getApplicationContext();
        CharSequence warning = str;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, warning, duration);
        toast.show();
    }

//    public void pickTimeStart(View v)
//    {
//        Calendar mcurrentTime = Calendar.getInstance();
//        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//        int minute = mcurrentTime.get(Calendar.MINUTE);
//
////        String am_pm = "";
////        if (mcurrentTime.get(Calendar.AM_PM) == Calendar.AM)
////        {
////            am_pm = "AM";
////        }
////        else if (mcurrentTime.get(Calendar.AM_PM) == Calendar.PM)
////        {
////            am_pm = "PM";
////        }
////
////        final String ampm = am_pm;
//        final EditText editText = (EditText) findViewById(R.id.tbStartTime);
//
//        TimePickerDialog mTimePicker;
//        mTimePicker = new TimePickerDialog(ActivityCreateMeeting.this, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                editText.setText( selectedHour + ":" + selectedMinute);
//            }
//        }, hour, minute, true);
//        mTimePicker.show();
//    }
//
//
//    public void pickTimeEnd(View v)
//    {
//        Calendar mcurrentTime = Calendar.getInstance();
//        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//        int minute = mcurrentTime.get(Calendar.MINUTE);
//
////        String am_pm = "";
////        if (mcurrentTime.get(Calendar.AM_PM) == Calendar.AM)
////        {
////            am_pm = "AM";
////        }
////        else if (mcurrentTime.get(Calendar.AM_PM) == Calendar.PM)
////        {
////            am_pm = "PM";
////        }
////        final String ampm = am_pm;
//        final EditText editText = (EditText) findViewById(R.id.tbEndTime);
//
//        TimePickerDialog mTimePicker;
//        mTimePicker = new TimePickerDialog(ActivityCreateMeeting.this, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                editText.setText( selectedHour + ":" + selectedMinute);
//            }
//        }, hour, minute, true);
//        mTimePicker.show();
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

}
