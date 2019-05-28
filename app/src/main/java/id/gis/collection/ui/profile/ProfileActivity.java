package id.gis.collection.ui.profile;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.IndeterminateProgressButton;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.ui.PickImageActivity;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;

import java.io.File;
import java.text.DateFormat;
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
import id.gis.collection.BuildConfig;
import id.gis.collection.R;
import id.gis.collection.api.RestClient;
import id.gis.collection.model.Login.ResponseLoginUser;
import id.gis.collection.model.Province.ResponseProvinceList;
import id.gis.collection.model.Province.Result;
import id.gis.collection.model.SignUp.ResponseSignUp;
import id.gis.collection.ui.addplace.AddPlaceActivity;
import id.gis.collection.ui.addplace.GridAdapter;
import id.gis.collection.ui.addplace.ImageAdapter;
import id.gis.collection.ui.common.BaseActivity;
import id.gis.collection.utils.AppConstant;
import id.gis.collection.utils.IqbalUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by dell on 13/08/18.
 */

public class ProfileActivity extends BaseActivity {

    private static final String TAG = "PROFILE";
    @BindView(R.id.circleView)
    ImageView mImgAvatar;

    @BindView(R.id.etNama)
    EditText mEtNama;

    @BindView(R.id.etEmail)
    EditText mEtEmail;

    @BindView(R.id.labelNp)
    TextView mLNp;

    @BindView(R.id.labelAp)
    TextView mLAp;

    @BindView(R.id.etPhone)
    EditText mEtPhone;

    @BindView(R.id.etTgl)
    EditText mEtTgl;

    @BindView(R.id.etAddress)
    EditText mEtAddress;

    @BindView(R.id.etPassword)
    EditText mEtPassword;

    @BindView(R.id.spGender)
    MaterialSpinner mSpGender;

    @BindView(R.id.toolbarProfile)
    Toolbar toolbarProfile;

    @BindView(R.id.btnUpdateProfile)
    IndeterminateProgressButton mBtnProfile;

    private DialogPlus dialogPlus;
    private ImagePicker imagePicker;
    private CameraImagePicker imgCameraPicker;
    String imgPath;
    id.gis.collection.model.Login.Result mDataUser;

    @Override
    public int getResLayout() { return R.layout.activity_profile; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
        initModel();
        morphToSquare(mBtnProfile, 0);
    }


    private void morphToSquare(final IndeterminateProgressButton btnMorph, int duration){
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        setSupportActionBar(toolbarProfile);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");
    }

    private void initModel() {
        String avatar = IqbalUtils.readPreference(this, "avatar", "");
        String nama = IqbalUtils.readPreference(this, "nama", "");
        String email = IqbalUtils.readPreference(this, "email", "");
        String alamat = IqbalUtils.readPreference(this, "alamat", "");
        String kota = IqbalUtils.readPreference(this, "kota", "");
        String prov = IqbalUtils.readPreference(this, "provinsi", "");
        String gender = IqbalUtils.readPreference(this, "gender", "");
        String hp = IqbalUtils.readPreference(this, "hp", "");
        String tgl = IqbalUtils.readPreference(this, "tgl", "");
        String realPassword = IqbalUtils.readPreference(this, "real_password", "");

        Date sD = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
             sD = sdf.parse(tgl);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat tdf = new SimpleDateFormat("dd-MM-yyyy");
        mEtTgl.setText(tdf.format(sD));
        mEtNama.setText(nama);
        mEtEmail.setText(email);
        mEtPhone.setText(hp);
        mEtAddress.setText(alamat);
        mEtPassword.setText(realPassword);
        mSpGender.setItems("Pilih Jenis Kelamin","Laki-Laki", "Perempuan");
        mLAp.setText(IqbalUtils.capitalize(kota )+ ", " + IqbalUtils.capitalize(prov));
        mLNp.setText(nama);
        mSpGender.setBackgroundColor(getResources().getColor(R.color.colorBackGrey));
        mSpGender.setSelectedIndex(Integer.parseInt(gender));

        if (!avatar.equals("default.png")){
            Glide.with(getApplicationContext()).load(avatar).into(mImgAvatar);
        }

        if(gender.equals("1")){
            mSpGender.setSelectedIndex(1);
        }else{
            mSpGender.setSelectedIndex(2);
        }
    }

