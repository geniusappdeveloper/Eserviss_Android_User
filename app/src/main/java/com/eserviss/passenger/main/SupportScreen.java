package com.eserviss.passenger.main;



import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.egnyt.eserviss.R;

public class SupportScreen extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.disputedialog);
	}

}
