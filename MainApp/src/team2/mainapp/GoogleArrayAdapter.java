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
import android.widget.TextView;

public class GoogleArrayAdapter extends ArrayAdapter<String[]> {
	
	private final Context context;
	private final ArrayList<String[]> values;
	
	public GoogleArrayAdapter(Context context, ArrayList<String[]> values) {
		super(context, R.layout.googlerow, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		String[] current = values.get(position);
		View rowView = inflater.inflate(R.layout.googlerow, parent, false);
		TextView textView1 = (TextView) rowView.findViewById(R.id.labelo);
		textView1.setText(current[0]);
		TextView textView2 = (TextView) rowView.findViewById(R.id.labelp);
		textView2.setText(current[1]);
		TextView textView3 = (TextView) rowView.findViewById(R.id.labelq);
		textView3.setText(Html.fromHtml(current[2]));
		TextView textView4 = (TextView) rowView.findViewById(R.id.labelr);
		textView4.setText(current[4].substring(0,current[4].length()-11));
		
		double sentiment = Double.parseDouble(current[3]);
		
		Double green = ((sentiment + 1) / 2.0) * 255;
		Double red = 255.0 - green;
		Integer igreen = (int) Math.round(green);
		Integer ired = (int) Math.round(red);
		
		textView1.setTextColor(Color.rgb(ired, igreen, 0));

		Log.d("Debug", Integer.toString(values.size()));
		return rowView;
	}
	
	

}
