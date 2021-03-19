package com.group2.recipeze.data;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.group2.recipeze.MainActivity;
import com.group2.recipeze.data.model.LoggedInUser;
import com.group2.recipeze.data.services.UserService;
import com.group2.recipeze.ui.login.LoginActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;
    protected Retrofit retrofit;
    protected Gson gson;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user;

    private UserService service;

    // private constructor : singleton access
    private LoginRepository() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://recipeze-backend.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gson = new Gson();
        service = retrofit.create(UserService.class);
        user = new LoggedInUser();
    }

    public static LoginRepository getInstance() {
        if (instance == null) {
            instance = new LoginRepository();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user.getUsername() != null;
    }

    /**
     * Log out
     * @param context The current context. Get via Context.getApplicationContext()
     */
    public void logout(Context context) {
        user = new LoggedInUser();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    // get profile based on token and go to next screen
    public void getProfileFromServer(LoginActivity loginActivity) {
        UserRepository userRepository = UserRepository.getInstance();
        MutableLiveData<Boolean> got_profile = new MutableLiveData<>();
        got_profile.observe(loginActivity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Intent intent = new Intent(loginActivity, MainActivity.class);
                loginActivity.startActivity(intent);
                loginActivity.finish();
            }
        });
        userRepository.getPrivateProfile(got_profile);

    }

    public LoggedInUser getUser() {
        return user;
    }

    public void tokenSignin(String token, String type, MutableLiveData<Response<JsonElement>> resultingJSON) {
        Call<JsonElement> result = service.tokenSignIn(token, type);
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull retrofit2.Response<JsonElement> response) {
                resultingJSON.postValue(response);
            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error LoginRepository tokenSignin");
                System.out.println(t.getMessage());
            }
        });
    }

    public void setUsername(String username, MutableLiveData<String> newToken) {
        Call<JsonElement> result = service.setUsername(username, user.getToken());
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    String token = response.body().getAsJsonObject().get("token").getAsString();
                    newToken.postValue(token);

                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

    public void sendLoginEmail(String email, MutableLiveData<Boolean> emailSent) {
        Call<JsonElement> result = service.requestTokenEmail(email);
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    emailSent.postValue(Boolean.TRUE);

                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                emailSent.postValue(Boolean.FALSE);
                System.out.println("error");
            }
        });
    }
}