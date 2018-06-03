package com.multicraftbusiness.mobile_multicraft.detailproduk;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.multicraftbusiness.mobile_multicraft.R;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);

        //menyembunyikan nama di actionbar

        getSupportActionBar().hide();

        // menerima data
        String id_produk = getIntent().getExtras().getString(EXTRA_ID_PRODUK);
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

        //setting values

        name.setText(nama_produk);
        des.setText(deskripsi);

        //agar sama dengan judulnya
        collapsingToolbarLayout.setTitle(nama_produk);

        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

        //set image using glide

        Glide.with(this).load(foto).apply(requestOptions).into(img);

    }
}
