package id.gis.collection.model.Login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("jns_klm")
    @Expose
    private String jnsKlm;
    @SerializedName("no_hp")
    @Expose
    private String noHp;
    @SerializedName("tgl_lahir")
    @Expose
    private String tglLahir;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("negara")
    @Expose
    private Negara negara;
    @SerializedName("provinsi")
    @Expose
    private Provinsi provinsi;
    @SerializedName("kota")
    @Expose
    private Kota kota;
    @SerializedName("kecamatan")
    @Expose
    private Kecamatan kecamatan;
    @SerializedName("desa")
    @Expose
    private Desa desa;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("level")
    @Expose
    private String level;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJnsKlm() {
        return jnsKlm;
    }

    public void setJnsKlm(String jnsKlm) {
        this.jnsKlm = jnsKlm;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(String tglLahir) {
        this.tglLahir = tglLahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
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
        return kecamatan;
    }

    public void setKecamatan(Kecamatan kecamatan) {
        this.kecamatan = kecamatan;
    }

    public Desa getDesa() {
        return desa;
    }

    public void setDesa(Desa desa) {
        this.desa = desa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}