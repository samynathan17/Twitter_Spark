package com.bt.NonSocialMedia;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.time.SUTime.Temporal;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;

import com.bt.NLP.NLP_Pipeline;


public class Gigablast_parser {
	static StanfordCoreNLP pipeline;

	public static void init() {
		pipeline = new StanfordCoreNLP("MyPropFile.properties");
	}
	public static void main(String[] args) throws Exception{
		NLP_Pipeline.init();

		String serializedClassifier = "/home/pradeep/txttordf/stanford-ner-2014-08-27/classifiers/english.all.3class.distsim.crf.ser.gz";
		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
		String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
		if (args.length > 0) {
			parserModel = args[0];
		}
		LexicalizedParser lp = LexicalizedParser.loadModel(parserModel);
		WritableWorkbook wworkbook;
		wworkbook = Workbook.createWorkbook(new File("/home/pradeep/Desktop/output.xls"));
		WritableSheet wsheet = wworkbook.createSheet("Forum_data", 0);		
		String content = new String(Files.readAllBytes(Paths.get("/home/pradeep/Documents/nathankeywords.txt"))); 
		System.out.println(content);
		String[] querywords=content.split("\n");
		int j=0;
		int ex1=0;

		do{

			int i=0;
			String query=querywords[j].toString();


			String input_link="http://52.76.26.24:8000/search?format=xml&c=Forums&q="+ URLEncoder.encode(query, "UTF-8")+"&n=20";
			System.out.println(input_link);
			String htmlget=getsource(input_link);
			org.jsoup.nodes.Document doc = Jsoup.parse(htmlget, "UTF-8");
			Elements Result=doc.select("result");
			Elements URLdata=Result.select("url");
			String URLResult=URLdata.toString().replaceAll("<url>","").replaceAll("</url>","").replaceAll("(?m)^[ \t]*\r?\n", "");
			String[] IndividualURL=URLResult.split("\n");
			Elements textdata=doc.select("sum");
			//	System.out.println(textdata);
			String TextResult=textdata.toString().replaceAll("<sum>"+"\n"+"</sum>","<sum>\nNO SUMMARY DATA AVAILABLE FOR PARSING\n</sum>").replaceAll("<sum>","").replaceAll("</sum>","").replaceAll("(?m)^[ \t]*\r?\n", "");
			String[] Resulttext=TextResult.split("\n");
			Elements htmlcontent= Result.select("docId");
			String htmlstring[]=htmlcontent.text().split(" ");
			System.out.println(TextResult);
			System.out.println(URLResult);
			System.out.println(Resulttext.length+"   "+IndividualURL.length);

			do{
				Label excel_keyword = new Label(0, ex1, query);
				wsheet.addCell(excel_keyword);

				Label excel_querylink = new Label(1, ex1,input_link );
				wsheet.addCell(excel_querylink);

				System.out.println(i);
				//System.out.println(Resulttext[i]);
				String post_url="http://"+IndividualURL[i].replace(" ","");
				System.out.println(post_url);
				Label excel_posturl = new Label(2, ex1,post_url);
				wsheet.addCell(excel_posturl);
				org.jsoup.nodes.Document doc2 = Jsoup.parse(getsource("http://52.76.26.24:8000/get?c=Forums&d="+htmlstring[i]), "UTF-8");
				System.out.println("http://52.76.26.24:8000/get?c=main&d="+htmlstring[i]);
				URL myUrl = new URL("http://"+IndividualURL[i].replace(" ",""));
				String Domain_name=myUrl.getHost().toString();
				Label excel_domain = new Label(3, ex1,Domain_name);
				Label excel_docid = new Label(4, ex1,htmlstring[i]);
				wsheet.addCell(excel_docid);
				wsheet.addCell(excel_domain);
				System.out.println(Domain_name);
				if(Resulttext[i]!=null && !Resulttext[i].isEmpty())
				{
					if (Resulttext[i].contains(" "));
					{
						String finder[]=Resulttext[i].replace("... ","").split(" ");
						if(finder.length>2)
						{
							System.out.println(Resulttext[i]);
							String finderstring=finder[1]+" "+finder[2];//+" "+finder[3];
							System.out.println(finderstring.replace(" Discussion","").replace("&amp;",""));
							Elements classname=doc2.getElementsContainingOwnText(finderstring);
							System.out.println(classname.text());
							Label excel_postdata = new Label(5, ex1,classname.text());
							wsheet.addCell(excel_postdata);
							String lexical_parser_input=classname.text().replace("...",".").replace("..","").replace(". .",". ");
							String Taxonomy_out="";
							//date extraction
							List<Date> dates = new PrettyTimeParser().parse(lexical_parser_input);
							System.out.println("dates:"+dates.toString());
							String Post_date=dates.toString();

							if(lexical_parser_input!=null && !lexical_parser_input.isEmpty())
							{
								//add pipeline here
								//	lexparser(lp,lexical_parser_input);
								//	NER(lexical_parser_input,classifier);


								//dbfy
								if(lexical_parser_input.length()<500)
								{
								System.out.println("**** DBPedia Tagging ****");
								System.out.println(NLP_Pipeline.DBFY(lexical_parser_input));
								Label excel_dbfy = new Label(9, ex1,NLP_Pipeline.DBFY(lexical_parser_input));
								wsheet.addCell(excel_dbfy);
								}
								//ner
								System.out.println("**** NER Tagging ****");
								System.out.println(NLP_Pipeline.findNer(lexical_parser_input));

								org.jsoup.nodes.Document doc1 = Jsoup.parse(NLP_Pipeline.findNer(lexical_parser_input));
								System.out.println("\n Entities Listed Below: \n");
								Elements e1=doc1.getElementsByTag("LOCATION");
								Elements e2=doc1.getElementsByTag("ORGANIZATION");
								Elements e3=doc1.getElementsByTag("PERSON");
								Elements e4=doc1.getElementsByTag("MONEY");
								Elements e5=doc1.getElementsByTag("PERCENT");
								Elements e6=doc1.getElementsByTag("DATE");
								Label excel_ner = new Label(10, ex1," Location: "+e1.text()+" "+" Organization: "+e2.text()+" Person: "+e3.text()+" Money: "+e4.text()+" Percent: "+e5.text()+" Date: "+e6.text());
								wsheet.addCell(excel_ner);

								System.out.println("**** Phone Number Extraction ****");
								// phone number extractor
								ArrayList<String> results = NLP_Pipeline.findPhNum(lexical_parser_input);
								System.out.println(results); 
								Label excel_phn = new Label(11, ex1,NLP_Pipeline.findPhNum(lexical_parser_input).toString());
								wsheet.addCell(excel_phn);
								// email extractor 
								System.out.println("**** Email Extraction ****");
								System.out.println(NLP_Pipeline.Email_Extract(lexical_parser_input)); 
								Label excel_email = new Label(12, ex1,NLP_Pipeline.Email_Extract(lexical_parser_input).toString());
								wsheet.addCell(excel_email);
								// date extractor


								System.out.println("**** Date Extraction ****");

								Map<String, Temporal> results1 = NLP_Pipeline.ExtractDate(lexical_parser_input);
								ArrayList<String> phnos = new ArrayList<String>();

								for (String key : results1.keySet())
								{
									System.out.println(key + ": " + results1.get(key));
									phnos.add(results1.get(key).toString());

								}
								Label excel_date = new Label(13, ex1,phnos.toString());
								wsheet.addCell(excel_date);	
								//taxonomy based event extraction
								System.out.println(Taxonomy(lexical_parser_input));
								Taxonomy_out=(Taxonomy(lexical_parser_input)).toString();

								// Sentiment Tagging'
								System.out.println("**** Sentiment Extraction ****");

								if (NLP_Pipeline.findSentiment(lexical_parser_input)==0)
								{
									System.out.println("Very Negative");
									Label excel_sentiment = new Label(14, ex1,"Very Negative");
									wsheet.addCell(excel_sentiment);	

								}
								else if (NLP_Pipeline.findSentiment(lexical_parser_input)==1)
								{
									System.out.println("Negative");
									Label excel_sentiment = new Label(14, ex1,"Negative");
									wsheet.addCell(excel_sentiment);	
								}else 
									if (NLP_Pipeline.findSentiment(lexical_parser_input)==2)
									{
										System.out.println("Neutral");
										Label excel_sentiment = new Label(14, ex1,"Neutral");
										wsheet.addCell(excel_sentiment);	
									}
									else 
										if (NLP_Pipeline.findSentiment(lexical_parser_input)==3)
										{
											System.out.println("Positive");
											Label excel_sentiment = new Label(14, ex1,"Positive");
											wsheet.addCell(excel_sentiment);	
										}
										else 
											if (NLP_Pipeline.findSentiment(lexical_parser_input)==4)
											{
												System.out.println("Very Positive");
												Label excel_sentiment = new Label(14, ex1,"Very Positive");
												wsheet.addCell(excel_sentiment);	
											}		    


							}
							System.out.println( Taxonomy_out);

							String[] Taxonomy_field = Taxonomy_out.split(":");
							System.out.println(Taxonomy_field.length);
							if(Taxonomy_field.length>2)
							{
								System.out.println(Taxonomy_field[0]+""+Taxonomy_field[1]+""+Taxonomy_field[2]);
								Label Taxonomy_keyword = new Label(6, ex1,Taxonomy_field[0]);
								wsheet.addCell(Taxonomy_keyword);
								Label Taxonomy_lifestage = new Label(7, ex1,Taxonomy_field[1]);
								wsheet.addCell(Taxonomy_lifestage);
								Label Taxonomy_impact = new Label(8, ex1,Taxonomy_field[2]);
								wsheet.addCell(Taxonomy_impact);
							}
							if(Domain_name.equals("singaporemotherhood.com"))
							{

								try
								{
									Elements userclass=classname.parents();
									org.jsoup.nodes.Document doc3 = Jsoup.parse(userclass.toString(), "UTF-8");
									Element userdata=doc3.getElementsByClass("authorEnd").first();
									String user_href= userdata.toString().substring(userdata.toString().indexOf("href"),userdata.toString().indexOf("\" c")).replace("href=\"","");
									String user_link="http://singaporemotherhood.com/forum/"+user_href;
									String user_name=userdata.text();
									Label excel_userlink = new Label(15, ex1,user_link);
									wsheet.addCell(excel_userlink);
									Label excel_username = new Label(16, ex1,user_name);
									wsheet.addCell(excel_username);
									System.out.println(user_link);
									System.out.println(user_name);
								}
								catch(Exception e)
								{

								}
							}
							else

								if(Domain_name.equals("www.renotalk.com"))
								{
									try
									{
										Elements userclass=classname.parents();
										org.jsoup.nodes.Document doc3 = Jsoup.parse(userclass.toString(), "UTF-8");
										Element userdata=doc3.getElementsByClass("author_info").first();
										System.out.println(userdata.text());
										String[] userdetails=userdata.text().split(" ");
										String userdetail_1=userdetails[0];
										String userdetail_2=userdetails[1];
										String userdetail_3=userdetails[2];
										String userdetail_4=userdetails[3];
										String userdetail_5=userdetails[4];
										Label excel_user1 = new Label(15, ex1,userdetails[0]);
										wsheet.addCell(excel_user1);
										Label excel_user2 = new Label(16, ex1,userdetails[1]);
										wsheet.addCell(excel_user2);
										Label excel_user3 = new Label(17, ex1,userdetails[2]);
										wsheet.addCell(excel_user3);
										Label excel_user4 = new Label(18, ex1,userdetails[3]);
										wsheet.addCell(excel_user4);
										Label excel_user5 = new Label(19, ex1,userdetails[4]);
										wsheet.addCell(excel_user5);
										System.out.println(userdetail_1+" "+userdetail_2+" "+userdetail_3+" "+userdetail_4+" "+userdetail_5);

									}
									catch(Exception e)
									{

									}
								}


								else

									if(Domain_name.equals("www.towkayzone.com.sg"))
									{
										try
										{
											Elements userclass=classname.parents();
											org.jsoup.nodes.Document doc3 = Jsoup.parse(userclass.toString().replace("username offline popupctrl", "Towuser"), "UTF-8");
											Element userdata=doc3.getElementsByClass("Towuser").first();
											System.out.println(userdata.text());
											Elements userdata1=doc3.getElementsByClass("TowUser");
											Elements userdata2=doc3.getElementsByClass("userinfo_extra");
											System.out.println("usertitle is: " + userdata.text());  
											System.out.println("username is: " + userdata1.text());  

											System.out.println( userdata2.text());
											Label excel_user2 = new Label(15, ex1,userdata1.text());
											wsheet.addCell(excel_user2);
											Label excel_user3 = new Label(16, ex1,userdata.text());
											wsheet.addCell(excel_user3);

										}
										catch(Exception e)
										{

										}

									}
									else if(Domain_name.equals("forums.asiaone.com"))
									{
										try{

											Elements userclass=classname.parents();
											org.jsoup.nodes.Document doc3 = Jsoup.parse(userclass.toString(), "UTF-8");
											Elements userdata=doc3.getElementsByClass("bigusername");
											Elements userdata1=doc3.getElementsByClass("bigusername");

											System.out.println(userdata.text());
											System.out.println(userdata1.text());
											Label excel_user2 = new Label(15, ex1,userdata1.text());
											wsheet.addCell(excel_user2);


										}

										catch(Exception e)
										{

										}
									}
									else if(Domain_name.equals("forums.hardwarezone.com.sg"))
									{
										try{


											Elements userclass=classname.parents();
											org.jsoup.nodes.Document doc3 = Jsoup.parse(userclass.toString(), "UTF-8");
											Element userdata1=doc3.getElementsByClass("alt2").first() ; 
											Element z1=userdata1.select("div").first(); 
											System.out.println(z1.select("a").toString().substring(z1.select("a").toString().indexOf("\"http"),z1.select("a").toString().indexOf("/\">")).replace("\"",""));  
											System.out.println(z1.select("a").text());   
											Label excel_user1 = new Label(15, ex1,z1.select("a").toString().substring(z1.select("a").toString().indexOf("\"http"),z1.select("a").toString().indexOf("/\">")).replace("\"",""));
											wsheet.addCell(excel_user1);
											Label excel_user2 = new Label(16, ex1,z1.select("a").text());
											wsheet.addCell(excel_user2);


										}
										catch(Exception e)
										{

										}
									}
									else if(Domain_name.equals("www.mycarforum.com"))
									{
										try{
											Elements userclass=classname.parents();
											org.jsoup.nodes.Document doc3 = Jsoup.parse(userclass.toString().replace("custom_author_info", "myCar"), "UTF-8");
											Elements userdata1=doc3.getElementsByClass("myCar");
											System.out.println("username is: " + userdata1.text());
											Label excel_user2 = new Label(15, ex1,userdata1.text());
											wsheet.addCell(excel_user2);

										}
										catch(Exception e)
										{

										}
									}	}
					}

				}
				ex1++;
				i++;
			}while(i<(Resulttext.length)-1); 


			j++;

		}while(j<20);
		wworkbook.write();
		wworkbook.close();

	}

