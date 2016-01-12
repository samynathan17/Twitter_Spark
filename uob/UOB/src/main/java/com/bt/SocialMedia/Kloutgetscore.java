package com.bt.SocialMedia;

import java.util.Scanner;

public class Kloutgetscore 
{
	public static void main(String[] args) throws Exception {
		KloutScore k = new KloutScore("mjhzb6hqunkry7ptxg7qgc94");

		// retrieves klout id with twitter screen name
		Scanner in = new Scanner(System.in);
		String twitter_id=in.next();
		String[] data = k.getIdentity(twitter_id, KloutScore.TWITTER_SCREEN_NAME); // contains ["635263", "ks]
		//System.out.println(data[0]+""+data[1]);

		// retrieves klout id with twitter id
		//String[] d = k.getIdentity("500042487", KloutScore.TWITTER); // contains ["54887627490056592", "ks"]


		// gets user with klout id
		KloutUser u = k.getUser(data[0]);

		double score = u.score();
		System.out.println(score);

		//KloutTopic[] topics = u.getTopics();

		//KloutUser[] influencers = u.getInfluencers();
		//KloutUser[] influencees = u.getInfluencees();
	}
	
}
