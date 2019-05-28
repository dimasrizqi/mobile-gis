package id.gis.collection.ui.place;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.IndeterminateProgressButton;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.robertlevonyan.views.expandable.Expandable;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
import id.gis.collection.model.Survey.Project.ResponseSurveyProject;
import id.gis.collection.model.Survey.ResponseSurveyList;
import id.gis.collection.model.Survey.Result;
import id.gis.collection.ui.addplace.AddPlaceActivity;
import id.gis.collection.ui.common.BaseActivity;
import id.gis.collection.ui.profile.ProfileActivity;
import id.gis.collection.utils.IqbalUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class PlaceActivity extends BaseActivity {

    private static final String TAG = "PLACE ACTIVITY";
    private final String DATABASE_NAME = "place_db";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.rv_place)
    RecyclerView mRvPlace;

    @BindView(R.id.pbLoad)
    ProgressBar mPbLoad;

    @BindView(R.id.labelNoSurvey)
    TextView mLabelNoSurvey;

    @BindView(R.id.btnReset)
    IndeterminateProgressButton mBtnReset;

    @BindView(R.id.btnTerapkan)
    IndeterminateProgressButton mBtnTerapkan;

    @BindView(R.id.srPlace)
    SwipeRefreshLayout srPlace;

    @BindView(R.id.exPeriode)
    Expandable mExPeriode;

    @BindView(R.id.exKategori)
    Expandable mExKategori;

    @BindView(R.id.msPeriodeSpinner)
    MaterialSpinner mMsPeriodeSpinner;

    @BindView(R.id.msProject)
    MaterialSpinner mMsProject;

    @BindView(R.id.pbLoadProjek)
    ProgressBar mPbloadProjek;

    @BindView(R.id.msJenis)
    MaterialSpinner mMsJenis;

    @BindView(R.id.pbLoadJenis)
    ProgressBar mPbloadJenis;

    @BindView(R.id.msObjek)
    MaterialSpinner mMsObjek;

    @BindView(R.id.pbLoadObjek)
    ProgressBar mPbloadObjek;

    @BindView(R.id.drawerLayoutPlace)
    DrawerLayout mDrawerLayoutPlace;

    @BindView(R.id.suplPlaces)
    SlidingUpPanelLayout mSuplPlaces;

    @BindView(R.id.etDateFrom)
    EditText mEtDateFrom;

    @BindView(R.id.etDateEnd)
    EditText mEtDateEnd;

    private PlaceDatabase placeDatabase;
    private PlaceViewModel mViewModel;
    private LinearLayoutManager manager;

    private List<Result> mSurveyData;
    List<id.gis.collection.model.Project.Result> mProjectData;
    List<id.gis.collection.model.Jenis.Result> mJenisData;
    List<id.gis.collection.model.Objek.Result> mObjekData;

    ArrayList<String> mProjectName;
    ArrayList<String> mJenisName;
    ArrayList<String> mObjekName;

    private PlaceRvAdapterOnline mAdapter;
    private PlaceRvAdapter mAdapterOff;
    protected Handler handler;
    private int currentPage;
    Toolbar mSearchToolbar;
    String idUser, querySearch, idJenis, idObjek, idProject, tglAwal, tglAkhir;
    Menu search_menu;
    MenuItem item_search;

    @Override
    public int getResLayout() {
        return R.layout.activity_place;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        initToolbar();
        initViewModel();
        initPlaces();
    }

    @OnClick(R.id.btnReset)
    public void resetFilter(){
        mEtDateFrom.setText("");
        mEtDateEnd.setText("");
        mMsPeriodeSpinner.setSelectedIndex(0);
        mMsProject.setSelectedIndex(0);
        mMsJenis.setSelectedIndex(0);
        mMsObjek.setSelectedIndex(0);
        idJenis = "";
        idObjek = "";
        idProject = "";
    }

    @OnClick(R.id.btnTerapkan)
    public void terapkanFilter(){
        if (mMsJenis.getSelectedIndex() != 0 && mMsObjek.getSelectedIndex() == 0){
            Toast.makeText(this, "Pilih Objek", Toast.LENGTH_SHORT).show();
        }else{
            initPlaces();
            mSuplPlaces.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }
    }

    public void addDate(EditText mEtDate){
        Date sD = null;
        try {
            String tgl;
            if (!mEtDate.getText().toString().isEmpty()){
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                tgl = mEtDate.getText().toString();
                sD = sdf1.parse(tgl);
            }else{
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                tgl = IqbalUtils.getCurrentDate();
                sD = sdf1.parse(tgl);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        Date finalSD = sD;
        calendar.setTime(finalSD);
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            mEtDate.setText(sdf.format(calendar.getTime()));
            if (mEtDate.getResources().getResourceEntryName(mEtDate.getId()).equals("etDateFrom")){
                tglAwal = sdf2.format(calendar.getTime()).replace("-", "");
            }else{
                tglAkhir = sdf2.format(calendar.getTime()).replace("-", "");
            }
        };

        new DatePickerDialog(PlaceActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void initExpandable() {
        mEtDateEnd.setOnClickListener(v -> addDate(mEtDateEnd));
        mEtDateFrom.setOnClickListener(v -> addDate(mEtDateFrom));

        mBtnReset.setBackgroundColor(getResources().getColor(R.color.colorBackGrey));
        mBtnReset.setTextColor(getResources().getColor(R.color.colorPrimary));
        mBtnTerapkan.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mExPeriode.setAnimateExpand(true);
        mExKategori.setAnimateExpand(true);

        //MATERIAL SPINNER
        mMsPeriodeSpinner.setBackgroundColor(getResources().getColor(R.color.colorBackGrey));
        mMsPeriodeSpinner.setDropdownHeight(200);
        mMsPeriodeSpinner.setItems("Semua", "90 Hari", "30 Hari", "Seminggu Lalu");

        mMsProject.setBackgroundColor(getResources().getColor(R.color.colorBackGrey));
        mMsProject.setDropdownMaxHeight(300);
        mProjectName = new ArrayList<>();
        mProjectName.add(0, "Pilih Projek");
        Call<ResponseProjectList> callProject = RestClient.get().getProjectList(idUser);
        callProject.enqueue(new Callback<ResponseProjectList>() {
            @Override
            public void onResponse(Call<ResponseProjectList> call, Response<ResponseProjectList> response) {
                mPbloadProjek.setVisibility(View.GONE);
                mMsProject.setClickable(true);
                if (response.isSuccessful()) {
                    mProjectData = response.body().getResult();
                    for (int a = 0; a < mProjectData.size(); a++) {
                        mProjectName.add(mProjectData.get(a).getProject().getNama());
                    }
                    mMsProject.setItems(mProjectName);
                }
            }

            @Override
            public void onFailure(Call<ResponseProjectList> call, Throwable t) {
                Toast.makeText(PlaceActivity.this, "Gagal Ambil Daftar Survei", Toast.LENGTH_SHORT).show();
            }
        });

        mMsProject.setOnItemSelectedListener((view, position, id, item) -> {
            int realPosition = position - 1;

            if (position != 0) {
                if (mProjectData.size() > 0) {
                    idProject = String.valueOf(mProjectData.get(realPosition).getIdProject());
                }
            }
        });

        mMsPeriodeSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position != 0){
                    mEtDateEnd.setEnabled(false);
                    mEtDateFrom.setEnabled(false);
                    Calendar awal = Calendar.getInstance();
                    Calendar akhir = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    if (position == 1){
                        awal.add(Calendar.DAY_OF_YEAR, -90);

                        tglAwal = sdf.format(awal.getTime()).replace("-", "");
                        tglAkhir = sdf.format(akhir.getTime()).replace("-", "");
                    }else if(position == 2){
                        awal.add(Calendar.DAY_OF_YEAR, -30);

                        tglAwal = sdf.format(awal.getTime()).replace("-", "");
                        tglAkhir = sdf.format(akhir.getTime()).replace("-", "");
                    }else{
                        awal.add(Calendar.DAY_OF_YEAR, -7);

                        tglAwal = sdf.format(awal.getTime()).replace("-", "");
                        tglAkhir = sdf.format(akhir.getTime()).replace("-", "");
                    }
                }else{
                    mEtDateEnd.setEnabled(true);
                    mEtDateFrom.setEnabled(true);
                }
            }
        });

        mMsJenis.setBackgroundColor(getResources().getColor(R.color.colorBackGrey));
        mMsJenis.setDropdownMaxHeight(300);
        mJenisName = new ArrayList<>();
        mJenisName.add(0, "Pilih Jenis Tempat");
        Call<ResponseJenisList> callJenis = RestClient.get().getJenisList();
        callJenis.enqueue(new Callback<ResponseJenisList>() {
            @Override
            public void onResponse(Call<ResponseJenisList> call, Response<ResponseJenisList> response) {
                mPbloadJenis.setVisibility(View.GONE);
                mMsJenis.setClickable(true);
                if (response.isSuccessful()) {
                    mJenisData = response.body().getResult();
                    for (int a = 0; a < mJenisData.size(); a++) {
                        mJenisName.add(mJenisData.get(a).getNama());
                    }
                    mMsJenis.setItems(mJenisName);
                }
            }

            @Override
            public void onFailure(Call<ResponseJenisList> call, Throwable t) {
                mPbloadJenis.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Gagal Ambil Daftar Jenis", Toast.LENGTH_SHORT).show();
            }
        });

        mMsObjek.setBackgroundColor(getResources().getColor(R.color.colorBackGrey));
        mMsObjek.setDropdownMaxHeight(300);
        mMsJenis.setOnItemSelectedListener((view, position, id, item) -> {
            int realPosition = position - 1;

            if (position == 0) {
                mMsObjek.setSelectedIndex(0);
                mMsObjek.setClickable(false);

                idJenis = "0";
                idObjek = "0";
            } else {
                mMsObjek.setClickable(false);
                mPbloadObjek.setVisibility(View.VISIBLE);
                if (mJenisData.size() > 0) {
                    mPbloadObjek.setVisibility(View.VISIBLE);
                    idJenis = mJenisData.get(realPosition).getId();

                    Call<ResponseObjekList> call = RestClient.get().getObjekList(idJenis);
                    call.enqueue(new Callback<ResponseObjekList>() {
                        @Override
                        public void onResponse(Call<ResponseObjekList> call, Response<ResponseObjekList> response) {
                            mMsObjek.setClickable(true);
                            mPbloadObjek.setVisibility(View.GONE);
                            if (response.isSuccessful()) {
                                mObjekData = response.body().getResult();
                                mObjekName = new ArrayList<>();
                                for (int a = 0; a < mObjekData.size(); a++) {
                                    mObjekName.add(mObjekData.get(a).getNama());
                                }

                                mObjekName.add(0, "Pilih Objek Tempat");
                                mMsObjek.setItems(mObjekName);
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
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void circleReveal(int viewID, int posFromRight, boolean containsOverflow, final boolean isShow)
    {
        final View myView = findViewById(viewID);

        int width=myView.getWidth();

        if(posFromRight>0)
            width-=(posFromRight*getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material))-(getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)/ 2);
        if(containsOverflow)
            width-=getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);

        int cx=width;
        int cy=myView.getHeight()/2;

        Animator anim;
        if(isShow)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0,(float)width);
        else
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float)width, 0);

        anim.setDuration((long)220);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
            if(!isShow)
            {
                super.onAnimationEnd(animation);
                myView.setVisibility(View.INVISIBLE);
            }
            }
        });

        // make the view visible and start the animation
        if(isShow)
            myView.setVisibility(View.VISIBLE);

        // start the animation
        anim.start();


    }

    public void setSearchtollbar()
    {
        mSearchToolbar = (Toolbar) findViewById(R.id.searchToolbar);
        if (mSearchToolbar != null) {
            mSearchToolbar.inflateMenu(R.menu.menu_search);
            search_menu = mSearchToolbar.getMenu();

            mSearchToolbar.setNavigationOnClickListener(v -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    circleReveal(R.id.searchToolbar,1,true,false);
                else
                    mSearchToolbar.setVisibility(View.GONE);
            });

            item_search = search_menu.findItem(R.id.action_filter_search);

            MenuItemCompat.setOnActionExpandListener(item_search, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    // Do something when collapsed
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleReveal(R.id.searchToolbar,1,true,false);
                    }
                    else
                        mSearchToolbar.setVisibility(View.GONE);
                    return true;
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    // Do something when expanded
                    return true;
                }
            });

            initSearchView();
        } else{
            Log.d("toolbar", "setSearchtollbar: NULL");
        }
    }

    public void initSearchView()
    {
        final android.support.v7.widget.SearchView searchView =
                (android.support.v7.widget.SearchView) search_menu.findItem(R.id.action_filter_search).getActionView();

        // Enable/Disable Submit button in the keyboard

        searchView.setSubmitButtonEnabled(false);

        // Change search close button image

        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setImageResource(R.drawable.ic_close);


        // set hint and the text colors

        EditText txtSearch = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        txtSearch.setHint("Cari..");
        txtSearch.setHintTextColor(Color.DKGRAY);
        txtSearch.setTextColor(getResources().getColor(R.color.colorAccent));


        // set the cursor

        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.search_cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                callSearch(newText);
                return true;
            }

            public void callSearch(String query) {
                mPbLoad.setVisibility(View.VISIBLE);
                mLabelNoSurvey.setVisibility(View.GONE);
                querySearch = query;
                if (IqbalUtils.readPreference(PlaceActivity.this, "online", "").equals("1")){
                    if (mSurveyData != null && mSurveyData.size() > 0){
                        mSurveyData.clear();
                    }

                    Call<ResponseSurveyList> callSearch = RestClient.get().getSurveyList(idUser, 0, 5, query.toLowerCase(), tglAwal, tglAkhir, idObjek, idJenis, idProject);
                    callSearch.enqueue(new Callback<ResponseSurveyList>() {
                        @Override
                        public void onResponse(Call<ResponseSurveyList> call, Response<ResponseSurveyList> response) {
                            if (response.isSuccessful()){
                                mSurveyData = response.body().getResult();
                                mPbLoad.setVisibility(View.GONE);
                                if (response.body().getResult().size() > 0){
                                    mAdapter = new PlaceRvAdapterOnline(mSurveyData, PlaceActivity.this, mRvPlace);
                                    mRvPlace.setAdapter(mAdapter);
                                    srPlace.setRefreshing(false);
                                }else{
                                    mLabelNoSurvey.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseSurveyList> call, Throwable t) {

                        }
                    });
                }else{
                    mAdapterOff = new PlaceRvAdapter((placeEntity, v, mList, position) -> {
                        PopupMenu popup = new PopupMenu(PlaceActivity.this, v);
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.item_action, popup.getMenu());
                        popup.setOnMenuItemClickListener(item -> {
                            switch (item.getItemId()) {
                                case R.id.hapusItem :
                                    new AlertDialog.Builder(PlaceActivity.this).setTitle("Konfirmasi")
                                            .setMessage("Yakin menghapus survey " + IqbalUtils.capitalize(placeEntity.getName()) + " ?")
                                            .setPositiveButton("Iya", (dialog, which) -> {
                                                mViewModel.deletePlace(placeEntity);
                                                mList.remove(position);
                                                mAdapterOff.notifyItemRemoved(position);
                                                mAdapterOff.notifyItemRangeChanged(position, mList.size());
                                            })
                                            .setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss())
                                            .create()
                                            .show();
                                    break;
                                case R.id.editItem :
                                    Intent i = new Intent(PlaceActivity.this, AddPlaceActivity.class);
                                    i.putExtra("edit", true);
                                    i.putExtra("id", placeEntity.getId());
                                    i.putExtra("nama", placeEntity.getName());
                                    i.putExtra("alamat", placeEntity.getAddress());
                                    i.putExtra("deskripsi", placeEntity.getDescription());
                                    i.putExtra("geom", placeEntity.getLat() +""+ placeEntity.getLng());
                                    i.putExtra("lat", placeEntity.getLat());
                                    i.putExtra("lon", placeEntity.getLng());
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    getApplicationContext().startActivity(i);
                                    break;
                            }
                            return false;
                        });
                        popup.show();
                    });

                    mRvPlace.setAdapter(mAdapterOff);
                    List<PlaceEntity> listSearch = new ArrayList<>();
                    mViewModel.getPlaces().observe(PlaceActivity.this, list -> {
                        if (list != null && list.size() > 0){
                            for (int i = 0; i < list.size(); i++){
                                if (list.get(i).getName().toLowerCase().contains(query)){
                                    listSearch.add(list.get(i));
                                }
                            }
                            mAdapterOff.setPlaceList(listSearch);
                            mAdapterOff.notifyDataSetChanged();
                        }else{
                            mLabelNoSurvey.setVisibility(View.VISIBLE);
                            mPbLoad.setVisibility(View.GONE);
                        }
                    });
                }
            }

        });

    }

    private void initPlaces() {
        mSuplPlaces.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (previousState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)){
                    mSuplPlaces.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }
            }
        });

        mLabelNoSurvey.setVisibility(View.GONE);
        idUser =  IqbalUtils.readPreference(this, "id", "");
        mPbLoad.setVisibility(View.VISIBLE);
        mRvPlace.setHasFixedSize(true);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvPlace.setLayoutManager(manager);

        if (IqbalUtils.readPreference(PlaceActivity.this, "online", "").equals("1")) {
            Call<ResponseSurveyList> call = RestClient.get().getSurveyList(idUser, 0, 5, querySearch, tglAwal, tglAkhir, idObjek, idJenis, idProject);
            call.enqueue(new Callback<ResponseSurveyList>() {
                @Override
                public void onResponse(Call<ResponseSurveyList> call, Response<ResponseSurveyList> response) {
                    if (response.isSuccessful() && response.body().getResult().size() > 0) {
                        mSurveyData = response.body().getResult();
                        mAdapter = new PlaceRvAdapterOnline(mSurveyData, PlaceActivity.this, mRvPlace);
                        mRvPlace.setAdapter(mAdapter);
                        mPbLoad.setVisibility(View.GONE);
                        srPlace.setRefreshing(false);

                        mAdapter.setOnLoadMoreListener(() -> {
                            currentPage++;
                            mSurveyData.add(null);
                            mAdapter.notifyItemInserted(mSurveyData.size() - 1);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mSurveyData.size() > 0){
                                        mSurveyData.remove(mSurveyData.size() - 1);
                                        mAdapter.notifyItemRemoved(mSurveyData.size());
                                        Call<ResponseSurveyList> call1 = RestClient.get().getSurveyList(idUser, currentPage, 5, querySearch, tglAwal, tglAkhir, idObjek, idJenis, idProject);
                                        call1.enqueue(new Callback<ResponseSurveyList>() {
                                            @Override
                                            public void onResponse(Call<ResponseSurveyList> call1, Response<ResponseSurveyList> response1) {
                                                if (response1.isSuccessful() && response1.body().getResult().size() > 0) {
                                                    mSurveyData.addAll(response1.body().getResult());
                                                    mAdapter.setLoaded();
                                                    mAdapter.notifyDataSetChanged();
                                                }else{
                                                    Toast.makeText(PlaceActivity.this, "Data Lengkap", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseSurveyList> call1, Throwable t) {
                                                Toast.makeText(PlaceActivity.this, "Gagal Ambil Halaman Berikutnya dari Daftar Survei", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }, 2000);
                        });
                    } else {
                        mPbLoad.setVisibility(View.GONE);
                        mLabelNoSurvey.setVisibility(View.VISIBLE);
                        srPlace.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseSurveyList> call, Throwable t) {
                    mPbLoad.setVisibility(View.GONE);
                    Toast.makeText(PlaceActivity.this, "Gagal Ambil Daftar Survei", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            mAdapterOff = new PlaceRvAdapter((placeEntity, v, mList, position) -> {
                PopupMenu popup = new PopupMenu(PlaceActivity.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.item_action, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.hapusItem :
                            new AlertDialog.Builder(PlaceActivity.this).setTitle("Konfirmasi")
                                .setMessage("Yakin menghapus survey " + IqbalUtils.capitalize(placeEntity.getName()) + " ?")
                                .setPositiveButton("Iya", (dialog, which) -> {
                                    mViewModel.deletePlace(placeEntity);
                                    mList.remove(position);
                                    mAdapterOff.notifyItemRemoved(position);
                                    mAdapterOff.notifyItemRangeChanged(position, mList.size());
                                })
                                .setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss())
                                .create()
                                .show();
                            break;
                        case R.id.editItem :
                            Intent i = new Intent(PlaceActivity.this, AddPlaceActivity.class);
                            i.putExtra("edit", true);
                            i.putExtra("id", placeEntity.getId());
                            i.putExtra("nama", placeEntity.getName());
                            i.putExtra("alamat", placeEntity.getAddress());
                            i.putExtra("deskripsi", placeEntity.getDescription());
                            i.putExtra("geom", placeEntity.getLat() +""+ placeEntity.getLng());
                            i.putExtra("lat", placeEntity.getLat());
                            i.putExtra("lon", placeEntity.getLng());
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(i);
                            break;
                    }
                    return false;
                });
                popup.show();
            });

            mRvPlace.setAdapter(mAdapterOff);
            mViewModel.getPlaces().observe(this, list -> {
                if (list != null && list.size() > 0){
                    mAdapterOff.setPlaceList(list);
                    mAdapterOff.notifyDataSetChanged();
                }else{
                    mLabelNoSurvey.setVisibility(View.VISIBLE);
                    mPbLoad.setVisibility(View.GONE);
                }
            });
            srPlace.setRefreshing(false);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void initToolbar() {
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Daftar Survey");
        setSearchtollbar();
        srPlace.setColorSchemeColors(R.color.colorAccent);
        srPlace.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 0;
                initPlaces();
            }
        });
        handler = new Handler();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search :
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    circleReveal(R.id.searchToolbar,1, true, true);
                }else{
                    mSearchToolbar.setVisibility(View.VISIBLE);
                }
                item_search.expandActionView();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_filter:
                mSuplPlaces.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                initExpandable();
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);

        placeDatabase = Room.databaseBuilder(getApplicationContext(),
                PlaceDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

        mViewModel.initViewModel(placeDatabase.placeDao());
    }
}
