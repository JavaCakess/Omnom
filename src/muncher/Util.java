package muncher;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Util {

	public static int runProcess(String command) {
		try {
			Process pro = Runtime.getRuntime().exec(command);
			printLines(command + " stdout:", pro.getInputStream());
			printLines(command + "stderr:", pro.getErrorStream());
			pro.waitFor();
			System.out.println(pro.exitValue());
			if (pro.exitValue() != 0) {
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
		return 0;
	}

	public static void printLines(String name, InputStream ins) throws Exception {
		String line = null;
		BufferedReader in = new BufferedReader(
				new InputStreamReader(ins));
		int l = 1;
		while ((line = in.readLine()) != null) {
			if (l == 6) {
				CommandHandler.send("-- Terminating: Max lines reached --");
				break;
			}
			if (line.length() > 128) {
				break;
			}
			CommandHandler.send(line);
			try {
				Thread.sleep(1000);
			} catch (Exception e) {

			}
			l++;
		}
	}
}
