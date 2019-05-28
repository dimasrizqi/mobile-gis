package id.gis.collection.api;

import java.util.List;

import id.gis.collection.model.DetailSurvey.ResponseDetailSurvey;
import id.gis.collection.model.Jenis.ResponseJenisList;
import id.gis.collection.model.Login.ResponseLoginUser;
import id.gis.collection.model.Objek.ResponseObjekList;
import id.gis.collection.model.Project.ResponseProjectList;
import id.gis.collection.model.SignUp.ResponseSignUp;
import id.gis.collection.model.Survey.ResponseSurveyList;
import id.gis.collection.model.Survey.Project.ResponseSurveyProject;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by dell on 15/07/18.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST(ApiUrl.LOGIN_URL)
    Call<ResponseLoginUser> loginUser(@Field("api_key") String api_key, @Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST(ApiUrl.SIGNUP_URL)
    Call<ResponseSignUp> signUpUser(@Field("api_key") String api_key, @Field("username") String username, @Field("nama") String name, @Field("email") String email, @Field("no_hp") String no_hp, @Field("password") String password, @Field("level") String level);

    @Multipart
    @POST(ApiUrl.SURVEI_STORE)
    Call<ResponseSignUp> surveyStore(
        @Part("api_key") RequestBody api_key,
        @Part("id_project") RequestBody id_project,
        @Part("id_wil_survei") RequestBody id_survey,
        @Part("id_users") RequestBody id_users,
        @Part("id_jenis") RequestBody id_jenis,
        @Part("id_objek") RequestBody id_objek,
        @Part("jns_utama") RequestBody jns_utama,
        @Part("nama_survei") RequestBody nama_survei,
        @Part("keterangan") RequestBody keterangan,
        @Part("alamat") RequestBody alamat,
        @Part("jns_bangun") RequestBody jns_bangun,
        @Part("jum_lantai") RequestBody jum_lantai,
        @Part("latitude") RequestBody latitude,
        @Part("longitude") RequestBody longitude,
        @Part("status") RequestBody status,
        @Part("geom") RequestBody geom,
        @Part List<MultipartBody.Part> images,
        @Part List<MultipartBody.Part> videos,
        @Part List<MultipartBody.Part> records,
        @Part List<MultipartBody.Part> files
    );

    @FormUrlEncoded
    @POST(ApiUrl.SURVEI_STORE)
    Call<ResponseSignUp> surveyStoreOffline(
        @Field("api_key") String api_key,
        @Field("id_users") String id_user,
        @Field("nama_survei") String nama_survei,
        @Field("keterangan") String keterangan,
        @Field("alamat") String alamat,
        @Field("latitude") String lat,
        @Field("longitude") String lng,
        @Field("geom") String geom
    );

    @FormUrlEncoded
    @POST(ApiUrl.FORGOT)
    Call<ResponseSignUp> forgotPassword(
        @Field("api_key") String api_key,
        @Field("username") String username,
        @Field("email") String email
    );

    @Multipart
    @POST(ApiUrl.UPDATE_PROFILE)
    Call<ResponseSignUp> profileUpdate(
        @Part("api_key") RequestBody api_key,
        @Part("id") RequestBody id_user,
        @Part("nama") RequestBody nama,
        @Part("alamat") RequestBody alamat,
        @Part("email") RequestBody email,
        @Part("no_hp") RequestBody no_hp,
        @Part("jns_klm") RequestBody gender,
        @Part("tgl_lahir") RequestBody tgl_lahir,
        @Part("password") RequestBody password,
        @Part MultipartBody.Part avatar
    );

    @Multipart
    @POST(ApiUrl.UPDATE_PROFILE)
    Call<ResponseSignUp> profileUpdate(
        @Part("api_key") RequestBody api_key,
        @Part("id") RequestBody id_user,
        @Part("nama") RequestBody nama,
        @Part("alamat") RequestBody alamat,
        @Part("email") RequestBody email,
        @Part("no_hp") RequestBody no_hp,
        @Part("jns_klm") RequestBody gender,
        @Part("tgl_lahir") RequestBody tgl_lahir,
        @Part("password") RequestBody password
    );

    @FormUrlEncoded
    @POST(ApiUrl.UPDATE_POSITION)
    Call<ResponseSignUp> updatePosition(
        @Field("api_key") String api_key,
        @Field("id") String id,
        @Field("lon") double lng,
        @Field("lat") double lat
    );

    @FormUrlEncoded
    @POST(ApiUrl.SURVEI_DELETE)
    Call<ResponseSignUp> deleteSurvey(
        @Field("api_key") String api_key,
        @Field("id") String id
    );

    @Multipart
    @POST(ApiUrl.SURVEI_UPDATE)
    Call<ResponseSignUp> updateSurvey(
        @Part("api_key") RequestBody api_key,
        @Part("id") RequestBody id,
        @Part("id_project") RequestBody id_project,
        @Part("id_wil_survei") RequestBody id_survey,
        @Part("id_users") RequestBody id_users,
        @Part("id_jenis") RequestBody id_jenis,
        @Part("id_objek") RequestBody id_objek,
        @Part("jns_utama") RequestBody jns_utama,
        @Part("nama_survei") RequestBody nama_survei,
        @Part("keterangan") RequestBody keterangan,
        @Part("alamat") RequestBody alamat,
        @Part("jns_bangun") RequestBody jns_bangun,
        @Part("jum_lantai") RequestBody jum_lantai,
        @Part("latitude") RequestBody latitude,
        @Part("longitude") RequestBody longitude,
        @Part("status") RequestBody status,
        @Part("geom") RequestBody geom,
        @Part List<MultipartBody.Part> images,
        @Part List<MultipartBody.Part> videos,
        @Part List<MultipartBody.Part> records,
        @Part List<MultipartBody.Part> files
    );

//    @GET(ApiUrl.DAFTAR_PROVINSI)
//    Call<ResponseProvinceList> getProvinceList();
//
//    @GET(ApiUrl.DAFTAR_KOTA)
//    Call<ResponseCityList> getKotaList(@Query("subid") String idProvinsi);
//
//    @GET(ApiUrl.DAFTAR_KEC)
//    Call<ResponseKecamatanList> getKecList(@Query("subid") String idKota);
//
//    @GET(ApiUrl.DAFTAR_DESA)
//    Call<ResponseDesaList> getDesaList(@Query("subid") String idKec);

//    @GET(ApiUrl.DETAIL_SURVEY)
//    Call<ResponseDetailSurvey> getDetailSurvey(@Query("id") String idSurvey);

    @GET(ApiUrl.DAFTAR_SURVEY_PROJEK)
    Call<ResponseSurveyProject> getSurveyProject(@Query("id_project") String idProjek);

    @GET(ApiUrl.INFO_USER)
    Call<ResponseLoginUser> getUserDetail(@Query("id") String idUser);

    @GET(ApiUrl.DAFTAR_JENIS)
    Call<ResponseJenisList> getJenisList();

    @GET(ApiUrl.DAFTAR_OBJEK)
    Call<ResponseObjekList> getObjekList(@Query("subid") String idJenis);

    @GET(ApiUrl.DAFTAR_PROJECT)
    Call<ResponseProjectList> getProjectList(@Query("id_users") String idUser);

    @GET(ApiUrl.DAFTAR_SUVERY)
    Call<ResponseSurveyList> getSurveyList(
        @Query("id_users") String idUser,
        @Query("offset") Integer offset,
        @Query("limit") Integer limit,
        @Query("q") String query,
        @Query("tglawal") String tglawal,
        @Query("tglakhir") String tglakhir,
        @Query("objek") String objek,
        @Query("jenis") String jenis,
        @Query("id_project") String idProject
    );
}
