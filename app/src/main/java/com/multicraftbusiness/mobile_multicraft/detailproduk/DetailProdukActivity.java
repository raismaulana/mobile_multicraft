package com.multicraftbusiness.mobile_multicraft.detailproduk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.multicraftbusiness.mobile_multicraft.AppSingleton;
import com.multicraftbusiness.mobile_multicraft.R;
import com.multicraftbusiness.mobile_multicraft.keranjang.KeranjangActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.multicraftbusiness.mobile_multicraft.listproduk.ListProdukActivity.EXTRA_BERAT_PRODUK;
import static com.multicraftbusiness.mobile_multicraft.listproduk.ListProdukActivity.EXTRA_DESKRIPSI_PRODUK;
import static com.multicraftbusiness.mobile_multicraft.listproduk.ListProdukActivity.EXTRA_FOTO_PRODUK;
import static com.multicraftbusiness.mobile_multicraft.listproduk.ListProdukActivity.EXTRA_HARGA_PRODUK;
import static com.multicraftbusiness.mobile_multicraft.listproduk.ListProdukActivity.EXTRA_ID_PRODUK;
import static com.multicraftbusiness.mobile_multicraft.listproduk.ListProdukActivity.EXTRA_LEBAR_PRODUK;
import static com.multicraftbusiness.mobile_multicraft.listproduk.ListProdukActivity.EXTRA_NAMA_PRODUK;
import static com.multicraftbusiness.mobile_multicraft.listproduk.ListProdukActivity.EXTRA_PANJANG_PRODUK;
import static com.multicraftbusiness.mobile_multicraft.listproduk.ListProdukActivity.EXTRA_STOK_PRODUK;
import static com.multicraftbusiness.mobile_multicraft.listproduk.ListProdukActivity.EXTRA_TINGGI_PRODUK;

public class DetailProdukActivity extends AppCompatActivity {
    private static final String URL_FOR_MASUK_KERANJANG = "http://tifb.multicraftbusiness.com/mdetailproduk/Detail_produk/masuk_keranjang";
    private static final String TAG = DetailProdukActivity.class.getSimpleName();
    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);

        //menyembunyikan nama di actionbar

        //getSupportActionBar().hide();

        // menerima data
        int int_id_produk = getIntent().getExtras().getInt(EXTRA_ID_PRODUK);
        final String id_produk = Integer.toString(int_id_produk);
        String nama_produk= getIntent().getExtras().getString(EXTRA_NAMA_PRODUK);
        String deskripsi= getIntent().getExtras().getString(EXTRA_DESKRIPSI_PRODUK);
        String foto= getIntent().getExtras().getString(EXTRA_FOTO_PRODUK);
        int harga= getIntent().getExtras().getInt(EXTRA_HARGA_PRODUK);
        double panjang= getIntent().getExtras().getDouble(EXTRA_PANJANG_PRODUK);
        double lebar= getIntent().getExtras().getDouble(EXTRA_LEBAR_PRODUK);
        double tinggi= getIntent().getExtras().getDouble(EXTRA_TINGGI_PRODUK);
        double berat= getIntent().getExtras().getDouble(EXTRA_BERAT_PRODUK);
        int stok = getIntent().getExtras().getInt(EXTRA_STOK_PRODUK);

        CollapsingToolbarLayout collapsingToolbarLayout= findViewById(R.id.collapsingtoolbar_id);
        collapsingToolbarLayout.setTitleEnabled(true);

        TextView name = findViewById(R.id.nama_produk2);
        TextView price = findViewById(R.id.harga_produk2);
        ImageView img = findViewById(R.id.imageView2);
        TextView stock = findViewById(R.id.stok2);
        TextView weight = findViewById(R.id.berat2);
        TextView des = findViewById(R.id.description2);
        Button beli = findViewById(R.id.tambah_ke_keranjang);

        //setting values

        name.setText(nama_produk);
        des.setText(deskripsi);
        price.setText("Rp"+String.valueOf(harga));
        stock.setText("Stok = "+String.valueOf(stok));
        weight.setText(String.valueOf(berat)+" kg");

        //agar sama dengan judulnya
        collapsingToolbarLayout.setTitle(nama_produk);

        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

        //set image using glide

        Glide.with(this).load(foto).apply(requestOptions).into(img);

        sharedPreferences = this.getSharedPreferences("data_pengguna", Context.MODE_PRIVATE);
        int int_id_user = sharedPreferences.getInt("id_user", 0);
        final String id_user = Integer.toString(int_id_user);
        final String sberat = Double.toString(berat);
        final String sharga = Double.toString(harga);

        beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlebukeranjang(id_produk, id_user, sberat, sharga);

            }
        });

    }
    public void mlebukeranjang(final String id_produk, final String id_user, final String berat, final String harga){
        String cancel_req_tag = "masuk keranjang";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_MASUK_KERANJANG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "MASUK KERANJANG Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response); //membuat JSON object sesuat dengan JSON Object yang dikirim dari webservice
                    boolean error = jObj.getBoolean("error"); //mengambil key JSON bernama error

                    if (!error) { //jika tidak error
                        String pesan = jObj.getString("pesan"); //mengambil key nama_user dari json object user
                        Toast.makeText(getApplicationContext(), pesan, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(DetailProdukActivity.this, KeranjangActivity.class);
                        startActivity(intent);

                    } else { //jika error
                        String errorMsg = jObj.getString("pesan");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(DetailProdukActivity.this, KeranjangActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "MASUK KERANJANG Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            //mengirim data nama, email, password, nohp, gender, tanggal, negara ke webservice menggunakan method POST
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_produk", id_produk);
                params.put("id_user", id_user);
                params.put("total_berat", berat);
                params.put("total_harga", harga);
                return params;
            }
        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }
}
