package com.app.taxisharingDriver.calander;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.taxisharingDriver.BookingHistory;
import com.app.taxisharingDriver.R;
import com.app.taxisharingDriver.SplahsActivity;
import com.app.taxisharingDriver.pojo.AppointmentDetailList;
import com.app.taxisharingDriver.pojo.GetNumberOffMasterApointment;
import com.app.taxisharingDriver.pojo.GetWholeAppointmentDetail;
import com.app.taxisharingDriver.response.AppointmentDetailData;
import com.app.taxisharingDriver.utility.ConnectionDetector;
import com.app.taxisharingDriver.utility.Scaler;
import com.app.taxisharingDriver.utility.SessionManager;
import com.app.taxisharingDriver.utility.Utility;
import com.app.taxisharingDriver.utility.VariableConstants;
import com.google.gson.Gson;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.roomorama.caldroid.CalendarHelper;

@SuppressLint("SimpleDateFormat")
public class CalendarFragMent extends Fragment implements OnItemClickListener
{

	private CaldroidFragment caldroidFragment;
	private AppointmentListAdapter appointmentListAdapter;
	private View header;
	private static boolean mDebugLog = true;
	private static String mDebugTag = "TabbedHorizontalPagerDemo";
	private ArrayList<Integer>arrayList=new ArrayList<Integer>();
	private ArrayList<GetNumberOffMasterApointment>numberOfAppointmentsList;
	private String currencySymbol = VariableConstants.CURRENCY_SYMBOL;
	private SessionManager sessionManager;

	void logDebug(String msg)
	{
		if (mDebugLog) 
		{
			//Log.d(mDebugTag, msg);
		}
	}

	void logError(String msg)
	{

		if (mDebugLog) 
		{
			Utility.printLog(mDebugTag, msg);
		}
	}
	private ArrayList<AppointmentDetailList>appointmentDetailLists=new ArrayList<AppointmentDetailList>();
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		final SimpleDateFormat formatter = new SimpleDateFormat("dd");

		// Setup caldroid fragment
		// **** If you want normal CaldroidFragment, use below line ****
		caldroidFragment = new CaldroidFragment();

		// **** This is to show customized fragment. If you want customized
		// version, uncomment below line ****
		// caldroidFragment = new CaldroidSampleCustomFragment();
		// If Activity is created after rotation
		if (savedInstanceState != null) 
		{
			caldroidFragment.restoreStatesFromKey(savedInstanceState,"CALDROID_SAVED_STATE");
		}
		// If activity is created from fresh
		else 
		{
			Bundle args = new Bundle();
			Calendar cal = Calendar.getInstance();
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
			caldroidFragment.setArguments(args);
		}

		setCustomResourceForDates();

		final CaldroidListener listener = new CaldroidListener() 
		{

			@Override
			public void onSelectDate(Date date, View view)
			{
				int selectedDate=Integer.parseInt(formatter.format(date));
				refreshListview(selectedDate);
			}

			@Override
			public void onChangeMonth(int month, int year) 
			{
				//String text = "month: " + month + " year: " + year;
			}

			@Override
			public void onLongClickDate(Date date, View view) 
			{
			}

			@Override
			public void onCaldroidViewCreated() 
			{
				if (caldroidFragment.getLeftArrowButton() != null) 
				{
				}
			}

		};

		caldroidFragment.setCaldroidListener(listener);

		Utility utility=new Utility();
		ConnectionDetector connectionDetector=new ConnectionDetector(getActivity());
		sessionManager = new SessionManager(getActivity());
		sessionManager.setIsFlagForOther(true);

