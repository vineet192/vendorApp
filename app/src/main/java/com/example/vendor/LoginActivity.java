package com.example.vendor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Constants.ApiInterface;
import com.example.Models.LoginResponse;
import com.example.Models.ResponseServerToken;
import com.example.Models.TokenKeySaveRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity
{

    private Button loginButton;
    private static final String TAG = "[RegWithPhNum]";
    private Button otpVerify;
    private EditText vendorID;
    private Retrofit retrofit;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    String verificationId;
    private String phone;
    private LoginResponse vendorDetails;
    private TextView otpSentTo;
    private ProgressBar loginLoader;
    private ProgressBar otpLoader;
    private ProgressBar otpVerifier;
    EditText[] ets;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.welcome_login_button);
        vendorID = (EditText) findViewById(R.id.vendor_id);
        otpVerify = (Button) findViewById(R.id.verify_and_proceed_button);
        otpSentTo = (TextView) findViewById(R.id.otp_sent_to_tv);
        loginLoader = (ProgressBar) findViewById(R.id.login_progress_bar);
        otpVerifier = (ProgressBar) findViewById(R.id.otp_progress_bar);

        ets = new EditText[]{findViewById(R.id.otp1), findViewById(R.id.otp2), findViewById(R.id.otp3),
                findViewById(R.id.otp4), findViewById(R.id.otp5), findViewById(R.id.otp6)};

        initOtpEdit();
        retrofit = new Retrofit.Builder().baseUrl("https://gocoding.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        otpVerifier.setVisibility(View.GONE);
        otpVerify.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences(
                getString(R.string.shared_preference_key), Context.MODE_PRIVATE);

        if (!sharedPref.getString(getString(R.string.vendor_id_key), "default").equals("default"))
        {
            Intent in = new Intent(LoginActivity.this, MainActivity_.class);
            startActivity(in);
            finish();
        }

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
        {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
            {
                System.out.println("Verification success !");
                signInWithPhoneAuthCredential(phoneAuthCredential);
                System.out.println("SMS Code is (onVerificationCompleted) : " + phoneAuthCredential.getSmsCode());
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e)
            {
                System.out.println("Verification failed " + e.getMessage());
                loginButton.setVisibility(View.VISIBLE);
                loginLoader.setVisibility(View.GONE);
                Log.d("failtoget", String.valueOf(e));
                Toast.makeText(getApplicationContext(), "Firebase verification failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken)
            {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(LoginActivity.this, "On code sent", Toast.LENGTH_SHORT).show();
                verificationId = s;
                loginButton.setVisibility(View.GONE);
                vendorID.setVisibility(View.GONE);
                loginLoader.setVisibility(View.GONE);
                findViewById(R.id.otp_page).setVisibility(View.VISIBLE);
                otpSentTo.setText("OTP sent to " + phone);
            }
        };


        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String vId = vendorID.getText().toString();
                loginButton.setVisibility(View.GONE);
                loginLoader.setVisibility(View.VISIBLE);
                vendorID.setEnabled(false);
                vendorID.setFocusable(false);

                loginLoader.setVisibility(View.VISIBLE);

                if (vId.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Please enter a Vendor Id !", Toast.LENGTH_SHORT).show();
                }


                ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                String requestStr = "{\n" +
                        "                    'vendor_id' : '" + vId + "'\n" +
                        "                }";

                LoginRequestVendor loginRequest = new Gson().fromJson(requestStr, LoginRequestVendor.class);

                //Getting delivery boy details for debugging purposes. Change the serialized names in LoginResponse to use
                //vendor login call.
                Call<LoginResponse> listCall = apiInterface.getLogin(loginRequest);

                listCall.enqueue(new Callback<LoginResponse>()
                {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response)
                    {
                        System.out.println("VENDOR NAME : " + response.body().getVendor_name());

                        vendorDetails = response.body();

                        phone = "+16505553434";

                        //Replace this phone number with response.body().getVendor_phone() or a valid phone number.

                        //Verifying phone number with firebase and sending sms code.
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phone,        // Phone number to verify
                                60,                 // Timeout duration
                                TimeUnit.SECONDS,   // Unit of timeout
                                LoginActivity.this,               // Activity (for callback binding)
                                mCallbacks);        // OnVerificationStateChangedCallbacks
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t)
                    {
                        System.out.println("Something went wrong :(");
                        loginButton.setVisibility(View.VISIBLE);
                        vendorID.setEnabled(true);
                        vendorID.setFocusable(true);
                    }
                });
            }
        });

        otpVerify.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                otpVerifier.setVisibility(View.VISIBLE);
                otpVerify.setVisibility(View.GONE);

                verifyOTP(verificationId);
            }
        });
    }

    private void verifyOTP(String verificationId)
    {
        String code;

        code = ((EditText) findViewById(R.id.otp1)).getText().toString() +
                ((EditText) findViewById(R.id.otp2)).getText().toString() +
                ((EditText) findViewById(R.id.otp3)).getText().toString() +
                ((EditText) findViewById(R.id.otp4)).getText().toString() +
                ((EditText) findViewById(R.id.otp5)).getText().toString() +
                ((EditText) findViewById(R.id.otp6)).getText().toString();
        if (code.length() < 6)
        {
            Toast.makeText(this, "Please enter a 6 digit number !", Toast.LENGTH_SHORT).show();
            otpVerifier.setVisibility(View.GONE);
            otpVerify.setVisibility(View.VISIBLE);
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            System.out.println("Sign in complete");
                            System.out.println("SMS Code is : " + credential.getSmsCode());

                            final String[] token = new String[1];
                            FirebaseInstanceId.getInstance().getInstanceId()
                                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<InstanceIdResult> task)
                                        {
                                            if (!task.isSuccessful())
                                            {
                                                Log.d("Task : ", "Hi getInstanceId failed * " + task.getException());
                                                return;
                                                //Remember that there is error in generating token if mobile is not connected to internet.
                                            }
                                            token[0] = task.getResult().getToken();
                                            Log.d("tokenSent to server ", "" + token[0]);
                                            Log.d("InstanceId(Token) : ", token[0]);
                                            saveTokenToServer(token[0], vendorDetails.getVendor_phone());
                                        }
                                    });

                            //Add details to sharedPreferences.
                            SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences(
                                    getString(R.string.shared_preference_key), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.vendor_phone_key), vendorDetails.getVendor_phone());
                            editor.putString(getString(R.string.vendor_name_key), vendorDetails.getVendor_name());
                            editor.putString(getString(R.string.vendor_id_key), vendorDetails.getVendor_id());
                            editor.putString(getString(R.string.vendor_found_key), vendorDetails.getFound());
                            editor.putString(getString(R.string.vendor_image_key), vendorDetails.getImagePath());
                            editor.putString(getString(R.string.vendor_city_key), vendorDetails.getVendor_city());
                            editor.putString(getString(R.string.vendor_address_key), vendorDetails.getVendor_address());
                            editor.putString("fcm_token", token[0]);
                            editor.putString("vendor_lat", vendorDetails.getVendor_lat());
                            editor.putString("vendor_long", vendorDetails.getVendor_long());
                            Toast.makeText(getApplicationContext(), vendorDetails.getVendor_lat(), Toast.LENGTH_LONG).show();


                            if (sharedPref.getString(getString(R.string.vendor_active_status_key), "default").equals("default"))
                            {
                                editor.putString(getString(R.string.vendor_active_status_key), "inactive");
                            }
                            editor.commit();

                            //start the mainactivity.
                            Intent in = new Intent(LoginActivity.this, MainActivity_.class);
                            in.putExtra("activity", "Login");
                            startActivity(in);
                            finish();
                        } else
                        {
                            Toast.makeText(LoginActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            otpVerifier.setVisibility(View.GONE);
                            otpVerify.setVisibility(View.VISIBLE);
                        }
                    }

                });
    }

    private void saveTokenToServer(String token, String phone)
    {
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        String requestStr = "{" +
                "                    'vendor_phone' : '" + phone + "',\n" +
                "                    'vendor_fcm_token' : '" + token + "'\n" +
                "             }";

        TokenKeySaveRequest tokenKeySaveRequest = new Gson().fromJson(requestStr, TokenKeySaveRequest.class);

        Call<ResponseServerToken> call = apiInterface.saveFcmToken(tokenKeySaveRequest);
        call.enqueue(new Callback<ResponseServerToken>()
        {
            @Override
            public void onResponse(Call<ResponseServerToken> call, Response<ResponseServerToken> response)
            {
                Log.d("repons.issuccessfull() ", "" + response.isSuccessful());
                Log.d("response", "" + response.body());
                if (response.isSuccessful())
                {
                    Log.d("token ", "" + token);
                    Log.d("responseSuccessful", "" + response.body());
                    Toast.makeText(LoginActivity.this, "Token Saved Successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseServerToken> call, Throwable t)
            {
                Log.d("responseUNSuccessful", "" + t.getMessage());
                Toast.makeText(getApplicationContext(), "token not saved", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initOtpEdit()
    {
        View.OnKeyListener otpKeyListener = new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL)
                {
                    Log.d(TAG, "onKey: DELETE PRESSED");
                    if (((EditText) v).getText().length() == 1)
                    {
                        ((EditText) v).setText("");
                        return true;
                    }
                    switch (v.getId())
                    {
                        case R.id.otp1:
                            break;
                        case R.id.otp2:
                            ets[0].requestFocus();
                            break;
                        case R.id.otp3:
                            ets[1].requestFocus();
                            break;
                        case R.id.otp4:
                            ets[2].requestFocus();
                            break;
                        case R.id.otp5:
                            ets[3].requestFocus();
                            break;
                        case R.id.otp6:
                            ets[4].requestFocus();
                            break;
                    }
                    return true;
                } else
                {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9)
                    {
                        Log.d(TAG, "onKey: NUMBER PRESSED");
                        switch (v.getId())
                        {
                            case R.id.otp1:
                                ets[1].requestFocus();
                                break;
                            case R.id.otp2:
                                ets[2].requestFocus();
                                break;
                            case R.id.otp3:
                                ets[3].requestFocus();
                                break;
                            case R.id.otp4:
                                ets[4].requestFocus();
                                break;
                            case R.id.otp5:
                                ets[5].requestFocus();
                                break;
                            case R.id.otp6:
                                break;
                        }
                    }
                    return false;
                }
//                return false;
            }
        };

        View.OnFocusChangeListener otpFocusChangeListener = new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    EditText et = (EditText) v;
                    if (et.getText().length() == 1)
                    {
                        et.setText("");
                    }
                }
            }
        };

        for (EditText et : ets)
        {
            et.setOnKeyListener(otpKeyListener);
            et.setOnFocusChangeListener(otpFocusChangeListener);
        }
    }

}