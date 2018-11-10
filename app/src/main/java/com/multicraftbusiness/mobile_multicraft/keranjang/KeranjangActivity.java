package com.multicraftbusiness.mobile_multicraft.keranjang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.multicraftbusiness.mobile_multicraft.AppSingleton;
import com.multicraftbusiness.mobile_multicraft.R;
import com.multicraftbusiness.mobile_multicraft.RegisterActivity;
import com.multicraftbusiness.mobile_multicraft.checkout.MainActivity;
import com.multicraftbusiness.mobile_multicraft.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.multicraftbusiness.mobile_multicraft.home.HomeActivity.EXTRA_COVER_KATEGORI;
import static com.multicraftbusiness.mobile_multicraft.home.HomeActivity.EXTRA_ID;
import static com.multicraftbusiness.mobile_multicraft.home.HomeActivity.EXTRA_NAMA_KATEGORI;

public class KeranjangActivity extends AppCompatActivity {
    private static final String TAG = KeranjangActivity.class.getSimpleName();
    private static final String URL_FOR_AMBIL_KERANJANG = "http://tifb.multicraftbusiness.com/mkeranjang/Keranjang/ambil_keranjang"; //url controller webservice
    private static final String URL_FOR_ADDLESS_KERANJANG = "http://tifb.multicraftbusiness.com/mkeranjang/Keranjang/tambah_kurang"; //url controller webservice
    private static final String URL_FOR_DELETE_KERANJANG = "http://tifb.multicraftbusiness.com/mkeranjang/Keranjang/hapus_dari_keranjang"; //url controller webservice

    private RecyclerView mRecyclerView;
    private KeranjangAdapter mKeranjangAdapter;
    private List<Keranjang> mKeranjang;
    public SharedPreferences sharedPreferences;
    public RecyclerView.LayoutManager mLayoutManager;

    TextView keranjangTotalHargaPesan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();


        sharedPreferences = this.getSharedPreferences("data_pengguna", Context.MODE_PRIVATE);
        int int_id_user = sharedPreferences.getInt("id_user", 0);
        String id_user = Integer.toString(int_id_user);
        Log.d(TAG, "onCreate: "+id_user);

