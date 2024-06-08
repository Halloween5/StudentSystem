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
import com.example.studentsystem.adapter.signManageAdapter;
import com.example.studentsystem.bean.signInfo;
import com.example.studentsystem.studentView.signActivity;
import com.example.studentsystem.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class signManageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private signManageAdapter adapter;
    private List<signInfo> signList = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_manage);
        initComponent();
        getData("nn");
    }

    private void initComponent(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recyclerview);
    }

    private void getData(String c_num){
        String url = "http://47.113.197.249:8081/api/v2/sign/queryClass";
        JSONObject jsonInput = new JSONObject();
        try {
            jsonInput.put("course", c_num);
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
                    String responseBody = response.body().string();
                    signList = dataProcess(responseBody);
                    runOnUiThread(() -> {
                        if (signList != null) {
                            setupRecyclerView(signList);
                        } else {
                            Toast.makeText(signManageActivity.this, "数据处理错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.d("HttpPostExample", "POST request was successful.");
                } else {
                    runOnUiThread(() -> Toast.makeText(signManageActivity.this, "error", Toast.LENGTH_SHORT).show());
                    Log.d("HttpPostExample", "POST request failed. Response Code: " + response.code());
                }
            }
        });
    }

    private void setupRecyclerView(List<signInfo> data) {
        Collections.reverse(data);
        adapter = new signManageAdapter(this, this, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(signManageActivity.this));
        adapter.setOnItemClickListener(new signManageAdapter.OnRecyclerViewItemClickListener() {
            public void onItemClick(RecyclerView parent, View view, int position) {
                if (signList != null && signList.get(position).getState() == 1) {
                    SweetAlertDialog dialog = new SweetAlertDialog(signManageActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    dialog.setContentText("是否已读？");
                    dialog.setConfirmButton("确认", (v) -> {
                        signList.get(position).setState(1);
                        adapter.notifyDataSetChanged();
                        dialog.cancel();
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

    private List<signInfo> dataProcess(String responseBody) {
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONArray dataArray = jsonResponse.getJSONArray("data");
            List<signInfo> data = new ArrayList<>();
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject signObject = dataArray.getJSONObject(i);
                JSONArray imageArray = new JSONArray(signObject.getString("image"));
                List<String> images = new ArrayList<>();
                for (int j = 0; j < imageArray.length(); j++) {
                    images.add(imageArray.getString(j));
                }

                signInfo sign = new signInfo();
                sign.setSid(signObject.getString("sid"));
                sign.setSname(signObject.getString("sname"));
                sign.setCourse(signObject.getString("course"));
                sign.setImage(images);
                String timeString = signObject.getString("time");
                LocalDateTime time = LocalDateTime.parse(timeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                sign.setTime(time);
                sign.setState(signObject.getInt("state"));
                data.add(sign);
                return data;
            }
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

    @Override
    public void onResume() {
        super.onResume();
        getData("nn");
    }
}
