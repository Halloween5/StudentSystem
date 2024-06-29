package com.example.studentsystem.teacherView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentsystem.R;
import com.example.studentsystem.teacherView.manage.courseManageActivity;
import com.example.studentsystem.teacherView.manage.leaveManageActivity;
import com.example.studentsystem.teacherView.manage.signManageActivity;
import com.example.studentsystem.teacherView.manage.studentManageActivity;

public class teacherMainActivity extends AppCompatActivity {
    private ImageView bt1, bt2, bt3, bt4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);
        initComponent();
    }

    private void initComponent(){
        bt1 = findViewById(R.id.coursemanage);
        bt2 = findViewById(R.id.studentmanage);
        bt3 = findViewById(R.id.signmanage);
        bt4 = findViewById(R.id.leavemanage);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(teacherMainActivity.this, courseManageActivity.class);
                startActivity(intent);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(teacherMainActivity.this, studentManageActivity.class);
                startActivity(intent);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(teacherMainActivity.this, signManageActivity.class);
                startActivity(intent);
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(teacherMainActivity.this, leaveManageActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mine_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mine:
                Intent intent = new Intent(teacherMainActivity.this, mineActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {

    }
}
