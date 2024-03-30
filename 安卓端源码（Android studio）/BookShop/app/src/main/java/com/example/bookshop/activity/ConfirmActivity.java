package com.example.bookshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bookshop.R;
import com.example.bookshop.adapter.ConfirOrderAdapter;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.CartItem;
import com.example.bookshop.model.UserGain;
import com.example.bookshop.utils.SetUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.litepal.LitePal;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 确认订单界面
 */

public class ConfirmActivity extends AppCompatActivity {
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ArrayList<String> picture_uri;
    private ArrayList<CartItem> cartItems ;
    private ArrayList<Book> books;
    private ConfirOrderAdapter confirOrderAdapter;
    private LinearLayout linearLayout;
    private TextView mGainName;
    private TextView mEtPhone;
    private TextView mEtAddress;

    private ListView mList;
    private TextView mTvtotal;

    private UserGain userGainCheck;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_order);
        intview();
        initdate();
        intievnet();
    }

    private void intievnet() {
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("cartItems",cartItems);
                bundle.putSerializable("books",books);
                bundle.putSerializable("picture_uri",picture_uri);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(getApplicationContext(), ConfirmGainActivity.class);//到确认收货信息界面
                startActivity(intent);
                finish();
            }
        });
    }

    //初始化数据信息
    private void initdate() {
        UserGain userGain;
        if(userGainCheck==null){
            List<UserGain> userGainList = LitePal.where("type=?","默认地址").find(UserGain.class);

            userGain = userGainList.get(0);
            userGainCheck = userGain;
        } else{
            userGain = userGainCheck;
        }

        mGainName.setText(SetUtils.GetUserName(getApplicationContext()));
        mEtPhone.setText(userGain.getPhone());
        mEtAddress.setText("地址："+userGain.getAddress());

        double money = 0.0;
        for(int i = 0; i<cartItems.size(); i++) {
            money += (cartItems.get(i).getCartitemBookNumber() * books.get(i).getBookPrice());
        }
        mTvtotal.setText("总计：￥ " + money);

    }

    /**
     * 初始化视图，从安卓端读出数据
     */
    private void intview() {
        picture_uri = (ArrayList<String>) getIntent().getSerializableExtra("picture_uri");
        cartItems = (ArrayList<CartItem>) getIntent().getSerializableExtra("cartItems");
        books = (ArrayList<Book>) getIntent().getSerializableExtra("books");
        if(getIntent().getSerializableExtra("userGain") != null ) {
            userGainCheck = (UserGain) getIntent().getSerializableExtra("userGain");
        }

        mList = (ListView) findViewById(R.id.list);
        linearLayout = (LinearLayout) findViewById(R.id.gain);
        mGainName = (TextView) findViewById(R.id.gain_name);
        mEtPhone = (TextView) findViewById(R.id.et_phone);
        mEtAddress = (TextView) findViewById(R.id.et_address);
        mTvtotal = (TextView) findViewById(R.id.tv_total);
        confirOrderAdapter = new ConfirOrderAdapter(getApplicationContext(),cartItems,books,picture_uri,imageLoader);
        mList.setAdapter(confirOrderAdapter);

    }

    //购买
    public void buy(View view) throws Exception {
        Thread9 thread9 = new Thread9();//进入子线程
        thread9.start();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                startActivity(new Intent(getApplicationContext(), MyOrderActivity.class));//进入我的订单界面
                finish();
            }
        };
    }

    public void back(View view) {
        finish();
    }

    class Thread9 extends Thread {
        @Override
        public void run() {
            String userId = SetUtils.GetId(getApplicationContext());
            String phone = userGainCheck.getPhone();
            String address = userGainCheck.getAddress();
            String path = "http://47.100.121.231:8080/addOrder";
            String params = null;
            try {
                params = "userId=" + URLEncoder.encode(userId, "UTF-8") + "&phone=" + URLEncoder.encode(phone, "UTF-8")
                        + "&address=" + URLEncoder.encode(address, "UTF-8");
                URL url = new URL(path);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(5000);
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setUseCaches(false);
                urlConnection.setRequestMethod("POST");
                urlConnection.setInstanceFollowRedirects(true);

                urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                urlConnection.connect();

                DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
                out.writeBytes(params);
                out.flush();
                out.close();
                System.out.println(urlConnection.getResponseCode());
                if(urlConnection.getResponseCode() == 200){
                    Message message = new Message();
                    mHandler.sendMessage(message);
                }
                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        // 回收该页面缓存在内存中的图片
        imageLoader.clearMemoryCache();
        super.onDestroy();
    }
}
