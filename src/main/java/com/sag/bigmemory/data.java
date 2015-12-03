package com.sag.bigmemory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class data {

	public static void main(String[] args){
	String filename =  "hcoprovider.csv";	

		try {
			BufferedReader in = new BufferedReader(new FileReader((new File(
					ClassLoader.getSystemResource(filename).toURI()))));
			if (in == null) {
				System.out.println("Unable to read CSV  File. " + filename);
				System.exit(1);
			}
		
			
			String line = null;
			int tranid = 1;

			while ((line = in.readLine()) != null) {
				String fields[] = line.split(",");
				String result = fields[2]+","+fields[3]+fields[4]+fields[6]+fields[8]+fields[17]+fields[21]+fields[22]+fields[24]+fields[25]+fields[26];
					System.out.println(result);
				
				}
				
			
			in.close();
		} catch (Exception ex) {
			System.out.println(ex);
			ex.printStackTrace();
		}
		System.out.println("Completed  loading data to cache");
		
		
		
		
	}
	
	
	
}
