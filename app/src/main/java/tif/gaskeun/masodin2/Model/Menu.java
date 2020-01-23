package tif.gaskeun.masodin2.Model;

import java.io.Serializable;

public class Menu implements Serializable {
    private String nama,deskripsi,harga,kategori,gambar,mnID;

    public Menu() {
    }

    public Menu(String nama, String deskripsi, String harga, String kategori) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.kategori = kategori;
    }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getHarga() { return harga; }
    public void setHarga(String harga) { this.harga = harga; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public String getMnID() { return mnID; }
    public void setMnID(String mnID) { this.mnID = mnID; }
}
