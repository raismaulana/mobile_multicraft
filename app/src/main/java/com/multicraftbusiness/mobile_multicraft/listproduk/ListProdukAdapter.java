package com.multicraftbusiness.mobile_multicraft.listproduk;


import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.multicraftbusiness.mobile_multicraft.R;

import java.util.List;

public class ListProdukAdapter extends RecyclerView.Adapter<ListProdukAdapter.ListProdukViewHolder> {

    private Context mContext;
    private List<ListProduk> mListProduk;
    private ListProdukListener mListener;

    public interface ListProdukListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ListProdukListener listener) {
        mListener = listener;
    }

    public ListProdukAdapter(Context context, List<ListProduk> listProduk) {
        this.mContext = context;
        this.mListProduk = listProduk;
    }

    @NonNull
    @Override
    public ListProdukAdapter.ListProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_produk_card, parent, false);

        return new ListProdukViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListProdukAdapter.ListProdukViewHolder holder, int position) {
        ListProduk currentItem = mListProduk.get(position);
        holder.nama_produk.setText(currentItem.getNama_produk());
        holder.harga_produk.setText("Rp" + String.valueOf(currentItem.getHarga()));
        Glide.with(mContext).load(currentItem.getFoto()).into(holder.gambar_produk);

    }

    @Override
    public int getItemCount() {
        return mListProduk.size();
    }

    public class ListProdukViewHolder extends RecyclerView.ViewHolder {
        public TextView nama_produk, harga_produk;
        public ImageView gambar_produk;
        public CardView cardView;

        public ListProdukViewHolder(View itemView) {
            super(itemView);
            nama_produk = itemView.findViewById(R.id.nama_produk);
            harga_produk = itemView.findViewById(R.id.harga_produk);
            gambar_produk = itemView.findViewById(R.id.gambar_produk);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

}