package com.smart.ticketing.agriculture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.smart.ticketing.R;
import com.smart.ticketing.Utils;
import com.smart.ticketing.agriculture.data.Items;
import com.smart.ticketing.agriculture.data.Orders;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OrdersActivity extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText etQuantity;

    @BindView(R.id.editText2)
    EditText etDate;

    @BindView(R.id.button2)
    Button btnSubmit;


    private Unbinder unbinderknife;
    private String TAG = "RegisterActivity";

    Items item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agri_order);

        unbinderknife = ButterKnife.bind(this);

        item = (Items) getIntent().getSerializableExtra("crop");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String quntity = etQuantity.getText().toString();
                ;
                String date = etDate.getText().toString();

                if (quntity.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter quantity");
                } else if (date.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter date");
                } else {

                    Orders orders = new Orders();
                    orders.setQuantity(Integer.parseInt(quntity));
                    orders.setBuyDate(date);
                    orders.setPhone(LoginActivity.phone);
                    orders.setName(LoginActivity.username);
                    orders.setUsername(LoginActivity.username);
                    orders.setCropId(item.getObjectId());
                    orders.setFarmerusername(item.getUsername());
                    orders.setCropName(item.getName());
                    orders.setAmount(item.getPrice() * Integer.parseInt(quntity));

                    Backendless.Persistence.save(orders, new AsyncCallback<Orders>() {
                        @Override
                        public void handleResponse(Orders response) {
//                            Utils.showToast(getApplicationContext(), "Your item ordered successfully");
//                            finish();

                            item.setAvailableQuantity(item.getAvailableQuantity() - Integer.parseInt(quntity));
                            Backendless.Persistence.save(item, new AsyncCallback<Items>() {
                                @Override
                                public void handleResponse(Items response) {
                                    Log.i(TAG, "Your item ordered successfully " + response.getAvailableQuantity());
                                    finish();
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Log.i(TAG, "fault: "+ fault);
                                }
                            });
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.i(TAG, "ORDER failed: " + fault);
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
