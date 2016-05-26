package com.app.taxisharingDriver;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.app.taxisharingDriver.pojo.AppointmentDetailList;
import com.app.taxisharingDriver.response.AppointmentDetailData;
import com.app.taxisharingDriver.response.CancelResponse;
import com.app.taxisharingDriver.response.UpdateAppointMentstatus;
import com.app.taxisharingDriver.utility.ConnectionDetector;
import com.app.taxisharingDriver.utility.LocationUpdateNew;
import com.app.taxisharingDriver.utility.LocationUpdateNew.LocationResult;
import com.app.taxisharingDriver.utility.NetworkConnection;
import com.app.taxisharingDriver.utility.PublishUtility;
import com.app.taxisharingDriver.utility.Scaler;
import com.app.taxisharingDriver.utility.SessionManager;
import com.app.taxisharingDriver.utility.Utility;
import com.app.taxisharingDriver.utility.VariableConstants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.javapapers.android.maps.path.HttpConnection;
import com.javapapers.android.maps.path.PathJSONParser;
import com.pubnub.api.Pubnub;

public class IAmOnTheWay extends FragmentActivity implements View.OnClickListener,OnMapClickListener, LocationListener
{
	private static boolean mDebugLog = true;
	private static String mDebugTag = "IHaveArrivedActivity";	
	void logError(String msg)
	{
		if (mDebugLog) 
		{
			Utility.printLog(mDebugTag, msg);
		}
	}
	private TextView passengerName,passengerfulladdress,bookingId,network_text,actionbar_textview;
	private Button cancelButton;
	private ImageView direction,passengerPhone;
	private RelativeLayout zoom_in,zoom_out,normal_map,hybrid_map;
	private RelativeLayout network_bar;
	private GoogleMap googleMap;
	private Marker secondmarker;
	private ActionBar actionBar;
	private AppointmentDetailList appointmentDetailList;
	private AppointmentDetailData appointmentDetailData;
	//private android.widget.ImageView  detailimageview;
	private LocationUpdateNew newLocationFinder;
	private Button ihavearrived;
	private LatLng aptLatLng;
	private LatLng currentLatLng ;
	double mLatitude ;
	double mLongitude ; 
	private double previousmLatitude ;
	private double previousmLongitude ; 
	private boolean isFirsttime;
	private boolean issendnotificationCalled = true;
	private ProgressDialog mdialog;
	private Pubnub pubnub;
	private double apntLat;
	private double apntLong;
	private String reason;
	private Marker driver_marker;
	private SessionManager sessionManager;
	private LocationManager locationManager;
	private Location location;
	private String provider;
	private LatLng currentLatiLngi;
	private IntentFilter filter;
	private BroadcastReceiver receiver;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		setContentView(R.layout.imon_the_way);
		overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
		initlayoutid();
		sessionManager = new SessionManager(this);

		Bundle bundle=getIntent().getExtras();
		appointmentDetailList = (AppointmentDetailList) bundle.getSerializable(VariableConstants.APPOINTMENT);
		appointmentDetailData = appointmentDetailList.getAppointmentDetailData();
		// Get the location manager
		getLocation(IAmOnTheWay.this);
		
