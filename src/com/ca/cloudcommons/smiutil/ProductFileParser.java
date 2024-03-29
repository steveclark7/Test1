package com.ca.cloudcommons.smiutil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class ProductFileParser {

	static Logger _log = Logger.getLogger(ProductFileParser.class);

	public ProductFileParser() {
	}

	/**
	 * Read a file in the format VM,Connecting IP,Result CC-Prod-Web1,50.56.38.156,connects
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	ArrayList<ProductData> parseFile(String fileName) throws IOException {
		ArrayList<ProductData> al = new ArrayList<ProductData>();
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		String line;

		HeaderPosition hp = null;

		for (int i = 0; (line = in.readLine()) != null; i++) {
			Pattern pat = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))");

			String[] str = pat.split(line);

			// first line is header
			if (i == 0) {
				hp = getHeader(str);
				// TODO validate header

			} else {
				if (str.length >= hp.getLength() ) {
//					System.out.println("Name: " + str[hp.getProduct_name()]);

//					stripQuotes
					ProductData values = new ProductData();					
					values.setStatus(str[hp.getStatus()]);
					values.setProduct_name(stripQuotes(str[hp.getProduct_name()]));
					values.setManufacturer(stripQuotes(str[hp.getManufacturer()]));
					values.setDescription(stripQuotes(""));
					
					al.add( values );

					// System.out.println("Description: " + str[hp.getDescription()]);
				}
			}
		}

		in.close();

		return al;
	}

	HeaderPosition getHeader(String[] str) {

		HeaderPosition hp = new HeaderPosition();

		if (str.length <= 0) {
			throw new IllegalArgumentException("CSV Header has no values");
		}

		// status,product_name,manufacturer,description
		hp.setStatus(0);
		hp.setProduct_name(1);
		hp.setManufacturer(2);
		hp.setDescription(3);
		
		if( str.length == 4 ){
			hp.setLength(3); // TODO fudge for empty description, remove this 			
		}
		else{
			hp.setLength(str.length);			
		}
			

		// TODO
		for (int i = 0; i < str.length; i++) {
			String name = str[i];

			if (name.equalsIgnoreCase(HeaderPosition.STATUS)) {

			}

		}

		// private ArrayList<String> csvToList(String csv) {

		return hp;
	}
	
	private String stripQuotes(String element){
		String retval = element;
		
		if (element.length() > 2) {
			if (element.startsWith("\"") && element.endsWith("\"")) {
				retval = element.substring(1, element.length() - 1);
			}
		}
		
		return retval;
	}

	int getHeaderPosition(String[] str, String header) {
		int retval = -1;

		for (int i = 0; i < str.length; i++) {
			String string = str[i];

			if (string.equalsIgnoreCase(header)) {
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
	class ProductData {
		private String status, product_name, manufacturer, description;

		public ProductData(){
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getProduct_name() {
			return product_name;
		}

		public void setProduct_name(String product_name) {
			this.product_name = product_name;
		}

		public String getManufacturer() {
			return manufacturer;
		}

		public void setManufacturer(String manufacturer) {
			this.manufacturer = manufacturer;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		

	}

	class HeaderPosition {
		public static final String STATUS = "status";
		public static final String PRODUCT_NAME = "product_name";
		public static final String MANUFACTURER = "manufacturer";
		public static final String DESCRIPTION = "description";

		// the number of headers
		private int length = 0;

		private int status = -1;
		private int product_name = -1;
		private int manufacturer = -1;
		private int description = -1;

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public int getProduct_name() {
			return product_name;
		}

		public void setProduct_name(int product_name) {
			this.product_name = product_name;
		}

		public int getManufacturer() {
			return manufacturer;
		}

		public void setManufacturer(int manufacturer) {
			this.manufacturer = manufacturer;
		}

		public int getDescription() {
			return description;
		}

		public void setDescription(int description) {
			this.description = description;
		}

	}

}
