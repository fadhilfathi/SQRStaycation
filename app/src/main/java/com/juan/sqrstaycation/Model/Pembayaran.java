package com.juan.sqrstaycation.Model;

public class Pembayaran {

    String customer, penginapan, gambar, status;

    public Pembayaran(String customer, String penginapan, String gambar, String status) {
        this.customer = customer;
        this.penginapan = penginapan;
        this.gambar = gambar;
        this.status = status;
    }

    public Pembayaran(){

    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getPenginapan() {
        return penginapan;
    }

    public void setPenginapan(String penginapan) {
        this.penginapan = penginapan;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
