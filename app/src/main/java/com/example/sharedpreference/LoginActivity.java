package com.example.sharedpreference;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LoginActivity extends AppCompatActivity {
//    1.定义控件对象
private EditText etusername,etpassword;
private CheckBox remember;
private Button login;
private String fileName = "login.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        2.获取控件对象
        login = findViewById(R.id.login);
        etusername = findViewById(R.id.username);
        etpassword = findViewById(R.id.password);
        remember = findViewById(R.id.remember);

//        3.获取存储的用户信息，若有侧写入
//        String username = readPref();
        String username = readDataInternal(fileName);
//        String username = saveDataPrivate(fileName);
        if(username!=null){
            etusername.setText(username);

        }

//        4.设置登录按钮的点击监听事件的监听器
 //        5.处理点击事件
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//        5.1获取输入的用户名和密码
               String username = etusername.getText().toString();
               String password = etpassword.getText().toString();
            }
        });

//        5.2判断“记住用户名”是否勾选，若已选则存储用户名，未选则清空存储的用户名
        if(remember.isChecked()){
            savePref(username);
            saveDataInternal(fileName,username);
            readDataInternal(fileName);
            saveDataPrivate(fileName,username);

        }else{
            clearPref();
            deleteDataFile(fileName);
            deleteFilePrivate(fileName);

        }
//        5.3判断输入的用户名，密码是否正确，正确则跳转MainActivity
        if("kong".equals(username)){
            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("username",username);
            startActivity(intent);

        }else {
            Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
            etusername.setFocusable(true);
        }

    }
    // 删除外部私有存储的文件
    private void deleteFilePrivate(String fileName) {
        File file = new File(getExternalFilesDir(""), fileName);
        if (file.isFile()) {
            if (file.delete()) {
                Toast.makeText(this, "删除外部公有文件成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "删除外部公有文件失败", Toast.LENGTH_SHORT).show();
            }
        }

    }
    // 删除内部存储文件
    private void deleteDataFile(String fileName) {
        if (deleteFile(fileName)) {
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveDataPrivate(String fileName, String username) {
//        内部存储目录:storage/emulated/0/Android/data/包名/files
        File file = new File(getExternalFilesDir(""),fileName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(username);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


//    2.内部存储
//    保存

    private void saveDataInternal(String fileName, String username) {
//        内部存储目录：data/data/包名/files/

//            1.打开文件输出流

        try {
            FileOutputStream out = openFileOutput(fileName,MODE_PRIVATE);
 //            2.创建BufferedWriter对象
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
//            3.写入数据
            writer.write(username);
//            4.关闭输出流
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
//读取
    private String readDataInternal(String fileName) {
        String data =null;
        try {
            FileInputStream in = openFileInput(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            data=reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    private void clearPref() {
        SharedPreferences.Editor editor = getSharedPreferences("userInfo",MODE_PRIVATE).edit();
        editor.clear().apply();

    }

    private void savePref(String username) {
        SharedPreferences.Editor editor = getSharedPreferences("userInfo",MODE_PRIVATE).edit();
        editor.putString("username",username);
        editor.apply();
    }

    private String readPref() {
        SharedPreferences sp =getSharedPreferences("userInfo",MODE_PRIVATE);
        return sp.getString("username","");
    }
}
