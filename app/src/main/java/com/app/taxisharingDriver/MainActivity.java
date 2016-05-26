package com.app.taxisharingDriver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.http.NameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.taxisharingDriver.calander.MyBookingsFrag;
import com.app.taxisharingDriver.pojo.AppointmentDetailList;
import com.app.taxisharingDriver.response.AppointmentDetailData;
import com.app.taxisharingDriver.response.AppointmentStatusResponse;
import com.app.taxisharingDriver.response.LocationUpdateResponse;
import com.app.taxisharingDriver.response.LogoutResponse;
import com.app.taxisharingDriver.response.MasterStatusResponse;
import com.app.taxisharingDriver.utility.ConnectionDetector;
import com.app.taxisharingDriver.utility.Scaler;
import com.app.taxisharingDriver.utility.SessionManager;
import com.app.taxisharingDriver.utility.Utility;
import com.app.taxisharingDriver.utility.VariableConstants;
import com.google.gson.Gson;
import com.pubnub.api.Pubnub;
@SuppressLint("Wakelock")
public class MainActivity extends FragmentActivity
{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private BackGroundTaskForLogout backGroundTaskForLogout;
	private ActionBarDrawerToggle mDrawerToggle;
	private String driverChannelName;
	private Button /*online_button,offline_button*/schedule_button;
	// nav drawer title
	private CharSequence mDrawerTitle;
	// used to store app title
	private CharSequence mTitle;
	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	private int currentTabStatus = -1;
	private android.app.ActionBar actionBar;
	private ImageButton button_menu;
	private TextView nav_img;
	//private ImageButton button_right_menu;
	private TextView activity_main_content_title;
	public static boolean isResponse = true;
	private static Pubnub pubnub;
	private SessionManager session;
	private ProgressDialog mdialog;
	private Utility utility;
	private ArrayList<AppointmentDetailData>appointmentDetailDatas;
	private AppointmentDetailList appointmentDetailList = new AppointmentDetailList();
	private PowerManager.WakeLock wl;
	//public static TextView schedule;
	String latitude ;
	String longitude ;
	
	//private String message,action;
	Context context = MainActivity.this;
	static Activity mainActivity;

	@SuppressLint({ "NewApi", "Wakelock" })
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mainActivity=this;

