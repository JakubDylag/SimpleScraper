package base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class sorter {
    private String fileName;
    private boolean printEnable = true;

    public sorter(String fileName) {
        this.fileName = fileName;
        List<String> seenNames = new ArrayList<>();
        seenNames.add("...");
        List<List<String>> records = new ArrayList<>();
        List<List<String>> smallRecord = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for( List<String> row : records){
            //print(row.get(0));
            if (seenNames.size() > 0 && row.size() > 0){
                String b = row.get(0);
                if ( seenNames.contains(b) == false && !b.equals("First Name") && !b.equals("") && b.length() > 2){
                    seenNames.add(b);
                    b = b.replaceFirst(" ",",");
                    if (!b.contains(",")) {
                        b += ",";
                    }
                    row.set(0, b);
                    smallRecord.add(row);
                    print(row.get(0));
                }
            }
        }

        FileWriter csvWriter2 = null;
        try {
            csvWriter2 = new FileWriter("C:/Users/jakub/OneDrive/X Machines/shrunkContacts.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter csvWriter = new BufferedWriter(csvWriter2);

        for (List<String> row: smallRecord) {
            if(row.get(6).length() > 0) {
                String webs = row.get(6);
                //
                webs = webs.toLowerCase();
                webs = webs.replaceAll("www.", "");
                webs = webs.replaceAll(".com", "");
                webs = webs.replaceAll(".co.uk", "");
                webs = webs.replaceAll(".pl", "");
                webs = webs.replaceAll(".ie", "");
                webs = webs.replaceAll(".nl", "");
                webs = webs.replaceAll(".dk", "");
                webs = webs.replaceAll(".fi", "");
                webs = webs.replaceAll(".se", "");
                webs = webs.replaceAll(".ws", "");
                webs = webs.replaceAll(".de", "");
                webs = webs.replaceAll(".co.za", "");
                webs = webs.replaceAll(".net", "");
                //print(webs);
                row.set(6,webs);
            }
            try {
                csvWriter.append(String.join(",",row));
                csvWriter.append("\n");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void print(String string) {
        if (printEnable == true){
            System.out.println(string);
        }
    }
}
