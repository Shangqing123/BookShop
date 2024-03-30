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
public class BookAdapter extends BaseAdapter {
	private ImageLoader imageLoader ;			//ImageLoader加载图片
	private ArrayList<String> picture_uri ;		//图片URL列表
	private Context context;
	private ArrayList<Book> mDate;		//书籍列表
	private ViewHold viewHold;			//存放缓存

	public BookAdapter(Context context, ArrayList<Book> mDate,ArrayList<String> picture_uri,ImageLoader imageLoader) {
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
		if(convertView==null){					//未缓存过书籍信息
			convertView= View.inflate(context, R.layout.item_book, null);
			viewHold = new ViewHold();
			viewHold.TvAuth= (TextView) convertView.findViewById(R.id.tv_auth);
			viewHold.TvName= (TextView) convertView.findViewById(R.id.tv_name);
			viewHold.TvPrice= (TextView) convertView.findViewById(R.id.tv_price);
			viewHold.TvDelete= (TextView) convertView.findViewById(R.id.tv_delete);
			viewHold.TvBuy= (TextView) convertView.findViewById(R.id.tv_buy);
			viewHold.TvImage = (ImageView) convertView.findViewById(R.id.tv_bookimg);
			convertView.setTag(viewHold);		//将控件的实例都放进ViewHolder里，然后存进view
		}else {
			viewHold= (ViewHold) convertView.getTag();		//重新调出ViewHolder的控件用例
		}
		   viewHold.TvName.setText("书名:"+mDate.get(position).getBookName());    //get(position)是获取当前book实例
		   viewHold.TvPrice.setText("价格:"+mDate.get(position).getBookPrice());
		   viewHold.TvAuth.setText("作者:"+mDate.get(position).getBookAuthor());

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

		viewHold.TvDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(onDelLitner!=null){
					onDelLitner.del(position);
				}
			}
		});
		viewHold.TvBuy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(onBuyLitner!=null){
					onBuyLitner.buy(position,mDate.get(position).getBookId());
				}
			}
		});
		return convertView;//返回布局
	}

	//删除购物车书籍
	public interface OnDelLitner{
		void del(int pos);
	}
	//加入购物车书籍
	public interface OnBuyLitner{
		void buy(int pos,int bookId);
	}

	public OnDelLitner onDelLitner;

	public OnBuyLitner onBuyLitner;

	public void setOnDelLitner(OnDelLitner litner){
		this.onDelLitner=litner;
	}

	public void setOnBuyLitner(OnBuyLitner litner){
		this.onBuyLitner=litner;
	}

	//定义ViewHold缓存
	class ViewHold{
		TextView TvName;
		TextView TvPrice;
		TextView TvAuth;
		TextView TvDelete;
		TextView TvBuy;
		ImageView TvImage;
	}
}
