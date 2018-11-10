package com.multicraftbusiness.mobile_multicraft;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadBuktiBayarActivity extends AppCompatActivity {
    private static final String TAG = UploadBuktiBayarActivity.class.getSimpleName();
    private String URL_FOR_UPLOAD_BUKTI = "http://tifb.multicraftbusiness.com/mUploadBukti/UploadBukti/upload";

    Button buttonChoose;
    Button buttonUpload;
    ImageView pembayaranFotoBukti;
    EditText pembayaranNomorRekening, pembayaranNominalBayar, pembayaranTanggalBayar, pembayaranJamBayar;
    Bitmap bitmap, decoded;

    int success;
    int Pick_Image_Request = 1;
    int bitmap_size = 60;



    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_bukti_bayar);

        buttonChoose = findViewById(R.id.buttonChoose);
        buttonUpload = findViewById(R.id.buttonUpload);

        pembayaranNomorRekening = findViewById(R.id.nomor_rekening);
        pembayaranNominalBayar = findViewById(R.id.nominal_bayar);
        pembayaranTanggalBayar = findViewById(R.id.tgl_bayar);
        pembayaranJamBayar = findViewById(R.id.jam_bayar);

        pembayaranFotoBukti = findViewById(R.id.foto_bukti);

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        return encodedImage;
    }
    private void uploadImage(){
        final ProgressDialog loading = ProgressDialog.show(this, "Mengunggah", "Tunggu Sejenak ....",false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FOR_UPLOAD_BUKTI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Upload Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.e("v Add", jObj.toString());

                        Toast.makeText(UploadBuktiBayarActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        kosong();
                    } else {
                        Toast.makeText(UploadBuktiBayarActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();

                Toast.makeText(UploadBuktiBayarActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                Log.e(TAG, error.getMessage().toString());
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();


                params.put(KEY_NAME, pembayaranTanggalBayar.getText().toString().toString().trim());
                params.put(KEY_NAME, pembayaranJamBayar.getText().toString().toString().trim());
                params.put(KEY_NAME, pembayaranNomorRekening.getText().toString().toString().trim());
                params.put(KEY_IMAGE, getStringImage(decoded));
                params.put(KEY_NAME, pembayaranNominalBayar.getText().toString().toString().trim());

                Log.e(TAG, ""+ params);
                return params;
            }
        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest, tag_json_obj);
    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), Pick_Image_Request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Pick_Image_Request && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                setToImageView(getResizedBitmap(bitmap, 512));
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void kosong(){
        pembayaranFotoBukti.setImageResource(0);
        pembayaranNomorRekening.setText(null);
        pembayaranNominalBayar.setText(null);
        pembayaranJamBayar.setText(null);
        pembayaranTanggalBayar.setText(null);
    }

    private void setToImageView(Bitmap bmp){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        pembayaranFotoBukti.setImageBitmap(decoded);
    }

    public  Bitmap getResizedBitmap(Bitmap image, int maxSize){
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1){
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
