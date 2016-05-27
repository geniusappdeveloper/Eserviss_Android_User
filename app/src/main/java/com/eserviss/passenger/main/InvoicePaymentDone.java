 package com.eserviss.passenger.main;

import java.io.IOException;
import java.math.BigDecimal;
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

public class InvoicePaymentDone extends Activity implements OnClickListener
{
	private RelativeLayout RL_Invoice,relative_tip_discount_layout,invoice_navigation_relative,back_per;//layout;
	private TextView Pickup,Dropoff,Distance,Duration,avg_speed,Pickup_Date,Drop_Date,Fare,bookingId;
	private TextView subtotal,promoCode,discount,newSubtotal,total;//,invoice_ccfee_amount,tipPercentage,tip
	private String appt_date,apt_email;
	private SessionManager session;
	TextView login_nav_text,txt_duration,txt_avg_speed,txt_distance,invoice_subtotal_txt,invoice_newsubtotal_txt,invoice_total_txt;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.invoice_payment_done);
		session=new SessionManager(InvoicePaymentDone.this);
		
		appt_date = getIntent().getStringExtra("apt_date");
		apt_email = getIntent().getStringExtra("apt_email");
		
		intialize();
		new BackgroundGetInvoiceDetails().execute();
	}
	
	private void intialize()
	{
		relative_tip_discount_layout=(RelativeLayout) findViewById(R.id.relative_tip_discount_layout);
		invoice_navigation_relative=(RelativeLayout) findViewById(R.id.relative_tip_discount_layout);
		RL_Invoice=(RelativeLayout)findViewById(R.id.rl_invoice);
		Pickup=(TextView)findViewById(R.id.invoice_pickup_location);
		Dropoff=(TextView)findViewById(R.id.invoice_dropoff_location);
		Pickup_Date=(TextView)findViewById(R.id.pickup_date);
		Drop_Date=(TextView)findViewById(R.id.dropoff_date);
		Distance=(TextView)findViewById(R.id.invoice_distance);
		Duration=(TextView)findViewById(R.id.invoice_duration);
		Fare=(TextView)findViewById(R.id.txt_fare);
		bookingId=(TextView)findViewById(R.id.booking_id);
		avg_speed=(TextView)findViewById(R.id.invoice_avg_speed);
		//layout=(RelativeLayout)findViewById(R.id.rl_invoice_details);
		//invoice_ccfee_amount=(TextView) findViewById(R.id.invoice_ccfee_amount);

		subtotal=(TextView)findViewById(R.id.invoice_subtotal_amount);
		promoCode=(TextView)findViewById(R.id.invoice_discount_txt);
		discount=(TextView)findViewById(R.id.invoice_discount_amount);
		newSubtotal=(TextView)findViewById(R.id.invoice_newsubtotal_amount);
		//tipPercentage=(TextView)findViewById(R.id.invoice_tip_txt);
		//tip=(TextView)findViewById(R.id.invoice_tip_amount);
		total=(TextView)findViewById(R.id.invoice_total_amount);


		//==============================My change April========================================\
		invoice_total_txt= (TextView) findViewById(R.id.invoice_total_txt);
		invoice_newsubtotal_txt= (TextView) findViewById(R.id.invoice_newsubtotal_txt);
		invoice_subtotal_txt= (TextView) findViewById(R.id.invoice_subtotal_txt);

		back_per= (RelativeLayout) findViewById(R.id.back_per);
		txt_duration= (TextView) findViewById(R.id.txt_duration);
		txt_distance= (TextView) findViewById(R.id.txt_distance);
		txt_avg_speed= (TextView) findViewById(R.id.txt_avg_speed);
		login_nav_text= (TextView) findViewById(R.id.login_nav_text);
		Typeface roboto_condensed = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/BebasNeue.otf");
		Pickup.setTypeface(roboto_condensed);
		Dropoff.setTypeface(roboto_condensed);
		Pickup_Date.setTypeface(roboto_condensed);
		Drop_Date.setTypeface(roboto_condensed);
		Distance.setTypeface(roboto_condensed);
		Fare.setTypeface(roboto_condensed);
		Duration.setTypeface(roboto_condensed);
		avg_speed.setTypeface(roboto_condensed);
		bookingId.setTypeface(roboto_condensed);
		subtotal.setTypeface(roboto_condensed);
		promoCode.setTypeface(roboto_condensed);
		discount.setTypeface(roboto_condensed);
		newSubtotal.setTypeface(roboto_condensed);
		total.setTypeface(roboto_condensed);
		login_nav_text.setTypeface(roboto_condensed);
		txt_duration.setTypeface(roboto_condensed);
		txt_distance.setTypeface(roboto_condensed);
		txt_avg_speed.setTypeface(roboto_condensed);
		invoice_subtotal_txt.setTypeface(roboto_condensed);
		invoice_newsubtotal_txt.setTypeface(roboto_condensed);
		invoice_total_txt.setTypeface(roboto_condensed);
		//=========================================================================



		RL_Invoice.setOnClickListener(this);
		back_per.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InvoicePaymentDone.this.finish();
			}
		});
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
			dialogL=Utility.GetProcessDialog(InvoicePaymentDone.this);
			
			if (dialogL!=null) 
			{
				dialogL.show();
			}
		}
		
		@Override
		protected String doInBackground(String... params)
		{
			// TODO Auto-generated method stub
			SessionManager session=new SessionManager(InvoicePaymentDone.this);
			String url=VariableConstants.BASE_URL+"getAppointmentDetails";
			
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			Utility.printLog("","dataandTime "+curenttime);
			Utility.printLog("ccc ent_sess_token="+session.getSessionToken()+" ent_dev_id="+session.getDeviceId()
					+" ent_appnt_dt="+appt_date+" ent_doc_email="+apt_email);
			
			Map<String, String> kvPairs = new HashMap<String, String>();
			kvPairs.put("ent_sess_token",session.getSessionToken());
			kvPairs.put("ent_dev_id",session.getDeviceId());
			kvPairs.put("ent_email",apt_email);
			kvPairs.put("ent_appnt_dt",appt_date);
			kvPairs.put("ent_user_type","2");
			kvPairs.put("ent_date_time",curenttime);
			
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
						
						Utility.printLog("InvoiceResponse : "+jsonResponse);
						Gson gson = new Gson();
						response=gson.fromJson(jsonResponse,InvoiceResponse.class);
						Utility.printLog(TAG,"DONE WITH GSON");
					}
					else
					{
						Toast.makeText(InvoicePaymentDone.this,getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
					}
					
					if(response!=null)
					{
						if(response.getErrFlag().equals("0"))
						{
							Utility.printLog("InvoiceResponse amount="+response.getAmount());
							//layout.setVisibility(View.VISIBLE);
							relative_tip_discount_layout.setVisibility(View.VISIBLE);
							invoice_navigation_relative.setVisibility(View.VISIBLE);
							//Typeface segoeuiregular3 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");
							//Pickup.setTypeface(segoeuiregular3);
							Pickup.setText(response.getAddr1());
							Pickup.setTextSize(15);
							
							Pickup_Date.setText(response.getPickupDt());
							//Typeface segoeuiregular4 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");
							//Dropoff.setTypeface(segoeuiregular4);
							Dropoff.setText(response.getDropAddr1());
							Dropoff.setTextSize(15);
							Drop_Date.setText(response.getDropDt());
							//double distance=Double.parseDouble(response.getDis());
							Distance.setText(response.getDis()+" " +getResources().getString(R.string.distanceUnit));
							
							Fare.setText(getResources().getString(R.string.currencuSymbol)+" "+response.getFare());
							bookingId.setText("Booking ID: "+response.getBid());
							double distance1=round(Double.parseDouble(response.getAvgSpeed()),1);

							avg_speed.setText(distance1+" " +getResources().getString(R.string.distanceUnit)+"/h");
							
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
								
								subtotal.setText(getResources().getString(R.string.currencuSymbol)+" "+round(Double.parseDouble(response.getFare())));
								if(response.getCode()!=null && response.getCode().equals(""))
								{
									promoCode.setText("DISCOUNT");
								}
								else
								{
									promoCode.setText("DISCOUNT");
								}
								discount.setText(getResources().getString(R.string.currencuSymbol)+" "+round(Double.parseDouble(response.getDiscount())));
								/*if(response.getTipPercent()!=null && response.getTipPercent().equals(""))
								{
									tipPercentage.setText("TIP");
								}
								else
								{
									tipPercentage.setText("TIP("+response.getTipPercent()+"%)");
								}
								if(response.getTip()!=null && response.getTip().equals(""))
								{
									tip.setText(getResources().getString(R.string.currencuSymbol)+" "+" 00");
								}
								else
								{
									tip.setText(getResources().getString(R.string.currencuSymbol)+" "+round(Double.parseDouble(response.getTip())));
								}*/
								
								if(!response.getFare().equals("") && !response.getDiscount().equals(""))
								{
									double sub = Double.parseDouble(response.getFare()) - Double.parseDouble(response.getDiscount());
									newSubtotal.setText(getResources().getString(R.string.currencuSymbol)+" "+round(sub));
								}
		                       // invoice_ccfee_amount.setText(getResources().getString(R.string.currencuSymbol)+" "+round(Double.parseDouble(response.getCc_fee())));
								total.setText(getResources().getString(R.string.currencuSymbol)+" "+round(Double.parseDouble(response.getAmount())));
								
							} catch (Exception e) {
								// TODO: handle exception
								Duration.setText(response.getDur());
							}
							
							/*YoYo.with(Techniques.ZoomIn)
							.duration(700)
							.playOn(findViewById(R.id.rl_invoice_details));*/
							
						}
						else
						{
							Toast.makeText(InvoicePaymentDone.this, response.getErrMsg(),Toast.LENGTH_SHORT).show();
						}
					}
					else
					{
						Toast.makeText(InvoicePaymentDone.this, getResources().getString(R.string.server_error),Toast.LENGTH_SHORT).show();
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

	public static double round(double value, int numberOfDigitsAfterDecimalPoint)
	{
		BigDecimal bigDecimal = new BigDecimal(value);
		bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint,BigDecimal.ROUND_HALF_UP);
		return bigDecimal.doubleValue();
	}
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		
		if(v.getId()==R.id.rl_invoice)
		{
			finish();
			overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
		}
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
	
	

}