	public static void NER(String input,AbstractSequenceClassifier classifier) throws ClassCastException, ClassNotFoundException, IOException
	{

		int count=0;
		String[] allMatches = new String[2];
		Matcher m1 = Pattern.compile("(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d").matcher(input);
		while (m1.find()) {
			allMatches[count] = m1.group();
			System.out.println("Dates:"+allMatches);
			count++;
		}
		//System.out.println("Dates:"+allMatches);	
		System.out.println("**** Named Entity Classifier Tagging ****");
		System.out.println(classifier.classifyToString(input));
		String xmlner=classifier.classifyWithInlineXML(input);
		org.jsoup.nodes.Document doc1 = Jsoup.parse(xmlner);
		System.out.println("\n Entities Listed Below: \n");
		Elements e1=doc1.getElementsByTag("LOCATION");
		Elements e2=doc1.getElementsByTag("ORGANIZATION");
		Elements e3=doc1.getElementsByTag("PERSON");
		Elements e4=doc1.getElementsByTag("MONEY");
		Elements e5=doc1.getElementsByTag("PERCENT");
		Elements e6=doc1.getElementsByTag("DATE");

		String loc[] =e1.toString().split("</location>");
		String org[] =e2.toString().split("</organization>");
		String Person[]=e3.toString().split("</person>");
		String Money[]=e4.toString().split("</money>");
		String Percent[]=e5.toString().split("</percent>");
		String date[]=e6.toString().split("</date>");
		int i=0,j=0,k=0,l=0,m=0,n=0;

		System.out.println("Location:");
		do
		{

			System.out.println(loc[i].replaceAll("\\<.*?>","").replace("\n",""));
			i++;
		}while(i<loc.length);
		System.out.println("--------------------");
		System.out.println("Organization:");
		do
		{


			System.out.println(org[j].replaceAll("\\<.*?>","").replace("\n",""));
			j++;
		}while(j<org.length);
		System.out.println("--------------------");
		System.out.println("Person:");
		do
		{

			System.out.println(Person[k].replaceAll("\\<.*?>","").replace("\n",""));
			k++;
		}while(k<Person.length);
		System.out.println("--------------------");
		System.out.println("Money:");
		do
		{ 
			System.out.println(Money[l].replaceAll("\\<.*?>","").replace("\n",""));
			l++;
		}while(l<Money.length);
		System.out.println("--------------------");
		System.out.println("Percent:");
		do
		{
			System.out.println(Percent[m].replaceAll("\\<.*?>","").replace("\n",""));
			m++;
		}while(m<Percent.length);
		System.out.println("--------------------");


		System.out.println("Date:");
		do
		{ 

			System.out.println(date[n].replaceAll("\\<.*?>","").replace("\n",""));
			n++;
		}while(n<date.length);
	}
	public static void lexparser(LexicalizedParser lp, String filename) {
		int a=0;
		String[] lexin=filename.split("[.]|[!?]+|[。]|[！？]+");
		for(a=0;a<lexin.length;a++)
		{
			//	System.out.println(lexin[a]);
			if(lexin[a]!=null && !lexin[a].isEmpty())
			{
				TokenizerFactory<CoreLabel> tokenizerFactory =
						PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
				Tokenizer<CoreLabel> tok =
						tokenizerFactory.getTokenizer(new StringReader(lexin[a]));
				List<CoreLabel> rawWords2 = tok.tokenize();
				Tree parse = lp.apply(rawWords2);

				TreebankLanguagePack tlp = lp.treebankLanguagePack(); // PennTreebankLanguagePack for English
				GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
				GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
				List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
				System.out.println(tdl);
				String[] dependancies=tdl.toString().replace("[","").replace("]","").replace("),",")@$").split("@$");
				dependancies[0]=" "+dependancies[0];

				int x=0;

				do{

					if(dependancies[x]!=null && !dependancies[x].isEmpty())
					{
						int st=dependancies[x].indexOf(" ");
						int en=dependancies[x].indexOf("(");
						String Dependancy_check=dependancies[x].substring(st,en);
						System.out.println(st+"&&"+en);
						if(Dependancy_check.equals(" dobj")){
							System.out.println(dependancies[x]);	
						}else if(Dependancy_check.equals(" prep_in"))
						{
							System.out.println(dependancies[x]);
						}else if(Dependancy_check.equals(" prep_from"))
						{
							System.out.println(dependancies[x]);
						}else if(Dependancy_check.equals(" prep_for"))
						{
							System.out.println(dependancies[x]);
						}else if(Dependancy_check.equals(" prep_with"))
						{
							System.out.println(dependancies[x]);
						}else if(Dependancy_check.equals(" auxpass"))
						{
							System.out.println(dependancies[x]);
						}else if(Dependancy_check.equals(" dep"))
						{
							System.out.println(dependancies[x]);
						}
						else if(Dependancy_check.equals(" Xcomp"))
						{
							System.out.println(dependancies[x]);
						}
						else
							System.out.println("No Dependancies Found");
					}
					x++;
				}while(x<dependancies.length);
			}

		}

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

	public static String Taxonomy(String strTest) {
		String output="";
		try {
			String line = strTest.toLowerCase();
			Workbook wrk1 =  Workbook.getWorkbook(new File("/home/pradeep/lifeeventsedited.xls"));
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
