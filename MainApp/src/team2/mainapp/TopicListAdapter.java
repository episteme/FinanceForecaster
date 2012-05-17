package team2.mainapp;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TopicListAdapter extends ArrayAdapter<Topic> {
	
	private final Context context;
	private final ArrayList<Topic> values;
	
	public TopicListAdapter(Context context, ArrayList<Topic> values) {
		super(context, R.layout.topiclayout, values);
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
		Log.d("Debug", values.get(position).getTitle());
		textView.setText(values.get(position).getTitle());
		String s = "Aggregated from " + values.get(position).getArts() + " source";
		if((values.get(position).getArts() != 1))
			s += "s";
		s += " since " + values.get(position).getDate();
		textView2.setText(s);
		textView3.setText(values.get(position).getWords());
		textView4.setText(Integer.toString(values.get(position).getUid()));
		textView5.setText(values.get(position).getSector());
		
		switch(values.get(position).getState()){
			case -1:
				rowView.setBackgroundColor(Color.rgb(255,50,50));break;
			case 1:
				rowView.setBackgroundColor(Color.rgb(235,235,235));break;
		}
		return rowView;
	}
	
	

}
