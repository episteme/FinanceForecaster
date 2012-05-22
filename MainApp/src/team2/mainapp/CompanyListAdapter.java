package team2.mainapp;

import java.util.ArrayList;

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
		TextView textView = (TextView) rowView.findViewById(R.id.name);
		TextView textView2 = (TextView) rowView.findViewById(R.id.mentions);
		TextView textView3 = (TextView) rowView.findViewById(R.id.stockprice);
		TextView textView4 = (TextView) rowView.findViewById(R.id.stockchange);
		TextView textView5 = (TextView) rowView.findViewById(R.id.sector);
		
		
		Log.d("Debug", Integer.toString(values.size()));
		Log.d("Debug", values.get(position).getName());
		textView.setText(values.get(position).getName());
		String s = "Mentioned in " + values.get(position).getArticles() + " article";
		if((values.get(position).getArticles() != 1))
			s += "s";
		textView2.setText(s);
		textView3.setText(Double.toString(values.get(position).getStockPrice()));
		String htmlColor;
		if(values.get(position).getStockChange() < 0)
			htmlColor = "<font color=#BB0000>";
		else
			htmlColor = "<font color=#00BB00>";
		
		textView4.setText(Html.fromHtml(htmlColor + values.get(position).getStockChange() + "</font>" + " " + values.get(position).getRelevance()));
		textView5.setText(values.get(position).getSector());
		
		if(!values.get(position).isTraded()){
			textView3.setVisibility(View.GONE);
			textView4.setText("This company is not publically traded");
		}
		return rowView;
	}
	
	

}
