package dev.blacksheep.trif.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import dev.blacksheep.trif.R;
import dev.blacksheep.trif.classes.Utils;

public class WalletFragment extends Fragment {
	TextView tvCurrentWalletAmount;
	Button bTopupWallet;

	public WalletFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.wallet_fragment, container, false);
		tvCurrentWalletAmount = (TextView) rootView.findViewById(R.id.tvCurrentWalletAmount);
		tvCurrentWalletAmount.setText("Wallet : $" + new Utils(getActivity()).getWalletAmount());
		bTopupWallet = (Button) rootView.findViewById(R.id.bTopupWallet);
		bTopupWallet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});
		return rootView;
	}

}
