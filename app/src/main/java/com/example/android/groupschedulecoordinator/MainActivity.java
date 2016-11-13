package com.example.android.groupschedulecoordinator;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.FreeBusyCalendar;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.FreeBusyResponse;
import com.google.api.services.calendar.model.TimePeriod;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    Button btn;
    ListView grouplv;
    ArrayList<String> group_list;
    ArrayList<String> groupID_list;
    final Context c = this;
    private static final String TAG = "MainActivity";
    private static final String PREF_ACCOUNT_NAME = "accountName";

    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String[] SCOPES = { CalendarScopes.CALENDAR };

    ProgressDialog mProgress;
    private Button logout;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference mDatabase;
    private DatabaseReference mUsersReference;
    private ValueEventListener mListener;
    private GoogleAccountCredential mCredential;
    private String userName;
    private User currentUser;
    private Group tempGrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        logout = (Button) findViewById(R.id.button2);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mAuth.getInstance().signOut();
                signOut();
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
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                group_list.add(userInputDialogEditText.getText().toString());
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                        c,
                                        android.R.layout.simple_list_item_1,
                                        group_list);

                                grouplv.setAdapter(arrayAdapter);
                                addGroup(userInputDialogEditText.getText().toString());
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
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Updating database");

        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        System.out.println("NONNULL EXTRAS");
        userName = mAuth.getCurrentUser().getEmail();
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        if (userName != null) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(PREF_ACCOUNT_NAME, userName);
            editor.apply();
        }

        if(userName==null){
            Log.e("Error: ","null account name");
        }
        mCredential.setSelectedAccountName(userName);
        currentUser = new User(userName);
        System.out.println("CURRENT USER: "+userName);
        System.out.println("ATTEMPTING");
        System.out.println(mCredential.getSelectedAccountName());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        System.out.println(mDatabase.toString());
        mUsersReference = mDatabase.child("users").child(encodeEmailKey(userName));
        System.out.println(mUsersReference.toString());

        ValueEventListener dataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    // Get CustomUser object and use the values to update the UI
                    System.out.println("Data change for " + userName);
                    System.out.println("Data: "+dataSnapshot.toString());
                    User tempUser = dataSnapshot.getValue(User.class);
                    if(tempUser.getPendingGroups()==null)
                        tempUser.setPendingGroups(new HashMap<String, String>());
                    if(tempUser.getAcceptedGroups()==null)
                        tempUser.setAcceptedGroups(new HashMap<String, String>());
                    if(tempUser.getFreeTimes()!=null)
                        currentUser.setFreeTimes(tempUser.getFreeTimes());
                    currentUser.setPendingGroups(tempUser.getPendingGroups());
                    currentUser.setAcceptedGroups(tempUser.getAcceptedGroups());
                }
                else{
                    System.out.println(dataSnapshot.toString()+"Does not exist");
                    mUsersReference.setValue(currentUser);
                }
                updateGroupList();
                userUpdate();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
                // ...
            }
        };

        mUsersReference.addValueEventListener(dataListener);
        mListener = dataListener;
        System.out.println("Added listener");

    }

    private void signOut(){
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                startActivity(new Intent(MainActivity.this, LoginScreen.class));
            }
        });
    }

    private void userUpdate(){
        MakeRequestTask makeRequestTask = new MakeRequestTask(mCredential);
        try {
            makeRequestTask.execute();
        }
        catch(Exception e){
            Log.e("GetDataFromAPI",e.toString());
        }
    }

    private void addGroup(String groupName){
        tempGrp = new Group(groupName);
        tempGrp.addMember(encodeEmailKey(currentUser.getUserName()),currentUser.getUserName());
        String groupID = mDatabase.child("groups").push().getKey();
        currentUser.acceptGroup(groupID,groupName);
        mUsersReference.setValue(currentUser);
        mDatabase.child("groups").child(groupID).setValue(tempGrp);
    }

    private void updateGroupList(){

        System.out.println("Entered updateGroupList");
        grouplv = (ListView) findViewById(R.id.groupList);

        group_list = new ArrayList<String>();
        groupID_list = new ArrayList<String>();

        HashMap<String,String> acceptedMap = currentUser.getAcceptedGroups();
        Set<String> keySet = acceptedMap.keySet();
        ArrayList<String> sortedKeys = new ArrayList<String>();
        for(String i:keySet){
            sortedKeys.add(i);
        }
        Collections.sort(sortedKeys);
        System.out.println(sortedKeys);

        for(String s: sortedKeys){
            groupID_list.add(s);
            group_list.add(acceptedMap.get(s));
        }

        if(group_list != null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    group_list);
            grouplv.setAdapter(arrayAdapter);
        }

        grouplv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String groupID = groupID_list.get(position);
                Intent intent = new Intent(MainActivity.this, GroupActivity.class);
                intent.putExtra("groupID", groupID);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mUsersReference.removeEventListener(mListener);
    }

    @Override
    public void onBackPressed() {
    }


    private class MakeRequestTask extends AsyncTask<Void, Void, HashMap<String,ArrayList<Integer>>> {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Coordinatr")
                    .build();
        }

        /**
         * Background task to call Google Calendar API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected HashMap<String,ArrayList<Integer>> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of the next 10 events from the primary calendar.
         * @return List of Strings describing returned events.
         * @throws IOException
         */
        private HashMap<String,ArrayList<Integer>> getDataFromApi() throws IOException {
            HashMap<String,ArrayList<Integer>> freeTimeMap = new HashMap<String, ArrayList<Integer>>();

            //Making it so that we know the freeTimes of the user for next 2 weeks
            java.util.GregorianCalendar currentDay = new GregorianCalendar();
            java.util.GregorianCalendar rangeEnd = new GregorianCalendar();
            rangeEnd.add(java.util.Calendar.DAY_OF_MONTH,14);
            rangeEnd.set(java.util.Calendar.HOUR_OF_DAY,23);
            rangeEnd.set(java.util.Calendar.MINUTE,59);
            rangeEnd.set(java.util.Calendar.SECOND,59);
            DateTime timeMin = new DateTime(currentDay.getTime());
            DateTime timeMax = new DateTime(rangeEnd.getTime());

            //By default, sets them all to completely free days
            for(int i=0;i<15;i++) {
                String newDay = currentDay.get(java.util.Calendar.MONTH) + "-" +
                        currentDay.get(java.util.Calendar.DAY_OF_MONTH) + "-" +
                        currentDay.get(java.util.Calendar.YEAR);
                System.out.printf("Day: %s\n",newDay);
                ArrayList<Integer> fillList = new ArrayList<Integer>(48);
                for(int j=0;j<48;j++){
                    fillList.add(0);
                }
                freeTimeMap.put(newDay,fillList);
                currentDay.add(java.util.Calendar.DAY_OF_MONTH,1);
            }

            //Creates FreeBusy Request and returns the events in a list of timePeriods
            FreeBusyRequestItem requestItem = new FreeBusyRequestItem().setId("primary");
            List<FreeBusyRequestItem> listOfRequest = new ArrayList<FreeBusyRequestItem>();
            listOfRequest.add(requestItem);
            FreeBusyRequest freeBusyRequest = new FreeBusyRequest().setTimeMax(timeMax).setTimeMin(timeMin).setTimeZone("America/Los_Angeles").setItems(listOfRequest);
            Calendar.Freebusy.Query freebusy = mService.freebusy().query(freeBusyRequest);
            FreeBusyResponse busyTimes = freebusy.execute();
            Map<String,FreeBusyCalendar> calendarMap = busyTimes.getCalendars();
            List<TimePeriod> timeRanges = calendarMap.get("primary").getBusy();

            //For every time period, fill respective values in the String->ArrayList map
            for (TimePeriod time:timeRanges){
                Date startPeriod = new Date(time.getStart().getValue());
                Date endPeriod = new Date(time.getEnd().getValue());
                GregorianCalendar startPCalendar = new GregorianCalendar();
                startPCalendar.setTime(startPeriod);
                GregorianCalendar endPCalendar = new GregorianCalendar();
                endPCalendar.setTime(endPeriod);

                String day = startPCalendar.get(java.util.Calendar.MONTH) +"-"+
                        startPCalendar.get(java.util.Calendar.DAY_OF_MONTH)+"-"+
                        startPCalendar.get(java.util.Calendar.YEAR);

                ArrayList<Integer> timeBlockList = freeTimeMap.get(day);
                if(timeBlockList == null){
                    System.out.println("This shouldn't happen");
                }

                int timeBlockStart = startPCalendar.get(java.util.Calendar.HOUR_OF_DAY)*2;
                if(startPCalendar.get(java.util.Calendar.MINUTE)>30)
                    timeBlockStart++;
                int timeBlockEnd = endPCalendar.get(java.util.Calendar.HOUR_OF_DAY)*2;
                if(endPCalendar.get(java.util.Calendar.MINUTE)>0) {
                    timeBlockEnd++;
                    if(endPCalendar.get(java.util.Calendar.MINUTE)>30)
                        timeBlockEnd++;
                }

                for(int i=timeBlockStart;i<timeBlockEnd;i++){
                    timeBlockList.set(i,timeBlockList.get(i)+1);
                }

                freeTimeMap.put(day,timeBlockList);

                System.out.println(timeBlockList.toString());

                System.out.printf("%s, %s - %s \n",day,timeBlockStart,timeBlockEnd);

            }

            return freeTimeMap;
        }


        @Override
        protected void onPreExecute() {
            //mOutputText.setText("");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(HashMap<String,ArrayList<Integer>> output) {
            mProgress.dismiss();
            if (output == null || output.size() == 0) {
                //mOutputText.setText("No results returned.");
            } else {
                System.out.println(mDatabase.toString());
                currentUser.setFreeTimes(output);
                mUsersReference.setValue(currentUser);
                //mOutputText.setText(TextUtils.join("\n", output));
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.dismiss();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            TestActivity.REQUEST_AUTHORIZATION);
                } else {
                    //mOutputText.setText("The following error occurred:\n"
                    //+ mLastError.getMessage());
                }
            } else {
                //mOutputText.setText("Request cancelled.");
            }
        }
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                MainActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
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

    public String encodeEmailKey(String inString){
        return inString.replace('.',(char)0xA4);
    }

    public String decodeEmailKey(String inString){
        return inString.replace((char)0xA4,'.');
    }
}
