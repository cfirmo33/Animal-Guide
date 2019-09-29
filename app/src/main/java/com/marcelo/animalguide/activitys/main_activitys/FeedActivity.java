package com.marcelo.animalguide.activitys.main_activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.marcelo.animalguide.R;
import com.marcelo.animalguide.activitys.feed.OpenPostagemActivity;
import com.marcelo.animalguide.activitys.gallery_photos.OpenGalleryActivity;
import com.marcelo.animalguide.encryption.EncryptionSHA1;
import com.marcelo.animalguide.firebase.ServicesFirebase;
import com.marcelo.animalguide.fragments.FeedFragment;
import com.marcelo.animalguide.models.classes.UserClass;
import com.marcelo.animalguide.models.message_toast.MessagesToast;
import com.marcelo.animalguide.permissions.CheckPermissions;
import com.marcelo.animalguide.utils.BottomNavigation;
import com.marcelo.animalguide.utils.DirectoryScanner;
import com.marcelo.animalguide.utils.SectionPagerAdapter;
import com.marcelo.animalguide.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class FeedActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks
{
    private Activity activity = this;
    private Context mContext = activity;
    private FirebaseAuth auth = ServicesFirebase.getFirebaseAuth();
    private FirebaseUser userFirebase = auth.getCurrentUser();
    private DatabaseReference firebaseRef = ServicesFirebase.getFirebaseDatabase();
    private DatabaseReference userRef;
    private StorageReference storageReference = ServicesFirebase.getFirebaseStorage();
    private SharedPreferences sharedPreferences;
    private static final String ARQUIVO_PREFERENCIA = "SaveDados";

    private Toolbar toolbar;
    private SectionPagerAdapter sectionAdapter;
    private CircleImageView imageUserBoxPosts;
    private ImageView imageOpenGalleryBoxPosts;
    private TextView textBoxPosts;
    private AlertDialog dialogPermissions;

    private static final int SELECAO_GALLERY = 300;
    private static final int ACTIVITY_NUMBER = 0;
    private String typeAccount, getNameOng;
    private final String[] permissionsRequired = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    @Override
    protected void onStart()
    {
        super.onStart();

        initImageLoader();
    }

    private void initImageLoader()
    {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        setupFragment();
        setupBottomNavigationView();
        setupToolbar();
        setupBoxPostagem();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_icon_chat, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch ( item.getItemId() )
        {
            case R.id.ic_menu_chat:
                //Open Chat
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupFragment()
    {
        sectionAdapter = new SectionPagerAdapter(FeedActivity.this.getSupportFragmentManager());
        sectionAdapter.addFragment(new FeedFragment());

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(sectionAdapter);
        viewPager.setOffscreenPageLimit(1);
    }

    private void setupBottomNavigationView()
    {
        BottomNavigationViewEx bottomNavigationViewEx =  findViewById(R.id.bottom_navigation);
        BottomNavigation.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigation.enableNavigation(mContext, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUMBER);
        menuItem.setChecked(true);
    }

    private void setupToolbar()
    {
        String id = EncryptionSHA1.encryptionString(userFirebase.getEmail());

        DatabaseReference accountsRef = ServicesFirebase
                .getFirebaseDatabase()
                .child("registered_users")
                .child(Objects.requireNonNull(id));

        accountsRef.addValueEventListener(new ValueEventListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                UserClass userClass = dataSnapshot.getValue(UserClass.class);

                typeAccount = Objects.requireNonNull(userClass).getTypeUser();
                getNameOng = Objects.requireNonNull(userClass).getNameONG();

                if ("ONG".equals(typeAccount))
                {
                    toolbar = findViewById(R.id.toolbarMain);
                    toolbar.setTitle(getNameOng);
                    setSupportActionBar(toolbar);
                }
                else
                {
                    toolbar = findViewById(R.id.toolbarMain);
                    setSupportActionBar(toolbar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                MessagesToast.createMessageError(getString(R.string.exception_accounts_user_database) + databaseError, activity);
            }
        });
    }

    private void setupBoxPostagem()
    {
        imageUserBoxPosts = findViewById(R.id.imageViewBoxUser);
        imageOpenGalleryBoxPosts = findViewById(R.id.imageViewCameraPosts);
        textBoxPosts = findViewById(R.id.textViewBoxPosts);

        sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);

        if (sharedPreferences.contains("path_foto_user_menu"))
        {
            String getPhotoPreferences = sharedPreferences.getString("path_foto_user_menu", "");

            Picasso.get()
                    .load(Uri.parse(getPhotoPreferences))
                    .into(imageUserBoxPosts);
        }
        else
        {
            String idStorage = EncryptionSHA1.encryptionString(userFirebase.getEmail());

            StorageReference imageReference = storageReference
                    .child("Cadastro do Usuário")
                    .child(Objects.requireNonNull(idStorage))
                    .child("photo.png");

            imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
            {
                @Override
                public void onSuccess(Uri uri)
                {
                    Picasso.get()
                            .load(uri)
                            .into(imageUserBoxPosts);
                }
            });
        }

        textBoxPosts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(activity, OpenPostagemActivity.class));
                overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
            }
        });

        imageOpenGalleryBoxPosts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openGallery();
            }
        });
    }

    private void createAlertDialogPermissionsApp()
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        @SuppressLint("InflateParams") View alertView = getLayoutInflater().inflate(R.layout.dialog_permissions_app, null);
        TextView textNotPermission = alertView.findViewById(R.id.textViewNotPermission);
        TextView textOpenSettings = alertView.findViewById(R.id.textViewOpenSettingsPermissions);

        textNotPermission.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                MessagesToast.createMessageWarning(getString(R.string.permission_negada), activity);
                dialogPermissions.cancel();
            }
        });

        textOpenSettings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        alertDialogBuilder.setView(alertView);
        dialogPermissions = alertDialogBuilder.create();
        dialogPermissions.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPermissions.show();
    }

    private void openGallery()
    {
        if (EasyPermissions.hasPermissions(activity, permissionsRequired))
        {
            new DirectoryScanner().execute(Environment.getExternalStorageDirectory().getAbsolutePath());

            startActivity(new Intent(activity, OpenGalleryActivity.class));
            overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
        }
        else
        {
            EasyPermissions.requestPermissions(activity, getString(R.string.message_alert_permissions_negada_gallery_postagens), 1, permissionsRequired);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE)
        {
            //Lib EasyPermissions interface takes action if this is true.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

        if (requestCode == SELECAO_GALLERY)
        {
            if (CheckPermissions.hasPermissions(mContext, permissionsRequired))
            {
                new DirectoryScanner().execute(Environment.getExternalStorageDirectory().getAbsolutePath());
                startActivity(new Intent(activity, OpenGalleryActivity.class));
                overridePendingTransition(R.anim.animation_fade_in, R.anim.animation_fade_out);
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms)
    {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms)
    {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
        {
            createAlertDialogPermissionsApp();
        }
    }
}
