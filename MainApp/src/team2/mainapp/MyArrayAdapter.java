package team2.mainapp;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
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
		TextView textView4 = (TextView) rowView.findViewById(R.id.uid);
		TextView textView5 = (TextView) rowView.findViewById(R.id.sector);
		Log.d("Debug", Integer.toString(values.size()));
		Log.d("Debug", values.get(position)[0]);
		textView.setText(values.get(position)[0]);
		textView2.setText(values.get(position)[1]);
		textView3.setText(values.get(position)[2]);
		textView4.setText(values.get(position)[3]);
		textView5.setText(values.get(position)[4]);
		
//		Double sentiment = Double.parseDouble(values.get(position)[6]);
//		Double green = ((sentiment + 1) / 2.0) * 255;
//		Double red = 255.0 - green;
//		Integer igreen = (int) Math.round(green);
//		Integer ired = (int) Math.round(red);
//		
//		rowView.setBackgroundColor(Color.rgb(ired, igreen, 0));
		
		switch(Integer.parseInt(values.get(position)[5])){
			case -1:
				rowView.setBackgroundColor(Color.rgb(255,50,50));break;
			case 1:
				rowView.setBackgroundColor(Color.rgb(235,235,235));break;
		}
		return rowView;
	}



}
