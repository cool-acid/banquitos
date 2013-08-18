package com.coolacid.banquitos;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class Mapa extends Activity {
	
	final int RQS_GooglePlayServices = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapa);
		Context context = getApplicationContext();
		int GooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		if(GooglePlayServicesAvailable != ConnectionResult.SUCCESS){
			GooglePlayServicesUtil.getErrorDialog(GooglePlayServicesAvailable, this, RQS_GooglePlayServices).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mapa, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
			case R.id.center_position:
				centerPosition();
				return true;
			default:
				return super.onOptionsItemSelected(item); 
		}
	}

	private void centerPosition() {
		Context context = getApplicationContext();
		CharSequence text = "Centrando mapa";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	

}
