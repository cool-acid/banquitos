package com.coolacid.banquitos;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class Mapa extends Activity implements GooglePlayServicesClient.ConnectionCallbacks, 
											  GooglePlayServicesClient.OnConnectionFailedListener{
	
	final int RQS_GooglePlayServices = 1;
	final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleMap mapa;

	LocationClient mLocationClient;
	Location mCurrentLocation;
	LocationRequest mLocationRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapa);
		Context context = getApplicationContext();
		int GooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		if(GooglePlayServicesAvailable != ConnectionResult.SUCCESS){
			GooglePlayServicesUtil.getErrorDialog(GooglePlayServicesAvailable, this, RQS_GooglePlayServices).show();
			finish();
		}
		mLocationClient = new LocationClient(this, this, this);
		mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		if (mapa != null){
			mapa.setMyLocationEnabled(true);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mLocationClient.connect();
	}
	
	@Override
	protected void onStop() {
		mLocationClient.disconnect();
		super.onStop();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
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
			default:
				return super.onOptionsItemSelected(item); 
		}
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
        	;
        }
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

}
