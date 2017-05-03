package com.example.kash.techtag;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;

public class ListPlayers extends BaseActivity {

    String currentUserEmail;
    String currentUserUID;
    String enteredCode;

    private static final String GROUPS = "groups";
    private static final String USERS = "users";
    private static final String STATUS = "status";
    private static final String BUTTON = "button";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_players);

        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        enteredCode = getIntent().getStringExtra("GROUP_CODE");

        final TextView showCode = (TextView) findViewById(R.id.codeDisplay);
        showCode.setText(enteredCode);

        setUpButtons();
        runThroughout();
    }

    @Override
    public void onResume() {
        runThroughout();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        // mDatabase.child(GROUPS).child(enteredCode).child(currentUserUID).removeValue();
        mDatabase.child(GROUPS).child(enteredCode).child(USERS).child(currentUserUID).removeValue();
        super.onBackPressed();
    }

    /**
     * Provides functionality to the start game button
     */
    private void setUpButtons() {
        final Button startGame = (Button) this.findViewById(R.id.startGame);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hello", mDatabase.child(GROUPS).child(enteredCode).child(STATUS).toString());
                mDatabase.child(GROUPS).child(enteredCode).child(BUTTON).setValue("pressed");
                getStatusValue(enteredCode);
                /*
                if (mDatabase.child(GROUPS).child(enteredCode).child(STATUS).equals("ready")) {
                    mDatabase.child(GROUPS).child(enteredCode).child(STATUS).setValue("start");
                    Intent startGameIntent = new Intent(ListPlayers.this, MapLocations.class);
                    startGameIntent.putExtra("GROUP_CODE", enteredCode);
                    ListPlayers.this.startActivity(startGameIntent);
                }
                */
            }
        });
    }

    /**
     * Separate thread that handles onChildAdded and
     * onChildRemoved methods inside addChildEventListener
     */
    private void runThroughout() {
        // mDatabase.addChildEventListener(new ChildEventListener() {
        mDatabase.child(GROUPS).child(enteredCode).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!dataSnapshot.hasChild(STATUS)) {
                    mDatabase.child(GROUPS).child(enteredCode).child(STATUS).setValue("");
                    mDatabase.child(GROUPS).child(enteredCode).child(BUTTON).setValue("");
                }
                if (dataSnapshot.getChildrenCount() == 4) {
                    mDatabase.child(GROUPS).child(enteredCode).child(STATUS).setValue("ready");
                }
                getStatusValue(enteredCode);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child(USERS).exists()) {
                    mDatabase.child(GROUPS).child(enteredCode).removeValue();
                } else if (dataSnapshot.child(USERS).getChildrenCount() < 4) {
                    mDatabase.child(GROUPS).child(enteredCode).child(STATUS).setValue("");
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Updates the RecyclerView to display all the emails
     * of people using the same code as the user
     */
    private void hello() {
        // ArrayList<String> helloList = (ArrayList<String>) mDatabase.child(GROUPS).child(enteredCode).child(USERS);
    }

    /**
     * Run the hello method in another
     */
    private void anotherThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                hello();
            }
        });
    }

    /**
     * Checks if new activity can be started
     * @param code  entered code from the previous screen
     */
    private void getStatusValue(final String code) {
        mDatabase.child(GROUPS).child(code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(STATUS).getValue().toString().equals("ready")) {
                    mDatabase.child(GROUPS).child(enteredCode).child(STATUS).setValue("start");
                    Intent startGameIntent = new Intent(ListPlayers.this, MapLocations.class);
                    startGameIntent.putExtra("GROUP_CODE", enteredCode);
                    ListPlayers.this.startActivity(startGameIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
