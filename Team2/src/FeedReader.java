import java.net.URL;
import java.util.Iterator;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class FeedReader {

    public static void main(final String[] args) {
            try {
            	
            	//  Initialize feed
                final URL feedUrl = new URL("http://news.google.com/news?pz=1&cf=all&ned=uk&hl=en&q=Oil&output=rss");
                final SyndFeedInput input = new SyndFeedInput();
                final SyndFeed feed = input.build(new XmlReader(feedUrl));
                
                // Initialize storage
                Integer index, subIndex, numNews, sHttp;
                String sTitle, sDesc, relURL;
                SyndEntry synd;
                NewsAnalyst nAnal;
                
                for (final Iterator iter = feed.getEntries().iterator();
                     iter.hasNext();)
                {
                		// Grabs the RSS object
                    	synd = (SyndEntry) iter.next();
                    	sTitle = (String) synd.getTitle();         
                        sDesc = (String) synd.getDescription().getValue();
                        
                        // Display Title + Description
                        System.out.println(sTitle);
                       
                        // Print the number of related articles
                        index = sDesc.lastIndexOf("<b>all ") + 7;
                        if (index != 6) {           
                        	// Grabs the number of articles
	                        subIndex = sDesc.substring(index).indexOf(" ") + index;
	                        numNews = Integer.parseInt(sDesc.substring(index, subIndex).replaceAll(",", ""));
	                        System.out.println(numNews);
	                        
                        	// Grab related articles RSS feed
	                        sHttp = sDesc.substring(0, index).lastIndexOf("http://");
	                        relURL = sDesc.substring(sHttp, index - 15).replaceAll("&amp;", "&") + "&output=rss";
                        	System.out.println(relURL);
                        	
                        	// Prints all top 10 related titles
	                        nAnal = new NewsAnalyst(relURL);
	                        System.out.println(nAnal.getkeyWords());
	                        
                        }
                        else {
                        	System.out.println("1");
                        }
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("ERROR: " + ex.getMessage());
            }
    }
}