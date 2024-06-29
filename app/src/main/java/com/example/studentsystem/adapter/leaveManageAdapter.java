package com.example.studentsystem.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentsystem.R;
import com.example.studentsystem.bean.leaveInfo;

import java.util.List;

public class leaveManageAdapter extends RecyclerView.Adapter<leaveManageAdapter.ViewHolder>{
    private List<leaveInfo> data;
    private Context mContext;
    private Activity activity;
    private View view;
    private RecyclerView recyclerView;
    private leaveManageAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public leaveManageAdapter(Context context, Activity activity, List<leaveInfo> data) {
        this.mContext = context;
        this.activity = activity;
        this.data = data;
    }

    @NonNull
    @Override
    public leaveManageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        recyclerView = (RecyclerView) parent;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_manage_list_item, parent, false);
        return new leaveManageAdapter.ViewHolder(view);
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(RecyclerView recyclerView, View view, int position);
    }

    public void setOnItemClickListener(leaveManageAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull leaveManageAdapter.ViewHolder holder, int position) {
        holder.reason.setText(data.get(position).getReason());
        holder.c_num.setText(data.get(position).getClassNum() + "");
        holder.s_num.setText(data.get(position).getId());
        holder.reason.setText(data.get(position).getReason());
        holder.time.setText(data.get(position).getTime().toString().replaceAll("[Tt]", " "));
        holder.f_time.setText(data.get(position).getFrom().toString());
        holder.t_time.setText(data.get(position).getTo().toString());
        holder.name.setText(data.get(position).getName());
        if (data.get(position).getState() == 0) {
            holder.state.setText("等待审核");
            holder.state.setTextColor(Color.RED);
        } else {
            holder.state.setText("已读");
            holder.state.setTextColor(Color.GREEN);
        }
        if (onRecyclerViewItemClickListener != null) {
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRecyclerViewItemClickListener.onItemClick(recyclerView, view, holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (data != null)
            return data.size();
        else
            return 0;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout item;
        private TextView c_num;
        private TextView s_num;
        private TextView name;
        private TextView reason;
        private TextView time;
        private TextView f_time;
        private TextView t_time;
        private TextView state;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            c_num = itemView.findViewById(R.id.c_num);
            s_num = itemView.findViewById(R.id.s_num);
            name = itemView.findViewById(R.id.name);
            reason = itemView.findViewById(R.id.reason);
            f_time = itemView.findViewById(R.id.f_time);
            t_time = itemView.findViewById(R.id.t_time);
            time = itemView.findViewById(R.id.time);
            state = itemView.findViewById(R.id.state);
        }
    }
}