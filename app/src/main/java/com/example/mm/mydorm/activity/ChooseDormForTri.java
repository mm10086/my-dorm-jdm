package com.example.mm.mydorm.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.mm.mydorm.R;
import com.example.mm.mydorm.bean.ResultInfo;
import com.example.mm.mydorm.bean.RoomInfo;
import com.example.mm.mydorm.util.HttpsUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by mm on 2017/12/20.
 */

public class ChooseDormForTri extends Activity implements View.OnClickListener {
    public static final int UPDATE_ROOM_INFO = 3;
    public static final int SUCCESS_INFO = 4;
    public static final int FAIL_INFO = 5;
    private TextView name, id, gender, building5, building13, building14;
    private EditText id1, vcode1, id2, vcode2;
    private String id1_str,vcode1_str, id2_str, vcode2_str;

    String id_str, name_str, gender_str, gender_id;

    private RoomInfo mRoomInfo = new RoomInfo();
    private ResultInfo mResultInfo = new ResultInfo();
    private Button mEnsureBtn;
    private static Handler mHandler = null;
    //设置选择宿舍下拉菜单
    private Spinner spinner = null;
    String spinID = "";
    Map<String, String> params = new HashMap<String, String>();

    private void initHandler() {
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_ROOM_INFO:
                        updateRoomInfo((RoomInfo) msg.obj);
                        break;
                    case SUCCESS_INFO:
                        Intent i = new Intent(ChooseDormForTri.this, SuccessNotify.class);
                        startActivityForResult(i, 1);
                        break;
                    case FAIL_INFO:
                        Toast.makeText(ChooseDormForTri.this, "办理失败！", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_dorm_3);
        //初始化登录界面
        initView();
        initHandler();
        queryRoomInfo();
    }

    private void queryRoomInfo() {
        if (gender_str.equals("男")) {
            gender_id = "1";
        } else {gender_id = "2";}

        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/getRoom?gender=" + gender_id;
        Log.d("address",address);
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
                    mRoomInfo = JSON.parseObject(in, RoomInfo.class);
                    if (mRoomInfo != null) {
                        if (mRoomInfo.getErrcode().equals("0")) {
                            Message msg =new Message();
                            msg.what = UPDATE_ROOM_INFO;
                            msg.obj=mRoomInfo;
                            mHandler.sendMessage(msg);
                            Log.d("myDorm", "sendMsg");
                        }
                        else if (mRoomInfo.getErrcode().equals("40001")) {
                            Log.d("myDorm", "学号不存在");
                        }
                        else if (mRoomInfo.getErrcode().equals("40002")) {
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

    private void updateRoomInfo(RoomInfo rinfo) {
        building5.setText(rinfo.getData().get("5").toString());
        building13.setText(rinfo.getData().get("13").toString());
        building14.setText(rinfo.getData().get("14").toString());
    }

    private void initView() {
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        id_str = sharedPreferences.getString("login_id", "1111111111");
        name_str = sharedPreferences.getString("name", "1111111111");
        gender_str = sharedPreferences.getString("gender", "1111111111");

        name = (TextView)findViewById(R.id.cd3_name);
        id = (TextView)findViewById(R.id.cd3_id);
        gender = (TextView)findViewById(R.id.cd3_gender);
        building5 = (TextView)findViewById(R.id.cd3_building5);
        building13 = (TextView)findViewById(R.id.cd3_building13);
        building14 = (TextView)findViewById(R.id.cd3_building14);

        name.setText(name_str);
        id.setText(id_str);
        gender.setText(gender_str);


        initEditViewer();

        mEnsureBtn = (Button)findViewById(R.id.ensure_button_3);
        mEnsureBtn.setOnClickListener(this);

        spinner = (Spinner) findViewById(R.id.spinner_3);
        //建立数据源
        String[] mItems = getResources().getStringArray(R.array.buildings);
        //建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        //绑定 Adapter到控件
        spinner.setPrompt("请选择目标宿舍楼");
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                spinID= spinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ensure_button_3){
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String stu_id = sharedPreferences.getString("login_id","1111111111");
            params.put("num", "1000");
            params.put("stuid", stu_id);
            params.put("stu1id", id1_str);
            params.put("v1code", vcode1_str);
            params.put("stu2id", id2_str);
            params.put("v2code", vcode2_str);
            params.put("buildingNo", spinID);
            submitPostData(params,"utf-8");
        }
    }

    //发送Post请求到服务器
    public void submitPostData(Map<String, String> params, String encode) {
        final byte[] data = getRequestData(params, encode).toString().getBytes();//获得请求体
        final String address2 = "https://api.mysspku.com/index.php/V1/MobileCourse/SelectRoom";
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                try {
                    URL url = new URL(address2);
                    if (url.getProtocol().toUpperCase().equals("HTTPS")) {
                        HttpsUtil.trustAllHosts();
                        HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                        https.setHostnameVerifier(HttpsUtil.DO_NOT_VERIFY);
                        con = https;
                    } else {
                        con = (HttpURLConnection) url.openConnection();
                    }
                    con.setConnectTimeout(3000);           //设置连接超时时间
                    con.setDoInput(true);                  //打开输入流，以便从服务器获取数据
                    con.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
                    con.setRequestMethod("POST");          //设置以Post方式提交数据
                    con.setUseCaches(false);               //使用Post方式不能使用缓存
                    //设置请求体的类型是文本类型
                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    //设置请求体的长度
                    con.setRequestProperty("Content-Length", String.valueOf(data.length));
                    //获得输出流，向服务器写入数据
                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(data);

                    int response = con.getResponseCode();            //获得服务器的响应码
                    if (response == HttpURLConnection.HTTP_OK) {
                        InputStream in = con.getInputStream();
                        Log.d("hahaha",mResultInfo.toString());
                        mResultInfo = JSON.parseObject(in, ResultInfo.class);                     //处理服务器的响应结果
                        if (mResultInfo != null) {
                            Log.d("hahaha",mResultInfo.toString());
                            if (mResultInfo.getErrcode().equals("0")) {
                                Message msg = new Message();
                                msg.what = SUCCESS_INFO;
                                mHandler.sendMessage(msg);
                                Log.d("myDorm", "sendMsg");
                            }
                            else {
                                Message msg = new Message();
                                msg.what = FAIL_INFO;
                                mHandler.sendMessage(msg);
                                Log.d("myDorm", "sendMsg");
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    private void initEditViewer() {
        id1 = (EditText)findViewById(R.id.cd3_id1);
        id2 = (EditText)findViewById(R.id.cd3_id2);
        vcode1 = (EditText)findViewById(R.id.cd3_vcode1);
        vcode2 = (EditText)findViewById(R.id.cd3_vcode2);


        id1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                id1_str = id1.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        id2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                id2_str = id2.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        vcode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vcode1_str = vcode1.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        vcode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vcode2_str = vcode2.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
}