        keranjangTotalHargaPesan = findViewById(R.id.total_harga_pesan_keranjang);
        loadData(id_user);
        buildTampilan();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KeranjangActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

//    public void checkOut(final String id_user, final String nama_penerima, final String no_hp_penerima,
//                         final String alamat_penerima, final String catatan, final String kurir,
//                         final String rekening){
//        String cancel_req_tag = "checkout";
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                , new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "checkout Response: " + response.toString());
//
//                try {
//                    JSONArray array = new JSONArray(response);
//
//                    for (int i = 0; i < array.length(); i++) {
//
//                        JSONObject keranjang = array.getJSONObject(i);
//                        mKeranjang.add(new Keranjang(
//                                keranjang.getInt("id_pesan"),
//                                keranjang.getString("nama_produk"),
//                                keranjang.getInt("harga"),
//                                keranjang.getDouble("berat"),
//                                keranjang.getInt("jumlah"),
//                                keranjang.getString("foto"),
//                                keranjang.getInt("id_produk")
//                        ));
//                    }
//                    mKeranjangAdapter.notifyDataSetChanged();
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "checkout Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }){
//            //mengirim data nama, email, password, nohp, gender, tanggal, negara ke webservice menggunakan method POST
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("id_user", id_user);
//                params.put("nama_penerima", nama_penerima);
//                params.put("no_hp_penerima", no_hp_penerima);
//                params.put("alamat_penerima", alamat_penerima);
//                params.put("catatan", catatan);
//                params.put("kurir", kurir);
//                params.put("rekening", rekening);
//                return params;
//            }
//        };
//
//        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
//
//    }


    public void buildTampilan(){

        mKeranjang = new ArrayList<>();

        mRecyclerView = findViewById(R.id.rv_listkeranjang);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(this);

        mKeranjangAdapter = new KeranjangAdapter(KeranjangActivity.this, mKeranjang);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mKeranjangAdapter);

        mKeranjangAdapter.setOnItemClickListener(new KeranjangAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                Keranjang clickedItem = mKeranjang.get(position);
                int int_id_pesan = clickedItem.getId_pesan();
                int int_id_produk = clickedItem.getId_produk();
                int int_stok = clickedItem.getStok();

                int int_harga = clickedItem.getHarga();
                double double_berat = clickedItem.getBerat();
                int int_jumlah = clickedItem.getJumlah();

                int temp_jumlah = int_jumlah+1;
                double temp_berat = double_berat*temp_jumlah;
                int temp_harga = int_harga*temp_jumlah;

                String jumlah = Integer.toString(temp_jumlah);
                String total_berat = Double.toString(temp_berat);
                String total_harga = Integer.toString(temp_harga);

                String id_pesan = Integer.toString(int_id_pesan);
                String id_produk = Integer.toString(int_id_produk);
                Log.d(TAG, "onAddClick: "+id_pesan + " "+id_produk+" " + jumlah+" " +total_berat+" " +total_harga);

                String temp_total_harga_pesan = keranjangTotalHargaPesan.getText().toString();
                int int_temp_total_harga_pesan = Integer.parseInt(temp_total_harga_pesan);
                int int_total_harga_pesan = int_temp_total_harga_pesan + int_harga;
                String total_harga_pesan = Integer.toString(int_total_harga_pesan);

                if(temp_jumlah<=int_stok){
                    gantiJumlah(id_pesan, id_produk, jumlah, total_berat, total_harga, total_harga_pesan);
                    clickedItem.setJumlah(temp_jumlah);
                    clickedItem.setTotal_berat(temp_berat);
                    clickedItem.setTotal_harga(temp_harga);


                    keranjangTotalHargaPesan.setText(total_harga_pesan);
                    mKeranjangAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(KeranjangActivity.this,"wew",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onLessClick(int position) {
                Keranjang clickedItem = mKeranjang.get(position);
                int int_id_pesan = clickedItem.getId_pesan();
                int int_id_produk = clickedItem.getId_produk();

                int int_harga = clickedItem.getHarga();
                double double_berat = clickedItem.getBerat();
                int int_jumlah = clickedItem.getJumlah();

                int temp_jumlah = int_jumlah-1;
                double temp_berat = double_berat*temp_jumlah;
                int temp_harga = int_harga*temp_jumlah;

                String jumlah = Integer.toString(temp_jumlah);
                String total_berat = Double.toString(temp_berat);
                String total_harga = Integer.toString(temp_harga);

                String id_pesan = Integer.toString(int_id_pesan);
                String id_produk = Integer.toString(int_id_produk);
                Log.d(TAG, "onLessClick: "+id_pesan + " "+id_produk+" " + jumlah+" " +total_berat+" " +total_harga);

                String temp_total_harga_pesan = keranjangTotalHargaPesan.getText().toString();
                int int_temp_total_harga_pesan = Integer.parseInt(temp_total_harga_pesan);
                int int_total_harga_pesan = int_temp_total_harga_pesan - int_harga;
                String total_harga_pesan = Integer.toString(int_total_harga_pesan);

                if(temp_jumlah>0){
                    gantiJumlah(id_pesan, id_produk, jumlah, total_berat, total_harga, total_harga_pesan);
                    clickedItem.setJumlah(temp_jumlah);
                    clickedItem.setTotal_berat(temp_berat);
                    clickedItem.setTotal_harga(temp_harga);


                    keranjangTotalHargaPesan.setText(total_harga_pesan);
                    mKeranjangAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(KeranjangActivity.this,"wew",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onDeleteClick(int position) {
                Keranjang clickedItem = mKeranjang.get(position);
                int int_id_pesan = clickedItem.getId_pesan();
                int int_id_produk = clickedItem.getId_produk();

                int int_total_harga = clickedItem.getTotal_harga();

                String temp_total_harga_pesan = keranjangTotalHargaPesan.getText().toString();
                int int_temp_total_harga_pesan = Integer.parseInt(temp_total_harga_pesan);
                int int_total_harga_pesan = int_temp_total_harga_pesan - int_total_harga;
                String total_harga_pesan = Integer.toString(int_total_harga_pesan);

                String id_produk = Integer.toString(int_id_produk);
                String id_pesan = Integer.toString(int_id_pesan);
                Log.d(TAG, "onDeleteClick: " + id_pesan + " "+id_produk);
                hapusItemKeranjang(id_pesan, id_produk, total_harga_pesan);

                mKeranjang.remove(position);
                keranjangTotalHargaPesan.setText(total_harga_pesan);
                mKeranjangAdapter.notifyDataSetChanged();

            }
        });

    }

//    public void setButton(){
//        btnTambah = findViewById(R.id.tambah_ke_keranjang);
//        btnKurang = findViewById(R.id.kurang_keranjang);
//        btnHapus = findViewById(R.id.hapus_keranjang);
//
//        btnTambah.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = Integer.parseInt(tvJumlah.getText().toString());
//                tambahJumlah();
//            }
//        });
//        btnKurang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = Integer.parseInt(tvJumlah.getText().toString());
//
//            }
//        });
//    }

    public void gantiJumlah (final String id_pesan, final String id_produk, final String jumlah,
                             final String total_berat, final String total_harga, final String total_harga_pesan){

        String cancel_req_tag = "add keranjang";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_ADDLESS_KERANJANG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Keranjang Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            //mengirim data nama, email, password, nohp, gender, tanggal, negara ke webservice menggunakan method POST
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_pesan", id_pesan);
                params.put("id_produk", id_produk);
                params.put("jumlah", jumlah);
                params.put("total_berat", total_berat);
                params.put("total_harga", total_harga);
                params.put("total_harga_pesan", total_harga_pesan);
                return params;
            }
        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    public void hapusItemKeranjang(final String id_pesan, final String id_produk, final String total_harga_pesan){
        String cancel_req_tag = "hapus keranjang";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_DELETE_KERANJANG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "HAPUS Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response); //membuat JSON object sesuat dengan JSON Object yang dikirim dari webservice
                    boolean error = jObj.getBoolean("error"); //mengambil key JSON bernama error

                    if (!error) { //jika tidak error
                        String pesan = jObj.getString("pesan"); //mengambil key nama_user dari json object user
                        Toast.makeText(getApplicationContext(), pesan, Toast.LENGTH_LONG).show();


                    } else { //jika error
                        String errorMsg = jObj.getString("pesan");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "HAPUS Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            //mengirim data nama, email, password, nohp, gender, tanggal, negara ke webservice menggunakan method POST
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_pesan", id_pesan);
                params.put("id_produk", id_produk);
                params.put("total_harga_pesan", total_harga_pesan);
                return params;
            }
        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void loadData(final String id_user){
        String cancel_req_tag = "keranjang";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_AMBIL_KERANJANG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Keranjang Response: " + response.toString());

                try {
                    JSONObject jobj = new JSONObject(response);
                    JSONArray array = jobj.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject keranjang = array.getJSONObject(i);
                       mKeranjang.add(new Keranjang(
                               keranjang.getInt("id_pesan"),
                               keranjang.getString("nama_produk"),
                               keranjang.getInt("harga"),
                               keranjang.getDouble("berat"),
                               keranjang.getInt("jumlah"),
                               keranjang.getInt("total_harga"),
                               keranjang.getDouble("total_berat"),
                               keranjang.getInt("stok"),
                               keranjang.getString("foto"),
                               keranjang.getInt("id_produk")
                        ));
                    }
                    mKeranjangAdapter.notifyDataSetChanged();
                    JSONArray total_harga_pesan_keranjang = jobj.getJSONArray("total_harga_pesan");
                    JSONObject total_harga_pesan = total_harga_pesan_keranjang.getJSONObject(0);
                    keranjangTotalHargaPesan.setText(String.valueOf(total_harga_pesan.getInt("total_harga_pesan")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Keranjang Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            //mengirim data nama, email, password, nohp, gender, tanggal, negara ke webservice menggunakan method POST
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_user", id_user);
                return params;
            }
        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }



//
    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_keranjang);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }



}
