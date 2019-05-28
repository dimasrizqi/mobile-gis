package id.gis.collection.api;

/**
 * Created by dell on 15/07/18.
 */

public class ApiUrl {
    //https://apimonitoring.herokuapp.com/
    //http://api.simjalapandeglang.com/
    public final static String BASE_URL = "http://api.simjalapandeglang.com/";
    public final static String LOGIN_URL = "user/valid";
    public final static String FORGOT = "user/forgot";
    public final static String SIGNUP_URL = "user/create";
    public final static String INFO_USER = "user/info?api_key=b292f7cdc712e0110f62b6d333b9ff86";
    public final static String SURVEI_STORE = "survei/create";
    public final static String SURVEI_UPDATE = "survei/update";
    public final static String SURVEI_DELETE= "survei/delete";
    public final static String UPDATE_POSITION = "user/update_position";
    public final static String UPDATE_PROFILE = "user/update";

    public final static String DAFTAR_PROVINSI = "wilayah/lists_prov?api_key=b292f7cdc712e0110f62b6d333b9ff86";
    public final static String DAFTAR_KOTA = "wilayah/lists_kota?api_key=b292f7cdc712e0110f62b6d333b9ff86";
    public final static String DAFTAR_KEC = "wilayah/lists_kec?api_key=b292f7cdc712e0110f62b6d333b9ff86";
    public final static String DAFTAR_DESA = "wilayah/lists_desa?api_key=b292f7cdc712e0110f62b6d333b9ff86";

    public final static String DAFTAR_SURVEY_PROJEK = "wilsurvei/lists?api_key=b292f7cdc712e0110f62b6d333b9ff86";
    public final static String DAFTAR_JENIS = "objek/lists_jenis?api_key=b292f7cdc712e0110f62b6d333b9ff86";
    public final static String DAFTAR_OBJEK = "objek/lists_objek?api_key=b292f7cdc712e0110f62b6d333b9ff86";
    public final static String DAFTAR_PROJECT = "refproject/infos?api_key=b292f7cdc712e0110f62b6d333b9ff86";
    public final static String DAFTAR_SUVERY = "survei/infos_detail?api_key=b292f7cdc712e0110f62b6d333b9ff86";
//    public final static String DETAIL_SURVEY = "info?api_key=b292f7cdc712e0110f62b6d333b9ff86";
}
