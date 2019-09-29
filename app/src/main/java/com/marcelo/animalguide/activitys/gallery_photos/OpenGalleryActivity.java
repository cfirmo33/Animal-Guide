package com.marcelo.animalguide.activitys.gallery_photos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.marcelo.animalguide.R;
import com.marcelo.animalguide.adapters.GridViewAdapterGallery;
import com.marcelo.animalguide.models.message_toast.MessagesToast;
import com.marcelo.animalguide.utils.DirectoryScanner;
import com.marcelo.animalguide.utils.MediaFilesScanner;
import com.marcelo.animalguide.utils.UniversalImageLoader;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class OpenGalleryActivity extends AppCompatActivity
{
    private Activity activity = this;
    private TextView nextScreen;
    private ProgressBar progressBar;
    private GridView gridView;
    private ImageView camImage, close_gallery;
    private VideoView videoView;
    private RelativeLayout rl;
    private Spinner dirSpinner;

    private static final int NUM_GRID_COLUMNS = 4;
    private String mPath;
    private ArrayList<String> listPaths, dirPaths, directoryNames;
    private GestureDetector gestureDetector;
    private final String append = "file:/";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gallery);

        initializeObjects();
        clickButtons();
        setupSpinner();
        hideImageView();
    }

    private void initializeObjects()
    {
        gridView = findViewById(R.id.galleryGrid);
        nextScreen = findViewById(R.id.gallery_next);
        progressBar = findViewById(R.id.galleryProgress);
        camImage = findViewById(R.id.camImage);
        videoView = findViewById(R.id.videoView);
        close_gallery = findViewById(R.id.gallery_close);
        dirSpinner = findViewById(R.id.spinnerGallery);
        rl = findViewById(R.id.gallery_rl);
    }

    private void clickButtons()
    {
        nextScreen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        close_gallery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    private void setupSpinner()
    {
        dirPaths = DirectoryScanner.getFileDirectories();
        directoryNames = DirectoryScanner.getDirectoryNames();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, directoryNames);

        dirSpinner.setAdapter(arrayAdapter);

        dirSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (listPaths != null)
                {
                    listPaths.clear();
                }
                setupGridView(dirPaths.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    private void setupGridView(final String selectedDirectory)
    {
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth / NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        try
        {
            listPaths = new MediaFilesScanner(progressBar).execute(selectedDirectory).get();
        }
        catch (InterruptedException | ExecutionException e)
        {
            MessagesToast.createMessageError(getString(R.string.exception_spinner_galeria), activity);
        }

        GridViewAdapterGallery adapter = new GridViewAdapterGallery(getApplicationContext(), R.layout.layout_grid_image_gallery, append, listPaths);
        gridView.setAdapter(adapter);

        if (listPaths != null)
        {
            final int position = 0;
            mPath = listPaths.get(position);
            if (MediaFilesScanner.isVideo(mPath))
            {
                playVideo(mPath);
            }
            else
            {
                displayImage(mPath);
            }
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                mPath = listPaths.get(position);
                if (MediaFilesScanner.isVideo(mPath))
                {
                    playVideo(mPath);
                }
                else
                {
                    displayImage(mPath);
                }
            }
        });
    }

    private void playVideo(String path)
    {
        camImage.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        videoView.setVideoPath(path);
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                videoView.start();
            }
        });
    }

    private void displayImage(String path)
    {
        videoView.setVisibility(View.GONE);
        camImage.setVisibility(View.VISIBLE);
        UniversalImageLoader.setImage(path, camImage, progressBar, append);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void hideImageView()
    {
        gestureDetector = new GestureDetector(getApplicationContext(), new com.marcelo.animalguide.utils.GestureDetector(rl, gridView));

        rl.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }
}
