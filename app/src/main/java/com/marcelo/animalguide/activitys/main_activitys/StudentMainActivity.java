package com.marcelo.animalguide.activitys.main_activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.marcelo.animalguide.R;
import com.marcelo.animalguide.fragments.fragment_mapa.GoogleMapsFragment;
import com.marcelo.animalguide.fragments.fragment_menu.MenuNavigationFragment;
import com.marcelo.animalguide.fragments.fragment_notifications.NotificationFragment;
import com.marcelo.animalguide.fragments.fragment_search.SearchFragment;
import com.marcelo.animalguide.fragments.fragments_feed.FeedStudentFragment;

public class StudentMainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        settingsNavigationBottom();
    }

    private void settingsNavigationBottom()
    {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.navigation_bottom_student);
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
        bottomNavigationViewEx.setIconSize(30);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        loadingFragments(bottomNavigationViewEx);
        fragmentTransaction.replace(R.id.frameLayoutStudent, new FeedStudentFragment()).commit();

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }

    private void loadingFragments(BottomNavigationViewEx viewEx)
    {
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (item.getItemId())
                {
                    case R.id.ic_home:
                        fragmentTransaction.replace(R.id.frameLayoutStudent, new FeedStudentFragment()).commit();
                        return true;

                    case R.id.ic_search:
                        fragmentTransaction.replace(R.id.frameLayoutStudent, new SearchFragment()).commit();
                        return true;

                    case R.id.ic_notification:
                        fragmentTransaction.replace(R.id.frameLayoutStudent, new NotificationFragment()).commit();
                        return true;

                    case R.id.ic_mapa:
                        fragmentTransaction.replace(R.id.frameLayoutStudent, new GoogleMapsFragment()).commit();
                        return true;

                    case R.id.ic_menu:
                        fragmentTransaction.replace(R.id.frameLayoutStudent, new MenuNavigationFragment()).commit();
                        return true;
                }

                return false;
            }
        });
    }
}
