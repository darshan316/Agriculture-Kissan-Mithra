package com.smart.ticketing.agriculture;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.smart.ticketing.R;
import com.smart.ticketing.Utils;
import com.smart.ticketing.agriculture.data.LandPledge;

import java.util.ArrayList;
import java.util.List;

public class ViewLandsActivity extends AppCompatActivity {


    ListView lvDiseses;
    EditText etDiseaseName;
    Button btnSubmit;

    private String TAG = "ViewBooksActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_issues);


        lvDiseses = (ListView) findViewById(R.id.listview);
        etDiseaseName = (EditText) findViewById(R.id.editText);
        btnSubmit = (Button) findViewById(R.id.button1);
        btnSubmit.setVisibility(View.GONE);
        etDiseaseName.setVisibility(View.GONE);

        fetchDoctors(getIntent().getStringExtra("name"));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = etDiseaseName.getText().toString();
                if(name.equals("")){
                    Utils.showToast(getApplicationContext(), "Enter name");
                    return;
                }
            }
        });

        lvDiseses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + landsList.get(i).getPhone()));
                startActivity(intent);
            }
        });
    }


    public void fetchDoctors(String name) {
        IDataStore<LandPledge> querydata = Backendless.Data.of(LandPledge.class);
        DataQueryBuilder query = DataQueryBuilder.create();
        query.setPageSize(100);

        querydata.find(query, new AsyncCallback<List<LandPledge>>() {
            @Override
            public void handleResponse(List<LandPledge> response) {
                if (response.size() > 0) {
                    landsList = response;
                    updateList();
                } else {
                    Utils.showToast(getApplicationContext(), "Details did not match. Verify details and try again.");
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
            }
        });
    }

    List<LandPledge> landsList = new ArrayList<>();
    List<String> nameList = new ArrayList<>();
    public void updateList(){
        nameList.clear();
        for(LandPledge land : landsList){
            nameList.add("Name: " + land.getName()+"\nArea: "+land.getArea()+"\nAddress: "+land.getAddress()+"\nPhone: "+land.getPhone()+"\nExpected Amount: "+land.getAmount() + "\nDuration: "+land.getDuration());
        }

        ArrayAdapter<String> aa = new ArrayAdapter<String>(ViewLandsActivity.this, android.R.layout.simple_list_item_1, nameList);
        lvDiseses.setAdapter(aa);
    }

}
