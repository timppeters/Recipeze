package com.group2.recipeze.ui.login;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import com.group2.recipeze.MainActivity;
import com.group2.recipeze.R;
import com.group2.recipeze.ui.SplashActivity;
import com.group2.recipeze.ui.login.LoginViewModel;
import com.group2.recipeze.ui.login.LoginViewModelFactory;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        Button emailBtn = this.findViewById(R.id.emailBtn);
        Button googleBtn = this.findViewById(R.id.googleBtn);
        Button facebookBtn = this.findViewById(R.id.facebookBtn);

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.login("test2");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.login("test2");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.login("test2");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Your own Activity code
    }

    /*private  callback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {
            LoggedInUserView model = new LoggedInUserView("Test");
            updateUiWithUser(model);

            setResult(Activity.RESULT_OK);
            //Complete and destroy login activity once successful
            finish();
        }

        @Override
        public void onCanceled() {
            showLoginFailed(android.R.string.cancel);
        }

        @Override
        public void onError(LockException error){
            showLoginFailed(android.R.string.httpErrorBadUrl);
        }
    };*/

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getUsername();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}