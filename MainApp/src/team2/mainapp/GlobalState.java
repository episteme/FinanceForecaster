package team2.mainapp;

import java.util.LinkedList;

import android.app.Application;

public class GlobalState extends Application {
	
	static LinkedList<Sector> allTopics;

	public LinkedList<Sector> getAllTopics() {
		return allTopics;
	}

	public void setAllTopics(LinkedList<Sector> allTopics) {
		GlobalState.allTopics = allTopics;
	}
	
	

}
