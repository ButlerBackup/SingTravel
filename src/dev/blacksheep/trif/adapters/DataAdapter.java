package dev.blacksheep.trif.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import dev.blacksheep.trif.R;

public class DataAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public DataAdapter(Context con, ArrayList<HashMap<String, String>> d) {
		context = con;
		data = d;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//Ion.getDefault(context).configure().setLogging("MyLogs", Log.DEBUG);
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
		public ImageView ivImage;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_item, null);

			holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HashMap<String, String> item = new HashMap<String, String>();
		item = data.get(position);
		final String image = item.get("image").replace("62fx62f", "350fx275f");
		final String name = item.get("name");
		holder.tvName.setText(name);
		Ion.with(holder.ivImage).placeholder(R.drawable.placeholder).load(image);
		holder.tvName.setTextColor(Color.parseColor("#" + item.get("color")));
		holder.ivImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*Intent i = new Intent(context, FullscreenImage.class);
				i.putExtra("image", image).putExtra("name", name);
				context.startActivity(i);*/
			}
		});
		return convertView;
	}

}
