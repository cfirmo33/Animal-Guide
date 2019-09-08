package com.marcelo.animalguide.fragments.fragments_feed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelo.animalguide.R;

public class FeedOwnerFragment extends Fragment
{
    private View view;

    public FeedOwnerFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_feed_owner, container, false);


        return view;
    }
}
