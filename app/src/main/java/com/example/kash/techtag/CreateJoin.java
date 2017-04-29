package com.example.kash.techtag;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class CreateJoin extends BaseActivity implements View.OnClickListener {

    String createOrJoin = "";
    String groupName;

    String currentUserEmail;

    boolean isCreate = false;

    EditText nameInput;
    TextView mStatusOutput;

    private static final String GROUPS = "groups";

    boolean inUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join);

        // Get the email of the user from previous activity
        // currentUserEmail = getIntent().getStringExtra("EMAIL");
        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        final Button selectButton = (Button) findViewById(R.id.selectButtonCreateJoin);
        final TextView createView = (TextView) findViewById(R.id.createLabel);
        final TextView joinView = (TextView) findViewById(R.id.joinLabel);
        mStatusOutput = (TextView) findViewById(R.id.statusOutputCreateJoin);
        nameInput = (EditText) findViewById(R.id.createJoinInput);

        mStatusOutput.setText("");

        nameInput.setFocusable(false);
        nameInput.setFocusable(true);
        nameInput.setFocusableInTouchMode(true);

        createView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createView.setBackground(getDrawable(R.color.loginBlue));
                createView.setTextColor(Color.parseColor("#FFFFFF"));
                joinView.setBackground(getDrawable(R.color.white));
                joinView.setTextColor(Color.parseColor("#000000"));
                nameInput.setFocusable(false);
                nameInput.setFocusable(true);
                nameInput.setFocusableInTouchMode(true);
                createOrJoin = "create";
            }
        });

        joinView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinView.setBackground(getDrawable(R.color.loginBlue));
                joinView.setTextColor(Color.parseColor("#FFFFFF"));
                createView.setBackground(getDrawable(R.color.white));
                createView.setTextColor(Color.parseColor("#000000"));
                nameInput.setFocusable(false);
                nameInput.setFocusable(true);
                nameInput.setFocusableInTouchMode(true);
                createOrJoin = "join";
            }
        });

    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View v) {
        Intent listPlayersIntent = new Intent(CreateJoin.this, ListPlayers.class);
        int i = v.getId();
        if (nameInput.getText().toString().equals("")) {
            mStatusOutput.setText(R.string.code_field_empty);
            mStatusOutput.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mStatusOutput.setText("");
                }
            }, 4000);
            return;
        }
        if (i == R.id.selectButtonCreateJoin) {
            String enteredCode = nameInput.getText().toString();
            switch (createOrJoin) {
                case "create":
                    if (codeInUse(mDatabase, enteredCode)) {
                        mStatusOutput.setText(R.string.code_in_use);
                        mStatusOutput.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mStatusOutput.setText("");
                            }
                        }, 4000);
                    } else {
                        mDatabase.child(GROUPS).child(enteredCode).push().setValue(currentUserEmail);
                        listPlayersIntent.putExtra("GROUP_CODE", enteredCode);
                        CreateJoin.this.startActivity(listPlayersIntent);
                    }
                    break;
                case "join":
                    if (codeInUse(mDatabase, enteredCode)) {
                        mDatabase.child(GROUPS).child(enteredCode).push().setValue(currentUserEmail);
                        listPlayersIntent.putExtra("GROUP_CODE", enteredCode);
                        CreateJoin.this.startActivity(listPlayersIntent);
                    } else {
                        mStatusOutput.setText(R.string.code_not_in_use);
                        mStatusOutput.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mStatusOutput.setText("");
                            }
                        }, 4000);
                    }
                    break;
                default:
                    mStatusOutput.setText(R.string.option_not_selected);
                    mStatusOutput.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mStatusOutput.setText("");
                        }
                    }, 4000);
                    break;
            }
        }
        if (i == R.id.button2) {
            Intent tempIntent = new Intent(CreateJoin.this, MapLocations.class);
            Log.d("Intent", "received");
            startActivity(tempIntent);
        }
    }

    /**
     * Helper function that returns a boolean indicating whether the
     * database contains the code of the group trying to created or joined.
     * @param dataRef       DatabaseReference argument
     * @param codeToCheck   Name of group user tries to create or join
     * @return  boolean stating if database already contains group name
     */
    public boolean codeInUse(DatabaseReference dataRef, final String codeToCheck) {
        Log.d("valueEventListener", "Entered");
        inUse = false;
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child(GROUPS).hasChild(codeToCheck)) {
                    inUse = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return inUse;
    }
}
