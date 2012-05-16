package team2.mainapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import android.graphics.Color;
import android.util.Log;

public class Topic implements Comparable<Topic> {
	private String title;
	private ArrayList<String> URLS;
	private ArrayList<KeyWord> keyWords;
	private String date;
	private int uid;
	private int artsLastHour;
	private ArrayList<String> titles;
	int state;
	private double sentiment;
	private int arts;
	
	Topic (String title, String date,  int artsLH, ArrayList<String> URLS, ArrayList<KeyWord> keyWords, String uid, ArrayList<String> titles, double sentiment2, String rawData) {
		this.uid = (int) Integer.parseInt(uid);
		this.keyWords = keyWords;
		this.title = title.replace("?","");
		this.URLS = URLS;
		this.date = date;
		this.artsLastHour = artsLH;
		this.titles = titles;
		this.sentiment = sentiment2;
		state = 0;
		this.arts = Integer.parseInt(rawData);
	}
	
	public double getSentiment() {
		return sentiment;
	}

	public int compareTo(Topic temp) {
		if(temp.getState() > this.getState())
			return 1;
		else if(temp.getState() < this.getState())
			return -1;
		else
			return temp.artsLastHour - this.artsLastHour;
	}

	public int getArtsLastHour() {
		return artsLastHour;
	}

	public void setArtsLastHour(int artsLastHour) {
		this.artsLastHour = artsLastHour;
	}

	public int getUid() {
		return uid;
	}

	public String getTitle() {
		return title;
	}

	public String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/d HH:mm:ss");
		Date temp;
		try {
			temp = dateFormat.parse(date);
		} catch (ParseException e) {
			return "some time in the past";
		}
		dateFormat = new SimpleDateFormat("hh:mma");
		String result = dateFormat.format(temp);
		dateFormat = new SimpleDateFormat("EEEE");
		result += " on " + dateFormat.format(temp);

		return result;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ArrayList<String> getTitles() {
		return titles;
	}

	public ArrayList<String> getURLS() {
		return URLS;
	}

	public ArrayList<KeyWord> getKeyWords() {
		return keyWords;
	}
	
	public ArrayList<String[]> getTitleUrl() {
		ArrayList<String[]> titleUrls = new ArrayList<String[]>();
		for(int i = 0; i < URLS.size(); i++)
		{
			if(titles.get(i) != null && URLS.get(i) != null){
				String[] temp = new String[2];
				temp[0] = titles.get(i);
				temp[1] = URLS.get(i);
				titleUrls.add(temp);
			}
		}
		return titleUrls;
	}
	
	public String getWords() {
		if (keyWords.size() == 0) {
			return "";
		}
		
		int num;
		
		if(keyWords.size() < 5)
			num = keyWords.size();
		else
			num = 5;

		String result = keyWords.get(0).getWord();
		for (int i = 1; i < num; i++) {
			result += ", " + keyWords.get(i).getWord();
		}
		Log.d("herro",result);
		return result;
	}

	public void setState(int i) {
		state = i;
	}
	
	public int getState() {
		return state;
	}

	public void setArts(int i) {
		this.arts = i;
	}

	public int getArts() {
		return arts;	
	}
}

class CompanyLink {
	String name;
	Double sentiment;
	Double relevance;
	int num;

	CompanyLink (String n, String s, String r)
	{
		name = n;
		sentiment = Double.parseDouble(s);
		relevance = Double.parseDouble(r);
		num = 1;
	}

	public String getCompany() {
		return name;
	}

	public Double getSentiment() {
		return sentiment;
	}

	public Double getRelevance() {
		return relevance;
	}
}