import java.net.URL;
import java.util.Iterator;

import com.sun.syndication.feed.module.Module;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * Reads and prints any RSS/Atom feed type. Adopted from the example by the
 * same name at http://wiki.java.net/bin/view/Javawsxml/Rome05TutorialFeedReader
 *
 */
public class FeedReader {

    public static void main(final String[] args) {
        boolean readOk = false;
            try {
                final URL feedUrl = new URL("http://news.google.com/news?pz=1&cf=all&ned=uk&hl=en&q=Oil&output=rss");

                final SyndFeedInput input = new SyndFeedInput();
                final SyndFeed feed = input.build(new XmlReader(feedUrl));
//                System.out.println("Title: " + feed.getTitle());
//                System.out.println("Author: " + feed.getAuthor());
//                System.out.println("Description: " + feed.getDescription());
//                System.out.println("Pub date: " + feed.getPublishedDate());
//                System.out.println("Copyright: " + feed.getCopyright());
//                System.out.println("Modules used:");
                for (final Iterator iter = feed.getModules().iterator();
                     iter.hasNext();)
                {
                    ((Module)iter.next()).getUri();
                }
                System.out.println("Titles of the " + feed.getEntries().size() +
                                   " entries:");
                for (final Iterator iter = feed.getEntries().iterator();
                     iter.hasNext();)
                {
          
                    	SyndEntry trial = (SyndEntry) iter.next();
                    	

                    	String title = (String) trial.getTitle();
                        
                        String description = (String) trial.getDescription().getValue();
                        System.out.println(title + "\n" + description + "\n");
                }
                if (feed.getImage() != null)
                {
                    System.out.println("Feed image URL: " +
                                       feed.getImage().getUrl());
                }

                readOk = true;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("ERROR: " + ex.getMessage());
            }

        if (! readOk) {
            System.out.println();
            System.out.println("FeedReader reads and prints info on any RSS/Atom feed.");
            System.out.println("The first parameter must be the URL of the feed to read.");
            System.out.println();
        }
    }
}