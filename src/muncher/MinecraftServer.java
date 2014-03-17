package muncher;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;




public class MinecraftServer extends Thread {
	
	public static ServerSocket ssock = null;
	public static Socket sock = null;
	public static BufferedWriter writer = null;
	public static BufferedReader reader = null;
	
	public MinecraftServer() {

	}
	
	public void run() {
		try { 
			waitForConnect();
			setUpConnection();
			listen();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void waitForConnect() throws IOException {
		ssock = new ServerSocket(7770);
		sock = ssock.accept();
		Omnom.send(Omnom.channel, "Connection to Minecraft Server established.");
	}
	
	public void setUpConnection() throws IOException {
		writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	}
	
	public void listen() throws IOException {
		String line = "";
		while ((line = reader.readLine()) != null) {
			Omnom.send(Omnom.channel, line);
		}
	}
	
	public void write(String s) {
		try {
			writer.write(s + "\r\n");
			writer.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
