import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class Renotalk
{
	public static void main(String[] args) throws Exception{

		WritableWorkbook wworkbook;
		wworkbook = Workbook.createWorkbook(new File("/home/pradeep/Desktop/output.xls"));
		WritableSheet wsheet = wworkbook.createSheet("First Sheet", 0);
		int ex1=1;
		int ex2=0;
		int ex3=1;
		int ex4;

		String input_link="http://www.renotalk.com/forum/forum/2-financial-asessment-and-planning/";
		org.jsoup.nodes.Document doc = Jsoup.parse(getsource(input_link), "UTF-8");
		Elements linkcheck = doc.select("h4");
		Elements postclass = linkcheck.select("a[href]");
		String[] href=postclass.toString().split("\n");

		int i=0;

		do{
			org.jsoup.nodes.Document input_linktag = Jsoup.parse((href[i].replace("\n","")), "UTF-8");
			Element link = input_linktag.select("a").first();
			String post_link=link.attr("href");
			String post_title=link.attr("title");
			// System.out.println(post_title);
			// System.out.println(post_link);

			Label posttitle = new Label(0, ex1, post_title);
			wsheet.addCell(posttitle);
			Label postlink = new Label(1, ex1, post_link);
			wsheet.addCell(postlink);

			org.jsoup.nodes.Document input_postlink = Jsoup.parse(getsource(post_link), "UTF-8");
			Elements usertag=input_postlink.getElementsByClass("author_info");
			Elements posttag=input_postlink.getElementsByClass("post_body");
			String[] userdetails=usertag.toString().split("</div>");
			String[] postdetails=posttag.toString().split("</a></li> ");
			System.out.println(usertag);
			int j=0;
			do{
				org.jsoup.nodes.Document input_usertag = Jsoup.parse((userdetails[j]), "UTF-8");
				//	System.out.println(userdetails[j]);
				Elements e1=input_usertag.select("p");
				Elements e2=input_usertag.select("li");
				Elements e3=input_usertag.select("span");

				String userdetails1=input_usertag.text();
				org.jsoup.nodes.Document input_posttag = Jsoup.parse((postdetails[j]), "UTF-8");
				String postdetails1=input_posttag.text();
				// System.out.println(input_usertag); 
				// System.out.println(userdetails1); 
				String code_name=e3.text().toString();
				String user_name=e1.text().toString();
				String user_type=e2.text().toString();


				String user_no_of_posts=userdetails1.replaceAll("\\D+","");
				String[] post_detailsarray=postdetails1.split("-");
				String post_date=post_detailsarray[0].toString();
				System.out.println(code_name+"**"+user_name+"**"+user_type+"**"+user_no_of_posts);
				Label codename = new Label(2, ex1, code_name);
				wsheet.addCell(codename);
				Label username = new Label(3, ex1, user_name);
				wsheet.addCell(username);
				Label usertype = new Label(4, ex1, user_type);
				wsheet.addCell(usertype);
				Label noofposts = new Label(5, ex1, user_no_of_posts);
				wsheet.addCell(noofposts);
				Label postdate = new Label(6, ex1, post_date);
				wsheet.addCell(postdate);
				Label postdata = new Label(7, ex1, postdetails1);
				wsheet.addCell(postdata);

				// System.out.println(userdetails1);	
				// System.out.println(postdetails1);
				j++;
				ex1++;
			}while(j<1);//postdetails.length);
			i++;
			ex1++;
		}while(i<href.length);
		wworkbook.write();
		wworkbook.close();
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

}
