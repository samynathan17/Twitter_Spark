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

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;




import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
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
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeAnnotator;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.time.SUTime.Temporal;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Stream 
{
	static StanfordCoreNLP pipeline;
    int sm=1;

	public static void main(String[] args) throws TwitterException, IOException, RowsExceededException, WriteException 
	{

		Stream stream = new Stream();
		stream.execute();
	}

	private final Object lock = new Object();

	public List<Status> execute() throws TwitterException, IOException, RowsExceededException, WriteException 
	{

		WritableWorkbook wworkbook;
		wworkbook = Workbook.createWorkbook(new File("twitterdata.xls"));
		final WritableSheet wsheet = wworkbook.createSheet("twitter_data", 0);
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final List<Status> statuses = new ArrayList();
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey("mmkrPknQiNX7AlkDwhADRgAJs");
		cb.setOAuthConsumerSecret("WoR05dPkPYMUGNV52WWShflgFF6kmOdNleTiDdRdpDTzX510wD");
		cb.setOAuthAccessToken("3557086092-LOZOqkjgyYFmimwk4OUP31iqCaaGJID5DQUIfuT");
		cb.setOAuthAccessTokenSecret("yPUWhZ5NN8f0le9q13KN01oVMyFpEmhued2HyKyVJbK6E");
		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		
		Label id=new Label(0,0,"ID");
		 wsheet.addCell(id);
		 
		 Label name=new Label(1,0,"Name");
		 wsheet.addCell(name);
		 
		 Label scname=new Label(2,0,"screenName");
		 wsheet.addCell(scname);
		 
		 Label loca=new Label(3,0,"Location");
		 wsheet.addCell(loca);
		 
		 Label description=new Label(4,0,"Description");
		 wsheet.addCell(description);
		 
		 Label isContributorsEnabled=new Label(5,0,"IsContributorsEnabled");
		 wsheet.addCell(isContributorsEnabled);
		 
		 Label profileImageUrl=new Label(6,0,"ProfileImageUrl");
		 wsheet.addCell(profileImageUrl);
		 
		 Label profileImageUrlHttps=new Label(7,0,"ProfileImageUrlHttps");
		 wsheet.addCell(profileImageUrlHttps);
		 
		 Label isDefaultProfileImage=new Label(8,0,"IsDefaultProfileImage");
		 wsheet.addCell(isDefaultProfileImage);
		 
		 Label url=new Label(9,0,"Url");
		 wsheet.addCell(url);
		 
		 Label isProtected=new Label(10,0,"IsProtected");
		 wsheet.addCell(isProtected);
		 
		 Label followersCount=new Label(11,0,"FollowersCount");
		 wsheet.addCell(followersCount);
		 
		 Label status=new Label(12,0,"Status");
		 wsheet.addCell(status);
		 
		 Label profileBackgroundColor=new Label(13,0,"ProfileBackgroundColor");
		 wsheet.addCell(profileBackgroundColor);
		 
		 Label profileTextColor=new Label(14,0,"ProfileTextColor");
		 wsheet.addCell(profileTextColor);
		 
		 Label profileLinkColor=new Label(15,0,"profileLinkColor");
		 wsheet.addCell(profileLinkColor);
		 
		 Label profileSidebarFillColor=new Label(16,0,"profileSidebarFillColor");
		 wsheet.addCell(profileSidebarFillColor);
		 
		 Label profileSidebarBorderColor=new Label(17,0,"profileSidebarBorderColor");
		 wsheet.addCell(profileSidebarBorderColor);
		 
		 Label profileUseBackgroundImage=new Label(18,0,"profileUseBackgroundImage");
		 wsheet.addCell(profileUseBackgroundImage);
		 
		 Label isDefaultProfile=new Label(19,0,"isDefaultProfile");
		 wsheet.addCell(isDefaultProfile);
		 
		 Label showAllInlineMedia=new Label(20,0,"showAllInlineMedia");
		 wsheet.addCell(showAllInlineMedia);
		 
		 Label friendsCount=new Label(21,0,"friendsCount");
		 wsheet.addCell(friendsCount);
		 
		 Label createdAt=new Label(22,0,"createdAt");
		 wsheet.addCell(createdAt);
		 
		 Label favouritesCount=new Label(23,0,"favouritesCount");
		 wsheet.addCell(favouritesCount);
		 
		 Label utcOffset=new Label(24,0,"utcOffset");
		 wsheet.addCell(utcOffset);
		 
		 Label timeZone=new Label(25,0,"timeZone");
		 wsheet.addCell(timeZone);
		 
		 Label profileBackgroundImageUrl=new Label(26,0,"profileBackgroundImageUrl");
		 wsheet.addCell(profileBackgroundImageUrl);
		 
		 Label profileBackgroundImageUrlHttps=new Label(27,0,"profileBackgroundImageUrlHttps");
		 wsheet.addCell(profileBackgroundImageUrlHttps);
		 
		 Label profileBackgroundTiled=new Label(28,0,"profileBackgroundTiled");
		 wsheet.addCell(profileBackgroundTiled);
		 
		 Label lang=new Label(29,0,"lang");
		 wsheet.addCell(lang);
		 
		 Label statusesCount=new Label(30,0,"statusesCount");
		 wsheet.addCell(statusesCount);
		 
		 Label isGeoEnabled=new Label(31,0,"isGeoEnabled");
		 wsheet.addCell(isGeoEnabled);
		 
		 Label isVerified=new Label(32,0,"isVerified");
		 wsheet.addCell(isVerified);
		 
		 Label translator=new Label(33,0,"translator");
		 wsheet.addCell(translator);
		 
		 Label listedCount=new Label(34,0,"listedCount");
		 wsheet.addCell(listedCount);
		 
		 Label isFollowRequestSent=new Label(35,0,"isFollowRequestSent");
		 wsheet.addCell(isFollowRequestSent);
		 
		 Label withheldInCountries=new Label(36,0,"withheldInCountries");
		 wsheet.addCell(withheldInCountries);
		 
		 Label usertext=new Label(37,0,"usertext");
		 wsheet.addCell(usertext);
		 Label dbPedia =new Label(38,0,"DbPedia");
			wsheet.addCell(dbPedia);
			Label ner =new Label(39,0,"Named Entities");
			wsheet.addCell(ner);
			Label phNum =new Label(40,0,"Phone_Number");
			wsheet.addCell(phNum);
			Label mail_id =new Label(41,0,"Mail_id");
			wsheet.addCell(mail_id);
			Label date =new Label(42,0,"Date_Time");
			wsheet.addCell(date);
			Label senti =new Label(43,0,"Sentiment");
			wsheet.addCell(senti);

		StatusListener listener = new StatusListener() 
		{

			public void onStatus(Status status) 
			{
				

				twitter4j.User user= status.getUser();
				String st=user.toString();
				statuses.add(status);
				
				//System.out.println(statuses.size() + ":" +st);

				try 
				{

//add excel
					Label id= new Label(0, sm,""+user.getId());
					wsheet.addCell(id);
					
					Label name= new Label(1, sm,user.getName());
					wsheet.addCell(name);
					
					Label scname= new Label(2, sm,user.getScreenName());
					wsheet.addCell(scname);
					
					Label location= new Label(3, sm,user.getLocation());
					wsheet.addCell(location);
					
					Label description= new Label(4, sm,user.getDescription());
					wsheet.addCell(description);
					
					Label isContributorsEnabled= new Label(5, sm,""+user.isContributorsEnabled());
					wsheet.addCell(isContributorsEnabled);
					
					Label profileImageUrl= new Label(6, sm,""+user.getProfileImageURL());
					wsheet.addCell(profileImageUrl);
					
					Label profileImageUrlHttps= new Label(7, sm,""+user.getProfileImageURLHttps());
					wsheet.addCell(profileImageUrlHttps);
					
					Label isDefaultProfileImage= new Label(8, sm,""+user.isDefaultProfileImage());
					wsheet.addCell(isDefaultProfileImage);
					
					Label url= new Label(9, sm,""+user.getURL());
					wsheet.addCell(url);
					
					Label isProtected= new Label(10, sm,""+user.isProtected());
					wsheet.addCell(isProtected);
					
					Label followersCount= new Label(11, sm,""+user.getFollowersCount());
					wsheet.addCell(followersCount);
					
					Label status1= new Label(12, sm,""+user.getStatus());
					wsheet.addCell(status1);
					
					Label profileBackgroundColor= new Label(13, sm,""+user.getProfileBackgroundColor());
					wsheet.addCell(profileBackgroundColor);
					
					Label profileTextColor= new Label(14, sm,""+user.getProfileTextColor());
					wsheet.addCell(profileTextColor);
					
					Label profileLinkColor= new Label(15, sm,""+user.getProfileLinkColor());
					wsheet.addCell(profileLinkColor);
					
					Label profileSidebarFillColor= new Label(16, sm,""+user.getProfileSidebarFillColor());
					wsheet.addCell(profileSidebarFillColor);
					
					Label profileSidebarBorderColor= new Label(17, sm,""+user.getProfileSidebarBorderColor());
					wsheet.addCell(profileSidebarBorderColor);
					
					Label profileUseBackgroundImage= new Label(18, sm,""+user.isProfileUseBackgroundImage());
					wsheet.addCell(profileUseBackgroundImage);
					
					Label isDefaultProfile= new Label(19, sm,""+user.isDefaultProfile());
					wsheet.addCell(isDefaultProfile);
					
					Label showAllInlineMedia= new Label(20, sm,""+user.isShowAllInlineMedia());
					wsheet.addCell(showAllInlineMedia);
					
					Label friendsCount= new Label(21, sm,""+user.getFriendsCount());
					wsheet.addCell(friendsCount);
					
					Label createdAt= new Label(22, sm,""+user.getCreatedAt());
					wsheet.addCell(createdAt);
					
					Label favouritesCount= new Label(23, sm,""+user.getFavouritesCount());
					wsheet.addCell(favouritesCount);
					
					Label utcOffset= new Label(24, sm,""+user.getUtcOffset());
					wsheet.addCell(utcOffset);
					
					Label timeZone= new Label(25, sm,""+user.getTimeZone());
					wsheet.addCell(timeZone);
					
					Label profileBackgroundImageUrl= new Label(26, sm,""+user.getProfileBackgroundImageURL());
					wsheet.addCell(profileBackgroundImageUrl);
					
					Label profileBackgroundImageUrlHttps= new Label(27, sm,""+user.getProfileBackgroundImageUrlHttps());
					wsheet.addCell(profileBackgroundImageUrlHttps);
					
					Label profileBackgroundTiled= new Label(28, sm,""+user.isProfileBackgroundTiled());
					wsheet.addCell(profileBackgroundTiled);
					
					Label lang= new Label(29, sm,""+user.getLang());
					wsheet.addCell(lang);
					
					Label statusesCount= new Label(30, sm,""+user.getStatusesCount());
					wsheet.addCell(statusesCount);
					
					Label isGeoEnabled= new Label(31, sm,""+user.isGeoEnabled());
					wsheet.addCell(isGeoEnabled);
					
					Label isVerified= new Label(32, sm,""+user.isVerified());
					wsheet.addCell(isVerified);
					
					Label translator= new Label(33, sm,""+user.isTranslator());
					wsheet.addCell(translator);
					
					Label listedCount= new Label(34, sm,""+user.getListedCount());
					wsheet.addCell(listedCount);
					
					Label isFollowRequestSent= new Label(35, sm,""+user.isFollowRequestSent());
					wsheet.addCell(isFollowRequestSent);
					
					Label withheldInCountries= new Label(36, sm,""+user.getWithheldInCountries());
					wsheet.addCell(withheldInCountries);
					Label usertweet= new Label(37, sm,""+status.getText());
					wsheet.addCell(usertweet);
					
					
					ArrayList<String> nlp=findNLPResults(status.getText());
					System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					System.out.println(nlp);
					Label excel_keyword_nlp1 = new Label(38, sm, nlp.get(0));
		  			Label excel_keyword_nlp2 = new Label(39, sm, nlp.get(1));
		  			Label excel_keyword_nlp3 = new Label(40, sm, nlp.get(2));
		  			Label excel_keyword_nlp4 = new Label(41, sm, nlp.get(3));
		  			Label excel_keyword_nlp5 = new Label(42, sm, nlp.get(4));
		  			Label excel_keyword_nlp6 = new Label(43, sm, nlp.get(5));
		  			
		  			wsheet.addCell(excel_keyword_nlp1);
		  			wsheet.addCell(excel_keyword_nlp2);
		  			wsheet.addCell(excel_keyword_nlp3);
		  			wsheet.addCell(excel_keyword_nlp4);
		  			wsheet.addCell(excel_keyword_nlp5);
		  			wsheet.addCell(excel_keyword_nlp6);
		  			sm++;
					//System.out.println(sm);
				} 
				catch (RowsExceededException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				catch (WriteException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassCastException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (statuses.size() >100) {
					synchronized (lock) {
						lock.notify();
					}
					System.out.println("unlocked");
				}
			}

			private ArrayList<String> findNLPResults(String string) throws ClassCastException, ClassNotFoundException, IOException {
				// TODO Auto-generated method stub
				//System.out.println(data);
				ArrayList<String> nlp_results=new ArrayList<String>();
				
				
				String input1=(String) string;
				//System.out.println(input1);
				String ner="NA";
				String sentiment=null;
				String phNum=null;
				String email=null;
				String date=null;
				//String input2="I am John, I have four different email ids. I work for Sony.  john.d@example.com, john_d@example.com john-d@example.com My personal preference is to use johndoe@example.co.uk. If You want to reach by phone dial: +65 6889-7445 or (65) 6889-7445 or +6568897445. If You are from Singapore dial: 68897445.  please contact betweem 10 am to 6 pm. I am pleased to inform that I will be leaving Singapore on Nov 25, to join Flipkart, Bangalore, India.";
		//String input1="Sreetha and Anagha are tom and jerry.";

				Stream.init();
				//DBFY


				//System.out.println("**** DBPedia Tagging ****");
				//System.out.println(DBFY(input1)); 
				String db = DBFY(input1);
			   nlp_results.add(db);
				// NER
				//System.out.println("**** NER Tagging ****");
				//System.out.println(findNer(input1));
				
				ner=findNer(input1).toString();
					//e.printStackTrace(s);
				
					
				//System.out.println(ner);
				String ner1=ner.toString();
				//if(ner!=null)
				nlp_results.add(ner1);
				//else
					//nlp_results.add("Null");
				
				//System.out.println("**** Phone Number Extraction ****");
				// phone number extractor
				
				ArrayList<String> results = findPhNum(input1);
				String reslts = results.toString();
				nlp_results.add(results.toString());
				//System.out.println(results); 


				// email extractor 
				//System.out.println("**** Email Extraction ****");
				nlp_results.add(Email_Extract(input1).toString());
				//System.out.println(Email_Extract(input1)); 

				//// date extractor
				//System.out.println("**** Date Extraction ****");

				Map<String, Temporal> results1 = ExtractDate(input1);
				nlp_results.add(ExtractDate(input1).toString());
				//for (String key : results1.keySet())
				//{
					//System.out.println(key + ": " + results1.get(key));
				//}	



				// Sentiment Tagging'
				//System.out.println("**** Sentiment Extraction ****");
				
				if (findSentiment(input1)==0)
				{
					sentiment="Very Negative";
					//System.out.println("Very Negative");
				}
				else if (findSentiment(input1)==1)
				{
					sentiment="Negative";
					//System.out.println("Negative");
				}else 
					if (findSentiment(input1)==2)
					{
						sentiment="Neutral";
						//System.out.println("Neutral");
					}
					else 
						if (findSentiment(input1)==3)
						{
							sentiment="Positive";
							//System.out.println("Positive");
						}
						else 
							if (findSentiment(input1)==4)
							{
								sentiment="Very Positive";
								//System.out.println("Very Positive");
							}		
				nlp_results.add(sentiment);
				
				//System.out.println(sentiment);
			
				return nlp_results;
			}

			public Map<String, Temporal> ExtractDate(String text){
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
			public ArrayList<String> Email_Extract(String text) 
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


			public ArrayList<String> findPhNum(String string2) {
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

			public void lexparser(LexicalizedParser lp, String filename) {
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

			public int findSentiment(String text) {
		        System.out.println(text);
				int mainSentiment = 0;
				if (text != null && text.length() > 0) 
				{
					int longest = 0;
					try{
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
					}catch(NullPointerException e){
						
						e.printStackTrace();
						//System.out.println("Exception");
					}
				}
				return mainSentiment;
			}



			public String findNer(String line) throws ClassCastException, ClassNotFoundException, IOException {  
				
				
				String serializedClassifier = "/home/bigtapp/workspace/recon_backend-master/named-entity/classifiers/english.all.3class.distsim.crf.ser.gz";

				String output=null;
				AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
			//	System.out.println("**** Named Entity Classifier Tagging ****");
			//	System.out.println(classifier.classifyToString(line));
				String xmlner=classifier.classifyWithInlineXML(line);
			
				

				return xmlner;
			}


			public String DBFY(String input) throws IOException
			{
				String dblink="http://52.76.15.236:2222/rest/annotate?text=";
				String encodedURL=java.net.URLEncoder.encode(input,"UTF-8");

				String url=dblink+""+encodedURL+"&confidence=0.0&support=0";
				org.jsoup.nodes.Document doc = Jsoup.parse(getsource(url), "UTF-8");
				Elements z1=doc.select("a");
				//	System.out.println(z1.toString());
				return z1.toString();
			}
			private String getsource(String input1) throws IOException {
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

			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) 
			{
				System.out.println("Got a status deletion notice id:"
						+ statusDeletionNotice.getStatusId());
			}

			public void onTrackLimitationNotice(int numberOfLimitedStatuses)
			{
				System.out.println("Got track limitation notice:"
						+ numberOfLimitedStatuses);
			}

			public void onScrubGeo(long userId, long upToStatusId) 
			{
				System.out.println("Got scrub_geo event userId:" + userId
						+ " upToStatusId:" + upToStatusId);
			}

			public void onException(Exception ex) 
			{
				ex.printStackTrace();
			}

			public void onStallWarning(StallWarning sw) 
			{
				System.out.println(sw.getMessage());

			}

		};


		FilterQuery fq = new FilterQuery();
		String keywords[] = { "birthday",
				"Bankruptcy",
				"Accident/Insurance Claim",
				"Travel",
				"Going to college",
				"Promotion",
				"Bonus",
				"Growing a small business",
				"Retirement",
				"Home Shifting",
				"Change of address",
				"Home Renovation",
				"Fine dining",
				"Vacation",
				"Illness",
				"Learning",
				"Walking",
				"Talking",
				"Minor Illnesses",
				"School",
				"Growth",
				"Minor Injuries and Illnesses",
				"School",
				"Puberty",
				"Emotions",
				"Sex",
				"Growth",
				"Begin Driving",
				"Risky Behaviors",
				"Completion of Education",
				"Begin Career",
				"Move Out",
				"Marriage",
				"First Child",
				"Career Pressures",
				"Job Advances",
				"Managing Family" };

		fq.track(keywords);
//track Singapore location
		double [][]location ={{1,14},{103,55}};

		fq.locations(location);

		twitterStream.addListener(listener); 
		twitterStream.filter(fq);

		try 
		{
			synchronized (lock) 
			{
				lock.wait();
			}
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("returning statuses");
		twitterStream.shutdown();
		wworkbook.write();
		wworkbook.close();
		System.out.println("done....!!!!");
		return statuses;
		
	}

	protected static void init() {
		// TODO Auto-generated method stub
		pipeline = new StanfordCoreNLP("MyPropFile.properties");
		
	}

}