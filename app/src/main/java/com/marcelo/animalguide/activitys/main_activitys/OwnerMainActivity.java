package com.marcelo.animalguide.activitys.main_activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.marcelo.animalguide.R;
import com.marcelo.animalguide.activitys.google_maps_activity.GoogleMapsOwnerActivity;
import com.marcelo.animalguide.activitys.menus_activity.MenuNavigationOwnerActivity;
import com.marcelo.animalguide.activitys.notifications_activity.NotificationsOwnerActivity;
import com.marcelo.animalguide.activitys.searchs_activity.SearchOwnerActivity;

public class OwnerMainActivity extends AppCompatActivity
{
    private Activity activity = this;
    private TextView textViewBoxPost;
    private ImageView imagePhotoPost, imageVideoPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);

        settingsNavigationBottom();
        settingsBoxPosts();
    }

    private void settingsNavigationBottom()
    {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.navigation_bottom_owner);
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
        bottomNavigationViewEx.setIconSize(30);

        loadingNavigations(bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }

    private void settingsBoxPosts()
    {
        textViewBoxPost = findViewById(R.id.textViewBoxPosts);
        imagePhotoPost = findViewById(R.id.imageViewCameraPosts);
        imageVideoPosts = findViewById(R.id.imageViewVideoPosts);
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
                        startActivity(new Intent(activity, OwnerMainActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        return true;

                    case R.id.ic_search:
                        startActivity(new Intent(activity, SearchOwnerActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        return true;

                    case R.id.ic_notification:
                        startActivity(new Intent(activity, NotificationsOwnerActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        return true;

                    case R.id.ic_mapa:
                        startActivity(new Intent(activity, GoogleMapsOwnerActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        return true;

                    case R.id.ic_menu:
                        startActivity(new Intent(activity, MenuNavigationOwnerActivity.class));
                        overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                        return true;
                }

                return false;
            }
        });
    }
}