    @OnClick(R.id.btnUpdateProfile)
    public void updateProfile(){
        mBtnProfile.blockTouch();
        mBtnProfile.morphToProgress(getResources().getColor(R.color.colorBackGrey), 100, 100,100, 10, getResources().getColor(R.color.blue), getResources().getColor(R.color.colorBackGrey));
        int idGender = mSpGender.getSelectedIndex();
        RequestBody api_key = RequestBody.create(MultipartBody.FORM, AppConstant.API_KEY);
        RequestBody id_user = RequestBody.create(MultipartBody.FORM, IqbalUtils.readPreference(ProfileActivity.this, "id", "0"));
        RequestBody nama = RequestBody.create(MultipartBody.FORM, mEtNama.getText().toString());
        RequestBody no_hp = RequestBody.create(MultipartBody.FORM, mEtPhone.getText().toString());
        RequestBody email = RequestBody.create(MultipartBody.FORM, mEtEmail.getText().toString());
        RequestBody alamat = RequestBody.create(MultipartBody.FORM, mEtAddress.getText().toString());
        RequestBody gender = RequestBody.create(MultipartBody.FORM, String.valueOf(idGender));
        RequestBody tgl_lahir = RequestBody.create(MultipartBody.FORM, mEtTgl.getText().toString());
        RequestBody password = RequestBody.create(MultipartBody.FORM, mEtPassword.getText().toString());


        Call<ResponseSignUp> call = null;

        if (imgPath == null){
            call = RestClient.get().profileUpdate(api_key,id_user, nama, alamat, email, no_hp, gender, tgl_lahir, password);
        }else{
            File imgFile = new File (imgPath);
            RequestBody imgBody = RequestBody.create(MediaType.parse("image/*"), imgFile);
            MultipartBody.Part avatar =  MultipartBody.Part.createFormData("avatar", imgFile.getName(), imgBody);
            call = RestClient.get().profileUpdate(api_key,id_user, nama, alamat, email, no_hp, gender, tgl_lahir, password, avatar);

        }
        call.enqueue(new Callback<ResponseSignUp>() {
            @Override
            public void onResponse(Call<ResponseSignUp> call, Response<ResponseSignUp> response) {
                if (response.isSuccessful()){
                    updateProfileOnline();
                    morphToSquare(mBtnProfile, 0);
                    mBtnProfile.unblockTouch();
                    Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseSignUp> call, Throwable t) {
                morphToSquare(mBtnProfile, 0);
                mBtnProfile.unblockTouch();
                Toast.makeText(ProfileActivity.this, "Gagal Update Profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfileOnline() {
        String id = IqbalUtils.readPreference(ProfileActivity.this, "id", "");
        Call<ResponseLoginUser> call = RestClient.get().getUserDetail(id);
        call.enqueue(new Callback<ResponseLoginUser>() {
            @Override
            public void onResponse(Call<ResponseLoginUser> call, Response<ResponseLoginUser> response) {
                if(response.isSuccessful()){
                    mDataUser = response.body().getResult();
                    saveToPreference(mDataUser);
                }
            }

            @Override
            public void onFailure(Call<ResponseLoginUser> call, Throwable t) {

            }
        });
    }

    private void saveToPreference(id.gis.collection.model.Login.Result mDataUser) {
        IqbalUtils.saveToPreference(this,"id", mDataUser.getId());
        IqbalUtils.saveToPreference(this,"username", mDataUser.getUsername());
        IqbalUtils.saveToPreference(this,"nama", mDataUser.getNama());
        IqbalUtils.saveToPreference(this,"email", mDataUser.getEmail());
        IqbalUtils.saveToPreference(this,"gender", mDataUser.getJnsKlm());
        IqbalUtils.saveToPreference(this,"hp", mDataUser.getNoHp());
        IqbalUtils.saveToPreference(this,"tgl", mDataUser.getTglLahir());
        IqbalUtils.saveToPreference(this,"avatar", mDataUser.getAvatar());
        IqbalUtils.saveToPreference(this,"alamat", mDataUser.getAlamat());
        IqbalUtils.saveToPreference(this,"IdNegara", mDataUser.getNegara().getIdNegara());
        IqbalUtils.saveToPreference(this,"negara", mDataUser.getNegara().getNama());
        IqbalUtils.saveToPreference(this,"IdProvinsi", mDataUser.getProvinsi().getIdProvinsi());
        IqbalUtils.saveToPreference(this,"provinsi", mDataUser.getProvinsi().getNama());
        IqbalUtils.saveToPreference(this,"IdKota", mDataUser.getKota().getIdKota());
        IqbalUtils.saveToPreference(this,"kota", mDataUser.getKota().getNama());
        IqbalUtils.saveToPreference(this,"IdKecamatan", mDataUser.getKecamatan().getIdKecamatan());
        IqbalUtils.saveToPreference(this,"kecamatan", mDataUser.getKecamatan().getNama());
        IqbalUtils.saveToPreference(this,"IdDesa", mDataUser.getDesa().getIdDesa());
        IqbalUtils.saveToPreference(this,"desa", mDataUser.getDesa().getNama());
        IqbalUtils.saveToPreference(this,"status", mDataUser.getStatus());
        IqbalUtils.saveToPreference(this,"level", mDataUser.getLevel());
        IqbalUtils.saveToPreference(this, "isUserLogged", AppConstant.USER_LOGGED);
    }
    
    @OnClick(R.id.etTgl)
    public void tgl(){
        Date sD = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String tgl = IqbalUtils.readPreference(this, "tgl", "");

        try {
            sD = sdf1.parse(tgl);
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

            String format = "dd-MM-yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
            mEtTgl.setText(sdf.format(calendar.getTime()));
        };

        new DatePickerDialog(ProfileActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.circleView)
    public void selectImage(){
        String [] name = {"Camera", "Photo"};
        int [] icon = {R.drawable.ic_camera, R.drawable.ic_folder};
        Context context = ProfileActivity.this;
        dialogPlus = DialogPlus.newDialog(this)
            .setExpanded(false)
            .setAdapter(new GridAdapter(this, name, icon))
            .setContentHolder(new GridHolder(2))
            .setGravity(Gravity.BOTTOM)
            .setCancelable(true)
            .setOnItemClickListener((dialog, item, view, position) -> {
                if (position == 0){
                    imgCameraPicker = new CameraImagePicker(ProfileActivity.this);
                    imgCameraPicker.setImagePickerCallback(new ImagePickerCallback() {
                        @Override
                        public void onImagesChosen(List<ChosenImage> list) {
                            imgPath = list.get(0).getOriginalPath();
                            Glide.with(getApplicationContext()).load(list.get(0).getOriginalPath()).into(mImgAvatar);
                        }

                        @Override
                        public void onError(String s) {

                        }
                    });
                    imgCameraPicker.setDebugglable(true);
                    imgCameraPicker.pickImage();
                }else{
                    imagePicker = new ImagePicker(ProfileActivity.this);
                    imagePicker.setImagePickerCallback(new PickImageActivity() {
                        @Override
                        public void onImagesChosen(List<ChosenImage> list) {
                            imgPath = list.get(0).getOriginalPath();
                            Glide.with(context).load(list.get(0).getOriginalPath()).into(mImgAvatar);
                        }

                        @Override
                        public void onError(String s) {

                        }
                    });
                    imagePicker.pickImage();
                }
            })
            .create();
        dialogPlus.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Picker.PICK_IMAGE_DEVICE && resultCode == RESULT_OK) {
            dialogPlus.dismiss();
            imagePicker.submit(data);
        }

        if(requestCode == Picker.PICK_IMAGE_CAMERA && resultCode == RESULT_OK){
            dialogPlus.dismiss();
            imgCameraPicker.submit(data);
        }
    }
}
