package com.multicraftbusiness.mobile_multicraft.checkout.ongkir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.multicraftbusiness.mobile_multicraft.AppSingleton;
import com.multicraftbusiness.mobile_multicraft.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.multicraftbusiness.mobile_multicraft.UploadBuktiBayarActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.multicraftbusiness.mobile_multicraft.checkout.ApiEndPoint.ApiKey;
import static com.multicraftbusiness.mobile_multicraft.checkout.ApiEndPoint.BaseUrl;
import static com.multicraftbusiness.mobile_multicraft.checkout.ApiEndPoint.Cost;

public class CounterActivity extends AppCompatActivity {
    private static final String URL_FOR_CHECKOUT = "http://tifb.multicraftbusiness.com/mcheckout/Checkout/input_check_out";
    String city, city_id, province , name , address , courier , bornday , telp, weight, curr_weight, total_harga_pesan, harga, ongkir;
    final static String TAG = CounterActivity.class.getSimpleName();
    Button BtnCheckout;
    TextView etData , etOngkir, etNamaKurir, etData1;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        etData = findViewById(R.id.etData);
        etData1 = findViewById(R.id.etData1);
        etOngkir = findViewById(R.id.etOngkir);
        etNamaKurir = findViewById(R.id.etNamaKurir);
        BtnCheckout = findViewById(R.id.btncheckout);

        sharedPreferences = this.getSharedPreferences("data_pengguna", Context.MODE_PRIVATE);
        int int_id_user = sharedPreferences.getInt("id_user", 0);
        final String id_user = Integer.toString(int_id_user);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            bornday = extras.getString("bornday");
            address = extras.getString("address");
            courier = extras.getString("courier");
            province = extras.getString("province");
            city_id = extras.getString("city_id");
            city = extras.getString("city");
            telp = extras.getString("telp");
            weight = extras.getString("weight");
            Log.d(TAG, "onCreate: "+weight);
            total_harga_pesan = extras.getString("total_harga_pesan");

            int temp_weight = Integer.parseInt(weight);
            temp_weight = temp_weight *1000;
            curr_weight = Integer.toString(temp_weight);

            int temp_harga = Integer.parseInt(total_harga_pesan);
            etData1.setText("TOTAL HARGA BARANG = "+total_harga_pesan);
            Log.d(TAG, "onCreate: "+ name + bornday + address + courier + province + city_id + city + telp+curr_weight);

            getCounter();

            Log.d(TAG, "onCreate: "+getOngkir());
//            int iongkir = Integer.parseInt(getOngkir());
//            temp_harga = temp_harga + iongkir;
//            harga = Integer.toString(temp_harga);
//            etData.setText("TOTAL PEMBAYARAN = "+harga);
        }else{
            Log.d(TAG, "kosong");
        }
        BtnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCheckout(id_user, ongkir, harga, name, telp, address, bornday);
                Intent intent = new Intent(CounterActivity.this, UploadBuktiBayarActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getCounter(){
        AndroidNetworking.post(BaseUrl + Cost)
                .addBodyParameter("origin", "160")
                .addBodyParameter("destination", city_id)
                .addBodyParameter("weight", curr_weight)
                .addBodyParameter("courier", courier)
                .addHeaders("key", ApiKey)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "raroh Response: " + response.toString());
                        try {
                            JSONObject jsonObject = response.getJSONObject("rajaongkir");
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            JSONObject jsonObject1 = null;
                            JSONObject jsonObject2 = null;
                            for(int i = 0; i < jsonArray.length(); i++)
                            {
                                jsonObject1 = jsonArray.getJSONObject(i);
                            }

                            assert jsonObject1 != null;
                            JSONArray jsonArray1 = jsonObject1.getJSONArray("costs");

                            for (int i = 0; i < jsonArray1.length(); i++){
                                jsonObject2 = jsonArray1.getJSONObject(i);
                            }

                            assert jsonObject2 != null;
                            JSONArray jsonArray2 = jsonObject2.getJSONArray("cost");
                            JSONObject jsonObject3 = jsonArray2.getJSONObject(0);
                            String ongkir1 = String.valueOf(jsonObject3.getString("value"));
                            setOngkir(ongkir1);
                            etOngkir.setText(ongkir1);
                            etNamaKurir.setText("Kurir: "+String.valueOf((jsonObject1.getString("name")))+" / "+ String.valueOf(jsonObject2.getString("description"))
                            +"("+String.valueOf(jsonObject2.getString("service")+")")+"estimasi "+String.valueOf(jsonObject3.getString("etd"))+" hari");



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                    }

                    @Override
                    public void onError(ANError anError) {
                       // Log.d(TAG, String.valueOf(anError.getErrorCode()));
                    }
                });
    }
    public void submitCheckout(final String id_user, final String ongkir, final String harga, final String name,
                               final String telp, final String address, final String bornday){
        String cancel_req_tag = "masuk keranjang";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_CHECKOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "MASUK KERANJANG Response: " + response.toString());

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
                params.put("id_user", id_user);
                params.put("biaya_pengiriman", ongkir);
                params.put("total_pembayaran", harga);
                params.put("nama_penerima", name);
                params.put("no_hp_penerima", telp);
                params.put("alamat_penerima", address);
                params.put("catatan", bornday);
                return params;
            }
        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }
    public class Ongkir{
        public String ongkir;
    }

    public void setOngkir(String ongkir) {
        this.ongkir = ongkir;
    }

    public String getOngkir() {
        return ongkir;
    }
}
