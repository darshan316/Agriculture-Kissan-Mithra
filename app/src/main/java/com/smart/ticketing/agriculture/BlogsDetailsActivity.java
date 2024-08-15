package com.smart.ticketing.agriculture;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.smart.ticketing.R;
import com.smart.ticketing.Utils;
import com.smart.ticketing.agriculture.data.Blogs;

import java.util.ArrayList;
import java.util.List;

public class BlogsDetailsActivity extends AppCompatActivity {


    TextView tvTopic, tvDesc, tvAuthor, tvLikeCount, tvDislikeCount, tvLink;
    Button btnLike, btnDislike, btnNewBlog;


    private String TAG = "ViewBooksActivity";

    List<Blogs> blogsList = new ArrayList<>();
    List<String> nameList = new ArrayList<>();

    Blogs blog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agri_blogs_view);


        blog = (Blogs) getIntent().getSerializableExtra("blog");

        tvTopic = findViewById(R.id.topic);
        tvDesc = findViewById(R.id.description);
        tvAuthor = findViewById(R.id.author);
        btnLike = findViewById(R.id.like);
        btnDislike = findViewById(R.id.dislike);
        btnNewBlog = findViewById(R.id.addnewblog);
        tvLikeCount = findViewById(R.id.likecount);
        tvLink = findViewById(R.id.link);
        tvDislikeCount = findViewById(R.id.dislikecount);

        tvTopic.setText(blog.getTopic());
        tvDesc.setText(blog.getDescription());
        tvDislikeCount.setText(blog.getDislikeCount() + "");
        tvLikeCount.setText(blog.getLikeCount() + "");
        tvAuthor.setText(blog.getUsername());
        if (blog.getLink() == null || blog.getLink().equals("")) {
            tvLink.setVisibility(View.GONE);
        } else {
            tvLink.setText(blog.getLink());
        }


        tvLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(tvLink.getText().toString());
                uri = Uri.parse( "vnd.youtube:" + uri.getQueryParameter( "v" ) );
                startActivity( new Intent( Intent.ACTION_VIEW, uri ) );

            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLike.setEnabled(false);
                int like = blog.getLikeCount() + 1;
                blog.setLikeCount(like);
                tvLikeCount.setText(like + "");
                Backendless.Persistence.save(blog, new AsyncCallback<Blogs>() {
                    @Override
                    public void handleResponse(Blogs response) {
                        Utils.showToast(getApplicationContext(), "like updated");
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Utils.showToast(getApplicationContext(), "like update error : " + fault.getMessage());
                    }
                });
            }
        });

        btnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnDislike.setEnabled(false);
                int like = blog.getDislikeCount() + 1;
                blog.setDislikeCount(like);

                tvDislikeCount.setText(like + "");
                Backendless.Persistence.save(blog, new AsyncCallback<Blogs>() {
                    @Override
                    public void handleResponse(Blogs response) {
                        Utils.showToast(getApplicationContext(), "Dislike updated");
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Utils.showToast(getApplicationContext(), "dislike update error : " + fault.getMessage());
                    }
                });
            }
        });


        btnNewBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BlogsDetailsActivity.this, AddBlogActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
