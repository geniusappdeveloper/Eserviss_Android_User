package com.eserviss.passenger.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eserviss.passenger.pojo.ContactsAdapPojo;
import com.eserviss.passenger.pojo.ContactsPojo;
import com.egnyt.eserviss.R;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;

public class InviteFragment extends Fragment implements OnClickListener
{
	private  View view;
	private ArrayList<ContactsPojo> contact_list;
	private ArrayList<ContactsAdapPojo>rowItems;
	private ListView contact_listView;
	private String emailOrMessage[]={"Email","Message"};
	private ImageView facebook_share,twitter_share,message_share,email_share;
	private TextView coupon_code;
	private SessionManager session;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ResideMenuActivity.invoice_driver_tip.setVisibility(View.INVISIBLE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		if(view != null) 
		{
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}

		try 
		{
			view = inflater.inflate(R.layout.invite_screen, container, false);
			//view = inflater.inflate(R.layout.invite_contact, container, false);
			initialize();

		} catch (InflateException e)
		{
			Utility.printLog("onCreateView  InflateException "+e);
		}
		
		session = new SessionManager(getActivity());
		coupon_code.setText(session.getCouponCode());
		Utility.printLog("coupon code"+session.getCouponCode());
		if(Utility.isNetworkAvailable(getActivity()))
		{
		}
		else
		{
			Intent homeIntent=new Intent("com.threembed.roadyo.internetStatus");
			homeIntent.putExtra("STATUS", "0");
			getActivity().sendBroadcast(homeIntent);
		}




		return view;
	}

	private void initialize()
	{
		facebook_share = (ImageView)view.findViewById(R.id.facebook_share);
		twitter_share = (ImageView)view.findViewById(R.id.twitter_share);
		message_share = (ImageView)view.findViewById(R.id.message_share);
		email_share = (ImageView)view.findViewById(R.id.email_share);
		coupon_code = (TextView)view.findViewById(R.id.share_code);

		facebook_share.setOnClickListener(this);
		twitter_share.setOnClickListener(this);
		message_share.setOnClickListener(this);
		email_share.setOnClickListener(this);
		//==============================change April========================================
		Typeface roboto_condensed = Typeface.createFromAsset(getActivity().getAssets(),"fonts/BebasNeue.otf");
		TextView txt_share_code_one= (TextView) view.findViewById(R.id.txt_share_code_one);
		TextView share_text= (TextView) view.findViewById(R.id.share_text);
		TextView share_code= (TextView) view.findViewById(R.id.share_code);
		TextView text_eseerviss= (TextView) view.findViewById(R.id.text_eseerviss);
		TextView text_fb= (TextView) view.findViewById(R.id.text_fb);
		TextView text_tw= (TextView) view.findViewById(R.id.text_tw);
		TextView text_message= (TextView) view.findViewById(R.id.text_message);
		TextView text_email= (TextView) view.findViewById(R.id.text_email);
		TextView txt_share_code= (TextView) view.findViewById(R.id.txt_share_code);

		txt_share_code.setTypeface(roboto_condensed);
		txt_share_code_one.setTypeface(roboto_condensed);
		share_code.setTypeface(roboto_condensed);
		share_text.setTypeface(roboto_condensed);
		text_eseerviss.setTypeface(roboto_condensed);
		coupon_code.setTypeface(roboto_condensed);
		text_email.setTypeface(roboto_condensed);
		text_fb.setTypeface(roboto_condensed);
		text_message.setTypeface(roboto_condensed);
		text_tw.setTypeface(roboto_condensed);
		text_eseerviss.setTypeface(roboto_condensed);


		//=========================================================================
	}

	@Override
	public void onClick(View v) 
	{
		if(v.getId() ==  R.id.facebook_share)
		{
			String urlToShare = "https://www.facebook.com/roadyo";
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, urlToShare);
			boolean facebookAppFound = false;
			List<ResolveInfo> matches = getActivity().getPackageManager().queryIntentActivities(intent, 0);
			for (ResolveInfo info : matches) {
				if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook")) {
					intent.setPackage(info.activityInfo.packageName);
					facebookAppFound = true;
					break;
				}
			}

