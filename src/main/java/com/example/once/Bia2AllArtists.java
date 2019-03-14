package com.example.once;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.IntStream;

import static com.example.once.Bia2.urlHost;


/**
 * is created by aMIN on 12/31/2018 at 1:07 AM
 */
public class Bia2AllArtists {
    public static void main(String[] args) throws IOException {
        final ArrayList<String> hrefs = new ArrayList<>();
        final Element body = Jsoup.connect("https://www.bia2.com/artists/").get().body();
        final Element albums = body.getElementById("content");

        final Element albms = albums.getElementsByTag("ul").get(0);

        final Elements lis = albms.getElementsByTag("li");
        int init = 0;

        for (int i = 0; i < lis.size(); i++) {
            String artistname = lis.get(i).getElementsByClass("artist-name").get(0).text();
            System.out.println(artistname);
            if (artistname.contains("Morvarid")) {
                init = i;
                break;
            }
        }


        IntStream.range(init, lis.size()).forEach(value -> {
            final Element element = lis.get(value);
            String href = urlHost + element.getElementsByTag("a").get(0).attr("href");
            String artistname = element.getElementsByClass("artist-name").get(0).text();
            System.err.println(value+" "+artistname);

            try {
                Bia2.DowlnAlbums(href);
                Bia2.DowlnSingles(href);
//                System.exit(0);
            } catch (IOException e) {
                System.err.println(e);
                try {
                    new FileWriter("config/fallurls.conf", true).write(href);
                } catch (IOException e1) {
                    System.out.println("jan jan " + href);
                }
            }
        });

//        lis.forEach(element -> {
//        });
    }
}
