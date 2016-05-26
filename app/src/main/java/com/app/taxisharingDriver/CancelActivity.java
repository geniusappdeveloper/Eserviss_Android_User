package com.app.taxisharingDriver;

import java.io.Serializable;
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
import com.app.taxisharingDriver.pojo.AppointmentDetailList;
import com.app.taxisharingDriver.response.AppointmentDetailData;
import com.app.taxisharingDriver.response.CancelResponse;
import com.app.taxisharingDriver.utility.ConnectionDetector;
import com.app.taxisharingDriver.utility.PublishUtility;
import com.app.taxisharingDriver.utility.SessionManager;
import com.app.taxisharingDriver.utility.Utility;
import com.app.taxisharingDriver.utility.VariableConstants;
import com.google.gson.Gson;
import com.pubnub.api.Pubnub;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CancelActivity extends Activity implements OnClickListener
{
	private Button doNotCharge,clientNotShow,wrongAddress,clientRequestCancel,otherButton,doNotCancelTrip;
	private ProgressDialog mdialog;
	private ActionBar actionBar;
	private AppointmentDetailList appointmentDetailList;
	private AppointmentDetailData appointmentDetailData;
	private Pubnub pubnub;
	private String reason;
	private SessionManager sessionManager;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cancel_trip);
		pubnub=ApplicationController.getInstacePubnub();
		initLayout();
		sessionManager = new SessionManager(this);
		sessionManager.setIsFlagForOther(true);
		actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		Bundle bundle=getIntent().getExtras();
		actionBar.setIcon(android.R.color.transparent);
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_screen_navigation_bar));
		Typeface robotoBoldCondensedItalic = Typeface.createFromAsset(getAssets(), "fonts/Zurich Condensed.ttf");
		try 
		{
			int actionBarTitle = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
			TextView actionBarTitleView = (TextView) getWindow().findViewById(actionBarTitle);

			if(actionBarTitleView != null)
			{
				actionBarTitleView.setTypeface(robotoBoldCondensedItalic);
				actionBarTitleView.setTextColor(android.graphics.Color.rgb(51, 51, 51));
			}
			actionBar.setTitle(getResources().getString(R.string.canceltrip));
		} 
		catch (NullPointerException e)
		{
		}
		appointmentDetailList = (AppointmentDetailList) bundle.getSerializable(VariableConstants.APPOINTMENT);
		appointmentDetailData = appointmentDetailList.getAppointmentDetailData();
		
	}
	
	private void initLayout()
	{
		doNotCharge = (Button)findViewById(R.id.donotcharge);
		clientNotShow = (Button)findViewById(R.id.clientnotshow);
		wrongAddress = (Button)findViewById(R.id.wrongaddressshown);
		clientRequestCancel = (Button)findViewById(R.id.clientrequested);
		otherButton = (Button)findViewById(R.id.other);
		doNotCancelTrip = (Button)findViewById(R.id.donotcanceltrip);
		
		doNotCharge.setOnClickListener(this);
		clientNotShow.setOnClickListener(this);
		wrongAddress.setOnClickListener(this);
		clientRequestCancel.setOnClickListener(this);
		otherButton.setOnClickListener(this);
		doNotCancelTrip.setOnClickListener(this);

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
								publishLocation(sessionManager.getDriverCurrentLat(),sessionManager.getDriverCurrentLongi());
							}
							ErrorMessage(getResources().getString(R.string.messagetitle),cancelResponse.getErrMsg(),true);
						}
						else if (cancelResponse.getErrFlag() == 1 && cancelResponse.getErrNum() == 75)
						{
							ErrorMessage(getResources().getString(R.string.messagetitle),cancelResponse.getErrMsg(),false);
						}
						else if (cancelResponse.getErrFlag() == 1 && cancelResponse.getErrNum() == 32)
						{
							ErrorMessage(getResources().getString(R.string.messagetitle),cancelResponse.getErrMsg(),false);
						}
						else if (cancelResponse.getErrFlag() == 1 && cancelResponse.getErrNum() == 44)
						{
							ErrorMessage(getResources().getString(R.string.messagetitle),cancelResponse.getErrMsg(),false);
						}
						else if (cancelResponse.getErrFlag() == 1 && cancelResponse.getErrNum() == 3)
						{
							ErrorMessage(getResources().getString(R.string.messagetitle),cancelResponse.getErrMsg(),false);
						}
						else if (cancelResponse.getErrFlag() == 1 && cancelResponse.getErrNum() == 1)
						{
							ErrorMessage(getResources().getString(R.string.messagetitle),cancelResponse.getErrMsg(),false);
						}
						else if(cancelResponse.getErrNum()==6|| cancelResponse.getErrNum()==7 ||
								cancelResponse.getErrNum()==94 || cancelResponse.getErrNum()==96)
						{
							Toast.makeText(getApplicationContext(), cancelResponse.getErrMsg(),Toast.LENGTH_SHORT).show();
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
							Toast.makeText(CancelActivity.this, ""+error, Toast.LENGTH_SHORT).show();
						}
					};
					
					private void ErrorMessage(String title,String message,final boolean flageforSwithchActivity)
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(CancelActivity.this);
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
									SessionManager manager = new SessionManager(CancelActivity.this);
									Intent intent = new Intent(CancelActivity.this,MainActivity.class);
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
					@Override
					public void onClick(View v)
					{
						if (v.getId() == R.id.donotcanceltrip)
						{
							Intent intent=new Intent(CancelActivity.this, IHaveArrivedActivity.class);
							Bundle bundle=new Bundle();
							bundle.putSerializable(VariableConstants.APPOINTMENT, (Serializable) appointmentDetailList);
							intent.putExtras(bundle);
							startActivity(intent);
							finish();
						}
						else if (v.getId() == R.id.clientnotshow) 
						{
							reason = "4";
							//sessionManager.setCancelReason(reason);
							getAbortJourney(4);
						}
						else if (v.getId() == R.id.wrongaddressshown) 
						{
							reason = "5";
							//sessionManager.setCancelReason(reason);
							getAbortJourney(5);
						}
						else if (v.getId() == R.id.clientrequested) 
						{
							reason = "6";
							//sessionManager.setCancelReason(reason);
							getAbortJourney(6);
						}
						else if (v.getId() == R.id.donotcharge) 
						{
							reason = "7";
							//sessionManager.setCancelReason(reason);
							getAbortJourney(7);
						}
						else if (v.getId() == R.id.other) 
						{
							reason = "8";
							//sessionManager.setCancelReason(reason);
							getAbortJourney(8);
						}
						
					}
					
					/**
					 * Method for publish current location to passenger. 
					 * @param latitude
					 * @param longitude
					 */
					public void publishLocation(double latitude,double longitude)
					{
						String message;
						String subscribChannel=sessionManager.getSubscribeChannel();
						message="{\"a\" :\""+5+"\", \"e_id\" :\""+sessionManager.getUserEmailid()+"\", \"lt\" :"+latitude+", \"lg\" :"+longitude+", \"r\" :\""+reason+",\"bid\" :\""+sessionManager.getBOOKING_ID()+"\",\"chn\" :\""+subscribChannel+"\"}";
						Utility.printLog("Publish Location = "+message);
						
						if (sessionManager.getPasChannel() != null)
						{
							Utility.printLog("Publish Passenger Channel"+appointmentDetailData.getPasChn());
							//Pubnub Change 17/5/2016
							PublishUtility.publish(sessionManager.getPasChannel(), message, pubnub);
						}
						else
						{
							ErrorMessage(getResources().getString(R.string.messagetitle),getResources().getString(R.string.passengercancelled),true);
						}
					}
					
					
}
