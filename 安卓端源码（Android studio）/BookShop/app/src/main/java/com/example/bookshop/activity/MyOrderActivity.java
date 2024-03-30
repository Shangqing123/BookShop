package com.example.bookshop.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.bookshop.R;
import com.example.bookshop.adapter.BookOrderAdapter;
import com.example.bookshop.model.OrderDetail;
import com.example.bookshop.model.OrderShow;
import com.example.bookshop.utils.HttpClientHelper;
import com.example.bookshop.utils.SetUtils;
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
 * 我 的订单
 */
public class MyOrderActivity extends AppCompatActivity {

    private static final int START_DOWNLOAD_MESSAGE = 0x01;
    private static final int FINISH_DOWNLOAD_MESSAGE = 0x02;
    private static final int ERROR_DOWNLOAD_MESSAGE = 0x03;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ArrayList<String> picture_uri = new ArrayList<>();
    private BookOrderAdapter adapter;
    private ArrayList<OrderDetail> mDate=new ArrayList<>();
    private ListView mList;
    private TextView mTvState;
    private MyHandler handler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        intview();
        initdate();
        intievnet();
    }

    //初始化事件的反应，当加入购物车时的反应
    private void intievnet() {
        adapter.setOnDelLitner(new BookOrderAdapter.OnDelLitner() {
            @Override
            public void update(final int pos) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyOrderActivity.this);
                builder.setMessage("确认要进行收货吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {   @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();}
                });
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {   @Override
                public void onClick(DialogInterface dialog, int which) {
                    /* 设置提交订单为不可点击 */
                    mDate.get(pos).setState(true);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),"提交成功",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                });
                builder.create().show();
            }
        });
    }

    private void initdate() {
        mDate.clear();
        String userId = SetUtils.GetId(getApplicationContext());
        QueryAllRunnable runnable = new QueryAllRunnable();
        runnable.setMyHandler(handler);
        runnable.setUrl(getResources().getString(R.string.url)+"/getOrderShow?userId="+userId);
        new Thread(runnable).start();
    }

    private void intview() {
        mTvState = (TextView) findViewById(R.id.tv_state);
        mList = (ListView) findViewById(R.id.list);
        adapter = new BookOrderAdapter(getApplicationContext(),mDate,picture_uri,imageLoader);
        mList.setAdapter(adapter);
        handler = new MyHandler();
    }

    public void back(View view) {
        finish();
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
            System.out.println(url);
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


    private class MyHandler extends Handler{
        @Override
        public void dispatchMessage(Message msg) {
            switch(msg.what){
                case START_DOWNLOAD_MESSAGE:
                    Toast.makeText(getApplicationContext(), "开始加载", Toast.LENGTH_SHORT).show();
                    Log.e("error","start");
                    break;

                case FINISH_DOWNLOAD_MESSAGE:
                    Toast.makeText(getApplicationContext(), "加载成功", Toast.LENGTH_SHORT).show();
                    Log.e("error","finish");
                    List<OrderShow> orderShows = JSON.parseArray((String)msg.obj, OrderShow.class);
                    for(OrderShow orderShow:orderShows){
                        if (orderShow.getOrderType()==null){
                            orderShows.remove(orderShow);
                        }
                    }
                    if(orderShows !=null) {
                        if(orderShows.size()==0){
                            mList.setVisibility(View.GONE);
                            mTvState.setVisibility(View.VISIBLE);
                            mTvState.setText("暂无数据");
                        }else {
                            for(OrderShow orderShow:orderShows){
                                Log.e("edeeeeeeeeee",orderShow.toString());
                                picture_uri.add(orderShow.getBookImage());
                                OrderDetail orderDetail = new OrderDetail();
                                orderDetail.setTvAddress(orderShow.getOrderAddress());
                                orderDetail.setTvCount(orderShow.getOrderitemBookNumber());
                                orderDetail.setTvPrice(orderShow.getBookPrice());
                                orderDetail.setTvName(orderShow.getBookName());
                                orderDetail.setTvPhone(orderShow.getOrderPhone());
                                orderDetail.setTvPrice(orderShow.getBookPrice());
                                orderDetail.setTvOrderAll(orderShow.getBookPrice()*orderShow.getOrderitemBookNumber());
                                if(orderShow.getOrderType().equals("noreceive")){
                                    orderDetail.setState(false);
                                }else if(orderShow.getOrderType().equals("receive")){
                                    orderDetail.setState(true);
                                }
                                mDate.add(orderDetail);
                            }
                        }
                    }

                        mList.setVisibility(View.VISIBLE);
                        mTvState.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    //简单起见，直接输出了。
                    System.out.println(msg.obj);
                    break;

                case ERROR_DOWNLOAD_MESSAGE:
                    Toast.makeText(getApplicationContext(), "下载失败" + msg.obj, Toast.LENGTH_SHORT).show();
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
