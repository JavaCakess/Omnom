package muncher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

import muncher.Inventory.Item;

import com.google.gson.Gson;

public class Command {

	static String trade_personBeingAsked = null;
	static String trade_personAsking = null;
	static Item item1_trade = null;
	static Item item2_trade = null;
	static Item titem1_trade = null;
	static Item titem2_trade = null;

	public static void inv() {
		ArrayList<String> data = 
				IOTools.readFile(new File("data/inventories/" + CommandHandler._user + ".txt"));

		String[] inv = data.get(0).split(",");

		String string = "Your inventory: ";
		for (int i = 0; i < inv.length; i++) {
			if (i != inv.length - 1) {
				string = string.concat(Inventory.Item.getNameFromID(Integer.parseInt(inv[i])) + ", ");
			} else {
				string = string.concat(Inventory.Item.getNameFromID(Integer.parseInt(inv[i])));
			}
		}
		CommandHandler.send(string);
	}

	public static void craft(String arg1, String arg2, String arg3) {
		/*
		 * Convert them to items.
		 */
		Item i1 = Item.nameToItem(arg1);
		Item i2 = Item.nameToItem(arg2);
		Item i3 = Item.nameToItem(arg3);
		Item newItem = null;
		/*
		 * First check if they have the items.
		 */
		Inventory inventory = new Inventory(CommandHandler._user);
		if (!inventory.contains(i1) || !inventory.contains(i2) || !inventory.contains(i3)) {
			CommandHandler.send("You do not have one of the items.");
			return;
		}
		CommandHandler.send("Crafting...");
		boolean success = false;
		for (CraftingRecipe cr : CraftingRecipe.list) {
			if (cr.item1 == i1 && cr.item2 == i2 && cr.item3 == i3) {
				System.out.println("=-=");
				inventory.remove(i1); inventory.remove(i2); inventory.remove(i3);
				System.out.println(i1 + ", " + i2 + ", " + i3 + " = " + cr.result);
				System.out.println("=-=");
				inventory.add(cr.result);
				newItem = cr.result;
				success = true;
			}
		}
		try {
			Thread.sleep(2500);
		} catch (Exception e) {

		}
		if (success) {
			CommandHandler.send("Crafting succeeded! New item: " + newItem.name);

		} else {
			CommandHandler.send("Crafting failed, no new items!");
		}
	}

	public static void decraft(String arg1) {
		/*
		 * Convert it to an items.
		 */
		Item i = Item.nameToItem(arg1);

		/*
		 * First check if they have the items.
		 */
		Inventory inventory = new Inventory(CommandHandler._user);
		if (!inventory.contains(i)) {
			CommandHandler.send("You do not have the item to decraft.");
			return;
		}
		CommandHandler.send("Decrafting...");
		boolean success = false;
		String items = "";
		for (DecraftRecipe dr : DecraftRecipe.list) {
			if (dr.item == i) {
				inventory.remove(i);
				for (int v = 0; v < dr.results.length; v++) {
					inventory.add(dr.results[v]);
					if (v != dr.results.length - 1) {
						items = items.concat(dr.results[v].name + ", ");
					} else {
						items = items.concat(dr.results[v].name);
					}
				}
				success = true;
			}
		}
		try {
			Thread.sleep(2500);
		} catch (Exception e) {

		}
		if (success) {
			CommandHandler.send("Decrafting successful! New items: " + items);

		} else {
			CommandHandler.send("Decrafting failed, no new items!");
		}
	}

	public static void trade(String person, String item1, String item2, String titem1, String titem2) {
		/*
		 * Convert it them to items.
		 */
		Item i = Item.nameToItem(item1);
		Item i2 = Item.nameToItem(item2);
		Item ti = Item.nameToItem(titem1);
		Item ti2 = Item.nameToItem(titem2);

		/*
		 * First check if they have the items.
		 */
		Inventory inventory = new Inventory(CommandHandler._user);
		if (!inventory.contains(i) || !inventory.contains(i2)) {
			CommandHandler.send("You do not have the item(s) to trade.");
			return;
		}

		if (i == Item.EMPTY && i2 == Item.EMPTY) {
			CommandHandler.send("You must put in an item!");
			return;
		}

		/*
		 * Now do the same for the other guy.
		 */
		Inventory tinventory = new Inventory(person);
		if (!tinventory.contains(i) || !tinventory.contains(i2)) {
			CommandHandler.send("They do not have those items!");
			return;
		}
		if (ti == Item.EMPTY && ti2 == Item.EMPTY) {
			CommandHandler.send("You must put an item that you want.");
			return;
		}
		trade_personBeingAsked = person;
		trade_personAsking = CommandHandler._user;
		item1_trade = i;
		item2_trade = i2;
		titem1_trade = ti;
		titem2_trade = ti2;
		CommandHandler.send(person + ": Do you accept this trade? " +
				"("+ i.name + " and "+i2.name + " for your " + ti.name + " and " + ti2.name + ")" +
				" Use !om accept to accept or !om deny to deny.");
	}

