package id.gis.collection.ui.addplace;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.IndeterminateProgressButton;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.iceteck.silicompressorr.SiliCompressor;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.kbeanie.multipicker.utils.FileUtils;
import com.schibstedspain.leku.LocationPickerActivity;
import com.schibstedspain.leku.LocationPickerActivityKt;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import id.gis.collection.R;
import id.gis.collection.api.RestClient;
import id.gis.collection.data.db.PlaceDatabase;
import id.gis.collection.data.entity.PlaceEntity;
import id.gis.collection.model.Jenis.ResponseJenisList;
import id.gis.collection.model.Objek.ResponseObjekList;
import id.gis.collection.model.Project.ResponseProjectList;
import id.gis.collection.model.SignUp.ResponseSignUp;
import id.gis.collection.model.Survey.Project.ResponseSurveyProject;
import id.gis.collection.ui.common.BaseActivity;
import id.gis.collection.ui.login.LoginActivity;
import id.gis.collection.ui.main.MainActivity;
import id.gis.collection.ui.place.PlaceActivity;
import id.gis.collection.utils.AppConstant;
import id.gis.collection.utils.IqbalUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class AddPlaceActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String TAG = "Add Place";
    private static final int MAP_BUTTON_REQUEST_CODE = 1212;
    private final String DATABASE_NAME = "place_db";
    private final int PLACE_PICKER_REQUEST = 1;
    private OnAboutDataReceivedListener mAboutDataListener;

    PlaceEntity placeEntity;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.fileFragment)
    LinearLayout mFileFragment;

    @BindView(R.id.et_name)
    EditText mEtName;

    @BindView(R.id.til_name)
    TextInputLayout mTilName;

    @BindView(R.id.et_desc)
    EditText mEtDesc;

    @BindView(R.id.til_desc)
    TextInputLayout mTilDesc;

    @BindView(R.id.et_address)
    EditText mEtAddress;

    @BindView(R.id.til_address)
    TextInputLayout mTilAddress;

    @BindView(R.id.et_lat)
    EditText mEtLat;

    @BindView(R.id.til_lat)
    TextInputLayout mTilLat;

    @BindView(R.id.et_lng)
    EditText mEtLng;

    @BindView(R.id.til_jenis)
    TextInputLayout mTilJenis;

    @BindView(R.id.spJenis)
    MaterialSpinner mSpJenis;

    @BindView(R.id.pbLoadJenis)
    ProgressBar mPbloadJenis;

    @BindView(R.id.til_objek)
    TextInputLayout mTilObjek;

    @BindView(R.id.spObjek)
    MaterialSpinner mSpObjek;

    @BindView(R.id.pbLoadObjek)
    ProgressBar mPbloadObjek;

    @BindView(R.id.til_jenis_bangunan)
    TextInputLayout mTilJB;

    @BindView(R.id.spJenisBangunan)
    MaterialSpinner mSpJenisBangunan;

    @BindView(R.id.til_jum_lantai)
    TextInputLayout mTilJL;

    @BindView(R.id.et_jml_lantai)
    EditText mEtJmlLantai;

    @BindView(R.id.til_project)
    TextInputLayout mTilProject;

    @BindView(R.id.spProjek)
    MaterialSpinner mSpProjek;

    @BindView(R.id.pbLoadProjek)
    ProgressBar mPbloadProjek;

    @BindView(R.id.til_survey)
    TextInputLayout mTilSurvei;

    @BindView(R.id.spSurvei)
    MaterialSpinner mSpSurvei;

    @BindView(R.id.pbLoadSurvei)
    ProgressBar mPbloadSurvei;

    @BindView(R.id.btn_submit)
    IndeterminateProgressButton mBtnSubmit;

    @BindView(R.id.tabs)
    TabLayout mTabs;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.resultAddr1)
    TextView mResultAddr1;

    @BindView(R.id.resultAddr2)
    TextView mResultAddr2;

    @BindView(R.id.resultGeom)
    TextView mResultGeom;

    @BindView(R.id.resultAkurasi)
    TextView mResultAkurasi;

    private PlaceDatabase placeDatabase;
    private AddPlaceViewModel mViewModel;
    private GoogleMap mMap;
    private SupportMapFragment mMapFrament;

    private List<String> videos = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    private List<String> files = new ArrayList<>();

    double longitude, latitude;
    List<id.gis.collection.model.Jenis.Result> mJenisData;
    List<id.gis.collection.model.Objek.Result> mObjekData;
    List<id.gis.collection.model.Project.Result> mProjectData;
    List<id.gis.collection.model.Survey.Project.Result> mSurveyData;
    ArrayList<String> mJenisName;
    ArrayList<String> mObjekName;
    ArrayList<String> mProjectName;
    ArrayList<String> mSurveyName;
    String audioPath, idJenis, idObjek, idProject, idUser, idWilSurvey, idSurvey, geom;


    @Override
    public int getResLayout() {
        return R.layout.activity_add_place;
    }

    public interface OnAboutDataReceivedListener {
        void onDataReceived(List<String> intentImg);
    }

    public void setAboutDataListener(OnAboutDataReceivedListener listener) {
        this.mAboutDataListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mMapFrament = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.miniMap);
        mMapFrament.getMapAsync(this);
        initToolbar();
        initContentViews();
        initViewModel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1212) {
            mEtAddress.setText(data.getStringExtra("location_address"));
            mEtLat.setText(String.valueOf(data.getDoubleExtra("latitude", 0.0)));
            mEtLng.setText(String.valueOf(data.getDoubleExtra("longitude", 0.0)));
            geom = String.valueOf(data.getDoubleExtra(LocationPickerActivityKt.LATITUDE, 0.0)) + String.valueOf(data.getDoubleExtra(LocationPickerActivityKt.LONGITUDE, 0.0));
            mResultGeom.setText(geom);

            mResultAkurasi.setText("Akurasi " + (int) 10 + " meter");

            MainActivity.MarkerInfo markerInfo = new MainActivity.MarkerInfo();
            Gson gson = new Gson();
            markerInfo.setTitle("Tempat Dipilih");
            markerInfo.setGeom(geom);
            String mInfoCurr = gson.toJson(markerInfo);

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(data.getDoubleExtra(LocationPickerActivityKt.LATITUDE, 0.0), data.getDoubleExtra(LocationPickerActivityKt.LONGITUDE, 0.0)), 17);
            mMap.moveCamera(cameraUpdate);
            mMap.clear();
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(data.getDoubleExtra(LocationPickerActivityKt.LATITUDE, 0.0), data.getDoubleExtra(LocationPickerActivityKt.LONGITUDE, 0.0)))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_me))
                    .snippet(mInfoCurr));
        }

        if (requestCode == 0 && resultCode == RESULT_OK) {
            ThreeFragment frag3 = (ThreeFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
            frag3.setAudioName();
        }

        if (requestCode == Picker.PICK_IMAGE_DEVICE && resultCode == RESULT_OK) {
            OneFragment frag1 = (OneFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
            frag1.setImagePicker(data);
        }

        if (requestCode == Picker.PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {
            OneFragment frag1 = (OneFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
            frag1.setImageCameraPicker(data);
        }

        if (requestCode == Picker.PICK_VIDEO_DEVICE && resultCode == RESULT_OK) {
            TwoFragment frag2 = (TwoFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
            frag2.setVideoPicker(data);
        }

        if (requestCode == Picker.PICK_VIDEO_CAMERA && resultCode == RESULT_OK) {
            TwoFragment frag2 = (TwoFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
            frag2.setVideoCameraPicker(data);
        }

        if (requestCode == Picker.PICK_FILE && resultCode == RESULT_OK) {
            FourFragment frag4 = (FourFragment) mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem());
            frag4.setFilePicker(data);
        }

        // You MUST have this line to be here
        // so ImagePicker can work with fragment mode
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initToolbar() {
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Lokasi");
    }


    private void morphToSquare(final IndeterminateProgressButton btnMorph, int duration) {
        MorphingButton.Params circle = MorphingButton.Params.create()
            .duration(duration)
            .cornerRadius(70)
            .height(FrameLayout.LayoutParams.WRAP_CONTENT)
            .width(FrameLayout.LayoutParams.FILL_PARENT)
            .color(getResources().getColor(R.color.colorAccent))
            .text("SUBMIT")
            .colorPressed(getResources().getColor(R.color.accentPressed));
        btnMorph.morph(circle);
    }

    private void initContentViews() {
        mResultGeom.setOnClickListener((v) -> openLocationPicker());
        if (IqbalUtils.readPreference(AddPlaceActivity.this, "online", "").equals("0")){
            mTilProject.setVisibility(View.GONE);
            mTilSurvei.setVisibility(View.GONE);
            mTilSurvei.setVisibility(View.GONE);
            mTilJenis.setVisibility(View.GONE);
            mTilObjek.setVisibility(View.GONE);
            mTilJL.setVisibility(View.GONE);
            mResultAddr2.setVisibility(View.GONE);
            mResultAddr1.setVisibility(View.GONE);
            mFileFragment.setVisibility(View.GONE);
        }
        morphToSquare(mBtnSubmit, 0);

        setupViewPager(mViewPager);
        mTabs.setupWithViewPager(mViewPager);
        setupTabIcons();
        mViewPager.setOffscreenPageLimit(3);
        mTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 2:
                        ThreeFragment frag3 = (ThreeFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":2");
                        frag3.setName(mEtName.getText().toString());
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Gson gson = new Gson();
                MainActivity.MarkerInfo infoMarker = gson.fromJson(marker.getSnippet(), MainActivity.MarkerInfo.class);
                View v = getLayoutInflater().inflate(R.layout.echo_info_window, null);
                TextView tvName = v.findViewById(R.id.nameSurvey);

                tvName.setText(IqbalUtils.capitalize(infoMarker.getTitle()));

                return v;
            }
        });

        if (getIntent().getBooleanExtra("edit", false)) {
            MainActivity.MarkerInfo markerInfo = new MainActivity.MarkerInfo();
            Gson gson = new Gson();
            markerInfo.setTitle("Tempat Dipilih");
            markerInfo.setGeom(getIntent().getStringExtra("geom"));
            String mInfoCurr = gson.toJson(markerInfo);
            double lat = 0, lon = 0;
            if (IqbalUtils.readPreference(AddPlaceActivity.this, "online", "").equals("0")){
                lat = Double.parseDouble(String.valueOf(getIntent().getFloatExtra("lat", (float) 0.0)));
                lon = Double.parseDouble(String.valueOf(getIntent().getFloatExtra("lon", (float) 0.0)));
            }else{
                lat = Double.parseDouble(getIntent().getStringExtra("lat"));
                lon = Double.parseDouble(getIntent().getStringExtra("lon"));
            }

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 17);
            mMap.moveCamera(cameraUpdate);
            mMap.clear();

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lon))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_me))
                    .snippet(mInfoCurr));
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        List<String> intentImgs = new ArrayList<>();
        List<String> intentVids = new ArrayList<>();
        List<String> intentFiles = new ArrayList<>();
        String rec = "";
        if (getIntent().getBooleanExtra("edit", false) && IqbalUtils.readPreference(AddPlaceActivity.this, "online", "").equals("1")){
            if (getIntent().getStringArrayListExtra("images").size() > 0){
                intentImgs = getIntent().getStringArrayListExtra("images");
            }
            if (getIntent().getStringArrayListExtra("videos").size() > 0){
                intentVids = getIntent().getStringArrayListExtra("videos");
            }
            if (getIntent().getStringArrayListExtra("files").size() > 0){
                intentFiles = getIntent().getStringArrayListExtra("files");
            }
            if(!getIntent().getStringExtra("audio").isEmpty()){
                rec = getIntent().getStringExtra("audio");
            }
        }

        adapter.addFrag(new OneFragment().newInstance(intentImgs), "ONE");
        adapter.addFrag(new TwoFragment().newInstance(intentVids), "TWO");
        adapter.addFrag(new ThreeFragment().newInstance(rec), "THREE");
        adapter.addFrag(new FourFragment().newInstance(intentFiles), "FOUR");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {

//        Drawable add_photo = ContextCompat.getDrawable(AddPlaceActivity.this, R.drawable.ic_add_photo);
//        add_photo.setBounds(0, 0, 80, 80);
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Image");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_add_photo, 0, 0);
        mTabs.getTabAt(0).setCustomView(tabOne);

//        Drawable add_video = ContextCompat.getDrawable(AddPlaceActivity.this, R.drawable.ic_add_video);
//        add_video.setBounds(0, 0, 80, 80);
        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Video");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_add_video, 0, 0);
        mTabs.getTabAt(1).setCustomView(tabTwo);

//        Drawable add_audio = ContextCompat.getDrawable(AddPlaceActivity.this, R.drawable.ic_note);
//        add_audio.setBounds(0, 0, 80, 80);
        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Audio");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_note, 0, 0);
        mTabs.getTabAt(2).setCustomView(tabThree);

//        Drawable add_file = ContextCompat.getDrawable(AddPlaceActivity.this, R.drawable.ic_attach_file);
//        add_file.setBounds(0, 0, 80, 80);
        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText("File");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_attach_file, 0, 0);
        mTabs.getTabAt(3).setCustomView(tabFour);
    }


    @SuppressLint("MissingPermission")
    private void openLocationPicker() {
//        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//        try {
//            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }
        LocationRequest request = new LocationRequest();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            client.requestLocationUpdates(request, new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    longitude = locationResult.getLastLocation().getLongitude();
                    latitude = locationResult.getLastLocation().getLatitude();
                }
            }, null);

        Intent locationPickerIntent = new LocationPickerActivity.Builder()
            .withLocation(latitude , longitude)
            .withGeolocApiKey("AIzaSyBlOTwWenPIwkjjUJFt_529NrsI1UJs0H0")
            .shouldReturnOkOnBackPressed()
            .withSearchZone("id_ID")
            .withGooglePlacesEnabled()
            .withVoiceSearchHidden()
            .build(AddPlaceActivity.this);

        startActivityForResult(locationPickerIntent, MAP_BUTTON_REQUEST_CODE);
    }

    private void initViewModel() {
        ProgressDialog mPd = ProgressDialog.show(AddPlaceActivity.this, "Ambil Data", "Mohon menunggu..", true, false);
        if (IqbalUtils.readPreference(AddPlaceActivity.this, "online", "").equals("1")) {
            idUser = IqbalUtils.readPreference(this, "id", "");
            mSpProjek.setDropdownMaxHeight(300);
            mSpJenis.setDropdownMaxHeight(300);
            mSpJenisBangunan.setDropdownMaxHeight(300);
            mSpSurvei.setDropdownMaxHeight(300);
            mSpObjek.setDropdownMaxHeight(300);
            mSpObjek.setItems("Pilih Objek");
            mSpSurvei.setItems("Pilih Survei");

            //PROJECT
            mProjectName = new ArrayList<>();
            if (mProjectName.size() > 0) {
                mProjectName.clear();
            }
            mProjectName.add(0, "Pilih Projek");
            mSpProjek.setClickable(false);
            mSpProjek.setBackgroundColor(getResources().getColor(R.color.colorBackGrey));

            Call<ResponseProjectList> callProject = RestClient.get().getProjectList(idUser);
            callProject.enqueue(new Callback<ResponseProjectList>() {
                @Override
                public void onResponse(Call<ResponseProjectList> call, Response<ResponseProjectList> response) {
                    mPbloadProjek.setVisibility(View.GONE);
                    mSpProjek.setClickable(true);
                    if (response.isSuccessful()) {
                        mProjectData = response.body().getResult();
                        for (int a = 0; a < mProjectData.size(); a++) {
                            mProjectName.add(mProjectData.get(a).getProject().getNama());
                        }
                        mSpProjek.setItems(mProjectName);

                        if (getIntent().getBooleanExtra("edit", false)) {
                            for (id.gis.collection.model.Project.Result data : mProjectData) {
                                if (data.getProject().getNama() != null && data.getProject().getNama().contains(getIntent().getStringExtra("project"))) {
                                    int realPosition = mProjectData.indexOf(data) + 1;
                                    idProject = data.getProject().getIdProject();
                                    mSpProjek.setSelectedIndex(realPosition);
                                    mPbloadSurvei.setVisibility(View.VISIBLE);
                                    Call<ResponseSurveyProject> callSurvey = RestClient.get().getSurveyProject(idProject);
                                    callSurvey.enqueue(new Callback<ResponseSurveyProject>() {
                                        @Override
                                        public void onResponse(Call<ResponseSurveyProject> call, Response<ResponseSurveyProject> response) {
                                            if (response.isSuccessful()) {
                                                mSurveyData = response.body().getResult();
                                                mSurveyName = new ArrayList<>();
                                                for (int a = 0; a < mSurveyData.size(); a++) {
                                                    mSurveyName.add(mSurveyData.get(a).getNmWilSurvei());
                                                }
                                                mSurveyName.add(0, "Pilih Wilayah Survei");
                                                mSpSurvei.setItems(mSurveyName);

                                                for (id.gis.collection.model.Survey.Project.Result dataWilSur : mSurveyData) {
                                                    if (dataWilSur.getNmWilSurvei() != null && dataWilSur.getNmWilSurvei().contains(IqbalUtils.capitalize(getIntent().getStringExtra("surveiWilayah")))) {
                                                        int realPosition = mSurveyData.indexOf(dataWilSur) + 1;
                                                        mSpSurvei.setSelectedIndex(realPosition);
                                                        idWilSurvey = dataWilSur.getId();
                                                        mPbloadSurvei.setVisibility(View.GONE);
                                                        break;
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseSurveyProject> call, Throwable t) {
                                            Toast.makeText(getApplicationContext(), "Gagal Pilih Projek (Edit)", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseProjectList> call, Throwable t) {
                    mPbloadProjek.setVisibility(View.GONE);
                    initViewModel();
                    Toast.makeText(getApplicationContext(), "Gagal Ambil Daftar Projek", Toast.LENGTH_SHORT).show();
                }
            });

            mSpProjek.setOnItemSelectedListener((view, position, id, item) -> {
                int realPosition = position - 1;

                if (position == 0) {
                    mSpSurvei.setSelectedIndex(0);
                    mSpSurvei.setClickable(false);

                } else {
                    mSpSurvei.setClickable(false);
                    mPbloadSurvei.setVisibility(View.VISIBLE);
                    mTilSurvei.setErrorEnabled(false);
                    mTilSurvei.setError(null);
                    if (mProjectData.size() > 0) {
                        mPbloadSurvei.setVisibility(View.VISIBLE);
                        idProject = String.valueOf(mProjectData.get(realPosition).getIdProject());

                        Call<ResponseSurveyProject> callSurvey = RestClient.get().getSurveyProject(idProject);
                        callSurvey.enqueue(new Callback<ResponseSurveyProject>() {
                            @Override
                            public void onResponse(Call<ResponseSurveyProject> call, Response<ResponseSurveyProject> response) {
                                if (response.isSuccessful()) {
                                    mSpSurvei.setClickable(true);
                                    mPbloadSurvei.setVisibility(View.GONE);
                                    mSurveyData = response.body().getResult();
                                    mSurveyName = new ArrayList<>();
                                    mSurveyName.add(0, "Pilih Wilayah Survei");
                                    for (int a = 0; a < mSurveyData.size(); a++) {
                                        mSurveyName.add(mSurveyData.get(a).getNmWilSurvei());
                                    }
                                    mSpSurvei.setItems(mSurveyName);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseSurveyProject> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "Gagal Ambil Daftar Wilayah Survei", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });


            //SURVEY
            mSpSurvei.setBackgroundColor(getResources().getColor(R.color.colorBackGrey));
            mSpSurvei.setOnItemSelectedListener((view, position, id, item) -> {
                int realPosition = position - 1;

                if (position != 0 && mSurveyData.size() > 0) {
                    id.gis.collection.model.Survey.Project.Result hasil = mSurveyData.get(realPosition);
                    idWilSurvey = String.valueOf(mSurveyData.get(realPosition).getId());
                    mTilSurvei.setErrorEnabled(false);
                    mTilSurvei.setError(null);

                    String desa = hasil.getDesa() != null ? IqbalUtils.capitalize(hasil.getDesa()) + ", " : "";
                    String kec = hasil.getKecamatan() != null ? IqbalUtils.capitalize(hasil.getKecamatan()) + ", " : "";
                    String kota = hasil.getKota() != null ? IqbalUtils.capitalize(hasil.getKota()) + ", " : "";
                    String prov = hasil.getProvinsi() != null ? IqbalUtils.capitalize(hasil.getProvinsi()) + ", Indonesia" : "";
                    mResultAddr1.setText(hasil.getAlamat());
                    mResultAddr2.setText(desa + kec + kota + prov);
                } else {
                    idWilSurvey = "0";
                }
            });

            //JENIS
            mJenisName = new ArrayList<>();
            if (mJenisName.size() > 0) {
                mJenisName.clear();
            }
            mJenisName.add(0, "Pilih Jenis Tempat");
            mSpJenis.setClickable(false);
            mSpJenis.setBackgroundColor(getResources().getColor(R.color.colorBackGrey));
            Call<ResponseJenisList> callJenis = RestClient.get().getJenisList();
            callJenis.enqueue(new Callback<ResponseJenisList>() {
                @Override
                public void onResponse(Call<ResponseJenisList> call, Response<ResponseJenisList> response) {
                    mPbloadJenis.setVisibility(View.GONE);
                    mSpJenis.setClickable(true);
                    if (response.isSuccessful()) {
                        mJenisData = response.body().getResult();
                        for (int a = 0; a < mJenisData.size(); a++) {
                            mJenisName.add(mJenisData.get(a).getNama());
                        }
                        mSpJenis.setItems(mJenisName);

                        if (getIntent().getBooleanExtra("edit", false)) {
                            for (id.gis.collection.model.Jenis.Result data : mJenisData) {
                                if (data.getNama() != null && data.getId().contains(getIntent().getStringExtra("jenis"))) {
                                    int realPosition = mJenisData.indexOf(data) + 1;
                                    mSpJenis.setSelectedIndex(realPosition);
                                    idJenis = data.getId();
                                    mPbloadObjek.setVisibility(View.VISIBLE);
                                    Call<ResponseObjekList> callObj = RestClient.get().getObjekList(idJenis);
                                    callObj.enqueue(new Callback<ResponseObjekList>() {
                                        @Override
                                        public void onResponse(Call<ResponseObjekList> callObj, Response<ResponseObjekList> responseObj) {
                                            if (response.isSuccessful()) {
                                                mObjekData = responseObj.body().getResult();
                                                mObjekName = new ArrayList<>();
                                                for (int a = 0; a < mObjekData.size(); a++) {
                                                    mObjekName.add(mObjekData.get(a).getNama());
                                                }

                                                mObjekName.add(0, "Pilih Objek Tempat");
                                                mSpObjek.setItems(mObjekName);

                                                for (id.gis.collection.model.Objek.Result dataObj : mObjekData) {
                                                    if (dataObj.getNama() != null && dataObj.getId().contains(getIntent().getStringExtra("objek"))) {
                                                        int real = mObjekData.indexOf(dataObj) + 1;
                                                        mSpObjek.setSelectedIndex(real);
                                                        idObjek = dataObj.getId();
                                                        mPbloadObjek.setVisibility(View.GONE);
                                                        break;
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseObjekList> call, Throwable t) {
                                            mPbloadObjek.setVisibility(View.GONE);
                                            Timber.e("gagal load spinner object caused by " + t.getMessage());
                                            Toast.makeText(getApplicationContext(), "Gagal Pilih Objek (Edit)", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseJenisList> call, Throwable t) {
                    mPbloadJenis.setVisibility(View.GONE);
                    initViewModel();
                    Toast.makeText(getApplicationContext(), "Gagal Ambil Daftar Jenis", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "gagal Ambil Jenis" + t.getMessage());
                }
            });


            mSpJenis.setOnItemSelectedListener((view, position, id, item) -> {
                int realPosition = position - 1;

                if (position == 0) {
                    mSpObjek.setSelectedIndex(0);
                    mSpObjek.setClickable(false);

                    idJenis = "0";
                    idObjek = "0";
                } else {
                    mSpObjek.setClickable(false);
                    mPbloadObjek.setVisibility(View.VISIBLE);
                    mTilJenis.setErrorEnabled(false);
                    mTilJenis.setError(null);
                    if (mJenisData.size() > 0) {
                        mPbloadObjek.setVisibility(View.VISIBLE);
                        idJenis = mJenisData.get(realPosition).getId();
                        if (idJenis.equals("5") || idJenis.equals("15")) {
                            mTilJB.setVisibility(View.VISIBLE);
                            mTilJL.setVisibility(View.VISIBLE);
                        } else {
                            mTilJB.setVisibility(View.GONE);
                            mTilJL.setVisibility(View.GONE);
                        }
                        Call<ResponseObjekList> call = RestClient.get().getObjekList(idJenis);
                        call.enqueue(new Callback<ResponseObjekList>() {
                            @Override
                            public void onResponse(Call<ResponseObjekList> call, Response<ResponseObjekList> response) {
                                mSpObjek.setClickable(true);
                                mPbloadObjek.setVisibility(View.GONE);
                                if (response.isSuccessful()) {
                                    mObjekData = response.body().getResult();
                                    mObjekName = new ArrayList<>();
                                    for (int a = 0; a < mObjekData.size(); a++) {
                                        mObjekName.add(mObjekData.get(a).getNama());
                                    }

                                    mObjekName.add(0, "Pilih Objek Tempat");
                                    mSpObjek.setItems(mObjekName);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseObjekList> call, Throwable t) {
                                mPbloadObjek.setVisibility(View.GONE);
                                Log.d(TAG, "gagal load spinner object caused by " + t.getMessage());
                                Toast.makeText(getApplicationContext(), "Gagal Ambil Daftar Objek", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

            mObjekName = new ArrayList<String>();
            mSpObjek.setBackgroundColor(getResources().getColor(R.color.colorBackGrey));
            mSpObjek.setOnItemSelectedListener((view, position, id, item) -> {
                int realPosition = position - 1;

                if (position != 0 && mObjekData.size() > 0) {
                    idObjek = String.valueOf(mObjekData.get(realPosition).getId());
                    mTilObjek.setErrorEnabled(false);
                    mTilObjek.setError(null);
                } else {
                    idObjek = "0";
                }
            });

            mSpJenisBangunan.setBackgroundColor(getResources().getColor(R.color.colorBackGrey));
            String[] jb = new String[]{"Pilih Jenis Bangunan", "Permanen", "Semi Permanen", "Non Permanen"};
            mSpJenisBangunan.setItems(jb);

            if (getIntent().getBooleanExtra("edit", false)) {
                String desa = getIntent().getStringExtra("desa") != null ? IqbalUtils.capitalize(getIntent().getStringExtra("desa")) + ", " : "";
                String kec = getIntent().getStringExtra("kecamatan") != null ? IqbalUtils.capitalize(getIntent().getStringExtra("kecamatan")) + ", " : "";
                String kota = getIntent().getStringExtra("kota") != null ? IqbalUtils.capitalize(getIntent().getStringExtra("kota")) + ", " : "";
                String prov = getIntent().getStringExtra("provinsi") != null ? IqbalUtils.capitalize(getIntent().getStringExtra("provinsi")) + ", Indonesia" : "";

                idSurvey = getIntent().getStringExtra("idSurvey");
                mEtName.setText(getIntent().getStringExtra("nama"));
                mEtAddress.setText(getIntent().getStringExtra("alamat"));
                mEtDesc.setText(getIntent().getStringExtra("deskripsi"));
                geom = getIntent().getStringExtra("geom");
                mResultAkurasi.setText("Akurasi 10 meter");
                mResultAddr1.setText(getIntent().getStringExtra("alamat"));
                mResultAddr2.setText(desa + kec + kota + prov);
                mResultGeom.setText(getIntent().getStringExtra("geom"));
                mEtLat.setText(getIntent().getStringExtra("lat"));
                mEtLng.setText(getIntent().getStringExtra("lon"));


                if (!getIntent().getStringExtra("jenis_bangunan").equals("")) {
                    mSpJenisBangunan.setSelectedIndex(Integer.parseInt(getIntent().getStringExtra("jenis_bangunan")));
                    mTilJL.setVisibility(View.VISIBLE);
                    mEtJmlLantai.setText(getIntent().getStringExtra("jml_lantai"));
                }
            }
            mPd.dismiss();
        }else{
            mViewModel = ViewModelProviders.of(this)
                    .get(AddPlaceViewModel.class);

            placeDatabase = Room.databaseBuilder(getApplicationContext(),
                    PlaceDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();

            mViewModel.initViewModel(placeDatabase.placeDao());

            if (getIntent().getBooleanExtra("edit", false)) {
                mResultGeom.setText(getIntent().getStringExtra("geom"));
                mResultAkurasi.setText("Akurasi 10 meter");
                mEtName.setText(getIntent().getStringExtra("nama"));
                mEtAddress.setText(getIntent().getStringExtra("alamat"));
                mEtDesc.setText(getIntent().getStringExtra("deskripsi"));
                geom = getIntent().getStringExtra("geom");
                mEtLat.setText(String.valueOf(getIntent().getFloatExtra("lat", (float) 0.0)));
                mEtLng.setText(String.valueOf(getIntent().getFloatExtra("lon", (float) 0.0)));
            }

            mPd.dismiss();
        }
    }

    private boolean isDataValid() {
        boolean valid = true;

        String name = mEtName.getText().toString();
        String description = mEtDesc.getText().toString();
        String address = mEtAddress.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            mTilName.setErrorEnabled(true);
            mTilName.setError("Minimal 3 Karakter");
            valid = false;
        } else {
            mTilName.setErrorEnabled(false);
            mTilName.setError(null);
        }

        if (description.isEmpty() || description.length() < 5) {
            mTilDesc.setErrorEnabled(true);
            mTilDesc.setError("Minimal 5 karakter");
            valid = false;
        } else {
            mTilDesc.setErrorEnabled(false);
            mTilDesc.setError(null);
        }

        if (address.isEmpty()) {
            mTilAddress.setErrorEnabled(true);
            mTilAddress.setError("Jangan Kosong");
            valid = false;
        } else {
            mTilAddress.setErrorEnabled(false);
            mTilAddress.setError(null);
        }

        if (IqbalUtils.readPreference(AddPlaceActivity.this, "online", "").equals("1")) {

            if (mSpSurvei.getSelectedIndex() == 0) {
                valid = false;
                mTilSurvei.setErrorEnabled(true);
                mTilSurvei.setError("Jenis Tempat Belum Dipilih");
            } else {
                mTilSurvei.setErrorEnabled(false);
                mTilSurvei.setError(null);
            }

            if (mSpJenis.getSelectedIndex() == 0) {
                valid = false;
                mTilJenis.setErrorEnabled(true);
                mTilJenis.setError("Jenis Tempat Belum Dipilih");
            } else {
                mTilJenis.setErrorEnabled(false);
                mTilJenis.setError(null);
            }

            if (mSpObjek.getSelectedIndex() == 0) {
                valid = false;
                mTilObjek.setErrorEnabled(true);
                mTilObjek.setError("Objek Tempat Belum Dipilih");
            } else {
                mTilObjek.setErrorEnabled(false);
                mTilObjek.setError(null);
            }

            if (mSpProjek.getSelectedIndex() == 0) {
                mTilProject.setErrorEnabled(true);
                mTilProject.setError("Project Belum Dipilih");
            } else {
                mTilProject.setErrorEnabled(false);
                mTilProject.setError(null);
            }
        }

        return valid;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    static class VideoCompressAsyncTask extends AsyncTask<String, String, String> {

        @SuppressLint("StaticFieldLeak")
        Context mContext;
        private ProgressDialog dialog;
        VideoCompressAsyncTask(Context context){
            mContext = context;
            dialog = new ProgressDialog(mContext);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Sedang mengompress video...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... paths) {
            String filePath = null;
            try {
                filePath = SiliCompressor.with(mContext).compressVideo(paths[0], paths[1]);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return  filePath;

        }


        @Override
        protected void onPostExecute(String compressedFilePath) {
            super.onPostExecute(compressedFilePath);
            if (dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }

    @OnClick(R.id.btn_submit)
    public void storeSurveyor() {
        if (!isDataValid()) {
            onErrorSurvey("Lengkapi Data");
            return;
        }

        mSpProjek.setEnabled(false);
        mEtName.setEnabled(false);
        mEtAddress.setEnabled(false);
        mEtLat.setEnabled(false);
        mEtLng.setEnabled(false);
        mEtDesc.setEnabled(false);
        mSpJenis.setEnabled(false);
        mEtJmlLantai.setEnabled(false);
        mSpObjek.setEnabled(false);
        mSpJenisBangunan.setEnabled(false);

        mBtnSubmit.blockTouch();
        mBtnSubmit.morphToProgress(getResources().getColor(R.color.colorBackGrey), 100, 100,100, 10, getResources().getColor(R.color.blue), getResources().getColor(R.color.colorBackGrey));

        if (IqbalUtils.readPreference(AddPlaceActivity.this, "online", "").equals("1")) {
            OneFragment frag1 = (OneFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.viewpager+":0");
            images = frag1.getListImage();
            TwoFragment frag2 = (TwoFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.viewpager+":1");
            videos = frag2.getListVideo();
            ThreeFragment frag3 = (ThreeFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.viewpager+":2");
            audioPath = frag3.getAudio();
            FourFragment frag4 = (FourFragment)getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.viewpager+":3");
            files = frag4.getListFiles();

            List<MultipartBody.Part> surveyImagesParts = new ArrayList<>();
            for (int i = 0; i < images.size(); i++) {
                File imgFile = null;
                if (URLUtil.isHttpUrl(images.get(i))){
                    try {
                        URL url = new URL(images.get(i));
                        imgFile = new File(Environment.getExternalStorageDirectory() + "/"+ URLUtil.guessFileName(String.valueOf(url), null, null));
                        org.apache.commons.io.FileUtils.copyURLToFile(url, imgFile, 120000, 120000);
                        imgFile.deleteOnExit();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    imgFile = new File(images.get(i));
                    imgFile.deleteOnExit();
                }

                RequestBody imagesBody = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);
                surveyImagesParts.add(i, MultipartBody.Part.createFormData("images[]", imgFile.getName(), imagesBody));
            }

            List<MultipartBody.Part> surveyVideosParts = new ArrayList<>();
            for (int i = 0; i < videos.size(); i++) {
                File vidFile = null;
                if (URLUtil.isHttpUrl(videos.get(i))){
                    try {
                        URL url = new URL(videos.get(i));
                        vidFile = new File(Environment.getExternalStorageDirectory() + "/"+ URLUtil.guessFileName(String.valueOf(url), null, null));
                        org.apache.commons.io.FileUtils.copyURLToFile(url, vidFile, 120000, 120000);
                        vidFile.deleteOnExit();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    vidFile = new File(videos.get(i));
                    vidFile.deleteOnExit();
                }
                RequestBody videosBody = RequestBody.create(MediaType.parse("video/*"), vidFile);
                surveyVideosParts.add(i, MultipartBody.Part.createFormData("videos[]", vidFile.getName(), videosBody));
            }

            List<MultipartBody.Part> surveyAudiosParts = new ArrayList<>();
            if (audioPath != null) {
                File recFile = new File(audioPath);
                recFile.deleteOnExit();
                RequestBody audiosBody = RequestBody.create(MediaType.parse("multipart/form-data"), recFile);
                surveyAudiosParts.add(0, MultipartBody.Part.createFormData("records[]", recFile.getName(), audiosBody));
            }

            List<MultipartBody.Part> surveyFilesParts = new ArrayList<>();
            for (int i = 0; i < files.size(); i++) {
                File pdfFile = null;
                if (URLUtil.isHttpUrl(files.get(i))){
                    try {
                        URL url = new URL(files.get(i));
                        pdfFile = new File(Environment.getExternalStorageDirectory() + "/"+ URLUtil.guessFileName(String.valueOf(url), null, null));
                        org.apache.commons.io.FileUtils.copyURLToFile(url, pdfFile, 120000, 120000);
                        pdfFile.deleteOnExit();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    pdfFile = new File(files.get(i));
                    pdfFile.deleteOnExit();
                }
                RequestBody filesBody = RequestBody.create(MediaType.parse("text/*"), pdfFile);
                surveyFilesParts.add(i, MultipartBody.Part.createFormData("files[]", pdfFile.getName(), filesBody));
            }

            RequestBody api_key = RequestBody.create(MultipartBody.FORM, AppConstant.API_KEY);
            RequestBody idProjectRB = RequestBody.create(MultipartBody.FORM, idProject);
            RequestBody idWilSurveyRB = RequestBody.create(MultipartBody.FORM, idWilSurvey);
            RequestBody idUserRB = RequestBody.create(MultipartBody.FORM, idUser);
            RequestBody idJenisRB = RequestBody.create(MultipartBody.FORM, idJenis);
            RequestBody idObjectRB = RequestBody.create(MultipartBody.FORM, idObjek);
            RequestBody jnsRB = RequestBody.create(MultipartBody.FORM, "1");
            RequestBody nameRB = RequestBody.create(MultipartBody.FORM, mEtName.getText().toString());
            RequestBody alamatRB = RequestBody.create(MultipartBody.FORM, mEtAddress.getText().toString());
            RequestBody jbRB = RequestBody.create(MultipartBody.FORM, String.valueOf(mSpJenisBangunan.getSelectedIndex()));
            RequestBody jlRB = RequestBody.create(MultipartBody.FORM, mEtJmlLantai.getText().toString());
            RequestBody descRB = RequestBody.create(MultipartBody.FORM, mEtDesc.getText().toString());
            RequestBody latRB = RequestBody.create(MultipartBody.FORM, mEtLat.getText().toString());
            RequestBody lngRB = RequestBody.create(MultipartBody.FORM, mEtLng.getText().toString());
            RequestBody statusRB = RequestBody.create(MultipartBody.FORM, "1");
            RequestBody geomRB = RequestBody.create(MultipartBody.FORM, geom);

            retrofit2.Call<ResponseSignUp> call = null;
            if (getIntent().getBooleanExtra("edit", false)) {
                RequestBody idSurveyRB = RequestBody.create(MultipartBody.FORM, idSurvey);
                call = RestClient.get().updateSurvey(api_key, idSurveyRB, idProjectRB, idWilSurveyRB, idUserRB, idJenisRB, idObjectRB, jnsRB, nameRB, descRB, alamatRB, jbRB, jlRB, latRB, lngRB, statusRB, geomRB, surveyImagesParts, surveyVideosParts, surveyAudiosParts, surveyFilesParts);
            } else {
                call = RestClient.get().surveyStore(api_key, idProjectRB, idWilSurveyRB, idUserRB, idJenisRB, idObjectRB, jnsRB, nameRB, descRB, alamatRB, jbRB, jlRB, latRB, lngRB, statusRB, geomRB, surveyImagesParts, surveyVideosParts, surveyAudiosParts, surveyFilesParts);
            }

            call.enqueue(new Callback<ResponseSignUp>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseSignUp> call, Response<ResponseSignUp> response) {
                    if (response.isSuccessful()) {
                        if(getIntent().getBooleanExtra("edit", false)){
                            openToast("Survei telah diperbaharui");
                        }else{
                            openToast("Terimakasih telah mengisi survey");
                        }
                        Intent intent = new Intent(AddPlaceActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        onErrorSurvey("Lengkapi Data");
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseSignUp> call, Throwable t) {
                    onErrorSurvey("Gagal Menyimpan");
                    Log.e(TAG, String.valueOf(t.getCause()));
                }
            });
        }else{
            if (getIntent().getBooleanExtra("edit", false)) {
                float lat = Float.parseFloat(String.valueOf(getIntent().getFloatExtra("lon", (float) 0.0)));
                float lng = Float.parseFloat(String.valueOf(getIntent().getFloatExtra("lon", (float) 0.0)));
                placeEntity = new PlaceEntity(getIntent().getIntExtra("id", 1), getIntent().getStringExtra("nama"), getIntent().getStringExtra("alamat"), getIntent().getStringExtra("deskripsi"), lat, lng, getIntent().getStringExtra("geom"), IqbalUtils.getCurrentDate());
                mViewModel.deletePlace(placeEntity);
            }

            float lat = Float.parseFloat(mEtLat.getText().toString());
            float lng = Float.parseFloat(mEtLng.getText().toString());
            mViewModel.addPlace(mEtName.getText().toString().trim(), mEtDesc.getText().toString().trim(), mEtAddress.getText().toString().trim(), lat, lng, geom, IqbalUtils.getCurrentDate())
            .observe(this, isSuccess->{
                if (isSuccess) {
                    Toast.makeText(this, "Succussfully saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddPlaceActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void onErrorSurvey(String message) {
        morphToSquare(mBtnSubmit, 0);
        mBtnSubmit.unblockTouch();
        openToast(message);
        mEtName.setEnabled(true);
        mEtAddress.setEnabled(true);
        mEtLat.setEnabled(true);
        mEtLng.setEnabled(true);
        mEtDesc.setEnabled(true);
    }

    private void openToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
