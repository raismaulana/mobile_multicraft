package com.multicraftbusiness.mobile_multicraft;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.multicraftbusiness.mobile_multicraft.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        ImageButton btnfacebook= (ImageButton)findViewById(R.id.btnfacebook);
        ImageButton btnwhatsapp= (ImageButton)findViewById(R.id.btnwhatsapp);
        ImageButton btninstagram= (ImageButton)findViewById(R.id.btninstagram);
        ImageButton btngoogle= (ImageButton)findViewById(R.id.btngoogle);



        btnfacebook.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                CallIntent(v);
            }
        });
        btnwhatsapp.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                CallIntent(v);
            }
        });
        btninstagram.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                CallIntent(v);
            }
        });
        btngoogle.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                CallIntent(v);
            }
        });

    }
    public void CallIntent(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btnfacebook:
                intent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("https://www.facebook.com/penjual.kerajinan"));
                startActivity(intent);
                break;
            case R.id.btnwhatsapp:
                intent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("http://www.whatsapp.com"));
                startActivity(intent);
                break;
            case R.id.btninstagram:
                intent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("https://www.instagram.com/abdilagarwood"));
                startActivity(intent);
                break;
            case R.id.btngoogle:
                intent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("http://multicraftbusiness.com/"));
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            String result = data.toURI();
            Toast.makeText(this, result, Toast.LENGTH_LONG);
        }


    }
}
