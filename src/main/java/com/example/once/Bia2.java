package com.example.once;

import com.example.database.QDB;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * is created by aMIN on 12/24/2018 at 3:25 AM
 */
public class Bia2 {

    public static void main(String[] args) throws IOException {

        String baseurl = "https://www.bia2.com/artist/shadmehr-aghili";
        baseurl = "https://www.bia2.com/artist/ebi";
        DowlnSingles(baseurl);

    }

    public static void DowlnAlbums(String baseurl) throws IOException {

        final ArrayList<String> hrefAlnums = hrefsof(baseurl);

        hrefAlnums.forEach(s -> {
            System.out.println(s);
            try {
                final ArrayList<String> hrefSongs = hrefSongs(s);
                for (String hrefSong : hrefSongs) {
                    System.out.println(hrefSong);
                    ;
                    String[] split1 = hrefSong.split(";;");
                    String albumname = split1[0];
                    String hrerf = split1[1];

                    final Element body = Jsoup.connect(hrerf).get().body();
                    final String src_url = body.getElementsByClass("add_all_without_cover_ol").get(0).attr("id");
                    System.err.println(src_url);
                    final String h1 = body.getElementsByClass("intro-holder").get(0).getElementsByTag("h1").text();
                    final String[] split = h1.split(" - ");
                    QDB.getInstance().insertTOBia2musicTable(split[1], src_url, split[0], albumname, "", "");
                }


            } catch (IOException e) {
//                e.printStackTrace();
            }
        });

    }


    public static void DowlnSingles(String baseurl) throws IOException {

        final ArrayList<String> hrefsofSIngleSong = hrfsofSIngleSong(baseurl);

        hrefsofSIngleSong.forEach(hrefSong -> {
            System.out.println(hrefSong);
            try {
                final Element body = Jsoup.connect(hrefSong).get().body();
                final String src_url = body.getElementsByClass("add_all_without_cover_ol").get(0).attr("id");
                final String h1 = body.getElementsByClass("intro-holder").get(0).getElementsByTag("h1").text();
                final String[] split = h1.split(" - ");
                String artist = split[0];
                System.out.println(artist + " " + src_url);

                QDB.getInstance().insertTOBia2musicTable(split[1], src_url, artist, "", "", "");

            } catch (IOException e) {
//                e.printStackTrace();
            }
        });

    }


    private static ArrayList<String> hrefSongs(String hrefAlnums) throws IOException {
        final ArrayList<String> hrefs = new ArrayList<>();
        Element body = Jsoup.connect(hrefAlnums).get().body();
        final Element element = body.getElementsByClass("list-items").get(0);
        String info = body.getElementsByClass("info").get(0).text();
        final Elements li = element.getElementsByTag("li");

        li.forEach(d -> {
            final String attr = d.getElementsByTag("div").get(0).child(0).attr("href");
            hrefs.add(info + ";;" + urlHost + attr);
        });

        return hrefs;
    }

    public static String urlHost = "https://www.bia2.com";
    public static String[] ablumName = null;

    private static ArrayList<String> hrefsof(String baseurl) throws IOException {
        final ArrayList<String> hrefs = new ArrayList<>();
        final Element body = Jsoup.connect(baseurl).get().body();
        final Element albums = body.getElementById("albums");
        if (albums == null)
            return hrefs;

        Elements elementsByClass = albums.getElementsByClass("list-media-items");

        if (elementsByClass.size() == 0)
            return hrefs;

        Elements h2 = albums.getElementsByTag("h2");
        if (h2.size() == 0)
            return hrefs;

        String mustalbum = h2.get(0).text();

        Element albms;
        if (mustalbum.toUpperCase().contains("ALBUMS")) {
            albms = elementsByClass.get(0);
        } else
            return hrefs;
        final Elements lis = albms.getElementsByTag("li");
        lis.forEach(element -> {
            final String path = element.getElementsByTag("a").get(0).attr("href");
            hrefs.add(urlHost + path);
        });
        return hrefs;
    }


    private static ArrayList<String> hrfsofSIngleSong(String baseurl) throws IOException {
        final ArrayList<String> hrefs = new ArrayList<>();
        final Element body = Jsoup.connect(baseurl).get().body();
        final Element albums = body.getElementById("albums");
        if (albums == null)
            return hrefs;

        Elements h2 = albums.getElementsByTag("h2");
        if (h2.size() == 0)
            return hrefs;

        String mustalbum = h2.get(0).text();
        String mustsingles;
        if (h2.size() > 1) {
            mustsingles = h2.get(1).text();
        } else
            mustsingles = "";

        Elements elementsByClass = albums.getElementsByClass("list-media-items");
        if (elementsByClass.size() == 0)
            return hrefs;
        Element albms;
        if (mustalbum.toUpperCase().contains("ALBUMS") && elementsByClass.size() > 1 && mustsingles.toUpperCase().contains("SINGLE TRACKS")) {
            albms = elementsByClass.get(1);
        } else if (mustalbum.toUpperCase().contains("SINGLE TRACKS"))
            albms = elementsByClass.get(0);
        else
            return hrefs;

        final Elements lis = albms.getElementsByTag("li");
        lis.forEach(element -> {
            final String path = element.getElementsByTag("a").get(0).attr("href");
            hrefs.add(urlHost + path);
        });
        return hrefs;
    }


}