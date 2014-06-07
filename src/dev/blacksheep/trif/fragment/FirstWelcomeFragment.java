package dev.blacksheep.trif.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.securepreferences.SecurePreferences;

import dev.blacksheep.trif.R;

public class FirstWelcomeFragment extends Fragment {
	public FirstWelcomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.first_welcome_layout, container, false);
		Button bRegister = (Button) rootView.findViewById(R.id.bRegister);
		final EditText etName = (EditText) rootView.findViewById(R.id.etName);
		final EditText etPassport = (EditText) rootView.findViewById(R.id.etPassport);
		final EditText etContact = (EditText) rootView.findViewById(R.id.etContact);
		final EditText etEmail = (EditText) rootView.findViewById(R.id.etEmail);
		bRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (etName.getText().toString().length() < 1 || etPassport.getText().toString().length() < 1 || etContact.getText().toString().length() < 1
						|| etEmail.getText().toString().length() < 1) {
					Toast.makeText(getActivity(), "Please fill up all the fields", Toast.LENGTH_LONG).show();
				} else {
					SecurePreferences sp = new SecurePreferences(getActivity());
					sp.edit().putString("initial", "1").commit();
					Intent i = getActivity().getPackageManager().getLaunchIntentForPackage(getActivity().getPackageName());
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}
			}
		});

		return rootView;
	}

}
