package team2.mainapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeView extends LinearLayout {
    ArrayList<Topic> mListItems;
    ArrayList<Company> nListItems;
    ListView listView1;
    ListView listView2;
    TopicListAdapter adapter1;
    CompanyListAdapter adapter2;
    String category;

		
	public HomeView(Context context, String category) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.addView(inflater.inflate(R.layout.homeview, null));
        this.category = category.toLowerCase();
        
        mListItems = new ArrayList<Topic>();
        
		adapter1 = new TopicListAdapter(context, mListItems);
        
		listView1 = (ListView) findViewById(R.id.listView1);

		listView1.setAdapter(adapter1);
		
        nListItems = new ArrayList<Company>();
        
		adapter2 = new CompanyListAdapter(context, nListItems);

		listView2 = (ListView) findViewById(R.id.listView2);

		listView2.setAdapter(adapter2);
	}
	
	public void refresh(){
		mListItems.clear();
		nListItems.clear();
		
		GlobalState gState = (GlobalState) ((Activity) this.getContext()).getApplication();
				
		Log.d("rferesh","marcusislame");
		
		for(Sector sector : gState.getAllSectors()){
			java.util.Collections.sort(sector.getTopicData());
			if(!sector.getName().equals(category))
				continue;
			int i = 0;
			for(Topic topic : sector.getTopicData()){
				if(i == 3)
					break;
				Log.d("refreshing",topic.getTitle());
				mListItems.add(topic);
				i++;
			}
			i = 0;
			for(Company comp : sector.getCompData()){
				if(i == 3)
					break;
				Log.d("refreshing",comp.getName());
				nListItems.add(comp);
				i++;
			}
			adapter1.notifyDataSetChanged();
			adapter2.notifyDataSetChanged();
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

}
