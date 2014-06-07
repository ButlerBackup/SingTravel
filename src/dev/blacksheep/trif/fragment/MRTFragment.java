package dev.blacksheep.trif.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dev.blacksheep.trif.R;

public class MRTFragment extends Fragment {

	public MRTFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.mrt_layout, container, false);
		return rootView;
	}

}
