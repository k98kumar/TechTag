package com.example.kash.techtag;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ListPlayers extends BaseActivity {

    String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_players);

        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        final TextView showCode = (TextView) findViewById(R.id.codeDisplay);
        showCode.setText(getIntent().getStringExtra("GROUP_CODE"));

        setUpButtons();
    }

    private void setUpButtons() {
        Button removePlayer = (Button) this.findViewById(R.id.leaveGroup);
        removePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("groups").child(getIntent().getStringExtra("GROUP_CODE")).child(FirebaseAuth.getInstance().getCurrentUser().getEmail()).removeValue();
            }
        });
    }
}
