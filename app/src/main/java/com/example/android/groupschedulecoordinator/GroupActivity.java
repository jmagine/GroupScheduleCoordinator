package com.example.android.groupschedulecoordinator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<String> group_list;
    final Context c = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        lv = (ListView) findViewById(R.id.groupList);
        group_list = new ArrayList<String>();

        if(group_list != null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    group_list);

            if( lv != null ) {
                lv.setAdapter(arrayAdapter);
            }
        }




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        String groupName = extras.getString("groupName");
        setTitle(groupName);

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



                Intent intent = new Intent(GroupActivity.this, ActivityCreateGroup.class);
                intent.putStringArrayListExtra("groupList", group_list);

                startActivity(intent);
                startActivity(new Intent(GroupActivity.this, ActivityCreateGroup.class));
            }

        });
        
    }
}
