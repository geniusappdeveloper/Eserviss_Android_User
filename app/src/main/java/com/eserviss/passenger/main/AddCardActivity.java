package com.eserviss.passenger.main;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.egnyt.eserviss.R;
import com.eserviss.passenger.pojo.AddCardResponse;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

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

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class AddCardActivity extends FragmentActivity implements OnClickListener
{
	Button scanButton;
	private ProgressDialogFragment progressFragment;
	private static final String MY_CARDIO_APP_TOKEN ="376f217254c64a2ca0179d5a459eda1a";
	public static final String PUBLISHABLE_KEY = VariableConstants.STRIPE_PUBLISH_KEY;;
	private int MY_SCAN_REQUEST_CODE = 100;
	private EditText card_number,card_cvc,card_year,card_month;
	ProgressDialog dialogL;
	String access_token;
	private Button save;
	ImageView back;
	private String cardNo,expiryMonth,expiryYear,cvv;
	@Override
	protected void onResume() 
	{
		super.onResume();
		if (CardIOActivity.canReadCardWithCamera())
		{
			scanButton.setText(getResources().getString(R.string.scan_credit_card));
		}
		else 
		{
			scanButton.setText(getResources().getString(R.string.enter_credit_card));
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_card);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		initializeVariables();
		progressFragment = ProgressDialogFragment.newInstance(R.string.progressMessage);

	}
	private void initializeVariables() 
	{
		// TODO Auto-generated method stub
		scanButton=(Button)findViewById(R.id.su_two_scan_button_add_card);
		card_number=(EditText)findViewById(R.id.add_credit_card_number);
		card_cvc=(EditText)findViewById(R.id.add_card_cvv);
		card_month=(EditText)findViewById(R.id.add_card_month);
		card_year=(EditText)findViewById(R.id.add_card_year);
		save=(Button)findViewById(R.id.su_two_next_button_add);
		back=(ImageView)findViewById(R.id.su_two_back_button_add);
		//==================change April=====================================================
		Typeface roboto_condensed = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/BebasNeue.otf");
		scanButton.setTypeface(roboto_condensed);
		card_number.setTypeface(roboto_condensed);
		card_cvc.setTypeface(roboto_condensed);
		card_month.setTypeface(roboto_condensed);
		card_year.setTypeface(roboto_condensed);
		save.setTypeface(roboto_condensed);




		//===============================================================================
		scanButton.setOnClickListener(this);
		back.setOnClickListener(this);
		save.setOnClickListener(this);
	}
	public void onScanPress(View v) 
	{
		// This method is set up as an onClick handler in the layout xml
		Intent scanIntent = new Intent(this, CardIOActivity.class);
		// required for authentication with card.io
		//scanIntent.putExtra(CardIOActivity.EXTRA_APP_TOKEN, MY_CARDIO_APP_TOKEN);
		// customize these values to suit your needs.
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: true
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
		// hides the manual entry button
		// if set, developers should provide their own manual entry mechanism in the app
		scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true); // default: false
		// MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
		startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("","inside onActivity result");
		if(data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT))
		{
			CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
			// Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
			//resultStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
			card_number.setText(scanResult.getFormattedCardNumber().replace(" ",""));
			// Do something with the raw number, e.g.:
			// myService.setCardNumber( scanResult.cardNumber );
			if (scanResult.isExpiryValid())
			{
				Log.i("","inside expiryDate expiryMonth"+scanResult.expiryMonth);
				Log.i("","inside expiryDate expiryYear"+scanResult.expiryYear);
				String temp=""+scanResult.expiryMonth;
				card_month.setText(temp);
				String temp2=""+scanResult.expiryYear;
				card_year.setText(temp2);
			}
			if (scanResult.cvv != null)
			{ 
				// Never log or display a CVV
				Log.i("","inside cvv");
				//resultStr += "CVV has " + scanResult.cvv.length()+ " digits.\n";
				card_cvc.setText(scanResult.cvv.toString());
			}
		}
		else 
		{
		}
	}
	public void saveCreditCard() 
	{
		Card card = new Card(card_number.getText().toString().trim(),
				Integer.parseInt(card_month.getText().toString().trim()),
				Integer.parseInt(card_year.getText().toString().trim()),
				card_cvc.getText().toString().trim());
		boolean validation = card.validateCard();
		if (validation) 
		{
			startProgress();
			new Stripe().createToken(
					card,
					PUBLISHABLE_KEY,
					new TokenCallback()
					{
						public void onSuccess(Token token)
						{
							// getTokenList().addToList(token);
							Log.i("ss","Inside OnSucess");
							Log.i("","Token getId "+token.getId());
							access_token=token.getId();
							Log.i("","Token getCreated"+token.getCreated());
							Log.i("","Token getLivemode"+token.getLivemode() );
							Log.i("","Token getUsed"+token.getUsed() );
							finishProgress();
							new BackGroundAddCard().execute();
						}
						public void onError(Exception error)
						{
							handleError(error.getLocalizedMessage());
							finishProgress();
						}
					});
		}
		else
		{
			handleError(getResources().getString(R.string.not_a_valid_card));
		}
	}
	private void startProgress() 
	{
		progressFragment.show(getSupportFragmentManager(), getResources().getString(R.string.progress));
	}
	private void finishProgress() 
	{
		progressFragment.dismiss();
	}
	private void handleError(String error)
	{
		DialogFragment fragment = ErrorDialogFragment.newInstance(R.string.validationErrors, error);
		fragment.show(getSupportFragmentManager(), "error");
	}
	@Override
	public void onClick(View arg0)
	{
		// TODO Auto-generated method stub
		if(arg0.getId()==R.id.su_two_next_button_add)
		{
			if(validateManualEntry())
			{
				expiryMonth=card_month.getText().toString().trim();
				expiryYear=card_year.getText().toString().trim();
				if(expiryMonth.length()==1)
				{
					expiryMonth="0"+expiryMonth;

				}
				if(expiryYear.length()==4)
				{
					expiryYear=expiryYear.substring(2);
				}
				cardNo=card_number.getText().toString().trim();
				cardNo = cardNo.replaceAll("\\D", "");
				Utility.printLog("card number in addcard=" + cardNo);
				cardNo=cardNo.replace(" ","");
				cvv=card_cvc.getText().toString().trim();
				Log.i("","value "+cardNo);
				Log.i("","value "+expiryMonth);
				Log.i("","value "+expiryYear);
				Log.i("","value "+cvv);
				saveCreditCard();			
			}
			else
			{
				Utility.ShowAlert(getResources().getString(R.string.fill_all_fields), AddCardActivity.this);
			}
		}
		if(arg0.getId()==R.id.su_two_back_button_add)
		{
			finish();
			overridePendingTransition(R.anim.anim_three, R.anim.anim_four);
		}
		if(arg0.getId()==R.id.su_two_scan_button_add_card)
		{
			View v = null;
			onScanPress(v);
		}
	}
	class BackGroundAddCard extends AsyncTask<String,Void,String>
	{
		ProgressDialog dialogL;
		AddCardResponse response;
		@Override
		protected void onPreExecute() 
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialogL= Utility.GetProcessDialog(AddCardActivity.this);
			if (dialogL!=null)
			{
				dialogL.show();
				dialogL.setCancelable(false);
			}
		}
		@Override
		protected String doInBackground(String... params)
		{
			// TODO Auto-generated method stub
			String url= VariableConstants.BASE_URL+"addCard";//http://www.privemd.com/test/process.php/addCard
			SessionManager session=new SessionManager(AddCardActivity.this);
			Utility utility=new Utility();
			String curenttime=utility.getCurrentGmtTime();
			Log.i("","dataandTime "+curenttime);
			Map<String, String> kvPairs = new HashMap<String, String>();
			kvPairs.put("ent_sess_token",session.getSessionToken() );
			kvPairs.put("ent_dev_id",session.getDeviceId());
			kvPairs.put("ent_token",access_token);
			kvPairs.put("ent_date_time",curenttime);
			HttpResponse httpResponse = null;
			try
			{
				httpResponse = Utility.doPost(url,kvPairs);
			} 
			catch (ClientProtocolException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.i("", "doPost Exception......."+e1);
			} 
			catch (IOException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.i("", "doPost Exception......."+e1);
			}
			String jsonResponse = null;
			if (httpResponse!=null)
			{
				try
				{
					jsonResponse = EntityUtils.toString(httpResponse.getEntity());
				}
				catch (ParseException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.i("", "doPost Exception......."+e);
				} 
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.i("", "doPost Exception......."+e);
				}
			}
			Log.i(" ","Add cards: "+jsonResponse);
			Log.i(" ",jsonResponse);

			if (jsonResponse!=null) 
			{
				Gson gson = new Gson();
				response=gson.fromJson(jsonResponse,AddCardResponse.class);
				Log.i("","DONE WITH GSON");
			}
			else
			{
				AddCardActivity.this.runOnUiThread(new Runnable()
				{
					public void run() 
					{
						Toast.makeText(AddCardActivity.this,getResources().getString(R.string.requestTimeout), Toast.LENGTH_SHORT).show();
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
			if (dialogL!=null)
			{
				dialogL.dismiss();
			}
			if(response!=null)
			{
				if(response.getErrFlag().equals("0"))
				{
					Toast.makeText(AddCardActivity.this,getResources().getString(R.string.Card_Added_Successfully), Toast.LENGTH_SHORT).show();
					Intent intent=new Intent();
					setResult(RESULT_OK,intent);
					finish();
					overridePendingTransition(R.anim.anim_three, R.anim.anim_four); 
				}
				else
				{
					Utility.ShowAlert(response.getErrMsg(), AddCardActivity.this);
				}
			}
			else
			{
				Utility.ShowAlert(getResources().getString(R.string.server_error), AddCardActivity.this);
			}
		}
	}
	private boolean validateManualEntry()
	{
		Log.i("","validate card_man.getText().toString().isEmpty()"+card_number.getText().toString().isEmpty());
		if(card_number.getText().toString().isEmpty())
		{
			return false;
		}
		if(card_month.getText().toString().isEmpty())
		{
			return false;
		}
		if(card_year.getText().toString().isEmpty())
		{
			return false;
		}
		Log.i("","validate cvc_man.getText().toString().isEmpty() "+card_cvc.getText().toString().isEmpty());
		if(card_cvc.getText().toString().isEmpty())
		{
			return false;
		}
		return true;
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
