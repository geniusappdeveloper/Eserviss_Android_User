package com.eserviss.passenger.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.egnyt.eserviss.R;
import com.eserviss.passenger.pojo.AddCardResponse;
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
import java.util.HashMap;
import java.util.Map;

public class DeleteCardActivity extends Activity implements OnClickListener
{
	private TextView card_numb_text,exp_text;
	private ImageView card_img;
	private Button delete;
	private RelativeLayout back;
	private String id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.delete_card);
		
		intializeVariables();
		
		Bundle bundle=getIntent().getExtras();
		card_numb_text.setText(bundle.getString("NUM"));
		exp_text.setText(bundle.getString("EXP"));
		byte[] byteArray = bundle.getByteArray("IMG");
		id=bundle.getString("ID");
		if(byteArray!=null)
			card_img.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
		
	}

	private void intializeVariables() {
		// TODO Auto-generated method stub
		card_numb_text=(TextView)findViewById(R.id.card_numb_delete);
		exp_text=(TextView)findViewById(R.id.exp_date_delete);
		delete=(Button)findViewById(R.id.delete_card_btn);
		card_img=(ImageView)findViewById(R.id.card_img_delete);
		back =(RelativeLayout)findViewById(R.id.rl_deletecard);
		//====================chnage april================================================================
		Typeface roboto_condensed = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/BebasNeue.otf");
		card_numb_text.setTypeface(roboto_condensed);
		exp_text.setTypeface(roboto_condensed);
		delete.setTypeface(roboto_condensed);

		//===================================================================================================
		delete.setOnClickListener(this);
		back.setOnClickListener(this);
		
	}
	
	
	class BackgroundDeleteCard extends AsyncTask<String, Void, String>
	{
		ProgressDialog dialogL;
		AddCardResponse response;
	

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialogL= Utility.GetProcessDialogNew(DeleteCardActivity.this,getResources().getString(R.string.deleting_card));

		if (dialogL!=null) {
			dialogL.show();
		}
	}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url= VariableConstants.BASE_URL+"removeCard";
			SessionManager session=new SessionManager(DeleteCardActivity.this);


			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			Log.i("","dataandTime "+curenttime);

			Map<String, String> kvPairs = new HashMap<String, String>();
			kvPairs.put("ent_sess_token",session.getSessionToken() );
			kvPairs.put("ent_dev_id",session.getDeviceId());
			kvPairs.put("ent_cc_id",id);
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
			if (httpResponse!=null) {

				try {
					jsonResponse = EntityUtils.toString(httpResponse.getEntity());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.i("", "doPost Exception......."+e);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.i("", "doPost Exception......."+e);
				}
			}
			Log.i(" ","Delete cards: "+jsonResponse);
			Log.i(" ",jsonResponse);
			
			if (jsonResponse!=null) 
			{
				Gson gson = new Gson();
				response=gson.fromJson(jsonResponse,AddCardResponse.class);
				Log.i("","DONE WITH GSON");
			}else
			{
				DeleteCardActivity.this.runOnUiThread(new Runnable()
				{
					public void run() 
					{
						Toast.makeText(DeleteCardActivity.this,getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
					}

				});
			}
			
			return null;
		}
		
		

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if (dialogL!=null) {
				dialogL.dismiss();
			}
			
			if(response!=null)
			{
				
				if(response.getErrFlag().equals("0"))
				{
					
					Toast.makeText(DeleteCardActivity.this,getResources().getString(R.string.card_removed), Toast.LENGTH_SHORT).show();
					Intent intent=new Intent();
					//intent.putExtra("RESPONSE",response);
					setResult(RESULT_OK,intent);
					
					finish();
					overridePendingTransition(R.anim.anim_three, R.anim.anim_four); 
				}
				else
				{
					Utility.ShowAlert(response.getErrMsg(), DeleteCardActivity.this);
				}
				
				
				
			}
			else
			{
				Utility.ShowAlert(getResources().getString(R.string.server_error), DeleteCardActivity.this);
			}
			
			
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(v.getId()==R.id.delete_card_btn)
		{
			new BackgroundDeleteCard().execute();
			
			return;
		}
		
		if(v.getId()==R.id.rl_deletecard)
		{
			
			finish();
			overridePendingTransition(R.anim.anim_three, R.anim.anim_four); 
		}
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
