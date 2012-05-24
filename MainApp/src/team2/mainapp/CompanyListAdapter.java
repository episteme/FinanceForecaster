package team2.mainapp;

import java.util.ArrayList;

import team2.mainapp.R.drawable;

import android.content.Context;
import android.graphics.Color;
import android.opengl.Visibility;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CompanyListAdapter extends ArrayAdapter<Company> {
	
	private final Context context;
	private final ArrayList<Company> values;
	
	public CompanyListAdapter(Context context, ArrayList<Company> values) {
		super(context, R.layout.companylayout, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.companylayout, parent, false);
		TextView textView1 = (TextView) rowView.findViewById(R.id.name);
		TextView textView2 = (TextView) rowView.findViewById(R.id.mentions);
		TextView textView3 = (TextView) rowView.findViewById(R.id.price);
		TextView textView4 = (TextView) rowView.findViewById(R.id.change);
		TextView textView5 = (TextView) rowView.findViewById(R.id.outlook);
		TextView textView6 = (TextView) rowView.findViewById(R.id.sector);
		
		Company thiscompany = values.get(position);
		
		textView1.setText(thiscompany.getName());
		
		if(thiscompany.isTraded()){
			
			String htmlColor;
			if(thiscompany.getStockChange() < 0)
				htmlColor = "<font color=#BB0000>";
			else
				htmlColor = "<font color=#00BB00>";
			
			textView3.setText(Double.toString(thiscompany.getStockPrice()));
			textView4.setText(Html.fromHtml(htmlColor + Math.abs(thiscompany.getStockChange()) + "</font>"));
			}else{
				textView3.setText("N/A");
				textView4.setVisibility(View.GONE);
			}
			
			ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1);

			
			double sent = thiscompany.getSentiment();
			String sentiment;
			if(sent > 0){
				sentiment = "positive";
				imageView.setImageResource(team2.mainapp.R.drawable.arrow_up);
			}
			else{
				sentiment = "negative";
				imageView.setImageResource(team2.mainapp.R.drawable.arrow_down);
			}
		
			textView2.setText("Mentioned in " + thiscompany.getArticles() + " topics");
			textView5.setText("Overall outlook for this company is " + sentiment);
			
			textView6.setText(thiscompany.getSector());
			
			
			
			Log.d("COMPANYLINKADAPTER","WORKING");

			return rowView;
	}
	
	

}
