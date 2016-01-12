package com.kafka.spark.mongodb.sparkmongodb;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import scala.Tuple2;
import akka.dispatch.Mapper;

import com.bericotech.clavin.GeoParser;
import com.bericotech.clavin.GeoParserFactory;
import com.bericotech.clavin.resolver.ResolvedLocation;
import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

public class Sparkstreaming {
	
@SuppressWarnings("serial")
public static void sparkdata(){
	
	 final Properties properties = new Properties();
	 try{
			properties.load(new FileInputStream("src/main/resources/config.properties"));
			properties.load(new FileInputStream("src/main/resources/Dbconfig.properties"));
			properties.load(new FileInputStream("src/main/resources/Twitter.properties"));
		}
		catch (IOException e) {
			System.out.println("Exception Occurred" + e.getMessage());
		}
	                  
         Map<String, Integer> topicMap = new HashMap<String,Integer>();
        topicMap.put(properties.getProperty("Topic"),1);
      
       
        JavaStreamingContext jssc = new JavaStreamingContext("local[4]", "SparkStream", new  Duration(1200));
        /*(or)*/
      //  JavaStreamingContext jssc = new JavaStreamingContext(conf, new  Duration(1200));
        JavaPairReceiverInputDStream<String, String> messages = KafkaUtils.createStream(jssc, properties.getProperty("zookeeper.broker"), properties.getProperty("group_Id"), topicMap);

        System.out.println("+++++++++++++++++++ kafka to spark Connection done ++++++++++++++");
        
       
		JavaDStream<String> jsondata = messages.map(new Function<Tuple2<String, String>, String>() 
                                                {
                                                    public String call(Tuple2<String, String> message)
                                                    {
                                                    	
                                                        return message._2();
                                                    }
           
                                                });
		
        jsondata.foreachRDD(new Function<JavaRDD<String>, Void>() {
       
			@SuppressWarnings("deprecation")
			Mongo mongo = new Mongo(properties.getProperty("MongoDB_Host"));
  			@SuppressWarnings("deprecation")
			DB db_Name = mongo.getDB(properties.getProperty("db_Name"));
         	DBCollection collection = db_Name.getCollection(properties.getProperty("Collection_Name"));

			public Void call(JavaRDD<String> data) throws Exception {
				// TODO Auto-generated method stub
					
					List<String>result=data.collect();
					
					for (String temp :result) 
					{

						JSONParser jsonParser = new JSONParser();
						JSONObject jsonObject = (JSONObject) jsonParser.parse(temp);
						
					String object =  (String) jsonObject.get("text");
						System.out.println("************************");
						System.out.println(object);
						JSONObject TempjsonObject = (JSONObject) jsonObject.get("user");
						if (TempjsonObject != null)
							jsonObject = TempjsonObject;
						String user = (String) TempjsonObject.get("description");
						System.out.println("#######################################");
						System.out.println(user);
						System.out.println("#######################################");
						
						System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
						
						GeoParser parser = GeoParserFactory.getDefault("/home/bigtapp/Desktop/IndexDirectory");
						List<ResolvedLocation> resolvedLocations = (List<ResolvedLocation>) parser.parse("description");
						ArrayList<String> identified_locations = new ArrayList<String>();
						for (ResolvedLocation resolvedLocation : resolvedLocations)
						{
							identified_locations.add(resolvedLocation.getMatchedName()+" resolved as :"+resolvedLocation.getGeoname().toString());
						}
						
						String clavin = identified_locations.toString();
						
						System.out.println("******************************GEO TAGGGGG*******************************" +clavin);
						DBObject dbObject = (DBObject) JSON.parse(temp.toString());
						collection.insert(dbObject);
						
						if(dbObject==null)
						{
							 System.out.println("Got no data in kafka-Producer");
								}else
								{
									System.out.println("Inserted Data Done");			
			               
			            }
					
			
			}
					return null; 
			}	
        });
        
   
        jssc.start();
        jssc.awaitTermination();
        jssc.stop();
    }
        public static void main( String[] args ) throws InterruptedException
        {
        	Sparkstreaming.sparkdata();
        	//kafka k=new kafka();
        	//kafka.run("mmkrPknQiNX7AlkDwhADRgAJs","WoR05dPkPYMUGNV52WWShflgFF6kmOdNleTiDdRdpDTzX510wD", "3557086092-LOZOqkjgyYFmimwk4OUP31iqCaaGJID5DQUIfuT", "yPUWhZ5NN8f0le9q13KN01oVMyFpEmhued2HyKyVJbK6E");
        	
    	
    }
}
