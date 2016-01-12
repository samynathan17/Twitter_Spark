package com.bt.SocialMedia;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bericotech.clavin.ClavinException;
import com.bericotech.clavin.GeoParser;
import com.bericotech.clavin.GeoParserFactory;
import com.bericotech.clavin.resolver.ResolvedLocation;
import com.bt.NLP.NLP_Pipeline;
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
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import edu.stanford.nlp.*;

public class TwitterData {
	int sm = 1;
	final static Logger logger = Logger.getLogger(Twitter.class);
	public static void main(String[] args) throws TwitterException,
			IOException, RowsExceededException, WriteException, ClavinException{
		NLP_Pipeline.init();
		TwitterData stream = new TwitterData();
		stream.execute();
	}

	private final Object lock = new Object();

	public List<Status> execute() throws TwitterException, IOException,
			RowsExceededException, WriteException, ClavinException {
		
		final GeoParser parser = GeoParserFactory.getDefault("/home/pradeep/clavin/clavin/IndexDirectory");
		WritableWorkbook wworkbook;
		wworkbook = Workbook.createWorkbook(new File("/home/pradeep/uoboutputs/Twitter/twitterdata.xls"));
		final WritableSheet wsheet = wworkbook.createSheet("twitter_data", 0);

		@SuppressWarnings({ "unchecked", "rawtypes" })
		final List<Status> statuses = new ArrayList();
		logger.info("Configure Access Token");
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey("mmkrPknQiNX7AlkDwhADRgAJs");
		cb.setOAuthConsumerSecret("WoR05dPkPYMUGNV52WWShflgFF6kmOdNleTiDdRdpDTzX510wD");
		cb.setOAuthAccessToken("3557086092-LOZOqkjgyYFmimwk4OUP31iqCaaGJID5DQUIfuT");
		cb.setOAuthAccessTokenSecret("yPUWhZ5NN8f0le9q13KN01oVMyFpEmhued2HyKyVJbK6E");
		TwitterStream twitterStream = new TwitterStreamFactory(cb.build())
				.getInstance();


   
		Label id = new Label(0, 0, "ID");
		wsheet.addCell(id);

		Label name = new Label(1, 0, "Name");
		wsheet.addCell(name);

		Label scname = new Label(2, 0, "screenName");
		wsheet.addCell(scname);

		Label loca = new Label(3, 0, "Location");
		wsheet.addCell(loca);

		Label description = new Label(4, 0, "Description");
		wsheet.addCell(description);

		Label isContributorsEnabled = new Label(5, 0, "IsContributorsEnabled");
		wsheet.addCell(isContributorsEnabled);

		Label profileImageUrl = new Label(6, 0, "ProfileImageUrl");
		wsheet.addCell(profileImageUrl);

		Label profileImageUrlHttps = new Label(7, 0, "ProfileImageUrlHttps");
		wsheet.addCell(profileImageUrlHttps);

		Label isDefaultProfileImage = new Label(8, 0, "IsDefaultProfileImage");
		wsheet.addCell(isDefaultProfileImage);

		Label url = new Label(9, 0, "Url");
		wsheet.addCell(url);

		Label isProtected = new Label(10, 0, "IsProtected");
		wsheet.addCell(isProtected);

		Label followersCount = new Label(11, 0, "FollowersCount");
		wsheet.addCell(followersCount);

		Label status = new Label(12, 0, "Status");
		wsheet.addCell(status);

		Label profileBackgroundColor = new Label(13, 0,
				"ProfileBackgroundColor");
		wsheet.addCell(profileBackgroundColor);

		Label profileTextColor = new Label(14, 0, "ProfileTextColor");
		wsheet.addCell(profileTextColor);

		Label profileLinkColor = new Label(15, 0, "profileLinkColor");
		wsheet.addCell(profileLinkColor);

		Label profileSidebarFillColor = new Label(16, 0,
				"profileSidebarFillColor");
		wsheet.addCell(profileSidebarFillColor);

		Label profileSidebarBorderColor = new Label(17, 0,
				"profileSidebarBorderColor");
		wsheet.addCell(profileSidebarBorderColor);

		Label profileUseBackgroundImage = new Label(18, 0,
				"profileUseBackgroundImage");
		wsheet.addCell(profileUseBackgroundImage);

		Label isDefaultProfile = new Label(19, 0, "isDefaultProfile");
		wsheet.addCell(isDefaultProfile);

		Label showAllInlineMedia = new Label(20, 0, "showAllInlineMedia");
		wsheet.addCell(showAllInlineMedia);

		Label friendsCount = new Label(21, 0, "friendsCount");
		wsheet.addCell(friendsCount);

		Label createdAt = new Label(22, 0, "createdAt");
		wsheet.addCell(createdAt);

		Label favouritesCount = new Label(23, 0, "favouritesCount");
		wsheet.addCell(favouritesCount);

		Label utcOffset = new Label(24, 0, "utcOffset");
		wsheet.addCell(utcOffset);

		Label timeZone = new Label(25, 0, "timeZone");
		wsheet.addCell(timeZone);

		Label profileBackgroundImageUrl = new Label(26, 0,
				"profileBackgroundImageUrl");
		wsheet.addCell(profileBackgroundImageUrl);

		Label profileBackgroundImageUrlHttps = new Label(27, 0,
				"profileBackgroundImageUrlHttps");
		wsheet.addCell(profileBackgroundImageUrlHttps);

		Label profileBackgroundTiled = new Label(28, 0,
				"profileBackgroundTiled");
		wsheet.addCell(profileBackgroundTiled);

		Label lang = new Label(29, 0, "lang");
		wsheet.addCell(lang);

		Label statusesCount = new Label(30, 0, "statusesCount");
		wsheet.addCell(statusesCount);

		Label isGeoEnabled = new Label(31, 0, "isGeoEnabled");
		wsheet.addCell(isGeoEnabled);

		Label isVerified = new Label(32, 0, "isVerified");
		wsheet.addCell(isVerified);

		Label translator = new Label(33, 0, "translator");
		wsheet.addCell(translator);

		Label listedCount = new Label(34, 0, "listedCount");
		wsheet.addCell(listedCount);

		Label isFollowRequestSent = new Label(35, 0, "isFollowRequestSent");
		wsheet.addCell(isFollowRequestSent);

		Label withheldInCountries = new Label(36, 0, "withheldInCountries");
		wsheet.addCell(withheldInCountries);

		Label usertext = new Label(37, 0, "usertext");
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
			
			Label geotag =new Label(44,0,"Geo_tag");
			wsheet.addCell(geotag);               
		
			logger.info("Twitter Crawling Started");

		StatusListener listener = new StatusListener() {

			public void onStatus(Status status) {

				
				twitter4j.User user = status.getUser();
				String st = user.toString();
				statuses.add(status);

				

				try {

					logger.info("Writing Data To Excel");
					Label id = new Label(0, sm, "" + user.getId());
					wsheet.addCell(id);
			
					Label name = new Label(1, sm, user.getName());
					wsheet.addCell(name);

					Label scname = new Label(2, sm, user.getScreenName());
					wsheet.addCell(scname);

					Label location = new Label(3, sm, user.getLocation());
					wsheet.addCell(location);

					Label description = new Label(4, sm, user.getDescription());
					wsheet.addCell(description);

					Label isContributorsEnabled = new Label(5, sm, ""
							+ user.isContributorsEnabled());
					wsheet.addCell(isContributorsEnabled);

					Label profileImageUrl = new Label(6, sm, ""
							+ user.getProfileImageURL());
					wsheet.addCell(profileImageUrl);

					Label profileImageUrlHttps = new Label(7, sm, ""
							+ user.getProfileImageURLHttps());
					wsheet.addCell(profileImageUrlHttps);

					Label isDefaultProfileImage = new Label(8, sm, ""
							+ user.isDefaultProfileImage());
					wsheet.addCell(isDefaultProfileImage);

					Label url = new Label(9, sm, "" + user.getURL());
					wsheet.addCell(url);

					Label isProtected = new Label(10, sm, ""
							+ user.isProtected());
					wsheet.addCell(isProtected);

					Label followersCount = new Label(11, sm, ""
							+ user.getFollowersCount());
					wsheet.addCell(followersCount);

					Label status1 = new Label(12, sm, "" + user.getStatus());
					wsheet.addCell(status1);

					Label profileBackgroundColor = new Label(13, sm, ""
							+ user.getProfileBackgroundColor());
					wsheet.addCell(profileBackgroundColor);

					Label profileTextColor = new Label(14, sm, ""
							+ user.getProfileTextColor());
					wsheet.addCell(profileTextColor);

					Label profileLinkColor = new Label(15, sm, ""
							+ user.getProfileLinkColor());
					wsheet.addCell(profileLinkColor);

					Label profileSidebarFillColor = new Label(16, sm, ""
							+ user.getProfileSidebarFillColor());
					wsheet.addCell(profileSidebarFillColor);

					Label profileSidebarBorderColor = new Label(17, sm, ""
							+ user.getProfileSidebarBorderColor());
					wsheet.addCell(profileSidebarBorderColor);

					Label profileUseBackgroundImage = new Label(18, sm, ""
							+ user.isProfileUseBackgroundImage());
					wsheet.addCell(profileUseBackgroundImage);

					Label isDefaultProfile = new Label(19, sm, ""
							+ user.isDefaultProfile());
					wsheet.addCell(isDefaultProfile);

					Label showAllInlineMedia = new Label(20, sm, ""
							+ user.isShowAllInlineMedia());
					wsheet.addCell(showAllInlineMedia);

					Label friendsCount = new Label(21, sm, ""
							+ user.getFriendsCount());
					wsheet.addCell(friendsCount);

					Label createdAt = new Label(22, sm, ""
							+ user.getCreatedAt());
					wsheet.addCell(createdAt);

					Label favouritesCount = new Label(23, sm, ""
							+ user.getFavouritesCount());
					wsheet.addCell(favouritesCount);

					Label utcOffset = new Label(24, sm, ""
							+ user.getUtcOffset());
					wsheet.addCell(utcOffset);

					Label timeZone = new Label(25, sm, "" + user.getTimeZone());
					wsheet.addCell(timeZone);

					Label profileBackgroundImageUrl = new Label(26, sm, ""
							+ user.getProfileBackgroundImageURL());
					wsheet.addCell(profileBackgroundImageUrl);

					Label profileBackgroundImageUrlHttps = new Label(27, sm, ""
							+ user.getProfileBackgroundImageUrlHttps());
					wsheet.addCell(profileBackgroundImageUrlHttps);

					Label profileBackgroundTiled = new Label(28, sm, ""
							+ user.isProfileBackgroundTiled());
					wsheet.addCell(profileBackgroundTiled);

					Label lang = new Label(29, sm, "" + user.getLang());
					wsheet.addCell(lang);

					Label statusesCount = new Label(30, sm, ""
							+ user.getStatusesCount());
					wsheet.addCell(statusesCount);

					Label isGeoEnabled = new Label(31, sm, ""
							+ user.isGeoEnabled());
					wsheet.addCell(isGeoEnabled);

					Label isVerified = new Label(32, sm, "" + user.isVerified());
					wsheet.addCell(isVerified);

					Label translator = new Label(33, sm, ""
							+ user.isTranslator());
					wsheet.addCell(translator);

					Label listedCount = new Label(34, sm, ""
							+ user.getListedCount());
					wsheet.addCell(listedCount);

					Label isFollowRequestSent = new Label(35, sm, ""
							+ user.isFollowRequestSent());
					wsheet.addCell(isFollowRequestSent);

					Label withheldInCountries = new Label(36, sm, ""
							+ user.getWithheldInCountries());
					wsheet.addCell(withheldInCountries);


					String tweet = "" + status.getText();
					Label usertweet = new Label(37, sm, tweet);
					wsheet.addCell(usertweet);
					logger.info("Initializing NLP Pipeline");
					logger.info("DBPedia Annotation In Progress");
					String DBFY1 = NLP_Pipeline.DBFY(tweet).toString();
					Label excel_keyword_nlp2 = new Label(38, sm, DBFY1);
					wsheet.addCell(excel_keyword_nlp2);
					logger.info("NER Extraction In Progress");
					String ner1 = NLP_Pipeline.findNer(tweet).toString();
					Label excel_keyword_nlp1 = new Label(39, sm, ner1);
					wsheet.addCell(excel_keyword_nlp1);
	
					logger.info("Phone Number Extraction In Progress");
					
					String phNum1 = NLP_Pipeline.findPhNum(tweet).toString();
					Label excel_keyword_phNum1 = new Label(40, sm, phNum1);
					wsheet.addCell(excel_keyword_phNum1);

					logger.info("Email Extraction In Progress");
					String mail_id1 = NLP_Pipeline.Email_Extract(tweet)
							.toString();
					Label excel_keyword_mail_id = new Label(41, sm, mail_id1);
					wsheet.addCell(excel_keyword_mail_id);
					
					logger.info("Date Extraction In Progress");
					String date = NLP_Pipeline.ExtractDate(tweet)
							.toString();
					Label excel_keyword_date = new Label(42, sm,date);
					wsheet.addCell(excel_keyword_date);
					logger.info("Sentiment Extraction In Progress");
					String sentiment1 = NLP_Pipeline.findSentiment(tweet)
							.toString();
					Label excel_keyword_sentiment1 = new Label(43, sm,
							sentiment1);
					wsheet.addCell(excel_keyword_sentiment1);

					
					
					
					logger.info("Clavin-GEO Tagging In Progress");
						List<ResolvedLocation> resolvedLocations = parser.parse(tweet);
						ArrayList<String> identified_locations = new ArrayList<String>();
						for (ResolvedLocation resolvedLocation : resolvedLocations)
						{
							System.out.println(resolvedLocation);
							identified_locations.add(resolvedLocation.getMatchedName()+" resolved as :"+resolvedLocation.getGeoname().toString());

						}
						Label excel_geo = new Label(44, sm,identified_locations.toString());
						wsheet.addCell(excel_geo);
						
						logger.info("Klout score Retrieval In Progress");
						
				
						KloutScore k = new KloutScore("mjhzb6hqunkry7ptxg7qgc94");
						String twitter_id=user.getScreenName();
						String[] data = k.getIdentity(twitter_id, KloutScore.TWITTER_SCREEN_NAME); // contains ["635263", "ks]
						KloutUser u = k.getUser(data[0]);
						double score = u.score();
						System.out.println(score);
						Label excel_klout = new Label(45, sm,"Klout Score is "+score);
						wsheet.addCell(excel_klout);
						logger.info("Hash tag Extraction In Progress");
						
						
						Label excel_hashtags = new Label(46, sm,NLP_Pipeline.hashtagextract(tweet).toString());
						wsheet.addCell(excel_hashtags);	
						
						logger.info("User Mention Extraction In Progress");
						
						Label excel_usermentions = new Label(47, sm,NLP_Pipeline.twitterusermentionsextract(tweet).toString());
						wsheet.addCell(excel_usermentions);	
					
					
					
					
					
					sm++;
					

				} catch (RowsExceededException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (WriteException e) {
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
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (statuses.size() > 100) {
					synchronized (lock) {
						lock.notify();
					}
					
				}
			}

			public void onDeletionNotice(
					StatusDeletionNotice statusDeletionNotice) {
				logger.warn("Got a status deletion notice id:"
						+ statusDeletionNotice.getStatusId());
			}

			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		         logger.warn("Got track limitation notice:"
						+ numberOfLimitedStatuses);
			}

			public void onScrubGeo(long userId, long upToStatusId) {
				logger.info("Got scrub_geo event userId:" + userId
						+ " upToStatusId:" + upToStatusId);
			}

			public void onException(Exception ex) {
				ex.printStackTrace();
			}

			public void onStallWarning(StallWarning sw) {
				logger.warn(sw.getMessage());

			}

		};

		FilterQuery fq = new FilterQuery();
//key_word filter		
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
				"Managing Family","date time"};
		  fq.track(keywords);
//track Singapore location  filter		  
		  double [][]location ={{1,14},{103,55}};
		  fq.locations(location);
		  twitterStream.addListener(listener);
		  twitterStream.filter(fq);

		try {
			synchronized (lock) {
				lock.wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		twitterStream.shutdown();
		wworkbook.write();
		wworkbook.close();
		logger.info("Data Extraction Done");
		logger.info("Excel Write Complete");
		return statuses;
	}


}
