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
import com.smart.ticketing.agriculture.data.Blogs;

import java.util.ArrayList;
import java.util.List;

public class AddBlogActivity extends AppCompatActivity {


    EditText etTopic, etDesc, etLinks;
    Button btnSubmit;

    private String TAG = "ViewBooksActivity";

    List<Blogs> blogsList = new ArrayList<>();
    List<String> nameList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agri_blogs_add);


        etTopic = (EditText) findViewById(R.id.topic);
        etDesc = (EditText) findViewById(R.id.desc);
        etLinks = (EditText) findViewById(R.id.links);
        btnSubmit = (Button) findViewById(R.id.submit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = etTopic.getText().toString();
                String desc = etDesc.getText().toString();
                String links = etLinks.getText().toString();

                if (name.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter topic");
                    return;
                }

                if (desc.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter description");
                    return;
                }

                Blogs blog = new Blogs();
                blog.setTopic(name);
                blog.setDescription(desc);
                blog.setUsername(LoginActivity.username);
                blog.setName(LoginActivity.name);
                blog.setLikeCount(0);
                blog.setDislikeCount(0);
                blog.setLink(links == null ? "" : links);

                Backendless.Persistence.save(blog, new AsyncCallback<Blogs>() {
                    @Override
                    public void handleResponse(Blogs response) {
                        Utils.showToast(getApplicationContext(), "Blogs posted successfully");
                        finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Utils.showToast(getApplicationContext(), "Blogs post error");
                    }
                });

            }
        });


    }

}
