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
	private ArrayList<KeyWord> keyWords;
	private String date;
	private int uid;
	private int artsLastHour;
	int state;
	private double sentiment;
	private int arts;
	private ArrayList<CompanyLink> companyLinks;
	private ArrayList<Article> articles;
	private String sector;
	
	Topic (String title, String date,  int artsLH, ArrayList<Article> arts, ArrayList<KeyWord> keyWords, String uid, double sentiment2, String rawData, ArrayList<CompanyLink> companyLinks, String sector) {
		this.uid = (int) Integer.parseInt(uid);
		this.keyWords = keyWords;
		this.title = title.replace("?","");
		this.date = date;
		this.artsLastHour = artsLH;
		this.sentiment = sentiment2;
		state = 0;
		this.arts = Integer.parseInt(rawData);
		this.companyLinks = companyLinks;
		this.articles = arts;
		this.sector = sector;
	}
	
	public double getSentiment() {
		return sentiment;
	}

	public int compareTo(Topic temp) {
//		if(temp.getState() > this.getState())
//			return 1;
//		else if(temp.getState() < this.getState())
//			return -1;
//		else
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
		dateFormat = new SimpleDateFormat("HH:mm");
		String result = dateFormat.format(temp);
		dateFormat = new SimpleDateFormat("EEEE");
		result += " on " + dateFormat.format(temp);

		return result;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ArrayList<Article> getArticles() {
		return articles;
	}

	public ArrayList<KeyWord> getKeyWords() {
		return keyWords;
	}
	
	public ArrayList<String[]> getArticle() {
		ArrayList<String[]> arts = new ArrayList<String[]>();
		for(int i = 0; i < articles.size(); i++)
		{
			if(articles.get(i) != null){
				String[] temp = new String[4];
				temp[0] = articles.get(i).getTitle();
				temp[1] = articles.get(i).getURL();
				temp[2] = articles.get(i).getSource();
				temp[3] = articles.get(i).getDescription();
				arts.add(temp);
			}
		}
		return arts;
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

	public String getSector() {
		return sector;
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

	public ArrayList<CompanyLink> getCompanyLinks() {
		return companyLinks;
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