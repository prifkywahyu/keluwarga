package com.mobile.apps.keluwarga.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mobile.apps.keluwarga.R;

public class SplashScreen extends AppCompatActivity {

    private boolean internetCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        postDelayedMethod();
    }

    protected boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null) {
                if (info.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                    return internetCheck;
                }
            }
        }

        return false;
    }

    public void postDelayedMethod() {
        int SPLASH_TIME_OUT = 2500;
        new Handler().postDelayed(() -> {
            boolean internetResult = checkConnection();
            if (internetResult) {
                Intent intent = new Intent(SplashScreen.this, LandingMain.class);
                startActivity(intent);
                finish();
            } else {
                showBottomSheetDialog();
            }
        }, SPLASH_TIME_OUT);
    }

    public boolean checkConnection() {
        if (isOnline()) {
            return internetCheck;
        } else {
            internetCheck = false;
            return false;
        }
    }

    public void showBottomSheetDialog() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.dialog_neutral, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();

        TextView title = view.findViewById(R.id.error_title);
        title.setText(R.string.error);
        TextView message = view.findViewById(R.id.text_error);
        message.setText(R.string.network_failed);

        Button logout = view.findViewById(R.id.neutral_button);
        logout.setText(R.string.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}