package com.bt.NLP;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.POSTaggerAnnotator;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.TokenizerAnnotator;
import edu.stanford.nlp.pipeline.WordsToSentencesAnnotator;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.time.SUTime.Temporal;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeAnnotator;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;

public class NLP_Pipeline {

	static StanfordCoreNLP pipeline;
	public static void init() 
	{
		pipeline = new StanfordCoreNLP("MyPropFile.properties");
	}

	public static String findNer(String line) throws ClassCastException, ClassNotFoundException, IOException {

		String serializedClassifier = "edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz";

		String output=null;
		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
		//	System.out.println("**** Named Entity Classifier Tagging ****");
		//	System.out.println(classifier.classifyToString(line));
		String xmlner=classifier.classifyWithInlineXML(line);
		//System.out.println(xmlner);


		return xmlner;
	}
	public static String removeDupWord(String input) {
		List<String> list = Arrays.asList(input.split(" "));
		LinkedHashSet<String> lhs = new LinkedHashSet<String>(list);

		return lhs.toString().replace(",", "").replace("[", "").replace("]", "").toString();                   
	}

	public static String findLemmas(String input) {

		String lemma=null;
		Annotation document = pipeline.process(input);  

		for(CoreMap sentence: document.get(SentencesAnnotation.class))
		{    
			for(CoreLabel token: sentence.get(TokensAnnotation.class))
			{       
				String word = token.get(TextAnnotation.class);      
				lemma = token.get(LemmaAnnotation.class); 
				//System.out.println("lemmatized version :" + lemma);
			}

		}

		return lemma;
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
	public static List<String> usermentionsextract(String str) throws IOException
	{
		String patternStr = "(?:\\s|\\A)[@]+([A-Za-z0-9-_]+)";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(str);
		String result = "";
		List<String> usertags=new ArrayList<String>();
		while (matcher.find()) 
		{
			result = matcher.group();
			usertags.add((matcher.group()));
		}
		return usertags;
	}
	public static List<String> facebookusermentionsextract(String str) throws IOException
	{
		String patternStr = "(?:\\s|\\A)[@]+([A-Za-z0-9-_]+)";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(str);
		String result = "";
		List<String> usertags=new ArrayList<String>();
		while (matcher.find()) 
		{
			result = matcher.group();
			usertags.add((matcher.group()).replace("@", "https://www.facebook.com/"));
		}
		return usertags;
	}
	public static List<String> instagramusermentionsextract(String str) throws IOException
	{
		String patternStr = "(?:\\s|\\A)[@]+([A-Za-z0-9-_]+)";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(str);
		String result = "";
		List<String> usertags=new ArrayList<String>();
		while (matcher.find()) 
		{
			result = matcher.group();
			usertags.add((matcher.group()).replace("@", "https://www.instagram.com/"));
		}
		return usertags;
	}

	public static String DBFY(String input) throws IOException
	{
		String dblink="http://52.76.15.236:2222/rest/annotate?text=";
		String encodedURL=java.net.URLEncoder.encode(input,"UTF-8");

		String url=dblink+""+encodedURL+"&confidence=0.0&support=0";
		org.jsoup.nodes.Document doc = Jsoup.parse(getsource(url), "UTF-8");
		Elements z1=doc.select("a");
		//	System.out.println(z1.toString());
		return z1.toString();
	}

	private static String getsource(String input1) throws IOException {
		URL url = new URL(input1);
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream(), "UTF-8"));

		String inputLine;
		StringBuilder a = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
			a.append(inputLine);
		in.close();

		return a.toString();
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

	public static void lexparser(LexicalizedParser lp, String filename) {
		TokenizerFactory<CoreLabel> tokenizerFactory =
				PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
		Tokenizer<CoreLabel> tok =
				tokenizerFactory.getTokenizer(new StringReader(filename));
		List<CoreLabel> rawWords2 = tok.tokenize();
		Tree parse = lp.apply(rawWords2);

		TreebankLanguagePack tlp = lp.treebankLanguagePack(); // PennTreebankLanguagePack for English
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
		List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
		//System.out.println(tdl);
		//System.out.println();

	}

	public static String findSentiment(String text) {

		int mainSentiment = 0;
		if (text != null && text.length() > 0) 
		{
			int longest = 0;
			Annotation annotation = pipeline.process(text);
			for (CoreMap sentence : annotation
					.get(CoreAnnotations.SentencesAnnotation.class))
			{
				Tree tree = sentence
						.get(SentimentCoreAnnotations.AnnotatedTree.class);
				int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
				String partText = sentence.toString();
				if (partText.length() > longest) 
				{
					mainSentiment = sentiment;
					longest = partText.length();
				}

			}
		}
		String sentiout = "neutral";
		if (mainSentiment==0)
		{
			sentiout= "Very Negative";
		}
		else if (mainSentiment==1)
		{
			sentiout="Negative";
		}else 
			if (mainSentiment==2)
			{
				sentiout="Neutral";
			}
			else 
				if (mainSentiment==3)
				{
					sentiout="Positive";
				}
				else 
					if (mainSentiment==4)
					{
						sentiout="Very Positive";
					}		    




		return sentiout;
	}
	public static String Taxonomy(String strTest) {
		String output="";
		try {
			String line = strTest.toLowerCase();
			Workbook wrk1 =  Workbook.getWorkbook(new File("/home/bigtapp/lifeeventsedited.xls"));
			Sheet sheet1 = wrk1.getSheet(0);
			ArrayList<String> a1= new ArrayList<String>();
			for (int j = 1; j < sheet1.getRows(); j++) {
				for (int i = 1; i < sheet1.getColumns(); i++) {

					Cell cell = sheet1.getCell(i, j);
					String s1=(String)cell.getContents();
					s1= s1.toLowerCase();
					if(s1!="")
					{

						if (line.contains(s1)){

							if(i==2){
								Cell cell1 = sheet1.getCell(i-1, j);
								String s2=(String)cell1.getContents();
								s2= s2.toLowerCase();
								Cell cell2 = sheet1.getCell(0,j);
								String s3=(String)cell2.getContents();
								s3= s3.toLowerCase();
								Cell cell3 = sheet1.getCell(i,0);
								String s4=(String)cell3.getContents();
								s4= s4.toLowerCase();
								output=s1+":"+s3+":"+s4+":";
								//	System.out.println(s1+"**"+s3+"**"+s4);
							}
							else
							{
								Cell cell1 = sheet1.getCell(i+1, j);
								String s2=(String)cell1.getContents();
								s2= s2.toLowerCase();
								Cell cell2 = sheet1.getCell(0,j);
								String s3=(String)cell2.getContents();
								s3= s3.toLowerCase();
								Cell cell3 = sheet1.getCell(i,0);
								String s4=(String)cell3.getContents();
								s4= s4.toLowerCase();
								output=s1+":"+s3+":"+s4+":";
								//System.out.println(s1+"**"+s3+"**"+s4);

							}
						}
					}
				}
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}
}










