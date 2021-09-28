package com.android.recipe.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.recipe.MainActivity;
import com.android.recipe.R;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class ResetActivity extends AppCompatActivity {

    private EditText account;
    private EditText password;
    private EditText sure;
    private EditText proof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        account=findViewById(R.id.account);
        password=findViewById(R.id.password);
        proof=findViewById(R.id.proof);
        sure=findViewById(R.id.sure);
        Button getProof=findViewById(R.id.getProof);
        Button reset=findViewById(R.id.reset);
        getProof.setOnClickListener(v -> {

            String phone=account.getText().toString();
            BmobSMS.requestSMSCode(phone, "", new QueryListener<Integer>() {
                @Override
                public void done(Integer smsId, BmobException e) {
                    if (e == null) {
                        Toast.makeText(ResetActivity.this,"发送验证码成功，短信ID：" + smsId + "\n",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ResetActivity.this,"发送验证码失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        reset.setOnClickListener(v -> {

            String passwordNum=password.getText().toString();
            String pwd=sure.getText().toString();
            String code=proof.getText().toString();
            if(!passwordNum.equals(pwd)){
                Toast.makeText(ResetActivity.this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
            }
            BmobUser.resetPasswordBySMSCode(code, passwordNum, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Toast.makeText(ResetActivity.this,"重置成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ResetActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ResetActivity.this,"重置失败：" + e.getErrorCode() + "-" + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

    }
}