package com.app.taxisharingDriver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.app.taxisharingDriver.response.LocationUpdateResponse;
import com.app.taxisharingDriver.response.LoginResponse;
import com.app.taxisharingDriver.response.LoginResponseDetails;
import com.app.taxisharingDriver.utility.ConnectionDetector;
import com.app.taxisharingDriver.utility.LocationUtil;
import com.app.taxisharingDriver.utility.NetworkConnection;
import com.app.taxisharingDriver.utility.NetworkNotifier;
import com.app.taxisharingDriver.utility.SessionManager;
import com.app.taxisharingDriver.utility.UltilitiesDate;
import com.app.taxisharingDriver.utility.Utility;
import com.app.taxisharingDriver.utility.VariableConstants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

public class LoginActivity extends Activity implements OnClickListener,NetworkNotifier
{
	private RelativeLayout network_bar;
	private TextView network_text;
	private Location gpsLocation;
	private Button submit;
	private EditText email, password,car_id;
	private TextView forgot_password;
	private String deviceid;
	private String regid;
	//private String registerKey;
	public String TAG = "LoginActivity";
	
	GoogleCloudMessaging gcm;
	Context context;
	ProgressDialog dialogL;
	private SessionManager session;
	String SENDER_ID = VariableConstants.PROJECT_ID;
	String latitude ;
	String longitude ;
	private ProgressDialog mdialog;
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	
	private LocationUtil locationUtil;

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public void  initializeVariables() 
	{
		email = (EditText)findViewById(R.id.login_email_id);
		password = (EditText)findViewById(R.id.login_password);
		car_id = (EditText)findViewById(R.id.car_id);
		submit = (Button)findViewById(R.id.login_button);
		forgot_password = (TextView)findViewById(R.id.forgot_password);
		network_bar = (RelativeLayout)findViewById(R.id.network_bar);
		network_text = (TextView)findViewById(R.id.network_text);
		submit.setOnClickListener(this);
		forgot_password.setOnClickListener(this);
		context=getApplicationContext();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initActionBar();
		initializeVariables();
		
		locationUtil = new LocationUtil(this, this);
		checkingNetworkState();
		if (checkPlayServices()) 
		{

			if (gcm == null)
			{
				Utility.printLog("===", "gcm IS null"+gcm);
				gcm=GoogleCloudMessaging.getInstance(LoginActivity.this);
			}
			session=new SessionManager(LoginActivity.this);
			regid=session.getRegistrationId();
			Utility.printLog("", "BackgroundForUpdateToken login regid test ......."+regid);
			if (regid==null) 
			{
				//new BackgroundForRegistrationId().execute();
				deviceid=getDeviceId(context);
				getRegistrationId(LoginActivity.this);
			} else {
				deviceid=getDeviceId(context);
			}

			Utility.printLog("", "doInBackground regid.........."+regid);
			Utility.printLog("===", "doInBackground deviceid"+deviceid);

		} 
		else 
		{
			Utility.printLog("", "No valid Google Play Services APK found.");                        
		}

	}

