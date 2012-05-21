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

public class GoogleArrayAdapter extends ArrayAdapter<GoogleStory> {
	
	private final Context context;
	private final ArrayList<GoogleStory> values;
	
	public GoogleArrayAdapter(Context context, ArrayList<GoogleStory> values) {
		super(context, R.layout.googlerow, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		GoogleStory current = values.get(position);
		View rowView = inflater.inflate(R.layout.googlerow, parent, false);
		TextView textView1 = (TextView) rowView.findViewById(R.id.labelo);
		textView1.setText(current.getTitle());
		TextView textView2 = (TextView) rowView.findViewById(R.id.labelp);
		textView2.setText(current.getTimestamp());
		TextView textView3 = (TextView) rowView.findViewById(R.id.labelq);
		textView3.setText(Html.fromHtml(current.printKeyWords()));
		TextView textView4 = (TextView) rowView.findViewById(R.id.labelr);
		textView4.setText(current.getLink().substring(0,current.getLink().length()-11));
		
		double sentiment = current.getSentiment();
		
		Double green = ((sentiment + 1) / 2.0) * 255;
		Double red = 255.0 - green;
		Integer igreen = (int) Math.round(green);
		Integer ired = (int) Math.round(red);
		
		textView1.setTextColor(Color.rgb(ired, igreen, 0));

		Log.d("Debug", Integer.toString(values.size()));
		return rowView;
	}
	
	

}
