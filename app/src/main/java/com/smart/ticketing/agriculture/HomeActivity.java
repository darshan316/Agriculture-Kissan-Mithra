package com.smart.ticketing.agriculture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.smart.ticketing.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeActivity extends AppCompatActivity {


    @BindView(R.id.price)
    Button btnTransfer;


    @BindView(R.id.prep)
    Button btnPreProduction;

    @BindView(R.id.pop)
    Button btnPostProduction;

    @BindView(R.id.production)
    Button btnBalance;

    @BindView(R.id.vieworders)
    Button btnViewOrders;

    @BindView(R.id.subsidy)
    Button btnBlogs;

    @BindView(R.id.viewotherlands)
    Button btnViewLands;

    @BindView(R.id.viewItems)
    Button btnCropsInfo;

    private String TAG = "ViewBooksActivity";
    private Unbinder unbinderknife;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agai_home);
        unbinderknife = ButterKnife.bind(this);


        if (LoginActivity.userType.equalsIgnoreCase("customer")) {
            btnCropsInfo.setVisibility(View.VISIBLE);
//            btnViewLands.setVisibility(View.GONE);
            btnViewOrders.setVisibility(View.GONE);
            btnTransfer.setVisibility(View.GONE);
            btnBlogs.setVisibility(View.GONE);
            btnPreProduction.setVisibility(View.GONE);
            btnPostProduction.setVisibility(View.GONE);
//            btnBalance.setVisibility(View.GONE);

        } else {

            btnCropsInfo.setVisibility(View.GONE);
            btnViewLands.setVisibility(View.GONE);
            btnViewOrders.setVisibility(View.GONE);
            btnTransfer.setVisibility(View.GONE);
            btnBlogs.setVisibility(View.VISIBLE);
            btnBlogs.setText("Blogs");
            btnPreProduction.setVisibility(View.VISIBLE);
            btnPostProduction.setVisibility(View.VISIBLE);
            btnBalance.setVisibility(View.GONE);
            btnBalance.setText("Balance: "+LoginActivity.balance);
            btnTransfer.setText("Transfer");
            btnTransfer.setVisibility(View.GONE);
            btnCropsInfo.setVisibility(View.VISIBLE);
            btnCropsInfo.setText("Crops Info");

        }


        btnCropsInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CropDetailsList.class);
                startActivity(intent);
            }
        });


        btnViewLands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ViewLandsActivity.class);
                startActivity(intent);
            }
        });

        btnPreProduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, RegisterCropActivity.class);
                startActivity(intent);
            }
        });

       /* btnBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, LandPledgeActivity.class);
                startActivity(intent);
            }
        });*/

        btnPostProduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PublishCropsActivity.class);
                startActivity(intent);
            }
        });

        btnBlogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ViewBlogsActivity.class);
                startActivity(intent);
            }
        });

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TransferActivity.class);
                startActivity(intent);
            }
        });

        btnViewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ViewOrdersActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onResume(){
        super.onResume();
        btnBalance.setText("Balance: "+LoginActivity.balance);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinderknife.unbind();
    }
}
