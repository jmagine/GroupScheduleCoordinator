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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<String> group_list;
    final Context c = this;
    WeekView mWeekView;
    WeekView.EventClickListener mEventClickListener;
    WeekView.EventLongPressListener mEventLongPressListener;


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



        lv = (ListView) findViewById(R.id.lvMembers);
        group_list = new ArrayList<String>();

        final Bundle bundle1 = getIntent().getExtras();
        if(bundle1 != null)
        {
//            String groupName = extras.getString("groupName");
//            if(groupName != null) {
//                group_list.add(groupName);
//            }
            group_list = bundle1.getStringArrayList("groupList");
        }

        if(group_list != null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    group_list);

            if( lv != null ) {
                lv.setAdapter(arrayAdapter);
            }
        }








//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        String groupName = extras.getString("groupName");

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
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.calendar);
        spec.setIndicator("Calendar");
        host.addTab(spec);




        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Set an action when any event is clicked.
        mWeekView.setOnEventClickListener(mEventClickListener);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(mMonthChangeListener);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(mEventLongPressListener);


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

                                lv.setAdapter(arrayAdapter);
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

                startActivity(intent);
                //startActivity(new Intent(GroupActivity.this, ActivityCreateGroup.class));
            }

        });

        Button fab1 = (Button) findViewById(R.id.btnNewMeeting);
        fab1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this, ActivityCreateMeeting.class);;

                startActivity(intent);
            }

        });
        
    }
    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }
}
