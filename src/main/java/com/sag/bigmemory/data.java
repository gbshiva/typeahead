package com.sag.bigmemory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
		
			
			File file = new File("/tmp/hcohcp.csv");

			  if (!file.exists()) {
			     file.createNewFile();
			  }

			  FileWriter fw = new FileWriter(file);
			  BufferedWriter bw = new BufferedWriter(fw);
			  
			String[] alpha = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
			String line = null;
			int tranid = 1;

			while ((line = in.readLine()) != null) {
				String fields[] = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				
				if (fields.length >= 11){
					for (int i=0; i <alpha.length;i++){
//						String result = fields[0]+","+alpha[i]+fields[3]+","+alpha[i]+fields[4]+","+fields[6]+","+fields[8]+","+fields[17]+","+fields[21]+","+fields[22]+","+fields[24]+","+fields[25]+","+fields[26]+"\n";
						String result = fields[0]+","+alpha[i]+fields[1]+","+alpha[i]+fields[2]+","+fields[3]+","+fields[4]+","+fields[5]+","+fields[6]+","+fields[7]+","+fields[8]+","+fields[9]+","+fields[10]+"\n";
						System.out.println(result);
						bw.write(result);
					}
				
				
				
				}
				
				
				
				
				
			}

			in.close();
			
			in = new BufferedReader(new FileReader((new File(
					ClassLoader.getSystemResource(filename).toURI()))));

			while ((line = in.readLine()) != null) {
				String fields[] = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				if (fields.length > 25){
				String result = fields[1]+","+fields[4]+","+fields[3]+","+fields[6]+","+fields[8]+","+fields[17]+","+fields[21]+","+fields[22]+","+fields[24]+","+fields[25]+","+fields[26]+"\n";
					System.out.println(result);
					bw.write(result);
				
				}
			}
			in.close();
		} catch (Exception ex) {
			System.out.println(ex);
			ex.printStackTrace();
		}
		System.out.println("Completed  loading data to cache");
		
		
		
		
	}
	
	
	
}
