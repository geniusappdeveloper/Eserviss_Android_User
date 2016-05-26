package com.app.taxisharingDriver;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
@SuppressLint("SetJavaScriptEnabled")
public class ShowWebView extends Activity
{

	//private Button button;
	private WebView webView;
	private ActionBar actionBar;
	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) 
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_web_view);
		//   com.flurry.android.FlurryAgent.logEvent("ShowWebView");
		overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
		Bundle extas = getIntent().getExtras();
		actionBar=getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_screen_navigation_bar));
		actionBar.setIcon(android.R.color.transparent);
		// actionBar.setIcon(getResources().getDrawable(R.drawable.login_screen_back_btn_off));

		actionBar.setTitle(extas.getString("TEXTSTRING"));
		String url=extas.getString("LINK");
		int actionBarTitle = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		TextView actionBarTitleView = (TextView) getWindow().findViewById(actionBarTitle);
		Typeface robotoBoldCondensedItalic = Typeface.createFromAsset(getAssets(), "fonts/Zurich Condensed.ttf");
		if(actionBarTitleView != null)
		{
			actionBarTitleView.setTypeface(robotoBoldCondensedItalic);
			actionBarTitleView.setTextColor(android.graphics.Color.rgb(51, 51, 51));
		}
		//Get webview
		webView = (WebView) findViewById(R.id.webView1);
		startWebView(url);

	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case android.R.id.home:
			// NavUtils.navigateUpFromSameTask(this);
			//	 com.flurry.android.FlurryAgent.logEvent("Click Back On ShowWebView");
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

	private void startWebView(String url)
	{
		//Create new web view Client to show progress dialog
		//When opening a url or click on link
		webView.setWebViewClient(new WebViewClient()
		{     
			ProgressDialog progressDialog;
			//If you will not use this method url links are open in new browser not in web view
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{             
				view.loadUrl(url);
				return true;
			}

			//Show loader on url load
			public void onLoadResource (WebView view, String url)
			{
				if (progressDialog == null)
				{
					// in standard case YourActivity.this
					progressDialog = new ProgressDialog(ShowWebView.this);
					progressDialog.setCancelable(true);
					progressDialog.setMessage("Loading...");
					progressDialog.show();
				}
			}
			public void onPageFinished(WebView view, String url) {
				try
				{
					if (progressDialog.isShowing())
					{
						progressDialog.dismiss();
						progressDialog = null;
					}
				}catch(Exception exception){
					exception.printStackTrace();
				}
			}

		});

		// Javascript inabled on webview 
		webView.getSettings().setJavaScriptEnabled(true);
		// Other webview options
		/*
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        String summary = "<html><body>You scored <b>192</b> points.</body></html>";
        webview.loadData(summary, "text/html", null);
		*/

		//Load url in webview
		webView.loadUrl(url);
	}

	// Open previous opened link from history on webview when back button pressed

	@Override
	// Detect when the back button is pressed
	public void onBackPressed()
	{
		if(webView.canGoBack()) 
		{
			webView.goBack();
		}
		else
		{
			// Let the system handle the back button
			super.onBackPressed();
		}
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