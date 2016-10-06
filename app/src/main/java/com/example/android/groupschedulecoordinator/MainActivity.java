package com.example.android.groupschedulecoordinator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
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
    setContentView(R.layout.activity_main);
    // Get a reference for the week view in the layout.
    mWeekView = (WeekView) findViewById(R.id.weekView);

    // Set an action when any event is clicked.
    mWeekView.setOnEventClickListener(mEventClickListener);

    // The week view has infinite scrolling horizontally. We have to provide the events of a
    // month every time the month changes on the week view.
    mWeekView.setMonthChangeListener(mMonthChangeListener);

    // Set long press listener for events.
    mWeekView.setEventLongPressListener(mEventLongPressListener);
  }

  protected String getEventTitle(Calendar time) {
    return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
  }
}
