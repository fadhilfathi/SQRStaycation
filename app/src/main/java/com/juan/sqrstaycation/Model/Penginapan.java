package com.juan.sqrstaycation.Model;

public class Penginapan {

    private String judul, alamat, kamar, harga, status, owner;

    public Penginapan(String judul, String alamat, String kamar, String harga, String status, String owner) {
        this.judul = judul;
        this.alamat = alamat;
        this.kamar = kamar;
        this.harga = harga;
        this.status = status;
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Penginapan(){

    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKamar() {
        return kamar;
    }

    public void setKamar(String kamar) {
        this.kamar = kamar;
    }
}
