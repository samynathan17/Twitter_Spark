package Nlp_Pipeline;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.POSTaggerAnnotator;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.TokenizerAnnotator;
import edu.stanford.nlp.pipeline.WordsToSentencesAnnotator;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeAnnotator;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.time.SUTime.Temporal;
import edu.stanford.nlp.util.CoreMap;

public class Nlp_Pipeline
{
	static StanfordCoreNLP pipeline;
	public static void init() 
	{
		pipeline = new StanfordCoreNLP("MyPropFile.properties");
	}

	public static List<String> hashtagextract(String str) throws IOException
	{

		Pattern MY_PATTERN = Pattern.compile("#(\\w+|\\W+)");
		Matcher mat = MY_PATTERN.matcher(str);
		List<String> hashtags=new ArrayList<String>();
		while (mat.find()) 
		{
			hashtags.add(mat.group(1));
		}
		return hashtags;
	}
	public static List<String> twitterusermentionsextract(String str) throws IOException
	{
		String patternStr = "(?:\\s|\\A)[@]+([A-Za-z0-9-_]+)";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(str);
		String result = "";
		List<String> usertags=new ArrayList<String>();
		while (matcher.find()) 
		{
			result = matcher.group();
			usertags.add((matcher.group()).replace("@", "https://twitter.com/"));
		}
		return usertags;
	}
	public static Map<String, Temporal> ExtractDate(String text){
		Map<String, Temporal> hm = new HashMap<String, Temporal>();
		List input=null;
		List results=null;
		Properties props = new Properties();
		AnnotationPipeline pipeline = new AnnotationPipeline();
		pipeline.addAnnotator(new TokenizerAnnotator(false));
		pipeline.addAnnotator(new WordsToSentencesAnnotator(false));
		pipeline.addAnnotator(new POSTaggerAnnotator(false));
		pipeline.addAnnotator(new TimeAnnotator("sutime", props));
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");		    
		String formatted = format1.format(cal.getTime());
		Annotation annotation = new Annotation(text);
		annotation.set(CoreAnnotations.DocDateAnnotation.class, formatted);
		pipeline.annotate(annotation);
		//System.out.println(annotation.get(CoreAnnotations.TextAnnotation.class));	
		List<CoreMap>timexAnnsAll=annotation.get(TimeAnnotations.TimexAnnotations.class);	
		for (CoreMap cm : timexAnnsAll) {
			List tokens = cm.get(CoreAnnotations.TokensAnnotation.class); 	         
			/* System.out.println(cm + " [from char offset " +
		        				 ((CoreMap) tokens.get(0)).get(CoreAnnotations.CharacterOffsetBeginAnnotation.class) +
		        				 " to " + ((CoreMap) tokens.get(tokens.size() - 1)).get(CoreAnnotations.CharacterOffsetEndAnnotation.class) + ']' +
		        				 " --> " + cm.get(TimeExpression.Annotation.class).getTemporal());*/

			hm.put(cm.toString(), cm.get(TimeExpression.Annotation.class).getTemporal());

		}

		return hm;

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


	public static ArrayList<String> findPhNum(String string2) {
		String line = string2.toLowerCase();
		line = line.replaceAll("[/?.^:,\"\']"," ");
		line = line.replaceAll(" ","  ");
		line = line.concat(" ");
		String regex = "(^|\\s)(-*\\s*\\(?-*\\s*(\\+\\(?|00)?65-*\\s*\\)?-*\\s*)?-*\\s*[986]-*\\s*\\d-*\\s*\\d-*\\s*\\d-*\\s*\\d-*\\s*\\d-*\\s*\\d-*\\s*\\d\\s";
		Set<String> set = new HashSet<String>();
		Matcher m = Pattern.compile(regex).matcher(line);  	
		while (m.find()) {
			set.add(m.group());
		}	
		ArrayList<String> results = new ArrayList<String>();
		Iterator iter = set.iterator();
		while (iter.hasNext()) {
			String newString = ((String) iter.next()).trim();
			results.add(newString);
		}
		return results;
	}	

}
