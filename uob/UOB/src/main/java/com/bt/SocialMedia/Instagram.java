package com.bt.SocialMedia;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class Instagram
{
	static String temp=null;
	public static void main(String[]args) throws IOException, ParseException, RowsExceededException, WriteException 
	{
		
		WritableWorkbook wworkbook;
		wworkbook = Workbook.createWorkbook(new File("/home/pradeep/Desktop/instagramoutput.xls"));
		WritableSheet wsheet = wworkbook.createSheet("Instagram_data", 0);


		JSONParser parser = new JSONParser();
		String base_url= "https://api.instagram.com/v1/";
		String searchtype = "media/search?";
		String latitude="1.3000";
		String longitude="103.8000";
		String distance = "5000";
		String count="100";
		String access_token="2206980605.1fb234f.82cd96b751324e87942cd3f84d8957ef";

		String get_url=base_url+""+searchtype+""+"access_token="+access_token+"&lat="+latitude+"&lng="+longitude+"&distance="+distance+"&count="+count;
		//BufferedReader reader = new BufferedReader(new InputStreamReader(((HttpURLConnection) (new URL(urlString)).openConnection()).getInputStream(), Charset.forName("UTF-8")));
		System.out.println(get_url);

		Object obj = parser.parse(new StringReader(getsource(get_url).toString()));

		String jsonout=getsource(get_url).replace("{\"meta\":{\"code\":200},\"data\":[", "").replace("}}]}", "}}");
		//System.out.println(jsonout);
		FileWriter file = new FileWriter("file1.json");
		file.write(getsource(get_url).replace("{\"meta\":{\"code\":200},\"data\":[", "").replace("}}]}", "}}"));
		file.flush();
		file.close();
		String content = new String(Files.readAllBytes(Paths.get("file1.json")));

		JSONObject jsonObject = (JSONObject) obj;
		JSONArray lang= (JSONArray) jsonObject.get("data");
		Iterator i = lang.iterator();
		int p2=0;
		
		while (i.hasNext()) 
		{
			int p1=0;
		
	
			JSONObject innerObj = (JSONObject) i.next();
			/*	System.out.println("tags"+ innerObj.get("t"));
			Label a=new Label(p1,p2," "+innerObj.get("t").toString());
			wsheet.addCell(a);
			p1++;	
			
			System.out.println("type"+ innerObj.get("type"));
			Label b=new Label(p1,p2," "+innerObj.get("type").toString());
			wsheet.addCell(b);
			p1++;*/
			
			System.out.println("location:");
			JSONObject jobj= (JSONObject) innerObj.get("location");
			
			for (Object key : jobj.keySet()) 
			{
				String keyStr = (String)key;
				Object keyvalue = jobj.get(keyStr);				
				System.out.println("\t"+keyStr + ": " + keyvalue);
				Label c=new Label(p1,p2," "+keyvalue.toString());
				wsheet.addCell(c);
				p1++;
			}
			
			System.out.println("Comments:");
			JSONObject jobj1= (JSONObject) innerObj.get("comments");
			for (Object key : jobj1.keySet()) 
			{
				String keyStr = (String)key;
				Object keyvalue = jobj1.get(keyStr);
				System.out.println("\t"+keyStr + ": " + keyvalue);
				Label d=new Label(p1,p2," "+keyvalue.toString());
				wsheet.addCell(d);
				p1++;


			}

			System.out.println("filter "+ innerObj.get("filter"));
			Label e=new Label(p1,p2," "+innerObj.get("filter").toString());
			wsheet.addCell(e);
			p1++;
			
			System.out.println("created_time "+ innerObj.get("created_time"));
			Label f=new Label(p1,p2," "+innerObj.get("created_time").toString());
			wsheet.addCell(f);
			p1++;
			
			System.out.println("link "+ innerObj.get("link"));
			
			Label g=new Label(p1,p2," "+innerObj.get("link").toString());
			wsheet.addCell(g);
			p1++;
			
			System.out.println("Likes");
			JSONObject jobj2= (JSONObject) innerObj.get("likes");
			
			Label h=new Label(p1,p2," "+innerObj.get("likes").toString());
			wsheet.addCell(h);
			p1++;
			
			for (Object key : jobj2.keySet()) 
			{
				String keyStr = (String)key;
				Object keyvalue = jobj2.get(keyStr);
				System.out.println("\t"+keyStr + ": " + keyvalue);
				JSONObject jobj6= (JSONObject) innerObj.get("url");
			//	Label i1=new Label(p1,p2," "+innerObj.get("url").toString());
				//wsheet.addCell(i1);
				//p1++;
				

				System.out.println("Images");
				System.out.println("images"+ innerObj.get("images"));
				JSONObject jobj3= (JSONObject) innerObj.get("images");
				Label j1=new Label(p1,p2," "+innerObj.get("images").toString());
				wsheet.addCell(j1);
				p1++;
				

				System.out.println("users_in_photo "+ innerObj.get("users_in_photo"));
				Label k=new Label(p1,p2," "+innerObj.get("users_in_photo").toString());
				wsheet.addCell(k);
				p1++;
				
				System.out.println("caption:");
				JSONObject jobj4= (JSONObject) innerObj.get("caption");
				
			//	Label l=new Label(p1,p2," "+innerObj.get("caption").toString());
			//	wsheet.addCell(l);
			//	p1++;
				
				System.out.println("user_has_liked:"+ innerObj.get("user_has_liked"));
				Label m=new Label(p1,p2," "+innerObj.get("user_has_liked").toString());
				wsheet.addCell(m);
				p1++;
				
				System.out.println("id: "+ innerObj.get("id"));
				Label n=new Label(p1,p2," "+innerObj.get("id").toString());
				wsheet.addCell(n);
				p1++;
				
				
				System.out.println("user: "+ innerObj.get("user"));
				
				Label o=new Label(p1,p2," "+innerObj.get("user").toString());
				wsheet.addCell(o);
				p1++;
				

				System.out.println("");
				temp=(String) innerObj.get("id");
				
			}
		p2++;
		}
	
		wworkbook.write();
		wworkbook.close();
	}

	private static String getsource(String input1) throws IOException 
	{
		URL url = new URL(input1);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		InputStream inputStream = url.openStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(((HttpURLConnection) (new URL(input1)).openConnection()).getInputStream(), Charset.forName("UTF-8")));
		String inputLine;
		StringBuilder a = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
			a.append(inputLine);
		in.close();
		return a.toString();

	}
}
