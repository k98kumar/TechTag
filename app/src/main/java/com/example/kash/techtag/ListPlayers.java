package com.example.kash.techtag;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ListPlayers extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_players);

        final TextView showCode = (TextView) findViewById(R.id.codeDisplay);
        showCode.setText(getIntent().getStringExtra("GROUP_CODE"));
    }

}
