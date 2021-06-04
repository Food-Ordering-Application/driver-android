package com.foa.driver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.foa.driver.dialog.LoadingDialog;
import com.foa.driver.network.RetrofitClient;
import com.foa.driver.network.body.LoginBody;
import com.foa.driver.network.response.LoginData;
import com.foa.driver.network.response.ResponseAdapter;
import com.foa.driver.session.LoginSession;
import com.foa.driver.util.Constants;
import com.foa.driver.util.Helper;
import com.nineoldandroids.animation.Animator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    Context context;
    private EditText txtusername;
    private EditText txtpassword;
    private Button btnLogin;
    private Button btnSaleOffline;
    LinearLayout wrapperLogin;
    private LoadingDialog loading;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        btnLogin =  findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        wrapperLogin = findViewById(R.id.loginWrapper);
        txtusername = findViewById(R.id.txtUserName);
        txtpassword = findViewById(R.id.txtPassword);

        loading = new LoadingDialog(this);
        if (getIntent().getExtras()!=null){
            Bundle bundle  = getIntent().getExtras();
            StringBuilder stringBuilder = new StringBuilder("Extras: \n");
            for(String key: bundle.keySet()){
                Object value = bundle.get(key);
                stringBuilder.append(key+" : "+value+"\n");
            }
        Log.e("hello: ",stringBuilder.toString());
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        String phoneNumber = txtusername.getText().toString();
        String password = txtpassword.getText().toString();
        phoneNumber = "0123123123";
        password= "123123";


        if (phoneNumber.equals("") || password.equals("")) {
            YoYo.with(Techniques.Shake).duration(700).playOn(findViewById(R.id.loginWrapper));
            Toast.makeText(com.foa.driver.LoginActivity.this, getString(R.string.error_field_empty), Toast.LENGTH_SHORT).show();

            return;
        }

        loading.show();
        Call<ResponseAdapter<LoginData>> responseCall = RetrofitClient.getInstance().getAppService()
                .login(new LoginBody(phoneNumber, password));
        responseCall.enqueue(new Callback<ResponseAdapter<LoginData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<LoginData>> call, Response<ResponseAdapter<LoginData>> response) {
                switch (response.code()) {
                    case Constants.STATUS_CODE_SUCCESS:
                        ResponseAdapter<LoginData> res = response.body();
                        if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                            LoginSession.setInstance(res.getData());
                            RetrofitClient.getInstance().setAuthorizationHeader(res.getData().getBearerAccessToken());

                            btnLogin.setEnabled(false);
                            YoYo.with(Techniques.FadeOutDown).interpolate(new OvershootInterpolator()).duration(500).withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator arg0) {
                                    // TODO Auto-generated method stub
                                }

                                @Override
                                public void onAnimationRepeat(Animator arg0) {
                                    // TODO Auto-generated method stub
                                }

                                @Override
                                public void onAnimationEnd(Animator arg0) {
                                    // TODO Auto-generated method stub
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }

                                @Override
                                public void onAnimationCancel(Animator arg0) {
                                    // TODO Auto-generated method stub
                                }
                            }).playOn(findViewById(R.id.loginWrapper));
                        }else{
                            Helper.showFailNotification(context,loading,wrapperLogin,getString(R.string.login_failed));
                        }
                        break;

                    case Constants.STATUS_CODE_UNAUTHORIZED:
                        Helper.showFailNotification(context,loading,wrapperLogin,getString(R.string.error_user_or_password));
                        break;
                    default:
                        Helper.showFailNotification(context,loading,wrapperLogin,getString(R.string.login_failed));
                }
                loading.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseAdapter<LoginData>> call, Throwable t) {
                Log.e("Login Error", t.getMessage());
                Helper.showFailNotification(context,loading,wrapperLogin,getString(R.string.login_failed));

            }
        });
    }
}