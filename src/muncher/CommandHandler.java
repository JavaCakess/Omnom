package muncher;


public class CommandHandler {

	public static String _line, _user, _channel;
	public static String[] _args;
	public static boolean log = true;
	public static void handleCommand(String channel, String line, String user, String cmd) {
		_line = line; _user = user; _channel = channel;
		String[] args = cmd.split(" ");
		_args = args;
		String command = args[0];
		switch (command) {
		case "inv":
			/*
			 * Get the user's inventory
			 */
			Command.inv();
			break;
		case "craft":
			if (args.length > 2 && args.length < 5) {
				if (args.length > 3) {
					Command.craft(args[1], args[2], args[3]);
				} else {
					Command.craft(args[1], args[2], "--");
				}
			} else {
				CommandHandler.send("Not enough arguments.");
			}
			break;
		case "decraft":
			if (args.length != 2) {
				CommandHandler.send("Not enough arguments.");
			} else {
				Command.decraft(args[1]);
			}
			break;
		case "trade":
			if (args.length == 6) {
				Command.trade(args[1], args[2], args[3], args[4], args[5]);
			} else {
				CommandHandler.send("Improper amount of arguments!");
			}
			break;
		case "accept":
			Command.accept();
			break;
		case "deny":
			Command.deny();
			break;
		case "ping":
			/*
			 * Respond to them with pong.
			 */
			send("Pong!");
			break;
		case "pmath":
			/*
			 * Python math!
			 */
			if (args.length > 1) {
				Command.pmath(IOTools2.bundleStrings(args, 1, args.length));
			}
			break;
		case "py":
			/*
			 * Run python scripts.
			 */
			if (args.length > 1) {
				Command.py(IOTools2.bundleStrings(args, 1, args.length));
			}
			break;
		case "log":
			/*
			 * Whether to print stuff or not.
			 */
			log = !log;
			break;
		case "pyimp":
			/*
			 * Import python stuffz.
			 */
			if (args.length > 1) {
				Command.pyimp(IOTools2.bundleStrings(args, 1, args.length));
			}
			break;
		case "alarm":
			/*
			 * Time an alarm, which will alert the user.
			 */
			if (args.length > 1) {
				Command.alarm(args[1]);
			}
			break;
		case "mcsay":
			if (args.length > 1) {
				Command.MCSay(IOTools2.bundleStrings(args, 1, args.length));
			}
			break;
		case "merge":
			if (args.length == 3) {
				Command.merge("-n", args[1], args[2]);
			} else if (args.length == 4) {
				Command.merge(args[1], args[2], args[3]);
			}
			break;
		case "jumble":
			if (args.length > 1) {
				Command.jumble(args[1]);
			}
			break;
		case "tries":
			if (args.length > 2) {
				Command.tries(args[1], args[2]);
			}
			break;
		case "search":
			if (args.length > 1) {
				Command.search(args[1]);
			}
			break;
		case "lda":
			if (args.length > 1) {
				Command.lda(args[1]);
			}
			break;
		case "sta":
			if (args.length > 1) {
				Command.sta(args[1]);
			}
			break;
		case "prn":
			if (args.length > 1) {
				Command.prn(args[1]);
			}
			break;
		case "asm":
			if (args.length > 1) {
				Command.asm(args[1]);
			}
			break;
		case "edit":
			if (args.length > 1) {
				Command.edit(args[1]);
			}
			break;
		case "readprog":
			if (args.length > 1) {
				Command.readProgram(args[1]);
			}
			break;
		case "gooby":
			send("dolan");
			break;
		}
	}
	
	public static void send(String msg) {

		if (_line.toLowerCase().contains("privmsg omnom :")) {
			Omnom.pm(_user, msg);
		} else {
			Omnom.send(_channel, msg);
		}
	}

	public static boolean NEP(int am) {
		if (_args.length - 1 < am) {
			return true;
		}
		return false;
	}
}
