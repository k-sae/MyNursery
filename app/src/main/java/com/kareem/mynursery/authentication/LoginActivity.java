package com.kareem.mynursery.authentication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.kareem.mynursery.R;
import com.kareem.mynursery.Utils;
import com.kareem.mynursery.model.User;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private EditText mVerificationField;
    private EditText mPhoneNumberField;
    private View verificationContainer;
    private String mVerificationId;
    private static final String TAG = "PhoneAuthActivity";
    private FirebaseAuth mAuth;
    private TextView informativeTextView;
    private CountryCodePicker countryCodePicker;
    private Switch schoolOwnerSwitch;
    private String country;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        initUi();
        initVerificationCallBack();

    }
    private void initUi()
    {
        mPhoneNumberField = findViewById(R.id.phone_number);
        mVerificationField = findViewById(R.id.verification_key);
        findViewById(R.id.send_verification).setOnClickListener(this);
        findViewById(R.id.verify).setOnClickListener(this);
        verificationContainer = findViewById(R.id.verification_container);
        informativeTextView = findViewById(R.id.informative_textView);
        countryCodePicker = findViewById(R.id.country_picker);
        schoolOwnerSwitch = findViewById(R.id.is_school_owner_switch);
    }
    private void initVerificationCallBack() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
                informativeTextView.setText(R.string.code_sent);
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    informativeTextView.setText(R.string.invalid_phone_number);
                    mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                informativeTextView.setText(R.string.code_sent);

            }
        };
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        String phonePrefix;

        try {
            phonePrefix = countryCodePicker.getFullNumberWithPlus();
            country = countryCodePicker.getSelectedCountryNameCode();
        }catch (Exception e)
        {
            phonePrefix = "+1";
        }


            PhoneAuthProvider.getInstance().verifyPhoneNumber(
               phonePrefix + phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,                // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_verification:
                if (!validatePhoneNumber()) {
                    return;
                }
                verificationContainer.setVisibility(View.VISIBLE);
                startPhoneNumberVerification(mPhoneNumberField.getText().toString());
                break;
            case R.id.verify:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
        }
    }
    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid phone number.");
            informativeTextView.setText(R.string.invalid_phone_number);
            return false;
        }

        return true;
    }
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        if (verificationId == null || code == null || verificationId.equals("") || code.equals("")) {
            informativeTextView.setText(R.string.invalid_phone_number_or_code);
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            // [START_EXCLUDE]
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            User  user = new User();
                            user.setId(firebaseUser.getUid());
                            if (schoolOwnerSwitch.isChecked()) user.update("type", 1);
                            else user.update("type", 3);
                            user.update("country", country );
                            //TODO
                            finish();
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                mVerificationField.setError("Invalid code.");
                            }
                            Utils.showToast(getString(R.string.undefined_error_message), LoginActivity.this);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
}
