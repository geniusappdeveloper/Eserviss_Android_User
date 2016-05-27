package com.eserviss.passenger.main;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Color;
import android.location.Geocoder;

import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Address;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.egnyt.eserviss.MainActivity;
import com.egnyt.eserviss.R;
import com.eserviss.passenger.pojo.CarsDetailList;
import com.eserviss.passenger.pojo.ETADistanceResponse;
import com.eserviss.passenger.pojo.GeocodingResponse;
import com.eserviss.passenger.pojo.GetAppointmentDetails;
import com.eserviss.passenger.pojo.GetCarDetails;
import com.eserviss.passenger.pojo.GetCardResponse;
import com.eserviss.passenger.pojo.LiveBookingResponse;
import com.eserviss.passenger.pojo.LoginResponse;
import com.eserviss.passenger.pojo.StatusInformation;
import com.eserviss.passenger.pojo.TipResponse;
import com.eserviss.passenger.pubnu.pojo.PubNubResponse;
import com.eserviss.passenger.pubnu.pojo.PubnubResponseNew;
import com.log.LogFile;
import com.special.ResideMenu.ResideMenu;
import com.squareup.picasso.Picasso;
import com.threembed.utilities.CircleTransform;
import com.threembed.utilities.MyEventBus;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gc.materialdesign.views.ProgressBarCircularIndetermininate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;
import com.threembed.utilities.Scaler;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

import de.greenrobot.event.EventBus;
import io.card.payment.CardType;

public class HomePageFragment extends Fragment implements OnClickListener,OnTouchListener
{
	public static GoogleMap googleMap;
	private double currentLatitude,currentLongitude;
	private double searchlocatinlat,searchlocatinlng;
	private static final String TAG ="HomePage";
	private Button Request_Pick_up_here,Fare_Quote,Driver_on_the_way_txt,AddLocation,new_add_drop_off_location;//promeCode,Driver_Arrow,
	private ImageButton Btn_Back,Img_Map,Img_Dropoff,Mid_Pointer;
	private ImageButton addressSearchButton,myPosition,new_img_dropoff;
	private ImageView Card_Image,Driver_Profile_Pic,carnowimage,carlatrimage;//,setLocationArrow;
	private TextView current_address,Txt_Pickup,Driver_Name,Driver_Distance,Driver_Time,Driver_Car_Type,Driver_Car_Num,new_dropoff_location_address;
	private TextView pickupLocationAddress,Dropoff_Location_Address,Card_Info,pickup_Distance,share_eta,contact_driver,cancel_trip;
	private String pick_up,getCancelResponse;
	private static View view;
	private boolean isSetDropoffLocation=false,isBackPressed=false;
	public static int cardChecked=-1;
	private Timer myTimer;
	private TimerTask myTimerTask;
	private RelativeLayout pickup,show_address_relative,relativePickupLocation,Relative_Pickup_Navigation,Relative_Dropoff_Location,Relative_Card_Info,Relative_Payment_Info;
	private RelativeLayout RL_homepage,Driver_Confirmation,all_types_layout,now_button,later_button,choose_payment_layout;//Rl_distance_time,
	private LoginResponse response = new LoginResponse();
	public static Activity mActivity;
	private boolean isFareQuotePressed=false;
	private String from_latitude,from_longitude,to_latitude,to_longitude,mDROPOFF_ADDRESS,mPICKUP_ADDRESS;
	private String Default_Card_Id,carDetailsResponse;
	private String Car_Type_Id=null;
	private Pubnub pubnub;
	private HashMap<String,Marker> marker_map,marker_map_on_the_way,marker_map_arrived;
	private HashMap<String,Location> type1_markers,type2_markers,type3_markers,type4_markers;
	private ArrayList<String> type1_channels_list,type2_channels_list,type3_channels_list,type4_channels_list;
	private RatingBar Driver_Rating;
	private IntentFilter filter;
	private BroadcastReceiver receiver;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private View popupLayout;
	private PopupWindow popup_share;//popup_driver;
	private Button message_shar,whatsapp_sahre,email_share,cancel_share;//facebook_share,twitter_share
	private Button pay_cancel,pay_cash,pay_card,paymenttype,payment_done;//,pay_cash,,pay_card
	//private RelativeLayout driver_parent,choose_payment_layout;
	private LiveBookingResponse CancelBooking;
	private ProgressDialog dialogL;
	private SessionManager session;
	private GetAppointmentDetails appointmentResponse;
	private StatusInformation statusResponse;
	private GetCarDetails getCarDetails;
	private Marker drivermarker,driver_on_the_way_marker,driver_arrived;
	private Marker picupmarker;
	private String payment_type;
	private ArrayList<String> old_subscribed_driver_channels = new ArrayList<String>();
	private ArrayList<String> type1NearestDrivers,type2NearestDrivers,type3NearestDrivers,type4NearestDrivers;
	private ArrayList<String> nearestDrivers;
	private String locationName,formattedAddress,car_name,distance="--",car_size;
	private float rotation_angle;
	private Timer pollingTimer = new Timer(); 
	private Timer myTimer_publish;
	private TimerTask myTimerTask_publish;
	private static boolean visibility=false;
	private ProgressBarCircularIndetermininate pubnubProgressDialog;
	private int height,width,current_widht=0;
	private ImageView image,three_image,four_image;
	private ImageButton one_car_button,two_cars_button1,two_cars_button2,three_cars_button1,three_cars_button2,three_cars_button3;
	private ImageButton four_cars_button1,four_cars_button2,four_cars_button3,four_cars_button4;
	private RelativeLayout one_car_layout,two_cars_layout,three_cars_layout,four_cars_layout,now_later_status;
	private TextView one_car_Type_Name,two_cars_Type1_Name,two_cars_Type2_Name,three_cars_Type1_Name,three_cars_Type2_Name,three_cars_Type3_Name;
	private TextView four_cars_Type1_Name,four_cars_Type2_Name,four_cars_Type3_Name,four_cars_Type4_Name;//,booked_now_later
	private Geocoder geocoder;
	// rahul : car type index
	private boolean IsreturnFromSearch=false;
	private int car_type_index=0;
	TimerTask myTimerTask_ETA;
	Timer myTimer_ETA;
	private boolean isSetClicked=false;
	private RelativeLayout new_relative_dropoff_location;
	private boolean isType1MarkersPloted=false,isType2MarkersPloted=false,isType3MarkersPloted=false,isType4MarkersPloted=false;
	private boolean firstTimeMarkersPlotting = true,isCouponValid = false;
	private String Type1Distance,Type2Distance,Type3Distance,Type4Distance;
	private String laterBookingDate=null,mPromoCode=null;
	private String later_year,later_month,later_day,later_hour,later_min;
	//private ImageButton normal_view,hybrid_view;
	private LinearLayout now_later_layout;//
	private RelativeLayout farePromoLayouy;//
	private double[] dblArray;
	public boolean gpsEnabled;
	public boolean gpsFix;
	public double latitude;
	public double longitude;
	private Dialog dialog;
	private LocationManager locationManager;
	private MyGpsListener gpsListener;
	private long DURATION_TO_FIX_LOST_MS = 10000;
	private long MINIMUM_UPDATE_TIME = 0;
	private float MINIMUM_UPDATE_DISTANCE = 0.0f;
	private boolean isGpsEnable = false;
	private TipResponse tipResponse;
	private String eta_latitude;
	private String eta_longitude;
	//private RelativeLayout relative_now_later_status;
	private TextView rate_unit;
	ProgressDialog dialog2;
	private Dialog mDialog;
	TextView Appointment_location;
	TextView booknwtxt,bookltrtxt;
	Typeface roboto_condensed;
	String baseFare0="--",pricePerMin0="--",pricePerKm0="--",minFare0="--",maxSize0="--",baseFare1="--",pricePerMin1="--",pricePerKm1="--",minFare1="--",
			maxSize1="--",baseFare2="--",pricePerMin2="--",pricePerKm2="--",minFare2="--",maxSize2="--",
	baseFare3="--",pricePerMin3="--",pricePerKm3="--",minFare3="--",maxSize3="--";
   TextView cancel,txt_roadyo;
	//====================My change====================
	String card_info="cash";
	RelativeLayout req_lay,relative_booking;
	TextView car_color,seater,booking_id,pageno;
	ImageView logo,image_home;


	RelativeLayout map_view;
	//====================My change====================
	@SuppressWarnings("unchecked")
	private void _publish(final String channel,String message)
	{

		// This is where we publish the request to pubnub to get :
		// 1) car types
		// 2) online drivers

		Utility.printLog("CONTROL INSIDE _publish");
		@SuppressWarnings("rawtypes")
		Hashtable args = new Hashtable(2);

		if(args.get("message") == null)
		{
			try {
				Integer i = Integer.parseInt(message);
				args.put("message", i);
			} catch (Exception e) {

			}
		}
		if (args.get("message") == null) 
		{
			try {
				Double d = Double.parseDouble(message);
				args.put("message", d);
			} catch (Exception e) 
			{

			}
		}
		if (args.get("message") == null)
		{
			try {
				JSONArray js = new JSONArray(message);
				args.put("message", js);
			} catch (Exception e) 
			{

			}
		}
		if (args.get("message") == null) 
		{
			try 
			{
				JSONObject js = new JSONObject(message);
				args.put("message", js);
			} catch (Exception e) 
			{
			}
		}
		if(args.get("message") == null) 
		{
			args.put("message", message);
		}

		// Publish Message
		args.put("channel", channel); // Channel Name

		//Pubnub Change 17/5/2016
		LogFile.log("channel"+channel);
		pubnub.publish(args, new Callback() 
		{
			@Override
			public void successCallback(String channel,Object message)
			{
				Utility.printLog("CONTROL INSIDE _publish successCallback");
				Utility.printLog("successCallback PUBLISH message: " + message);

				//Pubnub Change 17/5/2016
				LogFile.log("channel"+channel+"successCallback PUBLISH messag"+message);
				Log.i("","successCallback PUBLISH message: " + message+"channel"+channel);
			}
			@Override
			public void errorCallback(String channel,PubnubError error)
			{
				Utility.printLog("CONTROL INSIDE _publish errorCallback");
				Utility.printLog("errorCallback PUBLISH : " + error);
              //Pubnub Change 17/5/2016
				LogFile.log("errorCallback"+error);
				if(isAdded())
					getActivity().runOnUiThread(new Runnable() 
					{
						@Override
						public void run()
						{
							// TODO Auto-generated method stub


							pubnubProgressDialog.setVisibility(View.INVISIBLE);
							pickup_Distance.setVisibility(View.VISIBLE);
						}
					});
			}
		});
	}

	private void subscribe(String[] channels)
	{

		// this is where we subscribe to our own channel ( doubt why are we using a for loop below ? ) rahul

		//Hashtable args = new Hashtable(1);
		Utility.printLog("CONTROL INSIDE subscribe");
		for(int i=0;i<channels.length;i++)
			Utility.printLog("","subscribed channel is= "+i+" "+channels[i]);
		//args.put("channel",session.getChannelName());
		try 
		{
			pubnub.subscribe(channels, new Callback()
			{
				@Override
				public void connectCallback(String channel,Object message)
				{
					Utility.printLog("SUBSCRIBE : CONNECT on channel:"
							+ channel
							+ " : "
							+ message.getClass()
							+ " : "
							+ message.toString());

					//Pubnub Change 17/5/2016
					LogFile.log("SUBSCRIBE : CONNECT on channel:"
							+ channel
							+ " : "
							+ message.getClass()
							+ " : "
							+ message.toString());
				}
				@Override
				public void disconnectCallback(String channel,Object message)
				{
					Utility.printLog("SUBSCRIBE : DISCONNECT on channel:"
							+ channel
							+ " : "
							+ message.getClass()
							+ " : "
							+ message.toString());

					//Pubnub Change 17/5/2016
					LogFile.log("SUBSCRIBE : DISCONNECT on channel:"
							+ channel
							+ " : "
							+ message.getClass()
							+ " : "
							+ message.toString());
				}

				@Override
				public void reconnectCallback(String channel,Object message) 
				{
					Utility.printLog("SUBSCRIBE : RECONNECT on channel:"
							+ channel
							+ " : "
							+ message.getClass()
							+ " : "
							+ message.toString());


					//Pubnub Change 17/5/2016
					LogFile.log("SUBSCRIBE : RECONNECT on channel:"
							+ channel
							+ " : "
							+ message.getClass()
							+ " : "
							+ message.toString());
				}

				@Override
				public void successCallback(String channel, final Object message)
				{
					// rahul : sucessfully listening to our own channel


					Log.i("Ali","successCallback  "+message.toString());


					//Pubnub Change 17/5/2016
					LogFile.log("successCallback  "+message.toString());

					if(getActivity()!=null)
					{
						if(isAdded())
							getActivity().runOnUiThread(new Runnable()
							{
								@Override
								public void run()
								{
									pubnubProgressDialog.setVisibility(View.INVISIBLE);
									//pickup_Distance.setVisibility(View.VISIBLE);
									String responsebid="";//Roshani
									String sessionbid="";//Roshani
									PubnubResponseNew filterResponse = null ;
									if(message.toString()!=null)
									{
										Gson gson = new Gson();
										filterResponse=gson.fromJson(message.toString(), PubnubResponseNew.class);
										responsebid=filterResponse.getBid();//Roshani
										if(responsebid!=null &&   responsebid.contains("BID"))
										{
											responsebid= responsebid.replace("BID :","");
										}
										sessionbid=session.getBookingId();//Roshani
										Utility.printLog(" successCallback MSG getA="+filterResponse.getA());

										if((filterResponse!=null) && (filterResponse.getMasArr()!=null && filterResponse.getMasArr().size()>0))
										{
											if(car_type_index==0)
											{
												baseFare0=filterResponse.getTypes().get(0).getBasefare();
												pricePerKm0=filterResponse.getTypes().get(0).getPrice_per_km();
												pricePerMin0=filterResponse.getTypes().get(0).getPrice_per_min();
												minFare0=filterResponse.getTypes().get(0).getMin_fare();
												maxSize0=filterResponse.getTypes().get(0).getMax_size();
											}

											if(car_type_index==1)
											{
												baseFare1=filterResponse.getTypes().get(1).getBasefare();
												pricePerKm1=filterResponse.getTypes().get(1).getPrice_per_km();
												pricePerMin1=filterResponse.getTypes().get(1).getPrice_per_min();
												minFare1=filterResponse.getTypes().get(1).getMin_fare();
												maxSize1=filterResponse.getTypes().get(1).getMax_size();
											}

											if(car_type_index==2)
											{
												baseFare2=filterResponse.getTypes().get(2).getBasefare();
												pricePerKm2=filterResponse.getTypes().get(2).getPrice_per_km();
												pricePerMin2=filterResponse.getTypes().get(2).getPrice_per_min();
												minFare2=filterResponse.getTypes().get(2).getMin_fare();
												maxSize2=filterResponse.getTypes().get(2).getMax_size();
											}

											if(car_type_index==3)
											{
												baseFare3=filterResponse.getTypes().get(3).getBasefare();
												pricePerKm3=filterResponse.getTypes().get(3).getPrice_per_km();
												pricePerMin3=filterResponse.getTypes().get(3).getPrice_per_min();
												minFare3=filterResponse.getTypes().get(3).getMin_fare();
												maxSize3=filterResponse.getTypes().get(3).getMax_size();
											}
										}

									}


									// rahul : here we are checking for booking status as we have to handle the case where the app may have been killed or from background to foreground

									if((filterResponse!=null && filterResponse.getA().equals("2")) && !(session.isDriverOnWay()  || session.isDriverOnArrived()  ||	session.isTripBegin() || session.isInvoiceRaised()))
									{

										// rahul : we are listening to the server channel so this will be invoked when we are listneing to server only 

										Utility.printLog("rahul : INSIDE DRIVERS AROUND YOU message="+message.toString());

										if(filterResponse.getTypes()!=null && filterResponse.getTypes().size()>0)
										{
											Utility.printLog("rahul : the number of car types ="+filterResponse.getTypes().size());

											boolean newCarTypesFound = false;

											if(response == null || response.getCarsdetails() == null)
											{
												newCarTypesFound = true;
												response = new LoginResponse();
											}

											else if(filterResponse.getTypes().size() != response.getCarsdetails().size())
											{
												newCarTypesFound = true;
											}



											if(!newCarTypesFound)


												for(int i=0;i<filterResponse.getTypes().size();i++)
												{
													for(int j=0;j<response.getCarsdetails().size();j++)
													{
														Utility.printLog("NEW CAR TYPES FOUND AT "+i+" "+filterResponse.getTypes().get(i).getType_name());
														Utility.printLog("NEW CAR TYPES FOUND old AT "+j+" "+response.getCarsdetails().get(j).getType_name());

														if(car_type_index==0)                          //akbar if change the types price  showing the popup
														{
															if(!(filterResponse.getTypes().get(0).getPrice_per_min().
																	equals(response.getCarsdetails().get(0).getPrice_per_min())) ||
																	!(filterResponse.getTypes().get(0).getPrice_per_km().
																			equals(response.getCarsdetails().get(0).getPrice_per_km())) || 
																			!(filterResponse.getTypes().get(0).getBasefare().
																			equals(response.getCarsdetails().get(0).getBasefare()))
																			|| !(filterResponse.getTypes().get(0).getMin_fare().
																			equals(response.getCarsdetails().get(0).getMin_fare())))
																	{
																		Utility.printLog("akbar pubnub response"+filterResponse.getTypes().get(0).getPrice_per_km());
																		Utility.printLog("akbar login response"+response.getCarsdetails().get(0).getPrice_per_km());
																		//==============================My Changes 11-5-2016======================================================

																	//	Utility.ShowAlert("Please check the prices before booking.", getActivity());
																		Gson gson1 = new Gson();
																		response=gson1.fromJson(message.toString(), LoginResponse.class);
																	}	
														}
														
														if(car_type_index==1)
														{
															if(!(filterResponse.getTypes().get(1).getPrice_per_min().
																	equals(response.getCarsdetails().get(1).getPrice_per_min())) ||
																	!(filterResponse.getTypes().get(1).getPrice_per_km().
																			equals(response.getCarsdetails().get(1).getPrice_per_km())) || 
																			!(filterResponse.getTypes().get(1).getBasefare().
																			equals(response.getCarsdetails().get(1).getBasefare()))
																			|| !(filterResponse.getTypes().get(1).getMin_fare().
																			equals(response.getCarsdetails().get(1).getMin_fare())))
																	{
																		Utility.printLog("akbar pubnub response"+filterResponse.getTypes().get(0).getPrice_per_km());
																		Utility.printLog("akbar login response"+response.getCarsdetails().get(0).getPrice_per_km());
                                        //==============================My Changes 11-5-2016======================================================
																		//Utility.ShowAlert("Please check the prices before booking.", getActivity());
																		Gson gson1 = new Gson();
																		response=gson1.fromJson(message.toString(), LoginResponse.class);
																	}	
														}
														
														if(car_type_index==2)
														{
															if(!(filterResponse.getTypes().get(2).getPrice_per_min().
																	equals(response.getCarsdetails().get(2).getPrice_per_min())) ||
																	!(filterResponse.getTypes().get(2).getPrice_per_km().
																			equals(response.getCarsdetails().get(2).getPrice_per_km())) || 
																			!(filterResponse.getTypes().get(2).getBasefare().
																			equals(response.getCarsdetails().get(2).getBasefare()))
																			|| !(filterResponse.getTypes().get(2).getMin_fare().
																			equals(response.getCarsdetails().get(2).getMin_fare())))
																	{
																		Utility.printLog("akbar pubnub response"+filterResponse.getTypes().get(0).getPrice_per_km());
																		Utility.printLog("akbar login response"+response.getCarsdetails().get(0).getPrice_per_km());
																		//==============================My Changes 11-5-2016======================================================

																		//	Utility.ShowAlert("Please check the prices before booking.", getActivity());
																		Gson gson1 = new Gson();
																		response=gson1.fromJson(message.toString(), LoginResponse.class);
																	}	
														}
														
														if(car_type_index==3)
														{
															if(!(filterResponse.getTypes().get(3).getPrice_per_min().
																	equals(response.getCarsdetails().get(3).getPrice_per_min())) ||
																	!(filterResponse.getTypes().get(3).getPrice_per_km().
																			equals(response.getCarsdetails().get(3).getPrice_per_km())) || 
																			!(filterResponse.getTypes().get(3).getBasefare().
																			equals(response.getCarsdetails().get(3).getBasefare()))
																			|| !(filterResponse.getTypes().get(3).getMin_fare().
																			equals(response.getCarsdetails().get(3).getMin_fare())))
																	{
																		Utility.printLog("akbar pubnub response"+filterResponse.getTypes().get(0).getPrice_per_km());
																		Utility.printLog("akbar login response"+response.getCarsdetails().get(0).getPrice_per_km());
																		//==============================My Changes 11-5-2016======================================================

																		//Utility.ShowAlert("Please check the prices before booking.", getActivity());
																		Gson gson1 = new Gson();
																		response=gson1.fromJson(message.toString(), LoginResponse.class);
																	}	
														}
														
														if(filterResponse.getTypes().get(i).getType_name().equals(response.getCarsdetails().get(j).getType_name()))
														{
															//newCarTypesFound = false;

															//MY CHANGE CAR CHANGE INVERSE
															newCarTypesFound = true;
															//
															break;
														}
														else
														{
															//newCarTypesFound = true;
															//MY CHANGE CAR CHANGE INVERSE
															newCarTypesFound = false;
															//
														}
													}

													if(newCarTypesFound)
													{
														break;
													}
												}


											Utility.printLog("NEW CAR TYPES FOUND status: "+newCarTypesFound);
											Log.e("Session STAtus",""+newCarTypesFound);
											if(newCarTypesFound)
											{
												// rahul : commented this
												Gson gson1 = new Gson();
												response=gson1.fromJson(message.toString(), LoginResponse.class);

												type1_channels_list.clear();
												type2_channels_list.clear();
												type3_channels_list.clear();
												type4_channels_list.clear();

												type1_markers.clear();
												type2_markers.clear();
												type3_markers.clear();
												type4_markers.clear();

												marker_map.clear();
												marker_map_on_the_way.clear();
												Utility.printLog("marker_map_on_the_way 8");
												marker_map_arrived.clear();
												googleMap.clear();

												firstTimeMarkersPlotting = true;

												session.storeCarTypes(message.toString());
												Log.e("Session Storage",""+message);

												Utility.printLog("INSIDE DRIVERS AROUND YOU CAR TYPES CHANGED");

												if(response.getCarsdetails().size()>0)
												{
													Utility.printLog("INSIDE DRIVERS AROUND YOU CAR TYPES ARE CHANGING");

													car_type_index = 0;
													Car_Type_Id=response.getCarsdetails().get(0).getType_id();
													car_name=response.getCarsdetails().get(0).getType_name();
													//================My Change===============
													car_size=response.getCarsdetails().get(0).getMax_size();
													//================My Change===============
													if(response.getCarsdetails().size()==1)
													{
														one_car_layout.setVisibility(View.VISIBLE);
														two_cars_layout.setVisibility(View.GONE);
														three_cars_layout.setVisibility(View.GONE);
														four_cars_layout.setVisibility(View.GONE);

														one_car_Type_Name.setText(response.getCarsdetails().get(0).getType_name());
													}
													else if(response.getCarsdetails().size()==2)
													{
														one_car_layout.setVisibility(View.GONE);
														two_cars_layout.setVisibility(View.VISIBLE);
														three_cars_layout.setVisibility(View.GONE);
														four_cars_layout.setVisibility(View.GONE);

														two_cars_Type1_Name.setText(response.getCarsdetails().get(0).getType_name());
														two_cars_Type2_Name.setText(response.getCarsdetails().get(1).getType_name());
													}
													else if(response.getCarsdetails().size()==3)
													{
														one_car_layout.setVisibility(View.GONE);
														two_cars_layout.setVisibility(View.GONE);
														three_cars_layout.setVisibility(View.VISIBLE);
														four_cars_layout.setVisibility(View.GONE);

														three_cars_Type1_Name.setText(response.getCarsdetails().get(0).getType_name());
														three_cars_Type2_Name.setText(response.getCarsdetails().get(1).getType_name());
														three_cars_Type3_Name.setText(response.getCarsdetails().get(2).getType_name());
													}
													else if(response.getCarsdetails().size()==4)
													{
														one_car_layout.setVisibility(View.GONE);
														two_cars_layout.setVisibility(View.GONE);
														three_cars_layout.setVisibility(View.GONE);
														four_cars_layout.setVisibility(View.VISIBLE);

														four_cars_Type1_Name.setText(response.getCarsdetails().get(0).getType_name());
														four_cars_Type2_Name.setText(response.getCarsdetails().get(1).getType_name());
														four_cars_Type3_Name.setText(response.getCarsdetails().get(2).getType_name());
														four_cars_Type4_Name.setText(response.getCarsdetails().get(3).getType_name());
													}
													else if(response.getCarsdetails().size()>4)
													{
														one_car_layout.setVisibility(View.GONE);
														two_cars_layout.setVisibility(View.GONE);
														three_cars_layout.setVisibility(View.GONE);
														four_cars_layout.setVisibility(View.VISIBLE);

														four_cars_Type1_Name.setText(response.getCarsdetails().get(0).getType_name());
														four_cars_Type2_Name.setText(response.getCarsdetails().get(1).getType_name());
														four_cars_Type3_Name.setText(response.getCarsdetails().get(2).getType_name());
														four_cars_Type4_Name.setText(response.getCarsdetails().get(3).getType_name());
													}
												}
											}
											else
											{
												Utility.printLog("INSIDE DRIVERS AROUND YOU CAR TYPES NOT CHANGED");
											}
										}
										else
										{
											Utility.printLog("INSIDE DRIVERS AROUND YOU CAR TYPES NO CAR TYPES FOUND");

											response=null;//if no car types found, then make car details as null
											distance = getResources().getString(R.string.nocabs);

											//Txt_Pickup.setTextSize(20f);
                                            rate_unit.setVisibility(View.INVISIBLE);
											pickup_Distance.setVisibility(View.GONE);
											pubnubProgressDialog.setVisibility(View.INVISIBLE);
											Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
											Utility.printLog("INSIDE DRIVERS NOT FOUND 1");

											one_car_layout.setVisibility(View.GONE);
											two_cars_layout.setVisibility(View.GONE);
											three_cars_layout.setVisibility(View.GONE);
											four_cars_layout.setVisibility(View.GONE);

											googleMap.clear();
											type1_channels_list.clear();
											type1_markers.clear();
											type1NearestDrivers.clear();

											type2_channels_list.clear();
											type2_markers.clear();
											type2NearestDrivers.clear();

											type3_channels_list.clear();
											type3_markers.clear();
											type3NearestDrivers.clear();

											type4_channels_list.clear();
											type4_markers.clear();
											type4NearestDrivers.clear();

											//Toast.makeText(getActivity(),getResources().getString(R.string.not_available_area), Toast.LENGTH_LONG).show();
										}

										//moving inside if drivers are available


										if((message.toString()!=null) && (filterResponse.getMasArr()!=null && filterResponse.getMasArr().size()>0))

											//&& (filterResponse.getMasArr().get(0).getMas()!=null && filterResponse.getMasArr().get(0).getMas().size()>0))
										{
											ArrayList<String> driver_channels = new ArrayList<String>();


											// rahul : this code is to only show the distance of the closest car for a particular vehicle type

											if(filterResponse.getMasArr().size()>0)
											{
												for(int i=0;i<filterResponse.getMasArr().size();i++)
												{
													if(car_type_index==i)
													{
														if(filterResponse.getMasArr().get(i).getMas().size()>0)
														{
															pubnubProgressDialog.setVisibility(View.INVISIBLE);
															pickup_Distance.setVisibility(View.VISIBLE);
															//rate_unit.setVisibility(View.VISIBLE);
														}
														else
														{
															marker_map_on_the_way.clear();
															Utility.printLog("marker_map_on_the_way 1");
															marker_map_arrived.clear();
															googleMap.clear();

															distance = getResources().getString(R.string.nocabs);
															//Txt_Pickup.setTextSize(20f);
															pickup_Distance.setVisibility(View.GONE);
															rate_unit.setVisibility(View.GONE);
															pubnubProgressDialog.setVisibility(View.INVISIBLE);
															Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
														}
													}


													for(int j=0 ;j<filterResponse.getMasArr().get(i).getMas().size();j++)
													{

														driver_channels.add(filterResponse.getMasArr().get(i).getMas().get(j).getChn());
													}
												}
											}



											if(driver_channels!=null && driver_channels.size()>0)
											{

												/*Subscribe to new Channels obtained in result. 
												 * It will be creating the new marker on the map*/

												//Rotating for loop to remove the invisible driver from map and marker map

												boolean itemFound=false;

												ArrayList<String> channels_to_unsubscribe = new ArrayList<String>();

												for(int i=0;i<old_subscribed_driver_channels.size();i++)
												{
													for(int j=0;j<driver_channels.size();j++)
													{
														if(old_subscribed_driver_channels.get(i).equals(driver_channels.get(j)))
														{
															itemFound=true;
															break;
														}
														else
														{
															itemFound=false;
														}
													}

													if(!itemFound)
													{
														if(marker_map.containsKey(old_subscribed_driver_channels.get(i)))
														{
															marker_map.get(old_subscribed_driver_channels.get(i)).remove();
															marker_map.remove(old_subscribed_driver_channels.get(i));
															channels_to_unsubscribe.add(old_subscribed_driver_channels.get(i));
															if(type1_markers.containsKey(old_subscribed_driver_channels.get(i)))
															{
																type1_markers.remove(old_subscribed_driver_channels.get(i));

																type1_channels_list.remove(old_subscribed_driver_channels.get(i));
															}
															else if(type2_markers.containsKey(old_subscribed_driver_channels.get(i)))
															{
																type2_markers.remove(old_subscribed_driver_channels.get(i));
																type2_channels_list.remove(old_subscribed_driver_channels.get(i));
															}
															else if(type3_markers.containsKey(old_subscribed_driver_channels.get(i)))
															{
																type3_markers.remove(old_subscribed_driver_channels.get(i));
																type3_channels_list.remove(old_subscribed_driver_channels.get(i));
															}
															else if(type4_markers.containsKey(old_subscribed_driver_channels.get(i)))
															{
																type4_markers.remove(old_subscribed_driver_channels.get(i));
																type4_channels_list.remove(old_subscribed_driver_channels.get(i));
															}
														}
													}
												}

												//UnSubscribing the channels
												if(channels_to_unsubscribe.size()>0)
												{
													//Putting ArrayList data to String[]
													String[] unsubscribe_channels=new String[channels_to_unsubscribe.size()];
													unsubscribe_channels=channels_to_unsubscribe.toArray(unsubscribe_channels);

													Utility.printLog("abcdefgh unsubscribe_channels size="+unsubscribe_channels.length);
													//UnSubcribing
													new BackgroundUnSubscribeChannels().execute(unsubscribe_channels);
												}

												ArrayList<String> new_channels_to_subscribe = new ArrayList<String>();

												if(isAdded())
												{
													Utility.printLog("rahul StoreAllAvailableDrivers isAdded = "+isAdded());
													new_channels_to_subscribe = StoreAllAvailableDrivers(filterResponse);
												}
												else
												{
													Utility.printLog("StoreAllAvailableDrivers isAdded = "+isAdded());
												}

												//subcribing for new drivers 
												if(new_channels_to_subscribe.size()>0)
												{
													//Putting ArrayList data to String[]
													String[] new_channels=new String[new_channels_to_subscribe.size()];
													new_channels=new_channels_to_subscribe.toArray(new_channels);

													Utility.printLog("abcdefgh BackgroundSubscribeChannels size="+new_channels.length);
													for(int i=0;i<new_channels.length;i++)
													{
														Utility.printLog("abcdefgh BackgroundSubscribeChannels at "+i+"="+new_channels[i]);
													}
													//Subcribing
													new BackgroundSubscribeChannels().execute(new_channels);
												}

												//storing latest channels to old channels
												old_subscribed_driver_channels=driver_channels;
											}
											else
											{
												ArrayList<String> channels_to_unsubscribe = new ArrayList<String>();

												for(int i=0;i<old_subscribed_driver_channels.size();i++)
												{

													if(marker_map.containsKey(old_subscribed_driver_channels.get(i)))
													{
														marker_map.get(old_subscribed_driver_channels.get(i)).remove();
														marker_map.remove(old_subscribed_driver_channels.get(i));
														channels_to_unsubscribe.add(old_subscribed_driver_channels.get(i));
														if(type1_markers.containsKey(old_subscribed_driver_channels.get(i)))
														{
															type1_markers.remove(old_subscribed_driver_channels.get(i));

															type1_channels_list.remove(old_subscribed_driver_channels.get(i));
														}
														else if(type2_markers.containsKey(old_subscribed_driver_channels.get(i)))
														{
															type2_markers.remove(old_subscribed_driver_channels.get(i));
															type2_channels_list.remove(old_subscribed_driver_channels.get(i));
														}
														else if(type3_markers.containsKey(old_subscribed_driver_channels.get(i)))
														{
															type3_markers.remove(old_subscribed_driver_channels.get(i));
															type3_channels_list.remove(old_subscribed_driver_channels.get(i));
														}
														else if(type4_markers.containsKey(old_subscribed_driver_channels.get(i)))
														{
															type4_markers.remove(old_subscribed_driver_channels.get(i));
															type4_channels_list.remove(old_subscribed_driver_channels.get(i));
														}
													}


												}
												old_subscribed_driver_channels.clear();
												if(channels_to_unsubscribe.size()>0)
												{
													//Putting ArrayList data to String[]
													String[] unsubscribe_channels=new String[channels_to_unsubscribe.size()];
													unsubscribe_channels=channels_to_unsubscribe.toArray(unsubscribe_channels);

													Utility.printLog("abcdefgh unsubscribe_channels size="+unsubscribe_channels.length);
													//UnSubcribing
													new BackgroundUnSubscribeChannels().execute(unsubscribe_channels);
												}
												Utility.printLog("INSIDE DRIVERS AROUND YOU CAR TYPES NO CAR TYPES FOUND");

												distance =getResources().getString(R.string.nocabs);
												//Txt_Pickup.setTextSize(20f);
												pickup_Distance.setVisibility(View.GONE);
												rate_unit.setVisibility(View.GONE);
												pubnubProgressDialog.setVisibility(View.INVISIBLE);
												Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
												Utility.printLog("INSIDE DRIVERS NOT FOUND 78");

												googleMap.clear();
												type1_channels_list.clear();
												type1_markers.clear();
												type1NearestDrivers.clear();

												type2_channels_list.clear();
												type2_markers.clear();
												type2NearestDrivers.clear();

												type3_channels_list.clear();
												type3_markers.clear();
												type3NearestDrivers.clear();

												type4_channels_list.clear();
												type4_markers.clear();
												type4NearestDrivers.clear();
											}
										}
										else//clear the google map if there is no drivers to show in map
										{ 
											//marker_map.clear();
											marker_map_on_the_way.clear();
											Utility.printLog("marker_map_on_the_way 1");
											marker_map_arrived.clear();
											googleMap.clear();

											distance =getResources().getString(R.string.nocabs);
											//Txt_Pickup.setTextSize(20f);
											pickup_Distance.setVisibility(View.GONE);
											rate_unit.setVisibility(View.GONE);
											pubnubProgressDialog.setVisibility(View.INVISIBLE);
											Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
											Utility.printLog("INSIDE DRIVERS NOT FOUND 2");
										}
									}
									else if(filterResponse!=null && filterResponse.getA().equals("5") && filterResponse.getBid()!=null && filterResponse.getBid().equals(session.getBookingId())) //Driver cancelled the request
									{
										Utility.printLog("INSIDE DRIVER CANCELLED BOOKING message="+message.toString());
										Log.i("INSIDE DRIVER", "cancel booking " + message.toString());

										/*mPromoCode=null;
										promeCode.setText("");*/


										DriverCancelledAppointment();
									}
									else if(filterResponse!=null && filterResponse.getA().equals("6") && !session.isDriverOnWay() && 
											filterResponse.getBid()!=null && filterResponse.getBid().equals(session.getBookingId()))//Driver on the way
									{
										Utility.printLog("INSIDE DRIVER ON THE WAY: 6 message="+message.toString());



										session.setDriverOnWay(true);
										Utility.printLog("Wallah set as true Homepage 6");
										session.setDriverArrived(false);
										session.setTripBegin(false);
										session.setInvoiceRaised(false);

										setHasOptionsMenu(false);
										getCarDetails();
										if(session.getLat_DOW()==null || session.getLon_DOW()==null)
										{
											session.storeLat_DOW(filterResponse.getLt());
											session.storeLon_DOW(filterResponse.getLg());
											getAppointmentDetails_Volley(session.getAptDate());
										}
									}
									else if(filterResponse!=null && filterResponse.getA().equals("4") && session.isDriverOnWay())//Driver on the way
									{
										Utility.printLog("INSIDE DRIVER ON THE WAY: 4 message="+message.toString());
										eta_latitude=filterResponse.getLt();
										eta_longitude=filterResponse.getLg();
										getETAWithTimer();
										UpdateDriverLocation_DriverOnTheWay(filterResponse);
									}
									else if(filterResponse.getA().equals("7") && !session.isDriverOnArrived() && responsebid.equals(sessionbid))//Driver arrived   
										//filterResponse.getBid().equals(session.getBookingId()) //Roshani
									{
										Utility.printLog("INSIDE DRIVER REACHED: 7 message="+message.toString());
										session.setDriverOnWay(false);
										Utility.printLog("Wallah set as false Homepage 7");
										session.setDriverArrived(true);
										session.setTripBegin(false);
										session.setInvoiceRaised(false);

										session.storeLat_DOW(filterResponse.getLt());
										session.storeLon_DOW(filterResponse.getLg());

										//new CallGooglePlayServices().execute();
										setHasOptionsMenu(false);
										getCarDetails();
										getAppointmentDetails_Volley(session.getAptDate());
									}
									else if(filterResponse!=null && filterResponse.getA().equals("4") && session.isDriverOnArrived()  )
										//Driver arrived//Roshani

									{
										Utility.printLog("INSIDE DRIVER REACHED:4 message="+message.toString());

										eta_latitude=filterResponse.getLt();
										eta_longitude=filterResponse.getLg();

										UpdatDriverLocation_DriverArrived(filterResponse);
									}
									else if( "8".equals(filterResponse.getA())  && !session.isTripBegin() && responsebid.equals(sessionbid))
										//Driver Started journey // && int foo = Integer.parseInt("1234");filterResponse.getBid().equals(session.getBookingId())//&& session.isTripBegin() Roshani
									{
										//ctivity.invoice_driver_tip.setVisibility(View.VISIBLE);
										Utility.printLog("INSIDE DRIVER REACHED: 8 message="+message.toString());
										session.setDriverOnWay(false);
										Utility.printLog("Wallah set as false Homepage 8");
										session.setDriverArrived(false);
										session.setTripBegin(true);
										session.setInvoiceRaised(false);
										//new CallGooglePlayServices().execute();
										setHasOptionsMenu(false);
										getCarDetails();
										getAppointmentDetails_Volley(session.getAptDate());
									}
									else if(filterResponse!=null && filterResponse.getA().equals("4") && session.isTripBegin())//Driver Started journey
									{
										//ResideMenuActivity.invoice_driver_tip.setVisibility(View.VISIBLE);
										Utility.printLog("INSIDE DRIVER TripBegin:4 message="+message.toString());
										eta_latitude=filterResponse.getLt();
										eta_longitude=filterResponse.getLg();
										UpdateDriverLocation_JourneyStarted(filterResponse);
									}
									else if(filterResponse!=null && filterResponse.getA().equals("9") && !session.isInvoiceRaised() &&
											filterResponse.getBid()!=null && filterResponse.getBid().equals(session.getBookingId()))//invoice raised
									{
										Utility.printLog("onResume INSIDE DriverInvoiceRaised");
										eta_latitude=filterResponse.getLt();
										eta_longitude=filterResponse.getLg();
										AppointmentCompleted_InvoiceRaised(filterResponse);

										Utility.printLog("onResume INSIDE DriverInvoiceRaised");

										//AppointmentCompleted_InvoiceRaised(filterResponse);
									}
									else if(filterResponse!=null && filterResponse.getA().equals("4"))//updating drivers locations who are all around you
									{
										Utility.printLog("INSIDE DRIVER Update Locations:4 message="+message.toString());
										eta_latitude=filterResponse.getLt();
										eta_longitude=filterResponse.getLg();
										UpdateDriverLocations(filterResponse);

										Utility.printLog("INSIDE DRIVER Update Locations:4 message="+message.toString());
										//UpdateDriverLocations(filterResponse);
									}

								}
							});
					}
				}

				@Override
				public void errorCallback(String arg0, PubnubError arg1)
				{
					super.errorCallback(arg0, arg1);

					if(isAdded())
						getActivity().runOnUiThread(new Runnable() 
						{
							@Override
							public void run()
							{
								//if(pubnubProgressDialog.isShown())
								pubnubProgressDialog.setVisibility(View.INVISIBLE);
								pickup_Distance.setVisibility(View.VISIBLE);
								//rate_unit.setVisibility(View.VISIBLE);
							}
						});
					//Pubnub Change 17/5/2016
					LogFile.log("subscribe pubnub inside error MSG IS: "+arg1.toString());
					Utility.printLog("CONTROL INSIDE subscribe errorCallback");
					Utility.printLog("sss subscribe pubnub inside error MSG IS: "+arg1.toString());
				}
			});
		}
		catch(Exception e)
		{
			Utility.printLog("Exception in subscribe "+e);
		}
	}

	public void subscribeMultipleChannels(String[] channels)
	{ 
		// Add channels as an array
		try 
		{
			Utility.printLog("inside subscribeMultipleChannels channels size="+channels.length);
			//Pubnub Change 17/5/2016
			LogFile.log("inside subscribeMultipleChannels channels size="+channels.length);

			pubnub.subscribe(channels, new Callback()
			{
				@Override
				public void connectCallback(String channel, Object message)
				{
					Utility.printLog("SUBSCRIBE : CONNECT on channel:" + channel
							+ " : " + message.getClass() + " : "
							+ message.toString());

					//Pubnub Change 17/5/2016
					LogFile.log("SUBSCRIBE : CONNECT on channel:" + channel
							+ " : " + message.getClass() + " : "
							+ message.toString());

				}
				@Override
				public void disconnectCallback(String channel, Object message)
				{
					Utility.printLog("SUBSCRIBE : DISCONNECT on channel:" + channel
							+ " : " + message.getClass() + " : "
							+ message.toString());

					//Pubnub Change 17/5/2016
					LogFile.log("SUBSCRIBE : DISCONNECT on channel:" + channel
							+ " : " + message.getClass() + " : "
							+ message.toString());

				}
				public void reconnectCallback(String channel, Object message)
				{
					Utility.printLog("SUBSCRIBE : RECONNECT on channel:" + channel
							+ " : " + message.getClass() + " : "
							+ message.toString());

					//Pubnub Change 17/5/2016
					LogFile.log("SUBSCRIBE : RECONNECT on channel:" + channel
							+ " : " + message.getClass() + " : "
							+ message.toString());
				}
				@Override
				public void successCallback(String channel, final Object message) 
				{
					Utility.printLog("subscribeMultipleChannels success channel: " + channel + " getClass: "
							+ message.getClass() + "  message=" + message.toString());

					//Pubnub Change 17/5/2016
					LogFile.log("subscribeMultipleChannels success channel: " + channel + " getClass: "
							+ message.getClass() + "  message=" + message.toString());
					if(getActivity()!=null)
					{
						Utility.printLog("getActivity()!=null");

						getActivity().runOnUiThread(new Runnable()
						{
							@Override
							public void run() 
							{
								PubNubResponse filterResponse = null ;
								String responsebid="";//Roshani
								String sessionbid="";//Roshani
								Utility.printLog("subscribeMultipleChannels message.toString(): "+message.toString());
								if(message.toString()!=null) 
								{
									Gson gson = new Gson();
									filterResponse=gson.fromJson(message.toString(),PubNubResponse.class);
									if(responsebid!=null &&   responsebid.contains("BID"))
									{
										responsebid= responsebid.replace("BID :","");
									}
									Utility.printLog("subscribeMultipleChannels filterResponse: SUBSCRIBE'SSS : "+filterResponse.getA());
								}
							}
						});
					}
				}
				@Override
				public void errorCallback(String channel, PubnubError error) 
				{
					System.out.println("SUBSCRIBE : ERROR on channel " + channel
							+ " : " + error.toString());


					//Pubnub Change 17/5/2016
					LogFile.log("SUBSCRIBE : ERROR on channel " + channel
							+ " : " + error.toString());
				}
			});
		} 
		catch (PubnubException e)
		{
			System.out.println(e.toString());
		}
	}
	private void animateMarker(final Marker marker, final LatLng target) 
	{
		final long duration = 400;
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		Projection proj = googleMap.getProjection();
		Point startPoint = proj.toScreenLocation(marker.getPosition());
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final Interpolator interpolator = new LinearInterpolator();
		handler.post(new Runnable() 
		{
			@Override
			public void run() 
			{
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed / duration);
				double lng = t * target.longitude + (1 - t) * startLatLng.longitude;
				double lat = t * target.latitude + (1 - t) * startLatLng.latitude;
				marker.setRotation(bearingBetweenLatLngs(marker.getPosition(), target));
				marker.setPosition(new LatLng(lat, lng));
				if(t < 1.0) 
				{
					// Post again 16ms later.
					handler.postDelayed(this, 16);
				} 
				else
				{
					// animation ended
				}
			}
		});
		/*final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		final LatLng startLatLng = marker.getPosition();
		final long duration = 500;

		final Interpolator interpolator = new LinearInterpolator();

		handler.post(new Runnable() 
		{
			@Override
			public void run() 
			{
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed/ duration);

				double lng = t * location.getLongitude() + (1 - t) * startLatLng.longitude;
				double lat = t * location.getLatitude() + (1 - t) * startLatLng.latitude;
				//float rotation = (float) (t * location.getBearing() + (1 - t) * startRotation);

				marker.setPosition(new LatLng(lat, lng));
				// marker.setRotation(rotation);

				if(t < 1.0)
				{
					// Post again 16ms later.
					handler.postDelayed(this, 16);
				}
			}
		});*/
	}
	private Location convertLatLngToLocation(LatLng latLng)
	{
		Location location = new Location("someLoc");
		location.setLatitude(latLng.latitude);
		location.setLongitude(latLng.longitude);
		return location;
	}
	private float bearingBetweenLatLngs(LatLng beginLatLng,LatLng endLatLng)
	{
		Location beginLocation = convertLatLngToLocation(beginLatLng);
		Location endLocation = convertLatLngToLocation(endLatLng);
		return beginLocation.bearingTo(endLocation);
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		 roboto_condensed = Typeface.createFromAsset(getActivity().getAssets(),"fonts/BebasNeue.otf");

		Utility.printLog("CONTROL INSIDE onCreate");
		session = new SessionManager(mActivity);
		mDialog = Utility.GetProcessDialog(getActivity());
		// add PhoneStateListener
		PhoneCallListener phoneListener = new PhoneCallListener();
		TelephonyManager telephonyManager = (TelephonyManager) mActivity.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
		marker_map=new HashMap<String, Marker>();
		marker_map_on_the_way = new HashMap<String, Marker>();
		marker_map_arrived = new HashMap<String, Marker>();
		type1_markers = new HashMap<String, Location>();
		type2_markers = new HashMap<String, Location>();
		type3_markers = new HashMap<String, Location>();
		type4_markers = new HashMap<String, Location>();
		type1_channels_list = new ArrayList<String>();
		type2_channels_list = new ArrayList<String>();
		type3_channels_list = new ArrayList<String>();
		type4_channels_list = new ArrayList<String>();
		type1NearestDrivers = new ArrayList<String>();
		type2NearestDrivers = new ArrayList<String>();
		type3NearestDrivers = new ArrayList<String>();
		type4NearestDrivers = new ArrayList<String>();
		nearestDrivers= new ArrayList<String>();
		// ask Android for the GPS service
		locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			isGpsEnable = true;
		}
		// make a delegate to receive callbacks
		gpsListener = new MyGpsListener();
		// ask for updates on the GPS status
		locationManager.addGpsStatusListener(gpsListener);
		// ask for updates on the GPS location
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MINIMUM_UPDATE_TIME, MINIMUM_UPDATE_DISTANCE, gpsListener);
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,gpsListener);
			Location  location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if(location!=null)
			{
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				Utility.printLog("if locationManager latitude = "+latitude+" longitude = "+longitude);
			}
			else
			{
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,gpsListener);
				location =locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				if(location!=null)
				{
					latitude = location.getLatitude();
					longitude = location.getLongitude();
					Utility.printLog("else locationManager latitude = "+latitude+" longitude = "+longitude);
				}
			}
		}
		else
		{
			showGpsAlert();
		}
		filter = new IntentFilter();
		filter.addAction("com.threembed.driverapp.activity.push.popup");//com.threembed.driverapp.activity.push.popup
		receiver = new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{
				SessionManager session=new SessionManager(context);
				Utility.printLog("BroadcastReceiver pushwoosh Driver Arrived ONRECIVE");
				Utility.printLog(TAG, "INSIDE ONRECIVE ..............");
				Utility.printLog("ONRECIVE session.isdriverOnWay() "+session.isDriverOnWay());
				Utility.printLog("ONRECIVE session.isdriverOnArrived() "+session.isDriverOnArrived());
				Utility.printLog("ONRECIVE session.isInvoiceRaised() "+session.isInvoiceRaised());
				Utility.printLog("ONRECIVE session.isTripBegin() "+session.isTripBegin());
				Utility.printLog("ONRECIVE session.isDriverCancelledApt() "+session.isDriverCancelledApt());
				if(session.isDriverOnWay())
				{
					Utility.printLog("ONRECIVE INSIDE ON_THE_WAY");
					//stop the timer to get the current address if any booking is going on
					if(myTimer!=null)
					{
						myTimer.cancel();
						myTimer=null;
					}
					//marker_map.clear();
					marker_map_on_the_way.clear();
					Utility.printLog("marker_map_on_the_way 3");
					marker_map_arrived.clear();
					googleMap.clear();
					//new CallGooglePlayServices().execute();
					getETAWithTimer();
					setHasOptionsMenu(false);
					getCarDetails();
					getAppointmentDetails_Volley(session.getAptDate());

					return;
				}
				else if(session.isDriverOnArrived() && !session.getBookingId().equals("0"))
				{
					Utility.printLog("ONRECIVE INSIDE Driver Arrived");
					//stop the timer to get the current address if any booking is going on
					if(myTimer!=null)
					{
						myTimer.cancel();
						myTimer=null;
					}
					googleMap.clear();
					//marker_map.clear();
					marker_map_on_the_way.clear();
					Utility.printLog("marker_map_on_the_way 4");
					marker_map_arrived.clear();
					//new CallGooglePlayServices().execute();

					setHasOptionsMenu(true);
					getCarDetails();
					getAppointmentDetails_Volley(session.getAptDate());
					return;
				}
				else if(session.isTripBegin() && !session.getBookingId().equals("0"))
				{
					Utility.printLog("ONRECIVE INSIDE Driver TripBegin");
					//stop the timer to get the current address if any booking is going on
					if(myTimer!=null)
					{
						myTimer.cancel();
						myTimer=null;
					}

					/*if(isAdded())
						{
							Driver_on_the_way_txt.setText("DRIVER REACHED: "+session.getBookingId());
							Driver_Arrow.setVisibility(View.INVISIBLE);
						}
						else*/
					{
						googleMap.clear();
						//marker_map.clear();
						marker_map_on_the_way.clear();
						Utility.printLog("marker_map_on_the_way 6");
						marker_map_arrived.clear();
						//new CallGooglePlayServices().execute();
						setHasOptionsMenu(true);
						getCarDetails();
						getAppointmentDetails_Volley(session.getAptDate());
					}
					return;
				}

				else if (!session.isLoggedIn())
				{
					Utility.printLog("out side 12"+session.getRejectedFromAdmin());
					if((session.getRejectedFromAdmin()).equals("true"))
					{

						Utility.printLog("out side 12 inside");
						session=new SessionManager(getActivity());
						session.setIsUserRejectedFromAdmin("false");
						session.setIsLogin(false);


						Intent intent2=new Intent(getActivity(), MainActivity.class);

						intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent2);
						getActivity().finish();
						Toast.makeText(getActivity(), getResources().getString(R.string.profileLogout), Toast.LENGTH_LONG).show();

					}


				}

				//else if(session.isInvoiceRaised() && !session.isBookingCancelled() && !session.getBookingId().equals("0"))
				else if(session.isInvoiceRaised() && !session.getBookingId().equals("0"))
				{   
					Utility.printLog("ONRECIVE INSIDE Driver InvoiceRaised");

					Utility.printLog("mActivity="+mActivity+" "+isAdded());
					if(mActivity!=null)
					{
						new BackgroundUnSubscribeAll().execute();
						session.storeBookingId("0");
						Utility.printLog("mActivity if="+mActivity);
						Intent intent1 = new Intent(mActivity,InvoiceActivity.class);
						if (tipResponse != null && "0".equals(tipResponse.getErrFlag())) {
							intent1.putExtra("Tip", tipResponse);

						}
						mActivity.startActivity(intent1);
					}
					else
					{
						new BackgroundUnSubscribeAll().execute();
						session.storeBookingId("0");
						mActivity=(ResideMenuActivity)getActivity();
						Utility.printLog("mActivity else="+mActivity);
						Intent intent1 = new Intent(mActivity,InvoiceActivity.class);	
						if (tipResponse != null && "0".equals(tipResponse.getErrFlag())) {
							intent1.putExtra("Tip", tipResponse);

						}
						mActivity.startActivity(intent1);
					}
					return;
				}
				else if(session.isDriverCancelledApt())
				{
					session.setDriverOnWay(false);
					session.setBookingCancelled(true);
					Utility.printLog("Wallah set as false Homepage cancel 2");
					session.setDriverArrived(false);
					session.setTripBegin(false);
					session.setInvoiceRaised(false);
					session.storeAptDate(null);
					session.storeBookingId("0");
					/*mPromoCode = null;
					promeCode.setText("");*/
					//relative_now_later_status.setVisibility(View.VISIBLE);

					Toast.makeText(mActivity, "Driver cancelled the booking", Toast.LENGTH_SHORT).show();
					Intent i = new Intent(mActivity, ResideMenuActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					mActivity.startActivity(i);
					mActivity.overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);

					/*session.setDriverCancelledApt(false);
					session.setDriverOnWay(false);
					Utility.printLog("Wallah DriverCancelledApt");
					session.setDriverArrived(false);
					session.setInvoiceRaised(false);
					session.setTripBegin(false);

					//marker_map.clear();
					marker_map_on_the_way.clear();
					Utility.printLog("marker_map_on_the_way 5");
					marker_map_arrived.clear();
					googleMap.clear();

					ResideMenuActivity.main_frame_layout.setVisibility(View.VISIBLE);

					isSetDropoffLocation=false;
					isBackPressed=false;
					isFareQuotePressed=false;
					to_latitude=null;
					to_longitude=null;

					Driver_Confirmation.setVisibility(View.INVISIBLE);
					show_address_relative.setVisibility(View.VISIBLE);
					pickup.setVisibility(View.VISIBLE);
					Txt_Pickup.setVisibility(View.VISIBLE);
					Mid_Pointer.setVisibility(View.VISIBLE);
					Driver_on_the_way_txt.setVisibility(View.INVISIBLE);
					Rl_distance_time.setVisibility(View.INVISIBLE);

					session.storeAptDate(null);
					session.storeBookingId("0");
					now_later_status.setVisibility(View.VISIBLE);

					Intent i = new Intent(mActivity, ResideMenuActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					mActivity.startActivity(i);
					mActivity.overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);*/

					//Utility.ShowAlert(getResources().getString(R.string.driver_cancelled_booking), getActivity());

					/*AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
					alertDialog.setTitle(getResources().getString(R.string.alert));
					alertDialog.setMessage(getResources().getString(R.string.driver_cancelled_booking));
					alertDialog.setPositiveButton(getActivity().getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog,int which) 
						{
							dialog.dismiss();
							Intent i = new Intent(mActivity, ResideMenuActivity.class);
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							mActivity.startActivity(i);
							mActivity.overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
						}
					});*/
				}
			}
		};
		//getActivity().registerReceiver(receiver, filter);
	}

	private void getCarDetails()
	{
		RequestQueue volleyRequest = Volley.newRequestQueue(mActivity);

		StringRequest myReq = new StringRequest(Request.Method.POST,VariableConstants.BASE_URL+"getMasterCarDetails",
				new Listener<String>()
				{
			@Override
			public void onResponse(String response) 
			{
				carDetailsResponse = response;
				Utility.printLog("Success of getting getCarDetails"+response);
				getCarInfo();
			}
				}, 	new ErrorListener(){

					@Override
					public void onErrorResponse(VolleyError error)
					{
						Utility.printLog("Error for volley");
					}
				}){  
			protected HashMap<String,String> getParams() throws com.android.volley.AuthFailureError
			{  
				HashMap<String,String> kvPair = new HashMap<String, String>(); 
				SessionManager session=new SessionManager(mActivity);
				Utility utility=new Utility();
				String currenttime=utility.getCurrentGmtTime();

				kvPair.put("ent_sess_token",session.getSessionToken());
				kvPair.put("ent_dev_id",session.getDeviceId());
				kvPair.put("ent_email",session.getDriverEmail());
				kvPair.put("ent_date_time",currenttime);

				return kvPair;  
			};  

		};

		volleyRequest.add(myReq);
	}
	/**
	 * Handling CarDetails response 
	 */
	private void getCarInfo()
	{
		try
		{
			JSONObject jsnResponse = new JSONObject(carDetailsResponse);
			String jsonErrorParsing = jsnResponse.getString("errFlag");

			Utility.printLog("jsonErrorParsing is ---> "+jsonErrorParsing);
			parseCarDetails();

			if(getCarDetails!=null)
			{
				if(getCarDetails.getErrFlag().equals("0"))
				{
					SessionManager session =new SessionManager(mActivity);
					Utility.printLog("bbbb getPlateNo="+getCarDetails.getPlateNo());
					session.storePlateNO(getCarDetails.getPlateNo());
					session.storeCarModel(getCarDetails.getModel());
					Utility.printLog("bbbb getPlateNo="+session.getPlateNO());
					Driver_Car_Type.setText(getCarDetails.getModel());
					Driver_Car_Num.setText(getCarDetails.getPlateNo());
				}
				else
				{
					SessionManager session =new SessionManager(mActivity);
					session.storePlateNO("--");
					session.storeCarModel("--");
				}
			}
		}
		catch(JSONException e)
		{
			Utility.printLog("exp "+e);
			e.printStackTrace();
			Utility.ShowAlert(getResources().getString(R.string.server_error), mActivity);
		}
	}
	//parsing CarDetails response
	private void parseCarDetails()
	{
		Utility.printLog("getFare parseResponse  " + carDetailsResponse);
		Gson gson = new Gson();
		getCarDetails = gson.fromJson(carDetailsResponse, GetCarDetails.class);
	}


	/*private final class MyTouchListener implements OnTouchListener 
	{
	    public boolean onTouch(View view, MotionEvent motionEvent) 
	    {
	      if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) 
	      {
	        ClipData data = ClipData.newPlainText("", "");
	        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
	        view.startDrag(data, shadowBuilder, view, 0);
	       // view.setVisibility(View.INVISIBLE);
	        return true;
	      } else {
	        return false;
	      }
	    }
	  }

	  class MyDragListener implements OnDragListener {
	   // Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
	  //  Drawable normalShape = getResources().getDrawable(R.drawable.shape);

	    @Override
	    public boolean onDrag(View v, DragEvent event) 
	    {
	      switch (event.getAction()) 
	      {
	      case DragEvent.ACTION_DRAG_STARTED:
	    	  Utility.printLog("setOnDragListener ACTION_DRAG_STARTED getX="+event.getX()+" getY="+event.getY());
	        break;
	      case DragEvent.ACTION_DRAG_ENTERED:
	    	  Utility.printLog("setOnDragListener ACTION_DRAG_ENTERED getX="+event.getX()+" getY="+event.getY());
	        break;
	      case DragEvent.ACTION_DRAG_EXITED:
	    	  Utility.printLog("setOnDragListener ACTION_DRAG_EXITED getX="+event.getX()+" getY="+event.getY());
	        break;
	      case DragEvent.ACTION_DROP:
	    	  Utility.printLog("setOnDragListener ACTION_DROP getX="+event.getX()+" getY="+event.getY());
	        // Dropped, reassign View to ViewGroup
	        View view = (View) event.getLocalState();
	        ViewGroup owner = (ViewGroup) view.getParent();
	        owner.removeView(view);
	        LinearLayout container = (LinearLayout) v;
	        container.addView(view);
	       // view.setVisibility(View.VISIBLE);
	        break;
	      case DragEvent.ACTION_DRAG_ENDED:
	    	  Utility.printLog("setOnDragListener ACTION_DRAG_ENDED getX="+event.getX()+" getY="+event.getY());
	      default:
	        break;
	      }
	      return true;
	    }
	  }*/

	/**
	 * Initializing all variables in this view
	 * @param v
	 */
	private void initializeVariables(View v)
	{
		Utility.printLog(TAG, "onCreateView  inside initializeVariables");
		new_dropoff_location_address=(TextView) v.findViewById(R.id.new_dropoff_location_address);
		new_add_drop_off_location=(Button) v.findViewById(R.id.new_add_drop_off_location);
		new_img_dropoff=(ImageButton) v.findViewById(R.id.new_img_dropoff);
		new_relative_dropoff_location=(RelativeLayout) v.findViewById(R.id.new_relative_dropoff_location);
		myPosition=(ImageButton)v.findViewById(R.id.go_to_current_position);
		pickup=(RelativeLayout)v.findViewById(R.id.relative_center);
		current_address=(TextView)v.findViewById(R.id.show_addr_text_view);
		Dropoff_Location_Address=(TextView)v.findViewById(R.id.dropoff_location_address);
		addressSearchButton=(ImageButton)v.findViewById(R.id.address_search_button);
		//setLocationArrow=(ImageView)v.findViewById(R.id.mid_pointer_arrow);
		show_address_relative=(RelativeLayout)v.findViewById(R.id.show_address_relative);
		relativePickupLocation=(RelativeLayout)v.findViewById(R.id.relative_pickup_location);
		Relative_Pickup_Navigation=(RelativeLayout)v.findViewById(R.id.relative_pickup_navigation);
		Relative_Dropoff_Location=(RelativeLayout)v.findViewById(R.id.relative_dropoff_location);
		Relative_Card_Info=(RelativeLayout)v.findViewById(R.id.relative_card_info);
		Relative_Payment_Info= (RelativeLayout) v.findViewById(R.id.relative_payment_info);
		pickupLocationAddress=(TextView)v.findViewById(R.id.pickup_location_address);
		Request_Pick_up_here = (Button)v.findViewById(R.id.request_pick_up_here);
		Fare_Quote = (Button)v.findViewById(R.id.fare_quote);
		//promeCode = (Button)v.findViewById(R.id.promo_code);
		Card_Info = (TextView)v.findViewById(R.id.card_info);
		AddLocation = (Button)v.findViewById(R.id.add_drop_off_location);
		Txt_Pickup = (TextView)v.findViewById(R.id.txt_pickup);
		Btn_Back = (ImageButton)v.findViewById(R.id.btn_back);
		cancel= (TextView) v.findViewById(R.id.txt_cancel);
		txt_roadyo=(TextView) v.findViewById(R.id.txt_roadyo);
		Card_Image = (ImageView)v.findViewById(R.id.card_image);
		RL_homepage = (RelativeLayout)v.findViewById(R.id.rl_homepage);
		Driver_Confirmation = (RelativeLayout)v.findViewById(R.id.driver_confirmation);
		Driver_Rating = (RatingBar)v.findViewById(R.id.driver_rating);
		Img_Map = (ImageButton)v.findViewById(R.id.img_map);
		Img_Dropoff= (ImageButton)v.findViewById(R.id.img_dropoff);
		Driver_Profile_Pic = (ImageView)v.findViewById(R.id.driver_profile_pic);
		Driver_Name = (TextView)v.findViewById(R.id.driver_name);
		Driver_on_the_way_txt = (Button)v.findViewById(R.id.driver_on_the_way);
		Mid_Pointer = (ImageButton)v.findViewById(R.id.mid_pointer);
		//Driver_Arrow = (Button)v.findViewById(R.id.driver_arrow);
		//Rl_distance_time = (RelativeLayout)v.findViewById(R.id.rl_distance_time);
		Driver_Distance = (TextView)v.findViewById(R.id.driver_distance);
		Driver_Time = (TextView)v.findViewById(R.id.driver_time);
		Driver_Car_Type = (TextView)v.findViewById(R.id.driver_car_type);
		Driver_Car_Num = (TextView)v.findViewById(R.id.driver_car_plate_no);
		pickup_Distance = (TextView)v.findViewById(R.id.txt_pickup_distance);
		pubnubProgressDialog = (ProgressBarCircularIndetermininate)v.findViewById(R.id.progressBar);
		now_button = (RelativeLayout)v.findViewById(R.id.now_button);
		later_button = (RelativeLayout)v.findViewById(R.id.later_button);
		//booked_now_later=(TextView)v.findViewById(R.id.booking_for);
		//relative_now_later_status =(RelativeLayout)v.findViewById(R.id.relative_now_later_status);
		all_types_layout=(RelativeLayout)v.findViewById(R.id.relative_all_car_types);
		now_later_layout=(LinearLayout)v.findViewById(R.id.now_later_layout);
		choose_payment_layout=(RelativeLayout)v.findViewById(R.id.choose_payment_screen);
		pay_cash=(Button)v.findViewById(R.id.payment_cash);
		pay_card=(Button)v.findViewById(R.id.payment_card);
		pay_cancel=(Button)v.findViewById(R.id.payment_cancel);
		//=======================My change==========================
		paymenttype=(Button)v.findViewById(R.id.payment_type);
		payment_done=(Button)v.findViewById(R.id.payment_done);
		req_lay= (RelativeLayout) v.findViewById(R.id.req_lay);
		car_color= (TextView) v.findViewById(R.id.car_color);
		seater= (TextView) v.findViewById(R.id.seater);
		relative_booking= (RelativeLayout) v.findViewById(R.id.relative_booking);
		booking_id= (TextView) v.findViewById(R.id.booking_id);
		logo= (ImageView) getActivity().findViewById(R.id.logo);
		pageno= (TextView) getActivity().findViewById(R.id.current_page_name);


		//=======================My change==========================
		image_home= (ImageView) v.findViewById(R.id.image_home);
		farePromoLayouy=(RelativeLayout)v.findViewById(R.id.fare_promo_layouy);
		rate_unit=(TextView) v.findViewById(R.id.rate_unit);
		//driver_parent=(RelativeLayout)v.findViewById(R.id.driver_parent);
		share_eta=(TextView)v.findViewById(R.id.share_eta);
		contact_driver=(TextView)v.findViewById(R.id.contact_driver);
		cancel_trip=(TextView)v.findViewById(R.id.cancel_trip);

		carlatrimage = (ImageView) v.findViewById(R.id.carlatrimage);
		carnowimage = (ImageView) v.findViewById(R.id.carnowimage);
		Appointment_location=(TextView)v.findViewById(R.id.Appointment_location);
		bookltrtxt=(TextView)v.findViewById(R.id.bookltrtxt);
		booknwtxt=(TextView)v.findViewById(R.id.booknwtxt);


		share_eta.setOnClickListener(this);
		contact_driver.setOnClickListener(this);
		cancel_trip.setOnClickListener(this);
		farePromoLayouy.setOnClickListener(this);
		//driver_parent.setOnClickListener(this);

		/*now_button.setBackgroundResource(R.color.lightgrey1);
		later_button.setBackgroundResource(R.color.white);*/

		/*now_button.setTextColor(getResources().getColor(R.color.dark_orange));
		later_button.setTextColor(getResources().getColor(R.color.white));*/
		dblArray = Scaler.getScalingFactor(getActivity());

		one_car_Type_Name = (TextView)v.findViewById(R.id.one_car_type_name);
		two_cars_Type1_Name = (TextView)v.findViewById(R.id.two_car_type1_name);
		two_cars_Type2_Name = (TextView)v.findViewById(R.id.two_car_type2_name);
		three_cars_Type1_Name = (TextView)v.findViewById(R.id.three_car_type1_name);
		three_cars_Type2_Name = (TextView)v.findViewById(R.id.three_car_type2_name);
		three_cars_Type3_Name = (TextView)v.findViewById(R.id.three_car_type3_name);
		four_cars_Type1_Name = (TextView)v.findViewById(R.id.four_car_type1_name);
		four_cars_Type2_Name = (TextView)v.findViewById(R.id.four_car_type2_name);
		four_cars_Type3_Name = (TextView)v.findViewById(R.id.four_car_type3_name);
		four_cars_Type4_Name = (TextView)v.findViewById(R.id.four_car_type4_name);

		/*normal_view = (ImageButton)v.findViewById(R.id.map_normal_view);
		hybrid_view = (ImageButton)v.findViewById(R.id.map_hybrid_view);*/
       
		new_add_drop_off_location.setOnClickListener(this);
		pay_cash.setOnClickListener(this);
		pay_card.setOnClickListener(this);
		pay_cancel.setOnClickListener(this);
		now_button.setOnClickListener(this);
		later_button.setOnClickListener(this);
		myPosition.setOnClickListener(this);
		pickup.setOnClickListener(this);
		addressSearchButton.setOnClickListener(this);
		AddLocation.setOnClickListener(this);
		Card_Info.setOnClickListener(this);
		Btn_Back.setOnClickListener(this);
		cancel.setOnClickListener(this);
		Fare_Quote.setOnClickListener(this);
		//promeCode.setOnClickListener(this);
		Request_Pick_up_here.setOnClickListener(this);
		Relative_Card_Info.setOnClickListener(this);
		Relative_Payment_Info.setOnClickListener(this);
		//RL_homepage.setOnClickListener(this);
		relativePickupLocation.setOnClickListener(this);
		Relative_Dropoff_Location.setOnClickListener(this);
		Img_Map.setOnClickListener(this);
		Img_Dropoff.setOnClickListener(this);
		//Driver_Arrow.setOnClickListener(this);
		/*normal_view.setOnClickListener(this);
		hybrid_view.setOnClickListener(this);*/
		choose_payment_layout.setOnClickListener(this);
		new_img_dropoff.setOnClickListener(this);
		new_relative_dropoff_location.setOnClickListener(this);
		Typeface roboto_regular = Typeface.createFromAsset(getActivity().getAssets(),"fonts/BebasNeue.otf");
		Typeface roboto_condensed = Typeface.createFromAsset(getActivity().getAssets(),"fonts/BebasNeue.otf");

		Driver_Name.setTypeface(roboto_condensed);
		Driver_on_the_way_txt.setTypeface(roboto_condensed);
		booking_id.setTypeface(roboto_condensed);
		Driver_Car_Type.setTypeface(roboto_condensed);
		Driver_Car_Num.setTypeface(roboto_condensed);
        pageno.setTypeface(roboto_condensed);
		//========================My Change 15 April==================================
		payment_done.setOnClickListener(this);

		current_address.setTypeface(roboto_condensed);
		new_dropoff_location_address.setTypeface(roboto_condensed);
		new_add_drop_off_location.setTypeface(roboto_condensed);
		Dropoff_Location_Address.setTypeface(roboto_condensed);
		pickupLocationAddress.setTypeface(roboto_condensed);
		Request_Pick_up_here.setTypeface(roboto_condensed);
		Fare_Quote.setTypeface(roboto_condensed);
		Card_Info.setTypeface(roboto_condensed);
		AddLocation.setTypeface(roboto_condensed);
		Txt_Pickup.setTypeface(roboto_condensed);
		Driver_Distance.setTypeface(roboto_condensed);
		pickup_Distance.setTypeface(roboto_condensed);
		share_eta.setTypeface(roboto_condensed);
		//pay_card.setTypeface(roboto_condensed);
		//pay_cash.setTypeface(roboto_condensed);
		pay_cancel.setTypeface(roboto_condensed);
		rate_unit.setTypeface(roboto_condensed);
		contact_driver.setTypeface(roboto_condensed);
		cancel_trip.setTypeface(roboto_condensed);
		one_car_Type_Name.setTypeface(roboto_condensed);
		two_cars_Type1_Name.setTypeface(roboto_condensed);
		two_cars_Type2_Name.setTypeface(roboto_condensed);
		three_cars_Type1_Name.setTypeface(roboto_condensed);
		three_cars_Type2_Name.setTypeface(roboto_condensed);
		three_cars_Type3_Name.setTypeface(roboto_condensed);
		four_cars_Type1_Name.setTypeface(roboto_condensed);
		four_cars_Type2_Name.setTypeface(roboto_condensed);
		four_cars_Type3_Name.setTypeface(roboto_condensed);
		four_cars_Type4_Name.setTypeface(roboto_condensed);
		Appointment_location.setTypeface(roboto_condensed);
		bookltrtxt.setTypeface(roboto_condensed);
		booknwtxt.setTypeface(roboto_condensed);
       cancel.setTypeface(roboto_condensed);
		txt_roadyo.setTypeface(roboto_condensed);
		paymenttype.setTypeface(roboto_condensed);
		payment_done.setTypeface(roboto_condensed);
		car_color.setTypeface(roboto_condensed);
		seater.setTypeface(roboto_condensed);
		Driver_Time.setTypeface(roboto_condensed);

		pageno.setVisibility(View.INVISIBLE);
		logo.setVisibility(View.VISIBLE);

		//=======================My Change 15 April==================================

		payment_type="2";
		one_car_layout = (RelativeLayout)v.findViewById(R.id.one_car_layout);
		one_car_button = (ImageButton)v.findViewById(R.id.one_car_iv_logo);

		one_car_button.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				car_type_index = 0;
				if(response.getCarsdetails()!=null && response.getCarsdetails().size()>0)
				{
					car_name = response.getCarsdetails().get(0).getType_name();
					//================My Change===============
					car_size=response.getCarsdetails().get(0).getMax_size();
					//================My Change===============
					Intent intent = new Intent(mActivity,HomePagePopUp.class);
					intent.putExtra("chooser",car_type_index);
					intent.putExtra("DISTANCE", distance);
					intent.putExtra("BASEFARE0",baseFare0);
					intent.putExtra("PRICEPERMINUTE0", pricePerMin0);
					intent.putExtra("PRICEPERKM0", pricePerKm0);
					intent.putExtra("MINFARE0", minFare0);
					intent.putExtra("MAXSIZE0", maxSize0);
					intent.putExtra("BASEFARE1",baseFare1);
					intent.putExtra("PRICEPERMINUTE1", pricePerMin1);
					intent.putExtra("PRICEPERKM1", pricePerKm1);
					intent.putExtra("MINFARE1", minFare1);
					intent.putExtra("MAXSIZE1", maxSize1);
					intent.putExtra("BASEFARE2",baseFare2);
					intent.putExtra("PRICEPERMINUTE2", pricePerMin2);
					intent.putExtra("PRICEPERKM2", pricePerKm2);
					intent.putExtra("MINFARE2", minFare2);
					intent.putExtra("MAXSIZE2", maxSize2);
					intent.putExtra("BASEFARE3",baseFare3);
					intent.putExtra("PRICEPERMINUTE3", pricePerMin3);
					intent.putExtra("PRICEPERKM3", pricePerKm3);
					intent.putExtra("MINFARE3", minFare3);
					intent.putExtra("MAXSIZE3", maxSize3);
					mActivity.startActivityForResult(intent, 2);
				}
			}
		});

		height=Utility.getHeight(getActivity());
		width=Utility.getWidth(getActivity());
		Utility.printLog("width="+width+"height="+height);

		two_cars_layout = (RelativeLayout)v.findViewById(R.id.two_cars_layout);
		two_cars_button1=(ImageButton)v.findViewById(R.id.two_cars_button1);
		image=(ImageView)v.findViewById(R.id.two_cars_iv_logo);
		two_cars_button2=(ImageButton)v.findViewById(R.id.two_cars_button2);


		//View viewLine  = (View)v.findViewById(R.id.vDivider);

		/*
		RelativeLayout.LayoutParams layoutParams =
				new RelativeLayout.LayoutParams(image.getLayoutParams());

		layoutParams.leftMargin=30;
		image.setLayoutParams(layoutParams);


		RelativeLayout.LayoutParams layoutParams1 =
				new RelativeLayout.LayoutParams(two_cars_button1.getLayoutParams());

		layoutParams1.leftMargin=30;
		two_cars_button1.setLayoutParams(layoutParams1);

		RelativeLayout.LayoutParams layoutParams2 =
				new RelativeLayout.LayoutParams(two_cars_button2.getLayoutParams());




		layoutParams2.leftMargin=(width)-200; 
		two_cars_button2.setLayoutParams(layoutParams2);


		RelativeLayout.LayoutParams llParams = new RelativeLayout.LayoutParams(viewLine.getLayoutParams());

		llParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		llParams.topMargin = 5;
		llParams.addRule(RelativeLayout.RIGHT_OF, R.id.two_cars_button1);
		//llParams.addRule(RelativeLayout.LEFT_OF, R.id.two_cars_button2);
		llParams.addRule(RelativeLayout.CENTER_VERTICAL);


		viewLine.setLayoutParams(llParams);*/

		//two_cars_Type2_Name.setLayoutParams(layoutParams2);

		/*RelativeLayout relative_path = new RelativeLayout(getActivity());

		relative_path.setBackgroundResource(R.drawable.home_carinfo_timeslider);

		RelativeLayout.LayoutParams layoutParams_path = 
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

		layoutParams_path.addRule(RelativeLayout.LEFT_OF, R.id.two_cars_button2);
		layoutParams_path.addRule(RelativeLayout.RIGHT_OF, R.id.two_cars_button1);

		relative_path.setLayoutParams(layoutParams_path);*/

		image.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Utility.printLog("AddScrollView car id index="+(0));

				Intent intent = new Intent(mActivity,HomePagePopUp.class);
				intent.putExtra("chooser",car_type_index);
				intent.putExtra("DISTANCE", distance);
				intent.putExtra("BASEFARE0",baseFare0);
				intent.putExtra("PRICEPERMINUTE0", pricePerMin0);
				intent.putExtra("PRICEPERKM0", pricePerKm0);
				intent.putExtra("MINFARE0", minFare0);
				intent.putExtra("MAXSIZE0", maxSize0);
				intent.putExtra("BASEFARE1",baseFare1);
				intent.putExtra("PRICEPERMINUTE1", pricePerMin1);
				intent.putExtra("PRICEPERKM1", pricePerKm1);
				intent.putExtra("MINFARE1", minFare1);
				intent.putExtra("MAXSIZE1", maxSize1);
				intent.putExtra("BASEFARE2",baseFare2);
				intent.putExtra("PRICEPERMINUTE2", pricePerMin2);
				intent.putExtra("PRICEPERKM2", pricePerKm2);
				intent.putExtra("MINFARE2", minFare2);
				intent.putExtra("MAXSIZE2", maxSize2);
				intent.putExtra("BASEFARE3",baseFare3);
				intent.putExtra("PRICEPERMINUTE3", pricePerMin3);
				intent.putExtra("PRICEPERKM3", pricePerKm3);
				intent.putExtra("MINFARE3", minFare3);
				intent.putExtra("MAXSIZE3", maxSize3);
				mActivity.startActivityForResult(intent, 2);
			}
		});

		two_cars_button1.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(car_type_index!=0)
				{


					image.setBackgroundResource(R.drawable.home_carinfo_caricon_blue_1);
					//=================MyChange=====================
					//image.setBackgroundResource(R.drawable.car);
					//=================MyChange=====================
					two_cars_button1.setVisibility(View.INVISIBLE);
					two_cars_button2.setVisibility(View.VISIBLE);
					ObjectAnimator anim = ObjectAnimator.ofFloat(image, "translationX", (getRelativeLeft(two_cars_button2)-(2*two_cars_button2.getMeasuredWidth())),(getRelativeLeft(two_cars_button1)-(2*two_cars_button1.getMeasuredWidth())));
					anim.start();
					current_widht=(int)two_cars_button2.getX();
					car_type_index=0;

					isType1MarkersPloted = true;
					isType2MarkersPloted = false;



					//marker_map.clear();
					marker_map_on_the_way.clear();
					Utility.printLog("marker_map_on_the_way 8");
					marker_map_arrived.clear();
					googleMap.clear();


					if(Type1Distance!=null && !Type1Distance.isEmpty())
					{
						pickup_Distance.setVisibility(View.VISIBLE);
						rate_unit.setVisibility(View.VISIBLE);
						pickup_Distance.setText(Type1Distance);
						Utility.printLog("pickup_Distance 1");
					}
					else
					{
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);
					}

					/*pubnubProgressDialog.setClickable(false);
					pubnubProgressDialog.setVisibility(View.VISIBLE);
					pickup_Distance.setVisibility(View.INVISIBLE);*/

					if(type1_markers!=null && type1_channels_list!=null && type1_markers.size()>0 && type1_channels_list.size()>0)
					{
						//Txt_Pickup.setTextSize(18f);
						pickup_Distance.setVisibility(View.VISIBLE);
						//rate_unit.setVisibility(View.VISIBLE);
						Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));

						for(int i=0;i<type1_markers.size();i++)
						{
							if(type1_markers.containsKey(type1_channels_list.get(i)))
							{
								Location location = type1_markers.get(type1_channels_list.get(i));

								LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
							/*	drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon_blue)));
*/
								//=============================================My change================================
								drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
								//=============================================My change================================
								marker_map.put(type1_channels_list.get(i), drivermarker);
							}
							else
							{
								type1_channels_list.remove(i);
							}
						}
					}
					else
					{
						distance = getResources().getString(R.string.nocabs);
						//Txt_Pickup.setTextSize(20f);
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);
						Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
						Utility.printLog("INSIDE DRIVERS NOT FOUND 3");
					}

					if(response.getCarsdetails()!=null &&  response.getCarsdetails().size()>0)
					{
						Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
						car_name = response.getCarsdetails().get(car_type_index).getType_name();
						//================My Change===============
						car_size=response.getCarsdetails().get(car_type_index).getMax_size();
						//================My Change===============
					}				
					Utility.printLog("Car_Type_Id in onresume="+Car_Type_Id);
					startPublishingWithTimer();
				}
			}
		});

		two_cars_button2.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(car_type_index!=1)
				{
					if(!(Txt_Pickup.equals(getResources().getString(R.string.no_drivers_found))))
					{
						//image.setBackgroundResource(R.drawable.home_carinfo_caricon_green_1);
						//=================MyChange=====================
						image.setBackgroundResource(R.drawable.home_carinfo_caricon_blue_1);
						//image.setBackgroundResource(R.drawable.car);
						//=================MyChange=====================
					}
					two_cars_button1.setVisibility(View.VISIBLE);
					//two_cars_button2.setVisibility(View.INVISIBLE);

					ObjectAnimator anim = ObjectAnimator.ofFloat(image, "translationX", getRelativeLeft(two_cars_button1),(getRelativeLeft(two_cars_button2)-(4*two_cars_button2.getMeasuredWidth())));
					anim.start();

					current_widht=(getRelativeLeft(two_cars_button2)-(2*two_cars_button2.getMeasuredWidth()));
					car_type_index=1;

					isType1MarkersPloted = false;
					isType2MarkersPloted = true;

					//marker_map.clear();
					marker_map_on_the_way.clear();
					Utility.printLog("marker_map_on_the_way 8");
					marker_map_arrived.clear();
					googleMap.clear();

					if(Type2Distance!=null && !Type2Distance.isEmpty())
					{
						pickup_Distance.setVisibility(View.VISIBLE);
						rate_unit.setVisibility(View.VISIBLE);
						pickup_Distance.setText(Type2Distance);
						Utility.printLog("pickup_Distance 2");
					}
					else
					{
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);
					}


					if(type2_markers!=null && type2_channels_list!=null && type2_markers.size()>0 && type2_channels_list.size()>0)
					{
						//Txt_Pickup.setTextSize(18f);
						pickup_Distance.setVisibility(View.VISIBLE);
						//rate_unit.setVisibility(View.VISIBLE);
						Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));

						for(int i=0;i<type2_markers.size();i++)
						{
							if(type2_markers.containsKey(type2_channels_list.get(i)))
							{
								Location location = type2_markers.get(type2_channels_list.get(i));

								LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

					/*			drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon)));*/
								//=============================================My change================================
								drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
								//=============================================My change================================

								marker_map.put(type2_channels_list.get(i), drivermarker);
							}
							else
							{
								type2_channels_list.remove(i);
							}
						}
					}
					else
					{
						distance = getResources().getString(R.string.nocabs);
						//Txt_Pickup.setTextSize(20f);
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);
						Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
						Utility.printLog("INSIDE DRIVERS NOT FOUND 4");
					}

					/*pubnubProgressDialog.setClickable(false);
					pubnubProgressDialog.setVisibility(View.VISIBLE);
					pickup_Distance.setVisibility(View.INVISIBLE);*/

					if( response.getCarsdetails()!=null &&  response.getCarsdetails().size()>0)
					{
						Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
						car_name = response.getCarsdetails().get(car_type_index).getType_name();
						//================My Change===============
						car_size=response.getCarsdetails().get(car_type_index).getMax_size();
						//================My Change===============
					}
					Utility.printLog("Car_Type_Id in onresume="+Car_Type_Id);
					startPublishingWithTimer();
				}
			}
		});


		two_cars_layout.setOnTouchListener(new OnTouchListener() 
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				// TODO Auto-generated method stub
				Utility.printLog("setOnTouchListener x="+event.getX()+"y="+event.getY());
				if(event.getX()>=0 && event.getX()<=width/2)
				{
					if(car_type_index!=0)
					{
						two_cars_button1.setVisibility(View.INVISIBLE);
						two_cars_button2.setVisibility(View.VISIBLE);
						image.setBackgroundResource(R.drawable.home_carinfo_caricon_blue_1);
						//=================MyChange=====================
						//image.setBackgroundResource(R.drawable.car);
						//=================MyChange=====================
						ObjectAnimator anim = ObjectAnimator.ofFloat(image, "translationX",( getRelativeLeft(two_cars_button2)-(4*two_cars_button2.getMeasuredWidth())),(getRelativeLeft(two_cars_button1)-(4*two_cars_button1.getMeasuredWidth())));
						anim.start();
						current_widht=(int)two_cars_button2.getX();
						car_type_index=0;

						isType1MarkersPloted = true;
						isType2MarkersPloted = false;

						//marker_map.clear();
						marker_map_on_the_way.clear();
						Utility.printLog("marker_map_on_the_way 8");
						marker_map_arrived.clear();
						googleMap.clear();

						if(Type1Distance!=null && !Type1Distance.isEmpty())
						{
							pickup_Distance.setVisibility(View.VISIBLE);
							rate_unit.setVisibility(View.VISIBLE);
							pickup_Distance.setText(Type1Distance);
							Utility.printLog("pickup_Distance 3");
						}
						else
						{
							pickup_Distance.setVisibility(View.GONE);
							rate_unit.setVisibility(View.GONE);
						}

						if(type1_markers!=null && type1_channels_list!=null && type1_markers.size()>0 && type1_channels_list.size()>0)
						{
							//Txt_Pickup.setTextSize(18f);
							pickup_Distance.setVisibility(View.VISIBLE);
							//rate_unit.setVisibility(View.VISIBLE);
							Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));

							for(int i=0;i<type1_markers.size();i++)
							{
								if(type1_markers.containsKey(type1_channels_list.get(i)))
								{
									Location location = type1_markers.get(type1_channels_list.get(i));

									LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
					/*				drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
											.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon_blue)));*/
									//=============================================My change================================
									drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
											.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
									//=============================================My change================================

									marker_map.put(type1_channels_list.get(i), drivermarker);
								}
								else
								{
									type1_channels_list.remove(i);
								}
							}
						}
						else
						{
							distance = getResources().getString(R.string.nocabs);
							//Txt_Pickup.setTextSize(20f);
							pickup_Distance.setVisibility(View.GONE);
							rate_unit.setVisibility(View.GONE);
							Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
							Utility.printLog("INSIDE DRIVERS NOT FOUND 5");
						}

						pubnubProgressDialog.setClickable(false);
						pubnubProgressDialog.setVisibility(View.VISIBLE);
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);
						if( response.getCarsdetails()!=null &&  response.getCarsdetails().size()>0)
						{
							Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
							car_name = response.getCarsdetails().get(car_type_index).getType_name();
							//================My Change===============
							car_size=response.getCarsdetails().get(car_type_index).getMax_size();
							//================My Change===============
						}

						Utility.printLog("Car_Type_Id in onresume="+Car_Type_Id);
						startPublishingWithTimer();
					}
				}
				if(event.getX()>=width/2 && event.getX()<=(width))
				{
					if(car_type_index!=1)
					{
						two_cars_button1.setVisibility(View.VISIBLE);
						two_cars_button2.setVisibility(View.INVISIBLE);
						//image.setBackgroundResource(R.drawable.home_carinfo_caricon_green_1);
						//=================MyChange=====================
					//	image.setBackgroundResource(R.drawable.car);
						image.setBackgroundResource(R.drawable.home_carinfo_caricon_blue_1);
						//=================MyChange=====================
						ObjectAnimator anim = ObjectAnimator.ofFloat(image, "translationX", getRelativeLeft(two_cars_button1),(getRelativeLeft(two_cars_button2)-(4*two_cars_button2.getMeasuredWidth())));
						anim.start();
						current_widht=(int)two_cars_button1.getX();
						car_type_index=1;
						isType1MarkersPloted = false;
						isType2MarkersPloted = true;


						//marker_map.clear();
						marker_map_on_the_way.clear();
						Utility.printLog("marker_map_on_the_way 8");
						marker_map_arrived.clear();
						googleMap.clear();

						if(Type2Distance!=null && !Type2Distance.isEmpty())
						{
							pickup_Distance.setVisibility(View.VISIBLE);
							rate_unit.setVisibility(View.VISIBLE);
							pickup_Distance.setText(Type2Distance);
							Utility.printLog("pickup_Distance 4");
						}
						else
						{
							pickup_Distance.setVisibility(View.GONE);
							pickup_Distance.setVisibility(View.GONE);
						}

						if(type2_markers!=null && type2_channels_list!=null && type2_markers.size()>0 && type2_channels_list.size()>0)
						{
							//Txt_Pickup.setTextSize(18f);
							pickup_Distance.setVisibility(View.VISIBLE);
							//rate_unit.setVisibility(View.VISIBLE);

							Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));

							for(int i=0;i<type2_markers.size();i++)
							{
								if(type2_markers.containsKey(type2_channels_list.get(i)))
								{
									Location location = type2_markers.get(type2_channels_list.get(i));

									LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
									//==================My change======================
								/*	drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
											.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon)));
							*/		//==================My change======================
									drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
											.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
									marker_map.put(type2_channels_list.get(i), drivermarker);
								}
								else
								{
									type2_channels_list.remove(i);
								}
							}
						}
						else
						{
							distance =getResources().getString(R.string.nocabs);
							//Txt_Pickup.setTextSize(20f);
							pickup_Distance.setVisibility(View.GONE);
							rate_unit.setVisibility(View.GONE);
							Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
							Utility.printLog("INSIDE DRIVERS NOT FOUND 6");
						}

						pubnubProgressDialog.setClickable(false);
						pubnubProgressDialog.setVisibility(View.VISIBLE);
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);

						if( response.getCarsdetails()!=null &&  response.getCarsdetails().size()>0)
						{
							Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
							car_name = response.getCarsdetails().get(car_type_index).getType_name();
							//================My Change===============
							car_size=response.getCarsdetails().get(car_type_index).getMax_size();
							//================My Change===============
						}					Utility.printLog("Car_Type_Id in onresume="+Car_Type_Id);
						startPublishingWithTimer();
					}
				}


				return false;
			}
		});

		three_cars_layout = (RelativeLayout)v.findViewById(R.id.three_cars_layout);
		three_image=(ImageView)v.findViewById(R.id.three_cars_iv_logo);
		three_cars_button1=(ImageButton)v.findViewById(R.id.three_cars_button1);
		three_cars_button2=(ImageButton)v.findViewById(R.id.three_cars_button2);

		three_cars_button3=(ImageButton)v.findViewById(R.id.three_cars_button3);

		/*RelativeLayout.LayoutParams three_layoutParams =
				new RelativeLayout.LayoutParams(three_image.getLayoutParams());

		layoutParams.leftMargin=30;
		//three_image.setLayoutParams(three_layoutParams);
		//image.setBackgroundResource(R.drawable.home_carinfo_caricon_blue_1);

		RelativeLayout.LayoutParams three_layoutParams1 =
				new RelativeLayout.LayoutParams(three_cars_button1.getLayoutParams());

		layoutParams1.leftMargin=30;
		//three_cars_button1.setLayoutParams(three_layoutParams1);


		RelativeLayout.LayoutParams three_layoutParams2 =
				new RelativeLayout.LayoutParams(three_cars_button2.getLayoutParams());

		layoutParams2.leftMargin=(width)-200;*/
		//three_cars_button3.setLayoutParams(three_layoutParams2);


		three_image.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{

				Utility.printLog("AddScrollView car id index="+(0));

				Intent intent = new Intent(mActivity,HomePagePopUp.class);
				intent.putExtra("chooser",car_type_index);
				intent.putExtra("DISTANCE", distance);
				intent.putExtra("BASEFARE0",baseFare0);
				intent.putExtra("PRICEPERMINUTE0", pricePerMin0);
				intent.putExtra("PRICEPERKM0", pricePerKm0);
				intent.putExtra("MINFARE0", minFare0);
				intent.putExtra("MAXSIZE0", maxSize0);
				intent.putExtra("BASEFARE1",baseFare1);
				intent.putExtra("PRICEPERMINUTE1", pricePerMin1);
				intent.putExtra("PRICEPERKM1", pricePerKm1);
				intent.putExtra("MINFARE1", minFare1);
				intent.putExtra("MAXSIZE1", maxSize1);
				intent.putExtra("BASEFARE2",baseFare2);
				intent.putExtra("PRICEPERMINUTE2", pricePerMin2);
				intent.putExtra("PRICEPERKM2", pricePerKm2);
				intent.putExtra("MINFARE2", minFare2);
				intent.putExtra("MAXSIZE2", maxSize2);
				intent.putExtra("BASEFARE3",baseFare3);
				intent.putExtra("PRICEPERMINUTE3", pricePerMin3);
				intent.putExtra("PRICEPERKM3", pricePerKm3);
				intent.putExtra("MINFARE3", minFare3);
				intent.putExtra("MAXSIZE3", maxSize3);
				mActivity.startActivityForResult(intent, 2);
			}
		});

		three_cars_button1.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(car_type_index!=0)
				{
					//three_image.setBackgroundResource(R.drawable.home_carinfo_caricon_blue_1);
					//=================MyChange=====================
					//three_image.setBackgroundResource(R.drawable.car);
					three_image.setBackgroundResource(R.drawable.home_carinfo_caricon_blue_1);
					//=================MyChange=====================
					three_cars_button1.setVisibility(View.INVISIBLE);
					three_cars_button2.setVisibility(View.VISIBLE);
					three_cars_button3.setVisibility(View.VISIBLE);
					int value=(int)Math.round(05*dblArray[0]);
					ObjectAnimator anim = ObjectAnimator.ofFloat(three_image, "translationX", width,value);
					anim.start();
					current_widht=30;

					Utility.printLog("three_cars_button1 width="+width+"  current_widht="+current_widht);

					car_type_index=0;

					//marker_map.clear();
					marker_map_on_the_way.clear();
					Utility.printLog("marker_map_on_the_way 8");
					marker_map_arrived.clear();
					googleMap.clear();

					if(Type1Distance!=null && !Type1Distance.isEmpty())
					{
						pickup_Distance.setVisibility(View.VISIBLE);
						rate_unit.setVisibility(View.VISIBLE);

						pickup_Distance.setText(Type1Distance);
						Utility.printLog("pickup_Distance 5");
					}
					else
					{
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);
					}

					/*pubnubProgressDialog.setClickable(false);
					pubnubProgressDialog.setVisibility(View.VISIBLE);
					pickup_Distance.setVisibility(View.INVISIBLE);*/

					isType1MarkersPloted = true;
					isType2MarkersPloted = false;
					isType3MarkersPloted = false;

					if(type1_markers!=null && type1_channels_list!=null && type1_markers.size()>0 && type1_channels_list.size()>0)
					{
						//Txt_Pickup.setTextSize(18f);
						pickup_Distance.setVisibility(View.VISIBLE);
						rate_unit.setVisibility(View.VISIBLE);

						Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));

						for(int i=0;i<type1_markers.size();i++)
						{
							if(type1_markers.containsKey(type1_channels_list.get(i)))
							{
								Location location = type1_markers.get(type1_channels_list.get(i));

								LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
								//drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon_blue)));
								//==================My change======================
									drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
								//==================My change======================


								marker_map.put(type1_channels_list.get(i), drivermarker);
							}
							else
							{
								type1_channels_list.remove(i);
							}
						}
					}
					else
					{
						distance = getResources().getString(R.string.nocabs);
						//Txt_Pickup.setTextSize(20f);
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);

						Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
						Utility.printLog("INSIDE DRIVERS NOT FOUND 7");
					}

					if(response.getCarsdetails()!=null &&  response.getCarsdetails().size()>0)
					{
						Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
						car_name = response.getCarsdetails().get(car_type_index).getType_name();
						//================My Change===============
						car_size=response.getCarsdetails().get(car_type_index).getMax_size();
						//================My Change===============
					}
					//Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
					Utility.printLog("Car_Type_Id in onresume="+Car_Type_Id);
					startPublishingWithTimer();
				}
			}
		});
		three_cars_button2.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(car_type_index!=1)
				{
					//=================MyChange=====================
					//three_image.setBackgroundResource(R.drawable.car);
					//=================MyChange=====================
					//three_image.setBackgroundResource(R.drawable.home_carinfo_caricon_green_1);
					three_image.setBackgroundResource(R.drawable.home_carinfo_caricon_blue_1);
					three_cars_button1.setVisibility(View.VISIBLE);
					three_cars_button2.setVisibility(View.INVISIBLE);
					three_cars_button3.setVisibility(View.VISIBLE);
					int value=(int)Math.round(70*dblArray[0]);

					ObjectAnimator anim = ObjectAnimator.ofFloat(three_image, "translationX", width,(width/2)-value);
					anim.start();
					Utility.printLog("three_cars_button2 previous width="+current_widht);
					current_widht=(width/2)-100;
					Utility.printLog("three_cars_button2 width="+width+"  current_widht="+current_widht);
					car_type_index=1;
					isType1MarkersPloted = false;
					isType2MarkersPloted = true;
					isType3MarkersPloted = false;

					//marker_map.clear();
					marker_map_on_the_way.clear();
					Utility.printLog("marker_map_on_the_way 8");
					marker_map_arrived.clear();
					googleMap.clear();

					if(Type2Distance!=null && !Type2Distance.isEmpty())
					{
						pickup_Distance.setVisibility(View.VISIBLE);
						rate_unit.setVisibility(View.VISIBLE);

						pickup_Distance.setText(Type2Distance);
						Utility.printLog("pickup_Distance 6");
					}
					else
					{
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);
					}

					/*pubnubProgressDialog.setClickable(false);
					pubnubProgressDialog.setVisibility(View.VISIBLE);
					pickup_Distance.setVisibility(View.INVISIBLE);*/

					if(type2_markers!=null && type2_channels_list!=null && type2_markers.size()>0 && type2_channels_list.size()>0)
					{
						//Txt_Pickup.setTextSize(18f);
						pickup_Distance.setVisibility(View.VISIBLE);
						//rate_unit.setVisibility(View.VISIBLE);

						Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));

						for(int i=0;i<type2_markers.size();i++)
						{
							if(type2_markers.containsKey(type2_channels_list.get(i)))
							{
								Location location = type2_markers.get(type2_channels_list.get(i));

								LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

							/*	drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon)));
*/
								//==================My change======================
								drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
								//==================My change======================

								marker_map.put(type2_channels_list.get(i), drivermarker);
							}
							else
							{
								type2_channels_list.remove(i);
							}
						}
					}
					else
					{
						distance = getResources().getString(R.string.nocabs);
						//Txt_Pickup.setTextSize(20f);
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);

						Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
						Utility.printLog("INSIDE DRIVERS NOT FOUND 8");
					}

					if(response.getCarsdetails()!=null &&  response.getCarsdetails().size()>0)
					{
						Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
						car_name = response.getCarsdetails().get(car_type_index).getType_name();
						//================My Change===============
						car_size=response.getCarsdetails().get(car_type_index).getMax_size();
						//================My Change===============
					}
					//Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
					Utility.printLog("Car_Type_Id in onresume="+Car_Type_Id);
					startPublishingWithTimer();
				}
			}
		});
		three_cars_button3.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(car_type_index!=2)
				{
					//=================MyChange=====================
					//three_image.setBackgroundResource(R.drawable.car);
					//=================MyChange=====================
					//three_image.setBackgroundResource(R.drawable.home_carinfo_caricon_red_1);
					three_image.setBackgroundResource(R.drawable.home_carinfo_caricon_blue_1);
					three_cars_button1.setVisibility(View.VISIBLE);
					three_cars_button2.setVisibility(View.VISIBLE);
					three_cars_button3.setVisibility(View.INVISIBLE);
					int value=(int)Math.round(140*dblArray[0]);

					ObjectAnimator anim = ObjectAnimator.ofFloat(three_image, "translationX", width,(width-value));
					anim.start();
					current_widht=width-200;
					Utility.printLog("three_cars_button3 width="+width+"  current_widht="+current_widht);
					car_type_index=2;

					isType1MarkersPloted = false;
					isType2MarkersPloted = false;
					isType3MarkersPloted = true;

					//marker_map.clear();
					marker_map_on_the_way.clear();
					Utility.printLog("marker_map_on_the_way 8");
					marker_map_arrived.clear();
					googleMap.clear();

					if(Type3Distance!=null && !Type3Distance.isEmpty())
					{
						pickup_Distance.setVisibility(View.VISIBLE);
						rate_unit.setVisibility(View.VISIBLE);

						pickup_Distance.setText(Type3Distance);
						Utility.printLog("pickup_Distance 7");
					}
					else
					{
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);
					}

					/*pubnubProgressDialog.setClickable(false);
					pubnubProgressDialog.setVisibility(View.VISIBLE);
					pickup_Distance.setVisibility(View.INVISIBLE);*/


					if(type3_markers!=null && type3_channels_list!=null && type3_markers.size()>0 && type3_channels_list.size()>0)
					{
						//Txt_Pickup.setTextSize(18f);
						pickup_Distance.setVisibility(View.VISIBLE);
						//rate_unit.setVisibility(View.VISIBLE);

						Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));

						for(int i=0;i<type3_markers.size();i++)
						{
							if(type3_markers.containsKey(type3_channels_list.get(i)))
							{
								Location location = type3_markers.get(type3_channels_list.get(i));

								LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
								/*drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon_red)));
*/
								//==================My change======================
								drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
								//==================My change======================


								marker_map.put(type3_channels_list.get(i), drivermarker);
							}
							else
							{
								type3_channels_list.remove(i);
							}
						}
					}
					else
					{
						distance = getResources().getString(R.string.nocabs);
						//Txt_Pickup.setTextSize(20f);
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);

						Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
						Utility.printLog("INSIDE DRIVERS NOT FOUND 9");
					}

					if( response.getCarsdetails()!=null &&  response.getCarsdetails().size()>0)
					{
						Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
						car_name = response.getCarsdetails().get(car_type_index).getType_name();
						//================My Change===============
						car_size=response.getCarsdetails().get(car_type_index).getMax_size();
						//================My Change===============
					}
					//Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
					Utility.printLog("Car_Type_Id in onresume="+Car_Type_Id);
					startPublishingWithTimer();
				}
			}
		});


		three_cars_layout.setOnTouchListener(new OnTouchListener() 
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				Utility.printLog("setOnTouchListener x="+event.getX()+"y="+event.getY());
				if(event.getX()>=0 && event.getX()<=width/4)
				{
					if(car_type_index!=0)
					{
						three_cars_button1.setVisibility(View.INVISIBLE);
						three_cars_button2.setVisibility(View.VISIBLE);
						three_cars_button3.setVisibility(View.VISIBLE);
						three_image.setBackgroundResource(R.drawable.home_carinfo_caricon_blue_1);
						//=================MyChange=====================
						//three_image.setBackgroundResource(R.drawable.car);
						//=================MyChange=====================
						ObjectAnimator anim = ObjectAnimator.ofFloat(three_image, "translationX", current_widht,30);
						anim.start();
						current_widht=30;
						Utility.printLog("three_cars_button1 width="+width+"  current_widht="+current_widht);
						car_type_index=0;

						isType1MarkersPloted = true;
						isType2MarkersPloted = false;
						isType3MarkersPloted = false;

						//marker_map.clear();
						marker_map_on_the_way.clear();
						Utility.printLog("marker_map_on_the_way 8");
						marker_map_arrived.clear();
						googleMap.clear();

						if(Type1Distance!=null && !Type1Distance.isEmpty())
						{
							pickup_Distance.setVisibility(View.VISIBLE);
							rate_unit.setVisibility(View.VISIBLE);

							pickup_Distance.setText(Type1Distance);
							Utility.printLog("pickup_Distance 8");
						}
						else
						{
							pickup_Distance.setVisibility(View.GONE);
							pickup_Distance.setVisibility(View.GONE);
						}
						/*pubnubProgressDialog.setClickable(false);
						pubnubProgressDialog.setVisibility(View.VISIBLE);
						pickup_Distance.setVisibility(View.INVISIBLE);*/

						if(type1_markers!=null && type1_channels_list!=null && type1_markers.size()>0 && type1_channels_list.size()>0)
						{
							//Txt_Pickup.setTextSize(18f);
							pickup_Distance.setVisibility(View.VISIBLE);
							//rate_unit.setVisibility(View.VISIBLE);

							Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));

							for(int i=0;i<type1_markers.size();i++)
							{
								if(type1_markers.containsKey(type1_channels_list.get(i)))
								{
									Location location = type1_markers.get(type1_channels_list.get(i));

									LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
/*									drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
											.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon_blue)));*/

									//===========================My Change============================
									drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
											.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
									//===========================My Change============================

									marker_map.put(type1_channels_list.get(i), drivermarker);
								}
								else
								{
									type1_channels_list.remove(i);
								}
							}
						}
						else
						{
							distance = getResources().getString(R.string.nocabs);
							//Txt_Pickup.setTextSize(20f);
							pickup_Distance.setVisibility(View.GONE);
							rate_unit.setVisibility(View.GONE);

							Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
							Utility.printLog("INSIDE DRIVERS NOT FOUND 10");
						}

						if( response.getCarsdetails()!=null &&  response.getCarsdetails().size()>0)
						{
							Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
							car_name = response.getCarsdetails().get(car_type_index).getType_name();
							//================My Change===============
							car_size=response.getCarsdetails().get(car_type_index).getMax_size();
							//================My Change===============
						}
						//Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
						Utility.printLog("Car_Type_Id in onresume="+Car_Type_Id);
						startPublishingWithTimer();
					}
				}
				if(event.getX()>width/4 && event.getX()<=((3*width)/4))
				{
					if(car_type_index!=1)
					{
						three_cars_button1.setVisibility(View.VISIBLE);
						three_cars_button2.setVisibility(View.INVISIBLE);
						three_cars_button3.setVisibility(View.VISIBLE);
						//three_image.setBackgroundResource(R.drawable.home_carinfo_caricon_green_1);
						//=================MyChange=====================
						//three_image.setBackgroundResource(R.drawable.car);
						three_image.setBackgroundResource(R.drawable.home_carinfo_caricon_blue_1);
						//=================MyChange=====================
						ObjectAnimator anim = ObjectAnimator.ofFloat(three_image, "translationX", current_widht,(width/2)-100);
						anim.start();
						current_widht=(width/2)-100;
						Utility.printLog("three_cars_button2 width="+width+"  current_widht="+current_widht);
						car_type_index=1;

						isType1MarkersPloted = false;
						isType2MarkersPloted = true;
						isType3MarkersPloted = false;

						//marker_map.clear();
						marker_map_on_the_way.clear();
						Utility.printLog("marker_map_on_the_way 8");
						marker_map_arrived.clear();
						googleMap.clear();

						if(Type2Distance!=null && !Type2Distance.isEmpty())
						{
							pickup_Distance.setVisibility(View.VISIBLE);
							rate_unit.setVisibility(View.VISIBLE);

							pickup_Distance.setText(Type2Distance);
							Utility.printLog("pickup_Distance 9");
						}
						else
						{
							pickup_Distance.setVisibility(View.GONE);
							rate_unit.setVisibility(View.GONE);
						}

						/*pubnubProgressDialog.setClickable(false);
						pubnubProgressDialog.setVisibility(View.VISIBLE);
						pickup_Distance.setVisibility(View.INVISIBLE);*/

						if(type2_markers!=null && type2_channels_list!=null && type2_markers.size()>0 && type2_channels_list.size()>0)
						{
							//Txt_Pickup.setTextSize(18f);
							pickup_Distance.setVisibility(View.VISIBLE);
							//rate_unit.setVisibility(View.VISIBLE);

							Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));

							for(int i=0;i<type2_markers.size();i++)
							{
								if(type2_markers.containsKey(type2_channels_list.get(i)))
								{
									Location location = type2_markers.get(type2_channels_list.get(i));

									LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
								/*	drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
											.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon)));*/

									//===========================My Change============================
									drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
											.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
									marker_map.put(type2_channels_list.get(i), drivermarker);
								}
								else
								{
									type2_channels_list.remove(i);
								}
							}
						}
						else
						{
							distance = getResources().getString(R.string.nocabs);
							//Txt_Pickup.setTextSize(20f);
							pickup_Distance.setVisibility(View.GONE);
							rate_unit.setVisibility(View.GONE);

							Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
							Utility.printLog("INSIDE DRIVERS NOT FOUND 11");
						}

						if( response.getCarsdetails()!=null &&  response.getCarsdetails().size()>0)
						{
							Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
							car_name = response.getCarsdetails().get(car_type_index).getType_name();
							//================My Change===============
							car_size=response.getCarsdetails().get(car_type_index).getMax_size();
							//================My Change===============
						}
						//Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
						Utility.printLog("Car_Type_Id in onresume="+Car_Type_Id);
						startPublishingWithTimer();
					}
				}
				if(event.getX()>((3*width)/4) && event.getX()<=(width))
				{
					if(car_type_index!=2)
					{
						two_cars_button1.setVisibility(View.VISIBLE);
						two_cars_button2.setVisibility(View.VISIBLE);
						three_cars_button3.setVisibility(View.INVISIBLE);
						//three_image.setBackgroundResource(R.drawable.home_carinfo_caricon_red_1);
						//=================MyChange=====================
						//three_image.setBackgroundResource(R.drawable.car);
						three_image.setBackgroundResource(R.drawable.home_carinfo_caricon_blue_1);
						//=================MyChange=====================
						ObjectAnimator anim = ObjectAnimator.ofFloat(three_image, "translationX", current_widht,(width-200));
						anim.start();
						current_widht=width-200;
						Utility.printLog("three_cars_button3 width="+width+"  current_widht="+current_widht);
						car_type_index=2;

						isType1MarkersPloted = false;
						isType2MarkersPloted = false;
						isType3MarkersPloted = true;

						//marker_map.clear();
						marker_map_on_the_way.clear();
						Utility.printLog("marker_map_on_the_way 8");
						marker_map_arrived.clear();
						googleMap.clear();

						if(Type3Distance!=null && !Type3Distance.isEmpty())
						{
							pickup_Distance.setVisibility(View.VISIBLE);
							rate_unit.setVisibility(View.VISIBLE);

							pickup_Distance.setText(Type3Distance);
							Utility.printLog("pickup_Distance 10");
						}
						else
						{
							pickup_Distance.setVisibility(View.GONE);
							rate_unit.setVisibility(View.GONE);
						}

						/*pubnubProgressDialog.setClickable(false);
						pubnubProgressDialog.setVisibility(View.VISIBLE);
						pickup_Distance.setVisibility(View.INVISIBLE);*/

						if(type3_markers!=null && type3_channels_list!=null && type3_markers.size()>0 && type3_channels_list.size()>0)
						{
							//Txt_Pickup.setTextSize(18f);
							pickup_Distance.setVisibility(View.VISIBLE);
							//rate_unit.setVisibility(View.VISIBLE);

							Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));

							for(int i=0;i<type3_markers.size();i++)
							{
								if(type3_markers.containsKey(type3_channels_list.get(i)))
								{
									Location location = type3_markers.get(type3_channels_list.get(i));

									LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
					/*				drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
											.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon_red)));*/
                                    //=============================================My change================================
									drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
											.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
									//=============================================My change================================
									marker_map.put(type3_channels_list.get(i), drivermarker);
								}
								else
								{
									type3_channels_list.remove(i);
								}
							}
						}
						else
						{
							distance = getResources().getString(R.string.nocabs);
							//Txt_Pickup.setTextSize(20f);
							pickup_Distance.setVisibility(View.GONE);
							rate_unit.setVisibility(View.GONE);

							Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
							Utility.printLog("INSIDE DRIVERS NOT FOUND 12");
						}

						if( response.getCarsdetails()!=null &&  response.getCarsdetails().size()>0)
						{
							Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
							car_name = response.getCarsdetails().get(car_type_index).getType_name();
							//================My Change===============
							car_size=response.getCarsdetails().get(car_type_index).getMax_size();
							//================My Change===============
						}
						//Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
						Utility.printLog("Car_Type_Id in onresume="+Car_Type_Id);
						startPublishingWithTimer();
					}
				}

				return false;
			}
		});


		four_cars_layout = (RelativeLayout)v.findViewById(R.id.four_cars_layout);
		four_image=(ImageView)v.findViewById(R.id.four_cars_iv_logo);
		four_cars_button1=(ImageButton)v.findViewById(R.id.four_cars_button1);
		four_cars_button2=(ImageButton)v.findViewById(R.id.four_cars_button2);
		four_cars_button3=(ImageButton)v.findViewById(R.id.four_cars_button3);
		four_cars_button4=(ImageButton)v.findViewById(R.id.four_cars_button4);

		//akbar commented because taking the layout from xml

		/*RelativeLayout.LayoutParams four_layoutParams =	new RelativeLayout.LayoutParams(four_image.getLayoutParams());

		four_layoutParams.leftMargin=(width/8)-30;
		//four_image.setLayoutParams(three_layoutParams);
		four_image.setBackgroundResource(R.drawable.home_carinfo_caricon_blue_1);

		four_cars_button1=(ImageButton)v.findViewById(R.id.four_cars_button1);

		RelativeLayout.LayoutParams four_layoutParams1 =
				new RelativeLayout.LayoutParams(four_cars_button1.getLayoutParams());
		four_layoutParams1.leftMargin=(width/8)-30;

		RelativeLayout.LayoutParams four_layoutParams2 =
				new RelativeLayout.LayoutParams(four_cars_button2.getLayoutParams());

		four_layoutParams2.leftMargin=(width/4)+(width/8);

		RelativeLayout.LayoutParams four_layoutParams3 =
				new RelativeLayout.LayoutParams(four_cars_button3.getLayoutParams());

		four_layoutParams3.leftMargin=2*(width/4)+(width/8);

		RelativeLayout.LayoutParams four_layoutParams4 =
				new RelativeLayout.LayoutParams(four_cars_button4.getLayoutParams());

		four_layoutParams4.leftMargin=(width)-(width/8);

		four_cars_button1.setLayoutParams(four_layoutParams1);
		four_cars_button2.setLayoutParams(four_layoutParams2);
		four_cars_button3.setLayoutParams(four_layoutParams3);
		four_cars_button4.setLayoutParams(four_layoutParams4);
		 */
		four_image.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub

				Utility.printLog("AddScrollView car id index="+(0));

				Intent intent = new Intent(mActivity,HomePagePopUp.class);
				intent.putExtra("chooser",car_type_index);
				intent.putExtra("DISTANCE", distance);
				intent.putExtra("BASEFARE0",baseFare0);
				intent.putExtra("PRICEPERMINUTE0", pricePerMin0);
				intent.putExtra("PRICEPERKM0", pricePerKm0);
				intent.putExtra("MINFARE0", minFare0);
				intent.putExtra("MAXSIZE0", maxSize0);
				intent.putExtra("BASEFARE1",baseFare1);
				intent.putExtra("PRICEPERMINUTE1", pricePerMin1);
				intent.putExtra("PRICEPERKM1", pricePerKm1);
				intent.putExtra("MINFARE1", minFare1);
				intent.putExtra("MAXSIZE1", maxSize1);
				intent.putExtra("BASEFARE2",baseFare2);
				intent.putExtra("PRICEPERMINUTE2", pricePerMin2);
				intent.putExtra("PRICEPERKM2", pricePerKm2);
				intent.putExtra("MINFARE2", minFare2);
				intent.putExtra("MAXSIZE2", maxSize2);
				intent.putExtra("BASEFARE3",baseFare3);
				intent.putExtra("PRICEPERMINUTE3", pricePerMin3);
				intent.putExtra("PRICEPERKM3", pricePerKm3);
				intent.putExtra("MINFARE3", minFare3);
				intent.putExtra("MAXSIZE3", maxSize3);
				mActivity.startActivityForResult(intent, 2);
			}
		});

		four_cars_button1.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(car_type_index!=0)
				{
					four_image.setBackgroundResource(R.drawable.home_carinfo_caricon_blue_1);
					//=================MyChange=====================
					//four_image.setBackgroundResource(R.drawable.car);
					//=================MyChange=====================
					four_cars_button1.setVisibility(View.INVISIBLE);
					four_cars_button2.setVisibility(View.VISIBLE);
					four_cars_button3.setVisibility(View.VISIBLE);
					four_cars_button4.setVisibility(View.VISIBLE);
					int value=(int)Math.round(70*dblArray[0]);
					ObjectAnimator anim = ObjectAnimator.ofFloat(four_image, "translationX", width,(width/8)-value);//(width/8)-30
					anim.start();
					Utility.printLog("four_cars_button1 previous width="+current_widht);
					//current_widht=(10/width)*100;
					current_widht=(width/8)-30;
					Utility.printLog("four_cars_button1 width="+width+"  current_widht="+current_widht);

					car_type_index=0;

					isType1MarkersPloted = true;
					isType2MarkersPloted = false;
					isType3MarkersPloted = false;
					isType4MarkersPloted = false;

					//marker_map.clear();
					marker_map_on_the_way.clear();
					Utility.printLog("marker_map_on_the_way 8");
					marker_map_arrived.clear();
					googleMap.clear();

					if(Type1Distance!=null && !Type1Distance.isEmpty())
					{
						pickup_Distance.setVisibility(View.VISIBLE);
						rate_unit.setVisibility(View.VISIBLE);

						pickup_Distance.setText(Type1Distance);
						Utility.printLog("pickup_Distance 11");
					}
					else
					{
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);
					}
					/*pubnubProgressDialog.setClickable(false);
					pubnubProgressDialog.setVisibility(View.VISIBLE);
					pickup_Distance.setVisibility(View.INVISIBLE);*/

					if(type1_markers!=null && type1_channels_list!=null && type1_markers.size()>0 && type1_channels_list.size()>0)
					{
						//Txt_Pickup.setTextSize(18f);
						pickup_Distance.setVisibility(View.VISIBLE);
						//rate_unit.setVisibility(View.VISIBLE);

						Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));

						for(int i=0;i<type1_markers.size();i++)
						{
							if(type1_markers.containsKey(type1_channels_list.get(i)))
							{
								Location location = type1_markers.get(type1_channels_list.get(i));

								LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			/*					drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon_blue)));*/
								//=============================================My change================================
								drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
                               //=============================================My change================================
								marker_map.put(type1_channels_list.get(i), drivermarker);
							}
							else
							{
								type1_channels_list.remove(i);
							}
						}
					}
					else
					{
						distance = getResources().getString(R.string.nocabs);
						//Txt_Pickup.setTextSize(20f);
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);

						Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
						Utility.printLog("INSIDE DRIVERS NOT FOUND 13");
					}

					if( response.getCarsdetails()!=null &&  response.getCarsdetails().size()>0)
					{
						Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
						car_name = response.getCarsdetails().get(car_type_index).getType_name();
						//================My Change===============
						car_size=response.getCarsdetails().get(car_type_index).getMax_size();
						//================My Change===============
					}
					Utility.printLog("Car_Type_Id in onresume="+Car_Type_Id);
					startPublishingWithTimer();
				}
			}
		});
		four_cars_button2.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(car_type_index!=1)
				{
					//four_image.setBackgroundResource(R.drawable.home_carinfo_caricon_green_1);
					//=================MyChange=====================
					//four_image.setBackgroundResource(R.drawable.car);
					four_image.setBackgroundResource(R.drawable.home_carinfo_caricon_blue_1);
					//=================MyChange=====================
					four_cars_button1.setVisibility(View.VISIBLE);
					four_cars_button2.setVisibility(View.INVISIBLE);
					four_cars_button3.setVisibility(View.VISIBLE);
					four_cars_button4.setVisibility(View.VISIBLE);
					int value=(int)Math.round(80*dblArray[0]);
					ObjectAnimator anim = ObjectAnimator.ofFloat(four_image, "translationX", width,(width/4)+(width/8)-value);
					anim.start();
					Utility.printLog("four_cars_button2 previous width="+current_widht);
					//current_widht=((33*width)/100);
					current_widht=(width/4)+(width/8);
					Utility.printLog("four_cars_button2 width="+width+"  current_widht="+current_widht);
					car_type_index=1;
					isType1MarkersPloted = false;
					isType2MarkersPloted = true;
					isType3MarkersPloted = false;
					isType4MarkersPloted = false;

					//marker_map.clear();
					marker_map_on_the_way.clear();
					Utility.printLog("marker_map_on_the_way 8");
					marker_map_arrived.clear();
					googleMap.clear();

					if(Type2Distance!=null && !Type2Distance.isEmpty())
					{
						pickup_Distance.setVisibility(View.VISIBLE);
						rate_unit.setVisibility(View.VISIBLE);

						pickup_Distance.setText(Type2Distance);
						Utility.printLog("pickup_Distance 12");
					}
					else
					{
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);
					}

					/*pubnubProgressDialog.setClickable(false);
					pubnubProgressDialog.setVisibility(View.VISIBLE);
					pickup_Distance.setVisibility(View.INVISIBLE);*/

					if(type2_markers!=null && type2_channels_list!=null && type2_markers.size()>0 && type2_channels_list.size()>0)
					{
						//Txt_Pickup.setTextSize(18f);
						pickup_Distance.setVisibility(View.VISIBLE);
						//rate_unit.setVisibility(View.VISIBLE);

						Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));

						for(int i=0;i<type2_markers.size();i++)
						{

							if(type2_markers.containsKey(type2_channels_list.get(i)))
							{
								Location location = type2_markers.get(type2_channels_list.get(i));

								LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

					/*			drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon)));*/
								//=============================================My change================================
								drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
								//=============================================My change================================
								marker_map.put(type2_channels_list.get(i), drivermarker);
							}
							else
							{
								type2_channels_list.remove(i);
							}
						}
					}
					else
					{
						distance = getResources().getString(R.string.nocabs);
						//Txt_Pickup.setTextSize(20f);
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);

						Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
						Utility.printLog("INSIDE DRIVERS NOT FOUND 14");
					}

					if( response.getCarsdetails()!=null &&  response.getCarsdetails().size()>0)
					{
						Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
						car_name = response.getCarsdetails().get(car_type_index).getType_name();
						//================My Change===============
						car_size=response.getCarsdetails().get(car_type_index).getMax_size();
						//================My Change===============
					}
					//Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
					Utility.printLog("Car_Type_Id in onresume="+Car_Type_Id);
					startPublishingWithTimer();
				}
			}
		});
		four_cars_button3.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(car_type_index!=2)
				{
					//four_image.setBackgroundResource(R.drawable.home_carinfo_caricon_red_1);
					//=================MyChange=====================
					//four_image.setBackgroundResource(R.drawable.car);
					four_image.setBackgroundResource(R.drawable.home_carinfo_caricon_blue_1);
					//=================MyChange=====================
					four_cars_button1.setVisibility(View.VISIBLE);
					four_cars_button2.setVisibility(View.VISIBLE);
					four_cars_button3.setVisibility(View.INVISIBLE);
					four_cars_button4.setVisibility(View.VISIBLE);
					int value=(int)Math.round(80*dblArray[0]);
					ObjectAnimator anim = ObjectAnimator.ofFloat(four_image, "translationX", width,4*(width/8)+(width/8)-value);
					anim.start();
					Utility.printLog("four_cars_button3 previous width="+current_widht);
					//current_widht=(63/width)*100;
					current_widht=2*(width/4)+(width/8);
					Utility.printLog("four_cars_button3 width="+width+"  current_widht="+current_widht);
					car_type_index=2;

					isType1MarkersPloted = false;
					isType2MarkersPloted = false;
					isType3MarkersPloted = true;
					isType4MarkersPloted = false;

					//marker_map.clear();
					marker_map_on_the_way.clear();
					Utility.printLog("marker_map_on_the_way 8");
					marker_map_arrived.clear();
					googleMap.clear();

					if(Type3Distance!=null && !Type3Distance.isEmpty())
					{
						pickup_Distance.setVisibility(View.VISIBLE);
						rate_unit.setVisibility(View.VISIBLE);

						pickup_Distance.setText(Type3Distance);
						Utility.printLog("pickup_Distance 13");
					}
					else
					{
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);
					}

					/*pubnubProgressDialog.setClickable(false);
					pubnubProgressDialog.setVisibility(View.VISIBLE);
					pickup_Distance.setVisibility(View.INVISIBLE);*/

					if(type3_markers!=null && type3_channels_list!=null && type3_markers.size()>0 && type3_channels_list.size()>0)
					{
						//Txt_Pickup.setTextSize(18f);
						pickup_Distance.setVisibility(View.VISIBLE);
						//rate_unit.setVisibility(View.VISIBLE);

						Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));

						for(int i=0;i<type3_markers.size();i++)
						{
							if(type3_markers.containsKey(type3_channels_list))
							{
								Location location = type3_markers.get(type3_channels_list.get(i));

								LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

				/*			drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon_red)));*/

								//=============================================My change================================
								drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
								//=============================================My change================================

								marker_map.put(type3_channels_list.get(i), drivermarker);
							}
							else
							{
								type3_channels_list.remove(i);
							}

						}
					}
					else
					{
						distance = getResources().getString(R.string.nocabs);
						//Txt_Pickup.setTextSize(20f);
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);
						Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
						Utility.printLog("INSIDE DRIVERS NOT FOUND 15");
					}

					if( response.getCarsdetails()!=null &&  response.getCarsdetails().size()>0)
					{
						Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
						car_name = response.getCarsdetails().get(car_type_index).getType_name();
						//================My Change===============
						car_size=response.getCarsdetails().get(car_type_index).getMax_size();
						//================My Change===============
					}
					//Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
					Utility.printLog("Car_Type_Id in onresume="+Car_Type_Id);
					startPublishingWithTimer();
				}
			}
		});

		four_cars_button4.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(car_type_index!=3)
				{
					//four_image.setBackgroundResource(R.drawable.home_carinfo_caricon_1);
					//======================Mychange=======================
					//four_image.setBackgroundResource(R.drawable.car);
					four_image.setBackgroundResource(R.drawable.home_carinfo_caricon_blue_1);
					//======================Mychange=======================
					four_cars_button1.setVisibility(View.VISIBLE);
					four_cars_button2.setVisibility(View.VISIBLE);
					four_cars_button3.setVisibility(View.VISIBLE);
					four_cars_button4.setVisibility(View.INVISIBLE);
					int value=(int)Math.round(80*dblArray[0]);
					ObjectAnimator anim = ObjectAnimator.ofFloat(four_image, "translationX", width,(width)-(width/6)-value);
					anim.start();
					Utility.printLog("four_cars_button4 previous width="+current_widht);
					//current_widht=(90/width)*100;
					current_widht=(width)-(width/8);
					Utility.printLog("four_cars_button4 width="+width+"  current_widht="+current_widht);
					car_type_index=3;

					isType1MarkersPloted = false;
					isType2MarkersPloted = false;
					isType3MarkersPloted = false;
					isType4MarkersPloted = true;

					//marker_map.clear();
					marker_map_on_the_way.clear();
					Utility.printLog("marker_map_on_the_way 8");
					marker_map_arrived.clear();
					googleMap.clear();

					if(Type4Distance!=null && !Type4Distance.isEmpty())
					{
						pickup_Distance.setVisibility(View.VISIBLE);
						rate_unit.setVisibility(View.VISIBLE);

						pickup_Distance.setText(Type4Distance);
						Utility.printLog("pickup_Distance 14");
					}
					else
					{
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);
					}
					/*pubnubProgressDialog.setClickable(false);
					pubnubProgressDialog.setVisibility(View.VISIBLE);
					pickup_Distance.setVisibility(View.INVISIBLE);*/

					if(type4_markers!=null && type4_channels_list!=null && type4_markers.size()>0 && type4_channels_list.size()>0)
					{
						//Txt_Pickup.setTextSize(18f);
						pickup_Distance.setVisibility(View.VISIBLE);
						//rate_unit.setVisibility(View.VISIBLE);

						Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));

						for(int i=0 ; (i<type4_markers.size() && type4_channels_list.size()>i) ; i++)
						{
							if(type4_markers.containsKey(type4_channels_list))
							{
								Location location = type4_markers.get(type4_channels_list.get(i));

								LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

								//=============================================My change================================
								drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
								//=============================================My change================================
					/*			drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon_black)));*/

								marker_map.put(type4_channels_list.get(i), drivermarker);
							}
							else
							{
								type4_channels_list.remove(i);
							}
						}
					}
					else
					{
						distance = getResources().getString(R.string.nocabs);
						//Txt_Pickup.setTextSize(20f);
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);

						Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
						Utility.printLog("INSIDE DRIVERS NOT FOUND 16");
					}

					if( response.getCarsdetails()!=null &&  response.getCarsdetails().size()>0)
					{
						Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
						car_name = response.getCarsdetails().get(car_type_index).getType_name();
						//================My Change===============
						car_size=response.getCarsdetails().get(car_type_index).getMax_size();
						//================My Change===============
					}
					//Car_Type_Id = response.getCarsdetails().get(car_type_index).getType_id();
					Utility.printLog("Car_Type_Id in onresume="+Car_Type_Id);
					startPublishingWithTimer();
				}
			}
		});
	}





	private void showPopupForShare()
	{
		LayoutInflater inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popupLayout = inflater.inflate(R.layout.popup_share_new,(ViewGroup)view.findViewById(R.id.share_parent));
		Typeface roboto_condensed = Typeface.createFromAsset(getActivity().getAssets(),"fonts/BebasNeue.otf");

		//facebook_share=(Button)popupLayout.findViewById(R.id.facebook_share);
		//twitter_share=(Button)popupLayout.findViewById(R.id.twitter_share);
		message_shar=(Button)popupLayout.findViewById(R.id.message_share);
		whatsapp_sahre=(Button)popupLayout.findViewById(R.id.whatsapp_share);
		email_share=(Button)popupLayout.findViewById(R.id.email_share);
		cancel_share=(Button)popupLayout.findViewById(R.id.cancel_share);

		message_shar.setTypeface(roboto_condensed);
		whatsapp_sahre.setTypeface(roboto_condensed);
		email_share.setTypeface(roboto_condensed);
		cancel_share.setTypeface(roboto_condensed);

		message_shar.setOnClickListener(this);
		whatsapp_sahre.setOnClickListener(this);
		email_share.setOnClickListener(this);
		cancel_share.setOnClickListener(this);

		popup_share = new PopupWindow(popupLayout, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		Utility.printLog("CONTROL INSIDE onCreateView");
		Utility.printLog("onCreateView = "+mActivity);
		setHasOptionsMenu(true);

		if(view != null)
		{
			ViewGroup parent = (ViewGroup) view.getParent();
			if(parent != null)
				parent.removeView(view);
		}
		try 
		{
			view = inflater.inflate(R.layout.homepage, container, false);
			visibility=true;
		}
		catch(InflateException e)
		{
			initializeVariables(view);
			/* map is already there, just return view as it is */
			Utility.printLog(TAG, "onCreateView  InflateException "+e);
		}

		initializeVariables(view);

		view.setOnTouchListener(this);




		//googleMap=((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		/*googleMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

		googleMap.setMyLocationEnabled(true);
		googleMap.getUiSettings().setZoomControlsEnabled(false);
		googleMap.getUiSettings().setMyLocationButtonEnabled(false);
*/

		SupportMapFragment fm = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
		googleMap=fm.getMap();

		ResideMenuActivity parentActivity = (ResideMenuActivity) getActivity();
		final ResideMenu  resideMenu = parentActivity.getResideMenu();
		resideMenu.addIgnore(fm.getView());

		googleMap.setMyLocationEnabled(true);
		googleMap.getUiSettings().setZoomControlsEnabled(false);
		//=======================================My Change==============================
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		//-------------------------------------------


		View myLocationButton=fm.getView().findViewById(0x2);


		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLocationButton.getLayoutParams();
		// Align it to - parent BOTTOM|LEFT
		//params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		//params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,-1);

	// Update margins, set to 10dp
		final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,0,
				getResources().getDisplayMetrics());
		params.setMargins(margin,200, margin,0);

		myLocationButton.setLayoutParams(params);

//=========================My Change=======================




		initImageLoader();
		imageLoader = ImageLoader.getInstance();

		options= new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.driver_image_border)
		.showImageForEmptyUri(R.drawable.driver_image_border)
		.showImageOnFail(R.drawable.driver_image_border)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(0))
		.build();



		if(isGpsEnable)
		{

			if(latitude==0.0 || longitude==0.0)
			{
				Toast.makeText(getActivity(), "NO Location view",Toast.LENGTH_SHORT).show();
			}
			else
			{
				LatLng latLng = new LatLng(latitude,longitude);
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
			}
		}
		else
		{
			showGpsAlert();
		}

		pubnub = new Pubnub(VariableConstants.PUBNUB_PUBLISH_KEY, VariableConstants.PUBUB_SUBSCRIBE_KEY, "", true);

		SessionManager session = new SessionManager(getActivity());
		String  jsonResponse=session.getCarTypes();
		Log.i("Loginres",jsonResponse);
		Utility.printLog("LoginResponse="+jsonResponse);

		if(jsonResponse!=null) {
			Gson gson = new Gson();
			response = gson.fromJson(jsonResponse, LoginResponse.class);

		/*	for (int i = 0; i <response.getCarsdetails().size(); i++)
			{
				Log.e("responce before is", "CarDetails " + response.getCarsdetails().get(i).getType_name());
		}*/
	/*		//=================My Change Car types Inverse========================
           ArrayList<CarsDetailList> arr = new ArrayList<CarsDetailList>();
			Log.i("responce is","LoginResponce "+response);

			Log.e("responce is","Before "+response.getCarsdetails().get(0).getType_name());
			Log.e("responce is","Before "+response.getCarsdetails().get(1).getType_name());
			for (int i=response.getCarsdetails().size();i>0;i--)
			{
				arr.add(response.getCarsdetails().get(i-1));
			}


			response.setCarsdetails((arr));
			Log.e("responce is","After "+response.getCarsdetails().get(0).getType_name());
			Log.e("responce is","after "+response.getCarsdetails().get(1).getType_name());
			//=================My Change Car types Inverse========================*/

			if(response.getCarsdetails()!=null && response.getCarsdetails().size()>0)
			{
				car_name=response.getCarsdetails().get(0).getType_name();
				Car_Type_Id=response.getCarsdetails().get(0).getType_id();
				//================My Change===============
				car_size=response.getCarsdetails().get(car_type_index).getMax_size();
				//================My Change===============
				car_type_index = 0;

				if(response.getCarsdetails().size()==1)
				{
					one_car_layout.setVisibility(View.VISIBLE);
					two_cars_layout.setVisibility(View.GONE);
					three_cars_layout.setVisibility(View.GONE);
					four_cars_layout.setVisibility(View.GONE);

					one_car_Type_Name.setText(response.getCarsdetails().get(0).getType_name());
				}
				else if(response.getCarsdetails().size()==2)
				{
					one_car_layout.setVisibility(View.GONE);
					two_cars_layout.setVisibility(View.VISIBLE);
					three_cars_layout.setVisibility(View.GONE);
					four_cars_layout.setVisibility(View.GONE);

					two_cars_Type1_Name.setText(response.getCarsdetails().get(0).getType_name());
					two_cars_Type2_Name.setText(response.getCarsdetails().get(1).getType_name());
				}
				else if(response.getCarsdetails().size()==3)
				{
					one_car_layout.setVisibility(View.GONE);
					two_cars_layout.setVisibility(View.GONE);
					three_cars_layout.setVisibility(View.VISIBLE);
					four_cars_layout.setVisibility(View.GONE);

					three_cars_Type1_Name.setText(response.getCarsdetails().get(0).getType_name());
					three_cars_Type2_Name.setText(response.getCarsdetails().get(1).getType_name());
					three_cars_Type3_Name.setText(response.getCarsdetails().get(2).getType_name());

				}
				else if(response.getCarsdetails().size()==4)
				{
					one_car_layout.setVisibility(View.GONE);
					two_cars_layout.setVisibility(View.GONE);
					three_cars_layout.setVisibility(View.GONE);
					four_cars_layout.setVisibility(View.VISIBLE);

					four_cars_Type1_Name.setText(response.getCarsdetails().get(0).getType_name());
					four_cars_Type2_Name.setText(response.getCarsdetails().get(1).getType_name());
					four_cars_Type3_Name.setText(response.getCarsdetails().get(2).getType_name());
					four_cars_Type4_Name.setText(response.getCarsdetails().get(3).getType_name());
				}
				else if(response.getCarsdetails().size()>4)
				{
					one_car_layout.setVisibility(View.GONE);
					two_cars_layout.setVisibility(View.GONE);
					three_cars_layout.setVisibility(View.GONE);
					four_cars_layout.setVisibility(View.VISIBLE);

					four_cars_Type1_Name.setText(response.getCarsdetails().get(0).getType_name());
					four_cars_Type2_Name.setText(response.getCarsdetails().get(1).getType_name());
					four_cars_Type3_Name.setText(response.getCarsdetails().get(2).getType_name());
					four_cars_Type4_Name.setText(response.getCarsdetails().get(3).getType_name());
				}
			}
			else
			{
				Toast.makeText(getActivity(),getResources().getString(R.string.not_available_area), Toast.LENGTH_LONG).show();
			}
		}
		else
		{
			Utility.printLog("LoginResponse null");
		}

		view.setFocusableInTouchMode(true);
		view.requestFocus();
		view.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) 
			{
				Utility.printLog("", "onKey Back listener keyCode: " + keyCode);
				if(isBackPressed)
				{
					if(keyCode == KeyEvent.KEYCODE_BACK) 
					{
						Utility.printLog("", "onKey Back listener is working!!!");
						getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
						//getActivity().getActionBar().show();
						ResideMenuActivity.main_frame_layout.setVisibility(View.VISIBLE);
						//MainActivity.main_frame_layout.setVisibility(View.VISIBLE);
						isSetDropoffLocation=false;
						isBackPressed=false;
						isFareQuotePressed=false;
						to_latitude=null;
						to_longitude=null;
						mDROPOFF_ADDRESS=null;
						//isCardSelected=false;
						Dropoff_Location_Address.setText("");
						new_dropoff_location_address.setText("");
						// relative_now_later_status.setVisibility(View.VISIBLE);
						show_address_relative.setVisibility(View.VISIBLE);
						Txt_Pickup.setVisibility(View.VISIBLE);
						pickup.setVisibility(View.VISIBLE);
						//setLocationArrow.setVisibility(View.VISIBLE);
						all_types_layout.setVisibility(View.VISIBLE);
						relativePickupLocation.setVisibility(View.INVISIBLE);
						Request_Pick_up_here.setVisibility(View.INVISIBLE);
						Fare_Quote.setVisibility(View.INVISIBLE);
						farePromoLayouy.setVisibility(View.INVISIBLE);
						//=============================My Change=================================
						req_lay.setVisibility(View.INVISIBLE);
						//=============================My Change=================================
						Relative_Card_Info.setVisibility(View.GONE);

						Card_Info.setVisibility(View.INVISIBLE);
						Card_Image.setVisibility(View.INVISIBLE);
						Relative_Dropoff_Location.setVisibility(View.INVISIBLE);
						Relative_Pickup_Navigation.setVisibility(View.INVISIBLE);
						now_later_layout.setVisibility(View.VISIBLE);

						LatLng latLng = new LatLng(currentLatitude,currentLongitude);
						googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
						return true;
					} 
					else 
					{
						return false;
					}
				}
				else
				{
					return false;
				}
			}
		});

		//if(session.isDriverOnWay() || session.isDriverOnArrived() || session.isTripBegin() || session.isInvoiceRaised())
		{

		}

		//polling for every 15 sec
		/*pollingTimer.scheduleAtFixedRate(new TimerTask() 
		    { 
		        public void run() 
		        { 
		        	Utility.printLog("timer for 10 seconds");
		        	AppStatus();
		        } 
		    }, delay, period); */

		googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition arg0) {

				float maxZoom = 15.0f;
				float minZoom = 2.0f;

				if (arg0.zoom > maxZoom) {
					googleMap.animateCamera(CameraUpdateFactory.zoomTo(maxZoom));
				} else if (arg0.zoom < minZoom) {
					googleMap.animateCamera(CameraUpdateFactory.zoomTo(minZoom));
				}

				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					public void run() {


						IsreturnFromSearch=false;


					}
				}, 5000);

			}
		});
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		Log.i("","INSDE onCreateOptionsMenu profile");
		//getActivity().getActionBar().addTab(tab, , setSelected);
		//inflater.inflate(R.menu.now_later_buttons, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{Utility.printLog("inside options onOptionsItemSelected");
	if(item.getItemId()==R.id.later_book)
	{
		//later.setTitle(R.string.later_selected);
		//now.setTitle(R.string.now_unselected);
		final TimePicker timep;
		final DatePicker datep;
		final Dialog picker ;
		Button set;
		Typeface roboto_condensed = Typeface.createFromAsset(getActivity().getAssets(),"fonts/BebasNeue.otf");


		picker = new Dialog(getActivity());
		picker.setContentView(R.layout.picker_frag);
		picker.setTitle("Select Date and Time");
		datep = (DatePicker)picker.findViewById(R.id.datePicker);
		timep = (TimePicker)picker.findViewById(R.id.timePicker);
		set = (Button)picker.findViewById(R.id.btnSet);

		//============================My Change 15 April==========================

	    	set.setTypeface(roboto_condensed);
		//============================My Change 15 April==========================
		datep.setMinDate(System.currentTimeMillis() - 1000);
		try {
			java.lang.reflect.Field[] f = datep.getClass().getDeclaredFields();
			for (java.lang.reflect.Field field : f) {
				if (field.getName().equals("mYearSpinner")) 
				{
					field.setAccessible(true);
					Object yearPicker = new Object();
					yearPicker = field.get(datep);
					((View) yearPicker).setVisibility(View.GONE);
				}
			}
		} 
		catch (SecurityException e) {
			Log.d("ERROR", e.getMessage());
		} 
		catch (IllegalArgumentException e) {
			Log.d("ERROR", e.getMessage());
		} 
		catch (IllegalAccessException e) {
			Log.d("ERROR", e.getMessage());
		}

		set.setOnClickListener(new OnClickListener()
		{
			public void onClick(View view)
			{
				// later_month = String.valueOf(datep.getMonth());
				// later_day = String.valueOf(datep.getDayOfMonth());
				// later_year = String.valueOf(datep.getYear());
				later_hour = String.valueOf(timep.getCurrentHour());
				later_min = String.valueOf(timep.getCurrentMinute());

				if((String.valueOf(later_hour).length()) == 1)
				{
					later_hour = "0"+later_hour;
				}
				if((String.valueOf(later_min).length()) == 1)
				{
					later_min = "0"+later_min;
				}
				/*if((String.valueOf(month).length()) == 1)
	            {
	            	 month = "0"+month;
	            }
	            if((String.valueOf(day).length()) == 1)
	            {
	            	 day = "0"+day;
	            }*/

				laterBookingDate = later_year+"-"+later_month+"-"+later_day+" "+later_hour+":"+later_min+":00";
				Utility.printLog("laterBookingDate="+laterBookingDate);
				picker.dismiss();
				Toast.makeText(getActivity(),laterBookingDate, Toast.LENGTH_SHORT).show();
			}
		});
		picker.show();

		selectDate(view);
	}

	if(item.getItemId()==R.id.now_book)
	{
		laterBookingDate = null;
		//later.setTitle(R.string.later_unselected);
		//now.setTitle(R.string.now_selected);
	}	return super.onOptionsItemSelected(item);
	}


	public void selectDate(View view)
	{
		Utility.printLog("inside selectDate");
		DialogFragment newFragment = new SelectDateFragment();
		newFragment.show(getFragmentManager(), "DatePicker");
	}
	public void populateSetDate(int year, int month, int day) 
	{
		//"YYYY-MM-DD HH:MM:SS"
		String later_booking_date= year+"-"+month+"-"+day;
		later_year = String.valueOf(year);
		later_month = String.valueOf(month);
		later_day = String.valueOf(day);

		Utility.printLog("later_booking_date="+later_booking_date);
		Toast.makeText(getActivity(), "Appointment Date: "+(month+"/"+day+"/"+year), Toast.LENGTH_SHORT).show();
	}

	@SuppressLint("ValidFragment")
	public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener 
	{
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			final Calendar calendar = Calendar.getInstance();
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, yy, mm, dd);
		}

		public void onDateSet(DatePicker view, int yy, int mm, int dd) {
			populateSetDate(yy, mm+1, dd);
		}
	}

	private boolean validateTime(long current, long selected) {
		if(selected > current)
			return true;
		return false;
	}

	@Override
	public void onClick(View v)
	{
		if(v.getId()==R.id.now_button)
		{   

			laterBookingDate = null;
			//booked_now_later.setText("YOU ARE BOOKING FOR NOW");
			//now_later_status.setVisibility(View.VISIBLE);
			//relative_now_later_status.setVisibility(View.VISIBLE);

			now_button.setBackgroundResource(R.color.yellowrequestpick);
			later_button.setBackgroundResource(R.color.booknwltr);
			carnowimage.setVisibility(View.VISIBLE);
			carlatrimage.setVisibility(View.GONE);
		}
		if(v.getId()==R.id.later_button)
		{
			isSetClicked=false;

			now_button.setBackgroundResource(R.color.booknwltr);
			later_button.setBackgroundResource(R.color.yellowrequestpick);

			carnowimage.setVisibility(View.GONE);
			carlatrimage.setVisibility(View.VISIBLE);

			//relative_now_later_status.setVisibility(View.GONE);
			//now_button.setBackgroundResource(R.color.white);
			//later_button.setBackgroundResource(R.color.lightgrey1);

			//booked_now_later.setText("YOU ARE BOOKING FOR LATER");
			Intent dialIntent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"+"+"96170111308"));
			startActivity(dialIntent);
			//========================MY CHANGE=====================================

		/*	final TimePicker timep;
			final DatePicker datep;
			final Dialog picker ;
			final Button set,btncancel;

			picker = new Dialog(getActivity());
			picker.setContentView(R.layout.picker_frag);
			picker.setTitle("Select Date and Time");
			datep = (DatePicker)picker.findViewById(R.id.datePicker);
			timep = (TimePicker)picker.findViewById(R.id.timePicker);
			set = (Button)picker.findViewById(R.id.btnSet);
			btncancel = (Button) picker.findViewById(R.id.btncancel);




			//============================My Change 15 April==========================

			set.setTypeface(roboto_condensed);
			btncancel.setTypeface(roboto_condensed);
			//============================My Change 15 April==========================
			//Long abc=System.currentTimeMillis()+500L;
			Long nn=Calendar.getInstance().getTimeInMillis();
			int hours = new Time(System.currentTimeMillis()).getHours();
			hours++;
			timep.setCurrentHour(hours);
			timep.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
				@Override
				public void onTimeChanged(TimePicker timePicker, int i, int i1) {
					Calendar cal = Calendar.getInstance();
					Calendar calendar = Calendar.getInstance();
					calendar.clear();
					calendar.set(Calendar.YEAR, datep.getYear());
					calendar.set(Calendar.DAY_OF_MONTH, datep.getDayOfMonth());
					calendar.set(Calendar.MONTH, datep.getMonth());
					calendar.set(Calendar.HOUR_OF_DAY, timep.getCurrentHour());
					calendar.set(Calendar.MINUTE, timep.getCurrentMinute());

					if (validateTime(cal.getTimeInMillis() / 1000L, calendar.getTimeInMillis() / 1000L)) {
						SimpleDateFormat format = new SimpleDateFormat("hh:mm a dd/MMM/yyyy");
						Date date = calendar.getTime();
						set.setEnabled(true);
					} else {
						set.setEnabled(false);
						Utility.ShowAlert(getResources().getString(R.string.bookingsNotSelected), getActivity());
					}
				}
			});
			datep.setMinDate(nn - 1000);
			try 
			{
				java.lang.reflect.Field[] f = datep.getClass().getDeclaredFields();  
				for (java.lang.reflect.Field field : f) 
				{
					if (field.getName().equals("mYearSpinner")) 
					{
						field.setAccessible(true);
						Object yearPicker = new Object();
						yearPicker = field.get(datep);
						((View) yearPicker).setVisibility(View.GONE);
					}
				}
			} 
			catch (SecurityException e) 
			{
				Log.d("ERROR", e.getMessage());
			} 
			catch (IllegalArgumentException e)
			{
				Log.d("ERROR", e.getMessage());
			} 
			catch (IllegalAccessException e) 
			{
				Log.d("ERROR", e.getMessage());
			}

			btncancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v)
				{
					laterBookingDate = null;
					picker.dismiss();
				}
			});

			set.setOnClickListener(new OnClickListener()
			{
				public void onClick(View view)
				{   ResideMenuActivity.main_frame_layout.setVisibility(View.GONE);
				isSetClicked=true;
				//getActivity().getActionBar().hide();
				Relative_Pickup_Navigation.setVisibility(View.VISIBLE);
				String  hour = String.valueOf(timep.getCurrentHour());

				String min = String.valueOf(timep.getCurrentMinute());
				//relative_now_later_status.setVisibility(View.GONE);
				if((String.valueOf(hour).length()) == 1)
				{
					hour = "0"+hour;
				}
				if((String.valueOf(min).length()) == 1)
				{
					min = "0"+min;
				}

				int year = datep.getYear();
				int month = datep.getMonth()+1;
				int day = datep.getDayOfMonth();  

				laterBookingDate = year+"-"+month+"-"+day+" "+hour+":"+min+":00";
				//booked_now_later.setText("YOU ARE BOOKING FOR LATER : "+day+"-"+month+"-"+year+" "+hour+":"+min);

				Utility.printLog("laterBookingDate="+laterBookingDate);
				picker.dismiss();
				LatLng latLng = new LatLng(currentLatitude,currentLongitude);
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f));



				//MainActivity.main_frame_layout.setVisibility(View.GONE);
				//new BackgroundGetCards().execute();

				isBackPressed=true;
				show_address_relative.setVisibility(View.INVISIBLE);
				Txt_Pickup.setVisibility(View.INVISIBLE);
				pickup.setVisibility(View.INVISIBLE);
				//setLocationArrow.setVisibility(View.INVISIBLE);
				all_types_layout.setVisibility(View.INVISIBLE);
				now_later_layout.setVisibility(View.INVISIBLE);
				Request_Pick_up_here.setVisibility(View.VISIBLE);
				pickupLocationAddress.setText(mPICKUP_ADDRESS);
				pick_up=mPICKUP_ADDRESS;
				Request_Pick_up_here.setText("REQUEST ON "+day+"-"+month+"-"+year+" "+hour+":"+min);
				//Toast.makeText(getActivity(),laterBookingDate, Toast.LENGTH_SHORT).show();
				relativePickupLocation.setVisibility(View.VISIBLE);
				Relative_Dropoff_Location.setVisibility(View.VISIBLE);
				Fare_Quote.setVisibility(View.VISIBLE);
				farePromoLayouy.setVisibility(View.VISIBLE);
					//=============================My Change=================================
					req_lay.setVisibility(View.VISIBLE);
					//=============================My Change=================================
				Card_Info.setVisibility(View.VISIBLE);
				Relative_Card_Info.setVisibility(View.VISIBLE);
				Card_Image.setVisibility(View.VISIBLE);


				}
			});
			picker.show();*/

			//========================MY CHANGE=====================================//

			if(!isSetClicked){
				//relative_now_later_status.setVisibility(View.VISIBLE);
				laterBookingDate = null;
				//booked_now_later.setText("YOU ARE BOOKING FOR NOW");
				now_button.setBackgroundResource(R.color.yellowrequestpick);
				later_button.setBackgroundResource(R.color.booknwltr);
				carnowimage.setVisibility(View.VISIBLE);
				carlatrimage.setVisibility(View.GONE);
			}
		}
		/*if(v.getId()==R.id.map_hybrid_view)
		{
			if(googleMap.getMapType() != GoogleMap.MAP_TYPE_HYBRID)
			{
				googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			}
		}*/
		/*if(v.getId()==R.id.map_normal_view)
		{
			if(googleMap.getMapType() != GoogleMap.MAP_TYPE_NORMAL)
			{
				googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			}
		}*/
		if (v.getId()==R.id.address_search_button)
		{
			Intent addressIntent=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
			addressIntent.putExtra("chooser", getResources().getString(R.string.pickup_location));
			startActivityForResult(addressIntent, 18);
			getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			return;
		}
		if(v.getId()==R.id.relative_pickup_location)
		{
			Intent addressIntent=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
			addressIntent.putExtra("chooser", getResources().getString(R.string.pickup_location));
			startActivityForResult(addressIntent, 18);
			getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			return;
		}
		if(v.getId()==R.id.img_map)
		{
			Intent addressIntent=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
			addressIntent.putExtra("chooser", getResources().getString(R.string.pickup_location));
			startActivityForResult(addressIntent, 18);
			getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			return;
		}
		if(v.getId()==R.id.relative_dropoff_location)
		{
			/*Intent addressIntent=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
			addressIntent.putExtra("chooser", getResources().getString(R.string.drop_location));
			startActivityForResult(addressIntent, 16);
			//getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			return;*/
			if((from_latitude!=to_latitude || from_longitude!=to_longitude) && to_latitude!=null && to_longitude!=null)
			{
				isSetDropoffLocation=true;
			}

			if(isSetDropoffLocation)
			{
				Intent intent = new Intent(getActivity(),FareQuoteActivity.class);

				Utility.printLog("fare_quote from_latitude="+from_latitude+" from_longitude="+from_longitude+" to_latitude="+to_latitude+" to_longitude="+to_longitude);
				Utility.printLog("fare_quote pick_up add="+pick_up+" drop_off="+mDROPOFF_ADDRESS); 
				Utility.printLog("fare_quote id="+Car_Type_Id+" "+Car_Type_Id); 

				Utility.printLog("fare_quote from session = "+session.getPickuplat()+" "+session.getPickuplng()+" "+session.getDropofflat()
						+" "+session.getDropofflng());

				intent.putExtra("PICKUP_ADDRESS", mPICKUP_ADDRESS);
				intent.putExtra("DROPOFF_ADDRESS", mDROPOFF_ADDRESS);
				intent.putExtra("FromLatitude", from_latitude);
				intent.putExtra("FromLongitude", from_longitude);
				intent.putExtra("ToLatitude", to_latitude);
				intent.putExtra("ToLongitude", to_longitude);
				intent.putExtra("TypeId", Car_Type_Id);

				//startActivity(intent);

				startActivityForResult(intent, 1);
				getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			}
			else
			{
				isFareQuotePressed=true;
				Utility.printLog("fare_quote pick_up onclick="+from_latitude+" "+from_longitude); 

				Relative_Dropoff_Location.setVisibility(View.VISIBLE);
				Intent addressIntent1=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
				addressIntent1.putExtra("chooser", getResources().getString(R.string.drop_location));
				startActivityForResult(addressIntent1, 16);
				getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			}
			return;
		}

		//Akbar for updating the dropoff location after booking accepted by driver		

		if(v.getId()==R.id.new_relative_dropoff_location)
		{
			Relative_Dropoff_Location.setVisibility(View.GONE);
			//session.setOldDropOff(false);
			Intent addressIntent=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
			addressIntent.putExtra("chooser", getResources().getString(R.string.drop_location));
			startActivityForResult(addressIntent, 16);
			getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			return;
			/*if((from_latitude!=to_latitude || from_longitude!=to_longitude) && to_latitude!=null && to_longitude!=null)
			{
				isSetDropoffLocation=true;
			}

			if(isSetDropoffLocation)
			{
				Intent intent = new Intent(getActivity(),FareQuoteActivity.class);

				Utility.printLog("fare_quote from_latitude="+from_latitude+" from_longitude="+from_longitude+" to_latitude="+to_latitude+" to_longitude="+to_longitude);
				Utility.printLog("fare_quote pick_up add="+pick_up+" drop_off="+mDROPOFF_ADDRESS); 
				Utility.printLog("fare_quote id="+Car_Type_Id+" "+Car_Type_Id); 

				Utility.printLog("fare_quote from session = "+session.getPickuplat()+" "+session.getPickuplng()+" "+session.getDropofflat()
						+" "+session.getDropofflng());

				intent.putExtra("PICKUP_ADDRESS", mPICKUP_ADDRESS);
				intent.putExtra("DROPOFF_ADDRESS", mDROPOFF_ADDRESS);
				intent.putExtra("FromLatitude", from_latitude);
				intent.putExtra("FromLongitude", from_longitude);
				intent.putExtra("ToLatitude", to_latitude);
				intent.putExtra("ToLongitude", to_longitude);
				intent.putExtra("TypeId", Car_Type_Id);

				//startActivity(intent);

				startActivityForResult(intent, 1);
				getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			}
			else
			{
				isFareQuotePressed=true;
				Utility.printLog("fare_quote pick_up onclick="+from_latitude+" "+from_longitude); 

				Relative_Dropoff_Location.setVisibility(View.INVISIBLE);
				Intent addressIntent1=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
				addressIntent1.putExtra("chooser", getResources().getString(R.string.drop_location));
				startActivityForResult(addressIntent1, 16);
				getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			}
			return;*/
		}


		if(v.getId()==R.id.new_add_drop_off_location)
		{
			//session.setOldDropOff(false);
			if(AddLocation.getText().toString().trim().equals("+"))
			{
				Relative_Dropoff_Location.setVisibility(View.GONE);
				Intent addressIntent=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
				addressIntent.putExtra("chooser", getResources().getString(R.string.drop_location));
				startActivityForResult(addressIntent, 16);
				getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_down_acvtivity);
			}
			else
			{
				Dropoff_Location_Address.setText("");
				new_dropoff_location_address.setText("");
				AddLocation.setText("+");
				to_latitude = null;
				to_longitude = null;
				mDROPOFF_ADDRESS=null;
				//newDropAddress=null;
				isSetDropoffLocation=false;
			}
			return;
		}
		
		
		if(v.getId()==R.id.new_img_dropoff)
		{
			Relative_Dropoff_Location.setVisibility(View.GONE);
			//session.setOldDropOff(false);
			Intent addressIntent=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
			addressIntent.putExtra("chooser", getResources().getString(R.string.drop_location));
			startActivityForResult(addressIntent, 16);
			getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			return;
			
			/*if((from_latitude!=to_latitude || from_longitude!=to_longitude) && to_latitude!=null && to_longitude!=null)
			{
				isSetDropoffLocation=true;
			}

			if(isSetDropoffLocation)
			{
				Intent intent = new Intent(getActivity(),FareQuoteActivity.class);

				Utility.printLog("fare_quote from_latitude="+from_latitude+" from_longitude="+from_longitude+" to_latitude="+to_latitude+" to_longitude="+to_longitude);
				Utility.printLog("fare_quote pick_up add="+pick_up+" drop_off="+mDROPOFF_ADDRESS); 
				Utility.printLog("fare_quote id="+Car_Type_Id+" "+Car_Type_Id); 

				Utility.printLog("fare_quote from session = "+session.getPickuplat()+" "+session.getPickuplng()+" "+session.getDropofflat()
						+" "+session.getDropofflng());

				intent.putExtra("PICKUP_ADDRESS", mPICKUP_ADDRESS);
				intent.putExtra("DROPOFF_ADDRESS", mDROPOFF_ADDRESS);
				intent.putExtra("FromLatitude", from_latitude);
				intent.putExtra("FromLongitude", from_longitude);
				intent.putExtra("ToLatitude", to_latitude);
				intent.putExtra("ToLongitude", to_longitude);
				intent.putExtra("TypeId", Car_Type_Id);

				//startActivity(intent);

				startActivityForResult(intent, 1);
				getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			}
			else
			{
				isFareQuotePressed=true;
				Utility.printLog("fare_quote pick_up onclick="+from_latitude+" "+from_longitude); 

				Relative_Dropoff_Location.setVisibility(View.INVISIBLE);
				Intent addressIntent1=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
				addressIntent1.putExtra("chooser", getResources().getString(R.string.drop_location));
				startActivityForResult(addressIntent1, 16);
				getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			}
			return;*/
		}

		

		if(v.getId()==R.id.img_dropoff)
		{
			/*Intent addressIntent=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
			addressIntent.putExtra("chooser", getResources().getString(R.string.drop_location));
			startActivityForResult(addressIntent, 16);
			//getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			return;*/
			if((from_latitude!=to_latitude || from_longitude!=to_longitude) && to_latitude!=null && to_longitude!=null)
			{
				isSetDropoffLocation=true;
			}

			if(isSetDropoffLocation)
			{
				Intent intent = new Intent(getActivity(),FareQuoteActivity.class);

				Utility.printLog("fare_quote from_latitude="+from_latitude+" from_longitude="+from_longitude+" to_latitude="+to_latitude+" to_longitude="+to_longitude);
				Utility.printLog("fare_quote pick_up add="+pick_up+" drop_off="+mDROPOFF_ADDRESS); 
				Utility.printLog("fare_quote id="+Car_Type_Id+" "+Car_Type_Id); 

				Utility.printLog("fare_quote from session = "+session.getPickuplat()+" "+session.getPickuplng()+" "+session.getDropofflat()
						+" "+session.getDropofflng());

				intent.putExtra("PICKUP_ADDRESS", mPICKUP_ADDRESS);
				intent.putExtra("DROPOFF_ADDRESS", mDROPOFF_ADDRESS);
				intent.putExtra("FromLatitude", from_latitude);
				intent.putExtra("FromLongitude", from_longitude);
				intent.putExtra("ToLatitude", to_latitude);
				intent.putExtra("ToLongitude", to_longitude);
				intent.putExtra("TypeId", Car_Type_Id);

				//startActivity(intent);

				startActivityForResult(intent, 1);
				getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			}
			else
			{
				isFareQuotePressed=true;
				Utility.printLog("fare_quote pick_up onclick="+from_latitude+" "+from_longitude); 

				Relative_Dropoff_Location.setVisibility(View.VISIBLE);
				Intent addressIntent1=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
				addressIntent1.putExtra("chooser", getResources().getString(R.string.drop_location));
				startActivityForResult(addressIntent1, 16);
				getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			}
			return;

		}

		if(v.getId()==R.id.go_to_current_position)
		{
			if(isGpsEnable)
			{
				if(latitude==0.0 || longitude==0.0)
				{
					Toast.makeText(getActivity(), "Location not found...",Toast.LENGTH_SHORT).show();
				}
				else
				{
					LatLng latLng = new LatLng(latitude,longitude);
					//googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
					googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
				}
			}
			else
			{
				showGpsAlert();
			}

			return;
		}

		if(v.getId()==R.id.relative_center)
		{
			if(car_type_index==0)
			{
				nearestDrivers = type1NearestDrivers;
			}
			else if(car_type_index==1)
			{
				nearestDrivers = type2NearestDrivers;
			}
			else if(car_type_index==2)
			{
				nearestDrivers = type3NearestDrivers;
			}
			else if(car_type_index==3)
			{
				nearestDrivers = type4NearestDrivers;
			}
			if((nearestDrivers!=null) && (nearestDrivers.size()>0))
			{
				LatLng latLng = new LatLng(currentLatitude,currentLongitude);
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f));

				//getActivity().getActionBar().hide();
				ResideMenuActivity.main_frame_layout.setVisibility(View.GONE);
				//relative_now_later_status.setVisibility(View.GONE);
				//new BackgroundGetCards().execute();
				isBackPressed=true;
				show_address_relative.setVisibility(View.GONE);
				Txt_Pickup.setVisibility(View.INVISIBLE);
				pickup.setVisibility(View.INVISIBLE);
				//setLocationArrow.setVisibility(View.INVISIBLE);
				all_types_layout.setVisibility(View.INVISIBLE);
				now_later_layout.setVisibility(View.INVISIBLE);
				pickupLocationAddress.setText(mPICKUP_ADDRESS);
				pick_up=mPICKUP_ADDRESS;

				if(car_name!=null)
				{
					if(laterBookingDate!=null)
					{
						String[] StartTime1 = laterBookingDate.split(" ");
						String[] date = StartTime1[0].split("-");
						String[] time = StartTime1[1].split(":");

						String hour = time[0];
						String min = time[1];

						String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

						if(Integer.parseInt(hour)==12)
						{
							String later_date = date[2]+" "+MONTHS[Integer.parseInt(date[1])-1]+" "+date[0]+" "+hour+":"+min+" PM";
							Request_Pick_up_here.setText("REQUEST ON "+later_date);
						}
						else if(Integer.parseInt(hour)>12)
						{
							int hr = Integer.parseInt(hour)-12;
							String later_date = date[2]+" "+MONTHS[Integer.parseInt(date[1])-1]+" "+date[0]+" "+hr+":"+min+" PM";
							Request_Pick_up_here.setText("REQUEST ON "+later_date);
						}
						else
						{
							String later_date = date[2]+" "+MONTHS[Integer.parseInt(date[1])-1]+" "+date[0]+" "+hour+":"+min+" AM";
							Request_Pick_up_here.setText("REQUEST ON "+later_date);
						}


					}
					else
					{
					/*	Request_Pick_up_here.setText("REQUEST "+car_name);*/
						//======================My Change======================
						Request_Pick_up_here.setText("REQUEST NOW");
						car_color.setText(car_name);
						seater.setText(car_size +" SEATES");
						//======================My Change======================
					}

				}
				else
				{
					Request_Pick_up_here.setText("REQUEST PICKUP");
				}
				relativePickupLocation.setVisibility(View.VISIBLE);
				Request_Pick_up_here.setVisibility(View.VISIBLE);
				Relative_Dropoff_Location.setVisibility(View.VISIBLE);
				Fare_Quote.setVisibility(View.VISIBLE);
				farePromoLayouy.setVisibility(View.VISIBLE);
				//=============================My Change=================================
				req_lay.setVisibility(View.VISIBLE);
				//=============================My Change=================================
	/*			Card_Info.setVisibility(View.VISIBLE);
				Relative_Card_Info.setVisibility(View.VISIBLE);
				Card_Image.setVisibility(View.VISIBLE);*/

				Relative_Pickup_Navigation.setVisibility(View.VISIBLE);
			}
			else
			{
				//Utility.ShowAlert("Drivers not found...", getActivity());
			}
		}
		//======================my change=================================
		//if(v.getId()==R.id.rl_homepage)
		//======================my change=================================
		if(v.getId()==R.id.txt_cancel)
		{
			//getActivity().getActionBar().show();
			ResideMenuActivity.main_frame_layout.setVisibility(View.VISIBLE);
			isSetDropoffLocation=false;
			isBackPressed=false;
			isFareQuotePressed=false;
			to_latitude=null;
			to_longitude=null;
			mDROPOFF_ADDRESS=null;
			//isCardSelected=false;
			Dropoff_Location_Address.setText("");
			new_dropoff_location_address.setText("");
			show_address_relative.setVisibility(View.VISIBLE);
			Txt_Pickup.setVisibility(View.VISIBLE);
			pickup.setVisibility(View.VISIBLE);
			//setLocationArrow.setVisibility(View.VISIBLE);
			all_types_layout.setVisibility(View.VISIBLE);
			now_later_layout.setVisibility(View.VISIBLE);
			relativePickupLocation.setVisibility(View.INVISIBLE);
			Request_Pick_up_here.setVisibility(View.INVISIBLE);
			Fare_Quote.setVisibility(View.INVISIBLE);
			farePromoLayouy.setVisibility(View.INVISIBLE);
			//=============================My Change=================================
			req_lay.setVisibility(View.INVISIBLE);
			//=============================My Change=================================

			Relative_Card_Info.setVisibility(View.GONE);
			Card_Info.setVisibility(View.INVISIBLE);
			Card_Image.setVisibility(View.INVISIBLE);
			Relative_Dropoff_Location.setVisibility(View.INVISIBLE);
			Relative_Pickup_Navigation.setVisibility(View.INVISIBLE);

			LatLng latLng = new LatLng(currentLatitude,currentLongitude);
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
			//======================my change=================================
			return;
			//======================my change=================================
		}

		if(v.getId()==R.id.add_drop_off_location)
		{
			if(AddLocation.getText().toString().trim().equals("+"))
			{
				Intent addressIntent=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
				addressIntent.putExtra("chooser", getResources().getString(R.string.drop_location));
				startActivityForResult(addressIntent, 16);
				getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_down_acvtivity);
			}
			else
			{
				Dropoff_Location_Address.setText("");
				new_dropoff_location_address.setText("");
				AddLocation.setText("+");
				to_latitude = null;
				to_longitude = null;
				mDROPOFF_ADDRESS=null;
				isSetDropoffLocation=false;
			}
			return;
		}
		/*if(v.getId() == R.id.promo_code)
		{
			showPromoCodeAlert();
		}*/
		//======================my change=================================
		/*if(v.getId()==R.id.relative_card_info)
		{

			choose_payment_layout.setVisibility(View.VISIBLE);
			YoYo.with(Techniques.SlideInUp)
					.duration(700)
					.playOn(view.findViewById(R.id.choose_payment_screen));


			return;
			//showAlert();
			//getActivity().setTheme(R.style.ActionSheetStyleIOS7);
			//showActionSheet();
		}
*/
        //======================my change=================================
		if(v.getId()==R.id.relative_card_info)
		{
			choose_payment_layout.setVisibility(View.VISIBLE);
		YoYo.with(Techniques.SlideInUp)
					.duration(700)
					.playOn(view.findViewById(R.id.choose_payment_screen));

			return;
		}

		if(v.getId()==R.id.relative_payment_info)
		{
			choose_payment_layout.setVisibility(View.VISIBLE);
			YoYo.with(Techniques.SlideInUp)
					.duration(700)
					.playOn(view.findViewById(R.id.choose_payment_screen));

			return;
		}



		if(v.getId()==R.id.payment_cash)
		{


			//choose_payment_layout.setVisibility(View.GONE);

			//========================My Change========================
		/*	YoYo.with(Techniques.SlideOutDown)
					.duration(700)
					.playOn(view.findViewById(R.id.choose_payment_screen));

			payment_type="2";
			Card_Info.setText("CASH");
			Card_Image.setImageBitmap(null);*/

     //========================My Change========================
			card_info="Cash";
			pay_cash.setTextColor(Color.BLACK);
			pay_card.setTextColor(Color.GRAY);
      //========================My Change========================
			return;
		}
		if(v.getId()==R.id.payment_card)
		{
			//choose_payment_layout.setVisibility(View.GONE);
		/*	YoYo.with(Techniques.SlideOutDown)
			.duration(700)
			.playOn(view.findViewById(R.id.choose_payment_screen));
			Intent addressIntent=new Intent(getActivity(), ChangeCardActivity.class);
			startActivityForResult(addressIntent, 17);
			getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_up);*/
			//========================My Change========================
			card_info="Card";
			pay_cash.setTextColor(Color.GRAY);
			pay_card.setTextColor(Color.BLACK);
			//========================My Change========================
			return;
		}
		if(v.getId()==R.id.payment_cancel)
		{
			//choose_payment_layout.setVisibility(View.GONE);
			YoYo.with(Techniques.SlideOutDown)
			.duration(700)
			.playOn(view.findViewById(R.id.choose_payment_screen));
			return;
		}
		//========================My Change========================

		if(v.getId()==R.id.payment_done)
		{
			//choose_payment_layout.setVisibility(View.GONE);

			if (card_info.equalsIgnoreCase("Cash"))
			{
				Relative_Card_Info.setVisibility(View.GONE);
				Relative_Payment_Info.setVisibility(View.VISIBLE);
				YoYo.with(Techniques.SlideOutDown)
						.duration(700)
						.playOn(view.findViewById(R.id.choose_payment_screen));

				payment_type="2";
				Card_Info.setText("CASH");
				//Card_Image.setImageBitmap(null);
			}
			else {
				Card_Info.setText("PAYMENT TYPE");
				Relative_Card_Info.setVisibility(View.VISIBLE);
				Relative_Payment_Info.setVisibility(View.GONE);
				YoYo.with(Techniques.SlideOutDown)
						.duration(700)
						.playOn(view.findViewById(R.id.choose_payment_screen));
				Intent addressIntent=new Intent(getActivity(), ChangeCardActivity.class);
				startActivityForResult(addressIntent, 17);
				getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_up);
			}

			return;
		}
		//========================My Change========================



		if(v.getId()==R.id.choose_payment_screen)
		{
			choose_payment_layout.setVisibility(View.INVISIBLE);
			YoYo.with(Techniques.SlideOutDown)
			.duration(700)
			.playOn(view.findViewById(R.id.choose_payment_screen));
			return;
		}

		if(v.getId()==R.id.fare_promo_layouy)
		{
			Utility.printLog("fare_quote before from_latitude="+from_latitude+" from_longitude="+from_longitude+" to_latitude="+to_latitude+" to_longitude="+to_longitude);

			if((from_latitude!=to_latitude || from_longitude!=to_longitude) && to_latitude!=null && to_longitude!=null)
			{
				isSetDropoffLocation=true;
			}

			if(isSetDropoffLocation)
			{
				Intent intent = new Intent(getActivity(),FareQuoteActivity.class);

				Utility.printLog("fare_quote from_latitude="+from_latitude+" from_longitude="+from_longitude+" to_latitude="+to_latitude+" to_longitude="+to_longitude);
				Utility.printLog("fare_quote pick_up add="+pick_up+" drop_off="+mDROPOFF_ADDRESS); 
				Utility.printLog("fare_quote id="+Car_Type_Id+" "+Car_Type_Id); 

				Utility.printLog("fare_quote from session = "+session.getPickuplat()+" "+session.getPickuplng()+" "+session.getDropofflat()
						+" "+session.getDropofflng());

				intent.putExtra("PICKUP_ADDRESS", mPICKUP_ADDRESS);
				intent.putExtra("DROPOFF_ADDRESS", mDROPOFF_ADDRESS);
				intent.putExtra("FromLatitude", from_latitude);
				intent.putExtra("FromLongitude", from_longitude);
				intent.putExtra("ToLatitude", to_latitude);
				intent.putExtra("ToLongitude", to_longitude);
				intent.putExtra("TypeId", Car_Type_Id);

				//startActivity(intent);

				startActivityForResult(intent, 1);
				getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			}
			else
			{
				isFareQuotePressed=true;
				Utility.printLog("fare_quote pick_up onclick="+from_latitude+" "+from_longitude); 

				Relative_Dropoff_Location.setVisibility(View.VISIBLE);
				Intent addressIntent=new Intent(getActivity(), SearchAddressGooglePlacesActivity.class);
				addressIntent.putExtra("chooser", getResources().getString(R.string.drop_location));
				startActivityForResult(addressIntent, 16);
				getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			}
			//======================my change=================================
			return;
			//======================my change=================================
		}

		if(v.getId()==R.id.request_pick_up_here)
		{


			if(car_type_index==0)
			{
				nearestDrivers = type1NearestDrivers;
			}
			else if(car_type_index==1)
			{
				nearestDrivers = type2NearestDrivers;
			}
			else if(car_type_index==2)
			{
				nearestDrivers = type3NearestDrivers;
			}
			else if(car_type_index==3)
			{
				nearestDrivers = type4NearestDrivers;
			}
			//add these lines when drop location is mandatory
			//if((nearestDrivers.size()>0) && (isSetDropoffLocation) && (payment_type!=null ) && (from_latitude!=to_latitude) && (from_longitude!=to_longitude))
			if((payment_type!=null ) && (from_latitude!=to_latitude) && (from_longitude!=to_longitude))
			{
				if(laterBookingDate!=null )
				{
					sendLaterBooking();

					/*	SessionManager session = new SessionManager(getActivity());
					session.setBookingCancelled(false);

					AddLocation.setText("+");
					Intent intent = new Intent(getActivity(),RequestPickupLater.class);

					Utility.printLog("request_pick_up_here from_latitude="+from_latitude+" "+from_longitude);
					Utility.printLog("request_pick_up_here car id="+Car_Type_Id);
					Utility.printLog("request_pick_up_here payment_type="+payment_type);
					double lat =  Double.parseDouble(from_latitude);
					double lon = Double.parseDouble(from_longitude);
					Utility.printLog("request_pick_up_here lat="+lat+" "+lon);
					String zipcode=getAddress(getActivity(),lat,lon);
					Utility.printLog("zip="+zipcode);
					if(zipcode!=null)
					{

					}
					else
					{
						zipcode = "560024";
					}

					intent.putExtra("PICKUP_ADDRESS", mPICKUP_ADDRESS);
					intent.putExtra("FromLatitude", from_latitude);
					intent.putExtra("FromLongitude", from_longitude);
					if(mDROPOFF_ADDRESS!=null && !mDROPOFF_ADDRESS.isEmpty())
					{
						intent.putExtra("DROPOFF_ADDRESS", mDROPOFF_ADDRESS);
						intent.putExtra("ToLatitude", to_latitude);
						intent.putExtra("ToLongitude", to_longitude);
					}

					intent.putExtra("my_drivers", nearestDrivers);
					intent.putExtra("Zipcode", zipcode);
					intent.putExtra("Car_Type", Car_Type_Id);
					intent.putExtra("PAYMENT_TYPE", payment_type);
					intent.putExtra("Later_Booking_Date", laterBookingDate);  
					if(isCouponValid)
					{
						intent.putExtra("coupon", mPromoCode);
					}

					session.storePickuplat(from_latitude);
					session.storePickuplng(from_longitude);
					session.storeDropofflat(to_latitude);
					session.storeDropofflng(to_longitude);

					for(int i=0;i<nearestDrivers.size();i++)
					{
						Utility.printLog("RequestPickup nearestDrivers onClick at "+i+" "+nearestDrivers.get(i));
					}



					startActivityForResult(intent,20);*/

				}
				else
				{
					SessionManager session = new SessionManager(getActivity());
					session.setBookingCancelled(false);

					AddLocation.setText("+");
					Intent intent = new Intent(getActivity(),RequestPickup.class);

					Log.e("request_pick_up_here",""+from_latitude+" "+from_longitude);
					Utility.printLog("request_pick_up_here from_latitude="+from_latitude+" "+from_longitude);
					Utility.printLog("request_pick_up_here car id="+Car_Type_Id);
					Utility.printLog("request_pick_up_here payment_type="+payment_type);
					double lat =  Double.parseDouble(from_latitude);
					double lon = Double.parseDouble(from_longitude);
					Utility.printLog("request_pick_up_here lat="+lat+" "+lon);
					String zipcode=getAddress(getActivity(),lat,lon);
					Utility.printLog("zip="+zipcode);
					if(zipcode!=null)
					{

					}
					else
					{
						zipcode = "560024";
					}

					intent.putExtra("PICKUP_ADDRESS", mPICKUP_ADDRESS);
					intent.putExtra("FromLatitude", from_latitude);
					intent.putExtra("FromLongitude", from_longitude);
					if(mDROPOFF_ADDRESS!=null && !mDROPOFF_ADDRESS.isEmpty())
					{
						intent.putExtra("DROPOFF_ADDRESS", mDROPOFF_ADDRESS);
						intent.putExtra("ToLatitude", to_latitude);
						intent.putExtra("ToLongitude", to_longitude);
					}
					intent.putExtra("my_drivers", nearestDrivers);
					intent.putExtra("Zipcode", zipcode);
					intent.putExtra("Car_Type", Car_Type_Id);
					intent.putExtra("PAYMENT_TYPE", payment_type);
					intent.putExtra("Later_Booking_Date", laterBookingDate);
					if(isCouponValid)
					{
						intent.putExtra("coupon", mPromoCode);
						mPromoCode = null;
						//promeCode.setText(getResources().getString(R.string.promo_code));
					}



					session.storePickuplat(from_latitude);
					session.storePickuplng(from_longitude);
					session.storeDropofflat(to_latitude);
					session.storeDropofflng(to_longitude);

					for(int i=0;i<nearestDrivers.size();i++)
					{
						Utility.printLog("RequestPickup nearestDrivers onClick at "+i+" "+nearestDrivers.get(i));
					}

					startActivityForResult(intent,19);
				}


			}

			else if(payment_type==null)
			{
				Utility.ShowAlert(getResources().getString(R.string.select_payment_type), getActivity());
			}
  //======================my change=================================
			return;
	//======================my change=================================
		}

		/*if(v.getId()==R.id.driver_arrow)
		{
			//showPopupForDriverMenu();
			//popup_driver.showAtLocation(popupLayout,Gravity.CENTER, 0,0); 

			driver_parent.setVisibility(View.VISIBLE);
		}*/

		if(v.getId()==R.id.share_eta)
		{
			//driver_parent.setVisibility(View.GONE);
			showPopupForShare();
			popup_share.showAtLocation(popupLayout,Gravity.CENTER, 0,0);
			//======================my change=================================
			return;
			//======================my change=================================
			//popup_driver.dismiss();
		}


		if(v.getId()==R.id.contact_driver)	
		{
			SessionManager session = new SessionManager(mActivity);
			//popup_driver.dismiss();
			//driver_parent.setVisibility(View.GONE);
			Utility.printLog("phone num="+session.getDocPH());
			if(session.getDocPH()!=null)
			{
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:"+session.getDocPH()));
				mActivity.startActivity(callIntent);
			}
			else
			{
				Toast.makeText(mActivity, getResources().getString(R.string.driver_phone_num_not_available), Toast.LENGTH_SHORT).show();
			}
			//======================my change=================================
			return;
			//======================my change=================================
		}

		if(v.getId()==R.id.cancel_trip)
		{
			//driver_parent.setVisibility(View.GONE);
			CancelAppointment();
			//======================my change=================================
			return;
			//======================my change=================================
		}


		if(v.getId()==R.id.message_share)
		{
			popup_share.dismiss();
			String smsBody="Track my journey in"+ getResources().getString(R.string.app_name)+ "@"+"\n"+session.getShareLink();

			Intent intentsms = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:"));
			intentsms.putExtra( "sms_body", smsBody);
			startActivity( intentsms );
			//showPopupForShare();
			//======================my change=================================
			return;
			//======================my change=================================
		}
		if(v.getId()==R.id.whatsapp_share)
		{
			popup_share.dismiss();

			PackageManager pm = getActivity().getPackageManager();
			try 
			{

				String smsBody="Track my journey in"+ getResources().getString(R.string.app_name)+ "@"+"\n"+session.getShareLink();
				Intent waIntent = new Intent(Intent.ACTION_SEND);
				waIntent.setType("text/plain");

				PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
				//Check if package exists or not. If not then code 
				//in catch block will be called
				waIntent.setPackage("com.whatsapp");

				waIntent.putExtra(Intent.EXTRA_TEXT, smsBody);
				startActivity(Intent.createChooser(waIntent, "Share with"));

			} catch (NameNotFoundException e) {
				Toast.makeText(getActivity(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
			}
//======================my change=================================
			return;
			//======================my change=================================
		}

		if(v.getId()==R.id.email_share)
		{
			popup_share.dismiss();

			File _sdCard = Utility.getSdCardPath();
			Log.i("SD CARD PATH!!", ""+_sdCard);        

			//creating directory
			File _picDir  = new File(_sdCard, getResources().getString(R.string.imagedire));                                
			Utility.deleteNon_EmptyDir(_picDir);

			//create file
			File imageFile= Utility.createFile(getResources().getString(R.string.imagedire), (getResources().getString(R.string.imagefilename)+"_twit"+".jpg"));

			//get a bitmap object from this image
			Bitmap bm = BitmapFactory.decodeResource( getResources(), R.drawable.ic_launcher);
			OutputStream outStream = null;

			try {
				outStream = new FileOutputStream(imageFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);

			try {
				outStream.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				outStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//Storing path in a variable "image_path"
			//String image_path=imageFile.getAbsolutePath();

			String smsBody="Track my journey in"+ getResources().getString(R.string.app_name)+ "@"+"\n"+session.getShareLink();
			String mail="";
			Uri uri = Uri.fromFile(new File(imageFile.getAbsolutePath()));
			Intent email = new Intent(Intent.ACTION_SEND/*Intent.ACTION_VIEW*/);
			email.setType("message/rfc822");
			email.putExtra(Intent.EXTRA_SUBJECT,getResources().getString(R.string.app_name));
			email.putExtra(Intent.EXTRA_STREAM, uri);
			email.putExtra(Intent.EXTRA_EMAIL,mail); 
			email.putExtra(Intent.EXTRA_TEXT, smsBody);
			startActivity(Intent.createChooser(email, "Choose an Email client :"));
			//======================my change=================================
			return;
			//======================my change=================================
		}
		if(v.getId()==R.id.cancel_share)
		{
			popup_share.dismiss();
			//======================my change=================================
			return;
			//======================my change=================================
		}
		/*if(v.getId()==R.id.driver_parent)
		{
			driver_parent.setVisibility(View.GONE);
			//popup_driver.dismiss();
		}*/
	}


	private void showPromoCodeAlert()
	{
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.promo_code_layout);

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;

		dialog.getWindow().setLayout((6 * width)/7, LayoutParams.WRAP_CONTENT);

		final EditText text = (EditText) dialog.findViewById(R.id.et_promoCode);
		Button submit = (Button) dialog.findViewById(R.id.apply_promo);
		submit.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Utility.printLog("user msg="+text.getText().toString().trim());
				if(text.getText().toString().trim().equals(""))
				{
					Utility.ShowAlert(getResources().getString(R.string.pls_enter_promo_code), getActivity());
				}
				else
				{
					dialog.dismiss();
					ValidatePromoCode(text.getText().toString().trim());
				}
			}
		});

		Button cancel = (Button) dialog.findViewById(R.id.cancel_promo);
		//=======================My change 15 April====================
		text.setTypeface(roboto_condensed);
		submit.setTypeface(roboto_condensed);
		cancel.setTypeface(roboto_condensed);
		//=======================My change 15 April====================


		cancel.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				dialog.dismiss();
			}
		});



		dialog.show();


	}

	private void ValidatePromoCode(final String promoCode)
	{
		dialogL= Utility.GetProcessDialogNew(mActivity,getResources().getString(R.string.please_wait));
		dialogL.setCancelable(true);
		if (dialogL!=null)
		{
			dialogL.show();
		}
		RequestQueue volleyRequest = Volley.newRequestQueue(mActivity);

		StringRequest myReq = new StringRequest(Request.Method.POST,VariableConstants.BASE_URL+"checkCoupon",
				new Listener<String>() {
			@Override
			public void onResponse(String response) 
			{

				// {"errNum":"101","errFlag":"0","errMsg":"Valid coupon","test":"15"}

				getCancelResponse = response;
				Utility.printLog("Success of getting ValidatePromoCode "+response);
				JSONObject jsnResponse;
				try 
				{
					dialogL.dismiss();
					jsnResponse = new JSONObject(response);
					String mErrNum = jsnResponse.getString("errFlag");
					Utility.printLog("jsonErrorParsing is ---> "+mErrNum);

					if(mErrNum.equals("0"))
					{
						mPromoCode = promoCode;
						isCouponValid = true;
						//promeCode.setText(promoCode);
					}
					else
					{  	
						Toast.makeText(mActivity,jsnResponse.getString("errMsg"), Toast.LENGTH_LONG).show();
						//promeCode.setText(getResources().getString(R.string.promo_code));
						mPromoCode = null;
						isCouponValid = false;
					}



				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 	new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error)
			{
				// TODO Auto-generated method stub
				dialogL.dismiss();
				mPromoCode = null;
				isCouponValid = false;
				Toast.makeText(mActivity, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
				Utility.printLog("Error for volley");
			}
		}){  
			protected HashMap<String,String> getParams() throws com.android.volley.AuthFailureError
			{  
				HashMap<String,String> kvPair = new HashMap<String, String>(); 

				SessionManager session=new SessionManager(mActivity);
				Utility utility=new Utility();
				String curenttime=utility.getCurrentGmtTime();
				Utility.printLog("ValidatePromoCode dataandTime="+curenttime);
				Utility.printLog("ValidatePromoCode promoCode="+promoCode);
				Utility.printLog("ValidatePromoCode getSessionToken()="+session.getSessionToken());
				Utility.printLog("ValidatePromoCode getDeviceId()="+session.getDeviceId());

				kvPair.put("ent_sess_token",session.getSessionToken() );
				kvPair.put("ent_dev_id",session.getDeviceId());
				kvPair.put("ent_coupon",promoCode);
				kvPair.put("ent_lat",""+currentLatitude);
				kvPair.put("ent_long",""+currentLongitude);
				kvPair.put("ent_date_time",curenttime);

				return kvPair;  
			};  
		};
		volleyRequest.add(myReq);
	}

	private void CancelAppointment()
	{
		dialogL= Utility.GetProcessDialogNew(mActivity,getResources().getString(R.string.cancelling_trip));
		dialogL.setCancelable(true);
		if (dialogL!=null)
		{
			dialogL.show();
		}
		RequestQueue volleyRequest = Volley.newRequestQueue(mActivity);

		StringRequest myReq = new StringRequest(Request.Method.POST,VariableConstants.BASE_URL+"cancelAppointment",
				new Listener<String>() {

			@Override
			public void onResponse(String response) 
			{
				// TODO Auto-generated method stub
				getCancelResponse = response;
				Utility.printLog("Success of getting user Info"+response);
				getCancelInfo();
			}
		}, 	new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error)
			{
				dialogL.dismiss();
				Toast.makeText(mActivity, getResources().getString(R.string.server_error)+error, Toast.LENGTH_LONG).show();
				Utility.printLog("Error for volley");
			}
		}){  
			protected HashMap<String,String> getParams() throws com.android.volley.AuthFailureError
			{  
				HashMap<String,String> kvPair = new HashMap<String, String>(); 

				SessionManager session=new SessionManager(mActivity);
				Utility utility=new Utility();
				String curenttime=utility.getCurrentGmtTime();
				Utility.printLog("dataandTime="+curenttime);

				kvPair.put("ent_sess_token",session.getSessionToken() );
				kvPair.put("ent_dev_id",session.getDeviceId());
				kvPair.put("ent_appnt_dt",session.getAptDate());
				kvPair.put("ent_dri_email",session.getDriverEmail());
				kvPair.put("ent_date_time",curenttime);

				return kvPair;  
			};  
		};
		volleyRequest.add(myReq);
	}

	private void getCancelInfo()
	{
		try
		{
			JSONObject jsnResponse = new JSONObject(getCancelResponse);

			String jsonErrorParsing = jsnResponse.getString("errFlag");

			Utility.printLog("jsonErrorParsing is ---> "+jsonErrorParsing);

			Gson gson = new Gson();
			CancelBooking = gson.fromJson(getCancelResponse, LiveBookingResponse.class);

			if(CancelBooking!=null)
			{
				if(CancelBooking.getErrFlag().equals("0"))
				{

					SessionManager session = new SessionManager(mActivity);
					session.setDriverOnWay(false);
					session.setBookingCancelled(true);
					Utility.printLog("Wallah set as false Homepage cancel 2");
					session.setDriverArrived(false);
					session.setTripBegin(false);
					session.setInvoiceRaised(false);
					session.storeAptDate(null);
					session.storeBookingId("0");
					//relative_now_later_status.setVisibility(View.VISIBLE);
					Toast.makeText(mActivity, CancelBooking.getErrMsg(), Toast.LENGTH_SHORT).show();
					dialogL.dismiss();
					Intent i = new Intent(mActivity, ResideMenuActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					mActivity.startActivity(i);
					mActivity.overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
				}
				else if(CancelBooking.getErrNum().equals("49"))
				{
					dialogL.dismiss();

					SessionManager session = new SessionManager(mActivity);
					session.setDriverOnWay(false);
					session.setBookingCancelled(true);
					Utility.printLog("Wallah set as false Homepage cancel 2");
					session.setDriverArrived(false);
					session.setTripBegin(false);
					session.setInvoiceRaised(false);
					session.storeAptDate(null);
					session.storeBookingId("0");
					//relative_now_later_status.setVisibility(View.VISIBLE);


					Toast.makeText(mActivity, CancelBooking.getErrMsg(), Toast.LENGTH_SHORT).show();
					dialogL.dismiss();
					Intent i = new Intent(mActivity, ResideMenuActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					mActivity.startActivity(i);
					mActivity.overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
					//Toast.makeText(mActivity, CancelBooking.getErrMsg(), Toast.LENGTH_SHORT).show();
				}
			}
		}
		catch(JSONException e)
		{
			Utility.printLog("exp "+e,"");
			e.printStackTrace();
			Utility.ShowAlert(getResources().getString(R.string.server_error), mActivity);
		}
	}


	public  String getAddress(Context ctx, double latitude, double longitude)
	{
		StringBuilder result = new StringBuilder();
		String Zipcode = null;
		try {
			Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
			List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
			if (addresses.size() > 0) {
				Address address = addresses.get(0);

				String locality=address.getLocality();
				String city=address.getCountryName();
				String region_code=address.getCountryCode();
				String zipcode=address.getPostalCode();
				Utility.printLog("zip="+zipcode);

				result.append(locality+" ");
				result.append(city+" "+ region_code+" ");
				result.append(zipcode);
			}
		}
		catch(IOException e) 
		{
			Utility.printLog("tag", e.getMessage());
		}

		return Zipcode;
	}



	private static void showSettingsAlert()
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);

		// Setting Dialog Title
		alertDialog.setTitle(mActivity.getResources().getString(R.string.gps_settings));

		// Setting Dialog Message
		alertDialog.setMessage(mActivity.getResources().getString(R.string.gps_alert_message));

		// On pressing Settings button
		alertDialog.setPositiveButton(mActivity.getResources().getString(R.string.settings), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog,int which) 
			{
				dialog.dismiss();
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				mActivity.startActivity(intent);
				mActivity.finish();
			}
		});

		// on pressing cancel button
		alertDialog.setNegativeButton(mActivity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
				mActivity.finish();
			}
		});

	}

	class BackgroundGeocodingTaskNew extends AsyncTask<String, Void, String>
	{
		GeocodingResponse response;

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) 
		{

			Utility.printLog("bbb BackgroundGeocodingTask");
			//String url="https://maps.googleapis.com/maps/api/geocode/json?latlng="+currentLatitude+","+currentLongitude+"&sensor=false&key="+VariableConstants.GOOGLE_API_KEY;

			String url="https://maps.google.com/maps/api/geocode/json?latlng="+currentLatitude+","+currentLongitude+"&sensor=false";

			Utility.printLog("Geocoding url: "+url);

			String stringResponse=Utility.callhttpRequest(url);

			if(stringResponse!=null)
			{
				Gson gson=new Gson();
				response=gson.fromJson(stringResponse, GeocodingResponse.class);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);

			if(response!=null)
			{
				if(response.getStatus().equals("OK") && response.getResults()!=null && response.getResults().size()>0)
				{
					if(response.getResults().size()>0 && !response.getResults().get(0).getFormatted_address().isEmpty())
					{
						current_address.setText(response.getResults().get(0).getFormatted_address());
						pickupLocationAddress.setText(response.getResults().get(0).getFormatted_address());
						mPICKUP_ADDRESS=response.getResults().get(0).getFormatted_address();
					}

					/*if(response.getResults().size()>1 && !response.getResults().get(1).getFormatted_address().isEmpty())
					{

						current_address.setText(response.getResults().get(1).getFormatted_address());
						pickupLocationAddress.setText(response.getResults().get(1).getFormatted_address());
						mPICKUP_ADDRESS=response.getResults().get(1).getFormatted_address();

					}
					else if(response.getResults().size()==1)
					{

						current_address.setText(response.getResults().get(0).getFormatted_address());
						pickupLocationAddress.setText(response.getResults().get(0).getFormatted_address());
						mPICKUP_ADDRESS=response.getResults().get(0).getFormatted_address();

					}*/
				}

				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						if (formattedAddress != null) {
							if (locationName != null) {

								current_address.setText(locationName + " " + formattedAddress);
								pickupLocationAddress.setText(locationName + " " + formattedAddress);
								mPICKUP_ADDRESS = locationName + " " + formattedAddress;

								formattedAddress = null;
								locationName = null;
							} else {
								current_address.setText(formattedAddress);
								pickupLocationAddress.setText(formattedAddress);
								mPICKUP_ADDRESS = formattedAddress;

								formattedAddress=null;
							}
						}
					}
				}, 3000);
			}
		}
	}






	/**
	 * To get the directions url between pickup location and driver current location
	 * @param driver_lat
	 * @param driver_long
	 * @return DirectionsUrl
	 * 
	 */
	private String getMapsApiDirectionsUrl(String driver_lat,String driver_long)
	{
		Utility.printLog("HomePageFragment Driver on the way getMapsApiDirectionsUrl");
		//SessionManager session = new SessionManager(getActivity());

		Utility.printLog("latitude="+session.getPickuplat()+" "+session.getPickuplng());
		LatLng latLng=new LatLng(Double.parseDouble(session.getPickuplat()),Double.parseDouble(session.getPickuplng()));
		googleMap.addMarker(new MarkerOptions().position(latLng)
				//.title("Booking Location")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_markers_pickup)));

		LatLng latLng1=new LatLng(Double.parseDouble(session.getPickuplat()),Double.parseDouble(session.getPickuplng()));
		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 17.0f));

		/*String waypoints = "waypoints=optimize:true|"
				+ session.getPickuplat() + "," + session.getPickuplng()
				+ "|" + "|" + driver_lat + ","
				+ driver_long;*/

		String waypoints = "origin="+session.getPickuplat()+","+session.getPickuplng()+"&"+"destination="+driver_lat+","+driver_long;

		String sensor = "sensor=false";
		String params = waypoints + "&" + sensor;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + params;
		return url;
	}

	/**
	 * To get the directions url between pickup location and drop location
	 */

	private String getMapsApiDirectionsFromTourl()
	{
		SessionManager session = new SessionManager(mActivity);
		Utility.printLog("getMapsApiDirectionsFromTourl HomePageFragment Driver reached  ");
		Utility.printLog("getDropofflat=" + session.getDropofflat() + " getDropofflng=" + session.getDropofflng());

		LatLng latLngDrop=new LatLng(Double.parseDouble(session.getDropofflat()),Double.parseDouble(session.getDropofflng()));

		googleMap.addMarker(new MarkerOptions().position(latLngDrop)
				//.title("Drop Location")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_markers_dropoff)));

		/*String waypoints = "waypoints=optimize:true|"
				+ session.getPickuplat() + "," +session.getPickuplng()
				+ "|" + "|" + session.getDropofflat() + ","
				+ session.getDropofflng();*/

		String waypoints = "origin="+session.getPickuplat()+","+session.getPickuplng()+"&"+"destination="+session.getDropofflat()+","+session.getDropofflng();

		String sensor = "sensor=false";
		String params = waypoints + "&" + sensor;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + params;
		return url;
	}


	/**
	 *plotting the directions in google map
	 */


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Utility.printLog("check inside onActivityResult requestCode=" + requestCode);

		if(requestCode==1)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				Utility.printLog("on sucess inside if");
				//called if coming from FareQuote activity
				from_latitude=data.getStringExtra("FROM_LATITUDE");
				from_longitude=data.getStringExtra("FROM_LONGITUDE");
				to_latitude=data.getStringExtra("TO_LATITUDE");
				to_longitude=data.getStringExtra("TO_LONGITUDE");
				String from_searchAddress=data.getStringExtra("from_SearchAddress");
				String to_searchAddress=data.getStringExtra("to_SearchAddress");
				Utility.printLog("onActivityResult from"+from_latitude+" "+from_longitude);
				Utility.printLog("onActivityResult to"+to_latitude+" "+to_longitude);
				pickupLocationAddress.setText(from_searchAddress);
				Dropoff_Location_Address.setText(to_searchAddress);
				new_dropoff_location_address.setText(to_searchAddress);
				//LatLng latLng = new LatLng(Double.parseDouble(to_latitude), Double.parseDouble(to_longitude));
				// Showing the current location in Google Map & Zoom in the Google Map
				//googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.5f));
			} else {
				Utility.printLog("on sucess inside else");
			}
		}

		if(requestCode==2)
		{
			if(resultCode==Activity.RESULT_OK)
			{
				Utility.printLog("inside on activty result 2");
				googleMap.clear();
				Log.i("requestcode publish"+session.getServerChannel(),"{a:1,pid:" + session.getLoginId() + ",lt:" + currentLatitude + ",lg:" + currentLongitude + ",chn:" + session.getChannelName() + ",st:3,tp:" + Car_Type_Id + "}");
				_publish(session.getServerChannel(), "{a:1,pid:" + session.getLoginId() + ",lt:" + currentLatitude + ",lg:" + currentLongitude + ",chn:" + session.getChannelName() + ",st:3,tp:" + Car_Type_Id + "}");
			}
		}

		if(requestCode==16)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				if(isFareQuotePressed)
				{
					//isSetDropoffLocation=true;
					String latitudeString=data.getStringExtra("LATITUDE_SEARCH");
					String logitudeString=data.getStringExtra("LONGITUDE_SEARCH");
					String searchAddress=data.getStringExtra("SearchAddress");
					formattedAddress = data.getStringExtra("SearchAddress");
					locationName = 	data.getStringExtra("ADDRESS_NAME");

					to_latitude=latitudeString;
					to_longitude=logitudeString;
					Utility.printLog(TAG, "onActivityResult latitudeString...."+latitudeString+"...logitudeString..."+logitudeString);
					if(searchAddress!=null)
					{
						isSetDropoffLocation=true;
						if(locationName!=null && !locationName.isEmpty())
						{
							mDROPOFF_ADDRESS=locationName+" "+searchAddress;
							Dropoff_Location_Address.setText(locationName+" "+searchAddress);
							new_dropoff_location_address.setText(locationName+" "+searchAddress);
						}
						else
						{
							mDROPOFF_ADDRESS=searchAddress;
							Dropoff_Location_Address.setText(searchAddress);
							new_dropoff_location_address.setText(searchAddress);

						}
					}
					//Placing marker for current location
					SessionManager session = new SessionManager(getActivity());
					Intent intent = new Intent(getActivity(),FareQuoteActivity.class);
					Utility.printLog("fare_quote from_latitude="+from_latitude+" from_longitude="+from_longitude+" to_latitude="+to_latitude+" to_longitude="+to_longitude);
					Utility.printLog("fare_quote pick_up add="+pick_up+" drop_off="+mDROPOFF_ADDRESS); 
					Utility.printLog("fare_quote id="+Car_Type_Id+" "+Car_Type_Id); 
					session.storeDropofflat(to_latitude);
					session.storeDropofflng(to_longitude);
					Utility.printLog("fare_quote from session = "+session.getPickuplat()+" "+session.getPickuplng()+" "+session.getDropofflat()
							+" "+session.getDropofflng());
					intent.putExtra("PICKUP_ADDRESS", mPICKUP_ADDRESS);
					intent.putExtra("DROPOFF_ADDRESS", mDROPOFF_ADDRESS);
					intent.putExtra("FromLatitude", from_latitude);
					intent.putExtra("FromLongitude", from_longitude);
					intent.putExtra("ToLatitude", to_latitude);
					intent.putExtra("ToLongitude", to_longitude);
					intent.putExtra("TypeId", Car_Type_Id);
					//startActivity(intent);
					startActivityForResult(intent, 1);
					getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
				}
				//isSetDropoffLocation=true;
				String latitudeString=data.getStringExtra("LATITUDE_SEARCH");
				String logitudeString=data.getStringExtra("LONGITUDE_SEARCH");
				String searchAddress=data.getStringExtra("SearchAddress");
				to_latitude=latitudeString;
				to_longitude = logitudeString;
				Utility.printLog(TAG, "onActivityResult latitudeString...." + latitudeString + "...logitudeString..." + logitudeString);
				if(searchAddress!=null)
				{
					AddLocation.setText("-");
					//Relative_Dropoff_Location.setVisibility(View.VISIBLE);
					isSetDropoffLocation=true;
					mDROPOFF_ADDRESS=searchAddress;
					Dropoff_Location_Address.setText(searchAddress);
					new_dropoff_location_address.setText(searchAddress);

					//Akbar for updating the new address after the booking accepted by driver
					
					if(Utility.isNetworkAvailable(getActivity()))
					{
						  new BackgroundUpdateAddress().execute();

					}
					
				}
			}
		}

		if(requestCode==17)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				int select_card_position=data.getIntExtra("position", 0);
				String card_no=data.getStringExtra("NUMB");
				Bitmap bitmap = (Bitmap)data.getParcelableExtra("Image");
				Default_Card_Id = data.getStringExtra("ID");
				Card_Image.setImageBitmap(bitmap);
				cardChecked=select_card_position;
				payment_type="1";
				Utility.printLog("set checked="+cardChecked);
				Utility.printLog(TAG, "onActivityResult position="+select_card_position+" card_no="+card_no);
				Card_Info.setText("**** **** **** "+card_no);
				new BackGroundChangeDefault().execute();
			}
		}


		if(requestCode==18)
		{

			if(data!=null)
			{


				IsreturnFromSearch=true;
				String latitudeString=data.getStringExtra("LATITUDE_SEARCH");
				String logitudeString=data.getStringExtra("LONGITUDE_SEARCH");

				mPICKUP_ADDRESS=data.getStringExtra("SearchAddress");

				formattedAddress = data.getStringExtra("SearchAddress");
				locationName = 	data.getStringExtra("ADDRESS_NAME");

				if(mPICKUP_ADDRESS!=null)
				{
					/*if(locationName!=null && !locationName.isEmpty())
					{
						pickupLocationAddress.setText(locationName+" "+mPICKUP_ADDRESS);
					}
					else
					{*/
					current_address.setText(mPICKUP_ADDRESS);
					pickupLocationAddress.setText(mPICKUP_ADDRESS);
					/*}*/
				}

				from_latitude=latitudeString;
				from_longitude=logitudeString;
				Utility.printLog(TAG, "onActivityResult latitudeString...."+latitudeString+"...logitudeString..."+logitudeString);
				//Placing marker for current location
				searchlocatinlat= Double.parseDouble(latitudeString);
				searchlocatinlng = Double.parseDouble(logitudeString);

				currentLatitude =Double.parseDouble(latitudeString);
				currentLongitude =Double.parseDouble(logitudeString);
				LatLng latLng = new LatLng(searchlocatinlat, searchlocatinlng);
				// Showing the current location in Google Map & Zoom in the Google Map
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));

			}
		}

		if(requestCode==19)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				Utility.printLog("inside RESULT_OK true");
				//getActivity().getActionBar().show();
				ResideMenuActivity.main_frame_layout.setVisibility(View.VISIBLE);
				isSetDropoffLocation=false;
				isBackPressed=false;
				isFareQuotePressed=false;
				isSetDropoffLocation=false;
				to_latitude=null;
				to_longitude=null;
				mDROPOFF_ADDRESS=null;
				new_dropoff_location_address.setText("");
				Dropoff_Location_Address.setText("");
				show_address_relative.setVisibility(View.VISIBLE);
				Txt_Pickup.setVisibility(View.VISIBLE);
				pickup.setVisibility(View.VISIBLE);
				//setLocationArrow.setVisibility(View.VISIBLE);
				relativePickupLocation.setVisibility(View.INVISIBLE);
				Request_Pick_up_here.setVisibility(View.INVISIBLE);
				Fare_Quote.setVisibility(View.INVISIBLE);
				farePromoLayouy.setVisibility(View.INVISIBLE);
				//=============================My Change=================================
				req_lay.setVisibility(View.INVISIBLE);
				//=============================My Change=================================
				Relative_Card_Info.setVisibility(View.GONE);
				Card_Info.setVisibility(View.INVISIBLE);
			    Card_Image.setVisibility(View.INVISIBLE);
				Relative_Dropoff_Location.setVisibility(View.INVISIBLE);
				Relative_Pickup_Navigation.setVisibility(View.INVISIBLE);
				all_types_layout.setVisibility(View.VISIBLE);
				now_later_layout.setVisibility(View.VISIBLE);
				nearestDrivers.clear();
			}
			else
			{
				Utility.printLog("inside RESULT_OK false");
				//getActivity().getActionBar().show();
				ResideMenuActivity.main_frame_layout.setVisibility(View.VISIBLE);
				isSetDropoffLocation=false;
				isBackPressed=false;
				isFareQuotePressed=false;
				to_latitude=null;
				to_longitude=null;
				mDROPOFF_ADDRESS=null;
				Dropoff_Location_Address.setText("");
				new_dropoff_location_address.setText("");
				show_address_relative.setVisibility(View.VISIBLE);
				Txt_Pickup.setVisibility(View.VISIBLE);
				pickup.setVisibility(View.VISIBLE);
				//setLocationArrow.setVisibility(View.VISIBLE);
				relativePickupLocation.setVisibility(View.INVISIBLE);
				Request_Pick_up_here.setVisibility(View.INVISIBLE);
				Fare_Quote.setVisibility(View.INVISIBLE);
				farePromoLayouy.setVisibility(View.INVISIBLE);
				//=============================My Change=================================
				req_lay.setVisibility(View.INVISIBLE);
				//=============================My Change=================================
				Relative_Card_Info.setVisibility(View.GONE);
				Card_Info.setVisibility(View.INVISIBLE);
				Card_Image.setVisibility(View.INVISIBLE);
				Relative_Dropoff_Location.setVisibility(View.INVISIBLE);
				Relative_Pickup_Navigation.setVisibility(View.INVISIBLE);
				all_types_layout.setVisibility(View.VISIBLE);
				now_later_layout.setVisibility(View.VISIBLE);
				nearestDrivers.clear();
			}
		}


		if(requestCode==20)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				Utility.printLog("inside RESULT_OK true");
				//getActivity().getActionBar().show();
				ResideMenuActivity.main_frame_layout.setVisibility(View.VISIBLE);
				isSetDropoffLocation=false;
				isBackPressed=false;
				isFareQuotePressed=false;
				isSetDropoffLocation=false;
				to_latitude=null;
				to_longitude=null;
				mDROPOFF_ADDRESS=null;
				Dropoff_Location_Address.setText("");
				new_dropoff_location_address.setText("");
				show_address_relative.setVisibility(View.VISIBLE);
				Txt_Pickup.setVisibility(View.VISIBLE);
				pickup.setVisibility(View.VISIBLE);
				//setLocationArrow.setVisibility(View.VISIBLE);
				relativePickupLocation.setVisibility(View.INVISIBLE);
				Request_Pick_up_here.setVisibility(View.INVISIBLE);
				Fare_Quote.setVisibility(View.INVISIBLE);
				farePromoLayouy.setVisibility(View.INVISIBLE);
				//=============================My Change=================================
				req_lay.setVisibility(View.INVISIBLE);
				//=============================My Change=================================
				Relative_Card_Info.setVisibility(View.GONE);
				Card_Info.setVisibility(View.INVISIBLE);
				Card_Image.setVisibility(View.INVISIBLE);
				Relative_Dropoff_Location.setVisibility(View.INVISIBLE);
				Relative_Pickup_Navigation.setVisibility(View.INVISIBLE);
				all_types_layout.setVisibility(View.VISIBLE);
				now_later_layout.setVisibility(View.VISIBLE);

				laterBookingDate = null;//after completing the booking resetting the booking date
				Utility.ShowAlert(data.getStringExtra("LaterMsg"), getActivity());
			}
			else
			{
				Utility.printLog("inside RESULT_OK true");
				//getActivity().getActionBar().show();
				ResideMenuActivity.main_frame_layout.setVisibility(View.VISIBLE);
				isSetDropoffLocation=false;
				isBackPressed=false;
				isFareQuotePressed=false;
				isSetDropoffLocation=false;
				to_latitude=null;
				to_longitude=null;
				mDROPOFF_ADDRESS=null;
				Dropoff_Location_Address.setText("");
				new_dropoff_location_address.setText("");
				show_address_relative.setVisibility(View.VISIBLE);
				Txt_Pickup.setVisibility(View.VISIBLE);
				pickup.setVisibility(View.VISIBLE);
				//setLocationArrow.setVisibility(View.VISIBLE);
				relativePickupLocation.setVisibility(View.INVISIBLE);
				Request_Pick_up_here.setVisibility(View.INVISIBLE);
				Fare_Quote.setVisibility(View.INVISIBLE);
				farePromoLayouy.setVisibility(View.INVISIBLE);
				Relative_Card_Info.setVisibility(View.GONE);
				//=============================My Change=================================
				req_lay.setVisibility(View.INVISIBLE);
				//=============================My Change=================================
				Card_Info.setVisibility(View.INVISIBLE);
				Card_Image.setVisibility(View.INVISIBLE);
				Relative_Dropoff_Location.setVisibility(View.INVISIBLE);
				Relative_Pickup_Navigation.setVisibility(View.INVISIBLE);
				all_types_layout.setVisibility(View.VISIBLE);
				now_later_layout.setVisibility(View.VISIBLE);

				laterBookingDate = null;//after completing the booking resetting the booking date

				//Utility.ShowAlert(data.getStringExtra("LaterMsg"), getActivity());

			}
		}



	}


	/**
	 * Making the selected card as default card while booking
	 * @author embed-pc
	 *
	 */
	class BackGroundChangeDefault extends AsyncTask<String,Void,String>
	{
		private  String TAG = null;
		BookAppointmentResponse response;
		ProgressDialog dialogL;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialogL= Utility.GetProcessDialog(getActivity());
			dialogL.setCancelable(true);
			if (dialogL!=null) {
				dialogL.show();
			}
		}

		@Override
		protected String doInBackground(String... params) {

			String url=VariableConstants.BASE_URL+"makeCardDefault";

			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			Utility.printLog("dataandTime="+curenttime);

			SessionManager session=new SessionManager(getActivity());
			Map<String, String> kvPairs = new HashMap<String, String>();
			kvPairs.put("ent_sess_token",session.getSessionToken());
			kvPairs.put("ent_dev_id",session.getDeviceId());
			kvPairs.put("ent_cc_id",Default_Card_Id);
			kvPairs.put("ent_date_time",curenttime);

			HttpResponse httpResponse = null;
			try {
				httpResponse = Utility.doPost(url,kvPairs);
			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
				Utility.printLog(TAG, "doPost Exception......."+e1);
			} catch (IOException e1) {
				e1.printStackTrace();
				Utility.printLog(TAG, "doPost Exception......."+e1);
			}


			String jsonResponse = null;
			if (httpResponse!=null) {

				try {
					jsonResponse = EntityUtils.toString(httpResponse.getEntity());
				} catch (ParseException e) {
					e.printStackTrace();
					Utility.printLog(TAG, "doPost Exception......."+e);
				} catch (IOException e) {
					e.printStackTrace();
					Utility.printLog(TAG, "doPost Exception......."+e);
				}
			}
			Utility.printLog("Getdriveraround Response: ","Default card: "+jsonResponse);
			Utility.printLog("Getdriveraround Response: ",jsonResponse);

			if (jsonResponse!=null)
			{
				Gson gson = new Gson();
				response=gson.fromJson(jsonResponse,BookAppointmentResponse.class);
				Utility.printLog("","DONE WITH GSON");
			}
			else
			{

				getActivity().runOnUiThread(new Runnable()
				{
					public void run()
					{
						Toast.makeText(getActivity(),getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
					}
				});
			}
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
		}
	}

	private Bitmap setCreditCardLogo(String type)
	{
		// TODO Auto-generated method stub
		CardType cardType;
		if(type.equals("Visa"))
		{
			cardType=CardType.VISA;
			Bitmap bitmap=cardType.imageBitmap(getActivity());
			//cardLogo.setImageBitmap(bitmap);
			return bitmap;
		}
		if(type.equals("MasterCard"))
		{
			cardType=CardType.MASTERCARD;
			Bitmap bitmap=cardType.imageBitmap(getActivity());
			//cardLogo.setImageBitmap(bitmap);
			return bitmap;
		}
		if(type.equals("American Express"))
		{
			cardType=CardType.AMEX;
			Bitmap bitmap=cardType.imageBitmap(getActivity());
			//cardLogo.setImageBitmap(bitmap);
			return bitmap;
		}
		if(type.equals("Discover"))
		{
			cardType=CardType.DISCOVER;
			Bitmap bitmap=cardType.imageBitmap(getActivity());
			//cardLogo.setImageBitmap(bitmap);
			return bitmap;
		}

		if(type.equals("JCB"))
		{
			cardType=CardType.JCB;
			Bitmap bitmap=cardType.imageBitmap(getActivity());
			//cardLogo.setImageBitmap(bitmap);
			return bitmap;
		}
		cardType=CardType.UNKNOWN;
		Bitmap bitmap=cardType.imageBitmap(getActivity());
		//cardLogo.setImageBitmap(bitmap);
		return bitmap;
	}



	/**
	 *To Get the current appointment details
	 * @param date date time
	 */
	private void getAppointmentDetails_Volley(final String date)
	{
		Utility.printLog("Appointments details inside getAppointmentDetails_Volley");
		final ProgressDialog dialogL;
		dialogL=Utility.GetProcessDialog(getActivity());
		dialogL.setCancelable(false);

			if (dialogL != null) {
				dialogL.show();
			}

		RequestQueue volleyRequest = Volley.newRequestQueue(getActivity());
		StringRequest myReq = new StringRequest(Request.Method.POST,
				VariableConstants.BASE_URL+"getAppointmentDetails",
				new Listener<String>()
				{
			@Override
			public void onResponse(String response) 
			{
				if(dialogL!=null)
				{
					dialogL.dismiss();
				}
				Utility.printLog("Appointments details success Response json: "+response);
				fetchAptData(response);
			}
				},
				new ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error) 
					{
						// TODO Auto-generated method stub
						if(dialogL!=null)
						{
							dialogL.dismiss();
						}
						Utility.printLog("Appointments details error Response:ERROR");
						Toast.makeText(getActivity(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
					}
				}){
			protected HashMap<String,String> getParams() throws com.android.volley.AuthFailureError 
			{
				SessionManager session=new SessionManager(getActivity());

				Utility utility=new Utility();
				String curenttime=utility.getCurrentGmtTime();
				Utility.printLog("Appointments "+session.getSessionToken());
				Utility.printLog("Appointments "+session.getDeviceId());
				Utility.printLog("Appointments LoginId "+session.getLoginId());
				Utility.printLog("Appointments DriverEmail "+session.getDriverEmail());
				Utility.printLog("Appointments currentdateandTime="+curenttime+" Apptdate="+date);

				HashMap<String, String> kvPairs = new HashMap<String, String>();

				kvPairs.put("ent_sess_token",session.getSessionToken());
				kvPairs.put("ent_dev_id",session.getDeviceId());
				kvPairs.put("ent_email",session.getDriverEmail());
				kvPairs.put("ent_appnt_dt",date);
				kvPairs.put("ent_user_type","2");
				kvPairs.put("ent_date_time",curenttime);
				return kvPairs;  
			}
		};
		volleyRequest.add(myReq);
	}

	private void fetchAptData(String jsonResponse)
	{
		Utility.printLog("Appointments details inside fetchAptData");
		try
		{
			Gson gson = new Gson();
			appointmentResponse=gson.fromJson(jsonResponse,GetAppointmentDetails.class);

			if(appointmentResponse!=null)
			{
				if(appointmentResponse.getErrFlag().equals("0"))
				{
					Utility.printLog("Appointments details success response 0");

					session.storeDocName(appointmentResponse.getfName()+" "+appointmentResponse.getlName());
					session.storeDocPic(VariableConstants.IMAGE_BASE_URL+appointmentResponse.getpPic());
					session.storeAptDate(appointmentResponse.getApptDt());
					session.storeDocPH(appointmentResponse.getMobile());
					session.storeDocDist(appointmentResponse.getDis());
					session.storeBookingId(appointmentResponse.getBid());
					session.storeCurrentAptChannel(appointmentResponse.getChn());
					session.storeDriverEmail(appointmentResponse.getEmail());
					session.storeDropofflat(appointmentResponse.getDropLat());
					session.storeDropofflng(appointmentResponse.getDropLong());
					session.storePickuplat(appointmentResponse.getPickLat());
					session.storePickuplng(appointmentResponse.getPickLong());
					session.storePlateNO(appointmentResponse.getPlateNo());
					session.storeCarModel(appointmentResponse.getModel());
					session.storeShareLink(appointmentResponse.getShare());

					/*if(driver_parent.getVisibility() == View.VISIBLE)
					{
						driver_parent.setVisibility(View.GONE);
					}*/

					Utility.printLog("Appointments details Response: getCurrentAptChannel="+session.getCurrentAptChannel()+"  getBookingId="+session.getBookingId());

					String[] subscribed_channels = pubnub.getSubscribedChannelsArray();
					Utility.printLog("Appointments details subscribed_channels my channel="+session.getChannelName());
					ArrayList<String> unsubscribeChannels= new ArrayList<String>();

					boolean isCurrentDriverChannelSubscribed=false;
					boolean isMyChannelSubscribed=false;
					for(int i=0;i<subscribed_channels.length;i++)
					{
						Utility.printLog("Appointments details subscribed_channels at"+i+" "+subscribed_channels[i]);

						if(subscribed_channels[i].equals(session.getCurrentAptChannel()))
						{
							isCurrentDriverChannelSubscribed=true;
						}
						else if(subscribed_channels[i].equals(session.getChannelName()))
						{
							isMyChannelSubscribed=true;
						}
						else
						{
							unsubscribeChannels.add(subscribed_channels[i]);
						}
					}

					//unsubscribing all previously subscribed channels except current apt channel
					Utility.printLog("Appointments details BackgroundSubscribeMyChannel unsubscribeChannels size="+unsubscribeChannels.size());
					if(unsubscribeChannels.size()>0)
					{
						Utility.printLog("Appointments details channels unsubscribeChannels channel list size="+unsubscribeChannels.size());
						String[] new_un_sub_channels=new String[unsubscribeChannels.size()];
						new_un_sub_channels=unsubscribeChannels.toArray(new_un_sub_channels);

						new BackgroundUnSubscribeChannels().execute(new_un_sub_channels);
					}

					Utility.printLog("Appointments details BackgroundSubscribeMyChannel status ="+isMyChannelSubscribed);
					if(!isMyChannelSubscribed)
					{
						Utility.printLog("Appointments details BackgroundSubscribeMyChannel");
						new BackgroundSubscribeMyChannel().execute();
					}
					Utility.printLog("Appointments details isCurrentDriverChannelSubscribed status ="+isCurrentDriverChannelSubscribed);

					if(!isCurrentDriverChannelSubscribed)
					{
						Utility.printLog("Appointments details isCurrentDriverChannelSubscribed");
						String[] my_driver_channel=new String[1];
						my_driver_channel[0]=session.getCurrentAptChannel();
						Utility.printLog("Appointments details channels null current apt channel not present in old channels"+my_driver_channel[0]);
						new BackgroundSubscribeChannels().execute(my_driver_channel);
					}

					Utility.printLog("Appointments details session.isdriverOnWay() "+session.isDriverOnWay());
					Utility.printLog("Appointments details session.isdriverOnArrived() "+session.isDriverOnArrived());
					Utility.printLog("Appointments details session.isInvoiceRaised() "+session.isInvoiceRaised());
					Utility.printLog("Appointments details session.isTripBegin() "+session.isTripBegin());

					//if channel is not null then subscribing only for that channel to get driver on way the response
					if(session.isDriverOnWay())
					{
						Utility.printLog("Appointments details inside isDriverOnWay");

						//marker_map.clear();
						marker_map_on_the_way.clear();
						Utility.printLog("marker_map_on_the_way 1");
						marker_map_arrived.clear();
						googleMap.clear();

						if(appointmentResponse.getDropAddr1().equals(""))
						{
							new_relative_dropoff_location.setVisibility(View.VISIBLE);
							new_dropoff_location_address.setText("");

						}
						else
						{
							new_relative_dropoff_location.setVisibility(View.VISIBLE);
							new_dropoff_location_address.setText(appointmentResponse.getDropAddr1());
						}


						//Plot the path of the driver
						if(session.getLat_DOW()!=null && session.getLon_DOW()!=null)
						{
							LatLng latLng=new LatLng(Double.parseDouble(session.getLat_DOW()),Double.parseDouble(session.getLon_DOW()));

							//Showing the driver location in Google Map & Zoom in the Google Map
							//googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17.0f));
                         //=================================My change=====================================
							driver_on_the_way_marker = googleMap.addMarker(new MarkerOptions().position(latLng)
									.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)).rotation(0)
									.flat(true));


							//========================My change==========================
						/*	driver_on_the_way_marker = googleMap.addMarker(new MarkerOptions().position(latLng)
									.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon))
									.rotation(0)
									.flat(true));*/



							// driver_on_the_way_marker.setTitle("DOW");

							marker_map_on_the_way.put(session.getCurrentAptChannel(), driver_on_the_way_marker);

							//Akbar commented to remove the path plotting
							
							/*String url = getMapsApiDirectionsUrl(session.getLat_DOW(),session.getLon_DOW());
							ReadTask downloadTask = new ReadTask();
							downloadTask.execute(url);*/
						}

						if(session.getPickuplat()!=null && session.getPickuplng()!=null)
						{

							LatLng latLng=new LatLng(Double.parseDouble(session.getPickuplat()),Double.parseDouble(session.getPickuplng()));
							googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17.0f));
							picupmarker = googleMap.addMarker(new MarkerOptions().position(latLng)
									.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_markers_pickup))
									.rotation(0)
									.flat(true));

						}

						Utility.printLog("marker_map_on_the_way get app details="+session.getCurrentAptChannel());										
						Utility.printLog("marker_map_on_the_way get app details status="+marker_map_on_the_way.containsKey(session.getCurrentAptChannel()));

						if(session.getDocPic() != null && !session.getDocPic().equalsIgnoreCase("null") && !session.getDocPic().equalsIgnoreCase(""))
						{


							double width1 = dblArray[0] * 40;
							double height1 = dblArray[1] * 0;

							Picasso.with(getActivity()).load(session.getDocPic())
									//.centerCrop()
									.transform(new CircleTransform())
									.placeholder(R.drawable.profile_image_frame)
									.resize((int)width1,(int)height1).into(Driver_Profile_Pic);

							/*imageLoader.displayImage(session.getDocPic(), Driver_Profile_Pic, options,new SimpleImageLoadingListener()
							{
								@Override
								public void onLoadingComplete(String imageUri,View view,Bitmap loadedImage)
								{
									super.onLoadingComplete(imageUri, view,loadedImage);
									Driver_Profile_Pic.setImageBitmap(loadedImage);
								}
							});*/
						}
						else 
						{
							Driver_Profile_Pic.setImageResource(R.drawable.ic_launcher);
						}

						Driver_Name.setText(session.getDocName());
						//Removing buttons on screen
						Driver_Confirmation.setVisibility(View.VISIBLE);
						//Rl_distance_time.setVisibility(View.VISIBLE);
						show_address_relative.setVisibility(View.GONE);
						pickup.setVisibility(View.INVISIBLE);
						//setLocationArrow.setVisibility(View.INVISIBLE);
						Txt_Pickup.setVisibility(View.INVISIBLE);
						Mid_Pointer.setVisibility(View.INVISIBLE);
						all_types_layout.setVisibility(View.INVISIBLE);
						now_later_layout.setVisibility(View.INVISIBLE);
						//Rl_distance_time.setVisibility(View.VISIBLE);
						//Driver_on_the_way_txt.setVisibility(View.VISIBLE);
						//relative_now_later_status.setVisibility(View.GONE);
						//=================My Change======================
						relative_booking.setVisibility(View.VISIBLE);

                      //  booking_id.setText("BOOKING ID :"+session.getBookingId());
						booking_id.setText(appointmentResponse.getAddr1());
						pageno.setVisibility(View.VISIBLE);
						logo.setVisibility(View.INVISIBLE);
						pageno.setText("DRIVER ON THE WAY");

						//===============My Change=======================
					    Driver_on_the_way_txt.setText("DRIVER ON THE WAY : "+session.getBookingId());
						if(session.getDriverRating()!=null)
							Driver_Rating.setRating(Float.parseFloat(session.getDriverRating()));

					}
					else if(session.isDriverOnArrived())
					{
						Utility.printLog("Appointments details inside isDriverOnArrived");

						//marker_map.clear();
						marker_map_on_the_way.clear();
						Utility.printLog("marker_map_on_the_way 15");
						marker_map_arrived.clear();
						googleMap.clear();

						/*new BackgroundUnSubscribeAll().execute();

							new BackgroundSubscribeMyChannel().execute();
							String[] driver_channel = new String[1];
							driver_channel[0] = session.getCurrentAptChannel();
							Utility.printLog("CONTROL INSIDE getCurrentApt driver_channel DA:"+driver_channel[0]);

							new BackgroundSubscribeChannels().execute(driver_channel);*/

						if(appointmentResponse.getDropAddr1().equals(""))
						{
							new_relative_dropoff_location.setVisibility(View.VISIBLE);
							new_dropoff_location_address.setText("");

						}
						else
						{
							new_relative_dropoff_location.setVisibility(View.VISIBLE);
							new_dropoff_location_address.setText(appointmentResponse.getDropAddr1());
						}


						if(session.getLat_DOW()!=null && session.getLon_DOW()!=null)
						{
							LatLng latLng=new LatLng(Double.parseDouble(session.getLat_DOW()),Double.parseDouble(session.getLon_DOW()));
						/*	driver_arrived = googleMap.addMarker(new MarkerOptions().position(latLng)
									.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon)));*/
							//=================================My change=====================================
							driver_arrived = googleMap.addMarker(new MarkerOptions().position(latLng)
									.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
							//========================My change==========================


							//driver_arrived.setTitle("Driver reached");
							googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));

							marker_map_arrived.put(session.getCurrentAptChannel(), driver_arrived);
						}
						if(session.getPickuplat()!=null && session.getPickuplng()!=null)
						{

							LatLng latLng=new LatLng(Double.parseDouble(session.getPickuplat()),Double.parseDouble(session.getPickuplng()));
							googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17.0f));
							picupmarker = googleMap.addMarker(new MarkerOptions().position(latLng)
									.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_markers_pickup))
									.rotation(0)
									.flat(true));

						}

						//Plot the polyline from pickup location to destination location
						//===========My Change Journey Pin Problem 23/May/2016 ====================
						if(session.getDropofflat()!=null &&Double.parseDouble(session.getDropofflat())!=0.0&& session.getDropofflng()!=null)
							//===========My Change Journey Pin Problem 23/May/2016 ====================
					//	if(session.getDropofflat()!=null && session.getDropofflng()!=null)
						{

							LatLng latLng=new LatLng(Double.parseDouble(session.getDropofflat()),Double.parseDouble(session.getDropofflng()));
							//googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17.0f));
							picupmarker = googleMap.addMarker(new MarkerOptions().position(latLng)
									.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_markers_dropoff))
									.rotation(0)
									.flat(true));

							//Akbar commented to remove the path plotting
							/*String url = getMapsApiDirectionsFromTourl();
							Utility.printLog("getMapsApiDirectionsFromTourl ="+url);

							ReadTask downloadTask = new ReadTask();
							downloadTask.execute(url);	*/
						}

						if(session.getDocPic() != null && !session.getDocPic().equalsIgnoreCase("null") && !session.getDocPic().equalsIgnoreCase(""))
						{
							double width1 = dblArray[0] * 40;
							double height1 = dblArray[1] * 0;

							Picasso.with(getActivity()).load(session.getDocPic())
									//.centerCrop()
									.transform(new CircleTransform())
									.placeholder(R.drawable.profile_image_frame)
									.resize((int)width1,(int)height1).into(Driver_Profile_Pic);
						} 
						else 
						{
							Driver_Profile_Pic.setImageResource(R.drawable.ic_launcher);
						}

						Driver_Name.setText(session.getDocName());
						Driver_Confirmation.setVisibility(View.VISIBLE);
						show_address_relative.setVisibility(View.GONE);
						pickup.setVisibility(View.INVISIBLE);
						//setLocationArrow.setVisibility(View.INVISIBLE);
						Txt_Pickup.setVisibility(View.INVISIBLE);
						Mid_Pointer.setVisibility(View.INVISIBLE);
						all_types_layout.setVisibility(View.INVISIBLE);
						now_later_layout.setVisibility(View.INVISIBLE);
						//Driver_on_the_way_txt.setVisibility(View.VISIBLE);

						//=================My Change======================
						relative_booking.setVisibility(View.VISIBLE);
					//	booking_id.setText("BOOKING ID :"+session.getBookingId());

						booking_id.setText(appointmentResponse.getAddr1());
						pageno.setVisibility(View.VISIBLE);
						logo.setVisibility(View.INVISIBLE);
						pageno.setText("DRIVER REACHED");
						//===============My Change=======================

						//Driver_on_the_way_txt.setText("DRIVER REACHED: "+session.getBookingId());


						//Rl_distance_time.setVisibility(View.VISIBLE);
						//relative_now_later_status.setVisibility(View.GONE);

						if(session.getDriverRating()!=null)
							Driver_Rating.setRating(Float.parseFloat(session.getDriverRating()));


					}
					else if(session.isTripBegin())
					{
						Utility.printLog("Appointments details inside isTripBegin");
						//marker_map.clear();
						marker_map_on_the_way.clear();
						Utility.printLog("marker_map_on_the_way 7");
						marker_map_arrived.clear();
						googleMap.clear();

						if(appointmentResponse.getDropAddr1().equals(""))
						{
							new_relative_dropoff_location.setVisibility(View.VISIBLE);
							new_dropoff_location_address.setText("");

						}
						else
						{
							new_relative_dropoff_location.setVisibility(View.VISIBLE);
							new_dropoff_location_address.setText(appointmentResponse.getDropAddr1());
						}

						if(session.getLat_DOW()!=null && session.getLon_DOW()!=null)//else adding driver current location
						{
							LatLng latLng=new LatLng(Double.parseDouble(session.getLat_DOW()),Double.parseDouble(session.getLon_DOW()));
	/*						driver_arrived = googleMap.addMarker(new MarkerOptions().position(latLng)
									.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon)));*/
							//=================================My change=====================================
							driver_arrived = googleMap.addMarker(new MarkerOptions().position(latLng)
									.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
							//========================My change==========================

							marker_map_arrived.put(session.getCurrentAptChannel(), driver_arrived);

							//driver_arrived.setTitle("journey started");

							googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17.0f));
						}

						//Plot the polyline from pickup to destination
						//===========My Change Journey Pin Problem 23/May/2016 ====================
						if(session.getDropofflat()!=null &&Double.parseDouble(session.getDropofflat())!=0.0&& session.getDropofflng()!=null)
                      //===========My Change Journey Pin Problem 23/May/2016 ====================
						//	if(session.getDropofflat()!=null && session.getDropofflng()!=null)
						{
							LatLng latLng=new LatLng(Double.parseDouble(session.getDropofflat()),Double.parseDouble(session.getDropofflng()));
							googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17.0f));
							picupmarker = googleMap.addMarker(new MarkerOptions().position(latLng)
									.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_markers_dropoff))
									.rotation(0)
									.flat(true));

							//Akbar commented to remove the path plotting
							/*String url = getMapsApiDirectionsFromTourl();
							Utility.printLog("getMapsApiDirectionsFromTourl ="+url);

							ReadTask downloadTask = new ReadTask();
							downloadTask.execute(url);*/

						}
						if(session.getPickuplat()!=null && session.getPickuplng()!=null)
						{

							LatLng latLng=new LatLng(Double.parseDouble(session.getPickuplat()),Double.parseDouble(session.getPickuplng()));
							//googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17.0f));
							picupmarker = googleMap.addMarker(new MarkerOptions().position(latLng)
									.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_markers_pickup))
									.rotation(0)
									.flat(true));


						}

						if(session.getDocPic() != null && !session.getDocPic().equalsIgnoreCase("null") && !session.getDocPic().equalsIgnoreCase(""))
						{

							double width1 = dblArray[0] * 40;
							double height1 = dblArray[1] * 0;

							Picasso.with(getActivity()).load(session.getDocPic())
									//.centerCrop()
									.transform(new CircleTransform())
									.placeholder(R.drawable.profile_image_frame)
									.resize((int)width1,(int)height1).into(Driver_Profile_Pic);

							/*imageLoader.displayImage(session.getDocPic(), Driver_Profile_Pic, options,new SimpleImageLoadingListener()
							{
								@Override
								public void onLoadingComplete(String imageUri,View view,Bitmap loadedImage)
								{
									super.onLoadingComplete(imageUri, view,loadedImage);
									Driver_Profile_Pic.setImageBitmap(loadedImage);
								}
							});*/
						} 
						else 
						{
							Driver_Profile_Pic.setImageResource(R.drawable.ic_launcher);
						}

						Driver_Name.setText(session.getDocName());
						Driver_Confirmation.setVisibility(View.VISIBLE);
						//Driver_Arrow.setVisibility(View.INVISIBLE);
						cancel_trip.setVisibility(View.INVISIBLE);
						show_address_relative.setVisibility(View.GONE);
						pickup.setVisibility(View.INVISIBLE);
						//setLocationArrow.setVisibility(View.INVISIBLE);
						Txt_Pickup.setVisibility(View.INVISIBLE);
						Mid_Pointer.setVisibility(View.INVISIBLE);
						all_types_layout.setVisibility(View.INVISIBLE);
						now_later_layout.setVisibility(View.INVISIBLE);
						//Driver_on_the_way_txt.setVisibility(View.VISIBLE);
						Driver_on_the_way_txt.setText("JOURNEY STARTED: "+session.getBookingId());

						//=================My Change======================
						relative_booking.setVisibility(View.VISIBLE);
						//booking_id.setText("BOOKING ID :"+session.getBookingId());
						booking_id.setText(appointmentResponse.getAddr1());
						pageno.setVisibility(View.VISIBLE);
						logo.setVisibility(View.INVISIBLE);
						pageno.setText("JOURNEY STARTED");
						//===============My Change=======================
						//Rl_distance_time.setVisibility(View.VISIBLE);
						//relative_now_later_status.setVisibility(View.GONE);

						if(session.getDriverRating()!=null)
							Driver_Rating.setRating(Float.parseFloat(session.getDriverRating()));
					}
					else
					{
						//Toast.makeText(getActivity(), "Driver not foune", Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					Toast.makeText(getActivity(), appointmentResponse.getErrMsg(),Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
				// set title
				alertDialogBuilder.setTitle(getResources().getString(R.string.error));
				// set dialog message
				alertDialogBuilder
				.setMessage(getResources().getString(R.string.error_message))
				.setCancelable(false)
				.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						AppStatus();
						dialog.dismiss();
					}
				});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
				alertDialog.show();
			}
		}
		catch(Exception e)
		{
			//Utility.ShowAlert("Error. Please Retry!!", getActivity());
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
	}
	/**
	 * Initialization of variables for Image loader
	 */
	private void initImageLoader() {
		int memoryCacheSize;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			int memClass = ((ActivityManager) 
					getActivity().getSystemService(Context.ACTIVITY_SERVICE))
					.getMemoryClass();
			memoryCacheSize = (memClass / 8) * 1024 * 1024;
		} else {
			memoryCacheSize = 2 * 1024 * 1024;
		}

		final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity()).threadPoolSize(5)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(memoryCacheSize)
				.memoryCache(new FIFOLimitedMemoryCache(memoryCacheSize-1000000))
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)//.enableLogging() 
				.build();
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if(locationManager != null) 
		{
			// remove the delegates to stop the GPS
			locationManager.removeGpsStatusListener(gpsListener);
			locationManager.removeUpdates(gpsListener);
			locationManager = null;
		}
		EventBus.getDefault().unregister(this);
		Utility.printLog("CONTROL INSIDE onPause");

		getActivity().unregisterReceiver(receiver);

		visibility=false;

		if(myTimer_publish!=null)
		{
			myTimer_publish.cancel();
			myTimer_publish=null;
		}

		if(myTimer!=null)
		{
			myTimer.cancel();
			myTimer=null;
		}
	}

	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		Utility.printLog("CONTROL INSIDE onDestroy");
		visibility=false;
		if(locationManager != null) 
		{
			// remove the delegates to stop the GPS
			locationManager.removeGpsStatusListener(gpsListener);
			locationManager.removeUpdates(gpsListener);
			locationManager = null;
		}
		new BackgroundShutdownPubnub().execute();

		try {
			/*if(googleMap!=null)
			{
				Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));  
				android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				ft.remove(fragment);
				ft.commit();
			}*/
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void onResume()
	{
		super.onResume();

		visibility=true;
		EventBus.getDefault().register(this);
		initializeVariables(view);


		getActivity().registerReceiver(receiver, filter);
		//=====================My Change 20/5/2016======================
		if (VariableConstants.card_respons.equalsIgnoreCase("0"))
		{
			Relative_Payment_Info.setVisibility(View.VISIBLE);
			Relative_Card_Info.setVisibility(View.GONE);
			card_info="CASH";
		}
//=====================My Change 20/5/2016======================
		if(Utility.isNetworkAvailable(getActivity()))
		{
			AppStatus();
		}
		else
		{
			getResources().getString(R.string.network_connection_fail);
		}
		/*if(isGpsEnable)
		{
			if(latitude==0.0 || longitude==0.0)
			{
				Toast.makeText(getActivity(), "NO Location view",Toast.LENGTH_SHORT).show();
			}
			else
			{
				LatLng latLng = new LatLng(latitude,longitude);
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
			}
		}
		else
		{
			showGpsAlert();
		}*/

		Utility.printLog("CONTROL INSIDE onResume");
		Utility.printLog("onResume session.isdriverOnWay() "+session.isDriverOnWay());
		Utility.printLog("onResume session.isdriverOnArrived() "+session.isDriverOnArrived());
		Utility.printLog("onResume session.isInvoiceRaised() "+session.isInvoiceRaised());
		Utility.printLog("onResume session.isTripBegin() "+session.isTripBegin());

		if(myTimer != null)
		{
			Utility.printLog(TAG, "Timer already started");
			return;
		}
		else
		{
			myTimer = new Timer();
			myTimerTask = new TimerTask()
			{
				@Override
				public void run() 
				{
					if(getActivity()!=null)
					{
						getActivity().runOnUiThread(new Runnable() 
						{
							@Override
							public void run() 
							{
								// TODO Auto-generated method stub
								System.out.println("Calling Location...");
								VisibleRegion visibleRegion = googleMap.getProjection()
										.getVisibleRegion();
								Point x1 = googleMap.getProjection().toScreenLocation(
										visibleRegion.farRight);
								Point y = googleMap.getProjection().toScreenLocation(
										visibleRegion.nearLeft);
								Point centerPoint = new Point(x1.x / 2, y.y / 2);
								LatLng centerFromPoint = googleMap.getProjection().fromScreenLocation(centerPoint);
								double lat = centerFromPoint.latitude;
								double lon = centerFromPoint.longitude;
								Utility.printLog(TAG	, "lat.........."+lat+".........long....."+lon);
								if((lat==currentLatitude&&lon==currentLongitude))
								{
									//Pointer is in same location, so dont call the service to get address 
									Utility.printLog("Update Address: FALSE");
								}
								else
								{
									SessionManager session = new SessionManager(mActivity);
									Utility.printLog("on location change car id="+Car_Type_Id);
									//To update current location
									Utility.printLog("sss PUBLISHING a:3...");
									if(lat!=0.0 && lon!=0.0)
									{
										Log.i("onresume publish"+session.getServerChannel(),"{a:3,chn:"+session.getChannelName()+",lt:"+lat+",lg:"+lon+",st:3,tp:"+Car_Type_Id+"}");

										_publish(session.getServerChannel(),"{a:3,chn:"+session.getChannelName()+",lt:"+lat+",lg:"+lon+",st:3,tp:"+Car_Type_Id+"}");		

										Utility.printLog("Update Address: TRUE");

										currentLatitude=lat;
										currentLongitude=lon;
										from_latitude=String.valueOf(lat);
										from_longitude=String.valueOf(lon);

										String[] params=new String[]{""+lat,""+lon};
										if(isAdded())
										{
											if(!IsreturnFromSearch)
											{
												Utility.printLog("here addres search start");
												new BackgroundGetAddress().execute(params);
											}
										}
									}
									//new BackgroundGetAddress().execute(params);
								}
							}
						});
					}
				}
			};
			myTimer.schedule(myTimerTask, 000, 2000);
		}

		//driver on the way
		if(session.isDriverOnWay())
		{
			Utility.printLog("onResume INSIDE ON_THE_WAY");

			//stop the timer to get the current address if any booking is going on
			if(myTimer!=null)
			{
				myTimer.cancel();
				myTimer=null;
			}
			//new CallGooglePlayServices().execute();
			setHasOptionsMenu(false);
			getCarDetails();
			getAppointmentDetails_Volley(session.getAptDate());

			return;
		}

		else if(session.isDriverOnArrived())
		{
			Utility.printLog("onResume INSIDE Driver Arrived");

			//stop the timer to get the current address if any booking is going on
			if(myTimer!=null)
			{
				myTimer.cancel();
				myTimer=null;
			}

			//new CallGooglePlayServices().execute();
			setHasOptionsMenu(false);
			getCarDetails();
			getAppointmentDetails_Volley(session.getAptDate());
			return;
		}

		else if(session.isTripBegin())
		{
			Utility.printLog("onResume INSIDE Driver Arrived");

			//stop the timer to get the current address if any booking is going on
			if(myTimer!=null)
			{
				myTimer.cancel();
				myTimer=null;
			}
			//new CallGooglePlayServices().execute();
			setHasOptionsMenu(false);
			getCarDetails();
			getAppointmentDetails_Volley(session.getAptDate());

			return;
		}
		else if(session.isInvoiceRaised() && !session.isBookingCancelled())
		{
			Utility.printLog("onResume INSIDE DriverInvoiceRaised");

			if(mActivity!=null)
			{
				new BackgroundUnSubscribeAll().execute();
				pollingTimer.cancel();
				session.storeBookingId("0");
				Utility.printLog("mActivity if="+mActivity);
				Intent intent1 = new Intent(mActivity,InvoiceActivity.class);	
				mActivity.startActivity(intent1);
			}
			else
			{
				new BackgroundUnSubscribeAll().execute();
				pollingTimer.cancel();
				session.storeBookingId("0");
				mActivity=(ResideMenuActivity)getActivity();
				Utility.printLog("mActivity else="+mActivity);
				Intent intent1 = new Intent(mActivity,InvoiceActivity.class);	
				mActivity.startActivity(intent1);  
			}
			return;
		}

		new BackgroundSubscribeMyChannel().execute();

		//pubnubProgressDialog.setClickable(false);
		//pubnubProgressDialog.setVisibility(View.VISIBLE);
		//pickup_Distance.setVisibility(View.INVISIBLE);

		Utility.printLog("Car_Type_Id in onresume="+Car_Type_Id);
		startPublishingWithTimer();
	}

	/**
	 * publishing for every 7 seconds to get the new drivers and also to remove the drivers who are going out
	 */
	private void startPublishingWithTimer()
	{
		// TODO Auto-generated method stub
		Utility.printLog("CONTROL INSIDE startPublishingWithTimer");

		if(myTimer_publish!= null)
		{
			Utility.printLog("Timer already started");
			return;
		}
		myTimer_publish = new Timer();

		myTimerTask_publish = new TimerTask()
		{
			@Override
			public void run()
			{
				if(currentLatitude==0.0 || currentLongitude==0.0)
				{
					Utility.printLog("startPublishingWithTimer getServerChannel no location");
				}
				else
				{
					Utility.printLog("startPublishingWithTimer getServerChannel="+session.getServerChannel());
					Utility.printLog("startPublishingWithTimer pubnub message="+"{a:1,pid:"+session.getLoginId()+",lt:"+currentLatitude+",lg:"+currentLongitude+",chn:"+session.getChannelName()+",st:3,tp:"+Car_Type_Id+"}");
					//Log.i("startPublishingWithTimer publish" + session.getServerChannel(),"{a:1,pid:" + session.getLoginId() + ",lt:" + currentLatitude + ",lg:" + currentLongitude + ",chn:" + session.getChannelName() + ",st:3,tp:" + Car_Type_Id + "}");


					_publish(session.getServerChannel(), "{a:1,pid:" + session.getLoginId() + ",lt:" + currentLatitude + ",lg:" + currentLongitude + ",chn:" + session.getChannelName() + ",st:3,tp:" + Car_Type_Id + "}");
					Log.i("","startPublishingWithTimer pubnub message="+"{a:1,pid:"+session.getLoginId()+",lt:"+currentLatitude+",lg:"+currentLongitude+",chn:"+session.getChannelName()+",st:3,tp:"+Car_Type_Id+"}");
				}
			}
		};
		myTimer_publish.schedule(myTimerTask_publish, 000, 5000);
	}

	@Override
	public void onAttach(Activity activity)
	{
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Utility.printLog("CONTROL INSIDE onAttach");
		mActivity = (ResideMenuActivity)getActivity();
	}


	private class PhoneCallListener extends PhoneStateListener
	{
		String LOG_TAG = "LOGGING 123";
		@Override
		public void onCallStateChanged(int state, String incomingNumber) 
		{
			if (TelephonyManager.CALL_STATE_RINGING == state)
			{
				// phone ringing
				Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
			}
			if (TelephonyManager.CALL_STATE_OFFHOOK == state) 
			{
				// active
				Log.i(LOG_TAG, "OFFHOOK");
			}
		}
	}

	/**
	 * checking is there any pending appointments or not while opening the application
	 */
	private void AppStatus()
	{
		if(getActivity()!=null)
		{
			dialog2 = new ProgressDialog(getActivity());
			// make the progress bar cancelable
			dialog2.setCancelable(false);
			// set a message text
			dialog2.setMessage("Checking booking status...");
			if(dialog2!=null) 
			{
				//dialog2.show();
			}
			RequestQueue volleyRequest = Volley.newRequestQueue(getActivity());
			StringRequest myReq = new StringRequest(Request.Method.POST,VariableConstants.BASE_URL+"getApptStatus",
					new Listener<String>()
					{
				@Override
				public void onResponse(String response)
				{
					if (dialog2!=null) 
					{
						//	dialog2.dismiss();
						dialog2=null;
					}
					Log.e("Cardetails Res",""+response);
					Utility.printLog("Success of getting user App Info="+response);
					getAppStatus(response);
				}
					}, 
					new ErrorListener()
					{
						@Override
						public void onErrorResponse(VolleyError error)
						{
							if (dialog2!=null) 
							{
								//dialog2.dismiss();
								dialog2=null;
							}
							Utility.printLog("Success of getting error="+error);
						}
					}){  
				protected HashMap<String,String> getParams() throws com.android.volley.AuthFailureError 
				{ 
					HashMap<String,String> kvPair = new HashMap<String, String>(); 
					//SessionManager session=new SessionManager(getActivity());
					Utility utility=new Utility();
					String curenttime=utility.getCurrentGmtTime();
					Utility.printLog("getSessionToken="+session.getSessionToken());
					Utility.printLog("getDeviceId="+session.getDeviceId());
					if(session.getAptDate()!=null)
						Utility.printLog("getAptDate="+session.getAptDate());
					Utility.printLog("dataandTime="+curenttime);

					kvPair.put("ent_sess_token",session.getSessionToken());
					kvPair.put("ent_dev_id",session.getDeviceId());
					if(session.getAptDate()!=null)
						kvPair.put("ent_appnt_dt",session.getAptDate());
					kvPair.put("ent_user_type","2");
					kvPair.put("ent_date_time",curenttime);
					return kvPair;  
				};  
			};
			volleyRequest.add(myReq);
		}
	}
	private void getAppStatus(String getAppStatus)
	{
		try
		{
			JSONObject jsnResponse = new JSONObject(getAppStatus);
			String jsonErrorParsing = jsnResponse.getString("errFlag");
			Utility.printLog("jsonErrorParsing is ---> "+jsonErrorParsing);

			Gson gson = new Gson();
			statusResponse = gson.fromJson(getAppStatus, StatusInformation.class);

			if(statusResponse!=null)
			{
				Utility.printLog("statusResponse getErrFlag="+statusResponse.getErrFlag());
				if(statusResponse.getErrNum().equals("6") || statusResponse.getErrNum().equals("7") || 
						statusResponse.getErrNum().equals("94") || statusResponse.getErrNum().equals("96"))
				{
					Toast.makeText(getActivity(), statusResponse.getErrMsg(),Toast.LENGTH_SHORT).show();
					Intent i = new Intent(getActivity(), MainActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					getActivity().startActivity(i);
					getActivity().overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
				}

				if(statusResponse.getErrFlag().equals("0"))
				{
					//pubnub = new Pubnub("pub-c-acddd409-5e4b-4fd2-ac67-1e0451ceba96", "sub-c-955bc4de-ee88-11e3-be0c-02ee2ddab7fe", "", true);

					String status_code=null;
					String rateStatus =null;

					if(statusResponse.getData()!=null)
					{
						status_code=statusResponse.getData().get(0).getStatus();
						rateStatus=statusResponse.getData().get(0).getRateStatus();
						Utility.printLog("statusResponse status_code="+status_code);
					}
					else
					{
						status_code=statusResponse.getStatus();
						rateStatus=statusResponse.getRateStatus();
						Utility.printLog("statusResponse status_code="+status_code);
					}

					//store the data when the booking is valid
					if(status_code!=null && session.getAptDate()==null)//if apt date not sending
					{
						if(status_code.equals("6") || status_code.equals("7") || status_code.equals("8") || (status_code.equals("9") && statusResponse.getData().get(0).getRateStatus().equals("1")))
						{
							session.storeAptDate(statusResponse.getData().get(0).getApptDt());
							session.storeDriverEmail(statusResponse.getData().get(0).getEmail());
							session.storeDocPic(VariableConstants.IMAGE_BASE_URL+statusResponse.getData().get(0).getpPic());
							session.storeBookingId(statusResponse.getData().get(0).getBid());
							session.storeDocName(statusResponse.getData().get(0).getfName()+" "+statusResponse.getData().get(0).getlName());
							session.storeDocPH(statusResponse.getData().get(0).getMobile());
							session.storeDropofflat(statusResponse.getData().get(0).getDropLat());
							session.storeDropofflng(statusResponse.getData().get(0).getDropLong());
							session.storePickuplat(statusResponse.getData().get(0).getPickLat());
							session.storePickuplng(statusResponse.getData().get(0).getPickLong());
						}
					}
					else if(status_code!=null)//if apt date sent
					{
						if(status_code.equals("6") || status_code.equals("7") || status_code.equals("8") || (status_code.equals("9") && statusResponse.getRateStatus().equals("1")) )
						{
							session.storeAptDate(statusResponse.getApptDt());
							session.storeDriverEmail(statusResponse.getEmail());
							session.storeDocPic(VariableConstants.IMAGE_BASE_URL+statusResponse.getpPic());
							session.storeBookingId(statusResponse.getBid());
							session.storeDocName(statusResponse.getfName());
							session.storeDocPH(statusResponse.getMobile());
						}
					}

					Utility.printLog("statusResponse DriverOntheWay="+session.isDriverOnWay());
					Utility.printLog("statusResponse DriverReached="+session.isDriverOnArrived());
					Utility.printLog("statusResponse DriverTripBegin="+session.isTripBegin());
					Utility.printLog("statusResponse DriverInvoiceRaised="+session.isInvoiceRaised());

					if(status_code!=null)
						if(status_code.equals("5"))//Driver cancelled apt
						{
							session.setDriverCancelledApt(false);
							session.setDriverOnWay(false);
							Utility.printLog("Wallah set as false Homepage cancel 3");
							session.setDriverArrived(false);
							session.setInvoiceRaised(false);
							session.setTripBegin(false);

							//marker_map.clear();
							marker_map_on_the_way.clear();
							Utility.printLog("marker_map_on_the_way 10");
							marker_map_arrived.clear();
							googleMap.clear();

							startPublishingWithTimer();

							/*if(!(getActivity().getActionBar().isShowing()))
						{
							getActivity().getActionBar().show();
						}*/
							ResideMenuActivity.main_frame_layout.setVisibility(View.VISIBLE);

							isSetDropoffLocation=false;
							isBackPressed=false;
							isFareQuotePressed=false;
							to_latitude=null;
							to_longitude=null;
							mDROPOFF_ADDRESS=null;
							Driver_Confirmation.setVisibility(View.INVISIBLE);
							show_address_relative.setVisibility(View.VISIBLE);
							pickup.setVisibility(View.VISIBLE);
							//setLocationArrow.setVisibility(View.VISIBLE);
							Txt_Pickup.setVisibility(View.VISIBLE);
							Mid_Pointer.setVisibility(View.VISIBLE);
							Driver_on_the_way_txt.setVisibility(View.INVISIBLE);
							//=================My Change======================
							relative_booking.setVisibility(View.INVISIBLE);
							pageno.setVisibility(View.INVISIBLE);
							logo.setVisibility(View.VISIBLE);
							//===============My Change=======================
							//Rl_distance_time.setVisibility(View.INVISIBLE);

							//Utility.ShowAlert("Driver Cancelled Booking Request", getActivity());
						}

					//if session.isDriverOnWay()=true then no need of setting view again. It will set in onresume

						else if(status_code.equals("6") && !session.isDriverOnWay())
						{
							Utility.printLog("getAppStatus INSIDE DriverOnWay");

							session.setDriverOnWay(true);
							Utility.printLog("Wallah set as true Homepage DOW");
							session.setDriverArrived(false);
							session.setTripBegin(false);
							session.setInvoiceRaised(false);

							LatLng latLng = new LatLng(Double.parseDouble(session.getPickuplat()), Double.parseDouble(session.getPickuplng()));

							googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));

							setHasOptionsMenu(false);
							getCarDetails();
							getAppointmentDetails_Volley(session.getAptDate());

							return;
						}
						else if(status_code.equals("7") && !session.isDriverOnArrived())
						{
							Utility.printLog("getAppStatus INSIDE DriverOnArrived");

							session.setDriverOnWay(false);
							Utility.printLog("Wallah set as false Homepage DA");
							session.setDriverArrived(true);
							session.setTripBegin(false);
							session.setInvoiceRaised(false);

							LatLng latLng = new LatLng(Double.parseDouble(session.getPickuplat()), Double.parseDouble(session.getPickuplng()));
							// Showing the current location in Google Map & Zoom in the Google Map
							googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));

							setHasOptionsMenu(false);
							getCarDetails();
							getAppointmentDetails_Volley(session.getAptDate());


							return;
						}
						else if(!session.isTripBegin() && status_code.equals("8"))
						{
							Utility.printLog("getAppStatus INSIDE Driver TripBegin");

							session.setDriverOnWay(false);
							Utility.printLog("Wallah set as false Homepage TB");
							session.setDriverArrived(false);
							session.setTripBegin(true);
							session.setInvoiceRaised(false);

							LatLng latLng = new LatLng(Double.parseDouble(session.getPickuplat()), Double.parseDouble(session.getPickuplng()));
							// Showing the current location in Google Map & Zoom in the Google Map
							googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));

							setHasOptionsMenu(false);
							getCarDetails();
							getAppointmentDetails_Volley(session.getAptDate());

							return;
						}
						else if(((status_code.equals("9"))  && (rateStatus.equals("2"))) && !(session.isInvoiceRaised()))
						{
							Utility.printLog("getAppStatus INSIDE InvoiceRaised");
							if(mActivity!=null)
							{
								new BackgroundUnSubscribeAll().execute();
								pollingTimer.cancel();
								session.storeBookingId("0");
								Utility.printLog("mActivity if="+mActivity);
								Intent intent1 = new Intent(mActivity,InvoiceActivity.class);	
								mActivity.startActivity(intent1);
							}
							else
							{
								new BackgroundUnSubscribeAll().execute();
								pollingTimer.cancel();
								session.storeBookingId("0");
								mActivity=(ResideMenuActivity)getActivity();
								Utility.printLog("mActivity else="+mActivity);
								Intent intent1 = new Intent(mActivity,InvoiceActivity.class);	
								mActivity.startActivity(intent1);  
							}
							return;
						}
				}
			}
		}
		catch(JSONException e)
		{
			Utility.printLog("exp "+e);
			e.printStackTrace();
		}
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		// TODO Auto-generated method stub
		Utility.printLog("inside onTouch");
		return false;
	}

	/**
	 *subscribing to my server channel to listen all available drivers around you
	 */
	class BackgroundSubscribeMyChannel extends AsyncTask<String,Void,String>
	{
		@Override
		protected String doInBackground(String... params)
		{
			// TODO Auto-generated method stub
			Utility.printLog("CONTROL INSIDE MyChannel Subscribe");
			String[] new_channels=new String[1];
			new_channels[0]=session.getChannelName();
			subscribe(new_channels);
			return null;
		}
	}

	/**
	 *stop pubnub while moving to other screen
	 */
	class BackgroundShutdownPubnub extends AsyncTask<String,Void,String>
	{
		@Override
		protected String doInBackground(String... params)
		{
			// TODO Auto-generated method stub
			pubnub.shutdown();
			return null;
		}
	}

	/**
	 * subscribing to driver channels to update the locations of drivers in map
	 */
	class BackgroundSubscribeChannels extends AsyncTask<String, Void, String>
	{
		@Override
		protected String doInBackground(String... params)
		{
			// TODO Auto-generated method stub
			Utility.printLog("CONTROL INSIDE Subscribe Channels length="+params.length);
			subscribe(params);
			//subscribeMultipleChannels(params);
			return null;
		}
	}

	/**
	 * unsubscribing the drivers channels who are going out of the application 
	 */
	class BackgroundUnSubscribeChannels extends AsyncTask<String,Void,String>
	{
		@Override
		protected String doInBackground(String... params) 
		{
			// TODO Auto-generated method stub
			Utility.printLog("CONTROL INSIDE UnSubscribe Channels");
			pubnub.unsubscribe(params);
			return null;
		}
	}

	/**
	 *unsubscribing the all channels  
	 *
	 */
	class BackgroundUnSubscribeAll extends AsyncTask<String,Void,String>
	{
		@Override
		protected String doInBackground(String... params) 
		{
			pubnub.unsubscribeAll();
			return null;
		}
	}






	public static double round(double value, int numberOfDigitsAfterDecimalPoint)
	{
		BigDecimal bigDecimal = new BigDecimal(value);
		bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint,BigDecimal.ROUND_HALF_UP);
		return bigDecimal.doubleValue();
	}



	public static double ConvertMetersToMiles(double meters)
	{
		return (meters / 1609.344);
	}

	/**
	 * checking whether this fragment is in foreground or not
	 */
	public static boolean visibleStatus()
	{
		return visibility;
	}

	private String round(double value)
	{
		String s = String.format("%.2f", value);
		Utility.printLog("rounded value="+s);

		return s;
	}


	/**
	 * Storing all available drivers and performing actions for driver offline and online
	 * @param filterResponse
	 * @return List of new channels to subscribe
	 * 
	 */
	private ArrayList<String> StoreAllAvailableDrivers(PubnubResponseNew filterResponse)   
	{
		//StoreAllAvailableDrivers(filterResponse);
		ArrayList<String> new_channels_to_subscribe = new ArrayList<String>();

		Utility.printLog("StoreAllAvailableDrivers filterResponse.getMasArr().size = "+filterResponse.getMasArr().size());

		Utility.printLog("rahul : showing type 0 StoreAllAvailableDrivers"+""+car_type_index);

		for(int type_count=0;type_count<filterResponse.getMasArr().size();type_count++)
		{
			if(type_count==0)
			{

				// rahul addded a printf 

				//rahul need to clean the code

				if(car_type_index==0)
				{

					Utility.printLog("rahul : showing type 0 StoreAllAvailableDrivers filterResponse.get(0).getMas().getMasArr().size = ");
					if(filterResponse.getMasArr().get(0).getMas()!=null && filterResponse.getMasArr().get(0).getMas().size()>0)
					{
						//Txt_Pickup.setTextSize(18f);
						Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));
						String dis = filterResponse.getMasArr().get(0).getMas().get(0).getD();

						pickup_Distance.setVisibility(View.VISIBLE);
						rate_unit.setVisibility(View.VISIBLE);

						pickup_Distance.setText(round((Double.parseDouble(dis))/1000));
						//pickup_Distance.setText(round(((Double.parseDouble(dis))/1000)*0.621));
						Utility.printLog("pickup_Distance 15");

						distance = round((Double.parseDouble(dis))/1000);

						Type1Distance = distance;
						distance = distance;
					}
					else
					{
						distance = getResources().getString(R.string.nocabs);
						Type1Distance="";
						//Txt_Pickup.setTextSize(20f);
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);

						Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
						Utility.printLog("INSIDE DRIVERS NOT FOUND 20");
					}


					type1NearestDrivers = new ArrayList<String>();

					for(int j=0;j<filterResponse.getEs().get(0).getEm().size();j++)
					{
						type1NearestDrivers.add(filterResponse.getEs().get(0).getEm().get(j));
					}

					for(int i=0;i<filterResponse.getMasArr().get(0).getMas().size();i++)
					{

						//adding new channels to ArrayList
						double driver_current_latitude=Double.parseDouble(filterResponse.getMasArr().get(0).getMas().get(i).getLt());
						double driver_cuttent_longitude=Double.parseDouble(filterResponse.getMasArr().get(0).getMas().get(i).getLg());
						Utility.printLog("Drivers around latlongs="+driver_current_latitude+" "+driver_cuttent_longitude);

						//Get the current location
						Location driverLocation = new Location("starting point");
						driverLocation.setLatitude(driver_current_latitude);
						driverLocation.setLongitude(driver_cuttent_longitude);

						LatLng latLng=new LatLng(driver_current_latitude,driver_cuttent_longitude);

						if(type1_markers.containsKey(filterResponse.getMasArr().get(0).getMas().get(i).getChn()))
						{
							type1_markers.get(filterResponse.getMasArr().get(0).getMas().get(i).getChn()).set(driverLocation);

							//marker_map.get(filterResponse.getMasArr().get(0).getMas().get(i).getChn()).setPosition(latLng);
							//animateMarker(marker_map.get(filterResponse.getMasArr().get(0).getMas().get(i).getChn()), driverLocation); 
						}
						else
						{
							new_channels_to_subscribe.add(filterResponse.getMasArr().get(0).getMas().get(i).getChn());
							type1_channels_list.add(filterResponse.getMasArr().get(0).getMas().get(i).getChn());
							type1_markers.put(filterResponse.getMasArr().get(0).getMas().get(i).getChn(), driverLocation);

							if(isType1MarkersPloted)
							{
							/*	drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon_blue)));*/

								//=============================================My change================================
								drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
								//=============================================My change================================

								marker_map.put(filterResponse.getMasArr().get(0).getMas().get(i).getChn(), drivermarker);
							}
							else if(firstTimeMarkersPlotting)
							{
					/*			drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon_blue)));*/
								//=============================================My change================================
								drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
								//=============================================My change================================

								marker_map.put(filterResponse.getMasArr().get(0).getMas().get(i).getChn(), drivermarker);
							}
							else
							{

								//=============================================My change================================
								drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
								//=============================================My change================================
							/*	drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon_blue)));
			*/					Utility.printLog("rahul type 1 marker ploted");

								marker_map.put(filterResponse.getMasArr().get(0).getMas().get(i).getChn(), drivermarker);
							}
						}
					}

					firstTimeMarkersPlotting = false;
				}
				//rahul :Tempary fix
			}
			else if(type_count==1)
			{
				//type2_markers.clear();
				//type2_channels_list.clear();

				// rahul added a print f 

				if(car_type_index==1)
				{

					Utility.printLog("rahul : showing type 1 StoreAllAvailableDrivers filterResponse.get(1).getMas().getMasArr().size = "+filterResponse.getMasArr().get(1).getMas().size());
					if(filterResponse.getMasArr().get(1).getMas()!=null && filterResponse.getMasArr().get(1).getMas().size()>0)
					{
						//Txt_Pickup.setTextSize(18f);
						Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));
						String dis = filterResponse.getMasArr().get(1).getMas().get(0).getD();

						pickup_Distance.setVisibility(View.VISIBLE);
						rate_unit.setVisibility(View.VISIBLE);

						pickup_Distance.setText(round((Double.parseDouble(dis))/1000));
						//pickup_Distance.setText(round(((Double.parseDouble(dis))/1000)*0.621));
						Utility.printLog("pickup_Distance 16");

						distance = round((Double.parseDouble(dis))/1000);

						Type2Distance = distance;
						distance = distance;

					}
					else
					{
						distance = getResources().getString(R.string.nocabs);
						Type2Distance = "";
						//Txt_Pickup.setTextSize(20f);
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);

						Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
						Utility.printLog("INSIDE DRIVERS NOT FOUND 17");
					}


					type2NearestDrivers = new ArrayList<String>();

					for(int j=0;j<filterResponse.getEs().get(1).getEm().size();j++)
					{
						type2NearestDrivers.add(filterResponse.getEs().get(1).getEm().get(j));
					}

					for(int i=0;i<filterResponse.getMasArr().get(1).getMas().size();i++)
					{
						//adding new channels to ArrayList
						double driver_current_latitude=Double.parseDouble(filterResponse.getMasArr().get(1).getMas().get(i).getLt());
						double driver_cuttent_longitude=Double.parseDouble(filterResponse.getMasArr().get(1).getMas().get(i).getLg());
						Utility.printLog("Drivers around latlongs="+driver_current_latitude+" "+driver_cuttent_longitude);

						//Get the current location
						Location driverLocation = new Location("starting point");
						driverLocation.setLatitude(driver_current_latitude);
						driverLocation.setLongitude(driver_cuttent_longitude);
						LatLng latLng=new LatLng(driver_current_latitude,driver_cuttent_longitude);


						if(type2_markers.containsKey(filterResponse.getMasArr().get(1).getMas().get(i).getChn()))
						{
							Utility.printLog("rahul StoreAllAvailableDrivers Driver found in type2_markers"+filterResponse.getMasArr().get(1).getMas().get(i).getE());

							type2_markers.get(filterResponse.getMasArr().get(1).getMas().get(i).getChn()).set(driverLocation);
							//marker_map.get(filterResponse.getMasArr().get(1).getMas().get(i).getChn()).setPosition(latLng);
							//animateMarker(marker_map.get(filterResponse.getMasArr().get(1).getMas().get(i).getChn()), driverLocation); 													 
						}
						else
						{
							Utility.printLog("StoreAllAvailableDrivers Driver not found in type2_markers"+filterResponse.getMasArr().get(1).getMas().get(i).getE());

							new_channels_to_subscribe.add(filterResponse.getMasArr().get(1).getMas().get(i).getChn());
							type2_markers.put(filterResponse.getMasArr().get(1).getMas().get(i).getChn(), driverLocation);
							type2_channels_list.add(filterResponse.getMasArr().get(1).getMas().get(i).getChn());

							if(isType2MarkersPloted)
							{
							/*	drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon)));
*/
								//=============================================My change================================
								drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
								//=============================================My change================================
								Utility.printLog("rahul type 2 marker ploted");

								marker_map.put(filterResponse.getMasArr().get(1).getMas().get(i).getChn(), drivermarker);
							}
						}
					}
				}                  //rahul: commented this

				/*for(int i=0;i<type2_channels_list.size();i++)
			{
				Utility.printLog("abcdefghij type2_channels_list at "+i+" "+type2_channels_list.get(i));

				Location location = type2_markers.get(type2_channels_list.get(i));
				Utility.printLog("abcdefghij type2_channels_list type2_markers at "+i+" lt:"+location.getLatitude()+" lg:"+location.getLongitude());
			}*/
			}
			else if(type_count==2)
			{
				//type3_markers.clear();
				//type3_channels_list.clear();

				if(car_type_index==2)
				{
					if(filterResponse.getMasArr().get(2).getMas()!=null && filterResponse.getMasArr().get(2).getMas().size()>0)
					{
						//Txt_Pickup.setTextSize(18f);
						Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));
						String dis = filterResponse.getMasArr().get(2).getMas().get(0).getD();

						pickup_Distance.setVisibility(View.VISIBLE);
						rate_unit.setVisibility(View.VISIBLE);

						pickup_Distance.setText(round((Double.parseDouble(dis))/1000));
						//pickup_Distance.setText(round(((Double.parseDouble(dis))/1000)*0.621));
						Utility.printLog("pickup_Distance 17");

						distance = round((Double.parseDouble(dis))/1000);
						Type3Distance = distance;
						distance = distance;

					}
					else
					{
						distance = getResources().getString(R.string.nocabs);
						Type3Distance = "";
						//Txt_Pickup.setTextSize(20f);
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);

						Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
						Utility.printLog("INSIDE DRIVERS NOT FOUND 18");
					}


					type3NearestDrivers = new ArrayList<String>();

					for(int j=0;j<filterResponse.getEs().get(2).getEm().size();j++)
					{
						type3NearestDrivers.add(filterResponse.getEs().get(2).getEm().get(j));
					}
					for(int i=0;i<filterResponse.getMasArr().get(2).getMas().size();i++)
					{
						//adding new channels to ArrayList
						double driver_current_latitude=Double.parseDouble(filterResponse.getMasArr().get(2).getMas().get(i).getLt());
						double driver_cuttent_longitude=Double.parseDouble(filterResponse.getMasArr().get(2).getMas().get(i).getLg());
						Utility.printLog("Drivers around latlongs="+driver_current_latitude+" "+driver_cuttent_longitude);

						//Get the current location
						Location driverLocation = new Location("starting point");
						driverLocation.setLatitude(driver_current_latitude);
						driverLocation.setLongitude(driver_cuttent_longitude);
						LatLng latLng=new LatLng(driver_current_latitude,driver_cuttent_longitude);


						if(type3_markers.containsKey(filterResponse.getMasArr().get(2).getMas().get(i).getChn()))
						{
							//Modify the marker in the map

							type3_markers.get(filterResponse.getMasArr().get(2).getMas().get(i).getChn()).set(driverLocation);
							//marker_map.get(filterResponse.getMasArr().get(2).getMas().get(i).getChn()).setPosition(latLng);
							//animateMarker(marker_map.get(filterResponse.getMasArr().get(2).getMas().get(i).getChn()), driverLocation); 													 
						}
						else
						{
							new_channels_to_subscribe.add(filterResponse.getMasArr().get(2).getMas().get(i).getChn());
							type3_markers.put(filterResponse.getMasArr().get(2).getMas().get(i).getChn(), driverLocation);
							type3_channels_list.add(filterResponse.getMasArr().get(2).getMas().get(i).getChn());

							if(isType3MarkersPloted)
							{
							/*	drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon_red)));*/
								//=============================================My change================================
								drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
								//=============================================My change================================

								marker_map.put(filterResponse.getMasArr().get(2).getMas().get(i).getChn(), drivermarker);
							}
						}
					}
				}
			}
			else if(type_count==3)
			{
				//type4_markers.clear();
				//type4_channels_list.clear();

				if(car_type_index==3)
				{
					if(filterResponse.getMasArr().get(3).getMas()!=null && filterResponse.getMasArr().get(3).getMas().size()>0)
					{
						//Txt_Pickup.setTextSize(18f);
						Txt_Pickup.setText(getResources().getString(R.string.set_pickup_location));
						String dis = filterResponse.getMasArr().get(3).getMas().get(0).getD();

						pickup_Distance.setVisibility(View.VISIBLE);
						rate_unit.setVisibility(View.VISIBLE);

						pickup_Distance.setText(round((Double.parseDouble(dis))/1000));
						//pickup_Distance.setText(round(((Double.parseDouble(dis))/1000)*0.621));
						Utility.printLog("pickup_Distance 18");

						distance = round((Double.parseDouble(dis))/1000);
						Type4Distance = distance;
						distance = distance;

					}
					else
					{
						distance = getResources().getString(R.string.nocabs);
						Type4Distance = "";
						//Txt_Pickup.setTextSize(20f);
						pickup_Distance.setVisibility(View.GONE);
						rate_unit.setVisibility(View.GONE);

						Txt_Pickup.setText(getResources().getString(R.string.no_drivers_found));
						Utility.printLog("INSIDE DRIVERS NOT FOUND 19");
					}


					type4NearestDrivers = new ArrayList<String>();

					for(int j=0;j<filterResponse.getEs().get(3).getEm().size();j++)
					{
						type4NearestDrivers.add(filterResponse.getEs().get(3).getEm().get(j));
					}
					for(int i=0;i<filterResponse.getMasArr().get(3).getMas().size();i++)
					{
						//adding new channels to ArrayList
						double driver_current_latitude=Double.parseDouble(filterResponse.getMasArr().get(3).getMas().get(i).getLt());
						double driver_cuttent_longitude=Double.parseDouble(filterResponse.getMasArr().get(3).getMas().get(i).getLg());
						Utility.printLog("Drivers around latlongs="+driver_current_latitude+" "+driver_cuttent_longitude);

						//Get the current location
						Location driverLocation = new Location("starting point");
						driverLocation.setLatitude(driver_current_latitude);
						driverLocation.setLongitude(driver_cuttent_longitude);
						LatLng latLng=new LatLng(driver_current_latitude,driver_cuttent_longitude);

						if(type4_markers.containsKey(filterResponse.getMasArr().get(3).getMas().get(i).getChn()))
						{
							//Modify the marker in the map
							type4_markers.get(filterResponse.getMasArr().get(3).getMas().get(i).getChn()).set(driverLocation);
							//marker_map.get(filterResponse.getMasArr().get(3).getMas().get(i).getChn()).setPosition(latLng);
							//animateMarker(marker_map.get(filterResponse.getMasArr().get(3).getMas().get(i).getChn()), driverLocation); 													 
						}
						else
						{
							new_channels_to_subscribe.add(filterResponse.getMasArr().get(3).getMas().get(i).getChn());
							type4_markers.put(filterResponse.getMasArr().get(3).getMas().get(i).getChn(), driverLocation);
							type4_channels_list.add(filterResponse.getMasArr().get(3).getMas().get(i).getChn());

							if(isType4MarkersPloted)
							{
							/*	drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_caricon_black)));*/
								//=============================================My change================================
								drivermarker = googleMap.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
								//=============================================My change================================

								marker_map.put(filterResponse.getMasArr().get(3).getMas().get(i).getChn(), drivermarker);
							}
						}
					}
				}
			}
		}
		return new_channels_to_subscribe;
	}

	/**
	 * Updating all drivers locations in map screen
	 * @param filterResponse
	 * 
	 */
	private void UpdateDriverLocations(PubnubResponseNew filterResponse)
	{
		//Utility.printLog("INSIDE UPDATE DRIVERS marker_map.containsKey="+marker_map.containsKey(filterResponse.getChn()));

		double latitude=Double.parseDouble(filterResponse.getLt());
		double longitude=Double.parseDouble(filterResponse.getLg());

		LatLng latLng=new LatLng(latitude,longitude);

		if(response!=null && response.getCarsdetails().size()>0 && filterResponse.getTp()!=null)
			if(response.getCarsdetails().size()>0 && filterResponse.getTp().equals(response.getCarsdetails().get(0).getType_id()))
			{
				if(marker_map.containsKey(filterResponse.getChn()))
				{
					//Update Lat-Lng
					//marker_map.get(filterResponse.getChn()).setPosition(latLng);
					animateMarker(marker_map.get(filterResponse.getChn()), latLng);

				}
			}
			else if(response.getCarsdetails().size()>1 && filterResponse.getTp().equals(response.getCarsdetails().get(1).getType_id()))
			{
				if(marker_map.containsKey(filterResponse.getChn()))
				{
					//Update Lat-Lng
					//marker_map.get(filterResponse.getChn()).setPosition(latLng);
					animateMarker(marker_map.get(filterResponse.getChn()), latLng);
				}
			}
			else if(response.getCarsdetails().size()>2 && filterResponse.getTp().equals(response.getCarsdetails().get(2).getType_id()))
			{
				if(marker_map.containsKey(filterResponse.getChn()))
				{
					//Update Lat-Lng
					//marker_map.get(filterResponse.getChn()).setPosition(latLng);
					animateMarker(marker_map.get(filterResponse.getChn()), latLng);
				}
			}
			else if(response.getCarsdetails().size()>3 && filterResponse.getTp().equals(response.getCarsdetails().get(3).getType_id()))
			{
				if(marker_map.containsKey(filterResponse.getChn()))
				{
					//Update Lat-Lng
					Utility.printLog("UDATING marker: "+filterResponse.getN());
					//marker_map.get(filterResponse.getChn()).setPosition(latLng);
					animateMarker(marker_map.get(filterResponse.getChn()), latLng);
				}
			}
	}

	/**
	 *  Updating driver location when Driver On The Way
	 * @param filterResponse
	 *
	 */
	private void UpdateDriverLocation_DriverOnTheWay(PubnubResponseNew filterResponse)
	{
		/*session.setDriverOnWay(true);
		Utility.printLog("Wallah set as true Homepage 4 6");
		session.setDriverArrived(false);
		session.setTripBegin(false);
		session.setInvoiceRaised(false);*///Roshani

		double driver_current_latitude=Double.parseDouble(filterResponse.getLt());
		double driver_cuttent_longitude=Double.parseDouble(filterResponse.getLg());

		Utility.printLog("marker_map_on_the_way inside channel contains LAT:"+filterResponse.getLt());
		Utility.printLog("marker_map_on_the_way inside channel contains LON:"+filterResponse.getLg());

		session.storeLat_DOW(filterResponse.getLt());
		session.storeLon_DOW(filterResponse.getLg());

		Utility.printLog("INSIDE DRIVER ON THE WAY:4 lat="+driver_current_latitude+
				"  lng="+driver_cuttent_longitude);

		//new CallGooglePlayServices().execute();
		getETAWithTimer();
		LatLng latLng=new LatLng(driver_current_latitude,driver_cuttent_longitude);

		Utility.printLog("marker_map_on_the_way contains session.getCurrentAptChannel="+session.getCurrentAptChannel());	
		Utility.printLog("marker_map_on_the_way contains filterResponse.getChn="+filterResponse.getChn());										
		Utility.printLog("marker_map_on_the_way contains status="+marker_map_on_the_way.containsKey(filterResponse.getChn()));

		if(marker_map_on_the_way.containsKey(filterResponse.getChn()))
		{
			Utility.printLog("marker_map_on_the_way inside channel contains");	
			//if(driver_current_latitude!=0 && driver_cuttent_longitude!=0)
			{
				Utility.printLog("INSIDE DRIVER ON THE WAY:4 getTitle="+driver_on_the_way_marker.getTitle()+" "+ marker_map_on_the_way.get(filterResponse.getChn()).getTitle());
				//Get the current location
				Location driverLocation = new Location("starting_point");
				driverLocation.setLatitude(driver_current_latitude);
				driverLocation.setLongitude(driver_cuttent_longitude);

				rotation_angle = driverLocation.getBearing();

				googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));



				marker_map_on_the_way.get(filterResponse.getChn()).setRotation(rotation_angle);
				marker_map_on_the_way.get(filterResponse.getChn()).setPosition(latLng);
				//======================My Change Car rotation==================//
				animateMarker(marker_map_on_the_way.get(filterResponse.getChn()), latLng);
				//======================My Change Car rotation==================//
			}								
		}

	}

	/**
	 * Updating driver location when Driver Arrived
	 * @param filterResponse
	 * 
	 */
	private void UpdatDriverLocation_DriverArrived(PubnubResponseNew filterResponse)
	{
		/*session.setDriverOnWay(false);
		Utility.printLog("Wallah set as false Homepage 4 7");
		session.setDriverArrived(true);
		session.setTripBegin(false);
		session.setInvoiceRaised(false);*/    //Roshani

		Utility.printLog("marker_map_arrived inside channel contains LAT:"+filterResponse.getLt());
		Utility.printLog("marker_map_arrived inside channel contains LON:"+filterResponse.getLg());


		double driver_current_latitude=Double.parseDouble(filterResponse.getLt());
		double driver_cuttent_longitude=Double.parseDouble(filterResponse.getLg());

		session.storeLat_DOW(filterResponse.getLt());
		session.storeLon_DOW(filterResponse.getLg());

		Utility.printLog("INSIDE DRIVER REACHED:4 current lat="+driver_current_latitude+
				" lng="+driver_cuttent_longitude);

		//new CallGooglePlayServices().execute();
		getETAWithTimer();
		LatLng latLng=new LatLng(driver_current_latitude,driver_cuttent_longitude);
		Utility.printLog("marker_map_arrived contains status="+marker_map_arrived.containsKey(filterResponse.getChn()));

		if(marker_map_arrived.containsKey(filterResponse.getChn()))
		{
			//Modify the marker in the map
			//if(driver_current_latitude!=0 && driver_cuttent_longitude!=0)
			{
				Utility.printLog("INSIDE DRIVER REACHED:4 getTitle="+driver_arrived.getTitle());
				//Get the current location
				Location driverLocation = new Location("starting_point");
				driverLocation.setLatitude(driver_current_latitude);
				driverLocation.setLongitude(driver_cuttent_longitude);

				rotation_angle = driverLocation.getBearing();
				googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
				//googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18f));
				marker_map_arrived.get(filterResponse.getChn()).setRotation(rotation_angle);
				marker_map_arrived.get(filterResponse.getChn()).setPosition(latLng);
				//======================My Change Car rotation==================//
				animateMarker(marker_map_arrived.get(filterResponse.getChn()), latLng);
				//======================My Change Car rotation==================//

			}
		}

	}

	/**
	 * Updating driver location when Driver Journey Started
	 * @param filterResponse
	 */
	private void UpdateDriverLocation_JourneyStarted(PubnubResponseNew filterResponse)
	{
		/*session.setDriverOnWay(false);
		Utility.printLog("Wallah set as false Homepage 4 8");
		session.setDriverArrived(false);
		session.setTripBegin(true);
		session.setInvoiceRaised(false);*/               //Roshani

		double driver_current_latitude=Double.parseDouble(filterResponse.getLt());
		double driver_cuttent_longitude=Double.parseDouble(filterResponse.getLg());

		Utility.printLog("INSIDE DRIVER TripBegin:4 current lat="+driver_current_latitude
				+" lng="+driver_cuttent_longitude);
		session.storeLat_DOW(filterResponse.getLt());
		session.storeLon_DOW(filterResponse.getLg());

		//new CallGooglePlayServices().execute();
		getETAWithTimer();
		LatLng latLng=new LatLng(driver_current_latitude,driver_cuttent_longitude);
		Utility.printLog("marker_map_arrived contains status="+marker_map_arrived.containsKey(filterResponse.getChn()));

		if(marker_map_arrived.containsKey(filterResponse.getChn()))
		{
			//Modify the marker in the map if lat lngs are valid
			//if(driver_current_latitude!=0 && driver_cuttent_longitude!=0)
			{
				Utility.printLog("INSIDE DRIVER TripBegin:4 message="+driver_arrived.getTitle());
				//Get the current location
				Location driverLocation = new Location("starting_point");
				driverLocation.setLatitude(driver_current_latitude);
				driverLocation.setLongitude(driver_cuttent_longitude);

				rotation_angle =driverLocation.getBearing();

				googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
				//googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18f));

				marker_map_arrived.get(filterResponse.getChn()).setRotation(rotation_angle);
				marker_map_arrived.get(filterResponse.getChn()).setPosition(latLng);
				//======================My Change Car rotation==================//
				animateMarker(marker_map_arrived.get(filterResponse.getChn()), latLng);
				//======================My Change Car rotation==================//
			}
		}
	}

	/**
	 *  Updating invoice screen when appointment completed
	 * @param filterResponse
	 */
	private void AppointmentCompleted_InvoiceRaised(PubnubResponseNew filterResponse)
	{
		if(mActivity!=null)
		{
			new BackgroundUnSubscribeAll().execute();
			pollingTimer.cancel();
			session.storeBookingId("0");
			Utility.printLog("mActivity if="+mActivity);
			Intent intent1 = new Intent(mActivity,InvoiceActivity.class);	
			mActivity.startActivity(intent1);
		}
		else
		{
			new BackgroundUnSubscribeAll().execute();
			pollingTimer.cancel();
			session.storeBookingId("0");
			mActivity=(ResideMenuActivity)getActivity();
			Utility.printLog("mActivity else="+mActivity);
			Intent intent1 = new Intent(mActivity,InvoiceActivity.class);	
			mActivity.startActivity(intent1);  
		}
	}

	/**
	 * 
	 *  Driver cancelled the appointment
	 *
	 *
	 */

	private void DriverCancelledAppointment()
	{
		session.setDriverCancelledApt(false);
		session.setDriverOnWay(false);
		Utility.printLog("Wallah set as false Homepage 5");
		session.setDriverArrived(false);
		session.setInvoiceRaised(false);
		session.setTripBegin(false);
		Toast.makeText(getActivity(), "Driver cancelled the request", Toast.LENGTH_LONG).show();
		//marker_map.clear();
		marker_map_on_the_way.clear();
		Utility.printLog("marker_map_on_the_way 2");
		marker_map_arrived.clear();
		googleMap.clear();

		startPublishingWithTimer();

		/*if(!(getActivity().getActionBar().isShowing()))
		{
			getActivity().getActionBar().show();
		}*/
		ResideMenuActivity.main_frame_layout.setVisibility(View.VISIBLE);

		isSetDropoffLocation=false;
		isBackPressed=false;
		isFareQuotePressed=false;
		to_latitude=null;
		to_longitude=null;

		Driver_Confirmation.setVisibility(View.INVISIBLE);
		show_address_relative.setVisibility(View.VISIBLE);
		pickup.setVisibility(View.VISIBLE);
		//setLocationArrow.setVisibility(View.VISIBLE);
		Txt_Pickup.setVisibility(View.VISIBLE);
		Mid_Pointer.setVisibility(View.VISIBLE);
		Driver_on_the_way_txt.setVisibility(View.INVISIBLE);
		//=================My Change======================
		relative_booking.setVisibility(View.GONE);
		pageno.setVisibility(View.INVISIBLE);
		logo.setVisibility(View.VISIBLE);
		//===============My Change=======================
		///Rl_distance_time.setVisibility(View.INVISIBLE);

		//if(isAdded())
		//Utility.ShowAlert("Driver Cancelled Booking Request", getActivity());
	}

	private void sendLaterBooking()
	{
		final ProgressDialog dialogL= Utility.GetProcessDialogNew(getActivity(), "Sending Request");
		if (dialogL!=null)
		{
			dialogL.show();
		}

		if(getActivity()!=null)
		{
			RequestQueue volleyRequest = Volley.newRequestQueue(getActivity());
			StringRequest myReq = new StringRequest(Request.Method.POST,VariableConstants.BASE_URL+"liveBooking",
					new Listener<String>()
					{
				@Override
				public void onResponse(String response)
				{
					if (dialogL!=null)
					{
						dialogL.dismiss();
					}
					Utility.printLog("Success of getting later booking response="+response);
					mPromoCode = null;
					parseSendLaterBooking(response);
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
							}
							Utility.printLog("Success of getting later booking error="+error);
						}
					}){  
				protected HashMap<String,String> getParams() throws com.android.volley.AuthFailureError 
				{ 
					HashMap<String,String> kvPair = new HashMap<String, String>(); 
					SessionManager session=new SessionManager(getActivity());
					Utility utility=new Utility();
					String curenttime=utility.getCurrentGmtTime();

					Utility.printLog("sendLaterBooking Car_Type_Id = "+Car_Type_Id);

					kvPair.put("ent_sess_token",session.getSessionToken());
					kvPair.put("ent_dev_id",session.getDeviceId());
					kvPair.put("ent_wrk_type",Car_Type_Id);
					kvPair.put("ent_addr_line1",mPICKUP_ADDRESS);
					kvPair.put("ent_lat",from_latitude);
					kvPair.put("ent_long",from_longitude);
					Utility.printLog("akbar params"+kvPair);

					if(mDROPOFF_ADDRESS!=null)
					{
						kvPair.put("ent_drop_addr_line1",mDROPOFF_ADDRESS);
						kvPair.put("ent_drop_addr_line2"," ");
						kvPair.put("ent_drop_lat",to_latitude);
						kvPair.put("ent_drop_long",to_longitude);
					}

					kvPair.put("ent_payment_type",payment_type);
					kvPair.put("ent_zipcode","560024");
					kvPair.put("ent_later_dt",laterBookingDate);
					kvPair.put("ent_date_time",curenttime);  

					//kvPair.put("ent_dri_email",nearestDrivers.get(0));

					Utility.printLog("The value of kvPair later booking ::  "+kvPair);

					return kvPair;  
				};  
			};
			int socketTimeout = 30000;//30 seconds - change to what you want
			RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
			myReq.setRetryPolicy(policy);

			volleyRequest.add(myReq);
		}
	}
	private void parseSendLaterBooking(String liveBookingStatus)
	{
		GetCarDetails response = new GetCarDetails();
		Gson gson = new Gson();
		response=gson.fromJson(liveBookingStatus,GetCarDetails.class);

		if(response!=null)
		{
			if(response.getErrFlag().equals("0"))
			{
				//getActivity().getActionBar().show();
				ResideMenuActivity.main_frame_layout.setVisibility(View.VISIBLE);
				isSetDropoffLocation=false;
				isBackPressed=false;
				isFareQuotePressed=false;
				isSetDropoffLocation=false;
				to_latitude=null;
				to_longitude=null;
				mDROPOFF_ADDRESS=null;
				Dropoff_Location_Address.setText("");
				new_dropoff_location_address.setText("");
				show_address_relative.setVisibility(View.VISIBLE);
				Txt_Pickup.setVisibility(View.VISIBLE);
				pickup.setVisibility(View.VISIBLE);
				//setLocationArrow.setVisibility(View.VISIBLE);
				relativePickupLocation.setVisibility(View.INVISIBLE);
				Request_Pick_up_here.setVisibility(View.INVISIBLE);
				Fare_Quote.setVisibility(View.INVISIBLE);
				farePromoLayouy.setVisibility(View.INVISIBLE);
				Relative_Card_Info.setVisibility(View.GONE);
				//=============================My Change=================================
				req_lay.setVisibility(View.INVISIBLE);
				//=============================My Change=================================
				Card_Info.setVisibility(View.INVISIBLE);
				Card_Image.setVisibility(View.INVISIBLE);
				Relative_Dropoff_Location.setVisibility(View.INVISIBLE);
				Relative_Pickup_Navigation.setVisibility(View.INVISIBLE);
				all_types_layout.setVisibility(View.VISIBLE);
				now_later_layout.setVisibility(View.VISIBLE);

				laterBookingDate = null;//after completing the booking resetting the booking date
				Utility.ShowAlert(response.getErrMsg(), getActivity());
			}
			else
			{
				Utility.printLog("inside RESULT_OK true");
				//getActivity().getActionBar().show();
				ResideMenuActivity.main_frame_layout.setVisibility(View.VISIBLE);
				isSetDropoffLocation=false;
				isBackPressed=false;
				isFareQuotePressed=false;
				isSetDropoffLocation=false;
				to_latitude=null;
				to_longitude=null;
				mDROPOFF_ADDRESS=null;
				Dropoff_Location_Address.setText("");
				new_dropoff_location_address.setText("");
				show_address_relative.setVisibility(View.VISIBLE);
				Txt_Pickup.setVisibility(View.VISIBLE);
				pickup.setVisibility(View.VISIBLE);
				//setLocationArrow.setVisibility(View.VISIBLE);
				relativePickupLocation.setVisibility(View.INVISIBLE);
				Request_Pick_up_here.setVisibility(View.INVISIBLE);
				Fare_Quote.setVisibility(View.INVISIBLE);
				farePromoLayouy.setVisibility(View.INVISIBLE);
				Relative_Card_Info.setVisibility(View.GONE);
				//=============================My Change=================================
				req_lay.setVisibility(View.INVISIBLE);
				//=============================My Change=================================
				Card_Info.setVisibility(View.INVISIBLE);
				Card_Image.setVisibility(View.INVISIBLE);
				Relative_Dropoff_Location.setVisibility(View.INVISIBLE);
				Relative_Pickup_Navigation.setVisibility(View.INVISIBLE);
				all_types_layout.setVisibility(View.VISIBLE);
				now_later_layout.setVisibility(View.VISIBLE);

				laterBookingDate = null;//after completing the booking resetting the booking date
				Utility.ShowAlert(response.getErrMsg(), getActivity());
			}
		}	
		else
		{
			Utility.printLog("inside RESULT_OK true");
			//getActivity().getActionBar().show();
			ResideMenuActivity.main_frame_layout.setVisibility(View.VISIBLE);
			isSetDropoffLocation=false;
			isBackPressed=false;
			isFareQuotePressed=false;
			isSetDropoffLocation=false;
			to_latitude=null;
			to_longitude=null;
			mDROPOFF_ADDRESS=null;
			Dropoff_Location_Address.setText("");
			new_dropoff_location_address.setText("");
			show_address_relative.setVisibility(View.VISIBLE);
			Txt_Pickup.setVisibility(View.VISIBLE);
			pickup.setVisibility(View.VISIBLE);
			//setLocationArrow.setVisibility(View.VISIBLE);
			relativePickupLocation.setVisibility(View.INVISIBLE);
			Request_Pick_up_here.setVisibility(View.INVISIBLE);
			Fare_Quote.setVisibility(View.INVISIBLE);
			farePromoLayouy.setVisibility(View.INVISIBLE);
			Relative_Card_Info.setVisibility(View.GONE);
			//=============================My Change=================================
			req_lay.setVisibility(View.INVISIBLE);
			//=============================My Change=================================
			Card_Info.setVisibility(View.INVISIBLE);
			Card_Image.setVisibility(View.INVISIBLE);
			Relative_Dropoff_Location.setVisibility(View.INVISIBLE);
			Relative_Pickup_Navigation.setVisibility(View.INVISIBLE);
			all_types_layout.setVisibility(View.VISIBLE);
			now_later_layout.setVisibility(View.VISIBLE);

			laterBookingDate = null;//after completing the booking resetting the booking date
		}
	}

	private int getRelativeLeft(View myView) 
	{
		if (myView.getParent() == myView.getRootView())
			return myView.getLeft();
		else
			return myView.getLeft() + getRelativeLeft((View) myView.getParent());
	}


	protected class MyGpsListener implements GpsStatus.Listener, LocationListener 
	{
		// the last location time is needed to determine if a fix has been lost
		private long locationTime = 0;
		@Override
		public void onGpsStatusChanged(int changeType) 
		{
			Utility.printLog("HomePage onGpsStatusChanged="+gpsEnabled);

			if(locationManager != null) 
			{
				// status changed so ask what the change was
				switch(changeType) 
				{
				case GpsStatus.GPS_EVENT_FIRST_FIX:

					Utility.printLog("HomePage GPS_EVENT_FIRST_FIX");
					ResideMenuActivity.gpsErrorLayout.setVisibility(View.GONE);
					/*if(dialog!=null && dialog.isShowing())
					{
						dialog.dismiss();
					}*/
					gpsEnabled = true;
					gpsFix = true;

					break;
				case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
					Utility.printLog("HomePage GPS_EVENT_SATELLITE_STATUS");
					ResideMenuActivity.gpsErrorLayout.setVisibility(View.GONE);
					/*if(dialog!=null && dialog.isShowing())
					{
						dialog.dismiss();
					}*/
					gpsEnabled = true;
					// if it has been more then 10 seconds since the last update, consider the fix lost
					gpsFix = System.currentTimeMillis() - locationTime < DURATION_TO_FIX_LOST_MS;
					break;
				case GpsStatus.GPS_EVENT_STARTED: // GPS turned on
					Utility.printLog("HomePage GPS_EVENT_STARTED");
					ResideMenuActivity.gpsErrorLayout.setVisibility(View.GONE);
					/*if(dialog!=null && dialog.isShowing())
					{
						dialog.dismiss();
					}*/
					gpsEnabled = true;
					gpsFix = false;
					break;
				case GpsStatus.GPS_EVENT_STOPPED: // GPS turned off
					Utility.printLog("HomePage GPS_EVENT_STOPPED");
					if(isAdded())
					{
						//showGpsAlert();
						ResideMenuActivity.gpsErrorLayout.setVisibility(View.VISIBLE);
					}
					gpsEnabled = false;
					gpsFix = false;
					break;
				default:
					if(isAdded())
					{
						//showGpsAlert();
						ResideMenuActivity.gpsErrorLayout.setVisibility(View.VISIBLE);
					}
					Utility.printLog("HomePage default "+changeType);
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
			// dont need this info 
		}

		@Override
		public void onProviderEnabled(String provider) {
			// dont need this info 
		}

		@Override
		public void onProviderDisabled(String provider) {
			// dont need this info 
		}
	}

	public void showGpsAlert()
	{
		dialog = new Dialog(getActivity());
		dialog.setTitle("No Location Access");
		dialog.setContentView(R.layout.gps_alert);
		dialog.setCancelable(false);

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
				getActivity().finish();
			}
		});
		dialog.show();
	}

	private void OnBackStackChangedListener() 
	{
		// TODO Auto-generated method stub

		Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show();
	}

	public void onEventMainThread(MyEventBus myEventBus)
	{
		Utility.printLog("onEventMainThread() called: "+myEventBus.getEventType());

		if(myEventBus.getEventType().equals("info"))
		{
			getTipOnClick();
		}
	}

	public void getTipOnClick()
	{

		AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
		//builderSingle.setIcon(R.drawable.ic_launcher);
		builderSingle.setTitle("Select Driver Tip:");
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				getActivity(),
				android.R.layout.select_dialog_singlechoice);

		if(ResideMenuActivity.invoice_driver_tip.getText().equals(getResources().getString(R.string.driver_tip)))
		{
			for(int i=0;i<=30;i+=5)
			{
				arrayAdapter.add(i+"%");
			}
		}
		else
		{
			arrayAdapter.add("Remove Tip");
			for(int i=0;i<=30;i+=5)
			{
				arrayAdapter.add(i+"%");
			}
		}

		builderSingle.setNegativeButton("cancel",new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		});

		builderSingle.setAdapter(arrayAdapter,new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
				if(which==0)
				{
					if(ResideMenuActivity.invoice_driver_tip.getText().equals(getResources().getString(R.string.driver_tip)))
					{
						ResideMenuActivity.invoice_driver_tip.setText(arrayAdapter.getItem(which));
						UpdateTipforDriver(""+arrayAdapter.getItem(which));
					}
					else
					{
						ResideMenuActivity.invoice_driver_tip.setText(getResources().getString(R.string.driver_tip));
						UpdateTipforDriver("0");
					}
				}
				else
				{
					ResideMenuActivity.invoice_driver_tip.setText(arrayAdapter.getItem(which));
					UpdateTipforDriver(""+arrayAdapter.getItem(which));
				}
			}
		});
		builderSingle.show();

	}

	public void UpdateTipforDriver(final String driverTip)
	{
		final ProgressDialog dialogL= Utility.GetProcessDialogNew(getActivity(),getResources().getString(R.string.please_wait));
		dialogL.setCancelable(true);
		if(dialogL!=null)
		{
			dialogL.show();
		}
		RequestQueue volleyRequest = Volley.newRequestQueue(getActivity());

		StringRequest myReq = new StringRequest(Request.Method.POST,VariableConstants.BASE_URL+"updateTip",
				new Listener<String>() {
			@Override
			public void onResponse(String response2) 
			{
				Utility.printLog("Success of getting user Info"+response2);
				JSONObject jsnResponse;
				try 
				{
					dialogL.dismiss();
					jsnResponse = new JSONObject(response2);
					String mErrNum = jsnResponse.getString("errNum");
					Utility.printLog("jsonErrorParsing is ---> "+mErrNum);

					Toast.makeText(getActivity(),jsnResponse.getString("errMsg"), Toast.LENGTH_SHORT).show();
					Gson gson = new Gson();
					tipResponse = gson.fromJson(response2,TipResponse.class);
					Utility.printLog("tipResponse = "+tipResponse.getTip());


					/*subtotal.setText(session.getCurrencySymbol()+" "+round(Double.parseDouble(tipResponse.getFare())));
					if(tipResponse.getCode()==null || tipResponse.getCode().equals(""))
					{
						promoCode.setText("DISCOUNT");
					}
					else
					{
						promoCode.setText("DISCOUNT("+tipResponse.getCode()+")");
					}
					if(tipResponse.getDiscount()==null || tipResponse.getDiscount().equals(""))
					{
						discount.setText(session.getCurrencySymbol()+" 0.00");
					}
					else
					{
						discount.setText(session.getCurrencySymbol()+" "+round(Double.parseDouble(tipResponse.getDiscount())));
					}

					tipPercentage.setText("TIP("+driverTip+"%)");

					if(tipResponse.getTip()==null || tipResponse.getTip().equals(""))
					{
						tip.setText(session.getCurrencySymbol()+" 0.00");
					}
					else
					{
						tip.setText(session.getCurrencySymbol()+" "+round(Double.parseDouble(tipResponse.getTip())));
					}

					if(!tipResponse.getFare().equals("") && !tipResponse.getDiscount().equals(""))
					{
						double sub = Double.parseDouble(tipResponse.getFare()) - Double.parseDouble(tipResponse.getDiscount());
						newSubtotal.setText(session.getCurrencySymbol()+" "+round(sub));
					}

					total.setText(session.getCurrencySymbol()+" "+round(Double.parseDouble(tipResponse.getAmount())));

					Fare.setText(session.getCurrencySymbol()+" "+round(Double.parseDouble(tipResponse.getAmount())));
					total_fare.setText(session.getCurrencySymbol()+" "+round(Double.parseDouble(tipResponse.getAmount())));*/

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, 	new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error)
			{
				dialogL.dismiss();
				Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
				Utility.printLog("Error for volley");
			}
		}){  
			protected HashMap<String,String> getParams() throws com.android.volley.AuthFailureError
			{  
				HashMap<String,String> kvPair = new HashMap<String, String>(); 

				SessionManager session=new SessionManager(getActivity());
				Utility utility=new Utility();
				String curenttime=utility.getCurrentGmtTime();
				Utility.printLog("dataandTime="+curenttime);
				//	Utility.printLog("UpdateTipforDriver ent_amount="+tip);
				Utility.printLog("UpdateTipforDriver getEmail="+response.getEmail());
				//	Utility.printLog("UpdateTipforDriver getApptDt="+response.getApptDt());

				kvPair.put("ent_sess_token",session.getSessionToken());
				kvPair.put("ent_dev_id",session.getDeviceId());
				kvPair.put("ent_tip",driverTip);
				kvPair.put("ent_booking_id",session.getBookingId());
				kvPair.put("ent_date_time",curenttime);
				Utility.printLog("UpdateTipforDriver session token="+session.getSessionToken());
				Utility.printLog("UpdateTipforDriver = "+kvPair);

				return kvPair;  
			};  
		};
		volleyRequest.add(myReq);
	}

	//to get the address on the homescreen
	class BackgroundGetAddress extends AsyncTask<String, Void, String>
	{
		List<Address> address;
		String lat,lng;
		@Override
		protected String doInBackground(String... params) {


			try {
				Log.i("","LatLong test lat "+ params[0] );
				Log.i("","LatLong test lon "+ params[1] );
				lat = params[0];
				lng = params[1];

				if(lat!=null && lng!=null)
				{
					if(getActivity()!=null)
					{

						geocoder=new Geocoder(getActivity());
					}

					if(geocoder!=null)
					{
						address=geocoder.getFromLocation(Double.parseDouble(params[0]), Double.parseDouble(params[1]), 1);

					}
				}
			} catch (IOException e) 
			{

				e.printStackTrace();
				Utility.printLog("address.get(0): INSIDE CATCH");
				new BackgroundGeocodingTask().execute();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) 
		{


			super.onPostExecute(result);
			if(address!=null && address.size()>0)
			{
				Utility.printLog("mmm my addre out side"+address);
				Log.i("","Current "+address.get(0).getAddressLine(0)+", "+address.get(0).getAddressLine(1));
				Utility.printLog("address.get(0).getLocality() "+address.get(0).getLocality(),"address.get(0).getSubAdminArea() "+address.get(0).getSubAdminArea(),"address.get(0).getSubLocality()"+address.get(0).getSubLocality());


				Utility.printLog("mmm my addre in side"+address.get(0).getAddressLine(0)+", "+address.get(0).getAddressLine(1));



				current_address.setText(address.get(0).getAddressLine(0)+", "+address.get(0).getAddressLine(1));
				pickupLocationAddress.setText(address.get(0).getAddressLine(0)+", "+address.get(0).getAddressLine(1));
				mPICKUP_ADDRESS=pickupLocationAddress.getText().toString();
				Utility.printLog("akbar:location"+mPICKUP_ADDRESS);
				//Full address
				//	VariableConstants.book_address=address.get(0).getAddressLine(0)+", "+address.get(0).getAddressLine(1);


				//Area name for addrs line 2
				if(address.get(0).getLocality()!=null)
				{
					/*if(address.get(0).getSubLocality()!=null)
							//VariableConstants.area_name=address.get(0).getSubLocality()+", "+address.get(0).getLocality();
						else if(address.get(0).getSubAdminArea()!=null)
							VariableConstants.area_name=address.get(0).getSubAdminArea()+", "+address.get(0).getLocality();
						else
							VariableConstants.area_name=address.get(0).getLocality();*/
				}
				else
				{
					//VariableConstants.area_name=address.get(0).getAddressLine(0)+", "+address.get(0).getAddressLine(1);
				}
				//Utility.printLog("VariableConstants.area_name: "+VariableConstants.area_name);

			}

		}
	}



	class BackgroundGeocodingTask extends AsyncTask<String, Void, String>
	{
		GeocodingResponse response;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {

			String url="https://maps.googleapis.com/maps/api/geocode/json?latlng="+currentLatitude+","+currentLongitude+"&sensor=false&key="+VariableConstants.GOOGLE_SERVER_API_KEY;
			Utility.printLog("Geocoding url: "+url);

			String stringResponse=Utility.callhttpRequest(url);

			if(stringResponse!=null)
			{
				Gson gson=new Gson();
				response=gson.fromJson(stringResponse, GeocodingResponse.class);
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			if(response!=null)
			{
				if(response.getStatus().equals("OK"))
				{

					current_address.setText(response.getResults().get(0).getFormatted_address());
					//VariableConstants.book_address=response.getResults().get(0).getFormatted_address();
					pickupLocationAddress.setText(response.getResults().get(0).getFormatted_address());
					Utility.printLog("akbar:location"+pickupLocationAddress);

					String short_address[]=response.getResults().get(0).getFormatted_address().split(",");
					String temp_address=null;
					if(short_address.length==1)
					{

						if(short_address[0]!=null)
						{
							temp_address=short_address[0];
							if(short_address.length==2)
							{

								if(short_address[1]!=null)
								{
									temp_address=temp_address+", "+short_address[1];


									if(short_address.length==3)
									{

										if(short_address[2]!=null)
										{
											temp_address=temp_address+", "+short_address[2];
										}
									}

								}
							}


						}
						Utility.printLog("VariableConstants.area_name: "+temp_address);
						//VariableConstants.area_name=temp_address;
					}
				}
			}
		}
	}

	private void getETAWithTimer()
	{
		Utility.printLog("Test ETA Inside getETAWithTimer()");

		if (myTimer_ETA!= null) {


			Utility.printLog("Test ETA Inside myTimer_ETA!= null");
			return;
		}
		myTimer_ETA = new Timer();

		myTimerTask_ETA = new TimerTask()
		{
			@Override
			public void run() 
			{

				Utility.printLog("Test ETA Inside run()");
				if(getActivity()!=null)
				{
					getActivity().runOnUiThread(new Runnable()
					{
						public void run() 
						{
							String[] params=new String[4];
							if(session.isDriverOnWay())
							{
								params[0]=session.getPickuplat();
								params[1]=session.getPickuplng();
								params[2]=eta_latitude;//session.getLat_DOW()
								params[3]=eta_longitude;//session.getLon_DOW()
							}
							if(session.isDriverOnArrived() || session.isTripBegin())
							{
								params[0]=session.getDropofflat();
								params[1]=session.getDropofflng();
								params[2]=eta_latitude;//session.getLat_DOW()
								params[3]=eta_longitude;//session.getLon_DOW()

							}


							Utility.printLog("Test ETA session.getLat_DOW(): "+session.getLat_DOW());
							Utility.printLog("Test ETA session.getLon_DOW(): "+session.getLon_DOW());
							if("0".equals(params[0]) || "0".equals(params[1]) || "0".equals(params[2]) || "0".equals(params[3]))
							{

								Driver_Distance.setText("DISTANCE: .....");
								Driver_Time.setText("TIME: ....");


							}


							else
							{


								new BackgroundETAandDistanceTask().execute(params);

							}

						}
					});
				}

			}
		};
		myTimer_ETA.schedule(myTimerTask_ETA, 000,5000);
	}

	private double distance(double lat1, double lng1, double lat2, double lng2) {

		double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

		double dLat = Math.toRadians(lat2-lat1);
		double dLng = Math.toRadians(lng2-lng1);

		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);

		double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
				* Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

		double dist = earthRadius * c;

		return dist; // output distance, in MILES
	}

	class BackgroundETAandDistanceTask extends AsyncTask<String, Void, String>
	{
		ETADistanceResponse response;

		@Override
		protected String doInBackground(String... params) 
		{


			String url="https://maps.googleapis.com/maps/api/directions/json?origin="+params[0]+","+params[1]+"&destination="+params[2]+","+params[3]+"&key="+VariableConstants.GOOGLE_SERVER_API_KEY;
			Utility.printLog("Test ETA URL: "+url);

			String stringResponse=Utility.callhttpRequest(url);

			if(stringResponse!=null)
			{
				Gson gson=new Gson();
				response=gson.fromJson(stringResponse, ETADistanceResponse.class); 
				Utility.printLog("Test ETA Response: "+stringResponse);
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{

			super.onPostExecute(result);
			Utility.printLog("get eta response"+response);

			if(response!=null)
			{
				Utility.printLog("get eta status"+response.getStatus());

				if(response.getStatus().equals("OK"))
				{
					Utility.printLog("Test ETA DIstance: "+eta_latitude);
					Utility.printLog("Test ETA Value: "+eta_longitude);
					try
					{
						Driver_Time.setText("TIME:"+response.getRoutes().get(0).getLegs().get(0).getDuration().getText());
						Driver_Distance.setText( "DISTANCE:"+round(Double.parseDouble(response.getRoutes().get(0).getLegs().get(0).getDistance().getValue())/1609.344)+" Miles");
						//Driver_Distance.setText(getResources().getString(R.string.dist)+response.getRoutes().get(0).getLegs().get(0).getDistance().getValue());
						//Driver_Distance.setText(getResources().getString(R.string.dist)+round(Double.parseDouble(response.getRoutes().get(0).getLegs().get(0).getDistance().getValue())/1000)+" "+getResources().getString(R.string.distanceUnit));
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}


				}
			}
		}

	}

	class BackgroundUpdateAddress extends AsyncTask<String, Void,String>
	{
		GetCardResponse response;
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			Utility.printLog("akbar new address  onPreExecute");

			//mDialog = Utility.getProgressDialog(getActivity(),getResources().getString(R.string.please_wait));
			

			if(mDialog!=null)
			{
				mDialog.dismiss();
				mDialog = null;
			}
		}

		@Override
		protected String doInBackground(String... params)
		{
			String url=VariableConstants.BASE_URL+"updateDropOff";
			SessionManager session=new SessionManager(getActivity());

			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			Utility.printLog("Appointments "+session.getSessionToken());
			Utility.printLog("Appointments "+session.getDeviceId());
			Utility.printLog("Appointments LoginId "+session.getLoginId());
			Utility.printLog("Appointments DriverEmail "+session.getDriverEmail());
			Utility.printLog("Appointments currentdateandTime="+curenttime+" Apptdate="+session.getAptDate());

			HashMap<String, String> kvPairs = new HashMap<String, String>();

			kvPairs.put("ent_sess_token",session.getSessionToken());
			kvPairs.put("ent_dev_id",session.getDeviceId());
			kvPairs.put("ent_booking_id",session.getBookingId());
			kvPairs.put("ent_drop_addr1",mDROPOFF_ADDRESS);
			kvPairs.put("ent_lat",to_latitude);
			kvPairs.put("ent_long",to_longitude);
            kvPairs.put("ent_date_time",curenttime);
            Utility.printLog("akbar new params"+kvPairs);
			HttpResponse httpResponse = null;
			try
			{
				httpResponse = Utility.doPost(url,kvPairs);
			} catch (ClientProtocolException e1) 
			{
				e1.printStackTrace();
				Utility.printLog("", "doPost Exception......."+e1);
			} catch (IOException e1) 
			{
				e1.printStackTrace();
				Utility.printLog("", "doPost Exception......."+e1);
			}

			String jsonResponse = null;
			if (httpResponse!=null) 
			{

				try 
				{
					jsonResponse = EntityUtils.toString(httpResponse.getEntity());
				} catch (ParseException e) {
					e.printStackTrace();
					Utility.printLog("", "doPost Exception......."+e);
				} catch (IOException e) {
					e.printStackTrace();
					Utility.printLog("", "doPost Exception......."+e);
				}
			}
			Utility.printLog(" ","GetCards: "+jsonResponse);
			Utility.printLog(" ",jsonResponse);
			Log.e("Drop off" ,""+jsonResponse);

			if(jsonResponse!=null) 
			{
				Gson gson = new Gson();
				response=gson.fromJson(jsonResponse,GetCardResponse.class);
				Utility.printLog("","DONE WITH GSON");
			}else
			{
				getActivity().runOnUiThread(new Runnable()
				{
					public void run() 
					{
						Toast.makeText(getActivity(),getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
					}
				});
			}
			return null;
		}


		@Override
		protected void onPostExecute(String result) 
		{
			super.onPostExecute(result);
			if (mDialog!=null) 
			{
				mDialog.dismiss();
				mDialog.cancel();
				mDialog = null;
			}
			if(response!=null)
			{
				if(response.getErrFlag().equals("0"))
				{
					Utility.GetProcessDialogNew(getActivity(),"Thank you for confirming your destination address on update drop off location");
					new_dropoff_location_address.setText(mDROPOFF_ADDRESS);
					//new_add_drop_off_location.setVisibility(View.GONE);
				}
				else
				{
					/*Utility.ShowAlert(response.getErrMsg(), getActivity());
					if(response.getErrNum().equals("51"))
					{
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								getActivity());

						// set title
						alertDialogBuilder.setTitle(getResources().getString(R.string.error));

						// set dialog message
						alertDialogBuilder
						.setMessage(getResources().getString(R.string.credit_cards_not_found))
						.setCancelable(false)

						.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
								Intent i = new Intent(getActivity(), OPA_MapPage.class);
								// set the new task and clear flags
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(i);
							}
						});

						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
						// show it
						alertDialog.show();
					}*/
				}
			}
			else
			{
				Utility.ShowAlert(getResources().getString(R.string.server_error), getActivity());
			}
		}
	}
	
	
	
	
}