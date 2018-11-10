package com.multicraftbusiness.mobile_multicraft.listproduk;



import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.multicraftbusiness.mobile_multicraft.AppSingleton;
import com.multicraftbusiness.mobile_multicraft.R;
import com.multicraftbusiness.mobile_multicraft.RegisterActivity;
import com.multicraftbusiness.mobile_multicraft.detailproduk.DetailProdukActivity;
import com.multicraftbusiness.mobile_multicraft.home.Home;
import com.multicraftbusiness.mobile_multicraft.home.HomeActivity;
import com.multicraftbusiness.mobile_multicraft.home.HomeAdapter;

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


public class ListProdukActivity extends AppCompatActivity implements ListProdukAdapter.ListProdukListener {
    private static final String TAG = ListProdukActivity.class.getSimpleName();
    private static final String URL_FOR_PRODUK = "http://tifb.multicraftbusiness.com/mlistproduk/List_produk/list"; //url controller webservice

    public static final String EXTRA_ID_PRODUK = "id_produk";
    public static final String EXTRA_NAMA_PRODUK = "nama_produk";
    public static final String EXTRA_HARGA_PRODUK = "harga_produk";
    public static final String EXTRA_PANJANG_PRODUK = "panjang_produk";
    public static final String EXTRA_LEBAR_PRODUK = "lebar_produk";
    public static final String EXTRA_TINGGI_PRODUK = "tinggi_produk";
    public static final String EXTRA_BERAT_PRODUK = "berat_produk";
    public static final String EXTRA_DESKRIPSI_PRODUK = "deskripsi_produk";
    public static final String EXTRA_FOTO_PRODUK = "foto_produk";
    public static final String EXTRA_STOK_PRODUK = "stok_produk";

    ProgressDialog progressDialog;

    public RecyclerView mRecyclerView;
    public ListProdukAdapter mListProdukAdapter;
    public List<ListProduk> mListProduk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_produk);

        Intent intent = getIntent();
        int int_id_kategori = intent.getIntExtra(EXTRA_ID, 0);
        String id_kategori = Integer.toString(int_id_kategori);
        String cover_kategori = intent.getStringExtra(EXTRA_COVER_KATEGORI);
        String nama_kategori = intent.getStringExtra(EXTRA_NAMA_KATEGORI);
        Log.d(TAG, "onCreate: "+ cover_kategori);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        mRecyclerView = findViewById(R.id.rv_listproduk);

        mListProduk = new ArrayList<>();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(ListProdukActivity.this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());



        loadData(id_kategori);

        mListProdukAdapter = new ListProdukAdapter(ListProdukActivity.this, mListProduk);
        mRecyclerView.setAdapter(mListProdukAdapter);

        try {
            Glide.with(this)
                    .load(cover_kategori)
                    .into((ImageView) findViewById(R.id.backdrop));
        }catch (Exception e){
            e.printStackTrace();
        }

        TextView title_kategori = findViewById(R.id.backdrop_title);
        title_kategori.setText(nama_kategori);

    }


    private void loadData(final String id_kategori) {
        String cancel_req_tag = "produk";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FOR_PRODUK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Produk Response: " + response.toString());
                try {

                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject produk = array.getJSONObject(i);
                        mListProduk.add(new ListProduk(
                                produk.getInt("id_produk"),
                                produk.getString("nama_produk"),
                                produk.getInt("harga"),
                                produk.getDouble("panjang"),
                                produk.getDouble("lebar"),
                                produk.getDouble("tinggi"),
                                produk.getDouble("berat"),
                                produk.getString("deskripsi"),
                                produk.getString("foto"),
                                produk.getInt("stok")
                        ));
                    }
                    mListProdukAdapter.notifyDataSetChanged();
                    mListProdukAdapter.setOnItemClickListener(ListProdukActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Produk Error: " + error.getMessage());
                }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_kategori", id_kategori);
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest, cancel_req_tag);
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_listproduk);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_banner);
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

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onItemClick(int position) {
        Intent kedetailproduk = new Intent(ListProdukActivity.this, DetailProdukActivity.class);
        ListProduk clickedItem = mListProduk.get(position);

        kedetailproduk.putExtra(EXTRA_ID_PRODUK, clickedItem.getId_produk());
        kedetailproduk.putExtra(EXTRA_NAMA_PRODUK, clickedItem.getNama_produk());
        kedetailproduk.putExtra(EXTRA_HARGA_PRODUK, clickedItem.getHarga());
        kedetailproduk.putExtra(EXTRA_PANJANG_PRODUK, clickedItem.getPanjang());
        kedetailproduk.putExtra(EXTRA_LEBAR_PRODUK, clickedItem.getLebar());
        kedetailproduk.putExtra(EXTRA_TINGGI_PRODUK, clickedItem.getTinggi());
        kedetailproduk.putExtra(EXTRA_BERAT_PRODUK, clickedItem.getBerat());
        kedetailproduk.putExtra(EXTRA_DESKRIPSI_PRODUK, clickedItem.getDeskripsi());
        kedetailproduk.putExtra(EXTRA_FOTO_PRODUK, clickedItem.getFoto());
        kedetailproduk.putExtra(EXTRA_STOK_PRODUK, clickedItem.getStok());
        Log.d(TAG, "onItemClick: "+ clickedItem.getId_produk());

        //Log.d(TAG, "onItemClick: "+ clickedItem.getId_kategori());

        startActivity(kedetailproduk);
    }


}




