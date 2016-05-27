package com.eserviss.passenger.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.egnyt.eserviss.MainActivity;
import com.egnyt.eserviss.R;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.eserviss.passenger.pojo.AppointmentResponse;
import com.eserviss.passenger.pojo.AppointmentsPojo;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.UltilitiesDate;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

public class Appointments extends Fragment implements OnItemClickListener{

	private View view;
	private ListView listView;
	private List<AppointmentsPojo> rowItems = new ArrayList<AppointmentsPojo>();
	private CustomListViewAdapter adapter;
	private AppointmentResponse response;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ProgressDialog dialogL;
	private String appointmentMonth,appointmentYear;
	private SessionManager session;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (view != null) 
		{
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}
		try 
		{
			view = inflater.inflate(R.layout.appointment, container, false);
            

		} catch (InflateException e)
		{
			/* map is already there, just return view as it is */
			Log.e("", "onCreateView  InflateException "+e);
		}

		session =  new SessionManager(getActivity());

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.profile_image_frame)
		.showImageForEmptyUri(R.drawable.profile_image_frame)
		.showImageOnFail(R.drawable.profile_image_frame)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(0))
		.build();

		initializeVariables(view);


		SetDate();
		
		if(Utility.isNetworkAvailable(getActivity()))
		{
			BackgroundAppointmentMonthChange_Volley(appointmentYear+"-"+appointmentMonth);
		}
		else
		{
			Intent homeIntent=new Intent("com.threembed.roadyo.internetStatus");
			homeIntent.putExtra("STATUS", "0");
			getActivity().sendBroadcast(homeIntent);
		}

		

		return view;
	}

	private void initializeVariables(View view2) 
	{
		ResideMenuActivity.invoice_driver_tip.setVisibility(View.GONE);
		listView=(ListView)view2.findViewById(R.id.list_appointment);
		listView.setOnItemClickListener(this);
		adapter=new CustomListViewAdapter(getActivity(),R.layout.appointment_list_row, rowItems);
		listView.setAdapter(adapter);
	}


	private void SetDate()
	{
		Utility utility=new Utility();
		String curenttime=utility.getCurrentGmtTime();
		Utility.printLog("MasterJobStarted curenttime="+curenttime);
		//Local Time_date 2014-12-02 13:10:52

		String[] date_time = curenttime.split(" ");
		String[] date = date_time[0].split("-");

		String year = date[0];
		String month = date[1];

		appointmentYear = year;
		appointmentMonth = month;

		/*
		  String[] time = date_time[1].split(":");
		  String day = date[2];
		  String hour = time[0];
		  String min = time[1];

		  booking_date = year+"-"+month+"-"+day;
		  booking_time = hour+":"+min+":00";

		String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

		apt_date.setText(day+" "+MONTHS[Integer.parseInt(month)-1]+" | ");
		date_btn.setText(day+" "+MONTHS[Integer.parseInt(month)-1]);

		if(Integer.parseInt(hour)>=12)
		{
			String later_date = hour+":"+min+" PM";
			apt_time.setText(later_date);
			time_btn.setText(later_date);
		}
		else
		{
			String later_date = hour+":"+min+" AM";
			apt_time.setText(later_date);
			time_btn.setText(later_date);
		}*/
	}


	class CustomListViewAdapter extends ArrayAdapter<AppointmentsPojo> {


		Context context;
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
		// Typeface typeFace1=Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaNeue-Light.otf");
		Typeface typeFace2=Typeface.createFromAsset(getActivity().getAssets(),"fonts/BebasNeue.otf");
		Typeface typeFace3=Typeface.createFromAsset(getActivity().getAssets(),"fonts/BebasNeue.otf");

		public CustomListViewAdapter(Context context, int resourceId,
				List<AppointmentsPojo> items) {
			super(context, resourceId, items);
			this.context = context;
	
		}


		private class ViewHolder
		{
			ImageView driver_pic;
			TextView name;
			TextView drop;
			TextView time;
			TextView pickup;
			TextView amount;
			TextView distance;
			TextView status;
			RelativeLayout drop_layout;
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			LayoutInflater mInflater = (LayoutInflater) context
			.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			ViewHolder holder = null;
			AppointmentsPojo rowItem = getItem(position);

			/*Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
			    view.startAnimation(animation);
			    lastPosition = position;*/

			if(convertView == null)
			{
				convertView = mInflater.inflate(R.layout.appt_list_row_new, null);
				holder = new ViewHolder();

				holder.driver_pic=(ImageView)convertView.findViewById(R.id.driver_profile_pic_appointment);
				holder.name=(TextView)convertView.findViewById(R.id.driver_name_appointment);
				holder.pickup=(TextView)convertView.findViewById(R.id.pickup);
				holder.drop=(TextView)convertView.findViewById(R.id.drop);
				holder.time=(TextView)convertView.findViewById(R.id.duration);
				holder.amount=(TextView)convertView.findViewById(R.id.amount);
				holder.distance=(TextView)convertView.findViewById(R.id.distance);
				holder.status=(TextView)convertView.findViewById(R.id.apt_status);
				holder.drop_layout = (RelativeLayout)convertView.findViewById(R.id.rl_drop);

				holder.distance.setTypeface(typeFace2);
				holder.pickup.setTypeface(typeFace2);
				holder.drop.setTypeface(typeFace2);
				holder.time.setTypeface(typeFace3);
				holder.status.setTypeface(typeFace3);

				convertView.setTag(holder);
			}
			else
				holder = (ViewHolder) convertView.getTag();

			String url=VariableConstants.IMAGE_BASE_URL+rowItem.getPic();
			imageLoader.displayImage(url, holder.driver_pic, options, animateFirstListener);

			holder.name.setText(rowItem.getName());
			holder.pickup.setText(rowItem.getLocation());
			if(rowItem.getDrop_address()==null || rowItem.getDrop_address().isEmpty() )
			{
				holder.drop_layout.setVisibility(View.GONE);
			}
			else
			{
				holder.drop_layout.setVisibility(View.VISIBLE);
				holder.drop.setText(rowItem.getDrop_address());
			}
			double distance=Double.parseDouble(rowItem.getDistance());
			holder.distance.setText(distance+" "+getResources().getString(R.string.distanceUnit));
			holder.time.setText(rowItem.getDate()+" "+rowItem.getTime());
			holder.status.setText(rowItem.getStatus());
			holder.amount.setText(getResources().getString(R.string.currencuSymbol)+" "+rowItem.getAmount()); 

			return convertView;
		}
	}


	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	private void BackgroundAppointmentMonthChange_Volley(final String date)
	{
		dialogL=Utility.GetProcessDialog(getActivity());
		dialogL.setCancelable(false);
		if(dialogL!=null) 
		{
			dialogL.show();
		}

		RequestQueue volleyRequest = Volley.newRequestQueue(getActivity());
		StringRequest myReq = new StringRequest(com.android.volley.Request.Method.POST, 
				VariableConstants.BASE_URL+"process.php/getSlaveAppointments",

				new Listener<String>() {

			@Override
			public void onResponse(String response) {

				if (dialogL!=null) 
				{
					dialogL.dismiss();
					dialogL=null;
				}

				Utility.printLog("AppointmentResponse: "+response);

				fetchData(response);
			}

			private void fetchData(String jsonResponse) {
				try
				{
					Gson gson = new Gson();
					response=gson.fromJson(jsonResponse,AppointmentResponse.class);

					if(response!=null)
					{
						rowItems.clear();

						if(response.getErrFlag().equals("0") && isAdded())
						{
							Log.i("","response.getAppointments().size() "+response.getAppointments().size());

							for(int i=0;i<response.getAppointments().size();i++)
							{
								Log.i("","STARTING OF OUTER FOR LOOP i="+i);

								for(int j=0;j<response.getAppointments().get(i).getAppt().size();j++)
								{
									String name,date,status,pay_status,pic,time,email,phone,pickup_address,notes,apt_date,status_code,amount,drop_address,distance,cancel_status;

									name=response.getAppointments().get(i).getAppt().get(j).getFname();
									date=response.getAppointments().get(i).getAppt().get(j).getApntDate();
									status=response.getAppointments().get(i).getAppt().get(j).getStatus();
									pay_status=response.getAppointments().get(i).getAppt().get(j).getPayStatus();
									pic=response.getAppointments().get(i).getAppt().get(j).getpPic();
									time=response.getAppointments().get(i).getAppt().get(j).getApntTime();
																	
									email=response.getAppointments().get(i).getAppt().get(j).getEmail();
									phone=response.getAppointments().get(i).getAppt().get(j).getPhone();
									pickup_address=response.getAppointments().get(i).getAppt().get(j).getAddrLine1();
									notes=response.getAppointments().get(i).getAppt().get(j).getNotes();
									apt_date=response.getAppointments().get(i).getAppt().get(j).getApntDt();
									status_code=response.getAppointments().get(i).getAppt().get(j).getStatCode();
									amount=response.getAppointments().get(i).getAppt().get(j).getAmount();
									drop_address=response.getAppointments().get(i).getAppt().get(j).getDropLine1();
									distance=response.getAppointments().get(i).getAppt().get(j).getDistance();
									cancel_status=response.getAppointments().get(i).getAppt().get(j).getCancel_status();

									AppointmentsPojo item=new AppointmentsPojo(name,apt_date,date, status,pay_status,pic,time,email,phone,pickup_address,notes,status_code,amount,drop_address,distance,cancel_status);
									rowItems.add(item);
								}
							}
						}
					 if(response.getErrNum().equals("6") || response.getErrNum().equals("7") || 
								response.getErrNum().equals("94") || response.getErrNum().equals("96"))
						{
							Toast.makeText(getActivity(), response.getErrMsg(),Toast.LENGTH_SHORT).show();
							Intent i = new Intent(getActivity(), MainActivity.class);
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							getActivity().startActivity(i);
							getActivity().overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
						}						else if(isAdded())
						{
							Toast.makeText(getActivity(), response.getErrMsg(),Toast.LENGTH_SHORT).show();
						}

						adapter.notifyDataSetChanged();
					}
					else if(isAdded())
					{
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								getActivity());

						// set title
						alertDialogBuilder.setTitle("Note :");

						// set dialog message
						alertDialogBuilder
						.setMessage("Server Error.Retry!!")
						.setCancelable(false)

						.setNegativeButton("OK",new DialogInterface.OnClickListener() {
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
				catch(Exception e)
				{
					if(isAdded())
					Utility.ShowAlert("Server Error", getActivity());
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
				if(isAdded())
				Toast.makeText(getActivity(), "Error. Try Again!!", Toast.LENGTH_SHORT).show();

			}

		}){
			protected HashMap<String,String> getParams() throws com.android.volley.AuthFailureError {

				SessionManager session=new SessionManager(getActivity());

				Utility utility=new Utility();
				String curenttime=utility.getCurrentGmtTime();
				String dateandTime=UltilitiesDate.getLocalTime(curenttime);
				HashMap<String, String> kvPairs = new HashMap<String, String>();
				kvPairs.put("ent_sess_token",session.getSessionToken() );
				kvPairs.put("ent_dev_id",session.getDeviceId());
				kvPairs.put("ent_appnt_dt",date);
				kvPairs.put("ent_date_time",dateandTime);
				Utility.printLog("params"+kvPairs);
				return kvPairs; 
			
			}
		};
		volleyRequest.add(myReq);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		Utility.printLog("CONTROL INSIDEonItemClick");
		final AppointmentsPojo rowItem=(AppointmentsPojo)arg0.getItemAtPosition(arg2);

		Utility.printLog("onItemClick apt date="+rowItem.getAptDate()+" getEmail="+rowItem.getEmail());
		Utility.printLog("onItemClick getPayStatus="+rowItem.getPayStatus());

		if(rowItem.getStatCode()!=null)
		{   if( rowItem.getStatCode().equals("9") || (rowItem.getStatCode().equals("4") && rowItem.getCancel_status().equals("3")))
		   {
			if(rowItem.getPayStatus().equals("1") || rowItem.getPayStatus().equals("3"))
			{
				Intent intent = new Intent(getActivity(),InvoicePaymentDone.class);
				intent.putExtra("apt_date", rowItem.getAptDate());
				intent.putExtra("apt_email", rowItem.getEmail());
				startActivity(intent);
			}
			else
			{
				Intent intent = new Intent(getActivity(),InvoicePaymentNotDone.class);
				intent.putExtra("apt_date", rowItem.getAptDate());
				intent.putExtra("apt_email", rowItem.getEmail());
				startActivity(intent);
			}
			/*if(rowItem.getStatCode().equals("9"))//completed
			{
				if(rowItem.getPayStatus()==null || (rowItem.getPayStatus().equals("") || rowItem.getPayStatus().equals("0")))
				{
					Utility.ShowAlert(rowItem.getStatus(), getActivity());
				}
				else if(rowItem.getPayStatus().equals("1") || rowItem.getPayStatus().equals("3"))
				{
					Intent intent = new Intent(getActivity(),InvoicePaymentDone.class);
					intent.putExtra("apt_date", rowItem.getAptDate());
					intent.putExtra("apt_email", rowItem.getEmail());
					startActivity(intent);
				}
				else
				{
					Intent intent = new Intent(getActivity(),InvoicePaymentNotDone.class);
					intent.putExtra("apt_date", rowItem.getAptDate());
					intent.putExtra("apt_email", rowItem.getEmail());
					startActivity(intent);
				}
			}
			else if(rowItem.getStatCode().equals("4"))//passenger cancelled
			{
				if(rowItem.getPayStatus()==null || (rowItem.getPayStatus().equals("") || rowItem.getPayStatus().equals("0")))
				{
					Utility.ShowAlert(rowItem.getStatus(), getActivity());
				}
				else if(rowItem.getPayStatus().equals("1") || rowItem.getPayStatus().equals("3"))
				{
					Intent intent = new Intent(getActivity(),InvoicePaymentDone.class);
					intent.putExtra("apt_date", rowItem.getAptDate());
					intent.putExtra("apt_email", rowItem.getEmail());
					startActivity(intent);
				}
				else
				{
					Intent intent = new Intent(getActivity(),InvoicePaymentNotDone.class);
					intent.putExtra("apt_date", rowItem.getAptDate());
					intent.putExtra("apt_email", rowItem.getEmail());
					startActivity(intent);
				}
			}

			else if(rowItem.getStatCode().equals("5"))//driver cancelled
			{
				if(rowItem.getPayStatus()==null || (rowItem.getPayStatus().equals("") || rowItem.getPayStatus().equals("0")))
				{
					Utility.ShowAlert(rowItem.getStatus(), getActivity());
				}
				else if(rowItem.getPayStatus().equals("1") || rowItem.getPayStatus().equals("3"))
				{
					Intent intent = new Intent(getActivity(),InvoicePaymentDone.class);
					intent.putExtra("apt_date", rowItem.getAptDate());
					intent.putExtra("apt_email", rowItem.getEmail());
					startActivity(intent);
				}
				else
				{
					Intent intent = new Intent(getActivity(),InvoicePaymentNotDone.class);
					intent.putExtra("apt_date", rowItem.getAptDate());
					intent.putExtra("apt_email", rowItem.getEmail());
					startActivity(intent);
				}
			}*/
		}
		else{
			Utility.ShowAlert(rowItem.getStatus(), getActivity());
		}
		/*
		if(rowItem.getPayStatus().equals("1"))
		{
			Intent intent = new Intent(getActivity(),InvoicePaymentDone.class);
			intent.putExtra("apt_date", rowItem.getAptDate());
			intent.putExtra("apt_email", rowItem.getEmail());
			startActivity(intent);

		}
		else if(rowItem.getPayStatus().equals("0"))
		{
			Intent intent = new Intent(getActivity(),InvoicePaymentNotDone.class);
			intent.putExtra("apt_date", rowItem.getAptDate());
			intent.putExtra("apt_email", rowItem.getEmail());
			startActivity(intent);
		}*/
	}
	}
	@Override
	public void onResume() 
	{
		super.onResume();
		SetDate();
    	ResideMenuActivity.invoice_driver_tip.setVisibility(View.INVISIBLE);
	}

}
