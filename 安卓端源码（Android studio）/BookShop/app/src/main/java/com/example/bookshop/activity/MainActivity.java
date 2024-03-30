package com.example.bookshop.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookshop.R;
import com.example.bookshop.fragment.HomeFragment;
import com.example.bookshop.fragment.MineFragment;
import com.example.bookshop.fragment.ResearchFragment;
import com.example.bookshop.utils.SetUtils;

import java.util.ArrayList;

/**
 * 主页
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private static final int CITY_CHANGE = 1;

    private ViewPager mVpTabs;
    private ArrayList<Fragment> mFragments;
    private FragmentPagerAdapter mAdapter;
    private RelativeLayout mRtlMine;
    private TextView mTvMine;
    private RelativeLayout mRtlResearch;
    private TextView mTvResearch;
    private HomeFragment homeFragment;
    private MineFragment mineFragment;
    private ResearchFragment researchFragment;
    private RelativeLayout mRtlHome;
    private TextView mTvHome;
    private RelativeLayout mRtlType;
    private TextView mTvType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
        initPage();
    }

    /**
     * 初始化页面，homeFragment，mineFragment，researchFragment
     */
    private void initPage() {
        mVpTabs.setOffscreenPageLimit(0);
        mFragments=new ArrayList<Fragment>();
        if(homeFragment==null){
            homeFragment=new HomeFragment();//创建一个新的HomeFragment
        }
        if(mineFragment==null){
            mineFragment=new MineFragment();
        }if(researchFragment==null) {
            researchFragment = new ResearchFragment();
        }

        mFragments.add(homeFragment);
        mFragments.add(mineFragment);
        mFragments.add(researchFragment);
        mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.size();
            }
            @Override
            public Fragment getItem(int arg0) {
                return mFragments.get(arg0);
            }
        };
        mVpTabs.setAdapter(mAdapter);//添加适配器
        mVpTabs.setCurrentItem(0);
        mVpTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public int mCurentPageIndex;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                ResetTab();
                switch (position) {
                    case 0:
                        ChangeType(mTvHome, getResources().getDrawable(R.mipmap.homes), R.color.blue);
                        homeFragment.initdate();
                        break;
                    case 1:
                        ChangeType(mTvMine, getResources().getDrawable(R.mipmap.mines), R.color.blue);
                        break;
                    case 2:
                        ChangeType(mTvResearch, getResources().getDrawable(R.mipmap.researchs), R.color.blue);
                }
                mCurentPageIndex = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 重置Tab
     */
    public  void  ResetTab(){
        ChangeType(mTvHome, getResources().getDrawable(R.mipmap.home),R.color.black);
        ChangeType(mTvMine, getResources().getDrawable(R.mipmap.mine),R.color.black);
        ChangeType(mTvResearch, getResources().getDrawable(R.mipmap.research),R.color.black);
    }
    public  void ChangeType(TextView mTv, Drawable drawable, int color){
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTv.setCompoundDrawables(null, drawable, null, null);
        mTv.setTextColor(getResources().getColor(color));
    }
    private void initview() {
        mVpTabs = (ViewPager) findViewById(R.id.vPager);
        mRtlHome = (RelativeLayout) findViewById(R.id.rtl_home);
        mRtlMine = (RelativeLayout) findViewById(R.id.rtl_mime);
        mRtlResearch = (RelativeLayout) findViewById(R.id.rt1_research);
        mTvHome = (TextView) findViewById(R.id.tv_home);
        mTvMine = (TextView) findViewById(R.id.tv_mime);
        mTvResearch = (TextView) findViewById(R.id.tv_research);
        mRtlHome.setOnClickListener(this);
        mRtlMine.setOnClickListener(this);
        mRtlResearch.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rtl_home:
                mVpTabs.setCurrentItem(0);
                break;
            case R.id.rtl_mime:
                mVpTabs.setCurrentItem(1);
                break;
            case R.id.rt1_research:
                mVpTabs.setCurrentItem(2);
                break;
        }
    }
    private long firstTime=0;

    /**
     * 双击退出
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                long secondTime=System.currentTimeMillis();
                if(secondTime-firstTime>2000){
                    Toast.makeText(MainActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                    firstTime=secondTime;
                    return true;
                }else{
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }


}
