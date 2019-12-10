package com.my.orderclassroom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.my.orderclassroom.R;
import com.my.orderclassroom.entity.OrderInfoEntity;

import java.util.List;

public class MyListAdapter extends RecyclerView.Adapter {
    private List<OrderInfoEntity> list;
    private Context context;
    private OnMyClickListener onMyClickListener;

    public MyListAdapter(List<OrderInfoEntity> list, Context context) {
        this.list = list;
        this.context = context;
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;

    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder= (MyViewHolder) holder;

        if ((position+1)%4==0){
            myViewHolder.iv.setImageResource(R.drawable.my_signify04);
        }else if ((position+1)%4==3){
            myViewHolder.iv.setImageResource(R.drawable.my_signify03);
        }else if ((position+1)%4==2){
            myViewHolder.iv.setImageResource(R.drawable.my_signify02);
        }else {
            myViewHolder.iv.setImageResource(R.drawable.my_signify01);
        }

        myViewHolder.tvEndTime.setText(list.get(position).getEndTime().substring(0,16));
        myViewHolder.tvStartTime.setText(list.get(position).getBeginTime().substring(0,16));
        myViewHolder.tvRoom.setText(list.get(position).getAddress()+list.get(position).getClassroomName());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMyClickListener.onItemclick(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoom,tvStartTime,tvEndTime;
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvRoom=itemView.findViewById(R.id.tvRoom);
            tvStartTime=itemView.findViewById(R.id.tvStartTime);
            tvEndTime=itemView.findViewById(R.id.tvEndTime);
            iv=itemView.findViewById(R.id.iv);
        }
    }

    public interface OnMyClickListener{
        public void onItemclick(OrderInfoEntity entity);
    }
    public void setItemOnClick(OnMyClickListener itemOnClick) {
        this.onMyClickListener = itemOnClick;
    }
}
