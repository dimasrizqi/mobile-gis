package id.gis.collection.model.Login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 20/07/18.
 */

public class Desa {
    @SerializedName("id_desa")
    @Expose
    private String idDesa;
    @SerializedName("nama")
    @Expose
    private String nama;

    public String getIdDesa() {
        return idDesa;
    }

    public void setidDesa(String idDesa) {
        this.idDesa = idDesa;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
