package com.example.mastercheffood.Activities;

import static com.example.mastercheffood.Network.NetworkUtil.isNetworkAvailable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mastercheffood.R;
import com.example.mastercheffood.databinding.ActivityVerifyOtpactivityBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOTPActivity extends AppCompatActivity {

    ActivityVerifyOtpactivityBinding binding;

    TextView userNo;
    EditText enterOTPField;
    Button verifyOTP,countdown,resendOTP;
    String verificationId, userPhoneNumber;
    FirebaseAuth fAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    PhoneAuthProvider.ForceResendingToken token;
    CountDownTimer countDownTimer;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_verify_otpactivity);

        fAuth = FirebaseAuth.getInstance();

        userNo = (TextView) findViewById(R.id.user_mobile_number);
        enterOTPField = (EditText) findViewById(R.id.otpedittext);
        countdown = (Button) findViewById(R.id.timer);
        verifyOTP = (Button) findViewById(R.id.verifyotp);
        resendOTP = (Button) findViewById(R.id.btnResendOtp);


        userPhoneNumber = getIntent().getStringExtra("mobile").toString();
        userNo.setText(userPhoneNumber);

        validateMobileNumber();
        verifyPhoneNumber(userPhoneNumber);

        startCountDownTimer(countdown);

        checkNetwork();

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        verifyOTP.setOnClickListener(myListener);
        resendOTP.setOnClickListener(myListener);

    }

    private void checkNetwork()
    {
        if (!isNetworkAvailable(VerifyOTPActivity.this, true))
            return;
    }

    public View.OnClickListener  myListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.verifyotp:

                    if (enterOTPField.getText().toString().isEmpty()) {
                        enterOTPField.setError("Enter OTP First");
                        return;
                    }
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, enterOTPField.getText().toString());
                    authenticateUser(credential);
                    break;

                case R.id.btnResendOtp:
                    enterOTPField.setText("");
                    countdown.setVisibility(View.VISIBLE);
                    startCountDownTimer(countdown);
                    progressDialog.show();
                    validateMobileNumber();
                    verifyPhoneNumber(userPhoneNumber);
                    Toast.makeText(getApplicationContext(), "Clicked on Resend Otp", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;

            }
        }
    };


    private void validateMobileNumber()
    {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                authenticateUser(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.cancel();

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                token = forceResendingToken;
                progressDialog.cancel();

            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);


            }
        };
    }


    public void verifyPhoneNumber(String phoneNum) {

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(fAuth)
                .setActivity(this)
                .setPhoneNumber(phoneNum)
                .setTimeout(90L, TimeUnit.SECONDS)
                .setCallbacks(callbacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    public void authenticateUser(PhoneAuthCredential credential)
    {
        fAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Enter Wrong Verification Code", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void startCountDownTimer(final Button btnResend) {

        if (countDownTimer != null)
            countDownTimer.cancel();

        countDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                countdown.setTextColor(Color.BLACK);
                countdown.setText("0:" + millisUntilFinished / 1000);
                resendOTP.setEnabled(false);
            }

            public void onFinish() {
                countdown.setVisibility(View.GONE);
                resendOTP.setEnabled(true);
            }
        }.start();
    }
}