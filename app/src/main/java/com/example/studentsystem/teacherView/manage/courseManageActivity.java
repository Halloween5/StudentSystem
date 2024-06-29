package com.example.studentsystem.teacherView.manage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentsystem.R;
import com.example.studentsystem.adapter.courseManageAdapter;
import com.example.studentsystem.bean.courseInfo;
import com.example.studentsystem.teacherView.manageAdd.courseManageAddActivity;
import com.example.studentsystem.utils.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class courseManageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private courseManageAdapter adapter;
    private List<courseInfo> courseList = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_manage);
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
        String url = "http://47.113.197.249:8081/api/v2/course/get";
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
                    courseList = dataProcess(responseBody);
                    runOnUiThread(() ->{
                        if (courseList != null && courseList.size() != 0) {
                            setupRecyclerView(courseList);
                        }
                    });
                    Log.d("HttpPostExample", "POST request was successful.");
                } else {
                    runOnUiThread(() -> Toast.makeText(courseManageActivity.this, "error", Toast.LENGTH_SHORT).show());
                    Log.d("HttpPostExample", "POST request failed. Response Code: " + response.code());
                }
            }
        });
    }

    private void setupRecyclerView(List<courseInfo> data) {
        Collections.reverse(data);
        adapter = new courseManageAdapter(this, this, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(courseManageActivity.this));
        recyclerView.setAdapter(adapter);
    }

    private List<courseInfo> dataProcess(String responseBody) {
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONArray dataArray = jsonResponse.getJSONArray("data");
            List<courseInfo> data = new ArrayList<>();
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject courseObject = dataArray.getJSONObject(i);
                courseInfo course = new courseInfo();
                course.setTid(courseObject.getString("tid"));
                course.setTname(courseObject.getString("tname"));
                course.setCourse(courseObject.getString("course"));
                course.setClassNum(courseObject.getInt("classNum"));
                String timeString = courseObject.getString("time");
                LocalDateTime time = LocalDateTime.parse(timeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                course.setTime(time);
                data.add(course);
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
                Intent intent = new Intent(courseManageActivity.this, courseManageAddActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    protected void onResume() {
        super.onResume();
        getData();
    }
}
