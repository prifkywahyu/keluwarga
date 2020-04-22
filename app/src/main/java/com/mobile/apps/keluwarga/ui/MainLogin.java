package com.mobile.apps.keluwarga.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mobile.apps.keluwarga.R;

import java.util.Objects;

public class MainLogin extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainLogin.class.getSimpleName();
    TextInputLayout mailField, passField;
    TextInputEditText mail, pass;
    ProgressDialog progress;
    Button login, advice;
    String progressText;
    FirebaseAuth auth;
    TextView forgot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle(" ");
        actionBar.setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        auth = FirebaseAuth.getInstance();
        progressText = getString(R.string.progress_text);

        forgot = findViewById(R.id.forgot);
        forgot.setOnClickListener(this);
        mailField = findViewById(R.id.check_email_login);
        passField = findViewById(R.id.check_password_login);
        login = findViewById(R.id.button_login);
        login.setOnClickListener(this);
        advice = findViewById(R.id.advice_register);
        advice.setOnClickListener(this);

        mail = findViewById(R.id.email_main);
        mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Currently this code is blank
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Currently this code is blank
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputMail = Objects.requireNonNull(mail.getText()).toString().trim();
                String blank = getString(R.string.blank_email);
                String wrong = getString(R.string.wrong_email);

                if (inputMail.isEmpty()) {
                    mailField.setErrorEnabled(true);
                    mailField.setError(blank);
                    login.setClickable(false);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(inputMail).matches()) {
                    mailField.setErrorEnabled(true);
                    mailField.setError(wrong);
                    login.setClickable(false);
                } else {
                    login.setClickable(true);
                    login.setEnabled(true);
                    mailField.setError(null);
                    mailField.setErrorEnabled(false);
                }
            }
        });

        pass = findViewById(R.id.password_main);
        pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pass.setTypeface(ResourcesCompat.getFont(this, R.font.montserrat_regular));
        pass.setTransformationMethod(new PasswordTransformationMethod());
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Currently this code is blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Currently this code is blank
            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputPass = Objects.requireNonNull(pass.getText()).toString().trim();
                String blank = getString(R.string.blank_password);
                String minimum = getString(R.string.pass_minimum);
                String maximum = getString(R.string.pass_maximum);

                if (inputPass.isEmpty()) {
                    passField.setErrorEnabled(true);
                    passField.setError(blank);
                    login.setClickable(false);
                } else if (inputPass.length() < 6) {
                    passField.setErrorEnabled(true);
                    passField.setError(minimum);
                    login.setClickable(false);
                } else if (inputPass.length() > 20) {
                    passField.setErrorEnabled(true);
                    passField.setError(maximum);
                    login.setClickable(false);
                } else {
                    login.setClickable(true);
                    login.setEnabled(true);
                    passField.setError(null);
                    passField.setErrorEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        String forMail = Objects.requireNonNull(mail.getText()).toString().trim();
        String forPass = Objects.requireNonNull(pass.getText()).toString().trim();
        String blankMail = getString(R.string.blank_email);
        String blankPass = getString(R.string.blank_password);

        switch (view.getId()) {
            case R.id.forgot:
                Intent reset = new Intent(MainLogin.this, ResetPassword.class);
                startActivity(reset);
                finish();
                break;
            case R.id.advice_register:
                Intent enroll = new Intent(MainLogin.this, MainRegister.class);
                startActivity(enroll);
                finish();
                break;
            case R.id.button_login:
                if (forMail.isEmpty() & forPass.isEmpty()) {
                    mailField.setErrorEnabled(true);
                    mailField.setError(blankMail);
                    passField.setErrorEnabled(true);
                    passField.setError(blankPass);
                } else if (forMail.isEmpty()) {
                    mailField.setErrorEnabled(true);
                    mailField.setError(blankMail);
                } else if (forPass.isEmpty()) {
                    passField.setErrorEnabled(true);
                    passField.setError(blankPass);
                } else if (mailField.isErrorEnabled() | passField.isErrorEnabled()) {
                    login.setClickable(false);
                } else {
                    login.setClickable(true);
                    login.setEnabled(true);
                    methodForLogin();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent direct = new Intent(MainLogin.this, LandingMain.class);
        startActivity(direct);
        finish();
    }

    public void verificationDialog() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.dialog_neutral, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.setCancelable(false);

        TextView title = view.findViewById(R.id.error_title);
        title.setText(R.string.error);
        TextView message = view.findViewById(R.id.text_error);
        message.setText(R.string.advice_verify);

        Button logout = view.findViewById(R.id.neutral_button);
        logout.setText(R.string.understand);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void methodForLogin() {
        progress = ProgressDialog.show(MainLogin.this, null, progressText,
                true, false);

        (auth.signInWithEmailAndPassword(Objects.requireNonNull(mail.getText()).toString().trim(),
                Objects.requireNonNull(pass.getText()).toString().trim()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progress.dismiss();

                        if (task.isSuccessful()) {
                            if (Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified()) {
                                Log.d(TAG, "User is successfully logged in");
                                Intent success = new Intent(MainLogin.this, MainActivity.class);
                                success.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(success);
                                finish();
                            } else {
                                Log.e(TAG, "User email is not verified yet");
                                verificationDialog();
                            }
                        } else {
                            Log.e("Login return errors", Objects.requireNonNull(task.getException()).toString());
                            @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.dialog_neutral, null);
                            BottomSheetDialog getDialog = new BottomSheetDialog(MainLogin.this);
                            getDialog.setContentView(view);
                            getDialog.setCancelable(false);

                            TextView title = view.findViewById(R.id.error_title);
                            title.setText(R.string.error);
                            TextView message = view.findViewById(R.id.text_error);
                            message.setText(task.getException().getMessage());

                            Button logout = view.findViewById(R.id.neutral_button);
                            logout.setText(R.string.understand);
                            logout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    getDialog.dismiss();
                                }
                            });
                            getDialog.show();
                        }
                    }
                });
    }
}