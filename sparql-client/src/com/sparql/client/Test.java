package com.sparql.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = "";
		String queryString="";
		while ((line = br.readLine()) != null) {
			//System.out.println(line);
			queryString += line;
			if(line.endsWith("}")){
				break;
			}
		}
		System.out.println(queryString);
	}

}
