package com.group2.recipeze;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.group2.recipeze.ui.login.LoginActivity;

/**
 * MainActivity.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_feed,
                R.id.navigation_explore,
                R.id.navigation_addRecipe,
                R.id.navigation_recipeBook,
                R.id.navigation_profile)
                .build();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(navView, navController);
        navView.setItemIconTintList(null);
        navView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
    }

    /**
     * makes the back button on settings return to its parent with is the profile fragment
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navCon =  Navigation.findNavController(this, R.id.nav_host_fragment);
        return navCon.navigateUp() || super.onSupportNavigateUp();

    }

}