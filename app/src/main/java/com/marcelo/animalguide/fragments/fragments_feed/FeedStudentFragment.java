package com.marcelo.animalguide.fragments.fragments_feed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelo.animalguide.R;

public class FeedStudentFragment extends Fragment
{
    private View view;

    public FeedStudentFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_feed_student, container, false);


        return view;
    }
}
