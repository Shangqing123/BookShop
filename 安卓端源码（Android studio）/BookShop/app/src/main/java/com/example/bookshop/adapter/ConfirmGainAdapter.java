package com.example.bookshop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bookshop.R;
import com.example.bookshop.model.UserGain;
import com.example.bookshop.utils.SetUtils;

import java.util.ArrayList;
import java.util.Set;


/**
 * 书籍适配器
 */
public class ConfirmGainAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<UserGain> mDate;
	private ViewHold viewHold;
	public ConfirmGainAdapter(Context context, ArrayList<UserGain> mDate) {
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
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView= View.inflate(context, R.layout.confim_item_gain, null);
			viewHold = new ViewHold();
			viewHold.TvAddress = (TextView)  convertView.findViewById(R.id.et_address);
			viewHold.TvPhone = (TextView)  convertView.findViewById(R.id.et_phone);
			viewHold.TvName = (TextView) convertView.findViewById(R.id.et_name);
			convertView.setTag(viewHold);
		}else {
			viewHold= (ViewHold) convertView.getTag();
		}

		viewHold.TvName.setText(SetUtils.GetUserName(context));
		viewHold.TvAddress.setText("地址" + mDate.get(position).getAddress());
		viewHold.TvPhone.setText(mDate.get(position).getPhone());
		if(mDate.get(position).getType().equals("默认地址")) {
			viewHold.TvName.setTextColor(context.getResources().getColor(R.color.red_dark));
			viewHold.TvAddress.setTextColor(context.getResources().getColor(R.color.red_dark));
			viewHold.TvPhone.setTextColor(context.getResources().getColor(R.color.red_dark));
		}
		return convertView;
	}


	class ViewHold{
		TextView TvName;
		TextView TvAddress;
		TextView TvPhone;
	}
}
