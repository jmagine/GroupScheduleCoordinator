package com.example.android.groupschedulecoordinator;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityCreateMeeting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        String meetingStr = "";
        EditText eventName = (EditText) findViewById(R.id.eventName);
        Spinner hourLength =  (Spinner) findViewById(R.id.spinnerLengthHour);
        Spinner minutesLength = (Spinner) findViewById(R.id.spinnerLengthMinute);

        Spinner beginHour =  (Spinner) findViewById(R.id.spinnerBeginHour);
        Spinner beginMin = (Spinner) findViewById(R.id.spinnerBeginMinute);
        Spinner beginTime = (Spinner) findViewById(R.id.spinnerBeginToD);

        Spinner endHour = (Spinner) findViewById(R.id.spinnerBeginHour);
        Spinner endMin = (Spinner) findViewById(R.id.spinnerEndMinute);

        String eventNameStr = eventName.getText().toString();
        String hourLenStr = hourLength.getSelectedItem().toString();
        String minLenStr = minutesLength.getSelectedItem().toString();
        String beginHourStr = beginHour.getSelectedItem().toString();
        String beginMinStr = beginMin.getSelectedItem().toString();
        String beginTimeStr = beginTime.getSelectedItem().toString();
        String endHourStr = endHour.getSelectedItem().toString();
        String endMinStr = endMin.getSelectedItem().toString();

        String groupID;

        if(Integer.parseInt(beginHourStr) < 10)
            beginHourStr = "0" + beginHourStr;
        if(Integer.parseInt(beginMinStr) < 10)
            beginMinStr += "0";

        if(Integer.parseInt(endHourStr) < 10)
            endHourStr = "0" + endHourStr;
        if(Integer.parseInt(endMinStr) < 10)
            endMinStr += "0";

        meetingStr += eventNameStr + " - " + beginHourStr + ":" + beginMinStr + " " + beginTimeStr;
        if(eventNameStr.isEmpty()) {
            displayFuckingWarning("Please enter a valid event name");
        }
        else if(hourLenStr.isEmpty() || minLenStr.isEmpty())
        {
            displayFuckingWarning("Please enter a valid length.");
        }
        else if(beginHourStr.isEmpty() || beginMinStr.isEmpty())
        {
            displayFuckingWarning("Please enter a valid begin time.");
        }
        else
        {
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

    public void displayFuckingWarning(String str) {
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
