package com.app.taxisharingDriver;

import com.app.taxisharingDriver.utility.ConnectionDetector;
import com.app.taxisharingDriver.utility.SessionManager;
import com.app.taxisharingDriver.utility.Utility;
import com.app.taxisharingDriver.utility.VariableConstants;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;

public class AppInfo extends Fragment implements View.OnClickListener
{
	
	    
	private android.widget.TextView privemddotcomtextview,everyoncetextview,appratetextview,likeusonfacebooktextview,leagatextviewlayout;
	private android.widget.RelativeLayout appratelayout,likeusfacebooklayout,legallayout;
	@Override
    public View onCreateView(android.view.LayoutInflater inflater,
    		android.view.ViewGroup container,
    		android.os.Bundle savedInstanceState) 
    {

    	View view  = inflater.inflate(R.layout.appinfoxml, null);
    	initLayoutId(view);    	
  //  	HomeFragment.flagForHomeFragmentOpened = false;

    	initLayoutId(view);
    	 
    	return view;
    	
    }  
	private void initLayoutId(View view)
	{
		SessionManager sessionManager = new SessionManager( getActivity());
		sessionManager.setIsFlagForOther(true);
		privemddotcomtextview=(android.widget.TextView)view.findViewById(R.id.privemddotcomtextview);
		appratelayout=(android.widget.RelativeLayout)view.findViewById(R.id.appratelayout);
		likeusfacebooklayout=(android.widget.RelativeLayout)view.findViewById(R.id.likeusfacebooklayout);
		legallayout=(android.widget.RelativeLayout)view.findViewById(R.id.legallayout);
		everyoncetextview=(android.widget.TextView)view.findViewById(R.id.everyoncetextview);
		appratetextview=(android.widget.TextView)view.findViewById(R.id.appratetextview);
		likeusonfacebooktextview=(android.widget.TextView)view.findViewById(R.id.likeusonfacebooktextview);
		leagatextviewlayout=(android.widget.TextView)view.findViewById(R.id.leagatextviewlayout);
		Typeface robotoBoldCondensedItalic = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Zurich Condensed.ttf");
		everyoncetextview.setTypeface(robotoBoldCondensedItalic);
		privemddotcomtextview.setTypeface(robotoBoldCondensedItalic);
		appratetextview.setTypeface(robotoBoldCondensedItalic);
		likeusonfacebooktextview.setTypeface(robotoBoldCondensedItalic);
		leagatextviewlayout.setTypeface(robotoBoldCondensedItalic);
		everyoncetextview.setTextColor(android.graphics.Color.rgb(51, 51, 51));
		appratetextview.setTextColor(android.graphics.Color.rgb(51, 51, 51));
		likeusonfacebooktextview.setTextColor(android.graphics.Color.rgb(51, 51, 51));
		leagatextviewlayout.setTextColor(android.graphics.Color.rgb(51, 51, 51));
		privemddotcomtextview.setOnClickListener(this);
		appratelayout.setOnClickListener(this);
		likeusfacebooklayout.setOnClickListener(this);
		legallayout.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) 
	{
		if (v.getId()==R.id.privemddotcomtextview)
		{
			Utility utility=new Utility();
    		ConnectionDetector connectionDetector=new ConnectionDetector(getActivity());
    		if (connectionDetector.isConnectingToInternet()) 
    		{
    			String url = "http://"+VariableConstants.WEBSITE+"/";
        		Intent i = new Intent(Intent.ACTION_VIEW);
        		i.setData(Uri.parse(url));
        		startActivity(i);
    		}
    		else 
    		{
    			//utility.showDialogConfirm(getActivity(),"Alert"," working internet connection required", false).show();
    			utility.displayMessageAndExit(getActivity(),"Alert"," working internet connection required");
    		}
    		
		}
		if (v.getId()==R.id.appratelayout) 
		{
			Utility utility=new Utility();
    		ConnectionDetector connectionDetector=new ConnectionDetector(getActivity());
    		if (connectionDetector.isConnectingToInternet()) 
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
    			utility.displayMessageAndExit(getActivity(),"Alert"," working internet connection required");
    		}
		}
		if (v.getId()==R.id.likeusfacebooklayout) 
		{
			Utility utility=new Utility();
    		ConnectionDetector connectionDetector=new ConnectionDetector(getActivity());
    		if (connectionDetector.isConnectingToInternet()) 
    		{
    			String facebookUrl = VariableConstants.FACEBOOKLINK;
				try {
				        int versionCode = getActivity().getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
				        if (versionCode >= 3002850) 
				        {
				            Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
				            startActivity(new Intent(Intent.ACTION_VIEW, uri));;
				        } 
				        else 
				        {
				        	
			        		Intent i = new Intent(Intent.ACTION_VIEW);
			        		i.setData(Uri.parse(facebookUrl));
			        		startActivity(i);
				        }
				} catch (Exception e) 
				{
				    // Facebook is not installed. Open the browser
					Intent i = new Intent(Intent.ACTION_VIEW);
	        		i.setData(Uri.parse(facebookUrl));
	        		startActivity(i);
    		    }
    		}
    		else 
    		{
    			//utility.showDialogConfirm(getActivity(),"Alert"," working internet connection required", false).show();
    			utility.displayMessageAndExit(getActivity(),"Alert"," working internet connection required");
    		}
    		return;
		}
		if (v.getId()==R.id.legallayout) 
		{
			statrtncview();
		}
		
	}
	
	private void statrtncview()
	{
		//com.flurry.android.FlurryAgent.logEvent("ClickOnAcceptTermandCondition");
		Intent intent=new Intent(getActivity(), TermandCondtion.class);
		startActivity(intent);

	}
}
