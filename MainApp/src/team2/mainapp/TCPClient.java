package team2.mainapp;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;


public class TCPClient {
	
	public static String go() {
		
		String s;

		try {

			Log.d("TCP", "C: Connecting...");

			Socket socket = new Socket("www.ruination.org.uk", 19999);

			String message = "oil;currency;;1212/12/12 12:12:12";

			try {

				Log.d("TCP", "C: Sending: '" + message + "'");

				PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
				out.println(message);
				BufferedInputStream is = new BufferedInputStream(socket.getInputStream());
				InputStreamReader isr = new InputStreamReader(is);
				int character;
				StringBuffer process = new StringBuffer();
				while((character = isr.read()) != '\t') {
					process.append((char)character);
				}

				Log.d("TCP", "C: Sent.");
				
				Log.d("Debug", "Printing");
				
				s = process.toString();
				//Log.d("Output", s);

				Log.d("TCP", "C: Done.");
				return s;


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