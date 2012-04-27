package team2.mainapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;

public class SingleTopic extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singletopic);
		
		int uid = Integer.parseInt(getIntent().getStringExtra("EXTRA_UID"));
		
		Topic thistopic = null;
		
		GlobalState gState = (GlobalState) getApplication();
		for (Sector sector : gState.getAllSectors()) {
			for (Topic topic : sector.getTopicData()) {
				if (topic.getUid() == uid) {
					thistopic = topic;
					break;
				}
			}
		}
		
		ArrayList<KeyWord> mListItems = new ArrayList<KeyWord>();

  		KeywordAdapter adapter = new KeywordAdapter(this,
  				 mListItems);

		setListAdapter(adapter);
		
		mListItems.addAll(thistopic.getKeyWords());
		
	}
}
