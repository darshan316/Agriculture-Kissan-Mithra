package com.smart.ticketing.agriculture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.smart.ticketing.R;
import com.smart.ticketing.Utils;
import com.smart.ticketing.agriculture.data.Orders;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PriceCalculatorActivity extends AppCompatActivity {


    @BindView(R.id.editText0)
    EditText etCropname;

    @BindView(R.id.editText)
    EditText etSeeds;

    @BindView(R.id.editText2)
    EditText etLabour;

    @BindView(R.id.editText3)
    EditText etFertilizer;

    @BindView(R.id.editText4)
    EditText etTransport;

    @BindView(R.id.editText5)
    EditText etMiscealaneous;

    @BindView(R.id.button2)
    Button btnSubmit;

    @BindView(R.id.text)
    TextView tvText;

    private Unbinder unbinderknife;
    private String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agri_price);

        unbinderknife = ButterKnife.bind(this);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String cropname = etCropname.getText().toString();
                String seeds = etSeeds.getText().toString();

                String fertilizer = etFertilizer.getText().toString();
                String transport = etTransport.getText().toString();
                String labour = etLabour.getText().toString();
                String miscellaneous = etMiscealaneous.getText().toString();

                if(cropname.equals(cropname.equals(""))){
                    Utils.showToast(getApplicationContext(), "Enter cropname");
                }else if (seeds.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter seeds");
                } else if (labour.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter labour");
                } else if (fertilizer.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter fertilizer");
                } else if (transport.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter transport");
                } else if (etMiscealaneous.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter miscellaneous");
                } else {

                    int totalExp = convertint(seeds) + convertint(fertilizer) + convertint(transport) + convertint(miscellaneous) + convertint(labour);
                    fetchData(totalExp, cropname);
                }
            }
        });
    }


    public int convertint(String str){
        return Integer.parseInt(str);
    }
    int totalAmount = 0;

    public void fetchData(final int totalexp, String cropname) {
        IDataStore<Orders> querydata = Backendless.Data.of(Orders.class);
        DataQueryBuilder query = DataQueryBuilder.create();
        query.setWhereClause("farmerUsername = '"+ LoginActivity.username+"' and cropName = '"+cropname+"'");
        query.setPageSize(100);

        querydata.find(query, new AsyncCallback<List<Orders>>() {
            @Override
            public void handleResponse(List<Orders> response) {
                if (response.size() > 0) {
                    totalAmount = 0;
                    for(Orders orders : response){
                        totalAmount = totalAmount + orders.getAmount();

                        int profit = totalAmount - totalexp;
                        if(profit < 0){
                            tvText.setText("Total Expenditure: " + totalexp + "\nTotal Loss: "+profit);
                        }else{
                            tvText.setText("Total Expenditure: " + totalexp + "\nTotal profit: "+profit);
                        }
                    }
                } else {
                    Utils.showToast(getApplicationContext(), "No orders found yet");
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
