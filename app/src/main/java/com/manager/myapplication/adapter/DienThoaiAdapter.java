package com.manager.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
// Tạo adapter cho recycleView của điện thoại khi bắt sự kiện
public class DienThoaiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPhamMoi> array;
    private  static  final int VIEW_TYPE_DATA = 0;
    private  static  final int VIEW_TYPE_LOADING = 1;
    public DienThoaiAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nếu có data thì ta gọi đến item_dienthoai
        if(viewType == VIEW_TYPE_DATA){
            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dienthoai, parent,false);
            return  new MyViewHolder(view);
        }else {
            // Nếu ko có data thì ta gọi đến loading để nó xoay
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemdt_loading,parent,false);
            return  new LoadingViewHolder(view);
        }
    }

    // Chuyển đổi sự kiện màn hình
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if(holder instanceof MyViewHolder){
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        SanPhamMoi sanpham = array.get(position);
        myViewHolder.tensp.setText(sanpham.getTensp().trim());
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.##");
        myViewHolder.giasp.setText("Giá:" + decimalFormat.format(Double.parseDouble(sanpham.getGiasp())) + "Đ");
        myViewHolder.mota.setText(sanpham.getMota());
        Glide.with(context).load(sanpham.getHinhanh()).into(myViewHolder.hinhanh);
        myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if(!isLongClick){
                        // click
                        Intent intent = new Intent(context, ChitietActivity.class);
                        intent.putExtra("chitiet", sanpham);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
    }else{
        LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
        loadingViewHolder.progressBar.setIndeterminate(true);
    }
    }
    @Override
    public int getItemViewType(int position) {
        // Nếu mảng nào chúng ta get vị trí đó bằng NULL thì ta gọi đến loading ko thì ngược lại
        return array.get(position) == null?VIEW_TYPE_LOADING:VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }
    // Loading trong mục menu sản phẩm
    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tensp, giasp, mota, idsp;
        ImageView hinhanh;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tensp = itemView.findViewById(R.id.itemdt_ten);
            giasp = itemView.findViewById(R.id.itemdt_gia);
            mota = itemView.findViewById(R.id.item_mota);
            hinhanh = itemView.findViewById(R.id.itemdt_image);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
           itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }
}
