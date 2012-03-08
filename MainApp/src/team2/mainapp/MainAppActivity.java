package team2.mainapp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

import team2.mainapp.PullToRefreshListView.OnRefreshListener;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainAppActivity extends ListActivity {
	static LinkedList<LinkedList<Topic>> allTopics; 
	static String s;
	private LinkedList<String> mListItems;
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Log.d("debug","Hello");

		mListItems = new LinkedList<String>();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.rowlayout, R.id.labelo, mListItems);

		setListAdapter(adapter);

		// Set a listener to be invoked when the list should be refreshed.
		((PullToRefreshListView) getListView()).setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Do work to refresh the list here.
				GetDataTask task = new GetDataTask();
				task.execute();
			}
		});

		// Start the process of polling the server
		startProgress();
		// Do an initial refresh straight away without user input
		GetDataTask task = new GetDataTask();
		task.execute();
	}

	private class GetDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			// Do nothing
			return null;
		}

		@Override
		protected void onPostExecute(Void x) {
			// Clear current list
			mListItems.clear();
			// Wait for data to exist
			while(allTopics == null){			}

			// Go through the allTopics datastructure, pasting title & date
			for(LinkedList<Topic> topicsector : allTopics){
				for(Topic topic : topicsector){
					String allInfo = topic.getTitle() + "\n@ " + topic.getDate();
					mListItems.add(allInfo);
				}
			}
			// Complete the refresh
			((PullToRefreshListView) getListView()).onRefreshComplete();
		}
	}

	public void startProgress() {
		// Do something long
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// Initialise by always-in-past date
				String date = "1212/12/12 12:12:12";
				for (int i = 0; i >= 0; i++) {
					try {
						if(i > 0)
							Thread.sleep(60000);
						// Get new information from remote server
						s = TCPClient.go(date);
						
						// Parse retrieved information
						parseInput(s);
						
						// Reset date to current
						Date dateType = new Date();
						
						// Turns date into string
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/d HH:mm:ss");
						date = dateFormat.format(dateType);
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		};
		new Thread(runnable).start();
	}

	protected void parseInput(String s2) {
		try{
		// Create a data structure to hold all the parsed information
		LinkedList<LinkedList<Topic>> parseInfo = new LinkedList<LinkedList<Topic>>(); 

		// Read in flag which splits between feedReader and Parse infos
		String[] type = s2.split("SPLITINFO\n");

		// Read in flag which splits between each sector in the Parse information
		String[] topicsectors = type[1].split("TOPSTOP\n");
		for (String sector : topicsectors)
		{
			// Create data structure to hold information for each topic
			LinkedList<Topic> sectorInfo = new LinkedList<Topic>();

			// Read in flag which splits between each topic in the sector
			String[] topics = sector.split("SPECTOPS\n");
			for (String topic : topics)
			{
				// Splits the topic data into parts
				String[] rawData = topic.split(";;\n");

				// Splits the URLS and keyWords into individual parts
				String[] links = rawData[2].split(";\n");
				String[] words = rawData[3].split(";\n");

				// Creates an arraylist to hold the URLs
				ArrayList<String> URLS = new ArrayList<String>();
				for (String link : links)
				{
					// Adds each link to the list
					URLS.add(link);
				}

				// Creates an arraylist to hold the keyWords
				ArrayList<KeyWord> KeyWords = new ArrayList<KeyWord>();
				for (String word : words)
				{
					// Split each keyword into its word and its sentiment
					String[] bits = word.split("@");
					// Put each bit into the list
					KeyWords.add(new KeyWord(bits[0], bits[1]));
				}

				// Add the topic info to the sector info
				sectorInfo.add(new Topic(rawData[0],rawData[1],URLS,KeyWords));
			}
			// Add the sectorInfo to the parseInfo
			parseInfo.add(sectorInfo);
		}
		// Replaces allTopics with parseInf
		allTopics = parseInfo;
		}
		catch (Exception e){
			Log.d("Debug","Error parsing new data");
		}
	}
}
