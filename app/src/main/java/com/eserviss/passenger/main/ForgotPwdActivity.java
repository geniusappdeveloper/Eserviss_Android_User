package com.eserviss.passenger.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;
import com.egnyt.eserviss.R;


public class ForgotPwdActivity extends Activity implements OnClickListener
{
	private EditText email;
	private Button submit;
	private RelativeLayout Back;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.forgot_pwd);
		intialize();
		
	}

	private void intialize() 
	{
		email=(EditText)findViewById(R.id.email_frgt_pwd);
		Back=(RelativeLayout)findViewById(R.id.back_forgot_password);
		submit=(Button)findViewById(R.id.submit_frgt_pwd);


		
		Back.setOnClickListener(this);
		submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		if(v.getId()==R.id.back_forgot_password)
		{
			finish();
			overridePendingTransition(R.anim.anim_three, R.anim.anim_four); 
			return;
		}
		
		if(v.getId()==R.id.submit_frgt_pwd)
		{
			if(!email.getText().toString().isEmpty())
			{
				if(validateEmail(email.getText().toString()))
				{
					if(Utility.isNetworkAvailable(ForgotPwdActivity.this))
						new BackgroundFrgtPwd().execute();
					else
						Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), ForgotPwdActivity.this);
				}
				else
				{
					Utility.ShowAlert(getResources().getString(R.string.enter_valid_email), ForgotPwdActivity.this);
				}
			}
			else
			{
				Utility.ShowAlert(getResources().getString(R.string.email_empty), ForgotPwdActivity.this);
			}
			return;
		}
	}
	
	
	class BackgroundFrgtPwd extends AsyncTask<String, Void, String>
	{

		private  String TAG = null;
		ProgressDialog dialogL;
		BookAppointmentResponse response;
		
		@Override
		protected void onPreExecute() 
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			dialogL= Utility.GetProcessDialogNew(ForgotPwdActivity.this,getResources().getString(R.string.please_wait));
			dialogL.setCancelable(true);
			if (dialogL!=null) 
			{
				dialogL.show();
			}
		}
		
		@Override
		protected String doInBackground(String... params)
		{
			// TODO Auto-generated method stub
			String url=VariableConstants.BASE_URL+"forgotPassword";
			Map<String, String> kvPairs = new HashMap<String, String>();
			kvPairs.put("ent_email",email.getText().toString());
			kvPairs.put("ent_user_type","2");
			
			Utility.printLog("ent_email: "+email.getText().toString());
			
			HttpResponse httpResponse = null;
			try 
			{
				httpResponse = Utility.doPost(url,kvPairs);
			} catch (ClientProtocolException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.i(TAG, "doPost Exception......."+e1);
			} catch (IOException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.i(TAG, "doPost Exception......."+e1);
			}
			
			String jsonResponse = null;
			if (httpResponse!=null) 
			{

				try 
				{
					jsonResponse = EntityUtils.toString(httpResponse.getEntity());
				} catch (ParseException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.i(TAG, "doPost Exception......."+e);
				} catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.i(TAG, "doPost Exception......."+e);
				}
			}
			Utility.printLog("Login Response: ","Fwgt_pswd Response: "+jsonResponse);
			Utility.printLog("Login Response: ",jsonResponse);
			
			
			try
			{
				if (jsonResponse!=null) 
				{
					Gson gson = new Gson();
					response=gson.fromJson(jsonResponse, BookAppointmentResponse.class);
				}else
				{
					runOnUiThread(new Runnable()
					{
						public void run() 
						{
					Toast.makeText(ForgotPwdActivity.this,getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
						}

					});
				}
				
			}
			catch(Exception e)
			{
				Utility.ShowAlert(getResources().getString(R.string.server_error), ForgotPwdActivity.this);
			}
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
			
			if(response!=null)
			{
				if(response.getErrFlag().equals("0"))
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							ForgotPwdActivity.this);
				 
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
										finish();
										overridePendingTransition(R.anim.anim_three, R.anim.anim_four); 
									}
								});
				 
								// create alert dialog
								AlertDialog alertDialog = alertDialogBuilder.create();
								// show it
								alertDialog.show();
					
				}
				else
				{
					Utility.ShowAlert(response.getErrMsg(), ForgotPwdActivity.this);
				}
			}
			else
			{
				Utility.ShowAlert(getResources().getString(R.string.server_error),ForgotPwdActivity.this);
			}
		}
	}
	public boolean validateEmail(String email)
	{
	    boolean isValid = false;
	   

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches())
	    {
	        isValid = true;
	    }
	    
	    return isValid;
	}
	
	@Override
	public void onBackPressed() 
	{
		// TODO Auto-generated method stub
		finish();
		overridePendingTransition(R.anim.anim_three, R.anim.anim_four); 
		
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
