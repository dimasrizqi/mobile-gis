package id.gis.collection.model.Survey;

/**
 * Created by dell on 20/07/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("no")
    @Expose
    private Integer no;
    @SerializedName("id_survei")
    @Expose
    private String idSurvei;
    @SerializedName("id_jenis")
    @Expose
    private String idJenis;
    @SerializedName("id_objek")
    @Expose
    private String idObjek;
    @SerializedName("wil_survei")
    @Expose
    private String wilSurvei;
    @SerializedName("project")
    @Expose
    private String project;
    @SerializedName("users")
    @Expose
    private String users;
    @SerializedName("jenis")
    @Expose
    private String jenis;
    @SerializedName("objek")
    @Expose
    private String objek;
    @SerializedName("pins")
    @Expose
    private String pins;
    @SerializedName("negara")
    @Expose
    private String negara;
    @SerializedName("provinsi")
    @Expose
    private String provinsi;
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
    @SerializedName("kota")
    @Expose
    private String kota;
    @SerializedName("kec")
    @Expose
    private String kec;
    @SerializedName("desa")
    @Expose
    private String desa;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("jns_utama")
    @Expose
    private String jnsUtama;
    @SerializedName("nama_survei")
    @Expose
    private String namaSurvei;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;
    @SerializedName("hasil")
    @Expose
    private String hasil;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("geom")
    @Expose
    private String geom;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("jns_bangun")
    @Expose
    private String jnsBangun;
    @SerializedName("jum_lantai")
    @Expose
    private String jumLantai;

    public String getJumLantai() {
        return jumLantai;
    }

    public void setJumLantai(String jumLantai) {
        this.jumLantai = jumLantai;
    }

    public String getJnsBangun() {
        return jnsBangun;
    }

    public void setJnsBangun(String jnsBangun) {
        this.jnsBangun = jnsBangun;
    }

    public String getWilSurvei() {
        return wilSurvei;
    }

    public void setWilSurvei(String wilSurvei) {
        this.wilSurvei = wilSurvei;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getIdSurvei() {
        return idSurvei;
    }

    public void setIdSurvei(String idSurvei) {
        this.idSurvei = idSurvei;
    }

    public String getIdJenis() {
        return idJenis;
    }

    public void setIdJenis(String idJenis) {
        this.idJenis = idJenis;
    }

    public String getIdObjek() {
        return idObjek;
    }

    public void setIdObjek(String idObjek) {
        this.idObjek = idObjek;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getObjek() {
        return objek;
    }

    public void setObjek(String objek) {
        this.objek = objek;
    }

    public String getPins() {
        return pins;
    }

    public void setPins(String pins) {
        this.pins = pins;
    }

    public String getNegara() {
        return negara;
    }

    public void setNegara(String negara) {
        this.negara = negara;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getKec() {
        return kec;
    }

    public void setKec(String kec) {
        this.kec = kec;
    }

    public String getDesa() {
        return desa;
    }

    public void setDesa(String desa) {
        this.desa = desa;
    }

    public String getImages() { return images; }

    public void setImages(String images) { this.images = images; }

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

    public String getHasil() {
        return hasil;
    }

    public void setHasil(String hasil) {
        this.hasil = hasil;
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

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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
