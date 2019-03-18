package sd;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class S {
    public static void main(String[] args) throws IOException {
        Element body = Jsoup.connect("https://www.musixmatch.com/lyrics/Aref/Soltane-Ghalbha")
                .get().body();

        Elements lyrics__content__ok = body.getElementsByClass("lyrics__content__ok");

        Element element = lyrics__content__ok.get(0);
        Element elementa = lyrics__content__ok.get(1);

        System.out.println(element.text());
        System.out.println(elementa.text());

    }
}
