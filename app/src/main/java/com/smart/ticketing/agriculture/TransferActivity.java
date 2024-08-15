package com.smart.ticketing.agriculture;

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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TransferActivity extends AppCompatActivity {


    @BindView(R.id.editText0)
    EditText etMobile;

    @BindView(R.id.editText)
    EditText etAmount;


    @BindView(R.id.button2)
    Button btnSubmit;


    private Unbinder unbinderknife;
    private String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agri_transfer);

        unbinderknife = ButterKnife.bind(this);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String mobile = etMobile.getText().toString();
                String amount = etAmount.getText().toString();



                if(mobile.equals(mobile.equals(""))){
                    Utils.showToast(getApplicationContext(), "Enter mobile");
                }else if (amount.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter amount");
                }  else {


                    if(LoginActivity.balance > Integer.parseInt(amount)){
                        fetchData(Integer.parseInt(amount), mobile);
                    }else{
                        Utils.showToast(getApplicationContext(), "Insufficient funds");
                    }

                }
            }
        });
    }


    public int convertint(String str){
        return Integer.parseInt(str);
    }
    int totalAmount = 0;

    public void fetchData(final int amont, String mobile) {


        IDataStore<Users> querydata = Backendless.Data.of(Users.class);
        DataQueryBuilder query = DataQueryBuilder.create();
        query.setWhereClause("phone = '"+ mobile+"'");
        query.setPageSize(100);

        querydata.find(query, new AsyncCallback<List<Users>>() {
            @Override
            public void handleResponse(List<Users> response) {
                if (response.size() > 0) {

                    int currentBal = response.get(0).getBalance();
                    int newBal = currentBal + amont;
                    response.get(0).setBalance(newBal);

                    Backendless.Persistence.save(response.get(0), new AsyncCallback<Users>() {
                        @Override
                        public void handleResponse(Users response) {
                            int curBal = LoginActivity.user.getBalance();
                            final int newBal = curBal - amont;
                            LoginActivity.user.setBalance(newBal);
                            Backendless.Persistence.save(LoginActivity.user, new AsyncCallback<Users>() {
                                @Override
                                public void handleResponse(Users response) {
                                    Utils.showToast(getApplicationContext(), "balance transfer succeesfull");
                                    LoginActivity.balance = newBal;
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Utils.showToast(getApplicationContext(), "balance transfer error: "+fault.getMessage());
                                }
                            });
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Utils.showToast(getApplicationContext(), "balance transfer error: "+fault.getMessage());
                        }
                    });
                } else {
                    Utils.showToast(getApplicationContext(), "Phone number not register");
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.i(TAG, "fault: "+fault);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinderknife.unbind();
    }
}
