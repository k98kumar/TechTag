package com.example.kash.techtag;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class ListPlayers extends BaseActivity {

    String currentUserEmail;
    String currentUserUID;
    String enteredCode;
    String enteredCodeStarted;

    boolean twoChildren;

    private static final String GROUPS = "groups";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_players);

        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        enteredCode = getIntent().getStringExtra("GROUP_CODE");
        enteredCodeStarted = enteredCode + " - Started";

        final TextView showCode = (TextView) findViewById(R.id.codeDisplay);
        showCode.setText(enteredCode);

        twoChildren = false;

        setUpButtons();
        runEveryTime();
    }

    private void setUpButtons() {
        Button startGame = (Button) this.findViewById(R.id.startGame);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        mDatabase.child(GROUPS).child(enteredCode).child(currentUserUID).removeValue();
        Log.d("twoChildren", twoChildren + "");
        if (twoChildren) {
            mDatabase.child(GROUPS).child(enteredCode).child("status").setValue(null);
        }
        super.onBackPressed();
    }

    private void runEveryTime() {
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!dataSnapshot.child(GROUPS).child(enteredCode).hasChild("status")) {
                    mDatabase.child(GROUPS).child(enteredCode).child("status").setValue("");
                }
                if (dataSnapshot.child(enteredCode).getChildrenCount() == 5) {
                    mDatabase.child(GROUPS).child(enteredCode).child("status").setValue("start");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
