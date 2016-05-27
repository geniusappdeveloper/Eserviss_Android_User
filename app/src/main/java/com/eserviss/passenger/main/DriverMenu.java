package com.eserviss.passenger.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import com.flurry.android.FlurryAgent;
import com.egnyt.eserviss.R;

public class DriverMenu extends Activity implements OnTouchListener,OnClickListener
{
	private Button share_eta,contact_driver,cancel_trip;
	private Button facebook_share,twitter_share,email_share,cancel_share;
	private View popupLayout;
	private PopupWindow popup_share;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_menu);
		initialize();
	}

	private void initialize()
	{
		share_eta=(Button)findViewById(R.id.share_eta);
		contact_driver=(Button)findViewById(R.id.contact_driver);
		cancel_trip=(Button)findViewById(R.id.cancel_trip);

		//=====================change April================================================================
		Typeface roboto_condensed = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/BebasNeue.otf");
		share_eta.setTypeface(roboto_condensed);
		contact_driver.setTypeface(roboto_condensed);
		cancel_trip.setTypeface(roboto_condensed);


		//===============================================================================================
		share_eta.setOnClickListener(this);
		contact_driver.setOnClickListener(this);
		cancel_trip.setOnClickListener(this);

		showPopupForShare();
	}
	

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		// TODO Auto-generated method stub
		finish();
		return false;
	}

	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		if(v.getId()==R.id.share_eta)
		{
			//finish();
			popup_share.showAtLocation(popupLayout,Gravity.BOTTOM, 0,0); 
		}
		if(v.getId()==R.id.contact_driver)
		{
			finish();
		}
		if(v.getId()==R.id.cancel_trip)
		{
			finish();
		}
		if(v.getId()==R.id.facebook_share)
		{
			Intent intent=new Intent(DriverMenu.this,FBShareActivity.class);
			intent.putExtra("chooser", "facebook");
			startActivity(intent);
			popup_share.dismiss();
		}
		if(v.getId()==R.id.twitter_share)
		{
			Intent intent=new Intent(DriverMenu.this,FBShareActivity.class);
			intent.putExtra("chooser", "twitter");
			startActivity(intent);
			popup_share.dismiss();
			
		}
		if(v.getId()==R.id.email_share)
		{
			Intent intent=new Intent(DriverMenu.this,FBShareActivity.class);
			intent.putExtra("email_address", "");
			intent.putExtra("chooser", "email");
			startActivity(intent);
			popup_share.dismiss();
			
		}
		if(v.getId()==R.id.cancel_share)
		{
			popup_share.dismiss();
		}

	}
	
	private void showPopupForShare()
	{
		
		LayoutInflater inflater = (LayoutInflater)DriverMenu.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popupLayout = inflater.inflate(R.layout.popup_share,(ViewGroup)findViewById(R.id.share_parent));
		
		facebook_share=(Button)popupLayout.findViewById(R.id.facebook_share);
		twitter_share=(Button)popupLayout.findViewById(R.id.twitter_share);
		email_share=(Button)popupLayout.findViewById(R.id.email_share);
		cancel_share=(Button)popupLayout.findViewById(R.id.cancel_share);
		//====================chnage april================================================================
		Typeface roboto_condensed = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/BebasNeue.otf");
		facebook_share.setTypeface(roboto_condensed);
		twitter_share.setTypeface(roboto_condensed);
		email_share.setTypeface(roboto_condensed);
		cancel_share.setTypeface(roboto_condensed);


		//=================================================================================================
		facebook_share.setOnClickListener(this);
		twitter_share.setOnClickListener(this);
		email_share.setOnClickListener(this);
		cancel_share.setOnClickListener(this);

		popup_share = new PopupWindow(popupLayout, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,true);
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		FlurryAgent.onStartSession(this, "8c41e9486e74492897473de501e087dbc6d9f391");
	}
	 
	@Override
	protected void onStop()
	{
		super.onStop();		
		FlurryAgent.onEndSession(this);
	}
	

}