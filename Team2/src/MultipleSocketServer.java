import java.net.*;
import java.io.*;
import java.util.*;

import org.jsoup.Jsoup;

public class MultipleSocketServer implements Runnable {
	
	static Runnable[] parsers = new Runnable[2];
	private Socket connection;
	public static void main(String[] args) {
		parsers[0] = new Parse("oil");
		parsers[1] = new Parse("technology");

		Thread oiltopt = new Thread(parsers[0]);
		oiltopt.start();
		Thread currtopt = new Thread(parsers[1]);
		currtopt.start();

		// Start main server on this port
		int port = 8009;
		try {
			ServerSocket socket1 = new ServerSocket(port);
			System.out.println("Server starting..");
			while (true) {
				Socket connection = socket1.accept();
				Runnable runnable = new MultipleSocketServer(connection);
				Thread thread = new Thread(runnable);
				thread.start();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	MultipleSocketServer(Socket s) {
		this.connection = s;
	}

	public void run() {
		try {
			// Read input
			BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
			InputStreamReader isr = new InputStreamReader(is);
			int character;
			StringBuffer process = new StringBuffer();
			while((character = isr.read()) != '\n') {
				process.append((char)character);
			}
			String processStr = process.toString();
			

			// Process input
			// Format: topic;topic;;datetime
			// let convertedDate be input date
			String[] processArr = processStr.split(";;");
			String[] topicArr = processArr[0].split(";");
			BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream());
			OutputStreamWriter osw = new OutputStreamWriter(os, "US-ASCII");
			System.out.println(processArr[1]);

			// Send topics - Parse
			for (String topic : topicArr) {
				for (int i = 0; i < parsers.length; i++) {
					if (((Parse) parsers[i]).getSector().compareTo(topic) == 0) {
						Parse theparse = ((Parse) parsers[i]);
						java.util.Collections.sort(theparse.getTopics());
						int j = 0;
						@SuppressWarnings("unchecked")
						LinkedList<Topic> topicz = (LinkedList<Topic>) theparse.getTopics().clone();
						for (Topic T: topicz) {
							if (T == null)
								continue;
								String returnTitle = ((Integer) T.getUid()).toString() + ";;\n";
								returnTitle += T.getRecentTitle() + ";;\n"; 
								returnTitle += T.getDate() + ";;\n";
								returnTitle += T.artsLastHour() + ";;\n";
								returnTitle += T.topArticles() + ";;\n";
								returnTitle += T.topWords() + ";;\n";
								returnTitle += T.getSentiment()+ ";;\n";
								returnTitle += T.getCount() + ";;\n";
								returnTitle += T.sendCompanyList() + ";;\n";
								osw.write(returnTitle);
								osw.write("SPECTOPS\n");
							j++;
							if (j > 20) {
								break;
							}
						}

					}
				}
				osw.write("TOPSTOP\n");

			}
			
			osw.write("SPLITINFO\n");


			// Send companies - Parse
			for (String topic : topicArr) {
				for (int i = 0; i < parsers.length; i++) {
					if (((Parse) parsers[i]).getSector().compareTo(topic) == 0) {
						Parse theparse = ((Parse) parsers[i]);
						//java.util.Collections.sort(theparse.getTopics());
						@SuppressWarnings("unchecked")
						LinkedList<Company> companiez = (LinkedList<Company>) theparse.getCompanies().clone();
						for (Company C : companiez) {
							if (C == null) 

								continue;
							{
								String returnTitle = Jsoup.parse(C.getName()).text() + ";;\n";
								returnTitle += C.getSentiment() + ";;\n";
								returnTitle += C.getRelevance() + ";;\n";
								returnTitle += C.getArticles() + ";;\n";
								returnTitle += C.getStockPrice() + ";;\n";
								returnTitle += C.getStockChange() + ";;\n";
								returnTitle += C.isTraded() + ";;\n";
								osw.write(returnTitle);
								osw.write("SPECCOMP\n");
							}
						}

					}
				}
				osw.write("COMPSTOP\n");
			}
			osw.write("\t");
			osw.flush();

		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		finally {
			try {
				connection.close();
			}
			catch (IOException e) {}
		}
	}
}