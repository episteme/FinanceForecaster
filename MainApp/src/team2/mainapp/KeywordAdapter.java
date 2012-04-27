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

public class KeywordAdapter extends ArrayAdapter<KeyWord> {
	
	private final Context context;
	private final ArrayList<KeyWord> values;
	
	public KeywordAdapter(Context context, ArrayList<KeyWord> values) {
		super(context, R.layout.keywordlist, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.keywordlist, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.textView1);
		textView.setText(values.get(position).getWord());
		Double sentiment = values.get(position).getSentiment();
		Double green = ((sentiment + 1) / 2.0) * 255;
		Double red = 255.0 - green;
		Integer igreen = (int) Math.round(green);
		Integer ired = (int) Math.round(red);
		
		textView.setTextColor(Color.rgb(ired, igreen, 0));

		return rowView;
	}
	
	

}
