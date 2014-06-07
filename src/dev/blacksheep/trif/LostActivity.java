package dev.blacksheep.trif;

import java.util.List;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import dev.blacksheep.trif.classes.GPSTracker;
import dev.blacksheep.trif.classes.Utils;

public class LostActivity extends Activity {
	private GoogleMap googleMap;
	private Button bFlagTaxi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lost_activity);
		bFlagTaxi = (Button) findViewById(R.id.bFlagTaxi);
		bFlagTaxi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				bFlagTaxi.setEnabled(false);
				new Utils(getApplicationContext()).showLostNotification("Taxi is on the way!", "Your taxi is SFL " + new Utils(getApplicationContext()).taxiNumber() + "B", MainActivity.class, true, 2);
				finish();
			}
		});
		try {
			new initialiseMaps().execute();
		} catch (Exception e) {
			Toast.makeText(LostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	private class initialiseMaps extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Toast.makeText(LostActivity.this, "Trying to get location..", Toast.LENGTH_SHORT).show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				if (googleMap == null) {
					googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
					GPSTracker gps = new GPSTracker(LostActivity.this);
					if (gps.canGetLocation()) {
						LatLng currentLocation = new LatLng(gps.getLatitude(), gps.getLongitude());
						googleMap.setMyLocationEnabled(true);
						googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));
						Geocoder gCoder = new Geocoder(LostActivity.this);
						List<Address> addresses;
						String address = "Unable to get location.";
						try {
							addresses = gCoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);
							if (addresses != null && addresses.size() > 0) {
								address = addresses.get(0).getFeatureName();
							}
						} catch (Exception e) {
							Toast.makeText(LostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}
						googleMap.addMarker(new MarkerOptions().title("You are here!").snippet(address).position(currentLocation));
					}
					if (googleMap == null) {
						Toast.makeText(LostActivity.this, "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}

}
