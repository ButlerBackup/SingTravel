package dev.blacksheep.trif;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.jwetherell.quick_response_code.data.Contents;
import com.jwetherell.quick_response_code.qrcode.QRCodeEncoder;

import dev.blacksheep.trif.classes.Utils;

public class BookTicketsActivity extends Activity {
	SeekBar sbTicketsPeople, sbPoints;
	TextView tvTicketsPeople, tvTicketsPrices, tvWalletAmount, tvPoints;
	Button bBuyTickets;
	ImageView ivQRCode;
	double rate = 0;
	String title;
	Bitmap bitmap;
	int points, person, pointsToUse;
	double finalPrice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.book_tickets_activity);
		Intent i = getIntent();
		title = i.getStringExtra("title");
		rate = title.length() * 2.75;
		finalPrice = (double) rate;
		person = 1;
		pointsToUse = 0;
		ivQRCode = (ImageView) findViewById(R.id.ivQRCode);
		bBuyTickets = (Button) findViewById(R.id.bBuyTickets);
		bBuyTickets.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (new Utils(BookTicketsActivity.this).compareWalletAmount(finalPrice)) {
					String value = finalPrice + "|" + title;
					new Utils(BookTicketsActivity.this).setTickets(title, value);
					QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(value, null, Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), 250);
					if (bitmap != null) {
						bitmap.recycle();
					}
					try {
						bitmap = qrCodeEncoder.encodeAsBitmap();
						ivQRCode.setImageBitmap(bitmap);
						new Utils(BookTicketsActivity.this).minusWalletAmount(sbTicketsPeople.getProgress() * rate);
						tvWalletAmount.setText("Wallet : " + String.valueOf(new Utils(BookTicketsActivity.this).getWalletAmount()));
						if (pointsToUse > 0) {
							new Utils(BookTicketsActivity.this).minusPointsAmount(pointsToUse);
						}
						new Utils(BookTicketsActivity.this).addPointsAmount(person);
						Toast.makeText(BookTicketsActivity.this, "Successfully booked tickets. Show this at entrance\n" + person + " points have been added to your wallet!", Toast.LENGTH_SHORT)
								.show();

					} catch (WriterException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(BookTicketsActivity.this, "Wallet do not have enough money", Toast.LENGTH_SHORT).show();
				}
			}
		});
		tvTicketsPrices = (TextView) findViewById(R.id.tvTicketsPrices);
		tvTicketsPrices.setText("$" + String.valueOf(rate));
		tvTicketsPeople = (TextView) findViewById(R.id.tvTicketsPeople);
		tvWalletAmount = (TextView) findViewById(R.id.tvWalletAmount);
		tvWalletAmount.setText("Wallet : $" + String.valueOf(new Utils(BookTicketsActivity.this).getWalletAmount()));
		points = new Utils(BookTicketsActivity.this).getPoints();
		tvPoints = (TextView) findViewById(R.id.tvPoints);
		tvPoints.setText("0 Points");
		sbPoints = (SeekBar) findViewById(R.id.sbPoints);
		sbPoints.setMax(points);
		sbPoints.setProgress(0);
		sbPoints.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				tvPoints.setText(progress + " Point(s)");

				if (progress % 10 == 0) { // if it's multiple of 10
					double discount = (progress / 10) * 5;
					finalPrice = roundOff((person * rate) * (double) ((100 - discount) / 100));
					tvPoints.setText(progress + " Point(s) - " + String.format("%d", (int) discount) + "% discount");
					tvTicketsPrices.setText("$" + finalPrice);
					pointsToUse = progress;
				} else {
					pointsToUse = 0;
					finalPrice = person * rate;
					tvTicketsPrices.setText("$" + String.valueOf(person * rate));
				}
			}
		});
		sbTicketsPeople = (SeekBar) findViewById(R.id.sbTicketsPeople);
		sbTicketsPeople.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				person = progress;
				tvTicketsPeople.setText(progress + " Person(s)");
				tvTicketsPrices.setText("$" + String.valueOf(progress * rate));
				finalPrice = progress * rate;
				if (progress > 0) {
					bBuyTickets.setEnabled(true);
				} else {
					bBuyTickets.setEnabled(false);
				}
			}
		});
		
		if (!new Utils(BookTicketsActivity.this).getTickets(title).equals("0")) {
			tvTicketsPrices.setText("Scan QR Code");
			//startActivity(new Intent(BookTicketsActivity.this, ShowTicketsBought.class).putExtra("value", new Utils(BookTicketsActivity.this).getTickets(title)).putExtra("title", title));
			sbPoints.setEnabled(false);
			sbTicketsPeople.setEnabled(false);
			bBuyTickets.setEnabled(false);
			String value = new Utils(BookTicketsActivity.this).getTickets(title);
			QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(value, null, Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), 250);
			if (bitmap != null) {
				bitmap.recycle();
			}
			try {
				bitmap = qrCodeEncoder.encodeAsBitmap();
				ivQRCode.setImageBitmap(bitmap);
			} catch (WriterException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.anim_in_opposite, R.anim.anim_out_opposite);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private double roundOff(double number) {
		return Math.round(number * 100.0) / 100.0;
	}
}
