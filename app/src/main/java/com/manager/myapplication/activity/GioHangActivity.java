package com.manager.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.manager.myapplication.R;
import com.manager.myapplication.adapter.GiohangAdapter;
import com.manager.myapplication.model.Event.TinhTongEvent;
import com.manager.myapplication.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

public class GioHangActivity extends AppCompatActivity {
    TextView giohangtrong, tongtien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnmuahang;
    GiohangAdapter giohangAdapter;
    long tongtiensp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();
        initControl();

        if(Utils.mangmuahang != null){
            Utils.mangmuahang.clear();
        }
        tinhTongTien();
    }
    //Tinhs tổng tiền
    private void tinhTongTien() {
        tongtiensp = 0;
        for(int i = 0; i < Utils.mangmuahang.size(); i++){
            tongtiensp = tongtiensp +Utils.mangmuahang.get(i).getGiasp()*Utils.mangmuahang.get(i).getSoluong();
        }
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.##");

        tongtien.setText( decimalFormat.format(tongtiensp));
    }

    // Điều khiển câu lệnh người dùng
    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(Utils.manggiohang.size() == 0){
            giohangtrong.setVisibility(View.VISIBLE);
        }else{
            giohangAdapter = new GiohangAdapter(getApplicationContext(), Utils.manggiohang);
            recyclerView.setAdapter(giohangAdapter);
        }
        // nhấn nút mua hàng thì sang màn hình thanh toán
        btnmuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ThanhToanActivity.class);
                // chuyển tổng tiền qua bên thanh toán
                intent.putExtra("tongtien",tongtiensp);
//                Utils.manggiohang.clear();
                startActivity(intent);
            }
        });
    }

    private void initView() {
        giohangtrong = findViewById(R.id.giohangtrong);
        toolbar = findViewById(R.id.toobargiohang);
        recyclerView = findViewById(R.id.recycleViewgiohang);
        tongtien = findViewById(R.id.tongtienchitiet);
        btnmuahang = findViewById(R.id.btnmuahang);

    }
    // Bắt sự kiện giỏ hàng
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongEvent event){
        if(event != null){
            tinhTongTien();
        }
    }
}