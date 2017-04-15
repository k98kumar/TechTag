package com.example.kash.techtag;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private TextView mStatusView;
    private EditText mEmailInput;
    private EditText mPasswordInput;
    private Button mSignInButton;
    private Button mCreateButton;

    private static final String TAG = "AUTH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusView = (TextView) findViewById(R.id.statusOutput);
        mEmailInput = (EditText) findViewById(R.id.emailInput);
        mPasswordInput = (EditText) findViewById(R.id.passwordInput);
        mSignInButton = (Button) findViewById(R.id.signInButton);
        mCreateButton = (Button) findViewById(R.id.createAccountButton);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in:" + user.getUid());
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
                            Intent mIntent = new Intent(MainActivity.this, CreateJoin.class);
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

    private void signIn(String email, String password) {

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
                            Intent mIntent = new Intent(MainActivity.this, CreateJoin.class);
                            startActivity(mIntent);
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

    private void signOut() {
        mAuth.signOut();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.createAccountButton) {
            createAccount(mEmailInput.getText().toString(), mPasswordInput.getText().toString());
        } else if (i == R.id.signInButton) {
            signIn(mEmailInput.getText().toString(), mPasswordInput.getText().toString());
        }
    }
}
