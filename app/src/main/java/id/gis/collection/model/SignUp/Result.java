package id.gis.collection.model.SignUp;

/**
 * Created by dell on 16/07/18.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("create")
    @Expose
    private String create;

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

}