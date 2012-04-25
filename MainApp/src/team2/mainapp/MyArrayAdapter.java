package team2.mainapp;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter<String[]> {
	
	private final Context context;
	private final ArrayList<String[]> values;
	
	public MyArrayAdapter(Context context, ArrayList<String[]> values) {
		super(context, R.layout.rowlayout, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.labelo);
		TextView textView2 = (TextView) rowView.findViewById(R.id.labelp);
		TextView textView3 = (TextView) rowView.findViewById(R.id.labelq);
		Log.d("Debug", Integer.toString(values.size()));
		Log.d("Debug", values.get(position)[0]);
		textView.setText(values.get(position)[0]);
		textView2.setText(values.get(position)[1]);
		textView3.setText(values.get(position)[2]);


		return rowView;
	}
	
	

}
