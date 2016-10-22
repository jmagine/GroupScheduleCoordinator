package com.example.android.groupschedulecoordinator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TabHost;

public class GroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        String groupName = extras.getString("groupName");
        setTitle(groupName);

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec1 = host.newTabSpec("Tab One");
        spec1.setContent(R.id.info);
        spec1.setIndicator("Info");
        host.addTab(spec1);


        //Tab 2
        TabHost.TabSpec spec2 = host.newTabSpec("Tab Two");
        spec2.setContent(R.id.members);
        spec2.setIndicator("Members");
        host.addTab(spec2);

        //Tab 3
        TabHost.TabSpec spec3 = host.newTabSpec("Tab Three");
        spec3.setContent(R.id.calendar);
        spec3.setIndicator("Calendar");
        host.addTab(spec3);

    }
}
