package com.coolacid.banquitos;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Mapa extends Activity implements GooglePlayServicesClient.ConnectionCallbacks, 
											  GooglePlayServicesClient.OnConnectionFailedListener{
	
	final int RQS_GooglePlayServices = 1;
	final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleMap mapa;

	LocationClient mLocationClient;
	Location mCurrentLocation;
	LocationRequest mLocationRequest;
	SharedPreferences sharedPref;
	
	BanquitosAPI API;
	ArrayList<Marker> marcadores;

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
		sharedPref = this.getPreferences(Context.MODE_PRIVATE);
		mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		if (mapa != null){
			mapa.setMyLocationEnabled(true);
			LatLng pastCenter = new LatLng(Double.valueOf(sharedPref.getString("lat", "19.432681")), Double.valueOf(sharedPref.getString("lng", "-99.13332")));
			mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(pastCenter, Float.valueOf(sharedPref.getInt("zoom", 15))));
		}
		API = new BanquitosAPI();
		marcadores = new ArrayList<Marker>();
		try {
			refreshMap();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	protected void onPause() {
		SharedPreferences.Editor editor = sharedPref.edit();
		if (mapa != null){
			CameraPosition posicionCamara = mapa.getCameraPosition();
			editor.putString("lat", String.valueOf(posicionCamara.target.latitude));
			editor.putString("lng", String.valueOf(posicionCamara.target.longitude));
			editor.putInt("zoom", (int) posicionCamara.zoom);
			editor.commit();
		}
		super.onPause();
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
			case R.id.refresh:
				try {
					refreshMap();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
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
	
	private void refreshMap() throws JSONException{
		Toast.makeText(this, "Cargando mapas en esta area", Toast.LENGTH_SHORT).show();
		CameraPosition posicionCamara = mapa.getCameraPosition();
		String lat = String.valueOf(posicionCamara.target.latitude);
		String lng = String.valueOf(posicionCamara.target.longitude);
		RequestParams params = new RequestParams();
		params.put("lat",lat);
		params.put("lon", lng);
		API.apiCall("main/near", params, "POST", new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int arg0, String arg1) {
				Log.i("MYTAG", arg1);
				super.onSuccess(arg0, arg1);
			}
			
			@Override
			public void onSuccess(int arg0, JSONObject resp) {
				// TODO Auto-generated method stub
				try {
					if (resp.getInt("resultados")>0) {
						for (Marker marcador: marcadores) {
							marcador.remove();
						}
						marcadores.clear();
						JSONArray sucursales = resp.getJSONArray("sucursales");
						for (int i = 0; i < sucursales.length(); i++) {
						    JSONObject sucursal = sucursales.getJSONObject(i);
						    JSONArray latlon = sucursal.getJSONArray("latlon");
						    Log.i("MYTAG", sucursal.toString());
						    // Ponemos el marcador
						    Marker marcador = mapa.addMarker(
						    		new MarkerOptions().position(
						    				new LatLng(latlon.getDouble(0), latlon.getDouble(1))
						    		).title(sucursal.getString("banco").toUpperCase() + ": " + sucursal.getString("nombre"))
						    		.snippet(sucursal.getJSONObject("direccion").getString("calle"))
						    		.icon(BitmapDescriptorFactory.fromResource(getResources().getIdentifier("icon_"+sucursal.getString("banco"), "drawable", getApplicationContext().getPackageName())))
						    		.anchor((float)0.5, (float)0.5)
						    );
						    marcadores.add(marcador);
						}
					}else{
						Toast.makeText(getApplicationContext(), "No se han encontrado bancos en esta area.", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.onSuccess(arg0, resp);
			}
			
			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				// TODO Auto-generated method stub
				Log.i("MYTAG", arg1.toString());
				try {
					Toast.makeText(getApplicationContext(), arg1.getString("error"), Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.onFailure(arg0, arg1);
			}
		});
	}

}
