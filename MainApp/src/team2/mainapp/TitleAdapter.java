package team2.mainapp;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TitleAdapter extends ArrayAdapter<String[]> {
	
	private final Context context;
	private final ArrayList<String[]> values;
	
	public TitleAdapter(Context context, ArrayList<String[]> values) {
		super(context, R.layout.titlelist, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.titlelist, parent, false);
		TextView textView1 = (TextView) rowView.findViewById(R.id.textView1);
		TextView textView2 = (TextView) rowView.findViewById(R.id.textView2);
		textView1.setText(values.get(position)[0]);
		textView2.setText(values.get(position)[1]);
		
		return rowView;
	}
	

}
