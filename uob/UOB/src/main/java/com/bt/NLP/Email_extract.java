package com.bt.NLP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email_extract {
	public static void main(String[] args) throws Exception{

		String input1="I am John, I have four different email ids.  john.d@example.com, john_d@example.com john-d@example.com My personal preference is to use johndoe@example.co.uk. If You want to reach by phone dial: +65 6889-7445 or (65) 6889-7445 or +6568897445. If You are from Singapore dial: 68897445.  please contact betweem 10 am to 6 pm. I am pleased to inform that I will be leaving Singapore on Nov 25, to join Flipkart, Bangalore, India.";
		System.out.println(Email_Extract(input1)); 
	
	}
	public static ArrayList<String> Email_Extract(String text) 
	{
	String s1="my id is 123@gmail.com";
	
	Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(text);
	 ArrayList<String> results = new ArrayList<String>();
	while (m.find()) 
	{
		//System.out.println(m.group().toString());
	
		results.add(m.group().toString());
	}
	return results;
	}

}
