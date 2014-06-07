package dev.blacksheep.trif;

import com.securepreferences.SecurePreferences;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashActivity extends Activity {
	private final Handler mHandler = new Handler();
	private Runnable mTask = new Runnable() {
		public void run() {
			SecurePreferences settings = new SecurePreferences(SplashActivity.this);
			if (!settings.getString("initial", "0").equals("1")) {
				Log.e("FIRSTLOAD", "FIRSTLOAD");
				settings.edit().putString("wallet", "300.00").commit();
			}
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		mHandler.postDelayed(mTask, 1000);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacks(mTask);
	}
}
