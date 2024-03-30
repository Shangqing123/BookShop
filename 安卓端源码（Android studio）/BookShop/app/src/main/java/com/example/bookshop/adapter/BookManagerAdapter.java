package com.example.bookshop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookshop.R;
import com.example.bookshop.model.Book;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


/**
 * 书籍适配器
 */
public class BookManagerAdapter extends BaseAdapter {
	private ImageLoader imageLoader ;
	private ArrayList<String> picture_uri ;
	private Context context;
	private ArrayList<Book> mDate;
	private ViewHold viewHold;
	public BookManagerAdapter(Context context, ArrayList<Book> mDate,ArrayList<String> picture_uri,ImageLoader imageLoader) {
		this.context = context;
		this.mDate = mDate;
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
		if(position==0){
			System.out.println("position为0" + position);
		}

		if(convertView==null){
			convertView= View.inflate(context, R.layout.item_book, null);
			viewHold = new ViewHold();
			viewHold.TvAuth= (TextView) convertView.findViewById(R.id.tv_auth);
			viewHold.TvName= (TextView) convertView.findViewById(R.id.tv_name);
			viewHold.TvPrice= (TextView) convertView.findViewById(R.id.tv_price);
			viewHold.TvDelete= (TextView) convertView.findViewById(R.id.tv_delete);
			viewHold.TvBuy= (TextView) convertView.findViewById(R.id.tv_buy);
			viewHold.Image = (ImageView) convertView.findViewById(R.id.tv_bookimg);
			convertView.setTag(viewHold);
		}else {
			viewHold= (ViewHold) convertView.getTag();
		}
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
				viewHold.Image, options);

		viewHold.TvName.setText("书名:"+mDate.get(position).getBookName());
		   viewHold.TvPrice.setText("价格:"+mDate.get(position).getBookPrice());
		   viewHold.TvAuth.setText("作者:"+mDate.get(position).getBookAuthor());
		viewHold.TvBuy.setVisibility(View.GONE);
		viewHold.TvDelete.setVisibility(View.VISIBLE);
		viewHold.TvDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(onDelLitner!=null){
					onDelLitner.del(position);
				}
			}
		});viewHold.TvBuy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(onBuyLitner!=null){
					onBuyLitner.buy(position);
				}
			}
		});
		return convertView;
	}

	public interface OnDelLitner{
		void del(int pos);
	}
	public interface OnBuyLitner{
		void buy(int pos);
	}

	public OnDelLitner onDelLitner;
	public OnBuyLitner onBuyLitner;
	//删除书籍
	public void setOnDelLitner(OnDelLitner litner){
		this.onDelLitner=litner;
	}

	public void setOnBuyLitner(OnBuyLitner litner){
		this.onBuyLitner=litner;
	}

	class ViewHold{
		TextView TvName;
		TextView TvPrice;
		TextView TvAuth;
		TextView TvDelete;
		TextView TvBuy;
		ImageView Image;
	}
}
