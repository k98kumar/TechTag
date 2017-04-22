package com.example.kash.techtag;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

// - pass email and uid in intent.putExtra()
// -   email : intent.putExtra("EMAIL", user.getEmail());
// -   uid   : intent.putExtra("UID", user.getUid());
// - get email and uid in next activity
// -   email :
// -   uid   :

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView mStatusView;
    private EditText mEmailInput;
    private EditText mPasswordInput;

    private static final String TAG = "AUTH";

    String emailPass;
    String uidPass;

    // Create a string to store email and password
    // for use in other activities. Make sure that
    // the overridden strings get passed in the
    // sign-in and createPassword methods, within
    // the onClick and are overridden

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusView = (TextView) findViewById(R.id.statusOutput);
        mEmailInput = (EditText) findViewById(R.id.emailInput);
        mPasswordInput = (EditText) findViewById(R.id.passwordInput);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // Collect current user's email and uid
                    emailPass = user.getEmail();
                    uidPass = user.getUid();
                    // User signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in:" + user.getUid());
                    Log.d(TAG, "onAuthStateChanged: " + user.getEmail());
                    Log.d(TAG, "onAuthStateChanged: test" + user.getDisplayName());

                    // Intent
                    Intent mIntent = new Intent(MainActivity.this, CreateJoin.class);
                    // putExtras
                    mIntent.putExtra("EMAIL", emailPass);
                    mIntent.putExtra("UID", uidPass);
                    // start Intent
                    startActivity(mIntent);
                } else {
                    // User signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }

            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    // Check if finals are okay to be used
    private void createAccount(final String email, final String password) {

        Log.d(TAG, "createAccount: " + email);

        if (email.equals("") || password.equals("")) {
            mStatusView.setText(R.string.blank_input);
            mStatusView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mStatusView.setText("");
                }
            }, 4000);
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d("AccountCreation", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            // Stores the email user inputs to a string for later use
                            Intent mIntent = new Intent(MainActivity.this, CreateJoin.class);
                            // Can only store email.
                            // Uid comes after database registers email
                            mIntent.putExtra("EMAIL", email);
                            startActivity(mIntent);
                            return;
                        }

                        // If sign in fails, display a message to the user.
                        // Message disappears after 4 seconds
                        else {
                            Log.w(TAG, "createWithEmail:failed", task.getException());
                            mStatusView.setText(R.string.create_failed);
                            mStatusView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mStatusView.setText("");
                                }
                            }, 4000);
                        }

                    }
                });
    }

    // Check if finals are okay to be used
    private void signIn(final String email, final String password) {

        Log.d(TAG, "signIn:" + email);

        if (email.equals("") || password.equals("")) {
            mStatusView.setText(R.string.blank_input);
            mStatusView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mStatusView.setText("");
                }
            }, 4000);
            return;
        }

        final CountDownLatch fuckAndroid = new CountDownLatch(1);
        final ReturnObject returned = new ReturnObject(false, null, null);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in is successful, open another activity
                        if (task.isSuccessful()) {
                            returned.status = true;
                            returned.email = email;
                            returned.password = password;
                            return;
                        }

                        // If sign in fails, display a message to the user.
                        // Message disappears after 4 seconds
                        else {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            mStatusView.setText(R.string.auth_failed);
                            mStatusView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mStatusView.setText("");
                                }
                            }, 4000);
                        }
                        fuckAndroid.countDown();
                    }
                });

        if (returned.status) {
            // Stores the email user inputs to a string for later use
            finish();
            Intent mIntent = new Intent(MainActivity.this, CreateJoin.class);
            // Can only store email.
            // Uid comes after database registers email
            // Uid will be found
            mIntent.putExtra("EMAIL", returned.email);
            startActivity(mIntent);
        }
    }

    @Override
    public void onClick(View v) {int i = v.getId();
        if (i == R.id.createAccountButton) {
            createAccount(mEmailInput.getText().toString(), mPasswordInput.getText().toString());
        } else if (i == R.id.signInButton) {
            signIn(mEmailInput.getText().toString(), mPasswordInput.getText().toString());
        } else if (i == R.id.action_sign_out) {
            signOut();
        }
    }

    private class ReturnObject {

        public boolean status;
        public String email;
        public String password;

        public ReturnObject(boolean status, String email, String password) {
            this.status = status;
            this.email = email;
            this.password = password;
        }
    }
}
