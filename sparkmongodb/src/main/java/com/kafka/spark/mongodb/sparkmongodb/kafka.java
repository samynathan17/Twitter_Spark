package com.kafka.spark.mongodb.sparkmongodb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class kafka {
	
	public static Properties properties = new Properties();
	public static  void run(String consumerKey, String consumerSecret,
			String token, String secret) throws InterruptedException {
		try{
			
			properties.load(new FileInputStream("src/main/resources/config.properties"));
		}
		catch (IOException e) {
			System.out.println("Exception Occurred" + e.getMessage());
		}
		Properties propertie = new Properties();
		propertie.put("metadata.broker.list",properties.getProperty("metadata.broker.list"));
		propertie.put("serializer.class", properties.getProperty("serializer.class"));
		propertie.put("client.id",properties.getProperty("client.id"));
		ProducerConfig producerConfig = new ProducerConfig(propertie);
		
		Producer<String, String> Producer = new Producer<String, String>(producerConfig);
		System.out.println("producer_________"+Producer);
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(100);
		StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
		
		// add some track terms in hashtag
		endpoint.trackTerms(Lists.newArrayList("twitterapi",properties.getProperty("Hash_tag_Name")));
		

		Authentication auth = new OAuth1(consumerKey, consumerSecret, token,
				secret);
		// Authentication auth = new BasicAuth(username, password);

		// Create a new BasicClient. By default gzip is enabled.
		Client client = new ClientBuilder().hosts(Constants.STREAM_HOST)
				.endpoint(endpoint).authentication(auth)
				.processor(new StringDelimitedProcessor(queue)).build();

		// Establish a connection
		client.connect();

		// Do whatever needs to be done with messages
		for (int msgRead = 0; msgRead < 10; msgRead++) {
			KeyedMessage<String, String> message = null;
			try {
				message = new KeyedMessage<String, String>(properties.getProperty("Topic"), queue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Producer.send(message);
			System.out.println(message);
		}
		Producer.close();
		client.stop();

	}
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
					try {
					properties.load(new FileInputStream("src/main/resources/Twitter.properties"));
			kafka.run(properties.getProperty("consumerKey"),
					properties.getProperty("consumerSecret"),
					properties.getProperty("AccessToken"),
					properties.getProperty("AccessTokenSecret"));
			
		} catch (InterruptedException e) {
			System.out.println(e);
		}
}
}