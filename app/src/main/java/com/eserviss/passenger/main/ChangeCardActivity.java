package com.eserviss.passenger.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.egnyt.eserviss.R;
import com.eserviss.passenger.pojo.GetCardResponse;
import com.eserviss.passenger.pojo.card_info_pojo;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;

import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.card.payment.CardType;

public class ChangeCardActivity extends Activity implements OnClickListener,OnItemClickListener{
	
	private ListView card_list;
	private Button save;
	
	GetCardResponse response;
	CustomListViewAdapter adapter;
	List<card_info_pojo> rowItems;
	private Bitmap card_bitmap;
	private String card_numb,card_id;
	private boolean isCardChanged=false;
	private RelativeLayout add_cc_bt;
	private ImageButton back_btn;
	private RelativeLayout Rl_change_card;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.change_card);
		initializeVariables();
		new BackgroundGetCards().execute();
	}

	private void initializeVariables() {
		
		
		save=(Button)findViewById(R.id.save_change_card_btn);
		card_list=(ListView)findViewById(R.id.change_cards_list_view);
		Rl_change_card=(RelativeLayout)findViewById(R.id.rl_change_card);
		save.setText("Save");
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View fotter_view = inflater.inflate(R.layout.add_card_fotter, null);
		card_list.addFooterView(fotter_view);
		add_cc_bt=(RelativeLayout)fotter_view.findViewById(R.id.add_card_rel_fotter);
		
		back_btn = (ImageButton) findViewById(R.id.login_back_button);
		//Txt_Select_Payment =(TextView) findViewById(R.id.txt_select_payment);
		//Txt_Select_Payment.setPaintFlags(Txt_Select_Payment.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		//====================chnage april================================================================
		Typeface roboto_condensed = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/BebasNeue.otf");
		save.setTypeface(roboto_condensed);


		//===================================================================================================
		back_btn.setOnClickListener(this);
		add_cc_bt.setOnClickListener(this);
		card_list.setOnItemClickListener(this);
		save.setOnClickListener(this);
		Rl_change_card.setOnClickListener(this);
		
		rowItems = new ArrayList<card_info_pojo>();
		
		
	}


	class BackgroundGetCards extends AsyncTask<String, Void,String>
	{
		ProgressDialog dialogL;
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			dialogL= Utility.GetProcessDialog(ChangeCardActivity.this);

			if (dialogL!=null)
			{
				dialogL.show();
			}
		}

		@Override
		protected String doInBackground(String... params)
		{
			String url= VariableConstants.BASE_URL+"getCards";//http://www.privemd.com/test/process.php/getCards
			SessionManager session=new SessionManager(ChangeCardActivity.this);

			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			Log.i("","dataandTime "+curenttime);

			Map<String, String> kvPairs = new HashMap<String, String>();
			kvPairs.put("ent_sess_token",session.getSessionToken() );
			kvPairs.put("ent_dev_id",session.getDeviceId());
			kvPairs.put("ent_date_time",curenttime);

			HttpResponse httpResponse = null;
			try {
				httpResponse = Utility.doPost(url,kvPairs);
			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
				Log.i("", "doPost Exception......."+e1);
			} catch (IOException e1) {
				e1.printStackTrace();
				Log.i("", "doPost Exception......."+e1);
			}


			String jsonResponse = null;
			if (httpResponse!=null) {

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


			try
			{


				if (jsonResponse!=null)
					{
						Gson gson = new Gson();
						response=gson.fromJson(jsonResponse,GetCardResponse.class);
						Log.i("","DONE WITH GSON");
					}else
					{
						runOnUiThread(new Runnable()
						{
							public void run()
							{
								Toast.makeText(ChangeCardActivity.this,getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
							}

						});
					}

			}
			catch(Exception e)
			{
				Utility.ShowAlert(getResources().getString(R.string.server_error), ChangeCardActivity.this);
			}




			return null;
		}


		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);

			if (dialogL!=null)
			{
				dialogL.dismiss();
			}

			if(response!=null)
			{

				if(response.getErrFlag().equals("0"))
				{
					VariableConstants.card_respons="1";
					rowItems.clear();
					for(int i=0;i<response.getCards().size();i++)
					{
						Bitmap bitmap=setCreditCardLogo(response.getCards().get(i).getType());


						 card_info_pojo item = new card_info_pojo(bitmap,response.getCards().get(i).getLast4(),response.getCards().get(i).getExp_month(),
								 response.getCards().get(i).getExp_year(),response.getCards().get(i).getId());
				         rowItems.add(item);


					}
					adapter = new CustomListViewAdapter(ChangeCardActivity.this,
			                 R.layout.change_card_list_row, rowItems);

			         card_list.setAdapter(adapter);


				}
				else
				{
					if(response.getErrNum().equals("51"))
					{
						VariableConstants.card_respons="0";
						rowItems.clear();
						adapter = new CustomListViewAdapter(ChangeCardActivity.this,
				                 R.layout.change_card_list_row, rowItems);
						card_list.setAdapter(adapter);
					}

					if(response.getErrMsg()!=null)
					{
						Utility.ShowAlert(response.getErrMsg(), ChangeCardActivity.this);
					}
				}


			}
			else
			{
				Utility.ShowAlert(getResources().getString(R.string.server_error), ChangeCardActivity.this);
			}

		}

		private Bitmap setCreditCardLogo(String type)
		{

			CardType cardType;
			if(type.equals("Visa"))
			{
				cardType= CardType.VISA;
				Bitmap bitmap=cardType.imageBitmap(ChangeCardActivity.this);
				//cardLogo.setImageBitmap(bitmap);
				return bitmap;
			}
			if(type.equals("MasterCard"))
			{
				cardType= CardType.MASTERCARD;
				Bitmap bitmap=cardType.imageBitmap(ChangeCardActivity.this);
				//cardLogo.setImageBitmap(bitmap);
				return bitmap;
			}
			if(type.equals("American Express"))
			{
				cardType= CardType.AMEX;
				Bitmap bitmap=cardType.imageBitmap(ChangeCardActivity.this);
				//cardLogo.setImageBitmap(bitmap);
				return bitmap;
			}
			if(type.equals("Discover"))
			{
				cardType= CardType.DISCOVER;
				Bitmap bitmap=cardType.imageBitmap(ChangeCardActivity.this);
				//cardLogo.setImageBitmap(bitmap);
				return bitmap;
			}

			if(type.equals("JCB"))
			{
				cardType= CardType.JCB;
				Bitmap bitmap=cardType.imageBitmap(ChangeCardActivity.this);
				//cardLogo.setImageBitmap(bitmap);
				return bitmap;
			}
			cardType= CardType.UNKNOWN;
			Bitmap bitmap=cardType.imageBitmap(ChangeCardActivity.this);
			//cardLogo.setImageBitmap(bitmap);
			return bitmap;

		}


	}

	class CustomListViewAdapter extends ArrayAdapter<card_info_pojo> {

		Context context;
		int selectedPosition = 0;
	    public CustomListViewAdapter(Context context, int resourceId,
	            List<card_info_pojo> items) {
	        super(context, resourceId, items);
	        this.context = context;
	    }


	    private class ViewHolder {
	        ImageView card_image;
	        TextView card_numb;
	        RadioButton radioButton;
	        RelativeLayout change_card_relative;

	    }


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			 ViewHolder holder = null;
			 final card_info_pojo rowItem = getItem(position);

			 LayoutInflater mInflater = (LayoutInflater) context
             .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			 if (convertView == null)
			 {

				 convertView = mInflater.inflate(R.layout.change_card_list_row, null);
				 holder = new ViewHolder();

				 holder.card_numb = (TextView) convertView.findViewById(R.id.card_numb_row_change);
				 holder.card_image = (ImageView) convertView.findViewById(R.id.card_img_row_change);
				 holder.radioButton=(RadioButton)convertView.findViewById(R.id.radio_card_change);
				 holder.change_card_relative=(RelativeLayout)convertView.findViewById(R.id.change_card_relative);
				 convertView.setTag(holder);


			 }
			 else
				 holder = (ViewHolder) convertView.getTag();


			 if(position==0)
				{
					holder.change_card_relative.setBackgroundResource(R.drawable.choose_card_top_list_selector);
					holder.radioButton.setVisibility(View.VISIBLE);
				}
				else
				{
					holder.change_card_relative.setBackgroundResource(R.drawable.fotter_payment_selector);
					holder.radioButton.setVisibility(View.INVISIBLE);
				}

			 holder.card_image.setImageBitmap(rowItem.getCard_image());
			 holder.card_numb.setText(rowItem.getCard_numb());

				holder.radioButton.setChecked(position == selectedPosition);
				holder.radioButton.setTag(position);
				holder.radioButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    	isCardChanged=true;
                        selectedPosition = (Integer)view.getTag();
                       // booked_time=rowItem.getTime();
            			Log.i("","displaySelected onClick "+rowItem.getCard_numb());
            			card_bitmap=rowItem.getCard_image();
            			card_id=rowItem.getCard_id();
            			card_numb=rowItem.getCard_numb();
                       // Log.i("","displaySelected onClick position "+position);
                        //notifyDataSetInvalidated();
                        notifyDataSetChanged();
                    }
                });

				holder.change_card_relative.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v) 
					{
						// TODO Auto-generated method stub
						Log.i("","row_details numb "+rowItem.getCard_numb());
						Log.i("","row_details month "+rowItem.getExp_month());
						Log.i("","row_details year "+rowItem.getExp_year());
						Log.i("","row_details id "+rowItem.getCard_id());
						Intent returnIntent = new Intent();
						 returnIntent.putExtra("Image",rowItem.getCard_image());
						 returnIntent.putExtra("ID",rowItem.getCard_id());
						 returnIntent.putExtra("NUMB",rowItem.getCard_numb()); 
						 setResult(RESULT_OK,returnIntent);     
						 finish();
						 overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
					}
				});
		     
			return convertView;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1)
		{

			if(resultCode==Activity.RESULT_OK)
			{ 
				/*response=(GetCardResponse)getActivity().getIntent().getSerializableExtra("RESPONSE");
				 Log.i("","res in activity result "+response);
				 Log.i("",""+response);
				 adapter.notifyDataSetChanged();*/
				if(Utility.isNetworkAvailable(ChangeCardActivity.this)){
					new BackgroundGetCards().execute();

				}
				else{
					Toast.makeText(ChangeCardActivity.this,getResources().getString(R.string.network_connection_fail), Toast.LENGTH_LONG).show();
				}
			}


		}

	}

	
	@Override
	public void onClick(View v) {
		
		if(v.getId()==R.id.add_card_rel_fotter)
		{
			Intent intent =new Intent(ChangeCardActivity.this,AddCardActivity.class);
			startActivityForResult(intent,1);
			overridePendingTransition(R.anim.anim_two, R.anim.anim_one); 


		}
		
		if(v.getId()==R.id.save_change_card_btn)
		{
			if(isCardChanged){
			Intent returnIntent = new Intent();
			 returnIntent.putExtra("Image",card_bitmap);
			 returnIntent.putExtra("ID",card_id);
			 returnIntent.putExtra("NUMB",card_numb); 
			 setResult(RESULT_OK,returnIntent);     
			 finish();
			 overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
			}
			else
			{
				finish();
				overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
			}
			
		}
		
		if(v.getId()==R.id.login_back_button)
		{
			finish();
			overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
		}
		if(v.getId()==R.id.rl_change_card)
		{
			finish();
			overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
		}
		
		
		
		
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		card_info_pojo row_details =(card_info_pojo)card_list.getItemAtPosition(position);
        
		Log.i("","row_details numb "+row_details.getCard_numb());
		Log.i("","row_details month "+row_details.getExp_month());
		Log.i("","row_details year "+row_details.getExp_year());
		Log.i("","row_details id "+row_details.getCard_id());
	}
	
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
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
