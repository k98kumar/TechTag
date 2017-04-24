package com.example.kash.techtag;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateJoin extends BaseActivity implements View.OnClickListener {

    String createOrJoin = "";
    String code;

    String currentUserEmail;

    boolean isCreate = false;

    EditText codeInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join);

        // Get the email of the user from previous activity
        currentUserEmail = getIntent().getStringExtra("EMAIL");

        final Button selectButton = (Button) findViewById(R.id.selectButtonCreateJoin);
        final TextView createView = (TextView) findViewById(R.id.createLabel);
        final TextView joinView = (TextView) findViewById(R.id.joinLabel);
        codeInput = (EditText) findViewById(R.id.createJoinInput);

        codeInput.setFocusable(false);
        codeInput.setFocusable(true);
        codeInput.setFocusableInTouchMode(true);
        codeInput.setHintTextColor(Color.parseColor("#999999"));
        codeInput.setTextColor(Color.parseColor("#FFFFFF"));

        createView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createView.setBackground(getDrawable(R.color.loginBlue));
                createView.setTextColor(Color.parseColor("#FFFFFF"));
                joinView.setBackground(getDrawable(R.color.white));
                joinView.setTextColor(Color.parseColor("#000000"));
                codeInput.setFocusable(false);
                codeInput.setFocusable(true);
                codeInput.setFocusableInTouchMode(true);
                codeInput.setTextColor(Color.parseColor("#000000"));
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
                codeInput.setFocusable(false);
                codeInput.setFocusable(true);
                codeInput.setFocusableInTouchMode(true);
                codeInput.setTextColor(Color.parseColor("#FFFFFF"));
                createOrJoin = "join";
            }
        });

    }

    /**
     * Create a helper function that returns a boolean
     * indicating whether the database contains the
     * code of the group trying to be created or joined
     * @return
     */
    public boolean inDatabase(String possibleCode) {
        
    }

    @Override
    public void onClick(View v) {
        // Intent mIntent = new Intent(CreateJoin.this, ListPlayers.class);
        int i = v.getId();
        if (i == R.id.selectButtonCreateJoin) {
            switch (createOrJoin) {
                case "create" :
                    Intent mCreateIntent = new Intent(CreateJoin.this, ListPlayers.class);
                    mCreateIntent.putExtra("CREATE_OR_JOIN", isCreate);
                    break;
                case "join" :
                    if (codeInput.toString().equals("")) { // Rewrite what it equals
                        Intent mJoinIntent = new Intent(CreateJoin.this, ListPlayers.class);
                        startActivity(mJoinIntent);
                    }
                    break;
                default :
                    break;
            }
        }
    }

}
