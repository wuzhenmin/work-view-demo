package com.kingdee.min.logindemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingdee.min.logindemo.R;
import com.kingdee.min.logindemo.util.InputStream2String;

import org.droidparts.adapter.widget.TextWatcherAdapter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText mEtAcount;
    private EditText mEtPassword;
    private TextView mTvAcountError;
    private TextView mTvPasswordError;
    private EditText mEtCode;
    private TextView mTvCodeError;
    private Button mBtnLogin;
    private ImageView mImageViewCode;
    private boolean mAcountHasError = true;
    private boolean mPasswordHasError = true;
    private boolean mCodeHashError = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();
        getCodeImage();
        check();
    }

    private void initViews() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {

        String username = mEtAcount.getText().toString().trim();
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        Intent intent = new Intent(MainActivity.this, SelfDigitalActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void check() {
        mEtAcount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    mTvAcountError.setVisibility(View.INVISIBLE);
                    mEtAcount.addTextChangedListener(new TextWatcherAdapter(mEtAcount, new TextWatcherAdapter.TextWatcherListener() {
                        @Override
                        public void onTextChanged(EditText view, String text) {
                            if (TextUtils.isEmpty(mEtAcount.getText())) {
                                mTvAcountError.setText("用户名不能为空");
                                mAcountHasError = true;
                            } else if ((mEtAcount.getText().toString().length() > 5
                            ) || (mEtAcount.getText().toString().length() < 2)) {
                                mTvAcountError.setText("用户名必须2-5位");
                                mAcountHasError = true;
                            } else {
                                mAcountHasError = false;
                            }
                            if (mAcountHasError || mPasswordHasError || mCodeHashError) {
                                mBtnLogin.setEnabled(false);
                            } else {
                                mBtnLogin.setEnabled(true);
                            }
                        }
                    }));

                } else {
                    if (TextUtils.isEmpty(mEtAcount.getText())) {
                        mTvAcountError.setText("用户名不能为空");
                        mAcountHasError = true;
                    }
                    if (mAcountHasError) {
                        mTvAcountError.setVisibility(View.VISIBLE);
                    } else {
                        mTvAcountError.setVisibility(View.INVISIBLE);
                    }

                }
            }
        });

        mEtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    mTvPasswordError.setVisibility(View.INVISIBLE);
                    mEtPassword.addTextChangedListener(new TextWatcherAdapter(mEtPassword, new TextWatcherAdapter.TextWatcherListener() {
                        @Override
                        public void onTextChanged(EditText view, String text) {
                            if (TextUtils.isEmpty(mEtPassword.getText())) {
                                mTvPasswordError.setText("密码不能为空");
                                mPasswordHasError = true;
                            } else if (!Pattern.matches("^[a-zA-Z]\\w{5,15}$", mEtPassword.getText().toString())) {
                                mTvPasswordError.setText("以字母开头，长度为6~15之间的字母、数字和下划线");
                                mPasswordHasError = true;
                            } else {
                                mPasswordHasError = false;
                            }

                            if (mAcountHasError || mPasswordHasError || mCodeHashError) {
                                mBtnLogin.setEnabled(false);
                            } else {
                                mBtnLogin.setEnabled(true);
                            }
                        }
                    }));

                } else {
                    if (TextUtils.isEmpty(mEtPassword.getText())) {
                        mTvPasswordError.setText("密码不能为空");
                        mPasswordHasError = true;
                    }
                    if (mPasswordHasError) {
                        mTvPasswordError.setVisibility(View.VISIBLE);
                    } else {
                        mTvPasswordError.setVisibility(View.INVISIBLE);
                    }

                }
            }
        });

        mEtCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    mTvCodeError.setVisibility(View.INVISIBLE);
                    mEtCode.addTextChangedListener(new TextWatcherAdapter(mEtCode, new TextWatcherAdapter.TextWatcherListener() {
                        @Override
                        public void onTextChanged(EditText view, String text) {
                            if (TextUtils.isEmpty(mEtCode.getText())) {
                                mTvCodeError.setText("验证码不能为空");
                                mCodeHashError = true;
                            } else {
                                mCodeHashError = false;
                            }
                            if (mAcountHasError || mPasswordHasError || mCodeHashError) {
                                mBtnLogin.setEnabled(false);
                            } else {
                                mBtnLogin.setEnabled(true);
                            }
                        }
                    }));
                } else {
                    if (TextUtils.isEmpty(mEtCode.getText())) {
                        mTvCodeError.setText("验证码不能为空");
                        mCodeHashError = true;
                    }
                    if (mCodeHashError) {
                        mTvCodeError.setVisibility(View.VISIBLE);
                    } else {
                        mTvCodeError.setVisibility(View.INVISIBLE);
                    }

                }
            }
        });

    }

    private void findViews() {
        mBtnLogin = (Button) findViewById(R.id.login_aty_btn_loign);
        mImageViewCode = (ImageView) findViewById(R.id.login_aty_iv_code);
        mEtAcount = (EditText) findViewById(R.id.login_aty_et_acount);
        mEtPassword = (EditText) findViewById(R.id.login_aty_et_password);
        mEtCode = (EditText) findViewById(R.id.login_aty_et_code);
        mTvCodeError = (TextView) findViewById(R.id.login_aty_code_tv_error);
        mTvAcountError = (TextView) findViewById(R.id.login_aty_acount_tv_error);
        mTvPasswordError = (TextView) findViewById(R.id.login_aty_et_password_tv_error);

    }

    public void getCodeImage() {
        String result = "";
        new GetCodeAsynTask().execute("http://lttclaw.cn/vcode.php", "", result);
    }


    private class GetCodeAsynTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection conn = null;
            URL mUrl = null;
            try {
                mUrl = new URL(strings[0]);
                conn = (HttpURLConnection) mUrl.openConnection();
                InputStream response = conn.getInputStream();
                strings[2] = InputStream2String.getStringFromInputStream(response);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return strings[2];
        }


        @Override
        protected void onPostExecute(String s) {
            JSONObject info = null;
            try {
                info = new JSONObject(s);
                String imageDataStr = (String) info.get("validationCodeImage");
                byte[] decode = Base64.decode(imageDataStr, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                mImageViewCode.setImageBitmap(bitmap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
