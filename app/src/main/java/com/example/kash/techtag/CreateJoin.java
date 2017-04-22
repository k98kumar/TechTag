package com.example.kash.techtag;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateJoin extends BaseActivity {

    String createOrJoin = "";
    int code;

    String currentUserEmail;

    boolean isCreate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join);

        // Get the email of the user from previous activity
        currentUserEmail = getIntent().getStringExtra("EMAIL");

        final Button selectButton = (Button) findViewById(R.id.selectButtonCreateJoin);
        final ConstraintLayout createBox = (ConstraintLayout) findViewById(R.id.createFrame);
        final TextView createView = (TextView) findViewById(R.id.createLabel);
        final ConstraintLayout joinBox = (ConstraintLayout) findViewById(R.id.joinFrame);
        final TextView joinView = (TextView) findViewById(R.id.joinLabel);
        final EditText codeInput = (EditText) findViewById(R.id.joinInput);

        codeInput.setFocusable(false);
        codeInput.setHintTextColor(Color.parseColor("#999999"));

        createBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBox.setBackground(getDrawable(R.color.loginBlue));
                createView.setTextColor(Color.parseColor("#FFFFFF"));
                joinBox.setBackground(getDrawable(R.color.white));
                joinView.setTextColor(Color.parseColor("#000000"));
                codeInput.setFocusable(false);
                // codeInput.setFocusable(true);
                // codeInput.setFocusableInTouchMode(true);
                codeInput.setTextColor(Color.parseColor("#000000"));
                createOrJoin = "create";
            }
        });

        joinBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinBox.setBackground(getDrawable(R.color.loginBlue));
                joinView.setTextColor(Color.parseColor("#FFFFFF"));
                createBox.setBackground(getDrawable(R.color.white));
                createView.setTextColor(Color.parseColor("#000000"));
                codeInput.setFocusable(true);
                codeInput.setFocusableInTouchMode(true);
                codeInput.setTextColor(Color.parseColor("#FFFFFF"));
                createOrJoin = "join";
            }
        });


        codeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinBox.setBackground(getDrawable(R.color.loginBlue));
                joinView.setTextColor(Color.parseColor("#FFFFFF"));
                createBox.setBackground(getDrawable(R.color.white));
                createView.setTextColor(Color.parseColor("#000000"));
                codeInput.setFocusable(true);
                codeInput.setFocusableInTouchMode(true);
                codeInput.setTextColor(Color.parseColor("#FFFFFF"));
                createOrJoin = "join";
            }
        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (createOrJoin) {
                    case "create" :
                        code = (int) (Math.random() * 99999999 + 1);
                        break;
                    case "join" :
                        if (codeInput.toString().equals("")) { // Rewrite what it equals
                            Intent mIntent = new Intent(CreateJoin.this, ListPlayers.class);
                            startActivity(mIntent);
                        }
                        break;
                    default :
                        break;
                }
            }
        });

    }

    // For create:
    // -Create a code
    //  OR
    // -Create a boolean and initialize to false.
    // -If createOrJoin is "create" set boolean to true.
    // -Use an intent to pass the boolean to the ListPlayers activity
    //  with intent.putExtra("CreateOrJoin", boolean);
    // -Get the boolean back with boolean b = getIntent.getExtraString("CreateOrJoin")
    // -Create a code when boolean is true in second activity
    // -Check if database already has that code
    // -If it does keep generating codes

    // For join:
    // -Create a boolean and initialize to false. (Same as create)
    // -createOrJoin is "join" so keep boolean as false.
    // -Use intent to pass boolean with intent.putExtra("CreateOrJoin", boolean) (Same as create)
    // -Get the boolean back
    // -
    // -

}
