package com.example.mm.mydorm.activity;
import com.example.mm.mydorm.util.NetUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.*;

import com.example.mm.mydorm.R;
import com.example.mm.mydorm.bean.LoginInfo;
import com.example.mm.mydorm.util.HttpsUtil;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText userId = null;
    private EditText passWord = null;
    private Button loginBtn;
    private String uid = "", pwd = "";
    private LoginInfo mLoginInfo = new LoginInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //设置登录按钮点击事件
        loginBtn = (Button) findViewById(R.id.login_button);
        loginBtn.setOnClickListener(this);
        //初始化登录界面
        initView();
    }

    private void initView() {
        userId = (EditText) findViewById(R.id.login_id);
        passWord = (EditText) findViewById(R.id.login_password);

        userId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                uid = userId.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pwd = passWord.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void checkLogin(String userId, String passWord) {
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/Login?username="
                + userId + "&password=" + passWord;
        //新建一个线程，用于验证登录信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                try {
                    URL url = new URL(address);
                    if (url.getProtocol().toUpperCase().equals("HTTPS")) {
                        HttpsUtil.trustAllHosts();
                        HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                        https.setHostnameVerifier(HttpsUtil.DO_NOT_VERIFY);
                        con = https;
                    } else {
                        con = (HttpURLConnection) url.openConnection();
                    }
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    //从网络访问返回的结果读取字符串信息
                    InputStream in = con.getInputStream();
                    mLoginInfo = JSON.parseObject(in, LoginInfo.class);
                    if (mLoginInfo != null) {
                        Log.d("myDorm", mLoginInfo.toString());
                        if (mLoginInfo.getErrcode().equals("0")) {
                            if(Long.parseLong(uid) % 2 == 0) {
                                Intent i = new Intent(LoginActivity.this, StudentInfoYes.class);
                                startActivityForResult(i, 1);
                            }
                            else {
                                Intent i = new Intent(LoginActivity.this, StudentInfoNo.class);
                                startActivityForResult(i, 1);
                            }
                        }
                        else if (mLoginInfo.getErrcode().equals("40001")) {
                            Log.d("myDorm", "学号不存在");
                        }
                        else if (mLoginInfo.getErrcode().equals("40002")) {
                            Log.d("myDorm", "密码错误");
                        }
                        else {
                            Log.d("myDorm", "参数错误");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_button) {
            if (uid.equals("") || pwd.equals("")) {
                Toast.makeText(LoginActivity.this, "请输入用户名和密码！", Toast.LENGTH_LONG).show();
            } else {
                SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("login_id", uid);
                editor.commit();
                if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                    Log.d("myDorm", "网络OK");
                    //如果网络没问题，则调用函数从网络上获取当前编码城市的天气信息
                    checkLogin(uid, pwd);
                } else {
                    Log.d("myWeather", "网络挂了");
                    Toast.makeText(LoginActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
