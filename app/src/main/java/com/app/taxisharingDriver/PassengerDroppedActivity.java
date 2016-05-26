package com.app.taxisharingDriver;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.app.taxisharingDriver.pojo.AppointmentDetailList;
import com.app.taxisharingDriver.response.AppointmentData;
import com.app.taxisharingDriver.response.AppointmentDetailData;
import com.app.taxisharingDriver.response.GeocodingResponse;
import com.app.taxisharingDriver.response.UpdateAppointmentDetail;
import com.app.taxisharingDriver.utility.*;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.javapapers.android.maps.path.HttpConnection;
import com.javapapers.android.maps.path.PathJSONParser;


@SuppressLint("DefaultLocale")
public class PassengerDroppedActivity extends FragmentActivity implements OnClickListener, NetworkNotifier
{
	private TextView passenger_name,passengerDropoffAddress,bookingid,timer_text,network_text,actionbar_textview;
	private TextView distance;
	//private Button cancel;
	private ImageView passenger_phone,direction;
	private Button passengerDropped;
	private RelativeLayout network_bar;
	//private ImageView detailimageview;
	private GoogleMap googleMap;
	//private ActionBar actionBar;
	private int selectedindex;
	private int selectedListIndex;
	private AppointmentDetailList appointmentDetailList;
	private AppointmentDetailData appointmentDetailData;
	//private LocationUpdate newLocationFinder;
	double mLatitude ;
	double mLongitude ; 
	//private double previousmLatitude ;
	//private double previousmLongitude ;
	private Marker secondmarker;
	private boolean isFirsttime;
//	private boolean issendnotificationCalled = true;
	private LatLng aptLatLng;
	private LatLng currentLatLng ;
	private ProgressDialog mdialog;
	//private LocationManager locationManager;
	//private String provider;
	SessionManager sessionManager;
	private int timecunsumedsecond = 0;
	private boolean runTimer = true;
	private String timeWhile_Paused="";
	private IntentFilter filter;
	private BroadcastReceiver receiver;
	private LocationUtil locationUtil;
	private TextView payment_type;


	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		setContentView(R.layout.passengerdropped);
		overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
		initLayoutId();
		sessionManager = new SessionManager(this);
		//Utility utility=new Utility();
		sessionManager.setBeginJourney(true);
		locationUtil = new LocationUtil(this,this);
		
