package com.example.kash.techtag;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import java.util.List;
import java.util.Map;

/*
 * Copyright 2017  Koushhik Kumar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

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
    protected void onStop() {
        mDatabase.child(GROUPS).child(enteredCode).child(USERS).child(currentUserUID).removeValue();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        mDatabase.child(GROUPS).child(enteredCode).child(USERS).child(currentUserUID).removeValue();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {
            openInfo();
            return true;
        }
        if (id == R.id.action_sign_out) {
            mDatabase.child(GROUPS).child(enteredCode).child(USERS).child(currentUserUID).removeValue();
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Provides functionality to the start game button
     */
    private void setUpButtons() {
        final Button startGame = (Button) this.findViewById(R.id.startGame);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(GROUPS).child(enteredCode).child(BUTTON).setValue("pressed");
                getStatusValue(enteredCode);
            }
        });
    }

    /**
     * Separate thread that handles onChildAdded and
     * onChildRemoved methods inside addChildEventListener
     */
    private void runThroughout() {
        // mDatabase.addListenerForSingleValueEvent(new ChildEventListener() {
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
                // getStatusValue(enteredCode);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                /*
                if (dataSnapshot.child(STATUS).getValue().equals("start")) {
                    Intent startStatusIntent = new Intent(ListPlayers.this, MapLocations.class);
                    ListPlayers.this.startActivity(startStatusIntent);
                }
                */
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
        // mDatabase.child(GROUPS).child(code).addValueEventListener(new ValueEventListener() {
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
