package team2.mainapp;

import java.util.ArrayList;
import java.util.LinkedList;

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
	
}
