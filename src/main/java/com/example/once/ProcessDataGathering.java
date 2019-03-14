package com.example.once;

import com.example.database.QDB;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * is created by aMIN on 1/26/2018 at 12:04 AM
 */
public class ProcessDataGathering {
    public static List<String> hrefList = new ArrayList<>();

    public static void write(String filepath,String str)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath,true));
        writer.write(str);
        writer.close();
    }

    public static String readerline()
            throws IOException {
        String str = "World";
        BufferedReader riter = new BufferedReader(new FileReader(
                "C:\\Users\\AminAbvaal\\Desktop\\javas\\AllBots\\src\\main\\res\\urls.conf"));
        String s = riter.readLine();
        riter.close();
        return s;
    }

    public static void getData(String url) {
        Connection connection = Jsoup.connect(url);
        try {
            Document document = connection.get();
            document.body().getElementsByClass("more").forEach(element -> {
                final String href = element.attributes().get("href");
                System.err.println(href);
                Connection newConnection = Jsoup.connect(href);
                try {
                    final Element body = newConnection.get().body();
                    final Elements tboxes = body.getElementsByClass("tbox");
                    for (Element tbox : tboxes) {
                        final String href_song = tbox.getElementsByTag("a").get(0).attr("href");
                        System.err.println(href_song);
                        QDB.getInstance().insertTOhrefsTable(href_song);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }


            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        getData("http://nex1music.ir/top/");
    }



}
