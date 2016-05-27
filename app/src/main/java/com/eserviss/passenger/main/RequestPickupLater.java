package com.eserviss.passenger.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.eserviss.passenger.pojo.LiveBookingResponse;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.UltilitiesDate;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.egnyt.eserviss.R;
public class RequestPickupLater extends Activity
{
	private String from_latitude,from_longitude,to_latitude,to_longitude,zip_code,currenttime,mPromoCode;
	private String pickup_address,dropoff_address,getBookingResponse,car_Id,payment_type,laterBookingDate;
	private LiveBookingResponse liveBookingResponse;
	private RelativeLayout rl_blue_later,rl_red_later;
	private TextView hold_cancel_later;
	private ImageButton cancel_later;
	//private String addressLine1="",addressLine2="";
	private ArrayList<String> nearestDrivers = new ArrayList<String>();
	private int driverIndex=0;
	private NewBackGroundLiveBooking requestLiveBooking;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		
		//Hiding action bar
		ActionBar actionBar = getActionBar(); 
		actionBar.hide();
		
		//Inflating the layout xml
		setContentView(R.layout.request_pickup_later);	
		initialize();

		//Getting information from previous activity
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if(extras!=null)
		{
			from_latitude = extras.getString("FromLatitude");
			from_longitude = extras.getString("FromLongitude");
			to_latitude = extras.getString("ToLatitude");
			to_longitude = extras.getString("ToLongitude");
			zip_code= extras.getString("Zipcode");
			pickup_address = extras.getString("PICKUP_ADDRESS");
			dropoff_address = extras.getString("DROPOFF_ADDRESS");
			nearestDrivers = (ArrayList<String>) extras.getSerializable("my_drivers");
			car_Id= extras.getString("Car_Type");
			payment_type = extras.getString("PAYMENT_TYPE");
			laterBookingDate = extras.getString("Later_Booking_Date");
			mPromoCode = extras.getString("coupon");
			
			
		}
		
		Utility.printLog("RequestPickup Livebooking nearestDrivers size="+nearestDrivers.size());
		
