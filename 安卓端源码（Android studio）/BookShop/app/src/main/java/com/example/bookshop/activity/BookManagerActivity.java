package com.example.bookshop.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.bookshop.R;
import com.example.bookshop.adapter.BookManagerAdapter;
import com.example.bookshop.model.Book;
import com.example.bookshop.utils.HttpClientHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 书籍管理
 */

//创建BookManagerActivity类，定义了mList、mTvState两个字段名，书籍管理的适配器。
// 并且定义了一个新的书籍时间管理实例对象，继承list接口。
public class BookManagerActivity extends AppCompatActivity {

    private static final int START_DOWNLOAD_MESSAGE = 0x01;
    private static final int FINISH_DOWNLOAD_MESSAGE = 0x02;
    private static final int ERROR_DOWNLOAD_MESSAGE = 0x03;

    private static final int DELETE_START = 0x04;
    private static final int DELETE_ERROR = 0x05;
    private static final int DELETE_FINISH = 0x06;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ArrayList<String> picture_uri = new ArrayList<>();
    private ArrayList<Book> mDate=new ArrayList<>();
    private ListView mList;//实现滚动滑动功能
    private BookManagerAdapter adapter;
    private TextView mTvState;

    private ImageView imageView;
    Handler handler = new MyHandler();

    //用onCreate()方法，创建显示上下文，以activity_book_manager.xml布局文件显示，并且定义三个方法intview()、intidate()、intivetn()。

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        intview();
        intidate();
        intivetn();
    }

    //定义删除书籍方法，intivetn()；adapter是配给设置OnDelLitner删除按钮的接口，并且重定义一个书籍信息管理适配器的接口，用OnDelLitener()显示
    //定义一个新的提示信息，当结果集缺省时，按钮没有指定该命令，显示取消；当删除执行时，结果集为0时，执行确认，反之则删除成功。

    private void intivetn() {
        /**
         * 删除书籍
         */
        adapter.setOnDelLitner(new BookManagerAdapter.OnDelLitner() {
            @Override
            public void del(final int pos) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookManagerActivity.this);
                builder.setMessage("确认要删除该书籍？");
                builder.setTitle("提示");
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {   @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();}
                });
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {   @Override
                public void onClick(DialogInterface dialog, int which) {//监听
                    Book book = mDate.get(pos);
                    int bookId = book.getBookId();
                    String url = getResources().getString(R.string.url)+"/deleteBook?bookId=" + bookId;
                    Handler deleteHandler = new Handler();
                    DeleteRunnable runnable = new DeleteRunnable();
                    runnable.setUrl(url);
                    runnable.setMyHandler(deleteHandler);
                    new Thread(runnable).start();
                    mDate.remove(pos);
                    picture_uri.remove(pos);
                    adapter.notifyDataSetChanged();//数据库更改
                    dialog.dismiss();
                }
                });
                builder.create().show();
            }
        });


        /**
         * 查看和修改，ListView的监听
         */
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int note_id = mDate.get(position).getBookId();
                if (note_id >= 0) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    Book book = mDate.get(position);
                    bundle.putString("book_id",note_id + "");
                    bundle.putSerializable("book",book);
                    intent.putExtras(bundle);
                    intent.setClass(BookManagerActivity.this,UpdateBookActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });


        /**
         * 添加书籍
         */
        findViewById(R.id.tv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddBookActivity.class));
                finish();
            }
        });
    }

    private void intidate() {
        mDate.clear();
        QueryAllRunnable runnable = new QueryAllRunnable();
        runnable.setMyHandler(handler);
        runnable.setUrl(getResources().getString(R.string.url)+"/listBookByManager");
        new Thread(runnable).start();
    }

    private void intview() {
        mTvState = (TextView) findViewById(R.id.tv_state);
        mList = (ListView) findViewById(R.id.list);
        adapter = new BookManagerAdapter(getApplicationContext(),mDate,picture_uri,imageLoader);
        mList.setAdapter(adapter);
    }

    public void back(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) { //判断数据是否为空，就可以解决这个问题
            return;
        }else {
            /* 添加书籍 */
            if(resultCode==2&&requestCode==2){
                Bundle bundle = data.getExtras();
                Book book = new Book();
                book = (Book) bundle.getSerializable("book");
                mDate.add(book);
                adapter.notifyDataSetChanged();
            }
        }
    }


    class QueryAllRunnable implements Runnable{
        private Handler myHandler;
        private String url;

        public Handler getMyHandler() {
            return myHandler;
        }

        public void setMyHandler(Handler myHandler) {
            this.myHandler = myHandler;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            HttpGet httpGet = new HttpGet(url);

            //为这个HttpGet设置一些特定的属性，别的属性沿用HttpClient
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 60000);
            httpGet.setParams(params);

            myHandler.sendEmptyMessage(START_DOWNLOAD_MESSAGE);

            try {

                HttpResponse httpResponse = HttpClientHelper.getHttpClient().execute(httpGet);;

                byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());
                //在大多数情况下，这个下载下来的是XML或者Json。应该解析完组装成对象再放置到Message中。
                //这里简单起见，直接变成字符串打印了
                String result = new String(bytes);

                Message msg = myHandler.obtainMessage();
                msg.what = FINISH_DOWNLOAD_MESSAGE;
                msg.obj = result;
                Log.e("pan",result.toString());
                myHandler.sendMessage(msg);

            } catch (Exception ex){
                ex.printStackTrace();
                myHandler.sendEmptyMessage(ERROR_DOWNLOAD_MESSAGE);
            }
        }
    }

    class DeleteRunnable implements Runnable{
        private Handler myHandler;
        private String url;

        public Handler getMyHandler() {
            return myHandler;
        }

        public void setMyHandler(Handler myHandler) {
            this.myHandler = myHandler;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            HttpGet httpGet = new HttpGet(url);

            //为这个HttpGet设置一些特定的属性，别的属性沿用HttpClient
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 60000);
            httpGet.setParams(params);

            myHandler.sendEmptyMessage(DELETE_START);

            try {

                HttpResponse httpResponse = HttpClientHelper.getHttpClient().execute(httpGet);;

                byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());
                //在大多数情况下，这个下载下来的是XML或者Json。应该解析完组装成对象再放置到Message中。
                //这里简单起见，直接变成字符串打印了
                String result = new String(bytes);
                Looper.prepare();
                Message msg = myHandler.obtainMessage();
                msg.what = DELETE_FINISH;
                msg.obj = result;
                if(result.equals("Delete Success")){
                    Toast.makeText(BookManagerActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(BookManagerActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
                Log.e("pan",result.toString());
                myHandler.sendMessage(msg);
                Looper.loop();

            } catch (Exception ex){
                ex.printStackTrace();
                myHandler.sendEmptyMessage(DELETE_ERROR);
            }
        }
    }

    private class MyHandler extends Handler{
        @Override
        public void dispatchMessage(Message msg) {
            switch(msg.what){
                case START_DOWNLOAD_MESSAGE:
                    Toast.makeText(BookManagerActivity.this, "开始加载", Toast.LENGTH_SHORT).show();
                    Log.e("error","start");
                    break;

                case FINISH_DOWNLOAD_MESSAGE:
                    Toast.makeText(BookManagerActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                    Log.e("error","finish");
                    List<Book> books = JSON.parseArray((String)msg.obj,Book.class);
                    List<Book> bookList = new ArrayList<>();
                    for(Book book:books){
                        bookList.add(book);
                        Log.e("book",book.toString());
                    }
                    if(bookList.size()==0){
                        mList.setVisibility(View.GONE);
                        mTvState.setVisibility(View.VISIBLE);
                        mTvState.setText("暂无数据");
                    }else {
                        mDate.addAll(bookList);
                        for(int i = 0; i<mDate.size(); i++) {
                            picture_uri.add(mDate.get(i).getBookImage());
                        }
                        mList.setVisibility(View.VISIBLE);
                        mTvState.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                    //简单起见，直接输出了。
                    System.out.println(msg.obj);
                    break;

                case ERROR_DOWNLOAD_MESSAGE:
                    Toast.makeText(BookManagerActivity.this, "下载失败" + msg.obj, Toast.LENGTH_SHORT).show();
                    Log.e("error","error");
                    break;

                default:
                    System.out.println("nothing to do");
                    Log.e("error","nothing to do");
                    break;
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


