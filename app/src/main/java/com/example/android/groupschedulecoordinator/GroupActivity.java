package com.example.android.groupschedulecoordinator;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.api.client.util.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GroupActivity extends AppCompatActivity {

    private ListView lvMem;
    private ListView lvEvent;
    private ArrayList<String> group_list;
    private ArrayList<String> groupID_list;
    private ArrayList<String> event_list;
    private HashMap<String, Event> currEvents;
    private HashMap<String, String> currGroup;
    private HashMap<String, String> currAccepted;
    private final Context c = this;
    private WeekView mWeekView;
    private WeekView.EventClickListener mEventClickListener;
    private WeekView.EventLongPressListener mEventLongPressListener;
    private DatabaseReference mDatabase;
    private DatabaseReference mGroupsReference;
    private DatabaseReference mMembersRefernce;
    private DatabaseReference mAcceptedReference;
    private Group currentGroup;
    private String groupID;
    private ValueEventListener mListener;
    private String calling;
    private String memberName;
    private String memberEmail;
    private String groupName;
    private User currentUser;


    WeekView.MonthChangeListener mMonthChangeListener = new WeekView.MonthChangeListener() {
        @Override
        public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
            // Populate the week view with some events.
            List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, 3);
            startTime.set(Calendar.MINUTE, 0);
            startTime.set(Calendar.MONTH, newMonth-1);
            startTime.set(Calendar.YEAR, newYear);
            Calendar endTime = (Calendar) startTime.clone();
            endTime.add(Calendar.HOUR, 1);
            endTime.set(Calendar.MONTH, newMonth-1);
            WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
            //event.setColor(getResources().getColor(R.color.event_color_01));
            events.add(event);

            return events;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_group);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        System.out.println("GroupActivity Database Reference: " + mDatabase.toString());

        lvMem = (ListView) findViewById(R.id.lvMembers);
        group_list = new ArrayList<String>();
        groupID_list = new ArrayList<>();

        lvEvent = (ListView) findViewById(R.id.lvMeetings);
        event_list = new ArrayList<String>();

        final Bundle bundle1 = getIntent().getExtras();
        if(bundle1 != null)
        {
            groupID = bundle1.getString("groupID");
            Log.d("jlogs", "groupID: " + groupID);
            if(groupID == null) {
                groupID = "null";
                Log.d("jlogs", "null groupID");
            }

            currentGroup = new Group();
            currentUser = new User();

            mGroupsReference = mDatabase.child("groups").child(groupID);
            mMembersRefernce = mDatabase.child("groups").child(groupID);
            System.out.println("GroupActivity Database Group Reference: " + mGroupsReference.toString());
            group_list = bundle1.getStringArrayList("groupList");
            event_list = bundle1.getStringArrayList("eventList");

            calling = bundle1.getString("calling");
            memberName = bundle1.getString("member_name");
            memberEmail = bundle1.getString("member_email");
            if(memberName != null)
                memberName = memberEmail.replace('.',(char)0xA4);
        }
        else
            Log.d("jlogs", "bundle is null");

        if(group_list != null) {
            ArrayAdapter<String> arrayAdapterMember = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    group_list);

            if( lvMem != null ) {
                lvMem.setAdapter(arrayAdapterMember);
            }
        }

        if(event_list != null) {
            ArrayAdapter<String> arrayAdapterEvent = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    event_list);

            if(lvEvent != null) {
                lvEvent.setAdapter(arrayAdapterEvent);
            }
        }

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        final String groupName = extras.getString("groupName");

        TextView tbGroupName = (TextView) findViewById(R.id.groupName);
        tbGroupName.setText(groupName);


        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.info);
        spec.setIndicator("Info");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.members);
        spec.setIndicator("Members");
        host.addTab(spec);

        //Tab 3
        //spec = host.newTabSpec("Tab Three");
        //spec.setContent(R.id.calendar);
        //spec.setIndicator("Calendar");
        //host.addTab(spec);




        // Get a reference for the week view in the layout.
        //mWeekView = (WeekView) findViewById(R.id.weekView);

        // Set an action when any event is clicked.
        //mWeekView.setOnEventClickListener(mEventClickListener);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        //mWeekView.setMonthChangeListener(mMonthChangeListener);

        // Set long press listener for events.
        //mWeekView.setEventLongPressListener(mEventLongPressListener);


        Button fab = (Button) findViewById(R.id.addmembers);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_popup, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.enterEmail);

                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                group_list.add(userInputDialogEditText.getText().toString());
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                        c,
                                        android.R.layout.simple_list_item_1,
                                        group_list);

                                lvMem.setAdapter(arrayAdapter);
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        //dialogBox.cancel();
                                    }
                                });



                Intent intent = new Intent(GroupActivity.this, AddMemberToGroup.class);
                intent.putStringArrayListExtra("groupList", group_list);
                intent.putExtra("groupID", groupID);
                startActivity(intent);
                //startActivity(new Intent(GroupActivity.this, ActivityCreateGroup.class));
            }

        });

        Button fab1 = (Button) findViewById(R.id.btnNewMeeting);
        fab1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_popup, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.enterEmail);

                alertDialogBuilderUserInput
                      .setCancelable(false)
                      .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialogBox, int id) {
                              event_list.add(userInputDialogEditText.getText().toString());
                              ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                    c,
                                    android.R.layout.simple_list_item_1,
                                    event_list);

                              lvEvent.setAdapter(arrayAdapter);
                              //addEvent(userInputDialogEditText.getText().toString(), 1, 2);
                          }
                      })

                      .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    //dialogBox.cancel();
                                }
                            });
                Intent intent = new Intent(GroupActivity.this, ActivityCreateMeeting.class);;
                intent.putStringArrayListExtra("memberIDList", groupID_list);
                intent.putExtra("groupID", groupID);
                startActivity(intent);
            }

        });

        ValueEventListener dataListener = new ValueEventListener() {
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
                    updateEventList();
                    updateMemberList();
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

        mGroupsReference.addValueEventListener(dataListener);
        mListener = dataListener;
        //mListener = dataListener;

        System.out.println("Calling: " + calling);
        if(calling.equals("createMeeting")) {
            String name = bundle1.getString("eventName");

            //addEvent(name, 1, 2);
        }
        if(calling.equals("addMember")){
            addMember(memberName, memberEmail);
        }

    }

    private void updateMemberList(){
        lvMem = (ListView) findViewById(R.id.lvMembers);

        group_list = new ArrayList<>();
        groupID_list = new ArrayList<>();
        HashMap<String,String> memberMap;

        if (currentGroup.getMembers() != null) {
            System.out.println("TLOG: Setting current group to be currentGroup.getMembers()");
            memberMap = currentGroup.getMembers();
            System.out.println(memberMap.size() + " is the size!");
        }
        else{
            System.out.println("AMZ: Setting current group to be new");
            memberMap = new HashMap<>();
        }
        Set<String> memberSet = memberMap.keySet();
        System.out.println("AMZ: Size of keyset is: " + memberSet.size());
        ArrayList<String> sortedMemberKeys = new ArrayList<>();
        for(String keys: memberSet){
            sortedMemberKeys.add(keys);
        }
        Collections.sort(sortedMemberKeys);
        System.out.println("AMZ: Size of sortedKeys is: " + sortedMemberKeys.size());

        if(currGroup == null) currGroup = new HashMap<>();
        for(String sortedKeys: sortedMemberKeys){
            groupID_list.add(sortedKeys);
            group_list.add(memberMap.get(sortedKeys));
            System.out.println("AMZ: Sorted keys is: "+ sortedKeys);
            System.out.println("AMZ: key maps to: " + memberMap.get(sortedKeys));
            currGroup.put(sortedKeys,memberMap.get(sortedKeys));
        }

        if (group_list != null) {
            ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    group_list);
            lvMem.setAdapter(arrayAdapter2);
        }

        System.out.println("AMZ: size of currGroup before setValue: " + currGroup.size());
        mMembersRefernce.child("members").setValue(currGroup);

        //update user's accepted groups
        System.out.println("AMZ: MemberName is: " + memberName);
        if(memberName != null){
            System.out.println("AMZ: NONNULL- MemberName is: " + memberName);
            mAcceptedReference = mDatabase.child("users").child(memberName);
            ValueEventListener acceptedSnapShot = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        System.out.println("DMZ: IN UPDATING ACCEPTEDGROUPS");
                        User tempUser = dataSnapshot.getValue(User.class);
                        if(tempUser.getAcceptedGroups() == null){
                            System.out.println("DMZ: IN get ACCEPTEDGROUPS");
                            tempUser.setAcceptedGroups(new HashMap<String, String>());
                        }
                        if(tempUser.getUserName() == null){
                            System.out.println("DMZ: IN get username");
                            tempUser.setUserName("");
                        }
                        if(tempUser.getFreeTimes() == null){
                            System.out.println("DMZ: IN get freetimes");
                            tempUser.setFreeTimes(new HashMap<String, ArrayList<Integer>>());
                        }
                        if(tempUser.getPendingGroups() == null){
                            System.out.println("DMZ: IN get pending");
                            //tempUser.setPendingGroups(new HashMap<String, String>());
                        }
                        if(currAccepted == null){
                            System.out.println("DMZ: IN null currEvent");
                            currAccepted = new HashMap<>();
                        }
                        currAccepted = tempUser.getAcceptedGroups();
                        System.out.println("DMZ: CurrAccepted size: " + currAccepted.size());
                        currAccepted.put(groupID,currentGroup.getGroupName());
                        tempUser.setAcceptedGroups(currAccepted);
                        if(tempUser.getAcceptedGroups().size() > 0){
                            System.out.println("DMZ: Inside >0 condtion");
                            currentUser.setAcceptedGroups(tempUser.getPendingGroups());
                            currentUser.setPendingGroups(tempUser.getPendingGroups());
                            currentUser.setUserName(tempUser.getUserName());
                            currentUser.setFreeTimes(tempUser.getFreeTimes());
                        }
                        System.out.println("DMZ: updating currentUser's accepted groups");
                        if(tempUser.getUserName() != null){
                            System.out.println("DMZ: Currentuser is: " + tempUser.getUserName());
                        }
                        if(tempUser.getAcceptedGroups() != null){
                            System.out.println("DMZ: Size of acceptedGroups is: " + tempUser.getAcceptedGroups().size() );
                        }
                        if(tempUser.getAcceptedGroups().size() > 0)
                            mAcceptedReference.setValue(tempUser);
                    }else{
                        System.out.println("DMZ: DNE");
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mAcceptedReference.addValueEventListener(acceptedSnapShot);

            //if(currAccepted == null) currAccepted = new HashMap<>();
            //currAccepted.put(groupID, "test");
            //mAcceptedReference.child("acceptedGroups").setValue(currAccepted);

        }
        //HashMap<String,String> currAccepted = new HashMap<>();
        //System.out.println("AMZ: Group name is: " + groupName);
        //currAccepted.put(groupID, groupName);
        //mAcceptedReference.child("acceptedGroups").setValue(currAccepted);
    }

    private void updateEventList(){

        System.out.println("Entered updateEventList");
        lvEvent = (ListView) findViewById(R.id.lvMeetings);

        event_list = new ArrayList<String>();
        currEvents = currentGroup.getEvents();

        if(currEvents != null) {
            System.out.println("EventMap: " + currEvents);
        }
        else {
            System.out.println("currentGroup no events");
            currentGroup.setEvents(new HashMap<String, Event>());
        }
        Set<String> keySet = currEvents.keySet();
        ArrayList<String> sortedKeys = new ArrayList<String>();
        for(String i:keySet){
            sortedKeys.add(i);
        }
        Collections.sort(sortedKeys);
        System.out.println(sortedKeys);

        for(String s: sortedKeys){
            int time = currEvents.get(s).getStart();
            int dur = currEvents.get(s).getDuration();

            String start = "";
            String end = "";
            if(time / 2 < 10)
                start += "0";
            start += time / 2 + ":";

            if(time % 2 == 0)
                start += "00";
            else
                start += "30";

            if((time + dur) / 2 < 10)
                end = "0";

            end += (time + dur) / 2 + ":";

            if((time + dur) % 2 == 0)
                end += "00";
            else
                end += "30";

            event_list.add(s + " at " + start + " - " + end);
        }

        System.out.println("CurrEvents: " + currEvents);

        if(event_list.size() != 0) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                  this,
                  android.R.layout.simple_list_item_1,
                  event_list);
            lvEvent.setAdapter(arrayAdapter);
        }

        mGroupsReference.child("events").setValue(currEvents);

        /*
        lvEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String groupID = groupID_list.get(position);
                Intent intent = new Intent(MainActivity.this, GroupActivity.class);
                intent.putExtra("groupID", groupID);
                startActivity(intent);
            }
        });
        */
    }

    private void addMember(String name, String email){
        if(currGroup == null){
            currGroup = new HashMap<>();
        }
        currGroup.put(name,email);
    }
    private void addEvent(String eventName, int start, int duration){
        System.out.println("Entered addEvent: " + eventName + " " + start + " " + duration);
        System.out.println("EventList: " + event_list);
        System.out.println("currentGroup: " + currentGroup.toString());
        HashMap<String,String> members = currentGroup.getMembers();
        Set<String> tempSet = members.keySet();
        ArrayList<String> memList = new ArrayList<String>();
        for(String s:tempSet){
            memList.add(s);
        }
        Event tempEvent = new Event(eventName, memList);
        tempEvent.setStart(start);
        tempEvent.setEnd(start + duration);

        //String eventID = mDatabase.child("groups").child(groupID).push().getKey();
        //mDatabase.child("groups").child(groupID).child(eventID).setValue(tempEvent);

        //update group's events
        if(currEvents == null) {
            System.out.println("Instantiated currEvents - addEvent");
            currEvents = new HashMap<>();
        }
        currEvents.put(eventName, tempEvent);
        //updateEventList();
        //currentGroup.setEvents(currEvents);

        //push group events changes to firebase
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mGroupsReference.removeEventListener(mListener);
    }
}
