package com.example.kash.techtag;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
    To delete a complete node (if ever needed)
    @Override
    public void onClick(View v) {
        mDatabase.child("groups").child(getIntent().getStringExtra("GROUP_CODE")).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.setValue(null);
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
            }
        });
    }
 */

public class BaseActivity extends AppCompatActivity {

    static FirebaseAuth mAuth;
    static FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mDatabase;

    private static final String TAG = "AUTH";

    void signOut() {
        Log.d(TAG, "signOut: Complete");
        mAuth.signOut();
        Intent mIntent = new Intent(this, MainActivity.class);
        startActivity(mIntent);
        Log.d(TAG, "signOut: Should start login screen activity");
    }

    void openInfo() {
        Intent mIntent = new Intent(this, Instructions.class);
        startActivity(mIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        /*
        PowerManager mgr = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyWakeLock");
        wakeLock.acquire();
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
