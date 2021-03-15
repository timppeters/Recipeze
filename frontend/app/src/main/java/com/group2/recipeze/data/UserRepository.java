package com.group2.recipeze.data;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonElement;
import com.group2.recipeze.data.model.LoggedInUser;
import com.group2.recipeze.data.model.User;
import com.group2.recipeze.data.services.UserService;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository extends Repository {

    private static volatile UserRepository instance;
    private UserService service;

    private UserRepository() {
        super();
        service = retrofit.create(UserService.class);
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    /**
     * Get the profile of the currently logged in user.
     * Sets LoggedInUser data (accessible via LoginRepository.getUser())
     */
    public void getPrivateProfile() {
        Call<JsonElement> result = service.getPrivateProfile(this.username);
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    LoggedInUser loggedInUser = gson.fromJson(response.body().getAsJsonObject(), LoggedInUser.class);
                    LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());
                    LoggedInUser currentLoggedInUser = loginRepository.getUser();

                    currentLoggedInUser.setUsername(loggedInUser.getUsername());
                    currentLoggedInUser.setBio(loggedInUser.getBio());
                    currentLoggedInUser.setEmail(loggedInUser.getEmail());
                    currentLoggedInUser.setSettings(loggedInUser.getSettings());

                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

    /**
     * Get public profile of a user.
     *
     * @param username Username
     * @param resultingUser Resulting user object will be stored here.
     */
    public void getPublicProfile(String username, MutableLiveData<User> resultingUser) {
        Call<JsonElement> result = service.getPublicProfile(username);
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    User user = gson.fromJson(response.body().getAsJsonObject(), User.class);
                    resultingUser.postValue(user);

                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

    /**
     * Update the currently logged in user's profile. Do not put any values as empty. Just use old
     * values if not updating.
     *
     * @param username new username
     * @param email new email
     * @param bio new bio
     * @param settings new settings
     */
    public void updateProfile(String username, String email, String bio, HashMap<String, ?> settings) {
        JSONObject updates = new JSONObject();
        try {
            updates.put("username", username)
                    .put("email", email)
                    .put("bio", bio)
                    .put("settings", settings);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<JsonElement> result = service.updateProfile(this.username, updates);
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    System.out.println("Updated user successfully!");

                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }


    /**
     * Deletes the user's account and logs out
     *
     */
    public void deleteAccount() {
        Call<JsonElement> result = service.deleteAccount(this.username);
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    System.out.println("Deleted user successfully!");
                    LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());
                    loginRepository.logout();

                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

    /**
     * Follow a user
     *
     * @param username User to follow
     */
    public void followUser(String username) {
        Call<JsonElement> result = service.followUser(this.username, username);
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    System.out.println("Followed user successfully!");
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

    /**
     * Unfollow a user
     *
     * @param username user to unfollow
     */
    public void unfollowUser(String username) {
        Call<JsonElement> result = service.unfollowUser(this.username, username);
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NotNull Call<JsonElement> call, @NotNull Response<JsonElement> response) {

                if (response.code() == 200 && response.body() != null) {
                    System.out.println("Unfollowed user successfully!");
                }

            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {
                System.out.println("error");
            }
        });
    }

}