		if(!isFirsttime) 
		{
			setupMap();
		}
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatiLngi,	14));
		//driver_marker=googleMap.addMarker(new MarkerOptions().position(currentLatiLngi).title("First Point").icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon)));
		sessionManager.setIsAppointmentAccept(false);
		pubnub=ApplicationController.getInstacePubnub();
		//HomeFragment.flagForHomeFragmentOpened = false;
		sessionManager.setIsFlagForOther(true);
		initActionBar();
		/*actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setIcon(android.R.color.transparent);

		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));
		Typeface robotoBoldCondensedItalic = Typeface.createFromAsset(getAssets(), "fonts/Zurich Condensed.ttf");
		try 
		{
			int actionBarTitle = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
			TextView actionBarTitleView = (TextView) getWindow().findViewById(actionBarTitle);

			if(actionBarTitleView != null)
			{
				actionBarTitleView.setTypeface(robotoBoldCondensedItalic);
				actionBarTitleView.setTextColor(getResources().getColor(R.color.white));
			}
			actionBar.setTitle(getResources().getString(R.string.imonthewaymessage));
		} 
		catch (NullPointerException e)
		{
		}
		ihavearrived.setTypeface(robotoBoldCondensedItalic);*/
		/*double arrayOfscallingfactor[]=Scaler.getScalingFactor(IAmOnTheWay.this);
		double width = (160)*arrayOfscallingfactor[0];
		double height = (150)*arrayOfscallingfactor[1];
		String imageUrl=utility.getImageHostUrl(this)+appointmentDetailData.getpPic();
		Picasso.with(this) //
		.load(imageUrl) //
		.placeholder(R.drawable.ic_launcher) //
		.error(R.drawable.ic_launcher).fit()
		.resize((int)Math.round(width),(int)Math.round(height))	 //
		.into(detailimageview);*/
		//	detailimageview.setImageUrl(utility.getImageHostUrl(this)+appointmentDetailList.getpPic(), imageLoader,"CircularImge",this);
		
		
		filter = new IntentFilter();
		filter.addAction("com.app.driverapp.internetStatus");
		receiver = new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{
				try 
				{
					Bundle bucket=intent.getExtras();
					
					String status = bucket.getString("STATUS");

					if("1".equals(status))
					{
						network_bar.setVisibility(View.GONE);
					}
					else
					{
						if (!Utility.isNetworkAvailable(IAmOnTheWay.this))
						{
							network_bar.setVisibility(View.VISIBLE);
							return;
						}
						else if (!NetworkConnection.isConnectedFast(IAmOnTheWay.this)) 
						{
							network_bar.setVisibility(View.VISIBLE);
							network_text.setText(getResources().getString(R.string.lownetwork));
							return;
						}
					}
				} 
				catch (Exception e)
				{
					Utility.printLog("BroadcastReceiver Exception "+e);
				}
			}
		};
		
		passengerName.setText(appointmentDetailData.getfName());
		//passengerPhone.setText(appointmentDetailData.getMobile());
		passengerfulladdress.setText(appointmentDetailData.getAddr1()/*+""+"\n"+appointmentDetailData.getAddr2()*/);
		bookingId.setText(appointmentDetailData.getBid());


		//selectedindex = bundle.getInt("selectedindex");
		//selectedListIndex = bundle.getInt("horizontapagerIndex");
		if(appointmentDetailData != null)
		{
			Utility.printLog("AAA Appointment Detail"+appointmentDetailData.getPasChn());
		}
		else 
		{
			Utility.printLog("Rahul");
		}

		Typeface zurichLightCondensed = Typeface.createFromAsset(getAssets(), "fonts/Zurich Light Condensed.ttf");
		passengerName.setTypeface(zurichLightCondensed);
		//passengerPhone.setTypeface(zurichLightCondensed);
		passengerfulladdress.setTypeface(zurichLightCondensed);
		passengerName.setTextColor(Color.rgb(51, 51, 51));
		passengerfulladdress.setTextColor(Color.rgb(153, 153, 153));
		newLocationFinder = new LocationUpdateNew();
		newLocationFinder.getLocation(this, mLocationResult);

	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case android.R.id.home:
			Intent intent = new Intent(IAmOnTheWay.this,PendingBooking.class);
			startActivity(intent);
			finish();
			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@SuppressLint("NewApi")
	private void initActionBar()
	{
		actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));
		LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout mActionBarCustom = (RelativeLayout)inflater.inflate(R.layout.custom_actionbar_back, null);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setIcon(android.R.color.transparent);
		actionbar_textview=(TextView)mActionBarCustom.findViewById(R.id.actionbar_textview);
		cancelButton = (Button)mActionBarCustom.findViewById(R.id.cancel);
		cancelButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				getCancelAlertDialog();
			}
		});
		/*back_button = (Button)mActionBarCustom.findViewById(R.id.back);
		back_button.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(IAmOnTheWay.this,PendingBooking.class);
				startActivity(intent);
				finish();
			}
		});*/
		actionbar_textview.setText(getResources().getString(R.string.imonthewaymessage));
		actionbar_textview.setGravity(Gravity.CENTER);
		double scaler[]=Scaler.getScalingFactor(this);
		int padding = (int)Math.round(80*scaler[0]);
		actionbar_textview.setPadding(padding, 0,0,0);
		actionBar.setCustomView(mActionBarCustom);
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) 
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.ihavereachedmenubutton, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private void startGoogleMap()
	{
		double apntLat = appointmentDetailData.getPickLat();
		double apntLong = appointmentDetailData.getPickLong();
		String muri="http://maps.google.com/maps?saddr="+sessionManager.getDriverCurrentLat()+","+sessionManager.getDriverCurrentLongi()+"&daddr="+apntLat+","+apntLong;
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(muri));
		startActivity(intent);
	}

	LocationResult mLocationResult = new LocationResult()
	{
		public void gotLocation(final double latitude, final double longitude,final float rotation)
		{
			Utility.printLog("updated1 lat="+latitude+" lng="+longitude);
			if (latitude == 0.0 || longitude == 0.0) 
			{
				runOnUiThread(new Runnable() 
				{
					public void run() 
					{
						showGPSDisabledAlertToUser();
					}
				});    
				return;
			}
			else
			{
				runOnUiThread(new Runnable() 
				{
					public void run() 
					{
						mLatitude = latitude;
						mLongitude = longitude;  

						if(previousmLatitude == mLatitude && previousmLongitude == mLongitude) 
						{

						}
						else 
						{
							if (isFirsttime && issendnotificationCalled) 
							{
								previousmLatitude = mLatitude ;
								previousmLongitude = mLongitude;; 
								//driver_marker.setPosition(new LatLng(mLatitude, mLongitude));
								secondmarker.setRotation(rotation);

								apntLat = appointmentDetailData.getPickLat();
								apntLong = appointmentDetailData.getPickLong();

								double d = calculateDistance(apntLat, apntLong, mLatitude, mLongitude);
								if (d<100)
								{  
									issendnotificationCalled=false;
									//sendNotificationTOPassenger(6);
								}
								else 
								{

								}
							}
						}
					}
				});
			}
		}
	};

	public  double calculateDistance(double fromLatitude,double fromLongitude,double toLatitude,double toLongitude)
	{
		float results[] = new float[1];

		try 
		{
			Location.distanceBetween(fromLatitude,fromLongitude, toLatitude, toLongitude, results);
		} catch (Exception e)
		{
			if (e != null)
				e.printStackTrace();
		}

		int dist = (int) results[0];
		if(dist<=0)
			return 0D;

		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		// DecimalFormat twoDForm = new DecimalFormat("#.##");
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(dfs);

		results[0]/=1000D;
		String distance = decimalFormat.format(results[0]);
		double d = Double.parseDouble(distance);
		double km = d * 1.609344f;
		// logDebug("mLocationResult  km "+km);

		double meters=km*1000.0;
		//logDebug("mLocationResult  meters "+meters);
		return meters;
	}
	private void setupMap()
	{
		mLatitude = appointmentDetailData.getPickLat();
		mLongitude = appointmentDetailData.getPickLong();
		apntLat = sessionManager.getDriverCurrentLat();
		apntLong = sessionManager.getDriverCurrentLongi();

		DrowPath(apntLat,apntLong,mLatitude,mLongitude);
	}


	private void DrowPath(double sourcelatitude,double sourcelongitude,double destinationlatitude,double destinationlogitude)
	{
		double apntLat = sourcelatitude;
		double apntLong = sourcelongitude;
		if (apntLat!=0.0 && apntLong!=0.0) 
		{
			isFirsttime = true;
			mLatitude=destinationlatitude;
			mLongitude=destinationlogitude;
			currentLatLng = new LatLng(apntLat,apntLong);
			aptLatLng = new LatLng(mLatitude, mLongitude);
			String url = getMapsApiDirectionsUrl(currentLatLng,aptLatLng);
			ReadTask downloadTask = new ReadTask();
			downloadTask.execute(url);
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,14));
			addMarkers(aptLatLng,currentLatLng);
		}
		else
		{
			mLatitude=destinationlatitude;
			mLongitude=destinationlogitude;
			aptLatLng = new LatLng(mLatitude, mLongitude);
			MarkerOptions options = new MarkerOptions();
			options.position(aptLatLng);
			googleMap.addMarker(options);

			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(aptLatLng, 14));
			Toast.makeText(IAmOnTheWay.this, "Appointment location  not found", Toast.LENGTH_LONG).show();
		}
	}

	private void addMarkers(LatLng aptLatLng,LatLng currentLatLng)
	{
		if (googleMap != null) 
		{
			secondmarker = googleMap.addMarker(new MarkerOptions().position(aptLatLng).title(getResources().getString(R.string.secondpoint)).icon(BitmapDescriptorFactory.fromResource(R.drawable.home_markers_pickup)));
			driver_marker=googleMap.addMarker(new MarkerOptions().position(currentLatLng).title(getResources().getString(R.string.firstpoint)).icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon)));
		}
	}

	private String getMapsApiDirectionsUrl( LatLng aptLatLng,LatLng currentLatLng)
	{
		
		String waypoints = "origin="+aptLatLng.latitude+","+aptLatLng.longitude+"&"+"destination="+currentLatLng.latitude+","+currentLatLng.longitude;

		Utility.printLog("getMapsApiDirectionsUrl waypoints = "+waypoints );

		String sensor = "sensor=false";
		String params = waypoints + "&" + sensor;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + params;
		return url;
		
		
		/*String waypoints = "waypoints=optimize:true|"+ aptLatLng.latitude + "," + aptLatLng.longitude+ "|" + "|" + currentLatLng.latitude + ","
				+ currentLatLng.longitude + "|" + WALL_STREET.latitude + ","+ WALL_STREET.longitude;

		String sensor = "sensor=false";
		String params = waypoints + "&" + sensor;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + params;
		return url;*/
	}


	private class ReadTask extends AsyncTask<String, Void, String> 
	{
		@Override
		protected String doInBackground(String... url) 
		{
			String data = "";
			try {
				HttpConnection http = new HttpConnection();
				data = http.readUrl(url[0]);
			} 
			catch (Exception e)
			{
				Utility.printLog("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) 
		{
			super.onPostExecute(result);
			new ParserTask().execute(result);
		}
	}

	private class ParserTask extends
	AsyncTask<String, Integer, List<List<HashMap<String, String>>>>
	{

		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				PathJSONParser parser = new PathJSONParser();
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> routes) 
		{
			try 
			{
				ArrayList<LatLng> points = null;
				PolylineOptions polyLineOptions = null;

				// traversing through routes
				for (int i = 0; i < routes.size(); i++) 
				{
					points = new ArrayList<LatLng>();
					polyLineOptions = new PolylineOptions();
					List<HashMap<String, String>> path = routes.get(i);

					for (int j = 0; j < path.size(); j++) 
					{
						HashMap<String, String> point = path.get(j);

						double lat = Double.parseDouble(point.get("lat"));
						double lng = Double.parseDouble(point.get("lng"));
						LatLng position = new LatLng(lat, lng);
						points.add(position);
					}

					polyLineOptions.addAll(points);
					polyLineOptions.width(4);
					polyLineOptions.color(Color.BLUE);
				}

				googleMap.addPolyline(polyLineOptions);
			} catch (Exception e) 
			{
			}

		}
	}

	private void showGPSDisabledAlertToUser()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage(getResources().getString(R.string.gpsdisable))
		.setCancelable(false)
		.setPositiveButton(getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(callGPSSettingIntent);
			}
		});
		alertDialogBuilder.setNegativeButton(getResources().getString(R.string.cancel),	new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)	
			{
				dialog.cancel();
				finish();
			}
		});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
	@Override
	protected void onPause()
	{
		super.onPause();
		locationManager.removeUpdates(this);
		//closing transition animations
		overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
		if (mdialog!=null) 
		{
			mdialog.cancel();
			mdialog.dismiss();
			mdialog=null;
		}
		unregisterReceiver(receiver);
		finish();
	}

	@Override
	public void onLocationChanged(Location location)
	{
		double lat =  (location.getLatitude());
		double lng =  (location.getLongitude());
		sessionManager.setDriverCurrentlat(""+lat);
		sessionManager.setDriverCurrentLongi(""+lng);
		Utility.printLog("onLocationChanged lat="+lat+"  lng="+lng);
		LatLng latlng = new LatLng(lat, lng);
		if(driver_marker!=null)
			driver_marker.setPosition(latlng);
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


	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		if (mdialog!=null) 
		{
			mdialog.cancel();
			mdialog.dismiss();
			mdialog=null;
		}
	}
	private void initlayoutid()
	{

		passengerName=(TextView)findViewById(R.id.passenger_name);
		passengerPhone=(ImageView)findViewById(R.id.passenger_mob);
		passengerfulladdress=(TextView)findViewById(R.id.passengerfulladdress);
		bookingId = (TextView)findViewById(R.id.booking_id_text);
		//detailimageview=(ImageView)findViewById(R.id.detailimageview);
		ihavearrived=(Button)findViewById(R.id.ihavearrived);
		direction = (ImageView)findViewById(R.id.direction);
		network_bar = (RelativeLayout)findViewById(R.id.network_bar);
		network_text = (TextView)findViewById(R.id.network_text);
		
		//cancelButton = (Button)findViewById(R.id.cancel);
		direction.setOnClickListener(this);
		ihavearrived.setOnClickListener(this);
		passengerPhone.setOnClickListener(this);

		zoom_in = (RelativeLayout)findViewById(R.id.Plus);
		zoom_out = (RelativeLayout)findViewById(R.id.minus);
		normal_map = (RelativeLayout)findViewById(R.id.normal_map);
		hybrid_map = (RelativeLayout)findViewById(R.id.hybrid_map);
		zoom_in.setOnClickListener(this);
		zoom_out.setOnClickListener(this);

		normal_map.setOnClickListener(this);
		hybrid_map.setOnClickListener(this);


		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		googleMap = fm.getMap();
		googleMap.setMyLocationEnabled(true);
		googleMap.getUiSettings().setZoomControlsEnabled(false);

	}

	@Override
	protected void onResume() 
	{
		super.onResume();
		if (receiver != null) 
		{
			registerReceiver(receiver, filter);
		}

		locationManager.requestLocationUpdates(provider, 2000, 1, this);
	}

	/**
	 * Method for updating appointment status  
	 * @param responsecode
	 */
	private void sendNotificationToPassenger(final int responsecode)
	{
		Utility utility=new Utility();

		ConnectionDetector connectionDetector=new ConnectionDetector(IAmOnTheWay.this);
		if (connectionDetector.isConnectingToInternet()) 
		{
			String deviceid = Utility.getDeviceId(IAmOnTheWay.this);
			String currenttime = utility.getCurrentGmtTime();
			String passengerEmailid = appointmentDetailData.getEmail();
			String appointdatetime = appointmentDetailData.getApptDt();
			String sessiontoken = sessionManager.getSessionToken();

			String notes="";
			final String mparams[]={sessiontoken,deviceid,passengerEmailid,appointdatetime,""+responsecode ,notes,currenttime};
			mdialog = Utility.GetProcessDialog(IAmOnTheWay.this);
			mdialog.setMessage(getResources().getString(R.string.Pleasewaitmessage));
			mdialog.show();
			mdialog.setCancelable(false);
			RequestQueue queue = Volley.newRequestQueue(this);  // this = context
			String  url = VariableConstants.getAppointmentstatusUpdate_url;
			StringRequest postRequest = new StringRequest(Request.Method.POST, url,
					new Listener<String>()
					{
				@Override
				public void onResponse(String response) 
				{
					Utility.printLog("III sendNotificationtoPassenger ="+response);
					UpdateAppointMentstatus appointMentstatus;
					Gson gson = new Gson();
					appointMentstatus = gson.fromJson(response, UpdateAppointMentstatus.class);	

					try 
					{
						if (mdialog!=null)
						{
							mdialog.dismiss();
							mdialog.cancel();
						}
						//               	   			1 -> (1) Mandatory field missing
						if (appointMentstatus.getErrFlag()==0 && appointMentstatus.getErrNum() == 57)
						{
							//appointmentDetailList.setStatCode(7);
							Intent intent=new Intent(IAmOnTheWay.this, IHaveArrivedActivity.class);
							Bundle bundle=new Bundle();
							sessionManager.setIhavarrived(true);
							Utility.printLog("Latitude  = "+sessionManager.getDriverCurrentLat(),"Longitude = "+sessionManager.getDriverCurrentLongi());
							for (int i = 0; i < 5; i++) 
							{
								publishLocation(sessionManager.getDriverCurrentLat(),sessionManager.getDriverCurrentLongi());
							}
							bundle.putSerializable(VariableConstants.APPOINTMENT, appointmentDetailList);
							intent.putExtras(bundle);
							Utility.printLog("Rahul SendNotification "+bundle);
							startActivity(intent);
							finish();
						}
						else if (appointMentstatus.getErrFlag()==1 && appointMentstatus.getErrNum() == 41)
						{
							// 41 -> (1) Passenger canceled.
							ErrorMessage(getResources().getString(R.string.messagetitle),appointMentstatus.getErrMsg(),true);
						}
						else if (appointMentstatus.getErrFlag()==1&&appointMentstatus.getErrNum()==56)
						{
							// 56 -> (1) Invalid status, cannot update.
							ErrorMessage(getResources().getString(R.string.messagetitle),appointMentstatus.getErrMsg(),false);
						}

						else if (appointMentstatus.getErrFlag()==1&&appointMentstatus.getErrNum()==3)
						{
							// 3 -> (1) Error occurred while processing your request.
							ErrorMessage(getResources().getString(R.string.messagetitle),appointMentstatus.getErrMsg(),false);
						}
						else if (appointMentstatus.getErrFlag()==1&&appointMentstatus.getErrNum()==1)
						{
							// 1 -> (1) Mandatory field missing
							ErrorMessage(getResources().getString(R.string.messagetitle), appointMentstatus.getErrMsg(), false);

						}
						else if(appointMentstatus.getErrNum()==6|| appointMentstatus.getErrNum()==7 ||
								appointMentstatus.getErrNum()==94 || appointMentstatus.getErrNum()==96)
						{
							Toast.makeText(getApplicationContext(), appointMentstatus.getErrMsg(), Toast.LENGTH_SHORT).show();
							Intent i = new Intent(getApplicationContext(), SplahsActivity.class);
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(i);
							overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
						}

					}
					catch (Exception e) 
					{
						Utility.printLog("ExceptionException"+e);
						//ErrorMessage(getResources().getString(R.string.messagetitle),getResources().getString(R.string.servererror),false);
					}
				}
					},
					new ErrorListener()
					{
						@Override
						public void onErrorResponse(VolleyError error) {
							// error
							Utility.printLog("Error.Response", error.toString());
							if (mdialog!=null)
							{
								mdialog.dismiss();
								mdialog.cancel();
								//mdialog=null;
							}
							//ErrorMessage(getResources().getString(R.string.messagetitle),getResources().getString(R.string.servererror),false);
						}
					}
					) {    
				@Override
				protected Map<String, String> getParams()
				{ 
					Map<String, String>  params = new HashMap<String, String>(); 
					params.put("ent_sess_token",mparams[0]); 
					params.put("ent_dev_id",mparams[1]);

					params.put("ent_pas_email", mparams[2]); 
					params.put("ent_appnt_dt",mparams[3]);

					params.put("ent_response", mparams[4]);
					params.put("ent_amount", "");
					params.put("ent_doc_remarks", mparams[5]);
					params.put("ent_date_time", mparams[6]);

					return params; 
				}
			};
			int socketTimeout = 60000;//60 seconds - change to what you want
			RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
			postRequest.setRetryPolicy(policy);
			queue.add(postRequest);


		}
		else 
		{
			utility.showDialogConfirm(IAmOnTheWay.this,"Alert"," working internet connection required", false).show();
			//utility.displayMessageAndExit(getActivity(),"Alert"," working internet connection required");
		}
	}

	private void ErrorMessage(String title,String message,final boolean flageforSwithchActivity)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(IAmOnTheWay.this);
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
					Intent intent = new Intent(IAmOnTheWay.this,MainActivity.class);
					startActivity(intent);
					finish();
				}
				else
				{
					// only show message  
					dialog.dismiss();
				}

			}
		});

		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

	private void ErrorMessageForInvalidOrExpired(String title,String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(IAmOnTheWay.this);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(getResources().getString(R.string.cancelbutton),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// Intent intent=new Intent(getActivity(), MainActivity.class);
				// startActivity(intent);
				dialog.dismiss();
			}
		});

		builder.setNegativeButton(getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				SessionManager sessionManager=new SessionManager(IAmOnTheWay.this);
				sessionManager.logoutUser();
				dialog.dismiss();
				Intent intent=new Intent(IAmOnTheWay.this, SplahsActivity.class);
				startActivity(intent);
				finish();
			}
		});

		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

	@Override
	public void onClick(View v) 
	{
		if (v.getId()==R.id.ihavearrived)
		{
			ConnectionDetector connectionDetector=new ConnectionDetector(IAmOnTheWay.this);
			if (connectionDetector.isConnectingToInternet()) 
			{
				sendNotificationToPassenger(6);
			}
		}
		else if (v.getId() == R.id.direction) 
		{
			startGoogleMap();
		}
		else if (v.getId() == R.id.passenger_mob) 
		{
			selectChoice(appointmentDetailData.getMobile());
		}

		else if (v.getId() == R.id.normal_map) 
		{
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		}
		else if (v.getId() == R.id.hybrid_map)
		{
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		}
		else if (v.getId() == R.id.Plus) 
		{
			googleMap.animateCamera(CameraUpdateFactory.zoomIn());
		}
		else if (v.getId() == R.id.minus)
		{
			googleMap.animateCamera(CameraUpdateFactory.zoomOut());
		}
	}
	/**
	 * Method for publish current location to passenger. 
	 * @param latitude
	 * @param longitude
	 */
	public void publishLocation(double latitude,double longitude)
	{
		if (pubnub == null) 
		{
			pubnub = ApplicationController.getInstacePubnub();

		}
		String message ;
		SessionManager sessionManager = new SessionManager(this);
		String mobilestr = sessionManager.getMobile();
		String subscribChannel=sessionManager.getSubscribeChannel();
		message="{\"a\" :\""+6+"\", \"e_id\" :\""+sessionManager.getUserEmailid()+"\", \"lt\" :"+latitude+", \"lg\" :"+longitude+", \"ph\" :\""+mobilestr+"\",\"dt\" :\""+sessionManager.getDate()+"\",\"bid\" :\""+sessionManager.getBOOKING_ID()+"\",\"chn\" :\""+subscribChannel+"\"}";
		Utility.printLog("Publish Location = "+message);
		if (sessionManager.getPasChannel() != null)
		{
			Utility.printLog("Publish Channel = "+sessionManager.getPasChannel());
			//Pubnub Change 17/5/2016
			PublishUtility.publish(sessionManager.getPasChannel(), message, pubnub);
		}
		else
		{
			ErrorMessage(getResources().getString(R.string.messagetitle),getResources().getString(R.string.passengercancelled),true);
		}

	}


	private void selectChoice(final String phoneNo)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(phoneNo);

		builder.setPositiveButton("No",
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();

			}
		});
		builder.setNegativeButton("Call",
				new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				Intent dialIntent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNo));
				startActivity(dialIntent);
			}
		});

		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}
	public void getCancelAlertDialog() 
	{
		final Dialog deleteDialog = new Dialog(this);
		deleteDialog.setTitle(getResources().getString(R.string.whyareyoucanceling));
		deleteDialog.setCanceledOnTouchOutside(false);
		deleteDialog.setContentView(R.layout.cancel_trip);

		((Button) (deleteDialog.findViewById(R.id.clientnotshow))).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				reason = "4";
				getAbortJourney(4);
			}
		});

		((Button) (deleteDialog.findViewById(R.id.wrongaddressshown))).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				reason = "5";
				getAbortJourney(5);
			}
		});

		((Button) (deleteDialog.findViewById(R.id.clientrequested))).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				reason = "6";
				getAbortJourney(6);
			}
		});

		((Button) (deleteDialog.findViewById(R.id.donotcharge))).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				reason = "7";
				getAbortJourney(7);
			}
		});

		((Button) (deleteDialog.findViewById(R.id.other))).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				reason = "8";
				getAbortJourney(8);
			}
		});

		((Button) (deleteDialog.findViewById(R.id.donotcanceltrip))).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteDialog.dismiss();
			}
		});
		deleteDialog.show();
	}

	private void getAbortJourney(final int cancelType)
	{
		Utility utility=new Utility();
		ConnectionDetector connectionDetector=new ConnectionDetector(this);
		if (connectionDetector.isConnectingToInternet()) 
		{
			String deviceid=Utility.getDeviceId(this);
			String curenttime=utility.getCurrentGmtTime();
			String passengerEmailid = appointmentDetailData.getEmail();
			String appointdatetime = appointmentDetailData.getApptDt();
			MainActivity.isResponse=true;
			SessionManager sessionManager=new SessionManager(this);
			String sessiontoken = sessionManager.getSessionToken();
			final String mparams[]={sessiontoken,deviceid,appointdatetime,passengerEmailid,""+cancelType,curenttime};
			mdialog = Utility.GetProcessDialog(this);
			mdialog.setMessage(getResources().getString(R.string.Pleasewaitmessage));
			mdialog.show();
			mdialog.setCancelable(false);
			RequestQueue queue = Volley.newRequestQueue(this);  //this = context
			String  url = VariableConstants.getAbortJourney_url;
			StringRequest postRequest = new StringRequest(Request.Method.POST, url,responseListenerForAbortTrip,errorListenerAbort )
			{    
				@Override
				protected Map<String, String> getParams()
				{ 
					Map<String, String>  params = new HashMap<String, String>(); 
					params.put("ent_sess_token",mparams[0]); 
					params.put("ent_dev_id",mparams[1]);
					params.put("ent_appnt_dt", mparams[2]); 
					params.put("ent_pas_email", mparams[3]); 
					params.put("ent_cancel_type", mparams[4]);
					params.put("ent_date_time", mparams[5]);

					Utility.printLog("AAA paramsRequest"+params);
					return params;
				}
			};
			int socketTimeout = 60000;//60 seconds - change to what you want
			RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
			postRequest.setRetryPolicy(policy);
			queue.add(postRequest);    
		}	 

	}
	Listener<String> responseListenerForAbortTrip=new Listener<String>()
			{
		@Override
		public void onResponse(String response)
		{
			SessionManager sessionManager = new SessionManager(IAmOnTheWay.this);
			Utility.printLog("AAAA AbortTripResponse"+response);
			CancelResponse cancelResponse;
			Gson gson = new Gson();
			cancelResponse = gson.fromJson(response, CancelResponse.class); 
			Utility.printLog("AAAA CancelResponse = "+cancelResponse);
			try 
			{
				if (mdialog!=null)
				{
					mdialog.dismiss();
					mdialog.cancel();
				}
				if (cancelResponse.getErrFlag() == 0 && cancelResponse.getErrNum() == 42)
				{
					for (int i = 0; i < 5; i++) 
					{
						publishLocationCancel(sessionManager.getDriverCurrentLat(),sessionManager.getDriverCurrentLongi());
					}
					sessionManager.setBookingid("");
					ErrorMessageForCancel(getResources().getString(R.string.messagetitle),cancelResponse.getErrMsg(),true);
				}
				else if (cancelResponse.getErrFlag() == 1 && cancelResponse.getErrNum() == 75)
				{
					ErrorMessageForCancel(getResources().getString(R.string.messagetitle),cancelResponse.getErrMsg(),false);
				}
				else if (cancelResponse.getErrFlag() == 1 && cancelResponse.getErrNum() == 32)
				{
					ErrorMessageForCancel(getResources().getString(R.string.messagetitle),cancelResponse.getErrMsg(),false);
				}
				else if (cancelResponse.getErrFlag() == 1 && cancelResponse.getErrNum() == 44)
				{
					ErrorMessageForCancel(getResources().getString(R.string.messagetitle),cancelResponse.getErrMsg(),false);
				}
				else if (cancelResponse.getErrFlag() == 1 && cancelResponse.getErrNum() == 3)
				{
					ErrorMessageForCancel(getResources().getString(R.string.messagetitle),cancelResponse.getErrMsg(),false);
				}
				else if (cancelResponse.getErrFlag() == 1 && cancelResponse.getErrNum() == 1)
				{
					ErrorMessageForCancel(getResources().getString(R.string.messagetitle),cancelResponse.getErrMsg(),false);
				}
				else if(cancelResponse.getErrNum()==6|| cancelResponse.getErrNum()==7 ||
						cancelResponse.getErrNum()==94 || cancelResponse.getErrNum()==96)
				{
					Toast.makeText(getApplicationContext(), cancelResponse.getErrMsg(), Toast.LENGTH_SHORT).show();
					Intent i = new Intent(getApplicationContext(), SplahsActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(i);
					overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
				}


			} 
			catch (Exception e) 
			{
				Utility.printLog("Exception"+e);
				ErrorMessage(getResources().getString(R.string.messagetitle),getResources().getString(R.string.servererror),false);
			}

		}
			};

			ErrorListener errorListenerAbort=new ErrorListener()
			{
				@Override
				public void onErrorResponse(VolleyError error) 
				{
					Toast.makeText(IAmOnTheWay.this, ""+error, Toast.LENGTH_SHORT).show();
				}
			};

			private void ErrorMessageForCancel(String title,String message,final boolean flageforSwithchActivity)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(IAmOnTheWay.this);
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
							SessionManager manager = new SessionManager(IAmOnTheWay.this);
							Intent intent = new Intent(IAmOnTheWay.this,MainActivity.class);
							manager.setIsOnButtonClicked(true);
							startActivity(intent);
							finish();
						}
						else
						{
							// only show message 
							dialog.dismiss();
						}

					}
				});

				AlertDialog	 alert = builder.create();
				alert.setCancelable(false);
				alert.show();
			}

			public void publishLocationCancel(double latitude,double longitude)
			{
				String message;
				SessionManager sessionManager = new SessionManager(IAmOnTheWay.this);
				String subscribChannel=sessionManager.getSubscribeChannel();
				message="{\"a\" :\""+5+"\", \"e_id\" :\""+sessionManager.getUserEmailid()+"\", \"lt\" :"+latitude+", \"lg\" :"+longitude+", \"r\" :\""+reason+"\",\"bid\" :\""+sessionManager.getBOOKING_ID()+"\",\"chn\" :\""+subscribChannel+"\"}";
				Utility.printLog("Publish Location = "+message);

				if (sessionManager.getPasChannel() != null)
				{
					Utility.printLog("Publish Passenger Channel"+sessionManager.getPasChannel());
					//Pubnub Change 17/5/2016
					PublishUtility.publish(sessionManager.getPasChannel(), message, pubnub);
				}
				else
				{
					ErrorMessage(getResources().getString(R.string.messagetitle),getResources().getString(R.string.passengercancelled),true);
				}
			}

			@Override
			public void onMapClick(LatLng arg0) {

			}

			private Location findLocation(Context cntx,String provider)
			{
				locationManager = (LocationManager) cntx.getSystemService(Service.LOCATION_SERVICE);
				if (locationManager.isProviderEnabled(provider)) 
				{
					locationManager.requestLocationUpdates(provider,
							10, 1000, IAmOnTheWay.this);
					if (locationManager != null) 
					{
						location = locationManager.getLastKnownLocation(provider);
						return location;
					}
				}
				return null;
			}

			public double[] getLocation(Context cntx)
			{
				double [] location = new double[2];

				Location gpsLocation = findLocation(cntx,LocationManager.GPS_PROVIDER);

				Location nwLocation = findLocation(cntx,LocationManager.NETWORK_PROVIDER);
				
				if (nwLocation != null) 
				{
					location[0] = nwLocation.getLatitude();
					location[1] = nwLocation.getLongitude();
					provider = LocationManager.NETWORK_PROVIDER;
					onLocationChanged(nwLocation);
					Utility.printLog("data network"+location[0]+"<---->"+location[1]);
				} 
				else
				{
					if (gpsLocation != null) 
					{
						location[0] = gpsLocation.getLatitude();
						location[1] = gpsLocation.getLongitude();
						provider = LocationManager.GPS_PROVIDER;
						onLocationChanged(gpsLocation);
						Utility.printLog("data gps"+location[0]+"<---->"+location[1]);
					} 
				}
				currentLatiLngi = new LatLng(location[0],location[1]);
				return location;
			}

}
