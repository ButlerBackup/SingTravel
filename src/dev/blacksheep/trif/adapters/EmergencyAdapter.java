package dev.blacksheep.trif.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dev.blacksheep.trif.R;

public class EmergencyAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public EmergencyAdapter(Context con, ArrayList<HashMap<String, String>> d) {
		context = con;
		data = d;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// Ion.getDefault(context).configure().setLogging("MyLogs", Log.DEBUG);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	static class ViewHolder {
		public TextView tvName;
		public TextView tvNumber;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.emergency_activity_item, null);

			holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.tvNumber = (TextView) convertView.findViewById(R.id.tvNumber);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HashMap<String, String> item = new HashMap<String, String>();
		item = data.get(position);
		final String number = item.get("number");
		final String name = item.get("name");
		holder.tvName.setText(name);
		holder.tvNumber.setText(number);
		return convertView;
	}

}
