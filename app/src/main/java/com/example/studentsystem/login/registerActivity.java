package com.example.studentsystem.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentsystem.MainActivity;
import com.example.studentsystem.R;
import com.example.studentsystem.utils.HttpUtil;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class registerActivity extends AppCompatActivity {
    private CheckBox admin, student;
    private int type = 1;
    private EditText uid, password;
    private Button register;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponent();
    }

    private void initComponent(){
        admin = findViewById(R.id.admin);
        student = findViewById(R.id.student);
        uid = findViewById(R.id.uid);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upData();
            }
        });
        IDSelected();
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
                }
            }
        });
    }

    private boolean isChecked(){
        if(uid.getText().toString().isEmpty()){
            Toast.makeText(registerActivity.this, "请填写账号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.getText().toString().isEmpty()){
            Toast.makeText(registerActivity.this, "请填写密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(uid.getText().toString().length() != 10){
            Toast.makeText(registerActivity.this, "账号长度固定为10位", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void upData(){
        if(!isChecked()){
            return;
        }
        String url = "http://47.113.197.249:8081/api/v2/register";
        JSONObject jsonInput = new JSONObject();
        try {
            jsonInput.put("id", uid.getText());
            jsonInput.put("password", password.getText());
            jsonInput.put("type", type);
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
                    runOnUiThread(() -> Toast.makeText(registerActivity.this, "注册成功", Toast.LENGTH_SHORT).show());
                    Intent intent;
                    intent = new Intent(registerActivity.this, initActivity.class);
                    intent.putExtra("type", type);
                    startActivity(intent);
                    Log.d("HttpPostExample", "POST request was successful.");
                } else {
                    runOnUiThread(() -> Toast.makeText(registerActivity.this, "error", Toast.LENGTH_SHORT).show());
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
