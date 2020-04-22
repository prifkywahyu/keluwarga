package com.mobile.apps.keluwarga.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobile.apps.keluwarga.R;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View onView = inflater.inflate(R.layout.fragment_home, container, false);

        TextView textHome = onView.findViewById(R.id.text_welcome);
        textHome.setAllCaps(false);

        return onView;
    }
}