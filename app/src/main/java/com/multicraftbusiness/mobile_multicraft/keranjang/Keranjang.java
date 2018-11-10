package com.multicraftbusiness.mobile_multicraft.keranjang;

public class Keranjang {
    private int id_pesan, harga, total_harga, jumlah, id_produk, stok;
    private double berat, total_berat;
    private String nama_produk, foto;


    public Keranjang(int id_pesan, String nama_produk, int harga, double berat, int jumlah,
                     int total_harga, double total_berat, int stok, String foto, int id_produk) {
        this.id_pesan = id_pesan;
        this.nama_produk = nama_produk;
        this.harga = harga;
        this.berat = berat;
        this.jumlah = jumlah;
        this.total_harga = total_harga;
        this.total_berat = total_berat;
        this.stok = stok;
        this.foto = foto;
        this.id_produk = id_produk;
    }

    public int getId_pesan() {
        return id_pesan;
    }

    public void setId_pesan(int id_pesan) {
        this.id_pesan = id_pesan;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public double getBerat() {
        return berat;
    }

    public void setBerat(double berat) {
        this.berat = berat;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(int total_harga) {
        this.total_harga = total_harga;
    }

    public double getTotal_berat() {
        return total_berat;
    }

    public void setTotal_berat(double total_berat) {
        this.total_berat = total_berat;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getId_produk() {
        return id_produk;
    }

    public void setId_produk(int id_produk) {
        this.id_produk = id_produk;
    }
}
