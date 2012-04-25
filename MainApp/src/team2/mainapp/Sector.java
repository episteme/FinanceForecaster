package team2.mainapp;

import java.util.Iterator;
import java.util.LinkedList;

public class Sector {
	String name;
	LinkedList<Topic> topicData;
	LinkedList<GoogleStory> googStories;
	
	Sector(String name)
	{
		this.name = name;
		googStories = new LinkedList<GoogleStory>();
		topicData = new LinkedList<Topic>();
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
	    	   topicData.remove(tempTopic);
	    	   topicData.add(topic);
	    	   repeat = true;
	    	   break;
	       }
	    }
	    if (!repeat)
	    	topicData.add(topic);
	    if(topic.getArtsLastHour() >= 3)
	    	return true;
	    else
	    	return false;
	}
	
	
}
