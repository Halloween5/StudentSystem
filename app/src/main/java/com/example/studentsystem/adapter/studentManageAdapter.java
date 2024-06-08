package com.example.studentsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentsystem.R;
import com.example.studentsystem.bean.studentInfo;

import java.util.List;

public class studentManageAdapter extends RecyclerView.Adapter<studentManageAdapter.ViewHolder>{
    private List<studentInfo> data;
    private Context mContext;
    private View view;
    private RecyclerView recyclerView;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    public studentManageAdapter(Context context, List<studentInfo> data){
        this.mContext = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        recyclerView = (RecyclerView) parent;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_manage_list_item, parent, false);
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
        private TextView s_num;
        private TextView name;
        private TextView c_num;
        private TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            s_num = itemView.findViewById(R.id.s_num);
            name = itemView.findViewById(R.id.name);
            c_num = itemView.findViewById(R.id.c_num);
            time = itemView.findViewById(R.id.time);
        }
    }
}

