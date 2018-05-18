package com.multicraftbusiness.mobile_multicraft;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ResponseCache;

/**
 * Created by Asus Pc on 15/05/2018.
 */

public class LoginActivity extends AppCompatActivity {
   // SharedPreferences sharedpref;
  //  SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        /*sharedpref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
        if(sharedpref.contains("username")){
            //langsung masuk ke intent
            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
            intent.putExtra("username", sharedpref.getString("username", "none"));
            startActivity(intent);
            finish();
        }else{

        }
        */
        final EditText txt_username= (EditText)findViewById(R.id.txt_username);
        final EditText txt_password=(EditText)findViewById(R.id.txt_password);
        final Button btn_login= (Button)findViewById(R.id.btn_login);
        final TextView registerLink=(TextView)findViewById(R.id.tv_register);

       /* registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        */

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nama_user= txt_username.getText().toString();
                final String password = txt_password.getText().toString();

                Response.Listener<String> rerponseListener= new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                String nama_user= jsonResponse.getString("nama_user");
                                String password= jsonResponse.getString("password");

                                Intent intent= new Intent(LoginActivity.this, HomeActivity.class);

                                intent.putExtra("username",nama_user);

                                /*editor = sharedpref.edit();
                                editor.putString("username", nama_user);
                                editor.commit();
                                */
                                LoginActivity.this.startActivity(intent);
                                finish();

                            }else {
                                AlertDialog.Builder builder= new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest LoginRequest = new LoginRequest(nama_user, password, rerponseListener);
                RequestQueue queue= Volley.newRequestQueue(LoginActivity.this);
                queue.add(LoginRequest);
            }
        });


    }
}