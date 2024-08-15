package com.smart.ticketing.agriculture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.smart.ticketing.R;
import com.smart.ticketing.Utils;
import com.smart.ticketing.agriculture.data.Users;
import com.smart.ticketing.backendless.SplashActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText etUsernmae;

    @BindView(R.id.editText2)
    EditText etPassword;

    @BindView(R.id.button1)
    Button btnLogin;

    @BindView(R.id.button2)
    Button btnRegister;

    @BindView(R.id.forgotpassword)
    Button btnForgotpassword;


    private Unbinder unbinderknife;

    public static String name = "", username, password, phone, address, industry, userType = "";
    public static Users user;
    public static int balance = 100;
    private String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        unbinderknife = ButterKnife.bind(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnForgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, com.smart.ticketing.agriculture.ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        btnForgotpassword.setVisibility(View.GONE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = etUsernmae.getText().toString();
                String password = etPassword.getText().toString();

//                username = "daya@gmail.com";
//                password = "1234";

                if (username.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter username");
                } else if (password.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter password");
                } else {


                    IDataStore<Users> querydata = Backendless.Data.of(Users.class);
                    DataQueryBuilder query = DataQueryBuilder.create();
                    query.setWhereClause("username='" + username + "' and pass='" + password + "' and userType='farmer'");

                    querydata.find(query, new AsyncCallback<List<Users>>() {
                        @Override
                        public void handleResponse(List<Users> users) {

                            if (users.size() > 0) {

                                Utils.showToast(LoginActivity.this, "Login successfull");
                                Intent intent = new Intent(LoginActivity.this, SplashActivity.sClass);
                                startActivity(intent);
                                name = users.get(0).getName();
                                LoginActivity.username = users.get(0).getUsername();
                                LoginActivity.password = users.get(0).getPassword();
                                phone = users.get(0).getPhone();
                                address = users.get(0).getAddress();
                                industry = users.get(0).getIndustry();
                                userType = users.get(0).getUserType();
                                balance = users.get(0).getBalance();
                                user = users.get(0);

                            } else {
                                Utils.showToast(getApplicationContext(), "Invalid credentials");
                            }
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.i(TAG, "Login error: "+fault.getMessage());
                            Utils.showToast(getApplicationContext(), "Login error: " + fault);
                        }
                    });

                }

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinderknife.unbind();

    }

}
