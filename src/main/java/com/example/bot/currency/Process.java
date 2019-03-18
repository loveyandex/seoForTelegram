package com.example.bot.currency;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Process{
    private static boolean OXE;

    private static Process process = new Process();
    private static ArrayList<String> namesItem = new ArrayList<>();
    private static ArrayList<String> changeItem = new ArrayList<>();
    private static ArrayList<String> priceItem = new ArrayList<>();
    private static ArrayList<String> monetaryTextItmes = new ArrayList<>();
    private static ArrayList<String> monetaryBuyItems = new ArrayList<>();
    private static ArrayList<String> monetarySellItmes = new ArrayList<>();

    public static ArrayList<String> getPriceItem() {
        return priceItem;
    }

    public static ArrayList<String> getChangeItem() {
        return changeItem;
    }

    public static ArrayList<String> getNamesItem() {
        return namesItem;
    }

    private Process() {
    }

    public static Process getProcess() {
        return process;
    }


    public static ArrayList<String> getMonetaryTextItmes() {
        return monetaryTextItmes;
    }

    public static ArrayList<String> getMonetaryBuyItems() {
        return monetaryBuyItems;
    }

    public static ArrayList<String> getMonetarySellItmes() {
        return monetarySellItmes;
    }

    public static boolean isOXE() {
        return OXE;
    }

    public void getData() {
    }

    public void getData(String urls) {
        Document document;
        try {
            Connection connection;
            if (urls.contains("www.tgju.org")) {
                namesItem.clear();
                priceItem.clear();
                changeItem.clear();
                System.out.println(urls);
                connection = Jsoup.connect(urls);
                document = connection.get();
                OXE = false;
                Elements itemsName = document.body().getElementsByClass("info-bar mobile-hide").get(0).getElementsByTag("strong");
                Elements itemsPrice = document.body().getElementsByClass("info-price");
                Elements itemsChanges = document.body().getElementsByClass("info-bar mobile-hide").get(0).getElementsByClass("info-change");
                for (int j = 0; j < itemsName.size(); j++) {
                    namesItem.add(itemsName.get(j).text());
                    priceItem.add(itemsPrice.get(j).text());
                    changeItem.add(itemsChanges.get(j).text());
                }

            } else if (urls.contains("www.o-xe.com")) {
                monetaryBuyItems.clear();
                monetarySellItmes.clear();
                monetaryTextItmes.clear();
                connection = Jsoup.connect(urls);
                document = connection.get();
                OXE = true;
                Elements monetaryText = document.body().getElementsByClass("MonetaryText");
                Elements monetaryBuy = document.getElementsByClass("MonetaryBuy");
                Elements monetarySell = document.body().getElementsByClass("MonetarySell");

                for (int i = 0; i < monetaryBuy.size(); i++) {
                    monetaryBuyItems.add(monetaryBuy.get(i).text());
                    monetaryTextItmes.add(monetaryText.get(i).text());
                    monetarySellItmes.add(monetarySell.get(i).text());
                }


            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showDataOXE() {

    }


    public void afterConnect() {
    }
}
