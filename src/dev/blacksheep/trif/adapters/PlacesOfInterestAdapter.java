package dev.blacksheep.trif.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import dev.blacksheep.trif.R;
import dev.blacksheep.trif.classes.Item;

public class PlacesOfInterestAdapter extends BaseAdapter {
	Context context;
	int layoutResourceId;
	ArrayList<Item> data = new ArrayList<Item>();

	public PlacesOfInterestAdapter(Context context, int layoutResourceId, ArrayList<Item> data) {
		super();
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		Log.e("ADAPTER", "adapter");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RecordHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new RecordHolder();
			holder.txtTitle = (TextView) row.findViewById(R.id.tvPlacesName);
			holder.imageItem = (ImageView) row.findViewById(R.id.ivPlacesImage);
			holder.tvImage = (TextView) row.findViewById(R.id.tvImage);
			row.setTag(holder);
		} else {
			holder = (RecordHolder) row.getTag();
		}
		
		Item i = data.get(position);
		Log.e("ADAPTER", i.getName());
		Ion.with(holder.imageItem).load(i.getImage());
		holder.tvImage.setText(i.getImage());
		holder.txtTitle.setText(i.getName());
		return row;

	}

	static class RecordHolder {
		TextView txtTitle;
		TextView tvImage;
		ImageView imageItem;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
