package tif.gaskeun.masodin2.Model;

public class Informasi {
    private String namastaff,restoran,alamat,kontak,email;

    public Informasi() {
    }

    public Informasi(String namastaff, String restoran, String alamat, String kontak, String email) {
        this.namastaff = namastaff;
        this.restoran = restoran;
        this.alamat = alamat;
        this.kontak = kontak;
        this.email = email;
    }

    public String getNamastaff() {
        return namastaff;
    }

    public void setNamastaff(String namastaff) {
        this.namastaff = namastaff;
    }

    public String getRestoran() {
        return restoran;
    }

    public void setRestoran(String restoran) {
        this.restoran = restoran;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKontak() {
        return kontak;
    }

    public void setKontak(String kontak) {
        this.kontak = kontak;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
