package com.example.vendor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Constants.ApiInterface;
import com.example.Constants.ApiUtils;
import com.example.Models.LoginRequest;
import com.example.Models.LoginResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OtpView;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String vendorPhone;
    private String vendorIdentity;
    private String vendorName;
    private EditText vendorId;
    private Button submitId;
    private String id;
    final Context context = this;
    private FirebaseAuth mAuth;
    private String verificationid;
    private ApiInterface mAPIService;
    OtpView otpView;
    private final String PREFERENCE_FILE_KEY = "myAppPreference";
    private SharedPreferences.Editor editor;
    private static final String KEY_USERNAME = "VendorName";
    private static final String KEY_USERID = "VendorId";
    private static final String KEY_PHONE = "VendorPhone";
    private SharedPreferences sharedPref;
    private String defaultValueName = "NoNAME";
    private String defaultValueId = "NoID";
    private String defaultValuePhone = "NoPhone";
    private String[] oldUserName;
    private String[] oldUserId;
    private String[] oldUserPhone;
//    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mAPIService = ApiUtils.getAPIService();

        sharedPref = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        oldUserName = new String[]{sharedPref.getString(KEY_USERNAME, defaultValueName)};
        oldUserId = new String[]{sharedPref.getString(KEY_USERID, defaultValueId)};
        oldUserPhone = new String[]{sharedPref.getString(KEY_PHONE, defaultValuePhone)};
        vendorId = (EditText) findViewById(R.id.vendorID);
        submitId = (Button) findViewById(R.id.submitID);

        if(oldUserId[0].equals("NoID")) {

            submitId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    id = vendorId.getText().toString().trim();
//                    editor.putString(KEY_USERNAME, id);
//                    editor.commit();

                    LoginRequest request = new LoginRequest(id);
                    Call<LoginResponse> calldash = mAPIService.getLoginDelivery(request);
                    calldash.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                            if (response.isSuccessful()) {
                                Log.d("responseBody",response.body().getFound()+"*"+response.body().getVendor_id()+"*"+response.body().getVendor_name()+"*"+response.body().getVendor_phone());
                                if (response.body().getFound().equals("false")) {
                                    Toast.makeText(MainActivity.this, "Wrong Credentials ", Toast.LENGTH_LONG).show();
                                } else {
                                    ((ProductsClient)MainActivity.this.getApplicationContext()).setPhone_no(response.body().getVendor_phone());
                                    vendorPhone = response.body().getVendor_phone();
                                    vendorName = response.body().getVendor_name();
                                    vendorIdentity = response.body().getVendor_id();
                                    sendVerificationCode("+91" + vendorPhone);
                                    final Dialog dialog = new Dialog(context);
                                    dialog.setContentView(R.layout.customdialogotp);
                                    dialog.show();

                                    otpView = dialog.findViewById(R.id.otp_view);
                                    otpView.setItemWidth(50);

                                    Button btnSave = (Button) dialog.findViewById(R.id.otpget);

                                    btnSave.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String code = otpView.getText().toString();
                                            Toast.makeText(MainActivity.this, code, Toast.LENGTH_SHORT).show();
//                                            verifyCode(code);
                                            dialog.dismiss();

                                            oldUserId[0] = id;
                                            oldUserPhone[0] = "+91" + response.body().getVendor_phone();

                                            editor.putString(KEY_USERNAME, vendorName);
                                            editor.putString(KEY_USERID, vendorIdentity);
                                            editor.putString(KEY_PHONE, vendorPhone);
                                            editor.commit();
                                            loginIntoAccount();
                                        }
                                    });

                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Response from server is not successful", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Failure " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            });
        }
        else{
            vendorName = oldUserName[0];
            vendorIdentity = oldUserId[0];
            vendorPhone = oldUserPhone[0];
//            Toast.makeText(MainActivity.this, vendorName, Toast.LENGTH_SHORT).show();
            ((ProductsClient)MainActivity.this.getApplicationContext()).setPhone_no(vendorPhone);
            loginIntoAccount();
        }

    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
        signInWithCredential(credential);
    }

    private void loginIntoAccount(){
//        Toast.makeText(MainActivity.this, "Logging In", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, MainActivity_.class);
        intent.putExtra("vendorName", vendorName);
        intent.putExtra("vendorPhone", vendorPhone);
        intent.putExtra("vendorIdentity", vendorIdentity);
        startActivity(intent);
        finish();
    }


    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this, MainActivity_.class);
                            intent.putExtra("vendorName", vendorName);
                            intent.putExtra("vendorPhone", vendorPhone);
                            intent.putExtra("vendorIdentity", vendorIdentity);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private void sendVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationid = s;
            Toast.makeText(MainActivity.this, "onCodeSent",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            Toast.makeText(MainActivity.this, "onVerificationCompleted" +  code,Toast.LENGTH_LONG).show();
            if (code != null){
                otpView.setText(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(MainActivity.this, "onVerificationFailed " +  e.getMessage(),Toast.LENGTH_LONG).show();

        }
    };
}
