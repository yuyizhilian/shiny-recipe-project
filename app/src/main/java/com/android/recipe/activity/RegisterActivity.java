package com.android.recipe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.recipe.MainActivity;
import com.android.recipe.R;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegisterActivity extends AppCompatActivity {

    private EditText account;
    private EditText password;
    private EditText sure;
    private EditText proof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button register = findViewById(R.id.register);
        account=findViewById(R.id.account);
        password=findViewById(R.id.password);
        proof=findViewById(R.id.proof);
        sure=findViewById(R.id.sure);
        Button getProof = findViewById(R.id.getProof);

        getProof.setOnClickListener(v -> {

            String phone=account.getText().toString();
            BmobSMS.requestSMSCode(phone, "", new QueryListener<Integer>() {
                @Override
                public void done(Integer smsId, BmobException e) {
                    if (e == null) {
                        Toast.makeText(RegisterActivity.this,"发送验证码成功，短信ID：" + smsId + "\n",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this,"发送验证码失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n",Toast.LENGTH_SHORT).show();
                    }
                   }
            });
        });

        //注册界面
        register.setOnClickListener(v -> {
            String accountName=account.getText().toString();
            String passwordNum=password.getText().toString();
            String code=proof.getText().toString();
            String pwd=sure.getText().toString();

            if(!passwordNum.equals(pwd)){
                Toast.makeText(RegisterActivity.this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
            }

            BmobSMS.verifySmsCode(accountName, code, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        BmobUser bu = new BmobUser();
                        bu.setUsername(accountName);
                        bu.setPassword(passwordNum);
                        //写入bmob数据库
                        bu.signUp(new SaveListener<BmobUser>() {
                            @Override
                            public void done(BmobUser bmobUser, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(RegisterActivity.this, "注册失败，该用户名已被占用，请修改用户名再注册", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this,"验证码验证失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }

}
