package com.example.bookshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bookshop.R;
import com.example.bookshop.adapter.ConfirmGainAdapter;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.CartItem;
import com.example.bookshop.model.UserGain;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class ConfirmGainActivity extends AppCompatActivity {
    private ArrayList<String> picture_uri;
    private ArrayList<CartItem> cartItems ;
    private ArrayList<Book> books;
    private ArrayList<UserGain> mDate=new ArrayList<>();
    private ListView mList;
    private ConfirmGainAdapter adapter;
    private TextView mTvState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confim_gain);
        intview();
        initdate();
        intievnet();
    }

    private void intievnet() {
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                UserGain userGain = mDate.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("cartItems",cartItems);
                bundle.putSerializable("userGain",userGain);
                bundle.putSerializable("books",books);
                bundle.putSerializable("picture_uri",picture_uri);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(getApplicationContext(), ConfirmActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initdate() {
        List<UserGain> gainList = LitePal.findAll(UserGain.class);
        mDate.clear();
        if(gainList.size()==0){
            mList.setVisibility(View.GONE);
            mTvState.setVisibility(View.VISIBLE);
            mTvState.setText("暂无数据");
        }else {
            mDate.addAll(gainList);
            mList.setVisibility(View.VISIBLE);
            mTvState.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();

    }

    private void intview() {
        picture_uri = (ArrayList<String>) getIntent().getSerializableExtra("picture_uri");
        cartItems = (ArrayList<CartItem>) getIntent().getSerializableExtra("cartItems");
        books = (ArrayList<Book>) getIntent().getSerializableExtra("books");
        mTvState = (TextView) findViewById(R.id.tv_state);
        mList = (ListView) findViewById(R.id.list);
        adapter = new ConfirmGainAdapter(getApplicationContext(),mDate);
        mList.setAdapter(adapter);

    }

    public void back(View view) {
        finish();
    }

}
