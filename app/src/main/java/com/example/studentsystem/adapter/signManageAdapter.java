package com.example.studentsystem.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.studentsystem.R;
import com.example.studentsystem.bean.signInfo;
import com.example.studentsystem.utils.CommonUtils;

import java.util.List;

import cc.shinichi.library.ImagePreview;

public class signManageAdapter extends RecyclerView.Adapter<signManageAdapter.ViewHolder>{
    private List<signInfo> data;
    private Context mContext;
    private Activity activity;
    private View view;
    private RecyclerView recyclerView;
    private signManageAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    public signManageAdapter(Context context, Activity activity, List<signInfo> data){
        this.mContext = context;
        this.activity = activity;
        this.data = data;
    }

    @NonNull
    @Override
    public signManageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        recyclerView = (RecyclerView) parent;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sign_manage_list_item, parent, false);
        return new signManageAdapter.ViewHolder(view);
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(RecyclerView recyclerView, View view, int position);
    }
    public void setOnItemClickListener(signManageAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull signManageAdapter.ViewHolder holder, int position) {
        holder.cs_num.setText(data.get(position).getCourse());
        holder.c_num.setText(data.get(position).getClass_num() + "");
        holder.s_num.setText(data.get(position).getSid());
        holder.time.setText(data.get(position).getTime().toString().replaceAll("[Tt]", " "));
        holder.name.setText(data.get(position).getSname());
        if(data.get(position).getState() == 0){
            holder.state.setText("等待审核");
            holder.state.setTextColor(Color.RED);
        }else {
            holder.state.setText("已读");
            holder.state.setTextColor(Color.GREEN);
        }
        String url = "http://47.113.197.249:8888/static/images/" + data.get(position).getImage().get(0);
        holder.gridLayout.removeAllViews();
            ImageView imageView = new ImageView(mContext.getApplicationContext());
            RequestOptions options = new RequestOptions().disallowHardwareConfig();
            Glide.with(mContext.getApplicationContext())
                    .load(url)
                    .error(R.mipmap.no_picture)
                    .thumbnail(Glide.with(mContext).load(R.drawable.load))
                    .apply(options).into(imageView);
            imageShow(imageView, holder.gridLayout);
        holder.gridLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePreview
                        .getInstance()
                        .setContext(activity)
                        .setIndex(0)
                        .setImage(url)
                        .start();
            }
        });
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
        if(data != null)
            return data.size();
        else
            return 0;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{
        private ConstraintLayout item;
        private GridLayout gridLayout;
        private TextView c_num;
        private TextView s_num;
        private TextView cs_num;
        private TextView name;
        private TextView time;
        private TextView state;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            gridLayout = itemView.findViewById(R.id.img);
            c_num = itemView.findViewById(R.id.c_num);
            s_num = itemView.findViewById(R.id.s_num);
            cs_num = itemView.findViewById(R.id.cs_num);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            state = itemView.findViewById(R.id.state);
        }
    }

    private void imageShow(ImageView imageView, GridLayout gridLayout){
        imageView.setAdjustViewBounds(true);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        params.width = (metrics.widthPixels)/4 - CommonUtils.dp2px(mContext,5);
        imageView.setPadding(0,CommonUtils.dp2px(this.mContext,5),CommonUtils.dp2px(this.mContext,5),CommonUtils.dp2px(this.mContext,5));
        imageView.setMaxHeight(CommonUtils.dp2px(this.mContext,140));
        imageView.setLayoutParams(params);
        gridLayout.addView(imageView);
    }
}
