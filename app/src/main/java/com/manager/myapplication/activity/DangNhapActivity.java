package com.manager.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.manager.myapplication.R;
import com.manager.myapplication.retrofix.ApiBanHang;
import com.manager.myapplication.retrofix.RextrofixClient;
import com.manager.myapplication.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    TextView txtdangki, resetpass;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    EditText emaildangnhap, passdangnhap;
    AppCompatButton  btndangnhap;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable =  new CompositeDisposable();
    private boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        initView();
        initControl();
    }

    private void initControl() {
        txtdangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // chuyen man hinh
                Intent intent = new Intent(getApplicationContext(), DangKiActivity.class);
                startActivity(intent);
            }
        });
        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResetPassActivity.class);
                startActivity(intent);
            }
        });
        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String str_email = emaildangnhap.getText().toString().trim();
             String str_pass = passdangnhap.getText().toString().trim();
             if(TextUtils.isEmpty(str_email)) {
                 Toast.makeText(getApplicationContext(), "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
             }else if(TextUtils.isEmpty(str_pass)){
                 Toast.makeText(getApplicationContext(), "Bạn chưa nhập pass", Toast.LENGTH_SHORT).show();
             }else{
                 // Save gia tri email, pass
                 Paper.book().write("email", str_email);
                 Paper.book().write("pass", str_pass);
                 if(user != null){
                     // khi dang ki thi user duoc luu tru tren firebase
                     dangNhap(str_email, str_pass);
                 }else {
                     // user da signout
                     firebaseAuth.signInWithEmailAndPassword(str_email, str_pass).addOnCompleteListener(DangNhapActivity.this, new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if(task.isSuccessful()){
                                 dangNhap(str_email, str_pass);
                             }

                         }
                     });
                 }
             }
            }
        });

    }
    private void dangNhap(String email, String pass){

        compositeDisposable.add(apiBanHang.dangNhap(email, pass).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                isLogin = true;
                                Paper.book().write("isLogin", isLogin);
                                Utils.user_current = userModel.getResult().get(0);
                                // Lưu thông tin người dùng
                                Paper.book().write("user", userModel.getResult().get(0));
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }
    private void initView() {
        Paper.init(this);
        // khởi tạo API
        apiBanHang = RextrofixClient.getInstance(Utils.Base_URL).create(ApiBanHang.class);
        txtdangki = findViewById(R.id.txtdangki);
        emaildangnhap = findViewById(R.id.emaildangnhap);
        passdangnhap = findViewById(R.id.passdangnhap);
        btndangnhap = findViewById(R.id.btnDangnhap);
        resetpass = findViewById(R.id.resetpass);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        // read data user
        if(Paper.book().read("email") != null && Paper.book().read("pass") != null){
            emaildangnhap.setText(Paper.book().read("email"));
            passdangnhap.setText(Paper.book().read("pass"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.user_current.getEmail() != null && Utils.user_current.getPass() != null){
            emaildangnhap.setText(Utils.user_current.getEmail());
            passdangnhap.setText(Utils.user_current.getPass());
        }
    }
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}