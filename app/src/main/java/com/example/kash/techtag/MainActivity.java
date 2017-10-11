package com.example.kash.techtag;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/*
 * Copyright 2017  Koushhik Kumar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

public class MainActivity extends BaseActivity {

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

        mStatusView = (TextView) findViewById(R.id.statusOutputMain);
        mEmailInput = (EditText) findViewById(R.id.emailInput);
        mPasswordInput = (EditText) findViewById(R.id.passwordInput);

        mStatusView.setText("");

        mAuth = FirebaseAuth.getInstance();

        setUpAuthListener();
        setUpButtons();
    }

    /**
     * Sets the action that happens when app is opened
     */
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * Sets the action that happens when app is closed
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * When sign out button is pressed in
     * onCreateOptionsMenu, MainActivity starts.
     * To prevent user from going to a signed in screen
     * after signing out, back button is disabled.
     */
    @Override
    public void onBackPressed() {
    }

    /**
     * Creates accounts for users not already
     * authenticated. If the email is already used
     * (stored in the database), an error message
     * is displayed.
     * @param email     Email the user inputs into
     *                  the email field.
     *                  (can only be used once)
     * @param password  Password the user inputs
     *                  into the password field.
     *                  (can be used repeatedly)
     */
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
                            MainActivity.this.startActivity(mIntent);
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

    private void setUpAuthListener() {
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

                    // Intent
                    Intent mIntent = new Intent(MainActivity.this, CreateJoin.class);
                    /* Pass info through intent
                       mIntent.putExtra("EMAIL", emailPass);
                       mIntent.putExtra("UID", uidPass); */
                    // start Intent
                    MainActivity.this.startActivity(mIntent);
                } else {
                    // User signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }

            }
        };
    }

    /**
     * Sign in method that allows previously
     * authenticated users to use the app without
     * creating another login.
     * @param email     takes the email user puts
     *                  in to check if valid
     * @param password  checks user's password to
     *                  find if valid
     */
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

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in is successful, open another activity
                        if (task.isSuccessful()) {
                            Intent createJoinIntent = new Intent(MainActivity.this, CreateJoin.class);
                            createJoinIntent.putExtra("EMAIL", email);
                            MainActivity.this.startActivity(createJoinIntent);
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
                    }
                });
    }

    private void setUpButtons() {
        Button createAccountButton = (Button) this.findViewById(R.id.createAccountButton);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(mEmailInput.getText().toString(), mPasswordInput.getText().toString());
            }
        });

        Button signInButton = (Button) this.findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(mEmailInput.getText().toString(), mPasswordInput.getText().toString());
            }
        });
    }
}
