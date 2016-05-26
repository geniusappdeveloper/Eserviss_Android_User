package com.app.taxisharingDriver;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.taxisharingDriver.response.ProfileData;
import com.app.taxisharingDriver.response.ProfileDetailsData;
import com.app.taxisharingDriver.utility.ConnectionDetector;
import com.app.taxisharingDriver.utility.Scaler;
import com.app.taxisharingDriver.utility.SessionManager;
import com.app.taxisharingDriver.utility.Utility;
import com.app.taxisharingDriver.utility.VariableConstants;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class Profile_Fragment extends Fragment implements OnClickListener
{

	//private TextView avarageratingtextview;
	private TextView driverLicence ,expiryDate, totalEarning,monthEarning,weekEarning, todayEarning,lastBill,totalBooking;

	private TextView firstName,lastName,email,mobileNo,carMake,carType,seatingCapacity,licensePlate,insuranse,car_exp;
	private ImageView profile_pic;
	private RatingBar ratingBar;
	private String currencySymbol = VariableConstants.CURRENCY_SYMBOL;
	private MainActivity activityDrower;
	private SessionManager sessionManager;

	@Override
	public void onCreate(android.os.Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		Utility utility=new Utility();
		ConnectionDetector connectionDetector=new ConnectionDetector(getActivity());
		sessionManager = new SessionManager(getActivity());
		sessionManager.setIsFlagForOther(true);
		/*Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
		List<Address> addresses;
		try 
		{
			addresses = geocoder.getFromLocation(sessionManager.getDriverCurrentLat(), sessionManager.getDriverCurrentLongi(), 1);
			Address obj = addresses.get(0);
			currencySymbol = Currency.getInstance(obj.getLocale()).getSymbol();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}*/
		
		if (connectionDetector.isConnectingToInternet()) 
		{
			getUserProfile();

		}
		else 
		{
			utility.displayMessageAndExit(getActivity(),getResources().getString(R.string.Pleasewaitmessage),getResources().getString(R.string.internetconnectionmessage));
		}


		activityDrower=(MainActivity)getActivity();

		TextView titleTextview = activityDrower.getTitleTextview();
		titleTextview.setText(getResources().getString(R.string.myprofile));
	}
	private void getUserProfile()
	{ 
		SessionManager sessionManager=new SessionManager(getActivity());
		Utility utility=new Utility();
		String sessionToken=sessionManager.getSessionToken();
		String deviceid=Utility.getDeviceId(getActivity());
		String currentDate=utility.getCurrentGmtTime();
		final String mparams[]={sessionToken,deviceid,currentDate};
		final ProgressDialog mdialog;
		mdialog=Utility.GetProcessDialog(getActivity());
		mdialog.setMessage(getResources().getString(R.string.Pleasewaitmessage));
		mdialog.show();
		mdialog.setCancelable(false);
		RequestQueue queue = Volley.newRequestQueue(getActivity());  // this = context
		String	url = VariableConstants.getProfileinfo_url;
		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>()
				{
			@Override
			public void onResponse(String response) 
			{
				// response
				Utility.printLog("ProfileResponse"+response);
				ProfileData  profileData;
				Gson gson = new Gson();

				profileData=gson.fromJson(response, ProfileData.class);
				Utility.printLog("ProfileResponse"+response);

				if (mdialog!=null)
				{
					mdialog.dismiss();
					mdialog.cancel();
				}
				try
				{						
					if (profileData.getErrFlag()==0&&profileData.getErrNum()==21)
					{
						ProfileDetailsData profileDetailsData  = profileData.getData();
						// 21 -> (0) Got the details!
						Utility utility=new Utility();
						String imagehosturl=utility.getImageHostUrl(getActivity());
						String Imageurl=imagehosturl+profileDetailsData.getpPic();
						String firstNamestr=profileDetailsData.getfName();
						String lastNamestr=profileDetailsData.getlName();
						String mobilenostr=profileDetailsData.getMobile();
						String emailaddressstr=profileDetailsData.getEmail();
						String licPlateNumstr = profileDetailsData.getLicPlateNum();
						String carTypestr= profileDetailsData.getVehicleType();
						String carMakestr = profileDetailsData.getVehMake();
						String seatingCapacitystr = profileDetailsData.getSeatCapacity();
						String insuransestr = profileDetailsData.getVehicleInsuranceNum();



						String driverLicstr=profileDetailsData.getLicNo();
						String expirydatestr = profileDetailsData.getLicExp();
						String totalEarningstr,monthEarstr,weekEarstr,todayEar,lastBillstr,totalBookingstr;
						if (currencySymbol !=null) 
						{
							totalEarningstr = currencySymbol+profileDetailsData.getTotalAmt();
							monthEarstr = currencySymbol +profileDetailsData.getMonthAmt();
							weekEarstr = currencySymbol +profileDetailsData.getWeekAmt();
							todayEar = currencySymbol +profileDetailsData.getTodayAmt();
							lastBillstr = currencySymbol +profileDetailsData.getLastBilledAmt();
						}
						else 
						{
							totalEarningstr = profileDetailsData.getTotalAmt();
							monthEarstr = profileDetailsData.getMonthAmt();
							weekEarstr = profileDetailsData.getWeekAmt();
							todayEar = profileDetailsData.getTodayAmt();
							lastBillstr = profileDetailsData.getLastBilledAmt();
						}
						
						totalBookingstr = profileDetailsData.getTotRats();

						double arrayOfscallingfactor[]=Scaler.getScalingFactorwithDpConvesion(getActivity());
						int topstriplayoutmarigin = 300; // margin in dips
						int topstripMargin = (int)Math.round(topstriplayoutmarigin*arrayOfscallingfactor[1]);//(int)(topstriplayoutmarigin * d); // margin in pixels
						Picasso.with(getActivity()) //
						.load(Imageurl) //
						.placeholder(R.drawable.default_user) //
						.error(R.drawable.default_user)/*.fit()*/
						.resize(topstripMargin,topstripMargin)	 //
						.into(profile_pic);

						firstName.setText(""+firstNamestr);
						lastName.setText(""+lastNamestr);
						mobileNo.setText(mobilenostr);
						email.setText(emailaddressstr);
						carMake.setText(carMakestr);
						carType.setText(carTypestr);
						licensePlate.setText(licPlateNumstr);
						seatingCapacity.setText(seatingCapacitystr);
						insuranse.setText(insuransestr); 
						driverLicence.setText(driverLicstr);
						expiryDate.setText(expirydatestr);
						totalEarning.setText(totalEarningstr);
						monthEarning.setText(monthEarstr);
						weekEarning.setText(weekEarstr);
						todayEarning.setText(todayEar);
						lastBill.setText(lastBillstr);
						totalBooking.setText(totalBookingstr);
						car_exp.setText(profileDetailsData.getVehicleInsuranceExp());
						//avarageratingtextview.setText("   "+getResources().getString(R.string.rating)+"  :"+profileDetailsData.getAvgRate());
						ratingBar.setRating(profileDetailsData.getAvgRate()); 

					}
					else if (profileData.getErrFlag()==1 && profileData.getErrNum()==3)
					{

						// 3 -> (1) Error occurred while processing your request.
						ErrorMessage(getResources().getString(R.string.messagetitle),profileData.getErrMsg(),true);
					}
					else if (profileData.getErrFlag()==1 && profileData.getErrNum()==7)
					{
						//7 -> (1) Invalid token, please login or register.
						ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle),profileData.getErrMsg());
					}
					else if (profileData.getErrFlag() == 1 && profileData.getErrNum()==6)
					{
						//6-> (1)  Session token expired, please login.
						ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle),profileData.getErrMsg());
					}
					else if (profileData.getErrFlag() == 1 && profileData.getErrNum()==101)
					{
						ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle),profileData.getErrMsg());
					}
					else if (profileData.getErrFlag() == 1 && profileData.getErrNum()==99)
					{
						ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle),profileData.getErrMsg());
					}
					else if (profileData.getErrFlag() == 1 && profileData.getErrNum()==1)
					{
						//1 -> (1) Mandatory field missing
						ErrorMessage(getResources().getString(R.string.messagetitle),profileData.getErrMsg(),true);
					}
					else if(profileData.getErrNum()==6 || profileData.getErrNum()==7|| profileData.getErrNum()==96)
					{
						ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle), profileData.getErrMsg());
					}
					else if (profileData.getErrFlag()==1&&profileData.getErrNum()==94)
					{
						//94-> (1)  Your account is been deactivated by our admin, please write an email to know more
						ErrorMessageForInvalidOrExpired(getResources().getString(R.string.messagetitle),profileData.getErrMsg());
					}
				} 
				catch (Exception e) 
				{
					ErrorMessage(getResources().getString(R.string.messagetitle),getResources().getString(R.string.servererror),true);
				}

			}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error) 
					{
						if (mdialog!=null)
						{
							mdialog.dismiss();
							mdialog.cancel();
						}
						ErrorMessage(getResources().getString(R.string.messagetitle), getResources().getString(R.string.messagetitle), false);
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

		builder.setPositiveButton(getResources().getString(R.string.cancelbutton),
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
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

	@Override
	public View onCreateView(android.view.LayoutInflater inflater,android.view.ViewGroup container,android.os.Bundle savedInstanceState)
	{
		View view  = inflater.inflate(R.layout.profile_layout, null);
		initlayoutid(view);
		return view;
	}

	private void initlayoutid(View view)
	{
		profile_pic=(ImageView)view.findViewById(R.id.su_one_profile_image);


		car_exp = (TextView)view.findViewById(R.id.car_exp);
		firstName=(TextView)view.findViewById(R.id.first_name);
		lastName=(TextView)view.findViewById(R.id.last_name);
		mobileNo=(TextView)view.findViewById(R.id.Mobile_no);

		email=(TextView)view.findViewById(R.id.email);
		driverLicence=(TextView)view.findViewById(R.id.driverlicence);
		expiryDate=(TextView)view.findViewById(R.id.expiredate);

		carMake=(TextView)view.findViewById(R.id.car_make);
		carType=(TextView)view.findViewById(R.id.car_type);
		seatingCapacity=(TextView)view.findViewById(R.id.seatingcapacity);
		licensePlate=(TextView)view.findViewById(R.id.licenseplate);
		insuranse=(TextView)view.findViewById(R.id.insurance);

		totalEarning=(TextView)view.findViewById(R.id.total_earning);
		monthEarning=(TextView)view.findViewById(R.id.monthearning);
		weekEarning=(TextView)view.findViewById(R.id.weekearning);
		todayEarning=(TextView)view.findViewById(R.id.todayearning);
		lastBill=(TextView)view.findViewById(R.id.lastbill);
		totalBooking=(TextView)view.findViewById(R.id.totalbooking);

		ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);
		
		ratingBar.setEnabled(false);
		//avarageratingtextview=(TextView)view.findViewById(R.id.avarageratingtextview);

	
		int alpha =204;
		Typeface robotoBoldCondensedItalic = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Zurich Light Condensed.ttf");

		firstName.setTypeface(robotoBoldCondensedItalic);
		firstName.setTextColor(Color.argb(alpha, 51, 51, 51));

		lastName.setTypeface(robotoBoldCondensedItalic);
		lastName.setTextColor(Color.argb(alpha, 51, 51, 51));

		mobileNo.setTypeface(robotoBoldCondensedItalic);
		mobileNo.setTextColor(Color.argb(alpha, 51, 51, 51));


		email.setTypeface(robotoBoldCondensedItalic);
		email.setTextColor(Color.argb(alpha, 51, 51, 51));

		driverLicence.setTypeface(robotoBoldCondensedItalic);
		driverLicence.setTextColor(Color.argb(alpha, 51, 51, 51));

		expiryDate.setTypeface(robotoBoldCondensedItalic);
		expiryDate.setTextColor(Color.argb(alpha, 51, 51, 51));

		totalEarning.setTypeface(robotoBoldCondensedItalic);
		totalEarning.setTextColor(Color.argb(alpha, 51, 51, 51));


		monthEarning.setTypeface(robotoBoldCondensedItalic);
		monthEarning.setTextColor(Color.argb(alpha, 51, 51, 51));

		weekEarning.setTypeface(robotoBoldCondensedItalic);
		weekEarning.setTextColor(Color.argb(alpha, 51, 51, 51));

		todayEarning.setTypeface(robotoBoldCondensedItalic);
		todayEarning.setTextColor(Color.argb(alpha, 51, 51, 51));

		lastBill.setTypeface(robotoBoldCondensedItalic);
		lastBill.setTextColor(Color.argb(alpha, 51, 51, 51));


		totalBooking.setTypeface(robotoBoldCondensedItalic);
		totalBooking.setTextColor(Color.argb(alpha, 51, 51, 51));

		carMake.setTypeface(robotoBoldCondensedItalic);
		carMake.setTextColor(Color.argb(alpha, 51, 51, 51));

		carType.setTypeface(robotoBoldCondensedItalic);
		carType.setTextColor(Color.argb(alpha, 51, 51, 51));

		seatingCapacity.setTypeface(robotoBoldCondensedItalic);
		seatingCapacity.setTextColor(Color.argb(alpha, 51, 51, 51));

		licensePlate.setTypeface(robotoBoldCondensedItalic);
		licensePlate.setTextColor(Color.argb(alpha, 51, 51, 51));

		insuranse.setTypeface(robotoBoldCondensedItalic);
		insuranse.setTextColor(Color.argb(alpha, 51, 51, 51));


		//avarageratingtextview.setTypeface(robotoBoldCondensedItalic);
		//avarageratingtextview.setTextColor(Color.argb(alpha, 51, 51, 51));

	}

	@Override
	public void onClick(View v) 
	{

	}

}
