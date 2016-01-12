package com.bt.NLP;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class Sentiment_Wordnet {
	private Map<String, Double> dictionary;

	public Sentiment_Wordnet(String pathToSWN) throws IOException {

	  dictionary = new HashMap<String, Double>();
	  HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();
	  BufferedReader csv = null;
	  
	  try {
	    csv = new BufferedReader(new FileReader(pathToSWN));
	    int lineNumber = 0;
	    String line;
	    while ((line = csv.readLine()) != null) {
		lineNumber++;		
		if (!line.trim().startsWith("#")) {
		  String[] data = line.split("\t");
		  String wordTypeMarker = data[0];
		  if (data.length != 6) {
		    throw new IllegalArgumentException(
						       "Incorrect tabulation format in file, line: "
						       + lineNumber);
		  }
		
		  Double synsetScore = Double.parseDouble(data[2])
		    - Double.parseDouble(data[3]);
		
		  String[] synTermsSplit = data[4].split(" ");
	
		  for (String synTermSplit : synTermsSplit) {
		    String[] synTermAndRank = synTermSplit.split("#");
		    String synTerm = synTermAndRank[0] + "#"
		      + wordTypeMarker;
	
		    int synTermRank = Integer.parseInt(synTermAndRank[1]);
		    if (!tempDictionary.containsKey(synTerm)) {
		      tempDictionary.put(synTerm,
					 new HashMap<Integer, Double>());
		    }

		    tempDictionary.get(synTerm).put(synTermRank,
						    synsetScore);
		  }
		}
	    }

	    for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary
		     .entrySet()) {
		String word = entry.getKey();
		Map<Integer, Double> synSetScoreMap = entry.getValue();
		double score = 0.0;
		double sum = 0.0;
		for (Map.Entry<Integer, Double> setScore : synSetScoreMap
		       .entrySet()) {
		  score += setScore.getValue() / (double) setScore.getKey();
		  sum += 1.0 / (double) setScore.getKey();
		}
		score /= sum;

		dictionary.put(word, score);
	    }
	  } catch (Exception e) {
	    e.printStackTrace();
	  } finally {
	    if (csv != null) {
		csv.close();
	    }
	  }
	}

	public double extract(String word, String pos) {
	  return dictionary.get(word + "#" + pos);
	}
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private static String findSentiment(String line) throws IOException {
		String pathToSWN = "/home/pradeep/Documents/SentiWordNet_3.0.0_20130122.txt";
		Sentiment_Wordnet sentiwordnet = new Sentiment_Wordnet(pathToSWN);
		  String sentiment=null;
		try{
			  List results =lemmatizer.main(line); 
				
			  
			Iterator<List> iterator = results.iterator();
			         List words = iterator.next();
			         List tags = iterator.next();
			        Double total=0.0;
			        int count=0;
			        List<Double> scores=null;
			        for(int i=0; i< words.size();i++)
			        {
			        	String temp=(String) tags.get(i);
			        	if(temp.startsWith("V"))
			        	{
			        		count+=1;
			        		String tempword=(String) words.get(i);
			        		try{
			        			Double scr= sentiwordnet.extract(tempword,"v");
			        			total=total+scr;
			        		}
			        		catch(NullPointerException e)
			                {                   
			                }
			        	}
			        	if(temp.startsWith("N"))
			        	{
			        		count+=1;
			        		String tempword=(String) words.get(i);
			        		try{
			        			Double scr= sentiwordnet.extract(tempword,"n");
			        			total=total+scr;
			        		 }
			        		catch(NullPointerException e)
			                {
			                }
			        	}
			        	if(temp.startsWith("J"))
			        	{
			        		count+=1;
			        		String tempword=(String) words.get(i);
			        		try{
			        			Double scr= sentiwordnet.extract(tempword,"a");
			        			total=total+scr;
			        		 }
			        		catch(NullPointerException e)
			                {
			                }
			        	}
			        	if(temp.startsWith("A"))
			        	{
			        		count+=1;
			        		String tempword=(String) words.get(i);
			        		try{
			        			Double scr= sentiwordnet.extract(tempword,"r");
			        			total=total+scr;
			        		}
			        		catch(NullPointerException e)
			                {
			                }
			        	}
			        }
				
			        Double average=total/count;
			       DecimalFormat df = new DecimalFormat("#.##");   
					  average = Double.valueOf(df.format(average));
			       if (average>0&&average<=0.5)
			       {
			    	   sentiment="Positive Sentiment";
			       }
			       else if (average==0)
			       {
			    	   sentiment="Neutral Sentiment";
			       }
			       else if(average>0.5)
			       {
			    	   sentiment="Highly Positive Sentiment";
			       }
			       else if(average<1&&average>=-0.5)
			       {
			    	   sentiment="Negative Sentiment";
			       }
			       else if(average<-0.5)
			       {
			    	   sentiment="Highly Negative Sentiment";
			       }
			       else
			       {
			    	   sentiment="Neutral Sentiment";
			       }
				}catch(Exception ex) {
				       sentiment= "Neutral Sentiment";
				   }
		return sentiment;
	}
	@SuppressWarnings("resource")
	public static void main(String [] args) throws IOException, RowsExceededException, WriteException {
	  
      Scanner in = new Scanner(System.in);
      System.out.println("Enter the text: ");
		String line=in.nextLine();
		String output = findSentiment(line);
		System.out.println(output);
		
	}

}