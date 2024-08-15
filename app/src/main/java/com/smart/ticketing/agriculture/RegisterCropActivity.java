package com.smart.ticketing.agriculture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.smart.ticketing.R;
import com.smart.ticketing.Utils;
import com.smart.ticketing.common.TexttoSpeechHandler;
import com.smart.ticketing.qless.RegisterCrop;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RegisterCropActivity extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText etName;

    @BindView(R.id.editText2)
    EditText etTotalArea;

    @BindView(R.id.button1)
    Button btnLogin;

    @BindView(R.id.spin)
    Spinner spinner;

    private Unbinder unbinderknife;

    public static String name = "", username, password, phone, address;

    TexttoSpeechHandler tts ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registercrop);

        unbinderknife = ButterKnife.bind(this);
        tts = new TexttoSpeechHandler(RegisterCropActivity.this);
        etName.setVisibility(View.GONE);

        fetchData();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etName.getText().toString();
                final String area = etTotalArea.getText().toString();


                if (selectedCropName.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter cropname");
                } else if (area.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter area");
                } else {

                    IDataStore<RegisterCrop> querydata = Backendless.Data.of(RegisterCrop.class);

                    DataQueryBuilder query = DataQueryBuilder.create();
                    query.setWhereClause("name='" + selectedCropName + "'");

                    querydata.find(query, new AsyncCallback<List<RegisterCrop>>() {
                        @Override
                        public void handleResponse(List<RegisterCrop> response) {
                            if (response.size() > 0) {

                                RegisterCrop crop = response.get(0);
                                int totalArea = crop.getTotalArea() + Integer.parseInt(area);
                                if (totalArea > crop.getMaximumArea()) {

                                    tts.speak("Threshhold reached, can't guaratee amount. Please try another crop");
                                    Utils.showToast(getApplicationContext(), "Threshhold reached, can't guaratee amount. Please try another crop");
                                } else {

                                    crop.setTotalArea(crop.getTotalArea() + Integer.parseInt(area));
                                    Backendless.Data.save(crop, new AsyncCallback<RegisterCrop>() {
                                        @Override
                                        public void handleResponse(RegisterCrop response) {
                                            Utils.showToast(getApplicationContext(), "Registration successfull");
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                            Log.i("Registercrop", "fault: " + fault.getMessage());
                                        }
                                    });
                                }
                            } else {
                                Utils.showToast(getApplicationContext(), "Crop not found");
                            }
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.i("Registercrop", "fault: " + fault.getMessage());
                        }
                    });


                }

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCropName = nameList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinderknife.unbind();

        if(tts != null){
            tts.stop();
        }
    }

    List<String> nameList = new ArrayList<>();
    String selectedCropName = "";

    public void fetchData() {
        IDataStore<RegisterCrop> querydata = Backendless.Data.of(RegisterCrop.class);
        DataQueryBuilder query = DataQueryBuilder.create();
        query.setPageSize(100);
        querydata.find(query, new AsyncCallback<List<RegisterCrop>>() {
            @Override
            public void handleResponse(List<RegisterCrop> response) {
                if (response.size() > 0) {
                    nameList.clear();
                    for (RegisterCrop crop : response) {
                        nameList.add(crop.getName());
                    }
                    ArrayAdapter<String> aa = new ArrayAdapter<String>(RegisterCropActivity.this, android.R.layout.simple_dropdown_item_1line, nameList);
                    spinner.setAdapter(aa);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.i("Registercrop", fault.toString());
            }
        });

    }
}
