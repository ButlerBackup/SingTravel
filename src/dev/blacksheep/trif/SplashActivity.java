package dev.blacksheep.trif;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.securepreferences.SecurePreferences;

public class SplashActivity extends Activity {
	private final Handler mHandler = new Handler();
	private Runnable mTask = new Runnable() {
		public void run() {
			SecurePreferences settings = new SecurePreferences(SplashActivity.this);
			if (!settings.getString("initial", "0").equals("1")) {
				Log.e("FIRSTLOAD", "FIRSTLOAD");
				settings.edit().putString("wallet", "300.00").commit();
				settings.edit().putInt("points", 10).commit();
				settings.edit().putString("initial", "1").commit();
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
