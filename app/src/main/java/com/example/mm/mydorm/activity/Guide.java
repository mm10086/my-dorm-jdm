package com.example.mm.mydorm.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mm.mydorm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mm on 2017/12/20.
 */

public class Guide extends Activity implements View.OnClickListener{
    private Button btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        setGuide();
        btn = (Button)findViewById(R.id.guide_btn);
        btn.setOnClickListener(this);
    }

    private void setGuide() {
        SharedPreferences preferences = getSharedPreferences("config1", MODE_PRIVATE);
        String guideId = preferences.getString("guide", "1");
        if(guideId.equals("0")) {
            Intent i = new Intent(Guide.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        SharedPreferences preferences = getSharedPreferences("config1", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("guide", "0");
        editor.commit();
        Intent i = new Intent(Guide.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

}
