import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MultipleSocketServer implements Runnable {

	static Runnable[] feeds = new Runnable[2];
	private Socket connection;
	private String TimeStamp;
	private int ID;
	public static void main(String[] args) {
		feeds[0] = new FeedReader("oil");
		feeds[1] = new FeedReader("currency");
		int port = 19999;
		int count = 0;
		Thread tf = new Thread(feeds[0]);
		tf.start();
		Thread tf2 = new Thread(feeds[1]);
		tf2.start();
		try {
			ServerSocket socket1 = new ServerSocket(port);
			System.out.println("Server starting..");
			while (true) {
				Socket connection = socket1.accept();
				Runnable runnable = new MultipleSocketServer(connection, ++count);
				Thread thread = new Thread(runnable);
				thread.start();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	MultipleSocketServer(Socket s, int i) {
		this.connection = s;
		this.ID = i;
	}

	public void run() {
		try {
			BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
			InputStreamReader isr = new InputStreamReader(is);
			int character;
			StringBuffer process = new StringBuffer();
			while((character = isr.read()) != '\n') {
				process.append((char)character);
			}
			String processStr = process.toString();
			String[] processArr = processStr.split(";;");
			String[] topicArr = processArr[0].split(";");
			BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream());
			OutputStreamWriter osw = new OutputStreamWriter(os, "US-ASCII");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
			Date convertedDate = dateFormat.parse(processArr[1]); 
			System.out.println(processArr[1]);
			for (String topic : topicArr) {
				for (int i = 0; i < feeds.length; i++) {
					if (((FeedReader) feeds[i]).getSector().compareTo(topic) == 0) {
						FeedReader thefeed = ((FeedReader) feeds[i]);
						for (Story S: thefeed.getStories()) {
							if (S == null) {
									continue;
							}
							if (convertedDate.compareTo(S.getTimestamp()) < 0) {
								String title = S.getTitle();
								String returnTitle = thefeed.getSector() + ": " + title + " @ " + S.getDate() + '\n';
								osw.write(returnTitle);
							}
						}

					}
				}
			}
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
			catch (IOException e){}
		}
	}
}