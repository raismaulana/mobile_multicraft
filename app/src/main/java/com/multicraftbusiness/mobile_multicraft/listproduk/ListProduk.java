package com.multicraftbusiness.mobile_multicraft.listproduk;

import com.android.volley.toolbox.StringRequest;

public class ListProduk {
    private String nama_produk, foto, deskripsi;
    private double  panjang, lebar, tinggi, berat;
    private int stok, harga, id_produk;

    public ListProduk(int id_produk, String nama_produk,  int harga,double panjang,
                      double lebar,double tinggi, double berat, String deskripsi, String foto, int stok) {
        this.id_produk = id_produk;
        this.nama_produk = nama_produk;
        this.deskripsi= deskripsi;
        this.foto= foto;
        this.harga = harga;
        this.panjang= panjang;
        this.lebar = lebar;
        this.tinggi = tinggi;
        this.berat = berat;
        this.stok = stok;
    }

    public int getId_produk() {
        return id_produk;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getFoto() {
        return foto;
    }

    public int getHarga() {
        return harga;
    }

    public double getPanjang() {
        return panjang;
    }

    public double getLebar() {
        return lebar;
    }

    public double getTinggi() {
        return tinggi;
    }

    public double getBerat() {
        return berat;
    }

    public int getStok() {
        return stok;
    }



    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }
    public void setId(int id_produk) {
        this.id_produk = id_produk;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public void setPanjang(double panjang) {
        this.panjang = panjang;
    }

    public void setLebar(double lebar) {
        this.lebar = lebar;
    }

    public void setTinggi(double tinggi) {
        this.tinggi = tinggi;
    }

    public void setBerat(double berat) {
        this.berat = berat;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }


}
