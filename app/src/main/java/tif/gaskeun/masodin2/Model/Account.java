package tif.gaskeun.masodin2.Model;

import java.io.Serializable;

public class Account implements Serializable {

    private String namastaff;
    private String username;
    private String password;

    public Account() {
    }

    public Account(String namastaff, String username, String password) {
        this.namastaff = namastaff;
        this.username = username;
        this.password = password;
    }

    public String getNamastaff() {
        return namastaff;
    }

    public void setNamastaff(String namastaff) {
        this.namastaff = namastaff;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
