package com.app.taxisharingDriver;

import com.app.taxisharingDriver.utility.ConnectionDetector;
import com.app.taxisharingDriver.utility.Utility;
import com.app.taxisharingDriver.utility.VariableConstants;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

public class TermandCondtion  extends FragmentActivity implements View.OnClickListener
{
	private android.widget.Button termbutton,condtionbutton,policybutton,securitybutton;
	private android.app.ActionBar actionBar;
	
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(android.os.Bundle bundle)
    {
    	super.onCreate(bundle);
    	setContentView(R.layout.termandconditionactivity);
    	overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
    	 actionBar=getActionBar();
         actionBar.setDisplayShowTitleEnabled(true);
         actionBar.setDisplayHomeAsUpEnabled(true);
         actionBar.setDisplayUseLogoEnabled(false);
         actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));
		 actionBar.setIcon(android.R.color.transparent);
		// actionBar.setIcon(getResources().getDrawable(R.drawable.login_screen_back_btn_off));
		 
		 actionBar.setTitle(getResources().getString(R.string.termandcondition));
		 int actionBarTitle = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		 TextView actionBarTitleView = (TextView) getWindow().findViewById(actionBarTitle);
		 Typeface robotoBoldCondensedItalic = Typeface.createFromAsset(getAssets(), "fonts/Zurich Condensed.ttf");
		 if(actionBarTitleView != null)
		 {
		     actionBarTitleView.setTypeface(robotoBoldCondensedItalic);
		     //actionBarTitleView.setTextColor(android.graphics.Color.rgb(51, 51, 51));
		     
		 }
    	initLayout();
    	try 
    	{
    		termbutton.setOnClickListener(this);
    		condtionbutton.setOnClickListener(this);
    		policybutton.setOnClickListener(this);
    		securitybutton.setOnClickListener(this);
		} catch (NullPointerException e) 
		{
		}
    	
    }
    
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) 
    {
    	switch (item.getItemId()) 
    	{
    	 case android.R.id.home:
		  		// NavUtils.navigateUpFromSameTask(this);
    		// com.flurry.android.FlurryAgent.logEvent("Click Back On TermandCondtion");
		  		 finish();
		         return true;
		         
    	 /*case R.id.drowpath:
		  		// NavUtils.navigateUpFromSameTask(this);
    		// startActivity();
    		  return true;
    	 case R.id.googlenavigation:
		  		// NavUtils.navigateUpFromSameTask(this);
    		// 
    		 startHistoryActivity();
    		  return true;    */
		default:
			return super.onOptionsItemSelected(item);
		}
    	
    }
    
    private void initLayout()
    {
    	termbutton=(android.widget.Button)findViewById(R.id.termbutton);
    	condtionbutton=(android.widget.Button)findViewById(R.id.condtionbutton);
    	policybutton=(android.widget.Button)findViewById(R.id.policybutton);
    	securitybutton=(android.widget.Button)findViewById(R.id.securitybutton);
    	Typeface robotoBoldCondensedItalic = Typeface.createFromAsset(getAssets(), "fonts/Zurich Condensed.ttf");
    	securitybutton.setTypeface(robotoBoldCondensedItalic);
    	termbutton.setTypeface(robotoBoldCondensedItalic);
    	condtionbutton.setTypeface(robotoBoldCondensedItalic);
    	policybutton.setTypeface(robotoBoldCondensedItalic);
    }

	@Override
	public void onClick(View v) 
	{
		
		Utility utility=new Utility();
		ConnectionDetector connectionDetector=new ConnectionDetector(TermandCondtion.this);
		if (connectionDetector.isConnectingToInternet()) 
		{
			switch (v.getId()) 
			{
			case R.id.termbutton:
				
			//	com.flurry.android.FlurryAgent.logEvent("ClickTermandCondition");
				statrtncview("TERMS",VariableConstants.TERMSLINK);
				break;
			case R.id.condtionbutton:
			//	com.flurry.android.FlurryAgent.logEvent("ClickPrivacyPolicy");
				statrtncview("CONDITION",VariableConstants.PRIVACYLINK);
				break;
			case R.id.policybutton:
				statrtncview("POLICY",VariableConstants.PRIVACYLINK);
				break;
			case R.id.securitybutton:
				statrtncview("SECURITY",VariableConstants.PRIVACYLINK);
				break;

			default:
				break;
			}
		}
		else 
		{
			//utility.showDialogConfirm(getActivity(),"Alert"," working internet connection required", false).show();
			utility.displayMessageAndExit(TermandCondtion.this,"Alert"," working internet connection required");

		}
		

	}
	
	
	private void statrtncview(String textstring,String url)
	{
		
		Intent intent=new Intent(TermandCondtion.this, ShowWebView.class);
		android.os.Bundle bundle=new android.os.Bundle();
		bundle.putString("TEXTSTRING", textstring);
		bundle.putString("LINK", url);
		intent.putExtras(bundle);
		startActivity(intent);
//		 AlertDialog.Builder alert = new AlertDialog.Builder(SignUpOne.this,android.R.style.Theme);
//
//		// Dialog dialog=new Dialog(SignUpOne.this,android.R.style.Theme);
//         alert.setTitle("");
//
//         WebView wv = new WebView(SignUpOne.this);
//         wv.getSettings().setJavaScriptEnabled(true);
//         wv.loadUrl("https://docs.google.com/file/d/string/edit?usp=sharing");
//         wv.setWebViewClient(new WebViewClient() {
//             @Override
//             public boolean shouldOverrideUrlLoading(WebView view,
//                     String url) {
//                 view.loadUrl(url);
//
//                 return true;
//             }
//         });
//
//      /*   alert.setNegativeButton("Close",
//                 new DialogInterface.OnClickListener() 
//         {
//                     @Override
//                     public void onClick(DialogInterface dialog, int id) 
//                     {
//                     }
//                 });*/
//         Dialog d = alert.setView(wv).create();
//         d.show();
//         WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//         lp.copyFrom(d.getWindow().getAttributes());
//         lp.width = WindowManager.LayoutParams.FILL_PARENT;
//         lp.height = WindowManager.LayoutParams.FILL_PARENT;
//         d.getWindow().setAttributes(lp);
	}
	
	@Override
	protected void onStop() 
	{
		super.onStop();
	//	com.flurry.android.FlurryAgent.onEndSession(this);
		
	}
	@Override
	protected void onStart()
	{
		super.onStart();
	//	com.flurry.android.FlurryAgent.onStartSession(this,com.threembed.doctor.utilities.VariableConstants.FLLURY_ANALYTICS_ID);

	}
}
