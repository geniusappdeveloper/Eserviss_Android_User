package com.eserviss.passenger.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.egnyt.eserviss.R;
import com.threembed.utilities.Utility;

public class AboutFragment extends Fragment implements View.OnClickListener
{
	private Button Rate_GooglePlay,Like_Facebook,Legal;
	//private TextView txt_Rate_GooglePlay,txt_Like_Facebook,txt_Legal;
	private TextView Roadyo;
	@Override
    public View onCreateView(android.view.LayoutInflater inflater,
    		android.view.ViewGroup container,
    		android.os.Bundle savedInstanceState) 
    {
 
    	View view  = inflater.inflate(R.layout.about_fragment, null);
    	initLayoutId(view);    	
    	ResideMenuActivity.invoice_driver_tip.setVisibility(View.INVISIBLE);
    	initLayoutId(view);
    	
    	if(Utility.isNetworkAvailable(getActivity()))
		{
    		
		}
		else
		{
			Intent homeIntent=new Intent("com.threembed.roadyo.internetStatus");
			homeIntent.putExtra("STATUS", "0");
			getActivity().sendBroadcast(homeIntent);
		}

    	
    	/* Rate_GooglePlay.setOnTouchListener(new View.OnTouchListener() 
		{

		    @Override public boolean onTouch(View v, MotionEvent event)
		    {
		        switch(event.getAction())
		        {
		        case MotionEvent.ACTION_DOWN:
		        	txt_Rate_GooglePlay.setTextColor(Color.parseColor("#FFFFFF"));
		            
		            break;
		        case MotionEvent.ACTION_UP:
		            
		        	txt_Rate_GooglePlay.setTextColor(Color.parseColor("#000000"));
		            break;
		        }
		        return false;
		    }
		});
    	Like_Facebook.setOnTouchListener(new View.OnTouchListener() 
		{

		    @Override public boolean onTouch(View v, MotionEvent event)
		    {
		        switch(event.getAction())
		        {
		        case MotionEvent.ACTION_DOWN:
		        	txt_Like_Facebook.setTextColor(Color.parseColor("#FFFFFF"));
		            
		            break;
		        case MotionEvent.ACTION_UP:
		            
		        	txt_Like_Facebook.setTextColor(Color.parseColor("#000000"));
		            break;
		        }
		        return false;
		    }
		});
    	Legal.setOnTouchListener(new View.OnTouchListener() 
		{
		    @Override public boolean onTouch(View v, MotionEvent event)
		    {
		        switch(event.getAction())
		        {
		        case MotionEvent.ACTION_DOWN:
		        	txt_Legal.setTextColor(Color.parseColor("#FFFFFF"));
		            
		            break;
		        case MotionEvent.ACTION_UP:
		            
		        	txt_Legal.setTextColor(Color.parseColor("#000000"));
		            break;
		        }
		        return false;
		    }
		});*/
    	 
    	return view;
    }  
	private void initLayoutId(View view)
	{
		
		Rate_GooglePlay = (Button)view.findViewById(R.id.btn_rate_in_google_play);
		Like_Facebook = (Button)view.findViewById(R.id.btn_like_in_facebook);
		Legal = (Button)view.findViewById(R.id.btn_legal);
		Roadyo = (TextView)view.findViewById(R.id.txt_roadyo_net);
		//xt_Rate_GooglePlay = (TextView)view.findViewById(R.id.txt_rate_in_google_play);
	    TextView txt_Like_Facebook = (TextView)view.findViewById(R.id.txt_like_in_facebook);
		TextView txt_Legal = (TextView)view.findViewById(R.id.txt_legal);
		TextView txt_private_driver= (TextView) view.findViewById(R.id.txt_private_driver);


		//==============================change April========================================
		Typeface roboto_condensed = Typeface.createFromAsset(getActivity().getAssets(),"fonts/BebasNeue.otf");
		Rate_GooglePlay.setTypeface(roboto_condensed);
		Like_Facebook.setTypeface(roboto_condensed);
		Legal.setTypeface(roboto_condensed);
		Roadyo.setTypeface(roboto_condensed);
		txt_Like_Facebook.setTypeface(roboto_condensed);
		txt_Legal.setTypeface(roboto_condensed);
		txt_private_driver.setTypeface(roboto_condensed);
		//====================================================================================
		Rate_GooglePlay.setOnClickListener(this);
		Like_Facebook.setOnClickListener(this);
		Legal.setOnClickListener(this);
		Roadyo.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) 
	{
		if (v.getId()==R.id.txt_roadyo_net)
		{
			
    		if (Utility.isNetworkAvailable(getActivity())) 
    		{
    			String url = "http://roadyo.net/";
        		Intent i = new Intent(Intent.ACTION_VIEW);
        		i.setData(Uri.parse(url));
        		startActivity(i);
    		}
    		else 
    		{
    			//utility.showDialogConfirm(getActivity(),"Alert"," working internet connection required", false).show();
    		//	utility.displayMessageAndExit(getActivity(),"Alert"," working internet connection required");
    			Utility.ShowAlert("Network connection fail", getActivity());
    		}
    		
		}
		if (v.getId()==R.id.btn_rate_in_google_play) 
		{
			if (Utility.isNetworkAvailable(getActivity())) 
    		{
				Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
 				Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
 				try {
 				  startActivity(goToMarket);
 				} catch (ActivityNotFoundException e)
 				{
 				  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
 				}
    		}
    		else 
    		{
    			//utility.showDialogConfirm(getActivity(),"Alert"," working internet connection required", false).show();
    			Utility.ShowAlert("Network connection fail", getActivity());

    		}
		}
		if (v.getId()==R.id.btn_like_in_facebook) 
		{
			if (Utility.isNetworkAvailable(getActivity())) 
    		{
				String url = "https://www.facebook.com/roadyo";
        		Intent i = new Intent(Intent.ACTION_VIEW);
        		i.setData(Uri.parse(url));
        		startActivity(i);
    		
    		}
    		else 
    		{
    			//utility.showDialogConfirm(getActivity(),"Alert"," working internet connection required", false).show();
    			Utility.ShowAlert("Network connection fail", getActivity());
    		}
		}
		if (v.getId()==R.id.btn_legal) 
		{
			statrtncview();
		}
		
	}
	
	
		private boolean MyStartActivity(Intent aIntent) {
			try
			{
				startActivity(aIntent);
				return true;
			}
			catch (ActivityNotFoundException e)
			{
				return false;
			}
		
		}
	private void statrtncview()
	{
		
		Intent intent=new Intent(getActivity(), TermsActivity.class);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);

	}
	
	
}
