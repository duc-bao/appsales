package com.manager.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.manager.myapplication.Interface.ItemClickListener;
import com.manager.myapplication.R;
import com.manager.myapplication.activity.ChitietActivity;
import com.manager.myapplication.model.SanPhamMoi;

import java.text.DecimalFormat;
import java.util.List;
// Class Tao RecyeleView trong trang chu
// RecycleView Trong trang chủ

public class SanPhamMoiAdapter extends RecyclerView.Adapter<SanPhamMoiAdapter.MyViewHolder> {

    Context context;
    List<SanPhamMoi> array;

    public SanPhamMoiAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemsp_moi, parent, false);
        return new MyViewHolder(item);
    }
    // Đưa hình ảnh các text vào trong listview trang chủ chính
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPhamMoi sanPhamMoi = array.get(position);
        holder.txten.setText(sanPhamMoi.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat( "#,##0.##");
        holder.txgia.setText("Giá:" + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp())) + "Đ");
        // Taoj hinh anh
        Glide.with(context).load(sanPhamMoi.getHinhanh()).into(holder.imghinhanh);
        holder.setListenertrangchu(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    Intent intent = new Intent(context, ChitietActivity.class);
                    intent.putExtra("chitiet", sanPhamMoi);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
        

    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txgia, txten;
        ImageView imghinhanh;
        ItemClickListener listenertrangchu;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txgia = (TextView) itemView.findViewById(R.id.itemsp_giamoi);
            txten = (TextView) itemView.findViewById(R.id.itemsp_tenmoi);
            imghinhanh = itemView.findViewById(R.id.itemsp_imagemoi);
            itemView.setOnClickListener(this);

        }

        public void setListenertrangchu(ItemClickListener listenertrangchu) {
            this.listenertrangchu = listenertrangchu;
        }

        @Override
        public void onClick(View v) {
            listenertrangchu.onClick(v,getAdapterPosition(), false);
        }
    }
}
