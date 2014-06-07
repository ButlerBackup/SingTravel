package dev.blacksheep.trif.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import dev.blacksheep.trif.R;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class FragmentTemplate extends Fragment {
	EditText etRefinedMetal, etEarbuds, etKeys, etUSD;
	double refToUSD, keyToRefined, earbudToKey;
	DecimalFormat df = new DecimalFormat("#.00");

	public FragmentTemplate() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.maps_layout, container, false);

		return rootView;
	}

}
