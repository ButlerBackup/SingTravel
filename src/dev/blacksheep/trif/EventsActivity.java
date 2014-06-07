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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FadingActionBarHelper helper = new FadingActionBarHelper().actionBarBackground(R.drawable.abs__ab_bottom_solid_dark_holo).headerLayout(R.layout.events_activity_header)
				.contentLayout(R.layout.events_activity);
		setContentView(helper.createView(this));
		ImageView ivHeader = (ImageView) findViewById(R.id.image_header);
		TextView tvDetails = (TextView) findViewById(R.id.tvDetails);
		String details = EventsActivity.this.getResources().getString(R.string.event_singapore_zoo).toString();
		tvDetails.setText(Html.fromHtml(details));
		tvDetails.setMovementMethod(LinkMovementMethod.getInstance());
		Intent i = getIntent();
		String image = i.getStringExtra("image");
		String name = i.getStringExtra("name");
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
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

}