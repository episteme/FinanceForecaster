import java.net.URL;
import java.util.Iterator;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


public class NewsAnalyst {
	
	private String rURL;
	private Integer sent;
	private String keyWords;
	
	// Constructor
	NewsAnalyst(String URL) {
		rURL = URL;
		sent = 0;
		keyWords = "";
	}
	
	private void grabTitles() {
		try {
	        final URL feedUrl = new URL(rURL);
	        final SyndFeedInput input = new SyndFeedInput();
	        final SyndFeed feed = input.build(new XmlReader(feedUrl));
	        
	        for (final Iterator iter = feed.getEntries().iterator();
	                iter.hasNext();) {
	        	keyWords = keyWords + " " + ((String) ((SyndEntry) iter.next()).getTitle()); 
	        }
			
	        
		}
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR: " + ex.getMessage());
        }
	}
	
	public String getkeyWords() {
		grabTitles();
		return keyWords;
	}
	
}
