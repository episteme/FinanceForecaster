package team2.mainapp;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;


public class TCPClient {
	
	public static String go(String s) {

		String r;
		
		Log.d("Connection: ",s);
		
		try {

			Log.d("TCP", "C: Connecting...");

			Socket socket = new Socket("10.0.2.2", 19999);			

			try {

				Log.d("TCP", "C: Sending: '" + s + "'");

				PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
				Log.d("Message", "Message?");
				out.println(s);
				BufferedInputStream is = new BufferedInputStream(socket.getInputStream());
				InputStreamReader isr = new InputStreamReader(is);
				int character;
				StringBuffer process = new StringBuffer();
				while((character = isr.read()) != '\t') {
					process.append((char)character);
				}

				Log.d("Message","Received");
				r = process.toString();

				return r;

			} catch(Exception e) {

				Log.e("TCP", "S: Error1", e);

			} finally {

				socket.close();

			}

		} catch (Exception e) {
			Log.e("TCP", "C: Error", e);

		}
		return "error";

	}

}