package com.multicraftbusiness.mobile_multicraft;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.multicraftbusiness.mobile_multicraft.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.os.Build.VERSION_CODES.N;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final String URL_FOR_REGISTRATION = "http://tifb.multicraftbusiness.com/mregister/Register/daftar"; //url controller webservice
    ProgressDialog progressDialog;

    private EditText registerInputNama, registerInputEmail, registerInputPassword, registerInputNohp;
    private RadioGroup genderRadioGroup;
    private Button btnRegister;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private CheckBox registerShowPassword;
    private Spinner registerInputNegara;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        mDisplayDate = findViewById(R.id.register_input_tanggal);

        registerShowPassword = findViewById(R.id.register_show_password);

        registerInputNama = findViewById(R.id.register_input_nama);
        registerInputEmail = findViewById(R.id.register_input_email);
        registerInputPassword = findViewById(R.id.register_input_password);
        registerInputNohp = findViewById(R.id.register_input_nohp);
        registerInputNegara = findViewById(R.id.register_input_negara);
        btnRegister = findViewById(R.id.btn_register);
        genderRadioGroup = findViewById(R.id.gender_radio_group);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyyy" + month + "/"+ day +"/"+year);

                String date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        registerShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    registerInputPassword.setInputType(129);
                } else {
                    registerInputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

    }

    //mengecek tanggal null atau tidak
    private boolean cektanggal(String tanggal){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        Date strDate = null;
        try {
            strDate = sdf.parse(tanggal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            new Date().before(strDate);
            return false;
        }catch (Exception e){
        return true;
    }}

    //mengecek isi tiap inputan apakah valid jika valid lanjut register
    private void submitForm(){
        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        String gender;
        String nama = registerInputNama.getText().toString();
        String email = registerInputEmail.getText().toString();
        String password = registerInputPassword.getText().toString();
        String nohp = registerInputNohp.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String negara = registerInputNegara.getSelectedItem().toString();

        if(selectedId == R.id.lakilaki_radio_btn){
            gender = "L";
        }else{
            gender = "P";
        }

        String tanggal = mDisplayDate.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        Date strDate = null;
        try {
            strDate = sdf.parse(tanggal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (nama.equalsIgnoreCase("")){
            Toast.makeText(getApplicationContext(), "Isi Nama Lengkap dong", Toast.LENGTH_LONG).show();
            registerInputNama.requestFocus();
        }
        else if (!email.matches(emailPattern)){
            Toast.makeText(getApplicationContext(), "Isi Emailnya dulu kak :3", Toast.LENGTH_LONG).show();
            registerInputEmail.requestFocus();
        }
        else if (password.equalsIgnoreCase("")){
            Toast.makeText(getApplicationContext(), "Isi Password Dulu dong UwU", Toast.LENGTH_LONG).show();
            registerShowPassword.requestFocus();
        }
        else if (nohp.equalsIgnoreCase("")){
            Toast.makeText(getApplicationContext(), "Isi Nomor HP Dulu kak ^_^", Toast.LENGTH_LONG).show();
            registerInputNohp.requestFocus();
        }
        else if (cektanggal(tanggal)){
            Toast.makeText(getApplicationContext(), "Isi Tanggal Lahir Dulu kak ^_^", Toast.LENGTH_LONG).show();
        }
        else if (new Date().before(strDate)){
            Toast.makeText(getApplicationContext(), "Hayoo, isi yang bener tanggalnya ...", Toast.LENGTH_LONG).show();
        }
        else if (negara.matches("Negara/Country")){
            Toast.makeText(getApplicationContext(), "Pilih Negara Dulu kak ^_^", Toast.LENGTH_LONG).show();
        }
        else {

        registerUser(nama, email, password, nohp, gender, tanggal, negara);//mengirim nama, email, password, nohp, gender, tanggal, negara inputan ke fungsi registerUser()
        }
    }

    //mengirim data ke webservice menggunakan volley
    private void registerUser(final String nama, final String email,
                              final String password, final String nohp, final String gender,
                              final String tanggal, final String negara){
        String cancel_req_tag = "register";

        progressDialog.setMessage("Adding you ..."); // menampilkan progressdialog
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_REGISTRATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog(); //Setelah webservice mengirim response, progress dialog di dismiss

                try {
                    JSONObject jObj = new JSONObject(response); //membuat JSON object sesuat dengan JSON Object yang dikirim dari webservice
                    boolean error = jObj.getBoolean("error"); //mengambil key JSON bernama error

                    if (!error) { //jika tidak error
                        String user = jObj.getJSONObject("user").getString("nama_user"); //mengambil key nama_user dari json object user
                        Toast.makeText(getApplicationContext(), "Hi " + user + ", You are successfully Added! Silakan cek emailmu.", Toast.LENGTH_LONG).show();

                        //intent ke login
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else { //jika error
                        String errorMsg = jObj.getString("error_msg");
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
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            //mengirim data nama, email, password, nohp, gender, tanggal, negara ke webservice menggunakan method POST
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama", nama);
                params.put("email", email);
                params.put("password", password);
                params.put("nohp", nohp);
                params.put("gender", gender);
                params.put("tanggal", tanggal);
                params.put("negara", negara);
                return params;
            }
        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag); //menaruh strReq ke antrian di AppSingelton
    }

    private void showDialog(){
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog(){
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}
