package com.marcelo.animalguide.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.marcelo.animalguide.R;
import com.marcelo.animalguide.utils.GlideImageLoader;
import com.marcelo.animalguide.utils.MediaFilesScanner;

import java.util.ArrayList;

public class GridViewAdapterGallery extends ArrayAdapter<String>
{
    private Context mContext;
    private int layoutResource;
    private String append;

    public GridViewAdapterGallery(@NonNull Context context, int resource, String append, ArrayList<String> imagePath)
    {
        super(context, resource, imagePath);
        mContext = context;
        layoutResource = resource;
        this.append = append;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(layoutResource, parent, false);
        }

        ImageView gridImage =convertView.findViewById(R.id.grid_image_view);
        ImageView ic_video = convertView.findViewById(R.id.ic_video);
        ProgressBar progressBar = convertView.findViewById(R.id.grid_progress);
        String url = getItem(position);

        if (url != null && (!MediaFilesScanner.isVideo(url)))
        {
            if (isFirebaseVideo(url))
            {
                ic_video.setVisibility(View.VISIBLE);
            }
            else
            {
                ic_video.setVisibility(View.GONE);
            }
        }
        else
        {
            ic_video.setVisibility(View.VISIBLE);
        }

        GlideImageLoader.loadImageWithTransition(mContext, url, gridImage, progressBar);

        return convertView;
    }


    private boolean isFirebaseVideo(String url)
    {
        return (url.contains("video") && url.contains("videos"));
    }

}
