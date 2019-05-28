package id.gis.collection.ui.signup;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.dd.morphingbutton.MorphingButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import id.gis.collection.R;
import id.gis.collection.api.RestClient;
import id.gis.collection.model.SignUp.ResponseSignUp;
import id.gis.collection.ui.common.BaseActivity;
import id.gis.collection.ui.login.LoginActivity;
import id.gis.collection.utils.AppConstant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class SignupActivity extends BaseActivity {

    private final String TAG = SignupActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.et_name)
    EditText mEtName;

    @BindView(R.id.til_name)
    TextInputLayout mTilName;

    @BindView(R.id.et_username)
    EditText mEtUsername;

    @BindView(R.id.til_address)
    TextInputLayout mTilAddress;

    @BindView(R.id.et_email)
    EditText mEtEmail;

    @BindView(R.id.til_email)
    TextInputLayout mTilEmail;

    @BindView(R.id.et_phone)
    EditText mEtPhone;

    @BindView(R.id.til_phone)
    TextInputLayout mTilPhone;

    @BindView(R.id.til_level)
    TextInputLayout mTilLevel;

    @BindView(R.id.et_password)
    EditText mEtPassword;

    @BindView(R.id.til_password)
    TextInputLayout mTilPassword;

    @BindView(R.id.et_re_password)
    EditText mEtRePassword;

    @BindView(R.id.til_re_password)
    TextInputLayout mTilRePassword;

    @BindView(R.id.btn_signup)
    MorphingButton mBtnSignup;

    @BindView(R.id.tv_login)
    TextView mTvLogin;

    @BindView(R.id.spLevel)
    Spinner mSpLevel;

    String idLevel;

    @Override
    public int getResLayout() {
        return R.layout.activity_signup;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Signup");
        initLoginText();
    }

    private void initLoginText() {
        String text = getResources().getString(R.string.signup_text_login);

        int indexStart = 18;
        int indexEnd = text.length();

        SpannableStringBuilder linkTermsConds = new SpannableStringBuilder(text);
        linkTermsConds.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorAccent)), indexStart, indexEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        linkTermsConds.setSpan(new StyleSpan(Typeface.BOLD), indexStart, indexEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mTvLogin.setMovementMethod(LinkMovementMethod.getInstance());
        linkTermsConds.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }, indexStart, indexEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mTvLogin.setText(linkTermsConds);

        //SPinner
        String[] items = new String[]{"Choose Level","Admin", "Surveyor"};
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(SignupActivity.this, R.layout.spinner_item, items);
        mSpLevel.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @OnItemSelected(R.id.spLevel)
    public void setLevel(Spinner spinner, int position){
        int realPosition = position ;
        idLevel = String.valueOf(realPosition);
    }

    @OnClick(R.id.btn_signup)
    public void signup() {

        Timber.d("Signup");

        if (!validate()) {
            onSignupFailed("Fill Form");
            return;
        }

        String name = mEtName.getText().toString();
        String username = mEtUsername.getText().toString();
        String email = mEtEmail.getText().toString();
        String mobile = mEtPhone.getText().toString();
        String password = mEtPassword.getText().toString();

        mEtUsername.setEnabled(false);
        mEtName.setEnabled(false);
        mEtEmail.setEnabled(false);
        mEtPhone.setEnabled(false);
        mEtPassword.setEnabled(false);
        mEtRePassword.setEnabled(false);
        mSpLevel.setEnabled(false);

        // TODO: Implement your own signup logic here.

        Call<ResponseSignUp> call = RestClient.get().signUpUser(AppConstant.API_KEY, username, name, email, mobile, password, idLevel);
        call.enqueue(new Callback<ResponseSignUp>() {
            @Override
            public void onResponse(Call<ResponseSignUp> call, Response<ResponseSignUp> response) {
                if (response.isSuccessful()){
                    onSignupSuccess();
                }else{
                    onSignupFailed("Username/Email/Phone Number has Taken");
                }
            }

            @Override
            public void onFailure(Call<ResponseSignUp> call, Throwable t) {
                onSignupFailed("Connection is not established");
            }
        });

    }

    public boolean validate() {
        boolean valid = true;

        String name = mEtName.getText().toString();
        String username = mEtUsername.getText().toString();
        String email = mEtEmail.getText().toString();
        String mobile = mEtPhone.getText().toString();
        String password = mEtPassword.getText().toString();
        String reEnterPassword = mEtRePassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            mTilName.setErrorEnabled(true);
            mTilName.setError("At least 3 characters");
            valid = false;
        } else {
            mTilName.setErrorEnabled(false);
            mEtName.setError(null);
        }

        if (username.isEmpty()) {
            mTilAddress.setErrorEnabled(true);
            mTilAddress.setError("Enter Valid Address");
            valid = false;
        } else {
            mTilAddress.setErrorEnabled(false);
            mTilAddress.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mTilEmail.setErrorEnabled(true);
            mTilEmail.setError("Enter a valid email address");
            valid = false;
        } else {
            mTilEmail.setErrorEnabled(false);
            mTilEmail.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() < 12 || mobile.length() > 13) {
            mTilPhone.setErrorEnabled(true);
            mTilPhone.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            mTilPhone.setErrorEnabled(false);
            mTilPhone.setError(null);
        }

        if (idLevel.isEmpty() || idLevel.toString().equals("0")) {
            mTilLevel.setErrorEnabled(true);
            mTilLevel.setError("Choose User Level");
            valid = false;
        } else {
            mTilLevel.setErrorEnabled(false);
            mTilLevel.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mTilPassword.setErrorEnabled(true);
            mTilPassword.setError("Between 4 and 10 Alphanumeric Characters");
            valid = false;
        } else {
            mTilPassword.setErrorEnabled(false);
            mTilPassword.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            mTilRePassword.setErrorEnabled(true);
            mTilRePassword.setError("Password Do not match");
            valid = false;
        } else {
            mTilRePassword.setErrorEnabled(false);
            mTilRePassword.setError(null);
        }

        return valid;
    }

    public void onSignupSuccess() {
        mBtnSignup.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    private void openToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void onSignupFailed(String message) {
        openToast(message);
        mBtnSignup.setEnabled(true);
        mEtUsername.setEnabled(true);
        mEtName.setEnabled(true);
        mEtEmail.setEnabled(true);
        mEtPhone.setEnabled(true);
        mEtPassword.setEnabled(true);
        mEtRePassword.setEnabled(true);
        mSpLevel.setEnabled(true);
    }

}
