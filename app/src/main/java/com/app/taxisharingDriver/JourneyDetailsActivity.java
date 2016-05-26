package com.app.taxisharingDriver;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.net.ParseException;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
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
import com.app.taxisharingDriver.pojo.AppointmentDetailList;
import com.app.taxisharingDriver.response.AppointmentDetailData;
import com.app.taxisharingDriver.response.UpdateAppointMentstatus;
import com.app.taxisharingDriver.utility.*;
import com.google.gson.Gson;
import com.pubnub.api.Pubnub;

@SuppressLint("SimpleDateFormat")
public class JourneyDetailsActivity extends FragmentActivity
{
	private TextView bid_text,totalFare,pickupLocation,dropoffLocation,distance,pick_time,drop_time,total_time,approx_fare,waiting_time;
	private TextView finish;
	private TextView cs1,cs2,cs3,cs4;
	private RelativeLayout network_bar;
	private TextView network_text;
	//private ImageView detailimageview;
	private EditText toll_tax_fare,parking_tax_fare,airport_tax_fare,meter_tax_fare;
	//private ActionBar actionBar;
	private AppointmentDetailList appointmentDetailList;
	private AppointmentDetailData appointmentDetailData;
	private int selectedindex;
	private int selectedListIndex;
	private ProgressDialog mdialog;
	private Pubnub pubnub;
	private String currencySymbol = VariableConstants.CURRENCY_SYMBOL;
	private RatingBar ratingBar;
	String amount;
	private SessionManager sessionManager;
	//private double totalFareDouble = 0.0;
	private Timer myTimer_publish;
	private TimerTask myTimerTask_publish;
	private BroadcastReceiver receiver;
	private IntentFilter filter;
    private int count= -1;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle arg0) 
	{
		super.onCreate(arg0);
		setContentView(R.layout.journey_detail);
		overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
		pubnub=ApplicationController.getInstacePubnub();
		initLayoutId();
		sessionManager = new SessionManager(this);
		sessionManager.setDropAddress(getCompleteAddressString(sessionManager.getDriverCurrentLat(), sessionManager.getDriverCurrentLongi()));
		sessionManager.setIsFlagForOther(true);
		sessionManager.setDistance(""+0.0);
		Utility utility=new Utility();
		initActionBar();

		MainActivity.isResponse=true;
		Bundle bundle=getIntent().getExtras();
		
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
						if (!Utility.isNetworkAvailable(JourneyDetailsActivity.this))
						{
							network_bar.setVisibility(View.VISIBLE);
							return;
						}
						else if (!NetworkConnection.isConnectedFast(JourneyDetailsActivity.this))
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

		appointmentDetailList = (AppointmentDetailList) bundle.getSerializable(VariableConstants.APPOINTMENT);
		appointmentDetailData = appointmentDetailList.getAppointmentDetailData();
		Utility.printLog("AAAAADistanceeeeeeee   == "+appointmentDetailData.getDis());
		if (currencySymbol != null)
		{
			cs1.setText(currencySymbol);
			cs2.setText(currencySymbol);
			cs3.setText(currencySymbol);
			cs4.setText(currencySymbol);
			if ("2".equals(appointmentDetailData.getPayType()))
			{
				approx_fare.setText(getResources().getString(R.string.cash)+" "+currencySymbol +" "+sessionManager.getAPX_AMOUNT());
			}
			else if ("1".equals(appointmentDetailData.getPayType()))
			{
				approx_fare.setText(getResources().getString(R.string.card)+" "+currencySymbol +" "+sessionManager.getAPX_AMOUNT());
			}
		}

		selectedindex = bundle.getInt("selectedindex");
		selectedListIndex = bundle.getInt("horizontapagerIndex");
		bid_text.setText("BID : "+appointmentDetailData.getBid());

		if (sessionManager.getAPX_AMOUNT().contains(","))
		{
			String value = sessionManager.getAPX_AMOUNT().replace(",",".");
			meter_tax_fare.setText(value);
		}
		else{
			meter_tax_fare.setText(sessionManager.getAPX_AMOUNT());
		}

		waiting_time.setText(sessionManager.getWaitingTime());


		try
		{
			String datestr = sessionManager.getBeginTime();
			String date_string = datestr.toString();
			String[] temp1 = date_string.split(" ");
			String[] temp2=temp1[1].split(":");
			String final_date = null;
			if(Integer.parseInt(temp2[0])>12)
			{
				int temp_hh=Integer.parseInt(temp2[0])-12;
				final_date=""+temp_hh+":"+temp2[1]+" PM";
			}
			else
			{
				final_date=temp2[0]+":"+temp2[1]+" AM";
			}
			pick_time.setText(""+final_date);

			String droptime = utility.getCurrentGmtTime();
			date_string = droptime.toString();
			temp1 = date_string.split(" ");
			temp2 = temp1[1].split(":");
			Utility.printLog("", "final_date: "+final_date);

			if(Integer.parseInt(temp2[0])>12)
			{
				int temp_hh=Integer.parseInt(temp2[0])-12;
				final_date=""+temp_hh+":"+temp2[1]+" PM";
			}
			else
			{
				final_date=temp2[0]+":"+temp2[1]+" AM";
			}
			drop_time.setText(""+final_date);
			String dateString = appointmentDetailData.getApptDt();
			String[] parts = dateString.split(" ");
			String part1 = parts[0];
			/*try
			{
				String inputTimeStamp = part1;
				final String inputFormat = "yyyy-MM-dd";
				final String outputFormat = "dd-MMM-yyyy";
		//		appntDate.setText(TimeStampConverter(inputFormat, inputTimeStamp,outputFormat));
			} 
			catch (ParseException e)
			{
				e.printStackTrace();
			}*/

			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			java.util.Date stardDate=sd.parse(datestr);
			java.util.Date endDate=sd.parse(droptime);
			long msDiff = endDate.getTime() - stardDate.getTime();
			if(msDiff>0)
			{
				long totalSeconds = (msDiff)/1000;
				//long seconds  = totalSeconds%60;
				long Minute = (totalSeconds/60)%60;
				long Hours = (totalSeconds/(60*60))%(24);
				//long Days= totalSeconds/(60*60*24);
				total_time.setText(""+Hours+" H :"+Minute+" M");
			}
		}
		catch (Exception e)
		{
			Utility.printLog("Exception  "+e);
			e.printStackTrace();
		}


		final InputFilter[] filters = new InputFilter[1];
		filters[0] = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				if (end > start) {
					String destTxt = dest.toString();
					String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
					if (!resultingTxt.matches("^$?\\-?([1-9]{1}[0-9]{0,2}(\\,\\d{3})*(\\.\\d{0,2})?|[1-9]{1}\\d{0,}(\\.\\d{0,2})?|0(\\.\\d{0,2})?|(\\.\\d{1,2}))$|^\\-?$?([1-9]{1}\\d{0,2}(\\,\\d{3})*(\\.\\d{0,2})?|[1-9]{1}\\d{0,}(\\.\\d{0,2})?|0(\\.\\d{0,2})?|(\\.\\d{1,2}))$|^\\($?([1-9]{1}\\d{0,2}(\\,\\d{3})*(\\.\\d{0,2})?|[1-9]{1}\\d{0,}(\\.\\d{0,2})?|0(\\.\\d{0,2})?|(\\.\\d{1,2}))\\)$")) {
						return "";
					}
				}
				return null;
			}
		};


		meter_tax_fare.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) 
			{

			}

			@Override
			public void afterTextChanged(Editable s)
			{


				if (s.length() > 0)
				{
					String str = meter_tax_fare.getText().toString();

					int indexOFdec =  str.indexOf(".");

					if(indexOFdec >=0) {
						if(str.substring(indexOFdec).length() >2)
						{
							meter_tax_fare.setFilters(filters);
						}
					}
				}
			}
		});

		toll_tax_fare.addTextChangedListener(new TextWatcher() 
		{

			int count = -1;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{


			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) 
			{

			}

			@Override
			public void afterTextChanged(Editable s) 
			{
				if (s.length() > 0)
				{
					String str = toll_tax_fare.getText().toString();

					int indexOFdec =  str.indexOf(".");

					if(indexOFdec >=0) {
						if(str.substring(indexOFdec).length() >2)
						{
							toll_tax_fare.setFilters(filters);
						}
					}
				}
			}
		});
		parking_tax_fare.addTextChangedListener(new TextWatcher() 
		{
			int count = -1;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) 
			{

			}

			@Override
			public void afterTextChanged(Editable s)
			{
				if (s.length() > 0)
				{
					String str = parking_tax_fare.getText().toString();

					int indexOFdec =  str.indexOf(".");

					if(indexOFdec >=0) {
						if(str.substring(indexOFdec).length() >2)
						{
							parking_tax_fare.setFilters(filters);
						}
					}
				}
			}
		});
		airport_tax_fare.addTextChangedListener(new TextWatcher() 
		{

			int count = -1;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) 
			{

			}

			@Override
			public void afterTextChanged(Editable s)
			{
				if (s.length() > 0)
				{
					String str = airport_tax_fare.getText().toString();

					int indexOFdec =  str.indexOf(".");

					if(indexOFdec >=0) {
						if(str.substring(indexOFdec).length() >2)
						{
							airport_tax_fare.setFilters(filters);
						}
					}
				}

			}
		});

		pickupLocation.setText(appointmentDetailData.getAddr1());
		dropoffLocation.setText(sessionManager.getDropAddress());
		if(!"".equals(sessionManager.getDistance_tag()) )
		{
			double dis = Double.parseDouble(sessionManager.getDistance_tag()) * 0.00062137;
			String disKM = String.format("%.2f",dis) ;
			distance.setText(disKM+" "+getResources().getString(R.string.km));
		}

		startTimerToGetFares();

	}

	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();
		finish();
	}
	
	private void initLayoutId()
	{
		//passengerName = (TextView)findViewById(R.id.passenger_name_text);
	//	appntDate = (TextView)findViewById(R.id.date_text);
		cs1 = (TextView)findViewById(R.id.cs1);
		cs2 = (TextView)findViewById(R.id.cs2);
		cs3 = (TextView)findViewById(R.id.cs3);
		cs4 = (TextView)findViewById(R.id.cs4);
		bid_text = (TextView)findViewById(R.id.bid_text);
		totalFare = (TextView)findViewById(R.id.total_tax_fare);
		toll_tax_fare = (EditText)findViewById(R.id.toll_tax_fare);
		parking_tax_fare = (EditText)findViewById(R.id.parking_tax_fare);
		airport_tax_fare = (EditText)findViewById(R.id.airport_tax_fare);
		meter_tax_fare = (EditText)findViewById(R.id.meter_tax_fare);
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
		ratingBar = (RatingBar)findViewById(R.id.invoice_driver_rating);
		//detailimageview = (ImageView)findViewById(R.id.detailimageview);
		/*finish = (Button)findViewById(R.id.finish_button);
		finish.setOnClickListener(this);*/
	}

	/*@Override
	public void onClick(View v) 
	{
		if (v.getId() == R.id.finish_button)
		{
			ConnectionDetector connectionDetector=new ConnectionDetector(JourneyDetailsActivity.this);
			if (connectionDetector.isConnectingToInternet()) 
			{
				amount = meter_tax_fare.getText().toString();
				if (!(amount.endsWith(".")) && !(amount.equals(currencySymbol+"0")) && !(amount.equals(currencySymbol)) && !(amount.startsWith(currencySymbol+".")) && !(amount.equals("0")) && !(amount.isEmpty()) ) 
				{
					sessionManager.setBeginJourney(false);
					sendNotificationToPassenger(9);
				}
				else
				{
					showAlert(getResources().getString(R.string.entervalidamount));
				}
			}
		}
	}*/
	
	
	@SuppressLint({ "NewApi", "InflateParams" })
	private void initActionBar()
	{
		getActionBar().hide();

		finish = (TextView)findViewById(R.id.cancel_text);
		finish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				ConnectionDetector connectionDetector = new ConnectionDetector(JourneyDetailsActivity.this);
				if (connectionDetector.isConnectingToInternet()) {
					amount = meter_tax_fare.getText().toString();
					if (!(amount.endsWith(".")) && !(amount.equals(currencySymbol + "0")) && !(amount.equals(currencySymbol)) && !(amount.startsWith(currencySymbol + ".")) && !(amount.equals("0")) && !(amount.isEmpty())) {
						sessionManager.setBeginJourney(false);
						sendNotificationToPassenger(9);
					} else {
						showAlert(getResources().getString(R.string.entervalidamount));
					}
				}

			}
		});

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
		finish = (Button)mActionBarCustom.findViewById(R.id.cancel);
		finish.setText("DONE");
		finish.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{

				ConnectionDetector connectionDetector=new ConnectionDetector(JourneyDetailsActivity.this);
				if (connectionDetector.isConnectingToInternet()) 
				{
					amount = meter_tax_fare.getText().toString();
					if (!(amount.endsWith(".")) && !(amount.equals(currencySymbol+"0")) && !(amount.equals(currencySymbol)) && !(amount.startsWith(currencySymbol+".")) && !(amount.equals("0")) && !(amount.isEmpty()) ) 
					{
						sessionManager.setBeginJourney(false);
						sendNotificationToPassenger(9);
					}
					else
					{
						showAlert(getResources().getString(R.string.entervalidamount));
					}
				}
			
			}
		});
		actionbar_textview.setText(getResources().getString(R.string.finish));
		actionbar_textview.setGravity(Gravity.CENTER);
		double scaler[]=Scaler.getScalingFactor(this);
		int padding = (int)Math.round(140*scaler[0]);
		actionbar_textview.setPadding(padding, 0,0,0);
		actionBar.setCustomView(mActionBarCustom);*/
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


	/**
	 * Method for updating appointment status  
	 * @param responsecode
	 */
	private void sendNotificationToPassenger(final int responsecode)
	{
		Utility utility=new Utility();

		ConnectionDetector connectionDetector=new ConnectionDetector(JourneyDetailsActivity.this);
		if (connectionDetector.isConnectingToInternet()) 
		{
			String deviceid = Utility.getDeviceId(JourneyDetailsActivity.this);
			String currenttime = utility.getCurrentGmtTime();
			SessionManager sessionManager=new SessionManager(JourneyDetailsActivity.this);
			//logDebug("getAppointmentDetail dataandTime "+dateandTime);
			String passengerEmailid = appointmentDetailData.getEmail();
			float ratingBarValue = ratingBar.getRating();
			String appointdatetime = appointmentDetailData.getApptDt();
			String sessiontoken = sessionManager.getSessionToken();
			String notes="";
			String parking_fare ="00.00";
			String toll_fare = "00.00";
			String airport_fare = "00.00";
			//String dropAddress = sessionManager.getDropAddress();
			String amount = meter_tax_fare.getText().toString();
			//amount = amount.replace(currencySymbol, "");
			double amount_double = Double.parseDouble(amount);

			if (!"".equals(parking_tax_fare.getText().toString())) 
			{
				parking_fare = parking_tax_fare.getText().toString();
			}

			//parking_fare = parking_fare.replace(currencySymbol,"");
			
			//double parking_fare_double = Double.parseDouble(parking_fare);

			if (!"".equals(toll_tax_fare.getText().toString())) 
			{
				toll_fare = toll_tax_fare.getText().toString();
			}
			
			//toll_fare = toll_fare.replace(currencySymbol,"");

			//double toll_fare_double = Double.parseDouble(toll_fare);


			if (!"".equals(airport_tax_fare.getText().toString())) 
			{
				airport_fare = airport_tax_fare.getText().toString();
			}

			//airport_fare = airport_fare.replace(currencySymbol,"");

			//double airport_fare_double = Double.parseDouble(airport_fare);
			
			final String mparams[]={sessiontoken,deviceid,passengerEmailid,appointdatetime,""+responsecode,""+amount_double ,notes,currenttime,/*dropAddress,*/""+ratingBarValue/*currentdate[0]*/,parking_fare,toll_fare,airport_fare};
			mdialog = Utility.GetProcessDialog(JourneyDetailsActivity.this);
			mdialog.setMessage(getResources().getString(R.string.Pleasewaitmessage));
			mdialog.show();
			mdialog.setCancelable(false);
			RequestQueue queue = Volley.newRequestQueue(this);  // this = context
			String  url = VariableConstants.getAppointmentstatusUpdate_url;
			StringRequest postRequest = new StringRequest(Request.Method.POST, url,
					new Response.Listener<String>()
					{
				@Override
				public void onResponse(String response)
				{
					try 
					{
						Utility.printLog("sendNotificationResponse9"+response);
						UpdateAppointMentstatus appointMentstatus;
						Gson gson = new Gson();
						appointMentstatus = gson.fromJson(response, UpdateAppointMentstatus.class);
						Utility.printLog("Journey Detail sendNotification9"+response);

						if (mdialog!=null)
						{
							mdialog.dismiss();
							mdialog.cancel();
						}
						// 1 -> (1) Mandatory field missing
						if (appointMentstatus.getErrFlag()==0 && appointMentstatus.getErrNum() == 59)
						{
							appointmentDetailList.setStatCode(9);					
							SessionManager sessionManager=new SessionManager(JourneyDetailsActivity.this);
							sessionManager.setIsAppointmentAccept(false);
							sessionManager.setIsPressedImonthewayorihvreached(false);
							sessionManager.setIsPassengerDropped(true);
							sessionManager.setindexofSelectedAppointment(selectedindex);
							sessionManager.setindexofSelectedList(selectedListIndex);
							sessionManager.setAppiontmentStatus(responsecode);
							appointmentDetailList.setCompletedPressed(true);
							appointmentDetailList.setIhaveReachedPressed(true);
							sessionManager.setFlagForStatusDropped(false);
							
							Utility.printLog("Latitude  = "+sessionManager.getDriverCurrentLat(),"Longitude = "+sessionManager.getDriverCurrentLongi());
							for (int i = 0; i < 5; i++) 
							{
								publishLocation(sessionManager.getDriverCurrentLat(),sessionManager.getDriverCurrentLongi());
							}
							sessionManager.setIsInBooking(false);
							android.widget.Toast.makeText(JourneyDetailsActivity.this, appointMentstatus.getErrMsg(), android.widget.Toast.LENGTH_SHORT).show();
							Intent intent=new Intent(JourneyDetailsActivity.this, MainActivity.class);
							sessionManager.setIsOnButtonClicked(true);
							Bundle bundle=new Bundle();
							bundle.putSerializable(VariableConstants.APPOINTMENT, (Serializable) appointmentDetailList);
							intent.putExtras(bundle);
							startActivity(intent);
							finish();
						}
						else if (appointMentstatus.getErrFlag()==1&&appointMentstatus.getErrNum()==56)
						{
							// 56 -> (1) Invalid status, cannot update.
							ErrorMessage(getResources().getString(R.string.messagetitle),appointMentstatus.getErrMsg(),false);
						}
						
						else if (appointMentstatus.getErrFlag()==1&&appointMentstatus.getErrNum()==95)
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
						else if (appointMentstatus.getErrFlag()==1&&appointMentstatus.getErrNum()==96)
						{
							// 1 -> (1) Mandatory field missing
							ErrorMessage(getResources().getString(R.string.messagetitle), appointMentstatus.getErrMsg(), false);
						}
						else if (appointMentstatus.getErrFlag()==1)
						{
							// 1 -> (1) Mandatory field missing
							ErrorMessage(getResources().getString(R.string.messagetitle),appointMentstatus.getErrMsg(),false);
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

					params.put("ent_pas_email", mparams[2]); 
					params.put("ent_appnt_dt",mparams[3]);

					params.put("ent_response", mparams[4]); 
					params.put("ent_meter",mparams[5] );
					params.put("ent_doc_remarks", mparams[6]);
					params.put("ent_date_time", mparams[7]);
					//params.put("ent_drop_addr_line1",mparams[8]);
					params.put("ent_rating",mparams[8]);

					params.put("ent_parking",mparams[9] );
					params.put("ent_toll", mparams[10]); 
					params.put("ent_airport", mparams[11]);

					Utility.printLog("get Notification request9 = "+params);

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
			utility.showDialogConfirm(JourneyDetailsActivity.this,"Alert"," working internet connection required", false).show();
		}
	}

	private void ErrorMessage(String title,String message,final boolean flageforSwithchActivity)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(JourneyDetailsActivity.this);
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
					Intent intent = new Intent(JourneyDetailsActivity.this,MainActivity.class);
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
		AlertDialog.Builder builder = new AlertDialog.Builder(JourneyDetailsActivity.this);
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

		builder.setNegativeButton(getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				SessionManager sessionManager=new SessionManager(JourneyDetailsActivity.this);
				sessionManager.logoutUser();
				dialog.dismiss();
				Intent intent=new Intent(JourneyDetailsActivity.this, SplahsActivity.class);
				startActivity(intent);
				finish();
			}
		});

		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

	/**
	 * Method for publish current location to passenger. 
	 * @param latitude
	 * @param longitude
	 */
	public void publishLocation(double latitude,double longitude)
	{
		String message;
		SessionManager sessionManager = new SessionManager(this);
		String subscribChannel=sessionManager.getSubscribeChannel();
		message="{\"a\" :\""+9+"\", \"e_id\" :\""+sessionManager.getUserEmailid()+"\", \"lt\" :"+latitude+"\", \"lg\" :"+longitude+"\", \"ph\" :\""+sessionManager.getMobile()+"\",\"dt\" :\""+sessionManager.getDate()+"\",\"bid\" :\""+sessionManager.getBOOKING_ID()+"\",\"chn\" :\""+subscribChannel+"\"}";
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

	private void showAlert(String message)
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setTitle(getResources().getString(R.string.note));

		alertDialogBuilder
		.setMessage(message)
		.setCancelable(false)
		.setNegativeButton(getResources().getString(R.string.okbuttontext),new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog,int id)
			{
				dialog.dismiss();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
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

	/**
	 * Format a time from a given format to given target format
	 * 
	 * @param inputFormat
	 * @param inputTimeStamp
	 * @param outputFormat
	 * @return
	 * @throws ParseException
	 * @throws java.text.ParseException 
	 */
	private static String TimeStampConverter(final String inputFormat,
			String inputTimeStamp, final String outputFormat)
					throws ParseException, java.text.ParseException {
		return new SimpleDateFormat(outputFormat).format(new SimpleDateFormat(
				inputFormat).parse(inputTimeStamp));
	}

	private void startTimerToGetFares()
	{
		Utility.printLog("CONTROL INSIDE startTimerToGetDistanceAndEta");

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
				runOnUiThread(new Runnable() 
				{
					public void run()
					{

						try {
						double fare = 0.0;
						if(!meter_tax_fare.getText().toString().trim().equals("") && !meter_tax_fare.getText().toString().trim().endsWith(".") && !meter_tax_fare.getText().toString().trim().startsWith("."))
						{
							String value = meter_tax_fare.getText().toString().trim();
							if (value.contains(","))
							{
								value = value.replace(",",".");
							}
							fare = fare + Double.parseDouble(value);
						}
						if(!toll_tax_fare.getText().toString().trim().equals("") && !toll_tax_fare.getText().toString().trim().endsWith(".") && !toll_tax_fare.getText().toString().trim().startsWith("."))
						{
							String value = toll_tax_fare.getText().toString().trim();
							if (value.contains(","))
							{
								value = value.replace(",",".");
							}
							fare = fare + Double.parseDouble(value);
						}
						if(!parking_tax_fare.getText().toString().trim().equals("") && !parking_tax_fare.getText().toString().trim().endsWith(".") && !parking_tax_fare.getText().toString().trim().startsWith("."))
						{
							String value = parking_tax_fare.getText().toString().trim();
							if (value.contains(","))
							{
								value = value.replace(",",".");
							}
							fare = fare + Double.parseDouble(value);
						}
						if(!airport_tax_fare.getText().toString().trim().equals("") && !airport_tax_fare.getText().toString().trim().endsWith(".") && !airport_tax_fare.getText().toString().trim().startsWith("."))
						{
							String value = airport_tax_fare.getText().toString().trim();
							if (value.contains(","))
							{
								value = value.replace(",",".");
							}
							fare = fare + Double.parseDouble(value);
						}

							String amount = String.format("%.2f",fare) ;
							totalFare.setText(""+amount);
						} 
						catch (Exception e) 
						{
							Utility.printLog("startTimerToGetFaresException = "+e);
							e.printStackTrace();
						}
					}
				});
			}
		};
		myTimer_publish.schedule(myTimerTask_publish, 000, 2000);
	}
	/*private Location findLocation(Context cntx,String provider)
	{
		locationManager = (LocationManager) cntx.getSystemService(Service.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(provider)) 
		{
			locationManager.requestLocationUpdates(provider,
					10, 1000, JourneyDetailsActivity.this);
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

		gpsLocation = findLocation(cntx,LocationManager.GPS_PROVIDER);

		Location nwLocation = findLocation(cntx,LocationManager.NETWORK_PROVIDER);

		if (gpsLocation != null) 
		{
			if (nwLocation != null) 
			{
				location[0] = nwLocation.getLatitude();
				location[1] = nwLocation.getLongitude();
				Utility.printLog("data gps"+location[0]+"<---->"+location[1]);
			}

			location[0] = gpsLocation.getLatitude();
			location[1] = gpsLocation.getLongitude();
			Utility.printLog("data network"+location[0]+"<---->"+location[1]);
		} 

		else 
		{
			showSettingsAlert();
		}
		return location;
	}*/
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
}