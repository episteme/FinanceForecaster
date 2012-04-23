package team2.mainapp;

import java.util.Iterator;
import java.util.LinkedList;

public class Sector {
	String name;
	LinkedList<Topic> topicData;
	
	Sector(String name)
	{
		this.name = name;
		topicData = new LinkedList<Topic>();
	}

	public String getName() {
		return name;
	}

	public LinkedList<Topic> getTopicData() {
		return topicData;
	}

	public void addTopic(Topic topic) {
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
	}
	
	
}
