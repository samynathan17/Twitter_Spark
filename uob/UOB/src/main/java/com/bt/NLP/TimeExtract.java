package com.bt.NLP;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

public class TimeExtract
{
   public static void main(String[] args)
   {
      List<Date> dates = new PrettyTimeParser().parse("I will be celebrating my girl's 1st birthday in October");
      System.out.println(dates);
      
      // Prints: "[Sun Dec 12 13:45:12 CET 2013]"
      
      
   }
}