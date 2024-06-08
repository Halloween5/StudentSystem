package com.example.studentsystem.studentView;

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
import com.example.studentsystem.adapter.signManageAdapter;
import com.example.studentsystem.bean.signInfo;
import com.example.studentsystem.studentView.add.signAddActivity;
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

public class signActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private signManageAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        initComponent();
        //getData("nn");
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
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String dataJson = jsonResponse.getJSONArray("data").toString();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<signInfo>>() {
                        }.getType();
                        List<signInfo> data = gson.fromJson(dataJson, listType);
                        initRecycleView(data);
                        Log.d("HttpPostExample", "POST request was successful.");
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(signActivity.this, "error", Toast.LENGTH_SHORT).show();
                    Log.d("HttpPostExample", "POST request failed. Response Code: " + response.code());
                }
            }
        });
    }

    private void initRecycleView(List<signInfo> data){
        Collections.reverse(data);
        adapter = new signManageAdapter(this, this, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(signActivity.this));
        recyclerView.setAdapter(adapter);
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
}
