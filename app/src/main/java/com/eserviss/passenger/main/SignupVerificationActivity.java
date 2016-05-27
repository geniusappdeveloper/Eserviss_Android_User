package com.eserviss.passenger.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;

import com.egnyt.eserviss.MainActivity;
import com.egnyt.eserviss.R;
import com.eserviss.passenger.pojo.UploadImgeResponse;
import com.eserviss.passenger.pojo.ValidVerificationCodeResponse;
import com.eserviss.passenger.pojo.VerificationCodeResponse;
import com.google.gson.Gson;
import com.threembed.utilities.AppLocationService;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.UltilitiesDate;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupVerificationActivity extends Activity
{
	TextView verificationnumber_tv,verificationnumber_countrycode_tv;
	Button resend_verificationcode_bt,signup_back; 
	EditText et_verificationcode;
	String regid,deviceid,firstName,lastName,email,password,mobileNo,fullMobileNumber,gender;;
    Button signup_payment_skip;
	ProgressDialog dialogL;
	private double currentLatitude,currentLongitude;
	private File mFileTemp=SignupActivity.mFileTemp;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signup_verification_activity);
		
		AppLocationService appLocationService = new AppLocationService(SignupVerificationActivity.this);
		Location gpsLocation = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
		if(gpsLocation != null) 
		{
			currentLatitude = gpsLocation.getLatitude();
			currentLongitude = gpsLocation.getLongitude();
		} 
		else 
		{
			Location nwLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);

			if(nwLocation != null) 
			{
				currentLatitude = nwLocation.getLatitude();
				currentLongitude = nwLocation.getLongitude();
			} 
		}
		Bundle bundle=getIntent().getExtras();
		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		if(bundle!=null)
		{
			regid=bundle.getString("REGID");
			deviceid=bundle.getString("DEVICEID");
			firstName=bundle.getString("FIRSTNAME");
			lastName=bundle.getString("LASTNAME");
			email=bundle.getString("EMAIL");
			password=bundle.getString("PASSWORD");
			mobileNo=bundle.getString("MOBILE");
			gender = bundle.getString("gender");
			Log.i("signupverifi","gender "+gender);
				
		}
		initializations();
				
	}
	
	void initializations()
	{
		verificationnumber_tv=(TextView)findViewById(R.id.verificationnumber_tv);
		verificationnumber_countrycode_tv=(TextView)findViewById(R.id.verificationnumber_countrycode_tv);
		resend_verificationcode_bt=(Button)findViewById(R.id.resend_verificationcode_bt);
		et_verificationcode=(EditText)findViewById(R.id.et_verificationcode);
		signup_back=(Button) findViewById(R.id.signup_back);
		verificationnumber_tv.setText(mobileNo);
		verificationnumber_countrycode_tv.setText("+91");
		signup_payment_skip=(Button) findViewById(R.id.signup_payment_skip);
		
		et_verificationcode.setFocusableInTouchMode(true);
		et_verificationcode.setFocusable(true);
		et_verificationcode.requestFocus();
		signup_payment_skip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				if(et_verificationcode.getText().toString().equals(""))
				{
					Utility.ShowAlert(getResources().getString(R.string.enter_verificationcode), SignupVerificationActivity.this);

				}
				else
				{
					Utility.ShowAlert(getResources().getString(R.string.enter_correct_verification), SignupVerificationActivity.this);

				}
			}
		});
		
		
		/*String str=Utility.getUserCountry(SignupVerificationActivity.this) ; 
		
		Utility.printLog("The value of the country code is :: "+str);
		Toast.makeText(SignupVerificationActivity.this,"The value of the country code is ::" +str, Toast.LENGTH_LONG).show();*/
		
		
		/*fullMobileNumber=verificationnumber_countrycode_tv.getText().toString()+verificationnumber_tv.getText().toString();
		
		
		
		Utility.printLog("the verification code number is  :: "+verificationnumber_countrycode_tv.getText().toString());
		
		Utility.printLog("the verification mobile number is  :: "+verificationnumber_tv.getText().toString());
		
		Utility.printLog("the full mobile number is  :: "+fullMobileNumber);*/
		fullMobileNumber = mobileNo;
		Utility.printLog("the full mobile number is  :: "+fullMobileNumber);
		/*et_verificationcode.setOnFocusChangeListener(new OnFocusChangeListener() 
		{
			
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				Utility.printLog("setOnFocusChangeListener hasFocus= "+hasFocus);
	            if(hasFocus) 
	            {
	            	
	            	if(et_verificationcode.getText().toString().length()==5)
	        		{
	        			Utility.printLog("The value is asdfsdf ::");
	        			
	        			new BackgroundForValid_VerificationCode().execute();
	        			
	        		}
	        		
	            }
			}
		});*/
	
		
		et_verificationcode.addTextChangedListener(new TextWatcher()
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				
				Utility.printLog(" inside onTextChanged  :: "+count);
				
				
			count=s.length();
			
			Utility.printLog(" inside onTextChanged  s.length() :: "+count);
				
				
				if(count==5)
        		{
        			Utility.printLog("inside onTextChanged  length is 5  ::");
        			
        			new BackgroundForValid_VerificationCode().execute();
        			
        		}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after)
			{
				
				Utility.printLog(" inside beforeTextChanged  :: "+count);
				
				count=s.length();
				
				Utility.printLog(" inside beforeTextChanged  s.length() :: "+count);
						
			}
			@Override
			public void afterTextChanged(Editable s) 
			{
				
				Utility.printLog(" inside afterTextChanged   :: "+s);
				
				Utility.printLog(" inside afterTextChanged  s.length() :: "+s.length());
				
			}
		});
		
		
		
		resend_verificationcode_bt.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				
				new BackgroundForGettingVerificationCode().execute();
				
			}
		});
		
		signup_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(SignupVerificationActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});
		
	}
	class BackgroundForGettingVerificationCode extends AsyncTask<String,Void,String>
	{
		VerificationCodeResponse verificationResponse;
		ProgressDialog dialogL;
		
		protected void onPreExecute() 
		{
			super.onPreExecute();
			dialogL=Utility.GetProcessDialogNew(SignupVerificationActivity.this,"Wait for verification code...");
			dialogL.setCancelable(true);
			if (dialogL!=null) 
			{
				dialogL.setCancelable(false);
				dialogL.show();
			}
		}
		@Override
		protected String doInBackground(String... params) 
		{
			String url=VariableConstants.BASE_URL+"getVerificationCode";
			
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			Log.i("","dateandTime "+curenttime);
			
			Map<String, String> kvPairs = new HashMap<String, String>();
			
			kvPairs.put("ent_mobile","+91"+fullMobileNumber);
			
			Utility.printLog("The kvPair values in verification code is :: "+kvPairs);
			
			HttpResponse httpResponse = null;
			try 
			{
				httpResponse = Utility.doPost(url,kvPairs);
			}
			catch (ClientProtocolException e1) 
			{
				e1.printStackTrace();
				Utility.printLog( "doPost Exception......."+e1);
			} 
			catch (IOException e1)
			{
				e1.printStackTrace();
				Utility.printLog( "doPost Exception......."+e1);
			}
			
			String jsonResponse = null;
			if (httpResponse!=null)
			{

				try
				{
					jsonResponse = EntityUtils.toString(httpResponse.getEntity());
					Utility.printLog("getVerification code Response: "+jsonResponse);
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
			try
			{
				
				if(jsonResponse!=null) 
				{
					Gson gson = new Gson();
					verificationResponse=gson.fromJson(jsonResponse,VerificationCodeResponse.class);
					Log.i("","DONE WITH GSON");
				}
				else
				{
					runOnUiThread(new Runnable()
					{
						public void run() 
						{
							Toast.makeText(getApplicationContext(),getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
			catch(Exception e)
			{
				Utility.ShowAlert(getResources().getString(R.string.server_error), SignupVerificationActivity.this);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			if(dialogL!=null) 
			{
				dialogL.dismiss();
			}
			if(verificationResponse!=null)
			{
				if(verificationResponse.getErrFlag().equals("0"))
				{
					
					showAlert(verificationResponse.getErrMsg());
					
				}
				else
				{
					showAlert(verificationResponse.getErrMsg());
				}
			}
			else
			{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupVerificationActivity.this);
						// set title
						alertDialogBuilder.setTitle(getResources().getString(R.string.alert));
						// set dialog message
						alertDialogBuilder
							.setMessage(getResources().getString(R.string.server_error))
							.setCancelable(false)
							
							.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog,int id) 
								{
									// if this button is clicked, just close
									// the dialog box and do nothing
									finish();
								}
							});
							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
							// show it
							alertDialog.show();
				}
		}
	}
	
	class BackgroundForValid_VerificationCode extends AsyncTask<String,Void,String>
	{
		ValidVerificationCodeResponse response;
		ProgressDialog dialogL;
		
		protected void onPreExecute() 
		{
			super.onPreExecute();
			dialogL=Utility.GetProcessDialogNew(SignupVerificationActivity.this,"Validating verification code...");
			dialogL.setCancelable(true);
			if (dialogL!=null) 
			{
				dialogL.setCancelable(false);
				dialogL.show();
			}
		}
		@Override
		protected String doInBackground(String... params) 
		{
			String url=VariableConstants.BASE_URL+"verifyPhone";
			
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			Log.i("","dateandTime "+curenttime);
			
			Map<String, String> kvPairs = new HashMap<String, String>();
			
			kvPairs.put("ent_phone","+91"+fullMobileNumber);
			kvPairs.put("ent_sess_token",regid);
			kvPairs.put("ent_dev_id", deviceid);
			kvPairs.put("ent_code",et_verificationcode.getText().toString());
			
			Utility.printLog("The kvPair values in Valid Verification Code Activity is :: "+kvPairs);
			
			HttpResponse httpResponse = null;
			try 
			{
				httpResponse = Utility.doPost(url,kvPairs);
			}
			catch (ClientProtocolException e1) 
			{
				e1.printStackTrace();
				Utility.printLog( "doPost Exception......."+e1);
			} 
			catch (IOException e1)
			{
				e1.printStackTrace();
				Utility.printLog( "doPost Exception......."+e1);
			}
			
			String jsonResponse = null;
			if (httpResponse!=null)
			{

				try
				{
					jsonResponse = EntityUtils.toString(httpResponse.getEntity());
					Utility.printLog("Check Valid verification Code Response: "+jsonResponse);
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
			try
			{
				
				if(jsonResponse!=null) 
				{
					Gson gson = new Gson();
					response=gson.fromJson(jsonResponse,ValidVerificationCodeResponse.class);
					Log.i("","DONE WITH GSON");
				}
				else
				{
					runOnUiThread(new Runnable()
					{
						public void run() 
						{
							Toast.makeText(getApplicationContext(),getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
						}
					});
				}
			
				
			}
			catch(Exception e)
			{
				Utility.ShowAlert(getResources().getString(R.string.server_error), SignupVerificationActivity.this);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			if(dialogL!=null) 
			{
				dialogL.dismiss();
			}
			if(response!=null)
			{
				
				
				if(response.getErrFlag().equals("0"))
				{
					
				
					
				/*	Intent intent=new Intent(SignupVerificationActivity.this,SignupVerificationActivity.class);
					intent.putExtra("REGID",regid);
					intent.putExtra("DEVICEID",deviceid);
					
					Utility.printLog("Before sendin deviceId"+deviceid);
					Utility.printLog("Before sendin regId"+regid);
					intent.putExtra("FIRSTNAME",firstName);
					
					if(lastName!=null)
						intent.putExtra("LASTNAME",lastName);
					
					intent.putExtra("EMAIL",email);
					intent.putExtra("PASSWORD",password);
					intent.putExtra("MOBILE",mobileNo);
					
					startActivity(intent);
					SignupVerificationActivity.this.finish();
					overridePendingTransition(R.anim.anim_two, R.anim.anim_one);*/
					if(Utility.isNetworkAvailable(SignupVerificationActivity.this))
					
						new BackgroundTaskSignUp().execute();
					
					else
						Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SignupVerificationActivity.this);
				}
				else
				{
					showAlert(response.getErrMsg());
					et_verificationcode.setText(null);
				}
			
				
				}
			else
			{
				
				
				
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupVerificationActivity.this);
			 
						// set title
						alertDialogBuilder.setTitle(getResources().getString(R.string.alert));
			 
						// set dialog message
						alertDialogBuilder
							.setMessage(getResources().getString(R.string.server_error))
							.setCancelable(false)
							
							.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog,int id) 
								{
									// if this button is clicked, just close
									// the dialog box and do nothing
									finish();
								}
							});
							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
							// show it
							alertDialog.show();
				 
				
				}
		}
	}
	
	
	
	private void showAlert(String message) 
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		
		// set title
		alertDialogBuilder.setTitle(getResources().getString(R.string.alert));
		
		// set dialog message
		alertDialogBuilder
			.setMessage(message)
			.setCancelable(false)
			.setNegativeButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					//closing the application
					dialog.dismiss();
				}
			});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}
	
	private class BackgroundTaskSignUp extends AsyncTask<String, Void, String>
	{
		SignUpResponse response;
		String jsonResponse;

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			dialogL= Utility.GetProcessDialogNew(SignupVerificationActivity.this,getResources().getString(R.string.signing_you_up));
			dialogL.setCancelable(true);
			if(dialogL!=null) 
			{
				dialogL.setCancelable(false);
				dialogL.show();
			}
		}

		@Override
		protected String doInBackground(String... params) 
		{
			Map<String, String> kvPairs = new HashMap<String, String>();

			String url=VariableConstants.BASE_URL+"slaveSignup";

			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();

			//Utility.printLog("BackgroundTaskSignUp currentLatitude="+currentLatitude+" currentLongitude="+currentLongitude);


			/*if(access_token!=null)
				Utility.printLog("ent_token",access_token);
*/
			kvPairs.put("ent_first_name",firstName);
			kvPairs.put("ent_last_name",lastName);
			kvPairs.put("ent_email",email);
			kvPairs.put("ent_password",password);
			kvPairs.put("ent_gender",gender);
			kvPairs.put("ent_mobile","+91"+fullMobileNumber);//fullMobileNumber mobileNo
			//kvPairs.put("ent_city",current_city_name);
			kvPairs.put("ent_latitude",String.valueOf(currentLatitude));
			kvPairs.put("ent_longitude",String.valueOf(currentLongitude));
			//kvPairs.put("ent_latitude","13.02882275");
			//kvPairs.put("ent_longitude","77.58966314");
			/*if(access_token!=null)
				kvPairs.put("ent_token",access_token);
			*/
			/*if(referalCode!=null)
				kvPairs.put("ent_referral_code",referalCode);
			*/
			kvPairs.put("ent_terms_cond","true");
			kvPairs.put("ent_pricing_cond","true");
			kvPairs.put("ent_dev_id",deviceid);
			kvPairs.put("ent_push_token",regid);
			kvPairs.put("ent_device_type","2");
			kvPairs.put("ent_date_time",curenttime);
            Utility.printLog("akbar params"+kvPairs);
            Log.i("akbar", "param "+kvPairs);
			HttpResponse httpResponse = null;
			try {
				httpResponse = Utility.doPost(url,kvPairs);
			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
				Utility.printLog("doPost Exception......."+e1);
			} catch (IOException e1) {
				e1.printStackTrace();
				Utility.printLog("doPost Exception......."+e1);
			}

			if (httpResponse!=null) {

				try {
					jsonResponse = EntityUtils.toString(httpResponse.getEntity());
					Utility.printLog("SignUp Response: "+jsonResponse);
				} catch (ParseException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utility.printLog("SignUp Response doPost ParseException......."+e);
				}
				catch(IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utility.printLog("SignUp Response doPost IOException......."+e);
				}
			}

			if (jsonResponse!=null) 
			{
				Gson gson = new Gson();
				response=gson.fromJson(jsonResponse, SignUpResponse.class);
			}else
			{
				runOnUiThread(new Runnable()
				{
					public void run() 
					{
						Toast.makeText(SignupVerificationActivity.this,getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
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

			Utility.printLog("SignUp Response onPostExecute"+jsonResponse);

			if(response!=null)
			{
				if(response.getErrFlag().equals("0"))
				{
					SessionManager session=new SessionManager(SignupVerificationActivity.this);
					if(response.getToken()!=null)
						session.storeSessionToken(response.getToken());
					session.storeRegistrationId(regid);
					session.storeLoginId(email);
					session.storeDeviceId(deviceid);
					session.setIsLogin(true);
					session.storeServerChannel(response.getServerChn());
					//session.storeLoginResponse(jsonResponse);
					//session.storeCarTypes(jsonResponse);
					session.storeChannelName(response.getChn());
					session.storeCouponCode(response.getCoupon());

					if(Utility.isNetworkAvailable(SignupVerificationActivity.this))
					{
						new BackGroundTaskForUploadImage().execute();
					}
					else
					{
						Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SignupVerificationActivity.this);
					}
				}
				else
				{
					Toast.makeText(SignupVerificationActivity.this,response.getErrMsg(), Toast.LENGTH_SHORT).show();
					Utility.printLog("","PROBLEM : errorNumb : "+response.getErrNum());

					if(dialogL!=null) 
					{
						dialogL.dismiss();
					}
				}
			}
			else
			{
				Toast.makeText(SignupVerificationActivity.this,getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
				if(dialogL!=null) 
				{
					dialogL.dismiss();
				}
			}
		}
	}

	//-----------------------------------------------------------------------------------------------------//
	private class  BackGroundTaskForUploadImage extends AsyncTask<String, Void, String>
	{
		private long chunkLength=1000*1024;
		private long totalBytesRead=0;
		private long bytesRemaining ;
		private long FILE_SIZE ;
		private String fileName;
		UploadImgeResponse response;
		private List<NameValuePair>uploadNameValuePairList;
		File mFile;

		@Override
		protected String doInBackground(String... params) 
		{
			// TODO Auto-generated method stub
     Utility.printLog("helloooooo");
			FileInputStream fin = null;
			totalBytesRead = 0;
			bytesRemaining=0;

			//mFile=new File("/sdcard"+"/SneekPeek", "picture"+".jpg");
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) 
			{     

				//mFile= new File(Environment.getExternalStorageDirectory(), VariableConstants.TEMP_PHOTO_FILE_NAME);
				mFile=mFileTemp;
				Utility.printLog("helloooooo file"+mFile);
			}
			else 
			{ 
				//mFile= new File(getFilesDir(),VariableConstants.TEMP_PHOTO_FILE_NAME);
				mFile=mFileTemp;
				Utility.printLog("helloooooo file"+mFile);
			}


			String temp= Utility.getCurrentDateTimeStringGMT();
			temp=new String(temp.trim().replace(" ", "20"));
			temp=new String(temp.trim().replace(":", "20"));
			temp=new String(temp.trim().replace("-", "20"));

			fileName="PA"+firstName+temp+mFile.getName();

			try 
			{
				fin= new FileInputStream(mFile);
			}
			catch (FileNotFoundException e2) 
			{
				e2.printStackTrace();
			}

			if(mFile.isFile() && mFile.length()>0) 
			{
				FILE_SIZE = mFile.length();
			}


			Utility.printLog("BackGroundTaskForUploadImage doInBackground  FILE_SIZE "+FILE_SIZE);


			while (totalBytesRead < FILE_SIZE ) 
			{
				try 
				{
					bytesRemaining = FILE_SIZE-totalBytesRead;

					if ( bytesRemaining < chunkLength ) // Remaining Data Part is Smaller Than CHUNK_SIZE
					{
						chunkLength = bytesRemaining;
					}

					byte []chunk = null;
					chunk=new byte[(int) chunkLength];

					byte fileContent[] = new byte[(int) chunkLength];
					try 
					{

						fin.read(fileContent,0,(int)chunkLength);

					} 
					catch (FileNotFoundException e1) 
					{
						e1.printStackTrace();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}

					System.arraycopy(fileContent, 0, chunk, 0, (int) chunkLength);
					//byte[] encoded = Base64.encodeBase64(chunk);
					byte[] encoded = Base64.encode(chunk, Base64.NO_WRAP);
					String encodedString = new String(encoded);

					Utility.printLog("Base 64: "+encodedString);

					SessionManager session=new SessionManager(SignupVerificationActivity.this);
					String sessiontoken=session.getSessionToken();

					Utility utility=new Utility();
					String curenttime=utility.getCurrentGmtTime();
					String dateandTime=UltilitiesDate.getLocalTime(curenttime);
					Utility.printLog("","dataandTime "+curenttime);

					String [] uploadParameter={sessiontoken,session.getDeviceId(),fileName,encodedString,"2","1","1",curenttime};


					// String [] uploadParameter={sessiontoken,deviceid,fileName,encodedString,"2","1","Submit"};
					//  com.threembed.utilities.Utility utility=new com.threembed.utilities.Utility();

					uploadNameValuePairList=utility.getUploadParameter(uploadParameter);
					//String url=com.threembed.utilites.Constants.reqUrl+"sendASnap_result.php?user_session_token="+userSession+"&owner_id="+userId+"&media_name="+fileName+"&media_chunk="+encodedString+"&upload_media_submit=Upload";
					//Utility.printLog("", "url to send :"+url);

					totalBytesRead=totalBytesRead+chunkLength;

					String result=utility.makeHttpRequest(VariableConstants.BASE_URL+"uploadImage","POST",uploadNameValuePairList);

					Utility.printLog("IMAGE UPLOAD",result);
					Utility.printLog("UPLOAD", "RESPONSE:  : "+result);
					Utility.printLog("IMAGE UPLOAD",result);

					if(result!=null)
					{
						Gson gson = new Gson();
						response=gson.fromJson(result, UploadImgeResponse.class);
					}

				}
				catch (Exception e) 
				{

				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) 
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if(dialogL!=null) 
			{
				dialogL.dismiss();
			}
			
			if(response!=null)
			{
				if(response.getErrFlag().equals("0"))
				{
					Utility.printLog("m inside and going to next "+response.getErrFlag());
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.signup_success), Toast.LENGTH_SHORT).show();
					SessionManager session=new SessionManager(SignupVerificationActivity.this);
					session.setIsLogin(true);
					//Move to map activity
					Intent intent=new Intent(SignupVerificationActivity.this,ResideMenuActivity.class);//SignupPayment.class
					startActivity(intent);
					finish();
					overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
				}
				else
				{
					//Utility.ShowAlert(response.getErrMsg(), SignupVerificationActivity.this);
					//showDialog(response.getErrFlag(), SignupVerificationActivity.this);
					try{
					showAlert(response.getErrMsg());
					}
					catch(Exception e)
					{
						Utility.printLog("catchblock "+e);
						e.printStackTrace();
						
					}

					Toast.makeText(getApplicationContext(), getResources().getString(R.string.signup_success), Toast.LENGTH_SHORT).show();
					SessionManager session=new SessionManager(SignupVerificationActivity.this);
					session.setIsLogin(true);
					//Move to map activity
					Intent intent=new Intent(SignupVerificationActivity.this,ResideMenuActivity.class);
					startActivity(intent);
					finish();
					overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
				}
			}
			else
			{
				//Utility.ShowAlert(response.getErrMsg(), SignupVerificationActivity.this);
				try{
					showAlert(response.getErrMsg());
					}
					catch(Exception e)
					{
						Utility.printLog("catchblock "+e);
						e.printStackTrace();
						
					}

			//	showAlert(response.getErrMsg());

				Toast.makeText(getApplicationContext(), getResources().getString(R.string.signup_success), Toast.LENGTH_SHORT).show();
				SessionManager session=new SessionManager(SignupVerificationActivity.this);
				session.setIsLogin(true);
				//Move to map activity
				Intent intent=new Intent(SignupVerificationActivity.this,ResideMenuActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			}
		}
	}
	
}
