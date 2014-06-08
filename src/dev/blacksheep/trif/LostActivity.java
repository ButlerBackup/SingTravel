package dev.blacksheep.trif;

import java.util.List;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
		//try {
		//	initialiseMaps();
		//} catch (Exception e) {
		//	Toast.makeText(LostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
		//}
	}

	private void initialiseMaps() {
		try {
			if (googleMap == null) {
				googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
				LatLng currentLocation = new LatLng(1.345315, 103.931822);
				googleMap.setMyLocationEnabled(true);
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18));
				Geocoder gCoder = new Geocoder(LostActivity.this);
				List<Address> addresses;
				String address = "Unable to get location.";
				try {
					addresses = gCoder.getFromLocation(1.345315, 103.931822, 1);
					if (addresses != null && addresses.size() > 0) {
						address = String.format("%s, %s, %s", addresses.get(0).getMaxAddressLineIndex() > 0 ? addresses.get(0).getAddressLine(0) : "", addresses.get(0).getLocality(), addresses.get(0)
								.getCountryName());
					}
				} catch (Exception e) {
					Toast.makeText(LostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				googleMap.addMarker(new MarkerOptions().title("You are here!").snippet(address).position(currentLocation));
				googleMap.addMarker(new MarkerOptions().title("Taxi Stand").icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi)).snippet(address)
						.position(new LatLng(1.344521284847749, 103.93132847354127)));
				googleMap.addMarker(new MarkerOptions().title("Taxi Stand").icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi)).snippet(address)
						.position(new LatLng(1.3456796798482762, 103.9316932539673)));

				googleMap.addMarker(new MarkerOptions().title("Taxi Stand").icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi)).snippet(address)
						.position(new LatLng(1.3450361271380884, 103.93227261111451)));

				googleMap.addMarker(new MarkerOptions().title("Taxi Stand").icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi)).snippet(address)
						.position(new LatLng(1.34647339462361, 103.93255156085206)));

				/*
				 * LocationResult locationResult = new LocationResult() {
				 * 
				 * @Override public void gotLocation(Location location) { if
				 * (location != null) { Log.e("MAPS", "got location"); LatLng
				 * currentLocation = new LatLng(location.getLatitude(),
				 * location.getLongitude());
				 * googleMap.setMyLocationEnabled(true);
				 * googleMap.moveCamera(CameraUpdateFactory
				 * .newLatLngZoom(currentLocation, 13)); Geocoder gCoder = new
				 * Geocoder(LostActivity.this); List<Address> addresses; String
				 * address = "Unable to get location."; try { addresses =
				 * gCoder.getFromLocation(location.getLatitude(),
				 * location.getLongitude(), 1); if (addresses != null &&
				 * addresses.size() > 0) { address = String.format("%s, %s, %s",
				 * addresses.get(0).getMaxAddressLineIndex() > 0 ?
				 * addresses.get(0).getAddressLine(0) : "",
				 * addresses.get(0).getLocality(),
				 * addresses.get(0).getCountryName()); } } catch (Exception e) {
				 * Toast.makeText(LostActivity.this, e.getMessage(),
				 * Toast.LENGTH_SHORT).show(); e.printStackTrace(); }
				 * googleMap.addMarker(new
				 * MarkerOptions().title("You are here!")
				 * .snippet(address).position(currentLocation)); } else {
				 * Toast.makeText(LostActivity.this, "Unable to get location",
				 * Toast.LENGTH_SHORT).show(); } } }; MyLocation myLocation =
				 * new MyLocation(); myLocation.getLocation(LostActivity.this,
				 * locationResult);
				 */
				if (googleMap == null) {
					Toast.makeText(LostActivity.this, "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
