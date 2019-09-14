package com.marcelo.animalguide.activitys.main_activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.marcelo.animalguide.R;
import com.marcelo.animalguide.encryption.EncryptionSHA1;
import com.marcelo.animalguide.firebase.ServicesFirebase;
import com.marcelo.animalguide.fragments.fragment_mapa.GoogleMapsFragment;
import com.marcelo.animalguide.fragments.fragment_menu.MenuNavigationFragment;
import com.marcelo.animalguide.fragments.fragment_notifications.NotificationFragment;
import com.marcelo.animalguide.fragments.fragment_search.SearchFragment;
import com.marcelo.animalguide.fragments.fragments_feed.FeedONGFragment;
import com.marcelo.animalguide.models.classes.UserClass;
import com.marcelo.animalguide.models.message_toast.MessagesToast;

import java.util.Objects;

public class ONGMainActivity extends AppCompatActivity
{
    private FirebaseAuth auth = ServicesFirebase.getFirebaseAuth();
    private FirebaseUser userFirebase = auth.getCurrentUser();
    private Activity activity = this;
    private SharedPreferences sharedPreferences;
    private static final String ARQUIVO_PREFERENCIA = "SaveDados";

    private TextView textViewToolbar;

    @Override
    protected void onStart()
    {
        super.onStart();
        loadingDadosPreferences();
    }

    private void loadingDadosPreferences()
    {
        sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);

        if (sharedPreferences.contains("name_ong"))
        {
            String getNameOngPreferences = sharedPreferences.getString("name_ong", "");
            textViewToolbar.setText(getNameOngPreferences);
        }
        else
        {
            getNameONG();
        }
    }

    private void getNameONG()
    {
        String id = EncryptionSHA1.encryptionString(userFirebase.getEmail());

        DatabaseReference ongRef = ServicesFirebase.getFirebaseDatabase()
                .child("registered_users")
                .child(Objects.requireNonNull(id));

        ongRef.addValueEventListener(new ValueEventListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                UserClass userClass = dataSnapshot.getValue(UserClass.class);

                String getNameONG = Objects.requireNonNull(userClass).getNameONG();

                if (!getNameONG.equals(""))
                {
                    textViewToolbar.setText(getNameONG);
                }
                else
                {
                    textViewToolbar.setText(getString(R.string.app_name));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                MessagesToast.createMessageError(getString(R.string.exception_accounts_user_name_ong) + databaseError, activity);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ong_main);

        initializedComponents();
        settingsNavigationBottom();
    }

    private void initializedComponents()
    {
        textViewToolbar = findViewById(R.id.textViewTitleToolbarONG);
    }

    private void settingsNavigationBottom()
    {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.navigation_bottom_ong);
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
        bottomNavigationViewEx.setIconSize(30);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        loadingFragments(bottomNavigationViewEx);
        fragmentTransaction.replace(R.id.frameLayoutONG, new FeedONGFragment()).commit();

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
                        fragmentTransaction.replace(R.id.frameLayoutONG, new FeedONGFragment()).commit();
                        return true;

                    case R.id.ic_search:
                        fragmentTransaction.replace(R.id.frameLayoutONG, new SearchFragment()).commit();
                        return true;

                    case R.id.ic_notification:
                        fragmentTransaction.replace(R.id.frameLayoutONG, new NotificationFragment()).commit();
                        return true;

                    case R.id.ic_mapa:
                        fragmentTransaction.replace(R.id.frameLayoutONG, new GoogleMapsFragment()).commit();
                        return true;

                    case R.id.ic_menu:
                        fragmentTransaction.replace(R.id.frameLayoutONG, new MenuNavigationFragment()).commit();
                        return true;
                }

                return false;
            }
        });
    }
}
