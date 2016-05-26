package com.app.taxisharingDriver;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.Response;
import com.app.taxisharingDriver.pojo.AppointmentDetailList;
import com.app.taxisharingDriver.response.AppointmentData;
import com.app.taxisharingDriver.response.AppointmentDetailData;
import com.app.taxisharingDriver.response.CancelResponse;
import com.app.taxisharingDriver.response.UpdateAppointMentstatus;
import com.app.taxisharingDriver.utility.*;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
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


@SuppressLint("InflateParams")
public class IHaveArrivedActivity extends FragmentActivity implements View.OnClickListener,OnMapClickListener, NetworkNotifier
{
	private static boolean mDebugLog = true;
	private static String mDebugTag = "IHaveArrivedActivity";
	private TextView payment_type;

	void logError(String msg)
	{
		if (mDebugLog) 
		{
			Utility.printLog(mDebugTag, msg);
		}
	}
	private TextView passengerName,passengerfulladdress,bookingId,network_text,timer_text,actionbar_textview;
	private RelativeLayout network_bar;
	private ImageView passengerPhone,direction;
	private GoogleMap googleMap;
	private Marker secondmarker;
	//private ActionBar actionBar;
	private TextView cancelButton;
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
	/*private int selectedindex;
	private int selectedListIndex;*/
	private ProgressDialog mdialog;
	private Pubnub pubnub;
	private double apntLat;
	private double apntLong;
	private String reason;
	private Marker driver_marker;
	private SessionManager sessionManager;
	//private LocationManager locationManager;
	//private String provider;
    private LocationUtil locationUtil;
	private int timecunsumedsecond = 0;
	private boolean runTimer = true;
	private String timeWhile_Paused="";
	private IntentFilter filter;
	private BroadcastReceiver receiver;
	

