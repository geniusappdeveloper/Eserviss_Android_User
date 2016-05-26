package com.app.taxisharingDriver;



import java.util.ArrayList;



import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerListAdapter extends BaseAdapter 
{
	
	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private Typeface robotoBoldCondensedItalic;
	public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
		  robotoBoldCondensedItalic = Typeface.createFromAsset(context.getAssets(), "fonts/Zurich Condensed.ttf");
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {		
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{

		if (convertView == null)
		{
			LayoutInflater mInflater = (LayoutInflater)
					context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
		}

		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		//TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
		//android.widget.RelativeLayout drawer_list_item_maillayout=(android.widget.RelativeLayout)convertView.findViewById(R.id.drawer_list_item_maillayout);
		//drawer_list_item_maillayout.setBackgroundResource(navDrawerItems.get(position).getIcon());
		txtTitle.setText(navDrawerItems.get(position).getTitle());
		//txtTitle.setPadding(140, 0, 0, 0);
		txtTitle.setTypeface(robotoBoldCondensedItalic);
		imgIcon.setBackgroundResource(navDrawerItems.get(position).getIcon());

		//imgIcon.setVisibility(View.GONE);
		// displaying count
		// check whether it set visible or not
		if(navDrawerItems.get(position).getCounterVisibility()){
		}else{
		}

		return convertView;
	}

}
