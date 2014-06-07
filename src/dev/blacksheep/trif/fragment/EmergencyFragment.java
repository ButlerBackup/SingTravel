package dev.blacksheep.trif.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import dev.blacksheep.trif.R;
import dev.blacksheep.trif.adapters.EmergencyAdapter;

public class EmergencyFragment extends Fragment {
	ListView lvEmergency;

	public EmergencyFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.emergency_activity, container, false);
		ArrayList<HashMap<String, String>> contacts = new ArrayList<HashMap<String, String>>();
		lvEmergency = (ListView) rootView.findViewById(R.id.lvEmergency);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("name", "Emergency Ambulance and fire");
		map.put("number", "995");
		contacts.add(map);
		map = new HashMap<String, String>();
		map.put("name", "Non-Emergency Ambulance");
		map.put("number", "1777");
		contacts.add(map);
		map = new HashMap<String, String>();
		map.put("name", "Police Emergency");
		map.put("number", "999");
		contacts.add(map);
		map = new HashMap<String, String>();
		map.put("name", "Police Hotline");
		map.put("number", "1800 255 0000");
		contacts.add(map);
		map = new HashMap<String, String>();
		map.put("name", "Drugs & Poison (non-emergency)");
		map.put("number", "6423 9119");
		contacts.add(map);
		EmergencyAdapter adapter = new EmergencyAdapter(getActivity(), contacts);
		lvEmergency.setAdapter(adapter);
		lvEmergency.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				TextView tvNumber = (TextView) arg1.findViewById(R.id.tvNumber);
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:" + tvNumber.getText().toString()));
				startActivity(intent);
			}
		});
		return rootView;
	}

}
