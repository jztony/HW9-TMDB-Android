package com.example.cs571hw9;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.smarteist.autoimageslider.SliderView;
import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    // message of the package
    public static final String EXTRA_MESSAGE = "com.example.cs571hw9.MESSAGE";

    /** Navigate to Details Activity */
//    public void sendMessage(View view) {
//        // Do something in response to button
//        Intent intent = new Intent(this, DetailsActivity.class);
//        TextView mediaType = (TextView) findViewById(R.id.media_type);
//        TextView mediaId = (TextView) findViewById(R.id.media_id);
//        String key_type = "";
//        if (mediaType.getText().toString().substring(0, 1).equals("m")) {
//            key_type = "movie";
//        } else {
//            key_type = "tv";
//        }
//        String[] message = {key_type, mediaId.getText().toString()};
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
//
//    }

    public void sendMessage(View view, String[] data) {
        // Do something in response to button
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, data);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        findViewById(android.R.id.content).getRootView().setBackgroundColor(0xFF1c272e);
        setTheme(R.style.Theme_CS571HW9);
//        getWindow().setNavigationBarColor(0xff1c272e);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_watchlist)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        getSupportActionBar().hide();

    }

}