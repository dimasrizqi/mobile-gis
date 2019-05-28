package id.gis.collection.model.Login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Negara {

    @SerializedName("id_negara")
    @Expose
    private String idNegara;
    @SerializedName("nama")
    @Expose
    private String nama;

    public String getIdNegara() {
        return idNegara;
    }

    public void setIdNegara(String idNegara) {
        this.idNegara = idNegara;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

}