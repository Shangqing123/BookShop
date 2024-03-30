package com.example.bookshop.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookshop.R;
import com.example.bookshop.model.Book;
import com.example.bookshop.utils.SetUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class BookDetailActivity extends AppCompatActivity {
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private Book book;
    private ImageView mTvbookImage;
    private TextView mTvbookName;
    private TextView mTvbookAuthor;
    private TextView mTvbookPrice;
    private TextView mTvbookDetail;
    private TextView mTvbooksales;
    private Handler mHandler;

    class Thread3 extends Thread {
        @Override
        public void run() {
            String userId = SetUtils.GetId(getApplicationContext());
            String bookId = String.valueOf(book.getBookId());
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail);
        intview();
        intidate();
        intivetn();
    }

    private void intivetn() {

    }

    private void intidate() {
        book =  (Book)getIntent().getSerializableExtra("book");
        mTvbookName.setText(book.getBookName());
        mTvbookAuthor.setText(book.getBookAuthor());
        mTvbookDetail.setText(book.getBookDescription());
        mTvbookPrice.setText("￥ " + String.valueOf(book.getBookPrice()));
        mTvbooksales.setText("销量：" + book.getBookSales());
        // 创建DisplayImageOptions对象并进行相关选项配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
/*                .showImageOnLoading(R.drawable.ic_launcher)// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_launcher)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_launcher)// 设置图片加载或解码过程中发生错误显示的图片*/
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                /*.displayer(new RoundedBitmapDisplayer(20))// 设置成圆角图片*/
                .build();// 创建DisplayImageOptions对象
        // 使用ImageLoader加载图片
        imageLoader.displayImage(book.getBookImage(),
                mTvbookImage, options);

    }

    private void intview() {
        mTvbookName = (TextView) findViewById(R.id.et_name);
        mTvbookAuthor = (TextView) findViewById(R.id.et_auth);
        mTvbookPrice = (TextView) findViewById(R.id.et_price);
        mTvbookDetail = (TextView) findViewById(R.id.et_salesDetail);
        mTvbookImage = (ImageView) findViewById(R.id.book_img);
        mTvbooksales = (TextView) findViewById(R.id.et_sales);

    }

    public void back(View view) {
        finish();
    }

    public void addCart(View view) throws Exception {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String str = (String) msg.obj;
                if(str.equals("加入成功")) {
                    Toast.makeText(getApplicationContext(),"添加成功，在购物车等亲",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"添加失败，再试一次哦",Toast.LENGTH_SHORT).show();
                }
            }
        };
        Thread thread = new Thread3();
        thread.start();

    }


    @Override
    protected void onDestroy() {
        // 回收该页面缓存在内存中的图片
        imageLoader.clearMemoryCache();
        super.onDestroy();
    }
}
