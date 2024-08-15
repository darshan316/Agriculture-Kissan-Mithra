package com.smart.ticketing.agriculture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.smart.ticketing.R;
import com.smart.ticketing.Utils;
import com.smart.ticketing.agriculture.data.Users;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText etUsernmae;

    @BindView(R.id.editText2)
    EditText etPassword;

    @BindView(R.id.editText3)
    EditText etName;

    @BindView(R.id.editText4)
    EditText etPhone;

    @BindView(R.id.editText5)
    EditText etAddress;

    @BindView(R.id.editText6)
    EditText etLand;

    @BindView(R.id.editText7)
    EditText etCropsGrown;

    @BindView(R.id.button2)
    Button btnSubmit;

    @BindView(R.id.usertype)
    RadioGroup radioGroup;

    @BindView(R.id.customer)
    RadioButton rbCustomer;

    @BindView(R.id.farmer)
    RadioButton rbFarmer;

    private Unbinder unbinderknife;
    private String TAG = "RegisterActivity";
    String usertype ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agri_register);

        unbinderknife = ButterKnife.bind(this);



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.i(TAG, "Index value: "+i);
                if(radioGroup.getCheckedRadioButtonId() == R.id.customer){
                    etCropsGrown.setHint("industry/enterprise");
                    etLand.setVisibility(View.GONE);
                    usertype = "customer";
                }else {
                    usertype = "farmer";
                    etCropsGrown.setHint("majorCrops grown comma(,) separated");
                    etLand.setVisibility(View.VISIBLE);
                }
            }
        });

        rbFarmer.setChecked(true);

        radioGroup.setVisibility(View.INVISIBLE);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String username = etUsernmae.getText().toString();
                String password = etPassword.getText().toString();
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                String address = etAddress.getText().toString();
                String crops = etCropsGrown.getText().toString();
                String land = etLand.getText().toString();

                if (username.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter username");
                } else if (password.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter password");
                } else if (name.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter aadharId");
                } else if (phone.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter phone");
                } else if (address.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter address");
                } else if(etLand.getText().equals("")){
                    Utils.showToast(getApplicationContext(), "Enter land area");
                } else {

                    if(name.length() != 12){
                        Utils.showToast(getApplication(), "Invalid aadharId");
                        return;
                    }
                    if (phone.length() != 10) {
                        Utils.showToast(getApplicationContext(), "Invalid phone");
                    } else {



//                        User user = new User();
//                        user.setAddress(address);
//                        user.setUsertype(usertype);
//                        user.setUsername(username);
//                        user.setName(name);
//                        user.setPhone(phone);

                        final BackendlessUser user = new BackendlessUser();

                        Random random = new Random();
                        int rand = random.nextInt(100000000);
                        user.setEmail("example"+rand +"@gmail.com");
                        user.setPassword(password);
                        user.setProperty("username", username);
                        user.setProperty("name", name);
                        user.setProperty("pass" , password);
                        user.setProperty("phone", phone);
                        user.setProperty("address", address);
                        user.setProperty("userType", "farmer");
                        user.setProperty("cropsGrown", crops);
                        user.setProperty("balance", 5000);

                        /*if(!usertype.equals("farmer")){
                            user.setProperty("industry", crops);
                        }*/

                        IDataStore<Users> querydata = Backendless.Data.of(Users.class);

                        DataQueryBuilder query = DataQueryBuilder.create();
                        query.setWhereClause("name='" + name + "'");

                        querydata.find(query, new AsyncCallback<List<Users>>() {
                            @Override
                            public void handleResponse(List<Users> users) {
                                if (users.size() > 0) {
                                    Utils.showToast(getApplicationContext(), "Aadhar number exists");
                                } else {

                                    Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                                        @Override
                                        public void handleResponse(BackendlessUser response) {
                                            Log.i(TAG, "user register successfull");
                                            Utils.showToast(getApplicationContext(), "Registration successfull");
                                            finish();
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                            Log.i(TAG, "user register error, "+fault);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Utils.showToast(getApplicationContext(), "Login error: "+fault);
                            }
                        });
                    }
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
