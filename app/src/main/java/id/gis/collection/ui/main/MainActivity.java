package id.gis.collection.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.IndeterminateProgressButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import id.gis.collection.BuildConfig;
import id.gis.collection.R;
import id.gis.collection.api.RestClient;
import id.gis.collection.data.db.PlaceDatabase;
import id.gis.collection.data.entity.PlaceEntity;
import id.gis.collection.model.SignUp.ResponseSignUp;
import id.gis.collection.model.Survey.ResponseSurveyList;
import id.gis.collection.model.Survey.Result;
import id.gis.collection.ui.addplace.AddPlaceActivity;
import id.gis.collection.ui.addplace.GridAdapter;
import id.gis.collection.ui.common.BaseActivity;
import id.gis.collection.ui.login.LoginActivity;
import id.gis.collection.ui.place.PlaceActivity;
import id.gis.collection.ui.profile.ProfileActivity;
import id.gis.collection.utils.AppConstant;
import id.gis.collection.utils.IqbalUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static java.lang.Double.parseDouble;

public class MainActivity extends BaseActivity implements OnMapReadyCallback {

    private final String DATABASE_NAME = "place_db";
    private final String TAG = MainActivity.class.getSimpleName();
    private final long UPDATE_INTERVAL = 60000;
    private final long FASTEST_INTERVAL = 5000;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.nvView)
    NavigationView mNavView;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.add_new_location)
    IndeterminateProgressButton mAddNewPlace;

    @BindView(R.id.vpPlace)
    RecyclerView mVpPlace;

    @BindView(R.id.suplSurveys)
    SlidingUpPanelLayout mSuplSurveys;

    @BindView(R.id.pbLoad)
    ProgressBar mPbLoad;

    @BindView(R.id.labelNoSurvey)
    TextView mLabelNoSurvey;

    @BindView(R.id.labelSurvey)
    TextView mLabelSurvey;

    private PlaceDatabase placeDatabase;
    private MainViewModel mViewModel;
    private SupportMapFragment mMapFrament;
    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private ActionBarDrawerToggle drawerToggle;
    private MainVpAdapterOnline mVpAdapter;
    private MainVpAdapter mVpAdapterOff;
    private List<Result> mSurveyData;

    SwitchCompat aSwitch;
    TextView msgCount;
    @Override
    public int getResLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Mobile Geo Collector");
        mMapFrament = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFrament.getMapAsync(this);
        setupDrawerContent();
        drawerToggle = setupDrawerToggle();
        mDrawerLayout.addDrawerListener(drawerToggle);
        morphToSquare(mAddNewPlace, 0);
        getData();

        String nama = IqbalUtils.readPreference(getApplicationContext(), "nama", "");
        String kota = IqbalUtils.readPreference(this, "kota", "");
        String prov = IqbalUtils.readPreference(this, "provinsi", "");
        String avatar = IqbalUtils.readPreference(this, "avatar", "");

        View header = mNavView.getHeaderView(0);
        TextView navName = header.findViewById(R.id.profile_name);
        TextView navAddress = header.findViewById(R.id.profile_address);
        ImageView mNavProfile = header.findViewById(R.id.circleView);
        if (!avatar.equals("default.png")){
            Glide.with(getApplicationContext()).load(avatar).into(mNavProfile);
        }

        navName.setText(nama);
        navAddress.setText(IqbalUtils.capitalize(kota + ", "+prov));
    }

    private void morphToSquare(final IndeterminateProgressButton btnMorph, int duration){
        ViewGroup.LayoutParams params = btnMorph.getLayoutParams();
        MorphingButton.Params circle = MorphingButton.Params.create()
            .duration(duration)
            .cornerRadius(70)
            .height(ViewGroup.LayoutParams.WRAP_CONTENT)
            .width(500)
            .text("Tambah Survey")
            .color(getResources().getColor(R.color.colorAccent))
            .colorPressed(getResources().getColor(R.color.accentPressed));
        btnMorph.morph(circle);
    }

    @OnClick(R.id.fab_map_types)
    public void dialogMapsType() {
        String [] name = {"Normal", "Terrain", "Hybrid", "Satellite"};

        int [] icon = {R.drawable.map_normal, R.drawable.map_terrain, R.drawable.map_hybrid, R.drawable.map_satellite};
        DialogPlus dialogPlus = DialogPlus.newDialog(MainActivity.this)
            .setExpanded(false)
            .setAdapter(new GridAdapter(MainActivity.this, name, icon))
            .setGravity(Gravity.BOTTOM)
            .setContentHolder(new GridHolder(4))
            .setOnItemClickListener((DialogPlus dialog, Object item, View view, int position) -> {
                if(position == 0){
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }else if(position == 1){
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                }else if(position == 2){
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }else if(position == 3){
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
                dialog.dismiss();
            })
            .setCancelable(true)
            .create();
        dialogPlus.show();
    }


    @OnClick(R.id.add_new_location)
    public void addNewPlace(){
        if (isGPSEnabled()){
            Intent intent = new Intent(this, AddPlaceActivity.class);
            intent.putExtra("edit", false);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }else{
            showSettingsAlert();
        }
    }

    @OnClick(R.id.fab_my_location)
    public void onMyLocationClicked() {
        startLocationUpdates();
    }

    @OnClick(R.id.fab_sinkron)
    public void onSyncronize(){
        if(IqbalUtils.isNetworkAvailable(MainActivity.this) && IqbalUtils.readPreference(MainActivity.this, "online", "").equals("1")){
            new android.app.AlertDialog.Builder(this).setTitle("Konfirmasi Sinkronisasi")
                .setMessage("Sinkronisasi membutuhkan beberapa waktu, ingin diteruskan ?")
                .setPositiveButton("Iya", (dialog, which) -> {
                    dialog.dismiss();
                    syncAction();
                })
                .setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
        }else{
            Toast.makeText(this, "Anda sedang Offline", Toast.LENGTH_LONG).show();
        }
    }

    private void syncAction(){
        ProgressDialog progressDialog = ProgressDialog.show(this, "Sinkronisasi", "Pengecekan Data Offline", true);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        placeDatabase = Room.databaseBuilder(getApplicationContext(),
                PlaceDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mViewModel.initViewModel(placeDatabase.placeDao());
                mVpAdapterOff = new MainVpAdapter(mSuplSurveys, mMap);
                mVpPlace.setAdapter(mVpAdapterOff);
                mViewModel.getPlaces().observe(MainActivity.this, list -> {
                    if (list != null && list.size() > 0) {
                        progressDialog.setMessage("Sinkronisasi sedang berlangsung...");
                        for (int i = 0; i < list.size(); i++) {
                            PlaceEntity pe = list.get(i);
                            storeSyncAction(pe, progressDialog);
                            if (i == list.size() - 1){
                                progressDialog.dismiss();
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_Light_Dialog);
                                alertDialog.setTitle("Sinkronisasi");
                                alertDialog.setMessage("Sinkronisasi data sudah selesai");
                                alertDialog.show();
                            }
                        }
                    }else{
                        progressDialog.dismiss();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_Light_Dialog);
                        alertDialog.setTitle("Data Offline");
                        alertDialog.setMessage("Maaf Anda tidak memiliki data offline");
                        alertDialog.show();
                    }
                });
            }
        }, 3000);


    }

    private void storeSyncAction(PlaceEntity pe, ProgressDialog progressDialog){
        Call<ResponseSignUp> call = RestClient.get().surveyStoreOffline(AppConstant.API_KEY, IqbalUtils.readPreference(this, "id", "") ,pe.getName(), pe.getDescription(), pe.getAddress(), String.valueOf(pe.getLat()), String.valueOf(pe.getLng()), pe.getGeom());
        call.enqueue(new Callback<ResponseSignUp>() {
            @Override
            public void onResponse(Call<ResponseSignUp> call, Response<ResponseSignUp> response) {
                if (response.isSuccessful()){
                    mViewModel.deletePlace(pe);
                }
            }

            @Override
            public void onFailure(Call<ResponseSignUp> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Gagal Sinkronisasi", Toast.LENGTH_LONG).show();
            }
        });
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent() {
        mNavView.getMenu().findItem(R.id.version).setTitle("versi : "+ BuildConfig.VERSION_NAME);
        mNavView.setNavigationItemSelectedListener(
            menuItem -> {
                selectDrawerItem(menuItem);
                return true;
            });

        msgCount = (TextView) MenuItemCompat.getActionView(mNavView.getMenu().findItem(R.id.nav_chat));
        msgCount.setGravity(Gravity.CENTER_VERTICAL);
        msgCount.setTypeface(null, Typeface.BOLD);
        msgCount.setTextColor(getResources().getColor(R.color.accent2));
        msgCount.setText("30");
    }

    private void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                Intent intent1 = new Intent(this, ProfileActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return;
            case R.id.nav_view:
                Intent intent2 = new Intent(this, PlaceActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return;
            case R.id.nav_logout:
                IqbalUtils.clearSharedPreferences(getApplicationContext());
                Intent intent3 = new Intent(getApplicationContext(), LoginActivity.class);
                intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent3);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return;

        }
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawers();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        MapsInitializer.initialize(getApplicationContext());
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setOnMyLocationButtonClickListener(() -> {
            startLocationUpdates();
            return false;
        });
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Gson gson = new Gson();
                MarkerInfo infoMarker = gson.fromJson(marker.getSnippet(), MarkerInfo.class);
                View v = getLayoutInflater().inflate(R.layout.echo_info_window, null);
                TextView tvName = v.findViewById(R.id.nameSurvey);

                tvName.setText(IqbalUtils.capitalize(infoMarker.getTitle()));

                return v;
            }
        });

        checkLocationPermission();
    }

    public static class MarkerInfo {
        private String title;
        private String geom;
        public MarkerInfo() { }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGeom() {
            return geom;
        }

        public void setGeom(String geom) {
            this.geom = geom;
        }

        // getters and setters omitted because I'm not writing this in
        // an IDE. But assume that they are there
    }

    @SuppressLint("MissingPermission")
    private void checkLocationPermission() {
        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WAKE_LOCK, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            .withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {
                    getMyLocation();
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                }
            }).check();
    }


    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog);
        alertDialog.setTitle("GPS Anda Mati");
        alertDialog.setMessage("GPS tidak nyala, silahkan nyalakan GPS Anda di Settings");
        alertDialog.setPositiveButton("Settings", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.show();
    }

    @OnClick(R.id.labelSurvey)
    public void listSurvey(){
        Intent listSurvey = new Intent(MainActivity.this, PlaceActivity.class);
        startActivity(listSurvey);
    }

    @SuppressWarnings({"MissingPermission"})
    void getMyLocation() {
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);
        locationClient.getLastLocation()
            .addOnSuccessListener(location -> {
                if (location != null) {
                    onLocationChanged(location);
                }
            })
            .addOnFailureListener(e -> {
                Timber.d("Error trying to get last GPS location");
                e.printStackTrace();
            });
    }

    public boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCurrentLocation != null) {
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            mMap.animateCamera(cameraUpdate);
        }
        startLocationUpdates();
        mNavView.setCheckedItem(0);
    }

    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {
        if (isGPSEnabled()) {
            startTrackerService();
            mLocationRequest = new LocationRequest();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(mLocationRequest);
            LocationSettingsRequest locationSettingsRequest = builder.build();

            SettingsClient settingsClient = LocationServices.getSettingsClient(this);
            settingsClient.checkLocationSettings(locationSettingsRequest);

            getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
                }
            },
            Looper.myLooper());
        } else {
            showSettingsAlert();
        }
    }

    private void startTrackerService() {
        startService(new Intent(this, TrackingService.class));
    }

    public void onLocationChanged(Location location) {
        if (location == null) {
            return;
        }

        mCurrentLocation = location;

        Gson gson = new Gson();
        MarkerInfo markerInfo = new MarkerInfo();
        markerInfo.setTitle("Posisi Sekarang");
        markerInfo.setGeom(location.getLatitude() + ""+location.getLongitude());

        String mInfoCurr = gson.toJson(markerInfo);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17);
        mMap.clear();
        mMap.animateCamera(cameraUpdate);
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_me))
                .snippet(mInfoCurr));

        getData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mSuplSurveys != null &&
                (mSuplSurveys.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mSuplSurveys.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mSuplSurveys.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    private void getData() {
        mPbLoad.setVisibility(View.VISIBLE);
        mLabelSurvey.setVisibility(View.VISIBLE);
        mLabelNoSurvey.setVisibility(View.GONE);
        mVpPlace.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mVpPlace.setHasFixedSize(true);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.divider));
        mVpPlace.addItemDecoration(itemDecoration);

        mSuplSurveys.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if (mSuplSurveys.getAnchorPoint() == 1.0) {
                    mSuplSurveys.setAnchorPoint(0.5f);
                    mSuplSurveys.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });

        mSuplSurveys.setFadeOnClickListener(view -> mSuplSurveys.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED));
        if (IqbalUtils.readPreference(MainActivity.this, "online", "").equals("1")) {
            Log.e("TAIKUCING", IqbalUtils.readPreference(MainActivity.this, "online", ""));
            String username = IqbalUtils.readPreference(this, "id", "");
            Call<ResponseSurveyList> call = RestClient.get().getSurveyList(username, 0, 5, "", "", "", "", "","");
            call.enqueue(new Callback<ResponseSurveyList>() {
                @Override
                public void onResponse(Call<ResponseSurveyList> call, Response<ResponseSurveyList> response) {
                    if (response.isSuccessful()) {
                        mSurveyData = response.body().getResult();
                        if (mSurveyData.size() > 0){
                            mVpAdapter = new MainVpAdapterOnline(mSurveyData, getApplicationContext(), mMap, mSuplSurveys);
                            mVpPlace.setAdapter(mVpAdapter);
                            mPbLoad.setVisibility(View.GONE);
                            mVpAdapter.notifyDataSetChanged();
                            for (int i = 0; i < response.body().getResult().size(); i++) {
                                MarkerInfo markerInfo = new MarkerInfo();
                                markerInfo.setTitle(response.body().getResult().get(i).getNamaSurvei());
                                markerInfo.setGeom(String.valueOf(response.body().getResult().get(i).getGeom()));
                                Gson gson = new Gson();
                                String markerInfoString = gson.toJson(markerInfo);
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(parseDouble(response.body().getResult().get(i).getLatitude()), parseDouble(response.body().getResult().get(i).getLongitude())))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_place))
                                        .snippet(markerInfoString));
                            }
                        }else{
                            mLabelSurvey.setVisibility(View.GONE);
                            mSuplSurveys.setTouchEnabled(false);
                            mLabelNoSurvey.setVisibility(View.VISIBLE);
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseSurveyList> call, Throwable t) {
                    mPbLoad.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Gagal Ambil Daftar 5 Survei Terbaru", Toast.LENGTH_SHORT).show();
                    getData();
                }
            });
        }else{
            mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

            placeDatabase = Room.databaseBuilder(getApplicationContext(),
                PlaceDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

            mViewModel.initViewModel(placeDatabase.placeDao());
            mVpAdapterOff = new MainVpAdapter(mSuplSurveys, mMap);
            mVpPlace.setAdapter(mVpAdapterOff);
            mViewModel.getPlaces().observe(this, list -> {
                if (list.size() > 0){
                    mVpAdapterOff.setPlaceList(list);
                    mVpAdapterOff.notifyDataSetChanged();
                    for (int i = 0; i < list.size(); i++) {
                        MarkerInfo markerInfo = new MarkerInfo();
                        markerInfo.setTitle(list.get(i).getName());
                        markerInfo.setGeom(String.valueOf(list.get(i).getGeom()));
                        Gson gson = new Gson();
                        String markerInfoString = gson.toJson(markerInfo);

                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(list.get(i).getLat(), list.get(i).getLng()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_place))
                                .snippet(markerInfoString));
                    }
                }else{
                    mLabelSurvey.setVisibility(View.GONE);
                    mLabelNoSurvey.setVisibility(View.VISIBLE);
                    mSuplSurveys.setTouchEnabled(false);
                }
                mPbLoad.setVisibility(View.GONE);
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.offline_online, menu);
        aSwitch = (SwitchCompat) menu.findItem(R.id.switchId).getActionView().findViewById(R.id.toggleMode);
        if (IqbalUtils.isNetworkAvailable(MainActivity.this) && !IqbalUtils.readPreference(MainActivity.this, "online", "").equals("0")){
            aSwitch.setChecked(true);
        }
        if (IqbalUtils.readPreference(MainActivity.this, "online", "").equals("1")){
            aSwitch.setChecked(true);
        }
        aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                IqbalUtils.saveToPreference(MainActivity.this, "online", "1");
                getData();
                mSuplSurveys.setTouchEnabled(true);
            }else{
                IqbalUtils.saveToPreference(MainActivity.this, "online", "0");
                Snackbar snackbar = Snackbar.make(getCurrentFocus(), "Anda Sedang Offline", Snackbar.LENGTH_LONG);
                snackbar.show();
                View snack = snackbar.getView();
                snackbar.setAction("BATALKAN", v -> aSwitch.setChecked(true));
                getData();
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}
