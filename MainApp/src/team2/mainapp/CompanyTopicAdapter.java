package team2.mainapp;

import java.util.ArrayList;

import team2.mainapp.R.drawable;

import android.app.Activity;
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

public class CompanyTopicAdapter extends ArrayAdapter<TopicLink> {

	private final Context context;
	private final ArrayList<TopicLink> values;

	public CompanyTopicAdapter(Context context, ArrayList<TopicLink> values) {
		super(context, R.layout.keywordlist, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.comptoplist, parent, false);

		CompanyLink thisCL = values.get(position).getCl();
		Topic thistopic = values.get(position).getT();
		Company thiscompany = values.get(position).getC();

		TextView textView1 = (TextView) rowView.findViewById(R.id.textView1);
		textView1.setText(thistopic.getTitle());
		TextView textView2 = (TextView) rowView.findViewById(R.id.textView2);

		String s = "Aggregated from " + thistopic.getArts() + " source";
		if((thistopic.getArts() != 1))
			s += "s";
		s += " since " + thistopic.getDate();
		textView2.setText(s);

		ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1);


		double sent = thisCL.getSentiment();
		String sentiment;
		if(sent > 0){
			sentiment = "positive";
			imageView.setImageResource(drawable.arrow_up);
		}
		else{
			sentiment = "negative";
			imageView.setImageResource(drawable.arrow_down);
		}

		TextView textView3 = (TextView) rowView.findViewById(R.id.textView3);
		textView3.setText("Outlook based on this topic is " + sentiment);

		TextView textView4 = (TextView) rowView.findViewById(R.id.uid);
		TextView textView5 = (TextView) rowView.findViewById(R.id.sector);

		textView4.setText(Integer.toString(thistopic.getUid()));
		textView5.setText(thistopic.getSector());

		Log.d("COMPANYTOPICADAPTER","WORKING");

		return rowView;
	}



}
