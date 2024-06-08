package com.example.studentsystem.studentView.add;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.studentsystem.R;
import com.example.studentsystem.utils.CommonUtils;
import com.example.studentsystem.utils.HttpUtil;
import com.example.studentsystem.utils.ScreenSizeUtils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class signAddActivity extends AppCompatActivity {
    private LinearLayout shot;
    private RelativeLayout photoContent;
    private Uri imgUri;
    private Bitmap bmp;
    private String img;
    private Dialog bottomDialog;
    private final int REQUEST_CAMERA_IMAGE = 100;
    private final int REQUEST_ALBUM = 200;
    private static final int REQUEST_PERMISSIONS = 100;
    private EditText c_num, name;
    private Button upload;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_add);
        initComponent();
    }

    private void initComponent(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        c_num = findViewById(R.id.c_num);
        name = findViewById(R.id.name);
        shot = findViewById(R.id.shot);
        photoContent = findViewById(R.id.content_photo);
        shot.setOnClickListener(this::getImg);
        upload = findViewById(R.id.upload);
        upload.setOnClickListener(this::upData);
    }

    private boolean isChecked(){
        if(c_num.getText().toString().isEmpty()){
            Toast.makeText(signAddActivity.this, "请填写课程名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(name.getText().toString().isEmpty()){
            Toast.makeText(signAddActivity.this, "请填写学生名", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void upData(View v){
        if(!isChecked()){
            return;
        }
        if(bmp != null){
            img = bitmapToBase64(bmp);
        }else{
            Toast.makeText(signAddActivity.this, "请上传图片！", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = "http://47.113.197.249:8081/api/v2/sign/signup";
        JSONObject jsonInput = new JSONObject();
        try {
            jsonInput.put("base64", img);
            jsonInput.put("sname", name.getText());
            jsonInput.put("course", c_num.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(signAddActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(signAddActivity.this, "账号密码有误", Toast.LENGTH_SHORT).show();
                    Log.d("HttpPostExample", "POST request failed. Response Code: " + response.code());
                }
            }
        });
    }

    private void getImg(View v) {
        bottomDialog = new Dialog(this,R.style.BottomSheetDialogStyle);
        View view = View.inflate(this, R.layout.edit_dialog_bottom, null);
        bottomDialog.setContentView(view);
        bottomDialog.setCanceledOnTouchOutside(true);
        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(this).getScreenHeight() * 0.23f));
        Window dialogWindow = bottomDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(this).getScreenWidth() * 0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
        bottomDialog.show();
        Button shot = view.findViewById(R.id.shot);
        Button album = view.findViewById(R.id.album);
        Button cancel = view.findViewById(R.id.cancel);
        shot.setOnClickListener(this::shot);
        album.setOnClickListener(this::Album);
        cancel.setOnClickListener((view1)->{
            bottomDialog.cancel();
        });
    }

    private void shot(View v){
        if(bottomDialog != null)
            bottomDialog.cancel();
        if(checkPermission(signAddActivity.this)){
            File mPictureFile = new File(getCacheDir(), "AddTmp.png");
            imgUri = getUriForFile(mPictureFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);
            startActivityForResult(intent, REQUEST_CAMERA_IMAGE);
        }
    }

    private void Album(View v){
        if(bottomDialog != null)
            bottomDialog.cancel();
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_ALBUM);
    }

    private void changePhoto(Uri uri){
        ImageView imgView = new ImageView(this);
        try{
            bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            imgView.setImageBitmap(bmp);
        }catch (FileNotFoundException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            return;
        }
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(CommonUtils.dp2px(this,300), CommonUtils.dp2px(this,200));
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        params.topMargin = CommonUtils.dp2px(this,5);
        imgView.setLayoutParams(params);
        imgView.setOnClickListener((v)->{
            bottomDialog = new Dialog(this,R.style.BottomSheetDialogStyle);
            View view = View.inflate(this, R.layout.edit_dialog_bottom, null);
            bottomDialog.setContentView(view);
            bottomDialog.setCanceledOnTouchOutside(true);
            view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(this).getScreenHeight() * 0.23f));
            Window dialogWindow = bottomDialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (ScreenSizeUtils.getInstance(this).getScreenWidth() * 0.9f);
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            dialogWindow.setAttributes(lp);
            bottomDialog.show();
            Button shot = view.findViewById(R.id.shot);
            Button album = view.findViewById(R.id.album);
            Button cancel = view.findViewById(R.id.cancel);
            shot.setOnClickListener(this::shot);
            album.setOnClickListener(this::Album);
            cancel.setOnClickListener((view1)->{
                bottomDialog.cancel();
            });
        });
        photoContent.removeAllViews();
        photoContent.addView(imgView);
    }

    public static boolean checkPermission(Activity activity) {
        if (isPermissionGranted(activity)) {
            return true;
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST_PERMISSIONS);
            return false;
        }
    }

    public static boolean isPermissionGranted(Activity activity) {
        boolean cameraPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean storagePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean readStoragePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        Log.d("PermissionCheck", "Camera permission: " + cameraPermission + ", Write storage permission: " + storagePermission + ", Read storage permission: " + readStoragePermission);
        return cameraPermission && storagePermission && readStoragePermission;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            boolean cameraPermissionGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean writeStoragePermissionGranted = grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean readStoragePermissionGranted = grantResults.length > 2 && grantResults[2] == PackageManager.PERMISSION_GRANTED;
            Log.d("PermissionRequest", "Camera permission granted: " + cameraPermissionGranted + ", Write storage permission granted: " + writeStoragePermissionGranted + ", Read storage permission granted: " + readStoragePermissionGranted);

            if (cameraPermissionGranted && writeStoragePermissionGranted && readStoragePermissionGranted) {
                shot(null);
            } else {
                Toast.makeText(this, "Camera and storage permissions are required", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private Uri getUriForFile(File file) {
        return FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileProvider", file);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CAMERA_IMAGE){
            if(resultCode == RESULT_OK){
                changePhoto(imgUri);
            }else if(resultCode!= RESULT_CANCELED){
                Toast.makeText(this,"未知错误",Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == REQUEST_ALBUM){
            if(resultCode == RESULT_OK){
                Uri uri = data.getData();
                changePhoto(uri);
            }else if(resultCode!= RESULT_CANCELED){
                Toast.makeText(this,"未知错误",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
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
