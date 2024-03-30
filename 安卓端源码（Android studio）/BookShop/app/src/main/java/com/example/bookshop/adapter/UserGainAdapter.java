package com.example.bookshop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bookshop.R;
import com.example.bookshop.model.UserGain;

import java.util.ArrayList;


/**
 * 书籍适配器
 */
public class UserGainAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<UserGain> mDate;
	private ViewHold viewHold;
	public UserGainAdapter(Context context, ArrayList<UserGain> mDate) {
		this.context = context;
		this.mDate = mDate;
	}
	@Override
	public int getCount() {
		return mDate.size();
	}
	@Override
	public Object getItem(int position) {
		return null;
	}
	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView= View.inflate(context, R.layout.item_gain_show, null);
			viewHold = new ViewHold();
			viewHold.TvID = (TextView)  convertView.findViewById(R.id.et_id);
			viewHold.TvAddress = (TextView)  convertView.findViewById(R.id.et_address);
			viewHold.TvPhone = (TextView)  convertView.findViewById(R.id.et_phone);
			viewHold.TvDelete= (TextView) convertView.findViewById(R.id.et_delete);
			viewHold.TvBuy= (TextView) convertView.findViewById(R.id.et_update);
			convertView.setTag(viewHold);
		}else {
			viewHold= (ViewHold) convertView.getTag();
		}
			viewHold.TvID.setText(position+1+"");
			viewHold.TvAddress.setText(mDate.get(position).getAddress());
			viewHold.TvPhone.setText(mDate.get(position).getPhone());
			viewHold.TvDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(onClickListner !=null){
					onClickListner.del(position);
				}
			}
		});viewHold.TvBuy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(onClickListner!=null){
					onClickListner.update(position);
				}
			}
		});
		return convertView;
	}

	public interface OnClickListner {
		void del(int pos);
		void update(int pos);
	}


	public OnClickListner onClickListner;
	public void setOnClickListner(OnClickListner litner){
		this.onClickListner =litner;
	}

	class ViewHold{
		TextView TvID;
		TextView TvAddress;
		TextView TvPhone;
		TextView TvDelete;
		TextView TvBuy;
	}
}
