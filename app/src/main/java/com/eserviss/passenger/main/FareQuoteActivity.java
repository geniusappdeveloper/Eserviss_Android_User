package com.eserviss.passenger.main;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.eserviss.passenger.pojo.FareCalculation;
import com.egnyt.eserviss.R;
import com.threembed.utilities.GpsListener;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

public class FareQuoteActivity extends Activity implements OnClickListener
{
	private String mPICKUP_ADDRESS,mDROPOFF_ADDRESS ;
	private TextView pickup_address,dropoff_address;
	private RelativeLayout back;
	RelativeLayout relative_fare_quote;
	private TextView Fare_Amount,Current_Distance,Dropoff_Distance;
	private String getFareResponse,car_type_id;
	private double currentLatitude,currentLongitude;
	private FareCalculation getFare;
	private String from_latitude,from_longitude,to_latitude,to_longitude;
	private RelativeLayout Relative_Pickup_Location,Relative_Drop_Location;  //RL_fare_quote,
	private SessionManager session;
	private RelativeLayout fare_quote_details;
	private Button enterDestiny,btnConfirm;
	LinearLayout destinylay;

	TextView txt_fare;
	//=======My Change==============
	TextView txt_dropoff_location,txt_pickup_location;
	//=========My Change======
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fare_quote);
		
		GpsListener gpsListener = new GpsListener(FareQuoteActivity.this);
		Location location = gpsListener.getLatLng();
		currentLatitude = location.getLatitude();
		currentLongitude = location.getLongitude();
		
		initialize();
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if(extras!=null)
		{
			 mPICKUP_ADDRESS = extras.getString("PICKUP_ADDRESS");
			 mDROPOFF_ADDRESS = extras.getString("DROPOFF_ADDRESS");
			 from_latitude = extras.getString("FromLatitude");
			 from_longitude = extras.getString("FromLongitude");
			 to_latitude = extras.getString("ToLatitude");
			 to_longitude = extras.getString("ToLongitude");
			 car_type_id = extras.getString("TypeId");
		}
		
		pickup_address.setText(mPICKUP_ADDRESS);
		dropoff_address.setText(mDROPOFF_ADDRESS);

		getUserProfile();
		
	}
	
	private void initialize()
	{
		session = new SessionManager(FareQuoteActivity.this);
		pickup_address=(TextView)findViewById(R.id.pickup_location_address);
		dropoff_address=(TextView)findViewById(R.id.dropoff_location_address);
		back=(RelativeLayout)findViewById(R.id.fare_quote_back);
		Fare_Amount = (TextView)findViewById(R.id.txt_fare_amount);
		Current_Distance = (TextView)findViewById(R.id.current_distance);
		Dropoff_Distance = (TextView)findViewById(R.id.dropoff_distance);
		//RL_fare_quote = (RelativeLayout)findViewById(R.id.rl_fare_quote);

		Relative_Pickup_Location = (RelativeLayout)findViewById(R.id.relative_pickup_location);
		Relative_Drop_Location = (RelativeLayout)findViewById(R.id.relative_dropoff_location);
		enterDestiny=(Button) findViewById(R.id.enterDestiny);
		btnConfirm= (Button) findViewById( R.id.btnConfirm);
		destinylay= (LinearLayout) findViewById(R.id.destinylay);

		enterDestiny.setOnClickListener(this);
		btnConfirm.setOnClickListener(this);
		back.setOnClickListener(this);
		fare_quote_details=(RelativeLayout) findViewById(R.id.fare_quote_details);
		//==============================change April========================================
		txt_fare= (TextView) findViewById(R.id.txt_fare);
		txt_dropoff_location= (TextView) findViewById(R.id.txt_dropoff_location);
		txt_pickup_location= (TextView) findViewById(R.id.txt_pickup_location);

		Typeface roboto_condensed = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/BebasNeue.otf");
		pickup_address.setTypeface(roboto_condensed);
		dropoff_address.setTypeface(roboto_condensed);
		//back.setTypeface(roboto_condensed);
		Fare_Amount.setTypeface(roboto_condensed);
		Current_Distance.setTypeface(roboto_condensed);
		Dropoff_Distance.setTypeface(roboto_condensed);
		enterDestiny.setTypeface(roboto_condensed);
		btnConfirm.setTypeface(roboto_condensed);
		txt_fare.setTypeface(roboto_condensed);
		txt_dropoff_location.setTypeface(roboto_condensed);
		txt_pickup_location.setTypeface(roboto_condensed);

		//=====================================================================================
	}
	
	@Override
	public void onClick(View v) 
	{
		if(v.getId()==R.id.fare_quote_back)
		{
			 Intent returnIntent = new Intent();
			 
			 returnIntent.putExtra("FROM_LATITUDE",from_latitude);
			 returnIntent.putExtra("FROM_LONGITUDE",from_longitude);
			 returnIntent.putExtra("from_SearchAddress",mPICKUP_ADDRESS);
			 returnIntent.putExtra("TO_LATITUDE",to_latitude);
			 returnIntent.putExtra("TO_LONGITUDE",to_longitude);
			 returnIntent.putExtra("to_SearchAddress",mDROPOFF_ADDRESS);
			 
			 setResult(RESULT_OK,returnIntent);     
			 finish();
	    	 overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
	    	 
		}
		
		if(v.getId()==R.id.relative_pickup_location)
		{
			Intent addressIntent=new Intent(FareQuoteActivity.this, SearchAddressGooglePlacesActivity.class);
			addressIntent.putExtra("chooser", "Pickup Location");
			startActivityForResult(addressIntent, 1);
			overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			return;
		}
		
		if(v.getId()==R.id.relative_dropoff_location)
		{
			Intent addressIntent=new Intent(FareQuoteActivity.this, SearchAddressGooglePlacesActivity.class);
			addressIntent.putExtra("chooser", "Dropoff Location");
			startActivityForResult(addressIntent, 2);
			overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			return;
		}
		
		if(v.getId()==R.id.enterDestiny)
		{
			Intent addressIntent=new Intent(FareQuoteActivity.this, SearchAddressGooglePlacesActivity.class);
			addressIntent.putExtra("chooser", "Dropoff Location");
			startActivityForResult(addressIntent, 2);
			overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			return;
		}
		if(v.getId()==R.id.btnConfirm)
		{
			Intent returnIntent = new Intent();

			returnIntent.putExtra("FROM_LATITUDE",from_latitude);
			returnIntent.putExtra("FROM_LONGITUDE",from_longitude);
			returnIntent.putExtra("from_SearchAddress",mPICKUP_ADDRESS);
			returnIntent.putExtra("TO_LATITUDE",to_latitude);
			returnIntent.putExtra("TO_LONGITUDE",to_longitude);
			returnIntent.putExtra("to_SearchAddress",mDROPOFF_ADDRESS);

			setResult(RESULT_OK,returnIntent);
			finish();
			overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
	
		Utility.printLog("inside onActivityResult requestCode="+requestCode);
		
		if(requestCode==1)
		{
			if(resultCode == Activity.RESULT_OK)
			{
			String latitudeString=data.getStringExtra("LATITUDE_SEARCH");
			String longitudeString=data.getStringExtra("LONGITUDE_SEARCH");
			String searchAddress=data.getStringExtra("SearchAddress");
			
			Utility.printLog("theja fare_quote pick_up fare1="+latitudeString+" "+longitudeString); 
			
			from_latitude=latitudeString;
			from_longitude=longitudeString;
			mPICKUP_ADDRESS=searchAddress;
			pickup_address.setText(searchAddress);
			
			Utility.printLog("theja fare_quote pick_up fare2="+from_latitude+" "+from_longitude); 
			getUserProfile();
			}
		}
		if(requestCode==2)
		{
			if(resultCode == Activity.RESULT_OK)
			{
			String latitudeString=data.getStringExtra("LATITUDE_SEARCH");
			String longitudeString=data.getStringExtra("LONGITUDE_SEARCH");
			String searchAddress=data.getStringExtra("SearchAddress");
			
			to_latitude=latitudeString;
			to_longitude=longitudeString;
			mDROPOFF_ADDRESS=searchAddress;
			dropoff_address.setText(searchAddress);
			
			getUserProfile();
			}
		}
	}
	
	private void getUserProfile()
	{
		final ProgressDialog dialogL;
		dialogL= Utility.GetProcessDialog(FareQuoteActivity.this);

		if (dialogL!=null) {
			dialogL.show();
		}

		RequestQueue volleyRequest = Volley.newRequestQueue(FareQuoteActivity.this);

		StringRequest myReq = new StringRequest(Request.Method.POST,VariableConstants.BASE_URL+"fareCalculator",
				new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub

				getFareResponse = response;
				Utility.printLog("Success of getting user Info"+response);

				getUserInfo(dialogL);

			}
		}, 	new Response.ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error)
			{
				// TODO Auto-generated method stub
				dialogL.dismiss();
				Toast.makeText(FareQuoteActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();

			}
		}){  
			protected HashMap<String,String> getParams() throws com.android.volley.AuthFailureError {  
				HashMap<String,String> kvPair = new HashMap<String, String>(); 
				
				SessionManager session=new SessionManager(FareQuoteActivity.this);

				Utility utility=new Utility();
				String curenttime=utility.getCurrentGmtTime();
				Utility.printLog("dataandTime "+curenttime);
				
				Utility.printLog("FareQuoteActivity currentLatitude="+currentLatitude+" currentLongitude="+currentLongitude);

				Utility.printLog("FareQuoteActivity from_latitude="+from_latitude+" from_longitude="+from_longitude+" to_latitude="+to_latitude+" to_longitude="+to_longitude);
				Utility.printLog("FareQuoteActivity getSessionToken="+session.getSessionToken());
				Utility.printLog("FareQuoteActivity getDeviceId="+session.getDeviceId());
				Utility.printLog("FareQuoteActivity dateandTime="+curenttime);
				Utility.printLog("FareQuoteActivity car_type_id="+car_type_id);
				
				kvPair.put("ent_sess_token",session.getSessionToken() );
				kvPair.put("ent_dev_id",session.getDeviceId());
				kvPair.put("ent_type_id",car_type_id);
				kvPair.put("ent_curr_lat",String.valueOf(currentLatitude));
				kvPair.put("ent_curr_long",String.valueOf(currentLongitude));
				kvPair.put("ent_from_lat",from_latitude);
				kvPair.put("ent_from_long",from_longitude);
				kvPair.put("ent_to_lat",to_latitude);
				kvPair.put("ent_to_long",to_longitude);
				kvPair.put("ent_date_time",curenttime);
				
				return kvPair;  
			};  

		};

		volleyRequest.add(myReq);
	}



	private void getUserInfo(ProgressDialog dialogL)
	{
		dialogL.dismiss();
		try
		{
			fare_quote_details.setVisibility(View.VISIBLE);
			destinylay.setVisibility(View.VISIBLE);
			JSONObject jsnResponse = new JSONObject(getFareResponse);

			String jsonErrorParsing = jsnResponse.getString("errFlag");

			Utility.printLog("jsonErrorParsing is ---> "+jsonErrorParsing);
			parseResponse();

			if(getFare!=null)
			{
				if(getFare.getErrFlag().equals("0"))
				{
					String cudis,dis;
					cudis=getFare.getCurDis();
					//cudis=round(Double.parseDouble(getFare.getCurDis())*0.621);
					//cudis=cudis.replace(" ", "\n");
					dis=getFare.getDis();
				   // dis=round(Double.parseDouble(getFare.getDis())*0.621);
					//dis=dis.replace(" ", "\n");
					Fare_Amount.setText(getResources().getString(R.string.currencuSymbol)+" "+round(Double.parseDouble(getFare.getFare())));
					Current_Distance.setText(cudis);
					Dropoff_Distance.setText(dis);
				}
				else
				{
					Toast.makeText(FareQuoteActivity.this, getFare.getErrMsg(),Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FareQuoteActivity.this);

				// set title
				alertDialogBuilder.setTitle(getResources().getString(R.string.error));

				// set dialog message
				alertDialogBuilder
				.setMessage(getResources().getString(R.string.server_error))
				.setCancelable(false)

				.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.dismiss();
					}
				});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
				alertDialog.show();
			}
		}
		catch(JSONException e)
		{
			Utility.printLog("exp "+e,"");
			e.printStackTrace();
			Utility.ShowAlert("Server error!!", FareQuoteActivity.this);
		}
	}

	private void parseResponse()
	{
		Utility.printLog("getFare parseResponse  " + getFareResponse);
		Gson gson = new Gson();
		getFare = gson.fromJson(getFareResponse, FareCalculation.class);
	}
	
	private String round(double value)
	{
		String s = String.format("%.2f", value);
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
