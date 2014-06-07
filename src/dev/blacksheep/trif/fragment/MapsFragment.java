package dev.blacksheep.trif.fragment;

import java.util.List;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import dev.blacksheep.trif.R;
import dev.blacksheep.trif.classes.MyLocation;
import dev.blacksheep.trif.classes.MyLocation.LocationResult;

public class MapsFragment extends Fragment {
	private GoogleMap googleMap;

	public MapsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.maps_layout, container, false);
		try {
			// new initialiseMaps().execute();
			loadMap();
		} catch (Exception e) {
			Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		return rootView;
	}

	private class initialiseMaps extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Toast.makeText(getActivity(), "Trying to get location..", Toast.LENGTH_SHORT).show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				if (googleMap == null) {
					googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
					LocationResult locationResult = new LocationResult() {
						@Override
						public void gotLocation(Location location) {
							Log.e("MAPS", "got location");
							LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
							googleMap.setMyLocationEnabled(true);
							googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));
							Geocoder gCoder = new Geocoder(getActivity());
							List<Address> addresses;
							String address = "Unable to get location.";
							try {
								addresses = gCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
								if (addresses != null && addresses.size() > 0) {
									address = addresses.get(0).getFeatureName();
								}
							} catch (Exception e) {
								Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
								e.printStackTrace();
							}
							googleMap.addMarker(new MarkerOptions().title("You are here!").snippet(address).position(currentLocation));

						}
					};
					MyLocation myLocation = new MyLocation();
					myLocation.getLocation(getActivity(), locationResult);

					if (googleMap == null) {
						Toast.makeText(getActivity(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(getActivity(), "postexecute", Toast.LENGTH_SHORT).show();
				}
			});
		}

	}

	private void loadMap() {
		try {
			if (googleMap == null) {
				googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
				LocationResult locationResult = new LocationResult() {
					@Override
					public void gotLocation(Location location) {
						if (location != null) {
							Log.e("MAPS", "got location");
							LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
							googleMap.setMyLocationEnabled(true);
							googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));
							Geocoder gCoder = new Geocoder(getActivity());
							List<Address> addresses;
							String address = "Unable to get location.";
							try {
								addresses = gCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
								if (addresses != null && addresses.size() > 0) {
									address = String.format("%s, %s, %s", addresses.get(0).getMaxAddressLineIndex() > 0 ? addresses.get(0).getAddressLine(0) : "", addresses.get(0).getLocality(),
											addresses.get(0).getCountryName());
								}
							} catch (Exception e) {
								Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
								e.printStackTrace();
							}
							googleMap.addMarker(new MarkerOptions().title("You are here!").snippet(address).position(currentLocation));
						} else {
							Toast.makeText(getActivity(), "Unable to get location", Toast.LENGTH_SHORT).show();
						}
					}
				};
				MyLocation myLocation = new MyLocation();
				myLocation.getLocation(getActivity(), locationResult);

				if (googleMap == null) {
					Toast.makeText(getActivity(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (googleMap != null) {
			googleMap = null;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (googleMap != null) {
			googleMap = null;
		}
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		destroyFragment();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		destroyFragment();

	}

	private void destroyFragment() {
		FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
		ft.remove((Fragment) getActivity().getFragmentManager().findFragmentById(R.id.map));
		ft.commit();
	}
}
