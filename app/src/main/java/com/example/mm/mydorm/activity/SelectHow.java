package com.example.mm.mydorm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mm.mydorm.R;

/**
 * Created by mm on 2017/12/17.
 */

public class SelectHow extends Activity implements View.OnClickListener{
    private Button btn1, btn2, btn3, btn4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_how);

        initButton();
    }

    private void initButton() {
        btn1 = (Button)findViewById(R.id.select_button_1);
        btn2 = (Button)findViewById(R.id.select_button_2);
        btn3 = (Button)findViewById(R.id.select_button_3);
        btn4 = (Button)findViewById(R.id.select_button_4);

        btn1.setOnClickListener(this);
        //btn2.setOnClickListener(this);
        //btn3.setOnClickListener(this);
        //btn4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.select_button_1) {
            Intent i = new Intent(SelectHow.this, ChooseDormForOne.class);
            Log.d("what", "111");
            startActivityForResult(i, 1);
            Log.d("what", "122");
        }
    }
}
