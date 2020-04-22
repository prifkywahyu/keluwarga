package com.mobile.apps.keluwarga.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobile.apps.keluwarga.R;

public class LandingMain extends AppCompatActivity implements View.OnClickListener {

    Button login, register;
    FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_layout);
        auth = FirebaseAuth.getInstance();

        login = findViewById(R.id.to_login);
        login.setOnClickListener(this);
        register = findViewById(R.id.to_register);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_login:
                Intent login = new Intent(LandingMain.this, MainLogin.class);
                startActivity(login);
                finish();
                break;
            case R.id.to_register:
                Intent direct = new Intent(LandingMain.this, MainRegister.class);
                startActivity(direct);
                finish();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            Intent intent = new Intent(LandingMain.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}