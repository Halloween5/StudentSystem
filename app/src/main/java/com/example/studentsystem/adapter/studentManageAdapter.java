package com.example.studentsystem.adapter;

import android.app.Activity;
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
    private Activity activity;
    private RecyclerView recyclerView;
    public studentManageAdapter(Context context, Activity activity, List<studentInfo> data){
        this.mContext = context;
        this.activity = activity;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        recyclerView = (RecyclerView) parent;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_manage_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.c_num.setText(data.get(position).getClassNum() + "");
        holder.name.setText(data.get(position).getName());
        holder.s_num.setText(data.get(position).getId());
    }

    @Override
    public int getItemCount() {
        if(data != null)
            return data.size();
        else
            return 0;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView s_num;
        private TextView name;
        private TextView c_num;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            s_num = itemView.findViewById(R.id.s_num);
            name = itemView.findViewById(R.id.name);
            c_num = itemView.findViewById(R.id.c_num);
        }
    }
}

