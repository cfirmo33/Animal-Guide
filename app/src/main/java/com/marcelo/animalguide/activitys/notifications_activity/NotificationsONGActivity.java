package com.marcelo.animalguide.activitys.notifications_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.marcelo.animalguide.R;
import com.marcelo.animalguide.activitys.main_activitys.ONGMainActivity;
import com.marcelo.animalguide.activitys.menus_activity.MenuNavigationONGActivity;
import com.marcelo.animalguide.activitys.searchs_activity.SearchONGActivity;

public class NotificationsONGActivity extends AppCompatActivity
{
    private Activity activity = this;

    private BottomNavigationViewEx bottomNavigationViewEx;

    @Override
    protected void onStart()
    {
        super.onStart();
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_ong);

        settingsNavigationBottom();
    }

    private void settingsNavigationBottom()
    {
        bottomNavigationViewEx = findViewById(R.id.navigation_bottom_notifications_ong);
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
        bottomNavigationViewEx.setIconSize(30);

        loadingNavigations(bottomNavigationViewEx);
    }

    private void loadingNavigations(BottomNavigationViewEx viewEx)
    {
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.ic_home:
                        startActivity(new Intent(activity, ONGMainActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        finish();
                        return true;

                    case R.id.ic_search:
                        startActivity(new Intent(activity, SearchONGActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        finish();
                        return true;

                    case R.id.ic_notification:
                        startActivity(new Intent(activity, NotificationsONGActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        finish();
                        return true;

                    case R.id.ic_mapa:

                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        finish();
                        return true;

                    case R.id.ic_menu:
                        startActivity(new Intent(activity, MenuNavigationONGActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        finish();
                        return true;
                }

                return false;
            }
        });
    }
}
