package id.gis.collection.model.DetailSurvey;

/**
 * Created by dell on 21/08/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import id.gis.collection.model.Login.Desa;
import id.gis.collection.model.Login.Kecamatan;
import id.gis.collection.model.Login.Kota;
import id.gis.collection.model.Login.Negara;
import id.gis.collection.model.Login.Provinsi;

public class Result {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("id_project")
    @Expose
    private IdProject idProject;
    @SerializedName("wil_survei")
    @Expose
    private WilSurvei wilSurvei;
    @SerializedName("id_users")
    @Expose
    private IdUsers idUsers;
    @SerializedName("jenis")
    @Expose
    private Jenis jenis;
    @SerializedName("object")
    @Expose
    private Object object;
    @SerializedName("negara")
    @Expose
    private Negara negara;
    @SerializedName("provinsi")
    @Expose
    private Provinsi provinsi;
    @SerializedName("kota")
    @Expose
    private Kota kota;
    @SerializedName("kec")
    @Expose
    private Kecamatan kec;
    @SerializedName("desa")
    @Expose
    private Desa desa;
    @SerializedName("jns_utama")
    @Expose
    private String jnsUtama;
    @SerializedName("nama_survei")
    @Expose
    private String namaSurvei;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("jns_bangun")
    @Expose
    private String jnsBangun;
    @SerializedName("jum_lantai")
    @Expose
    private String jumLantai;
    @SerializedName("geom")
    @Expose
    private String geom;
    @SerializedName("updated_at")
    @Expose
    private java.lang.Object updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("file")
    @Expose
    private List<File> file = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IdProject getIdProject() {
        return idProject;
    }

    public void setIdProject(IdProject idProject) {
        this.idProject = idProject;
    }

    public WilSurvei getWilSurvei() {
        return wilSurvei;
    }

    public void setWilSurvei(WilSurvei wilSurvei) {
        this.wilSurvei = wilSurvei;
    }

    public IdUsers getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(IdUsers idUsers) {
        this.idUsers = idUsers;
    }

    public Jenis getJenis() {
        return jenis;
    }

    public void setJenis(Jenis jenis) {
        this.jenis = jenis;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Negara getNegara() {
        return negara;
    }

    public void setNegara(Negara negara) {
        this.negara = negara;
    }

    public Provinsi getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(Provinsi provinsi) {
        this.provinsi = provinsi;
    }

    public Kota getKota() {
        return kota;
    }

    public void setKota(Kota kota) {
        this.kota = kota;
    }

    public Kecamatan getKecamatan() {
        return kec;
    }

    public void setKecamatan(Kecamatan kec) {
        this.kec = kec;
    }

    public Desa getDesa() {
        return desa;
    }

    public void setDesa(Desa desa) {
        this.desa = desa;
    }

    public String getJnsUtama() {
        return jnsUtama;
    }

    public void setJnsUtama(String jnsUtama) {
        this.jnsUtama = jnsUtama;
    }

    public String getNamaSurvei() {
        return namaSurvei;
    }

    public void setNamaSurvei(String namaSurvei) {
        this.namaSurvei = namaSurvei;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJnsBangun() {
        return jnsBangun;
    }

    public void setJnsBangun(String jnsBangun) {
        this.jnsBangun = jnsBangun;
    }

    public String getJumLantai() {
        return jumLantai;
    }

    public void setJumLantai(String jumLantai) {
        this.jumLantai = jumLantai;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public java.lang.Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.lang.Object updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<File> getFile() {
        return file;
    }

    public void setFile(List<File> file) {
        this.file = file;
    }

    class File {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("id_type")
        @Expose
        private String idType;
        @SerializedName("id_survei")
        @Expose
        private String idSurvei;
        @SerializedName("nama")
        @Expose
        private String nama;
        @SerializedName("ket")
        @Expose
        private String ket;
        @SerializedName("link")
        @Expose
        private java.lang.Object link;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("images")
        @Expose
        private String images;
        @SerializedName("videos")
        @Expose
        private String videos;
        @SerializedName("files")
        @Expose
        private String files;
        @SerializedName("records")
        @Expose
        private String records;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIdType() {
            return idType;
        }

        public void setIdType(String idType) {
            this.idType = idType;
        }

        public String getIdSurvei() {
            return idSurvei;
        }

        public void setIdSurvei(String idSurvei) {
            this.idSurvei = idSurvei;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getKet() {
            return ket;
        }

        public void setKet(String ket) {
            this.ket = ket;
        }

        public java.lang.Object getLink() {
            return link;
        }

        public void setLink(java.lang.Object link) {
            this.link = link;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getVideos() {
            return videos;
        }

        public void setVideos(String videos) {
            this.videos = videos;
        }

        public String getFiles() {
            return files;
        }

        public void setFiles(String files) {
            this.files = files;
        }

        public String getRecords() {
            return records;
        }

        public void setRecords(String records) {
            this.records = records;
        }

    }

    class IdProject {

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

    class WilSurvei {

        @SerializedName("id_wil_survei")
        @Expose
        private String idWilSurvei;
        @SerializedName("nama")
        @Expose
        private String nama;

        public String getIdWilSurvei() {
            return idWilSurvei;
        }

        public void setIdWilSurvei(String idWilSurvei) {
            this.idWilSurvei = idWilSurvei;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

    }

    class IdUsers {

        @SerializedName("id_user")
        @Expose
        private String idUser;
        @SerializedName("nama")
        @Expose
        private String nama;

        public String getIdUser() {
            return idUser;
        }

        public void setIdUser(String idUser) {
            this.idUser = idUser;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

    }

    class Jenis {

        @SerializedName("id_jenis")
        @Expose
        private String idJenis;
        @SerializedName("nama")
        @Expose
        private String nama;

        public String getIdJenis() {
            return idJenis;
        }

        public void setIdJenis(String idJenis) {
            this.idJenis = idJenis;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

    }

    class Object {

        @SerializedName("id_objek")
        @Expose
        private String idObjek;
        @SerializedName("nama")
        @Expose
        private String nama;
        @SerializedName("pins")
        @Expose
        private String pins;

        public String getIdObjek() {
            return idObjek;
        }

        public void setIdObjek(String idObjek) {
            this.idObjek = idObjek;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getPins() {
            return pins;
        }

        public void setPins(String pins) {
            this.pins = pins;
        }

    }
}