package com.example.bookshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.bookshop.R;
import com.example.bookshop.model.Book;
import com.example.bookshop.utils.HttpClientHelper;
import com.example.bookshop.utils.RealPathFromUriUtils;
import com.example.bookshop.utils.UploadUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 修改书籍界面
 */
//定义了三个编辑框的字段名mEtName、mEtPrice、mEtAuth、mEtCategory、mEDescription、imageView；
// 创建onCreate方法使界面显示布局文件activity_change_book。
// 定义三个方法， intview();、intidate();、intivetn();
public class UpdateBookActivity extends AppCompatActivity {

    private static final int START_UPDATE = 0x01;
    private static final int FINISH_UPDATE = 0x02;
    private static final int ERROR_UPDATE = 0x03;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private EditText mEtName;
    private EditText mEtPrice;
    private EditText mEtAuth;
    private EditText mEtCategory;
    private EditText mEDescription;
    private ImageView imageView;

    private String picture_uri;
    private String requestURL ="http://47.100.121.231:8080/updateBookImg";
    private String pickUri;
    private String  bookName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_book);
        intview();
        intidate();
        intivetn();
    }

    private void intivetn() {

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
        imageLoader.displayImage(picture_uri,
                imageView, options);
    }

    /**
     * 初始化数据，服务端获取的数据写入安卓框
     */
    private void intidate() {
        Bundle bundle = this.getIntent().getExtras();
        Book book = (Book) bundle.getSerializable("book");
        bookName = book.getBookName();
        mEtName.setText(book.getBookName());
        mEtPrice.setText(book.getBookPrice().toString());
        mEtAuth.setText(book.getBookAuthor());
        mEtCategory.setText(book.getBookCategory());
        mEDescription.setText(book.getBookDescription());
        picture_uri = book.getBookImage();
    }

    /**
     * 初始化视图，从安卓的获取数据，为写回服务器准备
     */
    private void intview() {
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtPrice = (EditText) findViewById(R.id.et_price);
        mEtAuth = (EditText) findViewById(R.id.et_auth);
        mEtCategory = (EditText) findViewById(R.id.et_category);
        mEDescription = (EditText) findViewById(R.id.et_salesDetail);
        imageView = (ImageView) findViewById(R.id.book_img);
    }

    /**
     * 从相册加载图片
     * @param view
     */
    public void upload(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            imageView.setImageURI(data.getData());
            pickUri = RealPathFromUriUtils.getRealPathFromUri(this, data.getData());
            System.out.println(pickUri);
        }


    }

    public void back(View view) {
        finish();
    }

    /**
     * 确认添加
     * @param view
     */
    //updateComfirm()方法，创建一个新的书籍，使能创建书籍名称、作者、价格。字段类型为boolean类型。
    // 当添加成功时，成功保存书籍，并且设置结果集为1.反之则添加失败。
    
    public void updateComfirm(View view) {
        if(mEtAuth.getText().toString().trim().equals("")||mEtCategory.getText().toString().trim().equals("")
                ||mEtName.getText().toString().trim().equals("")||mEDescription.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"请输入完整",Toast.LENGTH_SHORT).show();
        }else {
            Bundle bundle = this.getIntent().getExtras();
            String book_id = bundle.getString("book_id");
            Book book = new Book();
            book.setBookId(Integer.parseInt(book_id));
            book.setBookName(mEtName.getText().toString().trim());
            book.setBookAuthor(mEtAuth.getText().toString().trim());
            book.setBookPrice(Double.parseDouble(mEtPrice.getText().toString().trim()));
            book.setBookCategory(mEtCategory.getText().toString().trim());
            book.setBookDescription(mEDescription.getText().toString().trim());
            String url = getResources().getString(R.string.url)+"/updateBook";
            Handler deleteHandler = new Handler();
            UPDATERunnable runnable = new UPDATERunnable();
            runnable.setUrl(url);
            runnable.setBook(book);
            runnable.setMyHandler(deleteHandler);
            new Thread(runnable).start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent();
            intent.setClass(UpdateBookActivity.this,BookManagerActivity.class);//回到书籍管理界面
            startActivity(intent);
            finish();
        }
    }
    class UPDATERunnable implements Runnable{
        private Handler myHandler;
        private String url;
        private Book book;

        public Book getBook() {
            return book;
        }

        public void setBook(Book book) {
            this.book = book;
        }

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
            HttpPost httpPost = new HttpPost(url);
            //为这个HttpGet设置一些特定的属性，别的属性沿用HttpClient
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 60000);
            httpPost.setParams(params);
            try {
                httpPost.setEntity(new StringEntity(JSONObject.toJSONString(book), HTTP.UTF_8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            myHandler.sendEmptyMessage(START_UPDATE);

            try {

                HttpResponse httpResponse = HttpClientHelper.getHttpClient().execute(httpPost);;

                byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());
                //在大多数情况下，这个下载下来的是XML或者Json。应该解析完组装成对象再放置到Message中。
                //这里简单起见，直接变成字符串打印了
                String result = new String(bytes);

                Message msg = myHandler.obtainMessage();
                msg.what = FINISH_UPDATE;
                msg.obj = result;
                System.out.println(result);
                Log.e("pan",result.toString());
                myHandler.sendMessage(msg);
                Looper.prepare();
                if(result.equals("Update Success")){
                    Map<String, String> param2s = new HashMap<String, String>();
                    param2s.put("bookName", bookName);
                    UploadUtil.getInstance().uploadFile(pickUri,"imgFile",requestURL,param2s);
                    Toast.makeText(getApplicationContext(),"更新成功",Toast.LENGTH_SHORT).show();
                    setResult(1);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"更新失败",Toast.LENGTH_SHORT).show();
                }
                Looper.loop();
    } catch (Exception ex){
        ex.printStackTrace();
        myHandler.sendEmptyMessage(ERROR_UPDATE);
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
