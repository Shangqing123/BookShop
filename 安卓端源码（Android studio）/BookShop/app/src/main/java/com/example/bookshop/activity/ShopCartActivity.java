package com.example.bookshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bookshop.R;
import com.example.bookshop.adapter.CartAdapter;
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
import java.util.Iterator;
import java.util.List;

/**
 * 购物车界面
 */
public class ShopCartActivity extends AppCompatActivity {
    // 创建ImageLoader对象
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ArrayList<String> picture_uri = new ArrayList<>();
    private CartAdapter cartAdapter;
    private ArrayList<CartItem> cartItems = new ArrayList<>();
    private ArrayList<Book> books = new ArrayList<>();
    private ListView mList;
    private TextView mTvState;
    private Handler mHandler;
    private TextView mTvtotal;

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        intview();
        initdate();
        intievnet();
    }

    private void intievnet() {
        cartAdapter.setOnDelLitner(new CartAdapter.OnDelLitner() {
            @Override
            public void del(final int postion) {
                System.out.println(postion);
                AlertDialog.Builder builder = new AlertDialog.Builder(ShopCartActivity.this);

                new Thread() {
                    @Override
                    public void run() {
                        String path = "http://47.100.121.231:8080/deleteCartItem";
                        String params = null;
                        try {
                            params = "cartitemId=" + URLEncoder.encode(String.valueOf(postion), "UTF-8");
                            URL url = new URL(path);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setConnectTimeout(5000);
                            urlConnection.setDoOutput(true);
                            urlConnection.setDoInput(true);
                            urlConnection.setUseCaches(false);
                            urlConnection.setRequestMethod("POST");
                            urlConnection.setInstanceFollowRedirects(true);

                            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                            urlConnection.connect();

                            DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
                            out.writeBytes(params);
                            out.flush();
                            out.close();
                            System.out.println("删除的返回码" + urlConnection.getResponseCode());
                            urlConnection.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

                Iterator<CartItem> iterator = cartItems.iterator();
                while(iterator.hasNext()){
                    CartItem cartItem = iterator.next();
                    if (cartItem.getCartitemId() == postion) {

                        Iterator<Book> iterator1 = books.iterator();
                        while (iterator1.hasNext()) {
                            Book book = iterator1.next();
                            if (book.getBookId() == cartItem.getCartitemBookId()) {
                                iterator1.remove();
                            }
                        }
                        iterator.remove();
                    }
                }
                System.out.println(cartItems.size()+"第一次");
                System.out.println(books.size()+"第一次");
                cartAdapter.notifyDataSetChanged();
                double money = 0.0;
                for(int i = 0; i<cartItems.size(); i++) {
                    money += (cartItems.get(i).getCartitemBookNumber() * books.get(i).getBookPrice());
                }
                mTvtotal.setText("总计：" + money);
            }

        });

        cartAdapter.setOnDecLitner(new CartAdapter.OnDecLitner() {
            @Override
            public void dec(final int postion) {
                System.out.println(postion);
                AlertDialog.Builder builder = new AlertDialog.Builder(ShopCartActivity.this);

                new Thread() {
                    @Override
                    public void run() {
                        String path = "http://47.100.121.231:8080/descCartItem";
                        String params = null;
                        try {
                            params = "cartitemId=" + URLEncoder.encode(String.valueOf(postion), "UTF-8");
                            URL url = new URL(path);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setConnectTimeout(5000);
                            urlConnection.setDoOutput(true);
                            urlConnection.setDoInput(true);
                            urlConnection.setUseCaches(false);
                            urlConnection.setRequestMethod("POST");
                            urlConnection.setInstanceFollowRedirects(true);

                            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                            urlConnection.connect();

                            DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
                            out.writeBytes(params);
                            out.flush();
                            out.close();
                            System.out.println("减一的返回码" + urlConnection.getResponseCode());
                            urlConnection.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

                Iterator<CartItem> iterator = cartItems.iterator();
                while(iterator.hasNext()){
                    CartItem cartItem = iterator.next();
                    if (cartItem.getCartitemId() == postion) {
                        cartItem.setCartitemBookNumber(cartItem.getCartitemBookNumber()-1);
                    }
                }
                System.out.println(cartItems.size()+"第一次");
                System.out.println(books.size()+"第一次");
                cartAdapter.notifyDataSetChanged();
                double money = 0.0;
                for(int i = 0; i<cartItems.size(); i++) {
                    money += (cartItems.get(i).getCartitemBookNumber() * books.get(i).getBookPrice());
                }
                mTvtotal.setText("总计：" + money);
            }

        });

        cartAdapter.setOnAscLitner(new CartAdapter.OnAscLitner() {
            @Override
            public void asc(final int postion) {
                System.out.println(postion);
                AlertDialog.Builder builder = new AlertDialog.Builder(ShopCartActivity.this);

                new Thread() {
                    @Override
                    public void run() {
                        String path = "http://47.100.121.231:8080/addCartItem";
                        String params = null;
                        try {
                            params = "cartitemId=" + URLEncoder.encode(String.valueOf(postion), "UTF-8");
                            URL url = new URL(path);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setConnectTimeout(5000);
                            urlConnection.setDoOutput(true);
                            urlConnection.setDoInput(true);
                            urlConnection.setUseCaches(false);
                            urlConnection.setRequestMethod("POST");
                            urlConnection.setInstanceFollowRedirects(true);

                            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                            urlConnection.connect();

                            DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
                            out.writeBytes(params);
                            out.flush();
                            out.close();
                            System.out.println("加一的返回码" + urlConnection.getResponseCode());
                            urlConnection.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

                Iterator<CartItem> iterator = cartItems.iterator();
                while(iterator.hasNext()){
                    CartItem cartItem = iterator.next();
                    if (cartItem.getCartitemId() == postion) {
                        cartItem.setCartitemBookNumber(cartItem.getCartitemBookNumber()+1);
                    }
                }
                System.out.println(cartItems.size()+"第一次");
                System.out.println(books.size()+"第一次");
                cartAdapter.notifyDataSetChanged();
                double money = 0.0;
                for(int i = 0; i<cartItems.size(); i++) {
                    money += (cartItems.get(i).getCartitemBookNumber() * books.get(i).getBookPrice());
                }
                mTvtotal.setText("总计：" + money);
            }

        });


    }

    public void back(View view) {
        finish();
    }


    //初始化数据
    private void initdate() {
        System.out.println("准备进入子线程");
        new Thread5().start();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                List<CartItem> cartItems2 = (List<CartItem>) msg.obj;
                cartItems.clear();
                if(cartItems2.size()==0){
                    mList.setVisibility(View.GONE);
                    mTvState.setVisibility(View.VISIBLE);
                    mTvState.setText("暂无数据");
                    linearLayout.setVisibility(View.GONE);
                }else {
                    cartItems.addAll(cartItems2);
                    mList.setVisibility(View.VISIBLE);
                    mTvState.setVisibility(View.GONE);
                }
                cartAdapter.notifyDataSetChanged();

                double money = 0.0;
                for(int i = 0; i<cartItems.size(); i++) {
                    money += (cartItems.get(i).getCartitemBookNumber() * books.get(i).getBookPrice());
                }
                mTvtotal.setText("总计：￥" + money);
            }
        };

    }

    private void intview() {
        mTvState = (TextView) findViewById(R.id.tv_state);
        mList = (ListView) findViewById(R.id.list);
        mTvtotal = (TextView) findViewById(R.id.tv_total);
        cartAdapter = new CartAdapter(getApplicationContext(),cartItems,books,picture_uri,imageLoader);
        mList.setAdapter(cartAdapter);
        linearLayout = (LinearLayout) findViewById(R.id.cart_list);
    }

    //购买操作
    public void buy(View view) {
        List<UserGain> userGainList = LitePal.where("type=?","默认地址").find(UserGain.class);
        if(userGainList.size()==0) {
            Toast.makeText(getApplicationContext(),"无收货地址，请先添加收货地址",Toast.LENGTH_SHORT).show();
        }else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("cartItems",cartItems);
            bundle.putSerializable("books",books);
            bundle.putSerializable("picture_uri",picture_uri);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            intent.setClass(getApplicationContext(), ConfirmActivity.class);//进入确认订单界面
            startActivity(intent);
        }

    }

    class Thread5 extends Thread {
        @Override
        public void run() {
            List<CartItem> cartItems1;
            System.out.println("进入线程");
            String path = "http://47.100.121.231:8080/getCart";
            String params = null;
            try {
                params = "userId=" + URLEncoder.encode(SetUtils.GetId(getApplicationContext()), "UTF-8");
                URL url = new URL(path);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(5000);
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setUseCaches(false);
                urlConnection.setRequestMethod("POST");
                urlConnection.setInstanceFollowRedirects(true);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.connect();

                DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
                out.writeBytes(params);
                out.flush();
                out.close();
                System.out.println(urlConnection.getResponseCode());
                if (urlConnection.getResponseCode() == 200) {
                    String str = LoginActivity.getStringFromInputStream(urlConnection.getInputStream());
                    System.out.println(str);
                    JSONObject object = JSONObject.parseObject(str);
                    JSONArray jsonArray = object.getJSONArray("cartitems");
                    JSONArray jsonArray1 = object.getJSONArray("books");



                    cartItems1 = JSON.parseArray(jsonArray.toJSONString(),CartItem.class);
                    books.addAll(JSON.parseArray(jsonArray1.toJSONString(),Book.class));
                    for(Book book :books) {
                        picture_uri.add(book.getBookImage());
                    }
                    System.out.println("初始化后的picture_uri" + picture_uri.toString());
                    System.out.println(books.toString());
                    System.out.println(cartItems1.toString());
                    Message message2 = mHandler.obtainMessage();
                    message2.obj = cartItems1;
                    mHandler.sendMessage(message2);
                }
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
