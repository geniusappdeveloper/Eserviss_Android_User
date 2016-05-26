package com.app.taxisharingDriver;
import com.app.taxisharingDriver.utility.ConnectionDetector;
import com.app.taxisharingDriver.utility.SessionManager;
import com.app.taxisharingDriver.utility.Utility;
import com.app.taxisharingDriver.utility.VariableConstants;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class InviteFragmentNew extends Fragment 
{
	private  View view;
	private ImageView facebook,email_view,message,twitter;
	SessionManager sessionManager;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		sessionManager = new SessionManager(getActivity());
		sessionManager.setIsFlagForOther(true);
//		HomeFragment.flagForHomeFragmentOpened = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		
		if (view != null) 
		 {
		        ViewGroup parent = (ViewGroup) view.getParent();
		        if (parent != null)
		            parent.removeView(view);
		 }
		
		 try 
		    {
		        view = inflater.inflate(R.layout.invite_layout, container, false);
		    }
		 catch (InflateException e)
		    {
		        /* map is already there, just return view as it is */
		    	Utility.printLog("", "onCreateView  InflateException "+e);
		    }
		    
		    initLayout();
		return view;
	}
	
	
	private void initLayout()
	{
		facebook = (ImageView)view.findViewById(R.id.facebook);
		facebook.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v)
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
		});
		
		
		email_view = (ImageView)view.findViewById(R.id.email_view);
		email_view.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(Intent.ACTION_SEND);
				i.putExtra(Intent.EXTRA_EMAIL, new String[]{ sessionManager.getLoginId() });
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_SUBJECT, "Invite:"+getResources().getString(R.string.app_name));
				//i.putExtra(android.content.Intent.EXTRA_TEXT, text);
				startActivity(Intent.createChooser(i, "Send email"));
			}
		});
		
		
		message = (ImageView)view.findViewById(R.id.message);
		message.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v)
			{
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);         
				sendIntent.setType("vnd.android-dir/mms-sms");
				//sendIntent.putExtra("address", "12125551212");
				//sendIntent.putExtra("sms_body","Body of Message");
				startActivity(sendIntent);
			}
		});
		
		
		twitter = (ImageView)view.findViewById(R.id.twitter);
		twitter.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				 Intent intent = null;
				 try 
				 {
				     // get the Twitter app if possible
					/*getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
				     intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=2776019551"));
				     intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
					 intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com"));
					 
				 }
				 catch (Exception e)
                 {
				     // no Twitter app, revert to browser
				     intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com"));
				 }
				 startActivity(intent);
			}
		});
	}

	
}
