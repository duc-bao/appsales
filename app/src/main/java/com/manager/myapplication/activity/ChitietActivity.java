package com.manager.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.manager.myapplication.R;
import com.manager.myapplication.model.Giohang;
import com.manager.myapplication.model.SanPhamMoi;
import com.manager.myapplication.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ChitietActivity extends AppCompatActivity {
    TextView tensp, giasp, mota;
    Button btnthem, btnyoutube;
    ImageView imgHinhAnh;
    Spinner spinner;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet);
        intView();
        ActionToolBar();
        initData();
        initControl();
    }

    private void initControl() {
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themgiohang();
                Paper.book().write("giohang",Utils.manggiohang);
            }
        });
        btnyoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), XemYoutubeActivity.class);
                intent.putExtra("linkvideo",sanPhamMoi.getLinkvideo());
                startActivity(intent);
            }
        });
    }
    // Xử lí phần thêm giỏ hàng, hiện thị số lượng
    private void themgiohang() {
        if(Utils.manggiohang.size() > 0){
            // Nếu có sản phẩm trùng lặp thì ta add tiếp giá vào
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for(int i = 0; i < Utils.manggiohang.size(); i++){
                if(Utils.manggiohang.get(i).getIdsp() == sanPhamMoi.getId()){
                    Utils.manggiohang.get(i).setSoluong(soluong + Utils.manggiohang.get(i).getSoluong());
                    flag = true;
                }
            }
            if(flag == false){
                long gia = Long.parseLong(sanPhamMoi.getGiasp());
                Giohang giohang = new Giohang();
                giohang.setGiasp(gia);
                giohang.setSoluong(soluong);
                giohang.setIdsp(sanPhamMoi.getId());
                giohang.setHinhanh(sanPhamMoi.getHinhanh());
                giohang.setTensp(sanPhamMoi.getTensp());
                giohang.setSltonkho(sanPhamMoi.getSltonkho());

                Utils.manggiohang.add(giohang);

            }

        }else{
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong(sanPhamMoi.getGiasp());
            Giohang giohang = new Giohang();
            giohang.setGiasp(gia);
            giohang.setIdsp(sanPhamMoi.getId());
            giohang.setHinhanh(sanPhamMoi.getHinhanh());
            giohang.setSoluong(soluong);
            giohang.setTensp(sanPhamMoi.getTensp());
            giohang.setSltonkho(sanPhamMoi.getSltonkho());
            Utils.manggiohang.add(giohang);
        }
        // Mỗi lần duyệt qua thì nó lấy số lượng + với total
        int totalItem = 0;
        for(int i = 0; i < Utils.manggiohang.size(); i++){
            totalItem = totalItem +Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }

    // Đưa dữ liệu vào màn hình
    private void initData() {
        sanPhamMoi = sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
            tensp.setText(sanPhamMoi.getTensp());
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.##");
            giasp.setText("Giá:" + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp())) + "Đ");
            mota.setText(sanPhamMoi.getMota());
            Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imgHinhAnh);
            List<Integer> so = new ArrayList<>();
            for (int i = 1; i < sanPhamMoi.getSltonkho()+ 1; i++){
                so.add(i);
            }
            ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, so);
            spinner.setAdapter(adapterspin);
        }

    private void intView() {
        btnyoutube =findViewById(R.id.btnyoutube);

        tensp = findViewById(R.id.tenspchitiet);
        giasp = findViewById(R.id.giaspchitiet);
        mota = findViewById(R.id.textmotachitiet);
        btnthem = findViewById(R.id.btnthemvaogiohang);
        spinner = findViewById(R.id.spinerchitiet);
        toolbar = findViewById(R.id.toobarchitiet);
        imgHinhAnh = findViewById(R.id.imagechitiet);
        badge = findViewById(R.id.menu_sl);
        FrameLayout frameLayoutgiohang = findViewById(R.id.frameGiohang);
        frameLayoutgiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentgiohang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intentgiohang);
            }
        });
        if (Utils.manggiohang != null) {
            // Mỗi lần duyệt qua thì nó lấy số lượng + với total
            int totalItem = 0;
            for(int i = 0; i < Utils.manggiohang.size(); i++){
                totalItem = totalItem +Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }

    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.manggiohang != null) {
            // Mỗi lần duyệt qua thì nó lấy số lượng + với total
            int totalItem = 0;
            for(int i = 0; i < Utils.manggiohang.size(); i++){
                totalItem = totalItem +Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
    }
}