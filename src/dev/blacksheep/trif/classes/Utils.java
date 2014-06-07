package dev.blacksheep.trif.classes;

import java.text.DecimalFormat;
import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

	public boolean checkForUpdates() {
		SQLFunctions sql = new SQLFunctions(context);
		sql.open();
		Future<String> s = Ion.with(context, Consts.ITEMS_UPDATE_DATABASE_LINK + "?id=" + sql.getLastRowId()).asString(); // ADD
																															// //
																															// SHIT
		sql.close();
		try {
			String data = s.get();
			Log.e("checkForUpdates", data);
			if (data.equals(Consts.ITEMS_UPDATE_DATABASE_YES)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
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
}
