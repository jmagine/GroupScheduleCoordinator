package com.example.android.groupschedulecoordinator;

/**
 * Created by jeremyfolk on 10/17/16.
 * Firebase auth added by tommy 10/27/16
 */


import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.*;


public class LoginScreen extends AppCompatActivity{

    //global var
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "SignInActivity";
    private final int REQUEST_CODE_PERMISSIONS = 123;
    private final int REQUEST_CODE_MULTI = 124;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mFirebaseUser;

    private int permissionCode;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton mGoogleButton;
    private String mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Create the relevant instances for authenticating with google
        mGoogleButton = (SignInButton) findViewById(R.id.sign_in_button);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(LoginScreen.this, MainActivity.class));
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginScreen.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dummyPermission();
                //signIn();
                multiPermission();
            }
        });
        mGoogleButton.setSize(SignInButton.SIZE_WIDE);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /*
        Handles the authentication if sign in arguments are valid
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO remove this log
        Log.d("jasonlogs", "server_client_id     : " + this.getResources().getString(R.string.server_client_id));
        Log.d("jasonlogs", "default_web_client_id: " + getApplicationContext().getString(R.string.default_web_client_id));
        Log.d("jasonLogs", "onActivityResult: " + requestCode + " " + resultCode + " " + data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("jasonlogs", "result: " + result.getStatus());

            if (result.isSuccess()) {
                //TODO remove this log
                Log.d("jasonlogs", "successful google auth!");
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                //TODO remove this log
                Log.d("jasonlogs", "failed google auth.");


                // Google Sign In failed, update UI appropriately
                // ...

                //TODO don't actually go forwards until auth is completed
                //startActivity(new Intent(LoginScreen.this, MainActivity.class));
                //finish();
            }
        }
    }

    /*
        Authenticate sign in credentials with Firebase and update data on Firebase
        Sends user to MainActivity on success
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginScreen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else{
                            startActivity(new Intent(LoginScreen.this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /*
        Prevent users's from accidently returning to the MainActivity upon logout
     */
    @Override
    public void onBackPressed() {
    }

    /* Prompts user for access
        Popup dialog prompts user that contacts access is needed
        @success, user allows contacts, signIn() is called
        @failure, user denies, toast text shows
     */
    private void dummyPermission(){
        int dummyPermission = checkSelfPermission(Manifest.permission.GET_ACCOUNTS);
        if (dummyPermission != PackageManager.PERMISSION_GRANTED) {
            if(shouldShowRequestPermissionRationale(Manifest.permission.GET_ACCOUNTS)){
                needPermissions("You need to allow access to Contacts", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(new String[] {Manifest.permission.GET_ACCOUNTS}, REQUEST_CODE_PERMISSIONS);
                    }
                });
                return;
            }
            requestPermissions(new String[] {Manifest.permission.GET_ACCOUNTS}, REQUEST_CODE_PERMISSIONS);
            return;
        }
        signIn();
    }

    /*
        Requests permissions to access CONTACTS and CALENDER
        case 1: user allows both permissions --> call signIn()
        case 2: user denies 1 option --> stays on main screen and prompts user for access
                to the permission that was denied
        case 3: user denies both options --> prompts for both
     */
    private void multiPermission(){
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionList = new ArrayList<>();
        if(!addPermissions(permissionList, Manifest.permission.GET_ACCOUNTS))
            permissionsNeeded.add("ACCOUNTS");
        if (!addPermissions(permissionList,Manifest.permission.READ_CALENDAR))
            permissionsNeeded.add("READ_CALENDER");
        if(!addPermissions(permissionList, Manifest.permission.WRITE_CALENDAR))
            permissionsNeeded.add("WRITE_CALENDER");

        if (permissionList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = "You need to provide access to " + permissionsNeeded.get(0);
                for(int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                needPermissions(message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(permissionList.toArray(new String[permissionList.size()]), REQUEST_CODE_MULTI);
                    }
                });
                return;
            }
            requestPermissions(permissionList.toArray(new String[permissionList.size()]), REQUEST_CODE_MULTI);
            return;
        }
        signIn();
    }

    /*helper for adding permissions*/
    private boolean addPermissions(List<String> pList, String permission){
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            pList.add(permission);
            if(shouldShowRequestPermissionRationale(permission)){
                return false;
            }
        }
        return true;
    }

    private void needPermissions(String message, DialogInterface.OnClickListener okListener){
        new AlertDialog.Builder(LoginScreen.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }

    /*
        Helper to check for valid permissions and checking if there were granted.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    signIn();
                }else{
                    Toast.makeText(LoginScreen.this, "Please allow access to specified permissions", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_CODE_MULTI:
                Map<String,Integer> perms = new HashMap<String,Integer>();
                perms.put(Manifest.permission.GET_ACCOUNTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_CALENDAR, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CALENDAR, PackageManager.PERMISSION_GRANTED);

                for(int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if((perms.get(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED)
                        && (perms.get(Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED)
                        && (perms.get(Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED)){
                    signIn();
                }else{
                    Toast.makeText(LoginScreen.this, "Please provide the necessary permissions", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
