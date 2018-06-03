package com.multicraftbusiness.mobile_multicraft.home;

import android.graphics.Bitmap;

public class Home {
    private int id_kategori;
    private String nama_kategori, gambar_kategori, cover_kategori;

    public Home() {

    }

    public Home(int id_kategori, String nama_kategori, String gambar_kategori, String cover_kategori){
        this.id_kategori = id_kategori;
        this.nama_kategori = nama_kategori;
        this.gambar_kategori = gambar_kategori;
        this.cover_kategori = cover_kategori;
    }

    public int getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(int id_kategori) {
        this.id_kategori = id_kategori;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

    public void setGambar_kategori(String gambar_kategori) {
        this.gambar_kategori = gambar_kategori;
    }

    public String getGambar_kategori() {
        return gambar_kategori;
    }

    public void setCover_kategori(String cover_kategori) {
        this.cover_kategori = cover_kategori;
    }

    public String getCover_kategori() {
        return cover_kategori;
    }
}
