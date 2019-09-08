package com.marcelo.animalguide.fragments.fragment_notifications;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelo.animalguide.R;

public class NotificationFragment extends Fragment
{
    private View view;

    public NotificationFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_notification, container, false);


        return view;
    }
}
