package com.smart.ticketing.agriculture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.smart.ticketing.R;
import com.smart.ticketing.Utils;
import com.smart.ticketing.agriculture.data.Items;
import com.smart.ticketing.backendless.data.Pass;
import com.smart.ticketing.qless.ViewCartActivity;
import com.smart.ticketing.qless.ViewCustomItemActivity;
import com.smart.ticketing.scanner.CaptureActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ItemsListActivity extends AppCompatActivity {


    private final String TAG = "MainActivity";

    @BindView(R.id.viewcart)
    Button btnViewCart;

    @BindView(R.id.offers)
    Button btnOffers;

    @BindView(R.id.buyitems)
    Button btnBuyItems;

    @BindView(R.id.listview)
    ListView listView;

    private Unbinder unbinderknife;

    static String name = "";

    Pass ads = new Pass();

    private int CAMERA_REQUEST = 100;

    String filePath = "";

    boolean isUpdate;

    Map<String, List<String>> map = new HashMap<>();

    List<String> malls = new ArrayList<>();

    public static List<Items> cartList = new ArrayList<>();

    int totalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qless_items_list);

        unbinderknife = ButterKnife.bind(this);

        btnOffers.setVisibility(View.GONE);
        btnBuyItems.setVisibility(View.GONE);
        btnViewCart.setVisibility(View.GONE);

        fetchData();


        btnOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemsListActivity.this, CaptureActivity.class);
                startActivity(intent);
            }
        });

        btnViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ItemsListActivity.this, ViewCartActivity.class);
                startActivity(intent);
            }
        });

        btnBuyItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ItemsListActivity.this, ViewCustomItemActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ItemsListActivity.this, OrdersActivity.class);
                intent.putExtra("crop", itemList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinderknife.unbind();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
        }
    }

    public void fetchData() {
        IDataStore<Items> items = Backendless.Data.of(Items.class);
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setSortBy("name");
        queryBuilder.setPageSize(100);

        items.find(queryBuilder, new AsyncCallback<List<Items>>() {
            @Override
            public void handleResponse(List<Items> itemslist) {
                Log.i(TAG, "Retrieved " + itemslist.size() + " objects");

                if (itemslist.size() > 0) {

                    itemslist = filterZeroQuantity(itemslist);
                    itemList = itemslist;
//                    Utils.showToast(ItemsListActivity.this, "Items available: "+cartList.size());
                    CustomAdapter ca = new CustomAdapter(ItemsListActivity.this, itemslist);
                    listView.setAdapter(ca);
                } else {
                    Utils.showToast(getApplicationContext(), "No items available");
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "Server reported an error " + fault.getMessage());
            }
        });
    }

    List<Items> itemList = new ArrayList<>();
    public List<Items> filterZeroQuantity(List<Items> itemsList){
        List<Items> filteredItems = new ArrayList<>();
        filteredItems.addAll(itemsList);
        for(Items item : itemsList){
            if(item.getAvailableQuantity() == 0){
                filteredItems.remove(item);
            }
        }
        return filteredItems;
    }

    public class CustomAdapter extends BaseAdapter {

        List<Items> itemsList;

        Context mContext;

        ViewHolder viewHolder;

        public CustomAdapter(Context mContext, List<Items> itemslist) {
            this.mContext = mContext;
            this.itemsList = itemslist;
        }

        @Override
        public int getCount() {
            return itemsList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final View result;

            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(R.layout.items_list_rows, parent, false);

                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.textview);
                viewHolder.tvQuantity = (TextView) convertView.findViewById(R.id.quantity);
                viewHolder.btnAddtoCart = (Button) convertView.findViewById(R.id.addtocart);
                result = convertView;
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                result = convertView;
            }

            final Items item = itemsList.get(position);

            Picasso.with(mContext).load(item.getImageUrl()).into(viewHolder.imageView);
            viewHolder.textView.setText(item.getName() + "\n"+ item.getAvailableQuantity() + " Kg\n" + item.getPrice()+" Rs/Kg\nHarvestedDate: "+item.getHarvestedDate()+"\n"+item.getPhone());
            viewHolder.tvQuantity.setText(item.getAvailableQuantity() + "");

            viewHolder.btnAddtoCart.setVisibility(View.GONE);
            return result;
        }


        public class ViewHolder {
            TextView textView;
            ImageView imageView;
            Button btnAddtoCart;
            TextView tvQuantity;
        }
    }
}
