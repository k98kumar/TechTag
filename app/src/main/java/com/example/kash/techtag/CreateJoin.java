package com.example.kash.techtag;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class CreateJoin extends BaseActivity implements View.OnClickListener {

    String createOrJoin = "";
    String groupName;
    String currentUserEmail;
    String currentUserUID;

    User user;

    EditText nameInput;
    TextView mStatusOutput;
    String hello;

    String enteredCode = "";

    private static final String GROUPS = "groups";
    private static final String USERS = "users";

    boolean inUse;

    TextView createView;
    TextView joinView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join);

        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        user = new User(currentUserEmail, currentUserUID, 0, 0, "not tagged");
        hello = "hello";

        createView = (TextView) findViewById(R.id.createLabel);
        joinView = (TextView) findViewById(R.id.joinLabel);
        mStatusOutput = (TextView) findViewById(R.id.statusOutputCreateJoin);
        nameInput = (EditText) findViewById(R.id.createJoinInput);

        mStatusOutput.setText("");

        setupClicks();

    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View v) {
        Intent listPlayersIntent = new Intent(CreateJoin.this, ListPlayers.class);
        int i = v.getId();
        if (i == R.id.selectButtonCreateJoin) {
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
            enteredCode = nameInput.getText().toString();
            /*
            if (codeInUse(mDatabase, enteredCode)) {
                Log.d("CREATE", "codeInUse");
                mStatusOutput.setText(R.string.code_in_use);
                mStatusOutput.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mStatusOutput.setText("");
                    }
                }, 4000);
            } else {
                Log.d("CREATE", "codeNotInUse");
                // mDatabase.child(GROUPS).child(enteredCode).child(user.uid).setValue(user);
                mDatabase.child(GROUPS).child(enteredCode).child(USERS).child(user.uid).setValue(user);
                listPlayersIntent.putExtra("GROUP_CODE", enteredCode);
                CreateJoin.this.startActivity(listPlayersIntent);
            } */
            switch (createOrJoin) {
                case "create":
                    if (codeInUse(mDatabase, enteredCode)) {
                        Log.d("CREATE", "codeInUse");
                        mStatusOutput.setText(R.string.code_in_use);
                        mStatusOutput.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mStatusOutput.setText("");
                            }
                        }, 4000);
                    } else {
                        Log.d("CREATE", "codeNotInUse");
                        mDatabase.child(GROUPS).child(enteredCode).child(USERS).child(user.uid).setValue(user);
                        listPlayersIntent.putExtra("GROUP_CODE", enteredCode);
                        CreateJoin.this.startActivity(listPlayersIntent);
                    }
                    break;
                case "join":
                    if (codeInUse(mDatabase, enteredCode)) {
                        Log.d("JOIN", "codeInUse");
                        // mDatabase.child(GROUPS).child(enteredCode).child(USERS).child(user.uid).setValue(user);
                        mDatabase.child(GROUPS).child(enteredCode).child(USERS).child(user.uid).runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData) {
                                mutableData.setValue(currentUserEmail);
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                Log.d("onComplete", b + "");
                            }
                        });
                        listPlayersIntent.putExtra("GROUP_CODE", enteredCode);
                        CreateJoin.this.startActivity(listPlayersIntent);
                    } else {
                        Log.d("JOIN", "codeNotInUse");
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
    }

    /**
     * Helper function that returns a boolean indicating whether the
     * database contains the code of the group trying to created or joined.
     * @param dataRef       DatabaseReference argument
     * @param codeToCheck   Name of group user tries to create or join
     * @return  boolean stating if database already contains group name
     */
    private boolean codeInUse(DatabaseReference dataRef, final String codeToCheck) {
        inUse = false;
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child(GROUPS).hasChild(codeToCheck)) {
                    inUse = true;
                    Log.d("inUse", "setToTrue");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        /*
        while (!isFinished.equals("finished")) {
            try {
                wait(100);
                Log.d("Waited", ".1 Seconds");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } */
        return inUse;
    }

    private void setupClicks() {
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

}
