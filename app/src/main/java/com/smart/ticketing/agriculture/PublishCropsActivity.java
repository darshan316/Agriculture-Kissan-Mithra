package com.smart.ticketing.agriculture;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.persistence.DataQueryBuilder;
import com.smart.ticketing.R;
import com.smart.ticketing.Utils;
import com.smart.ticketing.agriculture.data.Items;
import com.smart.ticketing.qless.RegisterCrop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PublishCropsActivity extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText etName;

    @BindView(R.id.editText2)
    EditText etQuantity;

    @BindView(R.id.editText3)
    EditText etAmount;

    @BindView(R.id.editText4)
    EditText etDuration;


    @BindView(R.id.editText5)
    EditText etHarvestedDate;

    @BindView(R.id.button2)
    Button btnSubmit;

    @BindView(R.id.imageview)
    ImageView ivCropimage;

    @BindView(R.id.spin)
    Spinner spinner;

    private Unbinder unbinderknife;
    private String TAG = "RegisterActivity";
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_crop);

        unbinderknife = ButterKnife.bind(this);

        ivCropimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera(100);
            }
        });
        etName.setVisibility(View.GONE);

        fetchData();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name = etName.getText().toString();
                ;
                String amount = etAmount.getText().toString();
                String duration = etDuration.getText().toString();
                String quantity = etQuantity.getText().toString();
                String hDate = etHarvestedDate.getText().toString();

                if (selectedCropName.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter name");
                } else if (quantity.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter quantity");
                } else if (amount.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter amount");
                } else if (duration.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter duration");
                } else if (hDate.equals("")) {
                    Utils.showToast(getApplicationContext(), "Enter harvested date");
                } else if (file == null) {
                    Utils.showToast(getApplicationContext(), "Capture image");
                } else {
                    Items items = new Items();
                    items.setName(selectedCropName);
                    items.setPrice(Integer.parseInt(amount));
                    items.setDuration(Integer.parseInt(duration));
                    items.setHarvestedDate(hDate);
                    items.setAvailableQuantity(Integer.parseInt(quantity));
                    items.setUsername(LoginActivity.username);
                    items.setPhone(LoginActivity.phone);
                    saveFile(items);

                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCropName = nameList.get(i);
                btnSubmit.setText("Market rate: " + registerCrops.get(i).getMarketRate() + " /100kg");

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void saveCrops(Items items, String fileUrl) {

        items.setImageUrl(fileUrl);

        Backendless.Persistence.save(items, new AsyncCallback<Items>() {
            @Override
            public void handleResponse(Items response) {
                Utils.showToast(getApplicationContext(), "Your crop update successfull");
                finish();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Utils.showToast(getApplicationContext(), "failed: " + fault);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinderknife.unbind();
    }


    private static File getOutputMediaFilepath() {
        File mediaStorageDir = new File("/sdcard/cameraface/");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    public void launchCamera(int requestcode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            file = getOutputMediaFilepath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        } else {
            file = getOutputMediaFilepath();
            Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            startActivityForResult(intent, requestcode);
        }
    }


    String fileUrl;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                ivCropimage.setImageURI(Uri.fromFile(file));

            }
        }
    }

    public void saveFile(final Items items) {
        Backendless.Files.upload(file, "/agri", new AsyncCallback<BackendlessFile>() {
            @Override
            public void handleResponse(BackendlessFile response) {
                saveCrops(items, response.getFileURL());
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }

    List<String> nameList = new ArrayList<>();
    String selectedCropName = "";
    List<RegisterCrop> registerCrops = new ArrayList<>();
    public void fetchData() {
        IDataStore<RegisterCrop> querydata = Backendless.Data.of(RegisterCrop.class);
        DataQueryBuilder query = DataQueryBuilder.create();
        query.setPageSize(100);

        querydata.find(query, new AsyncCallback<List<RegisterCrop>>() {
            @Override
            public void handleResponse(List<RegisterCrop> response) {
                if (response.size() > 0) {
                    registerCrops = response;
                    nameList.clear();
                    for (RegisterCrop crop : response) {
                        nameList.add(crop.getName());
                    }

                    ArrayAdapter<String> aa = new ArrayAdapter<String>(PublishCropsActivity.this, android.R.layout.simple_dropdown_item_1line, nameList);
                    spinner.setAdapter(aa);


                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });

    }
}
