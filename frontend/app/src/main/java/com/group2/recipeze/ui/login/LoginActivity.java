package com.group2.recipeze.ui.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoginStatusCallback;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.group2.recipeze.MainActivity;
import com.group2.recipeze.R;
import com.group2.recipeze.data.LoginRepository;
import com.group2.recipeze.data.model.LoggedInUser;

import java.net.URLDecoder;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN_GOOGLE = 1000;
    private static final int RC_SIGN_IN_EMAIL = 1001;
    private LoginRepository loginRepository;
    private GoogleSignInClient googleSignInClient;
    private OkHttpClient okHttpClient;
    private boolean usernameFieldValid = false;
    private boolean termsconditionsChecked = false;
    private CallbackManager fbCallbackManager;
    private LoginManager fbLoginManager;
    private boolean emailFieldValid = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        loginRepository = LoginRepository.getInstance();

        // Configure Google sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);


        // Setup Facebook callback & login managers
        FacebookSdk.sdkInitialize(getApplicationContext());
        fbCallbackManager = CallbackManager.Factory.create();
        fbLoginManager = LoginManager.getInstance();

        Button emailBtn = this.findViewById(R.id.emailBtn);
        Button googleBtn = this.findViewById(R.id.googleBtn);
        Button facebookBtn = this.findViewById(R.id.facebookBtn);
        LoginActivity thisLoginActivity = this;
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEmailScreen();
            }
        });
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithGoogle();
            }
        });
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithFacebook();
            }
        });

        okHttpClient = new OkHttpClient();
        trySilentLogin();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
            loginWithToken(URLDecoder.decode(data.getLastPathSegment()), "email");
        }

    }

    private void trySilentLogin() {

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (googleAccount != null) {
            loginWithToken(googleAccount.getIdToken(), "google");
        }

        // Check if user is already signed in with facebook
        fbLoginManager.retrieveLoginStatus(this, new LoginStatusCallback() {
            @Override
            public void onCompleted(AccessToken accessToken) {
                if (googleAccount != null) {
                    loginWithToken(accessToken.getToken(), "facebook");
                }

            }
            @Override
            public void onFailure() {
                showLoginFailed("Login Failed");
            }
            @Override
            public void onError(Exception exception) {
                exception.printStackTrace();
                showLoginFailed("Login Failed");
            }
        });

    }

    private void loginWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    private void loginWithFacebook() {
        // Login Manager callback
        fbLoginManager.registerCallback(
                fbCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        loginWithToken(loginResult.getAccessToken().getToken(), "facebook");
                    }

                    @Override
                    public void onCancel() {
                        showLoginFailed("Login Failed");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        exception.printStackTrace();
                        showLoginFailed("Login Failed");
                    }
                }
        );

        fbLoginManager.logInWithReadPermissions(
                this,
                Arrays.asList("email")
        );


    }

    /**
     * login with the token from google/fb/email
     * @param IdToken
     */
    private void loginWithToken(String IdToken, String type) {
        MutableLiveData<retrofit2.Response<JsonElement>> result = new MutableLiveData<>();
        LoginActivity thisLoginActivity = this;
        loginRepository.tokenSignin(IdToken, type, result);
        result.observe(this, new Observer<retrofit2.Response<JsonElement>>() {
            @Override
            public void onChanged(retrofit2.Response<JsonElement> jsonElementResponse) {
                try {
                    JsonObject res = jsonElementResponse.body().getAsJsonObject();
                    if (res.get("auth").getAsBoolean()) {
                        // set token
                        LoggedInUser loggedInUser = loginRepository.getUser();
                        loggedInUser.setToken(res.get("token").toString());

                        if (res.get("newAccount").getAsBoolean()) {
                            goToSetUsernameScreen();

                        } else {
                            finishLogin();
                        }
                    } else {
                        showLoginFailed("Login Failed");
                    }

                } catch (Exception e) {
                    //e.printStackTrace();
                    showLoginFailed("Login Failed");
                }
            }
        });
    }

    private void goToSetUsernameScreen() {
        // show set username screen
        // attempt to set username, if successful, set new token & loginRepository.login();
        setContentView(R.layout.fragment_setusername);
        EditText usernameField = findViewById(R.id.usernameField);
        CheckBox termsconditions = findViewById(R.id.termsconditions);
        Button setUsernameButton = findViewById(R.id.setUsernameButton);
        LoginActivity thisLoginActivity = this;


        usernameField.addTextChangedListener(new TextValidator(usernameField) {
            @Override public void validate(TextView textView, String text) {
                Pattern validUsernamePattern = Pattern.compile("[a-zA-Z0-9]{3,12}");
                Matcher matcher = validUsernamePattern.matcher(text);
                usernameFieldValid = matcher.matches();
                if (usernameFieldValid && termsconditionsChecked) {
                    setUsernameButton.setEnabled(true);
                } else {
                    setUsernameButton.setEnabled(false);
                }

            }
        });

        termsconditions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                termsconditionsChecked = isChecked;
                if (usernameFieldValid && termsconditionsChecked) {
                    setUsernameButton.setEnabled(true);
                } else {
                    setUsernameButton.setEnabled(false);
                }
            }
        });

        setUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MutableLiveData<String> result = new MutableLiveData<>();
                loginRepository.setUsername(usernameField.getText().toString(), result);
                result.observe(thisLoginActivity, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        LoggedInUser loggedInUser = loginRepository.getUser();
                        loggedInUser.setToken(s);
                        finishLogin();
                    }
                });

            }
        });
    }

    private void goToEmailScreen() {
        // Show them enter email screen
        setContentView(R.layout.fragment_emailsignin);
        EditText emailField = findViewById(R.id.emailField);
        Button sendEmailButton = findViewById(R.id.sendEmailButton);
        LoginActivity thisLoginActivity = this;

        emailField.addTextChangedListener(new TextValidator(emailField) {
            @Override public void validate(TextView textView, String text) {
                Pattern validEmailPattern = Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
                Matcher matcher = validEmailPattern.matcher(text);
                emailFieldValid = matcher.matches();
                sendEmailButton.setEnabled(emailFieldValid);
            }
        });

        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MutableLiveData<Boolean> emailSentSuccessfully = new MutableLiveData<>();
                loginRepository.sendLoginEmail(emailField.getText().toString(), emailSentSuccessfully);
                emailSentSuccessfully.observe(thisLoginActivity, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean b) {
                        if (b) {
                            setContentView(R.layout.fragment_emailsent);
                        } else {
                            showLoginFailed("Failed to send email");
                        }
                    }
                });

            }
        });


    }

    private void finishLogin() {
        loginRepository.getProfileFromServer(this);
    }

    @Override
    // login with google & facebook login intent result
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                // Signed in successfully, show authenticated UI.
                loginWithToken(account.getIdToken(), "google");
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                System.out.println("signInResult:failed code=" + e.getStatusCode());
                showLoginFailed("signInResult:failed code=" + e.getStatusCode());
            } catch (Exception e) {
                showLoginFailed(null);
            }
        }

        else if (requestCode == RC_SIGN_IN_EMAIL) {
            System.out.println("email");
        }
        else { // facebook
            fbCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Your own Activity code
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}