		if (connectionDetector.isConnectingToInternet()) 
		{
			getWholeMonthAppointment();

		}
		else 
		{
			utility.displayMessageAndExit(getActivity(),getResources().getString(R.string.Pleasewaitmessage),getResources().getString(R.string.internetconnectionmessage));
		}

	}


	private void refreshListview(int index )
	{
		Date tempDate = null;
		HashMap<Date, Integer>dateColorMap=new HashMap<Date, Integer>();

		if (arrayList.contains(index-1))
		{
			for (int i = 0; i < numberOfAppointmentsList.size(); i++) 
			{
				if (numberOfAppointmentsList.get(i).getNumberOfAppointmentOftheDay()!=null&&numberOfAppointmentsList.get(i).getNumberOfAppointmentOftheDay().size()>0) 
				{
					// YYYY-MM-DD HH:MM:SS

					try 
					{
						tempDate=CalendarHelper.getDateFromString(numberOfAppointmentsList.get(i).getDate(),"yyyy-MM-dd");
					} 
					catch (ParseException e) 
					{
						e.printStackTrace();
					}
					if (i==index-1) 
					{
						dateColorMap.put(tempDate, R.color.blue);
					}
					else 
					{
						dateColorMap.put(tempDate, R.color.green);
					}

					caldroidFragment.setTextColorForDate(R.color.white, tempDate);
				}
				else 
				{
					try 
					{
						tempDate=CalendarHelper.getDateFromString(numberOfAppointmentsList.get(i).getDate(),"yyyy-MM-dd");
					} catch (ParseException e)
					{
						e.printStackTrace();
					}
					dateColorMap.put(tempDate, R.color.white);
					caldroidFragment.setTextColorForDate(R.color.black, tempDate);

				}
			}
			caldroidFragment.setBackgroundResourceForDates(dateColorMap);
			caldroidFragment.refreshView();

			ArrayList<AppointmentDetailList>mappointmentDetailLists=numberOfAppointmentsList.get(index-1).getNumberOfAppointmentOftheDay();
			appointmentDetailLists.clear();
			appointmentDetailLists.addAll(mappointmentDetailLists);
			appointmentListAdapter.notifyDataSetChanged();

		}
		else
		{
			logError("refreshListview");
		}
	}

	private void getWholeMonthAppointment()
	{
		Utility utility=new Utility();

		ConnectionDetector connectionDetector=new ConnectionDetector(getActivity());
		if (connectionDetector.isConnectingToInternet()) 
		{
			String deviceid=Utility.getDeviceId(getActivity());
			String curenttime=utility.getCurrentGmtTime();
			String currentdata[]=curenttime.split(" ");
			String datestr=currentdata[0];
			datestr = datestr.substring(0, datestr.lastIndexOf("-"));
			SessionManager sessionManager=new SessionManager(getActivity());
			final ProgressDialog mdialog;
			String sessiontoken=sessionManager.getSessionToken();
			final String mparams[]={sessiontoken,deviceid,datestr,curenttime};
			RequestQueue queue = Volley.newRequestQueue(getActivity());  // this = context
			mdialog=Utility.GetProcessDialog(getActivity());
			mdialog.setMessage(getResources().getString(R.string.Pleasewaitmessage));
			mdialog.show();
			mdialog.setCancelable(false);
			String  url = VariableConstants.getAppointmentDetail_url;
			StringRequest postRequest = new StringRequest(Request.Method.POST, url,
					new Response.Listener<String>()
					{
				@Override
				public void onResponse(String response) 
				{
					if (mdialog!=null)
					{
						mdialog.dismiss();
						mdialog.cancel();
						//mdialog=null;
					}
					Utility.printLog("AAAAAAAAA WholeMonthAppointment = "+response);

					try 
					{
						GetWholeAppointmentDetail appointmentMaster;
						Gson gson = new Gson();
						appointmentMaster=gson.fromJson(response, GetWholeAppointmentDetail.class);

						boolean isFirstAppointDateFound = false;
						int firstAppoitmentIndex = 0;

						if (appointmentMaster.getErrFlag() == 0 && appointmentMaster.getErrNum() == 31)
						{
							//31 -> (0) Got Appointments!
							Utility utility = new Utility();
							String currentdtString=utility.getCurrentGmtTime();
							String [] datearray=currentdtString.split(" ");
							arrayList.clear();
							numberOfAppointmentsList = appointmentMaster.getNumberOffAppointmentsList();
							//java.util.HashMap<Date, Integer>dateColorMap=new java.util.HashMap<Date, Integer>(); 
							for (int i = 0; i < numberOfAppointmentsList.size(); i++) 
							{
								if (numberOfAppointmentsList.get(i).getNumberOfAppointmentOftheDay()!=null&&numberOfAppointmentsList.get(i).getNumberOfAppointmentOftheDay().size()>0) 
								{
									// YYYY-MM-DD HH:MM:SS
									arrayList.add(i);
									if (datearray[0].equals(numberOfAppointmentsList.get(i).getDate())) 
									{
										isFirstAppointDateFound=true;
										firstAppoitmentIndex = i;
									}
								}
								else 
								{

								}
							}
							if (isFirstAppointDateFound) 
							{
								refreshListview(firstAppoitmentIndex+1);
							}
						}
						else if (appointmentMaster.getErrFlag()==1 && appointmentMaster.getErrNum()==30)
						{
							//30 -> (1) No appointments on this date.
							//31 -> (0) Got Appointments!
							arrayList.clear();
							numberOfAppointmentsList=appointmentMaster.getNumberOffAppointmentsList();
							HashMap<Date, Integer>dateColorMap=new HashMap<Date, Integer>();
							for (int i = 0; i < numberOfAppointmentsList.size(); i++) 
							{
								arrayList.add(i);
								Date tempDate=CalendarHelper.getDateFromString(numberOfAppointmentsList.get(i).getDate(),"yyyy-MM-dd");
								dateColorMap.put(tempDate, R.color.white);
								caldroidFragment.setTextColorForDate(R.color.black, tempDate);

							}
							caldroidFragment.setBackgroundResourceForDates(dateColorMap);
							caldroidFragment.refreshView();
							//Toast.makeText(getActivity(), appointmentMaster.getErrMsg(), Toast.LENGTH_SHORT).show();
							// ErrorMessage(getResources().getString(R.string.messagetitle),result.getErrMsg(),true);
						}
						else if (appointmentMaster.getErrFlag()==1&&appointmentMaster.getErrNum()==7)
						{
							//7 -> (1) Invalid token, please login or register.onClick
							ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle),appointmentMaster.getErrMsg());
						}
						else if (appointmentMaster.getErrFlag()==1&&appointmentMaster.getErrNum()==6)
						{
							//6-> (1)  Session token expired, please login.
							ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle),appointmentMaster.getErrMsg());
						}
						else if (appointmentMaster.getErrFlag()==1&&appointmentMaster.getErrNum()==101)
						{
							//6-> (1)  Session token expired, please login.
							ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle),appointmentMaster.getErrMsg());
						}
						else if (appointmentMaster.getErrFlag()== 1 && appointmentMaster.getErrNum()==1)
						{
							//1 -> (1) Mandatory field missing
							ErrorMessage(getResources().getString(R.string.messagetitle),appointmentMaster.getErrMsg(),true);
						}
						else if (appointmentMaster.getErrFlag()==1&&appointmentMaster.getErrNum()==99)
						{
							//6-> (1)  Session token expired, please login.
							ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle),appointmentMaster.getErrMsg());
						}

						else if (appointmentMaster.getErrFlag()==1&&appointmentMaster.getErrNum()==94)
						{
							//94-> (1)  Your account is been deactivated by our admin, please write an email to know more
							ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle),appointmentMaster.getErrMsg());
						}
						
					} 
					catch (Exception e) 
					{
						if (mdialog!=null)
						{
							mdialog.dismiss();
							mdialog.cancel();
							//mdialog=null;
						}
						logError("BackGroundTaskForGetAppointmentDetails onPostExecute Exception "+e);
					}
				}
					},
					new Response.ErrorListener()
					{
						@Override
						public void onErrorResponse(VolleyError error) 
						{
							Utility.printLog("Error.Response", error.toString());
							if (mdialog!=null)
							{
								mdialog.dismiss();
								mdialog.cancel();
								//mdialog=null;
							}
							ErrorMessage(getResources().getString(R.string.messagetitle),getResources().getString(R.string.servererror),true);
						}
					}
					) {    
				@Override
				protected Map<String, String> getParams()
				{ 
					Map<String, String>  params = new HashMap<String, String>(); 
					params.put("ent_sess_token", mparams[0]); 
					params.put("ent_dev_id", mparams[1]);
					params.put("ent_appnt_dt",mparams[2]); 
					params.put("ent_date_time",mparams[3]);

					return params; 
				}
			};
			int socketTimeout = 60000;//60 seconds - change to what you want
			RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
			postRequest.setRetryPolicy(policy);
			queue.add(postRequest);
		}
		else 
		{
			utility.showDialogConfirm(getActivity(),"Alert"," working internet connection required", false).show();
		}
	}

	private void ErrorMessage(String title,String message,final boolean flageforSwithchActivity)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setPositiveButton(getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if (flageforSwithchActivity) 
				{

				}
				else
				{
					// only show message  
				}
				dialog.dismiss();
			}
		});

		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

	private void ErrorMessageForInvalidOrExpired(String title,String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(message);

		/*builder.setPositiveButton(getResources().getString(R.string.cancelbutton),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});*/

		builder.setNegativeButton(getResources().getString(R.string.okbuttontext),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				Intent intent=new Intent(getActivity(), SplahsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				SessionManager sessionManager=new SessionManager(getActivity());
				sessionManager.logoutUser();
				startActivity(intent);
				getActivity().finish();
			}
		});

		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

	private ListView  appointmentlistview;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
	{
		View view  = inflater.inflate(R.layout.appointment, null);
		initLayuotid(view);
		header = inflater.inflate(R.layout.listviewheader, null);
		Utility utility=new Utility();
		double arrayOfscallingfactor[]=Scaler.getScalingFactor(getActivity());
		int topstriplayoutmarigin = 440; // margin in dips
		int topstripMargin = (int)Math.round(topstriplayoutmarigin*arrayOfscallingfactor[1]);//(int)(topstriplayoutmarigin * d); // margin in pixels
		LinearLayout.LayoutParams layoutParams=utility.getLinearLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, topstripMargin);
		RelativeLayout headechildlayout=(RelativeLayout)header.findViewById(R.id.myFragmentEmbedded);
		headechildlayout.setLayoutParams(layoutParams);
		appointmentlistview.addHeaderView(header);
		FragmentTransaction t = getChildFragmentManager().beginTransaction();
		t.replace(R.id.myFragmentEmbedded, caldroidFragment);
		t.commit();
		appointmentListAdapter=new AppointmentListAdapter(getActivity(), appointmentDetailLists);
		appointmentlistview.setAdapter(appointmentListAdapter);
		appointmentlistview.setOnItemClickListener(this);

		return view;
	}

	private void initLayuotid(View view)
	{
		appointmentlistview=(ListView)view.findViewById(R.id.appointmentlistview);
	}

	private class AppointmentListAdapter extends ArrayAdapter<AppointmentDetailList>
	{
		private Activity mActivity;
		Typeface robotoBoldCondensedItalic;
		private LayoutInflater mInflater;
		private Utility utility=new Utility();
		private String ImageHosturl;
		//private  int topstripMargin;
		private double width;
		private double height;
		public AppointmentListAdapter(Activity context,List<AppointmentDetailList> objects) 
		{
			super(context, R.layout.appointmentlistviewitem, objects);
			mActivity=context;
			mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ImageHosturl=utility.getImageHostUrl(mActivity);
			robotoBoldCondensedItalic = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Zurich Light Condensed.ttf");
			double arrayOfscallingfactor[]=Scaler.getScalingFactor(getActivity());
			width = (160)*arrayOfscallingfactor[0];
			height = (150)*arrayOfscallingfactor[1];
			//int topstriplayoutmarigin = 300; // margin in dips
			//topstripMargin = (int)Math.round(topstriplayoutmarigin*arrayOfscallingfactor[1]);//(int)(topstriplayoutmarigin * d); // margin in pixels
		}

		@Override
		public int getCount() 
		{
			return super.getCount();
		}

		@Override
		public AppointmentDetailList getItem(int position) 
		{
			return super.getItem(position);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder;
			if (convertView == null) 
			{
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.appointment_listview_item, null);
				//holder.imageview = (ImageView) convertView.findViewById(R.id.imageview);
				holder.passenger_name=(TextView)convertView.findViewById(R.id.passenger_name);
				holder.amount = (TextView)convertView.findViewById(R.id.amount);
				holder.amount_text = (TextView)convertView.findViewById(R.id.amount_text);
				holder.pick_location=(TextView)convertView.findViewById(R.id.pick_loc);
				holder.drop_location=(TextView)convertView.findViewById(R.id.drop_loc);
				//holder.petientnameandtimelayout=(RelativeLayout)convertView.findViewById(R.id.petientnameandtimelayout);
				//holder.appointmentitemmainlayout=(LinearLayout)convertView.findViewById(R.id.appointmentitemmainlayout);
				holder.time_text=(TextView)convertView.findViewById(R.id.time_text);
				//holder.destanceunittextview=(TextView)convertView.findViewById(R.id.destanceunittextview);
				holder.status = (TextView)convertView.findViewById(R.id.status);
				holder.BookingId = (TextView)convertView.findViewById(R.id.home_booking_id_text);
				//holder.destanceunittextview=(TextView)convertView.findViewById(R.id.destanceunittextview);
				//holder.arrowimageview=(ImageView)convertView.findViewById(R.id.arrowimageview);
				convertView.setTag(holder);
			}
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}

			String amountstr = currencySymbol+getItem(position).getAmount();


			holder.passenger_name.setId(position);
			//holder.imageview.setId(position);
			holder.pick_location.setId(position);
			holder.drop_location.setId(position);
			//holder.petientnameandtimelayout.setId(position);
			//holder.appointmentitemmainlayout.setId(position);
			holder.time_text.setId(position);
			holder.amount.setId(position);
			holder.amount_text.setId(position);
			holder.status.setId(position);
			holder.BookingId.setId(position);
			//holder.arrowimageview.setId(position);
			/*Picasso.with(getActivity()) //
			.load(ImageHosturl+getItem(position).getpPic()) //
			.placeholder(R.drawable.default_user) //
			.error(R.drawable.default_user).fit()
			.resize((int)Math.round(width),(int)Math.round(height))	 //
			.into(holder.imageview);*/
			holder.passenger_name.setText(getItem(position).getFname());
			holder.pick_location.setText(" "+getItem(position).getAddrLine1());
			//holder.destanceunittextview.setText(" "+getItem(position).getDistance()+" "+getResources().getString(R.string.kmh));
			holder.time_text.setText(getResources().getString(R.string.time_text)+" "+getItem(position).getApntTime());
			holder.drop_location.setText(" "+getItem(position).getDropLine1());
			holder.amount.setText(amountstr);
			holder.BookingId.setText(" "+getItem(position).getBid());
			if (getItem(position).getPayStatus() == 0)
			{
				holder.amount_text.setText(""+getResources().getString(R.string.paymentnot));
			}
			else if (getItem(position).getPayStatus() == 1)
			{
				holder.amount_text.setText(""+getResources().getString(R.string.paydone));
			}
			else if (getItem(position).getPayStatus() == 2)
			{
				holder.amount_text.setText(""+getResources().getString(R.string.dispute));
			}
			else
			{
				holder.amount_text.setText(""+getResources().getString(R.string.closed));
			}

			holder.status.setText(" "+getItem(position).getStatus().toUpperCase());

			//holder.passenger_name.setTypeface(robotoBoldCondensedItalic);
			//holder.pick_location.setTypeface(robotoBoldCondensedItalic);
			//holder.drop_location.setTypeface(robotoBoldCondensedItalic);
			//holder.destanceunittextview.setTypeface(robotoBoldCondensedItalic);
			//holder.time_text.setTypeface(robotoBoldCondensedItalic);
			//holder.amount.setTypeface(robotoBoldCondensedItalic);
			//holder.status.setTypeface(robotoBoldCondensedItalic);

			/*if (getItem(position).getStatCode()==6)
			{
				holder.appointmentstatus.setVisibility(View.GONE);
				holder.drop_location.setVisibility(View.VISIBLE);
			}
			else if (getItem(position).getStatCode()==7)
			{
				holder.appointmentstatus.setVisibility(View.GONE);
				holder.drop_location.setVisibility(View.VISIBLE);
			}
			else if (getItem(position).getStatCode()==9)
			{
				holder.appointmentstatus.setVisibility(View.VISIBLE);
				holder.drop_location.setVisibility(View.GONE);
				//holder.arrowimageview.setVisibility(View.GONE);
			}
			else 
			{
				holder.appointmentstatus.setVisibility(View.GONE);
				holder.drop_location.setVisibility(View.VISIBLE);
			}*/

			//holder.pick_location.setTextColor(android.graphics.Color.rgb(0, 102, 102));
			//holder.passenger_name.setTextColor(android.graphics.Color.rgb(51, 51, 51));
			//holder.destanceunittextview.setTextColor(android.graphics.Color.rgb(153, 153, 153));
			//holder.time_text.setTextColor(android.graphics.Color.rgb(153, 153, 153));
			/*holder.drop_location.setOnClickListener(new OnClickListener() 
			{

				@Override
				public void onClick(View view) 
				{
					if (getItem(position).getStatCode()!=7)
					{
						//selectChoice(getItem(position).getPhone());
					}
					else 
					{
						Toast.makeText(getActivity(), "Appointment already completed", Toast.LENGTH_SHORT).show();
					}

					//respondAppointment(getItem(position),2,position);
				}

			});*/

			return convertView;
		}

		class ViewHolder 
		{
			//android.widget.ImageView imageview;
			TextView  passenger_name,BookingId;
			TextView pick_location;
			TextView status;
			TextView drop_location;
			TextView amount,amount_text;
			//RelativeLayout petientnameandtimelayout;
			//LinearLayout appointmentitemmainlayout;
			TextView time_text;
			//TextView destanceunittextview;
		}

	}

	/*private void selectChoice(final String phoneNo)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(phoneNo);

		builder.setPositiveButton("No",
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();

			}
		});
		builder.setNegativeButton("Call",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				Intent dialIntent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNo));
				startActivity(dialIntent);
			}
		});

		AlertDialog	 alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}*/
	private void setCustomResourceForDates() 
	{
		Calendar cal = Calendar.getInstance();

		// Min date is last 7 days
		cal.add(Calendar.DATE, -18);
		Date blueDate = cal.getTime();

		// Max date is next 7 days
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 16);
		Date greenDate = cal.getTime();

		if (caldroidFragment != null) 
		{
			caldroidFragment.setBackgroundResourceForDate(R.color.blue,
					blueDate);
			caldroidFragment.setBackgroundResourceForDate(R.color.green,
					greenDate);
			caldroidFragment.setTextColorForDate(R.color.white, blueDate);
			caldroidFragment.setTextColorForDate(R.color.white, greenDate);
		}
	}
	/**
	 * Save current states of the Caldroid here
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);

		if (caldroidFragment != null)
		{
			caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		AppointmentDetailList appointmentDetailList= (AppointmentDetailList)arg0.getItemAtPosition(arg2);
		AppointmentDetailData appointmentDetailData=new AppointmentDetailData();
		appointmentDetailData.setfName(appointmentDetailList.getFname());

		Intent intent=new Intent(getActivity(),BookingHistory.class);

		sessionManager.setEMAIL_HISTORY(appointmentDetailList.getEmail());
		sessionManager.setAPPT_DATE_HISTORY(appointmentDetailList.getApptDt());

		//appointmentDetailList.setAppointmentDetailData(appointmentDetailData);
		//Bundle bundle=new Bundle();
		//bundle.putInt("selectedindex", arg2);
		//bundle.putSerializable(VariableConstants.APPOINTMENT, appointmentDetailList);
		//intent.putExtras(bundle);
		startActivity(intent);
		/*else if (appointmentDetailList.getStatCode()==7)
		{
			//intent=new Intent(getActivity(), PassengerDroppedActivity.class);
			return;
		}
		else if (appointmentDetailList.getStatCode()==8) 
		{
			return;
		}
		else if (appointmentDetailList.getStatCode()==9) 
		{
			return;
		}
		else
		{
			//intent=new Intent(getActivity(), HomeFragment.class);
			ArrayList<AppointmentDetailList>liAppointmentDetailLists=appointmentDetailLists;
			appointmentDetailList.setLiAppointmentDetailLists(liAppointmentDetailLists);
		}
        appointmentDetailList.setAppointmentDetailData(appointmentDetailData);
		Bundle bundle=new Bundle();
		bundle.putInt("selectedindex", arg2);
		bundle.putSerializable(VariableConstants.APPOINTMENT, appointmentDetailList);
		intent.putExtras(bundle);
		startActivity(intent);*/
	}

	@Override
	public void onDetach() 
	{
		super.onDetach();

		try 
		{
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} 
		catch (NoSuchFieldException e) 
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e) 
		{
			throw new RuntimeException(e);
		}
	}

}
