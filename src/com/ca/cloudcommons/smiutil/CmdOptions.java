package com.ca.cloudcommons.smiutil;

//OptionsTip.java
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CmdOptions {
	public static void main(String args[]) {
		try {
			Options opt = new Options();

			opt.addOption("h", false, "Print help for this application");
			opt.addOption("u", true, "Username");
			opt.addOption("p", true, "Password");

			BasicParser parser = new BasicParser();
			CommandLine cl = parser.parse(opt, args);

			if (cl.hasOption('h')) {
				HelpFormatter hf = new HelpFormatter();
				hf.printHelp("Options", opt);
			} else {
				System.out.println(cl.getOptionValue("u"));
				System.out.println(cl.getOptionValue("dsn"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
