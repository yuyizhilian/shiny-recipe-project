package com.android.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.recipe.activity.PrimaryActivity;
import com.android.recipe.activity.RegisterActivity;
import com.android.recipe.activity.ResetActivity;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private CheckBox remember;

    private EditText account;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this, "d8c5c191055ce4708b5a305abc10e4a6");

        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        account=findViewById(R.id.account);
        password=findViewById(R.id.password);
        Button login = findViewById(R.id.login);
        Button secret=findViewById(R.id.secret);
        //可以选择是否记住密码
        remember=findViewById(R.id.remember);
        Button register = findViewById(R.id.register);
        boolean isRemember=preferences.getBoolean("remember_password",false);
        if(isRemember){
            String accountName=preferences.getString("account","");
            String passwordNum=preferences.getString("password","");
            account.setText(accountName);
            password.setText(passwordNum);
            remember.setChecked(true);
        }

        register.setOnClickListener(v -> {
            Intent intent=new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        //登录点击监听事件
        login.setOnClickListener(v -> {
            String accountName=account.getText().toString();
            String passwordNum=password.getText().toString();

            BmobUser bu2 = new BmobUser();
            bu2.setUsername(accountName);
            bu2.setPassword(passwordNum);

            //登录判断逻辑
            bu2.login(new SaveListener<BmobUser>() {
                @Override
                public void done(BmobUser bmobUser, BmobException e) {
                    if(e==null){
                        editor=preferences.edit();
                        if(remember.isChecked()){
                            editor.putBoolean("remember_password",true);
                            editor.putString("account",accountName);
                            editor.putString("password",passwordNum);
                        }else{
                            editor.clear();
                        }
                        editor.apply();
                        Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MainActivity.this, PrimaryActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(MainActivity.this,"登录失败,账号或者密码错误",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        secret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ResetActivity.class);
                startActivity(intent);
            }
        });
    }
}