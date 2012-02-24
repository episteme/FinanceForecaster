import java.net.*;
import java.util.LinkedList;
import java.io.*;

import org.w3c.dom.Document;

import com.alchemyapi.api.AlchemyAPI;
import com.alchemyapi.api.AlchemyAPI_KeywordParams;

public class Parse {
	
	static Document doc;
	
    public static void main(String[] args) throws Exception {
    
    	
        URL yahoo = new URL("http://www.google.co.uk/search?q=oil&tbm=nws&tbs=sbd:1");
        URLConnection yc = yahoo.openConnection();
        yc.setRequestProperty
        ( "User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)" );
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                yc.getInputStream()));
        String inputLine;
        
        // Grabs all the links and stuffs them into theURLS
        LinkedList<String> theURLS = new LinkedList<String>();
        while ((inputLine = in.readLine()) != null) {
        	if (inputLine.indexOf(" minute") != -1 || inputLine.indexOf("></span>Shopping</a>") != -1) {
        		int linkPos = inputLine.indexOf("<a href=\"/url?q=http:");
        		if (linkPos == -1)
        			break;
        		String noFront = inputLine.substring(linkPos + 16, inputLine.length());
        		int endLink = noFront.indexOf("&amp;sa");
        		String theURL = noFront.substring(0, endLink);
                theURLS.add(theURL);
        	}
        }
        in.close();
        
        for (String URL : theURLS) {
        	System.out.println(URL);
            AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromString("fbde73712800960605177cdcf8cc5ade6ebd15a5");
            AlchemyAPI_KeywordParams params = new AlchemyAPI_KeywordParams();
            params.setKeywordExtractMode("strict");
            params.setMaxRetrieve(10);
            try {
            	doc = alchemyObj.URLGetRankedKeywords(URL, params);
            	// Convert output to String
            	String alchemyOutput = NewsAnalyst.getStringFromDocument(doc);

            	// Removes XML tags from doc
            	alchemyOutput = alchemyOutput.substring(alchemyOutput.indexOf("<keywords>"));
            	alchemyOutput = alchemyOutput.substring(0,alchemyOutput.lastIndexOf("</results>"));
            	alchemyOutput = alchemyOutput.replaceAll("<.*keyword.*>","");
            	alchemyOutput = alchemyOutput.replaceAll("\\s+?<text>(.*?)</text>\\s+?<relevance>(.*?)</relevance>\\s+?","$1;$2;");
            	alchemyOutput = alchemyOutput.substring(0,alchemyOutput.lastIndexOf(";"));

            	String[] result = alchemyOutput.split(";");

            	for (String s: result) {
            		System.out.println(s);
            	}
            } catch (Exception e) {}
        }
        

    
    }
}