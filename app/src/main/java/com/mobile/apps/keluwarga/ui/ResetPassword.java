package com.mobile.apps.keluwarga.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.mobile.apps.keluwarga.R;

import java.util.Objects;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ResetPassword.class.getSimpleName();
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    TextInputEditText email;
    TextInputLayout field;
    String progressText;
    Button reset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle(R.string.reset_button);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        progressText = getString(R.string.progress_reset);
        firebaseAuth = FirebaseAuth.getInstance();

        field = findViewById(R.id.check_email_reset);
        reset = findViewById(R.id.button_reset);
        reset.setOnClickListener(this);

        email = findViewById(R.id.email_reset);
        email.addTextChangedListener(new TextWatcher() {
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
                String inputMail = Objects.requireNonNull(email.getText()).toString().trim();
                String blank = getString(R.string.blank_email);
                String wrong = getString(R.string.wrong_email);

                if (inputMail.isEmpty()) {
                    field.setErrorEnabled(true);
                    field.setError(blank);
                    reset.setClickable(false);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(inputMail).matches()) {
                    field.setErrorEnabled(true);
                    field.setError(wrong);
                    reset.setClickable(false);
                } else {
                    reset.setClickable(true);
                    reset.setEnabled(true);
                    field.setError(null);
                    field.setErrorEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        String forMail = Objects.requireNonNull(email.getText()).toString().trim();
        String blankMail = getString(R.string.blank_email);

        if (view.getId() == R.id.button_reset) {
            if (forMail.isEmpty()) {
                field.setErrorEnabled(true);
                field.setError(blankMail);
            } else if (field.isErrorEnabled()) {
                reset.setClickable(false);
            } else {
                reset.setClickable(true);
                reset.setEnabled(true);
                methodForReset();
            }
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
        Intent direct = new Intent(ResetPassword.this, MainLogin.class);
        startActivity(direct);
        finish();
    }

    public void successDialog() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.dialog_neutral, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.setCancelable(false);

        TextView title = view.findViewById(R.id.error_title);
        title.setText(R.string.yeah);
        TextView message = view.findViewById(R.id.text_error);
        message.setText(R.string.success_reset);

        Button neutral = view.findViewById(R.id.neutral_button);
        neutral.setText(R.string.okay);
        neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                onBackPressed();
            }
        });
        dialog.show();
    }

    public void methodForReset() {
        progressDialog = ProgressDialog.show(ResetPassword.this, null, progressText,
                true, false);

        firebaseAuth.sendPasswordResetEmail(Objects.requireNonNull(email.getText()).toString().trim())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Log.d(TAG, "Reset password link already sent");
                            successDialog();
                        } else {
                            Log.e(TAG, "Reset password return errors");
                            @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.dialog_neutral, null);
                            final BottomSheetDialog dialog = new BottomSheetDialog(ResetPassword.this);
                            dialog.setContentView(view);
                            dialog.setCancelable(false);

                            TextView title = view.findViewById(R.id.error_title);
                            title.setText(R.string.error);
                            TextView message = view.findViewById(R.id.text_error);
                            message.setText(Objects.requireNonNull(task.getException()).getMessage());

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