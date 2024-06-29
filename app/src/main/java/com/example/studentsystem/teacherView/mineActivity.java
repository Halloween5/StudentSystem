package com.example.studentsystem.teacherView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentsystem.R;
import com.example.studentsystem.bean.signInfo;
import com.example.studentsystem.studentView.signActivity;
import com.example.studentsystem.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class mineActivity extends AppCompatActivity {
    private TextView name, c_num;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        initComponent();
        getData();
    }

    private void initComponent(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        name = findViewById(R.id.name);
        c_num = findViewById(R.id.c_num);
    }

    private void getData() {
        String url = "http://47.113.197.249:8081/api/v2/teacher/getMine";
        JSONObject jsonInput = new JSONObject();
        HttpUtil.getInstance().post(url, jsonInput.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONObject dataJson = jsonResponse.getJSONObject("data");
                        String t_name = dataJson.getString("name");
                        int t_classNum = dataJson.getInt("classNum");
                        runOnUiThread(() -> {
                            name.setText(t_name);
                            c_num.setText(String.valueOf(t_classNum));
                        });
                        Log.d("HttpPostExample", "POST request was successful.");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(mineActivity.this, "error", Toast.LENGTH_SHORT).show();
                    });
                    Log.d("HttpPostExample", "POST request failed. Response Code: " + response.code());
                }
            }
        });
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
