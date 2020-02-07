package com.example.paboosyar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.paboosyar.RetrofitModels.Authentication;
import com.example.paboosyar.RetrofitModels.User;
import com.example.paboosyar.RetrofitModels.NetworkAPIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {


    Button loginBtn;
    EditText mUsernameEt;
    EditText mPasswordEt;
    ImageView azzahraaLogo;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loginBtn = findViewById(R.id.activity_login_sign_in_button);
        mUsernameEt = findViewById(R.id.activity_login_username_et);
        mPasswordEt = findViewById(R.id.activity_login_password_tv);
        azzahraaLogo = findViewById(R.id.azzahraa_logo);

        preferences = getApplicationContext().getSharedPreferences(Prefs.MAIN_PREF, 0);
        editor = preferences.edit();
        if(preferences.getString(Prefs.TOKEN, "") != "") {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void signIn(View view) {
        String username = String.valueOf(mUsernameEt.getText());
        String password = String.valueOf(mPasswordEt.getText());




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://account.azzahraa.ir/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        NetworkAPIService retrofitHandler = retrofit.create(NetworkAPIService.class);

        Call<Authentication> call = retrofitHandler.getToken((new User(username, password)));
        call.enqueue(new Callback<Authentication>() {
            @Override
            public void onResponse(Call<Authentication> call, Response<Authentication> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    editor.putString(Prefs.TOKEN, response.body().getToken());
                    editor.commit();
                    Toast.makeText(LoginActivity.this, getString(R.string.welcome_khadem), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.incorrect_pass), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Authentication> call, Throwable t) {
                Toast.makeText(LoginActivity.this, getString(R.string.connection_error) , Toast.LENGTH_LONG).show();
            }
        });

    }
}
