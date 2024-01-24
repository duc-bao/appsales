package com.manager.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.manager.myapplication.R;
import com.manager.myapplication.model.Giohang;
import com.manager.myapplication.retrofix.ApiBanHang;
import com.manager.myapplication.retrofix.RextrofixClient;
import com.manager.myapplication.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbarthanhtoan;
    AppCompatButton btndathang;
    TextView txtemail, txtsdt, txttongtiendat;
    EditText edtdiachi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    long tongtien;
     int totalItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        countItem();
        initController();
    }
    // dem so luong co trong gio hang
    private void countItem() {
        totalItem = 0;
        if (Utils.mangmuahang != null) {
            for (int i = 0; i < Utils.mangmuahang.size(); i++) {
                totalItem = totalItem + Utils.mangmuahang.get(i).getSoluong();
            }
        }
    }

    private void initController() {
        setSupportActionBar(toolbarthanhtoan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarthanhtoan.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // truyền tổng tiền từ giỏ hàng qua
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.##");
        tongtien = getIntent().getLongExtra("tongtien", 0);
        txttongtiendat.setText(decimalFormat.format(tongtien));
        txtemail.setText(Utils.user_current.getEmail());
        txtsdt.setText(Utils.user_current.getMobile());
        // xử lí sự kiện khi click
        btndathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_diachi = edtdiachi.getText().toString().trim();
                if(TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
                }else{
                    if (Utils.user_current != null) {
                    String str_email = Utils.user_current.getEmail();
                    String str_sdt = Utils.user_current.getMobile();
                    String iduser = Utils.user_current.getId();
                    // post data
                    Log.d("Test", new Gson().toJson(Utils.mangmuahang));
                    // Viet dau API de tra ve cac gia tri trong thanh toan
                    compositeDisposable.add(apiBanHang.donHang(str_email, str_sdt, String.valueOf(tongtien),iduser,str_diachi, totalItem, new Gson().toJson(Utils.mangmuahang))
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                               userModel -> {
                                    Toast.makeText(getApplicationContext(),"Thanh cong", Toast.LENGTH_SHORT).show();
                                    // clear mang gio hang
                                    for(int i = 0; i < Utils.mangmuahang.size(); i++){
                                        Giohang giohang = Utils.mangmuahang.get(i);
                                        if(Utils.manggiohang.contains(giohang)){
                                            Utils.manggiohang.remove(giohang);
                                        }
                                    }
                                   Utils.mangmuahang.clear();
                                   Paper.book().write("giohang", Utils.manggiohang);
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);

                               },throwable -> {
                                   Toast.makeText(getApplicationContext(), throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }}
            }
        });
    }
    private void initView() {
        apiBanHang = RextrofixClient.getInstance(Utils.Base_URL).create(ApiBanHang.class);
        toolbarthanhtoan = findViewById(R.id.toobarthanhtoan);
        btndathang = findViewById(R.id.btndathang);
        txtemail = findViewById(R.id.txtemaildathang);
        txtsdt = findViewById(R.id.txtsdtdathang);
        txttongtiendat = findViewById(R.id.txttongtiendathang);
        edtdiachi = findViewById(R.id.edtdiachi);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}