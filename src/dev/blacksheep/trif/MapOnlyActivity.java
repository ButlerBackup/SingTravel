package dev.blacksheep.trif;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapOnlyActivity extends Activity {
	private GoogleMap googleMap;
	long longtitude, langtitude;
	String address = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_only_activity);
		Intent i = getIntent();
		longtitude = (long) 103.793023;
		langtitude = (long) 1.404348;
		address = i.getStringExtra("address");
		try {
			initialiseMaps();
		} catch (Exception e) {
			Toast.makeText(MapOnlyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	private void initialiseMaps() {

		try {
			if (googleMap == null) {
				googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
				LatLng currentLocation = new LatLng(langtitude, longtitude);
				googleMap.setMyLocationEnabled(true);
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));
				googleMap.addMarker(new MarkerOptions().title("You are here!").snippet(address).position(currentLocation));
				if (googleMap == null) {
					Toast.makeText(MapOnlyActivity.this, "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
