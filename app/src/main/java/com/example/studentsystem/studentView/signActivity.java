package com.example.studentsystem.studentView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentsystem.R;
import com.example.studentsystem.adapter.signManageAdapter;
import com.example.studentsystem.bean.signInfo;
import com.example.studentsystem.studentView.add.signAddActivity;
import com.example.studentsystem.teacherView.manage.signManageActivity;
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

public class signActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<signInfo> signList = null;
    private signManageAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
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
        String url = "http://47.113.197.249:8081/api/v2/sign/get";
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
                    signList = dataProcess(responseBody);
                    runOnUiThread(() ->{
                        if (signList != null && signList.size() != 0) {
                            setupRecyclerView(signList);
                        } else {
                            Toast.makeText(signActivity.this, "该课程没有签到记录", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.d("HttpPostExample", "POST request was successful.");
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(signActivity.this, "error", Toast.LENGTH_SHORT).show();
                        Log.d("HttpPostExample", "POST request failed. Response Code: " + response.code());
                    });
                }
            }
        });
    }

    private void setupRecyclerView(List<signInfo> data) {
        Collections.reverse(data);
        adapter = new signManageAdapter(this, this, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(signActivity.this));
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
                sign.setClass_num(signObject.getInt("classNum"));
                sign.setImage(images);
                String timeString = signObject.getString("time");
                LocalDateTime time = LocalDateTime.parse(timeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                sign.setTime(time);
                sign.setState(signObject.getInt("state"));
                data.add(sign);
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
                Intent intent = new Intent(signActivity.this, signAddActivity.class);
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
