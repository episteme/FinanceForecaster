package team2.mainapp;

import java.util.LinkedList;

import android.app.Application;

public class GlobalState extends Application {
	
	static LinkedList<Sector> allSectors;

	public LinkedList<Sector> getAllSectors() {
		return allSectors;
	}

	public void setSectors(LinkedList<Sector> allTopics) {
		GlobalState.allSectors = allTopics;
	}
	
	

}
