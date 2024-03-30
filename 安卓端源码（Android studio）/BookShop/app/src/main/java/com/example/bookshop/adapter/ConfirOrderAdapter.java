package com.example.bookshop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookshop.R;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.CartItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class ConfirOrderAdapter extends BaseAdapter {
    private ImageLoader imageLoader ;
    private Context context;
    private ArrayList<CartItem> mDate;
    private ArrayList<Book> mbooks;
    private ArrayList<String> picture_uri;
    private ViewHold viewHold;


    public ConfirOrderAdapter(Context context, ArrayList<CartItem> mDate, ArrayList<Book> mbooks, ArrayList<String> picture_uri, ImageLoader imageLoader) {
        this.context = context;
        this.mDate = mDate;
        this.mbooks = mbooks;
        this.picture_uri = picture_uri;
        this.imageLoader = imageLoader;
    }

    @Override
    public int getCount() {
        return mDate.size();
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return picture_uri.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        System.out.println("图片的size" + picture_uri.toString());
        System.out.println(mDate.size()+"11");
        System.out.println(mbooks.size() + "22");
        if(convertView==null){
            convertView= View.inflate(context, R.layout.confim_item_order, null);
            viewHold = new ViewHold();
            viewHold.TvName= (TextView) convertView.findViewById(R.id.tv_name);
            viewHold.TvPrice= (TextView) convertView.findViewById(R.id.tv_price);
            viewHold.TvCount= (TextView) convertView.findViewById(R.id.count);
            viewHold.TvImage = (ImageView) convertView.findViewById(R.id.book_img);
            convertView.setTag(viewHold);
        }else {
            viewHold= (ViewHold) convertView.getTag();
        }


        viewHold.TvName.setText("书名: "+ mbooks.get(position).getBookName());
        viewHold.TvPrice.setText("价格: "+mbooks.get(position).getBookPrice());
        viewHold.TvCount.setText("数量: "+mDate.get(position).getCartitemBookNumber());

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
        imageLoader.displayImage(picture_uri.get(position),
                viewHold.TvImage, options);

        return convertView;
    }

    public double getTotal() {
        double money = 0.0;
        for(int i = 0; i<mDate.size(); i++) {
            money += mDate.get(i).getCartitemBookNumber() * mbooks.get(i).getBookSales();
        }
        return money;
    }

    class ViewHold{
        TextView TvName;
        TextView TvPrice;
        TextView TvCount;
        ImageView TvImage;
    }
}
