 package com.eserviss.passenger.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.eserviss.passenger.pojo.InvoiceResponse;
import com.egnyt.eserviss.R;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

public class InvoicePaymentNotDone extends Activity implements OnClickListener
{
	private RelativeLayout RL_Invoice;
	private TextView Pickup,Dropoff,Distance,Duration,Pickup_Date,Drop_Date,bookingID,total_fare;
	private TextView subtotal,promoCode,discount,newSubtotal,total;//,tipPercentage,tip
	private Button Done,Fare;
	private String appt_date,apt_email;
	private SessionManager session;
	TextView login_nav_text,txt_duration,txt_avg_speed,txt_distance,invoice_subtotal_txt,invoice_newsubtotal_txt,invoice_total_txt,txt_rating;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.invoice_payment_not_done);
		session=new SessionManager(InvoicePaymentNotDone.this);
		appt_date = getIntent().getStringExtra("apt_date");
		apt_email = getIntent().getStringExtra("apt_email");
		
		intialize();
		new BackgroundGetInvoiceDetails().execute();
	}
	
	private void intialize()
	{
		RL_Invoice=(RelativeLayout)findViewById(R.id.rl_invoice);
		Done=(Button)findViewById(R.id.pay);
		Pickup=(TextView)findViewById(R.id.invoice_pickup_location);
		Dropoff=(TextView)findViewById(R.id.invoice_dropoff_location);
		Pickup_Date=(TextView)findViewById(R.id.pickup_date);
		Drop_Date=(TextView)findViewById(R.id.dropoff_date);
		Distance=(TextView)findViewById(R.id.invoice_distance);
		Duration=(TextView)findViewById(R.id.invoice_duration);
		Fare=(Button)findViewById(R.id.total_amount_button);
		bookingID=(TextView)findViewById(R.id.invoice_booking_id);
		total_fare=(TextView)findViewById(R.id.txt_fare);
		
		subtotal=(TextView)findViewById(R.id.invoice_subtotal_amount);
		promoCode=(TextView)findViewById(R.id.invoice_discount_txt);
		discount=(TextView)findViewById(R.id.invoice_discount_amount);
		newSubtotal=(TextView)findViewById(R.id.invoice_newsubtotal_amount);
		total=(TextView)findViewById(R.id.invoice_total_amount);
		invoice_total_txt= (TextView) findViewById(R.id.invoice_total_txt);
		invoice_newsubtotal_txt= (TextView) findViewById(R.id.invoice_newsubtotal_txt);
		//==============================My change April========================================
		invoice_total_txt= (TextView) findViewById(R.id.invoice_total_txt);
		invoice_newsubtotal_txt= (TextView) findViewById(R.id.invoice_newsubtotal_txt);
		invoice_subtotal_txt= (TextView) findViewById(R.id.invoice_subtotal_txt);
		txt_duration= (TextView) findViewById(R.id.txt_duration);
		txt_distance= (TextView) findViewById(R.id.txt_distance);
		txt_avg_speed= (TextView) findViewById(R.id.txt_avg_speed);

		Typeface roboto_condensed = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/BebasNeue.otf");
		Pickup.setTypeface(roboto_condensed);
		Dropoff.setTypeface(roboto_condensed);
		Pickup_Date.setTypeface(roboto_condensed);
		Drop_Date.setTypeface(roboto_condensed);
		Distance.setTypeface(roboto_condensed);
		Fare.setTypeface(roboto_condensed);
		Duration.setTypeface(roboto_condensed);
		Done.setTypeface(roboto_condensed);
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
		//=========================================================================
		RL_Invoice.setOnClickListener(this);
		Done.setOnClickListener(this);
	}
	
	class BackgroundGetInvoiceDetails extends AsyncTask<String,Void,String>
	{
		private  String TAG = null;
		private ProgressDialog dialogL;
		private String jsonResponse;
		InvoiceResponse response;
		
		@Override
		protected void onPreExecute()
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialogL=Utility.GetProcessDialog(InvoicePaymentNotDone.this);
			
			if (dialogL!=null) 
			{
				dialogL.show();
			}
		}
		
		@Override
		protected String doInBackground(String... params)
		{
			// TODO Auto-generated method stub
			SessionManager session=new SessionManager(InvoicePaymentNotDone.this);
			String url=VariableConstants.BASE_URL+"getAppointmentDetails";
			
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			Utility.printLog("","dataandTime "+curenttime);
			Utility.printLog("ccc ent_sess_token="+session.getSessionToken()+" ent_dev_id="+session.getDeviceId()
					+" ent_appnt_dt="+session.getAptDate()+" ent_doc_email="+session.getDriverEmail());
			
			Map<String, String> kvPairs = new HashMap<String, String>();
			kvPairs.put("ent_sess_token",session.getSessionToken());
			kvPairs.put("ent_dev_id",session.getDeviceId());
			kvPairs.put("ent_email",apt_email);
			kvPairs.put("ent_appnt_dt",appt_date);
			kvPairs.put("ent_user_type","2");
			kvPairs.put("ent_date_time", curenttime);
			
			HttpResponse httpResponse = null;
			try {
				httpResponse = Utility.doPost(url,kvPairs);
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Utility.printLog(TAG, "doPost Exception......."+e1);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
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
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utility.printLog(TAG, "doPost Exception......."+e);
				} catch (IOException e) {
					// TODO Auto-generated catch block
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
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					
					if (dialogL!=null) 
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
						Toast.makeText(InvoicePaymentNotDone.this,"Request Timeout !!", Toast.LENGTH_SHORT).show();
					}
					
					if(response!=null)
					{
						if(response.getErrFlag().equals("0"))
						{
							Utility.printLog("InvoiceResponse amount="+response.getAmount());
							
							Pickup.setText(response.getAddr1());
							Pickup_Date.setText(response.getPickupDt());
							Dropoff.setText(response.getDropAddr1());
							Drop_Date.setText(response.getDropDt());
							//double distance=Double.parseDouble(response.getDis());
							Distance.setText(response.getDis()+" " +getResources().getString(R.string.distanceUnit));
							Fare.setText(getResources().getString(R.string.currencuSymbol)+" "+response.getFare());
							bookingID.setText("Booking ID: "+response.getBid());
							total_fare.setText(getResources().getString(R.string.currencuSymbol)+" "+response.getFare());
							

							try {
								
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
								
								subtotal.setText(getResources().getString(R.string.currencuSymbol)+" "+round(Double.parseDouble(response.getFare())));
								if(response.getCode()!=null && response.getCode().equals(""))
								{
									promoCode.setText("DISCOUNT");
								}
								else
								{
									promoCode.setText("DISCOUNT("+response.getCode()+")");
								}
								discount.setText(getResources().getString(R.string.currencuSymbol)+" "+round(Double.parseDouble(response.getDiscount())));
								
								if(!response.getFare().equals("") && !response.getDiscount().equals(""))
								{
									double sub = Double.parseDouble(response.getFare()) - Double.parseDouble(response.getDiscount());
									newSubtotal.setText(getResources().getString(R.string.currencuSymbol)+" "+round(sub));
								}
								
								total.setText(session.getCurrencySymbol()+" "+round(Double.parseDouble(response.getAmount())));
								
							} catch (Exception e) {
								// TODO: handle exception
								Duration.setText(response.getDur());
							}
						}
						else
						{
							Toast.makeText(InvoicePaymentNotDone.this, response.getErrMsg(),Toast.LENGTH_SHORT).show();
						}
					}
					else
					{
						Toast.makeText(InvoicePaymentNotDone.this, "System Error!",Toast.LENGTH_SHORT).show();
					}
					
				}
	}

	

	@Override
	public void onBackPressed() 
	{
		// TODO Auto-generated method stub
		finish();
		overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
	}


	@Override
	public void onClick(View v)
	{

		if(v.getId()==R.id.rl_invoice)
		{
			finish();
			overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
		}
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
	
	private String round(double value)
	{
		String s = String.format(Locale.ENGLISH,"%.2f", value);
		Utility.printLog("rounded value="+s);
		return s;
	}

}
