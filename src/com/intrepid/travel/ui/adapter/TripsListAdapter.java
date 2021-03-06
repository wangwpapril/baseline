package com.intrepid.travel.ui.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.intrepid.travel.R;
import com.intrepid.travel.models.Destination;
import com.intrepid.travel.ui.activity.BaseActivity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

public class TripsListAdapter extends MyBaseAdapter {


	private List<Destination> datas;
	private List<Destination> datas_clone;
	
	private Filter filter;


	public TripsListAdapter(List<Destination> datas,
			BaseActivity context) {
		this.datas = datas;
		this.datas_clone = datas;
		this.context = context;
		super.init();
	}

	@Override
	public int getCount() {
		return datas == null ? 0 : datas.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public Filter getFilter(Activity activity)
	{
		if (filter == null)
		{
			filter = new TripListFilter(activity, this);
			return filter;
		}
		
		return filter;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.trip_list_item,
					null);

			holder.ivIcon = (ImageView) convertView
					.findViewById(R.id.country_flag_item_iv);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.country_name);
			holder.tvDesc = (TextView) convertView
					.findViewById(R.id.country_desc);
			holder.llBottomLine = convertView
					.findViewById(R.id.category_bottom_gray_line);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(position == datas.size()-1){
			holder.llBottomLine.setVisibility(View.VISIBLE);
		}else{
			holder.llBottomLine.setVisibility(View.GONE);
		}
		Destination model = datas.get(position);
		holder.tvName.setText(model.name);
		holder.tvDesc.setText(model.type);
		final ImageView imageView = holder.ivIcon;
		imageView.setTag(model.imageFlag.version3.sourceUrl);
//		ImageLoader.DisplayImage(null, context, imageView);
		ImageLoader.DisplayImage(model.imageFlag.version3.sourceUrl, context, imageView);
		convertView.setTag(holder);
		return convertView;
	}

	private class ViewHolder {
		public ImageView ivIcon;
		public TextView tvName;
		public TextView tvDesc;
		public View llBottomLine;
	}

	private class TripListFilter extends Filter {

		private Activity mActivity;
		private BaseAdapter mAdapter;

		public TripListFilter(Activity activity, BaseAdapter adapter)
		{
			mActivity = activity;
			mAdapter = adapter;
		}
		

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			
			constraint = constraint.toString().toLowerCase(Locale.getDefault());
			FilterResults results = new FilterResults();
			
			if (constraint != null && constraint.toString().length() > 0)
			{
				
				List<Destination> filteredDatas = new ArrayList<Destination>();
				
				for (int i=0; i < datas.size(); i++)
				{
					Destination tmpData = datas.get(i);
					String name = tmpData.name;
					name = name.toLowerCase(Locale.getDefault());
					
					if (name.contains(constraint))
						filteredDatas.add(tmpData);
				}
				results.count = filteredDatas.size();
				results.values = filteredDatas;
			}
			else
			{
				datas = datas_clone;
				synchronized (this) {
					results.count = datas.size();
					results.values = datas;
				}
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
							
				datas = (List<Destination>) results.values;
				if (results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}


		}

	}
}
