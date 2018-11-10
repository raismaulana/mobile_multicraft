package com.multicraftbusiness.mobile_multicraft.checkout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.multicraftbusiness.mobile_multicraft.AppSingleton;
import com.multicraftbusiness.mobile_multicraft.R;
import com.multicraftbusiness.mobile_multicraft.checkout.ongkir.CounterActivity;
import com.multicraftbusiness.mobile_multicraft.checkout.ongkir.ProvinceActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    private static final String URL_FOR_AmbilBerat = "http://tifb.multicraftbusiness.com/mcheckout/Checkout/ambil_hitungan";
    String province,city_id, city, gender , bornday, address , name, telp, courier, weight, total_harga_pesan;
    final static String TAG = MainActivity.class.getSimpleName();
    TextView etCity , etProvince , etBornDay;
    EditText etName , etNoTelp , etAddress ;
    private RadioGroup radioGroup;
    RadioButton radioButton1, radioButton2, radioButton3;
    private int mYear, mMonth, mDay;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("data_pengguna", Context.MODE_PRIVATE);
        int int_id_user = sharedPreferences.getInt("id_user", 0);
        String id_user = Integer.toString(int_id_user);
        ambilTotalBerat(id_user);

        etAddress = findViewById(R.id.etAddress);
        etName = findViewById(R.id.etName);
        etNoTelp = findViewById(R.id.etNotelp);
        etCity = findViewById(R.id.etCity);
        etProvince = findViewById(R.id.etProvince);
        radioGroup = findViewById(R.id.radioGroup);
        etBornDay = findViewById(R.id.etBornDay);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton3 = findViewById(R.id.radioButton3);

        radioGroup.clearCheck();



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = radioGroup.findViewById(i);
                if (null != radioButton && i > -1){
                    gender = String.valueOf(radioButton.getText());
                    Log.d(TAG , "kelaminnya adalah : " + gender);
                }
            }
        });

        checkKeyboard();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            bornday = extras.getString("bornday");
            address = extras.getString("address");
            gender = extras.getString("gender");
            province = extras.getString("province");
            city_id = extras.getString("city_id");
            city = extras.getString("city");
            telp = extras.getString("telp");
            Log.d(TAG, "provinsi : " + province + "city id : " + city_id + "city name : " + city);

            etCity.setText(city);
            etProvince.setText(province);
            etName.setText(name);
            etAddress.setText(address);
            etBornDay.setText(bornday);
            etNoTelp.setText(telp);

//            Log.d(TAG , gender);
            if (gender.equals("Jalur Nugraha Ekakurir (JNE)")){
                radioButton1.setChecked(true);
                courier = "jne";
            }else {
                radioButton3.setChecked(true);
                courier = "pos";
            }

        }


    }

    private void checkKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void listProvince(View view){

        name = etName.getText().toString();
        address = etAddress.getText().toString();
        telp = etNoTelp.getText().toString();
        bornday = etBornDay.getText().toString();


        if (gender != null && bornday != null && name != null && address != null && telp != null){
            Intent i = new Intent(getApplicationContext(), ProvinceActivity.class);
            i.putExtra("name", name);
            i.putExtra("address", address);
            i.putExtra("telp", telp);
            i.putExtra("gender", gender);
            i.putExtra("bornday", bornday);
            Log.d(TAG , "di validasi : " + gender);
            startActivity(i);
        }else{
            Toast.makeText(getApplicationContext(), "You must fill all before this",
                    Toast.LENGTH_SHORT).show();
        }



    }
    public class Weight {

        public String weight;
        public String total_harga_pesan;}


    public void ambilTotalBerat(final String id_user){
        String cancel_req_tag = "berat";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_AmbilBerat, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "berat Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response); //membuat JSON object sesuat dengan JSON Object yang dikirim dari webservice

                    String total_berat = jObj.getString("berat"); //mengambil key nama_user dari json object user
                    String total_harga_pesan = jObj.getString("harga");
                    setWeight(total_berat);
                    setTotal_harga_pesan(total_harga_pesan);
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
                params.put("id_user", id_user);
                return params;
            }
        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    public void nextCounter(View view){
        name = etName.getText().toString();
        address = etAddress.getText().toString();
        telp = etNoTelp.getText().toString();
        bornday = etBornDay.getText().toString();

        Log.d(TAG , "kelaminnya adalah : " + gender);
        if (gender != null && bornday != null && name != null && address != null && telp != null && city_id != null && city != null && province != null){
            Intent i = new Intent(getApplicationContext(), CounterActivity.class);
            i.putExtra("city", city);
            i.putExtra("city_id", city_id);
            i.putExtra("province", province);
            i.putExtra("name", name);
            i.putExtra("address", address);
            i.putExtra("telp", telp);
            i.putExtra("courier", courier);
            i.putExtra("bornday", bornday);
            i.putExtra("weight", getWeight());
            i.putExtra("total_harga_pesan", getTotal_harga_pesan());
            Log.d(TAG , "di validasi : " + gender+getWeight());
            startActivity(i);
        }else{
            Toast.makeText(getApplicationContext(), "You must fill all before this",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeight() {
        return weight;
    }

    public void setTotal_harga_pesan(String total_harga_pesan) {
        this.total_harga_pesan = total_harga_pesan;
    }

    public String getTotal_harga_pesan() {
        return total_harga_pesan;
    }
}
