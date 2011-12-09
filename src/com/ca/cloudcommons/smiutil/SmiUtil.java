package com.ca.cloudcommons.smiutil;


import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class SmiUtil {
	public static void main(String args[]) {
		try {
			Options opt = new Options();

			opt.addOption("h", false, "Print help for this application");
			opt.addOption("c", true, "Command [AddService|ListService]");
			opt.addOption("u", true, "provider UUID");
			opt.addOption("s", true, "Service name");
			opt.addOption("d", true, "Service description");

			BasicParser parser = new BasicParser();
			CommandLine cl = parser.parse(opt, args);

			if (cl.hasOption('h')) {
				HelpFormatter hf = new HelpFormatter();
				hf.printHelp("SmiUtil", "header", opt, "Use to add or view services by a providers UUID", true);
			} else {
				System.out.println(cl.getOptionValue("u"));
				System.out.println(cl.getOptionValue("dsn"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
