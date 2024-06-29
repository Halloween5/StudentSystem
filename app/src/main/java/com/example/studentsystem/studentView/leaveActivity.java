package com.example.studentsystem.studentView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentsystem.R;
import com.example.studentsystem.adapter.leaveManageAdapter;
import com.example.studentsystem.adapter.signManageAdapter;
import com.example.studentsystem.bean.leaveInfo;
import com.example.studentsystem.bean.signInfo;
import com.example.studentsystem.studentView.add.leaveAddActivity;
import com.example.studentsystem.teacherView.manage.leaveManageActivity;
import com.example.studentsystem.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
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

public class leaveActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<leaveInfo> leaveList = null;
    private leaveManageAdapter adapter;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        initComponent();
        getData();
    }

    private void initComponent(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recyclerview);
    }

    private void getData() {
        String url = "http://47.113.197.249:8081/api/v2/leave/get";
        JSONObject jsonInput = new JSONObject();
        HttpUtil.getInstance().post(url, jsonInput.toString(), new Callback() {
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
                            Toast.makeText(leaveActivity.this, "暂时没有请假记录", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.d("HttpPostExample", "POST request was successful.");
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(leaveActivity.this, "error", Toast.LENGTH_SHORT).show();
                        Log.d("HttpPostExample", "POST request failed. Response Code: " + response.code());
                    });
                }
            }
        });
    }

    private void setupRecyclerView(List<leaveInfo> data) {
        Collections.reverse(data);
        adapter = new leaveManageAdapter(this, this, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(leaveActivity.this));
        recyclerView.setAdapter(adapter);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.add:
                Intent intent = new Intent(leaveActivity.this, leaveAddActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
