package com.example.studentsystem.teacherView.manage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentsystem.R;
import com.example.studentsystem.adapter.classManageAdapter;
import com.example.studentsystem.bean.classInfo;
import com.example.studentsystem.teacherView.manageAdd.classManageAddActivity;
import com.example.studentsystem.teacherView.manageAdd.classManageModifyActivity;

import java.util.List;


public class classManageActivity extends AppCompatActivity {
    private RecyclerView recyclerview;
    private classManageAdapter adapter;
    private List<classInfo> data;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_manage);
        initComponent();
        //upData(data);
    }

    private void initComponent(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        recyclerview = findViewById(R.id.recyclerview);
    }

    private void upData(List<classInfo> data){
        adapter = new classManageAdapter(this, data);
        recyclerview.setLayoutManager(new LinearLayoutManager(classManageActivity.this));
        adapter.setOnItemClickListener(new classManageAdapter.OnRecyclerViewItemClickListener(){
            public void onItemClick(RecyclerView parent, View view, int position){
                Intent intent = new Intent(classManageActivity.this, classManageModifyActivity.class);
                intent.putExtra("data", data.get(position));
                startActivity(intent);
            }
        });
        recyclerview.setAdapter(adapter);
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
                Intent intent = new Intent(classManageActivity.this, classManageAddActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
