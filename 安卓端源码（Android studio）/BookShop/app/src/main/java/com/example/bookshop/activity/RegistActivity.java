package com.example.bookshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.bookshop.R;
import com.example.bookshop.model.User;
import com.example.bookshop.utils.SetUtils;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * description: 用户注册
 */
public class RegistActivity extends FragmentActivity {

    private EditText mEtPassword;
    private EditText mEtUsername;
    private EditText mEtPassword1;

    private Handler mHandler,mHandler2 ;//将mHandler指定轮询的Looper

    //异步消息处理机制
    class MyThread extends Thread{
        private Looper looper;//取出该子线程的Looper
        public void run() {

            Looper.prepare();//创建该子线程的Looper
            looper = Looper.myLooper();//取出该子线程的Looper

            mHandler = new Handler(looper) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    User user = (User) msg.obj;

                    String username = user.getUserName();
                    String userPassword = user.getUserPassword();
                    String path = "http://47.100.121.231:8080/regiseter";
                    String params = null;
                    try {
                        params = "userName=" + URLEncoder.encode(username, "UTF-8") + "&userPassword=" + URLEncoder.encode(userPassword, "UTF-8");
                        URL url = new URL(path);
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setConnectTimeout(5000);
                        urlConnection.setDoOutput(true);
                        urlConnection.setDoInput(true);
                        urlConnection.setUseCaches(false);
                        urlConnection.setRequestMethod("POST");//向服务器写
                        urlConnection.setInstanceFollowRedirects(true);

                        urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                        urlConnection.connect();

                        DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
                        out.writeBytes(params);
                        out.flush();
                        out.close();
                        if(urlConnection.getResponseCode() == 200) {
                            String str = LoginActivity.getStringFromInputStream(urlConnection.getInputStream());
                            String message = JSON.parseObject(str).getString("message");
                            Message message2 = mHandler2.obtainMessage();
                            message2.obj = message;
                            mHandler2.sendMessage(message2);
                        } else {
                            Toast.makeText(getApplicationContext(),"网络故障",Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            };
            Looper.loop();//只要调用了该方法才能不断循环取出消息
        }
    }


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initview();
        initdate();
    }

    /**
     * 初始化数据
     */
    private void initdate() {
        if(SetUtils.IsLogin(getApplicationContext())){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }

    /**
     * 初始化视图，从安卓段获取输入的信息
     */
    private void initview() {
        mEtUsername = (EditText) findViewById(R.id.et_name);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mEtPassword1 = (EditText) findViewById(R.id.et_password1);
    }

    /**
     * 注册按钮
     * @param view
     */
    public void regist(View view) throws Exception {

        String username = mEtUsername.getText().toString().trim();
        String userPassword1 = mEtPassword1.getText().toString().trim();
        String userPassword = mEtPassword.getText().toString().trim();
        if(username.equals("")||userPassword1.equals("")||userPassword.equals("")){
            Toast.makeText(getApplicationContext(),"请填写完整",Toast.LENGTH_SHORT).show();
        }else if(!userPassword1.equals(userPassword)){
            Toast.makeText(getApplicationContext(),"两次密码不一致",Toast.LENGTH_SHORT).show();
        } else {

            Thread thread = new MyThread();
            thread.start();
            Thread.sleep(50);
            Message message1 = new Message();
            User user = new User();
            user.setUserName(username);
            user.setUserPassword(userPassword);
            message1.obj = user;
            mHandler.sendMessage(message1);//发送数据

            mHandler2 = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);//服务端发回的数据
                    String str = (String) msg.obj;
                    if(str.equals("注册成功")) {
                        Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
                        finish();
                    } else if(str.equals("该用户名已注册过")) {
                        Toast.makeText(getApplicationContext(),"该用户名已注册过",Toast.LENGTH_SHORT).show();
                    }
                }
            };

        }
    }

    /**
     * 退出
     * @param view
     */
    public void back(View view) {
        finish();
    }
}
