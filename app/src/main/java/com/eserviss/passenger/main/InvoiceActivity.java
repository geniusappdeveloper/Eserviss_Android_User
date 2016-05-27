package com.eserviss.passenger.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.eserviss.passenger.pojo.InvoiceResponse;
import com.eserviss.passenger.pojo.StatusInformation;
import com.eserviss.passenger.pojo.TipResponse;
import com.egnyt.eserviss.R;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

public class InvoiceActivity extends Activity implements OnClickListener
{
	private RelativeLayout RL_Invoice,invoice_navigation_relative,relative_tip_discount_layout;
	private TextView Pickup,Dropoff,Distance,Duration,Pickup_Date,Drop_Date,Avg_Speed,total_fare,booking_id;
	private TextView subtotal,promoCode,discount,newSubtotal,total;//,invoice_ccfee_txt,invoice_ccfee_amount,,tipPercentage,tip
	private Button Done,disputeButton,tipButton;
	//Button Fare;
	private RatingBar ratingBar;
	private EditText review;
	private ProgressDialog progressDialog;
	private SessionManager session;
	private InvoiceResponse response;
	ProgressDialog dialogL;
	private TipResponse tipResponse;
	private  String driverEmail=null,apntDate=null;
	private StatusInformation statusResponse;
	String message,DisputeResponse;
	TextView login_nav_text,txt_duration,txt_avg_speed,txt_distance,invoice_subtotal_txt,invoice_newsubtotal_txt,invoice_total_txt,txt_rating,login_nav_text_invoice;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.invoice_screen);
		session= new SessionManager(InvoiceActivity.this);
		intialize();
		ResideMenuActivity.invoice_driver_tip.setVisibility(View.GONE);
		tipResponse = (TipResponse) getIntent().getSerializableExtra("Tip");
		if(session.getDriverEmail()!=null && session.getAptDate()!=null)
		{
			new BackgroundGetInvoiceDetails().execute();

		}
		else
		{
			AppStatus();                                        //added the service call to see whether driver email and apt date stored in session not empty
		}


	}

	@Override
	protected void onResume() {
		super.onResume();
		if(Utility.isNetworkAvailable(InvoiceActivity.this))
		{
			new BackgroundGetInvoiceDetails().execute();
		}
		else
		{
			Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), InvoiceActivity.this);
		}
	}

	private void intialize()
	{
		invoice_navigation_relative=(RelativeLayout) findViewById(R.id.invoice_navigation_relative);
		RL_Invoice=(RelativeLayout)findViewById(R.id.rl_invoice);
		Done=(Button)findViewById(R.id.doneButton);
		Pickup=(TextView)findViewById(R.id.invoice_pickup_location);
		Dropoff=(TextView)findViewById(R.id.invoice_dropoff_location);
		Pickup_Date=(TextView)findViewById(R.id.pickup_date);
		Drop_Date=(TextView)findViewById(R.id.dropoff_date);
		Distance=(TextView)findViewById(R.id.invoice_distance);
		Duration=(TextView)findViewById(R.id.invoice_duration);
	//	Fare=(Button)findViewById(R.id.total_amount_button);
		review=(EditText)findViewById(R.id.invoice_review);
		ratingBar=(RatingBar)findViewById(R.id.invoice_driver_rating);
		Avg_Speed=(TextView)findViewById(R.id.invoice_avg_speed);
		total_fare=(TextView)findViewById(R.id.txt_fare);
		booking_id=(TextView)findViewById(R.id.invoice_booking_id);
		relative_tip_discount_layout=(RelativeLayout) findViewById(R.id.relative_tip_discount_layout);
		subtotal=(TextView)findViewById(R.id.invoice_subtotal_amount);
		promoCode=(TextView)findViewById(R.id.invoice_discount_txt);
		discount=(TextView)findViewById(R.id.invoice_discount_amount);
		newSubtotal=(TextView)findViewById(R.id.invoice_newsubtotal_amount);
		total=(TextView)findViewById(R.id.invoice_total_amount);
		txt_rating=(TextView)findViewById(R.id.txt_rating);

		//==============================My change April========================================
		disputeButton=(Button) findViewById(R.id.disputeButton);
		login_nav_text_invoice= (TextView) findViewById(R.id.login_nav_text_invoice);
		invoice_total_txt= (TextView) findViewById(R.id.invoice_total_txt);
		invoice_newsubtotal_txt= (TextView) findViewById(R.id.invoice_newsubtotal_txt);
		invoice_subtotal_txt= (TextView) findViewById(R.id.invoice_subtotal_txt);
		txt_duration= (TextView) findViewById(R.id.txt_duration);
		txt_distance= (TextView) findViewById(R.id.txt_distance);
		txt_avg_speed= (TextView) findViewById(R.id.txt_avg_speed);
		Typeface roboto_condensed = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/BebasNeue.otf");
		Done.setTypeface(roboto_condensed);
		Pickup.setTypeface(roboto_condensed);
		Dropoff.setTypeface(roboto_condensed);
		Pickup_Date.setTypeface(roboto_condensed);
		Drop_Date.setTypeface(roboto_condensed);
		Distance.setTypeface(roboto_condensed);
		//Fare.setTypeface(roboto_condensed);
		review.setTypeface(roboto_condensed);
		Avg_Speed.setTypeface(roboto_condensed);
		total_fare.setTypeface(roboto_condensed);
		booking_id.setTypeface(roboto_condensed);
		subtotal.setTypeface(roboto_condensed);
		promoCode.setTypeface(roboto_condensed);
		discount.setTypeface(roboto_condensed);
		newSubtotal.setTypeface(roboto_condensed);
		total.setTypeface(roboto_condensed);
		txt_duration.setTypeface(roboto_condensed);
		txt_distance.setTypeface(roboto_condensed);
		txt_avg_speed.setTypeface(roboto_condensed);
		invoice_subtotal_txt.setTypeface(roboto_condensed);
		invoice_newsubtotal_txt.setTypeface(roboto_condensed);
		invoice_total_txt.setTypeface(roboto_condensed);
		txt_rating.setTypeface(roboto_condensed);
		login_nav_text_invoice.setTypeface(roboto_condensed);
		disputeButton.setTypeface(roboto_condensed);
		//=========================================================================

		disputeButton.setOnClickListener(this);
		RL_Invoice.setOnClickListener(this);
		Done.setOnClickListener(this);

	}



	class BackgroundGetInvoiceDetails extends AsyncTask<String,Void,String>
	{
		private  String TAG = null;
		private ProgressDialog dialogL;
		private String jsonResponse;

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			dialogL=Utility.GetProcessDialog(InvoiceActivity.this);

			if(dialogL!=null) 
			{
				dialogL.show();
			}
		}

		@Override
		protected String doInBackground(String... params)
		{
			SessionManager session=new SessionManager(InvoiceActivity.this);
			String url=VariableConstants.BASE_URL+"getAppointmentDetails";

			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			Utility.printLog("","dataandTime "+curenttime);
			Utility.printLog("input values ccc ent_sess_token="+session.getSessionToken()+" ent_dev_id="+session.getDeviceId()
					+" ent_appnt_dt="+session.getAptDate()+" ent_doc_email="+session.getDriverEmail());

			Map<String, String> kvPairs = new HashMap<String, String>();
			kvPairs.put("ent_sess_token",session.getSessionToken());
			kvPairs.put("ent_dev_id",session.getDeviceId());

			if (driverEmail != null) {
				kvPairs.put("ent_email", driverEmail);
			} else
			{
				kvPairs.put("ent_email", session.getDriverEmail());
			}
			if (apntDate != null) {
				kvPairs.put("ent_appnt_dt", apntDate);
			} else
			{
				kvPairs.put("ent_appnt_dt", session.getAptDate());
			}

			kvPairs.put("ent_user_type","2");
			kvPairs.put("ent_date_time",curenttime);
			Log.i("invice","param "+kvPairs);

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

			if (httpResponse!=null) 
			{
				try 
				{
					jsonResponse = EntityUtils.toString(httpResponse.getEntity());
				} 
				catch (ParseException e) {
					e.printStackTrace();
					Utility.printLog(TAG, "doPost Exception......."+e);
				} catch (IOException e) {
					e.printStackTrace();
					Utility.printLog(TAG, "doPost Exception......."+e);
				}
			}
			Utility.printLog("InvoiceResponse: "+jsonResponse);
			return null;
		}
		@Override
		protected void onPostExecute(String result) 
		{
			super.onPostExecute(result);

			if (dialogL!=null && dialogL.isShowing()) 
			{
				dialogL.dismiss();
			}

			if (jsonResponse!=null) 
			{
				Gson gson = new Gson();
				response=gson.fromJson(jsonResponse,InvoiceResponse.class);
				Utility.printLog(TAG,"DONE WITH GSON");
			}
			else
			{
				Toast.makeText(InvoiceActivity.this,getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
			}
			if(response!=null)
			{
				Utility.printLog("Invoiced Screen response = "+response);
				
				if(response.getErrFlag().equals("0"))
				{
					relative_tip_discount_layout.setVisibility(View.VISIBLE);
					invoice_navigation_relative.setVisibility(View.VISIBLE);
					Pickup.setText(response.getAddr1());
					Pickup_Date.setText(response.getPickupDt());
					Dropoff.setText(response.getDropAddr1());
					Drop_Date.setText(response.getDropDt());
					Distance.setText(response.getDis()+" " +getResources().getString(R.string.distanceUnit));
					Avg_Speed.setText(round(Double.parseDouble(response.getAvgSpeed()))+" " +getResources().getString(R.string.distanceUnit)+"/h");
					booking_id.setText("Booking ID: "+response.getBid());
					
					//Fare.setText(session.getCurrencySymbol()+" "+round(Double.parseDouble(response.getAmount())));
					total_fare.setText(getResources().getString(R.string.currencuSymbol)+" "+round(Double.parseDouble(response.getAmount())));
                   
					try 
					{
						String dropDate=response.getDropDt();
						Utility.printLog("Driver Started getDropDt="+dropDate);

						String StartTime=response.getPickupDt();
						Utility.printLog("Driver Started StartTime="+StartTime);
						String[] StartTime1 = StartTime.split(" ");
						String[] StartTime2 = StartTime1[1].split(":");

						String[] currentTime1 = dropDate.split(" ");
						String[] currentTime2 = currentTime1[1].split(":");

						int hours = Integer.parseInt(currentTime2[0])-Integer.parseInt(StartTime2[0]);
						int min = Integer.parseInt(currentTime2[1])-Integer.parseInt(StartTime2[1]);
						int sec = Integer.parseInt(currentTime2[2])-Integer.parseInt(StartTime2[2]);

						if(hours<0)
							hours=-(-hours);
						if(min<0)
							min=-(-min);
						if(sec<0)
							sec=-(-sec);

						Duration.setText(hours+"H : "+min+"M");

						//private TextView subtotal,promoCode,discount,newSubtotal,tipPercentage,tip,total;

						subtotal.setText(getResources().getString(R.string.currencuSymbol)+" "+round(Double.parseDouble(response.getFare())));
						if(response.getCode()==null || response.getCode().equals(""))
						{
							promoCode.setText("DISCOUNT");
						}
						else
						{
							promoCode.setText("DISCOUNT");
						}
						if(response.getDiscount()==null || response.getDiscount().equals(""))
						{
							discount.setText(getResources().getString(R.string.currencuSymbol)+" "+" 0.00");
						}
						else
						{
							discount.setText(getResources().getString(R.string.currencuSymbol)+" "+round(Double.parseDouble(response.getDiscount())));
						}


						if(!response.getFare().equals("") && !response.getDiscount().equals(""))
						{
							double sub = Double.parseDouble(response.getFare()) - Double.parseDouble(response.getDiscount());
							if(sub>0)
							{
								newSubtotal.setText(getResources().getString(R.string.currencuSymbol)+" "+round(sub));

							}
							else
							{
								newSubtotal.setText("0");
							}
						}
                        //invoice_ccfee_amount.setText(getResources().getString(R.string.currencuSymbol)+" "+round(Double.parseDouble(response.getCc_fee())));
						total.setText(getResources().getString(R.string.currencuSymbol)+" "+round(Double.parseDouble(response.getAmount())));

					} 
					catch (Exception e) 
					{
						Duration.setText(response.getDur());
					}
				}
				else
				{
				}
			}
			else
			{
				Toast.makeText(InvoiceActivity.this, getResources().getString(R.string.server_error),Toast.LENGTH_SHORT).show();
			}
		}
	}

	class BackgroundSubmitReview extends AsyncTask<String,Void,String>
	{
		private  String TAG = null;
		private String jsonResponse;
		BookAppointmentResponse response;
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			dialogL=Utility.GetProcessDialog(InvoiceActivity.this);

			if(dialogL!=null) 
			{
				dialogL.show();
			}
		}
		@Override
		protected String doInBackground(String... params)
		{
			SessionManager session=new SessionManager(InvoiceActivity.this);
			String url=VariableConstants.BASE_URL+"updateSlaveReview";
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			Utility.printLog("","dataandTime "+curenttime);

			Utility.printLog("ccc review ent_sess_token="+session.getSessionToken()+" ent_dev_id="+session.getDeviceId()
					+" ent_appnt_dt="+session.getAptDate()+" ent_dri_email="+session.getDriverEmail());
			Utility.printLog("ccc review ratingBar="+(int)ratingBar.getRating()+" review="+review.getText().toString());

			Map<String, String> kvPairs = new HashMap<String, String>();

			kvPairs.put("ent_sess_token",session.getSessionToken());
			kvPairs.put("ent_dev_id",session.getDeviceId());

			if (driverEmail != null) {
				kvPairs.put("ent_dri_email", driverEmail);
			} else
			{
				kvPairs.put("ent_dri_email", session.getDriverEmail());
			}
			if (apntDate != null) {
				kvPairs.put("ent_appnt_dt", apntDate);
			} else
			{
				kvPairs.put("ent_appnt_dt", session.getAptDate());
			}

			kvPairs.put("ent_rating_num",""+(int)ratingBar.getRating());

			if(review.getText().toString()!=null)
				kvPairs.put("ent_review_msg",review.getText().toString());

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

			if (httpResponse!=null) 
			{
				try 
				{
					jsonResponse = EntityUtils.toString(httpResponse.getEntity());
					//Toast.makeText(InvoiceActivity.this, "Success", Toast.LENGTH_LONG).show();
				} catch (ParseException e) {
					e.printStackTrace();
					Utility.printLog(TAG, "doPost Exception......."+e);
				} catch (IOException e) {
					e.printStackTrace();
					Utility.printLog(TAG, "doPost Exception......."+e);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			Utility.printLog("SubmitInvoiceResponse: "+jsonResponse);
			Utility.printLog(" ",jsonResponse);

			if(jsonResponse!=null)
			{
				Gson gson = new Gson();
				response=gson.fromJson(jsonResponse,BookAppointmentResponse.class);
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) 
		{
			super.onPostExecute(result);

			if (dialogL!=null) 
			{
				dialogL.dismiss();
				dialogL = null;
			}

			if(response!=null)
			{
				if(response.getErrFlag().equals("0"))
				{
					session.storeAptDate(null);
					session.storeBookingId("0");

					session.setInvoiceRaised(false);
					session.setDriverArrived(false);
					session.setTripBegin(false);
					session.setDriverOnWay(false);

					Toast.makeText(InvoiceActivity.this, "Booking Completed", Toast.LENGTH_LONG).show();
					Intent i = new Intent(InvoiceActivity.this, ResideMenuActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(i);
					overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
				}
				else
				{
					session.storeAptDate(null);
					session.storeBookingId("0");

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InvoiceActivity.this);

					// set title
					alertDialogBuilder.setTitle(getResources().getString(R.string.error));

					// set dialog message
					alertDialogBuilder
					.setMessage(response.getErrMsg())
					.setCancelable(false)

					.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog,int id) 
						{
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
							Intent i = new Intent(InvoiceActivity.this, ResideMenuActivity.class);
							// set the new task and clear flags
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(i);
							overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
						}
					});

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();
				}

			}

		}

	}

	@Override
	public void onBackPressed() 
	{
		//showAlert();
	}


	@Override
	public void onClick(View v)
	{
		if(v.getId()==R.id.doneButton)
		{

			if(Utility.isNetworkAvailable(InvoiceActivity.this))
			{
		         	new BackgroundSubmitReview().execute();
			}
			else{
				Toast.makeText(InvoiceActivity.this,getResources().getString(R.string.network_connection_fail),Toast.LENGTH_LONG).show();
			}
		}

		else if(v.getId()==R.id.rl_invoice)
		{
			showAlert();
		}
		else if(v.getId()==R.id.disputeButton)
		{
			if(Utility.isNetworkAvailable(InvoiceActivity.this))
			{
			   final Dialog	fDialog = new Dialog(InvoiceActivity.this);
				fDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				fDialog.setContentView(R.layout.disputedialog);



				Button okButton,cancelButton;
				final EditText dispute_message;
				TextView dispute_title,dispute_desp;
				dispute_message = (EditText) fDialog.findViewById(R.id.disputemessage);
				okButton = (Button) fDialog.findViewById(R.id.submit);
				cancelButton = (Button) fDialog.findViewById(R.id.cancel);
				dispute_title = (TextView) fDialog.findViewById(R.id.title_text);
				dispute_desp = (TextView) fDialog.findViewById(R.id.hesitate_txt);

				//======================My Change=============================
				Typeface roboto_condensed = Typeface.createFromAsset(InvoiceActivity.this.getAssets(),"fonts/BebasNeue.otf");
				//dispute_message.setTypeface(roboto_condensed);
				//dispute_title.setTypeface(roboto_condensed);
				//dispute_desp.setTypeface(roboto_condensed);
				//okButton.setTypeface(roboto_condensed);
				//cancelButton.setTypeface(roboto_condensed);

				cancelButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						fDialog.dismiss();
					}
				});
				okButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v)

					{
						if(!dispute_message.getText().toString().trim().isEmpty())
						{
								if(Utility.isNetworkAvailable(InvoiceActivity.this)) {
								DISPUTE(fDialog,dispute_message.getText().toString());

								}
								else

									Toast.makeText(InvoiceActivity.this,getResources().getString(R.string.network_connection_fail),Toast.LENGTH_SHORT).show();

						}
						else
						{
							Toast.makeText(InvoiceActivity.this,"Please provide valid reason",Toast.LENGTH_SHORT).show();

						}
					}
				});
				fDialog.show();



			}
			else
			{

				Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), InvoiceActivity.this);
			}
		}
	}

	public void showAlert()
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(InvoiceActivity.this);

		// Setting Dialog Title
		alertDialog.setTitle(getResources().getString(R.string.alert));

		// Setting Dialog Message
		alertDialog.setMessage(getResources().getString(R.string.skip_review_details));

		// On pressing Settings button
		alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog,int which)
			{

				SessionManager session =new SessionManager(InvoiceActivity.this);

				session.setInvoiceRaised(false);
				session.setDriverArrived(false);
				session.setTripBegin(false);
				session.setDriverOnWay(false);
				new BackgroundSubmitReview().execute();

				/*MainActivity.DriverInvoiceRaised=false;
				MainActivity.DriverOntheWay=false;
				MainActivity.DriverReached=false;*/
			}
		});

		// on pressing cancel button
		alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

	private String round(double value)
	{
		String s = String.format(Locale.ENGLISH,"%.2f", value);
		Utility.printLog("rounded value="+s);
		return s;
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		FlurryAgent.onStartSession(this, "8c41e9486e74492897473de501e087dbc6d9f391");
	}

	@Override
	protected void onStop()
	{
		super.onStop();		
		FlurryAgent.onEndSession(this);
	}

	private void AppStatus()
	{

		dialogL=Utility.GetProcessDialog(InvoiceActivity.this);
		// make the progress bar cancelable
		dialogL.setCancelable(false);
		// set a message text
		dialogL.setMessage("Checking booking status...");
		if(dialogL!=null)
		{
			//dialog2.show();
		}
		RequestQueue volleyRequest = Volley.newRequestQueue(InvoiceActivity.this);
		StringRequest myReq = new StringRequest(Request.Method.POST,VariableConstants.BASE_URL+"getApptStatus",
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response)
					{
						if (dialogL!=null)
						{
							//dialog2.dismiss();
							dialogL=null;
						}
						Utility.printLog("Success of getting user App Info="+response);
						getAppStatus(response);
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						if (dialogL!=null)
						{
							//dialog2.dismiss();
							dialogL=null;
						}
						Utility.printLog("Success of getting error="+error);
					}
				}){
			protected HashMap<String,String> getParams() throws com.android.volley.AuthFailureError
			{
				HashMap<String,String> kvPair = new HashMap<String, String>();
				SessionManager session=new SessionManager(InvoiceActivity.this);
				Utility utility=new Utility();
				String curenttime=utility.getCurrentGmtTime();
				Utility.printLog("getSessionToken="+session.getSessionToken());
				Utility.printLog("getDeviceId="+session.getDeviceId());
				if(session.getAptDate()!=null)
					Utility.printLog("getAptDate="+session.getAptDate());
				Utility.printLog("dataandTime=" + curenttime);

				kvPair.put("ent_sess_token",session.getSessionToken());
				kvPair.put("ent_dev_id",session.getDeviceId());
				if(session.getAptDate()!=null)
					kvPair.put("ent_appnt_dt",session.getAptDate());
				kvPair.put("ent_user_type","2");
				kvPair.put("ent_date_time",curenttime);
				Utility.printLog("akbar aptstatus params"+kvPair);
				return kvPair;
			};
		};
		volleyRequest.add(myReq);
	}

	private void getAppStatus(String getAppStatus)
	{
		try {
			JSONObject jsnResponse = new JSONObject(getAppStatus);
			String jsonErrorParsing = jsnResponse.getString("errFlag");
			Utility.printLog("jsonErrorParsing is ---> " + jsonErrorParsing);

			Gson gson = new Gson();
			statusResponse = gson.fromJson(getAppStatus, StatusInformation.class);
			Utility.printLog("jsonErrorParsing is avavscsc---> " + getAppStatus);
			Utility.printLog("jsonErrorParsing is status Response---> " + statusResponse);
			//Utility.printLog("akbar status response email"+statusResponse.getData().get(0).getEmail()+"");
			if (statusResponse.getData().get(0).getEmail() != null) {
				if (statusResponse.getErrFlag().equals("0")) {

					driverEmail = statusResponse.getData().get(0).getEmail();
					apntDate = statusResponse.getData().get(0).getApptDt();
					new BackgroundGetInvoiceDetails().execute();
				}
			}
		}


		catch (JSONException e) {
			e.printStackTrace();

		}
	}


	private void DISPUTE(final Dialog dialog, final String disputeMessage)
	{

		RequestQueue volleyRequest = Volley.newRequestQueue(InvoiceActivity.this);

		dialogL=ProgressDialog.show(InvoiceActivity.this,"Please Wait..",null);
		dialogL.setCanceledOnTouchOutside(false);

		// StringRequest myReq = new StringRequest(Request.Method.POST,"http://app.eserviss.com/Taxi/services.php/applyPromo",
		StringRequest myReq = new StringRequest(Request.Method.POST,VariableConstants.BASE_URL+"reportDispute",
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {

						dialogL.dismiss();
						try {
							JSONObject jsonObject= new JSONObject(response);
							message = jsonObject.getString("message");
						} catch (JSONException e) {
							e.printStackTrace();
						}

						DisputeResponse = response;
						Utility.printLog("Success of getting user Info"+response);
						Toast.makeText(InvoiceActivity.this, message, Toast.LENGTH_LONG).show();
						dialog.dismiss();
						System.out.println("DISPUTEResponse"+DisputeResponse);
						//dialogL.dismiss();

					}
				}, 	new Response.ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error)
			{
				dialogL.dismiss();
				Toast.makeText(InvoiceActivity.this, "System error in getting user Info please retry", Toast.LENGTH_LONG).show();
				Utility.printLog("Error for volley");


			}
		}){
			protected HashMap<String,String> getParams() throws com.android.volley.AuthFailureError {
				HashMap<String,String> kvPair = new HashMap<String, String>();

				SessionManager session=new SessionManager(InvoiceActivity.this);

				Utility utility=new Utility();
				String curenttime=utility.getCurrentGmtTime();

				kvPair.put("ent_appnt_dt",session.getAptDate());
				kvPair.put("ent_sess_token",session.getSessionToken() );
				kvPair.put("ent_dev_id",session.getDeviceId());
				kvPair.put("ent_report_msg",disputeMessage);
				kvPair.put("ent_date_time",curenttime);

				Log.i("","aaa="+session.getSessionToken());
				Log.i("","aaa="+session.getDeviceId());
				Log.i("","aaa="+curenttime);

				return kvPair;
			};

		};

		volleyRequest.add(myReq);
	}



}
