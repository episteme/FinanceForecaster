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

public class CompanyLinkAdapter extends ArrayAdapter<CompanyLink> {
	
	private final Context context;
	private final ArrayList<CompanyLink> values;
	
	public CompanyLinkAdapter(Context context, ArrayList<CompanyLink> values) {
		super(context, R.layout.keywordlist, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.keywordlist, parent, false);

		GlobalState gState = (GlobalState)  ((Activity) context).getApplication();
		
		Company thiscompany = null;
		
		for(Sector sector : gState.getAllSectors())
		{
			for(Company company : sector.getCompData())
			{
				if(company.getName().equals(values.get(position).getCompany())){
					thiscompany = company;
					break;
				}
			}
		}
		
		TextView textView1 = (TextView) rowView.findViewById(R.id.textView1);
		textView1.setText(thiscompany.getName());
		TextView textView2 = (TextView) rowView.findViewById(R.id.textView2);
		
		if(thiscompany.isTraded()){
		
		String htmlColor;
		if(thiscompany.getStockChange() < 0)
			htmlColor = "<font color=#BB0000>";
		else
			htmlColor = "<font color=#00BB00>";
		
		textView2.setText(Html.fromHtml("Current Stock Value : " + thiscompany.getStockPrice() + " : " 
		+ htmlColor + thiscompany.getStockChange() + "</font>"));
		}else{
			textView2.setVisibility(View.GONE);
		}
		
		ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1);

		
		double sent = thiscompany.getSentiment();
		String sentiment;
		if(sent > 0){
			sentiment = "positive";
			imageView.setImageResource(drawable.arrow_up);
		}
		else{
			sentiment = "negative";
			imageView.setImageResource(drawable.arrow_down);
		}
		
		double sent2 = values.get(position).getSentiment();
		String sentiment2;
		if(sent2 > 0)
			sentiment2 = "positive";
		else
			sentiment2 = "negative";
		
		
		TextView textView3 = (TextView) rowView.findViewById(R.id.textView3);
		textView3.setText("Outlook based on this topic is " + sentiment2);
		
		TextView textView4 = (TextView) rowView.findViewById(R.id.textView4);
		textView4.setText("Overall outlook for this company is " + sentiment);
		
		
		
		Log.d("COMPANYLINKADAPTER","WORKING");

		return rowView;
	}
	
	

}
