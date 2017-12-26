package com.example.mm.mydorm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mm.mydorm.R;
import com.example.mm.mydorm.bean.LoginInfo;

/**
 * Created by mm on 2017/12/18.
 */

public class SuccessNotify extends Activity implements View.OnClickListener{
    private Button returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_success);
        initButton();
    }

    private void initButton() {
        returnBtn = (Button) findViewById(R.id.return_button);
        returnBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.return_button){
            Intent i = new Intent(SuccessNotify.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Log.d("what", "111");
            startActivityForResult(i, 1);
            Log.d("what", "122");
        }
    }
}
