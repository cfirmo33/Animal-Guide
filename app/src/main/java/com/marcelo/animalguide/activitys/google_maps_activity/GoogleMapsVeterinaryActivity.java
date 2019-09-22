package com.marcelo.animalguide.activitys.google_maps_activity;

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
import com.marcelo.animalguide.activitys.main_activitys.VeterinaryMainActivity;
import com.marcelo.animalguide.activitys.menus_activity.MenuNavigationVeterinaryActivity;
import com.marcelo.animalguide.activitys.notifications_activity.NotificationsVeterinaryActivity;
import com.marcelo.animalguide.activitys.searchs_activity.SearchVeterinaryActivity;

public class GoogleMapsVeterinaryActivity extends AppCompatActivity
{
    private Activity activity = this;

    private BottomNavigationViewEx bottomNavigationViewEx;

    @Override
    protected void onStart()
    {
        super.onStart();
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps_veterinary);

        settingsNavigationBottom();
    }

    private void settingsNavigationBottom()
    {
        bottomNavigationViewEx = findViewById(R.id.navigation_bottom_maps_veterinary);
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
                        startActivity(new Intent(activity, VeterinaryMainActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        finish();
                        return true;

                    case R.id.ic_search:
                        startActivity(new Intent(activity, SearchVeterinaryActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        finish();
                        return true;

                    case R.id.ic_notification:
                        startActivity(new Intent(activity, NotificationsVeterinaryActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        finish();
                        return true;

                    case R.id.ic_mapa:
                        startActivity(new Intent(activity, GoogleMapsVeterinaryActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        finish();
                        return true;

                    case R.id.ic_menu:
                        startActivity(new Intent(activity, MenuNavigationVeterinaryActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        finish();
                        return true;
                }

                return false;
            }
        });
    }
}
