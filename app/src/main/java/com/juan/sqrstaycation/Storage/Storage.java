package com.juan.sqrstaycation.Storage;

public class Storage {

    public Storage() {
    }

    String judul;
    String alamat;
    String kamar;
    String harga;
    String jenis;
    String nama;
    String idPenginapan;

    public String getIdPenginapan() {
        return idPenginapan;
    }

    public void setIdPenginapan(String idPenginapan) {
        this.idPenginapan = idPenginapan;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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
