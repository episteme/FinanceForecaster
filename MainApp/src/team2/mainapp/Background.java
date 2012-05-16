package team2.mainapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

public class Background extends Service {

	private final IBinder mBinder = new Binder();
	private String sectors;

	@Override
	public void onCreate() {
		Log.d("Starting","Service");
		GlobalState gState = (GlobalState) getApplication();

		sectors = "";
		for(Sector sector : gState.getAllSectors())
		{
			sectors += sector.getName() + ";";
		}

		startProgress();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;	
	}	

	public void createNotification(String title, ArrayList<KeyWord> keyWords, String uid, String sector) {
		Log.d("Debug", "Creating Notification");
		NotificationManager notificationManager = (NotificationManager) 
				getSystemService(NOTIFICATION_SERVICE);
		// Construction
		Notification.Builder not = new Notification.Builder(this);
		not.setSmallIcon(R.drawable.psyduck2);
		not.setContentTitle(title);
		String result = keyWords.get(0).getWord();
		for (int i = 1; i < keyWords.size(); i++) {
			result += ", " + keyWords.get(i).getWord();
		}
		not.setContentText(result);
		Intent intent = new Intent(this, SingleTopic.class);
		intent.putExtra("EXTRA_UID",uid);
		intent.putExtra("SECTOR", sector);
		PendingIntent activity = PendingIntent.getActivity(this, Integer.parseInt(uid), intent, PendingIntent.FLAG_UPDATE_CURRENT);
		not.setContentIntent(activity);
		// Hide the notification after its selected
		not.setAutoCancel(true);
		Notification notification = not.getNotification();
		Log.d("Debug", "Sending Notification");
		notificationManager.notify(Integer.parseInt(uid), notification);
	}


	public void startProgress() {
		// Do something long
		Runnable runnable = new Runnable() {
			private String s;

			@Override
			public void run() {
				Looper.prepare();
				// Initialise by always-in-past date
				String date = "1212/12/12 12:12:12";
				String query = sectors + ";" + date;
				for (int i = 0; i >= 0; i++) {
					try {

						GlobalState gState = (GlobalState) getApplication();
						if(gState.isOn()){
							// Get new information from remote server
							Date dateType = new Date();
							Log.d("Debug1", query);
							s = TCPClient.go(query);

							// Parse retrieved information
							parseInput(s);

							// Reset date to current
//							Log.d("Debug2", s);
							// Turns date into string
							DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/d HH:mm:ss");
							query = sectors + ";" + dateFormat.format(dateType);

						}

						int waited = 0;
						while(waited <= gState.getFrequency()){
							waited++;
							Thread.sleep(60000);
						}


					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		new Thread(runnable).start();
	}

	protected void parseInput(String s2) {
		try {
			GlobalState gState = (GlobalState) getApplication();
			// Read in flag which splits between feedReader and Parse infos
			String[] type = s2.split("SPLITINFO\n");

			// Read in flag which splits between each sector in the Parse information
			String[] topicsectors = type[1].split("TOPSTOP\n");
//			Log.d("topicdata",type[1]);
//			Log.d("datatypes",Integer.toString(type.length));
			int i = 0;
			for (String sector : topicsectors)
			{
				// Read in flag which splits between each topic in the sector
				String[] topics = sector.split("SPECTOPS\n");
				for (String topic : topics)
				{
					// Splits the topic data into parts
					String[] rawData = topic.split(";;\n");

//					Log.d("datalength",Integer.toString(rawData.length));
					
					if(rawData.length == 2)
					{
						gState.getAllSectors().get(i).updateTopic(Integer.parseInt(rawData[0]), Integer.parseInt(rawData[1]));
						continue;
					}
					else if (rawData.length < 9)
						continue;

					// Splits the URLS and keyWords into individual parts
					String[] links = rawData[4].split(";\n");
					String[] titles = rawData[5].split(";\n");
					String[] words = rawData[6].split(";\n");

					// Creates an arraylist to hold the URLs
					ArrayList<String> URLS = new ArrayList<String>();
					for (String link : links)
					{
						// Adds each link to the list
						URLS.add(link);
					}

					ArrayList<String> Titles = new ArrayList<String>();
					for (String title : titles)
					{
						// Adds each link to the list
						Titles.add(title);
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

					ArrayList<CompanyLink> companyLinks = new ArrayList<CompanyLink>();
					if(rawData.length == 10){
						String[] companies = rawData[9].split(";\n");
						
						for(String comp : companies)
						{
							String[] bits = comp.split("@");
							companyLinks.add(new CompanyLink(bits[0],bits[1],bits[2]));
						}
					}

					// Add the topic info to the sector info
					boolean alert = gState.getAllSectors().get(i).addTopic(
							new Topic(rawData[1], rawData[2], Integer.parseInt(rawData[3]), URLS, KeyWords, rawData[0], Titles, Double.parseDouble(rawData[7]),rawData[8],companyLinks));
					if (alert) {
						createNotification(rawData[1],KeyWords,rawData[0],gState.getAllSectors().get(i).getName()); 
					}
				}
				// Add the sectorInfo to the parseInfo
				i++;
			}
			
//			Log.d("compdata",type[2]);
			// Parse Company Data
			// Split sectors
			String[] compsectors = type[2].split("COMPSTOP\n");
			int k = 0;
			for (String sector : compsectors)
			{
				String[] companies = sector.split("SPECCOMP\n");
				LinkedList<Company> tempComps = new LinkedList<Company>();
				for (String company : companies)
				{
					// Split story in to fields
					String[] rawData = company.split(";;\n");
					Log.d("newcompany",rawData[0]);
					tempComps.add(
							new Company(rawData[0], rawData[1], rawData[2], rawData[3], rawData[4], rawData[5], rawData[6]));
				}
				gState.getAllSectors().get(k).setCompanies(tempComps);
				k++;
			}

//			Log.d("newsdata",type[0]);
			// Parse Google News
			// Split sectors
			String[] newssectors = type[0].split("NEWSTOP\n");
			int j = 0;
			for (String sector : newssectors)
			{
				String[] stories = sector.split("SPECNEWS\n");
				LinkedList<GoogleStory> tempStories = new LinkedList<GoogleStory>();
				for (String story : stories)
				{
					// Split story in to fields
					String[] rawData = story.split(";;\n");

					// Splits the keyWords into individual parts
					String[] words = rawData[4].split(";\n");

					ArrayList<KeyWord> keyWords = new ArrayList<KeyWord>();
					for (String word : words)
					{
						// Split each keyword into its word and its sentiment
						String[] bits = word.split("@");
						// Put each bit into the list
						keyWords.add(new KeyWord(bits[0], bits[1]));
					}
					tempStories.add(new GoogleStory(
							Double.parseDouble(rawData[3]), rawData[0], rawData[1], keyWords, rawData[2]));

				}
				gState.getAllSectors().get(j).setGoogStories(tempStories);
				j++;
			}

			gState.setReady(true);
			gState.setUpdated(new Date());

			// Replaces allTopics with parseInf
			Log.d("debug","New info received");
		}
		catch (Exception e){
			e.printStackTrace();
			Log.d("parseerror",e.toString());
		}
	}

}
