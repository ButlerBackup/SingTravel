package dev.blacksheep.trif.classes;

import java.text.DecimalFormat;
import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.ion.Ion;
import com.securepreferences.SecurePreferences;

import dev.blacksheep.trif.Consts;
import dev.blacksheep.trif.R;
import dev.blacksheep.trif.SQLFunctions;

public class Utils {

	Context context;

	public Utils(Context con) {
		context = con;
	}

	public String getTickets(String event) {
		SecurePreferences sp = new SecurePreferences(context);
		return sp.getString(event, "0");
	}

	public void wifiState(boolean on) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (on) {
			wifiManager.setWifiEnabled(true);
		} else {
			wifiManager.setWifiEnabled(false);
		}
	}

	public void gpsState(boolean on) {
		Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		if (on) {
			intent.putExtra("enabled", true);
		} else {
			intent.putExtra("enabled", false);

		}
		context.sendBroadcast(intent);
		final Intent poke = new Intent();
		poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
		poke.setData(Uri.parse("3"));
		context.sendBroadcast(poke);
	}

	public void showLostNotification(String subject, String message, Class<?> cls, boolean onGoing, int id) {
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		@SuppressWarnings("deprecation")
		Notification notification = new Notification(R.drawable.ic_launcher, "I'm Lost!", System.currentTimeMillis());
		if (onGoing) {
			notification.flags = Notification.FLAG_ONGOING_EVENT;
		}
		Intent notificationIntent = new Intent(context, cls);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, subject, message, pendingIntent);
		notificationManager.notify(id, notification);
	}

	public int taxiNumber() {
		Random r = new Random(System.currentTimeMillis());
		return (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);
	}

	public void setPoints() {
		SecurePreferences sp = new SecurePreferences(context);
		if (sp.getInt("points", 0) == 0) {
			sp.edit().putInt("points", 10).commit();
		}
	}

	public boolean compareWalletAmount(double value) {
		if (Double.parseDouble(getWalletAmount()) >= value) {
			return true;
		} else {
			return false;
		}
	}

	public String getWalletAmount() {
		DecimalFormat df = new DecimalFormat("####0.00");
		SecurePreferences sp = new SecurePreferences(context);
		return String.valueOf(df.format(Double.valueOf(sp.getString("wallet", "0.00"))));
	}

	public boolean minusWalletAmount(double value) {
		SecurePreferences sp = new SecurePreferences(context);
		double wallet = Double.parseDouble(sp.getString("wallet", "0.00"));
		double newWalletAmount = wallet - value;
		if (sp.edit().putString("wallet", String.valueOf(newWalletAmount)).commit()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean addWalletAmount(double value) {
		Log.e("WALLET", "ADDING " + value);
		SecurePreferences sp = new SecurePreferences(context);
		double wallet = Double.parseDouble(sp.getString("wallet", "0.00"));
		double newWalletAmount = wallet + value;
		Log.e("NEW WALLET", String.valueOf(newWalletAmount));
		if (sp.edit().putString("wallet", String.valueOf(newWalletAmount)).commit()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean getPhotoDone() {
		SecurePreferences sp = new SecurePreferences(context);
		return sp.getBoolean("photo", false);
	}

	public boolean setCameraPhotoDone() {
		SecurePreferences sp = new SecurePreferences(context);
		if (sp.edit().putBoolean("photo", true).commit()) {
			return true;
		} else {
			return false;
		}
	}

	public int getPoints() {
		SecurePreferences sp = new SecurePreferences(context);
		return sp.getInt("points", 0);
	}

	public boolean minusPointsAmount(double value) {
		SecurePreferences sp = new SecurePreferences(context);
		double points = sp.getInt("points", 0);
		double newPointsAmount = points - value;
		Log.e("NEW POINTS", "" + (int) newPointsAmount);
		if (sp.edit().putInt("points", (int) newPointsAmount).commit()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean addPointsAmount(double value) {
		SecurePreferences sp = new SecurePreferences(context);
		double points = sp.getInt("points", 0);
		double newPointsAmount = points + value;
		Log.e("NEW POINTS", "" + (int) newPointsAmount);
		if (sp.edit().putInt("points", (int) newPointsAmount).commit()) {
			return true;
		} else {
			return false;
		}
	}

	public void setTickets(String title, String value) {
		SecurePreferences sp = new SecurePreferences(context);
		sp.edit().putString(title, value).commit();
	}
}
