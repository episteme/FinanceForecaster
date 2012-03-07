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

		startProgress();
		GetDataTask task = new GetDataTask();
		task.execute();
	}

	private class GetDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}

		@Override
		protected void onPostExecute(Void x) {
			mListItems.clear();
			if(allTopics != null){
			for(LinkedList<Topic> topicsector : allTopics){
				for(Topic topic : topicsector){
					String allInfo = topic.getTitle() + "\n@ " + topic.getDate();
					mListItems.add(allInfo);
				}
			}
			((PullToRefreshListView) getListView()).onRefreshComplete();
		}
		}
	}

	public void startProgress() {
		// Do something long
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i <= 1000; i++) {
					final int value = i;
					String date = "1212/12/12 12:12:12";
					try {
						if(i > 1)
							Thread.sleep(30000);
						Log.d("Debug", "Thread1");
						s = TCPClient.go(date);
						Date dateType = new Date();
						parseInput(s);
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
		allTopics = new LinkedList<LinkedList<Topic>>(); 

		String[] type = s2.split("SPLITINFO\n");
		String[] topicsectors = type[1].split("TOPSTOP\n");
		for (String sector : topicsectors)
		{
			LinkedList<Topic> topicSector = new LinkedList<Topic>();

			String[] topics = sector.split("SPECTOPS\n");
			for (String topic : topics)
			{
				String[] rawData = topic.split(";;\n");
				String[] links = rawData[2].split(";\n");
				String[] words = rawData[3].split(";\n");

				ArrayList<String> URLS = new ArrayList<String>();
				for (String link : links)
				{
					String[] bits = link.split("@");
					for(String bit : bits)
						URLS.add(bit);
				}

				ArrayList<KeyWord> KeyWords = new ArrayList<KeyWord>();
				for (String word : words)
				{
					String[] bits = word.split("@");
					for (int i = 0; i < bits.length; i += 2)
						KeyWords.add(new KeyWord(bits[i], bits[i+1]));
				}

				topicSector.add(new Topic(rawData[0],rawData[1],URLS,KeyWords));
			}

			allTopics.add(topicSector);
		}

	}
}
