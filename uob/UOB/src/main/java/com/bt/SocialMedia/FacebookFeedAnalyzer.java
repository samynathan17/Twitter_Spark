package com.bt.SocialMedia;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FacebookFeedAnalyzer {
	

	private static final String filePath = "/home/pradeep/fbon.json";

	public static void main(String[] args) {

		try {
			FileReader reader = new FileReader(filePath);

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
			JSONArray lang= (JSONArray) jsonObject.get("data");
			Iterator i = lang.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				String userdetails= ""+innerObj.get("from");
				String[] f1=userdetails.split(",");
				String f2=f1[0].replace("id", "");
				String f3=f2.replaceAll("[^a-zA-Z0-9_-]", "");
				String p1= ""+innerObj.get("id");
				String postid=p1.replace("136818983856_", "");//mention page id here

				System.out.println("Comment #  "+ "" +innerObj.get("message") + 
						" # post ID # "+ postid +" # userid # "+ f3);
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ParseException ex) {
			ex.printStackTrace();
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}

	}

}







