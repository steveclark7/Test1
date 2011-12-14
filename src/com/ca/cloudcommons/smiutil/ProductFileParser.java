package com.ca.cloudcommons.smiutil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.Arrays;

public class ProductFileParser {

	public ProductFileParser() {
	}

	/**
	 * Read a file in the format VM,Connecting IP,Result CC-Prod-Web1,50.56.38.156,connects
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	ArrayList<NameIp> parseFile(String fileName) throws IOException {
		ArrayList<NameIp> al = new ArrayList<NameIp>();
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		String line;

		while ((line = in.readLine()) != null) {
			if (line.contains("Connecting")) {
				continue;
			}
			
			Pattern pat = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))");

			String[] str = pat.split(line);
			
			getHeader(str);

			break;
//			if (str.length >= 2) {
//				al.add(new NameIp(str[0], str[1]));
//			}
		}

		in.close();

		return al;
	}
	
	void getHeader(String[] str){
		
		for (int i = 0; i < str.length; i++) {
			String string = str[i];
			
			
		}
		
//		private ArrayList<String> csvToList(String csv) {
		
	}
	
	int getHeaderPosition(String[] str, String header){
		int retval = -1;
		
		for (int i = 0; i < str.length; i++) {
			String string = str[i];
			
			if( string.equalsIgnoreCase(header)){
				retval = i;
				break;
			}						
		}
				
		return retval;
	}
	
	

	private ArrayList<String> csvToList(String csv) {
		ArrayList<String> retval = new ArrayList<String>();
		
		if (csv.length() > 0) {
			// regex matches csv string
			Pattern pat = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))");

			String[] str = pat.split(csv);

			for (String value : str) {
				retval.add(value);
			}
		}

		return retval;
	}

	/**
	 * Data transfer object to hold details read from the input file.
	 * 
	 * @author Developer
	 * 
	 */
	private class NameIp {
		String name;
		String ip;

		public NameIp(String name, String ip) {
			this.name = name;
			this.ip = ip;
		}

		public String getName() {
			return name;
		}

		public String getIp() {
			return ip;
		}
	}

}
