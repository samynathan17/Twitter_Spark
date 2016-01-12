package com.bt.SocialMedia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class FaceboolCrawl {
	public static void main(String[] args) throws Exception {
		//PrintStream out = new PrintStream(new FileOutputStream("/home/pradeep/facebook_crawl/feedoutput.json"));
		//	System.setOut(out);
		String base_url="https://graph.facebook.com/";
		String like_page_id="108329362587002";
		String feed_limit="100";
		String access_token="CAACEdEose0cBAME9d3NQCbx5l6oXZBXfZBX7Lz0PhhmbsZBBXp2HboMQsgfZC6uULC08CJuIbUHqE5VDzOiWiPBLXZCBObS1d3AVphCylZCjsTMpFDov4ZBsh2bieUZCl17TwpAnC7ZCooa22ojxDFwjAbITnuUbIoZBG3aPC3VsjHJCYpoIIEZBndElp3RZBoh99hKN7tw7CDZC9ZCgZDZD";
		String get_url=base_url+""+like_page_id+"?fields=feed.limit("+feed_limit+")&access_token="+access_token;
		System.out.println(get_url);
		URL facebook = new URL(get_url);
		BufferedReader in = new BufferedReader(new InputStreamReader(facebook.openStream()));
		String inputLine;
		File output_json = new File("/home/pradeep/Documents/check1.json");

		FileWriter fw = new FileWriter(output_json.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		//JsonElement je = jp.parse(uglyJSONString);
		//String prettyJsonString = gson.toJson(je);

		if (!output_json.exists()) {
			output_json.createNewFile();
		}
		while ((inputLine = in.readLine()) != null)
		{
			JsonElement je = jp.parse(inputLine);
			String prettyJsonString = gson.toJson(je);
			bw.write(gson.toJson(prettyJsonString).toString());
			System.out.println(inputLine);

		}
		in.close();
		bw.close();

	}

}
