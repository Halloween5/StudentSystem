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
import com.example.studentsystem.bean.courseInfo;

import java.util.List;


public class courseManageAdapter extends RecyclerView.Adapter<courseManageAdapter.ViewHolder>{
    private List<courseInfo> data;
    private Context mContext;
    private Activity activity;
    private View view;
    private RecyclerView recyclerView;
    public courseManageAdapter(Context context, Activity activity, List<courseInfo> data){
        this.mContext = context;
        this.activity = activity;
        this.data = data;
    }

    @NonNull
    @Override
    public courseManageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        recyclerView = (RecyclerView) parent;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_manage_list_item, parent, false);
        return new courseManageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull courseManageAdapter.ViewHolder holder, int position) {
        holder.c_name.setText(data.get(position).getCourse());
        holder.t_name.setText(data.get(position).getTname());
        holder.c_num.setText(data.get(position).getClassNum() + "");
        holder.time.setText(data.get(position).getTime().toString().replaceAll("[Tt]", " "));
    }

    @Override
    public int getItemCount() {
        if(data != null)
            return data.size();
        else
            return 0;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView c_num;
        private TextView c_name;
        private TextView t_name;
        private TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            c_num = itemView.findViewById(R.id.c_num);
            c_name = itemView.findViewById(R.id.c_name);
            t_name = itemView.findViewById(R.id.t_name);
            time = itemView.findViewById(R.id.time);
        }
    }
}
