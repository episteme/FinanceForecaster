package team2.mainapp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Sector {
	String name;
	LinkedList<Topic> topicData;
	LinkedList<GoogleStory> googStories;
	LinkedList<Company> compData;
	int threshold;
	
	Sector(String name)
	{
		this.name = name;
		googStories = new LinkedList<GoogleStory>();
		topicData = new LinkedList<Topic>();
		compData = new LinkedList<Company>();
		threshold = 100;
	}

	public LinkedList<GoogleStory> getGoogStories() {
		return googStories;
	}

	public void setGoogStories(LinkedList<GoogleStory> googStories) {
		this.googStories = googStories;
	}

	public String getName() {
		return name;
	}

	public LinkedList<Topic> getTopicData() {
		return topicData;
	}

	// Returns true if >= articles added in past hour.
	public boolean addTopic(Topic topic) {
		Iterator<Topic> iterator = topicData.iterator();
	    boolean repeat = false;
	    while (iterator.hasNext()) {
	    	Topic tempTopic = iterator.next();
	       if (topic.getUid() == tempTopic.getUid()) {
	    	   int tempState = tempTopic.getState();
	    	   topicData.remove(tempTopic);
	    	   topicData.add(topic);
	    	   topic.setState(tempState);
	    	   repeat = true;
	    	   break;
	       }
	    }
	    if (!repeat)
	    	topicData.add(topic);
	    if(topic.getArtsLastHour() >= (105-threshold) && threshold != 0)
	    	return true;
	    else
	    	return false;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int progress) {
		threshold = progress;
	}

	public void updateTopic(int parseInt, int parseInt2) {
		Iterator<Topic> iterator = topicData.iterator();
	    while (iterator.hasNext()) {
	    	Topic tempTopic = iterator.next();
	       if (tempTopic.getUid() == parseInt) {
	    	   tempTopic.setArtsLastHour(parseInt2);
	       }
	    }		
	}

	public LinkedList<Company> getCompData() {
		return compData;
	}

	public void setCompanies(LinkedList<Company> tempComps) {
		compData = tempComps;
	}
	
	public void setTopicData(LinkedList<Topic> tempTopics) {
		topicData = tempTopics;
	}
	
	
}
