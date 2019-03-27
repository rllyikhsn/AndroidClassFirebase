package model;

import java.io.Serializable;

public class Objek implements Serializable {

    private String Nama;
    private String Kelas;
    private String Pesan;

    private String Key;

    public Objek(){

    }

    public Objek(String nama, String kelas, String pesan) {
        Nama = nama;
        Kelas = kelas;
        Pesan = pesan;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getKelas() {
        return Kelas;
    }

    public void setKelas(String kelas) {
        Kelas = kelas;
    }

    public String getPesan() {
        return Pesan;
    }

    public void setPesan(String pesan) {
        Pesan = pesan;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    @Override
    public String toString(){
        return " "+Nama+"\n" +
                " "+Kelas+"\n"+
                " "+Pesan;
    }
}
