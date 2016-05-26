package com.app.taxisharingDriver;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.taxisharingDriver.response.AppointmentData;
import com.app.taxisharingDriver.response.AppointmentDetailData;
import com.app.taxisharingDriver.utility.ConnectionDetector;
import com.app.taxisharingDriver.utility.NetworkConnection;
import com.app.taxisharingDriver.utility.SessionManager;
import com.app.taxisharingDriver.utility.Utility;
import com.app.taxisharingDriver.utility.VariableConstants;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class BookingHistory extends Activity
{
	private TextView pickupLocation,dropoffLocation,distance,pick_time,drop_time,total_time,approx_fare,waiting_time,tip,tip_text;
	private TextView totalFare,toll_tax_fare,parking_tax_fare,airport_tax_fare,meter_tax_fare;
	private ActionBar actionBar;
	private TextView cs1,cs2,cs3,cs4,cs5,cs6;
	private String currencySymbol = VariableConstants.CURRENCY_SYMBOL;
	private SessionManager sessionManager;
	//private RatingBar ratingBar;
	private RelativeLayout network_bar;
	private TextView network_text;
	private ProgressDialog mdialog;
	private BroadcastReceiver receiver;
	private IntentFilter filter;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.booking_history);
		overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
		sessionManager = new SessionManager(this);
		initLayoutId();
		actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setTitle("BOOKING HISTORY");

		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();

		ConnectionDetector connectionDetector=new ConnectionDetector(BookingHistory.this);
		if (connectionDetector.isConnectingToInternet()) 
		{
			getAppointmentDetails(bundle.getString("EMAIL"), bundle.getString("APPTDT"));
		}

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

					if(status.equals("1"))
					{
						network_bar.setVisibility(View.GONE);
					}
					else
					{
						if (!Utility.isNetworkAvailable(BookingHistory.this))
						{
							network_bar.setVisibility(View.VISIBLE);
							return;
						}
						else if (!NetworkConnection.isConnectedFast(BookingHistory.this)) 
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

	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) 
		{
		case android.R.id.home:
			sessionManager.setFlagCalender(true);
			Intent intent = new Intent(BookingHistory.this,MainActivity.class);
			startActivity(intent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
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
	}

	@Override
	protected void onPause() 
	{
		super.onPause();
		unregisterReceiver(receiver);
	}

	private void initLayoutId()
	{
		cs1 = (TextView)findViewById(R.id.cs1);
		cs2 = (TextView)findViewById(R.id.cs2);
		cs3 = (TextView)findViewById(R.id.cs3);
		cs4 = (TextView)findViewById(R.id.cs4);
		cs5 = (TextView)findViewById(R.id.cs5);
		cs6 = (TextView)findViewById(R.id.cs6);
		totalFare = (TextView)findViewById(R.id.total_tax_fare);
		toll_tax_fare = (TextView)findViewById(R.id.toll_tax_fare);
		parking_tax_fare = (TextView)findViewById(R.id.parking_tax_fare);
		airport_tax_fare = (TextView)findViewById(R.id.airport_tax_fare);
		meter_tax_fare = (TextView)findViewById(R.id.meter_tax_fare);
		network_bar = (RelativeLayout)findViewById(R.id.network_bar);
		network_text = (TextView)findViewById(R.id.network_text);
		pickupLocation = (TextView)findViewById(R.id.pickup_address);
		dropoffLocation = (TextView)findViewById(R.id.dropoff_address);
		approx_fare = (TextView)findViewById(R.id.total_amount);
		distance = (TextView)findViewById(R.id.distance);
		pick_time = (TextView)findViewById(R.id.pickup_time);
		drop_time = (TextView)findViewById(R.id.dropoff_time);
		total_time = (TextView)findViewById(R.id.total_time);
		waiting_time = (TextView)findViewById(R.id.avg_spd);
		//ratingBar = (RatingBar)findViewById(R.id.invoice_driver_rating);
		tip = (TextView)findViewById(R.id.tip);
		tip_text = (TextView)findViewById(R.id.tip_text);

	}
	private void getAppointmentDetails(String email,String aptDateTime)
	{
		Utility.printLog("Animation action email"+email,"Animation action Date Time"+aptDateTime);
		SessionManager sessionManager=new SessionManager(BookingHistory.this);
		Utility utility=new Utility();
		String sessionToken=sessionManager.getSessionToken();
		String deviceid=Utility.getDeviceId(BookingHistory.this);
		String currentDate=utility.getCurrentGmtTime();

		final String mparams[]={sessionToken,deviceid,email,aptDateTime,currentDate};
		//final ProgressDialog mdialog;
		mdialog=Utility.GetProcessDialog(BookingHistory.this);
		mdialog.setMessage(getResources().getString(R.string.Pleasewaitmessage));
		mdialog.show();
		mdialog.setCancelable(false);
		RequestQueue queue = Volley.newRequestQueue(BookingHistory.this);
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
			Utility.printLog("getAppointmentDetails  response for History "+response);
			AppointmentData  appointmentData;
			Gson gson = new Gson();
			appointmentData=gson.fromJson(response, AppointmentData.class);

			if (mdialog!=null)
			{
				mdialog.dismiss();
				mdialog.cancel();
			}
			try
			{						
				if (appointmentData.getErrFlag()==0 && appointmentData.getErrNum() == 21)
				{
					// 21 -> (0) Got the details!
					AppointmentDetailData appointmentDetailData  = appointmentData.getData();
					if (currencySymbol != null) 
					{
						cs1.setText(currencySymbol);
						cs2.setText(currencySymbol);
						cs3.setText(currencySymbol);
						cs4.setText(currencySymbol);
						cs5.setText(currencySymbol);
						cs6.setText(currencySymbol);
						approx_fare.setText(currencySymbol +" "+appointmentDetailData.getMeterFee());
					}
					distance.setText(appointmentDetailData.getDis()+" "+getResources().getString(R.string.km));
					waiting_time.setText(appointmentDetailData.getWaitTime());
					pick_time.setText(appointmentDetailData.getPickupDt());
					drop_time.setText(appointmentDetailData.getDropDt());
					pickupLocation.setText(appointmentDetailData.getAddr1()+""+appointmentDetailData.getAddr2());
					dropoffLocation.setText(appointmentDetailData.getDropAddr1()+""+appointmentDetailData.getDropAddr2());
					meter_tax_fare.setText(appointmentDetailData.getMeterFee());
					parking_tax_fare.setText(appointmentDetailData.getParkingFee());
					airport_tax_fare.setText(appointmentDetailData.getAirportFee());
					toll_tax_fare.setText(appointmentDetailData.getTollFee());
					totalFare.setText(appointmentDetailData.getAmount());
					//ratingBar.setRating(appointmentDetailData.getRating());
					tip.setText(appointmentDetailData.getTip());
					if (appointmentDetailData.getTipPercent() != null) 
					{
						tip_text.setText("TIP  ("+appointmentDetailData.getTipPercent()+" %)");
					}
					else 
					{
						tip_text.setText("TIP  ("+"0"+" %)");
					}
					
					try 
					{
						SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						java.util.Date stardDate=sd.parse(appointmentDetailData.getPickupDt());
						java.util.Date endDate=sd.parse(appointmentDetailData.getDropoffDt());
						long msDiff = endDate.getTime() - stardDate.getTime();
						if(msDiff>0)
						{
							long totalSeconds = (msDiff)/1000;
							long seconds  = totalSeconds%60;
							long Minute = (totalSeconds/60)%60;
							long Hours = (totalSeconds/(60*60))%(24);
							//long Days= totalSeconds/(60*60*24);
							total_time.setText(""+Hours+" H :"+Minute+" M :"+seconds+" S");
						}
					} 
					catch (Exception e)
					{
						Utility.printLog("Exception = "+e);
					}

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
				else if(appointmentData.getErrNum()==6 || appointmentData.getErrNum()==7 ||
						appointmentData.getErrNum()==94 || appointmentData.getErrNum()==96)
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
					ErrorMessage(getResources().getString(R.string.messagetitle), getResources().getString(R.string.servererror), false);
					//Toast.makeText(BookingHistory.this, ""+error, Toast.LENGTH_SHORT).show();
					Utility.printLog("Animation action Response Didnt Came Error");
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
				AlertDialog.Builder builder = new AlertDialog.Builder(BookingHistory.this);
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
							Intent intent = new Intent(BookingHistory.this,MainActivity.class);
							startActivity(intent);
							finish();
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

			private void ErrorMessageForExpired(String title,String message)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(BookingHistory.this);
				builder.setTitle(title);
				builder.setMessage(message);
				builder.setPositiveButton(getResources().getString(R.string.okbuttontext),
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
	private void ErrorMessageForInvalidOrExpired(String title,String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(BookingHistory.this);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(getResources().getString(R.string.cancelbutton),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Intent intent=new Intent(getActivity(), MainActivity.class);
						// startActivity(intent);
						dialog.dismiss();
					}
				});

		builder.setNegativeButton(getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SessionManager sessionManager = new SessionManager(BookingHistory.this);
						sessionManager.logoutUser();
						dialog.dismiss();
						Intent intent = new Intent(BookingHistory.this, SplahsActivity.class);
						startActivity(intent);
						finish();
					}
				});

		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}


}