	public static int getAppVersion(Context context) 
	{
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}



	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) 
	{
		// This sample app persists the registration ID in shared preferences, but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(LoginActivity.class.getSimpleName(),Context.MODE_PRIVATE);
	}


	private String getRegistrationId(Context context) 
	{
		final SharedPreferences prefs = getGCMPreferences(this);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");

		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.

		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(this);

		Utility.printLog("APP registered VERSION:"+registeredVersion,"APP current VERSION"+currentVersion);
		if (registeredVersion != currentVersion) 
		{
			new GCMRegistration().execute();
			return "";
		}
		else
		{
			if (registrationId.isEmpty()) 
			{
				new GCMRegistration().execute();
				return "";
			}
			else
			{
				regid = registrationId;
				Utility.printLog("APP registrationId"+regid);
			}
		}

		return regid;

	} 


	private boolean checkPlayServices() 
	{
		Utility.printLog(TAG, "onCreate checkPlayServices ");
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) 
		{
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) 
			{
				Utility.printLog(TAG, "This device is supported.");
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} 
			else 
			{
				Utility.printLog(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	private class GCMRegistration extends AsyncTask<String, Void, Void>
	{

		@Override
		protected Void doInBackground(String... params) 
		{
			//String msg = "";
			//logDebug("GCMRegistration doInBackground  ");
			try {
				if (gcm == null)
				{
					gcm = GoogleCloudMessaging.getInstance(context);
					//logDebug("GCMRegistration  gcm "+gcm);
				}
				regid = gcm.register(SENDER_ID);

			} 
			catch (IOException ex)
			{

			}
			return null;
		}


		@Override
		protected void onPostExecute(Void result) 
		{	
			super.onPostExecute(result);

			if(regid==null||"".equals(regid))
			{
				Utility.printLog("There is no REGISTRATION ID");

			}
			else
			{
				Utility.printLog("REGISTRATION ID IS"+regid);
			}

		}

	}
	
	public static String getDeviceId(Context context)
	{
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();

	}
	@SuppressLint("NewApi")
	private void initActionBar()
	{
		android.app.ActionBar  actionBar=getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		// actionBar.setBackgroundDrawable(getResources().getDrawable());
		//actionBar.setIcon(R.drawable.login_btn_logo);
		// actionBar.setIcon(getResources().getDrawable(R.drawable.login_screen_back_btn_off));
		actionBar.setTitle("LOGIN");
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case android.R.id.home:

			/*Intent intent=new Intent(LoginActivity.this, SplahsActivity.class);
			Bundle bundle=new Bundle();
			bundle.putBoolean("isFirstrime", false);
			intent.putExtras(bundle);
			startActivity(intent);*/
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}
	private boolean validateFields() 
	{
		if(email.getText().toString().isEmpty())
		{
			showAlert(getResources().getString(R.string.emailfield));
			return false;
		}

		if(password.getText().toString().isEmpty())
		{
			showAlert(getResources().getString(R.string.passfield));
			return false;
		}
		if(car_id.getText().toString().isEmpty())
		{
			showAlert(getResources().getString(R.string.carfield));
			return false;
		}


		return true;
	}
	private void showAlert(String message) 
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle(getResources().getString(R.string.note));

		// set dialog message
		alertDialogBuilder
		.setMessage(message)
		.setCancelable(false)
		/*.setPositiveButton("Refresh",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {



					}
				  })*/
		.setNegativeButton(getResources().getString(R.string.okbuttontext),new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				//closing the application
				dialog.dismiss();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		locationUtil.connectGoogleApiClient();
	}
	@Override
	protected void onStop() 
	{
		super.onStop();
		locationUtil.disconnectGoogleApiClient();
	}

	@Override
	public void onClick(View v) 
	{
		if(v.getId()==R.id.login_button)
		{
			Utility.hideSoftKeyboard(this);
			if(validateFields()) 
			{
				ConnectionDetector connectionDetector=new ConnectionDetector(LoginActivity.this);
				if (connectionDetector.isConnectingToInternet()) 
				{
					loginRequest();
				}
				else
				{
					showAlert(getResources().getString(R.string.network));
				}
			}
		}
		else if (v.getId() == R.id.forgot_password) 
		{
			Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
			startActivity(intent);
		}
	}

	public void showSettingsAlert()
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		// Setting Dialog Title
		alertDialog.setTitle("GPS is settings");

		// Setting Dialog Message
		alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// Setting Icon to Dialog
		//alertDialog.setIcon(R.drawable.delete);

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
			}
		});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
				finish();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

	private void loginRequest()
	{
		dialogL=Utility.GetProcessDialog(LoginActivity.this);
		if (dialogL!=null) 
		{
			dialogL.setCancelable(false);
			dialogL.setMessage(getResources().getString(R.string.loginmassege));
			dialogL.show();
		}
		regid=getRegistrationId(LoginActivity.this);

		RequestQueue volleyRequest = Volley.newRequestQueue(this);
		StringRequest myReq = new StringRequest(Request.Method.POST,
				VariableConstants.hostUrl+"masterLogin",

				new Listener<String>()
				{

			@Override
			public void onResponse(String response) 
			{
				if (dialogL!=null) 
				{
					dialogL.dismiss();
					dialogL=null;
				}

				fetchData(response);
			}

			private void fetchData(String jsonResponse) 
			{
				LoginResponse response = null;
				Utility.printLog("AAA jsonResponsejsonResponse"+jsonResponse);

				if (jsonResponse!=null) 
				{
					Gson gson = new Gson();
					response=gson.fromJson(jsonResponse, LoginResponse.class);	
					Utility.printLog("AAA ResponseResponse"+response);											
				}
				else
				{
					runOnUiThread(new Runnable()
					{
						public void run() 
						{
							Toast.makeText(LoginActivity.this,"Request Timeout !!", Toast.LENGTH_SHORT).show();
						}
					});
				}

				if(response!=null)
				{
					if(response.getErrFlag().equals("0"))
					{

						LoginResponseDetails loginResponseDetails=response.getData();
						SessionManager session=new SessionManager(LoginActivity.this);
						session.storeSessionToken(loginResponseDetails.getToken());
						session.storeDeviceId(deviceid);
						session.createSession();
						session.storeLoginId(email.getText().toString());
						session.storeChannelName(loginResponseDetails.getChn());
						session.storeUserEmail(loginResponseDetails.getEmail());
						session.setSubscribeChannel(loginResponseDetails.getSusbChn());
						session.setListnerChannel(loginResponseDetails.getListner());
						session.storeUserEmailid(loginResponseDetails.getEmail());
						session.storeVehTypeId(loginResponseDetails.getVehTypeId());
						session.setDriverName(loginResponseDetails.getFname()+" "+loginResponseDetails.getLname());
						session.setDriverProfilePic(loginResponseDetails.getProfilePic());
						session.setIsOnButtonClicked(true);
						//getMasterUpdateLocation();
						startService(ApplicationController.getMyServiceInstance());
						Intent intent=new Intent(LoginActivity.this,MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(intent);
						finish();
						//Toast.makeText(getApplicationContext(), "Login successfull !!",Toast.LENGTH_SHORT).show();										
						//overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);

					}
					else if (response.getErrFlag().equals("1")&&response.getErrNum().equals("13"))
					{
						///Multiple logins not supported!
						ErrorMessage(getResources().getString(R.string.messagetitle),response.getErrMsg(),false);

					}
					else if (response.getErrFlag().equals("1")&&response.getErrNum().equals("8"))
					{
						//The email or password you entered is incorrect.
						ErrorMessage(getResources().getString(R.string.messagetitle),response.getErrMsg(),false);

					}
					else if (response.getErrFlag().equals("1")&&response.getErrNum().equals("5"))
					{
						//Device type not supported
						ErrorMessage(getResources().getString(R.string.messagetitle),response.getErrMsg(),false);

					}
					else if (response.getErrFlag().equals("1")&&response.getErrNum().equals("11"))
					{
						//Thank you for providing your details! We are not available in your area yet, but will inform you as soon as we are!
						ErrorMessageForPreMdNotinYourArea(getResources().getString(R.string.messagetitle),response.getErrMsg(),Integer.parseInt(response.getErrFlag()),Integer.parseInt(response.getErrNum()));

					}
					else if (response.getErrFlag().equals("1")&&response.getErrNum().equals("1"))
					{
						//com.flurry.android.FlurryAgent.logEvent("Mandatory field missing on Login");
						// Mandatory field missing
						ErrorMessage(getResources().getString(R.string.messagetitle),response.getErrMsg(),false);
					}

					else
					{
						Toast.makeText(getApplicationContext(),response.getErrMsg(),Toast.LENGTH_SHORT).show();

					}

				}

			}
				},
				new ErrorListener()
				{

					@Override
					public void onErrorResponse(VolleyError error)
					{
						if (dialogL!=null) 
						{
							dialogL.dismiss();
							dialogL=null;
						}
						ErrorMessage(getResources().getString(R.string.messagetitle),getResources().getString(R.string.servererror), false);
					}

				}){


			protected HashMap<String,String> getParams() throws com.android.volley.AuthFailureError
			{

				Utility utility=new Utility();
				String curenttime=utility.getCurrentGmtTime();
				String dataandTime=UltilitiesDate.getLocalTime(curenttime);
				
				
				String latitude= ""+session.getDriverCurrentLat();
				String longitude = ""+session.getDriverCurrentLongi();
				
				
				Utility.printLog("deviceid= "+deviceid+" regid="+regid);
				Utility.printLog(" latitude :="+latitude,"longitude : ="+longitude);

				HashMap<String, String> kvPairs = new HashMap<String, String>();

				kvPairs.put("ent_email",email.getText().toString());
				kvPairs.put("ent_password",password.getText().toString());
				kvPairs.put("ent_dev_id",deviceid);
				kvPairs.put("ent_push_token",regid);
				kvPairs.put("ent_device_type","2");
				kvPairs.put("ent_date_time",dataandTime);
				kvPairs.put("ent_car_id", car_id.getText().toString());
				kvPairs.put("ent_lat",latitude);
				kvPairs.put("ent_long", longitude);
				Utility.printLog(" getParams "+kvPairs);
				return kvPairs;  
			}

		};
		int socketTimeout = 60000;//60 seconds - change to what you want
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		myReq.setRetryPolicy(policy);
		volleyRequest.add(myReq);
	}

	private void ErrorMessage(String title,String message,final boolean flageforSwithchActivity)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

	private void ErrorMessageForPreMdNotinYourArea(String title,final String message,final int errorFlag,final int errornum)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(getResources().getString(R.string.cancelbutton),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				finish();
				dialog.dismiss();
			}
		});

		builder.setNegativeButton(getResources().getString(R.string.email),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// Intent intent=new Intent(SignUpOne.this, MainActivityDrower.class);
				// startActivity(intent);
				String messageBody=getResources().getString(R.string.emailbodytext);
				StringBuffer buffer=new StringBuffer(messageBody);
				buffer.append("\n");
				buffer.append(getResources().getString(R.string.accountdetails));
				buffer.append("\n");
				buffer.append(getResources().getString(R.string.email));
				buffer.append(email.getText().toString());
				//buffer.append(b)
				Intent emailIntent = new Intent(Intent.ACTION_VIEW);
				Uri data = Uri
						.parse("mailto:?subject="
								+ getResources().getString(R.string.putsubject)
								+ "&body="+buffer.toString()
								+ "&to=" 
								+ "admin@privemd.com");

				emailIntent.setData(data);
				startActivity(emailIntent);
				//admin@privemd.com

				dialog.dismiss();
			}
		});


		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}
	


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
					Toast.makeText(LoginActivity.this, locationUpdateResponse.getErrMsg(), Toast.LENGTH_SHORT).show();

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

	ErrorListener errorListenerofMasreStatus=new ErrorListener()
	{
		@Override
		public void onErrorResponse(VolleyError error) 
		{
			Toast.makeText(LoginActivity.this, ""+error, Toast.LENGTH_SHORT).show();
		}
	};
	
	private void checkingNetworkState()
	{
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run()
			{
				if (!(Utility.isNetworkAvailable(LoginActivity.this)) && !(gpsLocation != null))
				{
					network_bar.setVisibility(View.VISIBLE);
					network_text.setText(getResources().getString(R.string.gpsnetwork));
					return;
				}
				else if (!Utility.isNetworkAvailable(LoginActivity.this))
				{
					network_bar.setVisibility(View.VISIBLE);
					return;
				}
				else if (!NetworkConnection.isConnectedFast(LoginActivity.this)) 
				{
					network_bar.setVisibility(View.VISIBLE);
					network_text.setText(getResources().getString(R.string.lownetwork));
					return;
				}
				
				else
				{
					network_bar.setVisibility(View.GONE);
				}

			}
		} , 2000);
	}

	@Override
	public void updatedInfo(String info) 
	{
		
	}

	@Override
	public void locationUpdates(Location location) 
	{
		session.setDriverCurrentlat(""+location.getLatitude());
		session.setDriverCurrentLongi(""+location.getLongitude());
	}

	@Override
	public void locationFailed(String message)
	{
		
	}
}
