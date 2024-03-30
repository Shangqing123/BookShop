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
 * 主页
 */

public class HomeFragment extends Fragment {
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ArrayList<String> picture_uri = new ArrayList<>();
    private ArrayList<Book> mDate=new ArrayList<>();    //书籍列表
    private ListView mList;         //listview列表
    private BookAdapter adapter;    //书籍监听器
    private TextView mTvState;      //文本
    private Handler mHandler;
    private String check;
    private TextView mTvzh;         //综合
    private TextView mTvwx;         //文学
    private TextView mTvwuxia;      //武侠
    private TextView mTvkh;         //科幻
    private TextView mTvls;         //历史
    private int bookId1;

    /**
     * 通过http连接请求到json串并进行解析转化为数据列表对象
     */
    class MyThread1 extends Thread {
        @Override
        public void run() {
            String path = "http://47.100.121.231:8080/listBook"+"?bookcategory="+check;
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
                        picture_uri.clear();
                        for(int i = 0; i<jsonArray.size(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);//解析
                            Book book = JSON.parseObject(object1.toJSONString(),Book.class);
                            books.add(book);
                            picture_uri.add(books.get(i).getBookImage());
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_home,null);  //加载布局
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
                intent.setClass(getActivity(), BookDetailActivity.class); //书籍详情页面
                startActivity(intent);
            }
        });

        return layout;
    }

    //初始化事件的反应，当加入购物车时的反应
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
                Thread7 thread = new Thread7();//子线程
                thread.start();
            }
        });
    }

    //读入数据
    public void initdate() {
        MyThread1 thread1 = new MyThread1();//子线程
        thread1.start();
        mHandler = new Handler() {
            //通知书籍适配器内容变化，进行书籍显示
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                List<Book> bookList = (List<Book>) msg.obj;
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

    //分类浏览书籍的实现
    public void initview(View layout) {
        check = "综合";
        mTvzh = (TextView) layout.findViewById(R.id.tv_zh);         //综合布局
        mTvzh.setTextColor(getResources().getColor(R.color.dark_red));//综合颜色
        mTvwx = (TextView) layout.findViewById(R.id.tv_wx);             //文学布局
        mTvwuxia = (TextView) layout.findViewById(R.id.tv_wuxue);        //武侠布局
        mTvkh = (TextView) layout.findViewById(R.id.tv_kh);              //科幻布局
        mTvls = (TextView) layout.findViewById(R.id.tv_ls);              //历史布局
        mTvState = (TextView) layout.findViewById(R.id.tv_state);
        mList = (ListView) layout.findViewById(R.id.list);              //ListView列表
        adapter = new BookAdapter(getActivity(),mDate,picture_uri,imageLoader);
        mList.setAdapter(adapter);          //从适配器中读入适当的数据

        //设置事件监听器
        mTvzh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("综合 ");
                check = "综合";
                mTvzh.setTextColor(getResources().getColor(R.color.dark_red));
                mTvwx.setTextColor(getResources().getColor(R.color.lightss_black));
                mTvwuxia.setTextColor(getResources().getColor(R.color.lightss_black));
                mTvkh.setTextColor(getResources().getColor(R.color.lightss_black));
                mTvls.setTextColor(getResources().getColor(R.color.lightss_black));
                initdate();                 //读入相关数据
            }
        });
        mTvwx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("综合 ");
                check = "文学";
                mTvzh.setTextColor(getResources().getColor(R.color.lightss_black));
                mTvwx.setTextColor(getResources().getColor(R.color.dark_red));
                mTvwuxia.setTextColor(getResources().getColor(R.color.lightss_black));
                mTvkh.setTextColor(getResources().getColor(R.color.lightss_black));
                mTvls.setTextColor(getResources().getColor(R.color.lightss_black));
                initdate();
            }
        });
        mTvwuxia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("综合 ");
                check = "武侠";
                mTvzh.setTextColor(getResources().getColor(R.color.lightss_black));
                mTvwx.setTextColor(getResources().getColor(R.color.lightss_black));
                mTvwuxia.setTextColor(getResources().getColor(R.color.dark_red));
                mTvkh.setTextColor(getResources().getColor(R.color.lightss_black));
                mTvls.setTextColor(getResources().getColor(R.color.lightss_black));
                initdate();
            }
        });
        mTvkh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("综合 ");
                check = "科幻";
                mTvzh.setTextColor(getResources().getColor(R.color.lightss_black));
                mTvwx.setTextColor(getResources().getColor(R.color.lightss_black));
                mTvwuxia.setTextColor(getResources().getColor(R.color.lightss_black));
                mTvkh.setTextColor(getResources().getColor(R.color.dark_red));
                mTvls.setTextColor(getResources().getColor(R.color.lightss_black));
                initdate();
            }
        });
        mTvls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("综合 ");
                check = "历史";
                mTvzh.setTextColor(getResources().getColor(R.color.lightss_black));
                mTvwx.setTextColor(getResources().getColor(R.color.lightss_black));
                mTvwuxia.setTextColor(getResources().getColor(R.color.lightss_black));
                mTvkh.setTextColor(getResources().getColor(R.color.lightss_black));
                mTvls.setTextColor(getResources().getColor(R.color.dark_red));
                initdate();
            }
        });
    }


    class Thread7 extends Thread {
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
