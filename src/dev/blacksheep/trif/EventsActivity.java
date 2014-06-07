package dev.blacksheep.trif;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

public class EventsActivity extends Activity {
	String latitude, longtitude;
	String address = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FadingActionBarHelper helper = new FadingActionBarHelper().actionBarBackground(R.drawable.abs__ab_bottom_solid_dark_holo).headerLayout(R.layout.events_activity_header)
				.contentLayout(R.layout.events_activity);
		setContentView(helper.createView(this));
		ImageView ivHeader = (ImageView) findViewById(R.id.image_header);
		TextView tvDetails = (TextView) findViewById(R.id.tvDetails);
		Intent i = getIntent();
		String image = i.getStringExtra("image");
		String name = i.getStringExtra("name");
		String details = "";
		if (name.equals("Zoo")) {
			address = "80 Mandai Lake Rd, 729826 8.30am – 6.30pm";
			latitude = "1.404348";
			longtitude = "103.793023";
			details = EventsActivity.this.getResources().getString(R.string.event_singapore_zoo).toString();
		} else if (name.equals("Botanical Garden")) {
			address = "1 Cluny Rd, 259569 5.00am – 12.00mn";
			latitude = "1.322519";
			longtitude = "103.815406";
			details = EventsActivity.this.getResources().getString(R.string.event_botanical_garden).toString();
		} else if (name.equals("River Safari")) {
			address = "80 Mandai Lake Road, 729826 9.00am – 6.00pm";
			details = EventsActivity.this.getResources().getString(R.string.event_river_safari).toString();
		} else if (name.equals("Night Safari")) {
			latitude = "1.403836";
			longtitude = "103.789668";
			address = "80 Mandai Lake Rd, 729826 7.30pm – 12.00mn";
			details = EventsActivity.this.getResources().getString(R.string.event_night_safari).toString();
		} else if (name.equals("BirdPark")) {
			latitude = "1.321298";
			longtitude = "103.707755";
			address = "2 Jurong Hill Singapore 628925 8.30am – 6.00pm";
			details = EventsActivity.this.getResources().getString(R.string.event_birdpark).toString();
		} else if (name.equals("Garden By The Bay")) {
			latitude = "1.281514";
			longtitude = "103.864047";
			address = "18 Marina Gardens Dr, 018953 5:00 AM - 2:00 AM, Daily";
			details = EventsActivity.this.getResources().getString(R.string.event_garden_by_the_bay).toString();
		}
		invalidateOptionsMenu();
		tvDetails.setText(Html.fromHtml(details));
		tvDetails.setMovementMethod(LinkMovementMethod.getInstance());

		Ion.with(ivHeader).load(image);
		helper.initActionBar(this);
		getActionBar().setTitle(name);
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.events_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.push_down_in_opposite, R.anim.push_down_out_opposite);
			break;
		case R.id.menu_book_tickets:
			startActivity(new Intent(EventsActivity.this, BookTicketsActivity.class).putExtra("title", getActionBar().getTitle()));
			overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
			break;

		//case R.id.menu_map:
		//	startActivity(new Intent(EventsActivity.this, MapOnlyActivity.class).putExtra("address", address).putExtra("lat", latitude).putExtra("long", longtitude));
		//	break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

}