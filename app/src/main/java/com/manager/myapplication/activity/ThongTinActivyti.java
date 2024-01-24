package com.manager.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.manager.myapplication.R;

public class ThongTinActivyti extends AppCompatActivity {
    ImageView img;
    TextView txttentt, txtdiachitt, txtsdttt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_activyti);
        intit();
    }

    private void intit() {
        txttentt = findViewById(R.id.thongtinten);
        txtdiachitt = findViewById(R.id.diachitt);
        txtsdttt = findViewById(R.id.sdtthongtin);
        img = findViewById(R.id.imgtt);
    }
}