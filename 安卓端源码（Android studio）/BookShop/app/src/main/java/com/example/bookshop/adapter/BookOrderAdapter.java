package com.example.bookshop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookshop.R;
import com.example.bookshop.model.OrderDetail;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


/**
 * 书籍订单适配器
 */
public class BookOrderAdapter extends BaseAdapter {
	private ImageLoader imageLoader ;
	private ArrayList<String> picture_uri;
	private Context context;
	private ArrayList<OrderDetail> mDate;
	private ViewHold viewHold;

	public BookOrderAdapter(Context context, ArrayList<OrderDetail> mDate,ArrayList<String> picture_uri,ImageLoader imageLoader) {
		this.context = context;
		this.mDate = mDate;
		this.picture_uri = picture_uri;
		this.imageLoader = imageLoader;
	}
	@Override
	public int getCount() {
		return mDate.size();
	}
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
		if(convertView==null){
			convertView= View.inflate(context, R.layout.item_book_order, null);
			viewHold = new ViewHold();
			viewHold.TvName= (TextView) convertView.findViewById(R.id.tv_name);
			viewHold.TvPrice= (TextView) convertView.findViewById(R.id.tv_price);
			viewHold.Tvsure = (Button) convertView.findViewById(R.id.tv_sure);
			viewHold.TvCount= (TextView) convertView.findViewById(R.id.sell_count);
			viewHold.imageView = (ImageView) convertView.findViewById(R.id.book_img);
			convertView.setTag(viewHold);
		}else {
			viewHold= (ViewHold) convertView.getTag();
		}
		   viewHold.TvName.setText("书名:"+mDate.get(position).getTvName());
		   viewHold.TvPrice.setText("价格:"+mDate.get(position).getTvPrice());
		   viewHold.TvCount.setText("数量:"+mDate.get(position).getTvCount());

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
				viewHold.imageView, options);

			   if(mDate.get(position)!=null && mDate.get(position).getState()){
				   viewHold.Tvsure.setText("已收货");
				   viewHold.Tvsure.setEnabled(true);
			   }else {
				   viewHold.Tvsure.setText("确定收货");
			   }


		viewHold.Tvsure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(onDelLitner!=null){
					onDelLitner.update(position);
				}
			}
		});
		return convertView;
	}

	public interface OnDelLitner{
		void update(int pos);
	}

	public OnDelLitner onDelLitner;
	public void setOnDelLitner(OnDelLitner litner){
		this.onDelLitner=litner;
	}


	class ViewHold{
		ImageView imageView;
		Button Tvsure;
		TextView TvName;
		TextView TvPrice;
		TextView TvCount;
	}
}
