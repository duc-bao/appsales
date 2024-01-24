package com.manager.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.myapplication.Interface.ItemClickListener;
import com.manager.myapplication.R;
import com.manager.myapplication.model.DonHang;
import com.manager.myapplication.model.Event.DonHangEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class DonHangApdapter extends RecyclerView.Adapter<DonHangApdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> listDonHang;

    public DonHangApdapter(Context context, List<DonHang> listDonHang) {
        this.context = context;
        this.listDonHang = listDonHang;
    }
    // Hiển thị
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang,parent,false);
        return  new MyViewHolder(view);
    }
    // Thực thi
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang = listDonHang.get(position);
        holder.txtiddonhang.setText("Đơn hàng: " +donHang.getId());
        holder.trangthai.setText(trangThaiDonHang(donHang.getTrangthai()));
        holder.diachi.setText("Địa chỉ: " + donHang.getDiachi());
        // xử lí recycleview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                holder.rechitiet.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        linearLayoutManager.setInitialPrefetchItemCount(donHang.getItem().size());
        // apdapter chi tiet
        ChitietdonhangAdapter chitietdonhangAdapter = new ChitietdonhangAdapter(context, donHang.getItem());
        holder.rechitiet.setLayoutManager(linearLayoutManager);
        holder.rechitiet.setAdapter(chitietdonhangAdapter);
        // vì recycleview nằm trong recycleview nên phải đặt trong đó
        holder.rechitiet.setRecycledViewPool(viewPool);
        //Bắt sự kiện đơn hàng
        holder.setListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(isLongClick){
                    EventBus.getDefault().postSticky(new DonHangEvent(donHang));
                }
            }
        });

    }
    private  String trangThaiDonHang(int status){
        String result = "";
        switch (status){
            case 0 :
                result = "Đơn hàng đang được xử lí";
                break;
            case 1 :
                result = "Đơn hàng đã chấp nhận";
                break;
            case 2:
                result = "Đơn hàng đã giao cho đơn vị vận chuyển";
                break;
            case 3:
                result = "Đơn hàng đã giao thành công";
                break;
            case 4:
                result = "Đơn hàng đã bị hủy";
                break;
        }
        return  result;
    }
    @Override
    public int getItemCount() {
        return listDonHang.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView txtiddonhang, trangthai,diachi;
        RecyclerView rechitiet;
        ItemClickListener listener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtiddonhang =itemView.findViewById(R.id.iddonhang);
            rechitiet = itemView.findViewById(R.id.iddonhangchitiet);
            trangthai = itemView.findViewById(R.id.tinhtrang);
            diachi = itemView.findViewById(R.id.diachidonhang);
            itemView.setOnLongClickListener(this);
        }

        public void setListener(ItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public boolean onLongClick(View v) {
            listener.onClick(v, getAdapterPosition(), true);
            return false;
        }
    }
}
