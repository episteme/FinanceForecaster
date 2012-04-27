import java.net.URL;
import java.util.Iterator;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class FeedReader implements Runnable {
	// Permanent store of 10 stories
	Story[] stories = new Story[10];
	// Specifies which sector to watch
	String sector;
	// Constructor
	FeedReader(String sector) {
		this.sector = sector;
	}
	// 'Main'
	public void run() {
		while(true) {
			try {
				// Initialise storage
				Integer index, subIndex, numNews, sHttp, n;
				String sTitle, sDesc, relURL, strNum, rawURL;
				SyndEntry synd;
				NewsAnalyst nAnal;
				Story[] newStories = new Story[10];
				// Initialise feed
				final SyndFeed feed = makeFeed(sector);
				// n keeps track of which story we are looking at
				n = 1;
				for (final Iterator<?> iter = feed.getEntries().iterator();
						iter.hasNext();)
				{
					synd = (SyndEntry) iter.next();
					sTitle = (String) synd.getTitle();         
					sDesc = (String) synd.getDescription().getValue();
					// Print the number of related articles
					index = sDesc.lastIndexOf("<b>all ") + 7;
					// Grabs the RSS object
					// If number of other articles is stated
					if (index != 6) {
						// Grabs the number of articles
						subIndex = sDesc.substring(index).indexOf(" ") + index;
						strNum = sDesc.substring(index, subIndex).replaceAll(",", "");
						numNews = Integer.parseInt(strNum);
						// Grab related articles RSS feed
						sHttp = sDesc.substring(0, index).lastIndexOf("http://");
						rawURL = sDesc.substring(sHttp, index - 15);
						relURL = rawURL.replaceAll("&amp;", "&") + "&output=rss";

					}
					// If number of other articles is not stated, skip it
					else {
						System.out.println("Story skipped");
						newStories[n-1] = null;
						n++;
						continue;
					}

					// relURL is now RSS URL for this story
					byte storyid = -1;
					// Compare to old stories
					// Iterates through the ten old stories, checking whether they match
					for (byte i = 0; i < 10; i++) {
						if (stories[i] == null)
							continue;
						if(stories[i].getLink().compareTo(relURL) == 0 ||
								stories[i].getTitle().compareTo(sTitle) == 0) {
							storyid = i;
							// if there are some new stories, it will reanalyse the story
							if (numNews > (4 + stories[i].getSites())) {
								System.out.println("----- UPDATED -----");
								nAnal = new NewsAnalyst(stories[i]);
								// updates the stories
								newStories[n-1] = nAnal.updateStory();
								newStories[n-1].setSites(numNews);
								newStories[n-1].setRank(n,numNews);
							} else {
							// if not, the story is simply copied
								newStories[n-1] = stories[i];
							}
							// updates title and link
							newStories[n-1].setTitle(sTitle);
							newStories[n-1].setLink(relURL);
							break;
						}
					}

					// If story is new
					if (storyid == -1){
						// Analyses story
						System.out.println("------- NEW -------");
						newStories[n-1] = new Story(numNews, n, sTitle, relURL);
						nAnal = new NewsAnalyst(newStories[n-1]);
						newStories[n-1] = nAnal.getStory();
					}
					// prints story details
					printStory(n, newStories);
					n++;
					// Wait to avoid Google throttle
					Thread.sleep( (5000) ); 
				}
				stories = newStories;
				// Wait before repeating
				Thread.sleep( (60000) ); 
			}
			catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("ERROR: " + ex.getMessage());
			}
			// Spacer
			System.out.println("---------------------------------------------------------");
		}
	}

	public Story[] getStories() {
		return stories;
	}

	// Returns a SyndFeed object for the specified sector
	private SyndFeed makeFeed(String sector) {
		URL feedURL;
		try {
			feedURL = new URL(makeSectorURL(sector));
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedURL));
			return feed;
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: " + e.getMessage());
			System.out.println("Error: Using default feed: Oil");
			//feedURL = new URL(makeSectorURL("Oil"));
			return makeFeed("Oil");
		}
	}

	// Returns a google RSS URL for the specified sector
	private String makeSectorURL(String sector) {
		return "http://news.google.com/news?pz=1&cf=all&hl=en&q="
				+ sector + "&output=rss";
	}

	// Print info for the nth story
	private void printStory(Integer n, Story[] stories) {
//		System.out.println("-------------- " + n + " --------------");
//		System.out.println("Title: " + stories[n-1].getTitle());
//		System.out.println("Date: " + stories[n-1].getDate());
//		System.out.println("Sites: " + stories[n-1].getSites());
//		System.out.println("Rank: " + stories[n-1].getRank());
//		System.out.println("URL: " + stories[n-1].getLink());
//		System.out.println("Sentiment: " + stories[n-1].getSentiment());
//		System.out.println("Keywords: " + stories[n-1].printKeyWords());
	}
	public String getSector() {
		return sector;
	}

}