package com.example.studentsystem.teacherView.manage;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentsystem.R;
import com.example.studentsystem.adapter.studentManageAdapter;
import com.example.studentsystem.bean.studentInfo;
import com.example.studentsystem.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class studentManageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private studentManageAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_manage);
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
        String url = "http://47.113.197.249:8081/api/v2/teacher/getStudent";
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
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String dataJson = jsonResponse.getJSONArray("data").toString();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<studentInfo>>() {
                        }.getType();
                        List<studentInfo> data = gson.fromJson(dataJson, listType);
                        runOnUiThread(() -> {
                            initRecycleView(data);
                            Log.d("HttpPostExample", "POST request was successful.");
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(studentManageActivity.this, "error", Toast.LENGTH_SHORT).show();
                        Log.d("HttpPostExample", "POST request failed. Response Code: " + response.code());
                    });
                }
            }
        });
    }

    private void initRecycleView(List<studentInfo> data){
        Collections.reverse(data);
        adapter = new studentManageAdapter(this, this, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(studentManageActivity.this));
        recyclerView.setAdapter(adapter);
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
