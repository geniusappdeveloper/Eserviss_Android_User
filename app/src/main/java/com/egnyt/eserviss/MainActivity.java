package com.egnyt.eserviss;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.egnyt.eserviss.R;
import com.eserviss.passenger.main.BookAppointmentResponse;

import com.eserviss.passenger.main.ResideMenuActivity;
import com.eserviss.passenger.main.SigninActivity;
import com.eserviss.passenger.main.SignupActivity;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.flurry.android.FlurryAgent;
import com.google.ads.conversiontracking.AdWordsAutomatedUsageReporter;
import com.google.ads.conversiontracking.AdWordsConversionReporter;
import com.google.gson.Gson;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private RelativeLayout login_register;//dotsLayout
	//	private ImageView dot1,dot2,dot3,dot4;	private TextView pageNumber;
	private Button signin, register;
	private SessionManager session;
	public boolean gpsEnabled;
	public boolean gpsFix;
	public double latitude;
	public double longitude;
	private LocationManager locationManager;
	private MyGpsListener gpsListener;
	private long DURATION_TO_FIX_LOST_MS = 10000;
	private long MINIMUM_UPDATE_TIME = 0;
	private float MINIMUM_UPDATE_DISTANCE = 0.0f;
	private Dialog dialog;
	private boolean isGpsEnable = false;
	private Animation anime;
//	Spinner spinnerLanguages;
	//private ImageView amimagetedview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_splash);
		FacebookSdk.sdkInitialize(getApplicationContext());


		keyHash();
//=========================My Change Adwords=================================
		AdWordsConversionReporter.reportWithConversionId(this.getApplicationContext(),
				"940479733", "2nJuCJ6foGYQ9am6wAM", "1.00", true);
//=========================My Change Adwords=================================

		intializeVariables();
		session = new SessionManager(MainActivity.this);


		session.storeCurrencySymbol("$");

		// ask Android for the GPS service
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			isGpsEnable = true;
		}
		// make a delegate to receive callbacks
		gpsListener = new MyGpsListener();
		// ask for updates on the GPS status
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		locationManager.addGpsStatusListener(gpsListener);
		// ask for updates on the GPS location
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
				MINIMUM_UPDATE_TIME, MINIMUM_UPDATE_DISTANCE, gpsListener);

		Utility.printLog("splash gpsEnabled="+gpsEnabled);

		anime = AnimationUtils.loadAnimation(MainActivity.this, R.anim.profile_image_anim1);


		if(Utility.isNetworkAvailable(MainActivity.this))
		{
			Utility.printLog("splash isGpsEnable="+isGpsEnable);
			Thread timer=new Thread()
			{
				@Override
				public void run() 
				{
					// TODO Auto-generated method stub
					try 
					{
						sleep(500);
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally
					{
						if(isGpsEnable)
						{
							if(session.isLoggedIn())
							{
								if(Utility.isNetworkAvailable(MainActivity.this))
								{
									new BackgroundSessionCheck().execute();
								}
								else
								{
									runOnUiThread(new Runnable()
									{
										public void run() 
										{
											Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), MainActivity.this);
										}
									});
								}
							}
							else
							{
								runOnUiThread(new Runnable()
								{
									public void run() 
									{
										//dotsLayout.setVisibility(View.VISIBLE);
										login_register.setVisibility(View.VISIBLE);

										YoYo.with(Techniques.SlideInLeft)
										.duration(700)
										.playOn(findViewById(R.id.signin));

										YoYo.with(Techniques.SlideInRight)
										.duration(700)
										.playOn(findViewById(R.id.register));

										//signin.startAnimation(fadein);
										//register.startAnimation(fadein);
									}
								});
							}
						}
						else
						{
							runOnUiThread(new Runnable()
							{
								public void run() 
								{
									showGpsAlert();
								}
							});
						}
					}
				}
			};
			timer.start();
		}
		else
		{
			ShowAlert();
			//Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), MainActivity.this);
		}

	}

	public  void keyHash(){

		try {
		PackageInfo info = getPackageManager().getPackageInfo(
				"com.egnyt.eserviss",
				PackageManager.GET_SIGNATURES);
		for (Signature signature : info.signatures) {
			MessageDigest md = null;

				md = MessageDigest.getInstance("SHA");

			md.update(signature.toByteArray());
			Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
		}} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void ShowAlert()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

		// set title
		alertDialogBuilder.setTitle("Alert");

		// set dialog message
		alertDialogBuilder
		.setMessage("Network connection fail")
		.setCancelable(false)
		/*.setPositiveButton(getResources().getString(R.string.network_settings), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					startActivity(new Intent(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS));
					finish();
				}
			})*/

		.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog,int id)
			{
				//closing the application
				dialog.dismiss();
				//startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
				finish();
			}
		});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();

	}
/*

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)

	{

		((TextView) spinnerLanguages.getSelectedView())
				.setTextColor(getResources().getColor(R.color.black));
		String spin = spinnerLanguages.getSelectedItem().toString();
		Utility.printLog("AAAAAAA" + spin);

		if (spin.equals("English"))
		{
			*/
