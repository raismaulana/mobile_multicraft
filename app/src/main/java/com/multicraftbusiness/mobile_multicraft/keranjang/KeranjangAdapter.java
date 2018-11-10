package com.multicraftbusiness.mobile_multicraft.keranjang;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.multicraftbusiness.mobile_multicraft.AppSingleton;
import com.multicraftbusiness.mobile_multicraft.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeranjangAdapter extends RecyclerView.Adapter<KeranjangAdapter.KeranjangViewHolder> {

    private Context mContext;
    private List<Keranjang> mKeranjang;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onAddClick(int position);
        void onLessClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public static class KeranjangViewHolder extends RecyclerView.ViewHolder {
        public ImageView gambar_keranjang;
        public Button tambah_keranjang, kurang_keranjang, delete_keranjang;
        public TextView nama_keranjang, harga_keranjang, berat_keranjang, jumlah_keranjang, id_produk;

        public KeranjangViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            gambar_keranjang = itemView.findViewById(R.id.gambar_keranjang);
            nama_keranjang = itemView.findViewById(R.id.nama_keranjang);
            harga_keranjang = itemView.findViewById(R.id.harga_keranjang);
            berat_keranjang = itemView.findViewById(R.id.berat_keranjang);
            jumlah_keranjang = itemView.findViewById(R.id.jumlah_keranjang);
            tambah_keranjang = itemView.findViewById(R.id.tambah_jumlah);
            kurang_keranjang = itemView.findViewById(R.id.kurang_jumlah);
            delete_keranjang = itemView.findViewById(R.id.hapus_keranjang);

            tambah_keranjang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onAddClick(position);
                        }
                    }
                }
            });
            kurang_keranjang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onLessClick(position);
                        }
                    }
                }
            });
            delete_keranjang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

        }
    }

    public KeranjangAdapter(Context context, List<Keranjang> mKeranjang) {
        this.mContext = context;
        this.mKeranjang = mKeranjang;
    }

    @NonNull
    @Override
    public KeranjangAdapter.KeranjangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keranjang_card, parent, false);
        KeranjangViewHolder kvh = new KeranjangViewHolder(itemView, mListener);
        return kvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final KeranjangAdapter.KeranjangViewHolder holder, final int position) {
        Keranjang currentItem = mKeranjang.get(position);
        holder.nama_keranjang.setText(currentItem.getNama_produk());
        holder.jumlah_keranjang.setText(String.valueOf(currentItem.getJumlah()));
        holder.harga_keranjang.setText("Rp" + String.valueOf(currentItem.getTotal_harga()));
        holder.berat_keranjang.setText(String.valueOf(currentItem.getTotal_berat()) + " kg");
        Glide.with(mContext).load(currentItem.getFoto()).into(holder.gambar_keranjang);
    }

    @Override
    public int getItemCount() {
        return mKeranjang.size();
    }


}
