package com.manager.myapplication.retrofix;

import com.manager.myapplication.model.DonHangModel;
import com.manager.myapplication.model.LoaiSpModel;
import com.manager.myapplication.model.MessageModel;
import com.manager.myapplication.model.SanPhamMoiModel;
import com.manager.myapplication.model.UserModel;

import io.reactivex.rxjava3.core.Observable;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBanHang {
    // Getdata
    @GET("getloaisp.php")
    Observable<LoaiSpModel> getLoaiSp();
    @GET("getspmoi.php")
    Observable<SanPhamMoiModel> getspMoi();
    // Post data
    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getSanPham(
            @Field("page") int page,
            @Field("loai") int loai
            );

    @POST("dangki.php")
    @FormUrlEncoded
    Observable<UserModel> dangKi(
        @Field("email") String email,
        @Field("pass") String pass,
        @Field("username") String username,
        @Field("mobile") String mobile,
        @Field("uid") String uid
    );
    @POST("updatatoken.php")
    @FormUrlEncoded
    Observable<MessageModel> updateToken(
            @Field("id") int id,
            @Field("token") String token

    );
    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangNhap(
            @Field("email") String email,
            @Field("pass") String pass

    );
    @POST("donhang.php")
    @FormUrlEncoded
    Observable<UserModel> donHang(
            @Field("email") String email,
            @Field("sodienthoai") String sodienthoai,
            @Field("tongtien") String tongtien,
            @Field("iduser") String iduser,
            @Field("diachi") String diachi,
            @Field("soluong") int soluong,
            @Field("chitiet") String chitiet

    );
    @POST("xemdonhang.php")
    @FormUrlEncoded
    Observable<DonHangModel> xemdonhang(
            @Field("iduser") int id
    );
    @POST("timkiem.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> search(
            @Field("search") String search
    );
    @POST("gettoken.php")
    @FormUrlEncoded
    Observable<UserModel> gettoken(
            @Field("status") int status
    );
}
