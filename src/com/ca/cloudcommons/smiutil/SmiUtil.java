package com.ca.cloudcommons.smiutil;


import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

public class SmiUtil {
	
	static Logger _log = Logger.getLogger(SmiUtil.class);
	
	public static void main(String args[]) {
		try {
			Util.initLog4j();
			Options opt = new Options();
			opt.addOption("h", false, "Print help for this application");
			opt.addOption("f", true, "filename a csv format list containing the name and IP address of the servers to test");

			BasicParser parser = new BasicParser();
			CommandLine cl = parser.parse(opt, args);
			HelpFormatter hf = new HelpFormatter();

			if (cl.hasOption('h')) {
				printUsage(hf, opt);
			} else {
				String fileName = cl.getOptionValue("f");

				if (fileName != null) {
					_log.info("Opening csv file: " + fileName);

					System.exit(0);
				} else {
					printUsage(hf, opt);
					System.exit(1);
				}
			}

		} catch (Exception e) {
			_log.error("Main", e);
			System.exit(2);
		}
	}

	private static void printUsage(HelpFormatter hf, Options opt) {
		hf.printHelp("SmiUtil", 
				"Use to upload services into the Overlord database - Version 1.0", opt, "E.G. SmiUtil -f Betty.csv", true);
	}
}