	public static void accept() {
		if (CommandHandler._user.equalsIgnoreCase(trade_personBeingAsked)) {
			Inventory user1 = new Inventory(trade_personAsking);
			user1.remove(item1_trade);
			user1.remove(item2_trade);
			user1.add(titem1_trade);
			user1.add(titem2_trade);
			Inventory user2 = new Inventory(trade_personBeingAsked);
			user2.remove(titem1_trade);
			user2.remove(titem2_trade);
			user2.add(item1_trade);
			user2.add(item2_trade);
			CommandHandler.send("Trade accepted!");
			trade_personBeingAsked = null;
			trade_personAsking = null;
		}

	}

	public static void deny() {
		if (CommandHandler._user.equalsIgnoreCase(trade_personBeingAsked)) {
			trade_personBeingAsked = null;
			trade_personAsking = null;
			CommandHandler.send("Trade denied.");
		}


	}

	public static void pmath(String math) {
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream("pymath.py");
			fos.write(("import math;\nprint(" + math + ");").getBytes());
			fos.flush();
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		}

		Util.runProcess("python pymath.py");
	}

	public static void py(String py) {
		FileOutputStream fos = null;
		FileInputStream fis = null;
		try {
			fos = new FileOutputStream("pymath.py");
			ArrayList<String> imports = IOTools.readFile(new File("pyimp.txt"));
			for (String v : imports) {
				fos.write((v + "\n").getBytes());
			}
			fos.write(py.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		}


		Thread termThread = new Thread(
				new Runnable() {
					public void run() {
						Thread t = new Thread(
								new Runnable() {
									public void run() {
										Util.runProcess("python pymath.py");
									}
								}
								);
						t.start();
						try {
							Thread.sleep(10000);
						} catch (Exception e) {}
						if (t.isAlive()) {
							t.interrupt();
							System.out.println("-- Terminated --");
						}
					}
				}
				);

		termThread.start();
	}



	public static void pyimp(String imp) {
		if (imp.contains("os")) {
			return;
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream("pyimp.py");
			fos.write(imp.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		}
	}

	public static void alarm(final String string) {
		Thread t = new Thread(
				new Runnable() {
					public void run() {
						String user = CommandHandler._user;
						int time = Integer.parseInt(string);
						try {
							Thread.sleep(time * 1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						py("print(\"" + user + ", time to MOOOOOVE!\")");
					}
				}
				);
		t.start();
	}

	public static void MCSay(String bundleStrings) {
		Omnom.MCServer.write("say " + bundleStrings);
	}

	public static void merge(String string1, String string2) {
		byte[] data1 = string1.getBytes();
		byte[] data2 = string2.getBytes();

		byte[] c = getLarger(data1, data2);
		byte[] d = getSmaller(data1, data2);
		byte[] result = new byte[c.length];
		for (int i = 0; i < c.length; i++) {
			byte returnByte = 0x00;
			if (i < d.length) {
				returnByte = (byte)(c[i] | d[i]);
			} else returnByte = c[i];
			result[i] = returnByte;
		}
		CommandHandler.send(new String(result));
	}

	private static byte[] getSmaller(byte[] a, byte[] b) {
		return a.length > b.length ? b : a;
	}

	private static byte[] getLarger(byte[] a, byte[] b) {
		return a.length > b.length ? a : b;
	}

	public static void jumble(String str1) {
		byte[] arr = str1.getBytes();
		Random r = new Random();
		for(int i = arr.length-1; i > 0; i--){
			int rand = r.nextInt(i);
			byte temp = arr[i];
			arr[i] = arr[rand];
			arr[rand] = temp;
		}
		CommandHandler.send(new String(arr));
	}

	public static void tries(String str1, String str2) {
		byte[] arr = str1.getBytes();
		Random r = new Random();
		long l = 0;
		while (!new String(arr).equals(str2)) {
			if (l > 100000L) {
				CommandHandler.send("Took over 100,000 tries.");
				return;
			}
			for(int i = arr.length-1; i > 0; i--){
				int rand = r.nextInt(i);
				byte temp = arr[i];
				arr[i] = arr[rand];
				arr[rand] = temp;
			}
			l++;
		}
		CommandHandler.send(new String("Took " + l + " tries."));
	}

	public static void search(String str1) {
		String google = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
		String search = str1;
		String charset = "UTF-8";

		try {
			URL url = new URL(google + URLEncoder.encode(search, charset));

			Reader reader = new InputStreamReader(url.openStream(), charset);
			GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);

			// Show title of 1st result.
			String msg = "";
			int limit = results.getResponseData().getResults().size();
			if (limit > 25) limit = 25;
			for (int i = 0; i < limit; i++) {
				if (i < results.getResponseData().getResults().size() - 1) {
					msg = msg.concat(results.getResponseData().getResults().get(i).getTitle() + " || ");
				} else {
					msg = msg.concat(results.getResponseData().getResults().get(i).getTitle());
				}
			}
			CommandHandler.send(msg);
		} catch (IOException ioe) {
			CommandHandler.send("Failed :c");
			ioe.printStackTrace();
		}
	}
}
