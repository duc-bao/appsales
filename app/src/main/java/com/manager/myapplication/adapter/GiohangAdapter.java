package com.manager.myapplication.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.manager.myapplication.Interface.IImageClickListener;
import com.manager.myapplication.R;
import com.manager.myapplication.model.Event.TinhTongEvent;
import com.manager.myapplication.model.Giohang;
import com.manager.myapplication.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

import io.paperdb.Paper;

public class GiohangAdapter extends RecyclerView.Adapter<GiohangAdapter.MyViewHolder> {
    Context context;
    List<Giohang> giohangList;

    public GiohangAdapter(Context context, List<Giohang> giohangList) {
        this.context = context;
        this.giohangList = giohangList;
    }
    // Cài đặt dữ liệu hiển thị
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang,parent,false);

        return new MyViewHolder(view);
    }
    //ĐỔ dữ liệu
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Giohang giohang = giohangList.get(position);
        holder.item_giohang_tensp.setText(giohang.getTensp());
        holder.item_giohangluong.setText(giohang.getSoluong()+"");
        Glide.with(context).load(giohang.getHinhanh()).into(holder.item_giohang_img);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.##");
        holder.item_giohang_giasp.setText(decimalFormat.format((giohang.getGiasp())) + "Đ");
        long gia =  giohang.getSoluong() * giohang.getGiasp();
        holder.item_giohang_giasp2.setText(decimalFormat.format((giohang.getGiasp())));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Nếu người dùng click bỏ chọn thì ta chui vào mảng mua hàng tìm id giống để xóa đi
                if(isChecked == true){
                    Utils.manggiohang.get(holder.getAdapterPosition()).setCheck(true);
                    if(!Utils.mangmuahang.contains(giohang)){
                        Utils.mangmuahang.add(giohang);
                    }
                    EventBus.getDefault().postSticky( new TinhTongEvent());
                }else {
                    Utils.manggiohang.get(holder.getAdapterPosition()).setCheck(false);
                    for(int i = 0; i < Utils.mangmuahang.size(); i++){
                        if(Utils.mangmuahang.get(i).getIdsp() == giohang.getIdsp()){
                            Utils.mangmuahang.remove(i);
                            EventBus.getDefault().postSticky(new TinhTongEvent());
                        }
                    }
                }
            }
        });
        // lay nhung gi da co trong gio hang gan nguoc lai
        holder.checkBox.setChecked(giohang.isCheck());
        holder.setListener(new IImageClickListener() {
            @Override
            public void onClickImage(View view, int pos, int giatri) {
                if(giatri == 1){
                    if(giohangList.get(pos).getSoluong() > 1){
                        int soluongmoi = giohangList.get(pos).getSoluong() - 1;
                        giohangList.get(pos).setSoluong(soluongmoi);

                        holder.item_giohangluong.setText(giohangList.get(pos).getSoluong() + " ");
                        long gia =  giohangList.get(pos).getSoluong() * giohangList.get(pos).getGiasp();
                        holder.item_giohang_giasp2.setText(decimalFormat.format((gia)));
                        EventBus.getDefault().postSticky( new TinhTongEvent());
                    }else if(giohangList.get(pos).getSoluong() == 1){
                        // cài đặt thông báo để xóa sản phẩm khỏi giỏ hàng
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm này ra khỏi cửa hàng");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.mangmuahang.remove(giohang);
                                Utils.manggiohang.remove(pos);
                                Paper.book().write("giohang", Utils.manggiohang);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();

                    }
                }else if(giatri == 2) {
                    if(giohangList.get(pos).getSoluong() < giohangList.get(pos).getSltonkho()){
                        int soluongmoi = giohangList.get(pos).getSoluong() + 1;
                        giohangList.get(pos).setSoluong(soluongmoi);
                    }
                    holder.item_giohangluong.setText(giohangList.get(pos).getSoluong() + " ");
                    long gia =  giohangList.get(pos).getSoluong() * giohangList.get(pos).getGiasp();
                    holder.item_giohang_giasp2.setText(decimalFormat.format((gia)));
                    EventBus.getDefault().postSticky( new TinhTongEvent());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
            return giohangList.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_giohang_img, icontru, iconcong;
        IImageClickListener listener;
        TextView item_giohang_tensp, item_giohang_giasp, item_giohangluong, item_giohang_giasp2;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_giohang_img = itemView.findViewById(R.id.item_giohang_img);
            item_giohang_tensp = itemView.findViewById(R.id.item_giohang_tensp);
            item_giohang_giasp = itemView.findViewById(R.id.item_giohang_gia);
            item_giohangluong = itemView.findViewById(R.id.item_giohang_soluong);
            item_giohang_giasp2 = itemView.findViewById(R.id.item_giohang_giaspbang);
            icontru = itemView.findViewById(R.id.item_giohang_tru);
            iconcong = itemView.findViewById(R.id.item_giohang_cong);
            checkBox = itemView.findViewById(R.id.checkboxgiohang);
            // event click
            iconcong.setOnClickListener(this);
            icontru.setOnClickListener(this);
        }

        public void setListener(IImageClickListener listener) {
            this.listener = listener;
        }
        // Bắt sự kiện cộng trừ giỏ hàng
        @Override
        public void onClick(View v) {
            if(v == icontru){
                listener.onClickImage(itemView, getAdapterPosition(),1);
            }else if(v == iconcong){
                listener.onClickImage(itemView, getAdapterPosition(),2);
            }
        }
    }
}
