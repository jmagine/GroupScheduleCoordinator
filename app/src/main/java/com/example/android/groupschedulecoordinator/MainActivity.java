package com.example.android.groupschedulecoordinator;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.groupList);

        List<String> group_list = new ArrayList<String>();
        group_list.add("foo");
        group_list.add("bar");

//        Bundle extras = getIntent().getExtras();
//        if(extras != null)
//        {
//            String groupName = extras.getString("groupName");
//            group_list.add(groupName);
//        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                group_list);

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = ((TextView) view).getText().toString();

                Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, Main0Activity.class));
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.myFab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                android.content.Context context = getApplicationContext();
//                CharSequence text = "Text!";
//                int duration = Toast.LENGTH_SHORT;
//
//                Toast toast = Toast.makeText(context, text, duration);
//                toast.show();

                startActivity(new Intent(MainActivity.this, ActivityCreateGroup.class));
            }

        });



//    btn = (Button)findViewById(R.id.button);
//
//    btn.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        startActivity(new Intent(MainActivity.this, Main0Activity.class));
//      }
//    });
    }

}
