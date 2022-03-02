package com.example.mastercheffood.Activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mastercheffood.R;
import com.example.mastercheffood.databinding.ActivityLoginPageBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hbb20.CountryCodePicker;
import static com.example.mastercheffood.Network.NetworkUtil.isNetworkAvailable;

import java.util.Arrays;

public class LoginPageActivity extends AppCompatActivity {

   ActivityLoginPageBinding binding;
   String userMobile;

    ////////////////////////////////////////////////////////////////////////////////////
    CallbackManager callbackManager;
    ////////////////////////////////////////////////////////////////////////////////////
    private static final int RC_SIGN_IN = 101;
    GoogleSignInClient mGoogleSignInClient;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mAuth = FirebaseAuth.getInstance();

        userMobile = binding.phoneNumber.getText().toString().trim();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait....");

        binding.email.setOnClickListener(Mylistener);
        binding.facebook.setOnClickListener(Mylistener);
        binding.google.setOnClickListener(Mylistener);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        checkNetwork();
        validateButton();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void checkNetwork()
    {
        if (!isNetworkAvailable(LoginPageActivity.this, true))
            return;

    }

///////////////////////////////////////////////////////////////////////////////////////////////
    private View.OnClickListener Mylistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.email:
                    if (!isNetworkAvailable(LoginPageActivity.this, true)) {
                        return;
                    }
                    else
                    {
                        loginWithEmail();
                    }
                    break;

                case R.id.facebook:
                    if (!isNetworkAvailable(LoginPageActivity.this, true)) {
                        return;
                    }
                    else
                    {
                        facebookSignup();
                    }
                    break;

                case R.id.google:
                    if (!isNetworkAvailable(LoginPageActivity.this, true)) {
                        return;
                    }
                    else
                    {
                        googleSignIn();
                    }
                    break;

                default:
                    break;
            }
        }
    };
////////////////////////////////////////////////////////////////////////////////////////////////

    private void validateButton()
    {
        binding.phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString().trim()) && s.toString().trim().length() == 10)
                {
                    userMobile = s.toString().trim();
                    binding.validateButton.setEnabled(true);
                }
                else
                    binding.validateButton.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable(LoginPageActivity.this, true)) {
                    return;
                }
                else
                {
                    validateUser();
                }
            }
        });
    }

    private void validateUser()
    {
        binding.ccp.registerCarrierNumberEditText(binding.phoneNumber);

        Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);
        intent.putExtra("mobile", binding.ccp.getFullNumberWithPlus().trim());
        binding.phoneNumber.setText("");
        startActivity(intent);
        finish();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void loginWithEmail()
    {
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setAndroidPackageName(getPackageName(),
                        true, /* install if not available? */
                        null   /* minimum app version */)
                .setHandleCodeInApp(true)
                .setUrl("https://mastercheffood.com/finishSignUp")
                .build();

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().enableEmailLinkSignIn().setActionCodeSettings(actionCodeSettings).build()))
                        .build(), 1234);

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void facebookSignup()
    {
        progressDialog.show();
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        handleFacebookAccessToken(loginResult.getAccessToken());

                    }

                    @Override
                    public void onCancel() {
                        // App code
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "sign in fails", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "sign in fails", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token)
    {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "sign in fails"+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void googleSignIn()
    {
        progressDialog.show();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void updateUI(FirebaseUser user)
    {
        progressDialog.dismiss();
        startActivity(new Intent(LoginPageActivity.this, HomePageActivity.class));
        finish();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {
                // Google Sign In was successful, authenticate with Firebase

                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());

            }
            catch (ApiException e)
            {
                // Google Sign In failed, update UI appropriately
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "sign in fails" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        if (callbackManager != null)
        {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(),HomePageActivity.class));
            finish();
        }
    }

}