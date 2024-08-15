package com.smart.ticketing.agriculture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.smart.ticketing.R;
import com.smart.ticketing.Utils;
import com.smart.ticketing.agriculture.data.LandPledge;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LandPledgeActivity extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText etLandArea;

    @BindView(R.id.editText2)
    EditText etAddress;

    @BindView(R.id.editText3)
    EditText etAmount;

    @BindView(R.id.editText4)
    EditText etDuration;

    @BindView(R.id.button2)
    Button btnSubmit;


    private Unbinder unbinderknife;
    private String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agri_production);

        unbinderknife = ButterKnife.bind(this);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String landarea = etLandArea.getText().toString();
                ;
                String amount = etAmount.getText().toString();
                String duration = etDuration.getText().toString();
                String address = etAddress.getText().toString();

                if (landarea.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter landarea");
                } else if (address.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter address");
                } else if (amount.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter amount");
                } else if (duration.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter duraion");
                } else {
                    LandPledge land = new LandPledge();
                    land.setArea(Integer.parseInt(landarea));
                    land.setAmount(Integer.parseInt(amount));
                    land.setDuration(Integer.parseInt(duration));
                    land.setAddress(address);
                    land.setPhone(LoginActivity.phone);
                    land.setName(LoginActivity.username);

                    Backendless.Persistence.save(land, new AsyncCallback<LandPledge>() {
                        @Override
                        public void handleResponse(LandPledge response) {
                            Utils.showToast(getApplicationContext(), "Request submitted, interested will call you");
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Utils.showToast(getApplicationContext(), "Registration failed: " + fault);
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
