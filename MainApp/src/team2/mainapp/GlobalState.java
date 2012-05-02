package team2.mainapp;

import java.util.LinkedList;

import android.app.Application;

public class GlobalState extends Application {
	
	static LinkedList<Sector> allSectors;
	static boolean ready;
	static boolean on;
	int frequency;

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public LinkedList<Sector> getAllSectors() {
		return allSectors;
	}

	public void setSectors(LinkedList<Sector> allTopics) {
		GlobalState.allSectors = allTopics;
	}
	
	public void setReady(boolean r){
		ready = r;
	}
	
	public boolean getReady(){
		return ready;
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean o) {
		on = o;
	}
}
