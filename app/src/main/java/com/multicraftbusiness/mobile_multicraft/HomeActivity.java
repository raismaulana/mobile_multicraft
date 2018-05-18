package com.multicraftbusiness.mobile_multicraft;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    TextView text1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        text1= (TextView)findViewById(R.id.textView);

        Bundle bundle= getIntent().getExtras();
        String data1 = bundle.getString("uname");

        Toast.makeText(getBaseContext(),"log in success",Toast.LENGTH_SHORT).show();

        text1.setText(data1);


    }
}
