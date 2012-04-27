package team2.mainapp;

import java.util.ArrayList;
import java.util.Date;

public class Topic implements Comparable<Topic> {
	private String title;
	private ArrayList<String> URLS;
	private ArrayList<KeyWord> keyWords;
	private String date;
	private int uid;
	private int artsLastHour;
	private ArrayList<String> titles;
	
	Topic (String title, String date,  int artsLH, ArrayList<String> URLS, ArrayList<KeyWord> keyWords, String uid, ArrayList<String> titles) {
		this.uid = (int) Integer.parseInt(uid);
		this.keyWords = keyWords;
		this.title = title;
		this.URLS = URLS;
		this.date = date;
		this.artsLastHour = artsLH;
		this.titles = titles;
	}
	
	public int compareTo(Topic temp) {
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
		return date;
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
	
	public String getWords() {
		if (keyWords.size() == 0) {
			return "";
		}
		String result = keyWords.get(0).getWord();
		for (int i = 1; i < keyWords.size(); i++) {
			result += ", " + keyWords.get(i).getWord();
		}
		return result;
	}

}