package com.example.mm.mydorm.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.mm.mydorm.R;
import com.example.mm.mydorm.bean.LoginInfo;
import com.example.mm.mydorm.bean.StudentInfoY;
import com.example.mm.mydorm.util.HttpsUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by mm on 2017/12/17.
 */

public class StudentInfoYes extends Activity {
    public static final int UPDATE_STUDENT_INFO_YES = 1;
    private TextView name, id, gender, building, room;
    private StudentInfoY mStuInfo = new StudentInfoY();
    private static Handler mHandler = null;

    private void initHandler() {
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_STUDENT_INFO_YES:
                        updateStudentInfo((StudentInfoY) msg.obj);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void updateStudentInfo(StudentInfoY obj) {
        name.setText(obj.getData().get("name"));
        id.setText(obj.getData().get("studentid"));
        gender.setText(obj.getData().get("gender"));
        building.setText(obj.getData().get("building"));
        room.setText(obj.getData().get("room"));
        Log.d("update","success");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_yes);
        //初始化登录界面
        initView();
        initHandler();
        queryStudentInfo();
    }

    private void queryStudentInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String uid = sharedPreferences.getString("login_id", "1111111111");
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/getDetail?stuid=" + uid;
        //新建一个线程，用于获取个人信息
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
                    mStuInfo = JSON.parseObject(in, StudentInfoY.class);
                    Log.d("stuinfo", mStuInfo.toString());
                    if (mStuInfo != null) {
                        if (mStuInfo.getErrcode().equals("0")) {
                            Message msg =new Message();
                            msg.what = UPDATE_STUDENT_INFO_YES;
                            msg.obj=mStuInfo;
                            mHandler.sendMessage(msg);
                            Log.d("myDorm", "sendMsg");
                        }
                        else if (mStuInfo.getErrcode().equals("40001")) {
                            Log.d("myDorm", "学号不存在");
                        }
                        else if (mStuInfo.getErrcode().equals("40002")) {
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

    private void initView() {
        name = (TextView)findViewById(R.id.uiy_name);
        id = (TextView)findViewById(R.id.uiy_id);
        gender = (TextView)findViewById(R.id.uiy_gender);
        building = (TextView)findViewById(R.id.uiy_building);
        room = (TextView)findViewById(R.id.uiy_room);
    }
}
