package com.example.mastercheffood.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.mastercheffood.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int screen_timeout = 2000;
    String TAG = "EMAILING_AUTH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        signInWithEmail();
    }

    private void signInWithEmail() {

        if (AuthUI.canHandleIntent(getIntent())) {
            String link = getIntent().getData().toString();

            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build());

            Log.d(TAG, "got an btnEmail link: " + link);

            if (link != null) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setEmailLink(link)
                                .setAvailableProviders(providers)
                                .build(),
                        12345);
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), LoginPageActivity.class));
                    finish();
                }
            }, screen_timeout);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12345) {
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(getApplicationContext(), "Successfully signed in!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Sign in FAILED", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginPageActivity.class));
                finish();
            }

        }
    }
}