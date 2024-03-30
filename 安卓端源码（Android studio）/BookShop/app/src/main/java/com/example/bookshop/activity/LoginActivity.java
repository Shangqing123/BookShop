package com.example.bookshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.bookshop.R;
import com.example.bookshop.model.User;
import com.example.bookshop.utils.SetUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * description: 用户登录界面
 */

//创建一个新的类，命名为LoginActivity，继承FagmentActivity类的方法。
// 定义两个字段，分别为mEtPassword和mEtUsername，用来实现用户的登录。
public class LoginActivity extends FragmentActivity {

    private EditText mEtPassword;
    private EditText mEtUsername;

    //用onCreate()方法创建上下文，显示activity_login的布局，并且创建两个方法，分别为   initview()和initdate();

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initview();
        initdate();
    }

    /**
     * 初始化数据
     */
    //提供一个新的用户接口，当类型为2时，本地数据库的会找到用户的类
    //如果用户的大小为0，会定义一个新的用户设置它的账号密码为admin,admin，设置字段类型为2，保存信息。
    private void initdate() {
        //如果设置工具时，已经登录成功了，那么用getApplicationContext()方法获取到上下文。
        // 如果登陆成功为管理员，用intent()传递消息则设置管理员的活动类型，开启一个新的Activity
        if(SetUtils.IsLogin(getApplicationContext())){
            if(SetUtils.IsManager(getApplicationContext())){
                startActivity(new Intent(getApplicationContext(),ManagerActivity.class));//管理员活动
            }else {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));//用户活动
            }
            finish();
        }
    }

    /**
     * 初始化视图，从安卓段获取输入的信息
     */
    private void initview() {
        mEtUsername = (EditText) findViewById(R.id.et_name);
        mEtPassword = (EditText) findViewById(R.id.et_password);
    }

    /**
     * 用户或者管理员登录，在xml文件的onclick中填写跳转到这里
     * @param view
     */
    public void login(View view) throws Exception {
        final String userName = mEtUsername.getText().toString().trim();  //用户名
        final String userPassword = mEtPassword.getText().toString().trim(); //密码
        if(userName.equals("")||userPassword.equals("")){
            Toast.makeText(getApplicationContext(),"请输入完整",Toast.LENGTH_SHORT).show();
        }else {
             final Handler mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    User users = (User)msg.obj;
                    if(users.getUserName()!=null && !users.getUserName().equals("")){
                        Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
                        SetUtils.saveId(getApplicationContext(),users.getUserId()+"");    //保持用户ID
                        SetUtils.saveIsLogin(getApplicationContext(),true);                 //保持用户登录状态
                        SetUtils.saveUserName(getApplicationContext(),users.getUserName());         //保持用户名
                        if(!users.getUserType().equals("manager")){
                            SetUtils.saveIsManager(getApplicationContext(),false);          //保持用户权限
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else if(users.getUserType().equals("manager")){
                            SetUtils.saveIsManager(getApplicationContext(),true);        //保持管理员权限
                            startActivity(new Intent(getApplicationContext(),ManagerActivity.class));
                        }
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),"请输入正确的用户名或密码",Toast.LENGTH_SHORT).show();
                    }
                }
            };

            new Thread() {
                @Override
                public void run() {
                    //User users = LitePal.where("username = ? and password=?", mEtUsername.getText().toString().trim(),mEtPassword.getText().toString().trim()).findFirst(User.class);
                    String path = "http://47.100.121.231:8080/getuser?userName="+userName+"&userPassword=" + userPassword;
                    try{
                        URL url = new URL(path);
                        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                        urlConnection.setConnectTimeout(5000);
                        urlConnection.connect();
                        User users =null;
                        if(urlConnection.getResponseCode() == 200) {

                            String str = getStringFromInputStream(urlConnection.getInputStream());
                            System.out.println(str);
                            if(!str.equals("null")) {
                                users = JSON.parseObject(str,User.class);
                            }
                            System.out.println(users.toString());

                        }else {
                            System.out.println("网络出现问题");
                        }
                        urlConnection.disconnect();
                        Message message = new Message();
                        message.obj = users;
                        mHandler .sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    /**
     * 跳转到注册
     * @param view
     */
    public void regist(View view) {
        startActivity(new Intent(getApplicationContext(),RegistActivity.class));
    }


    public static String getStringFromInputStream(InputStream is){
        String result = null;
        Scanner s = new Scanner(is).useDelimiter("\\A");
        result = s.hasNext() ? s.next() : "";
        return result;
    }

    /**
     * 跳转到关于
     * @param view
     */
    public void about(View view){
        startActivity(new Intent(getApplicationContext(),AboutActivity.class));
    }
}
