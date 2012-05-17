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
		View rowView = inflater.inflate(R.layout.keywordlist, parent, false);

		return rowView;
	}
	
	

}
