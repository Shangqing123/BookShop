package com.example.bookshop.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.bookshop.R;
import com.example.bookshop.utils.SetUtils;

/**
 * 管理员页面
 */
public class ManagerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        //为书籍管理和退出添加监听按钮
        findViewById(R.id.rtl_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),BookManagerActivity.class));
            }
        });
        findViewById(R.id.rtl_exist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ManagerActivity.this);
                builder.setMessage("确认要退出？");
                builder.setTitle("提示");
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {   @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();}
                });
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {   @Override
                public void onClick(DialogInterface dialog, int which) {//取消登录状态
                    SetUtils.saveIsLogin(getApplicationContext(),false);
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                    dialog.dismiss();
                }
                });
                builder.create().show();
            }
        });

    }
}
