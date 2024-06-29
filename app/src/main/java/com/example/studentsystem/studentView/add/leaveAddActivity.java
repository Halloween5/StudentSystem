package com.example.studentsystem.studentView.add;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentsystem.R;
import com.example.studentsystem.utils.HttpUtil;
import com.example.studentsystem.utils.dateUtils;
import com.loper7.date_time_picker.dialog.CardDatePickerDialog;

import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class leaveAddActivity extends AppCompatActivity {
    private EditText name, cause;
    private TextView f_time, t_time;
    private LocalDate fromTime, toTime;
    private Button upload;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_add);
        initComponent();
    }

    private void initComponent(){
        name = findViewById(R.id.name);
        cause = findViewById(R.id.cause_content);
        f_time = findViewById(R.id.f_time);
        t_time = findViewById(R.id.t_time);
        f_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTime(f_time, true);
            }
        });
        t_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTime(t_time, false);
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        upload = findViewById(R.id.upload);
        upload.setOnClickListener(this::upData);
    }

    private void getTime(TextView t, boolean isFromTime) {
        List<Integer> lst = new ArrayList<>();
        lst.add(0);
        lst.add(1);
        lst.add(2);
        LocalDate min = LocalDate.parse("2020-01-01");
        CardDatePickerDialog.Builder builder = new CardDatePickerDialog.Builder(this)
                .setTitle("请选择日期")
                .setMinTime(dateUtils.dayStartMillis(min))
                .setThemeColor(getColor(R.color.black))
                .setBackGroundModel(CardDatePickerDialog.STACK)
                .setWrapSelectorWheel(false)
                .showBackNow(false)
                .setDisplayType(lst)
                .setOnChoose("确定", aLong -> {
                    LocalDate date = dateUtils.Millis2Date(aLong);
                    String dateStr = date.format(formatter);
                    t.setText(dateStr);
                    if (isFromTime) {
                        fromTime = date;
                    } else {
                        toTime = date;
                    }
                    return null;
                });

        if (isFromTime && toTime != null) {
            builder.setMaxTime(dateUtils.dayStartMillis(toTime));
        } else if (!isFromTime && fromTime != null) {
            builder.setMinTime(dateUtils.dayStartMillis(fromTime));
        }
        builder.build().show();
    }


        private boolean isChecked(){
        if(name.getText().toString().isEmpty()){
            Toast.makeText(leaveAddActivity.this, "请填写学生名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(cause.getText().toString().isEmpty()){
            Toast.makeText(leaveAddActivity.this, "请填写请假原因", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(f_time.getText().toString().isEmpty()){
            Toast.makeText(leaveAddActivity.this, "请填写起始时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(t_time.getText().toString().isEmpty()){
            Toast.makeText(leaveAddActivity.this, "请填写结束时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void upData(View v){
        if(!isChecked()){
            return;
        }
        String url = "http://47.113.197.249:8081/api/v2/leave/add";
        JSONObject jsonInput = new JSONObject();
        try {
            jsonInput.put("name", name.getText());
            jsonInput.put("from", f_time.getText());
            jsonInput.put("to", t_time.getText());
            jsonInput.put("reason", cause.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(leaveAddActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
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
                    runOnUiThread(() -> {
                        Toast.makeText(leaveAddActivity.this, "error", Toast.LENGTH_SHORT).show();
                        Log.d("HttpPostExample", "POST request failed. Response Code: " + response.code());
                    });
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
            case R.id.add:
                Intent intent = new Intent(leaveAddActivity.this, signAddActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