		pubnub=ApplicationController.getInstacePubnub();
		session = new SessionManager(this);
		driverChannelName = session.getSubscribeChannel();
		session.setIsHomeFragmentisOpened(true);
		utility=new Utility();
		if (!utility.isMyServiceRunning(MyService.class, this)) 
		{
			startService(ApplicationController.getMyServiceInstance());
		}
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_main_drawer);
		overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu12);

		navDrawerItems = new ArrayList<NavDrawerItem>();
		// adding nav drawer items to array 
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		// Pages
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// What's hot, We  will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));

		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),	navDrawerItems);
		mDrawerList.setAdapter(adapter);
		// enabling action bar app icon and behaving it as toggle button
		actionBar = getActionBar();
		actionBar.setTitle(getResources().getString(R.string.driverapptext));
		actionBar.setIcon(android.R.color.transparent);
		initActionbar() ;
		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.drawable.selector_for_menu_button, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
				)
		{
			public void onDrawerClosed(View view) 
			{
				getActionBar().setTitle("");
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) 
			{
				getActionBar().setTitle("");
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		if (savedInstanceState == null)
		{
			// on first time display view for first nav item
			if (session.getFlagCalender()) 
			{
				session.setFlagCalender(false);
				displayView(1);
			}
			else 
			{
				displayView(0);
			}
		}
	}

	public Pubnub getPubnubObject()
	{
		return pubnub;
	}

	/**
	 * Initializes the ActionBar for the <code>Activity</code>
	 */
	@SuppressLint("NewApi")
	private void initActionbar() 
	{
		//BitmapDrawable bg = (BitmapDrawable) getResources().getDrawable(R.drawable.login_screen_navigation_bar);
		//actionBar.setBackgroundDrawable(bg);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));
		LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout mActionBarCustom = (LinearLayout)inflater.inflate(R.layout.customactionbar, null);
		button_menu=(ImageButton)mActionBarCustom.findViewById(R.id.button_menu);
		button_menu.setVisibility(View.GONE);
		//schedule = (TextView)findViewById(R.id.schedule);
		//online_button = (Button)mActionBarCustom.findViewById(R.id.online_button);
		//offline_button = (Button)mActionBarCustom.findViewById(R.id.offline_button);
		actionBar.setCustomView(mActionBarCustom);
		actionBar.setDisplayShowCustomEnabled(true);
		
		//online_button.setOnClickListener(this);
		//offline_button.setOnClickListener(this);
		activity_main_content_title=(TextView)mActionBarCustom.findViewById(R.id.activity_main_content_title);
		double scaler[]=Scaler.getScalingFactor(this);
		int padding = (int)Math.round(120*scaler[0]);
		activity_main_content_title.setPadding(padding, 0, 0, 0);
		nav_img = (TextView)mActionBarCustom.findViewById(R.id.nav_img);
		/*schedule_button = (Button)mActionBarCustom.findViewById(R.id.schedule);
		
		schedule_button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(MainActivity.this,PendingBooking.class);
				startActivity(intent);
				finish();
			}
		});*/
	}

	public TextView getTitleTextview()
	{
		return activity_main_content_title;
	}

	public ImageButton getLeftmenubutton()
	{
		return button_menu;
	}
	public android.app.ActionBar getmActionBar()
	{
		return actionBar;
	}

	/**
	 * Slide menu item click listener
	 */
	private class SlideMenuClickListener implements
	ListView.OnItemClickListener 
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) 
		{
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	/**
	 * Displaying fragment view for selected nav drawer list item
	 * */
	@SuppressWarnings("deprecation")
	private void displayView(int position)
	{
		// update the main content by replacing fragments
		if (currentTabStatus==position) 
		{
			mDrawerLayout.closeDrawer(mDrawerList);
		} 
		else 
		{
			Fragment fragment = null;
			switch (position)
			{
			case 0:
				currentTabStatus=0;
				fragment = new HomeFragment();
				activity_main_content_title.setVisibility(View.GONE);
				nav_img.setVisibility(View.VISIBLE);
			//	nav_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.roadyo_logo));
				//schedule_button.setVisibility(View.VISIBLE);
				break;
			case 1:
				currentTabStatus=1;
				fragment = new MyBookingsFrag();
				nav_img.setVisibility(View.GONE);
				activity_main_content_title.setVisibility(View.VISIBLE);
				activity_main_content_title.setText(getResources().getString(R.string.appointment));
				//schedule_button.setVisibility(View.VISIBLE);
				break;
			case 2:
				currentTabStatus=2;
				fragment = new Profile_Fragment();
				nav_img.setVisibility(View.GONE);
				activity_main_content_title.setVisibility(View.VISIBLE);
				activity_main_content_title.setText(getResources().getString(R.string.myprofile));
				//schedule_button.setVisibility(View.VISIBLE);
				break;
			/*case 3:
				//fragment = new ProfileFragment();
				currentTabStatus=3;
				fragment = new FAQFragment();
				nav_img.setVisibility(View.GONE);
				activity_main_content_title.setVisibility(View.VISIBLE);
				activity_main_content_title.setText(getResources().getString(R.string.faq));
				schedule_button.setVisibility(View.VISIBLE);
				break;*/
			case 3:
				//fragment = new PagesFragment();
				currentTabStatus=3;
				fragment = new InviteFragmentNew();
				nav_img.setVisibility(View.GONE);
				activity_main_content_title.setVisibility(View.VISIBLE);
				activity_main_content_title.setText(getResources().getString(R.string.invite));
				//schedule_button.setVisibility(View.VISIBLE);
				break;
			case 4:
				//fragment = new WhatsHotFragment();
				currentTabStatus=4;
				fragment = new AppInfo();
				nav_img.setVisibility(View.GONE);
				activity_main_content_title.setVisibility(View.VISIBLE);
				activity_main_content_title.setText(getResources().getString(R.string.appinfo));
				//schedule_button.setVisibility(View.VISIBLE);
				break;
			case 5:
				currentTabStatus=5;
				ErrorMessageForLogout(getResources().getString(R.string.logout),getResources().getString(R.string.logoutmessage));
				break;
			default:
				break;
			}

			if (fragment != null)
			{
				performTransaction(fragment) ;
				// update selected item and title, then close the drawer
				mDrawerList.setItemChecked(position, true);
				mDrawerList.setSelection(position);
				setTitle(navMenuTitles[position]);
				mDrawerLayout.closeDrawer(mDrawerList);
			} 
			else 
			{
				// error in creating fragment
				Utility.printLog("MainActivity", "Error in creating fragment");
			}
		}
	}

	public void performTransaction(Fragment frag) 
	{
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.frame_container, mFragmentStack.push(frag));
		ft.commit();
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) 
	{
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public static String getDeviceId(Context context)
	{
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	@Override
	protected void onResume() 
	{
		super.onResume();
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		wl.acquire();
		/*ConnectionDetector connectionDetector=new ConnectionDetector(MainActivity.this);
		if (connectionDetector.isConnectingToInternet()) 
		{
			getAppointmentStatus();
		}*/

		session.setIsFlagForOther(false);
		session.setIsHomeFragmentisOpened(true);
		isResponse = true;
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();

		if (mdialog!=null)
		{
			mdialog.dismiss();
			mdialog.cancel();
		}
	wl.release();	
	}


	private Stack<Fragment> mFragmentStack = new Stack<Fragment>();

	@Override
	public void onBackPressed()
	{
		if (currentTabStatus == 0) 
		{
			finish();
		}
		try {
			mFragmentStack.pop();
			if (mFragmentStack.size() > 0 && (currentTabStatus != 0)) 
			{
				/*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.frame_container, mFragmentStack.peek());
				ft.commit();*/
				displayView(0);
			}
			else if (currentTabStatus == 0) 
			{
				finish();
			}
			else
			{
				finish();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}

	private void startLogout()
	{
		SessionManager sessionManager=new SessionManager(this);
		sessionManager.logoutUser();

		NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
		/*Intent serviceIntent= new Intent(MainActivity.this, PubnubService.class);
		stopService(serviceIntent);*/
		Intent intent=new Intent(MainActivity.this, SplahsActivity.class);
		stopService(ApplicationController.getMyServiceInstance());
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	private void ErrorMessageForLogout(String title,String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setNegativeButton(getResources().getString(R.string.logout),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{

				dialog.dismiss();
				logoutUser();
			}
		});

		builder.setPositiveButton(getResources().getString(R.string.cancle),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				currentTabStatus = -1;
				dialog.dismiss();
			}
		});
		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

	private void logoutUser()
	{
		SessionManager sessionManager=new SessionManager(this);
		//Utility utility=new Utility();
		String deviceid=Utility.getDeviceId(this);
		Utility utility=new Utility();
		String sessionToken=sessionManager.getSessionToken();
		String logoutType=VariableConstants.USERTYPE;
		String currenttime=utility.getCurrentGmtTime();
		String params[]={sessionToken,deviceid,logoutType,currenttime};
		backGroundTaskForLogout=new BackGroundTaskForLogout();
		backGroundTaskForLogout.execute(params);
	}

	private class BackGroundTaskForLogout extends android.os.AsyncTask<String, Void, LogoutResponse>
	{
		private Utility utility=new Utility();
		private List<NameValuePair>logoutParamList;
		private String logoutResponse;
		private LogoutResponse logoutUser;
		private boolean isSuccess=true;
		private  ProgressDialog mdialog;

		@Override
		protected LogoutResponse doInBackground(String... params) 
		{
			try {
				logoutParamList = utility.getLogoutParameter(params);
				logoutResponse =  utility.makeHttpRequest(VariableConstants.logOut_url,VariableConstants.methodeName,logoutParamList);
				Utility.printLog("logoutResponse"+logoutResponse);
				if (logoutResponse!=null)
				{
					Gson gson = new Gson();
					logoutUser=gson.fromJson(logoutResponse, LogoutResponse.class);
				}
				else 
				{
					isSuccess=false;
				}
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
			return logoutUser;
		}


		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			mdialog=Utility.GetProcessDialog(MainActivity.this);
			mdialog.setMessage(getResources().getString(R.string.Pleasewaitmessage));
			mdialog.show();
		}
		@Override
		protected void onPostExecute(LogoutResponse result)
		{
			super.onPostExecute(result);
			if (mdialog!=null)
			{
				mdialog.dismiss();
				mdialog.cancel();
				mdialog=null;
			}
			//	Error Codes:
			//ErrorNumber -> (ErrorFlag) ErrorMessage
			if (isSuccess)
			{
				try
				{
					if (result.getErrFlag()==0 && result.getErrNum()==29)
					{
						///29 -> (0) Logged out!
						NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
						notificationManager.cancelAll();
						startLogout();
					}
					else if (result.getErrFlag()==1&&result.getErrNum()==1)
					{
						//1 -> (1) Mandatory field missing
						ErrorMessage(getResources().getString(R.string.error),result.getErrMsg(),false);

					}
					else if (result.getErrFlag()==1&&result.getErrNum()==6)
					{
						//6 -> (1) Session token expired, please login.
						ErrorMessageForSessionExpoired(getResources().getString(R.string.error),result.getErrMsg(),result.getErrFlag(),result.getErrNum());

					}
					else if (result.getErrFlag()==1&&result.getErrNum()==7)
					{
						//7 -> (1) Invalid token, please login or register.
						ErrorMessageForSessionExpoired(getResources().getString(R.string.error),result.getErrMsg(),result.getErrFlag(),result.getErrNum());

					}
					else if (result.getErrFlag()==1&&result.getErrNum()==3)
					{
						//3 -> (1) Error occurred while processing your request.
						ErrorMessage(getResources().getString(R.string.error),result.getErrMsg(),false);
					}
				}catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else 
			{
				// some server issue ocured
				ErrorMessage(getResources().getString(R.string.error),"Request timeout",false);
			}
		}

		private void ErrorMessageForSessionExpoired(String title,final String message,final int errorFlag,final int errornum)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle(title);
			builder.setMessage(message);

			builder.setPositiveButton(getResources().getString(R.string.okbuttontext),
					new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{     

					if (errornum==6) 
					{
						//6 -> (1) Session token expired, please login.
						startLogout();
					}
					else if (errornum==7) 
					{
						//7 -> (1) Invalid token, please login or register.
						startLogout();
					}
					dialog.dismiss();
				}
			});

			builder.setNegativeButton(getResources().getString(R.string.cancelbutton),
					new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			});

			AlertDialog	 alert = builder.create();
			alert.setCancelable(false);
			alert.show();
		}

		private void ErrorMessage(String title,String message,final boolean flageforSwithchActivity)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle(title);
			builder.setMessage(message);

			builder.setPositiveButton(getResources().getString(R.string.okbuttontext),
					new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					if (flageforSwithchActivity) 
					{
						MainActivity.this.finish();
					}
					else
					{
						// only show message  
					}
					dialog.dismiss();
				}
			});

			AlertDialog	 alert = builder.create();
			alert.setCancelable(false);
			alert.show();
		}
	}

	private void getAppointmentStatus() 
	{
		Utility utility=new Utility();
		ConnectionDetector connectionDetector=new ConnectionDetector(this);
		if (connectionDetector.isConnectingToInternet()) 
		{
			String deviceid=Utility.getDeviceId(this);
			String curenttime=utility.getCurrentGmtTime();	            
			SessionManager sessionManager=new SessionManager(this);
			String sessiontoken = sessionManager.getSessionToken();
			String apttime = sessionManager.getAPPT_DATE();
			final String mparams[]={sessiontoken,deviceid ,curenttime};
			mdialog = Utility.GetProcessDialog(this);
			mdialog.setMessage(getResources().getString(R.string.Pleasewaitmessage));
			mdialog.show();
			mdialog.setCancelable(false);
			RequestQueue queue = Volley.newRequestQueue(this);
			String  url = VariableConstants.getAppointmenttatus_url;
			StringRequest postRequest = new StringRequest(Request.Method.POST, url,responseListenerofApptStatus,errorListenerApptStatus )
			{    
				@Override
				protected Map<String, String> getParams()
				{ 
					Map<String, String>  params = new HashMap<String, String>(); 
					params.put("ent_sess_token",mparams[0]); 
					params.put("ent_dev_id",mparams[1]);
					params.put("ent_user_type","1");
					params.put("ent_date_time", mparams[2]);
					params.put("ent_appnt_dt", "");
					Utility.printLog("paramsRequest"+params);
					return params; 
				}
			};
			int socketTimeout = 60000;//60 seconds - change to what you want
			RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
			postRequest.setRetryPolicy(policy);
			queue.add(postRequest);    
		}	 
	}


	Listener<String> responseListenerofApptStatus=new Listener<String>()
			{
		@Override
		public void onResponse(String response)
		{
			try 
			{
			Utility.printLog("AppointmentStatusResponse"+response);
			AppointmentStatusResponse appointmentStatusResponse;
			Gson gson = new Gson();
			appointmentStatusResponse = gson.fromJson(response, AppointmentStatusResponse.class);
			
				if (mdialog!=null)
				{
					mdialog.dismiss();
					mdialog.cancel();
				}
				if (appointmentStatusResponse.getErrFlag() == 0 && appointmentStatusResponse.getErrNum() == 21) 
				{
					appointmentDetailDatas = appointmentStatusResponse.getData();
					appointmentDetailList.setAppointmentDetailData(appointmentDetailDatas.get(0));
					Utility.printLog("AAAA Deveendra"+appointmentDetailDatas.get(0).getStatus());
						if ("6".equals(appointmentDetailDatas.get(0).getStatus()))
						{
							Utility.printLog("Inside appointmentDetailDatas");
							Intent intent=new Intent(MainActivity.this, IHaveArrivedActivity.class);
							Bundle bundle=new Bundle();
							bundle.putSerializable(VariableConstants.APPOINTMENT,appointmentDetailList );
							Utility.printLog("III Main appointmentDetailList "+appointmentDetailList);
							intent.putExtras(bundle);
							startActivity(intent);
							finish();	
						}
						else if ("7".equals(appointmentDetailDatas.get(0).getStatus())) 
						{
							Intent intent=new Intent(MainActivity.this, BeginJourneyActivity.class);
							Bundle bundle=new Bundle();
							bundle.putSerializable(VariableConstants.APPOINTMENT,appointmentDetailList);
							intent.putExtras(bundle);
							startActivity(intent);
							finish();	
						}
						else if ("8".equals(appointmentDetailDatas.get(0).getStatus())) 
						{
							if (session.getFlagForStatusDropped()) 
							{
								Intent intent=new Intent(MainActivity.this, JourneyDetailsActivity.class);
								Bundle bundle=new Bundle();
								bundle.putSerializable(VariableConstants.APPOINTMENT,appointmentDetailList);
								intent.putExtras(bundle);
								startActivity(intent);
								finish();
							}
							else 
							{
								Intent intent=new Intent(MainActivity.this, PassengerDroppedActivity.class);
								Bundle bundle=new Bundle();
								bundle.putSerializable(VariableConstants.APPOINTMENT,appointmentDetailList);
								intent.putExtras(bundle);
								startActivity(intent);
								finish();
							}
								
						}
					}
				else if (appointmentStatusResponse.getErrFlag() == 1 && appointmentStatusResponse.getErrNum() == 96) 
				{
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), appointmentStatusResponse.getErrMsg());
				}
				
				else if (appointmentStatusResponse.getErrFlag() == 1 && appointmentStatusResponse.getErrNum() == 79) 
				{
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), appointmentStatusResponse.getErrMsg());
				}
				else if (appointmentStatusResponse.getErrFlag() == 1 && appointmentStatusResponse.getErrNum() == 6) 
				{
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), appointmentStatusResponse.getErrMsg());
				}
				else if (appointmentStatusResponse.getErrFlag() == 1 && appointmentStatusResponse.getErrNum() == 7) 
				{
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), appointmentStatusResponse.getErrMsg());
				}
				else if (appointmentStatusResponse.getErrFlag() == 1 && appointmentStatusResponse.getErrNum() == 101) 
				{
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), appointmentStatusResponse.getErrMsg());
				}
				else if (appointmentStatusResponse.getErrFlag() == 1 && appointmentStatusResponse.getErrNum() == 99) 
				{
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), appointmentStatusResponse.getErrMsg());
				}
				else if(appointmentStatusResponse.getErrNum()==6 || appointmentStatusResponse.getErrNum()==7 ||
						appointmentStatusResponse.getErrNum()==94 || appointmentStatusResponse.getErrNum()==96)
				{
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), appointmentStatusResponse.getErrMsg());
				}
				
				} 
			catch (Exception e) 
			{
				Utility.printLog("Exception = "+e);
				//ErrorMessage(getResources().getString(R.string.messagetitle),getResources().getString(R.string.servererror),false);
			}
		}
			};

			ErrorListener errorListenerApptStatus=new ErrorListener()
			{
				@Override
				public void onErrorResponse(VolleyError error) 
				{
					//Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
					Utility.printLog("AAAAAAAAAVolleyError"+error);
					if (mdialog!=null)
					{
						mdialog.dismiss();
						mdialog.cancel();
					}
					ErrorMessage(getResources().getString(R.string.messagetitle), getResources().getString(R.string.servererror), false);
					//Utility.ShowAlert(getResources().getString(R.string.network), MainActivity.this);
				}
			};
			
			private void ErrorMessage(String title,String message,final boolean flageforSwithchActivity)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle(title);
				builder.setMessage(message);

				builder.setPositiveButton(getResources().getString(R.string.okbuttontext),
						new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{

						if (flageforSwithchActivity) 
						{
						}
						else
						{
							// only show message 
						}
						dialog.dismiss();

					}
				});
				AlertDialog	 alert = builder.create();
				alert.setCancelable(false);
				alert.show();
			}
			private void ErrorMessageForInvalidOrExpired(String title,String message)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle(title);
				builder.setMessage(message);


				builder.setNegativeButton(getResources().getString(R.string.okbuttontext),
						new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						Intent intent=new Intent(MainActivity.this, SplahsActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
						SessionManager sessionManager=new SessionManager(MainActivity.this);
						sessionManager.logoutUser();
						startActivity(intent);
						finish();
					}
				});
				AlertDialog	 alert = builder.create();
				alert.setCancelable(false);
				alert.show();
			}

			private void getMasterUpdateStatus(int masterStatus) 
			{
				SessionManager sessionManager=new SessionManager(this);
				Utility utility=new Utility();
				String sessionToken=sessionManager.getSessionToken();
				String deviceid=Utility.getDeviceId(this);
				String currentDate=utility.getCurrentGmtTime();

				final String mparams[]={sessionToken,deviceid,""+masterStatus,currentDate};
				mdialog=Utility.GetProcessDialog(this);
				mdialog.setMessage(getResources().getString(R.string.Pleasewaitmessage));
				mdialog.setCancelable(false);
				RequestQueue queue = Volley.newRequestQueue(this);
				String url = VariableConstants.getMasterStatus_url;
				StringRequest postRequest = new StringRequest(Request.Method.POST, url,responseListenerofMasterStatus,errorListenerofMasreStatus ) 
				{    
					@Override
					protected Map<String, String> getParams()
					{ 
						Map<String, String>  params = new HashMap<String, String>(); 
						params.put("ent_sess_token", mparams[0]); 
						params.put("ent_dev_id", mparams[1]);
						params.put("ent_status", mparams[2]);
						params.put("ent_date_time", mparams[3]);
						Utility.printLog("getMasterStatus  request "+params);
						return params; 
					}
				};
				int socketTimeout = 60000;//60 seconds - change to what you want
				RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
				postRequest.setRetryPolicy(policy);
				queue.add(postRequest);
			}

			Listener<String> responseListenerofMasterStatus=new Listener<String>()
					{
				@Override
				public void onResponse(String response) 
				{
					Utility.printLog("getMasterStatus response "+response);
					MasterStatusResponse masterStatusResponse;
					Gson gson = new Gson();
					masterStatusResponse = gson.fromJson(response, MasterStatusResponse.class);

					if (mdialog!=null)
					{
						mdialog.dismiss();
						mdialog.cancel();
					}
					try
					{						
						if (masterStatusResponse.getErrFlag() == 0 && masterStatusResponse.getErrNum() == 69)
						{
							Toast.makeText(MainActivity.this, masterStatusResponse.getErrMsg(), Toast.LENGTH_SHORT).show();
						}
					} 
					catch (Exception e) 
					{
						Utility.printLog("getAppointmentDetailException = "+e); 
						//ErrorMessage(getResources().getString(R.string.messagetitle),getResources().getString(R.string.servererror),true);
					}

				}
					};

					ErrorListener errorListenerofMasreStatus=new ErrorListener()
					{
						@Override
						public void onErrorResponse(VolleyError error) 
						{
							Toast.makeText(MainActivity.this, ""+error, Toast.LENGTH_SHORT).show();
						}
					};
	
   private void getMasterUpdateLocation() 
   {
	 SessionManager sessionManager=new SessionManager(this);
	 Utility utility=new Utility();
	 String sessionToken=sessionManager.getSessionToken();
	 String deviceid=Utility.getDeviceId(this);
	 String currentDate=utility.getCurrentGmtTime();
	 if (sessionManager.getDriverCurrentLat() != 0.0 && sessionManager.getDriverCurrentLongi()!= 0.0) 
	 {
		 latitude = Double.toString(sessionManager.getDriverCurrentLat());
		 longitude = Double.toString(sessionManager.getDriverCurrentLongi());
	 }
	 else
	 {
		 Utility.ShowAlert("Please wait for the locaion update...", context);
	 }

	 final String mparams[]={sessionToken,deviceid,latitude,longitude,currentDate};
	 mdialog=Utility.GetProcessDialog(this);
	 mdialog.setMessage(getResources().getString(R.string.Pleasewaitmessage));
	 mdialog.setCancelable(false);
	 RequestQueue queue = Volley.newRequestQueue(this);
	 String url = VariableConstants.getMasterLocation_url;
	 StringRequest postRequest = new StringRequest(Request.Method.POST, url,new Listener<String>()
			 {
		 @Override
		 public void onResponse(String response) 
		 {
			 LocationUpdateResponse locationUpdateResponse;
			 Gson gson = new Gson();
			 locationUpdateResponse = gson.fromJson(response, LocationUpdateResponse.class);
			 if (mdialog == null)
			 {
				 mdialog.dismiss();
				 mdialog.cancel();
			 }
			 if (locationUpdateResponse.getErrFlag() == 0) 
			 {
				 Toast.makeText(MainActivity.this, locationUpdateResponse.getErrMsg(), Toast.LENGTH_SHORT).show();
			 }
		 }
			 },errorListenerofMasreStatus ) 
	 {    
		 @Override
		 protected Map<String, String> getParams()
		 { 
			 Map<String, String>  params = new HashMap<String, String>(); 
			 params.put("ent_sess_token", mparams[0]); 
			 params.put("ent_dev_id", mparams[1]);
			 params.put("ent_latitude", mparams[2]);
			 params.put("ent_longitude", mparams[3]);
			 params.put("ent_date_time", mparams[4]);
			 Utility.printLog("getMasterStatus  request "+params);
			 return params; 
		 }
	 };
	 int socketTimeout = 60000;//60 seconds - change to what you want
	 RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
	 postRequest.setRetryPolicy(policy);
	 queue.add(postRequest);
 }
   
}
