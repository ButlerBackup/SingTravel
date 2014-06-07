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
	SeekBar sbTicketsPeople;
	TextView tvTicketsPeople, tvTicketsPrices, tvWalletAmount;
	Button bBuyTickets;
	ImageView ivQRCode;
	double rate = 0;
	String title;
	Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.book_tickets_activity);
		Intent i = getIntent();
		title = i.getStringExtra("title");
		rate = title.length() * 2.75;
		ivQRCode = (ImageView) findViewById(R.id.ivQRCode);
		bBuyTickets = (Button) findViewById(R.id.bBuyTickets);
		bBuyTickets.setEnabled(false);
		bBuyTickets.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (new Utils(BookTicketsActivity.this).compareWalletAmount(sbTicketsPeople.getProgress() * rate)) {
					String value = (sbTicketsPeople.getProgress() * rate) + "|" + title;
					QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(value, null, Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), 250);
					if (bitmap != null) {
						bitmap.recycle();
					}
					try {
						bitmap = qrCodeEncoder.encodeAsBitmap();
						ivQRCode.setImageBitmap(bitmap);
						new Utils(BookTicketsActivity.this).minusWalletAmount(sbTicketsPeople.getProgress() * rate);
						Toast.makeText(BookTicketsActivity.this, "Successfully booked tickets. Show this at entrance", Toast.LENGTH_SHORT).show();
						tvWalletAmount.setText("Wallet : " + String.valueOf(new Utils(BookTicketsActivity.this).getWalletAmount()));

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
		tvWalletAmount.setText("Wallet : " + String.valueOf(new Utils(BookTicketsActivity.this).getWalletAmount()));
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
				tvTicketsPeople.setText(progress + " Person(s)");
				tvTicketsPrices.setText("$" + String.valueOf(progress * rate));
				if (progress > 0) {
					bBuyTickets.setEnabled(true);
				} else {
					bBuyTickets.setEnabled(false);
				}
			}
		});
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

}