/*sessionManager.setLang("en");
			setLocale("en");
			signinbutton.setText(R.string.signin);
			registerbutton.setText(R.string.register);*//*


		}
		else if (spin.equals("العربية"))
		{
			 spinnerLanguages.getSelectedView().setPadding(0, 0, 95, 0);
			*/
/*setLocale("sp");
			sessionManager.setLang("sp");
			signinbutton.setText(R.string.signin);
			registerbutton.setText(R.string.register);*//*

		}

		else
		{
			return;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
*/

	class BackgroundSessionCheck extends AsyncTask<String, Void, String>
	{
		BookAppointmentResponse response;

		@Override
		protected void onPreExecute() 
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) 
		{
			if(Utility.isNetworkAvailable(MainActivity.this))
			{
				String url=VariableConstants.BASE_URL+"checkSession";

				Utility utility=new Utility();
				String curenttime=utility.getCurrentGmtTime();
				Utility.printLog("dataandTime "+curenttime);

				Utility.printLog("splash getSessionToken="+session.getSessionToken());
				Utility.printLog("splash getSessionDeviceId="+session.getDeviceId());


				Map<String, String> kvPairs = new HashMap<String, String>();

				kvPairs.put("ent_sess_token",session.getSessionToken());
				kvPairs.put("ent_dev_id",session.getDeviceId());
				kvPairs.put("ent_user_type","2");
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

				String jsonResponse = null;
				if (httpResponse!=null) 
				{
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
				Utility.printLog("GetDoctoraround Response: ","Session Check: "+jsonResponse);
				Utility.printLog("GetDoctoraround Response: "," "+jsonResponse);

				if (jsonResponse!=null) 
				{
					try
					{
						Gson gson = new Gson();
						response=gson.fromJson(jsonResponse,BookAppointmentResponse.class);
					}
					catch(Exception e)
					{
						runOnUiThread(new Runnable()
						{
							public void run() 
							{
								Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), MainActivity.this);
							}

						});
					}
				}
				else
				{
					runOnUiThread(new Runnable()
					{
						public void run() 
						{
							Toast.makeText(MainActivity.this,getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
						}

					});
				}
			}
			else
			{
				runOnUiThread(new Runnable()
				{
					public void run() 
					{
						Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), MainActivity.this);
					}
				});
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) 
		{
			super.onPostExecute(result);

			if(response!=null)
			{
				if(response.getErrFlag().equals("0"))
				{
					if(Utility.isNetworkAvailable(MainActivity.this))
					{
						Intent intent=new Intent(MainActivity.this,ResideMenuActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
					}
					else
					{
						Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), MainActivity.this);
					}
				}
				else
				{
					Toast.makeText(MainActivity.this, response.getErrMsg(), Toast.LENGTH_SHORT).show();

					session.setIsLogin(false);				
					login_register.setVisibility(View.VISIBLE);
					//dotsLayout.setVisibility(View.VISIBLE);

					YoYo.with(Techniques.BounceInLeft)
					.duration(700)
					.playOn(findViewById(R.id.signin));

					YoYo.with(Techniques.BounceInRight)
					.duration(700)
					.playOn(findViewById(R.id.register));
					//signin.startAnimation(fadein);
					//register.startAnimation(fadein);
				}

			}
			else
			{
				Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), MainActivity.this);
			}
		}

	}
	private void intializeVariables() 
	{
		login_register=(RelativeLayout)findViewById(R.id.login_buttons);
		signin=(Button)findViewById(R.id.signin);
		register=(Button)findViewById(R.id.register);
		//amimagetedview=(ImageView)findViewById(R.id.circle_image);
		//pageNumber = (TextView)findViewById(R.id.swipe_up);

	//	spinnerLanguages = (Spinner) findViewById(R.id.languages);
		/*spinnerLanguages.setPrompt("Select Language/اختار اللغة");
		spinnerLanguages.set*/
	//	spinnerLanguages.setOnItemSelectedListener(this);
		signin.setOnClickListener(this);
		register.setOnClickListener(this);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void onClick(View v) 
	{
		// Listener when Register button is clicked
		if(v.getId()==R.id.register)
		{
			if(Utility.isNetworkAvailable(MainActivity.this))
			{
				final Intent intent=new Intent(MainActivity.this,SignupActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
				this.finish();
			}
			else
			{
				Utility.ShowAlert(getResources().getString(R.string.network_connection_fail),MainActivity.this);
			}
		}
		// Listener when Sign-in button is clicked
		if(v.getId()==R.id.signin)
		{
			if(Utility.isNetworkAvailable(MainActivity.this))
			{
				Intent intent=new Intent(MainActivity.this,SigninActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
				this.finish();
			}
			else
			{
				Utility.ShowAlert(getResources().getString(R.string.network_connection_fail),MainActivity.this);
			}
		}
	}

	public void showGpsAlert()
	{
		dialog = new Dialog(MainActivity.this);
		dialog.setTitle("No Location Access");
		dialog.setContentView(R.layout.gps_alert);

		Button btn = (Button) dialog.findViewById(R.id.go_to_settings);
		btn.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				finish();
			}
		});
		dialog.show();
	}

	private void GpsEnabled()
	{
		Thread timer=new Thread()
		{
			@Override
			public void run() 
			{
				// TODO Auto-generated method stub
				try 
				{
					sleep(1000);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally
				{
					if(session.isLoggedIn())
					{
						if(Utility.isNetworkAvailable(MainActivity.this))
						{
							new BackgroundSessionCheck().execute();
						}
						else
						{
							runOnUiThread(new Runnable()
							{
								public void run() 
								{
									Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), MainActivity.this);
								}
							});
						}
					}
					else
					{
						runOnUiThread(new Runnable()
						{
							public void run() 
							{
								login_register.setVisibility(View.VISIBLE);
								//dotsLayout.setVisibility(View.VISIBLE);

								YoYo.with(Techniques.BounceInLeft)
								.duration(700)
								.playOn(findViewById(R.id.signin));

								YoYo.with(Techniques.BounceInRight)
								.duration(700)
								.playOn(findViewById(R.id.register));

								//signin.startAnimation(fadein);
								//register.startAnimation(fadein);
							}
						});
					}
				}
			}
		};
		timer.start();
	}

	/*public void showSettingsAlert()
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
		// Setting Dialog Title
		alertDialog.setTitle(getResources().getString(R.string.gps_settings));

		// Setting Dialog Message
		alertDialog.setMessage(getResources().getString(R.string.gps_alert_message));

		// On pressing Settings button
		alertDialog.setPositiveButton(getResources().getString(R.string.settings), new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog,int which) 
			{
				dialog.cancel();
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
				finish();
			}
		});

		// on pressing cancel button
		alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
				finish();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}*/


	@Override
	protected void onResume() 
	{
		super.onResume();
		AppEventsLogger.activateApp(this);


		AdWordsConversionReporter.registerReferrer(this.getApplicationContext(),this.getIntent().getData());
		AdWordsAutomatedUsageReporter.enableAutomatedUsageReporting(this.getApplicationContext(), "940479733");
		/** Your code that parses deep links and routes users to the right place. **/
		/*// ask Android for the GPS service
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// make a delegate to receive callbacks
		gpsListener = new MyGpsListener();
		// ask for updates on the GPS status
		locationManager.addGpsStatusListener(gpsListener);
		// ask for updates on the GPS location
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
				MINIMUM_UPDATE_TIME, MINIMUM_UPDATE_DISTANCE, gpsListener);*/
	}


	@Override
	protected void onPause() 
	{
		super.onPause();
		if(locationManager != null) 
		{
			// remove the delegates to stop the GPS
			locationManager.removeGpsStatusListener(gpsListener);
			locationManager.removeUpdates(gpsListener);
			locationManager = null;
		}
	}


	protected class MyGpsListener implements GpsStatus.Listener, LocationListener 
	{
		// the last location time is needed to determine if a fix has been lost
		private long locationTime = 0;

		@Override
		public void onGpsStatusChanged(int changeType) 
		{
			Utility.printLog("splash onGpsStatusChanged="+gpsEnabled);

			if(locationManager != null) 
			{
				// status changed so ask what the change was
				switch(changeType) 
				{
				case GpsStatus.GPS_EVENT_FIRST_FIX:

					Utility.printLog("splash GPS_EVENT_FIRST_FIX");
					if(dialog!=null && dialog.isShowing())
					{
						dialog.dismiss();
						GpsEnabled();
					}
					gpsEnabled = true;
					gpsFix = true;

					break;
				case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
					Utility.printLog("splash GPS_EVENT_SATELLITE_STATUS");
					if(dialog!=null && dialog.isShowing())
					{
						dialog.dismiss();
						GpsEnabled();
					}
					gpsEnabled = true;
					// if it has been more then 10 seconds since the last update, consider the fix lost
					gpsFix = System.currentTimeMillis() - locationTime < DURATION_TO_FIX_LOST_MS;
					break;
				case GpsStatus.GPS_EVENT_STARTED: // GPS turned on
					Utility.printLog("splash GPS_EVENT_STARTED");
					if(dialog!=null && dialog.isShowing())
					{
						dialog.dismiss();
						GpsEnabled();
					}
					gpsEnabled = true;
					gpsFix = false;
					break;
				case GpsStatus.GPS_EVENT_STOPPED: // GPS turned off
					Utility.printLog("splash GPS_EVENT_STOPPED");
					showGpsAlert();
					gpsEnabled = false;
					gpsFix = false;
					break;
				default:
					showGpsAlert();
					Utility.printLog("splash default "+changeType);
					return;
				}
				//updateView();
			}
		}

		@Override
		public void onLocationChanged(Location location) 
		{
			locationTime = location.getTime();
			latitude = location.getLatitude();
			longitude = location.getLongitude();

			if(location.hasAccuracy()) 
			{
				// rolling average of accuracy so "Signal Quality" is not erratic

			}
			//updateView();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {


		}

		@Override
		public void onProviderDisabled(String provider) {
		}

	}




	@Override
	public void onBackPressed() {
		if(dialog!=null && dialog.isShowing())
		{
			dialog.dismiss();
		}
		finish();
		overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
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

