package com.example.once;


import com.example.database.QDB;
import com.google.gson.Gson;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * is created by aMIN on 12/20/2018 at 9:46 PM
 */
public class Run {
    public static void main(String[] args) throws SQLException {
//        new Scanner(System.in).nextLine();
//        final ResultSet resultSet = QDB.getInstance().selectAllFromhrefs("1=1");
//        while (resultSet.next()) {
//            final String href;
//            try {
//                href = resultSet.getString(1);
//                if (href.isEmpty())
//                    continue;
//
//                getBestofMonthes(href);
//                Thread.sleep(100);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        try {
             String baseUrl = "https://nex1music.ir/tag/macan-band/";
            baseUrl = "https://nex1music.ir/tag/hoorosh-band/";
            baseUrl = "https://nex1music.ir/tag/reza-sadeghi/";
            baseUrl = "https://nex1music.ir/tag/reza-sadeghi/page/3/";
            baseUrl = "https://nex1music.ir/tag/reza-sadeghi/page/5/";
            System.out.println(baseUrl);
            final ArrayList<String> hrefOftagAfterNameofsinger = hrefOftagAfterNameofsinger(baseUrl);
            System.out.println(new Gson().toJson(hrefOftagAfterNameofsinger));
            hrefOftagAfterNameofsinger.forEach(s ->{
                try {
                    System.err.println(s);
                    inSongPage(s);
                    Thread.sleep(200);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    static void inSongPage(String url) throws IOException {
        Connection connection = Jsoup.connect(url);
        final Element body = connection.get().body();
        final String src_url = body.getElementsByClass("lnkdl animate").get(0).getElementsByTag("a")
                .get(0).attr("href");
        final Elements elementsByTagStrongd = body.getElementsByClass("pcnt").get(0).getElementsByTag("strong");

        if (elementsByTagStrongd.size() == 3) {
            System.out.println(src_url);
            System.out.println(elementsByTagStrongd.size());
            System.out.println(elementsByTagStrongd.text());

            final String artist_persian = elementsByTagStrongd.get(0).text();
            final String name_persian = elementsByTagStrongd.get(1).text();
            final String artist = elementsByTagStrongd.get(2).child(0).text();
            final String name = elementsByTagStrongd.get(2).child(1).text();
            System.out.printf("%s _ %s _ %s _ %s\n", artist_persian, name_persian, artist, name);

            QDB.getInstance().insertTOmusic2Table(name, src_url, artist, "", name_persian, artist_persian);

            return;
        }
        if (elementsByTagStrongd.size() == 4) {
            System.out.println(src_url);
            System.out.println(elementsByTagStrongd.size());
            System.out.println(elementsByTagStrongd.text());
            final String artist_persian = elementsByTagStrongd.get(0).text();
            final String name_persian = elementsByTagStrongd.get(1).text();
            final String artist = elementsByTagStrongd.get(2).text();
            String name = elementsByTagStrongd.get(3).text();
            System.out.printf("%s _ %s _ %s _ %s\n", artist_persian, name_persian, artist, name);

            if (name.contains("بوم را به صورت اورجینال از فروشگاههای عرضه محصولات"))
                name = name_persian.substring(0, 9);
            QDB.getInstance().insertTOmusic2Table(name, src_url, artist, "", name_persian, artist_persian);

            return;
        }

    }


    static ArrayList<String> hrefOftagAfterNameofsinger(String baseUrl) throws IOException {
        ArrayList<String> hrefsOFSongs = new ArrayList<>();

        Connection connection = Jsoup.connect(baseUrl);
        final Element body = connection.get().body();
        body.getElementsByClass("ps anm").forEach(element ->{
            final Element psdown_l2_f12 = element.getElementsByClass("psdown l2 f12").get(0);
            final String href = psdown_l2_f12.child(1).attr("href");
            hrefsOFSongs.add(href);
        });
        return hrefsOFSongs;
    }
}
