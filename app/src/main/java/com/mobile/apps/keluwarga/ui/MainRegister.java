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

public class MainRegister extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainRegister.class.getSimpleName();
    TextInputLayout mailField, passField, retypeField;
    TextInputEditText mail, pass, retype;
    ProgressDialog progressDialog;
    Button nextStep, advice;
    String progressText;
    FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle(" ");
        actionBar.setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        auth = FirebaseAuth.getInstance();
        progressText = getString(R.string.progress_text);

        mailField = findViewById(R.id.check_email_register);
        passField = findViewById(R.id.check_password_register);
        retypeField = findViewById(R.id.check_retype_register);
        nextStep = findViewById(R.id.button_next);
        nextStep.setOnClickListener(this);
        advice = findViewById(R.id.advice_login);
        advice.setOnClickListener(this);

        mail = findViewById(R.id.email_slave);
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
                    nextStep.setClickable(false);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(inputMail).matches()) {
                    mailField.setErrorEnabled(true);
                    mailField.setError(wrong);
                    nextStep.setClickable(false);
                } else {
                    nextStep.setClickable(true);
                    nextStep.setEnabled(true);
                    mailField.setError(null);
                    mailField.setErrorEnabled(false);
                }
            }
        });

        pass = findViewById(R.id.password_slave);
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
                    nextStep.setClickable(false);
                } else if (inputPass.length() < 6) {
                    passField.setErrorEnabled(true);
                    passField.setError(minimum);
                    nextStep.setClickable(false);
                } else if (inputPass.length() > 20) {
                    passField.setErrorEnabled(true);
                    passField.setError(maximum);
                    nextStep.setClickable(false);
                } else {
                    nextStep.setClickable(true);
                    nextStep.setEnabled(true);
                    passField.setError(null);
                    passField.setErrorEnabled(false);
                }
            }
        });

        retype = findViewById(R.id.retype_slave);
        retype.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        retype.setTypeface(ResourcesCompat.getFont(this, R.font.montserrat_regular));
        retype.setTransformationMethod(new PasswordTransformationMethod());
        retype.addTextChangedListener(new TextWatcher() {
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
                String inputRetype = Objects.requireNonNull(retype.getText()).toString().trim();
                String inputPass = Objects.requireNonNull(pass.getText()).toString().trim();
                String blank = getString(R.string.blank_retype);
                String wrong = getString(R.string.wrong_password);

                if (inputRetype.isEmpty()) {
                    retypeField.setErrorEnabled(true);
                    retypeField.setError(blank);
                    nextStep.setClickable(false);
                } else if (!inputRetype.equals(inputPass)) {
                    retypeField.setErrorEnabled(true);
                    retypeField.setError(wrong);
                    nextStep.setClickable(false);
                } else {
                    nextStep.setClickable(true);
                    nextStep.setEnabled(true);
                    retypeField.setError(null);
                    retypeField.setErrorEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        String forMail = Objects.requireNonNull(mail.getText()).toString().trim();
        String forPass = Objects.requireNonNull(pass.getText()).toString().trim();
        String forRetype = Objects.requireNonNull(retype.getText()).toString().trim();
        String blankMail = getString(R.string.blank_email);
        String blankPass = getString(R.string.blank_password);
        String blankRetype = getString(R.string.blank_retype);

        switch (view.getId()) {
            case R.id.advice_login:
                Intent signed = new Intent(MainRegister.this, MainLogin.class);
                startActivity(signed);
                finish();
                break;
            case R.id.button_next:
                if (forMail.isEmpty() & forPass.isEmpty() & forRetype.isEmpty()) {
                    mailField.setErrorEnabled(true);
                    mailField.setError(blankMail);
                    passField.setErrorEnabled(true);
                    passField.setError(blankPass);
                    retypeField.setErrorEnabled(true);
                    retypeField.setError(blankRetype);
                } else if (forMail.isEmpty()) {
                    mailField.setErrorEnabled(true);
                    mailField.setError(blankMail);
                } else if (forPass.isEmpty()) {
                    passField.setErrorEnabled(true);
                    passField.setError(blankPass);
                } else if (forRetype.isEmpty()) {
                    retypeField.setErrorEnabled(true);
                    retypeField.setError(blankPass);
                } else if (mailField.isErrorEnabled() | passField.isErrorEnabled() | retypeField.isErrorEnabled()) {
                    nextStep.setClickable(false);
                } else {
                    nextStep.setClickable(true);
                    nextStep.setEnabled(true);
                    sentRegister();
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
        Intent direct = new Intent(MainRegister.this, LandingMain.class);
        startActivity(direct);
        finish();
    }

    public void sendVerification() {
        Objects.requireNonNull(auth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Verification email was successfully sent");
                } else {
                    Log.e("Mail verification error", Objects.requireNonNull(task.getException()).getMessage());
                }
            }
        });
    }

    public void successDialog() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.dialog_neutral, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.setCancelable(false);

        TextView title = view.findViewById(R.id.error_title);
        title.setText(R.string.yeah);
        TextView message = view.findViewById(R.id.text_error);
        message.setText(R.string.success_register);

        Button neutral = view.findViewById(R.id.neutral_button);
        neutral.setText(R.string.okay);
        neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent direct = new Intent(MainRegister.this, MainLogin.class);
                startActivity(direct);
                finish();
            }
        });
        dialog.show();
    }

    public void sentRegister() {
        progressDialog = ProgressDialog.show(MainRegister.this, null, progressText,
                true, false);

        (auth.createUserWithEmailAndPassword(Objects.requireNonNull(mail.getText()).toString().trim(),
                Objects.requireNonNull(pass.getText()).toString().trim()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Log.d(TAG, "User is successfully registered");
                            sendVerification();
                            successDialog();
                        } else {
                            Log.e("Register return errors", Objects.requireNonNull(task.getException()).toString());
                            @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.dialog_neutral, null);
                            final BottomSheetDialog dialog = new BottomSheetDialog(MainRegister.this);
                            dialog.setContentView(view);
                            dialog.setCancelable(false);

                            TextView title = view.findViewById(R.id.error_title);
                            title.setText(R.string.error);
                            TextView message = view.findViewById(R.id.text_error);
                            message.setText(task.getException().getMessage());

                            Button neutral = view.findViewById(R.id.neutral_button);
                            neutral.setText(R.string.understand);
                            neutral.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    }
                });
    }
}