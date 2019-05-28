package id.gis.collection.model.Project;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 10/08/18.
 */

public class Project {

    @SerializedName("id_project")
    @Expose
    private String idProject;
    @SerializedName("nama")
    @Expose
    private String nama;

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

}