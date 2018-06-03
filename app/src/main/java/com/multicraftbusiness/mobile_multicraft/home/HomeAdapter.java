package com.multicraftbusiness.mobile_multicraft.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.multicraftbusiness.mobile_multicraft.R;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    private Context mContext;
    private ArrayList<Home> mHomeList;

    //awal membuat onclick event
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    //akhir

    public HomeAdapter(Context context, ArrayList<Home> homeList) {
        this.mContext = context;
        this.mHomeList = homeList;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.kategori_row, parent, false);
        return new HomeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Home currentItem = mHomeList.get(position);

        String gambar_kategori = currentItem.getGambar_kategori();
        String nama_kategori = currentItem.getNama_kategori();
        String id_kategori = String.valueOf(currentItem.getId_kategori());

        holder.mId_kategori.setText(id_kategori);
        Glide.with(mContext)
                .load(gambar_kategori)
                .into(holder.mGambar_kategori);
        holder.mNama_kategori.setText(nama_kategori);
    }

    @Override
    public int getItemCount() {
        return mHomeList.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        public ImageView mGambar_kategori;
        public TextView mNama_kategori, mId_kategori;

        public HomeViewHolder(View itemView) {
            super(itemView);
            mGambar_kategori = itemView.findViewById(R.id.gambar_kategori);
            mNama_kategori = itemView.findViewById(R.id.nama_kategori);
            mId_kategori = itemView.findViewById(R.id.id_kategori);

            //membuat onclick event
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
