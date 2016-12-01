package com.example.android.groupschedulecoordinator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class AddMemberToGroup extends AppCompatActivity {
    private String groupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member_to_group);
    }

    public void addMember(View v) {
        EditText text =  (EditText) findViewById(R.id.tbMemberName);
        String memberName = text.getText().toString();
        EditText text2 = (EditText) findViewById(R.id.tbMemberEmail);
        String memberEmail = text2.getText().toString();

        if(memberName.isEmpty())
        {
            displayWarning("Please enter a valid name.");
        }
        else if(memberEmail.isEmpty())
        {
            displayWarning("Please enter a valid email.");
        }
        else
        {
            Intent intent = new Intent(AddMemberToGroup.this, GroupActivity.class);

            Bundle extras = getIntent().getExtras();
            ArrayList<String> group_list = new ArrayList<>();
            if( extras != null ) {
                group_list = extras.getStringArrayList("groupList");
                groupID = extras.getString("groupID");
            }

            if(group_list == null)
            {
                group_list = new ArrayList<>();
            }
            group_list.add(memberEmail);

            //Pass the relevant information back into GroupActivity so
            //Changes are reflected.
            intent.putStringArrayListExtra("groupList", group_list);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("member_name", memberName);
            intent.putExtra("member_email", memberEmail);
            intent.putExtra("groupID",groupID);
            intent.putExtra("calling","addMember");
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

    public void cancelMember(View v)
    {
        onBackPressed();
    }

}
