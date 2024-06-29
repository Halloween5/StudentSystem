package com.example.studentsystem.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentsystem.R;
import com.example.studentsystem.studentView.studentMainActivity;
import com.example.studentsystem.teacherView.teacherMainActivity;
import com.example.studentsystem.utils.HttpUtil;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class initActivity extends AppCompatActivity {
    private EditText uid, num;
    private Button summit;
    private int type;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        initComponent();
    }

    private void initComponent(){
        uid = findViewById(R.id.uid);
        num = findViewById(R.id.number);
        summit = findViewById(R.id.summit);
        summit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upData();
            }
        });
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 1);
    }

    private boolean isChecked(){
        if(uid.getText().toString().isEmpty()){
            Toast.makeText(initActivity.this, "请填写姓名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(num.getText().toString().isEmpty()){
            Toast.makeText(initActivity.this, "请填写班级编号", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void upData(){
        if(!isChecked()){
            return;
        }
        String url = "http://47.113.197.249:8081/api/v2/init";
        JSONObject jsonInput = new JSONObject();
        try {
            int n = Integer.parseInt(num.getText().toString());
            jsonInput.put("name", uid.getText());
            jsonInput.put("classNum", n);
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
                    runOnUiThread(() -> Toast.makeText(initActivity.this, "提交成功", Toast.LENGTH_SHORT).show());
                    Intent intent;
                    if(type == 1) {
                        intent = new Intent(initActivity.this, teacherMainActivity.class);
                    }else {
                        intent = new Intent(initActivity.this, studentMainActivity.class);
                    }
                    startActivity(intent);
                    Log.d("HttpPostExample", "POST request was successful.");
                } else {
                    runOnUiThread(() -> Toast.makeText(initActivity.this, "error", Toast.LENGTH_SHORT).show());
                    Log.d("HttpPostExample", "POST request failed. Response Code: " + response.code());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
