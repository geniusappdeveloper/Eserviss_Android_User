package com.app.taxisharingDriver;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.taxisharingDriver.pojo.AppointmentDetailList;
import com.app.taxisharingDriver.response.AppointmentData;
import com.app.taxisharingDriver.response.AppointmentDetailData;
import com.app.taxisharingDriver.response.PendingBookingDetailList;
import com.app.taxisharingDriver.response.PendingBookingResponse;
import com.app.taxisharingDriver.utility.ConnectionDetector;
import com.app.taxisharingDriver.utility.NetworkConnection;
import com.app.taxisharingDriver.utility.SessionManager;
import com.app.taxisharingDriver.utility.UltilitiesDate;
import com.app.taxisharingDriver.utility.Utility;
import com.app.taxisharingDriver.utility.VariableConstants;
import com.app.taxisharingDriver.utility.VolleyErrorHelper;
import com.google.gson.Gson;

public class PendingBooking extends FragmentActivity 
{

	private ActionBar actionBar;
	private SessionManager sessionManager;
	private ListView listView;
	private RelativeLayout noappointmentlayout,network_bar;
	private PanddingBookingListAdapter paddingBookinListAdapter;
	private ArrayList<PendingBookingDetailList>penddingappointlist = new ArrayList<PendingBookingDetailList>();
	private ArrayList<PendingBookingDetailList>peddingList;
	private ProgressDialog mdialog;
	private AppointmentDetailList appointmentDetailList;
	private IntentFilter filter;
	private BroadcastReceiver receiver;
	private TextView network_text;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		setContentView(R.layout.pending_booking);
		overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
		sessionManager = new SessionManager(this);
		actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.bottom_bar));
		actionBar.setIcon(android.R.color.transparent);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));
		initLayoutid();
		try 
		{
			int actionBarTitle = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
			TextView actionBarTitleView = (TextView) getWindow().findViewById(actionBarTitle);
			Typeface robotoBoldCondensedItalic = Typeface.createFromAsset(getAssets(), "fonts/Zurich Condensed.ttf");
			if(actionBarTitleView != null)
			{
				actionBarTitleView.setTypeface(robotoBoldCondensedItalic);
				actionBarTitleView.setTextColor(getResources().getColor(R.color.white));

			}
			actionBar.setTitle(getResources().getString(R.string.todaybookings));
		} 
		catch (NullPointerException e)
		{
			Utility.printLog("Exception = "+e);
		}
		
		
		
		filter = new IntentFilter();
		filter.addAction("com.app.driverapp.internetStatus");
		receiver = new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{
				try 
				{
					Bundle bucket=intent.getExtras();
					
					String status = bucket.getString("STATUS");

					if(status.equals("1"))
					{
						network_bar.setVisibility(View.GONE);
					}
					else
					{
						if (!Utility.isNetworkAvailable(PendingBooking.this))
						{
							network_bar.setVisibility(View.VISIBLE);
							return;
						}
						else if (!NetworkConnection.isConnectedFast(PendingBooking.this)) 
						{
							network_bar.setVisibility(View.VISIBLE);
							network_text.setText(getResources().getString(R.string.lownetwork));
							return;
						}
					}
				} 
				catch (Exception e)
				{
					Utility.printLog("BroadcastReceiver Exception "+e);
				}
			}
		};


		Utility utility = new Utility();
		ConnectionDetector connectionDetector=new ConnectionDetector(PendingBooking.this);
		if (connectionDetector.isConnectingToInternet()) 
		{
			getPaddingAppontment();
		}
		else 
		{
			utility.displayMessageAndExit(PendingBooking.this,getResources().getString(R.string.Pleasewaitmessage),getResources().getString(R.string.internetconnectionmessage));
		}

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case android.R.id.home:
			// NavUtils.navigateUpFromSameTask(this);
			finish();
			if (sessionManager.isUserLogdIn())
			{
				Intent intent=new Intent(PendingBooking.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	@Override
	protected void onResume() 
	{
		super.onResume();
		if (receiver != null) 
		{
			registerReceiver(receiver, filter);
		}
	}
	
	@Override
	protected void onPause() 
	{
		super.onPause();
		unregisterReceiver(receiver);
	}

	private void initLayoutid()
	{
		listView=(ListView)findViewById(R.id.panddingappointmentlistview);
		
		paddingBookinListAdapter = new PanddingBookingListAdapter(this, penddingappointlist);
		listView.setAdapter(paddingBookinListAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Utility.printLog("AAAAA Inside itemClick Listner");
				if("2".equals(penddingappointlist.get(position).getStatCode()))
				{
					getAppointmentDetails(penddingappointlist.get(position).getEmail(), penddingappointlist.get(position).getApntDt());
				}

			}
		});
		network_bar = (RelativeLayout)findViewById(R.id.network_bar);
		network_text = (TextView)findViewById(R.id.network_text);
		noappointmentlayout=(RelativeLayout)findViewById(R.id.noappointmentlayout);
	}
	private void getPaddingAppontment()
	{
		String sessionToken=sessionManager.getSessionToken();
		String deviceid=Utility.getDeviceId(this);
		Utility utility=new Utility();
		String currentdataandtimeingmt=utility.getCurrentGmtTime();
		String currentdateandtimelocal=UltilitiesDate.getLocalTime(currentdataandtimeingmt);
		final String mparams[]={sessionToken,deviceid,currentdateandtimelocal};
		final ProgressDialog mdialog;
		mdialog=Utility.GetProcessDialog(PendingBooking.this);
		mdialog.setMessage(getResources().getString(R.string.Pleasewaitmessage));
		mdialog.show();
		mdialog.setCancelable(false);
		RequestQueue queue = Volley.newRequestQueue(this);  // this = context
		String	url = VariableConstants.getPandingBooking_url;
		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Listener<String>()
				{
			@Override
			public void onResponse(String response)
			{
				Utility.printLog("getPanddingBookingResponse  = "+response);
				if (mdialog!=null)
				{
					mdialog.dismiss();
					mdialog.cancel();
					//mdialog=null;
				}
				PendingBookingResponse  panddingBookingResponse;
				Gson gson = new Gson();
				panddingBookingResponse=gson.fromJson(response, PendingBookingResponse.class);
				try
				{
					if (panddingBookingResponse.getErrFlag()==0&&panddingBookingResponse.getErrNum()==31)
					{
						//31 -> (0) Got Appointments!
						peddingList=panddingBookingResponse.getAppointments();
						if (peddingList!=null && peddingList.size()>0)
						{
							penddingappointlist.clear();
							penddingappointlist.addAll(peddingList);
							paddingBookinListAdapter.notifyDataSetChanged();
						}
						noappointmentlayout.setVisibility(View.GONE);
					}
					else if (panddingBookingResponse.getErrFlag()==1&&panddingBookingResponse.getErrNum()==30)
					{
						//30 -> (1) No appointments on this date.
						//ErrorMessage(getResources().getString(R.string.messagetitle),result.getErrMsg(),true);
						noappointmentlayout.setVisibility(View.VISIBLE);
					}
					else if (panddingBookingResponse.getErrFlag()==1&&panddingBookingResponse.getErrNum()==7)
					{
						//7 -> (1) Invalid token, please login or register.
						ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle),panddingBookingResponse.getErrMsg());
					}
					else if (panddingBookingResponse.getErrFlag()==1&&panddingBookingResponse.getErrNum()==6)
					{
						//6-> (1)  Session token expired, please login.
						ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle),panddingBookingResponse.getErrMsg());
					}
					else if (panddingBookingResponse.getErrFlag()==1&&panddingBookingResponse.getErrNum()==1)
					{
						//1 -> (1) Mandatory field missing
						ErrorMessage(getResources().getString(R.string.messagetitle),panddingBookingResponse.getErrMsg(),true);
					}
				} 
				catch (Exception e) 
				{
					Utility.printLog("pendding appointment response", "BackGroundTaskForGetPaddingAppointment onPostExecute   Exception "+e);
					ErrorMessage(getResources().getString(R.string.messagetitle),getResources().getString(R.string.servererror),true);
				}

			}
				},
				new ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error) 
					{
						// error
						//  Log.d("Error.Response", error.toString());
						Context context =PendingBooking.this;
						String errorMessage =	VolleyErrorHelper.getMessage(error,context);
						if (mdialog!=null)
						{
							mdialog.dismiss();
							mdialog.cancel();
							//mdialog=null;
						}
						ErrorMessage(getResources().getString(R.string.messagetitle),errorMessage,true);
					}
				}
				) {    
			@Override
			protected Map<String, String> getParams()
			{ 
				Map<String, String>  params = new HashMap<String, String>(); 
				params.put("ent_sess_token", mparams[0]); 
				params.put("ent_dev_id", mparams[1]);
				params.put("ent_date_time", mparams[2]);

				Utility.printLog("getPanddingBookingRequest = "+params);
				return params; 
			}
		};

		int socketTimeout = 60000;//60 seconds - change to what you want
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		postRequest.setRetryPolicy(policy);
		queue.add(postRequest);

	}
	private void ErrorMessage(String title,String message,final boolean flageforSwithchActivity)
	{
		try
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(PendingBooking.this);
			builder.setTitle(title);
			builder.setMessage(message);
			builder.setPositiveButton(getResources().getString(R.string.okbuttontext),new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					if (dialog!=null)
					{
						dialog.dismiss();
						dialog.cancel();
						dialog=null;
					}

					if (flageforSwithchActivity) 
					{

					}
					else
					{
						// only show message  
					}
				}
			});

			AlertDialog	 alert = builder.create();
			alert.setCancelable(false);
			alert.show();
		} catch (Exception e)
		{
		}

	}

	private void ErrorMessageForInvalidOrExpired(String title,String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(PendingBooking.this);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(getResources().getString(R.string.cancelbutton),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// Intent intent=new Intent(getActivity(), MainActivity.class);
				// startActivity(intent);
				dialog.dismiss();
			}
		});

		builder.setNegativeButton(getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				Intent intent=new Intent(PendingBooking.this, SplahsActivity.class);
				//	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				//	Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK
				sessionManager.logoutUser();
				startActivity(intent);
				finish();
			}
		});

		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

	private class PanddingBookingListAdapter extends ArrayAdapter<PendingBookingDetailList>
	{
		private Activity mActivity;
		private LayoutInflater mInflater;

		public PanddingBookingListAdapter(Activity context,List<PendingBookingDetailList>objects)
		{
			super(context,R.layout.pending_booking_list,objects);
			mActivity = context;
			mInflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() 
		{
			return super.getCount();
		}
		@Override
		public PendingBookingDetailList getItem(int position)
		{
			return super.getItem(position);
		}
		@Override
		public void remove(PendingBookingDetailList object)
		{
			super.remove(object);
		}
		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder;
			if (convertView == null || convertView.getTag()==null)
			{
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.pending_booking_list, null);
				holder.bookingNumber = (TextView)convertView.findViewById(R.id.bookingnumber);
				holder.bookingId = (TextView)convertView.findViewById(R.id.booking_id);
				holder.bookingTime = (TextView)convertView.findViewById(R.id.booking_time);
				holder.phone_no = (TextView)convertView.findViewById(R.id.phone_no);
				holder.status = (TextView)convertView.findViewById(R.id.status);
				holder.passengerName = (TextView)convertView.findViewById(R.id.first_name);
				holder.picAddress = (TextView)convertView.findViewById(R.id.pickup_text);
				holder.dropAddress = (TextView)convertView.findViewById(R.id.dropoff_text);

				convertView.setTag(holder);
			}
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}

			holder.bookingNumber.setId(position);
			holder.bookingId.setId(position);
			holder.bookingTime.setId(position);
			holder.status.setId(position);
			holder.passengerName.setId(position);
			holder.picAddress.setId(position);
			holder.dropAddress.setId(position);

			int i = position + 1;
			
			holder.bookingNumber.setText(""+i);
			holder.bookingId.setText(getResources().getString(R.string.jobid)+getItem(position).getBid());
			holder.bookingTime.setText(getItem(position).getApntTime());
			holder.status.setText(getItem(position).getStatus());
			holder.passengerName.setText(getItem(position).getFname());
			holder.phone_no.setText(getItem(position).getMobile());
			holder.picAddress.setText("P: "+getItem(position).getAddrLine1());
			holder.dropAddress.setText("D: "+getItem(position).getDropLine1());

			return convertView;
		}

		class ViewHolder
		{
			TextView bookingNumber,bookingTime,bookingId,status,passengerName,picAddress,dropAddress,phone_no;
		}

	}
	private void getAppointmentDetails(String email,String aptDateTime)
	{
		Utility.printLog("Animation action email"+email,"Animation action Date Time"+aptDateTime);
		SessionManager sessionManager=new SessionManager(PendingBooking.this);
		Utility utility=new Utility();
		String sessionToken=sessionManager.getSessionToken();
		String deviceid=Utility.getDeviceId(PendingBooking.this);
		String currentDate=utility.getCurrentGmtTime();

		final String mparams[]={sessionToken,deviceid,email,aptDateTime,currentDate};
		//final ProgressDialog mdialog;
		mdialog=Utility.GetProcessDialog(PendingBooking.this);
		mdialog.setMessage(getResources().getString(R.string.Pleasewaitmessage));
		mdialog.show();
		mdialog.setCancelable(false);
		RequestQueue queue = Volley.newRequestQueue(PendingBooking.this);
		String url = VariableConstants.getAppointmentDetails_url;
		StringRequest postRequest = new StringRequest(Request.Method.POST, url,responseListenerofAppointment,errorListener1 ) 
		{    
			@Override
			protected Map<String, String> getParams()
			{ 
				Map<String, String>  params = new HashMap<String, String>(); 
				params.put("ent_sess_token", mparams[0]); 
				params.put("ent_dev_id", mparams[1]);
				params.put("ent_email", mparams[2]);
				params.put("ent_appnt_dt", mparams[3]);
				params.put("ent_date_time", mparams[4]);
				params.put("ent_user_type", "1");
				Utility.printLog("getAppointmentDetails  request "+params);
				return params; 
			}
		};
		int socketTimeout = 60000;//60 seconds - change to what you want
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		postRequest.setRetryPolicy(policy);
		queue.add(postRequest);	
	}
	Listener<String> responseListenerofAppointment=new Listener<String>()
			{
		@Override
		public void onResponse(String response) 
		{
			Utility.printLog("Animation action Response Came");

			Utility.printLog("getAppointmentDetails  response "+response);
			AppointmentData  appointmentData;
			Gson gson = new Gson();
			appointmentData=gson.fromJson(response, AppointmentData.class);

			if (mdialog!=null)
			{
				mdialog.dismiss();
				mdialog.cancel();
			}
			try
			{						
				if (appointmentData.getErrFlag()==0 && appointmentData.getErrNum() == 21)
				{
					AppointmentDetailData bookingDetailData  = appointmentData.getData();
					appointmentDetailList = new AppointmentDetailList();
					appointmentDetailList.setAppointmentDetailData(bookingDetailData);
					Intent intent = new Intent(PendingBooking.this,IAmOnTheWay.class); // IHaveArrivedActivity IAmOnTheWay
					Bundle bundle=new Bundle();
					bundle.putSerializable(VariableConstants.APPOINTMENT, appointmentDetailList);
					intent.putExtras(bundle);
					startActivity(intent);
				}
				else if (appointmentData.getErrFlag()==1)
				{
					// 3 -> (1) Error occurred while processing your request.
					ErrorMessage(getResources().getString(R.string.messagetitle),appointmentData.getErrMsg(),false);
				}
			} 
			catch (Exception e) 
			{
				Utility.printLog("getAppointmentDetailException = "+e); 
			}
		}
			};
			ErrorListener errorListener1=new ErrorListener()
			{
				@Override
				public void onErrorResponse(VolleyError error) 
				{
					Toast.makeText(PendingBooking.this, ""+error, Toast.LENGTH_SHORT).show();
					Utility.printLog("Animation action Response Didnt Came Error");
				}
			};
}
