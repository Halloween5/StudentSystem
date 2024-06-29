package com.example.studentsystem.teacherView.manageAdd;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentsystem.R;
import com.example.studentsystem.studentView.add.signAddActivity;
import com.example.studentsystem.utils.HttpUtil;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class courseManageAddActivity extends AppCompatActivity {
    private EditText c_name, t_name;
    private Button upload;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_manage_add);
        initComponent();
    }

    private void initComponent(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        c_name = findViewById(R.id.c_name);
        t_name = findViewById(R.id.t_name);
        upload = findViewById(R.id.upload);
        upload.setOnClickListener(this::upData);
    }

    private boolean isChecked(){
        if(c_name.getText().toString().isEmpty()){
            Toast.makeText(courseManageAddActivity.this, "请填写课程名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(t_name.getText().toString().isEmpty()){
            Toast.makeText(courseManageAddActivity.this, "请填写教师姓名", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void upData(View v){
        if(!isChecked()){
            return;
        }
        String url = "http://47.113.197.249:8081/api/v2/course/add";
        JSONObject jsonInput = new JSONObject();
        try {
            jsonInput.put("name", t_name.getText());
            jsonInput.put("course", c_name.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(courseManageAddActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
        HttpUtil.getInstance().post(url, jsonInput.toString(), new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    finish();
                    Log.d("HttpPostExample", "POST request was successful.");
                } else {
                    Toast.makeText(courseManageAddActivity.this, "error", Toast.LENGTH_SHORT).show();
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
