package com.example.studentsystem.teacherView.manage;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentsystem.R;
import com.example.studentsystem.adapter.leaveManageAdapter;
import com.example.studentsystem.bean.leaveInfo;
import com.example.studentsystem.utils.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class leaveManageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private leaveManageAdapter adapter;
    private List<leaveInfo> leaveList = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_manage);
        initComponent();
        getData();
    }

    private void initComponent(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recyclerview);
    }

    private void getData(){
        String url = "http://47.113.197.249:8081/api/v2/leave/queryClass";
        JSONObject jsonInput = new JSONObject();
        HttpUtil.getInstance().post(url, jsonInput.toString(), new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    leaveList = dataProcess(responseBody);
                    runOnUiThread(() ->{
                        if (leaveList != null && leaveList.size() != 0) {
                            setupRecyclerView(leaveList);
                        } else {
                            Toast.makeText(leaveManageActivity.this, "暂时没有请假记录", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.d("HttpPostExample", "POST request was successful.");
                } else {
                    runOnUiThread(() -> Toast.makeText(leaveManageActivity.this, "暂时没有请假记录", Toast.LENGTH_SHORT).show());
                    Log.d("HttpPostExample", "POST request failed. Response Code: " + response.code());
                }
            }
        });
    }

    private void setupRecyclerView(List<leaveInfo> data) {
        Collections.reverse(data);
        adapter = new leaveManageAdapter(this, this, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(leaveManageActivity.this));
        adapter.setOnItemClickListener(new leaveManageAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View view, int position) {
                if (leaveList != null && leaveList.get(position).getState() == 0) {
                    SweetAlertDialog dialog = new SweetAlertDialog(leaveManageActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setContentText("是否已读？");
                    dialog.setConfirmButton("确认", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            leaveList.get(position).setState(1);
                            haveRead(position);
                            adapter.notifyDataSetChanged();
                            dialog.cancel();
                        }
                    });
                    dialog.setCancelButton("取消", (v) -> {
                        dialog.cancel();
                    });
                    dialog.show();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void haveRead(int position){
        String url = "http://47.113.197.249:8081/api/v2/leave/read";
        JSONObject jsonInput = new JSONObject();
        try {
            jsonInput.put("sid", leaveList.get(position).getId());
            jsonInput.put("time", leaveList.get(position).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpUtil.getInstance().post(url, jsonInput.toString(), new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() ->{ Toast.makeText(leaveManageActivity.this, "已经审批", Toast.LENGTH_SHORT).show();});
                    Log.d("HttpPostExample", "POST request was successful.");
                } else {
                    runOnUiThread(() -> Toast.makeText(leaveManageActivity.this, "error", Toast.LENGTH_SHORT).show());
                    Log.d("HttpPostExample", "POST request failed. Response Code: " + response.code());
                }
            }
        });
    }

    public List<leaveInfo> dataProcess(String responseBody) {
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONArray dataArray = jsonResponse.getJSONArray("data");
            List<leaveInfo> data = new ArrayList<>();
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject leaveObject = dataArray.getJSONObject(i);

                leaveInfo leave = new leaveInfo();
                leave.setId(leaveObject.getString("id"));
                leave.setClassNum(leaveObject.getInt("classNum"));
                leave.setName(leaveObject.getString("name"));
                leave.setReason(leaveObject.getString("reason"));

                String timeString = leaveObject.getString("time");
                LocalDateTime time = LocalDateTime.parse(timeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                leave.setTime(time);

                String fromString = leaveObject.getString("from");
                LocalDate from = LocalDate.parse(fromString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                leave.setFrom(from);

                String toString = leaveObject.getString("to");
                LocalDate to = LocalDate.parse(toString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                leave.setTo(to);

                leave.setState(leaveObject.getInt("state"));

                data.add(leave);
            }
            return data;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
