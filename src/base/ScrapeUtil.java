package base;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScrapeUtil {
    private boolean printEnable = true;
    private BufferedWriter csvWriter;
    private FileWriter csvWriter2;
    private HashMap<String,String> dataVals = new HashMap<String,String>();
    private ArrayList<String> columnNames = new ArrayList<String>();


    public void parseGetTableThenPage() {

    }

    public ScrapeUtil() {
        String[] dataClasses;
    }

    public String linkValidation(String link, String baseUrl){
        //TODO:
        //  check if link is a webpage
        //  if it short then add to front
        //  check if local or external

        return null;
    }

    private boolean isWebpage(String url) {
        return true;
    }

    private boolean isLocal(String url) {
        return true;
    }

    public ArrayList<String> scrapePostTableLinks(String url ,String rowClasses, ArrayList<String> data){
        ArrayList<String> result = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();

            Elements rows = selectElement(document.select("html"), rowClasses);

            for (Element row: rows) {
                for (String d : data) {
                    String[] querySplit = d.split("/");

                    if (d.equals("a/href")) {
                        result.add(row.select("a").attr("href"));
                    }
                    else if (d.equals("a/text")) {
                        result.add(row.select("a").text());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public ArrayList<String> scrapeGetTableLinks(String url ,String rowClasses, ArrayList<String> data){
        ArrayList<String> result = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();

            Elements rows = selectElement(document.select("html"), rowClasses);

            for (Element row: rows) {
                for (String d : data) {
                    String[] querySplit = d.split("/");

                    if (d.equals("a/href")) {
                        result.add(row.select("a").attr("href"));
                    }
                    else if (d.equals("a/text")) {
                        result.add(row.select("a").text());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<String> getPageData(String url , String sectionClass, ArrayList<String> dataQuery) {
        ArrayList<String> result = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            Elements section;
            if (!sectionClass.equals("")) {
                section = selectElement(document.select("html"), sectionClass);
            } else {
                section = document.select("html");
            }

            for (String Query : dataQuery) {
                String[] querySplit = Query.split("/");

                String dataResult = selectTextElement(section, Query);
                dataResult = dataResult.replaceAll(",","");
                result.add(dataResult);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<String[]> getPageLabelData(String url , String sectionClass, ArrayList<String[]> dataLabelQuery) {
        ArrayList<String[]> results = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            Elements section;
            if (!sectionClass.equals("")) {
                section = selectElement(document.select("html"), sectionClass);
            } else {
                section = document.select("html");
            }

            for (String[] dataLabel : dataLabelQuery) {
                String labelQuery = dataLabel[0];
                String dataQuery = dataLabel[1];

                String labelResult = selectTextElement(section, labelQuery);
                String dataResult = selectTextElement(section, dataQuery);
                String[] result = new String[]{labelQuery, dataQuery};
                results.add(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    private String selectTextElement(Elements section, String Query) {
        Elements data = section;
        String[] querySplit = Query.split("/");

        for (String s: querySplit) {
            s.replaceAll("/","");
            if(s.equals("href")) {
                return data.attr("href");
            }
            else if (s.equals("text")) {
                return data.text();
            }
            //class
            else {
                data = data.select(s);
            }
        }
        return "";
    }

    private Elements selectElement(Elements elements, String className) {
        String[] classSplit = className.split("/");
        Elements copy = elements;
        for (String s: classSplit) {
            s.replaceAll("/","");
            if(!s.equals("href") && !s.equals("text")) {
                copy = copy.select(s);
            }
        }
        return copy;
    }

    //initialises the csv file, and writes one empty row for the headings
    public void initWriter(String filePath){
        try {
            csvWriter2 = new FileWriter(filePath);
            csvWriter = new BufferedWriter(csvWriter2);

            //List<String> rows = Arrays.asList("Listing ID","Make","Model","Serial No.","Cylinders","V / Inline","Power","RPM","Price","Condition","Application","Cooling","Lead Time","Country","State","Contact","Phone 1","Phone 2","Cell Phone","Fax","Email","Website","Address","Country");

            //csvWriter.append(String.join(",", rows));
            csvWriter.append("\n");

        } catch (IOException e) {
            e.printStackTrace();
            close();
        }

    }

    //adds the info and data strings to hashmap, so can all be written later in correct order by nextline()
    public void write(String info, String data){
        if(!dataVals.containsKey(info)) {
            print("new heading " + info);
            columnNames.add(info);
            dataVals.put(info,data);

        } else {
            dataVals.put(info,data);
        }

		/*try {
			csvWriter.append(data);
			csvWriter.append(",");
		} catch (IOException e) {
			e.printStackTrace();
			close();
		}*/
    }

    //resets the values of all keys in the hashmap, but importantly keeps all key values, in same order as before
    public void cleanHash() {
        for (String s: dataVals.keySet()) {
            dataVals.put(s,"");
        }
    }

    //writes contense of hashmap to csv file in correct order, so is consistant throughout csv file
    public void nextLine(){
        try {
            for (String s: columnNames ) {
                String x = dataVals.get(s);
                csvWriter.append(x);
                csvWriter.append(",");
            }
            csvWriter.append("\n");
            cleanHash();
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    //closes csv file and binary writers
    public void close(){
        try {
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //prints the string given
    public void print(String string) {
        if (printEnable == true){
            System.out.println(string);
        }
    }
}