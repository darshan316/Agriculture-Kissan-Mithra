package com.smart.ticketing.agriculture;

import android.content.Intent;
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
import com.smart.ticketing.agriculture.data.Blogs;

import java.util.ArrayList;
import java.util.List;

public class ViewBlogsActivity extends AppCompatActivity {


    ListView lvDiseses;
    EditText etDiseaseName;
    Button btnSubmit;

    private String TAG = "ViewBooksActivity";

    List<Blogs> blogsList = new ArrayList<>();
    List<String> nameList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_issues);


        lvDiseses = (ListView) findViewById(R.id.listview);
        etDiseaseName = (EditText) findViewById(R.id.editText);
        btnSubmit = (Button) findViewById(R.id.button1);
        btnSubmit.setText("Write Blog");
//        btnSubmit.setVisibility(View.GONE);
        etDiseaseName.setVisibility(View.GONE);

        fetchData();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(ViewBlogsActivity.this, AddBlogActivity.class);
                startActivity(browserIntent);
            }
        });

        lvDiseses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent browserIntent = new Intent(ViewBlogsActivity.this, BlogsDetailsActivity.class);
                browserIntent.putExtra("blog", blogsList.get(i));
                startActivity(browserIntent);
            }
        });
    }


    public void fetchData() {
        IDataStore<Blogs> querydata = Backendless.Data.of(Blogs.class);
        DataQueryBuilder query = DataQueryBuilder.create();
        query.setPageSize(100);

        querydata.find(query, new AsyncCallback<List<Blogs>>() {
            @Override
            public void handleResponse(List<Blogs> response) {
                if (response.size() > 0) {
                    blogsList = response;
                    updateList();
                } else {
                    Utils.showToast(getApplicationContext(), "No blogs found.");
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {

                Utils.showToast(getApplicationContext(), "Error in blogs: "+fault);
            }
        });
    }

    public void updateList(){
        nameList.clear();
        for(Blogs blog : blogsList){
            nameList.add(blog.getTopic()+"\nBy: "+blog.getUsername());
        }

        ArrayAdapter<String> aa = new ArrayAdapter<String>(ViewBlogsActivity.this, android.R.layout.simple_list_item_1, nameList);
        lvDiseses.setAdapter(aa);
    }

}
