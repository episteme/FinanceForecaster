import java.net.URL;
import java.util.Iterator;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import com.alchemyapi.api.AlchemyAPI;
import com.alchemyapi.api.AlchemyAPI_KeywordParams;

import org.w3c.dom.Document;
import java.io.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;



public class NewsAnalyst {
	
	private String rURL, nextStr, nextTit;

	private Story story;
	
	private Document doc, docSent;
	
	// Constructor
	NewsAnalyst(Story current) {
		rURL = current.getLink();
		story = current;
	}
	
	private void analyse() {
		try {
	
			// Initialize feed
	        final URL feedUrl = new URL(rURL);
	        final SyndFeedInput input = new SyndFeedInput();
	        final SyndFeed feed = input.build(new XmlReader(feedUrl));
	        
	        String allInfo  = "";
	        
	        // Concatenate all the titles together
	        for (final Iterator<?> iter = feed.getEntries().iterator();
	                iter.hasNext();) {
	        	SyndEntry synd =  ((SyndEntry) iter.next());
	        	nextTit = synd.getTitle();
	        	nextStr = nextTit.substring(0, nextTit.lastIndexOf("-"));
	        	allInfo = allInfo + " " + nextStr + synd.getDescription().getValue();
	        }
	        
	        // Grabs Document for these titles
	        AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromString("fbde73712800960605177cdcf8cc5ade6ebd15a5");
	        AlchemyAPI_KeywordParams params = new AlchemyAPI_KeywordParams();
	        params.setKeywordExtractMode("strict");
	        params.setSentiment(true);
	        params.setMaxRetrieve(10);
	        doc = alchemyObj.TextGetRankedKeywords(allInfo, params);
	        docSent = alchemyObj.TextGetTextSentiment(allInfo);
	        
	        // Convert output to String
	        String alchemyOutput = getStringFromDocument(doc);
	        String alchemyOutputSent = getStringFromDocument(docSent);
	       
	        alchemyOutput = removeKeywordXML(alchemyOutput);
	        
	        // Removes XML tags from docSent
	        alchemyOutputSent = alchemyOutputSent.substring(alchemyOutputSent.indexOf("<score>"));
	        alchemyOutputSent = alchemyOutputSent.substring(7,alchemyOutputSent.lastIndexOf("</score>"));
	        
	        story.setSentiment(Double.parseDouble(alchemyOutputSent));
	        
	        // Result is array of form result[0] = topic, result[1] = relevance etc
	        String[] result = alchemyOutput.split(";");
	        
	        // Puts each word and its relevance into a list of keyWord tuples
	        for (int x=0; x<result.length; x+=3){
	        	story.getList().add(new keyWord(result[x], result[x+1], result[x+2]));
	        }
		}
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR: " + ex.getMessage());
        }
	}
	
	public Story getStory() {
		analyse();
		return story;
	}
	
	public Story updateStory() {
		story.clearKeyWords();
		analyse();
		return story;
	}
	
    public static String getStringFromDocument(Document doc) {
        try {
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);

            return writer.toString();
        } catch (TransformerException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static String removeKeywordXML(String s) {
    	// Removes XML tags from doc
    	s = s.substring(s.indexOf("<keywords>"));
    	s = s.substring(0,s.lastIndexOf("</results>"));
    	s = s.replaceAll("<.*keyword.*>","");
    	s = s.replaceAll("<.*sentiment>","");
    	s = s.replaceAll("<type>neutral</type>", "<type>neutral</type> <score>0</score>");
    	s = s.replaceAll("\\s+?<text>(.*?)</text>\\s+?<relevance>(.*?)</relevance>\\s+?<type>.*?</type>\\s+?<score>(.*?)</score>\\s+?","$1;$2;$3;");
    	s = s.substring(0,s.lastIndexOf(";"));
    	return s;
    }
}



