package dev.blacksheep.trif;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

public class PreferenceActivity extends SherlockPreferenceActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		Preference versionPref = (Preference) findPreference("pref_version");
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			versionPref.setTitle("Version : " + pInfo.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		versionPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				try {
					Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Consts.STEAM_TRADE_URL));
					startActivity(myIntent);
				} catch (Exception e) {
					Toast.makeText(PreferenceActivity.this, "Unable to open trade link :( ", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				return false;
			}
		});

		Preference myPref = (Preference) findPreference("pref_contact");
		myPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL, new String[] { "shaunblacksheep@gmail.com" });
				i.putExtra(Intent.EXTRA_SUBJECT, "CS:GO Skins DB");
				try {
					startActivity(Intent.createChooser(i, "Send mail..."));
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(PreferenceActivity.this, "There are no email clients installed. Feel free to contact me at \"shaunblacksheep[AT]gmail.com\"", Toast.LENGTH_LONG)
							.show();
				}
				return false;
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}