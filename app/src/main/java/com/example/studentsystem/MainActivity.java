package com.example.studentsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentsystem.login.registerActivity;
import com.example.studentsystem.studentView.studentMainActivity;
import com.example.studentsystem.teacherView.teacherMainActivity;
import com.example.studentsystem.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private CheckBox admin, student;
    private int type = 1;
    private EditText uid, pwd;
    private Button enterButton;
    private ImageView forDisplay;
    private TextView register;
    private int[] image = {R.mipmap.teacher,R.mipmap.student};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        admin = findViewById(R.id.admin);
        student = findViewById(R.id.student);
        uid = findViewById(R.id.uid);
        pwd = findViewById(R.id.password);
        register = findViewById(R.id.register);
        enterButton = findViewById(R.id.enter);
        forDisplay = findViewById(R.id.image);
        forDisplay.setImageResource(image[0]);
        IDSelected();
        enterMain();
    }

    private void IDSelected(){
        admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(student.isChecked())
                        student.setChecked(false);
                }else{
                    if(!student.isChecked())
                        student.setChecked(true);
                        type = 0;
                        forDisplay.setImageResource(image[1]);
                }
            }
        });
        student.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(admin.isChecked())
                        admin.setChecked(false);
                }else{
                    if(!admin.isChecked())
                        admin.setChecked(true);
                        type = 1;
                        forDisplay.setImageResource(image[0]);
                }
            }
        });
    }

    private void enterMain(){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(MainActivity.this, registerActivity.class);
                startActivity(intent);
            }
        });
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isChecked()){
                    login(uid.getText().toString(), pwd.getText().toString(), type);
                }
            }
        });
    }

    private boolean isChecked(){
        if(uid.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this, "请填写用户名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pwd.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this, "请填写密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void login(String id, String password, int type){
        String url = "http://47.113.197.249:8081/api/v2/login";
        JSONObject jsonInput = new JSONObject();
        try {
            jsonInput.put("id", id);
            jsonInput.put("password", password);
            jsonInput.put("type", type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpUtil.getInstance().loginPost(url, jsonInput.toString(), new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Gson gson = new Gson();
                    JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
                    if (jsonResponse.get("data").toString().equals("-1")) {
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "账号密码有误", Toast.LENGTH_SHORT).show());
                    }else {
                        String data = jsonResponse.get("data").getAsString();
                        Intent intent;
                        if (data.equals("0")) {
                            intent = new Intent(MainActivity.this, studentMainActivity.class);
                        } else {
                            intent = new Intent(MainActivity.this, teacherMainActivity.class);
                        }
                        startActivity(intent);
                        Log.d("HttpPostExample", "POST request was successful.");
                    }
                } else {
                    Toast.makeText(MainActivity.this, "账号密码有误", Toast.LENGTH_SHORT).show();
                    Log.d("HttpPostExample", "POST request failed. Response Code: " + response.code());
                }
            }
        });
    }
}