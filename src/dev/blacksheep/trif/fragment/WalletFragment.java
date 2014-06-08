package dev.blacksheep.trif.fragment;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import dev.blacksheep.trif.R;
import dev.blacksheep.trif.classes.Utils;

public class WalletFragment extends Fragment {
	TextView tvCurrentWalletAmount, tvCurrentPoints;
	Button bTopupWallet;

	public WalletFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.wallet_fragment, container, false);
		tvCurrentWalletAmount = (TextView) rootView.findViewById(R.id.tvCurrentWalletAmount);
		tvCurrentWalletAmount.setText("Wallet : $" + new Utils(getActivity()).getWalletAmount());
		bTopupWallet = (Button) rootView.findViewById(R.id.bTopupWallet);
		bTopupWallet.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				fakeProgress();
				return false;
			}
		});
		bTopupWallet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// https://www.paypal.com/webapps/checkout/webflow/sparta/expresscheckoutvalidatedataflow?execution=e1s3
				Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://mobile.paypal.com/us/cgi-bin/wapapp?cmd=_wapapp-homepage"));
				startActivity(viewIntent);
			}
		});
		tvCurrentPoints = (TextView) rootView.findViewById(R.id.tvCurrentPoints);
		tvCurrentPoints.setText("Point(s) : " + new Utils(getActivity()).getPoints());
		return rootView;
	}

	private void fakeProgress() {
		final ProgressDialog dialog = new ProgressDialog(getActivity());
		dialog.setTitle("Please wait..");
		dialog.setMessage("Processing payment..");
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		long delayInMillis = 5000;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				dialog.dismiss();
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						new Utils(getActivity()).addWalletAmount(150.00);
						new Utils(getActivity()).addPointsAmount(new Random().nextInt(5-1) + 1);
						tvCurrentPoints.setText("Point(s) : " + new Utils(getActivity()).getPoints());
						tvCurrentWalletAmount.setText("Wallet : $" + new Utils(getActivity()).getWalletAmount());
						showMoneyCompleted();
					}
				});
			}
		}, delayInMillis);
	}

	private void showMoneyCompleted() {
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		alert.setTitle("Successful!");
		alert.setMessage("Transaction successful! Enjoy your additions points!");
		alert.setPositiveButton("YAY!", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}
}
