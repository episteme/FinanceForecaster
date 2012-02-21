import java.net.*;
import java.io.*;
import java.util.*;

public class MultipleSocketServer implements Runnable {

	static Runnable[] feeds = new Runnable[2];
	private Socket connection;
	private String TimeStamp;
	private int ID;
	public static void main(String[] args) {
		feeds[0] = new FeedReader("oil");
		feeds[1] = new FeedReader("currency");
		System.out.println("go");
		int port = 19999;
		int count = 0;
		Thread tf = new Thread(feeds[0]);
		tf.start();
		Thread tf2 = new Thread(feeds[1]);
		tf2.start();
		try {
			ServerSocket socket1 = new ServerSocket(port);
			System.out.println("MultipleSocketServer Initialized");
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
			System.out.println(process);
			String title = ((FeedReader) feeds[0]).getStories()[0].getTitle();
			String title2 = ((FeedReader) feeds[1]).getStories()[0].getTitle();
			String returnTitle2 = "Title2: " + title2 + '\n';
			String returnTitle = "Title: " + title + '\n';
			BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream());
			OutputStreamWriter osw = new OutputStreamWriter(os, "US-ASCII");
			osw.write(returnTitle);
			osw.write(returnTitle2);
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