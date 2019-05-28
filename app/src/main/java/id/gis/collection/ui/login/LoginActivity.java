package id.gis.collection.ui.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.IndeterminateProgressButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.gis.collection.BuildConfig;
import id.gis.collection.R;
import id.gis.collection.api.RestClient;
import id.gis.collection.model.Login.ResponseLoginUser;
import id.gis.collection.model.Login.Result;
import id.gis.collection.model.SignUp.ResponseSignUp;
import id.gis.collection.ui.common.BaseActivity;
import id.gis.collection.ui.intro.IntroActivity;
import id.gis.collection.ui.main.MainActivity;
import id.gis.collection.ui.signup.SignupActivity;
import id.gis.collection.ui.splashscreen.SplashScreenActivity;
import id.gis.collection.utils.AppConstant;
import id.gis.collection.utils.IqbalUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.et_username)
    EditText mEtUsername;

    @BindView(R.id.et_password)
    EditText mEtPassword;

    @BindView(R.id.tvForgot)
    TextView mTvForgot;

    @BindView(R.id.tv_version)
    TextView mTvVersion;

    @BindView(R.id.btn_login)
    IndeterminateProgressButton mBtnLogin;

    @BindView(R.id.til_email)
    TextInputLayout mTilEmail;

    @BindView(R.id.til_password)
    TextInputLayout mTilPassword;
    private final String PREFKEY_FIRST_START = "id.gis.collection.PREFKEY_FIRST_START";
    private final int RC_SPLASH = 15;
    Result mDataUser;
    @Override
    public int getResLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean firstStart = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(PREFKEY_FIRST_START, true);

        if (firstStart) {
            Intent in = new Intent(LoginActivity.this, IntroActivity.class);
            startActivityForResult(in, RC_SPLASH);
        }

        morphToSquare(mBtnLogin, 0);
        mTvVersion.setText("versi : " + BuildConfig.VERSION_NAME);
    }

    public void morphToSquare(final IndeterminateProgressButton btnMorph, int duration){
        MorphingButton.Params circle = MorphingButton.Params.create()
            .duration(duration)
            .cornerRadius(70)
            .height(FrameLayout.LayoutParams.WRAP_CONTENT)
            .width(FrameLayout.LayoutParams.FILL_PARENT)
            .color(getResources().getColor(R.color.colorAccent))
            .text("MASUK")
            .colorPressed(getResources().getColor(R.color.accentPressed));
        btnMorph.morph(circle);
    }


    @OnClick(R.id.tvForgot)
    public void forgotPassword(){
        LinearLayout layout = new LinearLayout(LoginActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText etEmail = new EditText(this);
        etEmail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        etEmail.setHint("Alamat Email");
        final EditText etUsername = new EditText(this);
        etUsername.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        etUsername.setHint("Username");
        etUsername.setHintTextColor(getResources().getColor(R.color.mb_gray));
        etEmail.setHintTextColor(getResources().getColor(R.color.mb_gray));
        etEmail.setTextColor(getResources().getColor(R.color.colorAccent));
        etUsername.setTextColor(getResources().getColor(R.color.colorAccent));
        layout.addView(etEmail);
        layout.addView(etUsername);

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Lupa Password");
        builder.setView(layout);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "Kirim Password", "Proses sedang dilakukan", true);
                String email = etEmail.getText().toString();
                String username = etUsername.getText().toString();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Call<ResponseSignUp> forgot = RestClient.get().forgotPassword(AppConstant.API_KEY, username, email);
                        forgot.enqueue(new Callback<ResponseSignUp>() {
                            @Override
                            public void onResponse(Call<ResponseSignUp> call, Response<ResponseSignUp> response) {
                                if (response.isSuccessful()){
                                    dialog.dismiss();
                                    progressDialog.dismiss();
                                    android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this, R.style.Theme_AppCompat_Light_Dialog);
                                    alertDialog.setTitle("Forgot Password");
                                    alertDialog.setMessage("Password Anda telah dipulihkan, silahkan cek email Anda");
                                    alertDialog.show();
                                }else{
                                    dialog.dismiss();
                                    android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this, R.style.Theme_AppCompat_Light_Dialog);
                                    alertDialog.setTitle("Forgot Password");
                                    alertDialog.setMessage("Email atau Password tidak sama");
                                    alertDialog.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseSignUp> call, Throwable t) {
                                Log.e("LOGIN ACTIVITY", t.getMessage());
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Maaf Gagal Ganti Password", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, 3000);
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s) && !IqbalUtils.isValidEmail(s)){
                    ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && IqbalUtils.isValidEmail(s)){
                    ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                }else{
                    ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        });

    }

    @OnClick(R.id.btn_login)
    public void login() {
        IqbalUtils.hideKeyboard(this);

        if (!validate()) {
            onLoginFailed("Fill Form");
            return;
        }

        mBtnLogin.blockTouch();
        mBtnLogin.morphToProgress(getResources().getColor(R.color.colorBackGrey), 100, 100,100, 10, getResources().getColor(R.color.blue), getResources().getColor(R.color.colorBackGrey));

        String username = mEtUsername.getText().toString();
        String password = mEtPassword.getText().toString();

        mEtUsername.setEnabled(false);
        mEtPassword.setEnabled(false);

        Call<ResponseLoginUser> call = RestClient.get().loginUser(AppConstant.API_KEY,username, password);
        call.enqueue(new Callback<ResponseLoginUser>() {
            @Override
            public void onResponse(Call<ResponseLoginUser> call, Response<ResponseLoginUser> response) {
                if (response.isSuccessful()){
                    if(response.body().getResult().getLevel().equals("2")){
                        mDataUser = response.body().getResult();
                        saveToPreference(mDataUser, mEtPassword.getText().toString());
                        onLoginSuccess();
                    }else{
                        onLoginFailed("Surveyor Only");
                    }
                }else{
                    onLoginFailed("Username or Password  Wrong");
                }
            }

            @Override
            public void onFailure(Call<ResponseLoginUser> call, Throwable t) {
                onLoginFailed("Unestablished Connection");
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void saveToPreference(Result mDataUser, String realPass) {
        IqbalUtils.saveToPreference(this,"id", mDataUser.getId());
        IqbalUtils.saveToPreference(this,"nama", mDataUser.getNama());
        IqbalUtils.saveToPreference(this,"avatar", mDataUser.getAvatar());
        IqbalUtils.saveToPreference(this,"real_password", realPass);
        IqbalUtils.saveToPreference(this,"username", mDataUser.getUsername());
        IqbalUtils.saveToPreference(this,"email", mDataUser.getEmail());
        IqbalUtils.saveToPreference(this,"gender", mDataUser.getJnsKlm());
        IqbalUtils.saveToPreference(this,"hp", mDataUser.getNoHp());
        IqbalUtils.saveToPreference(this,"tgl", mDataUser.getTglLahir());
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

    public boolean validate() {
        boolean valid = true;

        String email = mEtUsername.getText().toString();
        String password = mEtPassword.getText().toString();

        if (email.isEmpty()) {
            mTilEmail.setErrorEnabled(true);
            mTilEmail.setError("Masukkan Username");
            valid = false;
        } else {
            mTilEmail.setErrorEnabled(false);
            mTilEmail.setError(null);
        }

        if (password.isEmpty()) {
            mTilPassword.setErrorEnabled(true);
            mTilPassword.setError("Isi Password");
            valid = false;
        } else {
            mTilPassword.setErrorEnabled(false);
            mTilPassword.setError(null);
        }

        return valid;
    }

    public void onLoginSuccess() {
        mBtnLogin.setEnabled(true);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onLoginFailed(String message) {
        morphToSquare(mBtnLogin, 0);
        mBtnLogin.unblockTouch();
        openToast(message);
        mEtUsername.setEnabled(true);
        mEtPassword.setEnabled(true);
        mBtnLogin.setEnabled(true);
    }

    private void openToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
