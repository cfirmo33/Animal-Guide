package com.marcelo.animalguide.activitys.searchs_activity;

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
import com.marcelo.animalguide.activitys.google_maps_activity.GoogleMapsStudentActivity;
import com.marcelo.animalguide.activitys.main_activitys.StudentMainActivity;
import com.marcelo.animalguide.activitys.menus_activity.MenuNavigationStudentActivity;
import com.marcelo.animalguide.activitys.notifications_activity.NotificationsStudentActivity;

public class SearchStudentActivity extends AppCompatActivity
{
    private Activity activity = this;

    private BottomNavigationViewEx bottomNavigationViewEx;

    @Override
    protected void onStart()
    {
        super.onStart();
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_student);

        settingsNavigationBottom();
    }

    private void settingsNavigationBottom()
    {
        bottomNavigationViewEx = findViewById(R.id.navigation_bottom_search_student);
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
                        startActivity(new Intent(activity, StudentMainActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        finish();
                        return true;

                    case R.id.ic_search:
                        startActivity(new Intent(activity, SearchStudentActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        finish();
                        return true;

                    case R.id.ic_notification:
                        startActivity(new Intent(activity, NotificationsStudentActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        finish();
                        return true;

                    case R.id.ic_mapa:
                        startActivity(new Intent(activity, GoogleMapsStudentActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        finish();
                        return true;

                    case R.id.ic_menu:
                        startActivity(new Intent(activity, MenuNavigationStudentActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        finish();
                        return true;
                }

                return false;
            }
        });
    }
}
