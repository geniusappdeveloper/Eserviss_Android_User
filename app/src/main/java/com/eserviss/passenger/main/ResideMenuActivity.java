package com.eserviss.passenger.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.egnyt.eserviss.MainActivity;
import com.google.gson.Gson;
import com.egnyt.eserviss.R;
import com.eserviss.passenger.pojo.LogoutResponse;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.threembed.utilities.MyEventBus;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

import de.greenrobot.event.EventBus;

public class ResideMenuActivity extends FragmentActivity implements ResideMenuItem.ClickableMenu_Item, OnItemSelectedListener, OnClickListener {
	private ResideMenu resideMenu;
	private ResideMenuItem itemHome, itemPayment, itemProfile, itemBookings, itemSupport, itemShare, itemAbout, itemLogout;
	public static FrameLayout main_frame_layout;
	private TextView page_name;
	private String[] navMenuTitles;
	private int currentTabStatus = 0;
	private boolean backPressedToExitOnce = false;
	private IntentFilter filter;
	private BroadcastReceiver receiver;
	private Dialog dialog;
	public static int i = 0;
	private RelativeLayout network_error;
	public static Button edit_profile, invoice_driver_tip;
	private ImageView logo;
	public static RelativeLayout gpsErrorLayout;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);
		network_error = (RelativeLayout) findViewById(R.id.network_error_layout);
		main_frame_layout = (FrameLayout) findViewById(R.id.main_layout);
		page_name = (TextView) findViewById(R.id.current_page_name);
		edit_profile = (Button) findViewById(R.id.edit_profile);
		logo = (ImageView) findViewById(R.id.logo);
		gpsErrorLayout = (RelativeLayout) findViewById(R.id.gps_error_layout);
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		invoice_driver_tip = (Button) findViewById(R.id.invoice_driver_tip);
		invoice_driver_tip.setOnClickListener(this);

		setUpMenu();
		if (savedInstanceState == null)
			changeFragment(new HomePageFragment());
		resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);

		filter = new IntentFilter();
		filter.addAction("com.threembed.roadyo.internetStatus");
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle extras = intent.getExtras();
				String status = extras.getString("STATUS");

				if (status.equals("1")) {
					network_error.setVisibility(View.GONE);
				} else {
					network_error.setVisibility(View.VISIBLE);
				}
			}
		};
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.invoice_driver_tip) {
			MyEventBus myEventBus = new MyEventBus();
			myEventBus.setEventType("info");
			EventBus.getDefault().post(myEventBus);
		}
	}


	private void setUpMenu() {
		// attach to current activity;
		resideMenu = new ResideMenu(this);
		resideMenu.setBackground(R.color.yellowrequestpick);
		resideMenu.attachToActivity(this);
		resideMenu.setMenuListener(menuListener);
		//valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
		resideMenu.setScaleValue(0.6f);

		//create menu items;
		itemHome = new ResideMenuItem(ResideMenuActivity.this, R.drawable.menu_home_selector, "      BOOK A TAXI", this, 1);
		itemBookings = new ResideMenuItem(ResideMenuActivity.this, R.drawable.menu_booking_selector, "    BOOKINGS", this, 2);
		itemPayment = new ResideMenuItem(ResideMenuActivity.this, R.drawable.menu_payments_selector, "    PAYMENTS", this, 3);
		itemProfile = new ResideMenuItem(ResideMenuActivity.this, R.drawable.menu_profile_selector, "  PROFILE", this, 4);
		itemSupport = new ResideMenuItem(ResideMenuActivity.this, R.drawable.menu_support_selector, "                CUSTOMER CARE", this, 5);
		itemShare = new ResideMenuItem(ResideMenuActivity.this, R.drawable.menu_share_selector, "SHARE", this, 6);
		itemAbout = new ResideMenuItem(ResideMenuActivity.this, R.drawable.menu_about_selector, "ABOUT", this, 7);
		itemLogout = new ResideMenuItem(ResideMenuActivity.this, R.drawable.menu_logout_selector, "  LOGOUT", this, 8);

		resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemBookings, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemPayment, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemSupport, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemShare, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemAbout, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemLogout, ResideMenu.DIRECTION_LEFT);

		//You can disable a direction by setting ->
		resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
		//===============   My chnge==================

		/*resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);*/

		findViewById(R.id.title_bar_left_menu).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Utility.printLog("ResideMenuActivity inside onClick DIRECTION_LEFT");
				resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});
		//=========================My Change========================
        findViewById(R.id.eserviss).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Utility.printLog("ResideMenuActivity inside onClick DIRECTION_LEFT");
				resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});
		//=========================My Change========================
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return resideMenu.dispatchTouchEvent(ev);
	}


	@Override
	public void mi_Click(View view) {
		// TODO Auto-generated method stub
		Utility.printLog("ResideMenuActivity inside onClick view=" + view.getTag());

		if ((Integer) (view.getTag()) == 1) {
			edit_profile.setVisibility(View.INVISIBLE);
			page_name.setText(navMenuTitles[0]);

			displayView(0);
		} else if ((Integer) (view.getTag()) == 2) {
			edit_profile.setVisibility(View.INVISIBLE);
			page_name.setText(navMenuTitles[1]);
			displayView(1);
		} else if ((Integer) (view.getTag()) == 3) {
			page_name.setText(navMenuTitles[2]);
			edit_profile.setVisibility(View.INVISIBLE);
			displayView(2);
		} else if ((Integer) (view.getTag()) == 4) {
			edit_profile.setVisibility(View.VISIBLE);
			page_name.setText(navMenuTitles[3]);
			displayView(3);
		} else if ((Integer) (view.getTag()) == 5) {
			edit_profile.setVisibility(View.INVISIBLE);
			/*page_name.setText(navMenuTitles[4]);
			displayView(4)*/
			;
			if (Utility.isNetworkAvailable(ResideMenuActivity.this)) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ResideMenuActivity.this);

				// set title
				alertDialogBuilder.setTitle("Customer Care");

				// set dialog message
				alertDialogBuilder
						.setMessage("Need help Done hesitate to call us now?")
						.setCancelable(false)

						.setPositiveButton("Call", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								//closing the application
								Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+" + "96171111308"));
								if (ActivityCompat.checkSelfPermission(ResideMenuActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
									// TODO: Consider calling
									//    ActivityCompat#requestPermissions
									// here to request the missing permissions, and then overriding
									//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
									//                                          int[] grantResults)
									// to handle the case where the user grants the permission. See the documentation
									// for ActivityCompat#requestPermissions for more details.
									return;
								}
								startActivity(dialIntent);

							}
						})

						.setNegativeButton("Cancel",new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog,int id)
							{
								//closing the application
								dialog.dismiss();
							}
						});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
				alertDialog.show();

			}
			else
			{
				network_error.setVisibility(View.VISIBLE);
				Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), ResideMenuActivity.this);
			}
		}else if ((Integer)(view.getTag()) == 6){	edit_profile.setVisibility(View.INVISIBLE);

			page_name.setText(navMenuTitles[5]);
			displayView(5);
		}else if ((Integer)(view.getTag()) ==7){
			edit_profile.setVisibility(View.INVISIBLE);
			page_name.setText(navMenuTitles[6]);
			displayView(6);
		}else if((Integer)(view.getTag()) == 8){
			edit_profile.setVisibility(View.INVISIBLE);

			if(Utility.isNetworkAvailable(ResideMenuActivity.this))
			{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ResideMenuActivity.this);

				// set title
				alertDialogBuilder.setTitle(getResources().getString(R.string.alert));

				// set dialog message
				alertDialogBuilder
				.setMessage(getResources().getString(R.string.logout_alert_message))
				.setCancelable(false)

				.setPositiveButton(getResources().getString(R.string.yes),new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog,int id)
					{
						//closing the application
						dialog.dismiss();
						if(Utility.isNetworkAvailable(ResideMenuActivity.this))
						{
							new BackgroundLogOutTask().execute();

						}
						else
						{
							Toast.makeText(ResideMenuActivity.this,getResources().getString(R.string.network_connection_fail), Toast.LENGTH_LONG).show();
						}
					}
				})

				.setNegativeButton(getResources().getString(R.string.no),new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog,int id)
					{
						//closing the application
						dialog.dismiss();
					}
				});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
				alertDialog.show();
			}
			else
			{
				network_error.setVisibility(View.VISIBLE);
				Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), ResideMenuActivity.this);
			}
		}
		Typeface roboto_condensed = Typeface.createFromAsset(ResideMenuActivity.this.getAssets(),"fonts/BebasNeue.otf");

		page_name.setTypeface(roboto_condensed);
		resideMenu.closeMenu();
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) 
	{
//================================My Change===================================
		page_name.setVisibility(View.INVISIBLE);
		logo.setVisibility(View.VISIBLE);
		//===============My Change====================
		// update the main content by replacing fragments
		/*if(position == 0)
		{
			page_name.setVisibility(View.INVISIBLE);
			logo.setVisibility(View.VISIBLE);
		}
		else
		{
			logo.setVisibility(View.INVISIBLE);
			page_name.setVisibility(View.VISIBLE);
		}*/
		
		if(currentTabStatus==position) 
		{
			resideMenu.closeMenu();
		}
		else 
		{
			switch (position) 
			{
			case 0:
				currentTabStatus=0;
				resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
				changeFragment(new HomePageFragment());
				break;

			case 1:
				currentTabStatus=1;
				resideMenu.setSwipeDirectionEnable(ResideMenu.DIRECTION_LEFT);
				changeFragment(new Appointments());
				break;

			case 2:
				currentTabStatus=2;
				resideMenu.setSwipeDirectionEnable(ResideMenu.DIRECTION_LEFT);
				changeFragment(new PaymentFragment());
				break;

			case 3:
				currentTabStatus=3;
				resideMenu.setSwipeDirectionEnable(ResideMenu.DIRECTION_LEFT);
				changeFragment(new ProfileFragment());
				break;
			case 4:
				currentTabStatus=4;
				resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
				changeFragment(new HomePageFragment());
				break;

			case 5:
				currentTabStatus=5;
				resideMenu.setSwipeDirectionEnable(ResideMenu.DIRECTION_LEFT);
				changeFragment(new InviteFragment());
				break;

			case 6:
				currentTabStatus=6;
				resideMenu.setSwipeDirectionEnable(ResideMenu.DIRECTION_LEFT);
				changeFragment(new AboutFragment());
				break;
			default:
				break;
			}
		}
	}

	private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
		@Override
		public void openMenu() {
			//  Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void closeMenu() {
			// Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
		}
	};

	private void changeFragment(Fragment targetFragment)
	{
		resideMenu.clearIgnoredViewList();
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.main_fragment, targetFragment, "fragment")
		.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
		.commit();
	}

	// What good method is to access resideMenu？
	public ResideMenu getResideMenu(){
		return resideMenu;
	}

	class BackgroundLogOutTask extends AsyncTask<String,Void,String>
	{

		LogoutResponse response;
		String jsonResponse = null;
		SessionManager session;
		private ProgressDialog dialogL;

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			dialogL=Utility.GetProcessDialog(ResideMenuActivity.this);

			if (dialogL!=null) 
			{
				dialogL.show();
			}
		}

		@Override
		protected String doInBackground(String... params)
		{

			session=new SessionManager(ResideMenuActivity.this);
			session.getSessionToken();

			//
			//	String url=VariableConstants.BASE_URL+"process.php/logout";
			String url=VariableConstants.BASE_URL+"logout";

			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			Utility.printLog("dataandTime "+curenttime);

			Map<String, String> kvPairs = new HashMap<String, String>();
			kvPairs.put("ent_sess_token",session.getSessionToken());
			kvPairs.put("ent_dev_id",getDeviceId(ResideMenuActivity.this));
			kvPairs.put("ent_user_type","2");
			kvPairs.put("ent_submit","Submit");
			kvPairs.put("ent_date_time",curenttime);

			HttpResponse httpResponse = null;
			try {
				httpResponse = Utility.doPost(url,kvPairs);
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Utility.printLog( "doPost Exception......."+e1);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Utility.printLog( "doPost Exception......."+e1);
			}


			if (httpResponse!=null) {

				try {
					jsonResponse = EntityUtils.toString(httpResponse.getEntity());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utility.printLog( "doPost Exception......."+e);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utility.printLog( "doPost Exception......."+e);
				}
			}
			Log.i("Logout Response: ","GetLogoutResponse: "+jsonResponse);
			Log.i("Logout Response: ",jsonResponse);



			return null;
		}

		@Override
		protected void onPostExecute(String result) 
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (dialogL!=null)
			{
				dialogL.dismiss();
			}

			if (jsonResponse!=null) 
			{
				try{
					Gson gson = new Gson();
					response=gson.fromJson(jsonResponse,LogoutResponse.class);
					NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
					notificationManager.cancelAll();
					Utility.printLog("DONE WITH GSON");
				}
				catch(Exception e)
				{
					Utility.ShowAlert("Server Error", ResideMenuActivity.this);
				}
			}
			else
			{
				Toast.makeText(ResideMenuActivity.this,getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
			}

			if(response!=null)
			{
				session.setIsLogin(false);
				//session.clearSession();
				Intent intent=new Intent(ResideMenuActivity.this,MainActivity.class);
				startActivity(intent);
				finish();

			}
		}
	}


	public static String getDeviceId(Context context)
	{
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();

	}
	@Override
	protected void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
		Utility.printLog("INSIDE ON RESUME MAIN ACTIVITY");

		registerReceiver(receiver, filter);

		SessionManager session =new SessionManager(ResideMenuActivity.this);
		if(session.isInvoiceRaised())
		{
			session.storeBookingId("0");
			Intent intent=new Intent(ResideMenuActivity.this,InvoiceActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
		
		unregisterReceiver(receiver);
	}

	@Override
	public void onBackPressed() 
	{
		if(currentTabStatus==0)//HomePage
		{
			if(backPressedToExitOnce) 
			{
				//HomePageFragment.Car_Type_Id="1";
				finish();
				overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			} 
			else 
			{
				this.backPressedToExitOnce = true;
				Toast.makeText(ResideMenuActivity.this,"press again to exit",Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable() 
				{
					@Override
					public void run() 
					{
						backPressedToExitOnce = false;
					}
				}, 2000);
			}

		}
		else
		{
			page_name.setVisibility(View.INVISIBLE);
			logo.setVisibility(View.VISIBLE);
			
			currentTabStatus=0;
			resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
			changeFragment(new HomePageFragment());
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) 
	{
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
//=======================================================



}
