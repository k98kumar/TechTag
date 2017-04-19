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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join);

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

        /*
        MainActivity.mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in:" + user.getUid());
                    Intent mIntent = new Intent(MainActivity.this, CreateJoin.class);
                    startActivity(mIntent);
                } else {
                    // User signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }

            }
        };
        */

    }

}
