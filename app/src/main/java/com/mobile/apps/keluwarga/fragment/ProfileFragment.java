package com.mobile.apps.keluwarga.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobile.apps.keluwarga.R;
import com.mobile.apps.keluwarga.ui.LandingMain;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View onView = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView textHome = onView.findViewById(R.id.text_profile);
        textHome.setAllCaps(false);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Objects.requireNonNull(firebaseUser).getEmail();

        Button logout = onView.findViewById(R.id.button_logout);
        logout.setOnClickListener(view -> {
            firebaseAuth.signOut();
            Intent back = new Intent(getContext(), LandingMain.class);
            back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(back);
            Objects.requireNonNull(getActivity()).finish();
        });

        return onView;
    }
}
