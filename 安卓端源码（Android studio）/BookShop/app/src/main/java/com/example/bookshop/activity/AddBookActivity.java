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
 * 添加书籍界面
 */
//定义了三个编辑框的字段名mEtName、mEtPrice、mEtAuth、mEtCategory、mEDescription、imageView;
// 创建onCreate方法使界面显示布局文件activity_add_book。
// 定义三个方法， intview();、intidate();、intivetn();
public class AddBookActivity extends AppCompatActivity {

    private static final int START_ADD = 0x01;
    private static final int FINISH_ADD = 0x02;
    private static final int ERROR_ADD = 0x03;
    private static String requestURL = "http://47.100.121.231:8080/addBookImage";
    private EditText mEtName;
    private EditText mEtPrice;
    private EditText mEtAuth;
    private EditText mEtCategory;
    private EditText mEDescription;
    private ImageView imageView;

    private String pickUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        intview();
        intidate();
        intivetn();
    }

    private void intivetn() {

    }

    private void intidate() {
    }

    /**
     * 从安卓段获取输入的信息
     */
    private void intview() {
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtPrice = (EditText) findViewById(R.id.et_price);
        mEtAuth = (EditText) findViewById(R.id.et_auth);
        mEtCategory = (EditText) findViewById(R.id.et_category);
        mEDescription = (EditText) findViewById(R.id.et_salesDetail);
        imageView = (ImageView) findViewById(R.id.book_img);
    }

    public void back(View view) {
        finish();
    }

    /**
     * 从手机相册上传图片
     * @param view
     */
    public void upload(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,1);           //打开相册
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            Toast.makeText(getApplicationContext(),data.getData().toString(),Toast.LENGTH_SHORT).show();
            imageView.setImageURI(data.getData());
            pickUri = RealPathFromUriUtils.getRealPathFromUri(this, data.getData());
            System.out.println(pickUri);
        }


    }


    /**
     * 确认添加按钮
     * @param view
     */
    //addComfirm()方法，创建一个新的书籍，使能创建书籍名称、作者、价格。字段类型为boolean类型。
    // 当添加成功时，成功保存书籍，并且设置结果集为1.反之则添加失败。
    
    public void addComfirm(View view)  {
        if(mEtAuth.getText().toString().trim().equals("")||mEtCategory.getText().toString().trim().equals("")
                ||mEtName.getText().toString().trim().equals("")||mEDescription.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"请输入完整",Toast.LENGTH_SHORT).show();
        }else {
            Book book = new Book();
            book.setBookName(mEtName.getText().toString().trim());
            book.setBookAuthor(mEtAuth.getText().toString().trim());
            book.setBookPrice(Double.parseDouble(mEtPrice.getText().toString().trim()));
            book.setBookDescription(mEDescription.getText().toString().trim());
            book.setBookCategory( mEtCategory.getText().toString().trim());
            String url = getResources().getString(R.string.url)+"/addBook";
            Handler deleteHandler = new Handler();
            AddRunnable runnable = new AddRunnable();
            runnable.setUrl(url);        //保存地址，书籍，异步消息
            runnable.setBook(book);
            runnable.setMyHandler(deleteHandler);
            new Thread(runnable).start();  //开启线程
                try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            intent.setClass(getApplicationContext(),BookManagerActivity.class);//成功后到达书籍管理界面
            startActivity(intent);

            finish();
        }
    }

    //定义置子线程,异步消息处理机制
    class AddRunnable implements Runnable{
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

        //网络请求设置，设置子线程，处理来自服务端的反馈，添加成功或者失败
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

            myHandler.sendEmptyMessage(START_ADD);

            try {

                HttpResponse httpResponse = HttpClientHelper.getHttpClient().execute(httpPost);;

                byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());
                //在大多数情况下，这个下载下来的是XML或者Json。应该解析完组装成对象再放置到Message中。
                //这里简单起见，直接变成字符串打印了
                String result = new String(bytes);

                //异步消息处理机制
                Message msg = myHandler.obtainMessage();
                msg.what = FINISH_ADD;
                msg.obj = result;
                Log.e("pan",result.toString());
                myHandler.sendMessage(msg);
                Looper.prepare();
                if(result.equals("Add Success")){
                    Map<String, String> param2s = new HashMap<String, String>();
                    param2s.put("bookName", mEtName.getText().toString().trim());
                    UploadUtil.getInstance().uploadFile(pickUri,"imgFile",requestURL,param2s);
                    Toast.makeText(getApplicationContext(),"添加成功",Toast.LENGTH_SHORT).show();
                    setResult(1);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"添加失败",Toast.LENGTH_SHORT).show();
                }
                Looper.loop();
    } catch (Exception ex){
        ex.printStackTrace();
        myHandler.sendEmptyMessage(ERROR_ADD);
    }
}
    }

}
