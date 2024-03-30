package com.example.bookshop.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.bookshop.R;


/**
 * 下单
 */
//定义了三个编辑框的字段名mEtName、mEtPrice、mEtAuth；
// 创建onCreate方法使界面显示布局文件activity_add_book。
// 定义三个方法， intview();、intidate();、intivetn();
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

    }
    public void back(View view) {
        finish();
    }


}
