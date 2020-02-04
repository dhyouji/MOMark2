package tif.gaskeun.masodin2.Model;

import java.io.Serializable;

public class Order2 implements Serializable {
    private String mnID, mnNama, mnHarga, mnQty,waktu,nama,kontak,nmeja;

    public Order2() {
    }

    public Order2(String mnID, String mnNama, String mnHarga, String mnQty, String waktu, String nama, String kontak, String nmeja) {
        this.mnID = mnID;
        this.mnNama = mnNama;
        this.mnHarga = mnHarga;
        this.mnQty = mnQty;
        this.waktu = waktu;
        this.nama = nama;
        this.kontak = kontak;
        this.nmeja = nmeja;
    }

    public String getMnID() {
        return mnID;
    }

    public void setMnID(String mnID) {
        this.mnID = mnID;
    }

    public String getMnNama() {
        return mnNama;
    }

    public void setMnNama(String mnNama) {
        this.mnNama = mnNama;
    }

    public String getMnHarga() {
        return mnHarga;
    }

    public void setMnHarga(String mnHarga) {
        this.mnHarga = mnHarga;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKontak() {
        return kontak;
    }

    public void setKontak(String kontak) {
        this.kontak = kontak;
    }

    public String getNmeja() {
        return nmeja;
    }

    public void setNmeja(String nmeja) {
        this.nmeja = nmeja;
    }

    public String getMnQty() {
        return mnQty;
    }

    public void setMnQty(String mnQty) {
        this.mnQty = mnQty;
    }
}
