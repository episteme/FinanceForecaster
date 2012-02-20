import java.net.*;
import java.io.*;
import java.util.*;

public class MultipleSocketServer implements Runnable {

	static Runnable run2;
	private Socket connection;
	private String TimeStamp;
	private int ID;
	public void go() {
		System.out.println("go");
		int port = 19999;
		int count = 0;
		Thread tf = new Thread(run2);
		tf.start();
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


	public MultipleSocketServer() {
		run2 = new FeedReader("oil");
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
			String title = ((FeedReader) run2).getStories()[0].getTitle();
			String returnTitle = "Title: " + title + '\n';
			BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream());
			OutputStreamWriter osw = new OutputStreamWriter(os, "US-ASCII");
			osw.write(returnTitle);
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