		Bundle bundle=getIntent().getExtras();
		appointmentDetailList = (AppointmentDetailList) bundle.getSerializable(VariableConstants.APPOINTMENT);
		appointmentDetailData = appointmentDetailList.getAppointmentDetailData();
		 // Get the location manager
	    //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    // Define the criteria how to select the location provider -> use
	    // default
	   /* Criteria criteria = new Criteria();
	    provider = locationManager.getBestProvider(criteria, false);
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setCostAllowed(false); 
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
	    // Initialize the location fields
	    if(location != null) 
	    {*/
	    	//Utility.printLog("Provider " + provider + " has been selected.");
	    	if (!isFirsttime) 
			{
				setupMap();
			}
	    	double lati =  sessionManager.getDriverCurrentLat();
			double lngi =  sessionManager.getDriverCurrentLongi();
			LatLng currentLatiLngi = new LatLng(lati,lngi);
		secondmarker = googleMap.addMarker(new MarkerOptions().position(currentLatiLngi).title("First Point").icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon)));
		//	secondmarker = googleMap.addMarker(new MarkerOptions().position(currentLatiLngi).title("First Point").icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon)));
			distance.setText(sessionManager.getDistance()+" "+getResources().getString(R.string.km));
			/*onLocationChanged(location);
	    } */
	    sessionManager.setDropAddress(getCompleteAddressString(sessionManager.getDriverCurrentLat(), sessionManager.getDriverCurrentLongi()));
	    initActionBar();


		filter = new IntentFilter();
		filter.addAction("com.app.driverapp.internetStatus");
		filter.addAction("com.embed.anddroidpushntificationdemo11.push");
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
						if (!Utility.isNetworkAvailable(PassengerDroppedActivity.this))
						{
							network_bar.setVisibility(View.VISIBLE);
							return;
						}
						else if (!NetworkConnection.isConnectedFast(PassengerDroppedActivity.this))
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
	    
	    
	    
		/*actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		sessionManager.setIsFlagForOther(true);
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
			actionBar.setTitle(getResources().getString(R.string.journeystarted));
		} 
		catch (NullPointerException e)
		{
			
		}*/
		
		//passengerDropped.setTypeface(robotoBoldCondensedItalic);
		/*double arrayOfscallingfactor[]=Scaler.getScalingFactor(PassengerDroppedActivity.this);
		double width = (160)*arrayOfscallingfactor[0];
		double height = (150)*arrayOfscallingfactor[1];
		String imageUrl =utility.getImageHostUrl(this) + appointmentDetailData.getpPic();
		Picasso.with(this) //
		.load(imageUrl) //
		.placeholder(R.drawable.ic_launcher) //
		.error(R.drawable.ic_launcher).fit()
		.resize((int)Math.round(width),(int)Math.round(height))	 //
		.into(detailimageview); */
		//passenger_name.setText(appointmentDetailList.getfName());
		passenger_name.setText(appointmentDetailData.getfName().toUpperCase());
		//passenger_phone.setText(appointmentDetailData.getMobile());
		passengerDropoffAddress.setText(appointmentDetailData.getDropAddr1()+""+"\n"+appointmentDetailData.getDropAddr2());
		bookingid.setText(appointmentDetailData.getBid());

		if ("2".equals(appointmentDetailData.getPayType())) {
			payment_type.setText(getResources().getString(R.string.cash));
		}
		else if ("1".equals(appointmentDetailData.getPayType())) {
			payment_type.setText(getResources().getString(R.string.card));
		}
		selectedindex = bundle.getInt("selectedindex");
		selectedListIndex = bundle.getInt("horizontapagerIndex");

		//Typeface zurichLightCondensed = Typeface.createFromAsset(getAssets(), "fonts/Zurich Light Condensed.ttf");
		//passenger_name.setTypeface(zurichLightCondensed);
		//passenger_phone.setTypeface(zurichLightCondensed);
		//passengerDropoffAddress.setTypeface(zurichLightCondensed);
		//passenger_name.setTextColor(android.graphics.Color.rgb(51, 51, 51));
		//passengerDropoffAddress.setTextColor(android.graphics.Color.rgb(153, 153, 153));
		//newLocationFinder = new LocationUpdate();
		//newLocationFinder.getLocation(this, mLocationResult);
	}
	
	
	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();
		finish();
	}
	
	@SuppressLint({ "NewApi", "InflateParams" })
	private void initActionBar()
	{
		getActionBar().hide();
		/*actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));
		LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout mActionBarCustom = (RelativeLayout)inflater.inflate(R.layout.custom_actionbar, null);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setIcon(android.R.color.transparent);
		actionbar_textview=(TextView)mActionBarCustom.findViewById(R.id.actionbar_textview);
		cancel = (Button)mActionBarCustom.findViewById(R.id.cancel);
		cancel.setVisibility(View.GONE);
		actionbar_textview.setText(getResources().getString(R.string.passengerdrop));
		actionbar_textview.setGravity(Gravity.CENTER);
		double scaler[]=Scaler.getScalingFactor(this);
		int padding = (int)Math.round(80*scaler[0]);
		actionbar_textview.setPadding(padding, 0,0,0);
		actionBar.setCustomView(mActionBarCustom);*/
	}
	
	
	/*@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) 
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.ihavereachedmenubutton, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case android.R.id.home:
			finish();
			return true;

		case R.id.googlenavigation:
			// NavUtils.navigateUpFromSameTask(this);
			startGoogleMap();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}*/
	private void startGoogleMap()
	{
		double apntLat = appointmentDetailData.getDropLat();
		double apntLong = appointmentDetailData.getDropLong();
		String muri="http://maps.google.com/maps?saddr="+sessionManager.getDriverCurrentLat()+","+sessionManager.getDriverCurrentLongi()+"&daddr="+apntLat+","+apntLong;
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(muri));
		startActivity(intent);
	}

	private void initLayoutId()
	{
		passenger_name = (TextView)findViewById(R.id.passenger_name);
		passenger_phone = (ImageView)findViewById(R.id.passenger_mob);
		bookingid = (TextView)findViewById(R.id.booking_id_text);
		passengerDropoffAddress = (TextView)findViewById(R.id.passengerfulladdress);
		passengerDropped = (Button)findViewById(R.id.passengerdroped);
		payment_type= (TextView) findViewById(R.id.payment_type);
		//detailimageview=(ImageView)findViewById(R.id.detailimageview);
		distance = (TextView)findViewById(R.id.distance_speed);
		direction = (ImageView)findViewById(R.id.direction);
		timer_text = (TextView)findViewById(R.id.timer_text);
		network_text = (TextView)findViewById(R.id.network_text);
		network_bar = (RelativeLayout)findViewById(R.id.network_bar);
		passengerDropped.setOnClickListener(this);
		passenger_phone.setOnClickListener(this);
		direction.setOnClickListener(this);
		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		googleMap = fm.getMap();
		googleMap.setMyLocationEnabled(true);
	}


	/*public  double calculateDistance(double fromLatitude,double fromLongitude,double toLatitude,double toLongitude)
	{

		float results[] = new float[1];

		try 
		{
			Location.distanceBetween(fromLatitude,fromLongitude, toLatitude, toLongitude, results);
		} 
		catch (Exception e)
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
	}*/

	private void setupMap()
	{
		mLatitude =appointmentDetailData.getDropLat(); 
		mLongitude =appointmentDetailData.getDropLong();
		double apntLat = sessionManager.getDriverCurrentLat();
		double apntLong =  sessionManager.getDriverCurrentLongi();
		DrowPath(apntLat,apntLong,mLatitude,mLongitude);
	}

	private void DrowPath(double sourcelatitude,double sourcelongitude,double destinationlatitude,double destinationlogitude)
	{
		double apntLat = sourcelatitude;
		double apntLong = sourcelongitude;
		if (apntLat!=0.0 && apntLong!=0.0) 
		{
			isFirsttime=true;
			//mLatitude = latitude;
			//mLongitude = longitude;  
			mLatitude = destinationlatitude;
			mLongitude = destinationlogitude;
			currentLatLng  = new LatLng(apntLat,apntLong);
			aptLatLng = new LatLng(mLatitude, mLongitude);
			//	MarkerOptions options = new MarkerOptions();
			//options.position(aptLatLng);
			//options.position(currentLatLng);
			//options.position(WALL_STREET);
			//	googleMap.addMarker(options);
			String url = getMapsApiDirectionsUrl(currentLatLng,aptLatLng);
			ReadTask downloadTask = new ReadTask();
		//	downloadTask.execute(url);
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,	14));
			addMarkers(currentLatLng,aptLatLng);
		}
		else
		{
			//mLatitude = latitude;
			//mLongitude = longitude;  
			mLatitude = destinationlatitude;
			mLongitude = destinationlogitude;
			// aptLatLng = new LatLng(apntLat,apntLong);
			currentLatLng = new LatLng(mLatitude, mLongitude);
			MarkerOptions options = new MarkerOptions();
			//	options.position(aptLatLng);
			options.position(currentLatLng);
			//options.position(WALL_STREET);
			googleMap.addMarker(options);
			//String url = getMapsApiDirectionsUrl();
			//ReadTask downloadTask = new ReadTask();
			//downloadTask.execute(url);
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,	14));
			//addMarkers();
			//android.widget.Toast.makeText(PassengerDroppedActivity.this, "Booking location  not found", android.widget.Toast.LENGTH_LONG).show();
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
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + params;
		return url;*/
	}
	private void addMarkers(LatLng currentLatLng,LatLng  aptLatLng) 
	{
		if (googleMap != null) 
		{
			//secondmarker = googleMap.addMarker(new MarkerOptions().position(currentLatLng).title(getResources().getString(R.string.firstpoint)).icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon)));
			googleMap.addMarker(new MarkerOptions().position(aptLatLng).title(getResources().getString(R.string.secondpoint)).icon(BitmapDescriptorFactory.fromResource(R.drawable.home_markers_dropoff)));
		}
	}
	@Override
	protected void onResume() 
	{
		super.onResume();
		if (receiver != null) 
		{
			registerReceiver(receiver, filter);
		}
		//locationManager.requestLocationUpdates(provider, 1000, 1, this);
		if (sessionManager.getPassenger()) 
		{
			sessionManager.setPassenger(false);
			jobTimer();
		}
		else
		{
			if (sessionManager.getElapsedTime3() >= 0) 
			{
				timeWhile_Paused = sessionManager.getTimeWhile_Paused3();
				if(!"-1".equals(timeWhile_Paused))
				{
					long timePaused = Long.parseLong(timeWhile_Paused);
					long currentTime = System.currentTimeMillis() - timePaused;
					Utility.printLog("TIME ELAPSED EQUALS"+currentTime);
					timecunsumedsecond = sessionManager.getElapsedTime3();
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
	private class ReadTask extends AsyncTask<String, Void, String> 
	{
		@Override
		protected String doInBackground(String... url) 
		{
			String data = "";
			try
			{
				HttpConnection http = new HttpConnection();
				data = http.readUrl(url[0]);
			} 
			catch (Exception e) 
			{
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
			}
			catch (Exception e) 
			{
			}
		}
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
	
	@Override
	protected void onPause()
	{
		super.onPause();
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

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.passengerdroped)
		{
			ConnectionDetector connectionDetector=new ConnectionDetector(PassengerDroppedActivity.this);
			if (connectionDetector.isConnectingToInternet()) 
			{
				new BackgroundGeocodingTaskNew().execute();
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
	
	private void getUpdateAppointmentStatus()
	{
		Utility utility=new Utility();
		ConnectionDetector connectionDetector=new ConnectionDetector(PassengerDroppedActivity.this);
		if (connectionDetector.isConnectingToInternet()) 
		{
			String dropAddressLine1;
			String deviceid = Utility.getDeviceId(PassengerDroppedActivity.this);
			String currenttime = utility.getCurrentGmtTime();
			String sessiontoken = sessionManager.getSessionToken();
			String appntId = appointmentDetailData.getBid();
			dropAddressLine1 = appointmentDetailData.getDropAddr1();
			if ("".equals(dropAddressLine1))
			{
				dropAddressLine1 = "-" ;
			}
			double dropLat = sessionManager.getDriverCurrentLat();
			double dropLong = sessionManager.getDriverCurrentLongi();
			String distancestr = distance.getText().toString();
			String[] parts = distancestr.split(" "); 
			if(parts[0].isEmpty())
			{
				parts[0]="0.0";
			}
			double distanceKm = Double.valueOf(parts[0]);
			//double distanceMtr = distanceKm / 0.00062137;
			double distanceMtr = distanceKm / 0.001;
			DecimalFormat df = new DecimalFormat("#.##");
			String strDouble1 = df.format(distanceMtr);
			final String mparams[]={sessiontoken,deviceid,appntId,dropAddressLine1,""+dropLat,""+dropLong,strDouble1,currenttime};
			RequestQueue queue = Volley.newRequestQueue(this);  // this = context
			String  url = VariableConstants.getUpdateAppointmentDetail_url;
			StringRequest postRequest = new StringRequest(Request.Method.POST, url,
					new Response.Listener<String>()
					{
				@Override
				public void onResponse(String response)
				{
					Utility.printLog("PassengerupdateNotificationResponse"+response);
					UpdateAppointmentDetail updateAppointmentDetail;
					Gson gson = new Gson();
					updateAppointmentDetail = gson.fromJson(response, UpdateAppointmentDetail.class);
					Utility.printLog("PassengerDetailupdateNotification"+updateAppointmentDetail);

					try 
					{
						if (mdialog!=null)
						{
							mdialog.dismiss();
							mdialog.cancel();
						}
						// 1 -> (1) Mandatory field missing
						if (updateAppointmentDetail.getErrFlag() ==0 && updateAppointmentDetail.getErrNum() == 88)
						{
							SessionManager sessionManager = new SessionManager(PassengerDroppedActivity.this);
							sessionManager.setDistance_tag(updateAppointmentDetail.getDis());
							sessionManager.setAVG_SPEED(updateAppointmentDetail.getAvgSpeed());
							
							if (updateAppointmentDetail.getApprAmount() != null && !"".equals(updateAppointmentDetail.getApprAmount())) {
								Double value = Double.parseDouble(updateAppointmentDetail.getApprAmount());
								String amount = String.format("%.2f",value) ;
								sessionManager.setAPX_AMOUNT(amount);
							}
							
							sessionManager.setBeginJourney(false);
							sessionManager.setFlagForStatusDropped(true);
							sessionManager.setDistance("0.0");
							Intent intent=new Intent(PassengerDroppedActivity.this, JourneyDetailsActivity.class);
							Bundle bundle=new Bundle();
							sessionManager.setindexofSelectedAppointment(selectedindex);
							sessionManager.setindexofSelectedList(selectedListIndex);
							bundle.putSerializable(VariableConstants.APPOINTMENT, (Serializable) appointmentDetailList);
							intent.putExtras(bundle);
							startActivity(intent);
							finish();
						}
					} 
					catch (Exception e) 
					{
						Utility.printLog("ExceptionException  "+e);
					}
				}
					},
					new Response.ErrorListener()
					{
						@Override
						public void onErrorResponse(VolleyError error) 
						{
							if (mdialog!=null)
							{
								mdialog.dismiss();
								mdialog.cancel();
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
					params.put("ent_appnt_id", mparams[2]); 
					params.put("ent_drop_addr_line1",mparams[3]);
					//params.put("ent_drop_addr_line2", mparams[4]); 
					params.put("ent_drop_lat",mparams[4] );
					params.put("ent_drop_long", mparams[5]);
					params.put("ent_distance",mparams[6]);
					params.put("ent_date_time", mparams[7]);
					
					Utility.printLog("PassengerGetUpdaterequest = "+params);

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
			utility.showDialogConfirm(PassengerDroppedActivity.this,"Alert"," working internet connection required", false).show();
		}
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
					sessionManager.setTimeWhile_Paused3(String.valueOf(System.currentTimeMillis()));
					sessionManager.setElapsedTime3(timecunsumedsecond);
				}
			}
		} , 1000);
	}
	
	/**
	 * Method for getting Drop off address from latitude and longitude
	 * @param LATITUDE
	 * @param LONGITUDE
	 * @return
	 */
	private String getCompleteAddressString(double LATITUDE, double LONGITUDE) 
	{
		String strAdd = "";
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
			if (addresses != null) 
			{
				Address returnedAddress = addresses.get(0);
				StringBuilder strReturnedAddress = new StringBuilder("");

				for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) 
				{
					strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
				}
				strAdd = strReturnedAddress.toString();
			} 
			else 
			{
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return strAdd;
	}
	
	private class BackgroundGeocodingTaskNew extends AsyncTask<Void, Void,Void>
	{
		
		GeocodingResponse response;
		String stringResponse;
		SessionManager sm = new SessionManager(PassengerDroppedActivity.this);

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			mdialog = Utility.GetProcessDialog(PassengerDroppedActivity.this);
			mdialog.setMessage(getResources().getString(R.string.Pleasewaitmessage));
			mdialog.show();
			mdialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(Void...params) 
		{
			Utility.printLog("bbb BackgroundGeocodingTask");
			//String url="https://maps.googleapis.com/maps/api/geocode/json?latlng="+currentLatitude+","+currentLongitude+"&sensor=false&key="+VariableConstants.GOOGLE_API_KEY;

			String url="http://maps.google.com/maps/api/geocode/json?latlng="+sm.getDriverCurrentLat()+","+sm.getDriverCurrentLongi()+"&sensor=false";

			Utility.printLog("Geocoding url: "+url);

			stringResponse = Utility.callhttpRequest(url);
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			
			if(stringResponse!=null)
			{
				Gson gson=new Gson();
				response=gson.fromJson(stringResponse, GeocodingResponse.class);
			}

			if(response!=null)
			{
				if(response.getStatus().equals("OK") && response.getResults()!=null && response.getResults().size()>0)
				{
					Utility.printLog("formatted address size="+response.getResults().size());
					if(response.getResults().size()>1 && !response.getResults().get(1).getFormatted_address().isEmpty())
					{
						/*current_address.setText(response.getResults().get(0).getFormatted_address());
						pickupLocationAddress.setText(response.getResults().get(0).getFormatted_address());*/
						appointmentDetailData.setDropAddr1(response.getResults().get(0).getFormatted_address());
					}
					
					/*else if(response.getResults().size()==1)
					{
						current_address.setText(response.getResults().get(0).getFormatted_address());
						pickupLocationAddress.setText(response.getResults().get(0).getFormatted_address());
						mPICKUP_ADDRESS=response.getResults().get(0).getFormatted_address();

					}*/
				}
				getUpdateAppointmentStatus();
			}
		}
	}
	@Override
	public void updatedInfo(String info) {
		
	}


	@Override
	public void locationUpdates(Location location) 
	{
		double lat =  (location.getLatitude());
	    double lng =  (location.getLongitude());
	    Utility.printLog("onLocationChanged lat="+lat+" lng="+lng);
	    
	    try {
	    	
	    	distance.setText(sessionManager.getDistance()+" "+getResources().getString(R.string.km));
		    sessionManager.setDriverCurrentlat(""+lat);
			sessionManager.setDriverCurrentLongi(""+lng);
		    LatLng latlng = new LatLng(lat, lng);
		    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16.0f));
		    if(secondmarker!=null)
		    	secondmarker.setPosition(latlng);
			
		} catch (Exception e) {
			Utility.printLog("onLocationChanged Exception =" +e.toString());
		}
		
	}


	@Override
	public void locationFailed(String message) {
		
	}

	private void getAlertDropLoc(String title,String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
	Response.Listener<String> responseListenerofAppointment=new Response.Listener<String>()
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
					passengerDropoffAddress.setText(appointmentDetailData.getDropAddr1()+""+"\n"+appointmentDetailData.getDropAddr2());

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
	Response.ErrorListener errorListener1=new Response.ErrorListener()
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

	private void ErrorMessageForInvalidOrExpired(String title,String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);


		builder.setNegativeButton(getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SessionManager sessionManager = new SessionManager(PassengerDroppedActivity.this);
						sessionManager.logoutUser();
						dialog.dismiss();
						Intent intent = new Intent(PassengerDroppedActivity.this, SplahsActivity.class);
						stopService(ApplicationController.getMyServiceInstance());
						startActivity(intent);
						finish();

					}
				});

		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}
	private void ErrorMessage(String title,String message,final boolean flageforSwithchActivity)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(PassengerDroppedActivity.this);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (flageforSwithchActivity) {
							Intent intent = new Intent(PassengerDroppedActivity.this, MainActivity.class);
							startActivity(intent);
							finish();
						} else {
							// only show message
							dialog.dismiss();
						}

					}
				});

		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}
}
