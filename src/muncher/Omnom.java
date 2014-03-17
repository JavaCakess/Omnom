package muncher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Omnom {

	static BufferedReader reader = null;
	static BufferedWriter writer = null;
	static Socket socket;
	public static String server, channel;
	static Scanner s = new Scanner(System.in);
	public static String nick = "Omnom";
	public static String login = "muncher 8 * : Munchtoss' bot.";
	public static String helpPath = "data/help/";
	
	public ArrayList<String> users = new ArrayList<String>();
	public static Thread consoleThread = new Thread(
			new Runnable() {
				public void run() {
					while (true) {
						String command = input("] ");

						if (command.split(" ")[0].equals("say")) {
							CommandHandler.send(IOTools.bundleStrings(command.split(" "), 1, command.split(" ").length));
						} else if (command.split(" ")[0].equals("pm")) {
							send(command.split(" ")[1], IOTools.bundleStrings(command.split(" "), 2, command.split(" ").length));
						}
					}
				}
			}
			);
	public static MinecraftServer MCServer;

	public static void main(String[] args) {
		server = input("] Please enter a network. (e.g.: irc.freenode.net)");
		channel = input("] Specify a channel. (e.g. #pootis)");

		try {
			MCServer = new MinecraftServer();
			MCServer.start();
			/*
			 * Connect the socket to the server, port 6667.
			 */
			socket = new Socket(server, 6667);
			/*
			 * Initialize our BufferedReader and BufferedWriter.
			 */
			reader = new BufferedReader(
					new InputStreamReader(socket.getInputStream())
					);
			writer = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())
					);

			/*
			 * Next, we give the server our nick.
			 */
			send("NICK " + nick + "\r\n");
			send("USER " + login + "\r\n");

			// Read lines from the server until it tells us we have connected.
			String line = null;

			while ((line = reader.readLine( )) != null) {
				if (line.startsWith("PING ")) {
					writer.write("PONG " + line.substring(5) + "\r\n");
					writer.flush();
				}

				if (line.indexOf("004") >= 0) {
					// We are now logged in.
					print("Logged in.");
					break;
				}else if (line.indexOf("433") >= 0) {
					print("Nickname is already in use.");
					send("NICK " + nick + "_\r\n");
					send("USER " + login + "_\r\n");
				}
			}

			/*
			 * Finally, we join the channel!
			 */
			send("JOIN " + channel + "\r\n");
		} catch (Exception e) {
			print("Error during initialization.");
			e.printStackTrace();
			System.exit(-1);
		}
		String line = "";

		try{
			consoleThread.start();
			while ((line = reader.readLine( )) != null) {
				if (line.startsWith("PING ")) {
					// We must respond to PINGs to avoid being disconnected.

					writer.write("PONG " + line.substring(5) + "\r\n");
					writer.flush( );
				}
				if (line.contains("PRIVMSG")) {
					String channel = line.substring(line.indexOf("PRIVMSG ") + "PRIVMSG ".length(), line.indexOf(" :"));
					int o = line.indexOf(channel) + channel.length();
					try {
						if (line.substring(o + 2, o + 5).equalsIgnoreCase("!om")) {

							try {
								String cmd = line.substring(line.indexOf(":!om") + 5);
								String user = line.substring(1, line.indexOf("!"));
								CommandHandler.handleCommand(line.substring(line.indexOf("PRIVMSG ") + "PRIVMSG ".length(), line.indexOf(" :")), line, user, cmd);
							}catch(Exception e){

							}
						}
					} catch (Exception e) {

					}
				}
				if (CommandHandler.log) {
					print("[" + time() + "] << " + line);
				
				}

			}
		}catch(Exception ioe) {
			ioe.printStackTrace();
		}
	}

	public static String input() {
		return s.nextLine();
	}

	public static String input(String str) {
		print(str);
		return s.nextLine();
	}

	public static void print(String str) {
		System.out.println(str);

	}

	public static void send(String data) {
		try {
			writer.write(data);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String time() {
		Date d = new Date();
		int h = d.getHours();
		int m = d.getMinutes();
		int s = d.getSeconds();
		String hours = "" + h, minutes = "" + m, secs = "" + s;
		if (h < 10) {
			hours = "0" + h;
		}
		if (m < 10) {
			minutes = "0" + m;
		}
		if (s < 10) {
			secs = "0" + s;
		}
		return (hours + ":" + minutes + ":" + secs);
	}

	public static void send(String channel, String msg) {
		send("PRIVMSG " + channel + " :" + msg + "\r\n");
		print("[" + time() + "] >> [" + channel + "] " + msg);
	}
	public static void pm(String user, String msg) {
		send("PRIVMSG " + user + " :" + msg + "\r\n");
		print("[" + time() + "] >> [" + user + "] " + msg);
	}
}

