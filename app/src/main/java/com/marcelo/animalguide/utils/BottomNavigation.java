package com.marcelo.animalguide.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.marcelo.animalguide.R;
import com.marcelo.animalguide.activitys.main_activitys.FeedActivity;
import com.marcelo.animalguide.activitys.main_activitys.GoogleMapsActivity;
import com.marcelo.animalguide.activitys.main_activitys.MenuFacebookActivity;
import com.marcelo.animalguide.activitys.main_activitys.NotificationsActivity;
import com.marcelo.animalguide.activitys.main_activitys.SearchActivity;

public class BottomNavigation
{
    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx)
    {
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
        bottomNavigationViewEx.setIconSize(30);
    }

    public static void enableNavigation(final Context context, final Activity callingActivity,
                                        BottomNavigationViewEx view)
    {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.ic_home:
                        if (callingActivity.getClass() != FeedActivity.class)
                        {
                            Intent intent1 = new Intent(context, FeedActivity.class);//ACTIVITY_NUM = 0
                            callingActivity.overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                            context.startActivity(intent1);
                            callingActivity.overridePendingTransition(0, 0);
                        }
                        break;

                    case R.id.ic_search:
                        if (callingActivity.getClass() != SearchActivity.class)
                        {
                            Intent intent2 = new Intent(context, SearchActivity.class);//ACTIVITY_NUM = 1
                            callingActivity.overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                            context.startActivity(intent2);
                            callingActivity.overridePendingTransition(0, 0);
                        }
                        break;

                    case R.id.ic_notification:
                        if (callingActivity.getClass() != NotificationsActivity.class)
                        {
                            Intent intent3 = new Intent(context, NotificationsActivity.class);//ACTIVITY_NUM = 2
                            callingActivity.overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                            context.startActivity(intent3);
                            callingActivity.overridePendingTransition(0, 0);
                        }
                        break;

                    case R.id.ic_mapa:
                        if (callingActivity.getClass() != GoogleMapsActivity.class)
                        {
                            Intent intent4 = new Intent(context, GoogleMapsActivity.class);//ACTIVITY_NUM = 3
                            callingActivity.overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                            context.startActivity(intent4);
                            callingActivity.overridePendingTransition(0, 0);
                        }
                        break;

                    case R.id.ic_menu:
                        if (callingActivity.getClass() != MenuFacebookActivity.class)
                        {
                            Intent intent5 = new Intent(context, MenuFacebookActivity.class);//ACTIVITY_NUM = 4
                            callingActivity.overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
                            context.startActivity(intent5);
                            callingActivity.overridePendingTransition(0, 0);
                        }
                        break;
                }

                return false;
            }
        });
    }
}
