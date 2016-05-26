package com.app.taxisharingDriver;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.taxisharingDriver.response.EmailValidateResponse;
import com.app.taxisharingDriver.utility.SessionManager;
import com.app.taxisharingDriver.utility.Utility;
import com.app.taxisharingDriver.utility.VariableConstants;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ForgotPasswordActivity extends Activity implements OnClickListener
{
	private EditText email;
	private Button submit;
	Context context;
	ProgressDialog dialogL;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_pwd);
		initActionBar();
		initialize();
		SessionManager sessionManager = new SessionManager(this);
		sessionManager.setIsFlagForOther(true);
		
	}
	
    public void initialize()
	{
		email = (EditText)findViewById(R.id.email_frgt_pwd);
		submit = (Button)findViewById(R.id.submit_frgt_pwd);
		submit.setOnClickListener(this);
	}
	@SuppressLint("NewApi")
	private void initActionBar()
	  {
		  android.app.ActionBar actionBar=getActionBar();
		  actionBar.setDisplayShowTitleEnabled(true);
		  actionBar.setDisplayHomeAsUpEnabled(true);
		  actionBar.setDisplayUseLogoEnabled(false);
		 // actionBar.setBackgroundDrawable(getResources().getDrawable());
		  actionBar.setIcon(android.R.color.transparent);
		  // actionBar.setIcon(getResources().getDrawable(R.drawable.login_screen_back_btn_off));
		  actionBar.setTitle(getResources().getString(R.string.back));
	  }
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) 
	  	{
	  	 case android.R.id.home:
			  
			  		 finish();
			         return true;
			default:
		   // return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void BackgroundFrgtPwd_Volley() 
	{
	
		dialogL=Utility.GetProcessDialog(ForgotPasswordActivity.this);
		dialogL.setMessage("Sending...");
		dialogL.setCancelable(false);
		if (dialogL!=null) {
			dialogL.show();
		}
		
		RequestQueue volleyRequest = Volley.newRequestQueue(this);
		StringRequest myReq = new StringRequest(com.android.volley.Request.Method.POST, 
				VariableConstants.hostUrl+"forgotPassword",

				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						
						if (dialogL!=null) {
							dialogL.dismiss();
							dialogL=null;
						}
						
						
						Utility.printLog("LoginResponse: "+response);
						
						fetchData(response);
					}
			
					private void fetchData(String jsonResponse) {
						EmailValidateResponse response=null;
						try
						{
							if (jsonResponse!=null) 
							{
								Gson gson = new Gson();
								response=gson.fromJson(jsonResponse, EmailValidateResponse.class);
							}else
							{
								runOnUiThread(new Runnable()
								{
									public void run() 
									{
								//Toast.makeText(ForgotPasswordActivity.this,"Request Timeout !!", Toast.LENGTH_SHORT).show();
									}

								});
							}
							
							
							if(response!=null)
							{
								if(response.getErrFlag().equals("0"))
								{
									AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
											ForgotPasswordActivity.this);
								 
											// set title
											alertDialogBuilder.setTitle(getResources().getString(R.string.note));
								 
											// set dialog message
											alertDialogBuilder
												.setMessage(response.getErrMsg())
												.setCancelable(false)
												
												.setNegativeButton(getResources().getString(R.string.okbuttontext),new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog,int id) {
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
									Utility.ShowAlert(response.getErrMsg(), ForgotPasswordActivity.this);
									
								}
								
							}
							else
							{
								Utility.ShowAlert("Server Error!!",ForgotPasswordActivity.this);
							}
							
							
						}
						catch(Exception e)
						{
							Utility.ShowAlert("Server error!!", ForgotPasswordActivity.this);
						}
					}
		},
		new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error) {
				if (dialogL!=null) {
					dialogL.dismiss();
					dialogL=null;
				}
				Utility.printLog("LoginResponse:ERROR");
				//Toast.makeText(getApplicationContext(), "Error. Try Again!!", Toast.LENGTH_SHORT).show();
				
			}
			
		}){
			
			
			protected HashMap<String,String> getParams() throws com.android.volley.AuthFailureError {
				
			
				HashMap<String, String> kvPairs = new HashMap<String, String>();
				kvPairs.put("ent_email",email.getText().toString());
				kvPairs.put("ent_user_type","1");
				return kvPairs;  

			}
		
		};

		volleyRequest.add(myReq);
		
	}
	public boolean validateEmail(String email) {
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
	public void onClick(View v) 
	{
		if(!email.getText().toString().isEmpty())
		{
			if(validateEmail(email.getText().toString()))
			{

				if(Utility.isNetworkAvailable(ForgotPasswordActivity.this))
					BackgroundFrgtPwd_Volley();
				else
					Utility.ShowAlert(getResources().getString(R.string.network), ForgotPasswordActivity.this);
	
			}
			else {
				Utility.ShowAlert(getResources().getString(R.string.pleaseentervalidemailid), ForgotPasswordActivity.this);
			}
			
		}
		else
		{
			Utility.ShowAlert(getResources().getString(R.string.entervalidid), ForgotPasswordActivity.this);
		}
		return;
	}

}