			if(facebookAppFound)
			{
				startActivity(intent);
			}
			else
			{
				Intent shareIntent = new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello,"+"\n"+"  "+"Use my  referral code,"+" "+session.getCouponCode()+" "+"to signup on"+" "+getResources().getString(R.string.app_name)
						+" "+"and you will  get an exclusive promo code via email."
						);
				startActivity(Intent.createChooser(shareIntent, "Share your promo code"));
			}

		}
		else if(v.getId() ==  R.id.twitter_share)
		{
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, "Hello,"+"\n"+"  "+"Use my  referral code,"+" "+session.getCouponCode()+" "+"to signup on"+" "+getResources().getString(R.string.app_name)
					+" "+"and you will  get an exclusive promo code via email."
					);
			boolean facebookAppFound = false;
			List<ResolveInfo> matches = getActivity().getPackageManager().queryIntentActivities(intent, 0);
			for (ResolveInfo info : matches) {
				if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
					intent.setPackage(info.activityInfo.packageName);
					facebookAppFound = true;
					break;
				}
			}

			if(facebookAppFound)
			{
				startActivity(intent);
			}
			else
			{
				Intent shareIntent = new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello,"+"\n"+"  "+"Use my  referral code,"+" "+session.getCouponCode()+" "+"to signup on"+" "+getResources().getString(R.string.app_name)
						+" "+"and you will  get an exclusive promo code via email."
						);
				startActivity(Intent.createChooser(shareIntent, "Share your promo code"));
			}

		}
		else if(v.getId() ==  R.id.message_share)
		{
			/*Intent intentsms = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + "" ) );
			intentsms.putExtra(Intent.EXTRA_TEXT, "Hello,"+"\n"+"  "+"Use my  referral code,"+" "+session.getCouponCode()+" "+"to signup on"+" "+getResources().getString(R.string.app_name)
					+" "+"and you will  get an exclusive promo code via email."
					);
			startActivity( intentsms );*/
			String smsBody="Hello,"+"\n"+"  "+"Use my  referral code,"+" "+session.getCouponCode()+" "+"to signup on"+" "+getResources().getString(R.string.app_name)
					+" "+"and you will  get an exclusive promo code via email.";

			Intent intentsms = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:"));
			intentsms.putExtra( "sms_body", smsBody);
			startActivity( intentsms );
		}
		else if(v.getId() ==  R.id.email_share)
		{
			Intent email = new Intent(Intent.ACTION_SEND);
			//email.putExtra(Intent.EXTRA_EMAIL, new String[]{"youremail@yahoo.com"});		  
			email.putExtra(Intent.EXTRA_SUBJECT,getResources().getString(R.string.registeron)+" " +getResources().getString(R.string.app_name));
			email.putExtra(Intent.EXTRA_TEXT, "Hello,"+"\n"+"  "+"Use my  referral code,"+" "+session.getCouponCode()+" "+"to signup on"+" "+getResources().getString(R.string.app_name)
					+" "+"and you will  get an exclusive promo code via email."
					);
			email.setType("message/rfc822");
			startActivity(Intent.createChooser(email, "Choose an Email client :"));
		}
	}

	class BackgroundReadContacts extends AsyncTask<String,Void,String>
	{
		ProgressDialog dialogL;

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			dialogL= Utility.GetProcessDialog(getActivity());

			if (dialogL!=null) 
			{
				dialogL.show();
			}
		}



		@Override
		protected String doInBackground(String... params) 
		{


			readContacts();



			return null;
		}

		private void readContacts()
		{
			ContentResolver cr =getActivity().getContentResolver();
			Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);


			while (cursor.moveToNext()) 
			{
				ContactsPojo userInfoObj=new ContactsPojo();
				String  displayName,emailAddress, phoneNumber;

				//Name
				displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));     
				userInfoObj.setContactName(displayName);

				//Email
				String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
				Cursor emails = cr.query(Email.CONTENT_URI,null,Email.CONTACT_ID + " = " + id, null, null);

				if(emails.moveToNext()) 
				{ 
					emailAddress = emails.getString(emails.getColumnIndex(Email.DATA));
					if (emailAddress!=null) {
						userInfoObj.setEmail_addr(emailAddress);
					}



				}
				emails.close();


				//Phone
				if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
				{
					Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{id}, null);
					Utility.printLog( "readContacts cursor length pCur........"+pCur.getCount());

					if(pCur.moveToNext()) 
					{
						phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						phoneNumber=phoneNumber.replaceAll(" ", "");
						userInfoObj.setPh_numb(phoneNumber);
					}
					pCur.close();
				}
				//Add all to list
				contact_list.add(userInfoObj); 
			}

			cursor.close();
		}



		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (dialogL!=null) 
			{
				dialogL.dismiss();
			}

			rowItems=new ArrayList<ContactsAdapPojo>();

			for(int i=0;i<contact_list.size();i++)
			{
				Utility.printLog("Contact Name: "+contact_list.get(i).getContactName());
				Utility.printLog("Contact phone: "+contact_list.get(i).getPh_numb());
				Utility.printLog("Contact email: "+contact_list.get(i).getEmail_addr());
				Utility.printLog("Contact ------------------------ ");

				ContactsAdapPojo item=new ContactsAdapPojo(contact_list.get(i).getContactName(),contact_list.get(i).getPh_numb(),contact_list.get(i).getEmail_addr());

				rowItems.add(item);

			}
			Collections.sort(rowItems, new Comparator<ContactsAdapPojo>() 
					{
				public int compare(ContactsAdapPojo v1, ContactsAdapPojo v2)
				{
					return v1.getContactName().compareTo(v2.getContactName());
				}
					});

			CustomAdapterInvite adapter = new CustomAdapterInvite(getActivity(),
					R.layout.contact_list_row, rowItems);
			contact_listView.setAdapter(adapter);

		}

	}


	class CustomAdapterInvite extends ArrayAdapter<ContactsAdapPojo>
	{
		Context context;
		Typeface typeFace2=Typeface.createFromAsset(getActivity().getAssets(),"fonts/BebasNeue.otf");
		public CustomAdapterInvite(Context context, int resourceId,
				List<ContactsAdapPojo> items) {
			super(context, resourceId, items);
			this.context = context;
		}


		private class ViewHolder
		{
			TextView name;
			Button invite_btn;
			/*TextView ph_numb;
			TextView email_id;*/
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			ViewHolder holder = null;
			final ContactsAdapPojo rowItem = getItem(position);

			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			if (convertView == null)
			{
				convertView = mInflater.inflate(R.layout.contact_list_row, null);
				holder = new ViewHolder();

				holder.name=(TextView)convertView.findViewById(R.id.contact_name);
				holder.name.setTypeface(typeFace2);
				holder.invite_btn=(Button)convertView.findViewById(R.id.invite_btn);
				holder.invite_btn.setTypeface(typeFace2);
				convertView.setTag(holder);
			}
			else
				holder = (ViewHolder) convertView.getTag();

			holder.invite_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					selectOptionMenu(rowItem.getPh_numb(),rowItem.getEmail_addr());
				}
			});

			Utility.printLog("null holder.name "+holder.name);
			Utility.printLog("null rowItem.getContactName() "+rowItem.getContactName());
			holder.name.setText(rowItem.getContactName());


			return convertView;
		}





	}

	private void selectOptionMenu(final String phNumb,final String email_addr) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Select");

		builder.setItems(emailOrMessage, new DialogInterface.OnClickListener() 
		{

			@Override
			public void onClick(DialogInterface optiondialog, int which) 
			{
				if (emailOrMessage[which].equals(emailOrMessage[0])) 
				{
					Intent emailIntent = new Intent(Intent.ACTION_VIEW);
					Uri data = Uri.parse("mailto:?subject="
							+ "Invite to join PrevPatient App!"
							+ "&body="
							+ "Please download the PocketCab App from www.PocketCab.us"
							+ "&to=" 
							+ email_addr);

					emailIntent.setData(data);
					startActivity(emailIntent);
					optiondialog.dismiss();
				}
				if (emailOrMessage[which].equals(emailOrMessage[1])) 
				{
					/*startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:Please download the SneekPeek App from www.sneekpeek.com"
	                        + phNumb)));*/

					/*Intent smsIntent = new Intent(Intent.ACTION_VIEW);

			        smsIntent.putExtra("sms_body", "Please download the PrevPatient App from www.prevpatient.com"); 
			        smsIntent.putExtra("address", phNumb);
			        smsIntent.setType("vnd.android-dir/mms-sms");

			        startActivity(smsIntent);
			    	optiondialog.dismiss();*/


					Intent intentsms = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + ""+phNumb ) );
					intentsms.putExtra( "sms_body", "Please download the PrevPatient App from www.PocketCab.us" );
					startActivity( intentsms );
					optiondialog.dismiss();
				}
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		});
		AlertDialog  alert = builder.create();
		alert.setCancelable(true);
		alert.show();

	}







}
