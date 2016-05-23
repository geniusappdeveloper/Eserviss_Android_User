package com.eserviss.passenger.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.egnyt.eserviss.MainActivity;
import com.egnyt.eserviss.R;
import com.eserviss.passenger.pojo.GetCardResponse;
import com.eserviss.passenger.pojo.card_info_pojo;
import com.google.gson.Gson;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.card.payment.CardType;

public class PaymentFragment extends Fragment implements OnClickListener,OnItemClickListener
{
	private View view;
	private ListView card_list;
	private RelativeLayout add_cc_bt,Stripe_Paypal;
	private GetCardResponse response;
	private CustomListViewAdapter adapter;
	private List<card_info_pojo> rowItems;	RelativeLayout add_promo_layout,crd_layout;
	String message ="";
	String showCredit="";
	private ProgressDialog dialogL;
	TextView text_credit_promo,text_open,text_expiry;
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
	}

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
			view = inflater.inflate(R.layout.payments_xml, container, false);
		} catch (InflateException e)
		{
			/* map is already there, just return view as it is */
			Log.e("", "onCreateView  InflateException "+e);
		}

		initializeVariables(view);

		if(Utility.isNetworkAvailable(getActivity()))
		{
			new BackgroundGetCards().execute();
		}
		else
		{
			Intent homeIntent=new Intent("com.threembed.roadyo.internetStatus");
			homeIntent.putExtra("STATUS", "0");
			getActivity().sendBroadcast(homeIntent);
			
			Utility.ShowAlert("No network connection", getActivity());
		}
		
		return view;
	}

	private void initializeVariables(View view2) 
	{
		// TODO Auto-generated method stub
		card_list=(ListView)view2.findViewById(R.id.cards_list_view);//cards_list_view
		rowItems = new ArrayList<card_info_pojo>();
		//================================My change========================
		add_promo_layout= (RelativeLayout) view.findViewById(R.id.add_promo_layout);
		text_open= (TextView) view.findViewById(R.id.text_open);
		text_expiry= (TextView) view.findViewById(R.id.text_expiry);
		crd_layout=(RelativeLayout) view.findViewById(R.id.crd_layout);
		//================================My change========================
		adapter = new CustomListViewAdapter(getActivity(),R.layout.card_list_row, rowItems);
		card_list.setAdapter(adapter);

		LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View fotter_view = inflater.inflate(R.layout.add_card_fotter, null);
		card_list.addFooterView(fotter_view);
		add_cc_bt=(RelativeLayout)fotter_view.findViewById(R.id.add_card_rel_fotter);

		Stripe_Paypal = (RelativeLayout)view2.findViewById(R.id.stripe_paypal);

		add_cc_bt.setOnClickListener(this);
		card_list.setOnItemClickListener(this);

		rowItems = new ArrayList<card_info_pojo>();
		//================================My change========================
		getCredit();
		TextView text_add_card= (TextView) view.findViewById(R.id.text_add_card);
		 text_credit_promo= (TextView) view2.findViewById(R.id.text_credit_promo);
		TextView add_pay= (TextView) view2.findViewById(R.id.add_pay);

		Typeface roboto_condensed = Typeface.createFromAsset(getActivity().getAssets(),"fonts/BebasNeue.otf");
		text_open.setTypeface(roboto_condensed);
		text_add_card.setTypeface(roboto_condensed);
		text_credit_promo.setTypeface(roboto_condensed);
		add_pay.setTypeface(roboto_condensed);
		text_expiry.setTypeface(roboto_condensed);

		add_promo_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),PromocodeApply.class);
				startActivity(intent);
			}
		});
		//================================My change========================

	}

	class BackgroundGetCards extends AsyncTask<String, Void,String>
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

			String url= VariableConstants.BASE_URL+"getCards";
			SessionManager session=new SessionManager(getActivity());


			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();

			Map<String, String> kvPairs = new HashMap<String, String>();
			kvPairs.put("ent_sess_token",session.getSessionToken() );
			kvPairs.put("ent_dev_id",session.getDeviceId());
			kvPairs.put("ent_date_time",curenttime);

			HttpResponse httpResponse = null;
			try {
				httpResponse = Utility.doPost(url,kvPairs);
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.i("", "doPost Exception......."+e1);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.i("", "doPost Exception......."+e1);
			}


			String jsonResponse = null;
			if (httpResponse!=null) 
			{

				try {
					jsonResponse = EntityUtils.toString(httpResponse.getEntity());
				} catch (ParseException e) {
					e.printStackTrace();
					Log.i("", "doPost Exception......."+e);
				} catch (IOException e) {
					e.printStackTrace();
					Log.i("", "doPost Exception......."+e);
				}
			}
			Log.i(" ","GetCards: "+jsonResponse);
			Log.i(" ",jsonResponse);

			if (jsonResponse!=null) 
			{
				try
				{
					Gson gson = new Gson();
					response=gson.fromJson(jsonResponse,GetCardResponse.class);
					Log.i("","DONE WITH GSON");
					Utility.printLog("get cards response" + jsonResponse);
				}
				catch(Exception e)
				{
					Utility.ShowAlert("Server Error", getActivity());
				}
			}
			else
			{
				getActivity().runOnUiThread(new Runnable()
				{
					public void run() 
					{
						Toast.makeText(getActivity(),"Request Timeout !!", Toast.LENGTH_SHORT).show();
					}
				});
			}
			return null;
		}


		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (dialogL!=null) {
				dialogL.dismiss();
			}

			if(response!=null)
			{
				if(response.getErrFlag().equals("0") && isAdded())
				{
					//LayoutParams cardLinearLayout_params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
					rowItems.clear();
					for(int i=0;i<response.getCards().size();i++)
					{
						Bitmap bitmap=setCreditCardLogo(response.getCards().get(i).getType());

						card_info_pojo item = new card_info_pojo(bitmap,response.getCards().get(i).getLast4(),response.getCards().get(i).getExp_month(),
								response.getCards().get(i).getExp_year(),response.getCards().get(i).getId());
						rowItems.add(item);
					}
					adapter = new CustomListViewAdapter(getActivity(),
							R.layout.card_list_row, rowItems);
					card_list.setAdapter(adapter);

					Stripe_Paypal.setVisibility(View.GONE);
				}
				else if(response.getErrNum().equals("6") || response.getErrNum().equals("7") || 
						response.getErrNum().equals("94") || response.getErrNum().equals("96"))
				{
					Toast.makeText(getActivity(), response.getErrMsg(),Toast.LENGTH_SHORT).show();
					Intent i = new Intent(getActivity(), MainActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					getActivity().startActivity(i);
					getActivity().overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
				}
				else if(isAdded())
				{
					if(response.getErrNum().equals("51"))
					{
						rowItems.clear();
						adapter = new CustomListViewAdapter(getActivity(),
								R.layout.card_list_row, rowItems);
						card_list.setAdapter(adapter);
					}

					if(response.getErrMsg()!=null)
					{
						Toast.makeText(getActivity(), response.getErrMsg(), Toast.LENGTH_SHORT).show();
					}
				}
			}
			else if(isAdded())
			{
				Utility.ShowAlert("Server error!!", getActivity());
			}
		}

		private Bitmap setCreditCardLogo(String type) {
			// TODO Auto-generated method stub

			CardType cardType;
			if(type.equals("Visa"))
			{
				cardType= CardType.VISA;
				Bitmap bitmap=cardType.imageBitmap(getActivity());
				//cardLogo.setImageBitmap(bitmap);
				return bitmap;
			}
			if(type.equals("MasterCard"))
			{
				cardType= CardType.MASTERCARD;
				Bitmap bitmap=cardType.imageBitmap(getActivity());
				//cardLogo.setImageBitmap(bitmap);
				return bitmap;
			}
			if(type.equals("American Express"))
			{
				cardType= CardType.AMEX;
				Bitmap bitmap=cardType.imageBitmap(getActivity());
				//cardLogo.setImageBitmap(bitmap);
				return bitmap;
			}
			if(type.equals("Discover"))
			{
				cardType= CardType.DISCOVER;
				Bitmap bitmap=cardType.imageBitmap(getActivity());
				//cardLogo.setImageBitmap(bitmap);
				return bitmap;
			}

			if(type.equals("JCB"))
			{
				cardType= CardType.JCB;
				Bitmap bitmap=cardType.imageBitmap(getActivity());
				//cardLogo.setImageBitmap(bitmap);
				return bitmap;
			}
			cardType= CardType.UNKNOWN;
			Bitmap bitmap=cardType.imageBitmap(getActivity());
			//cardLogo.setImageBitmap(bitmap);
			return bitmap;
		}
	}

	class CustomListViewAdapter extends ArrayAdapter<card_info_pojo> {

		Context context;

		public CustomListViewAdapter(Context context, int resourceId,
				List<card_info_pojo> items) {
			super(context, resourceId, items);
			this.context = context;
		}


		private class ViewHolder {
			ImageView card_image;
			TextView card_numb;
			RelativeLayout payment_relative;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			ViewHolder holder = null;
			card_info_pojo rowItem = getItem(position);

			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			if (convertView == null) 
			{
				convertView = mInflater.inflate(R.layout.card_list_row, null);
				holder = new ViewHolder();

				holder.card_numb = (TextView) convertView.findViewById(R.id.card_numb_row);
				holder.card_image = (ImageView) convertView.findViewById(R.id.card_img_row);
				holder.payment_relative=(RelativeLayout)convertView.findViewById(R.id.payment_relative);
				//==============================change April========================================

				Typeface roboto_condensed = Typeface.createFromAsset(context.getAssets(),"fonts/BebasNeue.otf");
				holder.card_numb.setTypeface(roboto_condensed);
                //======================================================================================
				convertView.setTag(holder);
			}
			else
				holder = (ViewHolder) convertView.getTag();

			if(position==0)
			{
				holder.payment_relative.setBackgroundResource(R.drawable.selectpayment_listview_top);
			}
			else
			{
				holder.payment_relative.setBackgroundResource(R.drawable.selectpayment_listview_bottom);
			}

			holder.card_image.setImageBitmap(rowItem.getCard_image());
			holder.card_numb.setText(rowItem.getCard_numb());

			return convertView;
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
    	//MainActivityDrawer.invoice_driver_tip.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		Log.i("","INside onClick");

		if(v.getId()==R.id.add_card_rel_fotter)
		{
			Intent intent =new Intent(getActivity(),AddCardActivity.class);
			startActivityForResult(intent,1);
			getActivity().overridePendingTransition(R.anim.anim_two, R.anim.anim_one); 
		}
	}
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 1)
		{
			if(resultCode==Activity.RESULT_OK)
			{ 
				/*response=(GetCardResponse)getActivity().getIntent().getSerializableExtra("RESPONSE");
				 Log.i("","res in activity result "+response);
				 Log.i("",""+response);
				 adapter.notifyDataSetChanged();*/
				new BackgroundGetCards().execute();
			}
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Log.i("","INside onItemClick");

		card_info_pojo row_details =(card_info_pojo)card_list.getItemAtPosition(arg2);
		Log.i("","row_details numb "+row_details.getCard_numb());
		Log.i("","row_details month "+row_details.getExp_month());
		Log.i("","row_details year "+row_details.getExp_year());
		Log.i("","row_details id "+row_details.getCard_id());
		Utility.printLog("Card count: " + response.getCards().size());


		String expDate=row_details.getExp_month();

		if(expDate.length()==1)
		{
			expDate="0"+expDate;
		}

		expDate=expDate+"/"+row_details.getExp_year();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		row_details.getCard_image().compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();


		Intent intent=new Intent(getActivity(), DeleteCardActivity.class);
		intent.putExtra("NUM",row_details.getCard_numb());
		intent.putExtra("EXP",expDate);
		intent.putExtra("IMG",byteArray);
		intent.putExtra("ID",row_details.getCard_id());
		intent.putExtra("COUNT",response.getCards().size());
		startActivityForResult(intent, 1);
		getActivity().overridePendingTransition(R.anim.anim_two, R.anim.anim_one);


	}
	public void getCredit(){
		RequestQueue volleyRequest = Volley.newRequestQueue(getActivity());


		StringRequest myReq = new StringRequest(Request.Method.POST,"http://app.eserviss.com/Taxi/services.php/showCredit",
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {

						try {
							JSONObject jsonObject= new JSONObject(response);
							String status = jsonObject.getString("status");
							if (status.equalsIgnoreCase("1")) {
								text_credit_promo.setText("PROMO CREDIT");
								String discount = jsonObject.getJSONObject("data").getString("discount");
								String discount_type = jsonObject.getJSONObject("data").getString("discount_type");
								String expiry_date = jsonObject.getJSONObject("data").getString("expiry_date");
								String credit_expire_date = jsonObject.getJSONObject("data").getString("credit_expire_date");
								if (discount_type.equalsIgnoreCase("1")) {
									text_open.setText(discount +"% OFFER");
									text_expiry.setText("VALID THROUGH " +credit_expire_date);

								} else if (discount_type.equalsIgnoreCase("2"))
								{
									text_open.setText(discount +"$");
									text_expiry.setText("VALID THROUGH " +credit_expire_date);
								}
							}
							else {
								crd_layout.setVisibility(View.INVISIBLE);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

						showCredit = response;
						Utility.printLog("Success of show credit Info"+response);
						//Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
						System.out.println("show credit RESPONSE"+showCredit);
						//dialogL.dismiss();

					}
				}, 	new Response.ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error)
			{
				dialogL.dismiss();
				Toast.makeText(getActivity(), "System error in getting user Info please retry", Toast.LENGTH_LONG).show();
				Utility.printLog("Error for volley");
				System.out.println("Credit RESPONSE1"+error);

			}
		}){
			protected HashMap<String,String> getParams() throws com.android.volley.AuthFailureError {
				HashMap<String,String> kvPair = new HashMap<String, String>();

				SessionManager session=new SessionManager(getActivity());

				Utility utility=new Utility();


				kvPair.put("ent_sess_token",session.getSessionToken() );
				kvPair.put("ent_dev_id",session.getDeviceId());


				Log.i("","aaa="+session.getSessionToken());
				Log.i("","aaa="+session.getDeviceId());


				return kvPair;
			};

		};

		volleyRequest.add(myReq);
	}





}
