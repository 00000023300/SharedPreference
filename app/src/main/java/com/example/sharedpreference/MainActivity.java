package com.example.sharedpreference;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView view;
    private Button cun,du;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.view);
        du = findViewById(R.id.du);
        cun = findViewById(R.id.cun);

        du.setOnClickListener(this);
        cun.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cun:
                saveToSD("abc.jpg");
                break;
            case R.id.du:
//                readFromSD("abc.jpg");
                break;
        }
    }
//    SD卡：外部的公有文件夹，需要申请运行时权限

    private void saveToSD(String filename) {
//        1.申请写SD的权限，要求android的版本大于6.0（Build.VERSION.M)
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },1);
                return;
            }
        }

//        写SD卡的步骤
//        1.获取SD的Download目录，创建需要存储的文件
        String path = Environment.getExternalStoragePublicDirectory("").getPath()+
        File.separator + Environment.DIRECTORY_PICTURES;
        File file = new File(path,filename);
        try {
            if(file.createNewFile()){
    //            2.获取ImageView的Bitmap图片对象
                BitmapDrawable drawable = (BitmapDrawable) view.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

//                3.将Bitmap对象写入SD卡
                FileOutputStream outputStream = new FileOutputStream(file);

//                带缓冲
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);

//                4.关闭请求的资源
                outputStream.flush();
                outputStream.close();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length==0||grantResults[0]!=PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"权限申请被拒绝",Toast.LENGTH_SHORT).show();
            return;

        }
        switch (requestCode){
            case 1:
                saveToSD("abc.jpg");
                break;


        }
    }
}
