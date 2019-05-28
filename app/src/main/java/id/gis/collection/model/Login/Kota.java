package id.gis.collection.model.Login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Kota {

    @SerializedName("id_kota")
    @Expose
    private String idKota;
    @SerializedName("nama")
    @Expose
    private String nama;

    public String getIdKota() {
        return idKota;
    }

    public void setIdKota(String idKota) {
        this.idKota = idKota;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

}