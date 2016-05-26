package com.app.taxisharingDriver;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.taxisharingDriver.log.LogFile;
import com.app.taxisharingDriver.pojo.AppointmentDetailList;
import com.app.taxisharingDriver.pojo.PubnubResponse;
import com.app.taxisharingDriver.response.AppointmentData;
import com.app.taxisharingDriver.response.AppointmentDetailData;
import com.app.taxisharingDriver.response.AppointmentStatusResponse;
import com.app.taxisharingDriver.response.MasterStatusResponse;
import com.app.taxisharingDriver.response.RespondAppointment;
import com.app.taxisharingDriver.response.UpdateAppointMentstatus;
import com.app.taxisharingDriver.utility.*;
import com.app.taxisharingDriver.utility.LocationFinder.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("InflateParams")
public class HomeFragment extends Fragment implements OnMapClickListener,OnClickListener,NetworkNotifier
{
	//private Location gpsLocation;
	private LinearLayout drop_layout,rlAvailable;
	private RelativeLayout zoom_in,zoom_out,normal_map,hybrid_map;
	int availableStatus ;
	private Button tvAvailableText,tvnotAvailableText;
	//private CheckBox cbOn_Off_Available;
	private static View view;
	private GoogleMap googleMap;
	double mLatitude ;
	double mLongitude ;
	private RelativeLayout network_bar;
	public static TextView network_text;
	private LocationFinder newLocationFinder;
	private BroadcastReceiver receiver;
	private IntentFilter filter;
	private LinearLayout locLinear;
	private ProgressBar circularProgressBar;
	private RelativeLayout progressBarLayout;
	private RelativeLayout bottomLayout;
	private TextView pickUpLoacaton,dropOffLocation, dropoffDistanse,pickupDistanse,booking_id,progressText;
	public static ImageView car_icon;
	private Button acceptButton,rejectButton;
	private CountDownTimer countDownTimer;
	private AppointmentDetailList appointmentDetailList=new AppointmentDetailList();
	private ProgressDialog mdialog;
	private String listener_channel;
	private SessionManager session;
	private String driverChannelName;
	private static Pubnub pubnub;
	private MainActivity mainActivity;
	private String pasChannel ;
	private int type;
	private TextView now_later_type;
	private TextView datetimetext;
	//private Marker carMarker;
	//private LocationManager locationManager;
	//private Location location;
	Date date = new Date();