		if(nearestDrivers.size()>0)
		{
			for(int i=0;i<nearestDrivers.size();i++)
			{
				Utility.printLog("RequestPickup nearestDrivers at "+i+" "+nearestDrivers.get(i));
			}
			
			String[] params = {nearestDrivers.get(driverIndex)};
			
			Utility.printLog("RequestPickup Livebooking nearestDriver email="+params[0]);
			requestLiveBooking = new NewBackGroundLiveBooking();
			requestLiveBooking.execute(params);
		}
		else
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RequestPickupLater.this);
			// set title
			alertDialogBuilder.setTitle(getResources().getString(R.string.alert));
			// set dialog message
			alertDialogBuilder
			.setMessage(getResources().getString(R.string.no_drivers_found))
			.setCancelable(false)

			.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog,int id) 
				{
					// if this button is clicked, just close the dialog box and do nothing
					dialog.dismiss();
					Intent returnIntent = new Intent();
					setResult(RESULT_OK,returnIntent);     
					finish();
					overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
				}
			});
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			// show it
			alertDialog.show();
		
		}
		cancel_later.setOnLongClickListener(new OnLongClickListener() 
		{
			@Override
			public boolean onLongClick(View v)
			{
				// TODO Auto-generated method stub
				Utility.printLog("RequestPickupLater Livebooking Response inside setOnLongClickListener");
				hold_cancel_later.setText(getResources().getString(R.string.cancelling));
				rl_red_later.setVisibility(View.VISIBLE);
				rl_blue_later.setVisibility(View.INVISIBLE);
				
				CancelRequestPickup();
				
				return false;
			}
		});
	}

	
	/**
	 * Initializing all variables in my view
	 * 
	 */
	private void initialize()
	{
		rl_blue_later = (RelativeLayout)findViewById(R.id.relative_blue_later);
		rl_red_later = (RelativeLayout)findViewById(R.id.relative_red_later);
		cancel_later = (ImageButton)findViewById(R.id.btn_cancel_later);
		hold_cancel_later = (TextView)findViewById(R.id.request_later);
		//==============================My change April========================================
		Typeface roboto_condensed = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/BebasNeue.otf");
		hold_cancel_later.setTypeface(roboto_condensed);


		//=========================================================================


	}
	
	/**
	 * Sending the request for cancel the live booking request using VOLLEY
	 */
	private void CancelRequestPickup()
	{
		RequestQueue volleyRequest = Volley.newRequestQueue(RequestPickupLater.this);

		StringRequest myReq = new StringRequest(Request.Method.POST,VariableConstants.BASE_URL+"cancelAppointmentRequest",
				new Response.Listener<String>() {

			@Override
			public void onResponse(String response)
			{
				boolean status = requestLiveBooking.cancel(true);
				Utility.printLog("RequestPickupLater Livebooking  CancelRequestPickupLater status="+status);
				getBookingResponse = response;
				Utility.printLog("RequestPickupLater Livebooking Success of getting CancelRequestPickupLater "+response);
				getCancelInfo();
			}
		}, 	new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				Toast.makeText(RequestPickupLater.this, "System error. Please retry ", Toast.LENGTH_LONG).show();
				/*boolean status = requestLiveBooking.cancel(true);
				Utility.printLog("RequestPickup Livebooking  CancelRequestPickup onErrorResponse status="+status);
				Utility.printLog("Error for volley");
				Intent returnIntent = new Intent();
				setResult(RESULT_OK,returnIntent);     
				finish();
				overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);*/
			}
		}){  
			protected HashMap<String,String> getParams() throws com.android.volley.AuthFailureError
			{  
				HashMap<String,String> kvPair = new HashMap<String, String>(); 

				SessionManager session=new SessionManager(RequestPickupLater.this);

				kvPair.put("ent_sess_token",session.getSessionToken() );
				kvPair.put("ent_dev_id",session.getDeviceId());
				return kvPair;  
			};  
		};
		volleyRequest.add(myReq);
	}
	
	/**
	 * Handling cancel response 
	 */
	private void getCancelInfo()
	{
		try
		{
			JSONObject jsnResponse = new JSONObject(getBookingResponse);

			String jsonErrorParsing = jsnResponse.getString("errFlag");

			Utility.printLog("jsonErrorParsing is ---> "+jsonErrorParsing);
			
			Gson gson = new Gson();
			liveBookingResponse = gson.fromJson(getBookingResponse, LiveBookingResponse.class);

			if(liveBookingResponse!=null)
			{
				if(liveBookingResponse.getErrFlag().equals("0"))
				{
					SessionManager session = new SessionManager(RequestPickupLater.this);
					session.setBookingCancelled(true);
					session.storeBookingId("0");
					session.storeAptDate(null);
					Toast.makeText(getApplicationContext(), liveBookingResponse.getErrMsg(), Toast.LENGTH_SHORT).show();
					Intent returnIntent = new Intent();
					
					returnIntent.putExtra("LaterMsg", liveBookingResponse.getErrMsg());
					
					setResult(RESULT_OK,returnIntent);     
					finish();
					overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
				}
				else
				{
					//Toast.makeText(getApplicationContext(), liveBookingResponse.getErrMsg(), Toast.LENGTH_SHORT).show();
					
					SessionManager session = new SessionManager(RequestPickupLater.this);
					session.setBookingCancelled(true);
					session.storeBookingId("0");
					session.storeAptDate(null);
					Toast.makeText(getApplicationContext(), liveBookingResponse.getErrMsg(), Toast.LENGTH_SHORT).show();
					Intent returnIntent = new Intent();
					setResult(RESULT_OK,returnIntent);  
					returnIntent.putExtra("LaterMsg", liveBookingResponse.getErrMsg());
					finish();
					overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
				
					/*Intent returnIntent = new Intent();
					setResult(RESULT_OK,returnIntent);     
					finish();
					overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);*/
				}
			}
		}
		catch(JSONException e)
		{
			Utility.printLog("exp "+e);
			e.printStackTrace();
			Utility.ShowAlert(getResources().getString(R.string.server_error), RequestPickupLater.this);
		}
	}
	
	/**
	 * calling an API for booking driver
	 * @INPUT
	 * Session token, device id, car work type, pickup address, pickup latitude, pickup longitude
	 * payment type, zipcode, drop location, current date time
	 */
	class NewBackGroundLiveBooking extends AsyncTask<String,Void,String>
	{
		LiveBookingResponse response;
		ProgressDialog dialogL;
		String dateandTime;
		int i=1;

		@Override
		protected String doInBackground(String... params)
		{
			String url=VariableConstants.BASE_URL+"liveBooking";

			HashMap<String,String> kvPair = new HashMap<String, String>(); 

			SessionManager session = new SessionManager(RequestPickupLater.this);

			Utility utility=new Utility();
			currenttime=utility.getCurrentGmtTime();
			dateandTime=UltilitiesDate.getLocalTime(currenttime);
			Utility.printLog("","dataandTime "+currenttime);

			Utility.printLog("RequestPickupLater Livebooking from_latitude="+from_latitude+" from_longitude="+from_longitude+" to_latitude="+to_latitude+" to_longitude="+to_longitude);
			Utility.printLog("RequestPickupLater Livebooking curenttime in doInBackground="+currenttime);
			Utility.printLog("RequestPickupLater Livebooking curenttime in ent_sess_token="+session.getSessionToken());
			Utility.printLog("RequestPickupLater Livebooking curenttime in ent_sess_token="+session.getDeviceId());
			Utility.printLog("RequestPickupLater Livebooking curenttime in ent_sess_token="+mPromoCode);
			

			kvPair.put("ent_sess_token",session.getSessionToken());
			kvPair.put("ent_dev_id",session.getDeviceId());
			kvPair.put("ent_wrk_type",car_Id);
			kvPair.put("ent_addr_line1",pickup_address);
			//kvPair.put("ent_addr_line2",addressLine2);
			kvPair.put("ent_lat",from_latitude);
			kvPair.put("ent_long",from_longitude);
			if(dropoff_address!=null)
			{
				kvPair.put("ent_drop_addr_line1",dropoff_address);
				kvPair.put("ent_drop_addr_line2"," ");
				kvPair.put("ent_drop_lat",to_latitude);
				kvPair.put("ent_drop_long",to_longitude);
			}
			kvPair.put("ent_payment_type",payment_type);
			kvPair.put("ent_zipcode",zip_code);
			kvPair.put("ent_extra_notes"," ");
			if(mPromoCode!=null)
			{
				kvPair.put("ent_coupon",mPromoCode);
			}
			
			Utility.printLog("RequestPickupLater nearestDrivers request sent to "+params[0]);
			kvPair.put("ent_dri_email",params[0]);
			kvPair.put("ent_card_id"," ");
			kvPair.put("ent_later_dt",laterBookingDate);
			kvPair.put("ent_date_time",currenttime);

			Utility.printLog("aaa="+session.getSessionToken());
			Utility.printLog("aaa="+session.getDeviceId());
			Utility.printLog("aaa="+currenttime);

			HttpResponse httpResponse = null;
			try 
			{
				httpResponse = Utility.doPost(url,kvPair);
			}
			catch (ClientProtocolException e1) 
			{
				e1.printStackTrace();
				Utility.printLog("doPost Exception......."+e1);
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
				Utility.printLog("doPost Exception......."+e1);
			}

			String jsonResponse = null;
			if (httpResponse!=null)
			{
				try 
				{
					jsonResponse = EntityUtils.toString(httpResponse.getEntity());
				}
				catch (ParseException e) 
				{
					e.printStackTrace();
					Utility.printLog("doPost Exception......."+e);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
					Utility.printLog("doPost Exception......."+e);
				}
			}
			Utility.printLog("Livebooking Response: "+jsonResponse);

			if (jsonResponse!=null)
			{
				Gson gson = new Gson();
				response=gson.fromJson(jsonResponse,LiveBookingResponse.class);
				Utility.printLog("DONE WITH GSON LATER");
				
				
			}
			else
			{
				runOnUiThread(new Runnable() 
				{
					public void run() 
					{
						Toast.makeText(RequestPickupLater.this,getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
					}
				});
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			try
			{
				if(response!=null)
				{
					if(response.getErrFlag().equals("0") && response.getErrNum().equals("39"))
					{
						Utility.printLog("RequestPickup Livebooking onPostExecute sucess getErrNum="+response.getErrNum());
						
						
						Intent returnIntent = new Intent();
						returnIntent.putExtra("LaterMsg", response.getErrMsg());
						setResult(RESULT_OK,returnIntent); 
						
						finish();
						overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
						return;
					}
					else if(response.getErrFlag().equals("0") && response.getErrNum().equals("78"))
					{
						Utility.printLog("RequestPickup Livebooking onPostExecute sucess getErrNum="+response.getErrNum());
						
							
						Intent returnIntent = new Intent();
						returnIntent.putExtra("LaterMsg", response.getErrMsg());
						setResult(RESULT_OK,returnIntent);     
						finish();
						overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
						return;
					}
					else
					{
						//again sending the request to other driver if previous driver is not accepted our request
						++driverIndex;
						if(nearestDrivers.size() > driverIndex)
						{
							String[] params = {nearestDrivers.get(driverIndex)};
							//Utility.printLog("Livebooking Response: RequestPickup nearestDrivers email onPostExecute sending request again to "+params[0]);
							
							if(requestLiveBooking.getStatus() == Status.RUNNING)
							{
								Utility.printLog("RequestPickup Livebooking Response: getStatus:true");
								requestLiveBooking.cancel(true);
							}
							else
							{
								Utility.printLog("RequestPickup Livebooking Response: getStatus: false");
							}	
							
							Utility.printLog("RequestPickup Livebooking Response: RequestPickup nearestDrivers email onPostExecute sending request again to "+params[0]);
							requestLiveBooking = new NewBackGroundLiveBooking();
							requestLiveBooking.execute(params);
						}
						else
						{
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RequestPickupLater.this);
							// set title
							alertDialogBuilder.setTitle(getResources().getString(R.string.alert));
							// set dialog message
							alertDialogBuilder
							.setMessage(response.getErrMsg())
							.setCancelable(false)

							.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() 
							{
								public void onClick(DialogInterface dialog,int id) 
								{
									// if this button is clicked, just close the dialog box and do nothing
									dialog.dismiss();
									Intent returnIntent = new Intent();
									returnIntent.putExtra("LaterMsg", response.getErrMsg());
									finish();
									overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
								}
							});
							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
							// show it
							alertDialog.show();
						}
					}
				}
				else
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RequestPickupLater.this);

					// set title
					alertDialogBuilder.setTitle(getResources().getString(R.string.error));

					// set dialog message
					alertDialogBuilder
					.setMessage(getResources().getString(R.string.network_alert_message))
					.setCancelable(false)

					.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog,int id) 
						{
							// if this button is clicked, just close the dialog box and do nothing
							dialog.dismiss();
							Intent returnIntent = new Intent();
							setResult(RESULT_OK,returnIntent);     
							finish();
							overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
						}
					});
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
					// show it
					alertDialog.show();
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	

	//Using Flurry for App analytics
	@Override
	protected void onStart()
	{
		super.onStart();
		FlurryAgent.onStartSession(this, "8c41e9486e74492897473de501e087dbc6d9f391");
	}

	//Using Flurry for App analytics
	@Override
	protected void onStop()
	{
		super.onStop();		
		FlurryAgent.onEndSession(this);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
	}


	

	
}
