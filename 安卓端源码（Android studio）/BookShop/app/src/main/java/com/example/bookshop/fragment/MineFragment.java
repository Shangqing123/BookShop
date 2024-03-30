package com.example.bookshop.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bookshop.R;
import com.example.bookshop.activity.LoginActivity;
import com.example.bookshop.activity.MainActivity;
import com.example.bookshop.activity.MainGainActivity;
import com.example.bookshop.activity.MyOrderActivity;
import com.example.bookshop.activity.ShopCartActivity;
import com.example.bookshop.utils.LocationUtils;
import com.example.bookshop.utils.SetUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;

/**
 * 我的页面
 */
public class MineFragment extends Fragment {

    private static final int CITY_CHANGE = 1;

    private TextView mTvId;
    private LocationManager locationManager;
    private String locationProvider;
    private String city = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_mine, null);
        initview(layout);
        initdate();
        return layout;
    }

    private void initdate() {
        Map<String ,Double> map = LocationUtils.getInstance().getLocations(getContext());
        mTvId.setText("您好:" + SetUtils.GetUserName(getActivity()) + "\n" + "经度:" + map.get("latitude")+ "\n" + "纬度:" + map.get("longitude"));
    }


    /**
     * 初始化布局信息
     * @param layout
     */
    private void initview(View layout) {
        mTvId = (TextView) layout.findViewById(R.id.tv_id);
        layout.findViewById(R.id.rtl_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyOrderActivity.class));//我的订单界面
            }
        });
        layout.findViewById(R.id.shop_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ShopCartActivity.class));//购物车界面
            }
        });
        layout.findViewById(R.id.goods_receive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainGainActivity.class));//订单管理界面
            }
        });
        layout.findViewById(R.id.rtl_exist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("确认要退出？");
                builder.setTitle("提示");
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SetUtils.saveIsLogin(getActivity(), false);
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }



}