	//private boolean flagForCancel = true; 
	private boolean isOnButtonPressed,flag_for_state;
	private Marker carMarker;
	private ArrayList<AppointmentDetailData> appointmentDetailDatas;
	private Intent serviceIntent;
	private LocationUtil locationUtil;
	//Context context=getActivity();
	AppointmentDetailData appointmentDetailData;
	LocationManager manager;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mainActivity = (MainActivity)getActivity();
		pubnub = mainActivity.getPubnubObject();
		session = new SessionManager(getActivity());
		session.setIsHomeFragmentisOpened(true);
		mdialog = Utility.GetProcessDialog(getActivity());
		driverChannelName = session.getSubscribeChannel();
		locationUtil=new LocationUtil(getActivity(),this);
		manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );

		Utility.printLog("listener_channel = "+listener_channel+"  driverChannelName = "+driverChannelName);

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

					String action = bucket.getString("action");

					Utility.printLog("CCCCCC Animation action"+action);

					if("7".equals(action))
					{
						type = 1;
						Utility.printLog("CCCCCC Booking type1 = "+type);

						Utility.printLog("Animation action found 7");
						String aptDateTime=bucket.getString("dt");
						String email=bucket.getString("e");
						String bid = bucket.getString("bid");

						if (!(bid.equals(session.getBookingIdStatus())))
						{
							session.setBookingIdStatus(bid);
							getAppointmentStatus();
						}
					}
					else if("51".equals(action))
					{
						type = 2;
						Utility.printLog("CCCCCC Booking type2 = "+type);
						Utility.printLog("Animation action found 7");
						String aptDateTime=bucket.getString("dt");
						String email=bucket.getString("e");
						Utility.printLog("Driver Inside on recieve ");
						Utility.printLog("CCCCCC BroadcastReceiver bucket " + bucket);
						String bid = bucket.getString("bid");
						if (!(bid.equals(session.getBookingIdStatus())))
						{
							session.setBookingIdStatus(bid);
							getAppointmentStatus();
						}
					}
					else if ("11".equals(action))
					{
						String payload = bucket.getString("payload");
						ErrorMessage(getResources().getString(R.string.messagetitle),payload,false);
					}
					else if ("12".equals(action))
					{
						Utility.printLog("out side 12"+session.getIsUserRejectedFromAdmin());
						String payload = bucket.getString("payload");
						//ErrorMessage(getResources().getString(R.string.messagetitle),payload,false);
						ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), payload);



					}

					if("1".equals(status))
					{
						network_bar.setVisibility(View.GONE);
					}
					else
					{
						if (!Utility.isNetworkAvailable(getActivity()))
						{
							network_bar.setVisibility(View.VISIBLE);
							return;
						}
						else if (!NetworkConnection.isConnectedFast(getActivity()))
						{
							network_bar.setVisibility(View.VISIBLE);
							network_text.setText(getResources().getString(R.string.lownetwork));
							return;
						}
					}
				}
				catch (Exception e)
				{
					Utility.printLog("AAA BroadcastReceiver Exception "+e);
					e.printStackTrace();
				}
			}
		};

	}

	/**
	 * Method for subscribing the channel
	 * @param message
	 */
	private void subscribe(final String message)
	{
		try
		{
			Hashtable<String, String> args = new Hashtable<String, String>();
			Utility.printLog("AAA inside subscribe message channel="+message);
			LogFile.log(" subscribe message channel: "+message);
			args.put("channel",message);
			Utility.printLog("AAA inside subscribe args size="+args.size());

			Utility.printLog("AAA pubnub: "+pubnub);
			//Pubnub Change 17/5/2016
			//ErrorMessage("Subscribe",message,false);
			LogFile.log(" pubnub: "+pubnub);
			pubnub.subscribe(args, new Callback()
			{
				@Override
				public void connectCallback(String channel,Object message)
				{
					Utility.printLog("AAA SUBSCRIBE : CONNECT on channel:");
					//Pubnub Change 17/5/2016
					LogFile.log(" Subscribe: "+"CONNECT on channel");
					//ErrorMessage("Subscribe ","CONNECT on channel",false);
				}
				@Override
				public void disconnectCallback(String channel,Object message)
				{//Pubnub Change 17/5/2016
					LogFile.log(" Subscribe: "+"DiscONNECT on channel");
				//	ErrorMessage("Subscribe ","DisCONNECT on channel",false);
				}

				@Override
				public void reconnectCallback(String channel,Object message)
				{
					//Pubnub Change 17/5/2016
					LogFile.log(" Subscribe: "+"RECONNECT on channel");
					//ErrorMessage("Subscribe ","Reconnect on channel",false);
					Utility.printLog("AAA SUBSCRIBE : RECONNECT on channel:");
				}
				@Override
				public void successCallback(String channel, final Object message)
				{
					mainActivity.runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							Utility.printLog("AAA successCallback message="+message);
							//Pubnub Change 17/5/2016
							LogFile.log(" successCallback: "+message);
							PubnubResponse pubnubResponse;
							Gson gson = new Gson();
							pubnubResponse = gson.fromJson(String.valueOf(message), PubnubResponse.class);
							Utility.printLog("AAA inside successCallback bool = "+MainActivity.isResponse);
							Utility.printLog("AAA successCallback  if ");
								try {
									if ("11".equals(pubnubResponse.getA()) || "51".equals(pubnubResponse.getNt())) {
										//String aptDate = pubnubResponse.getDt();
										//String email = pubnubResponse.getE();
										String bid = pubnubResponse.getBid();

										if (!(bid.equals(session.getBookingIdStatus())))
										{
											session.setBookingIdStatus(bid);

											int icon = R.drawable.ic_launcher;
											long when = System.currentTimeMillis();
											NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
											Notification notification = new Notification(icon, "Congratulations you got a new booking request", when);
											String title = getActivity().getString(R.string.app_name);
											SessionManager sessionManager = new SessionManager(getActivity());
											Intent notificationIntent;
											notificationIntent = new Intent(getActivity(), MainActivity.class);
											notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

											PendingIntent intent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

											notification.setLatestEventInfo(getActivity(), title, "Congratulations you got a new booking request", intent);
											notification.flags |= Notification.FLAG_AUTO_CANCEL;

											notification.sound = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.taxina);
											sessionManager.setFlagNewBooking(false);

											// Vibrate if vibrate is enabled
											notification.defaults |= Notification.DEFAULT_VIBRATE;
											notificationManager.notify(0, notification);


											Utility.printLog("Animation Action successCallback  try if");
											MainActivity.isResponse = false;
											Utility.printLog("sendResponsePubnub :" + pubnubResponse);
											session.setAPT_DATE("");
											session.setPASSENGER_EMAIL("");

											if ("51".equals(pubnubResponse.getNt())){
												type = 2;
											}
											else
											{
												type = 1;
											}
											getAppointmentStatus();
										} else if ("4".equals(pubnubResponse.getA())) {
											Utility.printLog("successCallback  try if");
											session.setPasChannel("");
										}
									}
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							}
					});
				}

			});
		}
		catch(Exception e)
		{
			//Pubnub Change 17/5/2016

			Utility.printLog("AAA Exception in subscribe 1 "+e);
		}
	}

	@Override
	public View onCreateView(android.view.LayoutInflater inflater,	ViewGroup container,Bundle savedInstanceState)
	{
		if (view != null)
		{
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}
		try
		{
			view = inflater.inflate(R.layout.homefragment, null);
			//	view = inflater.inflate(R.layout.ihavereachedscreen, null);

		} catch (InflateException e)
		{
			/* map is already there, just return view as it is */
			Utility.printLog("onCreateView  InflateException "+e);
		}

		initLayoutid(view);


		return view;
	}

	@SuppressLint("NewApi")
	private void initLayoutid(View view)
	{
		locLinear = (LinearLayout)view.findViewById(R.id.loc_linear);
		bottomLayout = (RelativeLayout)view.findViewById(R.id.button_relative);
		circularProgressBar = (ProgressBar)view.findViewById(R.id.myProgress);
		progressBarLayout = (RelativeLayout)view.findViewById(R.id.progress_rel);
		tvnotAvailableText = (Button)view.findViewById(R.id.tvnotAvailableText);
		tvAvailableText = (Button)view.findViewById(R.id.tvAvailableText);
		//cbOn_Off_Available = (CheckBox)view.findViewById(R.id.cbOn_Off_Available);
		progressText = (TextView)view.findViewById(R.id.text_onbar);
		network_text = (TextView)view.findViewById(R.id.network_text);
		network_bar = (RelativeLayout)view.findViewById(R.id.network_bar);
		rlAvailable = (LinearLayout)view.findViewById(R.id.rlAvailable);
		drop_layout = (LinearLayout)view.findViewById(R.id.drop_layout);

		zoom_in = (RelativeLayout)view.findViewById(R.id.Plus);
		zoom_out = (RelativeLayout)view.findViewById(R.id.minus);
		normal_map = (RelativeLayout)view.findViewById(R.id.normal_map);
		hybrid_map = (RelativeLayout)view.findViewById(R.id.hybrid_map);

		//creentlocation = (RelativeLayout)view.findViewById(R.id.creentlocation);

		zoom_in.setOnClickListener(this);
		zoom_out.setOnClickListener(this);

		normal_map.setOnClickListener(this);
		hybrid_map.setOnClickListener(this);

		//creentlocation.setOnClickListener(this);

		double arrayOfscallingfactor[]=Scaler.getScalingFactor(getActivity());
		int topstriplayoutmarigin = 200; // margin in dips
		int dpValueleft = 800; // margin in dips
		int maptopMargin = (int)Math.round(dpValueleft*arrayOfscallingfactor[1]); // margin in pixels
		int topstripMargin = (int)Math.round(topstriplayoutmarigin*arrayOfscallingfactor[1]);//(int)(topstriplayoutmarigin * d); // margin in pixels
		ObjectAnimator  animation2 = ObjectAnimator.ofFloat(locLinear, "y", -topstripMargin);
		animation2.setDuration(1);
		ObjectAnimator  animation1 = ObjectAnimator.ofFloat(bottomLayout, "y",maptopMargin);
		animation1.setDuration(1);
		AnimatorSet set = new AnimatorSet();
		set.playTogether(animation1, animation2);
		set.start();
		acceptButton = (Button)view.findViewById(R.id.accept_button);
		rejectButton = (Button)view.findViewById(R.id.reject_button);
		car_icon = (ImageView)view.findViewById(R.id.car_icon);
		acceptButton.setOnClickListener(this);
		rejectButton.setOnClickListener(this);

		pickUpLoacaton = (TextView)view.findViewById(R.id.pic_up_loc);
		dropOffLocation = (TextView)view.findViewById(R.id.drop_off_up);
		pickupDistanse = (TextView)view.findViewById(R.id.pic_up_dis);
		dropoffDistanse = (TextView)view.findViewById(R.id.drop_off_dis);
		now_later_type = (TextView)view.findViewById(R.id.now_leter_type);
		datetimetext = (TextView)view.findViewById(R.id.date_time);
		booking_id = (TextView)view.findViewById(R.id.home_booking_id_text);




		tvAvailableText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utility.printLog("HomeFrag isOnButtonPressed: " + isOnButtonPressed);
				Utility.printLog("HomeFrag flag_for_state: " + flag_for_state);
				if (flag_for_state) {

				} else {
					getMasterUpdateStatus(3);
				}
			}
		});

		tvnotAvailableText.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Utility.printLog("HomeFrag flag_for_state: "+flag_for_state);
				if (flag_for_state)
				{
					getMasterUpdateStatus(4);
				}
			}
		});

	}

	private void getMasterUpdateStatus(int masterStatus)
	{
		SessionManager sessionManager=new SessionManager(getActivity());
		Utility utility=new Utility();
		String sessionToken=sessionManager.getSessionToken();
		String deviceid=Utility.getDeviceId(getActivity());
		String currentDate=utility.getCurrentGmtTime();

		availableStatus = masterStatus;

		final String mparams[]={sessionToken,deviceid,""+masterStatus,currentDate};

		mdialog.setMessage(getResources().getString(R.string.Pleasewaitmessage));
		mdialog.show();
		mdialog.setCancelable(false);
		RequestQueue queue = Volley.newRequestQueue(getActivity());
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
			try
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

				if (masterStatusResponse.getErrFlag() == 0 && masterStatusResponse.getErrNum() == 69)
				{
					Toast.makeText(getActivity(), masterStatusResponse.getErrMsg(), Toast.LENGTH_SHORT).show();

					if(flag_for_state)
					{
						flag_for_state = false;
						session.setIsOnButtonClicked(false);
						isOnButtonPressed = false;
						tvAvailableText.setSelected(false);
						tvnotAvailableText.setSelected(true);
					}
					else
					{
						flag_for_state = true;
						session.setIsOnButtonClicked(true);
						isOnButtonPressed = true;
						tvAvailableText.setSelected(true);
						tvnotAvailableText.setSelected(false);
					}

				}
				if (masterStatusResponse.getErrFlag() == 1 && masterStatusResponse.getErrNum() == 99)
				{
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), masterStatusResponse.getErrMsg());
				}
				if (masterStatusResponse.getErrFlag() == 1 && masterStatusResponse.getErrNum() == 101)
				{
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), masterStatusResponse.getErrMsg());
				}
				if (masterStatusResponse.getErrFlag() == 1 && masterStatusResponse.getErrNum() == 125)
				{
					Toast.makeText(getActivity(), masterStatusResponse.getErrMsg(), Toast.LENGTH_SHORT).show();
					//getAppointmentStatus();
				}
				if(masterStatusResponse.getErrFlag()==1 && masterStatusResponse.getErrNum()==7)
				{
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle),masterStatusResponse.getErrMsg());
				}

			}
			catch (Exception e)
			{
				e.printStackTrace();
				if (mdialog!=null)
				{
					mdialog.dismiss();
					mdialog.cancel();
				}
				//ErrorMessage(getResources().getString(R.string.messagetitle),getResources().getString(R.string.servererror),true);
			}

		}
			};

			ErrorListener errorListenerofMasreStatus=new ErrorListener()
			{
				@Override
				public void onErrorResponse(VolleyError error)
				{
					if (mdialog!=null)
					{
						mdialog.dismiss();
						mdialog.cancel();
					}

				}
			};


			String emailpassenger,currentDateTime;

			/**
			 * Method for getting Passenger email id and booking time.
			 * @param email
			 * @param dateTime
			 */
			private void getEmailCurrentDtPush(String email,String dateTime)
			{
				emailpassenger = email;
				currentDateTime = dateTime;
			}

			LocationResult mLocationResult = new LocationResult()
			{
				public void gotLocation(final double latitude, final double longitude)
				{
					if (latitude == 0.0 || longitude == 0.0)
					{
						getActivity().runOnUiThread(new Runnable()
						{
							public void run()
							{
								showSettingsAlert();
							}
						});
						return;
					}
					else
					{
						getActivity().runOnUiThread(new Runnable()
						{
							public void run()
							{
								try
								{
									mLatitude = latitude;
									mLongitude = longitude;
									googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude,mLongitude), 16.0f));
								}
								catch (Exception e)
								{
								   Utility.printLog("Exception = "+e);
								}
							}
						});
					}
				}
			};
			@Override
			public void onMapClick(LatLng arg0)
			{

			}

			private void showSettingsAlert()
			{
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
				alertDialog.setTitle(getResources().getString(R.string.gps));
				alertDialog.setMessage(getResources().getString(R.string.gpsdisable));
				alertDialog.setPositiveButton(getResources().getString(R.string.settings), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {
						Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);
					}
				});

				alertDialog.setNegativeButton(getResources().getString(R.string.cancelbutton), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						getActivity().finish();
					}
				});
				alertDialog.show();
			}

			@Override
			public void onPause()
			{
				super.onPause();
				SessionManager sessionManager=new SessionManager(getActivity());
				sessionManager.setIsHomeFragmentisOpened(false);
				getActivity().unregisterReceiver(receiver);
				try{
					locationUtil.disconnectGoogleApiClient();
					if(pubnub!=null)
						//Pubnub Change 17/5/2016
						LogFile.log("Unsubscribe "+""+listener_channel);

						pubnub.unsubscribe(listener_channel);

					if(mdialog != null){
						mdialog.cancel();
						mdialog.dismiss();
					}
					//getActivity().moveTaskToBack(true);
					if (carMarker != null)
					{
						carMarker.remove();
					}
					if(googleMap!=null){
						googleMap.clear();
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}

			@Override
			public void onResume()
			{
				super.onResume();
				listener_channel = session.getListerChannel();
				/*serviceIntent = new Intent(getActivity(), PubnubService.class);
				getActivity().stopService(serviceIntent);*/
				locationUtil.connectGoogleApiClient();
				if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
					buildAlertMessageNoGps();
				}



				subscribe(listener_channel);
				SessionManager sessionManager = new SessionManager(getActivity());
				sessionManager.setIsHomeFragmentisOpened(true);
				SupportMapFragment fm = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
				// Getting GoogleMap object from the fragment
				googleMap = fm.getMap();
				googleMap.setMyLocationEnabled(true);

				newLocationFinder = new LocationFinder();
				newLocationFinder.getLocation(getActivity(), mLocationResult);
				googleMap.setOnMapClickListener(this);
				LatLng latLng = new LatLng(sessionManager.getDriverCurrentLat(), sessionManager.getDriverCurrentLongi());
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
				googleMap.getUiSettings().setZoomControlsEnabled(false);

				try {
					carMarker = googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon_black)));

				}catch (Exception e){e.printStackTrace();}

				/*isOnButtonPressed = session.getIsOnButtonClicked();
				if (isOnButtonPressed)
				{
					Utility.printLog("HomeFrag isOnButtonPressed: "+isOnButtonPressed);
					//cbOn_Off_Available.setChecked(true);
					tvAvailableText.setSelected(true);
					tvnotAvailableText.setSelected(false);
					flag_for_state = true;

				}
				else
				{
					Utility.printLog("HomeFrag isOnButtonPressed: "+isOnButtonPressed);
					//cbOn_Off_Available.setChecked(false);
					tvAvailableText.setSelected(false);
					tvnotAvailableText.setSelected(true);
					flag_for_state = false;
				}*/
				//getLocation(getActivity());
				//checkingNetworkState();
				Utility.printLog("Animation action oNResume Called");
				sessionManager.setIsHomeFragmentisOpened(true);
				sessionManager.setIsFlagForOther(false);


				if (receiver!=null)
				{
					getActivity().registerReceiver(receiver, filter);
				}
				else
				{
					Utility.printLog("Animation actio receiver Null");
				}


				getAppointmentStatus();


				/*if (sessionManager.isRequested())
				{

					if(!("".equals(session.getPASSENGER_EMAIL())) && !("".equals(session.getAPT_DATE())))
					{
						if("51".equals(session.getACTION()))
						{
							type = 2;
						}
						else
						{
							type = 1;
						}
						Utility.printLog("Email = " + session.getPASSENGER_EMAIL(), "ApptDate = " + session.getAPT_DATE());
						sessionManager.setIsRequested(false);
						getEmailCurrentDtPush(session.getPASSENGER_EMAIL(), session.getAPT_DATE());
						//getAppointmentDetails(session.getPASSENGER_EMAIL(), session.getAPT_DATE());

					}
				}*/

				if (sessionManager.isCancelPushFlag())
				{
					sessionManager.setCancelPushFlag(false);
					sessionManager.setBookingid("");
					ErrorMessage(getResources().getString(R.string.messagetitle),getResources().getString(R.string.cancelbooking),true);
				}

				if (sessionManager.isFlagForPayment())
				{
					sessionManager.setFlagForPayment(false);
					ErrorMessage(getResources().getString(R.string.messagetitle), sessionManager.getPayload(), false);
				}
			}



	private void getAppointmentStatus()
	{
		Utility utility=new Utility();
		ConnectionDetector connectionDetector=new ConnectionDetector(getActivity());
		if (connectionDetector.isConnectingToInternet())
		{
			String deviceid=Utility.getDeviceId(getActivity());
			String curenttime=utility.getCurrentGmtTime();
			SessionManager sessionManager=new SessionManager(getActivity());
			String sessiontoken = sessionManager.getSessionToken();
			String apttime = sessionManager.getAPPT_DATE();
			final String mparams[]={sessiontoken,deviceid ,curenttime};

			mdialog.setMessage(getResources().getString(R.string.Pleasewaitmessage));
			mdialog.show();
			mdialog.setCancelable(false);
			RequestQueue queue = Volley.newRequestQueue(getActivity());
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
				Log.i("appointmentStatus ",response);
				Gson gson = new Gson();
				appointmentStatusResponse = gson.fromJson(response, AppointmentStatusResponse.class);
				if (appointmentStatusResponse.getErrFlag() == 0 && appointmentStatusResponse.getErrNum() == 21)
				{

					appointmentDetailDatas = appointmentStatusResponse.getData();
					appointmentDetailList.setAppointmentDetailData(appointmentDetailDatas.get(0));
					Utility.printLog("AAAA Deveendra" + appointmentDetailDatas.get(0).getStatus());


					if ("1".equals(appointmentDetailDatas.get(0).getStatus()))
					{
						session.setIsOnOff(true);
						tvAvailableText.setSelected(true);
						getAppointmentDetails(appointmentDetailDatas.get(0).getEmail(), appointmentDetailDatas.get(0).getApptDt());
					}
					else if ("6".equals(appointmentDetailDatas.get(0).getStatus()))
					{
						if (mdialog!=null)
						{
							mdialog.dismiss();
							mdialog.cancel();
						}
						Utility.printLog("Inside appointmentDetailDatas");
						Intent intent=new Intent(getActivity(), IHaveArrivedActivity.class);
						Bundle bundle=new Bundle();
						bundle.putSerializable(VariableConstants.APPOINTMENT, appointmentDetailList);
						Utility.printLog("III Main appointmentDetailList " + appointmentDetailList);
						intent.putExtras(bundle);
						startActivity(intent);
						getActivity().finish();
					}

					else if ("7".equals(appointmentDetailDatas.get(0).getStatus()))
					{
						if (mdialog!=null)
						{
							mdialog.dismiss();
							mdialog.cancel();
						}
						Intent intent=new Intent(getActivity(), BeginJourneyActivity.class);
						Bundle bundle=new Bundle();
						bundle.putSerializable(VariableConstants.APPOINTMENT,appointmentDetailList);
						intent.putExtras(bundle);
						startActivity(intent);
						getActivity().finish();
					}
					else if ("8".equals(appointmentDetailDatas.get(0).getStatus()))
					{
						if (mdialog!=null)
						{
							mdialog.dismiss();
							mdialog.cancel();
						}
						if (session.getFlagForStatusDropped())
						{
							Intent intent=new Intent(getActivity(), JourneyDetailsActivity.class);
							Bundle bundle=new Bundle();
							bundle.putSerializable(VariableConstants.APPOINTMENT,appointmentDetailList);
							intent.putExtras(bundle);
							startActivity(intent);
							getActivity().finish();
						}
						else
						{
							Intent intent=new Intent(getActivity(), PassengerDroppedActivity.class);
							Bundle bundle=new Bundle();
							bundle.putSerializable(VariableConstants.APPOINTMENT,appointmentDetailList);
							intent.putExtras(bundle);
							startActivity(intent);
							getActivity().finish();
						}

					}
				}
				else if (appointmentStatusResponse.getErrFlag() == 1 && appointmentStatusResponse.getErrNum() == 96)
				{
					if (mdialog!=null)
					{
						mdialog.dismiss();
						mdialog.cancel();
					}
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), appointmentStatusResponse.getErrMsg());
				}

				else if (appointmentStatusResponse.getErrFlag() == 1 && appointmentStatusResponse.getErrNum() == 79)
				{
					if (mdialog!=null)
					{
						mdialog.dismiss();
						mdialog.cancel();
					}
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), appointmentStatusResponse.getErrMsg());
				}
				else if (appointmentStatusResponse.getErrFlag() == 1 && appointmentStatusResponse.getErrNum() == 6)
				{
					if (mdialog!=null)
					{
						mdialog.dismiss();
						mdialog.cancel();
					}
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), appointmentStatusResponse.getErrMsg());
				}
				else if (appointmentStatusResponse.getErrFlag() == 1 && appointmentStatusResponse.getErrNum() == 7)
				{
					if (mdialog!=null)
					{
						mdialog.dismiss();
						mdialog.cancel();
					}
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), appointmentStatusResponse.getErrMsg());
				}
				else if (appointmentStatusResponse.getErrFlag() == 1 && appointmentStatusResponse.getErrNum() == 101)
				{
					if (mdialog!=null)
					{
						mdialog.dismiss();
						mdialog.cancel();
					}
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), appointmentStatusResponse.getErrMsg());
				}
				else if (appointmentStatusResponse.getErrFlag() == 1 && appointmentStatusResponse.getErrNum() == 99)
				{
					if (mdialog!=null)
					{
						mdialog.dismiss();
						mdialog.cancel();
					}
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), appointmentStatusResponse.getErrMsg());
				}
				else if(appointmentStatusResponse.getErrNum()==6 || appointmentStatusResponse.getErrNum()==7 ||
						appointmentStatusResponse.getErrNum()==94 || appointmentStatusResponse.getErrNum()==96)
				{
					if (mdialog!=null)
					{
						mdialog.dismiss();
						mdialog.cancel();
					}
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), appointmentStatusResponse.getErrMsg());

				}
				else if(appointmentStatusResponse.getErrFlag() == 1 && appointmentStatusResponse.getErrNum() == 49 && "3".equals(appointmentStatusResponse.getMasStatus()) )
				{
					session.setIsInBooking(false);
					if (mdialog!=null)
					{
						mdialog.dismiss();
						mdialog.cancel();
					}
					session.setIsOnOff(true);
					Utility.printLog("Status1 main= " + session.IsOnOff());
					getActivity().startService(ApplicationController.getMyServiceInstance());
				}
				else {
					session.setIsInBooking(false);
					if (mdialog!=null)
					{
						mdialog.dismiss();
						mdialog.cancel();
					}
					session.setIsOnOff(false);
					Utility.printLog("Status2 main= " + session.IsOnOff());
				}

				isOnButtonPressed = session.getIsOnButtonClicked();
				if (isOnButtonPressed)
				{
					if (!session.IsOnOff())
					{
						Utility.printLog("Status1 home= " + session.IsOnOff());
						tvAvailableText.setSelected(false);
						tvnotAvailableText.setSelected(true);
						flag_for_state = false;
					}
					else {
						Utility.printLog("HomeFrag isOnButtonPressed: ="+isOnButtonPressed);
						Utility.printLog("Status2 home = " + session.IsOnOff());
						tvAvailableText.setSelected(true);
						tvnotAvailableText.setSelected(false);
						flag_for_state = true;
					}
				}
				else
				{
					Utility.printLog("HomeFrag isOnButtonPressed: ="+isOnButtonPressed);
					Utility.printLog("Status3 home = " + session.IsOnOff());
					tvAvailableText.setSelected(false);
					tvnotAvailableText.setSelected(true);
					flag_for_state = false;
				}

				if (appointmentStatusResponse.getMasStatus().equals("4"))
				{
					tvAvailableText.setSelected(false);
					tvnotAvailableText.setSelected(true);
					flag_for_state = false;
					Toast.makeText(getActivity(),"You are Off the job.",Toast.LENGTH_SHORT).show();
				}
				else
				{
					tvAvailableText.setSelected(true);
					tvnotAvailableText.setSelected(false);
					flag_for_state = true;
				}

			}
			catch (Exception e)
			{
				Utility.printLog("Exception = "+e);
				e.printStackTrace();
				//ErrorMessage(getResources().getString(R.string.messagetitle),getResources().getString(R.string.servererror),false);
			}
		}
	};
	ErrorListener errorListenerApptStatus=new ErrorListener()
	{
		@Override
		public void onErrorResponse(VolleyError error)
		{
			try {
				//Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
				Utility.printLog("AAAAAAAAAVolleyError" + error);
				if (mdialog != null) {
					mdialog.dismiss();
					mdialog.cancel();
				}
				ErrorMessage(getResources().getString(R.string.messagetitle), getResources().getString(R.string.nonetwork), false);
			}catch (Exception e){
				e.printStackTrace();
			}
			//Utility.ShowAlert(getResources().getString(R.string.network), MainActivity.this);
		}
	};


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
				SessionManager sessionManager=new SessionManager(getActivity());
				Utility utility=new Utility();
				String sessionToken=sessionManager.getSessionToken();
				String deviceid=Utility.getDeviceId(getActivity());
				String currentDate=utility.getCurrentGmtTime();

				final String mparams[]={sessionToken,deviceid,email,aptDateTime,currentDate};
				//final ProgressDialog mdialog;
				RequestQueue queue = Volley.newRequestQueue(getActivity());
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
			Listener<String> responseListenerofAppointment=new Listener<String>()
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
						Log.i("getAppointmentDetails","resp "+response);
						AppointmentData  appointmentData;
						Gson gson = new Gson();
						appointmentData=gson.fromJson(response, AppointmentData.class);

						if (appointmentData.getErrFlag()==0 && appointmentData.getErrNum() == 21)
						{
							Utility.printLog("Animation action Response Came with 0 and 21");
							session.setAPT_DATE("");
							session.setPASSENGER_EMAIL("");
							 appointmentDetailData  = appointmentData.getData();
							appointmentDetailList = new AppointmentDetailList();
							appointmentDetailList.setAppointmentDetailData(appointmentDetailData);
							// 21 -> (0) Got the details!
							String pickuplocstr = appointmentDetailData.getAddr1();
							//String dropofflocstr = appointmentDetailData.getDropAddr2();
							String pickupdisstr = appointmentDetailData.getApptDis();
							//String dropoffstr = appointmentDetailData.getDis();
							String bookingIdstr = appointmentDetailData.getBid();
							pasChannel = appointmentDetailData.getPasChn();
							session.setPasChannel(pasChannel);
							session.setBOOKING_ID(appointmentDetailData.getBid());
							session.setBookingid(appointmentDetailData.getBid());
							session.setAPPT_DATE(appointmentDetailData.getApptDt());
							session.setMobile(appointmentDetailData.getMobile());
							session.setDate(appointmentDetailData.getApptDt());
							pickUpLoacaton.setText(""+pickuplocstr);
        //=================================My Change 19/5/2016==========================
							LatLng latLng = new LatLng(appointmentDetailData.getPickLat(),appointmentDetailData.getPickLong());
                         googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.home_markers_pickup)).title("Pick Up Here"));
			//=================================My Change 19/5/2016==========================
							pickupDistanse.setText(""+pickupdisstr);
							if (!"".equals(appointmentDetailData.getDropAddr1()))
							{
								drop_layout.setVisibility(View.VISIBLE);
								String dropofflocstr = appointmentDetailData.getDropAddr1();
								String dropoffstr = appointmentDetailData.getDis();
								dropOffLocation.setText(""+dropofflocstr);
								dropoffDistanse.setText(""+dropoffstr);
							}
							else
							{
								drop_layout.setVisibility(View.GONE);
							}


							booking_id.setText(""+bookingIdstr);
							/*if (type == 1) 
							{
								now_later_type.setText(getResources().getString(R.string.now));
								Utility.printLog("CCCCCC Booking type1 = "+type);
							}
							else 
							{
								now_later_type.setText(getResources().getString(R.string.later));
								Utility.printLog("CCCCCC Booking type2 = "+type);
							}*/

							datetimetext.setText(appointmentDetailData.getApptDt());
							int remainingSeconds=Integer.parseInt(appointmentDetailData.getExpireSec().toString());
							remainingSeconds=remainingSeconds*1000;
							animatedUiHomePage(remainingSeconds);

						}
						else if (appointmentData.getErrFlag()==1 && appointmentData.getErrNum()==3)
						{
							// 3 -> (1) Error occurred while processing your request.
							ErrorMessage(getResources().getString(R.string.messagetitle),appointmentData.getErrMsg(),false);
						}
						else if (appointmentData.getErrFlag()==1 && appointmentData.getErrNum()== 72 )
						{
							ErrorMessageForExpired(getResources().getString(R.string.messagetitle),appointmentData.getErrMsg());
						}
						else if (appointmentData.getErrFlag()==1 && appointmentData.getErrNum()== 62 )
						{
							// 3 -> (1) Error occurred while booking not found.
							ErrorMessage(getResources().getString(R.string.messagetitle),appointmentData.getErrMsg(),true);
						}
						else if (appointmentData.getErrFlag() == 1 && appointmentData.getErrNum() == 4)
						{
							ErrorMessageForExpired(getResources().getString(R.string.messagetitle),appointmentData.getErrMsg());
						}
						else if (appointmentData.getErrFlag() == 1 && appointmentData.getErrNum()==9)
						{
							ErrorMessageForExpired(getResources().getString(R.string.messagetitle),appointmentData.getErrMsg());
						}
						else if (appointmentData.getErrFlag() == 1 && appointmentData.getErrNum()==99)
						{
							ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle),appointmentData.getErrMsg());
						}
						else if (appointmentData.getErrFlag() == 1 && appointmentData.getErrNum()==101)
						{
							ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle),appointmentData.getErrMsg());
						}
						else if(appointmentData.getErrNum()==6|| appointmentData.getErrNum()==7 ||
								appointmentData.getErrNum()==94 || appointmentData.getErrNum()==96)
						{
							ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle),appointmentData.getErrMsg());
						}

					}
					catch (Exception e)
					{
						Utility.printLog("getAppointmentDetailException = "+e);
						e.printStackTrace();
						//ErrorMessage(getResources().getString(R.string.messagetitle),getResources().getString(R.string.servererror),true);
					}
				}
					};
					/**
					 * Method for showing error message
					 * @param title
					 * @param message
					 * @param flageforSwithchActivity
					 */
					private void ErrorMessage(String title,String message,final boolean flageforSwithchActivity)
					{
						session.setCancelPushFlag(false);
						session.setFlagForPayment(false);
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
									Intent intent = new Intent(getActivity(),MainActivity.class);
									startActivity(intent);
									getActivity().finish();
									dialog.dismiss();
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
					/**
					 * Method for updating appointment status
					 * @param responsecode
					 */
	private void sendNotificationTOPassenger(final int responsecode)
	{
		Utility utility=new Utility();
		ConnectionDetector connectionDetector=new ConnectionDetector(getActivity());
		if (connectionDetector.isConnectingToInternet())
		{
			String deviceid=Utility.getDeviceId(getActivity());
			String curenttime=utility.getCurrentGmtTime();
			SessionManager sessionManager=new SessionManager(getActivity());
			/*String passengerEmailid = emailpassenger;
			String appointdatetime = currentDateTime;*/
			String passengerEmailid = appointmentDetailDatas.get(0).getEmail();
			String appointdatetime = appointmentDetailDatas.get(0).getApptDt();
			String sessiontoken = sessionManager.getSessionToken();
			String notes="testing";
			final String mparams[]={sessiontoken,deviceid,passengerEmailid,appointdatetime,""+responsecode ,notes,curenttime/*currentdata[0]*/};

			mdialog.setMessage(getResources().getString(R.string.Pleasewaitmessage));
			mdialog.show();
			mdialog.setCancelable(false);
			RequestQueue queue = Volley.newRequestQueue(getActivity());  // this = context
			String  url = VariableConstants.getAppointmentstatusUpdate_url;
			StringRequest postRequest = new StringRequest(Request.Method.POST, url,responseListenerofSendNotification,errorListener1 )
			{
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
	Listener<String> responseListenerofSendNotification=new Listener<String>()
			{
		@Override
		public void onResponse(String response)
		{
			rlAvailable.setVisibility(View.VISIBLE);
			try
			{
				Utility.printLog("sendNotificationResponse"+response);
				UpdateAppointMentstatus appointMentstatus;
				Gson gson = new Gson();
				appointMentstatus = gson.fromJson(response, UpdateAppointMentstatus.class);
				Utility.printLog("AAA sendNotificationTOPassenger sendResponse"+appointMentstatus);

				if (mdialog!=null)
				{
					mdialog.dismiss();
					mdialog.cancel();
				}
				//  1 -> (1) Mandatory field missing
				if (appointMentstatus.getErrFlag()==0 && appointMentstatus.getErrNum() == 57)
				{
					// 57 -> (0) Status updated.
					SessionManager sessionManager=new SessionManager(getActivity());

					rlAvailable.setVisibility(View.VISIBLE);

					session.setIsInBooking(true);

					MainActivity.isResponse = true;
					sessionManager.setIsAppointmentAccept(true);
					sessionManager.setIhavarrived(true);
					sessionManager.setAppiontmentStatus(6);
					sessionManager.setAPT_DATE("");
					sessionManager.setPASSENGER_EMAIL("");
					Intent intent=new Intent(getActivity(), IHaveArrivedActivity.class);
					Bundle bundle=new Bundle();
					Utility.printLog("Latitude  = "+sessionManager.getDriverCurrentLat(),"Longitude = "+sessionManager.getDriverCurrentLongi());
					for (int i = 0; i < 5; i++)
					{
						publishLocation(sessionManager.getDriverCurrentLat(),sessionManager.getDriverCurrentLongi());
					}
					bundle.putSerializable(VariableConstants.APPOINTMENT, appointmentDetailList);
					intent.putExtras(bundle);
					Utility.printLog("Rahul SendNotification "+bundle);
					startActivity(intent);
					getActivity().finish();
				}

				else if (appointMentstatus.getErrFlag()==1 && appointMentstatus.getErrNum() == 56)
				{
					// 56 -> (1) Invalid status, cannot update.
					ErrorMessage(getResources().getString(R.string.messagetitle),appointMentstatus.getErrMsg(),false);
				}
				else if (appointMentstatus.getErrFlag()==1 && appointMentstatus.getErrNum() == 41)
				{
					// 41 -> (1) Passenger canceled.
					ErrorMessage(getResources().getString(R.string.messagetitle),appointMentstatus.getErrMsg(),false);
				}

				else if (appointMentstatus.getErrFlag()==1&&appointMentstatus.getErrNum()==3)
				{
					//           	   				3 -> (1) Error occurred while processing your request.
					ErrorMessage(getResources().getString(R.string.messagetitle),appointMentstatus.getErrMsg(),false);
				}
				else if (appointMentstatus.getErrFlag()==1&&appointMentstatus.getErrNum()==1)
				{
					//     1 -> (1) Mandatory field missing
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
				Utility.printLog("Exception"+e);
				//ErrorMessage(getResources().getString(R.string.messagetitle),getResources().getString(R.string.servererror),false);
			}
		}
			};

	ErrorListener errorListener1=new ErrorListener()
	{
		@Override
		public void onErrorResponse(VolleyError error)
		{
			if (mdialog!=null)
			{
				mdialog.dismiss();
				mdialog.cancel();
			}
			ErrorMessage(getResources().getString(R.string.messagetitle), getResources().getString(R.string.nonetwork), false);
			//Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
			Utility.printLog("Animation action Response Didnt Came Error");
		}
	};

	private void ErrorMessageForInvalidOrExpired(String title,String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(message);
		/*builder.setPositiveButton(getResources().getString(R.string.cancelbutton),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});*/

		builder.setNegativeButton(getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				SessionManager sessionManager=new SessionManager(getActivity());
				sessionManager.logoutUser();
				dialog.dismiss();
				/*getActivity().stopService(serviceIntent);*/
				Intent intent=new Intent(getActivity(), SplahsActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});

		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.accept_button)
		{
			//timer.cancel();

			MainActivity.isResponse = true;

			if (countDownTimer !=null)
			{
				countDownTimer.cancel();
				countDownTimer = null;
				progressBarLayout.setVisibility(View.GONE);
				locLinear.setVisibility(View.GONE);
				bottomLayout.setVisibility(View.GONE);
				rlAvailable.setVisibility(View.VISIBLE);
			}

			ConnectionDetector connectionDetector=new ConnectionDetector(getActivity());
			if (connectionDetector.isConnectingToInternet())
			{
				sendNotificationTOPassenger(6);
				/*if (type == 2)
				{
					getAcceptStatus(2);
				}
				else
				{
					sendNotificationTOPassenger(6);
				}*/
			}
		}

		else if (v.getId() == R.id.reject_button)
		{
			MainActivity.isResponse = true;
			if (countDownTimer != null)
			{
				countDownTimer.cancel();
				countDownTimer = null;
				progressBarLayout.setVisibility(View.GONE);
				locLinear.setVisibility(View.GONE);
				bottomLayout.setVisibility(View.GONE);
				rlAvailable.setVisibility(View.VISIBLE);
			}

			getRejectStatus(3);
			//ErrorMessageForReject(getResources().getString(R.string.messagetitle),getResources().getString(R.string.messageforreject));
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
		/*else if (v.getId() == R.id.creentlocation)
		{
		  googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(session.getDriverCurrentLat(),session.getDriverCurrentLongi()), 14.5f));
		}*/
	}
	/**
	 * Method for getting progress bar and layout when push will come
	 * It is coming for 30 seconds after that it will invisible 
	 */
	private void animatedUiHomePage(long timeScheduled )
	{
		Utility.printLog("Animation action Animated UI CAME");

		rlAvailable.setVisibility(View.GONE);
		network_bar.setVisibility(View.GONE);

		double arrayOfscallingfactor[]=Scaler.getScalingFactor(getActivity());
		int topstriplayoutmarigin = 0; // margin in dips
		int topMarginMap = 650; // margin in dips
		int topstriptopmarging = (int)Math.round(topstriplayoutmarigin*arrayOfscallingfactor[1]);//(int)(topstriplayoutmarigin * d); // margin in pixels
		int topMapmargin = (int)Math.round(topMarginMap*arrayOfscallingfactor[1]); // margin in pixels
		ObjectAnimator  animation2 = ObjectAnimator.ofFloat(locLinear, "y", topstriptopmarging);
		animation2.setDuration(500);
		ObjectAnimator animation1 = ObjectAnimator.ofFloat(bottomLayout, "y",topMapmargin);
		animation1.setDuration(500);
		AnimatorSet set = new AnimatorSet();
		set.playTogether(animation1, animation2);
		set.start();

		Utility.printLog("Animation started");
		/*if (countDownTimer == null) 
						{*/
		Utility.printLog(" Animation CountDown Timer IS NULL");
		final MediaPlayer mediaPlayer=MediaPlayer.create(getActivity(),R.raw.taxina);

		countDownTimer = new CountDownTimer(timeScheduled, 500)
		{
			// 500 means, onTick function will be called at every 500 milliseconds
			@Override
			public void onTick(long leftTimeInMilliseconds)
			{
				long seconds = leftTimeInMilliseconds / 1000;
				Utility.printLog("Animation action Animated UI TICK TICK");
				int barVal= (int)seconds;
				circularProgressBar.setProgress(barVal);
				progressText.setText(String.format("%02d", seconds % 60));
				progressBarLayout.setVisibility(View.VISIBLE);
				locLinear.setVisibility(View.VISIBLE);
				bottomLayout.setVisibility(View.VISIBLE);
				//=======================chaNGES===================
				//mediaPlayer.start();
				//=======================chaNGES===================
			}
			@Override
			public void onFinish()
			{
				if(progressText.getText().equals("00"))
				{
					Utility.printLog("Animation action Animated UI Finish");
					session.setAPT_DATE("");
					session.setPASSENGER_EMAIL("");
					MainActivity.isResponse = true;
					progressBarLayout.setVisibility(View.GONE);
					locLinear.setVisibility(View.GONE);
					bottomLayout.setVisibility(View.GONE);
					rlAvailable.setVisibility(View.VISIBLE);
					countDownTimer = null;
					//=======================chaNGES===================
					//mediaPlayer.stop();
					//=======================chaNGES===================
					//progressText.setText("STOP");          
				}
				else
				{
					progressText.setText("00");
				}
			}
		}.start();
		/*}
						else
						{
							Utility.printLog("Animation CounDownTimer Not Null");
						}*/
		/*circularProgressBar.setProgress(progressoffset10);
						mCountDownTimer=new CountDownTimer(timeScheduled, 100)
						{
							@Override
							public void onTick(long millisUntilFinished) 
							{
								progressoffset10++;
								circularProgressBar.setProgress(progressoffset10);
							}

							@Override
							public void onFinish() 
							{
								//Do what you want 
								progressoffset10++;
								circularProgressBar.setProgress(progressoffset10);
							}
						};
						mCountDownTimer.start();*/
	}

	/*public static LatLng getLatLongFromGivenAddress(String youraddress) {
		String uri = null;
		Double lat=null, lng =null;
		try {
			uri = "http://maps.google.com/maps/api/geocode/json?address=" + URLEncoder.encode(youraddress,"UTF-8") + "&sensor=false";
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpGet httpGet = new HttpGet(uri);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());

			lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lng");

			lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lat");


		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new LatLng(lat,lng);
	}
*/
	private void getAcceptStatus(final int responsecode)
	{
		Utility utility=new Utility();

		ConnectionDetector connectionDetector = new ConnectionDetector(getActivity());
		if (connectionDetector.isConnectingToInternet())
		{
			String deviceId = Utility.getDeviceId(getActivity());
			String currentTime = utility.getCurrentGmtTime();
			SessionManager sessionManager = new SessionManager(getActivity());
			String passengerEmailId = appointmentDetailData.getEmail();
			String appointmentDateTime = appointmentDetailData.getApptDt();
			String sessionToken = sessionManager.getSessionToken();
			int notes = 2;
			final String mParams[]={sessionToken,deviceId,passengerEmailId,appointmentDateTime,""+responsecode ,""+notes,currentTime/*currentdata[0]*/};

			mdialog.setMessage(getResources().getString(R.string.Pleasewaitmessage));
			mdialog.show();
			mdialog.setCancelable(false);

			RequestQueue queue = Volley.newRequestQueue(getActivity());  // this = context
			String url = VariableConstants.getRejectstatusUpdate_url;
			StringRequest postRequest = new StringRequest(Request.Method.POST, url,responseListenerofAcceptNotification,errorListener2 )
			{
				@Override
				protected Map<String, String> getParams()
				{
					Map<String, String>  params = new HashMap<String, String>();

					params.put("ent_sess_token",mParams[0]);
					params.put("ent_dev_id",mParams[1]);
					params.put("ent_pas_email", mParams[2]);
					params.put("ent_appnt_dt",mParams[3]);
					params.put("ent_response", mParams[4]);
					params.put("ent_book_type", mParams[5]);
					params.put("ent_date_time", mParams[6]);

					Utility.printLog("paramsRequest = "+params);
					return params;
				}
			};

			int socketTimeout = 60000;//60 seconds - change to what you want
			RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
			postRequest.setRetryPolicy(policy);
			queue.add(postRequest);
		}
	}

	Response.Listener<String> responseListenerofAcceptNotification = new Listener<String>()
	{

		@Override
		public void onResponse(String response)
		{
			rlAvailable.setVisibility(View.VISIBLE);
			try
			{
				RespondAppointment respondAppointment = null;
				Gson gson = new Gson();
				respondAppointment=gson.fromJson(response, RespondAppointment.class);
				Utility.printLog("Accept ="+respondAppointment);

				if (mdialog!=null)
				{
					mdialog.dismiss();
					mdialog.cancel();
				}

				if (respondAppointment.getErrFlag()==0 && respondAppointment.getErrNum()==40)
				{
					Intent intent = new Intent(getActivity(),PendingBooking.class);
					startActivity(intent);
					getActivity().finish();
				}

				else if (respondAppointment.getErrFlag()==0&&respondAppointment.getErrNum()==3)
				{
					// 3 -> (1) Error occurred while processing your request.
					ErrorMessage(getResources().getString(R.string.messagetitle),respondAppointment.getErrMsg(),false);
				}
				else if (respondAppointment.getErrFlag()==1 && respondAppointment.getErrNum()==77)
				{
					ErrorMessage(getResources().getString(R.string.messagetitle),respondAppointment.getErrMsg(),false);
				}

				else if (respondAppointment.getErrFlag()==0&&respondAppointment.getErrNum()==41)
				{
					// 41 -> (1) Sorry, passenger have cancelled this appointment.
					ErrorMessage(getResources().getString(R.string.messagetitle),respondAppointment.getErrMsg(),false);
				}
				else if (respondAppointment.getErrFlag()==1&&respondAppointment.getErrNum()==30)
				{
					//30 -> (1) No appointments on this date.
					ErrorMessage(getResources().getString(R.string.messagetitle),respondAppointment.getErrMsg(),false);
				}
				else if (respondAppointment.getErrFlag()==1&&respondAppointment.getErrNum()==7)
				{
					//7 -> (1) Invalid token, please login or register.
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle),respondAppointment.getErrMsg());
				}
				else if (respondAppointment.getErrFlag()==1&&respondAppointment.getErrNum()==6)
				{
					//6-> (1)  Session token expired, please login.
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle),respondAppointment.getErrMsg());
				}
				else if (respondAppointment.getErrFlag()==1&&respondAppointment.getErrNum()==1)
				{
					//1 -> (1) Mandatory field missing
					ErrorMessage(getResources().getString(R.string.messagetitle),respondAppointment.getErrMsg(),false);
				}
			}
			catch (Exception e)
			{
				Utility.printLog("Reject appointment response", "BackGroundTaskForGetPaddingAppointment doInBackground   Exception "+e);
			}

		}
	};

	;

	/**
	 * Method for publish current location to passenger. 
	 * @param latitude
	 * @param longitude
	 */
  public void publishLocation(double latitude,double longitude)
  {
	  String message ;
	  SessionManager sessionManager = new SessionManager(mainActivity);
	  String subscribChannel=sessionManager.getSubscribeChannel();
	  String phone = sessionManager.getMobile();
	  message="{\"a\" :\""+6+"\", \"e_id\" :\""+sessionManager.getUserEmailid()+"\", \"lt\" :"+latitude+", \"lg\" :"+longitude+", \"ph\" :\""+phone+"\",\"dt\" :\""+sessionManager.getDate()+"\",\"bid\" :\""+sessionManager.getBOOKING_ID()+"\",\"chn\" :\""+subscribChannel+"\"}";
	  Utility.printLog("Publish Location = "+message);

	  if (pasChannel != null)
	  {
		  Utility.printLog("Publish  pasChannel= "+pasChannel);
		  //Pubnub Change 17/5/2016
		  PublishUtility.publish(pasChannel, message, pubnub);
	  }
	  else
	  {
		  ErrorMessage(getResources().getString(R.string.messagetitle),getResources().getString(R.string.passengercancelled),false);
	  }
  }

 private void ErrorMessageForExpired(String title,String message)
 {
	 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	 builder.setTitle(title);
	 builder.setMessage(message);
	 builder.setPositiveButton(getResources().getString(R.string.okbuttontext),
			 new DialogInterface.OnClickListener()
	 {
		 @Override
		 public void onClick(DialogInterface dialog, int which)
		 {
			 if (countDownTimer != null)
			 {
				 MainActivity.isResponse = true;
				 countDownTimer.cancel();
				 countDownTimer = null;
				 progressBarLayout.setVisibility(View.GONE);
				 locLinear.setVisibility(View.GONE);
				 bottomLayout.setVisibility(View.GONE);
			 }
			 dialog.dismiss();
		 }
	 });

	 AlertDialog	 alert = builder.create();
	 alert.setCancelable(false);
	 alert.show();
 }
	/**
	 * Method for rejecting appointment 
	 * This method is showing alert with two buttons	
	 * @param title
	 * @param message
	 */
   /*private void ErrorMessageForReject(String title,String message)
   {
	   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	   MainActivity.isResponse = true;
	   builder.setTitle(title);
	   builder.setMessage(message);

	   builder.setPositiveButton(getResources().getString(R.string.cancelbutton),
			   new DialogInterface.OnClickListener()
	   {
		   @Override
		   public void onClick(DialogInterface dialog, int which)
		   {
			   dialog.dismiss();
		   }
	   });

	   builder.setNegativeButton(getResources().getString(R.string.reject),
			   new DialogInterface.OnClickListener()
	   {
		   @Override
		   public void onClick(DialogInterface dialog, int which)
		   {
			   dialog.dismiss();
			   //timer.cancel();
			   countDownTimer.cancel();
			   countDownTimer = null;
			   getRejectStatus(3);
			   //getActivity().finish();
		   }
	   });

	   AlertDialog	 alert = builder.create();
	   alert.setCancelable(false);
	   alert.show();
   }*/
	/**
	 * Method for calling service of rejecting Appointment				
	 * @param responsecode
	 */
	private void getRejectStatus(final int responsecode)
	{
		Utility utility=new Utility();

		ConnectionDetector connectionDetector=new ConnectionDetector(getActivity());
		if (connectionDetector.isConnectingToInternet())
		{
			String deviceid=Utility.getDeviceId(getActivity());
			String curenttime=utility.getCurrentGmtTime();
			SessionManager sessionManager=new SessionManager(getActivity());
			/*String passengerEmailid = emailpassenger;
			String appointdatetime = currentDateTime;*/
			String passengerEmailid = appointmentDetailDatas.get(0).getEmail();
			String appointdatetime = appointmentDetailDatas.get(0).getApptDt();
			String sessiontoken = sessionManager.getSessionToken();
			String notes="testing";
			final String mparams[]={sessiontoken,deviceid,passengerEmailid,appointdatetime,""+responsecode ,notes,curenttime/*currentdata[0]*/};

			mdialog.setMessage(getResources().getString(R.string.Pleasewaitmessage));
			mdialog.show();
			mdialog.setCancelable(false);
			RequestQueue queue = Volley.newRequestQueue(getActivity());  // this = context
			String  url = VariableConstants.getRejectstatusUpdate_url;
			StringRequest postRequest = new StringRequest(Request.Method.POST, url,responseListenerofRejectNotification,errorListener2 )
			{
				@Override
				protected Map<String, String> getParams()
				{
					Map<String, String>  params = new HashMap<String, String>();
					params.put("ent_sess_token",mparams[0]);
					params.put("ent_dev_id",mparams[1]);

					params.put("ent_pas_email", mparams[2]);
					params.put("ent_appnt_dt",mparams[3]);

					params.put("ent_response", mparams[4]);
					params.put("ent_book_type", mparams[5]);
					params.put("ent_date_time", mparams[6]);

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

	Listener<String> responseListenerofRejectNotification=new Listener<String>()
			{

		@Override
		public void onResponse(String response)
		{
			rlAvailable.setVisibility(View.VISIBLE);
			try
			{
				RespondAppointment respondAppointment = null;
				Gson gson = new Gson();
				respondAppointment=gson.fromJson(response, RespondAppointment.class);
				Utility.printLog("Reject "+response);

				if (mdialog!=null)
				{
					mdialog.dismiss();
					mdialog.cancel();
				}

				if (respondAppointment.getErrFlag()==0 && respondAppointment.getErrNum()==40)
				{
					// 40 -> (0) Appointment updated successfully!
					///Intent intent=new Intent(getActivity(), MainActivity.class);
					MainActivity.isResponse = true;
					session.setAPT_DATE("");
					session.setPASSENGER_EMAIL("");
					session.setIsInBooking(false);
					progressBarLayout.setVisibility(View.GONE);
					locLinear.setVisibility(View.GONE);
					bottomLayout.setVisibility(View.GONE);
					//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					//startActivity(intent);
					//getActivity().finish();
				}

				else if (respondAppointment.getErrFlag()==1 && respondAppointment.getErrNum()==102)
				{
					// 3 -> (1) Error occurred while processing your request.
					ErrorMessageReject(getResources().getString(R.string.messagetitle),respondAppointment.getErrMsg());
				}

				else if (respondAppointment.getErrFlag()==1 && respondAppointment.getErrNum()==3)
				{
					// 3 -> (1) Error occurred while processing your request.
					ErrorMessageReject(getResources().getString(R.string.messagetitle),respondAppointment.getErrMsg());
				}
				else if (respondAppointment.getErrFlag()==1 && respondAppointment.getErrNum()==77)
				{
					// 3 -> (1) Error occurred while processing your request.
					ErrorMessage(getResources().getString(R.string.messagetitle),respondAppointment.getErrMsg(),false);
				}

				else if (respondAppointment.getErrFlag()==0&&respondAppointment.getErrNum()==41)
				{
					// 41 -> (1) Sorry, passenger have cancelled this appointment.
					ErrorMessage(getResources().getString(R.string.messagetitle),respondAppointment.getErrMsg(),false);
				}
				else if (respondAppointment.getErrFlag()==1&&respondAppointment.getErrNum()==30)
				{
					//30 -> (1) No appointments on this date.
					ErrorMessage(getResources().getString(R.string.messagetitle),respondAppointment.getErrMsg(),false);
				}

				else if (respondAppointment.getErrFlag()==1&&respondAppointment.getErrNum()==1)
				{
					//1 -> (1) Mandatory field missing
					ErrorMessage(getResources().getString(R.string.messagetitle), respondAppointment.getErrMsg(), false);
				}
				else if(respondAppointment.getErrNum()==6|| respondAppointment.getErrNum()==7 ||
						respondAppointment.getErrNum()==94 || respondAppointment.getErrNum()==96)
				{
					ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle),respondAppointment.getErrMsg());
				}
			}
			catch (Exception e)
			{
				Utility.printLog("Reject appointment response =  "+e);
			}

		}
			};

	ErrorListener errorListener2=new ErrorListener()
	{
		@Override
		public void onErrorResponse(VolleyError error)
		{
			if (mdialog!=null)
			{
				mdialog.dismiss();
				mdialog.cancel();
			}
		}
	};

	private void ErrorMessageReject(String title,String message)
	{
		session.setCancelPushFlag(false);
		session.setFlagForPayment(false);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				session.setAPT_DATE("");
				session.setPASSENGER_EMAIL("");
				progressBarLayout.setVisibility(View.GONE);
				locLinear.setVisibility(View.GONE);
				bottomLayout.setVisibility(View.GONE);
				dialog.dismiss();
			}
		});
		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}
	@Override
	public void onStop()
	{
		super.onStop();

	/*	getActivity().startService(serviceIntent);*/
	}
	private void buildAlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
				.setTitle("Location Service Disabled:")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
						startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
						dialog.dismiss();
					}
				});
				/*.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
						dialog.cancel();
					}
				});*/
		final AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void updatedInfo(String info) {

	}

	@Override
	public void locationUpdates(Location location) {
		if(carMarker!=null){
			carMarker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 15.0f);
			googleMap.animateCamera(cameraUpdate);


		}
	}

	@Override
	public void locationFailed(String message) {

	}




}
