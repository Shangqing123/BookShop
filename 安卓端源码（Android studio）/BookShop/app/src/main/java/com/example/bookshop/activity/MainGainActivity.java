package com.example.bookshop.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.bookshop.R;
import com.example.bookshop.adapter.UserGainAdapter;
import com.example.bookshop.model.UserGain;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * 收货信息管理
 */

//创建UserGainManagerActivity类，定义了mList、mTvState两个字段名，书籍管理的适配器。
// 并且定义了一个新的书籍时间管理实例对象，继承list接口。
public class MainGainActivity extends AppCompatActivity {

    private ArrayList<UserGain> mDate=new ArrayList<>();
    private ListView mList;
    private UserGainAdapter adapter;
    private TextView mTvState;

    //用onCreate()方法，创建显示上下文，以activity_UserGain_manager.xml布局文件显示，并且定义三个方法intview()、intidate()、intivetn()。

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_gainmain);
        intview();
        intidate();
        intivetn();
    }

    //定义删除书籍方法，intivetn()；adapter是配给设置OnDelLitner删除按钮的接口，并且重定义一个书籍信息管理适配器的接口，用OnDelLitener()显示
    //定义一个新的提示信息，当结果集缺省时，按钮没有指定该命令，显示取消；当删除执行时，结果集为0时，执行确认，反之则删除成功。

    private void intivetn() {
        /**
         * 删除记录
         */
        adapter.setOnClickListner(new UserGainAdapter.OnClickListner() {
            @Override
            public void del(final int pos) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainGainActivity.this);
                builder.setMessage("确认要删除该记录？");
                builder.setTitle("提示");
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {   @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();}
                });
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {   @Override
                public void onClick(DialogInterface dialog, int which) {
                    UserGain userGain = mDate.get(pos);
                    //如果该条地址是默认地址，则如果列表还有地址，从中选取一条作为默认地址
                    int delete = userGain.delete();
                    if(delete >= 0){
                        if(userGain.getType().equals("默认地址")) {
                            List<UserGain> userGains = LitePal.findAll(UserGain.class);
                            if(userGains.size()>=1) {
                                UserGain userGain1 = userGains.get(0);
                                userGain1.setType("默认地址");
                                userGain1.updateAll("type=?","非");
                            }
                        }

                        Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_SHORT).show();


                    }else {
                        Toast.makeText(getApplicationContext(),"删除失败",Toast.LENGTH_SHORT).show();
                    }
                    mDate.remove(pos);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
                });
                builder.create().show();
            }

            @Override
            public void update(int pos) {
                UserGain userGain = mDate.get(pos);
                Intent intent = new Intent();
                Object object = JSONObject.toJSON(userGain);
                Bundle bundle = new Bundle();
                bundle.putString("userGain",object.toString());
                intent.putExtras(bundle);
                intent.setClass(getApplicationContext(),UpdateGainActivity.class);
                startActivityForResult(intent,3);
            }
        });

        findViewById(R.id.tv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(),AddGainActivity.class),2);
            }
        });

    }

    //初始化数据
    private void intidate() {
        List<UserGain> gainList = LitePal.findAll(UserGain.class);
        mDate.clear();
        if(gainList.size()==0){
            mList.setVisibility(View.GONE);
            mTvState.setVisibility(View.VISIBLE);
            mTvState.setText("暂无数据");
        }else {
            mDate.addAll(gainList);
            mList.setVisibility(View.VISIBLE);
            mTvState.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    private void intview() {
        mTvState = (TextView) findViewById(R.id.tv_state);
        mList = (ListView) findViewById(R.id.list);
        adapter = new UserGainAdapter(getApplicationContext(),mDate);
        mList.setAdapter(adapter);
    }

    public void back(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            intidate();
        }
        /* 添加 */
        if(requestCode==2){
            intidate();
        }
        /* 更新 */
        if(requestCode==3){
            intidate();
        }
    }
}


