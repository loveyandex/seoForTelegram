package com.example.bot.currency;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboard {
    public static boolean isAddedOXE = false;
    public static boolean isAddedtgju = false;
    public static String TGJU_MORE="tgjuMore";
    public static String TGJU_MORE_MORE="tgjuMoreMore";
    public static boolean moreIsRequested;

    public static InlineKeyboardMarkup setArzesForOXE() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        String[] items = new String[]{"دلار", "یورو", "پوند", "ین", "طلا", "فضه", "برنز"};
        List<InlineKeyboardButton> list = new ArrayList<InlineKeyboardButton>();
        for (int i = 0; i < 7; i++) {

            InlineKeyboardButton button = new InlineKeyboardButton(items[i])
                    .setCallbackData(items[i]);
            list.add(button);
        }

        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        lists.add(list);
        markup.setKeyboard(lists);
        return markup;
    }


    public static InlineKeyboardMarkup setWebsites() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        String[] items = new String[]{"tgju.org", "o-xe.com"/*, "", "", "", "", ""*/};
        String[] hostNamesItems = new String[]{"http://www.tgju.org/", "http://www.o-xe.com/"/*, "", "", "", "", ""*/};
        List<InlineKeyboardButton> list = new ArrayList<InlineKeyboardButton>();
        for (int i = 0; i < items.length; i++) {
            InlineKeyboardButton button = new InlineKeyboardButton(items[i])
                    .setCallbackData(hostNamesItems[i]);
            list.add(button);
        }

        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        lists.add(list);
        markup.setKeyboard(lists);
        return markup;
    }


    public static InlineKeyboardMarkup setArzesFortgju() {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> list = new ArrayList<>();
        List<InlineKeyboardButton> list1 = new ArrayList<>();
        List<InlineKeyboardButton> list2 = new ArrayList<>();
        List<InlineKeyboardButton> list3 = new ArrayList<>();
        if (!isAddedtgju) {


            for (int i = 0; i <3; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton(Process.getNamesItem().get(i))
                        .setCallbackData(Process.getNamesItem().get(i));
                list.add(button);
            }
            for (int i = 0; i < 3 ; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton(Process.getPriceItem().get(i))
                        .setCallbackData(Process.getPriceItem().get(i));
                list1.add(button);
            }
            for (int i = 0; i <3; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton(Process.getChangeItem().get(i))
                        .setCallbackData(Process.getChangeItem().get(i));
                list2.add(button);
            }

            for (int i = 0; i < 1; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton("بیشتر")
                        .setCallbackData(TGJU_MORE);
                list3.add(button);
            }
            isAddedtgju = true;
        }

        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        lists.add(list);
        lists.add(list1);
        lists.add(list2);
        lists.add(list3);
        markup.setKeyboard(lists);
        return markup;
    }


    public static InlineKeyboardMarkup setMoretgju() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> list = new ArrayList<>();
        List<InlineKeyboardButton> list1 = new ArrayList<>();
        List<InlineKeyboardButton> list2 = new ArrayList<>();
        List<InlineKeyboardButton> list3 = new ArrayList<>();
            for (int i = 0; i <3; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton(Process.getNamesItem().get(i+3))
                        .setCallbackData(Process.getNamesItem().get(i+3));
                list.add(button);
            }
            for (int i = 0; i < 3 ; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton(Process.getPriceItem().get(i+3))
                        .setCallbackData(Process.getPriceItem().get(i+3));
                list1.add(button);
            }
            for (int i = 0; i <3; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton(Process.getChangeItem().get(i+3))
                        .setCallbackData(Process.getChangeItem().get(i+3));
                list2.add(button);
            }

            for (int i = 0; i < 1; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton("بیشتر")
                        .setCallbackData(TGJU_MORE_MORE);
                list3.add(button);
            }
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        lists.add(list);
        lists.add(list1);
        lists.add(list2);
        lists.add(list3);
        markup.setKeyboard(lists);
        return markup;
    }


    public static InlineKeyboardMarkup setMoreMoretgju() {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> list = new ArrayList<>();
        List<InlineKeyboardButton> list1 = new ArrayList<>();
        List<InlineKeyboardButton> list2 = new ArrayList<>();
        List<InlineKeyboardButton> list3 = new ArrayList<>();
            for (int i = 0; i <3; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton(Process.getNamesItem().get(i+3+3))
                        .setCallbackData(Process.getNamesItem().get(i+3+3));
                list.add(button);
            }
            for (int i = 0; i < 3 ; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton(Process.getPriceItem().get(i+3+3))
                        .setCallbackData(Process.getPriceItem().get(i+3+3));
                list1.add(button);
            }
            for (int i = 0; i <3; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton(Process.getChangeItem().get(i+3+3))
                        .setCallbackData(Process.getChangeItem().get(i+3+3));
                list2.add(button);
            }

            for (int i = 0; i < 1; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton("اسی کیرمم نیستی")
                        .setCallbackData("befbef");
                list3.add(button);
            }
            isAddedtgju = true;

        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        lists.add(list);
        lists.add(list1);
        lists.add(list2);
        lists.add(list3);
        markup.setKeyboard(lists);
        return markup;
    }




    public static InlineKeyboardMarkup setAllAValaibleNamesArzOXE() {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();

        boolean t = true;
        int how = Process.getMonetaryTextItmes().size() - 1;

        while (t) {
            List<InlineKeyboardButton> list0 = new ArrayList<InlineKeyboardButton>();

            for (int i = how; i > how - 4 && i > 0; i--) {
                InlineKeyboardButton button = new InlineKeyboardButton(Process.getMonetaryTextItmes().get(
                        Process.getMonetaryTextItmes().size() - i))
                        .setCallbackData(Process.getMonetaryTextItmes().get(Process.getMonetaryTextItmes().size() - i));
                list0.add(button);
            }
            how -= 4;
            if (how < 0) t = false;
            lists.add(list0);
        }
        markup.setKeyboard(lists);
        return markup;
    }

    public static InlineKeyboardMarkup setdisiredOXE(String item,int index) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();

        boolean t = true;
        int how = Process.getMonetaryTextItmes().size() - 1;

        List<InlineKeyboardButton> list0 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> list1 = new ArrayList<InlineKeyboardButton>();
        for (int i = 0; i <1 ; i++) {
        InlineKeyboardButton button = new InlineKeyboardButton(Process.getMonetaryBuyItems().get(index))
                .setCallbackData(item);
        list0.add(button);
    }

        InlineKeyboardButton button = new InlineKeyboardButton(Process.getMonetarySellItmes().get(index))
                .setCallbackData(item);
        list1.add(button);

        lists.add(list0);
        lists.add(list1);

        markup.setKeyboard(lists);
        return markup;
    }


}
