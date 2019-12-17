package base;

import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.HashMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class Main {
    private static boolean printEnable = true;
    private static BufferedWriter csvWriter;
    private static FileWriter csvWriter2;
    private static HashMap<String,String> dataVals = new HashMap<String,String>();
    private static ArrayList<String> columnNames = new ArrayList<String>();
    private static int min = 179; //1
    private static int max = 407; //407

    //the first function that is run
    //calls all other functions.
    public static void main(String[] args) {
        print("running...");

        //goes through all rows in file, and picks out non duplicates
        //sorter S = new sorter("C:/Users/jakub/OneDrive/X Machines/CONTACTS.csv");

        //scrapeMachineTrack();
        //scrapeAfrika();
        scrapeUni();

        System.out.println("END");


    }

    public static void scrapeUni() {
        ScrapeUtil scrapper = new ScrapeUtil();

        // /adfs/ls/?wa=wsignin1.0&wtrealm=https%3a%2f%2ftimetable.soton.ac.uk&wctx=rm%3d0%26id%3dpassive%26ru%3d%252f&wct=2019-12-09T02%3a23%3a08Z&client-request-id=a31760b9-3039-494b-1d2d-008000100019
        // /adfs/ls/?wa=wsignin1.0&wtrealm=https%3a%2f%2ftimetable.soton.ac.uk &wctx=rm%3d0%26id%3dpassive%26ru%3d%252f&wct=2019-12-09T02%3a23%3a08Z&client-request-id=a31760b9-3039-494b-1d2d-008000100019
        // UserName Soton\jd8g19
        // Password: Kuba@uni1234


        try {
            Document login = Jsoup.connect("https://timetable.soton.ac.uk/adfs/ls/?wa=wsignin1.0&wtrealm=https%3a%2f%2ftimetable.soton.ac.uk&wctx=rm%3d0%26id%3dpassive%26ru%3d%252f&wct=2019-12-09T02%3a23%3a08Z&client-request-id=a31760b9-3039-494b-1d2d-008000100019")
                    .data("UserName","Soton\\jd8g19")
                    .data("Password", "Kubauni1234")
                    .userAgent("Mozilla/5.0")
                    .referrer("https://logon.soton.ac.uk/adfs/ls/?wa=wsignin1.0&wtrealm=https%3a%2f%2ftimetable.soton.ac.uk&wctx=rm%3d0%26id%3dpassive%26ru%3d%252f&wct=2019-12-09T02%3a23%3a08Z&client-request-id=a31760b9-3039-494b-1d2d-008000100019")
                    .post();
            print(login.text());
            Document document = Jsoup.connect("https://timetable.soton.ac.uk/").get();
            print(document.text());
            Elements rows = document.select("fc-event-container");
            print(rows.text());
        } catch (IOException e){
            print("error");
        }

    }

    public static void scrapeMachineTrack() {
        ScrapeUtil scrapper = new ScrapeUtil();
        scrapper.initWriter("C:/Users/jakub/OneDrive/WORK/SimpleScraper/CSVs/machineTrack.csv");

        ArrayList<String> results;
        ArrayList<String> dataClasses = new ArrayList<>();
        dataClasses.add("a/href");

        for (int i = 1; i<=17; i++){
            print("page: "+ String.valueOf(i));
            results = scrapper.scrapeGetTableLinks("https://www.machinetrack.nl/agricultural-machinery/harvesters.html?page_nr=" + i,".machine_list", dataClasses );
            for (String r :results){
                print("https://www.machinetrack.nl/"+r+"?languest_id=en");
                //print(r);

                ArrayList<String> dataQuery = new ArrayList<>();
                //name
                dataQuery.add(".machine_details/h2:contains(Verkoper)/text");
                //tel
                dataQuery.add(".machine_details_bg/h3:contains(Mobiel)/text");
                //address
                dataQuery.add(".machine_details_bg/h3/text");

                ArrayList<String> data = scrapper.getPageData("https://www.machinetrack.nl/"+r+"?languest_id=en", "", dataQuery);
                print("name: "+data.get(0).replaceAll("Verkoper: ",""));
                print("phone: "+data.get(1).replaceAll("Mobiel: ",""));
                print("address: "+data.get(2));
                scrapper.write("name", data.get(0).replaceAll("Verkoper: ",""));
                scrapper.write("phone", data.get(1).replaceAll("Mobiel: ",""));
                scrapper.write("address", data.get(2));
                scrapper.nextLine();
            }
        }
        scrapper.close();

    }

    public static void scrapeAfrika() {
        ScrapeUtil scrapper = new ScrapeUtil();
        scrapper.initWriter("C:/Users/jakub/OneDrive/WORK/SimpleScraper/CSVs/Afrikta.csv");

        ArrayList<String> results;
        ArrayList<String> dataClasses = new ArrayList<>();
        dataClasses.add("a/href");

        for (int i = 1; i<=4; i++){
            print("page: "+ String.valueOf(i));
            results = scrapper.scrapeGetTableLinks("https://afrikta.com/listing-categories/building-construction-companies-africa/page/" + i,".acadp-listings/.acadp-listings-title-block", dataClasses );
            for (String r :results){
                //print(r);

                ArrayList<String> dataQuery = new ArrayList<>();
                //name
                dataQuery.add("h1/text");
                //website
                dataQuery.add(".acadp-website/a/text");
                //tel
                dataQuery.add(".acadp-phone-number/a/text");

                ArrayList<String> data = scrapper.getPageData(r, "", dataQuery);
                scrapper.write("name", data.get(0));
                scrapper.write("website", data.get(1));
                scrapper.write("phone", data.get(2));
                scrapper.nextLine();
            }
        }
        scrapper.close();
    }

    //PARSES dieselenginetrader.com for all information about one specific engine page
    public static void scrape1DieselEngine(String id){
        ScrapeUtil scrapeUtil = new ScrapeUtil();
        try {
            Document document = Jsoup.connect("https://www.dieselenginetrader.com/engine_details.cfm?id="+id).get();
            //document = Jsoup.connect("https://www.dieselenginetrader.com/engines/Allis_Chalmers/670t/22985").get();

            //print(document.text());
            Elements heading = document.select("h1");
            if (heading.text().contains("does not exist")){
                print(id+ " is empty");
            } else {
                Elements dealers = document.select("tr");
                for (int i=1; i < dealers.size(); i++) {
                    //print(dealers.get(i).text());
                    Elements info = dealers.get(i).select("td.name");
                    Elements data = dealers.get(i).select("td:not(.name)");
                    if (!info.text().contains("General Info") && !info.text().contains("Location Info") && !info.text().contains("Engine Info")){
                        print(info.text() + "|" + data.text() );
                        scrapeUtil.write(info.text() , data.text().replaceAll(",", "."));
                    }
                }
                scrapeUtil.nextLine();
                print("________________");
            }

        } catch (IOException e) {
            e.printStackTrace();
            scrapeUtil.close();
        }

        print("done");
    }

    //PARSES dieselenginetrader.com EFFICIENTLY using POST() request.
    //cycling through list of all engines on the website
    //and calling the scrape1DieselEngine(), for individual engine pages
    public static void scrapeDieselEngineTrade(){
        ScrapeUtil scrapeUtil = new ScrapeUtil();
        for (Integer pageNum=min; pageNum<=max; pageNum++){
            print("parsing page: " + pageNum);
            System.out.println("parsing page: " + pageNum);
            try {
                Document document = Jsoup.connect("https://www.dieselenginetrader.com/engine_search.cfm")
                        .data("action", "engine_details.cfm")
                        .data("MakeBox", "0")
                        .data("ModelBox", "Model")
                        .data("CylinderBox", "0")
                        .data("ConditionBox", "0")
                        .data("PurposeBox", "0")
                        .data("PowerBox", "0")
                        .data("RPMBox", "0")
                        .data("RegionBox", "0")
                        .data("CountryBox", "0")
                        .data("ListingIDBox", "Listing ID")
                        .data("CustIntRefBox", "")
                        .data("SORT1", "e.Leadtime")
                        .data("Page",String.valueOf(pageNum))
                        .post();

                Elements rows = document.select("tr");
                //Element heading = rows.get(0);
                for (int rowNum=2; rowNum < rows.size(); rowNum++) {
                    Element row = rows.get(rowNum);
                    Elements cols = row.select("td");
	            	/*String make = cols.get(0).text();
	            	String condition = cols.get(1).text();
	            	String model = cols.get(2).text();
	            	String location = cols.get(3).text();
	            	String price = cols.get(4).text();
	            	String lead = cols.get(5).text();*/
                    String link = cols.get(6).select("a").attr("href");
                    //print(link);
                    String[] linkParts = link.split("'");
	            	/*for (String linkPart: linkParts) {
	            		print(linkPart);
	            	}*/
                    //print("listingID: "+linkParts[5]);
                    scrape1DieselEngine(linkParts[5]);
                }
                print("____________________________________________");
                //csvWriter.flush();

            } catch (IOException e) {
                e.printStackTrace();
                scrapeUtil.close();
            }
        }
        for(String a: columnNames) {
            System.out.println(a);
        }
    }

    public static void scrapeMachineryTrader (){
        ScrapeUtil scrapeUtil = new ScrapeUtil();
        Document document;
        try {
            document = Jsoup.connect("https://www.machinerytrader.com/dealer-directory/list?page=1").post();
            //print(document.text());
            Elements dealers = document.select(".dealer");
            //print(dealers.text());
            for (int i=0; i < dealers.size(); i++) {
                String website = dealers.get(i).select("a.businessBranchHeader").attr("href");
                String emaillink = dealers.get(i).select("div.businessEmailLink").select("a").attr("href");
                if ( emaillink.equals("") == false){
                    emaillink = "https://www.machinerytrader.com" + emaillink;
                }
                String address = dealers.get(i).select("div.businessBranchInfo").select(".columns").text();
                //Elements address = dealers.get(i).select("");
                Elements tel = dealers.get(i).select("a:contains(Phone)");
                String telephone = tel.text().split(":")[1];
                //if (info.text() != null){
                print(address + " | " + website+" | " + emaillink + " | "+ telephone );
                //}

            }

            print("________________");

        } catch (IOException e) {
            e.printStackTrace();
            scrapeUtil.close();
        }
        print("done");
    }

    public static void print(String string) {
        if (printEnable == true){
            System.out.println(string);
        }
    }
}