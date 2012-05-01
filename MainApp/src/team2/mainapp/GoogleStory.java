package team2.mainapp;

import java.util.ArrayList;
import java.util.LinkedList;

import android.graphics.Color;
import android.util.Log;

public class GoogleStory {
	
	private double sentiment;
	private String title;
	private String link;
	private ArrayList<KeyWord> keyWords;
	private String timestamp;
	
	public GoogleStory(double sentiment, String title, String link,
			ArrayList<KeyWord> keyWords, String timestamp) {
		this.sentiment = sentiment;
		this.title = title;
		this.link = link;
		this.keyWords = keyWords;
		this.timestamp = timestamp;
	}

	public double getSentiment() {
		return sentiment;
	}

	public void setSentiment(double sentiment) {
		this.sentiment = sentiment;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public ArrayList<KeyWord> getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(ArrayList<KeyWord> keyWords) {
		this.keyWords = keyWords;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String printKeyWords() {
		if (keyWords.size() == 0) {
			return "";
		}

		double sentiment = keyWords.get(0).getSentiment();
		
		Double green = ((sentiment + 1) / 2.0) * 255;
		Double red = 255.0 - green;
		Integer igreen = (int) Math.round(green);
		Integer ired = (int) Math.round(red);
		
		
		String result = "<font color=#" + Integer.toHexString(Color.rgb(ired, igreen, 0)).toString().substring(2) + ">" + keyWords.get(0).getWord() + "</font>";
		for (int i = 1; i < keyWords.size(); i++) {
			sentiment = keyWords.get(i).getSentiment();
			
			green = ((sentiment + 1) / 2.0) * 255;
			red = 255.0 - green;
			igreen = (int) Math.round(green);
			ired = (int) Math.round(red);
			result += ", " + "<font color=#" + Integer.toHexString(Color.rgb(ired, igreen, 0)).toString().substring(2) + ">" + keyWords.get(i).getWord() + "</font>";
		}
		Log.d("herro",result);
		return result;
	}
	
}
