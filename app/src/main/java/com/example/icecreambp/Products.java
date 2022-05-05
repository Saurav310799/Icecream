package com.example.icecreambp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Products extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<IceData> dataArrayList = new ArrayList<>();

    IceAdapter mainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        recyclerView = findViewById(R.id.recyclerview);

        mainAdapter = new IceAdapter(Products.this, dataArrayList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mainAdapter);

        getData();

    }

    private void getData() {
        ProgressDialog dialog = new ProgressDialog(Products.this);
        dialog.setMessage("please wait...");
        dialog.setCancelable(false);

        dialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://funked-submissions.000webhostapp.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiService mainInterface = retrofit.create(ApiService.class);

        Call<String> stringCall = mainInterface.STRING_CALL();
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dialog.dismiss();

                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        parseArray(jsonArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(Products.this, "Unable to fetch data ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void parseArray(JSONArray jsonArray) {
        dataArrayList.clear();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                IceData data = new IceData();


                data.setIcecreamflavour(jsonObject.getString("icecreamflavour"));
                data.setPrice(jsonObject.getString("price"));
                data.setImage(jsonObject.getString("image"));

                dataArrayList.add(data);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            mainAdapter = new IceAdapter(Products.this, dataArrayList);
            recyclerView.setAdapter(mainAdapter);
        }

    }
}
