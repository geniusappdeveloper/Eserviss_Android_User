package com.eserviss.passenger.main;

import android.app.Dialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.egnyt.eserviss.R;
import com.threembed.utilities.GpsListener;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashWithGps extends FragmentActivity implements OnClickListener, AnimationListener
{
	private LinearLayout login_register;
	private Button signin,register;
	Animation fadein;
	SessionManager session;
	List< String> list;
	public static Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		//Crashlytics.start(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		intializeVariables() ;
		session=new SessionManager(SplashWithGps.this);
		fadein = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.bounce);     

		session.storeCurrencySymbol("$");


		GpsListener gpsListener = new GpsListener(SplashWithGps.this);
		boolean bool = gpsListener.getGrade();
		if(!bool)
		{
			showGpsAlert();
		}
		else
		{
			Bundle bundle=getIntent().getExtras();

			if(bundle!=null&&bundle.getBoolean("NO_ANIMATION"))
			{
				login_register.setVisibility(View.VISIBLE);
			}
			else
			{
				overridePendingTransition(R.anim.mainfadein,R.anim.splashfadeout);
				Thread timer=new Thread()
				{
					@Override
					public void run() 
					{
						// TODO Auto-generated method stub
						try 
						{
							sleep(1000);
						} catch (InterruptedException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						finally
						{
							if(session.isLoggedIn())
							{
								if(Utility.isNetworkAvailable(SplashWithGps.this))
								{
									new BackgroundSessionCheck().execute();
								}
								else
								{
									runOnUiThread(new Runnable()
									{
										public void run() 
										{
											Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SplashWithGps.this);
											login_register.setVisibility(View.VISIBLE);
											signin.startAnimation(fadein);
											register.startAnimation(fadein);

										}
									});
								}
							}
							else
							{
								runOnUiThread(new Runnable()
								{
									public void run() 
									{
										login_register.setVisibility(View.VISIBLE);
										signin.startAnimation(fadein);
										register.startAnimation(fadein);
									}
								});
							}
						}
					}
				};
				timer.start();
			}

		}
	}



	class BackgroundSessionCheck extends AsyncTask<String, Void, String>
	{
		BookAppointmentResponse response;

		@Override
		protected void onPreExecute() 
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) 
		{
			// TODO Auto-generated method stub
			if(Utility.isNetworkAvailable(SplashWithGps.this))
			{
				String url= VariableConstants.BASE_URL+"checkSession";

				Utility utility=new Utility();
				String curenttime=utility.getCurrentGmtTime();
				Utility.printLog("dataandTime " + curenttime);

				Utility.printLog("splash getSessionToken=" + session.getSessionToken());
				Utility.printLog("splash getSessionDeviceId=" + session.getDeviceId());


				Map<String, String> kvPairs = new HashMap<String, String>();

				kvPairs.put("ent_sess_token",session.getSessionToken());
				kvPairs.put("ent_dev_id",session.getDeviceId());
				kvPairs.put("ent_user_type","2");
				kvPairs.put("ent_date_time",curenttime);

				HttpResponse httpResponse = null;
				try {
					httpResponse = Utility.doPost(url,kvPairs);
				} catch (ClientProtocolException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					Utility.printLog("doPost Exception......." + e1);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					Utility.printLog("doPost Exception......." + e1);
				}

				String jsonResponse = null;
				if (httpResponse!=null) 
				{
					try {
						jsonResponse = EntityUtils.toString(httpResponse.getEntity());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Utility.printLog("doPost Exception......." + e);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Utility.printLog("doPost Exception......." + e);
					}
				}
				Utility.printLog("GetDoctoraround Response: ", "Session Check: " + jsonResponse);
				Utility.printLog("GetDoctoraround Response: ", " " + jsonResponse);

				if (jsonResponse!=null) 
				{
					try
					{
						Gson gson = new Gson();
						response=gson.fromJson(jsonResponse,BookAppointmentResponse.class);
					}
					catch(Exception e)
					{
						runOnUiThread(new Runnable()
						{
							public void run() 
							{
								Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SplashWithGps.this);
							}

						});
					}
				}
				else
				{
					runOnUiThread(new Runnable()
					{
						public void run() 
						{
							Toast.makeText(SplashWithGps.this,getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
						}

					});
				}
			}
			else
			{
				runOnUiThread(new Runnable()
				{
					public void run() 
					{
						Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SplashWithGps.this);
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

			if(response!=null)
			{
				if(response.getErrFlag().equals("0"))
				{
					if(Utility.isNetworkAvailable(SplashWithGps.this))
					{
						Intent intent=new Intent(SplashWithGps.this,ResideMenuActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
					}
					else
					{
						Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SplashWithGps.this);
					}
				}
				else
				{
					Toast.makeText(SplashWithGps.this, response.getErrMsg(), Toast.LENGTH_SHORT).show();

					session.setIsLogin(false);				
					login_register.setVisibility(View.VISIBLE);
					signin.startAnimation(fadein);
					register.startAnimation(fadein);
				}

			}
			else
			{
				Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SplashWithGps.this);
			}
		}

	}
	private void intializeVariables() 
	{
		// TODO Auto-generated method stub
		login_register=(LinearLayout)findViewById(R.id.login_buttons);
		signin=(Button)findViewById(R.id.signin);
		register=(Button)findViewById(R.id.register);

		signin.setOnClickListener(this);
		register.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void onClick(View v) 
	{
		// Listener when Register button is clicked
		if(v.getId()==R.id.register)
		{
			if(Utility.isNetworkAvailable(SplashWithGps.this))
			{

				final Intent intent=new Intent(SplashWithGps.this,SignupActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
				this.finish();

			}
			else
			{
				Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SplashWithGps.this);
			}
		}
		// Listener when Sign-in button is clicked
		if(v.getId()==R.id.signin)
		{
			if(Utility.isNetworkAvailable(SplashWithGps.this))
			{
				Intent intent=new Intent(SplashWithGps.this,SigninActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
				this.finish();
			}
			else
			{
				Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SplashWithGps.this);
			}
		}
	}

	public void showGpsAlert()
	{
		dialog = new Dialog(SplashWithGps.this);
		dialog.setTitle("No Location Access");
		dialog.setContentView(R.layout.gps_alert);

		Button btn = (Button) dialog.findViewById(R.id.go_to_settings);
		btn.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
				finish();
			}
		});
		
		dialog.show();
	}
	
	public void gpsEnabled()
	{
		overridePendingTransition(R.anim.mainfadein,R.anim.splashfadeout);
		Thread timer=new Thread()
		{
			@Override
			public void run() 
			{
				// TODO Auto-generated method stub
				try 
				{
					sleep(1000);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally
				{
					if(session.isLoggedIn())
					{
						if(Utility.isNetworkAvailable(SplashWithGps.this))
						{
							new BackgroundSessionCheck().execute();
						}
						else
						{
							runOnUiThread(new Runnable()
							{
								public void run() 
								{
									Utility.ShowAlert(getResources().getString(R.string.network_connection_fail), SplashWithGps.this);
									login_register.setVisibility(View.VISIBLE);
									signin.startAnimation(fadein);
									register.startAnimation(fadein);

								}
							});
						}
					}
					else
					{
						runOnUiThread(new Runnable()
						{
							public void run() 
							{
								login_register.setVisibility(View.VISIBLE);
								signin.startAnimation(fadein);
								register.startAnimation(fadein);
							}
						});
					}
				}
			}
		};
		timer.start();
	
	}



	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
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