	@SuppressLint({ "NewApi", "DefaultLocale" })
	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		setContentView(R.layout.ihavereachedscreen);
		overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
		initlayoutid();
		sessionManager = new SessionManager(this);
		locationUtil = new LocationUtil(this, this);
		Bundle bundle=getIntent().getExtras();
		appointmentDetailList = (AppointmentDetailList) bundle.getSerializable(VariableConstants.APPOINTMENT);
		appointmentDetailData = appointmentDetailList.getAppointmentDetailData();
		// Get the location manager
		//locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		/**
		 * Define the criteria how to select the location provider -> use
		 *default
		 *criteria object will select best service based on
		 *Accuracy, power consumption, response, bearing and monetary cost
		 *set false to use best service otherwise it will select the default Sim network
		 *and give the location based on sim network 
		 *now it will first check satellite than Internet than Sim network location
		 */
		/*Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setCostAllowed(false); 
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);

		// Initialize the location fields
		if(location != null) 
		{
			Utility.printLog("Provider" + provider + " has been selected.");*/
			double lati =  sessionManager.getDriverCurrentLat();
			double lngi =  sessionManager.getDriverCurrentLongi();
			LatLng currentLatiLngi = new LatLng(lati,lngi);
			if(!isFirsttime) 
			{
				setupMap();
			}
		driver_marker=googleMap.addMarker(new MarkerOptions().position(currentLatiLngi).title("First Point").icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon)));
			/*onLocationChanged(location);
		} */
		sessionManager.setIsAppointmentAccept(false);
		pubnub=ApplicationController.getInstacePubnub();
		//HomeFragment.flagForHomeFragmentOpened = false;
		sessionManager.setIsFlagForOther(true);
		initActionBar();

		filter = new IntentFilter();
		filter.addAction("com.embed.anddroidpushntificationdemo11.push");
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
					try {
						String action = bucket.getString("action");
						String payload = bucket.getString("payload");
						if ("22".equals(action))
						{
							getAlertDropLoc(getResources().getString(R.string.messagetitle),payload);
						}
						else if ("10".equals(action))
						{
							sessionManager.setIsInBooking(false);
							sessionManager.setCancelPushFlag(false);
							ErrorMessage(getResources().getString(R.string.messagetitle),payload,true);
						}
					}catch (Exception e)
					{
						e.printStackTrace();
					}


					if("1".equals(status))
					{
						network_bar.setVisibility(View.GONE);
					}
					else
					{
						if (!Utility.isNetworkAvailable(IHaveArrivedActivity.this))
						{
							network_bar.setVisibility(View.VISIBLE);
							return;
						}
						else if (!NetworkConnection.isConnectedFast(IHaveArrivedActivity.this))
						{
							network_bar.setVisibility(View.VISIBLE);
							network_text.setText(getResources().getString(R.string.lownetwork));
							return;
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		};
		
		/*
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setIcon(android.R.color.transparent);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));
		//Typeface robotoBoldCondensedItalic = Typeface.createFromAsset(getAssets(), "fonts/Zurich Condensed.ttf");
		try 
		{
			int actionBarTitle = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
			TextView actionBarTitleView = (TextView) getWindow().findViewById(actionBarTitle);

			if(actionBarTitleView != null)
			{
				//actionBarTitleView.setTypeface(robotoBoldCondensedItalic);
				//actionBarTitleView.setTextColor(android.graphics.Color.rgb(51, 51, 51));
			}
			actionBar.setTitle(getResources().getString(R.string.imonthewaymessage));
		} 
		catch (NullPointerException e)
		{
		}*/
		//ihavearrived.setTypeface(robotoBoldCondensedItalic);
		/*double arrayOfscallingfactor[]=Scaler.getScalingFactor(IHaveArrivedActivity.this);
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
		passengerName.setText(appointmentDetailData.getfName().toUpperCase());
		
		//passengerPhone.setText(appointmentDetailData.getMobile());
		passengerfulladdress.setText(appointmentDetailData.getAddr1()/*+""+"\n"+appointmentDetailData.getAddr2()*/);
		bookingId.setText(appointmentDetailData.getBid());

		if ("2".equals(appointmentDetailData.getPayType())) {
			payment_type.setText(getResources().getString(R.string.cash));
		}
		else if ("1".equals(appointmentDetailData.getPayType())) {
			payment_type.setText(getResources().getString(R.string.card));
		}

		//selectedindex = bundle.getInt("selectedindex");
		//selectedListIndex = bundle.getInt("horizontapagerIndex");
		if(appointmentDetailData != null)
		{
			Utility.printLog("AAA Appointment Detail"+appointmentDetailData.getPasChn());
		}
		else 
		{
			Utility.printLog("Devendra");
		}

		Typeface zurichLightCondensed = Typeface.createFromAsset(getAssets(), "fonts/Zurich Light Condensed.ttf");
		passengerName.setTypeface(zurichLightCondensed);
		//passengerPhone.setTypeface(zurichLightCondensed);
		/*passengerfulladdress.setTypeface(zurichLightCondensed);
		passengerName.setTextColor(android.graphics.Color.rgb(51, 51, 51));
		passengerfulladdress.setTextColor(android.graphics.Color.rgb(153, 153, 153));*/
		//newLocationFinder = new LocationUpdateNew();
		//newLocationFinder.getLocation(this, mLocationResult);

		/*passengerPhone.setOnClickListener(new OnClickListener() 
			{

				@Override
				public void onClick(View view) 
				{
					if (appointmentDetailList.getStatus()!=7)
					{
						selectChoice(appointmentDetailList.getMobile());
					}
					else 
					{
						Toast.makeText(IHaveArrivedActivity.this, "Appointment already completed", Toast.LENGTH_SHORT).show();
					}

					//respondAppointment(getItem(position),2,position);
				}

			});

			//Object object=imageUrl;
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
		alert.show();*/
	}
	
	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();
		finish();
	}
	
	
	
	@Override
	protected void onStart() {
		super.onStart();
		locationUtil.connectGoogleApiClient();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		locationUtil.disconnectGoogleApiClient();
	}

	/*@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) 
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.ihavereachedmenubutton, menu);
		return super.onCreateOptionsMenu(menu);
	}*/

	/*@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case android.R.id.home:
			return true;
		case R.id.googlenavigation:

			getCancelAlertDialog();

			//startGoogleMap();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}*/

	private void startGoogleMap()
	{
		double apntLat = appointmentDetailData.getPickLat();
		double apntLong = appointmentDetailData.getPickLong();
		String muri="http://maps.google.com/maps?saddr="+sessionManager.getDriverCurrentLat()+","+sessionManager.getDriverCurrentLongi()+"&daddr="+apntLat+","+apntLong;
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(muri));
		startActivity(intent);
	}


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
		/*  bundle.putDouble("apntLat", apntLat);
	    	bundle.putDouble("apntLong", apntLong);
	    	mLatitude = latitude;
			mLongitude = longitude;  
	    	bundle.putDouble("mLatitude", mLatitude);
	    	bundle.putDouble("mLongitude", mLongitude);
	       	Intent intent=new Intent(AppointmentDetailActivity.this, PathGoogleMapActivity.class);
	    	intent.putExtras(bundle);
	    	startActivity(intent);*/
		DrowPath(apntLat,apntLong,mLatitude,mLongitude);
	}


	private void DrowPath(double sourcelatitude,double sourcelongitude,double destinationlatitude,double destinationlogitude)
	{
		double apntLat = sourcelatitude;
		double apntLong = sourcelongitude;
		if (apntLat!=0.0 && apntLong!=0.0) 
		{
			isFirsttime = true;
			//mLatitude = latitude;
			//mLongitude = longitude;  
			mLatitude=destinationlatitude;
			mLongitude=destinationlogitude;
			currentLatLng = new LatLng(apntLat,apntLong);
			aptLatLng = new LatLng(mLatitude, mLongitude);
			//MarkerOptions options = new MarkerOptions();
			//options.position(aptLatLng);
			//options.position(currentLatLng);
			//options.position(WALL_STREET);
			//googleMap.addMarker(options);
			String url = getMapsApiDirectionsUrl(currentLatLng,aptLatLng);
			ReadTask downloadTask = new ReadTask();
			//downloadTask.execute(url);
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,14));
			addMarkers(aptLatLng,currentLatLng);
		}
		else
		{
			//mLatitude = latitude;
			//mLongitude = longitude;  
			mLatitude=destinationlatitude;
			mLongitude=destinationlogitude;
			// aptLatLng = new LatLng(apntLat,apntLong);
			aptLatLng = new LatLng(mLatitude, mLongitude);
			MarkerOptions options = new MarkerOptions();
			//	options.position(aptLatLng);
			options.position(aptLatLng);
			//options.position(WALL_STREET);
			googleMap.addMarker(options);

			//String url = getMapsApiDirectionsUrl();
			//ReadTask downloadTask = new ReadTask();
			//downloadTask.execute(url);

			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(aptLatLng, 14));
			//addMarkers();
			Toast.makeText(IHaveArrivedActivity.this, "Appointment location  not found", Toast.LENGTH_LONG).show();
		}
	}

	private void addMarkers(LatLng aptLatLng,LatLng currentLatLng)
	{
		if (googleMap != null) 
		{
			secondmarker = googleMap.addMarker(new MarkerOptions().position(aptLatLng).title(getResources().getString(R.string.secondpoint)).icon(BitmapDescriptorFactory.fromResource(R.drawable.home_markers_pickup)));
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
					polyLineOptions.width(5);
					polyLineOptions.color(Color.BLUE);
				}

				if(polyLineOptions != null)
				{
					googleMap.addPolyline(polyLineOptions);
				}


			} catch (Exception e) 
			{
			}

		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		//locationManager.removeUpdates(this);
		//closing transition animations
		overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
		if (mdialog!=null) 
		{
			mdialog.cancel();
			mdialog.dismiss();
			mdialog=null;
		}
		unregisterReceiver(receiver);
		
		runTimer = false;
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
		payment_type= (TextView) findViewById(R.id.payment_type);
		ihavearrived=(Button)findViewById(R.id.ihavearrived);
		direction = (ImageView)findViewById(R.id.direction);
		network_bar = (RelativeLayout)findViewById(R.id.network_bar);
		network_text = (TextView)findViewById(R.id.network_text);
		timer_text = (TextView)findViewById(R.id.timer_text);
		direction.setOnClickListener(this);
		//cancelButton = (Button)findViewById(R.id.cancel);
		passengerPhone.setOnClickListener(this);
		ihavearrived.setOnClickListener(this);


		/*ihavearrived.setOnTouchListener(new OnSwipeTouchListener(IHaveArrivedActivity.this) {
			*//*public void onSwipeLeft() {
				Toast.makeText(IHaveArrivedActivity.this, "left", Toast.LENGTH_SHORT).show();
				//goneText.setVisibility(View.VISIBLE);

			}*//*

			public void onSwipeRight() {
				Toast.makeText(IHaveArrivedActivity.this, "right", Toast.LENGTH_SHORT).show();
				//goneText.setVisibility(View.GONE);
			}

		});*/


		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		googleMap = fm.getMap();
		googleMap.setMyLocationEnabled(true);

	}

	@Override
	protected void onResume() 
	{
		super.onResume();
		
		if (receiver != null) 
		{
			registerReceiver(receiver, filter);
		}

		//locationManager.requestLocationUpdates(provider, 2000, 1, this);

		if (sessionManager.getIhavarrived()) 
		{
			jobTimer();
			sessionManager.setIhavarrived(false);
		}
		else
		{
			if (sessionManager.getElapsedTime() >= 0) 
			{
				timeWhile_Paused = sessionManager.getTimeWhile_Paused();
				if(!"-1".equals(timeWhile_Paused))
				{
					long timePaused = Long.parseLong(timeWhile_Paused);
					long currentTime = System.currentTimeMillis() - timePaused;
					Utility.printLog("TIME ELAPSED EQUALS"+currentTime);
					timecunsumedsecond = sessionManager.getElapsedTime();
					timecunsumedsecond = timecunsumedsecond+(int)Math.round(((float)(currentTime))/1000f);
				}
				else
				{
					timecunsumedsecond = (0);
				}
			}
			else
			{
				timecunsumedsecond = (0);
			}
			runTimer = true;
			jobTimer();
		}
	}

	/**
	 * Method for updating appointment status  
	 * @param responsecode
	 */
	private void sendNotificationToPassenger(final int responsecode)
	{
		Utility utility=new Utility();

		ConnectionDetector connectionDetector=new ConnectionDetector(IHaveArrivedActivity.this);
		if (connectionDetector.isConnectingToInternet()) 
		{
			SessionManager sessionManager=new SessionManager(IHaveArrivedActivity.this);
			String deviceid = Utility.getDeviceId(IHaveArrivedActivity.this);
			String currenttime = utility.getCurrentGmtTime();
			String passengerEmailid = appointmentDetailData.getEmail();
			String appointdatetime = appointmentDetailData.getApptDt();
			String sessiontoken = sessionManager.getSessionToken();
			String notes="";
			final String mparams[]={sessiontoken,deviceid,passengerEmailid,appointdatetime,""+responsecode ,notes,currenttime/*currentdate[0]*/};
			mdialog = Utility.GetProcessDialog(IHaveArrivedActivity.this);
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
						if (appointMentstatus.getErrFlag()==0 && appointMentstatus.getErrNum() == 58)
						{
							//appointmentDetailList.setStatCode(7);
							SessionManager sessionManager=new SessionManager(IHaveArrivedActivity.this);
							sessionManager.setIsAppointmentAccept(false);
							sessionManager.setIsPressedImonthewayorihvreached(true);
							//appointmentDetailList.setIhaveReachedPressed(true);
							//sessionManager.setindexofSelectedAppointment(selectedindex);
							//sessionManager.setindexofSelectedList(selectedListIndex);
							timeWhile_Paused = "0";
							timecunsumedsecond = 0;
							sessionManager.setIBeginJourney(true);
							sessionManager.setAppiontmentStatus(responsecode);


							Utility.printLog("Latitude = "+sessionManager.getDriverCurrentLat(),"Longitude = "+sessionManager.getDriverCurrentLongi());
							Utility.printLog("III appointmentDetailList "+appointmentDetailList);
							for (int i = 0; i < 5; i++) 
							{
								publishLocation(sessionManager.getDriverCurrentLat(),sessionManager.getDriverCurrentLongi());
							}
							sessionManager.setIsInBooking(true);
							//android.widget.Toast.makeText(IHaveArrivedActivity.this, appointMentstatus.getErrMsg(), android.widget.Toast.LENGTH_SHORT).show();
							Intent intent=new Intent(IHaveArrivedActivity.this, BeginJourneyActivity.class);
							Bundle bundle=new Bundle();
							bundle.putSerializable(VariableConstants.APPOINTMENT, (Serializable) appointmentDetailList);
							intent.putExtras(bundle);
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
							ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), appointMentstatus.getErrMsg());
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
			utility.showDialogConfirm(IHaveArrivedActivity.this,"Alert"," working internet connection required", false).show();
			//utility.displayMessageAndExit(getActivity(),"Alert"," working internet connection required");
		}
	}

	private void ErrorMessage(String title,String message,final boolean flageforSwithchActivity)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(IHaveArrivedActivity.this);
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
					Intent intent = new Intent(IHaveArrivedActivity.this,MainActivity.class);
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
		AlertDialog.Builder builder = new AlertDialog.Builder(IHaveArrivedActivity.this);
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
				SessionManager sessionManager=new SessionManager(IHaveArrivedActivity.this);
				sessionManager.logoutUser();
				dialog.dismiss();
				Intent intent=new Intent(IHaveArrivedActivity.this, SplahsActivity.class);
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
			ConnectionDetector connectionDetector=new ConnectionDetector(IHaveArrivedActivity.this);
			if (connectionDetector.isConnectingToInternet()) 
			{
				sendNotificationToPassenger(7);
			}
		}
		else if (v.getId() == R.id.passenger_mob) 
		{
			selectChoice(appointmentDetailData.getMobile());
		}
		else if (v.getId() == R.id.direction)
		{
			startGoogleMap();
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
		//String driverName = sessionManager.getDriverName();
		//String driverProfilePic = sessionManager.getDriverProfilePic();
		String mobilestr = sessionManager.getMobile();
		//float driverRating = sessionManager.getDriverRating();
		String subscribChannel=sessionManager.getSubscribeChannel();
		message="{\"a\" :\""+7+"\", \"e_id\" :\""+sessionManager.getUserEmailid()+"\", \"lt\" :"+latitude+", \"lg\" :"+longitude+", \"ph\" :\""+mobilestr+"\",\"dt\" :\""+sessionManager.getDate()+"\",\"bid\" :\""+sessionManager.getBOOKING_ID()+"\",\"chn\" :\""+subscribChannel+"\"}";
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


	@SuppressLint("NewApi")
	private void initActionBar()
	{
		getActionBar().hide();
		cancelButton = (TextView)findViewById(R.id.cancel_text);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getCancelAlertDialog();
			}
		});

		/*actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));
		LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout mActionBarCustom = (RelativeLayout)inflater.inflate(R.layout.custom_actionbar, null);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setIcon(android.R.color.transparent);
		actionbar_textview=(TextView)mActionBarCustom.findViewById(R.id.actionbar_textview);

		actionbar_textview.setText(getResources().getString(R.string.ihavearrived));
		actionbar_textview.setGravity(Gravity.CENTER);
		double scaler[]=Scaler.getScalingFactor(this);
		int padding = (int)Math.round(80*scaler[0]);
		actionbar_textview.setPadding(padding, 0,0,0);
		actionBar.setCustomView(mActionBarCustom);*/
	}


	public void getCancelAlertDialog() 
	{
		final Dialog deleteDialog = new Dialog(this);
		//deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		deleteDialog.setTitle(getResources().getString(R.string.whyareyoucanceling));
		deleteDialog.setCanceledOnTouchOutside(false);
		deleteDialog.setContentView(R.layout.cancel_trip);

		((Button) (deleteDialog.findViewById(R.id.clientnotshow))).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				reason = "4";
				//sessionManager.setCancelReason(reason);
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
				//sessionManager.setCancelReason(reason);
				getAbortJourney(6);
			}
		});

		((Button) (deleteDialog.findViewById(R.id.donotcharge))).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				reason = "7";
				//sessionManager.setCancelReason(reason);
				getAbortJourney(7);
			}
		});

		((Button) (deleteDialog.findViewById(R.id.other))).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				reason = "8";
				//sessionManager.setCancelReason(reason);
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
			SessionManager sessionManager = new SessionManager(IHaveArrivedActivity.this);
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
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle),cancelResponse.getErrMsg());
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
					Toast.makeText(IHaveArrivedActivity.this, ""+error, Toast.LENGTH_SHORT).show();
				}
			};

			private void ErrorMessageForCancel(String title,String message,final boolean flageforSwithchActivity)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(IHaveArrivedActivity.this);
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
							SessionManager manager = new SessionManager(IHaveArrivedActivity.this);
							Intent intent = new Intent(IHaveArrivedActivity.this,MainActivity.class);
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
				SessionManager sessionManager = new SessionManager(IHaveArrivedActivity.this);
				String subscribChannel=sessionManager.getSubscribeChannel();
				message="{\"a\" :\""+5+"\", \"e_id\" :\""+sessionManager.getUserEmailid()+"\", \"lt\" :"+latitude+", \"lg\" :"+longitude+", \"r\" :\""+reason+"\",\"bid\" :\""+sessionManager.getBOOKING_ID()+"\",\"chn\" :\""+subscribChannel+"\"}";
				Utility.printLog("Publish Location = " + message);

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
			private String getDurationString(int seconds) 
			{
				int hours = seconds / 3600;
				int minutes = (seconds % 3600) / 60;
				seconds = seconds % 60;
				return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
			}
			private String twoDigitString(int number) {

				if (number == 0) {
					return "00";
				}

				if (number / 10 == 0) {
					return "0" + number;
				}
				return String.valueOf(number);
			}

			private void jobTimer()
			{
				final Handler handler = new Handler();
				handler.postDelayed(new Runnable() {

					@Override
					public void run()
					{
						if(runTimer)
						{
							timecunsumedsecond = timecunsumedsecond + 1;
							timer_text.setText(""+getDurationString(timecunsumedsecond));
							handler.postDelayed(this, 1000);
						}
						else
						{
							sessionManager.setTimeWhile_Paused(String.valueOf(System.currentTimeMillis()));
							sessionManager.setElapsedTime(timecunsumedsecond);
						}
					}
				} , 1000);
			}

			@Override
			public void updatedInfo(String info) {
				
			}

			@Override
			public void locationUpdates(Location location) 
			{
				double lat =  (location.getLatitude());
				double lng =  (location.getLongitude());
				sessionManager.setDriverCurrentlat("" + lat);
				sessionManager.setDriverCurrentLongi("" + lng);
				Utility.printLog("onLocationChanged lat=" + lat + "  lng=" + lng);
				LatLng latlng = new LatLng(lat, lng);
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16.0f));
				if(secondmarker!=null)
					secondmarker.setPosition(latlng);
			}

			@Override
			public void locationFailed(String message) {
				
			}
	private void getAlertDropLoc(String title,String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(IHaveArrivedActivity.this);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{

						// only show message
						getAppointmentDetails(appointmentDetailData.getEmail(),appointmentDetailData.getApptDt());
						dialog.dismiss();

					}
				});

		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

	/**
	 * Method for getting appointment detail.
	 * @param email
	 * @param aptDateTime
	 */
	private void getAppointmentDetails(String email,String aptDateTime)
	{
		// animation fo accept or reject with timer
		//animatedUiHomePage(30000);
		Utility.printLog("Animation action email"+email,"Animation action Date Time"+aptDateTime);
		SessionManager sessionManager=new SessionManager(this);
		Utility utility=new Utility();
		String sessionToken=sessionManager.getSessionToken();
		String deviceid=Utility.getDeviceId(this);
		String currentDate=utility.getCurrentGmtTime();

		final String mparams[]={sessionToken,deviceid,email,aptDateTime,currentDate};
		//final ProgressDialog mdialog;
		mdialog=Utility.GetProcessDialog(this);
		mdialog.setMessage(getResources().getString(R.string.Pleasewaitmessage));
		mdialog.show();
		mdialog.setCancelable(false);
		RequestQueue queue = Volley.newRequestQueue(this);
		String url = VariableConstants.getAppointmentDetails_url;
		StringRequest postRequest = new StringRequest(Request.Method.POST, url,responseListenerofAppointment,errorListener1 )
		{
			@Override
			protected Map<String, String> getParams()
			{
				Map<String, String>  params = new HashMap<String, String>();
				params.put("ent_sess_token", mparams[0]);
				params.put("ent_dev_id", mparams[1]);
				params.put("ent_email", mparams[2]);
				params.put("ent_appnt_dt", mparams[3]);
				params.put("ent_date_time", mparams[4]);
				params.put("ent_user_type", "1");
				Utility.printLog("getAppointmentDetails  request "+params);
				return params;
			}
		};
		int socketTimeout = 60000;//60 seconds - change to what you want
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		postRequest.setRetryPolicy(policy);
		queue.add(postRequest);
	}
	Response.Listener<String> responseListenerofAppointment=new Listener<String>()
	{
		@Override
		public void onResponse(String response)
		{
			if (mdialog!=null)
			{
				mdialog.dismiss();
				mdialog.cancel();
			}
			try
			{
				Utility.printLog("getAppointmentDetails  response "+response);
				AppointmentData appointmentData;
				Gson gson = new Gson();
				appointmentData=gson.fromJson(response, AppointmentData.class);

				if (appointmentData.getErrFlag()==0 && appointmentData.getErrNum() == 21)
				{
					Utility.printLog("Animation action Response Came with 0 and 21");
					AppointmentDetailData appointmentDetailData  = appointmentData.getData();
					appointmentDetailList = new AppointmentDetailList();
					appointmentDetailList.setAppointmentDetailData(appointmentDetailData);
					passengerfulladdress.setText(appointmentData.getData().getAddr1()/*+""+"\n"+appointmentDetailData.getAddr2()*/);

				}
				else if (appointmentData.getErrFlag()==1 && appointmentData.getErrNum()==3)
				{
					// 3 -> (1) Error occurred while processing your request.
					ErrorMessage(getResources().getString(R.string.messagetitle),appointmentData.getErrMsg(),false);
				}

				else if (appointmentData.getErrFlag()==1 && appointmentData.getErrNum()== 62 )
				{
					// 3 -> (1) Error occurred while booking not found.
					ErrorMessage(getResources().getString(R.string.messagetitle), appointmentData.getErrMsg(),true);
				}
				else if (appointmentData.getErrFlag() == 1 && appointmentData.getErrNum()==99)
				{
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), appointmentData.getErrMsg());
				}
				else if (appointmentData.getErrFlag() == 1 && appointmentData.getErrNum()==101)
				{
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), appointmentData.getErrMsg());
				}
			}
			catch (Exception e)
			{
				Utility.printLog("getAppointmentDetailException = "+e);
				//ErrorMessage(getResources().getString(R.string.messagetitle),getResources().getString(R.string.servererror),true);
			}
		}
	};
	Response.ErrorListener errorListener1=new ErrorListener()
	{
		@Override
		public void onErrorResponse(VolleyError error)
		{
			if (mdialog!=null)
			{
				mdialog.dismiss();
				mdialog.cancel();
			}
			//ErrorMessage(getResources().getString(R.string.messagetitle), getResources().getString(R.string.servererror), false);
			//Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
			Utility.printLog("Animation action Response Didnt Came Error");
		}
	};
}
