package com.manager.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.manager.myapplication.R;
import com.manager.myapplication.model.Item;

import java.util.List;

public class ChitietdonhangAdapter extends RecyclerView.Adapter<ChitietdonhangAdapter.MyViewHolder> {
    Context context;
    List<Item> itemList;

    public ChitietdonhangAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    // hien thi man hinh item don hang chi tiet
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhangchitiet, parent, false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.txtten.setText(item.getTensp() +"");
        holder.txtsoluong.setText(item.getSoluong()+"");
        Glide.with(context).load(item.getHinhanh()).into(holder.imagechitiet);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imagechitiet;
        TextView txtten, txtsoluong;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imagechitiet = itemView.findViewById(R.id.item_imgchitietdonhang);
            txtten = itemView.findViewById(R.id.itemtenspchitiet);
            txtsoluong = itemView.findViewById(R.id.itemsoluongchitiet);

        }
    }
}
