package com.eserviss.passenger.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.eserviss.passenger.pojo.LoginResponse;
import com.egnyt.eserviss.R;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;

public class HomePagePopUp extends Activity implements OnTouchListener,OnClickListener
{
	private RelativeLayout relativeLayout;
	private LoginResponse response;
	private int i=0;

	private String Base_Fare0,Base_Fare_per_Min0,Base_Fare_per_Km0,Base_Fare1,Base_Fare_per_Min1,Base_Fare_per_Km1,Base_Fare2,Base_Fare_per_Min2,Base_Fare_per_Km2,Base_Fare3,Base_Fare_per_Min3,Base_Fare_per_Km3,ServiceType;
	private String ETA,Min_Fare0,Max_Size0,Min_Fare1,Max_Size1,Min_Fare2,Max_Size2,Min_Fare3,Max_Size3,distance;
	private ImageButton cancel;
	private SessionManager session;

	private TextView base_fair,base_fair_per_min,base_fair_per_km,serviceType;
	private TextView eta,min_fair,max_size;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_details);
		
		session = new SessionManager(HomePagePopUp.this);
		
        Utility.printLog("HomePagePopUp  onCreate ");
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if(extras!=null) 
		{
			i = extras.getInt("chooser", 0);
			distance = extras.getString("DISTANCE");
			Base_Fare0=extras.getString("BASEFARE0");
			Base_Fare1=extras.getString("BASEFARE1");
			Base_Fare2=extras.getString("BASEFARE2");
			Base_Fare3=extras.getString("BASEFARE3");
			Base_Fare_per_Km0=extras.getString("BASEFARE0");
			Base_Fare_per_Km1=extras.getString("BASEFARE1");
			Base_Fare_per_Km2=extras.getString("BASEFARE2");
			Base_Fare_per_Km3=extras.getString("BASEFARE3");
			Base_Fare_per_Min0=extras.getString("BASEFARE0");
			Base_Fare_per_Min1=extras.getString("BASEFARE1");
			Base_Fare_per_Min2=extras.getString("BASEFARE2");
			Base_Fare_per_Min3=extras.getString("BASEFARE3");
			Min_Fare0=extras.getString("MINFARE0");
			Min_Fare1=extras.getString("MINFARE1");
			Min_Fare2=extras.getString("MINFARE2");
			Min_Fare3=extras.getString("MINFARE3");
			Max_Size0=extras.getString("MAXSIZE0");
			Max_Size1=extras.getString("MAXSIZE1");
			Max_Size2=extras.getString("MAXSIZE2");
			Max_Size3=extras.getString("MAXSIZE3");
			Utility.printLog("index="+i);
			Utility.printLog("params from homepage"+Base_Fare0+" "+Base_Fare_per_Min0+" "+Base_Fare_per_Km0+" "+Min_Fare0+" "+Max_Size0);
		}
		else 
		{

		}
		
		SessionManager session = new SessionManager(HomePagePopUp.this);
		String jsonResponse=session.getCarTypes();

		Gson gson = new Gson();
		response=gson.fromJson(jsonResponse, LoginResponse.class);		

		initialize();
		initializeValues(i);
		relativeLayout = (RelativeLayout)findViewById(R.id.alertDialog);
		relativeLayout.setOnTouchListener(this);
	}

	private void initialize()
	{
		base_fair = (TextView) findViewById(R.id.base_fair);
		base_fair_per_min = (TextView) findViewById(R.id.base_fair_min);
		base_fair_per_km = (TextView) findViewById(R.id.base_fair_km);
		serviceType = (TextView) findViewById(R.id.service_type);
		eta = (TextView) findViewById(R.id.eta_time);
		min_fair = (TextView) findViewById(R.id.min_fair_amount);
		max_size = (TextView) findViewById(R.id.max_size_num);
		cancel = (ImageButton) findViewById(R.id.service_type_cancel);

		//==============================change April========================================
		Typeface roboto_condensed = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/BebasNeue.otf");
		base_fair.setTypeface(roboto_condensed);
		base_fair_per_min.setTypeface(roboto_condensed);
		base_fair_per_km.setTypeface(roboto_condensed);
		serviceType.setTypeface(roboto_condensed);
		eta.setTypeface(roboto_condensed);
		min_fair.setTypeface(roboto_condensed);
		max_size.setTypeface(roboto_condensed);
		//=========================================================================




		cancel.setVisibility(View.GONE);
		cancel.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				 Intent returnIntent = new Intent();
				 setResult(RESULT_OK,returnIntent);     
				 finish();
			}
		});
	}
	private void initializeValues(int i)
	{
		Utility.printLog("size="+response.getCarsdetails().size());
		//Base_Fair=response.getCarsdetails().get(i).getBasefare();
		Utility.printLog("Base_Fair="+Base_Fare0);
		//Base_Fair_per_Min=response.getCarsdetails().get(i).getPrice_per_min();
		//Base_Fair_per_Km=response.getCarsdetails().get(i).getPrice_per_km();
		ServiceType = response.getCarsdetails().get(i).getType_name();
		//Min_Fair=response.getCarsdetails().get(i).getMin_fare();
		//Max_Size=response.getCarsdetails().get(i).getMax_size();

		if(i==0)
		{
			base_fair.setText(getResources().getString(R.string.currencuSymbol)+" " +Base_Fare0+ " Base fare");
			base_fair_per_min.setText(getResources().getString(R.string.currencuSymbol)+" " +Base_Fare_per_Min0+ "/Min and");
			base_fair_per_km.setText(getResources().getString(R.string.currencuSymbol)+" " +Base_Fare_per_Km0+ "/"+getResources().getString(R.string.distanceUnit));
			min_fair.setText(getResources().getString(R.string.currencuSymbol)+" "+Min_Fare0);
			max_size.setText(Max_Size0+" ppl");
		}

		else if(i==1)
		{
			base_fair.setText(getResources().getString(R.string.currencuSymbol)+" " +Base_Fare1+ " Base fare");
			base_fair_per_min.setText(getResources().getString(R.string.currencuSymbol)+" " +Base_Fare_per_Min1+ "/Min and");
			base_fair_per_km.setText(getResources().getString(R.string.currencuSymbol)+" " +Base_Fare_per_Km1+ "/"+getResources().getString(R.string.distanceUnit));
			min_fair.setText(getResources().getString(R.string.currencuSymbol)+" "+Min_Fare1);
			max_size.setText(Max_Size1+" ppl");
		}

		else if(i==2)
		{
			base_fair.setText(getResources().getString(R.string.currencuSymbol)+" " +Base_Fare2+ " Base fare");
			base_fair_per_min.setText(getResources().getString(R.string.currencuSymbol)+" " +Base_Fare_per_Min2+ "/Min and");
			base_fair_per_km.setText(getResources().getString(R.string.currencuSymbol)+" " +Base_Fare_per_Km2+ "/"+getResources().getString(R.string.distanceUnit));
			min_fair.setText(getResources().getString(R.string.currencuSymbol)+" "+Min_Fare2);
			max_size.setText(Max_Size2+" ppl");
		}

		else if(i==3)
		{
			base_fair.setText(getResources().getString(R.string.currencuSymbol)+" " +Base_Fare3+ " Base fare");
			base_fair_per_min.setText(getResources().getString(R.string.currencuSymbol)+" " +Base_Fare_per_Min3+ "/Min and");
			base_fair_per_km.setText(getResources().getString(R.string.currencuSymbol)+" " +Base_Fare_per_Km3+ "/"+getResources().getString(R.string.distanceUnit));
			min_fair.setText(getResources().getString(R.string.currencuSymbol)+" "+Min_Fare3);
			max_size.setText(Max_Size3+" ppl");
		}
		serviceType.setText(ServiceType);

		if(distance.equals("No Cabs"))
		{
			eta.setText(distance);

		}
		else
		{
			eta.setText(distance+" " +getResources().getString(R.string.distanceUnit));

		}
		
		//HomePageFragment.Car_Type=ServiceType;
		//HomePageFragment.Car_Type_Id=response.getCarsdetails().get(i).getType_id();

	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		// TODO Auto-generated method stub
		Intent returnIntent = new Intent();
		 setResult(RESULT_OK,returnIntent);     
		 finish();
		return false;
	}

	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		if(v.getId()==R.id.service_type_cancel)
		{
			Intent returnIntent = new Intent();
			 setResult(RESULT_OK,returnIntent);     
			 finish();
		}

	}

}