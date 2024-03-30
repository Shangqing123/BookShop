package com.example.bookshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bookshop.R;
import com.example.bookshop.activity.BookDetailActivity;
import com.example.bookshop.activity.LoginActivity;
import com.example.bookshop.adapter.BookAdapter;
import com.example.bookshop.model.Book;
import com.example.bookshop.utils.SetUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索
 */

public class ResearchFragment extends Fragment {
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ArrayList<String> picture_uri = new ArrayList<>();
    private ArrayList<Book> mDate=new ArrayList<>();
    private ListView mList;
    private BookAdapter adapter;
    private TextView mTvState;
    private Handler mHandler;
    private EditText mResearch;
    private Button mButton;
    private String booktxt;
    private int bookId1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_research,null);
        initview(layout);
        initdate();
        initevent();
        //为书籍列表设置监听器，点击书籍进入书籍详情页面
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("book",mDate.get(position));
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(getActivity(), BookDetailActivity.class);
                startActivity(intent);
            }
        });

        return layout;
    }
    private void initevent() {
        adapter.setOnBuyLitner(new BookAdapter.OnBuyLitner() {
            @Override
            public void buy(int pos,int id) {
                bookId1 = id;
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        String str = (String) msg.obj;
                        if(str.equals("加入成功")) {
                            Toast.makeText(getContext(),"添加成功，在购物车等亲",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(),"添加失败，再试一次哦",Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                Thread8 thread = new Thread8();//子线程
                thread.start();
            }
        });
    }

    public void initdate() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                booktxt = mResearch.getText().toString().trim();
                MyThread1 thread1 = new MyThread1();//子线程
                thread1.start();
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        List<Book> bookList = (List<Book>) msg.obj;
                        for(Book book : bookList) {
                            System.out.println("第二次"+book.toString());
                        }
                        mDate.clear();
                        if(bookList.size()==0){
                            mList.setVisibility(View.GONE);
                            mTvState.setVisibility(View.VISIBLE);
                            mTvState.setText("暂无数据");
                        }else {
                            mDate.addAll(bookList);
                            mList.setVisibility(View.VISIBLE);
                            mTvState.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                    }
                };
            }
        });
    }

    public void initview(View layout) {
        mResearch = (EditText) layout.findViewById(R.id.researchtxt);
        mButton = (Button) layout.findViewById(R.id.button1);
        mTvState = (TextView) layout.findViewById(R.id.tv_state);
        mList = (ListView) layout.findViewById(R.id.list);
        adapter = new BookAdapter(getActivity(),mDate,picture_uri,imageLoader);
        mList.setAdapter(adapter);//书籍适配器

    }

    class MyThread1 extends Thread {
        @Override
        public void run() {
            String path = "http://47.100.121.231:8080/searchBook"+"?booktxt="+booktxt;
            List<Book> books = null;
            try {
                URL url = new URL(path);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {
                    String str = LoginActivity.getStringFromInputStream(urlConnection.getInputStream());
                    System.out.println(str);
                    if (!str.equals("null")) {
                        JSONObject object = JSON.parseObject(str);
                        JSONArray jsonArray = object.getJSONArray("books");
                        books = new ArrayList<>();
                        books.clear();
                        for(int i = 0; i<jsonArray.size(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            Book book = JSON.parseObject(object1.toJSONString(),Book.class);
                            books.add(book);
                            picture_uri.add(books.get(i).getBookImage());
                        }


                        System.out.println("book的size" + books.size());
                        for(Book book : books) {
                            System.out.println("第一次"+book.toString());
                        }
                    }
                } else {
                    System.out.println("网络出现问题");
                }
                urlConnection.disconnect();

                Message message = new Message();
                message.obj = books;
                mHandler.sendMessage(message);

            } catch (Exception e) {

            }
        }
    }

    class Thread8 extends Thread {
        @Override
        public void run() {
            String userId = SetUtils.GetId(getContext());
            String bookId = String.valueOf(bookId1);
            String path = "http://47.100.121.231:8080/addBookToCart";
            String params = null;
            try {
                params = "userId=" + URLEncoder.encode(userId, "UTF-8") + "&bookId=" + URLEncoder.encode(bookId, "UTF-8");
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
                if(urlConnection.getResponseCode() == 200) {
                    String str = LoginActivity.getStringFromInputStream(urlConnection.getInputStream());
                    System.out.println(str);
                    Message message2 = mHandler.obtainMessage();
                    message2.obj = str;
                    mHandler.sendMessage(message2);
                } else {
                    System.out.println("网络故障");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
