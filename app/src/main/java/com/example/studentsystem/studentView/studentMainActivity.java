package com.example.studentsystem.studentView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentsystem.R;
import com.example.studentsystem.teacherView.manage.classManageActivity;
import com.example.studentsystem.teacherView.teacherMainActivity;

public class studentMainActivity extends AppCompatActivity {
    private Button leave, sign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        initComponent();
    }

   private void initComponent(){
        leave = findViewById(R.id.leave);
        sign = findViewById(R.id.sign);
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(studentMainActivity.this, leaveActivity.class);
                startActivity(intent);
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(studentMainActivity.this, signActivity.class);
                startActivity(intent);
            }
        });
   }
}
