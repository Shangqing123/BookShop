package com.example.bookshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.bookshop.R;
import com.example.bookshop.model.UserGain;

import org.litepal.LitePal;

import java.util.List;


/**
 * 下单
 */
//定义了三个编辑框的字段名mEtName、mEtPrice、mEtAuth；
// 创建onCreate方法使界面显示布局文件activity_add_book。
// 定义三个方法， intview();、intidate();、intivetn();
public class UpdateGainActivity extends AppCompatActivity {


    private EditText mEtPhone;
    private EditText mEAddress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_gain);
        intview();
        intidate();
        intivetn();
    }

    private void intivetn() {

    }

    private void intidate() {
    }

    private void intview() {
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEAddress = (EditText) findViewById(R.id.et_address);
        Bundle bundle = this.getIntent().getExtras();
        String obj = bundle.getString("userGain");
        UserGain userGain = JSONObject.parseObject(obj,UserGain.class);
        mEAddress.setText(userGain.getAddress());
        mEtPhone.setText(userGain.getPhone());
    }

    public void back(View view) {
        finish();
    }

    /**
     * 确认添加
     * @param view
     */
    //updateConfirm()方法，创建一个新的书籍，使能创建书籍名称、作者、价格。字段类型为boolean类型。
    // 当添加成功时，成功保存书籍，并且设置结果集为1.反之则添加失败。

    public void updateConfirm(View view) {
        if(mEtPhone.getText().toString().trim().equals("")|| mEAddress.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"请输入完整",Toast.LENGTH_SHORT).show();
        }else {
            UserGain userGain = new UserGain();
            String phone = mEtPhone.getText().toString().trim();
            String address = mEAddress.getText().toString().trim();
            userGain.setPhone(mEtPhone.getText().toString().trim());
            userGain.setAddress(mEAddress.getText().toString().trim());
            List<UserGain> userGains = LitePal.where("phone = ? and address = ?",
                    phone, address).find(UserGain.class);
            if(userGains.size()==0){
                Bundle bundle = this.getIntent().getExtras();
                String obj = bundle.getString("userGain");
                UserGain gainLast = JSONObject.parseObject(obj,UserGain.class);
                int update = userGain.updateAll("phone=? and address=?",gainLast.getPhone() ,gainLast.getAddress() );
                if (update>=0) {
                    Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "更新失败", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getApplicationContext(),"该信息已经存在",Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent();
            intent.setClass(UpdateGainActivity.this,MainGainActivity.class);
            setResult(2,intent);
            finish();
        }
    }

}
