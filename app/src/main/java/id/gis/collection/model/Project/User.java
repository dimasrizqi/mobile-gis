package id.gis.collection.model.Project;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 10/08/18.
 */

public class User {

    @SerializedName("id_users")
    @Expose
    private String idUsers;
    @SerializedName("nama")
    @Expose
    private String nama;

    public String getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(String idUsers) {
        this.idUsers = idUsers;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

}