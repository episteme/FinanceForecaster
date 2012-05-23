package team2.mainapp;

import java.util.ArrayList;

import team2.mainapp.ViewPagerAdapter.GetDataTask;

import android.R.drawable;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AnalogClock;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SingleTopic extends Activity {

	ImageButton hide;
	ImageButton star;
	int uid;
	String sectorName;
	boolean hidestate;
	boolean starstate;
	Topic thistopic;
	ArrayList<CompanyLink> companyLinks;
	ArrayList<String[]> titleLinks;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singletopic);

		uid = Integer.parseInt(getIntent().getStringExtra("EXTRA_UID"));
		sectorName = getIntent().getStringExtra("SECTOR");

		Log.d("uidsec",Integer.toString(uid));
		Log.d("uidsec",sectorName);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);


		GlobalState gState = (GlobalState) getApplication();
		for (Sector sector : gState.getAllSectors()) {
			if(!sector.getName().equals(sectorName))
				continue;
			for (Topic topic : sector.getTopicData()) {
				if (topic.getUid() == uid) {
					thistopic = topic;
					break;
				}
			}
		}

		if(thistopic == null){
			Toast toast = Toast.makeText(this, "This topic is old and no longer stored", 3000);
			toast.show();
			finish();
		}else{
			
			double sentiment = thistopic.getSentiment();
			Double green = sentiment * 7500;
			if(green < 0)
				green = 0d;
			Double red = sentiment * -750;
			if(red < 0)
				red = 0d;
			Log.d("Red",Double.toString(red));
			Log.d("Green",Double.toString(green));
			Integer igreen = (int) Math.round(green);
			Integer ired = (int) Math.round(red);
			if(ired > 125)
				ired = 140;
			if(igreen > 125)
				igreen = 125;
			

			TextView textView1 = (TextView) findViewById(R.id.part1text2);
			TextView textView2 = (TextView) findViewById(R.id.part1text3);
			TextView textView3 = (TextView) findViewById(R.id.part2text2);
			TextView textView4 = (TextView) findViewById(R.id.part2text3);
			TextView textView5 = (TextView) findViewById(R.id.part2text4);
			TextView textView10 = (TextView) findViewById(R.id.list1header);
			LinearLayout ll = (LinearLayout) findViewById(R.id.companyHeader);
			
			
			ll.setBackgroundColor((Color.rgb(ired, igreen, 0)));
			
			int mostRecent = 0;

			textView1.setText(thistopic.getArticles().get(mostRecent).getTitle());
			textView2.setText("Aggregated from " + thistopic.getArts() +
					" sources, last seen " + thistopic.getDate());
			textView3.setText(Html.fromHtml("<i>" + thistopic.getArticles().get(mostRecent).getSource() + "</i> - "
					+ thistopic.getArticles().get(mostRecent).getDescription()));
			textView4.setText(Html.fromHtml("<b>Keywords</b>: " + thistopic.printKeyWords()));
			textView5.setText(Integer.toString(thistopic.getArts()));

			Log.d("numberofarts",Integer.toString(thistopic.getArticles().size()));


			titleLinks = new ArrayList<String[]>();

			TableLayout listView2 = (TableLayout) findViewById(R.id.list2);

			for(Article article : thistopic.getArticles()){
				String[] title = new String[3];
				title[0] = article.getTitle();
				title[1] = article.getURL();
				title[2] = article.getSource();
				titleLinks.add(title);
			}
			
			int j = 0;
			for(Article article : thistopic.getArticles())
			{
				listView2.addView(getTitleRow(j,listView2));
				j++;
			}
			
			
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			listView2.setLayoutParams(lp);
			

			companyLinks = new ArrayList<CompanyLink>();

			TableLayout listView1 = (TableLayout) findViewById(R.id.list1);

			for(CompanyLink cl : thistopic.getCompanyLinks()){

				loop2: for(Sector sector : gState.getAllSectors())
				{
					for(Company company : sector.getCompData())
					{
						if(company.getName().equals(cl.getCompany())){
							Log.d("Found",cl.getCompany());
							companyLinks.add(cl);
							break loop2;
						}
					}
				}
			}
			
			listView1.addView(getRowHeader(listView1));
			
			int i = 0;
			for(CompanyLink cl : companyLinks)
			{
				listView1.addView(getRow(i,listView1));
				i++;
			}
						
			listView1.setLayoutParams(lp);

			if(companyLinks.size() == 0){
				listView1.setVisibility(View.GONE);
				textView10.setVisibility(View.GONE);
			}
		}
	}

	public void companyClickHandler(View view) {
		TextView tv = (TextView) view.findViewById(R.id.textView1);
		Intent myIntent = new Intent(this, SingleCompany.class);
		myIntent.putExtra("company", tv.getText());
		startActivity(myIntent);
	}


	public void titleClickHandler(View view) {
		TextView tv = (TextView) view.findViewById(R.id.textView2);
		Uri uriUrl = Uri.parse((String) tv.getText());
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);  
		startActivity(launchBrowser);
	}

	
	public void onPause(){
		super.onPause();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		
		case android.R.id.home:
			finish();
		default:
			break;
		}

		return true;
	}
	
	public View getRow(int position, TableLayout parent) {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.keywordlist, parent, false);

		GlobalState gState = (GlobalState)  ((Activity) this).getApplication();
		
		Company thiscompany = null;
		
		for(Sector sector : gState.getAllSectors())
		{
			for(Company company : sector.getCompData())
			{
				if(company.getName().equals(companyLinks.get(position).getCompany())){
					thiscompany = company;
					break;
				}
			}
		}
		
		TextView textView1 = (TextView) rowView.findViewById(R.id.textView1);
		textView1.setText(thiscompany.getName());
		TextView textView2 = (TextView) rowView.findViewById(R.id.textView2);
		TextView textView10 = (TextView) rowView.findViewById(R.id.textView10);
		
		if(thiscompany.isTraded()){
		
		String htmlColor;
		if(thiscompany.getStockChange() < 0)
			htmlColor = "<font color=#BB0000>";
		else
			htmlColor = "<font color=#00BB00>";
		
		textView2.setText(Double.toString(thiscompany.getStockPrice()));
		textView10.setText(Html.fromHtml(htmlColor + Math.abs(thiscompany.getStockChange()) + "</font>"));
		}else{
			textView2.setText("N/A");
			textView10.setVisibility(View.GONE);
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
		
		double sent2 = companyLinks.get(position).getSentiment();
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
	
	public View getRowHeader(TableLayout parent) {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.tableheader, parent, false);


		return rowView;
	}
	
	public View getTitleRow(int position, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.titlelist, parent, false);
		TextView textView1 = (TextView) rowView.findViewById(R.id.textView1);
		TextView textView2 = (TextView) rowView.findViewById(R.id.textView2);
		TextView textView3 = (TextView) rowView.findViewById(R.id.textView3);
		textView1.setText(titleLinks.get(position)[0]);
		textView2.setText(titleLinks.get(position)[1]);
		textView3.setText(titleLinks.get(position)[2]);
		

		return rowView;
	}

}


