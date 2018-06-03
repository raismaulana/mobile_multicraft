package com.multicraftbusiness.mobile_multicraft.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.multicraftbusiness.mobile_multicraft.AppSingleton;
import com.multicraftbusiness.mobile_multicraft.listproduk.ListProdukActivity;
import com.multicraftbusiness.mobile_multicraft.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements HomeAdapter.OnItemClickListener {

    private static final String URL_FOR_HOME = "http://tifb.multicraftbusiness.com/mhome/Home/kategori";
    private static final String TAG = HomeActivity.class.getSimpleName();

    public static final String EXTRA_ID = "id_kategori";
    public static final String EXTRA_COVER_KATEGORI = "cover_kategori";
    public static final String EXTRA_NAMA_KATEGORI = "nama_kategori";

    private HomeAdapter mHomeAdapter;
    private ArrayList<Home> mHomeList;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mRecyclerView = findViewById(R.id.rvhome);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mHomeList = new ArrayList<>();

        loadData();

        mHomeAdapter = new HomeAdapter(HomeActivity.this, mHomeList);
        mRecyclerView.setAdapter(mHomeAdapter);

    }

    private void loadData() {
        String cancel_req_tag = "home";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FOR_HOME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Home Response: " + response.toString());
                try {

                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject kategori = array.getJSONObject(i);

                        mHomeList.add(new Home(
                                kategori.getInt("id_kategori"),
                                kategori.getString("nama_kategori"),
                                kategori.getString("gambar_kategori"),
                                kategori.getString("cover_kategori")
                        ));
                    }
                    mHomeAdapter.notifyDataSetChanged();
                    mHomeAdapter.setOnItemClickListener(HomeActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Registration Error: " + error.getMessage());

                    }
                });
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest, cancel_req_tag);
    }

    @Override
    public void onItemClick(int position) {
        Intent kelistproduk = new Intent(this, ListProdukActivity.class);
        Home clickedItem = mHomeList.get(position);

        kelistproduk.putExtra(EXTRA_ID, clickedItem.getId_kategori());
        kelistproduk.putExtra(EXTRA_COVER_KATEGORI, clickedItem.getCover_kategori());
        kelistproduk.putExtra(EXTRA_NAMA_KATEGORI, clickedItem.getNama_kategori());
        Log.d(TAG, "onItemClick: "+ clickedItem.getId_kategori());
        Log.d(TAG, "onItemClick: "+ clickedItem.getCover_kategori());
        Log.d(TAG, "onItemClick: "+ clickedItem.getNama_kategori());

        startActivity(kelistproduk);
    }
}
