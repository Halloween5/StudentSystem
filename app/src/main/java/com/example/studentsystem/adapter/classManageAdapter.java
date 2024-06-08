package com.example.studentsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentsystem.bean.classInfo;

import java.util.List;
import com.example.studentsystem.R;


public class classManageAdapter extends RecyclerView.Adapter<classManageAdapter.ViewHolder>{
    private List<classInfo> data;
    private Context mContext;
    private View view;
    private RecyclerView recyclerView;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    public classManageAdapter(Context context, List<classInfo> data){
        this.mContext = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        recyclerView = (RecyclerView) parent;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_manage_list_item, parent, false);
        return new ViewHolder(view);
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(RecyclerView parent, View view,int position);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {}

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView num;
        private TextView name;
        private TextView teacher;
        private TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            num = itemView.findViewById(R.id.num);
            name = itemView.findViewById(R.id.name);
            teacher = itemView.findViewById(R.id.teacher);
            time = itemView.findViewById(R.id.time);
        }
    }
}
