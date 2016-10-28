package com.example.android.groupschedulecoordinator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn;
    ListView lv;
    ArrayList<String> group_list;
    final Context c = this;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.groupList);

          group_list = new ArrayList<String>();
          group_list.add("Test Group");
  //        group_list.add("bar");

        final Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
//            String groupName = extras.getString("groupName");
//            if(groupName != null) {
//                group_list.add(groupName);
//            }
            group_list = extras.getStringArrayList("groupList");
        }

        if(group_list != null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    group_list);

            lv.setAdapter(arrayAdapter);
        }




        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String groupName = (String) lv.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, GroupActivity.class);
                intent.putExtra("groupName", groupName);
                startActivity(intent);
            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.myFab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_popup, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);

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

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();

//                Intent intent = new Intent(MainActivity.this, ActivityCreateGroup.class);
//                intent.putStringArrayListExtra("groupList", group_list);
//
//                startActivity(intent);
                //startActivity(new Intent(MainActivity.this, ActivityCreateGroup.class));
            }

        });

    }

    //public

    @Override
    public void onBackPressed() {
    }


}
